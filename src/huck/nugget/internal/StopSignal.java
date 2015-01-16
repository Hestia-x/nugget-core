package huck.nugget.internal;

import java.util.concurrent.atomic.AtomicBoolean;

public class StopSignal {
	private AtomicBoolean signal = new AtomicBoolean(false);
	
	public boolean isOn() {
		return signal.get();
	}
	
	void setSignal() {
		signal.set(true);
	}
}
