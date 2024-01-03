package com.example.biosense;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;

import com.example.biosense.api.NetworkApiAccess;
import com.example.biosense.api.NetworkException;
import com.example.biosense.db.DBHelper;
import com.example.biosense.db.DbExamsList;
import com.example.biosense.db.DbFacade;
import com.example.biosense.db.Exam;
import com.example.biosense.db.ExamAdapter;
import com.example.biosense.json.JsonBaseHelper;
import com.example.biosense.json.JsonSaveException;
import com.example.biosense.utils.MensagensToast;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class HistoryActivity extends AppCompatActivity {

    private static final String TAG = "History";
    private RecyclerView recycler;
    private LinearLayoutManager layoutManager;

    private Button searchButton;

    private long examToUpdateId;                                        // Exam to update in case od success

    private ExamAdapter examAdapter;

    private Spinner spinnerTechnique, spinnerResult;

    private static String[] RESULTS = {
      "Neg", "Pos", "SR"
    };

    private static String[] TECHS = {
            "Rhodamine",
            "D-Hepatitis"
    };

    private DbFacade myDbFacade;

    private Handler connectionHandler;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_history);

        Toolbar myToolbar = (Toolbar) findViewById(R.id.toolbar_history_exams);
        myToolbar.setTitle("Bioconnect");
        setSupportActionBar(myToolbar);
        // Database init and create
        this.myDbFacade = new DbFacade(getApplicationContext());

        // Results and technique
        this.spinnerResult = findViewById(R.id.spinner_history_result);
        this.spinnerTechnique = findViewById(R.id.spinner_history_technique);

        ArrayAdapter<String> techAdapter = new ArrayAdapter<>(getApplicationContext(), android.R.layout.simple_spinner_dropdown_item, new ArrayList<String>(Arrays.asList(TECHS)));
        this.spinnerTechnique.setAdapter(techAdapter);

        ArrayAdapter<String> techResult = new ArrayAdapter<>(getApplicationContext(), android.R.layout.simple_spinner_dropdown_item, new ArrayList<String>(Arrays.asList(RESULTS)));
        this.spinnerResult.setAdapter(techResult);

        // Population the recyvlerview

        this.recycler = findViewById(R.id.recycler_history_exams);
        this.layoutManager = new LinearLayoutManager(this);
        this.recycler.setLayoutManager(this.layoutManager);
//        this.recycler.setAdapter(this.examAdapter);

//        List<Exam> myExams = this.myDbFacade.getExams();
//
//        if (myExams.size() > 0) {
//            this.examAdapter = new ExamAdapter(myExams);
//            this.examAdapter.setMyClickListener(new SearchListener());
//            // Exams historic
//            this.recycler.setLayoutManager(this.layoutManager);
//            this.recycler.setAdapter(this.examAdapter);
//        }else {
//            MensagensToast.showMessage(getApplicationContext(), "List empty");
//        }

        this.searchButton = findViewById(R.id.button_history_search);
        this.searchButton.setOnClickListener(new SearchOnDatabase());

        this.connectionHandler = new Handler(m->{
            Bundle b = m.getData();
            if (b != null) {
                String result = b.getString("RESULT");
                if (!result.isEmpty()) {

                    // Update the result in the database
                    int _result = 2;
                    if (result.equals("Positive")) {
                        _result = 1;
                    }else if (result.equals("Negative")) {
                        _result = 0;
                    }else{
                        MensagensToast.showMessage(getApplicationContext(), result);
                        return false;
                    }
                    // Update result
                    int r = myDbFacade.updateExam(examToUpdateId, _result);

                    if (r == 0) {
                        MensagensToast.showMessage(getApplicationContext(), "Update not done");
                    }else {
                        updateExamsListView();
                        MensagensToast.showMessage(getApplicationContext(), "Update done");
                    }

                    MensagensToast.showMessage(getApplicationContext(), result);

                    Log.d(TAG, result);
                    Log.d(TAG, String.valueOf(examToUpdateId));
                }else {
                    MensagensToast.showMessage(getApplicationContext(), "Fail obtaining response");
                }
            }
            return false;
        });
    }

    private class SearchListener implements ExamAdapter.MyOnclickAnalise {

        @Override
        public void onClick(String fileName, String technique, long id) {

            JsonBaseHelper jsonHelper = new JsonBaseHelper();
            examToUpdateId = id;

            try {

                String jsonPacket = jsonHelper.ReadJson(getApplicationContext(), fileName);
                NetworkApiAccess myApi = new NetworkApiAccess(connectionHandler, getApplicationContext());
//                String result = "SR";
                if (technique.equals("Rhodamine")) {
                    myApi.startHttpPost(jsonPacket, "rhod");
                }else if (technique.equals("D-Hepatitis")) {
                    myApi.startHttpPost(jsonPacket, "hep");
                }
//                Log.d(TAG, jsonPacket);
//                Log.d(TAG, result);

            } catch (JsonSaveException e) {
                MensagensToast.showMessage(getApplicationContext(), e.getMessage());
            }
        }
    }

    private class SearchOnDatabase implements View.OnClickListener {

        @Override
        public void onClick(View v) {
            updateExamsListView();
        }
    }

    private void updateExamsListView() {
        String technique = (String) spinnerTechnique.getSelectedItem();
        int result = spinnerResult.getSelectedItemPosition();

        List<Exam> list = myDbFacade.getExams(technique, result);

        if (list.size() == 0) {
            MensagensToast.showMessage(getApplicationContext(), "List is empty");
        }
        examAdapter = new ExamAdapter(list);
        examAdapter.setMyClickListener(new SearchListener());
        recycler.setAdapter(examAdapter);
    }
}