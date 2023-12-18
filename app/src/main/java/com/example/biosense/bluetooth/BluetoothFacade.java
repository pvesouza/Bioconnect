package com.example.biosense.bluetooth;

public class BluetoothFacade {
    private final BluetoothConnection connection;

    public BluetoothFacade(BluetoothConnection comm) {
        this.connection = comm;
    }

    public void sendEnableConnection() throws BluetoothException {
        String request = "connect\n";
        this.connection.sendData(request);

    }

    public void sendDisableConnection() throws BluetoothException {
        String request = "disconnect\n";
        this.connection.sendData(request);
    }
}
