import java.io.*;
import java.net.*;
import java.util.*;
import org.eclipse.paho.client.mqttv3.IMqttDeliveryToken;
import org.eclipse.paho.client.mqttv3.MqttCallback;
import org.eclipse.paho.client.mqttv3.MqttClient;
import org.eclipse.paho.client.mqttv3.MqttConnectOptions;
import org.eclipse.paho.client.mqttv3.MqttException;
import org.eclipse.paho.client.mqttv3.MqttMessage;
import org.eclipse.paho.client.mqttv3.persist.MemoryPersistence;

public class TCPServer{
	static List<String> gloMess;
	private final static int dayTimePort = 12125;
	private static class ThreadHandler extends Thread{
		private ServerSocket ssocket;
		Socket socket;
		
		DataOutputStream outToClient;
		BufferedReader inFromClient;
		private boolean running = false;

		public ThreadHandler(Socket socket){
			this.socket=socket;
		}
		
		public void run()  {
			// TODO Auto-generated method stub
			boolean flag=false;
			running = true;
			try{
				inFromClient = new BufferedReader(new InputStreamReader(socket.getInputStream()));
				outToClient = new DataOutputStream(socket.getOutputStream());
			while (running){
					try{	
						String clientCode = inFromClient.readLine();
						for(int i=0;i<gloMess.size();i++) {
								String sentence = gloMess.get(i);
								int markn = sentence.indexOf("!");
								String unique_code= sentence.substring(0, markn);
								
								if(unique_code.equals(clientCode)) {
								String mark1 ="@";
								String mark2 ="#";
								int getMark1 = sentence.indexOf(mark1);
								int getMark2 = sentence.indexOf(mark2);
								String newStr = sentence.substring(markn+1,getMark1)+":"+sentence.substring(getMark1+1,getMark2);
								outToClient.writeBytes(newStr);//send to client
								flag=true;
								running= false;
							}
						}
						running= false;
					}catch(SocketException e){						
						e.printStackTrace();
						running = false;
					}catch(IOException ioe){
						ioe.printStackTrace();
						running = false;
					}	
			}
		}catch(IOException ioe){
			ioe.printStackTrace();
			running = false;
		}finally{
			 try {
                 socket.close();
             } catch (IOException e) {
             	}
			}
		}
	}
	public static void main(String argv[]) throws Exception
	//public void TCPS_exe() throws IOException, MqttException
	{	
		gloMess  = new ArrayList<String>();
		ServerSocket welcomeSocket =  new ServerSocket(dayTimePort);
		Subscriber s = new Subscriber("tcp://m14.cloudmqtt.com:12737","nguyentansang2","sang","2",gloMess);
		try{
			while(true){
				new ThreadHandler(welcomeSocket.accept()).start();
			}
		} 
		finally {
			welcomeSocket.close();
		}
	}
}
