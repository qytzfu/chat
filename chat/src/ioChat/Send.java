package ioChat;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.TimerTask;

public class Send implements Runnable{
	
	Socket socket = null;
	String name;
	boolean isRun = true;
	long lastSendTime = 0;
	
	public long getLastSendTime() {
		return lastSendTime;
	}
	public void setLastSendTime(long lastSendTime) {
		this.lastSendTime = lastSendTime;
	}
	
	
	public Send() {
		// TODO Auto-generated constructor stub
		lastSendTime = System.currentTimeMillis();

	}
	public Send(Socket socket){
		this.socket = socket;
		lastSendTime = System.currentTimeMillis();
	}
	public Send(Socket socket,String name){
		this.socket = socket;
		this.name = name;
		lastSendTime = System.currentTimeMillis();

		DataOutputStream dos = null;
		try {
			dos = new DataOutputStream(socket.getOutputStream());
			dos.writeUTF(name);
			dos.flush();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			CloseUtil.close(dos);
		}
	}	
	public void sendMessage(){
		BufferedReader info = null;
		DataOutputStream dos = null;
		try {
			info = new BufferedReader(new InputStreamReader(System.in));
			String msg = info.readLine();
			//不要打印啦
//			System.out.println(msg);
//			PrintWriter pw = new PrintWriter(new OutputStreamWriter(socket.getOutputStream()));
//			pw.write(msg);
//			pw.flush();
			dos = new DataOutputStream(socket.getOutputStream());
			dos.writeUTF(msg);
			dos.flush();
			lastSendTime = System.currentTimeMillis();
			
		} catch (IOException e) {
			// TODO Auto-generated catch block
			isRun = false;
			CloseUtil.close(info,dos);
		}
	}
	
	@Override
	public void run(){
		while (isRun) {
			// TODO Auto-generated method stub
			sendMessage();
		}
	}
}

