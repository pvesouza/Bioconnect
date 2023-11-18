package com.example.biosense.bluetooth;

public class BluetoothException extends Exception {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private String message;
	
	public BluetoothException(String message){
		this.message = message;
	}
	
	public String getMessage(){
		return message;
	}
}
