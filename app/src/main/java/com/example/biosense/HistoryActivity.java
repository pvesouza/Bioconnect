package com.example.biosense;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;

import com.example.biosense.db.DdExamsList;
import com.example.biosense.db.ExamAdapter;
import com.example.biosense.utils.MensagensToast;

public class HistoryActivity extends AppCompatActivity {

    private RecyclerView recycler;
    private LinearLayoutManager layoutManager;

    private ExamAdapter examAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_history);

        Toolbar myToolbar = (Toolbar) findViewById(R.id.toolbar_history_exams);
        myToolbar.setTitle("Bioconnect");
        setSupportActionBar(myToolbar);

        // Population the recyvlerView

        this.recycler = findViewById(R.id.recycler_history_exams);
        this.layoutManager = new LinearLayoutManager(this);
        this.examAdapter = new ExamAdapter(new DdExamsList().getExams());
        this.examAdapter.setMyClickListener(new SearchListener());

        this.recycler.setLayoutManager(this.layoutManager);
        this.recycler.setAdapter(this.examAdapter);
    }

    private class SearchListener implements ExamAdapter.MyOnclickAnalise {

        @Override
        public void onClick(String fileName) {
            MensagensToast.showMessage(getApplicationContext(), fileName);
        }
    }
}