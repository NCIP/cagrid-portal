package gov.nih.nci.cagrid.data.codegen;

import gov.nih.nci.cagrid.common.Utils;
import gov.nih.nci.cagrid.data.DataServiceConstants;
import gov.nih.nci.cagrid.introduce.beans.method.MethodType;
import gov.nih.nci.cagrid.introduce.beans.service.ServiceType;
import gov.nih.nci.cagrid.introduce.codegen.services.methods.SyncHelper;
import gov.nih.nci.cagrid.introduce.extension.CodegenExtensionException;
import gov.nih.nci.cagrid.introduce.info.ServiceInformation;

import java.io.File;
import java.io.IOException;
import java.util.Properties;

/** 
 *  BDTFeatureCodegen
 *  Generates source code for the BDT data service and writes it
 *  into the service impl and BDT Resource
 * 
 * @author David Ervin
 * 
 * @created Mar 13, 2007 1:10:16 PM
 * @version $Id: BDTFeatureCodegen.java,v 1.8 2007-04-04 19:55:21 dervin Exp $ 
 */
public class BDTFeatureCodegen extends FeatureCodegen {
	// public static final String NL = System.getProperties().getProperty("line.separator");
    public static final String NL = "\n";
	
	// service impl edits
	public static final String SERVICE_START = "BDTResource thisResource = (BDTResource)bdtHome.find(bdtResourceKey);";
	public static final String SERVICE_LINE1 = "\t\t\tString classToQnameMapfile = gov.nih.nci.cagrid.data.service.ServiceConfigUtil.getClassToQnameMappingsFile();" + NL;
	public static final String SERVICE_LINE2 = "\t\t\tjava.io.InputStream wsddStream = new java.io.FileInputStream(gov.nih.nci.cagrid.data.service.ServiceConfigUtil" + NL 
		+ "\t\t\t\t.getConfigProperty(gov.nih.nci.cagrid.data.DataServiceConstants.SERVER_CONFIG_LOCATION));" + NL;
	public static final String SERVICE_LINE3 = "\t\t\tthisResource.initialize(cqlQuery, classToQnameMapfile, wsddStream);" + NL;
	
	// bdt resource edits
	public static final String RESOURCE_CLASS_DECLARATION = "public class BDTResource extends BDTResourceBase implements BDTResourceI {";
	public static final String RESOURCE_LINE1 = "\tprivate gov.nih.nci.cagrid.data.bdt.service.BDTResourceHelper helper;" + NL;
	public static final String RESOURCE_INITIALIZE = 
		"\tvoid initialize(gov.nih.nci.cagrid.cqlquery.CQLQuery query, " + NL +
		"\t\t\tString classToQnameMapfile, " + NL + 
		"\t\t\tjava.io.InputStream wsddStream) {" + NL +
		"\t\tthis.helper = new gov.nih.nci.cagrid.data.bdt.service.BDTResourceHelper(" + NL +
		"\t\t\tquery, classToQnameMapfile, wsddStream);" + NL +
		"\t}";
	public static final String RESOURCE_CREATE_ENUM_METHOD = "public EnumIterator createEnumeration() throws BDTException {";
	public static final String RESOURCE_CREATE_ENUM_METHOD_IMPL = 
		"\ttry {" + NL +
		"\t\treturn helper.createEnumIterator();" + NL +
		"\t} catch (gov.nih.nci.cagrid.data.faults.QueryProcessingExceptionType ex) {" + NL +
		"\t\tthrow new BDTException(\"Error processing query: \" + ex.getMessage(), ex);" + NL +
		"\t} catch (gov.nih.nci.cagrid.data.faults.MalformedQueryExceptionType ex) {" + NL +
		"\t\tthrow new BDTException(\"Improperly formed query: \" + ex.getMessage(), ex);" + NL +
		"\t}" + NL;
	public static final String RESOURCE_GET_METHOD = "public AnyXmlType get() throws BDTException {";
	public static final String RESOURCE_GET_METHOD_IMPL =
		"\ttry {" + NL +
		"\t\treturn helper.resultsAsAnyType();" + NL +
		"\t} catch (gov.nih.nci.cagrid.data.QueryProcessingException ex) {" + NL +
		"\t\tthrow new BDTException(\"Error processing query: \" + ex.getMessage(), ex);" + NL +
		"\t} catch (gov.nih.nci.cagrid.data.MalformedQueryException ex) {" + NL +
		"\t\tthrow new BDTException(\"Improperly formed query: \" + ex.getMessage(), ex);" + NL +
		"\t}" + NL;
	public static final String RESOURCE_REMOVE_METHOD = "public void remove() throws ResourceException {";
	public static final String RESOURCE_REMOVE_METHOD_IMPL = 
		"\thelper.cleanUp();" + NL;

