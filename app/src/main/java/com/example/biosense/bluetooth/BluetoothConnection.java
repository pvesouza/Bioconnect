package com.example.biosense.bluetooth;

import android.Manifest;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothSocket;
import android.content.Context;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;

import androidx.core.app.ActivityCompat;

import com.example.biosense.R;
import com.example.biosense.json.JsonBaseHelper;
import com.example.biosense.utils.MensagensToast;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.nio.charset.StandardCharsets;


public class BluetoothConnection extends Bluetooth implements Runnable {

	public static Method M;
	public static String CONEXAO = "createRfcommSocket";
	public static final String DADOS = "DADOS";
	private BluetoothSocket socket;
	private InputStream inputData;
	private OutputStream outputData;
	private final Handler handler;
	private static final long TIME = 500;
	private boolean stop = false;
	private final Context context;
	private JsonBaseHelper jsonHelper;

	public BluetoothConnection(Context context, Handler handler) {
		super(context);
		this.context = context;
		this.handler = handler;
		this.jsonHelper = new JsonBaseHelper();
	}

	public void eneableConnection() {
		this.stop = true;
	}

	@Override
	public void run() {

		while (this.stop) {

			try {
				int byteDisponivel = inputData.available();
				byte dados[] = new byte[byteDisponivel];
				inputData.read(dados, 0, byteDisponivel);
				//Testa o tamanho dos dados recebidos
				if (dados.length > 0) {
					sendHandleMessage(dados);
				}
				Thread.sleep(BluetoothConnection.TIME);
			} catch (IOException e) {
				MensagensToast.showMessage(context, com.example.biosense.R.string.bluetooth_connection_erro_conexao);

			} catch (InterruptedException e) {
				MensagensToast.showMessage(context, R.string.bluetooth_connection_recepcao_interrompida);
			}

		}
	}

	private void sendHandleMessage(byte[] dados) {

		Message mensagem = new Message();
		Bundle bundle = new Bundle();
		char[] data = this.byteToChar(dados);
		String s = new String(data);
		Log.d("RECEIVED: ", s);
		// Test if it is a JSON Packet
		if (s.contains("{") && s.contains("}")){
			this.jsonHelper.addJsonLine(s);
		}else{
			bundle.putCharArray(DADOS, data);
			mensagem.setData(bundle);
			handler.sendMessage(mensagem);
		}
	}

	private char[] byteToChar(byte[] bytes) {
		short[] dados = new short[bytes.length];
		char[] dadoc = new char[bytes.length];

		for (int i = 0; i < bytes.length; i++) {

			if (bytes[i] >= 0) {
				dados[i] = (short) bytes[i];

			} else {
				dados[i] = (short) ((short) bytes[i] + 256);
			}
			dadoc[i] = (char) dados[i];
		}
		return dadoc;
	}

	public void sendData(byte[] data) throws BluetoothException {
		//Testa se h� conex�o
		if (this.isConnectionStablished()) {
			try {
				outputData.write(data);
			} catch (IOException e) {
				String menssagem = context.getString(R.string.bluetooth_connection_envio_dados);
				throw new BluetoothException(menssagem);
			}
		} else {
			String menssagem = context.getString(R.string.bluetooth_connection_erro_conexao);
			throw new BluetoothException(menssagem);
		}
	}

	public void sendData(String request) throws BluetoothException {
		byte[] data = request.getBytes(StandardCharsets.UTF_8);
		//Testa se h� conex�o
		if (this.isConnectionStablished()) {
			try {
				outputData.write(data);
			} catch (IOException e) {
				String menssagem = context.getString(R.string.bluetooth_connection_envio_dados);
				throw new BluetoothException(menssagem);
			}
		} else {
			String menssagem = context.getString(R.string.bluetooth_connection_erro_conexao);
			throw new BluetoothException(menssagem);
		}
	}

	public boolean isConnectionStablished() {
		if (socket != null)
			return socket.isConnected();
		return false;
	}

	//Finaliza a conex�o bluetooth
	public void stopConnection() throws BluetoothException {
		this.stop = false;
		//Tenta fechar o socket
		try {
			if (this.isConnectionStablished()) {
				inputData.close();
				outputData.close();
				socket.close();
			}
		} catch (IOException e) {
			String mensagem = context.getString(R.string.bluetooth_connection_erro_fechar);
			throw new BluetoothException(mensagem);
		}
	}

	public void conectaBluetooth(BluetoothDevice device) throws BluetoothException {
		//Testa se o dispositivo para conex�o � nulo
		if (device != null) {
			try {
				M = device.getClass().getMethod(BluetoothConnection.CONEXAO, int.class);
			} catch (NoSuchMethodException e) {
				throw new BluetoothException("Method does not exist");
			}
			try {
				socket = (BluetoothSocket) M.invoke(device, 1);
			} catch (IllegalAccessException | InvocationTargetException e) {
				throw new BluetoothException("Socket could not be initialized");
			}
			//Testa se o Socket � nulo
			if (socket != null) {
				if (ActivityCompat.checkSelfPermission(this.context, Manifest.permission.BLUETOOTH_CONNECT) == PackageManager.PERMISSION_GRANTED) {
					try {
						socket.connect();
					} catch (IOException e) {
						throw new BluetoothException("Connection error");
					}
					try {
						inputData = socket.getInputStream();
					} catch (IOException e) {
						throw new BluetoothException("Input stream error");
					}
					try {
						outputData = socket.getOutputStream();
					} catch (IOException e) {
						throw new BluetoothException("Outputstream error");
					}
					MensagensToast.showMessage(this.context, "Bluetooth Connection Stablished");
				}
			}
		}else{
			throw new BluetoothException("Device does not exists");
		}

	}

}
