package com.example.demo.Service.impl;

import cn.hutool.core.util.IdUtil;
import cn.hutool.core.util.StrUtil;
import com.example.demo.Entity.*;
import com.example.demo.Enum.QuestionEnum;
import com.example.demo.Repo.*;
import com.example.demo.Service.ExamService;
import com.example.demo.Vo.*;
import jakarta.transaction.Transactional;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
@Transactional
public class ExamImpl implements ExamService {

    private final ExamRepo examRepository;
    private final ExamRecordRepo examRecordRepository;
    private final UserRepo userRepository;
    private final QuestionRepo questionRepository;
    private final QuestionTypeRepo questionTypeRepository;
    private final QuestionOptionRepo questionOptionRepository;
    private final QuestionLevelRepository questionLevelRepository;
    private final QuestionCategoryRepo questionCategoryRepository;
    public ExamImpl(ExamRepo examRepository, ExamRecordRepo examRecordRepository, UserRepo userRepository, QuestionRepo questionRepository, QuestionTypeRepo questionTypeRepository, QuestionOptionRepo questionOptionRepository, QuestionLevelRepository questionLevelRepository, QuestionCategoryRepo questionCategoryRepository) {
        this.examRepository = examRepository;
        this.examRecordRepository = examRecordRepository;
        this.userRepository = userRepository;
        this.questionRepository = questionRepository;
        this.questionTypeRepository = questionTypeRepository;
        this.questionOptionRepository = questionOptionRepository;
        this.questionLevelRepository = questionLevelRepository;
        this.questionCategoryRepository = questionCategoryRepository;
    }
    public static String trimMiddleLine(String str) {
        if (str.charAt(str.length() - 1) == '-') {
            str = str.substring(0, str.length() - 1);
        }
        return str;
    }

    @Override
    public List<QuestionVo> getQuestionAll() {
        List<Question> questionList = questionRepository.findAll();
        return getQuestionVos(questionList);
    }

    @Override
    public QuestionVo updateQuestion(QuestionVo questionVo) {
        StringBuilder questionAnswerOptionIds = new StringBuilder();
        List<QuestionOption> questionOptionList = new ArrayList<>();
        List<QuestionOptionVo> questionOptionVoList = questionVo.getQuestionOptionVoList();
        int size = questionOptionVoList.size();
        for (int i = 0; i < questionOptionVoList.size(); i++) {
            QuestionOptionVo questionOptionVo = questionOptionVoList.get(i);
            QuestionOption questionOption = new QuestionOption();
            BeanUtils.copyProperties(questionOptionVo, questionOption);
            questionOptionList.add(questionOption);
            if (questionOptionVo.getAnswer()) {
                if (i != size - 1) {

                    questionAnswerOptionIds.append(questionOptionVo.getQuestionOptionId()).append("-");
                } else {

                    questionAnswerOptionIds.append(questionOptionVo.getQuestionOptionId());
                }
            }
        }
        Question question = questionRepository.findById(questionVo.getQuestionId()).orElse(null);
        assert question != null;
        BeanUtils.copyProperties(questionVo, question);
        question.setQuestionAnswerOptionIds(questionAnswerOptionIds.toString());
        questionRepository.save(question);
        questionOptionRepository.saveAll(questionOptionList);
        return getQuestionVo(question);
    }

    @Override
    public void questionCreate(QuestionCreateVo questionCreateVo) {
        Question question = new Question();
        BeanUtils.copyProperties(questionCreateVo, question);
        List<QuestionOption> questionOptionList = new ArrayList<>();
        List<QuestionOptionCreateVo> questionOptionCreateVoList = questionCreateVo.getQuestionOptionCreateVoList();
        for (QuestionOptionCreateVo questionOptionCreateVo : questionOptionCreateVoList) {
            QuestionOption questionOption = new QuestionOption();
            questionOption.setQuestionOptionContent(questionOptionCreateVo.getQuestionOptionContent());
            questionOption.setQuestionOptionId(IdUtil.simpleUUID());
            questionOptionList.add(questionOption);
        }
        questionOptionRepository.saveAll(questionOptionList);
        String questionOptionIds = "";
        String questionAnswerOptionIds = "";

        for (int i = 0; i < questionOptionCreateVoList.size(); i++) {
            QuestionOptionCreateVo questionOptionCreateVo = questionOptionCreateVoList.get(i);
            QuestionOption questionOption = questionOptionList.get(i);
            questionOptionIds += questionOption.getQuestionOptionId() + "-";
            if (questionOptionCreateVo.getAnswer()) {
                questionAnswerOptionIds += questionOption.getQuestionOptionId() + "-";
            }
        }

        questionAnswerOptionIds = replaceLastSeparator(questionAnswerOptionIds);
        questionOptionIds = replaceLastSeparator(questionOptionIds);
        question.setQuestionOptionIds(questionOptionIds);
        question.setQuestionAnswerOptionIds(questionAnswerOptionIds);
        question.setQuestionId(IdUtil.simpleUUID());
        question.setCreateTime(new Date());
        question.setUpdateTime(new Date());
        questionRepository.save(question);
    }

