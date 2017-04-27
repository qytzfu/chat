package demo;

import java.io.IOException;
import java.nio.channels.ClosedChannelException;
import java.nio.channels.SelectableChannel;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.util.Iterator;
import java.util.Set;

public abstract class BaseSocketChannelSelector {
	private Selector selector;
	
	protected BaseSocketChannelSelector() throws IOException{
		// TODO Auto-generated constructor stub
		selector = Selector.open();
	}
	protected void registerChannel(SelectableChannel channel, int ops){
		try {
			channel.configureBlocking(false);
			SelectionKey key = channel.register(selector, ops);
		} catch (ClosedChannelException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	public void select(){
		try {
			while(true){
				//判断selector是否被关闭了
				//为什么重复两次呢
				if(!selector.isOpen()){
					break;
				}
				int readyChannels = selector.select();
				if(readyChannels == 0){
					continue;
				}
				if(!selector.isOpen()){
					break;
				}
				
				Set<SelectionKey> selectedKeys = selector.selectedKeys();
				Iterator<SelectionKey> keyIterator = selectedKeys.iterator();
				while(keyIterator.hasNext()){  
				    SelectionKey key = keyIterator.next();
				    doOnSelectionKey(key);
//				    if (key.isAcceptable()){      // a connection was accepted by a ServerSocketChannel  
//				  
//				    }else   
//				    if (key.isConnectable()){     // a connection was eatablished with a remote server  
//				  
//				    }else  
//				    if (key.isReadable()){        // a channel is ready for reading  
//				  
//				    }else  
//				    if (key.isWritable()){        // a channel is ready for writing  
//				  
//				    }  
//				  
//				    keyIterator.remove();  
				}  
			}
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	protected abstract void doOnSelectionKey(SelectionKey key);
	protected void closeSelector(){
		try {
			selector.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}










































