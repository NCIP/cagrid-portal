package gov.nih.nci.cagrid.data.creation;

import gov.nih.nci.cagrid.common.Utils;
import gov.nih.nci.cagrid.common.portal.PortalUtils;
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
import gov.nih.nci.cagrid.introduce.creator.extension.CreationExtensionException;
import gov.nih.nci.cagrid.introduce.creator.extension.CreationExtensionPostProcessor;
import gov.nih.nci.cagrid.introduce.portal.IntroducePortalConf;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.util.List;
import java.util.Properties;

import javax.xml.namespace.QName;

import org.jdom.Document;
import org.jdom.input.SAXBuilder;
import org.projectmobius.client.gme.ImportInfo;
import org.projectmobius.common.GridServiceResolver;
import org.projectmobius.common.MobiusException;
import org.projectmobius.common.Namespace;
import org.projectmobius.gme.XMLDataModelService;
import org.projectmobius.gme.client.GlobusGMEXMLDataModelServiceFactory;
import org.projectmobius.portal.PortalResourceManager;

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
	
	public static final String CQL_QUERY_URI = "http://CQL.caBIG/1/gov.nih.nci.cagrid.CQLQuery";
	public static final String CQL_RESULT_SET_URI = "http://CQL.caBIG/3/gov.nih.nci.cagrid.CQLResultSet";

	public void postCreate(Properties serviceProperties) throws CreationExtensionException {
		// load the introduce template stuff
		ServiceDescription description = null;
		String templateFilename = serviceProperties.getProperty(IntroduceConstants.INTRODUCE_SKELETON_DESTINATION_DIR) 
			+ File.separator + IntroduceConstants.INTRODUCE_XML_FILE;
		try {
			System.out.println("Loading existing introduce template");
			description = (ServiceDescription) Utils.deserializeDocument(
				templateFilename, ServiceDescription.class);
		} catch (Exception ex) {
			throw new CreationExtensionException("Error loading service template!", ex);
		}
		// apply data service requirements to it
		try {
			System.out.println("Adding data service components to template");
			makeDataService(description,serviceProperties);
		} catch (Exception ex) {
			throw new CreationExtensionException("Error adding data service components to template!", ex);
		}
		// save it back to disk
		try {
			System.out.println("Saving service description back to service directory");
			Utils.serializeDocument(templateFilename, description, IntroduceConstants.INTRODUCE_SKELETON_QNAME);
		} catch (Exception ex) {
			throw new CreationExtensionException("Error saving data service description!", ex);
		}
	}
	
	
	private void makeDataService(ServiceDescription description, Properties props) throws Exception {
		// grab cql query and result set schemas from GME
		String schemaDir = getServiceSchemaDir(props);
		String cqlQuerySchemaLocation = cacheSchema(CQL_QUERY_URI, schemaDir);
		String cqlResultSetSchemaLocation = cacheSchema(CQL_RESULT_SET_URI, schemaDir);
		// namespaces
		NamespacesType namespaces = description.getNamespaces();
		NamespaceType[] dsNamespaces = new NamespaceType[namespaces.getNamespace().length + 2];
		System.arraycopy(namespaces.getNamespace(), 0, dsNamespaces, 0, namespaces.getNamespace().length);
		dsNamespaces[dsNamespaces.length - 2] = CommonTools.createNamespaceType(schemaDir + File.separator + cqlQuerySchemaLocation);
		dsNamespaces[dsNamespaces.length - 2].setLocation("." + File.separator + cqlQuerySchemaLocation);
		dsNamespaces[dsNamespaces.length - 1] = CommonTools.createNamespaceType(schemaDir + File.separator + cqlResultSetSchemaLocation);
		dsNamespaces[dsNamespaces.length - 1].setLocation("." + File.separator + cqlResultSetSchemaLocation);
		namespaces.setNamespace(dsNamespaces);
		
		// query method
		MethodsType methods = description.getMethods();
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
		MethodType[] dsMethods = new MethodType[methods.getMethod().length + 1];
		System.arraycopy(methods.getMethod(), 0, dsMethods, 0, methods.getMethod().length);
		dsMethods[dsMethods.length - 1] = queryMethod;
		methods.setMethod(dsMethods);
	}
	
	
	private String getServiceSchemaDir(Properties props) {
		return props.getProperty(IntroduceConstants.INTRODUCE_SKELETON_DESTINATION_DIR) + File.separator + "schema";
	}
	
	
	private String cacheSchema(String uri, String directory) throws Exception {
		System.out.println("Retrieving schema " + uri);
		String cachedFilename = null;
		IntroducePortalConf conf = (IntroducePortalConf) PortalResourceManager.getInstance().getResource(
			IntroducePortalConf.RESOURCE);
		GridServiceResolver.getInstance().setDefaultFactory(new GlobusGMEXMLDataModelServiceFactory());
		try {
			XMLDataModelService handle = (XMLDataModelService) GridServiceResolver.getInstance().getGridService(
				conf.getGME());
			List retrievedNamespaces = handle.cacheSchema(new Namespace(uri), new File(directory));
			// determine which of those files is the one we originally asked for
			for (int i = 0; i < retrievedNamespaces.size(); i++) {
				ImportInfo nsInfo = new ImportInfo((Namespace) retrievedNamespaces.get(i));
				String xsdFilename = directory + File.separator + nsInfo.getFileName();
				String targetNamespace = extractTargetNamespace(xsdFilename);
				Namespace requestedNs = new Namespace(uri);
				Namespace targetNs = new Namespace(targetNamespace);
				if (requestedNs.getNamespace().equals(targetNs.getNamespace())) {
					cachedFilename = xsdFilename;
					break;
				}
			}
		} catch (MobiusException ex) {
			ex.printStackTrace();
			PortalUtils.showErrorMessage("Could not retrieve schema from GME!  Please check the GME URL and make sure that you have the appropriate credentials!");
		}
		return cachedFilename;
	}
	
	
	private String extractTargetNamespace(String filename) throws Exception {
		String fileContents = Utils.fileToStringBuffer(new File(filename)).toString();
		ByteArrayInputStream stream = new ByteArrayInputStream(fileContents.getBytes());
		SAXBuilder builder = new SAXBuilder(false);
		Document doc = builder.build(stream);
		stream.close();
		return doc.getRootElement().getAttributeValue("targetNamespace");
	}
}