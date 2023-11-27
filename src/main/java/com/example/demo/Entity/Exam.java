package com.example.demo.Entity;

import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import lombok.Data;
import org.hibernate.annotations.DynamicUpdate;

import java.util.Date;

@Entity
@Data
@DynamicUpdate
public class Exam {
    @Id
    @Column(length = 100)
    private String examId;
    private String examName;
    private String examAvatar;
    private String examDescription;
    private String examQuestionIds;
    private String examQuestionIdsRadio;
    private String examQuestionIdsCheck;
    private String examQuestionIdsJudge;
    private Integer examScore;
    private Integer examScoreRadio;
    private Integer examScoreCheck;
    private Integer examScoreJudge;
    private String examCreatorId;
    private Integer examTimeLimit;
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date examStartDate;
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date examEndDate;
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date createTime;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date updateTime;
}
