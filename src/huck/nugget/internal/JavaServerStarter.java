package huck.nugget.internal;

import huck.nugget.ServerEnvironment;
import huck.nugget.config.Config;
import huck.nugget.config.ConfigParser;
import huck.nugget.handler.ConnectionHandler;
import huck.nugget.processor.Processor;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.util.LinkedList;

public class JavaServerStarter {
	private static Config readConfig(String configFileName) throws Exception {
		File configFile = new File(configFileName);
		StringBuffer configSrc = new StringBuffer();
		try(InputStreamReader r = new InputStreamReader(new FileInputStream(configFile), "UTF-8")) {
			char[] buf = new char[10240];
			int readLen;
			while( 0 < (readLen=r.read(buf)) ) {
				configSrc.append(buf, 0, readLen);
			}
		}
		return ConfigParser.parseConfig(configSrc.toString());
	}
	
	private static StopMonitor createStopMonitor(Config config) throws Exception {
		ComponentInstance stopMonitorInst = ComponentLoader.createComponentInstance("_java_server_stop_monitor_.main", config.getChild("server"));
		return (StopMonitor)stopMonitorInst.getComponent();
	}
	
	private static LinkedList<ComponentInstance> createComponentntances(Config config) throws Exception {
		ComponentLoader.registerComponent("_java_server_stop_monitor_", StopMonitor.class);
		ComponentLoader.registerComponent("connector", Connector.class);
		ComponentLoader.registerComponent("thread-pool", ThreadPool.class);
		ComponentLoader.registerComponent("handler", ConnectionHandler.class);
		ComponentLoader.registerComponent("processor", Processor.class);
		
		LinkedList<ComponentInstance> componentList = new LinkedList<>();
		for(String childName : config.getChildNames()) {
			if( "server".equals(childName) ) {
				continue;
			}
			ComponentInstance componentInstance = ComponentLoader.createComponentInstance(childName, config.getChild(childName));
			componentList.add(componentInstance);
		}
		return componentList;
	}
	
	private static void initComponents(ServerEnvironment serverEnv, LinkedList<ComponentInstance> componentList) throws Exception {
		for( ComponentInstance componentInstance : componentList ) {
			componentInstance.initInstance(serverEnv);
		}
	}
	
	private static void runComponents(StopSignal stopSignal, ServerEnvironment serverEnv, LinkedList<ComponentInstance> componentList) {
		for( ComponentInstance componentInstance : componentList ) {
			componentInstance.runInstance(stopSignal, serverEnv);
		}
	}
	
	static class StopCause {
		public String componentName;
		public String msg;
		public Throwable th;
	}
	
	public static void main(String... args) throws Exception {
		String configFileName = "server.config";
		if( 0 < args.length ) {
			configFileName = args[0];
		}
		Config config = readConfig(configFileName);
		StopMonitor stopMonitor = createStopMonitor(config);
		StopSignal stopSignal = stopMonitor.getStopSignal();
		
		LinkedList<ComponentInstance> componentList = createComponentntances(config);
		ServerEnvironment serverEnv = new ServerEnvironment(componentList);
		
		initComponents(serverEnv, componentList);
		runComponents(stopSignal, serverEnv, componentList);
		while( !stopSignal.isOn() ) {
			Thread.sleep(100);
		}
		// TODO: wait threads
		System.exit(0);
	}
}
