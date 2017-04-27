package nioChat;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.SocketChannel;
import java.nio.charset.Charset;
import java.util.Scanner;

public class Client {
	private Selector selector = null;
	static final int port = 30001;
	private Charset charset = Charset.forName("utf-8");
	private SocketChannel sc = null;
	public void init() throws IOException{
		selector = Selector.open();
		sc = SocketChannel.open(new InetSocketAddress("127.0.0.1",port));
		sc.configureBlocking(false);
		sc.register(selector, SelectionKey.OP_READ);
		new Thread(new ClientThread()).start();
		
		Scanner scan = new Scanner(System.in);
		while(scan.hasNextLine()){
			String line = scan.nextLine();
			sc.write(charset.encode(line));
		}
	}
	
	
	
	public static void main(String[] args) throws IOException{
		new Client().init();
	}
	
	class ClientThread implements Runnable{
		@Override
		public void run() {
			// TODO Auto-generated method stub
			
			try {
				while(selector.select() > 0){
					for(SelectionKey sk : selector.selectedKeys()){
						selector.selectedKeys().remove(sk);
						if(sk.isReadable()){
							SocketChannel sc = (SocketChannel)sk.channel();
							ByteBuffer buff =ByteBuffer.allocate(1024);
							String content = "";
							while(sc.read(buff) > 0){
								buff.flip();
								content += charset.decode(buff);
							}
							System.out.println("message: "+ content);
							sk.interestOps(SelectionKey.OP_READ);
						}
					}
				}
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}
}











































