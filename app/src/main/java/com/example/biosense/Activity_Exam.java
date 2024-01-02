package com.example.biosense;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.bluetooth.BluetoothDevice;
import android.content.Intent;
import android.database.DataSetObserver;
import android.os.Bundle;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.SpinnerAdapter;
import android.widget.TextView;

import com.example.biosense.bluetooth.Bluetooth;
import com.example.biosense.bluetooth.BluetoothConnection;
import com.example.biosense.bluetooth.BluetoothException;
import com.example.biosense.utils.MensagensToast;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;

public class Activity_Exam extends AppCompatActivity {

    private Button start_button;
    private Button connection_tooggle;
    private Spinner exams_spinner;
    private BluetoothConnection connection;

    private boolean isEmstatOn = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_choose_exam);

        // Start Views
        start_button = findViewById(R.id.button_start);
        connection_tooggle = findViewById(R.id.toggleButton);
        exams_spinner = findViewById(R.id.spinner);

        String btName = "";
        String btMac = "";

        // Start the Spinner with the Techniques
        ArrayList<String> techniques = new ArrayList<>();
        techniques.add("Infarct");
        techniques.add("COVID-19");
        techniques.add("Tuberculosis");
        techniques.add("Hanseniasis");
        ArrayAdapter<String> myAdapter = new ArrayAdapter<String>(getApplicationContext(), android.R.layout.simple_spinner_item, techniques);
        exams_spinner.setAdapter(myAdapter);

        Intent it = this.getIntent();
        if (it != null) {
            Bundle bd = it.getExtras();
            if (bd != null) {
                btName = bd.getString("BT_NAME");
                btMac = bd.getString("BT_NAME");
            }
        }

        // Receives the Messages from the potentiostat
        Handler handler = new Handler(message -> {
            if (message != null) {
                String s = message.getData().getString(BluetoothConnection.DADOS);
                if (s.contains("PICO_OK")) {
                    isEmstatOn = true;
                }else if (s.contains("PICO_OK")) {
                    isEmstatOn = false;
                }else if (s.contains("end")) {
                    MensagensToast.showMessage(getApplicationContext(), "End of Measuring");
                }
                MensagensToast.showMessage(getApplicationContext(), s);
            }
            return false;
        });

        connection = new BluetoothConnection(getApplicationContext(), handler);
        Bluetooth bt = new Bluetooth(getApplicationContext());
        final BluetoothDevice conDevice = bt.getDevice(btName, btMac);


        connection_tooggle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try {
                    connection.conectaBluetooth(conDevice);
                    connection_tooggle.setText("Close");
                } catch (BluetoothException e) {
                    // In case of any treated error send a toast message
                    MensagensToast.showMessage(getApplicationContext(), e.getMessage());
                }
            }
        });

        start_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (isEmstatOn){
                    String technique = (String) exams_spinner.getSelectedItem();

                    if ("Infarct".equals(technique)) {
                        String command = "scan\n";
                        sendCommand(command);
                    }else if ("Infarct".equals(technique)) {
                        String command = "scan\n";
                        sendCommand(command);
                    }else if ("Infarct".equals(technique)) {
                        String command = "scan\n";
                        sendCommand(command);
                    }else if ("Infarct".equals(technique)) {
                        String command = "scan\n";
                        sendCommand(command);
                    }
                }else {
                    // Send a status request for emstat pico
                    String command = "teste\n";
                    sendCommand(command);
                }

            }
        });
    }

    //Creates a Menu on toolbar
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.menu_controller_history) {
            // Initializes History activity and finishes the current activity
            Intent it = new Intent(getApplicationContext(), HistoryActivity.class);
            startActivity(it);
            finish();
        }else if (id == R.id.menu_controller_quit) {
            finish();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_controller, menu);
        return super.onCreateOptionsMenu(menu);
    }

    private void sendCommand(String command) {
        if (connection.isConnectionStablished()) {
            try {
                connection.sendData(command.getBytes(StandardCharsets.UTF_8));
            } catch (BluetoothException e) {
                MensagensToast.showMessage(getApplicationContext(), e.toString());
            }
        }
    }

}