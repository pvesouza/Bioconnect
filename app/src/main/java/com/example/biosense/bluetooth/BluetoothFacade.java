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
        String request = "discon\n";
        this.connection.sendData(request);
    }

    public void testEmstat() throws BluetoothException {
        String request = "pico_ver\n";
        this.connection.sendData(request);
    }

    public void sendRhodamine() throws BluetoothException{
        this.connection.sendData("scan\n");
    }

    public void sendHepatitis() throws BluetoothException {
        this.connection.sendData("hep\n");
    }
}
