package com.example.biosense;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.Manifest;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import com.example.biosense.bluetooth.Bluetooth;
import com.example.biosense.bluetooth.BluetoothListAdapter;
import com.example.biosense.utils.MensagensToast;

import java.util.List;

public class List_Bluetooth_Devices extends AppCompatActivity {

    private RecyclerView myRecycler;
    private RecyclerView.LayoutManager recyclerManager;
    private Bluetooth btHelper;
    private List<BluetoothDevice> myBtDevices;
    private BluetoothListAdapter myAdapter;

    private TextView textViewTitle;

    protected static int REQUEST_BLUETOOTH_CONNECT = 12;

    private ActivityResultLauncher<Intent> resultActivity = registerForActivityResult(new ActivityResultContracts.StartActivityForResult(),
            result->{
                if (result.getResultCode() == RESULT_OK){
                    MensagensToast.showMessage(this, "Bluetooth eneabled");
//                    ActivityCompat.requestPermissions(List_Bluetooth_Devices.this, new String[]{
//                            Manifest.permission.BLUETOOTH_CONNECT
//                    }, REQUEST_BLUETOOTH_CONNECT);
                    // Update the Recycler View
                    updateListOfPairedDevices();
                }else{
                    MensagensToast.showMessage(this, "Bluetooth not eneabled");
                }
            }
    );

    private ActivityResultLauncher<String> requestResult = registerForActivityResult(new ActivityResultContracts.RequestPermission(), isGranted ->{

        if (isGranted){
            MensagensToast.showMessage(this, "Permission granted!!");

            if (this.btHelper.isEneable()) {
                // Update the Recycler View
                updateListOfPairedDevices();
            }else {
                Intent request = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
                resultActivity.launch(request);
            }

           }else{
            MensagensToast.showMessage(this, "Permission not granted!!");
            // TODO show educational UI
        }
    });

    // Result of Bluetooth eneble request



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list_bluetooth_devices);
        this.btHelper = new Bluetooth(getApplicationContext());
        this.myRecycler = findViewById(R.id.recycler_devices_list);
        this.textViewTitle = findViewById(R.id.textview_list_bt_devices_title);
        this.recyclerManager = new LinearLayoutManager(this);
        this.myRecycler.setLayoutManager(recyclerManager);



        if (this.btHelper.isBluetoothAvailable()) {
            // Bluetooth is enabled ? If not Request to enable bluetooth device
            if (!this.btHelper.getAdaptadorBluetooth().isEnabled()) {

                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
                    requestResult.launch(Manifest.permission.BLUETOOTH_CONNECT);
                }else {
                    Intent request = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
                    resultActivity.launch(request);
                }
            }else {
                updateListOfPairedDevices();
            }

        }else{
            MensagensToast.showMessage(this, "No Bluetooth Hardware Available");
        }
    }

    private void updateListOfPairedDevices() {
        // Update the Recycler View
        this.myBtDevices = this.btHelper.getPairedDevices();
        if (this.myBtDevices != null) {

            if (this.myBtDevices.isEmpty()) {
                MensagensToast.showMessage(this, "No Paired devices");
            } else {
                this.myAdapter = new BluetoothListAdapter(getApplicationContext(), this.myBtDevices);
                // Set Bluetooth item listener
                this.myAdapter.setOnClickListener(new MyHoldListener());
                // Setting adapter to recycler
                this.myRecycler.setAdapter(myAdapter);
                String title = getString(R.string.list_of_paired_devices);
                title = title + ": " + this.myBtDevices.size();
                this.textViewTitle.setText(title);
                Log.d("List 3", "paired");
            }
        }else{
            MensagensToast.showMessage(getApplicationContext(), "Permission not granted");
        }
    }

    public class MyHoldListener implements BluetoothListAdapter.MyOnclickListener {
        @Override
        public void onClick(String btName, String btAdd) {
            // Starts Controller Activity passing the Bluetooth specifications
            Bundle b = new Bundle();
            b.putString("BT_NAME",btName);
            b.putString("BT_ADD", btAdd);
            Intent it = new Intent(getApplicationContext(), ControllerActivity.class);
            it.putExtras(b);
            startActivity(it);
        }
    }
}