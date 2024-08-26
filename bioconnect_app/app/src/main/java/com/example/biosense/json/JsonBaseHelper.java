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
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.time.Instant;
import java.util.Calendar;
import java.util.Date;
import java.util.stream.Stream;

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

    public String saveJson(Context ctx, String data) throws JsonSaveException {
//        JSONObject jsonObject = new JSONObject();
        String[] jsonReadings = data.split("%");
        String filename = "test_" + this.getCurrentDate() + ".json";
        String jsonData = "{\n\"readings\": [\n";

        for (int i = 0; i < jsonReadings.length; i++) {
            String jsonPacket = jsonReadings[i];
            // Prevent Empty string - > last one
            if (jsonPacket.length() > 0) {

                if (jsonPacket.contains("\"V\":") && jsonPacket.contains("\"I\":")) {
                    jsonData += jsonPacket + ",\n";
                }
            }
        }
        jsonData = jsonData.substring(0, jsonData.length() - 2);
        jsonData += "\n]\n}";

//        try {
//            jsonObject.put("Data", data);
//        } catch (JSONException e) {
//            throw new JsonSaveException("Error putting data into Json");
//        }
        File file = new File(ctx.getFilesDir(), filename);
        FileWriter fileWriter = null;
        try {
            fileWriter = new FileWriter(file);
            BufferedWriter bufferedWriter = new BufferedWriter(fileWriter);
            bufferedWriter.write(jsonData);
            bufferedWriter.close();
            Log.d(TAG, "saveJson: " + ctx.getFilesDir() + file);
            return filename;
        } catch (IOException e) {
            throw new JsonSaveException("Error to write Json File");
        }

    }

    public String ReadJson(Context ctx, String filename) throws JsonSaveException {

        StringBuilder builder = new StringBuilder();
        File file = new File(ctx.getFilesDir(), filename);

        if (file.exists()) {
            int linesSum = 0;

            try {
                FileReader reader = new FileReader(file);
                BufferedReader buffRead = new BufferedReader(reader);
                String line  = buffRead.readLine();

                while (line != null) {
                    linesSum += 1;
                    builder.append(line);
                    builder.append("\n");
                    line = buffRead.readLine();
                }
                Log.d(TAG, String.valueOf(linesSum));
                // delete the last new line separator
                builder.deleteCharAt(builder.length() - 1);
                reader.close();
                return builder.toString();

            }catch (IOException e) {
                throw new JsonSaveException("IO error");
            }

        }else {
            throw new JsonSaveException("File does not exist");
        }
    }
//    public void saveJson(String data) throws JsonSaveException {
//        JSONObject jsonObject = new JSONObject();
//        try {
//            jsonObject.put("Data", data);
//        } catch (JSONException e) {
//            throw new JsonSaveException("Error putting data into Json");
//        }
//        File file = new File(this.filepath, this.filename + ".json");
//        FileWriter fileWriter = null;
//        try {
//            fileWriter = new FileWriter(file);
//            BufferedWriter bufferedWriter = new BufferedWriter(fileWriter);
//            bufferedWriter.write(jsonObject.toString());
//            bufferedWriter.close();
//        } catch (IOException e) {
//            throw new JsonSaveException("Error to write Json File");
//        }
//    }

    public String getCurrentDate() {
        Calendar cal = Calendar.getInstance();
        DateFormat df = new SimpleDateFormat("yyyy_MM_dd_HH_mm_ss");
        return df.format(cal.getTime()).toString();
    }
}
