package gov.nih.nci.cagrid.data.creation;

import gov.nih.nci.cagrid.data.common.DataServiceConstants;
import gov.nih.nci.cagrid.introduce.IntroduceConstants;
import gov.nih.nci.cagrid.introduce.beans.ServiceDescription;
import gov.nih.nci.cagrid.introduce.beans.method.MethodType;
import gov.nih.nci.cagrid.introduce.beans.method.MethodTypeInputs;
import gov.nih.nci.cagrid.introduce.beans.method.MethodTypeInputsInput;
import gov.nih.nci.cagrid.introduce.beans.method.MethodTypeOutput;
import gov.nih.nci.cagrid.introduce.beans.method.MethodsType;
import gov.nih.nci.cagrid.introduce.beans.namespace.NamespaceType;
import gov.nih.nci.cagrid.introduce.beans.namespace.NamespacesType;
import gov.nih.nci.cagrid.introduce.common.CommonTools;
import gov.nih.nci.cagrid.introduce.extension.CreationExtensionException;
import gov.nih.nci.cagrid.introduce.extension.CreationExtensionPostProcessor;
import gov.nih.nci.cagrid.introduce.extension.ExtensionsLoader;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Properties;

import javax.xml.namespace.QName;

/** 
 *  DataServiceCreationPostProcessor
 *  Creation post-processor for data services
 * 
 * @author <A HREF="MAILTO:ervin@bmi.osu.edu">David W. Ervin</A>
 * 
 * @created Mar 29, 2006 
 * @version $Id$ 
 */
public class DataServiceCreationPostProcessor implements CreationExtensionPostProcessor {
	
	public void postCreate(ServiceDescription serviceDescription, Properties serviceProperties) throws CreationExtensionException {
		// apply data service requirements to it
		try {
			System.out.println("Adding data service components to template");
			makeDataService(serviceDescription, serviceProperties);
		} catch (Exception ex) {
			throw new CreationExtensionException("Error adding data service components to template!", ex);
		}
	}
	
	
	private void makeDataService(ServiceDescription description, Properties props) throws Exception {
		// grab cql query and result set schemas and move them into the service's directory
		String schemaDir = getServiceSchemaDir(props);
		System.out.println("Copying schemas to " + schemaDir);
		copySchema(DataServiceConstants.CQL_QUERY_SCHEMA, schemaDir);
		copySchema(DataServiceConstants.CQL_RESULT_SET_SCHEMA, schemaDir);
		// namespaces
		System.out.println("Modifying namespace definitions");
		NamespacesType namespaces = description.getNamespaces();
		if (namespaces == null) {
			namespaces = new NamespacesType();
		}
		NamespaceType[] dsNamespaces = new NamespaceType[namespaces.getNamespace().length + 2];
		System.arraycopy(namespaces.getNamespace(), 0, dsNamespaces, 0, namespaces.getNamespace().length);
		dsNamespaces[dsNamespaces.length - 2] = CommonTools.createNamespaceType(schemaDir + File.separator + DataServiceConstants.CQL_QUERY_SCHEMA);
		dsNamespaces[dsNamespaces.length - 2].setLocation("." + File.separator + DataServiceConstants.CQL_QUERY_SCHEMA);
		dsNamespaces[dsNamespaces.length - 1] = CommonTools.createNamespaceType(schemaDir + File.separator + DataServiceConstants.CQL_RESULT_SET_SCHEMA);
		dsNamespaces[dsNamespaces.length - 1].setLocation("." + File.separator + DataServiceConstants.CQL_RESULT_SET_SCHEMA);
		namespaces.setNamespace(dsNamespaces);
		description.setNamespaces(namespaces);
		
		// query method
		System.out.println("Building query method");
		MethodsType methods = description.getMethods();
		if (methods == null) {
			methods = new MethodsType();
		}
		MethodType queryMethod = new MethodType();
		queryMethod.setName("query");
		// method input parameters
		MethodTypeInputs inputs = new MethodTypeInputs();
		MethodTypeInputsInput queryInput = new MethodTypeInputsInput();
		queryInput.setName("cqlQuery");
		queryInput.setIsArray(false);
		QName queryQname = new QName(dsNamespaces[dsNamespaces.length - 2].getNamespace(), dsNamespaces[dsNamespaces.length - 2].getSchemaElement(0).getType());
		queryInput.setQName(queryQname);
		inputs.setInput(new MethodTypeInputsInput[] {queryInput});
		queryMethod.setInputs(inputs);
		// method output
		MethodTypeOutput output = new MethodTypeOutput();
		output.setIsArray(false);
		QName resultSetQName = new QName(dsNamespaces[dsNamespaces.length - 1].getNamespace(), dsNamespaces[dsNamespaces.length - 1].getSchemaElement(0).getType());
		output.setQName(resultSetQName);
		queryMethod.setOutput(output);
		MethodType[] dsMethods = null;
		if (methods.getMethod() != null) {
			dsMethods = new MethodType[methods.getMethod().length + 1];
			System.arraycopy(methods.getMethod(), 0, dsMethods, 0, methods.getMethod().length);
		} else {
			dsMethods = new MethodType[1];
		}
		dsMethods[dsMethods.length - 1] = queryMethod;
		methods.setMethod(dsMethods);
		description.setMethods(methods);
	}
	
	
	private String getServiceSchemaDir(Properties props) {
		return props.getProperty(IntroduceConstants.INTRODUCE_SKELETON_DESTINATION_DIR) + File.separator + "schema" + File.separator + props.getProperty(IntroduceConstants.INTRODUCE_SKELETON_SERVICE_NAME);
	}
	
	
	private void copySchema(String schemaName, String outputDir) throws FileNotFoundException, IOException {
		File schemaFile = new File(getExtensionDirectory() + File.separator + "schema" + File.separator + schemaName);
		System.out.println("Loading schema from " + schemaFile.getAbsolutePath());
		File outputDirFile = new File(outputDir);
		System.out.println("Saving schema to " + outputDirFile.getAbsolutePath() + File.separator + schemaName);
		BufferedInputStream schemaStream =  new BufferedInputStream(new FileInputStream(schemaFile));
		FileOutputStream saveSchema = new FileOutputStream(outputDir + File.separator + schemaName);
		byte[] buffer = new byte[512];
		int length = -1;
		while ((length = schemaStream.read(buffer)) != -1) {
			saveSchema.write(buffer, 0, length);
		}
		saveSchema.flush();
		saveSchema.close();
		schemaStream.close();
	}
	
	
	private String getExtensionDirectory() {
		return ExtensionsLoader.EXTENSIONS_DIRECTORY + File.separator + "data";
	}
}