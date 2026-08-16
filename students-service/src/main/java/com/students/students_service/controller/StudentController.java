package com.students.students_service.controller;

import com.students.students_service.dto.ApiResponse;
import com.students.students_service.entity.SemesterResultEntity;
import com.students.students_service.entity.StudentEntity;
import com.students.students_service.exception.ResourceNotFoundException;
import com.students.students_service.service.StudentService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

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

    // 4. Retrieve a list of students by their names
    @GetMapping("/search")
    public ResponseEntity<?> getStudentsByName(@RequestParam("name") String name) {
        // Trim to handle empty spaces like ?name=
        if (name == null || name.trim().isEmpty()) {
            throw new ResourceNotFoundException("Search name cannot be empty");
        }

        List<StudentEntity> students = studentService.getStudentByName(name.trim());
        return ResponseEntity.ok(students);
    }

    // 5. Add or update a semester result for an existing student
    @PostMapping("/{id}/semesters")
    public ResponseEntity<StudentEntity> addSemesterResult(
            @PathVariable Long id,
            @RequestBody SemesterResultEntity semesterResult) {
        StudentEntity updatedStudent = studentService.updateStudent(id, semesterResult);
        return ResponseEntity.ok(updatedStudent);
    }

    // 6. Update personal details (name, email, department) and semester results
    // combined for an existing student
    @PutMapping("/{id}/details")
    public ResponseEntity<StudentEntity> updateStudentDetails(
            @PathVariable Long id,
            @RequestBody StudentEntity studentEntity) {
        StudentEntity updatedStudent = studentService.updateStudentDetails(id, studentEntity);
        return ResponseEntity.ok(updatedStudent);
    }

    // 7. Delete a student
    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse> deleteStudent(@PathVariable Long id) {
        return ResponseEntity.ok(studentService.deleteStudent(id));
    }

    // 8. Retrieve a list of students by their names
    @GetMapping("/nameSearch")
    public ResponseEntity<?> getStudentsByNameLike(@RequestParam("name") String name) {
        // Trim to handle empty spaces like ?name=
        if (name == null || name.trim().isEmpty() || name.length() < 3) {
            throw new ResourceNotFoundException("Search name cannot be empty or less than 3 characters");
        }

        List<StudentEntity> students2 = studentService.getStudentByNameLike(name.trim());
        return ResponseEntity.ok(students2);
    }
}