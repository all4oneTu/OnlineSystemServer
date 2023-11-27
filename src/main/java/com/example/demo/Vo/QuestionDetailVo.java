package com.example.demo.Vo;

import com.example.demo.Entity.QuestionOption;
import lombok.Data;

import java.util.ArrayList;
import java.util.List;

@Data
public class QuestionDetailVo {
    private String id;


    private String name;


    private String description;

    private String type;

    private List<QuestionOption> options;

    private List<String> answers = new ArrayList<>();
}
