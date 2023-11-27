package com.example.demo.Entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import lombok.Data;

@Data
@Entity
public class QuestionOption {
    @Id
    @Column(length = 100)
    private String questionOptionId;
    private String questionOptionContent;
    private String questionOptionDescription;
}
