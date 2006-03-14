package gov.nih.nci.cagrid.introduce.codegen;

import gov.nih.nci.cagrid.common.Utils;
import gov.nih.nci.cagrid.introduce.IntroduceConstants;
import gov.nih.nci.cagrid.introduce.ResourceManager;
import gov.nih.nci.cagrid.introduce.ServiceInformation;
import gov.nih.nci.cagrid.introduce.beans.ServiceDescription;
import gov.nih.nci.cagrid.introduce.beans.metadata.MetadataType;
import gov.nih.nci.cagrid.introduce.beans.method.MethodType;
import gov.nih.nci.cagrid.introduce.beans.method.MethodTypeInputsInput;
import gov.nih.nci.cagrid.introduce.beans.method.MethodTypeOutput;
import gov.nih.nci.cagrid.introduce.codegen.metadata.SyncMetadata;
import gov.nih.nci.cagrid.introduce.codegen.methods.SyncMethods;
import gov.nih.nci.cagrid.introduce.codegen.security.SyncSecurity;
import gov.nih.nci.cagrid.introduce.common.CommonTools;
import gov.nih.nci.cagrid.introduce.templates.NamespaceMappingsTemplate;
import gov.nih.nci.cagrid.introduce.templates.schema.service.ServiceWSDLTemplate;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Properties;
import java.util.Set;

import javax.xml.namespace.QName;

import org.apache.axis.wsdl.symbolTable.Element;
import org.apache.axis.wsdl.symbolTable.SymbolTable;
import org.apache.axis.wsdl.symbolTable.Type;
import org.apache.axis.wsdl.toJava.Emitter;
import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.CommandLineParser;
import org.apache.commons.cli.Option;
import org.apache.commons.cli.Options;
import org.apache.commons.cli.ParseException;
import org.apache.commons.cli.PosixParser;
import org.projectmobius.common.MalformedNamespaceException;
import org.projectmobius.common.Namespace;


