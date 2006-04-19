package gov.nih.nci.cagrid.introduce.extension;

import java.net.URL;
import java.net.URLClassLoader;
import java.util.HashMap;
import java.util.Map;


public class ExtensionLayeredClassLoader extends URLClassLoader {
	private Map loadedClasses;


	public ExtensionLayeredClassLoader(URL[] urls) {
		super(urls);
		loadedClasses = new HashMap();
	}


	public Class loadClass(String name) throws ClassNotFoundException {
		Class c = (Class) loadedClasses.get(name);
		if (c == null) {
			System.out.println("Loading in extension class: " + name);
			c = super.loadClass(name);
			if (c == null) {
				c = this.getClass().getClassLoader().loadClass(name);
			}
			if (c == null) {
				throw new ClassNotFoundException("Cannot find class:" + name);
			}
			loadedClasses.put(name, c);
		}
		return c;
	}
}
