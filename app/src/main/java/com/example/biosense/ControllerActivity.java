package com.example.biosense;

import static android.view.View.GONE;

import androidx.appcompat.app.AppCompatActivity;


import android.bluetooth.BluetoothDevice;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.HandlerThread;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.ToggleButton;

import com.example.biosense.bluetooth.Bluetooth;
import com.example.biosense.bluetooth.BluetoothConnection;
import com.example.biosense.bluetooth.BluetoothException;
import com.example.biosense.bluetooth.BluetoothFacade;
import com.example.biosense.json.JsonBaseHelper;
import com.example.biosense.json.JsonSaveException;
import com.example.biosense.utils.MensagensToast;

import java.util.ArrayList;
import java.util.Arrays;

public class ControllerActivity extends AppCompatActivity {

    protected static final String TAG = "Controller";
    protected boolean isBluetoothKnown;
    private TextView textView_btName, textView_btAdd, textViewResult;
    protected ToggleButton button_connect;
    protected Button button_testPico, buttonStart;
    protected Spinner spinnerTechniques;
    protected BluetoothConnection myConnection;
    private ProgressBar commBar;

    protected BluetoothDevice deviceToConnect;

    protected Handler myHandler;

    private final String[] techniques = new String[] {
            "",
            "Rhodamine test",
            "D-Hepatitis"
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_controller);
        this.textView_btAdd = findViewById(R.id.textview_controller_btAdd);                         // Shows the Bluetooth device address to connect
        this.textView_btName = findViewById(R.id.textview_controller_btName);                       // Shows the Bluetooth device name to connect
        this.button_connect = findViewById(R.id.button_controller_connect);                         // Tries to setup a connection to a Bluetooth device
        this.buttonStart = findViewById(R.id.button_controller_start);                              // Strts an Exam
        this.textViewResult = findViewById(R.id.textView_controller_result);
        this.spinnerTechniques = findViewById(R.id.spinner_controller_techniques);
        this.button_testPico = findViewById(R.id.button_controller_test_pico);                      // Test Emstat Pico
        this.button_testPico.setVisibility(GONE);
        this.commBar = findViewById(R.id.progressBar_controller);
        this.commBar.setVisibility(GONE);

        // Initializes the Spinner List
        ArrayList<String> techniquesArray = new ArrayList<>(Arrays.asList(techniques));
        ArrayAdapter<String> spinnerAdapter = new ArrayAdapter<>(getApplicationContext(), android.R.layout.simple_spinner_dropdown_item, techniquesArray);
        this.spinnerTechniques.setAdapter(spinnerAdapter);

        // This handles all Bluetooth messages
        this.myHandler = new Handler(m -> {
            Bundle dataMessage = m.getData();
            if (dataMessage != null) {
                String message = dataMessage.getString(BluetoothConnection.DADOS);
                if (message != null) {

                    Log.d(TAG, message);
                    if (message.contains("Pico_OK")) {
                        MensagensToast.showMessage(getApplicationContext(), "Potentiostat OK");
                        this.button_testPico.setVisibility(GONE);
                    } else if (message.contains("Pico_NOK")){
                        MensagensToast.showMessage(getApplicationContext(), "Potentiostat Not OK!\nTry again!!");
                    }else if (message.contains("End_")) {
                        String data = this.myConnection.getJsonData();

                        if (!data.isEmpty()) {
                            JsonBaseHelper jsonBaseHelper = new JsonBaseHelper();
                            try {
                                jsonBaseHelper.saveJson(getApplicationContext(), data);
                                this.myConnection.clearJasonData();
                            } catch (JsonSaveException e) {
                                MensagensToast.showMessage(getApplicationContext(), e.getMessage());
                            }
                            MensagensToast.showMessage(getApplicationContext(), "End of Exam!");
                        }

                        commBar.setVisibility(View.GONE);
                    }else if (message.contains("Response_begin")) {
                        commBar.setVisibility(View.VISIBLE);
                    }
                } else {
                    Log.d(TAG, "Null");
                }
            }
            return false;
        });

        // Getting Bluetooth device settings
        Intent it = getIntent();
        if (it != null) {
            Bundle data = it.getExtras();
            if (data != null) {
                String btName = data.getString("BT_NAME");
                String btAdd = data.getString("BT_ADD");

                textView_btName.setText(btName);
                textView_btAdd.setText(btAdd);

                this.isBluetoothKnown = true;

                Bluetooth bluetoothHelper = new Bluetooth(getApplicationContext());
                this.deviceToConnect = bluetoothHelper.getDevice(btName, btAdd);

                // Sets up and open a Bluetooth connection
                this.myConnection = new BluetoothConnection(getApplicationContext(), this.myHandler);

            } else {
                this.isBluetoothKnown = false;
                MensagensToast.showMessage(this, "No Bluetooth data received");
            }
        }

