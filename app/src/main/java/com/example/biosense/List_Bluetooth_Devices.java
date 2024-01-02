package com.example.biosense;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContract;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.ActivityCompat;
import androidx.core.app.ActivityOptionsCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.Manifest;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.example.biosense.bluetooth.Bluetooth;
import com.example.biosense.bluetooth.BluetoothListAdapter;
import com.example.biosense.bluetooth.BluetoothScanner;
import com.example.biosense.utils.MensagensToast;

import java.util.List;

public class List_Bluetooth_Devices extends AppCompatActivity {

    private RecyclerView myRecycler;
    private RecyclerView.LayoutManager recyclerManager;
    private Button scan_button;
    private ProgressBar myProgressBar;

    private RecyclerView recycler_not_paired;
    private Bluetooth btHelper;
    private List<BluetoothDevice> myBtDevices;
    private BluetoothListAdapter myAdapter;

    private BluetoothListAdapter scanAdapter;

    private TextView textViewTitle;

    protected BluetoothScanner myScanner;

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


    private ActivityResultLauncher<String> scanPermissionLauncher = registerForActivityResult(new ActivityResultContracts.RequestPermission(), isGranted ->{

        if (isGranted) {
            MensagensToast.showMessage(this, "Permission granted!!\nTry again");
        }else{
            MensagensToast.showMessage(this, "Permission not granted!!");
        }
    });



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list_bluetooth_devices);
        this.btHelper = new Bluetooth(getApplicationContext());                                     // Bluetooth Base Helper
        this.myRecycler = findViewById(R.id.recycler_devices_list);                                 // Recycler that stores paired devices
        this.textViewTitle = findViewById(R.id.textview_list_bt_devices_title);
        this.recycler_not_paired = findViewById(R.id.recycler_not_paired_devices_list);             // Found new bluetooth devices
        this.scan_button = findViewById(R.id.button_scan_list_bluetooth_devices);                   // Starts a Scan for new Bluetooth devices
        this.myScanner = new BluetoothScanner(getApplicationContext());                             // Used to SCAN for new Bluetooth devices
        this.myProgressBar = findViewById(R.id.progressBar);
        this.myProgressBar.setVisibility(View.GONE);

        Toolbar myToolbar = (Toolbar) findViewById(R.id.toolbar_list_bt_devices);
        myToolbar.setTitle("Bioconnect");
        setSupportActionBar(myToolbar);


        this.recyclerManager = new LinearLayoutManager(this);
        this.myRecycler.setLayoutManager(recyclerManager);

        this.scan_button.setOnClickListener(new ScanListener());
        this.recycler_not_paired.setLayoutManager(new LinearLayoutManager(this));

        if (this.btHelper.isBluetoothAvailable()) {
            // Bluetooth is enabled ? If not Request to enable bluetooth device
            if (!this.btHelper.getAdaptadorBluetooth().isEnabled()) {

                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
                    requestResult.launch(Manifest.permission.BLUETOOTH_CONNECT);
                }else {
                    Intent request = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
                    resultActivity.launch(request);
                }
            }else if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
                requestResult.launch(Manifest.permission.BLUETOOTH_CONNECT);
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

    public class MyScanTimer implements Runnable {

        public final int SCAN_TIME = 15000;
        @Override
        public void run() {
            // Stops discovery and reads all found bluetooth devices
            myScanner.stopDiscovery();
            myProgressBar.setVisibility(View.GONE);
            List<BluetoothDevice> foundDevices =  myScanner.getMyFoundDevicesList();

            if (foundDevices.size() > 0) {
                scanAdapter = new BluetoothListAdapter(getApplicationContext(), foundDevices);
                btHelper.setSurroundingDevices(foundDevices);
                scanAdapter.setOnClickListener(new MyHoldListener());
                recycler_not_paired.setAdapter(scanAdapter);
            }else {
                MensagensToast.showMessage(getApplicationContext(), "Devices not found");
            }

        }
    }

    public class ScanListener implements View.OnClickListener{

        @Override
        public void onClick(View v) {
            if (btHelper.isBluetoothAvailable()) {
                String permission = Manifest.permission.BLUETOOTH_SCAN;
                String permissionFineLocation = Manifest.permission.ACCESS_FINE_LOCATION;
                String permissionCoarseLocation = Manifest.permission.ACCESS_COARSE_LOCATION;

                if (getApplicationContext().checkSelfPermission(permission) == PackageManager.PERMISSION_GRANTED) {
                    if (getApplicationContext().checkSelfPermission(permissionCoarseLocation) == PackageManager.PERMISSION_GRANTED) {
                        if (getApplicationContext().checkSelfPermission(permissionFineLocation) == PackageManager.PERMISSION_GRANTED) {
                            MensagensToast.showMessage(getApplicationContext(), "Scanning new devices");
                            myScanner.startDiscovery();
                            Handler handler = new Handler();
                            MyScanTimer scanTimer = new MyScanTimer();
                            handler.postDelayed(scanTimer, scanTimer.SCAN_TIME);
                            myProgressBar.setVisibility(View.VISIBLE);
                        }else {
                            scanPermissionLauncher.launch(permissionFineLocation);
                        }

                    }else {
                        scanPermissionLauncher.launch(permissionCoarseLocation);
                    }

                }else{
                    scanPermissionLauncher.launch(permission);
                    MensagensToast.showMessage(getApplicationContext(), "Permission not conceded");
                }

            }else {
                MensagensToast.showMessage(getApplicationContext(), "No Bluetooth Hardware Available");
            }
        }
    }
}