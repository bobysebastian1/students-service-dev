package com.students.students_service.repository;

import com.students.students_service.entity.SemesterResultEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface StudentResultRepository extends JpaRepository<SemesterResultEntity, Long> {
}