    @Override
    public QuestionSelectionVo getSelections() {
        QuestionSelectionVo questionSelectionVo = new QuestionSelectionVo();
        questionSelectionVo.setQuestionCategoryList(questionCategoryRepository.findAll());
        questionSelectionVo.setQuestionLevelList(questionLevelRepository.findAll());
        questionSelectionVo.setQuestionTypeList(questionTypeRepository.findAll());
        return questionSelectionVo;
    }

    private List<QuestionVo> getQuestionVos(List<Question> questionList) {
        List<QuestionVo> questionVoList = new ArrayList<>();
        for (Question question : questionList) {
            QuestionVo questionVo = getQuestionVo(question);
            questionVoList.add(questionVo);
        }
        return questionVoList;
    }
    private QuestionVo getQuestionVo(Question question) {
        QuestionVo questionVo = new QuestionVo();
        BeanUtils.copyProperties(question, questionVo);
        questionVo.setQuestionCreator(
                Objects.requireNonNull(
                        userRepository.findById(
                                question.getQuestionCreatorId()
                        ).orElse(null)
                ).getUserUsername());
        questionVo.setQuestionLevel(
                Objects.requireNonNull(
                        questionLevelRepository.findById(
                                question.getQuestionLevelId()
                        ).orElse(null)
                ).getQuestionLevelDescription());
        questionVo.setQuestionType(
                Objects.requireNonNull(
                        questionTypeRepository.findById(
                                question.getQuestionTypeId()
                        ).orElse(null)
                ).getQuestionTypeDescription());
        questionVo.setQuestionCategory(
                Objects.requireNonNull(
                        questionCategoryRepository.findById(
                                question.getQuestionCategoryId()
                        ).orElse(null)
                ).getQuestionCategoryName()
        );
        List<QuestionOptionVo> optionVoList = new ArrayList<>();

        List<QuestionOption> optionList = questionOptionRepository.findAllById(
                Arrays.asList(question.getQuestionOptionIds().split("-"))
        );

        List<QuestionOption> answerList = questionOptionRepository.findAllById(
                Arrays.asList(question.getQuestionAnswerOptionIds().split("-"))
        );
        for (QuestionOption option : optionList) {
            QuestionOptionVo optionVo = new QuestionOptionVo();
            BeanUtils.copyProperties(option, optionVo);
            for (QuestionOption answer : answerList) {
                if (option.getQuestionOptionId().equals(answer.getQuestionOptionId())) {
                    optionVo.setAnswer(true);
                }
            }
            optionVoList.add(optionVo);
        }
        questionVo.setQuestionOptionVoList(optionVoList);
        return questionVo;
    }

    @Override
    public QuestionDetailVo getQuestionDetail(String id) {
        Question question = questionRepository.findById(id).orElse(null);
        QuestionDetailVo questionDetailVo = new QuestionDetailVo();
        questionDetailVo.setId(id);
        questionDetailVo.setName(question.getQuestionName());
        questionDetailVo.setDescription(question.getQuestionDescription());

        questionDetailVo.setType(
                Objects.requireNonNull(
                        questionTypeRepository.findById(
                                question.getQuestionTypeId()
                        ).orElse(null)
                ).getQuestionTypeDescription()
        );

        String optionIdsStr = trimMiddleLine(question.getQuestionOptionIds());
        String[] optionIds = optionIdsStr.split("-");

        List<QuestionOption> optionList = questionOptionRepository.findAllById(Arrays.asList(optionIds));
        questionDetailVo.setOptions(optionList);
        return questionDetailVo;
    }

    @Override
    public List<ExamVo> getExamAll() {
        List<Exam> examList = examRepository.findAll();
        return getExamVos(examList);
    }

