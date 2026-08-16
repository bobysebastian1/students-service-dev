package com.students.students_service.service;

import com.students.students_service.dto.ApiResponse;
import com.students.students_service.entity.SemesterResultEntity;
import com.students.students_service.entity.StudentEntity;
import com.students.students_service.exception.ResourceNotFoundException;
import com.students.students_service.repository.StudentRepository;

import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class StudentService implements StudentsService {

    private final StudentRepository studentRepository;

    public StudentService(StudentRepository studentRepository) {
        this.studentRepository = studentRepository;
    }

    @Override
    @Transactional(timeout = 10)
    @CacheEvict(value = { "students", "student_by_id", "student_by_name" }, allEntries = true)
    public StudentEntity createStudent(StudentEntity student) {
        if (student.getSemesterResults() != null) {
            for (SemesterResultEntity result : student.getSemesterResults()) {
                result.setStudent(student);
            }
            student.recalculateOverallCgpa();
        }
        return studentRepository.save(student);
    }

    @Override
    @Cacheable(value = "students")
    public List<StudentEntity> getAllStudents() {
        // THIS LINE ONLY PRINTS ON CACHE MISS (DATABASE CALL)
        System.out.println(">>> FETCHING FROM DATABASE - 1 ");
        return studentRepository.findAll();
    }

    @Override
    @Cacheable(value = "students_id", key = "#id")
    public StudentEntity getStudentById(Long id) {
        // THIS LINE ONLY PRINTS ON CACHE MISS (DATABASE CALL)
        System.out.println(">>> FETCHING FROM DATABASE FOR ID - 1: " + id);
        return studentRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Student not found with id: " + id));
    }

    @Override
    @Cacheable(value = "student_by_name", key = "#name.toLowerCase()")
    public List<StudentEntity> getStudentByName(String name) {
        List<StudentEntity> students = studentRepository.findByNameIgnoreCase(name);
        if (students.isEmpty()) {
            throw new ResourceNotFoundException("Student not found with the name: " + name);
        }
        return students;
    }

    @Override
    @Transactional(timeout = 10)
    @CacheEvict(value = { "students", "student_by_id", "student_by_name" }, allEntries = true)
    public StudentEntity updateStudent(Long studentId, SemesterResultEntity semesterResult) {
        StudentEntity student = getStudentById(studentId);
        student.addSemesterResult(semesterResult);
        return studentRepository.save(student);
    }

    @Override
    @Transactional(timeout = 10)
    @CacheEvict(value = { "students", "student_by_id", "student_by_name" }, allEntries = true)
    public StudentEntity updateStudentDetails(Long studentId, StudentEntity incomingData) {
        // 1. Fetch existing student directly from repository (throws
        // ResourceNotFoundException if 404)
        StudentEntity existingStudent = studentRepository.findById(studentId)
                .orElseThrow(() -> new ResourceNotFoundException("Student not found with id: " + studentId));

        // 2. Update only student profile fields
        existingStudent.setName(incomingData.getName());
        existingStudent.setEmail(incomingData.getEmail());
        existingStudent.setDepartment(incomingData.getDepartment());
        existingStudent.getSemesterResults().clear();

        if (incomingData.getSemesterResults() != null) {
            for (SemesterResultEntity semester : incomingData.getSemesterResults()) {
                semester.setStudent(existingStudent);
                existingStudent.getSemesterResults().add(semester);
            }
            existingStudent.recalculateOverallCgpa();
        }

        // 3. Save updated entity (keeps existing id, overallCgpa, and semesterResults
        // intact)
        return studentRepository.saveAndFlush(existingStudent);
    }

    @Override
    @Transactional(timeout = 10)
    @CacheEvict(value = { "students", "student_by_id", "student_by_name" }, allEntries = true)
    public ApiResponse deleteStudent(Long studentId) {
        if (!studentRepository.existsById(studentId)) {
            throw new ResourceNotFoundException("Student not found with id: " + studentId);
        }
        studentRepository.deleteById(studentId);
        return new ApiResponse("Student deleted successfully with id: " + studentId, true);
    }

    @Override
    @Cacheable(value = "student_by_name_starts", key = "#name.toLowerCase().trim()")
    public List<StudentEntity> getStudentByNameLike(String name) {
        List<StudentEntity> studentEntities = studentRepository.findByNameLikeIgnoreCase(name);
        if (studentEntities.isEmpty()) {
            throw new ResourceNotFoundException("Student not found with the name Starting like : " + name);
        }
        return studentEntities;
    }
}