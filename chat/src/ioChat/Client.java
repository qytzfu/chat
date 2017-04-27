package ioChat;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.Socket;
import java.util.Timer;

public class Client {
	
	public static void main(String[] args) throws IOException{
		
		BufferedReader in = new BufferedReader(new InputStreamReader(System.in));
		String name = in.readLine();
		if(name == null | name == ""){
			return;
		}
		
		Socket socket = new Socket("localhost",8888);
		Send send = new Send(socket,name);
		new Thread(send).start();
		new Thread(new Receive(socket,name)).start();
		
		Timer timer = new Timer();
//		timer.schedule(new HeartTask(socket,name), 1000,10000);
		
		new Timer().schedule(new TimeoutTask(send),1000,1000);
	}
}
