    @Override
    public ExamQuestionTypeVo getExamQuestionType() {
        ExamQuestionTypeVo examQuestionTypeVo = new ExamQuestionTypeVo();
        List<ExamQuestionSelectVo> radioQuestionVoList = new ArrayList<>();
        List<Question> radioQuestionList = questionRepository.findByQuestionTypeId(QuestionEnum.RADIO.getId());
        for (Question question : radioQuestionList) {
            ExamQuestionSelectVo radioQuestionVo = new ExamQuestionSelectVo();
            BeanUtils.copyProperties(question, radioQuestionVo);
            radioQuestionVoList.add(radioQuestionVo);
        }
        examQuestionTypeVo.setExamQuestionSelectVoRadioList(radioQuestionVoList);

        List<ExamQuestionSelectVo> checkQuestionVoList = new ArrayList<>();
        List<Question> checkQuestionList = questionRepository.findByQuestionTypeId(QuestionEnum.CHECK.getId());
        for (Question question : checkQuestionList) {
            ExamQuestionSelectVo checkQuestionVo = new ExamQuestionSelectVo();
            BeanUtils.copyProperties(question, checkQuestionVo);
            checkQuestionVoList.add(checkQuestionVo);
        }
        examQuestionTypeVo.setExamQuestionSelectVoCheckList(checkQuestionVoList);

        List<ExamQuestionSelectVo> judgeQuestionVoList = new ArrayList<>();
        List<Question> judgeQuestionList = questionRepository.findByQuestionTypeId(QuestionEnum.JUDGE.getId());
        for (Question question : judgeQuestionList) {
            ExamQuestionSelectVo judgeQuestionVo = new ExamQuestionSelectVo();
            BeanUtils.copyProperties(question, judgeQuestionVo);
            judgeQuestionVoList.add(judgeQuestionVo);
        }
        examQuestionTypeVo.setExamQuestionSelectVoJudgeList(judgeQuestionVoList);
        return examQuestionTypeVo;
    }

    @Override
    public Exam create(ExamCreateVo examCreateVo, String userId) {
        Exam exam = new Exam();
        BeanUtils.copyProperties(examCreateVo, exam);
        exam.setExamId(IdUtil.simpleUUID());
        exam.setExamCreatorId(userId);
        exam.setCreateTime(new Date());
        exam.setUpdateTime(new Date());

        exam.setExamStartDate(new Date());
        exam.setExamEndDate(new Date());
        String radioIdsStr = "";
        String checkIdsStr = "";
        String judgeIdsStr = "";
        List<ExamQuestionSelectVo> radios = examCreateVo.getRadios();
        List<ExamQuestionSelectVo> checks = examCreateVo.getChecks();
        List<ExamQuestionSelectVo> judges = examCreateVo.getJudges();
        int radioCnt = 0, checkCnt = 0, judgeCnt = 0;
        for (ExamQuestionSelectVo radio : radios) {
            if (radio.getChecked()) {
                radioIdsStr += radio.getQuestionId() + "-";
                radioCnt++;
            }
        }
        radioIdsStr = replaceLastSeparator(radioIdsStr);
        for (ExamQuestionSelectVo check : checks) {
            if (check.getChecked()) {
                checkIdsStr += check.getQuestionId() + "-";
                checkCnt++;
            }
        }
        checkIdsStr = replaceLastSeparator(checkIdsStr);
        for (ExamQuestionSelectVo judge : judges) {
            if (judge.getChecked()) {
                judgeIdsStr += judge.getQuestionId() + "-";
                judgeCnt++;
            }
        }
        judgeIdsStr = replaceLastSeparator(judgeIdsStr);
        exam.setExamQuestionIds(radioIdsStr + "-" + checkIdsStr + "-" + judgeIdsStr);

        exam.setExamQuestionIdsRadio(radioIdsStr);
        exam.setExamQuestionIdsCheck(checkIdsStr);
        exam.setExamQuestionIdsJudge(judgeIdsStr);

        int examScore = radioCnt * exam.getExamScoreRadio() + checkCnt * exam.getExamScoreCheck() + judgeCnt * exam.getExamScoreJudge();
        exam.setExamScore(examScore);
        examRepository.save(exam);
        return exam;
    }

