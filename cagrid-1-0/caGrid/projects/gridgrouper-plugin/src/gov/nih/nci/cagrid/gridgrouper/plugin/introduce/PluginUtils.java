package gov.nih.nci.cagrid.gridgrouper.plugin.introduce;

import gov.nih.nci.cagrid.introduce.beans.extension.ExtensionType;
import gov.nih.nci.cagrid.introduce.beans.extension.ExtensionTypeExtensionData;
import gov.nih.nci.cagrid.introduce.beans.method.MethodType;
import gov.nih.nci.cagrid.introduce.beans.service.ServiceType;

import javax.xml.namespace.QName;

import org.apache.axis.message.MessageElement;

public class PluginUtils {

	public static final QName NAMESPACE = new QName(
			"http://cagrid.nci.nih.gov/1/gridgrouper-plugin",
			"GridGrouperPlugin");

	public static GridGrouperPlugin resetPlugin(ExtensionType ext) {
		ExtensionTypeExtensionData data = ext.getExtensionData();
		if (data == null) {
			data = new ExtensionTypeExtensionData();
			ext.setExtensionData(data);
		}
		GridGrouperPlugin plugin = null;
		MessageElement[] mes = new MessageElement[1];
		data.set_any(mes);
		plugin = new GridGrouperPlugin();
		mes[0] = new MessageElement(NAMESPACE, plugin);
		return plugin;
	}

	public static GridGrouperPlugin getPlugin(ExtensionType ext)
			throws Exception {
		ExtensionTypeExtensionData data = ext.getExtensionData();
		if (data == null) {
			data = new ExtensionTypeExtensionData();
			ext.setExtensionData(data);
		}
		MessageElement[] mes = data.get_any();
		if (mes == null) {
			mes = new MessageElement[1];
			data.set_any(mes);
		}

		if (mes[0] == null) {
			return null;
		} else {
			if(mes[0].getObjectValue()==null){
			return (GridGrouperPlugin) mes[0].getValueAsType(NAMESPACE,GridGrouperPlugin.class);
			}else{
				return (GridGrouperPlugin) mes[0].getObjectValue();
			}
		}
	}

	public static GridGrouperPlugin getAddPlugin(ExtensionType ext) throws Exception{
		ExtensionTypeExtensionData data = ext.getExtensionData();
		if (data == null) {
			data = new ExtensionTypeExtensionData();
			ext.setExtensionData(data);
		}
		GridGrouperPlugin plugin = null;
		MessageElement[] mes = data.get_any();
		if (mes == null) {
			mes = new MessageElement[1];
			data.set_any(mes);
		}

		if (mes[0] == null) {
			plugin = new GridGrouperPlugin();
			mes[0] = new MessageElement(new QName(
					"http://cagrid.nci.nih.gov/1/gridgrouper-plugin",
					"GridGrouperPlugin"), plugin);
		} else {
			if (mes[0].getObjectValue() == null) {
				plugin =  (GridGrouperPlugin) mes[0].getValueAsType(NAMESPACE,GridGrouperPlugin.class);
			} else {
				plugin = (GridGrouperPlugin) mes[0].getObjectValue();
			}
		}
		return plugin;
	}

	public static ProtectedService getProtectedService(
			GridGrouperPlugin plugin, ServiceType st) {
		ProtectedService[] services = plugin.getProtectedService();
		if (services != null) {
			for (int i = 0; i < services.length; i++) {
				if (services[i].getServiceName().equals(st.getName())) {
					return services[i];
				}
			}
		}
		return null;
	}

	public static ProtectedService getAddProtectedService(
			GridGrouperPlugin plugin, ServiceType st) {
		ProtectedService service = getProtectedService(plugin, st);
		if (service != null) {
			return service;
		} else {
			service = new ProtectedService();
			service.setServiceName(st.getName());
			ProtectedService[] services = plugin.getProtectedService();
			ProtectedService[] newservices = null;
			if (services == null) {
				newservices = new ProtectedService[1];
				newservices[0] = service;
			} else {
				newservices = new ProtectedService[services.length + 1];
				System.arraycopy(services, 0, newservices, 0, services.length);
				newservices[services.length] = service;
			}
			plugin.setProtectedService(newservices);
			return service;
		}
	}

	public static MethodMembershipExpression getMethodMembershipExpression(ProtectedService ps, MethodType mt) {
			MethodMembershipExpression[] methods = ps
					.getMethodMembershipExpression();
			if (methods != null) {
				for (int i = 0; i < methods.length; i++) {
					if (methods[i].getMethodName().equals(mt.getName())) {
						return methods[i];
					}
				}
			}
		return null;
	}
	
	public static MethodMembershipExpression getAddMethodMembershipExpression(ProtectedService ps, MethodType mt) {
		MethodMembershipExpression method = getMethodMembershipExpression(ps,mt);
		if (method != null) {
			return method;
		} else {
			method = new MethodMembershipExpression();
			method.setMethodName(mt.getName());
			MethodMembershipExpression[] methods = ps.getMethodMembershipExpression();
			MethodMembershipExpression[] newmethods = null;
			if (methods == null) {
				newmethods = new MethodMembershipExpression[1];
				newmethods[0] = method;
			} else {
				newmethods = new MethodMembershipExpression[methods.length + 1];
				System.arraycopy(methods, 0, newmethods, 0, methods.length);
				newmethods[methods.length] = method;
			}
			ps.setMethodMembershipExpression(newmethods);
			return method;
		}
	}

}
