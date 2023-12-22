package com.example.biosense.db;

public class Exam {

    private int id;
    private String technique;
    private String examId;
    private int result;
    private String fileName;


    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getTechnique() {
        return technique;
    }

    public void setTechnique(String technique) {
        this.technique = technique;
    }

    public String getExamId() {
        return examId;
    }

    public void setExamId(String examId) {
        this.examId = examId;
    }

    public int getResult() {
        return result;
    }

    public void setResult(int result) {
        this.result = result;
    }

    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    @Override
    public String toString() {
        return String.format("Filename: %s\n" +
                "Result: %s\n" +
                "Technique %s\n", this.fileName, this.getResult(this.result), this.technique);
    }

    public String getResult(int result) {
        switch (result) {
            case 0:
                return "Neg";
            case 1:
                return "Pos";
            case 2:
                return "SR";
            default:
                return "";
        }
    }
}
