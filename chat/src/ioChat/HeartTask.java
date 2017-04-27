package ioChat;

import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.util.TimerTask;

public class HeartTask extends TimerTask{
	Socket socket = null;
	String name;
	boolean isRun = true;
	String heartMessage = "heart";
	public HeartTask() {
		// TODO Auto-generated constructor stub
	}
	public HeartTask(Socket socket){
		this.socket = socket;
	}
	public HeartTask(Socket socket, String name){
		this.socket = socket;
		this.name = name;
	}
	public void sendHeart(){
		DataOutputStream dos = null;
		try {
			dos = new DataOutputStream(socket.getOutputStream());
			dos.writeUTF(heartMessage);
			dos.flush();
			
		} catch (IOException e) {
			// TODO Auto-generated catch block
			isRun = false;
			CloseUtil.close(dos);
		}
	}
	@Override
	public void run() {
		// TODO Auto-generated method stub
		//应该是if 不是while 
		if (isRun) {
			// TODO Auto-generated method stub
			sendHeart();
			
		}
	}
}

