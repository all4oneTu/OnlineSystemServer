package com.example.demo.Vo;

import com.example.demo.Entity.QuestionCategory;
import com.example.demo.Entity.QuestionLevel;
import com.example.demo.Entity.QuestionType;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import java.util.List;

@Data
public class QuestionSelectionVo {
    @JsonProperty("types")
    private List<QuestionType> questionTypeList;

    @JsonProperty("categories")
    private List<QuestionCategory> questionCategoryList;

    @JsonProperty("levels")
    private List<QuestionLevel> questionLevelList;
}
