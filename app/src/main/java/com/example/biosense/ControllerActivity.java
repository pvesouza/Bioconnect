package com.example.biosense;

import androidx.appcompat.app.AppCompatActivity;


import android.bluetooth.BluetoothDevice;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.widget.TextView;
import android.widget.ToggleButton;

import com.example.biosense.bluetooth.Bluetooth;
import com.example.biosense.bluetooth.BluetoothConnection;
import com.example.biosense.bluetooth.BluetoothException;
import com.example.biosense.bluetooth.BluetoothFacade;
import com.example.biosense.utils.MensagensToast;

public class ControllerActivity extends AppCompatActivity {

    private static final String TAG = "Controller";
    private boolean isBluetoothKnown;
    private TextView textView_btName, textView_btAdd;
    private ToggleButton button_connect;
    private BluetoothConnection myConnection;

    private BluetoothDevice deviceToConnect;

    private Handler myHandler;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_controller);
        this.textView_btAdd = findViewById(R.id.textview_controller_btAdd);                         // Shows the Bluetooth device address to connect
        this.textView_btName = findViewById(R.id.textview_controller_btName);                       // Shows the Bluetooth device name to connect
        this.button_connect = findViewById(R.id.button_controller_connect);                         // Tries to setup a connection to a Bluetooth device

        // This handles all Bluetooth messages
        this.myHandler = new Handler(m->{

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

            }else{
                this.isBluetoothKnown = false;
                MensagensToast.showMessage(this, "No Bluetooth data received");
            }
        }

        this.button_connect.setOnClickListener(v -> {
            boolean isChecked = this.button_connect.isChecked();

            if (isChecked){
                Log.d(TAG, isChecked + "");
                if (isBluetoothKnown) {
                    try {
                        if (!this.myConnection.isConnectionStablished()) {
                            this.myConnection.conectaBluetooth(this.deviceToConnect);
                            this.myConnection.eneableConnection();
                            BluetoothFacade bluetoothFacade = new BluetoothFacade(this.myConnection);
                            bluetoothFacade.sendEnableConnection();
                            MensagensToast.showMessage(getApplicationContext(), "Conection Ready");
                        }else{
                            MensagensToast.showMessage(getApplicationContext(), "Conection Already Ready");
                        }

                    } catch (BluetoothException e) {
                        this.button_connect.setChecked(false);
                        this.button_connect.setText("Connect");
                        MensagensToast.showMessage(getApplicationContext(), e.getMessage());
                    }
                }else {
                    this.button_connect.setChecked(false);
                    this.button_connect.setText("Connect");
                    MensagensToast.showMessage(getApplicationContext(), "Bluetooth not known!!");
                }

            }else {
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
}