    private List<ExamVo> getExamVos(List<Exam> examList) {
        List<ExamVo> examVoList = new ArrayList<>();
        for (Exam exam : examList) {
            ExamVo examVo = new ExamVo();
            BeanUtils.copyProperties(exam, examVo);
            examVo.setExamCreator(
                    Objects.requireNonNull(
                            userRepository.findById(
                                    exam.getExamCreatorId()
                            ).orElse(null)
                    ).getUserUsername()
            );


            List<ExamQuestionSelectVo> radioQuestionVoList = new ArrayList<>();
            List<Question> radioQuestionList = questionRepository.findAllById(
                    Arrays.asList(exam.getExamQuestionIdsRadio().split("-"))
            );
            for (Question question : radioQuestionList) {
                ExamQuestionSelectVo radioQuestionVo = new ExamQuestionSelectVo();
                BeanUtils.copyProperties(question, radioQuestionVo);
                radioQuestionVo.setChecked(true);
                radioQuestionVoList.add(radioQuestionVo);
            }
            examVo.setExamQuestionSelectVoRadioList(radioQuestionVoList);


            List<ExamQuestionSelectVo> checkQuestionVoList = new ArrayList<>();
            List<Question> checkQuestionList = questionRepository.findAllById(
                    Arrays.asList(exam.getExamQuestionIdsCheck().split("-"))
            );
            for (Question question : checkQuestionList) {
                ExamQuestionSelectVo checkQuestionVo = new ExamQuestionSelectVo();
                BeanUtils.copyProperties(question, checkQuestionVo);
                checkQuestionVo.setChecked(true);
                checkQuestionVoList.add(checkQuestionVo);
            }
            examVo.setExamQuestionSelectVoCheckList(checkQuestionVoList);


            List<ExamQuestionSelectVo> judgeQuestionVoList = new ArrayList<>();
            List<Question> judgeQuestionList = questionRepository.findAllById(
                    Arrays.asList(exam.getExamQuestionIdsJudge().split("-"))
            );
            for (Question question : judgeQuestionList) {
                ExamQuestionSelectVo judgeQuestionVo = new ExamQuestionSelectVo();
                BeanUtils.copyProperties(question, judgeQuestionVo);
                judgeQuestionVo.setChecked(true);
                judgeQuestionVoList.add(judgeQuestionVo);
            }
            examVo.setExamQuestionSelectVoJudgeList(judgeQuestionVoList);

            examVoList.add(examVo);
        }
        return examVoList;
    }


    @Override
    public List<ExamCardVo> getExamCardList() {
        List<Exam> examList = examRepository.findAll();
        List<ExamCardVo> examCardVoList = new ArrayList<>();
        for (Exam exam : examList) {
            ExamCardVo examCardVo = new ExamCardVo();
            BeanUtils.copyProperties(exam, examCardVo);
            examCardVoList.add(examCardVo);
        }
        return examCardVoList;
    }

    @Override
    public List<ExamRecordVo> getExamRecordList(String userId) {
        List<ExamRecord> examRecordList = examRecordRepository.findByExamJoinerIdOrderByExamJoinDateDesc(userId);
        List<ExamRecordVo> examRecordVoList = new ArrayList<>();
        for (ExamRecord examRecord : examRecordList) {
            ExamRecordVo examRecordVo = new ExamRecordVo();
            Exam exam = examRepository.findById(examRecord.getExamId()).orElse(null);
            examRecordVo.setExam(exam);
            User user = userRepository.findById(userId).orElse(null);
            examRecordVo.setUser(user);
            examRecordVo.setExamRecord(examRecord);
            examRecordVoList.add(examRecordVo);
        }
        return examRecordVoList;

    }

    @Override
    public ExamDetailVo getExamDetail(String id) {
        Exam exam = examRepository.findById(id).orElse(null);
        ExamDetailVo examDetailVo = new ExamDetailVo();
        examDetailVo.setExam(exam);
        assert exam != null;
        examDetailVo.setRadioIds(exam.getExamQuestionIdsRadio().split("-"));
        examDetailVo.setCheckIds(exam.getExamQuestionIdsCheck().split("-"));
        examDetailVo.setJudgeIds(exam.getExamQuestionIdsJudge().split("-"));
        return examDetailVo;
    }

