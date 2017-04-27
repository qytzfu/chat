package ioChat;

import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.util.TimerTask;

public class TimeoutTask extends TimerTask{
	Send send = null;
	
	//15s
	long maxTime = 15000;
	public TimeoutTask() {
		// TODO Auto-generated constructor stub
	}

	public TimeoutTask(Send send){
		this.send = send;
	}

	@Override
	public void run() {
		// TODO Auto-generated method stub
		//应该是if 不是while 
		if (System.currentTimeMillis() - send.lastSendTime > maxTime) {
			// TODO Auto-generated method stub
			try {
				send.socket.close();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}
}

