package com.example.demo.Vo;

import com.example.demo.Entity.ExamRecord;
import lombok.Data;

import java.util.HashMap;
import java.util.List;

@Data
public class RecordDetailVo {
    private ExamRecord examRecord;

    private HashMap<String, List<String>> answersMap;

    private HashMap<String, String> resultsMap;

    private HashMap<String, List<String>> answersRightMap;
}
