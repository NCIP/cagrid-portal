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
						this.modifications.add(new Modification(mel, methods[i]));
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
