package com.example.biosense.db;

import android.content.Context;
import android.database.Cursor;
import android.util.Log;

import java.util.ArrayList;
import java.util.List;

public class DbFacade {
    private static final String TAG = "DBFACADE";
    private Context context;
    private DBHelper myHelper;
    
    public DbFacade(Context _context) {
        this.context = _context;
        this.myHelper = new DBHelper(_context);
    }

    public void insertExams(List<Exam> exams) {
        for (int i = 0; i < exams.size(); i++) {
            Exam e = exams.get(i);
            this.insertExam(e);
        }
    }

    public void insertExam(Exam e) {
        long i = this.myHelper.insertExamResult(e);
        Log.d(TAG, String.valueOf(i));
    }
    
    public List<Exam> getExams(String technique) {
        List<Exam> list = new ArrayList<>();
        
        Cursor c = this.myHelper.getResultsByTechnique(technique);
            
        // Runs all instances
        while (c.moveToFirst() && c.moveToNext()) {
            Exam e = getExam(c);
            if (e != null) {
                Log.d(TAG, "add exam");
                list.add(e);
            }else {
                Log.d(TAG, "Error to add exam");
            }

        }
        return list;
    }

    private static Exam getExam(Cursor c) {

        int techIndex = c.getColumnIndex(DBHelper.ExamEntry.COLUMN_NAME_TECHNIQUE);
        int resultIndex = c.getColumnIndex(DBHelper.ExamEntry.COLUMN_NAME_RESULT);
        int filenameIndex = c.getColumnIndex(DBHelper.ExamEntry.COLUMN_NAME_FILENAME);
        int examIdIndex = c.getColumnIndex(DBHelper.ExamEntry.COLUMN_NAME_EXAM_CODE);
        int idIndex = c.getColumnIndex(DBHelper.ExamEntry.COLUMN_NAME_ID);

        Log.d(TAG, "Indexes" + "-" + techIndex + "-" + resultIndex + "-" + filenameIndex + "-" + examIdIndex + "-" + idIndex);

        if (techIndex != -1 && resultIndex != -1 && filenameIndex != -1 && examIdIndex != -1 && idIndex != -1) {

            String _technique = c.getString(techIndex);
            String _filename = c.getString(filenameIndex);
            String _examCode = c.getString(examIdIndex);
            long _id = c.getLong(idIndex);
            int _result = c.getInt(resultIndex);

            Exam e = new Exam();
            e.setFileName(_filename);
            e.setExamId(_examCode);
            e.setTechnique(_technique);
            e.setId(_id);
            e.setResult(_result);

            return e;
        }
        return null;
    }

    public List<Exam> getExams(int result) {
        List<Exam> list = new ArrayList<>();

        Cursor c = this.myHelper.getResultsByStatus(result);

        if (c != null && c.moveToFirst()) {
            do {
                Exam e = getExam(c);
                if (e != null) {
                    Log.d(TAG, "add exam");
                    list.add(e);
                }else {
                    Log.d(TAG, "Error to add exam");
                }
            }while(c.moveToNext());
        }
        return list;
    }
    
    public List<Exam> getExams(String technique, int result) {

        List<Exam> list = new ArrayList<>();
        Cursor c = this.myHelper.getResultsByStatusAndTechnique(result, technique);

        if (c != null && c.moveToFirst()) {
            do {
                Exam e = getExam(c);
                if (e != null) {
                    Log.d(TAG, "add exam");
                    list.add(e);
                }else {
                    Log.d(TAG, "Error to add exam");
                }
            }while(c.moveToNext());
        }

        return list;
    }

    public List<Exam> getExams() {

        List<Exam> list = new ArrayList<>();
        Cursor c = this.myHelper.getAllEntries();

        if (c != null && c.moveToFirst()) {
            do {
                Exam e = getExam(c);
                if (e != null) {
                    Log.d(TAG, "add exam");
                    list.add(e);
                }else {
                    Log.d(TAG, "Error to add exam");
                }
            }while(c.moveToNext());
        }

        return list;
    }

    public int updateExam(long id, int status) {
        return this.myHelper.updateResult(id, status);
    }

}
