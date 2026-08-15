package com.students.students_service.service;

import com.students.students_service.dto.ApiResponse;
import com.students.students_service.entity.SemesterResultEntity;
import com.students.students_service.entity.StudentEntity;
import com.students.students_service.exception.ResourceNotFoundException;
import com.students.students_service.repository.StudentRepository;
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
    @Transactional
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
    public List<StudentEntity> getAllStudents() {
        return studentRepository.findAll();
    }

    @Override
    public StudentEntity getStudentById(Long id) {
        return studentRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Student not found with id: " + id));
    }

    @Override
    @Transactional
    public StudentEntity updateStudent(Long studentId, SemesterResultEntity semesterResult) {
        StudentEntity student = getStudentById(studentId);
        student.addSemesterResult(semesterResult);
        return studentRepository.save(student);
    }

    @Override
    @Transactional
    public StudentEntity updateStudentDetails(Long studentId, StudentEntity incomingData) {
        // 1. Fetch existing student or throw 404
        StudentEntity existingStudent = getStudentById(studentId);

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

        // 3. Save updated entity (keeps existing id, overallCgpa, and semesterResults intact)
        return studentRepository.saveAndFlush(existingStudent);
    }

    @Override
    @Transactional
    public ApiResponse deleteStudent(Long studentId) {
        if (!studentRepository.existsById(studentId)) {
            throw new ResourceNotFoundException("Student not found with id: " + studentId);
        }
        studentRepository.deleteById(studentId);
        return new ApiResponse("Student deleted successfully with id: " + studentId, true);
    }
}