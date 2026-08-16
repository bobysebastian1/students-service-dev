package com.students.students_service.entity;

import jakarta.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "students")
public class StudentEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;
    private String email;
    private String department;

    private Double overallCgpa;

    @OneToMany(mappedBy = "student", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<SemesterResultEntity> semesterResults = new ArrayList<>();

    // Default Constructor
    public StudentEntity() {
    }

    // Parameterized Constructor
    public StudentEntity(Long id, String name, String email, String department, Double overallCgpa) {
        this.id = id;
        this.name = name;
        this.email = email;
        this.department = department;
        this.overallCgpa = overallCgpa;
    }

    // Helper method to add a semester result and auto-calculate overall CGPA
    public void addSemesterResult(SemesterResultEntity result) {
        if (result != null) {
            semesterResults.add(result);
            result.setStudent(this); // Crucial for database foreign key
            recalculateOverallCgpa();
        }
    }

    // Helper method to recalculate overall CGPA average across semesters
    public void recalculateOverallCgpa() {
        if (semesterResults == null || semesterResults.isEmpty()) {
            this.overallCgpa = 0.0;
            return;
        }

        double sum = 0.0;
        int validCount = 0;

        for (SemesterResultEntity result : semesterResults) {
            // Safe check to avoid NullPointerException if sgpa was omitted or sent as null
            if (result != null && result.getSgpa() != null) {
                sum += result.getSgpa();
                validCount++;
            }
        }

        if (validCount == 0) {
            this.overallCgpa = 0.0;
            return;
        }

        this.overallCgpa = Math.round((sum / validCount) * 100.0) / 100.0;
    }

    // Getters and Setters
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getDepartment() {
        return department;
    }

    public void setDepartment(String department) {
        this.department = department;
    }

    public Double getOverallCgpa() {
        return overallCgpa;
    }

    public void setOverallCgpa(Double overallCgpa) {
        this.overallCgpa = overallCgpa;
    }

    public List<SemesterResultEntity> getSemesterResults() {
        return semesterResults;
    }

    public void setSemesterResults(List<SemesterResultEntity> semesterResults) {
        this.semesterResults.clear();
        if (semesterResults != null) {
            for (SemesterResultEntity result : semesterResults) {
                addSemesterResult(result);
            }
        }
    }
}