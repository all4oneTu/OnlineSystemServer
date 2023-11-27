package com.example.demo.Repo;

import com.example.demo.Entity.Exam;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface ExamRepo extends JpaRepository<Exam, String> {
    @Query("select e from Exam e order by e.updateTime desc")
    List<Exam> findAll();
}
