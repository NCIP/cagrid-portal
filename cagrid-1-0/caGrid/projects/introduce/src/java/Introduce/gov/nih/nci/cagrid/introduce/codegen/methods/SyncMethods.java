package gov.nih.nci.cagrid.introduce.codegen.methods;

import gov.nih.nci.cagrid.introduce.IntroduceConstants;
import gov.nih.nci.cagrid.introduce.beans.method.MethodType;
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

	String serviceInterface;
	List additions;
	List removals;
	List modifications;
	JavaSource sourceI;
	JavaSourceFactory jsf;
	JavaParser jp;


	public SyncMethods(File baseDirectory, ServiceInformation info) {
		super(baseDirectory, info);
		this.additions = new ArrayList();
		this.removals = new ArrayList();
		this.modifications = new ArrayList();
	}


	public void sync() throws SynchronizationException {
		this.lookForUpdates();

		try {
			String cmd = CommonTools.getAntFlattenCommand(getBaseDirectory().getAbsolutePath());
			Process p = CommonTools.createAndOutputProcess(cmd);
			p.waitFor();
			if (p.exitValue() != 0) {
				throw new SynchronizationException("Service flatten wsdl exited abnormally");
			}
		} catch (Exception e) {
			throw new SynchronizationException("Error executing wsdl flatten command:" + e.getMessage(), e);
		}

		// sync the methods fiels

		SyncSource methodSync = new SyncSource(getBaseDirectory(), getServiceInformation());
		// remove methods
		methodSync.removeMethods(this.removals);
		// add new methods
		methodSync.addMethods(this.additions);
		// modify method signatures
		methodSync.modifyMethods(this.modifications);
	}


	public void lookForUpdates() throws SynchronizationException {

		jsf = new JavaSourceFactory();
		jp = new JavaParser(jsf);

		serviceInterface = getBaseDirectory().getAbsolutePath() + File.separator + "src" + File.separator
			+ getServiceInformation().getServiceProperties().get(IntroduceConstants.INTRODUCE_SKELETON_PACKAGE_DIR)
			+ File.separator + "common" + File.separator
			+ getServiceInformation().getServiceProperties().get(IntroduceConstants.INTRODUCE_SKELETON_SERVICE_NAME)
			+ "I.java";

		try {
			jp.parse(new File(serviceInterface));
		} catch (Exception e) {
			throw new SynchronizationException("Error parsing service interface:" + e.getMessage(), e);
		}
		this.sourceI = (JavaSource) jsf.getJavaSources().next();
		this.sourceI.setForcingFullyQualifiedName(true);

		System.out.println(sourceI.getClassName());

		JavaMethod[] methods = sourceI.getMethods();

		// look at doc and compare to interface
		if (getServiceInformation().getMethods().getMethod() != null) {
			for (int methodIndex = 0; methodIndex < getServiceInformation().getMethods().getMethod().length; methodIndex++) {
				MethodType mel = getServiceInformation().getMethods().getMethod(methodIndex);
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
			if (getServiceInformation().getMethods().getMethod() != null) {
				for (int methodIndex = 0; methodIndex < getServiceInformation().getMethods().getMethod().length; methodIndex++) {
					MethodType mel = getServiceInformation().getMethods().getMethod(methodIndex);
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
