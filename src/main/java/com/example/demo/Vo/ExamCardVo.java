package com.example.demo.Vo;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;


@Data
public class ExamCardVo {
    @JsonProperty("id")
    private String examId;
    @JsonProperty("title")
    private String examName;
    @JsonProperty("avatar")
    private String examAvatar;
    @JsonProperty("content")
    private String examDescription;
    @JsonProperty("score")
    private Integer examScore;

    @JsonProperty("elapse")
    private Integer examTimeLimit;
}
