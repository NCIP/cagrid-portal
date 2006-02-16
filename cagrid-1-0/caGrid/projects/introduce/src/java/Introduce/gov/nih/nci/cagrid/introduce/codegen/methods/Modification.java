package gov.nih.nci.cagrid.introduce.codegen.methods;

import gov.nih.nci.cagrid.introduce.beans.method.MethodType;

import org.apache.ws.jaxme.js.JavaMethod;

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