/**
 * Top level controller for re-syncing the service.
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

	public File baseDirectory;


	public SyncTools(File directory) {
		this.baseDirectory = directory;
	}


	private String getRelativeClassName(String fullyQualifiedClassName) {
		int index = fullyQualifiedClassName.lastIndexOf(".");
		if (index >= 0) {
			return fullyQualifiedClassName.substring(index + 1);
		} else {
			return fullyQualifiedClassName;
		}
	}


	public void sync() throws Exception {
		// STEP 1: populate the object model representation of the service
		ServiceDescription introService = (ServiceDescription) Utils.deserializeDocument(baseDirectory + File.separator
			+ "introduce.xml", ServiceDescription.class);
		if (introService.getIntroduceVersion() == null
			|| !introService.getIntroduceVersion().equals(IntroduceConstants.INTRODUCE_VERSION)) {
			throw new Exception("Introduce version in project does not match version provided by Introduce Toolkit ( "
				+ IntroduceConstants.INTRODUCE_VERSION + " ): " + introService.getIntroduceVersion());
		}
		File servicePropertiesFile = new File(baseDirectory.getAbsolutePath() + File.separator + "introduce.properties");
		Properties serviceProperties = new Properties();
		serviceProperties.load(new FileInputStream(servicePropertiesFile));
		ServiceInformation info = new ServiceInformation(introService, serviceProperties, baseDirectory);
		File schemaDir = new File(baseDirectory.getAbsolutePath() + File.separator + "schema");

		// STEP 2: make a backup of the service implementation
		this.createArchive(info);

		// STEP 3: generate a set of namespaces to not make classes/stubs for as
		// the user specified them explicitly, then save them to the build
		// properties
		Set excludeSet = generateNamespaceExcludesSet(info);
		String excludeLine = "";
		for (Iterator iter = excludeSet.iterator(); iter.hasNext();) {
			String namespace = (String) iter.next();
			excludeLine += " -x " + namespace;
		}
		serviceProperties.setProperty("introduce.ns.excludes", excludeLine);
		serviceProperties.store(new FileOutputStream(servicePropertiesFile), "Introduce Properties");

		// STEP 4: write out namespace mappings and flatten the wsdl file
		flattenWSDL(info, schemaDir);

		// STEP 5: run axis to get the symbol table
		SymbolTable table = generateSymbolTable(info, excludeSet);

		// STEP 6: fill out the object model with the generated classnames where
		// the user didn't specify them explicitly
		populateClassnames(info, table);

		// STEP 7: run the code generation tools
		SyncMethods methodsS = new SyncMethods(baseDirectory, info);
		SyncMetadata metadata = new SyncMetadata(baseDirectory, info);
		SyncSecurity security = new SyncSecurity(baseDirectory, info);

		System.out.println("Synchronizing the methods");
		methodsS.sync();
		System.out.println("Synchronizing the metadata");
		metadata.sync();
		System.out.println("Synchronizing the security");
		security.sync();
	}


	private void populateClassnames(ServiceInformation info, SymbolTable table) throws MalformedNamespaceException {

		table.dump(System.out);

		// get the classnames from the axis symbol table
		if (info.getMetadata().getMetadata() != null) {
			for (int i = 0; i < info.getMetadata().getMetadata().length; i++) {
				MetadataType mtype = info.getMetadata().getMetadata(i);
				if (mtype.getNamespace() != null
					&& (mtype.getPackageName() == null || mtype.getPackageName().length() <= 0)) {
					mtype.setPackageName(CommonTools.getPackageName(new Namespace(mtype.getNamespace())));
				}
				if (mtype.getClassName() == null || mtype.getClassName().length() == 0) {
					Element element = table.getElement(new QName(mtype.getNamespace(), mtype.getType()));
					mtype.setClassName(getRelativeClassName(element.getName()));
				}
			}
		}

		// get the classnames from the axis symbol table
		if (info.getMethods().getMethod() != null) {
			for (int i = 0; i < info.getMethods().getMethod().length; i++) {
				MethodType mtype = info.getMethods().getMethod(i);
				// process the inputs
				if (mtype.getInputs() != null && mtype.getInputs().getInput() != null) {
					for (int j = 0; j < mtype.getInputs().getInput().length; j++) {
						MethodTypeInputsInput inputParam = mtype.getInputs().getInput(j);
						if (inputParam.getNamespace() != null
							&& !inputParam.getNamespace().equals(IntroduceConstants.W3CNAMESPACE)
							&& (inputParam.getPackageName() == null || inputParam.getPackageName().length() <= 0)) {
							inputParam.setPackageName(CommonTools.getPackageName(new Namespace(inputParam
								.getNamespace())));
						}

						if (inputParam.getClassName() == null) {
							if (inputParam.getNamespace().equals(IntroduceConstants.W3CNAMESPACE)) {
								Type type = table.getType(new QName(inputParam.getNamespace(), inputParam.getType()));
								inputParam.setClassName(getRelativeClassName(type.getName()));
							} else {
								Element element = table.getElement(new QName(inputParam.getNamespace(), inputParam
									.getType()));
								inputParam.setClassName(getRelativeClassName(element.getName()));
								Type type = table.getType(new QName(info.getServiceProperties().getProperty(
									"introduce.skeleton.namespace.domain")
									+ "/" + info.getServiceProperties().getProperty("introduce.skeleton.service.name"),
									">>" + mtype.getName() + ">" + inputParam.getName()));
								inputParam.setContainerClassName(info.getServiceProperties().getProperty(
									"introduce.skeleton.package")
									+ ".stubs." + getRelativeClassName(type.getName()));
							}
						}
					}
				}

				// process the outputs
				if (mtype.getOutput() != null) {
					MethodTypeOutput outputParam = mtype.getOutput();
					if (outputParam.getNamespace() != null
						&& !outputParam.getNamespace().equals(IntroduceConstants.W3CNAMESPACE)
						&& (outputParam.getPackageName() == null || outputParam.getPackageName().length() <= 0)) {
						outputParam.setPackageName(CommonTools
							.getPackageName(new Namespace(outputParam.getNamespace())));
					}
					if (outputParam.getClassName() != null && outputParam.getClassName().equals("void")) {
						outputParam.setPackageName("");
					} else if (outputParam.getClassName() == null) {
						if (outputParam.getNamespace().equals(IntroduceConstants.W3CNAMESPACE)) {
							Type type = table.getType(new QName(outputParam.getNamespace(), outputParam.getType()));
							outputParam.setClassName(getRelativeClassName(type.getName()));
						} else {
							Element element = table.getElement(new QName(outputParam.getNamespace(), outputParam
								.getType()));
							outputParam.setClassName(getRelativeClassName(element.getName()));
						}
					}
				}
			}
		}
	}


	/**
	 * Walk the model and build up a set of namespaces to not generate classes
	 * for (from schemas in wsdl) NOTE: must be called BEFORE populateClassnames
	 * TODO: we may handle this differently in the future if we have to add
	 * specifications for custom serialization/deserialization.
	 * 
	 * @param info
	 * @throws MalformedNamespaceException
	 */
	private Set generateNamespaceExcludesSet(ServiceInformation info) throws Exception {
		Set excludeSet = new HashSet();
		File schemaDir = new File(baseDirectory.getAbsolutePath() + File.separator + "schema" + File.separator
			+ info.getServiceProperties().getProperty("introduce.skeleton.service.name"));
		// exclude namespaces that have FQN for metadata class
		if (info.getMetadata().getMetadata() != null) {
			for (int i = 0; i < info.getMetadata().getMetadata().length; i++) {
				MetadataType mtype = info.getMetadata().getMetadata(i);
				if (mtype.getClassName() != null) {
					if (mtype.getLocation() != null) {
						excludeSet.add(mtype.getNamespace());
						TemplateUtils.walkSchemasGetNamespaces(schemaDir, schemaDir + File.separator
							+ mtype.getLocation(), excludeSet);
					}
				}
			}
		}

		// exclude namespaces that have FQN for method output or input
		if (info.getMethods().getMethod() != null) {
			for (int i = 0; i < info.getMethods().getMethod().length; i++) {
				MethodType mtype = info.getMethods().getMethod(i);
				// process the inputs
				if (mtype.getInputs() != null && mtype.getInputs().getInput() != null) {
					for (int j = 0; j < mtype.getInputs().getInput().length; j++) {
						MethodTypeInputsInput inputParam = mtype.getInputs().getInput(j);
						if (inputParam.getClassName() != null) {
							if (inputParam.getLocation() != null) {
								excludeSet.add(inputParam.getNamespace());
								TemplateUtils.walkSchemasGetNamespaces(schemaDir, schemaDir + File.separator
									+ inputParam.getLocation(), excludeSet);
							}
						}
					}
				}

				// process the outputs
				if (mtype.getOutput() != null) {
					MethodTypeOutput outputParam = mtype.getOutput();
					if (outputParam.getClassName() != null) {
						if (outputParam.getLocation() != null) {
							excludeSet.add(outputParam.getNamespace());
							TemplateUtils.walkSchemasGetNamespaces(schemaDir, schemaDir + File.separator
								+ outputParam.getLocation(), excludeSet);
						}
					}
				}
			}
		}

		return excludeSet;
	}


	private void writeNamespaceMappings(ServiceInformation info) throws IOException {
		NamespaceMappingsTemplate namespaceMappingsT = new NamespaceMappingsTemplate();
		String namespaceMappingsS = namespaceMappingsT.generate(info);
		File namespaceMappingsF = new File(baseDirectory.getAbsolutePath() + File.separator
			+ "namespace2package.mappings");
		FileWriter namespaceMappingsFW = new FileWriter(namespaceMappingsF);
		namespaceMappingsFW.write(namespaceMappingsS);
		namespaceMappingsFW.close();
	}


	private void flattenWSDL(ServiceInformation info, File schemaDir) throws Exception {
		ServiceWSDLTemplate serviceWSDLT = new ServiceWSDLTemplate();
		String serviceWSDLS = serviceWSDLT.generate(info);
		File serviceWSDLF = new File(schemaDir.getAbsolutePath() + File.separator
			+ info.getServiceProperties().getProperty("introduce.skeleton.service.name") + File.separator
			+ info.getServiceProperties().getProperty("introduce.skeleton.service.name") + ".wsdl");
		FileWriter serviceWSDLFW = new FileWriter(serviceWSDLF);
		serviceWSDLFW.write(serviceWSDLS);
		serviceWSDLFW.close();

		writeNamespaceMappings(info);

		String cmd = CommonTools.getAntFlattenCommand(baseDirectory.getAbsolutePath());
		Process p = CommonTools.createAndOutputProcess(cmd);
		p.waitFor();
		if (p.exitValue() != 0) {
			throw new Exception("Service flatten wsdl exited abnormally");
		}
	}


	private SymbolTable generateSymbolTable(ServiceInformation info, Set excludeSet) throws Exception {
		Emitter parser = new Emitter();
		SymbolTable table = null;

		parser.setQuiet(true);
		parser.setImports(true);

		List excludeList = new ArrayList();
		// one hammer(List), one solution
		excludeList.addAll(excludeSet);
		parser.setNamespaceExcludes(excludeList);

		parser.setOutputDir(baseDirectory.getAbsolutePath() + File.separator + "tmp");
		parser.setNStoPkg(baseDirectory.getAbsolutePath() + File.separator + "namespace2package.mappings");
		parser
			.run(new File(baseDirectory.getAbsolutePath() + File.separator + "build" + File.separator + "schema"
				+ File.separator + info.getServiceProperties().get("introduce.skeleton.service.name") + File.separator
				+ info.getServiceProperties().get("introduce.skeleton.service.name") + "_flattened.wsdl")
				.getAbsolutePath());
		table = parser.getSymbolTable();
		Utils.deleteDir(new File(baseDirectory.getAbsolutePath() + File.separator + "tmp"));

		// table.dump(System.out);
		return table;
	}


	private void createArchive(ServiceInformation info) throws Exception {
		// create the archive
		long id = System.currentTimeMillis();

		info.getServiceProperties().setProperty("introduce.skeleton.timestamp", String.valueOf(id));
		info.getServiceProperties().store(
			new FileOutputStream(baseDirectory.getAbsolutePath() + File.separator + "introduce.properties"),
			"Introduce Properties");

		ResourceManager.createArchive(String.valueOf(id), info.getServiceProperties().getProperty(
			"introduce.skeleton.service.name"), baseDirectory.getAbsolutePath());
	}


	public static void main(String[] args) {
		Options options = new Options();
		Option directoryOpt = new Option(DIR_OPT, DIR_OPT_FULL, true, "The include tool directory");
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
