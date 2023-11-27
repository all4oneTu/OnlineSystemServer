package com.example.demo.Vo;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import java.util.List;

@Data
public class QuestionCreateVo {
    @JsonProperty("name")
    private String questionName;

    @JsonProperty("desc")
    private String questionDescription;

    @JsonProperty("score")
    private Integer questionScore = 5;

    @JsonProperty("creator")
    private String questionCreatorId;


    @JsonProperty("level")
    private Integer questionLevelId;


    @JsonProperty("type")
    private Integer questionTypeId;


    @JsonProperty("category")
    private Integer questionCategoryId;


    @JsonProperty("options")
    private List<QuestionOptionCreateVo> questionOptionCreateVoList;
}
