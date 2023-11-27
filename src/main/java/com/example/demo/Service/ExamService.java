package com.example.demo.Service;

import com.example.demo.Entity.Exam;
import com.example.demo.Entity.ExamRecord;
import com.example.demo.Vo.*;

import java.util.HashMap;
import java.util.List;

public interface ExamService {
    List<QuestionVo> getQuestionAll();
//
    QuestionVo updateQuestion(QuestionVo questionVo);
//
    void questionCreate(QuestionCreateVo questionCreateVo);
//
    QuestionSelectionVo getSelections();
//
    QuestionDetailVo getQuestionDetail(String id);
//
    List<ExamVo> getExamAll();
//
//
    ExamQuestionTypeVo getExamQuestionType();
//
    Exam create(ExamCreateVo examCreateVo, String userId);

    List<ExamCardVo> getExamCardList();
    List<ExamRecordVo> getExamRecordList(String userId);

    ExamDetailVo getExamDetail(String id);
//
//
    ExamRecord judge(String userId, String examId, HashMap<String, List<String>> answersMap);
//
//

//
//
    RecordDetailVo getRecordDetail(String recordId);
//
//    Exam update(ExamVo examVo, String userId);




}
