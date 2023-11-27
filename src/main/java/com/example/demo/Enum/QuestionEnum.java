package com.example.demo.Enum;


import lombok.Getter;

@Getter
public enum QuestionEnum {
    RADIO(1, "Câu hỏi 1 lựa chọn"),
    CHECK(2, "Câu hỏi nhiều lựa chọn"),
    JUDGE(3, "Câu hỏi đúng sai");


    QuestionEnum(Integer id, String role) {
        this.id = id;
        this.role = role;
    }

    private Integer id;
    private String role;
}
