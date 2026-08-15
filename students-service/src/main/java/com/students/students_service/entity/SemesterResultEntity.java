package com.students.students_service.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;

@Entity
@Table(name = "semester_results")
public class SemesterResultEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Integer semesterNumber;
    private Double sgpa;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "student_id")
    @JsonIgnore // Prevents infinite recursion loop during JSON serialization
    private StudentEntity student;

    // Constructors
    public SemesterResultEntity() {
    }

    public SemesterResultEntity(Integer semesterNumber, Double sgpa, StudentEntity student) {
        this.semesterNumber = semesterNumber;
        this.sgpa = sgpa;
        this.student = student;
    }

    // Getters and Setters
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Integer getSemesterNumber() {
        return semesterNumber;
    }

    public void setSemesterNumber(Integer semesterNumber) {
        this.semesterNumber = semesterNumber;
    }

    public Double getSgpa() {
        return sgpa;
    }

    public void setSgpa(Double sgpa) {
        this.sgpa = sgpa;
    }

    public StudentEntity getStudent() {
        return student;
    }

    public void setStudent(StudentEntity student) {
        this.student = student;
    }
}