    @Override
    public ExamRecord judge(String userId, String examId, HashMap<String, List<String>> answersMap) {
        ExamDetailVo examDetailVo = getExamDetail(examId);
        Exam exam = examDetailVo.getExam();
        List<String> questionIds = new ArrayList<>();

        List<String> radioIdList = Arrays.asList(examDetailVo.getRadioIds());
        List<String> checkIdList = Arrays.asList(examDetailVo.getCheckIds());
        List<String> judgeIdList = Arrays.asList(examDetailVo.getJudgeIds());
        questionIds.addAll(radioIdList);
        questionIds.addAll(checkIdList);
        questionIds.addAll(judgeIdList);

        int radioScore = exam.getExamScoreRadio();
        int checkScore = exam.getExamScoreCheck();
        int judgeScore = exam.getExamScoreJudge();

        List<Question> questionList = questionRepository.findAllById(questionIds);
        Map<String, Question> questionMap = new HashMap<>();
        for (Question question : questionList) {
            questionMap.put(question.getQuestionId(), question);
        }

        Set<String> questionIdsAnswer = answersMap.keySet();

        Map<String, Integer> judgeMap = new HashMap<>();

        StringBuilder answerOptionIdsSb = new StringBuilder();

        int totalScore = 0;
        for (String questionId : questionIdsAnswer) {
            Question question = questionMap.get(questionId);
            String questionAnswerOptionIds = replaceLastSeparator(question.getQuestionAnswerOptionIds());
            List<String> questionAnswerOptionIdList = Arrays.asList(questionAnswerOptionIds.split("-"));
            Collections.sort(questionAnswerOptionIdList);
            String answerStr = listConcat(questionAnswerOptionIdList);

            List<String> questionUserOptionIdList = answersMap.get(questionId);
            Collections.sort(questionUserOptionIdList);
            String userStr = listConcat(questionUserOptionIdList);

            if (answerStr.equals(userStr)) {

                int score = 0;
                if (radioIdList.contains(questionId)) {
                    score = radioScore;
                }
                if (checkIdList.contains(questionId)) {
                    score = checkScore;
                }
                if (judgeIdList.contains(questionId)) {
                    score = judgeScore;
                }

                totalScore += score;

                answerOptionIdsSb.append(questionId + "@True_" + userStr + "$");
                judgeMap.put(questionId, score);
            } else {

                answerOptionIdsSb.append(questionId + "@False_" + userStr + "$");
                judgeMap.put(questionId, 0);
            }
        }

        ExamRecord examRecord = new ExamRecord();
        examRecord.setExamRecordId(IdUtil.simpleUUID());
        examRecord.setExamId(examId);

        examRecord.setAnswerOptionIds(replaceLastSeparator(answerOptionIdsSb.toString()));
        examRecord.setExamJoinerId(userId);
        examRecord.setExamJoinDate(new Date());
        examRecord.setExamJoinScore(totalScore);
        examRecordRepository.save(examRecord);
        return examRecord;
    }

    @Override
    public RecordDetailVo getRecordDetail(String recordId) {
        ExamRecord record = examRecordRepository.findById(recordId).orElse(null);
        RecordDetailVo recordDetailVo = new RecordDetailVo();
        recordDetailVo.setExamRecord(record);
        HashMap<String, List<String>> answersMap = new HashMap<>();
        HashMap<String, String> resultsMap = new HashMap<>();
        assert record != null;
        String answersStr = record.getAnswerOptionIds();
        String[] questionArr = answersStr.split("[$]");
        for (String questionStr : questionArr) {
            System.out.println(questionStr);
            String[] questionTitleResultAndOption = questionStr.split("_");
            String[] questionTitleAndResult = questionTitleResultAndOption[0].split("@");
            String[] questionOptions = questionTitleResultAndOption[1].split("-");
            answersMap.put(questionTitleAndResult[0], Arrays.asList(questionOptions));
            resultsMap.put(questionTitleAndResult[0], questionTitleAndResult[1]);
        }
        recordDetailVo.setAnswersMap(answersMap);
        recordDetailVo.setResultsMap(resultsMap);

        ExamDetailVo examDetailVo = getExamDetail(record.getExamId());
        List<String> questionIdList = new ArrayList<>();
        questionIdList.addAll(Arrays.asList(examDetailVo.getRadioIds()));
        questionIdList.addAll(Arrays.asList(examDetailVo.getCheckIds()));
        questionIdList.addAll(Arrays.asList(examDetailVo.getJudgeIds()));

        List<Question> questionList = questionRepository.findAllById(questionIdList);
        HashMap<String, List<String>> answersRightMap = new HashMap<>();
        for (Question question : questionList) {
            String questionAnswerOptionIdsStr = replaceLastSeparator(question.getQuestionAnswerOptionIds());
            String[] questionAnswerOptionIds = questionAnswerOptionIdsStr.split("-");
            answersRightMap.put(question.getQuestionId(), Arrays.asList(questionAnswerOptionIds));
        }
        recordDetailVo.setAnswersRightMap(answersRightMap);
        return recordDetailVo;
    }


    private String replaceLastSeparator(String str) {
        String lastChar = str.substring(str.length() - 1);
        if ("-".equals(lastChar) || "_".equals(lastChar) || "$".equals(lastChar)) {
            str = StrUtil.sub(str, 0, str.length() - 1);
        }
        return str;
    }
    private String listConcat(List<String> strList) {
        StringBuilder sb = new StringBuilder();
        for (String str : strList) {
            sb.append(str);
            sb.append("-");
        }
        return replaceLastSeparator(sb.toString());
    }
}
