package com.example.demo.Enum;


import lombok.Getter;

@Getter
public enum RoleEnum {
    ADMIN(1, "Admin"),
    TEACHER(2, "Giáo viên"),
    STUDENT(3, "Học sinh");


    RoleEnum(Integer id, String role) {
        this.id = id;
        this.role = role;
    }

    private Integer id;
    private String role;
}
