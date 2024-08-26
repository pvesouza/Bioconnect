package com.example.biosense.json;

public class JsonSaveException extends Exception{

    private String jsonMessage;
    public JsonSaveException(String message) {
        this.jsonMessage = message;
    }

    public String getMessage(){
        return this.jsonMessage;
    }
}
