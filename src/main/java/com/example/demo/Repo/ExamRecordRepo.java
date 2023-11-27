package com.example.demo.Repo;

import com.example.demo.Entity.ExamRecord;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ExamRecordRepo extends JpaRepository<ExamRecord, String> {
    List<ExamRecord> findByExamJoinerIdOrderByExamJoinDateDesc(String userId);
}
