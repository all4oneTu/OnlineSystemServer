package com.example.demo.Vo;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
public class QuestionOptionCreateVo {
    @JsonProperty("content")
    private String questionOptionContent;

    @JsonProperty("answer")
    private Boolean answer = false;
}
