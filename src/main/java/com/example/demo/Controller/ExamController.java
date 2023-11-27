package com.example.demo.Controller;


import com.example.demo.Entity.Exam;
import com.example.demo.Entity.ExamRecord;
import com.example.demo.Service.ExamService;
import com.example.demo.Vo.*;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;

@RestController
@RequestMapping("/api/exam")
public class ExamController {
    @Autowired
    private ExamService examService;

    @GetMapping("/card/list")
    ResultVo<List<ExamCardVo>> getExamCardList() {
        ResultVo<List<ExamCardVo>> resultVO;
        try {
            List<ExamCardVo> examCardVoList = examService.getExamCardList();
            resultVO = new ResultVo<>(0, "\n" +
                    "Nhận danh sách thi thành công", examCardVoList);
        } catch (Exception e) {
            e.printStackTrace();
            resultVO = new ResultVo<>(-1, "\n" +
                    "Không lấy được danh sách thi", null);
        }
        return resultVO;
    }

    @GetMapping("/record/list")
    ResultVo<List<ExamRecordVo>> getExamRecordList(HttpServletRequest request) {
        ResultVo<List<ExamRecordVo>> resultVO;
        try {
            String userId = (String) request.getAttribute("user_id");
            List<ExamRecordVo> examRecordVoList = examService.getExamRecordList(userId);
            resultVO = new ResultVo<>(0, "Nhận hồ sơ thi thành công", examRecordVoList);
        } catch (Exception e) {
            e.printStackTrace();
                resultVO = new ResultVo<>(-1, "Không lấy được hồ sơ thi", null);
        }
        return resultVO;
    }
    @GetMapping("/detail/{id}")
    ResultVo<ExamDetailVo> getExamDetail(@PathVariable String id) {
        ResultVo<ExamDetailVo> resultVO;
        try {
            ExamDetailVo examDetail = examService.getExamDetail(id);
            resultVO = new ResultVo<>(0, "Nhận thông tin chi tiết bài kiểm tra thành công", examDetail);
        } catch (Exception e) {
            resultVO = new ResultVo<>(-1, "\n" +
                    "Không thể lấy được chi tiết bài kiểm tra", null);
        }
        return resultVO;
    }

    @GetMapping("/question/detail/{id}")
    ResultVo<QuestionDetailVo> getQuestionDetail(@PathVariable String id) {
        System.out.println(id);
        ResultVo<QuestionDetailVo> resultVO;
        try {
            QuestionDetailVo questionDetailVo = examService.getQuestionDetail(id);
            resultVO = new ResultVo<>(0, "Đã nhận được chi tiết câu hỏi thành công", questionDetailVo);
        } catch (Exception e) {
            e.printStackTrace();
            resultVO = new ResultVo<>(-1, "Không thể lấy được chi tiết câu hỏi", null);
        }
        return resultVO;
    }

