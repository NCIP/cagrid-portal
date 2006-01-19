package gov.nih.nci.cagrid.introduce.codegen;

import gov.nih.nci.cagrid.common.CommonTools;
import gov.nih.nci.cagrid.introduce.ResourceManager;
import gov.nih.nci.cagrid.introduce.ServiceInformation;
import gov.nih.nci.cagrid.introduce.beans.metadata.ServiceMetadataListType;
import gov.nih.nci.cagrid.introduce.beans.metadata.ServiceMetadataType;
import gov.nih.nci.cagrid.introduce.beans.method.MethodType;
import gov.nih.nci.cagrid.introduce.beans.method.MethodTypeInputsInput;
import gov.nih.nci.cagrid.introduce.beans.method.MethodTypeOutput;
import gov.nih.nci.cagrid.introduce.beans.method.MethodsType;
import gov.nih.nci.cagrid.introduce.codegen.metadata.SyncMetadata;
import gov.nih.nci.cagrid.introduce.codegen.methods.SyncMethods;
import gov.nih.nci.cagrid.introduce.templates.NamespaceMappingsTemplate;
import gov.nih.nci.cagrid.introduce.templates.schema.service.ServiceWSDLTemplate;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.util.Properties;

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

	public File baseDirectory;


	public SyncTools(File directory) {
		this.baseDirectory = directory;
	}


	public void sync() throws Exception {

		MethodsType methods = (MethodsType) CommonTools.deserializeDocument(baseDirectory + File.separator
			+ "introduceMethods.xml", MethodsType.class);
		ServiceMetadataListType metadatas = (ServiceMetadataListType) CommonTools.deserializeDocument(baseDirectory
			+ File.separator + "introduceMetadata.xml", ServiceMetadataListType.class);

		File servicePropertiesFile = new File(baseDirectory.getAbsolutePath() + File.separator + "introduce.properties");
		Properties serviceProperties = new Properties();
		serviceProperties.load(new FileInputStream(servicePropertiesFile));

		ServiceInformation info = new ServiceInformation(methods, metadatas, serviceProperties);

		this.createArchive(info);

		File schemaDir = new File(baseDirectory.getAbsolutePath() + File.separator + "schema");

		ServiceWSDLTemplate serviceWSDLT = new ServiceWSDLTemplate();
		String serviceWSDLS = serviceWSDLT.generate(info);
		File serviceWSDLF = new File(schemaDir.getAbsolutePath() + File.separator
			+ info.getServiceProperties().getProperty("introduce.skeleton.service.name") + File.separator
			+ info.getServiceProperties().getProperty("introduce.skeleton.service.name") + ".wsdl");
		FileWriter serviceWSDLFW = new FileWriter(serviceWSDLF);
		serviceWSDLFW.write(serviceWSDLS);
		serviceWSDLFW.close();
		
		NamespaceMappingsTemplate namespaceMappingsT = new NamespaceMappingsTemplate();
		String namespaceMappingsS = namespaceMappingsT.generate(info);
		File namespaceMappingsF = new File(baseDirectory.getAbsolutePath() + File.separator + "namespace2package.mappings");
		FileWriter namespaceMappingsFW = new FileWriter(namespaceMappingsF);
		namespaceMappingsFW.write(namespaceMappingsS);
		namespaceMappingsFW.close();
		

		String cmd = CommonTools.getAntFlattenCommand(baseDirectory.getAbsolutePath());
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
		parser.setOutputDir(baseDirectory.getAbsolutePath() + File.separator + "tmp");
		parser.setNStoPkg(baseDirectory.getAbsolutePath() + File.separator + "namespace2package.mappings");
		parser
			.run(new File(baseDirectory.getAbsolutePath() + File.separator + "build" + File.separator + "schema"
				+ File.separator + info.getServiceProperties().get("introduce.skeleton.service.name") + File.separator
				+ info.getServiceProperties().get("introduce.skeleton.service.name") + "_flattened.wsdl")
				.getAbsolutePath());
		table = parser.getSymbolTable();
		CommonTools.deleteDir(new File(baseDirectory.getAbsolutePath() + File.separator + "tmp"));

		//table.dump(System.out);

		// get the classnames from the axis symbol table
		if (info.getMetadata().getMetadata() != null) {
			for (int i = 0; i < info.getMetadata().getMetadata().length; i++) {
				ServiceMetadataType mtype = info.getMetadata().getMetadata(i);
				if (mtype.getClassName() == null || mtype.getClassName().length() == 0) {
					Element element = table.getElement(new QName(mtype.getNamespace(), mtype.getType()));
					mtype.setClassName(element.getName());
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
						if (inputParam.getClassName() != null && inputParam.getClassName().equals("void")) {
						} else {
							Type type = table.getType(new QName(inputParam.getNamespace(), inputParam.getType()));

							if (inputParam.getIsArray().booleanValue()) {
								inputParam.setClassName(type.getName() + "[]");
							} else {
								inputParam.setClassName(type.getName());
							}
						}
					}
				}

				// process the outputs
				if (mtype.getOutput() != null) {
					MethodTypeOutput outputParam = mtype.getOutput();
					if (outputParam.getClassName() != null && outputParam.getClassName().equals("void")) {
					} else {
						Type type = table.getType(new QName(outputParam.getNamespace(), outputParam.getType()));
						if (outputParam.getIsArray().booleanValue()) {
							outputParam.setClassName(type.getName() + "[]");
						} else {
							outputParam.setClassName(type.getName());
						}
					}

				}
			}
		}

		SyncMethods methodsS = new SyncMethods(baseDirectory, info);
		SyncMetadata metadata = new SyncMetadata(baseDirectory, info);

		methodsS.sync();
		metadata.sync();

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
