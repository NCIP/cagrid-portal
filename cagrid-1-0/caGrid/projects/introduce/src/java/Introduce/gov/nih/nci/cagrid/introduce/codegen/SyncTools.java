package gov.nih.nci.cagrid.introduce.codegen;

import gov.nih.nci.cagrid.common.Utils;
import gov.nih.nci.cagrid.introduce.IntroduceConstants;
import gov.nih.nci.cagrid.introduce.ResourceManager;
import gov.nih.nci.cagrid.introduce.beans.ServiceDescription;
import gov.nih.nci.cagrid.introduce.beans.extension.ExtensionType;
import gov.nih.nci.cagrid.introduce.beans.extension.ServiceExtensionDescriptionType;
import gov.nih.nci.cagrid.introduce.beans.method.MethodType;
import gov.nih.nci.cagrid.introduce.beans.method.MethodTypeInputsInput;
import gov.nih.nci.cagrid.introduce.beans.namespace.NamespaceType;
import gov.nih.nci.cagrid.introduce.beans.namespace.SchemaElementType;
import gov.nih.nci.cagrid.introduce.codegen.common.SyncTool;
import gov.nih.nci.cagrid.introduce.codegen.common.SynchronizationException;
import gov.nih.nci.cagrid.introduce.codegen.metadata.SyncMetadata;
import gov.nih.nci.cagrid.introduce.codegen.methods.SyncMethods;
import gov.nih.nci.cagrid.introduce.codegen.security.SyncSecurity;
import gov.nih.nci.cagrid.introduce.codegen.serializers.SyncSerialization;
import gov.nih.nci.cagrid.introduce.codegen.utils.TemplateUtils;
import gov.nih.nci.cagrid.introduce.common.CommonTools;
import gov.nih.nci.cagrid.introduce.extension.CodegenExtensionPostProcessor;
import gov.nih.nci.cagrid.introduce.extension.CodegenExtensionPreProcessor;
import gov.nih.nci.cagrid.introduce.extension.ExtensionTools;
import gov.nih.nci.cagrid.introduce.extension.ExtensionsLoader;
import gov.nih.nci.cagrid.introduce.info.SchemaInformation;
import gov.nih.nci.cagrid.introduce.info.ServiceInformation;
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


	private String getPackageName(String fullyQualifiedClassName) {
		int index = fullyQualifiedClassName.lastIndexOf(".");
		if (index >= 0) {
			return fullyQualifiedClassName.substring(0, index);
		}
		return null;
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
		File servicePropertiesFile = new File(baseDirectory.getAbsolutePath() + File.separator
			+ IntroduceConstants.INTRODUCE_PROPERTIES_FILE);
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

		System.out.println("Synchronizing with pre processing extensions");
		// run any extensions that need to be ran
		if (introService.getExtensions() != null && introService.getExtensions().getExtension() != null) {
			ExtensionType[] extensions = introService.getExtensions().getExtension();
			for (int i = 0; i < extensions.length; i++) {
				CodegenExtensionPreProcessor pp = ExtensionTools.getCodegenPreProcessor(extensions[i].getName());
				ServiceExtensionDescriptionType desc = ExtensionsLoader.getInstance().getServiceExtension(extensions[i].getName());
				if (pp != null) {
					pp.preCodegen(desc, info);
				}
			}
		}

		// STEP 4: write out namespace mappings and flatten the wsdl file
		flattenWSDL(info, schemaDir);

		// STEP 5: run axis to get the symbol table
		SymbolTable table = generateSymbolTable(info, excludeSet);

		// STEP 6: fill out the object model with the generated classnames where
		// the user didn't specify them explicitly
		populateClassnames(info, table);

		// STEP 7: run the code generation tools
		SyncTool methodsS = new SyncMethods(baseDirectory, info);
		SyncTool metadata = new SyncMetadata(baseDirectory, info);
		SyncTool security = new SyncSecurity(baseDirectory, info);
		SyncTool serializerS = new SyncSerialization(baseDirectory, info);

		System.out.println("Synchronizing the methods");
		methodsS.sync();
		System.out.println("Synchronizing the metadata");
		metadata.sync();
		System.out.println("Synchronizing the security");
		security.sync();
		System.out.println("Synchronizing the type mappings");
		serializerS.sync();

		// STEP 8: run the extensions
		System.out.println("Synchronizing with post processing extensions");
		// run any extensions that need to be ran
		if (introService.getExtensions() != null && introService.getExtensions().getExtension() != null) {
			ExtensionType[] extensions = introService.getExtensions().getExtension();
			for (int i = 0; i < extensions.length; i++) {
				CodegenExtensionPostProcessor pp = ExtensionTools.getCodegenPostProcessor(extensions[i].getName());
				ServiceExtensionDescriptionType desc = ExtensionsLoader.getInstance().getServiceExtension(extensions[i].getName());
				if (pp != null) {
					pp.postCodegen(desc, info);
				}
			}
		}

	}


	private void populateClassnames(ServiceInformation info, SymbolTable table) throws MalformedNamespaceException,
		SynchronizationException {

		// table.dump(System.out);
		// get the classnames from the axis symbol table
		if (info.getNamespaces() != null && info.getNamespaces().getNamespace() != null) {
			for (int i = 0; i < info.getNamespaces().getNamespace().length; i++) {
				NamespaceType ntype = info.getNamespaces().getNamespace(i);
				if (ntype.getNamespace() != null && !ntype.getNamespace().equals(IntroduceConstants.W3CNAMESPACE)
					&& (ntype.getPackageName() == null || ntype.getPackageName().length() <= 0)) {
					ntype.setPackageName(CommonTools.getPackageName(new Namespace(ntype.getNamespace())));
				}
				if (ntype.getSchemaElement() != null) {
					for (int j = 0; j < ntype.getSchemaElement().length; j++) {
						SchemaElementType type = ntype.getSchemaElement(j);
						if (type.getClassName() == null) {
							if (ntype.getNamespace().equals(IntroduceConstants.W3CNAMESPACE)) {
								Type symtype = table.getType(new QName(ntype.getNamespace(), type.getType()));
								// type may not be being used so axis will
								// ignore it....
								if (symtype != null) {
									type.setClassName(getRelativeClassName(symtype.getName()));
									type.setPackageName(getPackageName(symtype.getName()));
								}
							} else {
								QName qname = new QName(ntype.getNamespace(), type.getType());
								Element element = table.getElement(qname);
								if (element == null) {
									table.dump(System.err);
									throw new SynchronizationException("Unable to find Element in symbol table for: "
										+ qname);

								}
								type.setClassName(getRelativeClassName(element.getName()));
								type.setPackageName(getPackageName(element.getName()));
							}
						} else {
							if (type.getSerializer() == null || type.getDeserializer() == null) {
								throw new SynchronizationException(
									"When specifying a custom classname, you must also specify both a serializer and deserializer.");
							}
							// it the classname is already set then set the
							// package name to the predefined
							// package name in the namespace type
							type.setPackageName(ntype.getPackageName());
						}
					}
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
						SchemaInformation namespace = info.getSchemaInformation(inputParam.getQName());
						if (!namespace.getNamespace().getNamespace().equals(IntroduceConstants.W3CNAMESPACE)) {
							Type type = table.getType(new QName(info.getIntroduceServiceProperties().getProperty(
								IntroduceConstants.INTRODUCE_SKELETON_NAMESPACE_DOMAIN)
								+ "/"
								+ info.getIntroduceServiceProperties().getProperty(
									IntroduceConstants.INTRODUCE_SKELETON_SERVICE_NAME), ">>" + mtype.getName() + ">"
								+ inputParam.getName()));
							inputParam.setContainerClassName(info.getIntroduceServiceProperties().getProperty(
								"introduce.skeleton.package")
								+ ".stubs." + getRelativeClassName(type.getName()));
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
			+ info.getIntroduceServiceProperties().getProperty(IntroduceConstants.INTRODUCE_SKELETON_SERVICE_NAME));
		// exclude namespaces that have FQN for metadata class
		// get the classnames from the axis symbol table
		if (info.getNamespaces() != null && info.getNamespaces().getNamespace() != null) {
			for (int i = 0; i < info.getNamespaces().getNamespace().length; i++) {
				NamespaceType ntype = info.getNamespaces().getNamespace(i);
				if (ntype.getSchemaElement() != null) {
					for (int j = 0; j < ntype.getSchemaElement().length; j++) {
						SchemaElementType type = ntype.getSchemaElement(j);
						if (type.getClassName() != null) {
							if (ntype.getLocation() != null) {
								excludeSet.add(ntype.getNamespace());
								TemplateUtils.walkSchemasGetNamespaces(schemaDir, schemaDir + File.separator
									+ ntype.getLocation(), excludeSet);
							}
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
			+ IntroduceConstants.NAMESPACE2PACKAGE_MAPPINGS_FILE);
		FileWriter namespaceMappingsFW = new FileWriter(namespaceMappingsF);
		namespaceMappingsFW.write(namespaceMappingsS);
		namespaceMappingsFW.close();
	}


	private void flattenWSDL(ServiceInformation info, File schemaDir) throws Exception {
		ServiceWSDLTemplate serviceWSDLT = new ServiceWSDLTemplate();
		String serviceWSDLS = serviceWSDLT.generate(info);
		File serviceWSDLF = new File(schemaDir.getAbsolutePath() + File.separator
			+ info.getIntroduceServiceProperties().getProperty(IntroduceConstants.INTRODUCE_SKELETON_SERVICE_NAME)
			+ File.separator
			+ info.getIntroduceServiceProperties().getProperty(IntroduceConstants.INTRODUCE_SKELETON_SERVICE_NAME)
			+ ".wsdl");
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
		parser.setNStoPkg(baseDirectory.getAbsolutePath() + File.separator
			+ IntroduceConstants.NAMESPACE2PACKAGE_MAPPINGS_FILE);
		parser.run(new File(baseDirectory.getAbsolutePath() + File.separator + "build" + File.separator + "schema"
			+ File.separator
			+ info.getIntroduceServiceProperties().get(IntroduceConstants.INTRODUCE_SKELETON_SERVICE_NAME)
			+ File.separator
			+ info.getIntroduceServiceProperties().get(IntroduceConstants.INTRODUCE_SKELETON_SERVICE_NAME)
			+ "_flattened.wsdl").getAbsolutePath());
		table = parser.getSymbolTable();
		Utils.deleteDir(new File(baseDirectory.getAbsolutePath() + File.separator + "tmp"));

		// table.dump(System.out);
		return table;
	}


	private void createArchive(ServiceInformation info) throws Exception {
		// create the archive
		long id = System.currentTimeMillis();

		info.getIntroduceServiceProperties().setProperty(IntroduceConstants.INTRODUCE_SKELETON_TIMESTAMP,
			String.valueOf(id));
		info.getIntroduceServiceProperties().store(
			new FileOutputStream(baseDirectory.getAbsolutePath() + File.separator
				+ IntroduceConstants.INTRODUCE_PROPERTIES_FILE), "Introduce Properties");

		ResourceManager.createArchive(String.valueOf(id), info.getIntroduceServiceProperties().getProperty(
			IntroduceConstants.INTRODUCE_SKELETON_SERVICE_NAME), baseDirectory.getAbsolutePath());
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
