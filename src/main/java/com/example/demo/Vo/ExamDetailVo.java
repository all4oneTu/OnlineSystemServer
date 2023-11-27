package com.example.demo.Vo;

import com.example.demo.Entity.Exam;
import lombok.Data;

@Data
public class ExamDetailVo{

        private Exam exam;

        private String[] radioIds;

        private String[] checkIds;

        private String[] judgeIds;
}
