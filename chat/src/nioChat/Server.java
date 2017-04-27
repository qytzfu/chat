package nioChat;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.Channel;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.nio.charset.Charset;

public class Server {
	private Selector selector = null;
	static final int port = 30001;
	private Charset charset = Charset.forName("utf-8");
	
	
	public void init() throws IOException{
		selector = Selector.open();
		ServerSocketChannel server = ServerSocketChannel.open();
		server.bind(new InetSocketAddress(port));
		
		server.configureBlocking(false);
		server.register(selector, SelectionKey.OP_ACCEPT);
		
		System.out.println("server is listening now ...");
		
		while(selector.select() > 0){
			for(SelectionKey sk : selector.selectedKeys()){
				selector.selectedKeys().remove(sk);
				if(sk.isAcceptable()){
					SocketChannel sc = server.accept();
					sc.configureBlocking(false);
					sc.register(selector,SelectionKey.OP_READ);
					sk.interestOps(SelectionKey.OP_ACCEPT);
					System.out.println("server is listening from client"+ sc.getRemoteAddress());
				}
				if(sk.isReadable()){
					SocketChannel sc = (SocketChannel)sk.channel();
					ByteBuffer buff = ByteBuffer.allocate(1024);
					StringBuilder content = new StringBuilder();
					
					try {
						while(sc.read(buff) > 0){
							buff.flip();
							content.append(charset.decode(buff));
						}
						System.out.println("server is listening from client"+ sc.getRemoteAddress()+
								"data recv is :"+content);
						sk.interestOps(SelectionKey.OP_READ);
					} catch (IOException e) {
						// TODO Auto-generated catch block
						sk.cancel();
						if(sk.channel() != null){
							sk.channel().close();
						}
					}
					if(content.length() > 0){
						for(SelectionKey key: selector.keys()){
							Channel targetchannel = key.channel();
							if(targetchannel instanceof SocketChannel){
								SocketChannel dest = (SocketChannel)targetchannel;
								dest.write(charset.encode(content.toString()));
							}
						}
					}
				}
			}
		}
	}
	public static void main(String[] args) throws IOException{
		new Server().init();
		
	}
}
