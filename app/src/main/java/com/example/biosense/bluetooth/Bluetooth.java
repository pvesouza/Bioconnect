package com.example.biosense.bluetooth;

import android.Manifest;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.Context;
import android.content.pm.PackageManager;

import androidx.core.app.ActivityCompat;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

public class Bluetooth {

	private BluetoothAdapter adaptadorDispositivo;
	private BluetoothDevice myDevice;
	public static final String MACADDRESS = "MACADD";
	public static final String NOMEBLUETOOTH = "NOMEBLUE";
	private Context btContext;

	public Bluetooth(Context context) {
		adaptadorDispositivo = BluetoothAdapter.getDefaultAdapter();
		this.btContext = context;
	}

	public boolean isEneable() {
		return this.adaptadorDispositivo.isEnabled();
	}

	public BluetoothDevice getMyDevice() {
		return myDevice;
	}

	public boolean isBluetoothAvailable() {
		return this.adaptadorDispositivo != null ? true : false;
	}

	public void setMyDevice(BluetoothDevice myDevice) {
		this.myDevice = myDevice;
	}

	public BluetoothAdapter getAdaptadorBluetooth() {
		return this.adaptadorDispositivo;
	}

	// If returns null - > There was no permission to bluetooth connection
	public List<BluetoothDevice> getPairedDevices() {

		if (ActivityCompat.checkSelfPermission(this.btContext, Manifest.permission.BLUETOOTH_CONNECT) == PackageManager.PERMISSION_GRANTED) {

			if (!this.adaptadorDispositivo.isEnabled()){
				this.adaptadorDispositivo.enable();
			}
			Set<BluetoothDevice> set = this.adaptadorDispositivo.getBondedDevices();
			List<BluetoothDevice> lista = new ArrayList<BluetoothDevice>();

			if (set.size() > 0) {

				for (BluetoothDevice bluetoothDevice2 : set) {

					if (bluetoothDevice2 != null) {
						lista.add(bluetoothDevice2);
					}
				}
			}

			return lista;
		}

		return null;
	}

	public BluetoothDevice getDevice(String nome, String macAddress) {

		if (ActivityCompat.checkSelfPermission(this.btContext, Manifest.permission.BLUETOOTH_CONNECT) == PackageManager.PERMISSION_GRANTED) {
			Set<BluetoothDevice> set = this.adaptadorDispositivo.getBondedDevices();
			BluetoothDevice bt = null;

			if (set.size() > 0){

				for (BluetoothDevice bluetoothDevice : set) {

					if (bluetoothDevice.getAddress().equals(macAddress) && bluetoothDevice.getName().equals(nome)){
						bt = bluetoothDevice;
					}
				}
			}

			return bt;
		}
		return null;
	}
	
	
}
