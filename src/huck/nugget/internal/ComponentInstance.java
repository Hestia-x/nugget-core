package huck.nugget.internal;

import huck.nugget.ServerEnvironment;
import huck.nugget.config.Config;

public class ComponentInstance {
	private Component component;
	private String componentName;
	private String instanceName;
	private String customClsName;
	private Config config;
	
	ComponentInstance(Component component, String componentName, String instanceName, String customClsName, Config config) {
		this.component = component;
		this.componentName = componentName;
		this.instanceName = instanceName;
		this.customClsName = customClsName;
		this.config = config;
	}
	
	public Component getComponent() {
		return component;
	}
	
	public String getComponentName() {
		return componentName;
	}
	
	public String getInstanceName() {
		return instanceName;
	}
	
	public String getCustomClsName() {
		return customClsName;
	}

	public void initInstance(ServerEnvironment serverEnv) throws Exception {
		component.init(serverEnv, instanceName, config);
	}

	public void runInstance(StopSignal stopSignal, ServerEnvironment serverEnv) {
	}
}
