package demo;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.SocketChannel;

public class TcpClientChannelSelector extends BaseSocketChannelSelector{
	private static final String HOSTNAME = "127.0.0.1";
	private static final int PORT = 5555;
	private static final int BUFF_SIZE = 255;
	
	private SocketChannel socketChannel;
	private ByteBuffer readBuffer;
	private ByteBuffer writeBuffer;
	private boolean isConnected;
	
	public TcpClientChannelSelector() throws IOException{
		super();
		socketChannel = SocketChannel.open();
		socketChannel.configureBlocking(false);
		socketChannel.connect(new InetSocketAddress(HOSTNAME,PORT));
		readBuffer = ByteBuffer.allocate(BUFF_SIZE);
		writeBuffer = ByteBuffer.allocate(BUFF_SIZE);
		isConnected = false;
		registerChannel(socketChannel,SelectionKey.OP_CONNECT | SelectionKey.OP_READ);
	}
	public boolean isConnected(){
		return isConnected;
	}
	@Override
	protected void doOnSelectionKey(SelectionKey key) {
		// TODO Auto-generated method stub
		if(key.isAcceptable()){
			System.out.println("client is accepted by server");
		}else if(key.isConnectable()){
			SocketChannel channel = (SocketChannel) key.channel();
			if(channel.isConnectionPending()){
				
			}
		}
	}
	
	
}
