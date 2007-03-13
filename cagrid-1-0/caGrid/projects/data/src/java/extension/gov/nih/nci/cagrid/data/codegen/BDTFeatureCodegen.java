package gov.nih.nci.cagrid.data.codegen;

import gov.nih.nci.cagrid.common.Utils;
import gov.nih.nci.cagrid.introduce.beans.service.ServiceType;
import gov.nih.nci.cagrid.introduce.extension.CodegenExtensionException;
import gov.nih.nci.cagrid.introduce.info.ServiceInformation;

import java.io.File;
import java.io.IOException;
import java.util.Properties;

/** 
 *  BDTFeatureCodegen
 *  TODO:DOCUMENT ME
 * 
 * @author David Ervin
 * 
 * @created Mar 13, 2007 1:10:16 PM
 * @version $Id: BDTFeatureCodegen.java,v 1.1 2007-03-13 18:34:28 dervin Exp $ 
 */
public class BDTFeatureCodegen extends FeatureCodegen {
	public static final String NL = System.getProperties().getProperty("line.separator");
	
	// service impl edits
	public static final String SERVICE_START = "BDTResource thisResource = (BDTResource)bdtHome.find(bdtResourceKey);";
	public static final String SERVICE_LINE1 = "String classToQnameMapfile = gov.nih.nci.cagrid.data.service.ServiceConfigUtil.getClassToQnameMappingsFile();" + NL;
	public static final String SERVICE_LINE2 = "java.io.InputStream wsddStream = new java.io.FileInputStream(gov.nih.nci.cagrid.data.service.ServiceConfigUtil" + NL 
		+ "\t.getConfigProperty(gov.nih.nci.cagrid.data.DataServiceConstants.SERVER_CONFIG_LOCATION));" + NL;
	public static final String SERVICE_LINE3 = "thisResource.initialize(cqlQuery, classToQnameMapfile, wsddStream);" + NL;
	
	// bdt resource edits
	public static final String RESOURCE_CLASS_DECLARATION = "public class BDTResource extends BDTResourceBase implements BDTResourceI {";
	public static final String RESOURCE_LINE1 = "private gov.nih.nci.cagrid.data.service.bdt.BDTResourceHelper helper;" + NL;
	public static final String RESOURCE_INITIALIZE = 
		"void initialize(gov.nih.nci.cagrid.cqlquery.CQLQuery query, " + NL +
		"\t\tString classToQnameMapfile, " + NL + 
		"\t\tjava.io.InputStream wsddStream) {" + NL +
		"\tthis.helper = new gov.nih.nci.cagrid.data.service.bdt.BDTResourceHelper(" + NL +
		"\t\tquery, classToQnameMapfile, wsddStream);" + NL +
		"}";
	public static final String RESOURCE_CREATE_ENUM_METHOD = "public EnumIterator createEnumeration() throws BDTException {";
	public static final String RESOURCE_CREATE_ENUM_METHOD_IMPL = 
		"try {" + NL +
		"\treturn helper.createEnumIterator();" + NL +
		"} catch (gov.nih.nci.cagrid.data.faults.QueryProcessingExceptionType ex) {" + NL +
		"\tthrow new BDTException(\"Error processing query: \" + ex.getMessage(), ex);" + NL +
		"} catch (gov.nih.nci.cagrid.data.faults.MalformedQueryExceptionType ex) {" + NL +
		"throw new BDTException(\"Improperly formed query: \" + ex.getMessage(), ex);" + NL +
		"}";
	public static final String RESOURCE_GET_METHOD = "public AnyXmlType get() throws BDTException {";
	public static final String RESOURCE_GET_METHOD_IMPL = "return helper.resultsAsAnyType();";
	

	public BDTFeatureCodegen(ServiceInformation info, ServiceType mainService, Properties serviceProps) {
		super(info, mainService, serviceProps);
	}


