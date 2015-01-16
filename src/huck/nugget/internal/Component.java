package huck.nugget.internal;

import huck.nugget.ServerEnvironment;
import huck.nugget.config.Config;

public interface Component {
	public void init(ServerEnvironment serverEnv, String name, Config config) throws Exception;
	public void destroy() throws Exception;
}
