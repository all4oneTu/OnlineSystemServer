package com.example.demo.Entity;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import lombok.Data;

@Data
@Entity
public class QuestionType {
    @Id
    @GeneratedValue
    @JsonProperty("id")
    private Integer questionTypeId;

    @JsonProperty("name")
    private String questionTypeName;

    @JsonProperty("description")
    private String questionTypeDescription;
}
