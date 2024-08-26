package com.example.biosense.bluetooth;

import android.Manifest;
import android.bluetooth.BluetoothDevice;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.example.biosense.Activity_Exam;
import com.example.biosense.R;
import com.example.biosense.utils.MensagensToast;

import java.util.List;

public class BluetoothListAdapter extends RecyclerView.Adapter<BluetoothListAdapter.MyViewHolder> {

	private final List<BluetoothDevice> listaDeBluetooth;
	private final Context context;
	private String deviceName;
	private String deviceMac;
	private MyOnclickListener myClickListener;

	//Construtor
	public BluetoothListAdapter(Context context, List<BluetoothDevice> lista) {
		this.context = context;
		this.listaDeBluetooth = lista;
		Log.d("Created", "Bluetooth Adapter");
	}

	@NonNull
	@Override
	public BluetoothListAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
		View view = LayoutInflater.from(parent.getContext())
				.inflate(R.layout.linha_lista_bluetooth, parent, false);
		return new MyViewHolder(view);
	}

	@Override
	public void onBindViewHolder(@NonNull BluetoothListAdapter.MyViewHolder holder, int position) {


		if (ActivityCompat.checkSelfPermission(this.context, Manifest.permission.BLUETOOTH_CONNECT) != PackageManager.PERMISSION_GRANTED) {
			MensagensToast.showMessage(this.context, "Permission not granted");
			return;
		}

		final String btName = this.listaDeBluetooth.get(position).getName();
		final String btMac = this.listaDeBluetooth.get(position).getAddress();
		holder.getTextViewName().setText(btName);
		holder.getTextView_Mac().setText(btMac);

		// Binds an on Click customized an OnClicklistener
		holder.itemView.setOnClickListener(v -> {
			if (myClickListener != null) {
				myClickListener.onClick(btName, btMac);
			}
		});
	}

	@Override
	public long getItemId(int location) {
		return location;
	}

	@Override
	public int getItemCount() {
		return this.listaDeBluetooth.size();
	}

	public void setOnClickListener(MyOnclickListener onclickListener){
		this.myClickListener = onclickListener;
	}

	// Customized OnClickListener
	public interface MyOnclickListener {
		void onClick(String btName, String btAdd);
	}

	public static class MyViewHolder extends RecyclerView.ViewHolder {
		private final TextView textView_Name;
		private final TextView textView_Mac;

		public MyViewHolder(View view) {
			super(view);
			// Define click listener for the ViewHolder's View
			this.textView_Name = (TextView) view.findViewById(R.id.linha_tabela_nome);
			this.textView_Mac = (TextView) view.findViewById(R.id.linha_tabela_mac);
		}

		public TextView getTextViewName() {
			return textView_Name;
		}

		public TextView getTextView_Mac(){
			return textView_Mac;
		}
	}

}