        this.button_testPico.setOnClickListener(new TestPicoListener());
        this.buttonStart.setOnClickListener(new RunExamListener());

        // Test Connection and tries to connect with potentiostat via bluetooth
        this.button_connect.setOnClickListener(v -> {
            boolean isChecked = this.button_connect.isChecked();

            if (isChecked) {
                Log.d(TAG, isChecked + "");
                if (isBluetoothKnown) {
                    try {
                        if (!this.myConnection.isConnectionStablished()) {
                            this.myConnection.conectaBluetooth(this.deviceToConnect);
                            this.myConnection.eneableConnection();
                            BluetoothFacade bluetoothFacade = new BluetoothFacade(this.myConnection);
                            bluetoothFacade.sendEnableConnection();
                            MensagensToast.showMessage(getApplicationContext(), "Conection Ready");
                            this.button_testPico.setVisibility(View.VISIBLE);

                        } else {
                            MensagensToast.showMessage(getApplicationContext(), "Conection Already Ready");
                        }

                    } catch (BluetoothException e) {
                        this.button_connect.setChecked(false);
                        this.button_connect.setText("Connect");
                        MensagensToast.showMessage(getApplicationContext(), e.getMessage());
                    }
                } else {
                    this.button_connect.setChecked(false);
                    this.button_connect.setText("Connect");
                    MensagensToast.showMessage(getApplicationContext(), "Bluetooth not known!!");
                }

            } else {
                Log.d(TAG, isChecked + "");
                if (isBluetoothKnown) {
                    if (this.myConnection.isConnectionStablished()) {
                        try {
                            BluetoothFacade facade = new BluetoothFacade(this.myConnection);
                            facade.sendDisableConnection();
                            this.myConnection.stopConnection();
                            MensagensToast.showMessage(getApplicationContext(), "Conection Stopped");
                        } catch (BluetoothException e) {
                            this.button_connect.setChecked(true);
                            this.button_connect.setText("Disconnect");
                            MensagensToast.showMessage(getApplicationContext(), e.getMessage());
                        }
                    }
                }
            }

        });
    }

    @Override
    public void onPause() {
        super.onPause();
        // Stops Bluetooth connection
        Log.d(TAG, "onPause: ");
    }
    
    @Override
    public void onStop() {
        super.onStop();
        finish();
        Log.d(TAG, "onStop: ");
    }
    @Override
    public void onDestroy(){
        // Stops Bluetooth connection
        super.onDestroy();
        if (this.myConnection.isConnectionStablished()) {
            BluetoothFacade f = new BluetoothFacade(this.myConnection);
            try {
                f.sendDisableConnection();
                this.myConnection.stopConnection();
            } catch (BluetoothException e) {
                MensagensToast.showMessage(getApplicationContext(), "Connection not interrupted");
            }
        }
    }

    // Sends a command to Potentiostat to test emstat hardware
    private class TestPicoListener implements View.OnClickListener {
        @Override
        public void onClick(View v) {
            if (isBluetoothKnown) {
                if (myConnection.isConnectionStablished() && myConnection.isEneable()){
                    BluetoothFacade facade = new BluetoothFacade(myConnection);
                    try {
                        facade.testEmstat();
                    } catch (BluetoothException e) {
                        MensagensToast.showMessage(getApplicationContext(), e.getMessage());
                    }
                }
            }
        }
    }

    // Chooses an exam in order to run
    private class RunExamListener implements View.OnClickListener{
        @Override
        public void onClick(View v) {
            if (isBluetoothKnown) {
                if (myConnection.isConnectionStablished() && myConnection.isEneable()){
                    BluetoothFacade facade = new BluetoothFacade(myConnection);
                    int position = spinnerTechniques.getSelectedItemPosition();

                    switch (position) {
                        case 0:
                            MensagensToast.showMessage(getApplicationContext(), "No Exam chosen");
                            Log.d(TAG, techniques[0]);
                            break;
                        case 1:
                            try {
                                facade.sendRhodamine();
                            } catch (BluetoothException e) {
                                MensagensToast.showMessage(getApplicationContext(), e.getMessage());
                            }
                            Log.d(TAG, techniques[1]);
                            break;
                        case 2:
                            try {
                                facade.sendHepatitis();
                            } catch (BluetoothException e) {
                                MensagensToast.showMessage(getApplicationContext(), e.getMessage());
                            }
                            Log.d(TAG, techniques[2]);
                            break;
                    }
                }
            }
        }
    }
}