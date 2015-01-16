package huck.nugget.internal;

import huck.nugget.config.Config;
import huck.nugget.util.SingletonGCPreventer;

import java.util.LinkedHashMap;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

class ComponentLoader {
	private static LinkedHashMap<String, Class<? extends Component>> componentMap = new LinkedHashMap<>();
	
	static {
		SingletonGCPreventer.add(new ComponentLoader());		
	}
	
	public static void registerComponent(String componentName, Class<? extends Component> componentCls) {
		componentMap.put(componentName, componentCls);
	}
	
	public static ComponentInstance createComponentInstance(String expression, Config config) throws Exception {
		String[] names = parseExpression(expression);
		String componentName = names[0];
		String instanceName = names[1];
		String customClsName = names[2];
		
		Class<? extends Component> compBaseCls = componentMap.get(componentName);
		if( null == compBaseCls ) {
			throw new Exception("unknown component - " + componentName);
		}
		
		Class<?> instanceCls;
		if( null != customClsName ) {
			Class<?> customCls = Class.forName(customClsName);
			if( !compBaseCls.isAssignableFrom(customCls) ) {
				throw new Exception("invalid class - " + componentName + "." + instanceName);
			}
			instanceCls = customCls;
		} else {
			instanceCls = compBaseCls;
		}
		Component component = (Component)instanceCls.newInstance();
		ComponentInstance componentInstance = new ComponentInstance(component, componentName, instanceName, customClsName, config);
		return componentInstance;
	}

	static String[] parseExpression(String expression) throws Exception {
		Pattern p = Pattern.compile("([^\\.]+)\\.([^\\(]+)(\\(([^\\)]+)\\))?");
		Matcher m = p.matcher(expression);
		if( !m.matches() ) {
			throw new Exception("invalid expression format");
		}
		String componentName = m.group(1);
		String instanceName = m.group(2);
		String customClsName = null;
		if( 3 <= m.groupCount() ) {
			customClsName = m.group(4);
		}
		return new String[]{componentName, instanceName, customClsName};
	}
}

