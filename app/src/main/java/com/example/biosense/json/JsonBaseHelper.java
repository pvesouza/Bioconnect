package com.example.biosense.json;

// This class contains the methods to create a JSON file and save it as a csv or a JSON

import android.content.Context;
import android.util.Log;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.time.Instant;
import java.util.Date;

public class JsonBaseHelper {
    private File file;
    private FileReader reader;
    private FileWriter writer;
    private BufferedReader bufferReader;
    private BufferedWriter bufferWriter;

    private String filename;
    private String filepath;

    private static final String TAG = "JsonBaseHelper";

    public JsonBaseHelper(String filepath, String filename) {
        this.filename = filename;
        this.filepath = filepath;
    }

    public JsonBaseHelper(){

    }

    public void saveJson(Context ctx, String data) throws JsonSaveException {
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("Data", data);
        } catch (JSONException e) {
            throw new JsonSaveException("Error putting data into Json");
        }
        File file = new File(ctx.getFilesDir(), Date.from(Instant.now()) + ".json");
        FileWriter fileWriter = null;
        try {
            fileWriter = new FileWriter(file);
            BufferedWriter bufferedWriter = new BufferedWriter(fileWriter);
            bufferedWriter.write(jsonObject.toString());
            bufferedWriter.close();
            Log.d(TAG, "saveJson: " + ctx.getFilesDir() + Date.from(Instant.now()) + ".json");
        } catch (IOException e) {
            throw new JsonSaveException("Error to write Json File");
        }

    }
    public void saveJson(String data) throws JsonSaveException {
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("Data", data);
        } catch (JSONException e) {
            throw new JsonSaveException("Error putting data into Json");
        }
        File file = new File(this.filepath, this.filename + ".json");
        FileWriter fileWriter = null;
        try {
            fileWriter = new FileWriter(file);
            BufferedWriter bufferedWriter = new BufferedWriter(fileWriter);
            bufferedWriter.write(jsonObject.toString());
            bufferedWriter.close();
        } catch (IOException e) {
            throw new JsonSaveException("Error to write Json File");
        }
    }
}
