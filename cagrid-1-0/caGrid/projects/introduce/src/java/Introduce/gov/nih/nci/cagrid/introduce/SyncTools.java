package gov.nih.nci.cagrid.introduce;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

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
import org.apache.ws.jaxme.js.Parameter;
import org.apache.ws.jaxme.js.util.JavaParser;
import org.jdom.Document;
import org.jdom.Element;
import org.jdom.JDOMException;
import org.jdom.input.SAXBuilder;

import antlr.RecognitionException;
import antlr.TokenStreamException;


/**
 * SyncMethodsOnDeployment TODO:DOCUMENT ME
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

	Document methodsDocument;

	List additions;

	List removals;

	JavaSource sourceI;

	JavaSourceFactory jsf;

	JavaParser jp;

	SyncSecurity secureSync;

	File baseDirectory;


	public SyncTools(File baseDirectory) {

		this.baseDirectory = baseDirectory;

		File deploymentPropertiesFile = new File(baseDirectory.getAbsolutePath() + File.separator
			+ "introduce.properties");
		SAXBuilder builder = new SAXBuilder(false);
		try {
			methodsDocument = builder.build(baseDirectory.getAbsolutePath() + File.separator + "introduceMethods.xml");
		} catch (JDOMException e1) {
			e1.printStackTrace();
		} catch (IOException e1) {
			e1.printStackTrace();
		}

		try {
			deploymentProperties = new Properties();
			deploymentProperties.load(new FileInputStream(deploymentPropertiesFile));

			secureSync = new SyncSecurity(baseDirectory, this.deploymentProperties);
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		this.additions = new ArrayList();
		this.removals = new ArrayList();
	}


	public void sync() {
		jsf = new JavaSourceFactory();
		jp = new JavaParser(jsf);

		serviceInterface = baseDirectory.getAbsolutePath() + File.separator + "src" + File.separator
			+ this.deploymentProperties.get("introduce.skeleton.package.dir") + "/common/"
			+ this.deploymentProperties.get("introduce.skeleton.service.name") + "I.java";

		try {
			jp.parse(new File(serviceInterface));
			this.sourceI = (JavaSource) jsf.getJavaSources().next();
			this.sourceI.setForcingFullyQualifiedName(true);

		} catch (RecognitionException e) {
			e.printStackTrace();
		} catch (TokenStreamException e) {
			e.printStackTrace();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}

		System.out.println(sourceI.getClassName());

		// check the interface for it's current list of methods
		this.lookForUpdates();

		// sync the gwsdl
		SyncWSDL wsdlSync = new SyncWSDL(baseDirectory, this.deploymentProperties);
		wsdlSync.sync(additions, removals);

		try {
			String cmd = CommonTools.getAntFlattenCommand(baseDirectory.getAbsolutePath());
			Process p = CommonTools.createAndOutputProcess(cmd);
			p.waitFor();
			if (p.exitValue() != 0) {
				throw new Exception("Service flatten wsdl exited abnormally");
			}
		} catch (Exception e1) {
			e1.printStackTrace();
			System.exit(1);
		}

		// regenerate stubs and get the symbol table
		Emitter parser = new Emitter();
		SymbolTable table = null;
		try {
			parser.setQuiet(true);
			parser.setImports(true);
			parser.setNStoPkg(baseDirectory.getAbsolutePath() + File.separator + "namespace2package.mappings");
			parser.run(new File(baseDirectory.getAbsolutePath() + File.separator + "build" + File.separator + "schema"
				+ File.separator + this.deploymentProperties.get("introduce.skeleton.service.name") + File.separator
				+ this.deploymentProperties.get("introduce.skeleton.service.name") + "_flattened" + ".wsdl")
				.getAbsolutePath());
			table = parser.getSymbolTable();

		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		// sync the methods fiels
		SyncMethods methodSync = new SyncMethods(table, baseDirectory, this.deploymentProperties);
		// remove methods
		methodSync.removeMethods(this.removals);
		// add new methods
		methodSync.addMethods(this.additions);

		// sync the security config
		// TODO: turning off this until we get a gt4 solution
		// List methodsFromDoc =
		// this.methodsDocument.getRootElement().getChildren();
		// secureSync.sync(methodsFromDoc);
	}


	public void lookForUpdates() {

		JavaMethod[] methods = sourceI.getMethods();
		List methodsFromDoc = this.methodsDocument.getRootElement().getChildren();

		// look at doc and compare to interface
		for (int methodIndex = 0; methodIndex < methodsFromDoc.size(); methodIndex++) {
			Element mel = (Element) methodsFromDoc.get(methodIndex);
			boolean found = false;
			for (int i = 0; i < methods.length; i++) {
				String methodName = methods[i].getName();
				if (mel.getAttributeValue("name").equals(methodName)) {
					List inputParamEls = mel.getChild("inputs", this.methodsDocument.getRootElement().getNamespace())
						.getChildren();
					Parameter[] classes = methods[i].getParams();
					boolean paramsOk = true;
					if (inputParamEls.size() == classes.length) {
						for (int paramIndex = 0; paramIndex < inputParamEls.size(); paramIndex++) {
							Element param = (Element) inputParamEls.get(paramIndex);
							String classTypeString = "";
							if (classes[paramIndex].getType().getPackageName().length() > 0) {
								classTypeString += classes[paramIndex].getType().getPackageName() + ".";
							}
							classTypeString += classes[paramIndex].getType().getClassName();
							if (classes[paramIndex].getType().isArray()) {
								classTypeString += "[]";
							}
							if (!param.getAttributeValue("className").equals(classTypeString)) {
								paramsOk = false;
							}
						}
					} else {
						paramsOk = false;
					}
					boolean returnOk = true;
					Element returnTypeEl = mel.getChild("output", this.methodsDocument.getRootElement().getNamespace());
					String returnClass = "";
					if (methods[i].getType().getPackageName().length() > 0) {
						returnClass += methods[i].getType().getPackageName() + ".";
					}
					returnClass += methods[i].getType().getClassName();
					if (methods[i].getType().isArray()) {
						returnClass += "[]";
					}
					if (!returnTypeEl.getAttributeValue("className").equals(returnClass)) {
						returnOk = false;
					}
					if (paramsOk && returnOk) {
						found = true;
						break;
					}
				}
			}
			if (!found) {
				System.out.println("Found a method for addition: " + mel.getAttributeValue("name"));
				this.additions.add(mel);
			}
		}

		// look at interface and compare to doc
		for (int i = 0; i < methods.length; i++) {
			String methodName = methods[i].getName();
			boolean found = false;
			for (int methodIndex = 0; methodIndex < methodsFromDoc.size(); methodIndex++) {
				Element mel = (Element) methodsFromDoc.get(methodIndex);
				if (mel.getAttributeValue("name").equals(methodName)) {
					List inputParamEls = mel.getChild("inputs", this.methodsDocument.getRootElement().getNamespace())
						.getChildren();
					Parameter[] classes = methods[i].getParams();
					boolean paramsOk = true;
					if (inputParamEls.size() == classes.length) {
						for (int paramIndex = 0; paramIndex < inputParamEls.size(); paramIndex++) {
							Element param = (Element) inputParamEls.get(paramIndex);
							String classTypeString = "";
							if (classes[paramIndex].getType().getPackageName().length() > 0) {
								classTypeString += classes[paramIndex].getType().getPackageName() + ".";
							}
							classTypeString += classes[paramIndex].getType().getClassName();
							if (classes[paramIndex].getType().isArray()) {
								classTypeString += "[]";
							}
							if (!param.getAttributeValue("className").equals(classTypeString)) {
								paramsOk = false;
							}
						}
					} else {
						paramsOk = false;
					}
					boolean returnOk = true;
					Element returnTypeEl = mel.getChild("output", this.methodsDocument.getRootElement().getNamespace());
					String returnClass = "";
					if (methods[i].getType().getPackageName().length() > 0) {
						returnClass += methods[i].getType().getPackageName() + ".";
					}
					returnClass += methods[i].getType().getClassName();
					if (methods[i].getType().isArray()) {
						returnClass += "[]";
					}
					if (!returnTypeEl.getAttributeValue("className").equals(returnClass)) {
						returnOk = false;
					}
					if (paramsOk && returnOk) {
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


	public static void main(String[] args) {
		Options options = new Options();
		Option directoryOpt = new Option(DIR_OPT, DIR_OPT_FULL, true, "The include tool directory");
		options.addOption(directoryOpt);

		CommandLineParser parser = new PosixParser();

		File directory = null;
		Document methodsDocument = null;

		try {
			CommandLine line = parser.parse(options, args);
			directory = new File(line.getOptionValue(DIR_OPT));
		} catch (ParseException e) {
			e.printStackTrace();
		}

		SyncTools sync = new SyncTools(directory);
		sync.sync();
	}

}
