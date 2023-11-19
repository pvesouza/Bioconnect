package com.example.biosense.bluetooth;

import android.Manifest;
import android.bluetooth.BluetoothDevice;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
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

import java.util.List;

public class BluetoothListAdapter extends RecyclerView.Adapter<BluetoothListAdapter.ViewHolder> {

	private List<BluetoothDevice> listaDeBluetooth;
	private Context context;

	//Construtor
	public BluetoothListAdapter(Context context, List<BluetoothDevice> lista) {
		this.context = context;
		this.listaDeBluetooth = lista;
	}


	@NonNull
	@Override
	public BluetoothListAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
		View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.linha_lista_bluetooth, parent, false);
		return new ViewHolder(view);
	}

	@Override
	public void onBindViewHolder(@NonNull BluetoothListAdapter.ViewHolder holder, int position) {
		String btName = "";
		String btMac = "";
		if (ActivityCompat.checkSelfPermission(this.context, Manifest.permission.BLUETOOTH_CONNECT) != PackageManager.PERMISSION_GRANTED) {
			btName = this.listaDeBluetooth.get(position).getName();
			btMac = this.listaDeBluetooth.get(position).getAddress();
			holder.getTextViewName().setText(btName);
			holder.getTextView_Mac().setText(btMac);
		}

		View v = holder.itemView;
		String finalBtMac = btMac;
		String finalBtName = btName;
		v.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View view) {
				// Starts the exam activity by clicking on item list
				if (!(finalBtMac.isEmpty() && finalBtName.isEmpty())){
					Bundle data = new Bundle();
					data.putString("BT_NAME", finalBtName);
					data.putString("BT_MAC", finalBtMac);
					Intent it = new Intent(context, Activity_Exam.class);
					it.putExtras(data);
					context.startActivity(it);
				}
			}
		});

	}

	@Override
	public long getItemId(int location) {
		return location;
	}

	@Override
	public int getItemCount() {
		return 0;
	}

	public static class ViewHolder extends RecyclerView.ViewHolder {
		private final TextView textView_Name;
		private final TextView textView_Mac;

		public ViewHolder(View view) {
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
