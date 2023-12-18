package com.example.biosense;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;

import com.example.biosense.utils.MensagensToast;

public class ControllerActivity extends AppCompatActivity {

    private boolean isBluetoothKnown;
    private TextView textView_btName, textView_btAdd;
    private Button button_connect;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_controller);
        this.textView_btAdd = findViewById(R.id.textview_controller_btAdd);                         // Shows the Bluetooth device address to connect
        this.textView_btName = findViewById(R.id.textview_controller_btName);                       // Shows the Bluetooth device name to connect
        this.button_connect = findViewById(R.id.button_controller_connect);                         // Tries to setup a connection to a Bluetooth device

        // Getting Bluetooth device settings
        Intent it = getIntent();
        if (it != null) {
            Bundle data = it.getExtras();
            if (data != null) {
                String btName = data.getString("BT_NAME");
                String btAdd = data.getString("BT_ADD");

                textView_btName.setText(btName);
                textView_btAdd.setText(btAdd);

            }else{
                this.isBluetoothKnown = false;
                MensagensToast.showMessage(this, "No Bluetooth data received");
            }
        }
    }
}