package tmp;

import java.util.Timer;
import java.util.TimerTask;

public class Test {
	public static void main(String[] args) {
//		Test t = new Test();
//		t.execute();
		long x = System.currentTimeMillis();
		try {
			Thread.sleep(3000);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		System.out.print(System.currentTimeMillis()- x);
	}
	
	public void execute(){
		Timer timer = new Timer();
		timer.schedule(new Task(), 2000,1000);		
	}
	
	
	
	class Task extends TimerTask {
		public void run(){
			System.out.println("this is my task");
		}
	}
}
