package huck.nugget;

import huck.nugget.handler.ConnectionHandler;
import huck.nugget.internal.Component;
import huck.nugget.internal.ComponentInstance;
import huck.nugget.internal.ThreadPool;
import huck.nugget.processor.Processor;

import java.util.LinkedHashMap;
import java.util.LinkedList;

public class ServerEnvironment {
	private LinkedHashMap<String, ComponentInstance> connectionHandlerMap;
	private LinkedHashMap<String, ComponentInstance> processorMap;
	private LinkedHashMap<String, ComponentInstance> threadPoolMap;
	
	public ServerEnvironment(LinkedList<ComponentInstance> componentList) throws Exception {
		connectionHandlerMap = new LinkedHashMap<>();
		processorMap = new LinkedHashMap<>();
		threadPoolMap = new LinkedHashMap<>();
		for( ComponentInstance componentInstance : componentList ) {
			LinkedHashMap<String, ComponentInstance> instMap = null;
			switch( componentInstance.getComponentName() ) {
			case "handler": instMap = connectionHandlerMap; break;
			case "processor": instMap = processorMap; break;			
			case "thread-pool": instMap = threadPoolMap; break;
			}
			if( null == instMap ) {
				continue;
			}
			if( instMap.containsKey(componentInstance.getInstanceName()) ) {
				throw new Exception(componentInstance.getComponentName()+"."+componentInstance.getInstanceName() + " are duplicated");
			}
			instMap.put(componentInstance.getInstanceName(), componentInstance);
		}
	}

	public <C extends ConnectionHandler> C getConnectionHandler(String name, Class<C> connectionHandlerCls) {
		ComponentInstance componentInstance = connectionHandlerMap.get(name);
		if( null == componentInstance ) {
			return null;
		}
		Component component = componentInstance.getComponent();
		return connectionHandlerCls.cast(component);
	}
	
	public <P extends Processor> P getProcessor(String name, Class<P> processorCls) {
		ComponentInstance componentInstance = processorMap.get(name);
		if( null == componentInstance ) {
			return null;
		}
		Component component = componentInstance.getComponent();
		return processorCls.cast(component);
	}
	
	public ThreadPool getThreadPool(String name) {
		ComponentInstance componentInstance = threadPoolMap.get(name);
		if( null == componentInstance ) {
			return null;
		}
		Component component = componentInstance.getComponent();
		return (ThreadPool)component;
	}
}
