package com.example.biosense.bluetooth;

import android.Manifest;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.os.Handler;
import android.util.Log;

import java.util.ArrayList;
import java.util.List;

public class BluetoothScanner {
    private BluetoothAdapter myAdapter;
    private Context context;

    private Handler listHandler;

    private List<BluetoothDevice> myFoundDevicesList;
    private static final String TAG = "BluetoothScanner";

    public BluetoothScanner(Context ctx){
        this.context = ctx;
        this.myAdapter = BluetoothAdapter.getDefaultAdapter();
        this.myFoundDevicesList = new ArrayList<>();
    }

    public void startDiscovery() {
        if (this.myAdapter == null) {
            Log.d(TAG, "No device");
            return;
        }

        if (this.myAdapter.isEnabled()) {

            String permission = Manifest.permission.BLUETOOTH_SCAN;

            if (this.context.checkSelfPermission(permission) == PackageManager.PERMISSION_GRANTED) {
                // Init Bluetooth scan
                if (this.myAdapter.startDiscovery()) {
                    this.context.registerReceiver(dicoveryReceiver, new IntentFilter(BluetoothDevice.ACTION_FOUND));
                    this.getMyFoundDevicesList().clear();
                    Log.d(TAG, "Start Discovery");
                }

            }else{
                Log.d(TAG, "Not permitted tp scan");
            }
        }
    }

    private final BroadcastReceiver dicoveryReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();
            // Test if it was found some bluetooth device
            if (BluetoothDevice.ACTION_FOUND.equals(action)) {
                // Get the BluetoothDevice Object from the Intent
                BluetoothDevice device = intent.getParcelableExtra(BluetoothDevice.EXTRA_DEVICE);

                if (device != null) {

                    if (context.checkSelfPermission(Manifest.permission.BLUETOOTH_CONNECT) == PackageManager.PERMISSION_GRANTED) {

                        if (!getMyFoundDevicesList().contains(device)) {
                            String btName, btAdd;
                            btName = device.getName();
                            btAdd =  device.getAddress();
                            if (btName != null && btAdd != null) {
                                Log.d(TAG, "Device Found\n" + device.getName() + "\n" + device.getAddress());
                                getMyFoundDevicesList().add(device);
                            }
                        }
                    }
                }
            }
        }
    };

    //Stop Bluetooth discovery
    public void stopDiscovery() {
        String permission = Manifest.permission.BLUETOOTH_SCAN;
        if (this.context.checkSelfPermission(permission) == PackageManager.PERMISSION_GRANTED) {
            // Init Bluetooth scan
            if ( this.myAdapter.cancelDiscovery()) {
                Log.d(TAG, "Stop Discovery");
                context.unregisterReceiver(this.dicoveryReceiver);
            }
        }
    }

    public List<BluetoothDevice> getMyFoundDevicesList() {
        return myFoundDevicesList;
    }
}