    @PostMapping("/finish/{examId}")
    ResultVo<ExamRecord> finishExam(@PathVariable String examId, @RequestBody HashMap<String, List<String>> answersMap, HttpServletRequest request) {
        ResultVo<ExamRecord> resultVO;
        try {
            String userId = (String) request.getAttribute("user_id");
            ExamRecord examRecord = examService.judge(userId, examId, answersMap);
            resultVO = new ResultVo<>(0, "Đã gửi bài thi thành công", examRecord);
        } catch (Exception e) {
            e.printStackTrace();
            resultVO = new ResultVo<>(-1, "\n" +
                    "Nộp bài kiểm tra không thành công", null);
        }
        return resultVO;
    }
    @GetMapping("/question/all")
    ResultVo<List<QuestionVo>> getQuestionAll() {
        ResultVo<List<QuestionVo>> resultVO;
        try {
            List<QuestionVo> questionAll = examService.getQuestionAll();
            resultVO = new ResultVo<>(0, "Có được danh sách tất cả các câu hỏi thành công", questionAll);
        } catch (Exception e) {
            e.printStackTrace();
            resultVO = new ResultVo<>(-1, "\n" +
                    "Không thể lấy được tất cả danh sách câu hỏi", null);
        }
        return resultVO;
    }
    @GetMapping("/all")
    ResultVo<List<ExamVo>> getExamAll() {

        ResultVo<List<ExamVo>> resultVO;
        try {
            List<ExamVo> examVos = examService.getExamAll();
            resultVO = new ResultVo<>(0, "Lấy danh sách tất cả các bài kiểm tra thành công", examVos);
        } catch (Exception e) {
            e.printStackTrace();
            resultVO = new ResultVo<>(-1, "Không thể lấy được danh sách tất cả các bài kiểm tra", null);
        }
        return resultVO;
    }
    @PostMapping("/question/update")
    ResultVo<QuestionVo> questionUpdate(@RequestBody QuestionVo questionVo) {
        System.out.println(questionVo);
        try {
            QuestionVo questionVoResult = examService.updateQuestion(questionVo);
            return new ResultVo<>(0, "\n" +
                    "Cập nhật câu hỏi thành công", questionVoResult);
        } catch (Exception e) {
            e.printStackTrace();
            return new ResultVo<>(-1, "Cập nhật câu hỏi không thành công", null);
        }
    }
    @PostMapping("/question/create")
    ResultVo<String> questionCreate(@RequestBody QuestionCreateSimplifyVo questionCreateSimplifyVo, HttpServletRequest request) {
        QuestionCreateVo questionCreateVo = new QuestionCreateVo();
        BeanUtils.copyProperties(questionCreateSimplifyVo, questionCreateVo);
        String userId = (String) request.getAttribute("user_id");
        questionCreateVo.setQuestionCreatorId(userId);
        System.out.println(questionCreateVo);
        try {
            examService.questionCreate(questionCreateVo);
            return new ResultVo<>(0, "Câu hỏi được tạo thành công", null);
        } catch (Exception e) {
            e.printStackTrace();
            return new ResultVo<>(-1, "Câu hỏi tạo thất bại", null);
        }
    }
    @GetMapping("/question/selection")
    ResultVo<QuestionSelectionVo> getSelections() {
        QuestionSelectionVo questionSelectionVo = examService.getSelections();
        if (questionSelectionVo != null) {
            return new ResultVo<>(0, "Lấy được các tùy chọn câu hỏi thành công", questionSelectionVo);
        } else {
            return new ResultVo<>(-1, "Không thể nhận được các tùy chọn phân loại câu hỏi", null);
        }
    }
    @GetMapping("/question/type/list")
    ResultVo<ExamQuestionTypeVo> getExamQuestionTypeList() {
        ResultVo<ExamQuestionTypeVo> resultVO;
        try {
            ExamQuestionTypeVo examQuestionTypeVo = examService.getExamQuestionType();
            resultVO = new ResultVo<>(0, "\n" +
                    "Lấy danh sách câu hỏi thành công", examQuestionTypeVo);
        } catch (Exception e) {
            e.printStackTrace();
            resultVO = new ResultVo<>(-1, "Không lấy được danh sách câu hỏi", null);
        }
        return resultVO;
    }
    @PostMapping("/create")
    ResultVo<Exam> createExam(@RequestBody ExamCreateVo examCreateVo, HttpServletRequest request) {
        ResultVo<Exam> resultVO;
        String userId = (String) request.getAttribute("user_id");
        try {
            Exam exam = examService.create(examCreateVo, userId);
            resultVO = new ResultVo<>(0, "Tạo bài kiểm tra thành công", exam);
        } catch (Exception e) {
            e.printStackTrace();
            resultVO = new ResultVo<>(-1, "Tạo bài kiểm tra thất bại", null);
        }
        return resultVO;
    }
    @GetMapping("/record/detail/{recordId}")
    ResultVo<RecordDetailVo> getExamRecordDetail(@PathVariable String recordId) {
        ResultVo<RecordDetailVo> resultVO;
        try {
            RecordDetailVo recordDetailVo = examService.getRecordDetail(recordId);
            resultVO = new ResultVo<>(0, "Lấy chi tiết bài kiểm tra thành công", recordDetailVo);
        } catch (Exception e) {
            e.printStackTrace();
            resultVO = new ResultVo<>(-1, "Lấy chi tiết bài kiểm tra thất bại", null);
        }
        return resultVO;
    }
}
