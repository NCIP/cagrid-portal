package gov.nih.nci.cagrid.introduce.codegen.services.methods;

import gov.nih.nci.cagrid.introduce.IntroduceConstants;
import gov.nih.nci.cagrid.introduce.beans.method.MethodType;
import gov.nih.nci.cagrid.introduce.beans.service.ServiceType;
import gov.nih.nci.cagrid.introduce.codegen.common.SyncTool;
import gov.nih.nci.cagrid.introduce.codegen.common.SynchronizationException;
import gov.nih.nci.cagrid.introduce.common.CommonTools;
import gov.nih.nci.cagrid.introduce.info.ServiceInformation;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import org.apache.ws.jaxme.js.JavaMethod;
import org.apache.ws.jaxme.js.JavaSource;
import org.apache.ws.jaxme.js.JavaSourceFactory;
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
public class SyncMethods extends SyncTool {

	private String serviceInterface;
	private List additions;
	private List removals;
	private List modifications;
	private ServiceType service;


	public SyncMethods(File baseDirectory, ServiceInformation info, ServiceType service) {
		super(baseDirectory, info);
		this.service = service;
		this.additions = new ArrayList();
		this.removals = new ArrayList();
		this.modifications = new ArrayList();
	}


	public void sync() throws SynchronizationException {
		this.lookForUpdates();

		// sync the methods fiels

		try {
			SyncSource methodSync = new SyncSource(getBaseDirectory(), getServiceInformation(), service);
			// remove methods
			methodSync.removeMethods(this.removals);
			// add new methods
			methodSync.addMethods(this.additions);
			// modify method signatures
			methodSync.modifyMethods(this.modifications);
		} catch (Exception e) {
			throw new SynchronizationException(e.getMessage(), e);
		}
	}


	public void lookForUpdates() throws SynchronizationException {
		JavaSource sourceI;
		JavaSourceFactory jsf;
		JavaParser jp;

		jsf = new JavaSourceFactory();
		jp = new JavaParser(jsf);

		serviceInterface = getBaseDirectory().getAbsolutePath() + File.separator + "src" + File.separator
			+ CommonTools.getPackageDir(service) + File.separator + "common" + File.separator + service.getName() + "I.java";

		try {
			jp.parse(new File(serviceInterface));
		} catch (Exception e) {
			throw new SynchronizationException("Error parsing service interface:" + e.getMessage(), e);
		}
		sourceI = (JavaSource) jsf.getJavaSources().next();
		sourceI.setForcingFullyQualifiedName(true);

		System.out.println(sourceI.getClassName());

		JavaMethod[] methods = sourceI.getMethods();

		// look at doc and compare to interface
		if (service.getMethods() != null && service.getMethods().getMethod() != null) {
			for (int methodIndex = 0; methodIndex < service.getMethods().getMethod().length; methodIndex++) {
				MethodType mel = service.getMethods().getMethod(methodIndex);
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
			if (service.getMethods().getMethod() != null) {
				for (int methodIndex = 0; methodIndex < service.getMethods().getMethod().length; methodIndex++) {
					MethodType mel = service.getMethods().getMethod(methodIndex);
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
