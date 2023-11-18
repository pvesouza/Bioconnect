package com.example.biosense.bluetooth;

public class Protocolo {

	//private static short START = 0X7E;
	//private static short DATAEND = 0XE7;
	
	public Protocolo() {
	}
	
	public byte[] montaPacote(byte[] data){
		
		int lengh = data.length;
		byte[] pacote = new byte[lengh];
		
		for (int i = 0; i < lengh; i++){
			
			pacote[i] = data[i];
			//if (i == 0){
				//pacote[i] = (byte) Protocolo.START;
			//}else if (i == (lengh - 1)){
				
				//pacote[i] = (byte) Protocolo.DATAEND;
			//}else{
				
				//pacote[i] = data[i - 1];
			//}
		}
		return pacote;
	}
	
	public byte[] montaPacote (String data){
		
		int lengh = data.length();
		byte[] pacote = new byte[lengh];
		
		for (int i = 0; i < lengh; i++){
			pacote[i] = (byte) data.charAt((i));
			//if (i == 0){
				//pacote[i] = (byte) Protocolo.START;
			//}else if (i == (lengh - 1)){
				//pacote[i] = (byte) Protocolo.DATAEND;
			//}else{
				//pacote[i] = (byte) data.charAt((i -1));
			//}
		}
		
		return pacote;
		
	}
	
	public byte[] sendRfidRequest(){
		
		String request = "REQUEST";
		byte data[] = this.montaPacote(request);
		return data;
		
	}
}
