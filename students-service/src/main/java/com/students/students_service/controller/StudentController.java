package com.students.students_service.controller;

import com.students.students_service.dto.ApiResponse;
import com.students.students_service.entity.SemesterResultEntity;
import com.students.students_service.entity.StudentEntity;
import com.students.students_service.service.StudentService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;
import java.util.HashMap;

@RestController
@RequestMapping("/api/students")
public class StudentController {

    private final StudentService studentService;

    public StudentController(StudentService studentService) {
        this.studentService = studentService;
    }

    // 1. Create a new student (with or without initial semester results)
    @PostMapping
    public ResponseEntity<StudentEntity> createStudent(@RequestBody StudentEntity student) {
        StudentEntity savedStudent = studentService.createStudent(student);
        return new ResponseEntity<>(savedStudent, HttpStatus.CREATED);
    }

    // 2. Retrieve all students
    @GetMapping
    public ResponseEntity<List<StudentEntity>> getAllStudents() {
        List<StudentEntity> students = studentService.getAllStudents();
        return ResponseEntity.ok(students);
    }

    // 3. Retrieve single student by ID
    @GetMapping("/{id}")
    public ResponseEntity<StudentEntity> getStudentById(@PathVariable Long id) {
        StudentEntity student = studentService.getStudentById(id);
        return ResponseEntity.ok(student);
    }

    // 4. Add or update a semester result for an existing student
    @PostMapping("/{id}/semesters")
    public ResponseEntity<StudentEntity> addSemesterResult(
            @PathVariable Long id,
            @RequestBody SemesterResultEntity semesterResult) {
        StudentEntity updatedStudent = studentService.updateStudent(id, semesterResult);
        return ResponseEntity.ok(updatedStudent);
    }

    // 5. Update personal details (name, email, department) of an existing student
    @PutMapping("/{id}/details")
    public ResponseEntity<StudentEntity> updateStudentDetails(
            @PathVariable Long id,
            @RequestBody StudentEntity studentEntity) {
        StudentEntity updatedStudent = studentService.updateStudentDetails(id, studentEntity);
        return ResponseEntity.ok(updatedStudent);
    }
    
    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse> deleteStudent(@PathVariable Long id) {
        return ResponseEntity.ok(studentService.deleteStudent(id));
    }
}