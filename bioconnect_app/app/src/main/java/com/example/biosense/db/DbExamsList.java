package com.example.biosense.db;

import android.content.ContentValues;
import android.database.sqlite.SQLiteDatabase;

import java.util.ArrayList;
import java.util.List;

public class DbExamsList {

    private List<Exam> examList;

    private static DbExamsList instance;
    private DBHelper myDb;

    private DbExamsList() {
        this.examList = new ArrayList<>();

        Exam e = new Exam();
        e.setExamId("1D9572014");
        e.setId(1);
        e.setFileName("test_2023_12_20_16_21_58.json");
        e.setResult(1);
        e.setTechnique("D-Hepatitis");

        Exam d = new Exam();
        d.setExamId("1D9572015");
        d.setId(2);
        d.setFileName("test_2023_12_20_20_31_46.json");
        d.setResult(2);
        d.setTechnique("D-Hepatitis");

        this.examList.add(e);
        this.examList.add(d);

    }

    public static DbExamsList getInstance() {
        if (instance == null) {
            instance = new DbExamsList();
        }
        return instance;
    }

    public List<Exam> getExams() {
        return this.examList;
    }
}
