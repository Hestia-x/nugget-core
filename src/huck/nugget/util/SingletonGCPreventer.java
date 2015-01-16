package huck.nugget.util;

import java.util.HashSet;

public class SingletonGCPreventer {
	private static HashSet<Object> holdingSet = null;
	
	private SingletonGCPreventer() {}
	
	static {
		init();
	}
	
	private synchronized static void init() {
		if( null != holdingSet ) {
			return;
		}
		holdingSet = new HashSet<>();
		holdingSet.add(new SingletonGCPreventer());
		Thread th = new Thread(new Runnable() {
			@Override
			public void run() {
				while(true) {
					try {
						holdingSet.size();
						Thread.sleep(1000);
					} catch (Exception ignore) {
					}
				}
			}
			;
		}, "SingltonGCPreventer");
		th.setDaemon(true);
		th.start();		
	}
	
	public synchronized static void add(Object obj) {
		holdingSet.add(obj);
	}
}
