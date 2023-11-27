package com.example.demo.Vo;

import com.example.demo.Entity.Exam;
import com.example.demo.Entity.ExamRecord;
import com.example.demo.Entity.User;
import lombok.Data;

@Data
public class ExamRecordVo {
    private Exam exam;

    private ExamRecord examRecord;

    private User user;
}
