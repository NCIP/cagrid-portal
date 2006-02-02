package gov.nih.nci.cagrid.introduce.codegen.methods;

import gov.nih.nci.cagrid.common.CommonTools;
import gov.nih.nci.cagrid.introduce.ServiceInformation;
import gov.nih.nci.cagrid.introduce.beans.method.MethodType;
import gov.nih.nci.cagrid.introduce.beans.method.MethodTypeExceptionsException;
import gov.nih.nci.cagrid.introduce.beans.method.MethodTypeInputsInput;
import gov.nih.nci.cagrid.introduce.beans.method.MethodTypeOutput;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import org.apache.ws.jaxme.js.JavaMethod;
import org.apache.ws.jaxme.js.JavaSource;
import org.apache.ws.jaxme.js.JavaSourceFactory;
import org.apache.ws.jaxme.js.Parameter;
import org.apache.ws.jaxme.js.util.JavaParser;


/**
 * SyncMethodsOnDeployment
 * 
 * @author <A HREF="MAILTO:hastings@bmi.osu.edu">Shannon Hastings </A>
 * @author <A HREF="MAILTO:oster@bmi.osu.edu">Scott Oster </A>
 * @author <A HREF="MAILTO:langella@bmi.osu.edu">Stephen Langella </A>
 * @created Jun 8, 2005
 * @version $Id: mobiusEclipseCodeTemplates.xml,v 1.2 2005/04/19 14:58:02 oster
 *          Exp $
 */
public class SyncMethods {

	String serviceInterface;

	List additions;

	List removals;

	List modifications;

	JavaSource sourceI;

	JavaSourceFactory jsf;

	JavaParser jp;

	SyncSecurity secureSync;

	File baseDirectory;

	ServiceInformation info;


	public SyncMethods(File baseDirectory, ServiceInformation info) {

		this.baseDirectory = baseDirectory;

		this.info = info;

		this.additions = new ArrayList();
		this.removals = new ArrayList();
		this.modifications = new ArrayList();
	}


	public void sync() throws Exception {
		this.lookForUpdates();

		String cmd = CommonTools.getAntFlattenCommand(baseDirectory.getAbsolutePath());
		Process p = CommonTools.createAndOutputProcess(cmd);
		p.waitFor();
		if (p.exitValue() != 0) {
			throw new Exception("Service flatten wsdl exited abnormally");
		}

		// sync the methods fiels

		SyncSource methodSync = new SyncSource(baseDirectory, this.info);
		// remove methods
		methodSync.removeMethods(this.removals);
		// add new methods
		methodSync.addMethods(this.additions);
		// modify method signatures
		methodSync.modifyMethods(this.modifications);
	}


	public void lookForUpdates() throws Exception {

		jsf = new JavaSourceFactory();
		jp = new JavaParser(jsf);

		serviceInterface = baseDirectory.getAbsolutePath() + File.separator + "src" + File.separator
			+ this.info.getServiceProperties().get("introduce.skeleton.package.dir") + "/common/"
			+ this.info.getServiceProperties().get("introduce.skeleton.service.name") + "I.java";

		jp.parse(new File(serviceInterface));
		this.sourceI = (JavaSource) jsf.getJavaSources().next();
		this.sourceI.setForcingFullyQualifiedName(true);

		System.out.println(sourceI.getClassName());

		JavaMethod[] methods = sourceI.getMethods();

		// look at doc and compare to interface
		if (info.getMethods().getMethod() != null) {
			for (int methodIndex = 0; methodIndex < this.info.getMethods().getMethod().length; methodIndex++) {
				MethodType mel = this.info.getMethods().getMethod(methodIndex);
				boolean found = false;
				for (int i = 0; i < methods.length; i++) {
					String methodName = methods[i].getName();
					if (mel.getName().equals(methodName)) {
						found = true;

						// SLH don't break until you check for modifications,
						// check more thoroughly to determine if it is a mod
						// if it is a mod we will handle that differently

						// first check for a parameter difference....
						if (mel.getInputs() != null && mel.getInputs().getInput() != null) {
							// check for same number of params
							MethodTypeInputsInput[] inputs = mel.getInputs().getInput();
							if (inputs.length != methods[i].getParams().length) {
								// params lengths are not the same
								// add to mods and break
								System.out.println("Found a method for modification: " + mel.getName());
								this.modifications.add(mel);
							}
							for (int inputI = 0; inputI < inputs.length; inputI++) {
								MethodTypeInputsInput input = inputs[inputI];
								Parameter param = methods[i].getParams()[inputI];
								// now compare the input
								if (input.getType().equals(param.getType())) {
									// not the same type or order so need to add
									if (!this.modifications.contains(mel)) {
										System.out.println("Found a method for modification: " + mel.getName());
										this.modifications.add(mel);
									}
								}

							}
						}

						// now check the outputType.....
						if (mel.getOutput() != null) {
							// now compare the output to
							MethodTypeOutput output = mel.getOutput();
							if (output.getType() != null && methods[i].getType() != null) {
								if (output.getType().equals(methods[i].getType())) {
									// they are differnt so add this to mods
									// list
									if (!this.modifications.contains(mel)) {
										System.out.println("Found a method for modification: " + mel.getName());
										this.modifications.add(mel);
									}
								}
							}
						}

						// now check the faults
						if (mel.getExceptions() != null && mel.getExceptions().getException() != null) {
							MethodTypeExceptionsException[] exceptions = mel.getExceptions().getException();
							if (exceptions.length != methods[i].getExceptions().length-1) {
								if (!this.modifications.contains(mel)) {
									System.out.println("Found a method for modification: " + mel.getName());
									this.modifications.add(mel);
								}
								for (int exceptionI = 0; exceptionI < exceptions.length; exceptionI++) {
									if (exceptions[exceptionI].getName().equals(
										methods[i].getExceptions()[exceptionI].getClassName())) {
										System.out.println("Found a method for modification: " + mel.getName());
										this.modifications.add(mel);
									}
								}
							}
						}

						break;
					}
				}
				if (!found) {
					System.out.println("Found a method for addition: " + mel.getName());
					this.additions.add(mel);
				}
			}
		}

		// look at interface and compare to doc
		for (int i = 0; i < methods.length; i++) {
			String methodName = methods[i].getName();
			boolean found = false;
			if (info.getMethods().getMethod() != null) {
				for (int methodIndex = 0; methodIndex < this.info.getMethods().getMethod().length; methodIndex++) {
					MethodType mel = this.info.getMethods().getMethod(methodIndex);
					if (mel.getName().equals(methodName)) {
						found = true;
						break;
					}
				}
			}
			if (!found) {
				System.out.println("Found a method for removal: " + methodName);
				this.removals.add(methods[i]);
			}
		}
	}

}
