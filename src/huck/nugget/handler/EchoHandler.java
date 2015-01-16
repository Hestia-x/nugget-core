package huck.nugget.handler;

import huck.nugget.ServerEnvironment;
import huck.nugget.config.Config;

public class EchoHandler implements ConnectionHandler {

	@Override
	public void init(ServerEnvironment serverEnv, String name, Config config) throws Exception {
	}

	@Override
	public void destroy() throws Exception {
	}
}
