package ioChat;

import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Iterator;

public class Server {
	static ArrayList<ReceiveAndSend> channels = new ArrayList<>();
	public static void main(String[] args) throws IOException{
		ServerSocket sc = new ServerSocket(8888);
		
		new Thread(new SendInput()).start();
		while(true){			
			Socket socket = sc.accept();
			ReceiveAndSend channel =  new ReceiveAndSend(socket);
			channels.add(channel);
			new Thread(channel).start(); 
		}
		
		
	}
}

class SendInput implements Runnable{	
	public SendInput() {
		// TODO Auto-generated constructor stub
	}

	public void sendMessage(){
		BufferedReader info = null;
		DataOutputStream dos = null;
		try {
			info = new BufferedReader(new InputStreamReader(System.in));
			String msg = info.readLine();
			
			//msg 做的是两件事， 发公告， T人 
			if(msg.indexOf('-')>-1){
				String name = msg.substring(1);
				Iterator<ReceiveAndSend> it = Server.channels.iterator();  
				//避免删除的坑 
				
			    while (it.hasNext()) {  
			    	ReceiveAndSend channel = it.next();  
					if(channel.name.equals(name)){
						it.remove();
					}else{
						try {
							dos = new DataOutputStream(channel.socket.getOutputStream());
							dos.writeUTF(" delete " + name);
						} catch (Exception e) {
							// TODO Auto-generated catch block
							CloseUtil.close(dos);
						}
					}
			    }  
					
			}else{//发送公告了
				for(ReceiveAndSend channel : Server.channels){
						try {
							dos = new DataOutputStream(channel.socket.getOutputStream());
							dos.writeUTF("tell you " + msg);
						} catch (IOException e) {
							// TODO Auto-generated catch block
							CloseUtil.close(dos);
						}
				}
			}
			
		} catch (IOException e) {
			// TODO Auto-generated catch block
			CloseUtil.close(info,dos);
		}
	}
	
	@Override
	public void run(){
		while (true) {
			// TODO Auto-generated method stub
			sendMessage();
		}
	}
}



class ReceiveAndSend implements Runnable{
	Socket socket;
	String name;
	boolean isRun = true;
	public ReceiveAndSend() {
		// TODO Auto-generated constructor stub
	}
	public ReceiveAndSend(Socket socket){
		this.socket = socket;
		DataInputStream dis = null;
		DataOutputStream dos = null;
		try {
			dis = new DataInputStream(socket.getInputStream());
			dos = new DataOutputStream(socket.getOutputStream());
			String name = dis.readUTF();
			this.name = name;
			dos.writeUTF("welcome"+ name);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			CloseUtil.close(dis,dos);
		}

		
		
	}
	
//	public void sendToOther(String msg){
//	Iterator<ReceiveAndSend> it = Server.channels.iterator();
//		while(it.hasNext()){
//			if(it.next() != this){
//				System.out.println("send it!");
//			}
//		}
//	}
	
	public void sendToOther(String msg){
		DataOutputStream dos = null;
		for(ReceiveAndSend channel : Server.channels){
			if(channel != this){
//				System.out.println("send it!");
				try {
					dos = new DataOutputStream(channel.socket.getOutputStream());
					dos.writeUTF(msg);
				} catch (IOException e) {
					// TODO Auto-generated catch block
					isRun = false;
					CloseUtil.close(dos);
					
					//移除自己
					Server.channels.remove(this);
				}
			}
		}
	}	
	@Override
	public void run() {
		// TODO Auto-generated method stub
		DataInputStream dis = null;
		DataOutputStream dos = null;
		try {
	//		BufferedReader br = new BufferedReader(new InputStreamReader(socket.getInputStream()));
	//		buffered老是因为\n的问题
			dis = new DataInputStream(socket.getInputStream());
	//		PrintWriter pw = new PrintWriter(new OutputStreamWriter(socket.getOutputStream()));
			dos = new DataOutputStream(socket.getOutputStream());
			while(isRun){
				String msg = dis.readUTF();
				//判断是不是心跳包 
				if(msg.equals("heart")){
					try {
						dos = new DataOutputStream(socket.getOutputStream());
						dos.writeUTF("heart");
					} catch (IOException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}
				//判断私聊群聊
				if(msg.indexOf('@')>-1){
					String name = msg.substring(1,msg.indexOf(':'));
					String content = msg.substring(msg.indexOf(':')+1);
					for(ReceiveAndSend channel : Server.channels){
						if(channel.name.equals(name)){
							try {
								System.out.println("***********");
								dos = new DataOutputStream(channel.socket.getOutputStream());
								dos.writeUTF(this.name + " say " + content);
							} catch (IOException e) {
								// TODO Auto-generated catch block
								isRun = false;
								CloseUtil.close(dos);
	
							}	
						}
					}
				}else{
					System.out.println("-------------------");
					sendToOther(msg);
				}	
				System.out.println("server received:"+msg);
	//			pw.write("socket had received,this is reply\n");
	//			pw.flush();
				//自己发的消息就不用看了
//				dos = new DataOutputStream(socket.getOutputStream());
//				dos.writeUTF(msg);
			}
		} catch (IOException e) {
			// TODO Auto-generated catch block
			isRun = false;
			CloseUtil.close(dis,dos);
			
			//移除自己
			Server.channels.remove(this);
		}
		
		
	}
}