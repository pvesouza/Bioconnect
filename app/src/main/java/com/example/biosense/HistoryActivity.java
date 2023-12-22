package com.example.biosense;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.os.Bundle;

public class HistoryActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_history);

        Toolbar myToolbar = (Toolbar) findViewById(R.id.toolbar_history_exams);
        myToolbar.setTitle("Bioconnect");
        setSupportActionBar(myToolbar);
    }
}