package com.example.demo.Enum;


import lombok.Getter;

@Getter
public enum LoginTypeEnum {
    USERNAME(1, "Tên tài khoản"),
    EMAIL(2, "Email");

    LoginTypeEnum(Integer type, String name) {
        this.type = type;
        this.name = name;
    }

    private Integer type;
    private String name;
}
