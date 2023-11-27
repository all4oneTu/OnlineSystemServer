package com.example.demo.Repo;

import com.example.demo.Entity.QuestionCategory;
import org.springframework.data.jpa.repository.JpaRepository;

public interface QuestionCategoryRepo extends JpaRepository<QuestionCategory, Integer> {
}
