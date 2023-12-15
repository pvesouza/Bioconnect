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

import com.example.biosense.bluetooth.Bluetooth;
import com.example.biosense.bluetooth.BluetoothListAdapter;
import com.example.biosense.utils.MensagensToast;

import java.util.List;

public class List_Bluetooth_Devices extends AppCompatActivity {

    private RecyclerView myRecycler;
    private RecyclerView.LayoutManager recyclerManager;
    private Bluetooth btHelper;
    private List<BluetoothDevice> myBtDevices;

    protected static int REQUEST_BLUETOOTH_CONNECT = 12;

    private ActivityResultLauncher<Intent> resultActivity = registerForActivityResult(new ActivityResultContracts.StartActivityForResult(),
            result->{
                if (result.getResultCode() == RESULT_OK){
                    MensagensToast.showMessage(this, "Bluetooth eneabled");
//                    ActivityCompat.requestPermissions(List_Bluetooth_Devices.this, new String[]{
//                            Manifest.permission.BLUETOOTH_CONNECT
//                    }, REQUEST_BLUETOOTH_CONNECT);
                    // Update the Recycler View
                    this.myBtDevices = this.btHelper.getPairedDevices();
                    if (this.myBtDevices != null){

                        if (this.myBtDevices.isEmpty()){
                            MensagensToast.showMessage(this, "No devices Connected");
                        }else{
                            BluetoothListAdapter myAdapter = new BluetoothListAdapter(getApplicationContext(), this.myBtDevices);
                            recyclerManager = new LinearLayoutManager(this);
                            this.myRecycler.setLayoutManager(recyclerManager);
                            this.myRecycler.setAdapter(myAdapter);
                            Log.d("List 1", "paired");
                        }
                        Log.d("Result", "passou aqui");
                    }else {
                        MensagensToast.showMessage(getApplicationContext(), "Permission not granted");
                    }
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
                this.myBtDevices = this.btHelper.getPairedDevices();
                if (this.myBtDevices != null){

                    if (this.myBtDevices.isEmpty()){
                        MensagensToast.showMessage(this, "No devices Connected");
                    }else{

                        BluetoothListAdapter myAdapter = new BluetoothListAdapter(getApplicationContext(), this.myBtDevices);
                        recyclerManager = new LinearLayoutManager(this);
                        this.myRecycler.setLayoutManager(recyclerManager);
                        this.myRecycler.setAdapter(myAdapter);
                        Log.d("List 2", "paired");
                    }
                }else {
                    MensagensToast.showMessage(getApplicationContext(), "Permission not granted");
                }
                Log.d("Request", "passou aqui");
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
                // Update the Recycler View
                this.myBtDevices = this.btHelper.getPairedDevices();
                if (this.myBtDevices != null) {

                    if (this.myBtDevices.isEmpty()) {
                        MensagensToast.showMessage(this, "No Paired devices");
                    } else {
                        BluetoothListAdapter myAdapter = new BluetoothListAdapter(getApplicationContext(), this.myBtDevices);
                        recyclerManager = new LinearLayoutManager(this);
                        this.myRecycler.setLayoutManager(recyclerManager);
                        this.myRecycler.setAdapter(myAdapter);
                        Log.d("List 3", "paired");
                    }
                }else{
                    Log.d("List", "Error");
                }
            }

        }else{
            MensagensToast.showMessage(this, "No Bluetooth Hardware Available");
        }
    }
}