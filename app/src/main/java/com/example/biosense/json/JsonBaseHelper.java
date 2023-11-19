package com.example.biosense.json;

// This class contains the methods to create a JSON file and save it as a csv or a JSON

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;

public class JsonBaseHelper {
    private File file;
    private FileReader reader;
    private FileWriter writer;
    private BufferedReader bufferReader;
    private BufferedWriter bufferWriter;

    private String filename;
    private String filepath;

    private String jsonString;

    public JsonBaseHelper(String filepath, String filename) {
        this.filename = filename;
        this.filepath = filepath;
    }

    public JsonBaseHelper(){

    }

    public void addJsonLine(String json) {
        this.jsonString += json;
    }


}
