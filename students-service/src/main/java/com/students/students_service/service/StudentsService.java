package com.students.students_service.service;

import com.students.students_service.dto.ApiResponse;
import com.students.students_service.entity.SemesterResultEntity;
import com.students.students_service.entity.StudentEntity;
import java.util.List;

public interface StudentsService {
    StudentEntity createStudent(StudentEntity student);
    List<StudentEntity> getAllStudents();
    StudentEntity getStudentById(Long id);
    StudentEntity updateStudent(Long studentId, SemesterResultEntity semesterResult);
    StudentEntity updateStudentDetails(Long studentId, StudentEntity student);
    ApiResponse deleteStudent(Long studentId);
}