	public BDTFeatureCodegen(ServiceInformation info, ServiceType mainService, Properties serviceProps) {
		super(info, mainService, serviceProps);
	}


	public void codegenFeature() throws CodegenExtensionException {
		editBdtImpl();
		editBdtResource();
	}
	
	
	private void editBdtImpl() throws CodegenExtensionException {
        // figure out what the method signature is
        MethodType bdtQueryMethod = null;
        MethodType[] allMethods = getMainService().getMethods().getMethod();
        for (int i = 0; i < allMethods.length; i++) {
            MethodType current = allMethods[i];
            if (current.getName().equals(DataServiceConstants.BDT_QUERY_METHOD_NAME)
                && current.getInputs().getInput(0).getQName().equals(DataServiceConstants.CQL_QUERY_QNAME)) {
                bdtQueryMethod = current;
                break;
            }
        }
        if (bdtQueryMethod == null) {
            throw new CodegenExtensionException("No BDT query method found!");
        }
        
        /*
        String methodSignatureStart = SyncHelper.createUnBoxedSignatureStringFromMethod(
            bdtQueryMethod, getServiceInformation());
        */

        String methodSignatureStart = null;
        // insert the new client method
        if (bdtQueryMethod.isIsImported() && (bdtQueryMethod.getImportInformation().getFromIntroduce() != null)
            && !bdtQueryMethod.getImportInformation().getFromIntroduce().booleanValue()) {
            methodSignatureStart = SyncHelper.createBoxedSignatureStringFromMethod(bdtQueryMethod) + " " 
                + SyncHelper.createClientExceptions(bdtQueryMethod, getServiceInformation());
        } else {
            methodSignatureStart = SyncHelper.createUnBoxedSignatureStringFromMethod(bdtQueryMethod, getServiceInformation()) + " "
                + SyncHelper.createExceptions(bdtQueryMethod, getServiceInformation());
        }
        System.out.println("Searching for method with signature:");
        System.out.println("\t" + methodSignatureStart);
        
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
            System.out.println("Source File Contents:");
            System.out.println(source.toString());
            // edit has never been performed, perform edits
            // find method start
            int methodStart = source.indexOf(methodSignatureStart);
            if (methodStart == -1) {
                throw new CodegenExtensionException("No signature for BDT query method found!");
            }
            int startIndex = source.indexOf(SERVICE_START, methodStart);
            if (startIndex == -1) {
                throw new CodegenExtensionException("BDT implementation insertion point not found!");
            }
            startIndex += SERVICE_START.length();
            // add the source
			source.insert(startIndex, NL);
            startIndex += NL.length();
			source.insert(startIndex, edit.toString());
		}

		// write the source file back out
		try {
			Utils.stringBufferToFile(source, sourceFileName);
		} catch (IOException ex) {
			throw new CodegenExtensionException("Error writing service source file: " + ex.getMessage(), ex);
		}
	}
	
	
	
	private void editBdtResource() throws CodegenExtensionException {		
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
            System.out.println("BDT Resource has never been edited, adding implementation");
            System.out.println("BDT Resource has never been edited, adding implementation");
            System.out.println("BDT Resource has never been edited, adding implementation");
            System.out.println("BDT Resource has never been edited, adding implementation");
            System.out.println("BDT Resource has never been edited, adding implementation");
        }
        
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
		
		// edits for the remove method
		if (doEdits) {
			int startIndex = source.indexOf(RESOURCE_REMOVE_METHOD) + RESOURCE_REMOVE_METHOD.length();
			source.insert(startIndex, NL);
			startIndex += NL.length();
			source.insert(startIndex, RESOURCE_REMOVE_METHOD_IMPL);
			startIndex += RESOURCE_REMOVE_METHOD_IMPL.length();
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
	}
}
