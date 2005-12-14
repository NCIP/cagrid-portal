package gov.nih.nci.cagrid.introduce;

import gov.nih.nci.cagrid.common.CommonTools;
import gov.nih.nci.cagrid.introduce.beans.method.MethodType;
import gov.nih.nci.cagrid.introduce.beans.method.MethodsType;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

import org.apache.axis.utils.XMLUtils;
import org.apache.axis.wsdl.symbolTable.SymbolTable;
import org.apache.axis.wsdl.toJava.Emitter;
import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.CommandLineParser;
import org.apache.commons.cli.Option;
import org.apache.commons.cli.Options;
import org.apache.commons.cli.ParseException;
import org.apache.commons.cli.PosixParser;
import org.apache.ws.jaxme.js.JavaMethod;
import org.apache.ws.jaxme.js.JavaSource;
import org.apache.ws.jaxme.js.JavaSourceFactory;
import org.apache.ws.jaxme.js.util.JavaParser;
import org.globus.wsrf.encoding.ObjectDeserializer;

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
public class SyncTools {

	public static final String DIR_OPT = "d";

	public static final String DIR_OPT_FULL = "directory";

	String serviceInterface;

	Properties deploymentProperties;

	List additions;

	List removals;

	JavaSource sourceI;

	JavaSourceFactory jsf;

	JavaParser jp;

	SyncSecurity secureSync;

	File baseDirectory;

	MethodsType methodsType;

	public SyncTools(File baseDirectory) {

		this.baseDirectory = baseDirectory;

		populateMetadata();

		File deploymentPropertiesFile = new File(baseDirectory
				.getAbsolutePath()
				+ File.separator + "introduce.properties");
		try {
			deploymentProperties = new Properties();
			deploymentProperties.load(new FileInputStream(
					deploymentPropertiesFile));

			secureSync = new SyncSecurity(baseDirectory,
					this.deploymentProperties);
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		this.additions = new ArrayList();
		this.removals = new ArrayList();
	}

	private void populateMetadata() {
		InputStream inputStream = null;

		try {
			inputStream = new FileInputStream(this.baseDirectory
					+ File.separator + "introduceMethods.xml");
			org.w3c.dom.Document doc = XMLUtils.newDocument(inputStream);

			this.methodsType = (MethodsType) ObjectDeserializer.toObject(doc
					.getDocumentElement(), MethodsType.class);
		} catch (Exception e) {
			System.err.println("ERROR: problem populating metadata from file: "
					+ e.getMessage());
		} finally {
			if (inputStream != null) {
				try {
					inputStream.close();
				} catch (IOException e) {
				}
			}
		}

	}

	public void sync() throws Exception {
		// create the archive
		long id = System.currentTimeMillis();
		deploymentProperties.setProperty("introduce.skeleton.timestamp", String
				.valueOf(id));
		deploymentProperties.store(new FileOutputStream(baseDirectory
				.getAbsolutePath()
				+ File.separator + "introduce.properties"),
				"Introduce Properties");

		Archive.createArchive(String.valueOf(id), deploymentProperties
				.getProperty("introduce.skeleton.service.name"), baseDirectory
				.getAbsolutePath());

		jsf = new JavaSourceFactory();
		jp = new JavaParser(jsf);

		serviceInterface = baseDirectory.getAbsolutePath()
				+ File.separator
				+ "src"
				+ File.separator
				+ this.deploymentProperties
						.get("introduce.skeleton.package.dir")
				+ "/common/"
				+ this.deploymentProperties
						.get("introduce.skeleton.service.name") + "I.java";

		jp.parse(new File(serviceInterface));
		this.sourceI = (JavaSource) jsf.getJavaSources().next();
		this.sourceI.setForcingFullyQualifiedName(true);

		System.out.println(sourceI.getClassName());

		// check the interface for it's current list of methods
		this.lookForUpdates();

		// sync the gwsdl
		SyncWSDL wsdlSync = new SyncWSDL(baseDirectory,
				this.deploymentProperties);
		wsdlSync.sync(additions, removals);

		String cmd = CommonTools.getAntFlattenCommand(baseDirectory
				.getAbsolutePath());
		Process p = CommonTools.createAndOutputProcess(cmd);
		p.waitFor();
		if (p.exitValue() != 0) {
			throw new Exception("Service flatten wsdl exited abnormally");
		}

		// regenerate stubs and get the symbol table
		Emitter parser = new Emitter();
		SymbolTable table = null;

		parser.setQuiet(true);
		parser.setImports(true);
		parser.setOutputDir(baseDirectory.getAbsolutePath() + File.separator
				+ "tmp");
		parser.setNStoPkg(baseDirectory.getAbsolutePath() + File.separator
				+ "namespace2package.mappings");
		parser.run(new File(baseDirectory.getAbsolutePath()
				+ File.separator
				+ "build"
				+ File.separator
				+ "schema"
				+ File.separator
				+ this.deploymentProperties
						.get("introduce.skeleton.service.name")
				+ File.separator
				+ this.deploymentProperties
						.get("introduce.skeleton.service.name") + "_flattened"
				+ ".wsdl").getAbsolutePath());
		table = parser.getSymbolTable();
		CommonTools.deleteDir(new File(baseDirectory.getAbsolutePath()
				+ File.separator + "tmp"));

		// sync the methods fiels
		SyncMethods methodSync = new SyncMethods(table, baseDirectory,
				this.deploymentProperties);
		// remove methods
		methodSync.removeMethods(this.removals);
		// add new methods
		methodSync.addMethods(this.additions);

		// sync the security config
		// TODO: turning off this until we get a gt4 solution
		// this will need to be refactored to use the bean...
		// List methodsFromDoc =
		// this.methodsDocument.getRootElement().getChildren();
		// secureSync.sync(methodsFromDoc);
	}

	public void lookForUpdates() {

		JavaMethod[] methods = sourceI.getMethods();

		// look at doc and compare to interface
		for (int methodIndex = 0; methodIndex < this.methodsType.getMethod().length; methodIndex++) {
			MethodType mel = this.methodsType.getMethod(methodIndex);
			boolean found = false;
			for (int i = 0; i < methods.length; i++) {
				String methodName = methods[i].getName();
				if (mel.getName().equals(methodName)) {
					found = true;
					break;
				}
			}
			if (!found) {
				System.out.println("Found a method for addition: "
						+ mel.getName());
				this.additions.add(mel);
			}
		}

		// look at interface and compare to doc
		for (int i = 0; i < methods.length; i++) {
			String methodName = methods[i].getName();
			boolean found = false;
			for (int methodIndex = 0; methodIndex < this.methodsType
					.getMethod().length; methodIndex++) {
				MethodType mel = this.methodsType.getMethod(methodIndex);
				if (mel.getName().equals(methodName)) {
					found = true;
					break;
				}
			}
			if (!found) {
				System.out.println("Found a method for removal: " + methodName);
				this.removals.add(methods[i]);
			}
		}
	}

	public static void main(String[] args) {
		Options options = new Options();
		Option directoryOpt = new Option(DIR_OPT, DIR_OPT_FULL, true,
				"The include tool directory");
		options.addOption(directoryOpt);

		CommandLineParser parser = new PosixParser();

		File directory = null;

		try {
			CommandLine line = parser.parse(options, args);
			directory = new File(line.getOptionValue(DIR_OPT));
		} catch (ParseException e) {
			e.printStackTrace();
		}

		SyncTools sync = new SyncTools(directory);
		try {
			sync.sync();
		} catch (Exception e) {
			e.printStackTrace();
			System.exit(1);
		}
	}

}
