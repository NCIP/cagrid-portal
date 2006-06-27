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
import gov.nih.nci.cagrid.introduce.beans.service.ServiceType;
import gov.nih.nci.cagrid.introduce.codegen.base.SyncBase;
import gov.nih.nci.cagrid.introduce.codegen.common.SyncTool;
import gov.nih.nci.cagrid.introduce.codegen.common.SynchronizationException;
import gov.nih.nci.cagrid.introduce.codegen.properties.SyncProperties;
import gov.nih.nci.cagrid.introduce.codegen.serializers.SyncSerialization;
import gov.nih.nci.cagrid.introduce.codegen.services.SyncServices;
import gov.nih.nci.cagrid.introduce.codegen.utils.TemplateUtils;
import gov.nih.nci.cagrid.introduce.common.CommonTools;
import gov.nih.nci.cagrid.introduce.creator.SkeletonSchemaCreator;
import gov.nih.nci.cagrid.introduce.creator.SkeletonSecurityOperationProviderCreator;
import gov.nih.nci.cagrid.introduce.creator.SkeletonSourceCreator;
import gov.nih.nci.cagrid.introduce.extension.CodegenExtensionPostProcessor;
import gov.nih.nci.cagrid.introduce.extension.CodegenExtensionPreProcessor;
import gov.nih.nci.cagrid.introduce.extension.ExtensionTools;
import gov.nih.nci.cagrid.introduce.extension.ExtensionsLoader;
import gov.nih.nci.cagrid.introduce.info.SchemaInformation;
import gov.nih.nci.cagrid.introduce.info.ServiceInformation;
import gov.nih.nci.cagrid.introduce.info.SpecificServiceInformation;
import gov.nih.nci.cagrid.introduce.templates.NamespaceMappingsTemplate;
import gov.nih.nci.cagrid.introduce.templates.NewServerConfigTemplate;
import gov.nih.nci.cagrid.introduce.templates.NewServiceJNDIConfigTemplate;
import gov.nih.nci.cagrid.introduce.templates.schema.service.ServiceWSDLTemplate;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintStream;
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
import org.jdom.Document;
import org.projectmobius.common.MalformedNamespaceException;
import org.projectmobius.common.MobiusException;
import org.projectmobius.common.XMLUtilities;


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

	class MultiServiceSymbolTable {
		ServiceInformation info;

		Set excludedSet;

		List symbolTables;

		public MultiServiceSymbolTable(ServiceInformation info, Set excludedSet)
				throws Exception {
			this.info = info;
			this.excludedSet = excludedSet;
			symbolTables = new ArrayList();
			generateSymbolTable(excludedSet);
		}

		public Element getElement(QName qname) {
			Element element = null;
			for (int i = 0; i < symbolTables.size(); i++) {

				element = ((SymbolTable) symbolTables.get(i)).getElement(qname);
				if (element != null) {
					break;
				}
			}
			return element;
		}

		public Type getType(QName qname) {
			Type type = null;
			for (int i = 0; i < symbolTables.size(); i++) {

				type = ((SymbolTable) symbolTables.get(i)).getType(qname);
				if (type != null) {
					break;
				}
			}
			return type;
		}

		public void dump(PrintStream stream) {
			for (int i = 0; i < symbolTables.size(); i++) {
				((SymbolTable) symbolTables.get(i)).dump(stream);
			}
		}

		public void generateSymbolTable(Set excludeSet)
				throws Exception {

			if (info.getServices() != null
					&& info.getServices().getService() != null) {
				for (int serviceI = 0; serviceI < info.getServices()
						.getService().length; serviceI++) {

					ServiceType service = info.getServices().getService(
							serviceI);

					Emitter parser = new Emitter();
					SymbolTable table = null;

					parser.setQuiet(true);
					parser.setImports(true);

					List excludeList = new ArrayList();
					// one hammer(List), one solution
					excludeList.addAll(excludeSet);
					parser.setNamespaceExcludes(excludeList);

					parser.setOutputDir(baseDirectory.getAbsolutePath()
							+ File.separator + "tmp");
					parser
							.setNStoPkg(baseDirectory.getAbsolutePath()
									+ File.separator
									+ "build"
									+ File.separator
									+ IntroduceConstants.NAMESPACE2PACKAGE_MAPPINGS_FILE);
					try {
						parser
								.run(new File(
										baseDirectory.getAbsolutePath()
												+ File.separator
												+ "build"
												+ File.separator
												+ "schema"
												+ File.separator
												+ info
														.getIntroduceServiceProperties()
														.get(
																IntroduceConstants.INTRODUCE_SKELETON_SERVICE_NAME)
												+ File.separator
												+ service.getName()
												+ "_flattened.wsdl")
										.getAbsolutePath());
					} catch (Exception e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
					table = parser.getSymbolTable();

					symbolTables.add(table);
					parser = null;
					System.gc();
				}

			}

			Utils.deleteDir(new File(baseDirectory.getAbsolutePath()
					+ File.separator + "tmp"));
		}
	}

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
		ServiceDescription introService = (ServiceDescription) Utils
				.deserializeDocument(baseDirectory + File.separator
						+ "introduce.xml", ServiceDescription.class);
		if (introService.getIntroduceVersion() == null
				|| !introService.getIntroduceVersion().equals(
						IntroduceConstants.INTRODUCE_VERSION)) {
			throw new Exception(
					"Introduce version in project does not match version provided by Introduce Toolkit ( "
							+ IntroduceConstants.INTRODUCE_VERSION
							+ " ): "
							+ introService.getIntroduceVersion());
		}
		File servicePropertiesFile = new File(baseDirectory.getAbsolutePath()
				+ File.separator + IntroduceConstants.INTRODUCE_PROPERTIES_FILE);
		Properties serviceProperties = new Properties();
		serviceProperties.load(new FileInputStream(servicePropertiesFile));
		
		// have to set the service directory in the service properties
		serviceProperties.setProperty(
				IntroduceConstants.INTRODUCE_SKELETON_DESTINATION_DIR,
				baseDirectory.getAbsolutePath());
		ServiceInformation info = new ServiceInformation(introService,
				serviceProperties, baseDirectory);
		File schemaDir = new File(baseDirectory.getAbsolutePath()
				+ File.separator + "schema");

		// STEP 2: make a backup of the service implementation
		this.createArchive(info);
		
		//before we actually process anything we must create the code and conf
		//required for any new services which were added.....
		createNewServices(info);

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

		// write all the services into the services list property
		String servicesList = "";
		if (info.getServices() != null
				&& info.getServices().getService() != null) {
			for (int serviceI = 0; serviceI < info.getServices().getService().length; serviceI++) {
				ServiceType service = info.getServices().getService(serviceI);
				servicesList += service.getName();
				if (serviceI < info.getServices().getService().length - 1) {
					servicesList += ",";
				}
			}
		}
		serviceProperties.setProperty(
				IntroduceConstants.INTRODUCE_SKELETON_SERVICES_LIST,
				servicesList);

		// store the modified properties back out....
		serviceProperties.store(new FileOutputStream(servicePropertiesFile),
				"Introduce Properties");

		System.out.println("Synchronizing with pre processing extensions");
		// run any extensions that need to be ran
		if (introService.getExtensions() != null
				&& introService.getExtensions().getExtension() != null) {
			ExtensionType[] extensions = introService.getExtensions()
					.getExtension();
			for (int i = 0; i < extensions.length; i++) {
				CodegenExtensionPreProcessor pp = ExtensionTools
						.getCodegenPreProcessor(extensions[i].getName());
				ServiceExtensionDescriptionType desc = ExtensionsLoader
						.getInstance().getServiceExtension(
								extensions[i].getName());
				if (pp != null) {
					pp.preCodegen(desc, info);
				}
			}
		}

		// STEP 4: write out namespace mappings and flatten the wsdl file then
		// merge namespace
		syncAndFlattenWSDL(info, schemaDir);

		mergeNamespaces();

		// STEP 5: run axis to get the symbol table
		MultiServiceSymbolTable table = new MultiServiceSymbolTable(info,
				excludeSet);

		// STEP 6: fill out the object model with the generated classnames where
		// the user didn't specify them explicitly
		populateClassnames(info, table);

		// STEP 7: run the code generation tools
		SyncTool baseS = new SyncBase(baseDirectory, info);
		SyncTool servicesS = new SyncServices(baseDirectory, info);
		SyncTool serializerS = new SyncSerialization(baseDirectory, info);
		SyncTool propertiesS = new SyncProperties(baseDirectory, info);

		System.out.println("Synchronizing the base files");
		baseS.sync();
		System.out.println("Synchronizing the services");
		servicesS.sync();
		System.out.println("Synchronizing the type mappings");
		serializerS.sync();
		System.out.println("Synchronizing the service properties");
		propertiesS.sync();

		// STEP 8: run the extensions
		System.out.println("Synchronizing with post processing extensions");
		// run any extensions that need to be ran
		if (introService.getExtensions() != null
				&& introService.getExtensions().getExtension() != null) {
			ExtensionType[] extensions = introService.getExtensions()
					.getExtension();
			for (int i = 0; i < extensions.length; i++) {
				CodegenExtensionPostProcessor pp = ExtensionTools
						.getCodegenPostProcessor(extensions[i].getName());
				ServiceExtensionDescriptionType desc = ExtensionsLoader
						.getInstance().getServiceExtension(
								extensions[i].getName());
				if (pp != null) {
					pp.postCodegen(desc, info);
				}
			}
		}
		
		table = null;
		System.gc();
		
		
	}

	private void populateClassnames(ServiceInformation info,
			MultiServiceSymbolTable table) throws MalformedNamespaceException,
			SynchronizationException {

		// table.dump(System.out);
		// get the classnames from the axis symbol table
		if (info.getNamespaces() != null
				&& info.getNamespaces().getNamespace() != null) {
			for (int i = 0; i < info.getNamespaces().getNamespace().length; i++) {
				NamespaceType ntype = info.getNamespaces().getNamespace(i);
				if (ntype.getSchemaElement() != null) {
					for (int j = 0; j < ntype.getSchemaElement().length; j++) {
						SchemaElementType type = ntype.getSchemaElement(j);
						if (type.getClassName() == null) {
							if (ntype.getNamespace().equals(
									IntroduceConstants.W3CNAMESPACE)) {
								Type symtype = table.getType(new QName(ntype
										.getNamespace(), type.getType()));
								// type may not be being used so axis will
								// ignore it....
								if (symtype != null) {
									type
											.setClassName(getRelativeClassName(symtype
													.getName()));
									type.setPackageName(getPackageName(symtype
											.getName()));
								}
							} else {
								QName qname = new QName(ntype.getNamespace(),
										type.getType());
								Element element = table.getElement(qname);
								if (element == null) {
									table.dump(System.err);
									throw new SynchronizationException(
											"Unable to find Element in symbol table for: "
													+ qname);

								}
								type.setClassName(getRelativeClassName(element
										.getName()));
								type.setPackageName(getPackageName(element
										.getName()));
							}
						} else {
							if (type.getSerializer() == null
									|| type.getDeserializer() == null) {
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
		if (info.getServices().getService() != null) {
			for (int serviceI = 0; serviceI < info.getServices().getService().length; serviceI++) {
				ServiceType service = info.getServices().getService(serviceI);
				if (service.getMethods() != null
						&& service.getMethods().getMethod() != null) {
					for (int i = 0; i < service.getMethods().getMethod().length; i++) {
						MethodType mtype = service.getMethods().getMethod(i);
						// process the inputs
						if (mtype.getInputs() != null
								&& mtype.getInputs().getInput() != null) {
							for (int j = 0; j < mtype.getInputs().getInput().length; j++) {
								MethodTypeInputsInput inputParam = mtype
										.getInputs().getInput(j);
								SchemaInformation namespace = CommonTools
										.getSchemaInformation(info
												.getNamespaces(), inputParam
												.getQName());
								if (!namespace
										.getNamespace()
										.getNamespace()
										.equals(IntroduceConstants.W3CNAMESPACE)) {
									QName qname = null;
									if (mtype.isIsImported()) {
										qname = new QName(
												mtype.getImportInformation()
														.getNamespace(),
												">>"
														+ TemplateUtils
																.upperCaseFirstCharacter(mtype
																		.getName())
														+ "Request>"
														+ inputParam.getName());
									} else {
										qname = new QName(
												service.getNamespace(),
												">>"
														+ TemplateUtils
																.upperCaseFirstCharacter(mtype
																		.getName())
														+ "Request>"
														+ inputParam.getName());
									}

									Type type = table.getType(qname);
									if (type == null) {
										table.dump(System.err);
										throw new SynchronizationException(
												"Unable to find Element in symbol table for: "
														+ qname);
									}

									if (mtype.isIsImported()) {
										inputParam.setContainerClassName(mtype
												.getImportInformation()
												.getPackageName()
												+ "."
												+ getRelativeClassName(type
														.getName()));
									} else {
										inputParam
												.setContainerClassName(service
														.getPackageName()
														+ ".stubs."
														+ getRelativeClassName(type
																.getName()));
									}
								}

							}
						}

					}
				}
			}
		}
	}

	public void createNewServices(ServiceInformation info) {
		List newServices = new ArrayList();
		if (info.getServices() != null
				&& info.getServices().getService() != null) {
			for (int serviceI = 0; serviceI < info.getServices().getService().length; serviceI++) {
				File serviceDir = new File(info.getBaseDirectory()
						+ File.separator
						+ "src"
						+ File.separator
						+ CommonTools.getPackageDir(info.getServices()
								.getService(serviceI)));
				if (!serviceDir.exists()) {
					newServices.add(info.getServices().getService(serviceI));
				}
			}
		}

		// add all new service information and
		// add the new service description to the service.wsdd
		File serverConfigF = new File(info.getBaseDirectory().getAbsolutePath()
				+ File.separator + "server-config.wsdd");

		Document serverConfigDoc = null;
		try {
			serverConfigDoc = XMLUtilities.fileNameToDocument(serverConfigF
					.getAbsolutePath());
		} catch (MobiusException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		
		File jndiConfigF = new File(info.getBaseDirectory().getAbsolutePath()
				+ File.separator + "jndi-config.xml");
		
		Document serverConfigJNDIDoc = null;
		try {
			serverConfigJNDIDoc = XMLUtilities.fileNameToDocument(jndiConfigF
					.getAbsolutePath());
		} catch (MobiusException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}

		for (int i = 0; i < newServices.size(); i++) {
			ServiceType newService = (ServiceType) newServices.get(i);
			SkeletonSourceCreator ssc = new SkeletonSourceCreator();
			SkeletonSchemaCreator sschc = new SkeletonSchemaCreator();
			SkeletonSecurityOperationProviderCreator ssopc = new SkeletonSecurityOperationProviderCreator();
			try {
				System.out.println("Adding Service for: "
						+ newService.getName());
				ssc.createSkeleton(info.getBaseDirectory(), info, newService);
				sschc.createSkeleton(info.getBaseDirectory(), info, newService);
				ssopc.createSkeleton(new SpecificServiceInformation(info, newService));

				// if this is a new service we need to add it's new "service"
				// element to the WSDD

				NewServerConfigTemplate newServerConfigT = new NewServerConfigTemplate();
				String newServerConfigS = newServerConfigT
						.generate(new SpecificServiceInformation(info,
								newService));
				org.jdom.Element newServiceElement = XMLUtilities
						.stringToDocument(newServerConfigS).getRootElement();
				serverConfigDoc.getRootElement().addContent(0,
						newServiceElement.detach());
				
				
				//if this is a new service we need to add it's new "service" element to the JNDI
				NewServiceJNDIConfigTemplate jndiConfigT = new NewServiceJNDIConfigTemplate();
				org.jdom.Element newServiceJNDIElement = XMLUtilities.stringToDocument(jndiConfigT.generate(new SpecificServiceInformation(info,newService))).getRootElement();
				serverConfigJNDIDoc.getRootElement().addContent(0,
						newServiceJNDIElement.detach());

			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		String serverConfigS;
		String serverConfigJNDIS;
		try {
			serverConfigS = XMLUtilities.formatXML(XMLUtilities
					.documentToString(serverConfigDoc));
			FileWriter serverConfigFW = new FileWriter(serverConfigF);
			serverConfigFW.write(serverConfigS);
			serverConfigFW.close();
			
			serverConfigJNDIS = XMLUtilities.formatXML(XMLUtilities
					.documentToString(serverConfigJNDIDoc));
			FileWriter jndiConfigFW = new FileWriter(jndiConfigF);
			jndiConfigFW.write(serverConfigJNDIS);
			jndiConfigFW.close();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
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
	private Set generateNamespaceExcludesSet(ServiceInformation info)
			throws Exception {
		Set excludeSet = new HashSet();
		File schemaDir = new File(baseDirectory.getAbsolutePath()
				+ File.separator
				+ "schema"
				+ File.separator
				+ info.getIntroduceServiceProperties().getProperty(
						IntroduceConstants.INTRODUCE_SKELETON_SERVICE_NAME));
		// exclude namespaces that have FQN for metadata class
		// get the classnames from the axis symbol table
		if (info.getNamespaces() != null
				&& info.getNamespaces().getNamespace() != null) {
			for (int i = 0; i < info.getNamespaces().getNamespace().length; i++) {
				NamespaceType ntype = info.getNamespaces().getNamespace(i);

				if (ntype.getGenerateStubs() != null
						&& !ntype.getGenerateStubs().booleanValue()) {
					// the model explictly says not to generate stubs
					excludeSet.add(ntype.getNamespace());
					TemplateUtils.walkSchemasGetNamespaces(schemaDir
							+ File.separator + ntype.getLocation(), excludeSet, new HashSet());
				} else if (ntype.getSchemaElement() != null) {
					for (int j = 0; j < ntype.getSchemaElement().length; j++) {
						SchemaElementType type = ntype.getSchemaElement(j);
						if (type.getClassName() != null) {
							if (ntype.getLocation() != null) {
								// the namespace contains customly serialized
								// beans... so don't generate stubs
								
								excludeSet.add(ntype.getNamespace());
								TemplateUtils.walkSchemasGetNamespaces(
										schemaDir + File.separator
												+ ntype.getLocation(),
										excludeSet, new HashSet());
								// this schema is excluded.. no need to check
								// the rest of the schemaelements
								break;
							}
						}
					}
				}
			}
		}

		return excludeSet;
	}

	private void writeNamespaceMappings(ServiceInformation info)
			throws IOException {
		NamespaceMappingsTemplate namespaceMappingsT = new NamespaceMappingsTemplate();
		String namespaceMappingsS = namespaceMappingsT.generate(info);
		File namespaceMappingsF = new File(baseDirectory.getAbsolutePath()
				+ File.separator
				+ IntroduceConstants.NAMESPACE2PACKAGE_MAPPINGS_FILE);
		FileWriter namespaceMappingsFW = new FileWriter(namespaceMappingsF);
		namespaceMappingsFW.write(namespaceMappingsS);
		namespaceMappingsFW.close();
	}

	private void mergeNamespaces() throws Exception {
		String cmd = CommonTools.getAntMergeCommand(baseDirectory
				.getAbsolutePath());
		Process p = CommonTools.createAndOutputProcess(cmd);
		p.waitFor();
		if (p.exitValue() != 0) {
			throw new Exception("Service merge exited abnormally");
		}
	}

	private void syncAndFlattenWSDL(ServiceInformation info, File schemaDir)
			throws Exception {
		// get the classnames from the axis symbol table
		if (info.getServices().getService() != null) {
			for (int serviceI = 0; serviceI < info.getServices().getService().length; serviceI++) {
				// rewrite the wsdl for each service....
				ServiceType service = info.getServices().getService(serviceI);
				ServiceWSDLTemplate serviceWSDLT = new ServiceWSDLTemplate();
				String serviceWSDLS = serviceWSDLT
						.generate(new SpecificServiceInformation(info, service));
				File serviceWSDLF = new File(
						schemaDir.getAbsolutePath()
								+ File.separator
								+ info
										.getIntroduceServiceProperties()
										.getProperty(
												IntroduceConstants.INTRODUCE_SKELETON_SERVICE_NAME)
								+ File.separator + service.getName() + ".wsdl");
				FileWriter serviceWSDLFW = new FileWriter(serviceWSDLF);
				serviceWSDLFW.write(serviceWSDLS);
				serviceWSDLFW.close();

			}
		}
		if (info.getServices().getService() != null) {
			for (int serviceI = 0; serviceI < info.getServices().getService().length; serviceI++) {
				// rewrite the wsdl for each service....
				ServiceType service = info.getServices().getService(serviceI);
				// for each service add any imported operations.....
				if (service.getMethods() != null
						&& service.getMethods().getMethod() != null) {
					for (int methodI = 0; methodI < service.getMethods()
							.getMethod().length; methodI++) {
						MethodType method = service.getMethods().getMethod(
								methodI);
						if (method.isIsImported()) {
							TemplateUtils.addImportedOperationToService(method,
									new SpecificServiceInformation(info,
											service));
						}
					}
				}
			}
		}

		writeNamespaceMappings(info);

		String cmd = CommonTools.getAntFlattenCommand(baseDirectory
				.getAbsolutePath());
		Process p = CommonTools.createAndOutputProcess(cmd);
		p.waitFor();
		if (p.exitValue() != 0) {
			throw new Exception("Service flatten wsdl exited abnormally");
		}
	}

	private void createArchive(ServiceInformation info) throws Exception {
		// create the archive
		long id = System.currentTimeMillis();

		info.getIntroduceServiceProperties().setProperty(
				IntroduceConstants.INTRODUCE_SKELETON_TIMESTAMP,
				String.valueOf(id));
		info.getIntroduceServiceProperties().store(
				new FileOutputStream(baseDirectory.getAbsolutePath()
						+ File.separator
						+ IntroduceConstants.INTRODUCE_PROPERTIES_FILE),
				"Introduce Properties");

		ResourceManager.createArchive(String.valueOf(id), info
				.getIntroduceServiceProperties().getProperty(
						IntroduceConstants.INTRODUCE_SKELETON_SERVICE_NAME),
				baseDirectory.getAbsolutePath());
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
