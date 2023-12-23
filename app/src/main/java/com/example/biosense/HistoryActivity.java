package com.example.biosense;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;

import com.example.biosense.db.DBHelper;
import com.example.biosense.db.DdExamsList;
import com.example.biosense.db.ExamAdapter;
import com.example.biosense.utils.MensagensToast;

import java.util.ArrayList;
import java.util.Arrays;

public class HistoryActivity extends AppCompatActivity {

    private RecyclerView recycler;
    private LinearLayoutManager layoutManager;

    private Button searchButton;

    private ExamAdapter examAdapter;

    private Spinner spinnerTechnique, spinnerResult;

    private static String[] RESULTS = {
      "Neg", "Pos", "SR"
    };

    private static String[] TECHS = {
            "Rhodamine",
            "D-Hepatitis"
    };

    private DBHelper myDbHelper;

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

        this.spinnerResult = findViewById(R.id.spinner_history_result);
        this.spinnerTechnique = findViewById(R.id.spinner_history_technique);

        ArrayAdapter<String> techAdapter = new ArrayAdapter<>(getApplicationContext(), android.R.layout.simple_spinner_dropdown_item, new ArrayList<String>(Arrays.asList(TECHS)));
        this.spinnerTechnique.setAdapter(techAdapter);

        ArrayAdapter<String> techResult = new ArrayAdapter<>(getApplicationContext(), android.R.layout.simple_spinner_dropdown_item, new ArrayList<String>(Arrays.asList(RESULTS)));
        this.spinnerResult.setAdapter(techResult);

        this.searchButton = findViewById(R.id.button_history_search);
        this.searchButton.setOnClickListener(new SearchOnDatabase());

        // Database init and create
        this.myDbHelper = new DBHelper(getApplicationContext());

    }

    private class SearchListener implements ExamAdapter.MyOnclickAnalise {

        @Override
        public void onClick(String fileName) {
            MensagensToast.showMessage(getApplicationContext(), fileName);
        }
    }

    private class SearchOnDatabase implements View.OnClickListener {

        @Override
        public void onClick(View v) {

        }
    }
}