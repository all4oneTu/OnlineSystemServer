package com.example.demo.Entity;

import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import lombok.Data;

import java.util.Date;

@Data
@Entity
public class ExamRecord {
    @Id
    @Column(length =  100)
    private String examRecordId;

    private String examId;

    private String answerOptionIds;

    private String examJoinerId;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date examJoinDate;

    private Integer examTimeCost;

    private Integer examJoinScore;

    private Integer examResultLevel;
}
