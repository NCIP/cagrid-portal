/*
 * Created on Jun 9, 2006
 */
package gov.nci.nih.cagrid.tests.core.util;

import java.lang.reflect.Method;
import java.util.ArrayList;

public class ReflectionUtils
{
	public static Method[] getMethodsByName(Class cl, String name)
	{
		Method[] methods = cl.getMethods();
		ArrayList<Method> methodList = new ArrayList<Method>(methods.length);
		for (int i = 0; i < methods.length; i++) {
			if (methods[i].getName().equals(name)) methodList.add(methods[i]);
		}
		return methodList.toArray(new Method[0]);
	}
}
