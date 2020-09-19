import java.io.*;
import java.net.*;
public class UDPClient {
	String unique_code;
	public String getUC(){
		return unique_code;
	}
	public UDPClient(Double no_longt,Double no_latt) 
	//public static void main(String args[]) throws Exception
	{
		DatagramSocket clientSocket = null;
		InetAddress IPAddress = null;
		DatagramPacket receiveP = null;
		byte[] sendData = new byte[1024];
		byte[] receiveData = new byte[1024];
		try {
			clientSocket = new DatagramSocket();
			receiveP = new DatagramPacket(receiveData,receiveData.length);
			IPAddress = InetAddress.getByName("172.28.19.105");
		/*****************************************/
		String longtitude = String.valueOf(no_longt);
		String lattitude = String.valueOf(no_latt);
		String modifiedsentence=longtitude +"@"+lattitude+"#";
		/***************************************/
		sendData = modifiedsentence.getBytes();
		DatagramPacket sendPacket = 
		new DatagramPacket(sendData,sendData.length,IPAddress,9090);
		clientSocket.send(sendPacket);
		clientSocket.receive(receiveP);
		String receive_code = new String(receiveP.getData());
		unique_code = receive_code.substring(0,receive_code.indexOf("!"));
		} catch (IOException e) {
			e.printStackTrace();
		}
		clientSocket.close();
	}
}
