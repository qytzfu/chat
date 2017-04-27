package ioChat;

import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.Socket;

public class Receive implements Runnable{
	Socket socket;
	String name;
	boolean isRun = true;
	public Receive() {
		// TODO Auto-generated constructor stub
	}
	public Receive(Socket socket){
		this.socket = socket;
	}
	public Receive(Socket socket,String name){
		this.socket = socket;
		this.name = name;
	}
	public void receive(){
		DataInputStream dis = null;
		try {
			//这里不用bufferedReader了
//			BufferedReader br = new BufferedReader(new InputStreamReader(socket.getInputStream()));
//			String msg = br.readLine();
			dis = new DataInputStream(socket.getInputStream());
			String msg = dis.readUTF();
			if(msg.equals("heart")){
//				System.out.println("get heart");
			}else{
				System.out.println("client received:"+msg);

			}
		} catch (IOException e) {
			// TODO Auto-generated catch block
			isRun = false;
			CloseUtil.close(dis);
		}
	}
	
	@Override
	public void run() {
		while (isRun) {
			// TODO Auto-generated method stub
			receive();
		}
	}
}
