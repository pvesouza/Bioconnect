package com.example.biosense.db;

import java.util.ArrayList;
import java.util.List;

public class DdExamsList {

    private List<Exam> examList;

    public DdExamsList() {
        this.examList = new ArrayList<>();
        Exam e = new Exam();
        e.setExamId("1D9572014");
        e.setId(1);
        e.setFileName("test_20231223.json");
        e.setResult(1);
        e.setTechnique("D-Hepatitis");

        Exam d = new Exam();
        d.setExamId("1D9572015");
        d.setId(2);
        d.setFileName("test_20231323.json");
        d.setResult(2);
        d.setTechnique("D-Hepatitis");

        this.examList.add(e);
        this.examList.add(d);

    }

    public List<Exam> getExams() {
        return this.examList;
    }
}
