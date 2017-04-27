package ioChat;

import java.io.Closeable;
import java.io.IOException;

public class CloseUtil {
	public static void close(Closeable...closeables){
		for(Closeable closeOne : closeables){
			try {
				if(closeOne != null){
					closeOne.close();
				}
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}
}
