package com.example.demo.Repo;

import com.example.demo.Entity.Action;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ActionRepo extends JpaRepository<Action, Integer> {
}