	public void codegenFeature() throws CodegenExtensionException {
		editBdtImpl();
		editBdtResource();
	}
	
	
	private void editBdtImpl() throws CodegenExtensionException {
		System.out.println("EDITING BDT IMPL");
		System.out.println("EDITING BDT IMPL");
		System.out.println("EDITING BDT IMPL");
		System.out.println("EDITING BDT IMPL");
		
		String serviceName = getServiceInformation().getServices().getService()[0].getName();
		String basePackage = getServiceInformation().getServices().getService()[0].getPackageName();
		// full name of the service impl class
		String fullClassName = basePackage + ".service." + serviceName + "Impl";
		// file name of the service impl java source
		String sourceFileName = getServiceInformation().getBaseDirectory().getAbsolutePath() + File.separator + "src" + File.separator 
			+ fullClassName.replace('.', File.separatorChar) + ".java";
		// read the source file in
		StringBuffer source = null;
		try {
			source = Utils.fileToStringBuffer(new File(sourceFileName));
		} catch (IOException ex) {
			throw new CodegenExtensionException("Error reading service source file: " + ex.getMessage(), ex);
		}
		
		// build the edit
		StringBuffer edit = new StringBuffer();
		edit.append(SERVICE_LINE1).append(SERVICE_LINE2).append(SERVICE_LINE3);
		if (source.indexOf(edit.toString()) == -1) {
			// edit has never been performed, perform edits
			int startIndex = source.indexOf(SERVICE_START) + SERVICE_START.length();
			source.insert(startIndex, NL);
			source.insert(startIndex + 1, edit.toString());
		}

		// write the source file back out
		try {
			Utils.stringBufferToFile(source, sourceFileName);
		} catch (IOException ex) {
			throw new CodegenExtensionException("Error writing service source file: " + ex.getMessage(), ex);
		}
		
		System.out.println("WROTE BDT IMPL");
		System.out.println("WROTE BDT IMPL");
		System.out.println("WROTE BDT IMPL");
		System.out.println("WROTE BDT IMPL");
		System.out.println("WROTE BDT IMPL");
	}
	
	
	
	private void editBdtResource() throws CodegenExtensionException {
		System.out.println("EDITING BDT RESOURCE");
		System.out.println("EDITING BDT RESOURCE");
		System.out.println("EDITING BDT RESOURCE");
		System.out.println("EDITING BDT RESOURCE");
		
		String basePackage = getServiceInformation().getServices().getService()[0].getPackageName();
		// full name of the service impl class
		String fullClassName = basePackage + ".service.BDTResource";
		// file name of the service impl java source
		String sourceFileName = getServiceInformation().getBaseDirectory().getAbsolutePath() + File.separator + "src" + File.separator 
			+ fullClassName.replace('.', File.separatorChar) + ".java";
		// read the source file in
		StringBuffer source = null;
		try {
			source = Utils.fileToStringBuffer(new File(sourceFileName));
		} catch (IOException ex) {
			throw new CodegenExtensionException("Error reading BDT Resource source file: " + ex.getMessage(), ex);
		}
		
		// build up the edits for the initialize method
		StringBuffer initEdits = new StringBuffer();
		initEdits.append(RESOURCE_LINE1);
		initEdits.append(NL);
		initEdits.append(RESOURCE_INITIALIZE);
		initEdits.append(NL).append(NL);
		
		boolean doEdits = source.indexOf(initEdits.toString()) == -1; 
		if (doEdits) {
			int startIndex = source.indexOf(RESOURCE_CLASS_DECLARATION) + RESOURCE_CLASS_DECLARATION.length();
			source.insert(startIndex, NL);
			source.insert(startIndex + NL.length(), initEdits.toString());
		}
		
		// edits for the create enumeration method
		if (doEdits) {
			int startIndex = source.indexOf(RESOURCE_CREATE_ENUM_METHOD) + RESOURCE_CREATE_ENUM_METHOD.length();
			source.insert(startIndex, NL);
			startIndex += NL.length();
			source.insert(startIndex, RESOURCE_CREATE_ENUM_METHOD_IMPL);
			startIndex += RESOURCE_CREATE_ENUM_METHOD_IMPL.length();
			// strip away the not implemented exception
			int endIndex = source.indexOf("}", startIndex);
			source.delete(startIndex, endIndex);
		}
		
		// edits for ws-transfer's get method
		if (doEdits) {
			int startIndex = source.indexOf(RESOURCE_GET_METHOD) + RESOURCE_GET_METHOD.length();
			source.insert(startIndex, NL);
			startIndex += NL.length();
			source.insert(startIndex, RESOURCE_GET_METHOD_IMPL);
			startIndex += RESOURCE_GET_METHOD_IMPL.length();
			// strip away the not implemented exception
			int endIndex = source.indexOf("}", startIndex);
			source.delete(startIndex, endIndex);
		}
		
		// write out the edited source file
		try {
			Utils.stringBufferToFile(source, sourceFileName);
		} catch (IOException ex) {
			throw new CodegenExtensionException("Error writing BDT Resource source file: " + ex.getMessage(), ex);
		}
		
		System.out.println("WROTE BDT RESOURCE");
		System.out.println("WROTE BDT RESOURCE");
		System.out.println("WROTE BDT RESOURCE");
		System.out.println("WROTE BDT RESOURCE");
	}
}
