package gov.nih.nci.cagrid.introduce.codegen.services.methods;

import gov.nih.nci.cagrid.introduce.beans.method.MethodType;

import org.apache.ws.jaxme.js.JavaMethod;


/**
 * Holds a modified method object and it's coresponding old
 * JavaMethod object
 * 
 * @author <A HREF="MAILTO:hastings@bmi.osu.edu">Shannon Hastings </A>
 * @author <A HREF="MAILTO:oster@bmi.osu.edu">Scott Oster </A>
 * @author <A HREF="MAILTO:langella@bmi.osu.edu">Stephen Langella </A>
 * @created Jun 8, 2005
 * @version $Id: mobiusEclipseCodeTemplates.xml,v 1.2 2005/04/19 14:58:02 oster
 *          Exp $
 */
public class Modification {
	
	private MethodType methodType;
	private JavaMethod javaMethod;

	public Modification(MethodType methodType, JavaMethod javaMethod) {
		this.methodType = methodType;
		this.javaMethod = javaMethod;
	}

	public JavaMethod getJavaMethod() {
		return javaMethod;
	}

	public void setJavaMethod(JavaMethod javaMethod) {
		this.javaMethod = javaMethod;
	}

	public MethodType getMethodType() {
		return methodType;
	}

	public void setMethodType(MethodType methodType) {
		this.methodType = methodType;
	}

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		// TODO Auto-generated method stub

	}

}
