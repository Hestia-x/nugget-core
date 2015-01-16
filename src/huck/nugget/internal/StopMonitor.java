package huck.nugget.internal;

import huck.nugget.ServerEnvironment;
import huck.nugget.config.Config;

class StopMonitor implements RunningComponent {
	@Override
	public void init(ServerEnvironment serverEnv, String name, Config config) throws Exception {
		
	}

	@Override
	public void destroy() throws Exception {

	}

	@Override
	public void run() throws Exception {
		
	}
	
	public StopSignal getStopSignal() {
		return null;
	}
}
