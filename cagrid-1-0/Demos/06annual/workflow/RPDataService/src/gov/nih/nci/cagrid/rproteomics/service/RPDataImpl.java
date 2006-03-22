package gov.nih.nci.cagrid.rproteomics.service;

import edu.duke.cabig.rproteomics.services.RProteomicsDataService;
import edu.duke.cabig.rproteomics.services.impl.RProteomicsDataServiceImpl;
import gov.nih.nci.cagrid.common.Utils;
import gov.nih.nci.cagrid.cql.CQLQueryResultType;
import gov.nih.nci.cagrid.cql.CQLQueryResultsType;
import gov.nih.nci.cagrid.cql2xpath.CqlNode;
import gov.nih.nci.cagrid.cql2xpath.CqlNodeParser;
import gov.nih.nci.cagrid.cql2xpath.CqlXpathConverter;
import gov.nih.nci.cagrid.rproteomics.common.RPDataI;

import java.io.File;
import java.io.FileFilter;
import java.io.IOException;
import java.io.StringReader;
import java.rmi.RemoteException;
import java.util.Hashtable;

import javax.xml.namespace.QName;
import javax.xml.parsers.ParserConfigurationException;

import org.apache.axis.MessageContext;
import org.apache.axis.message.MessageElement;
import org.apache.axis.utils.XMLUtils;
import org.globus.wsrf.utils.ContextUtils;
import org.w3c.dom.Document;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;


/**
 * gov.nih.nci.cagrid.rproteomicsI
 * 
 * @created by Introduce Toolkit version 1.0
 */
public class RPDataImpl 
	implements RPDataI 
{
	private File dataDir;
	private RProteomicsDataService ds;
	private CqlNode[] rootNodes;
	private Hashtable qNameTable;

	public RPDataImpl() 
	{
		try {
			// get the config params for the backend tools
			MessageContext context = MessageContext.getCurrentContext();
	
			// point to a temp dir
			String dataDirProperty = (String) ContextUtils.getServiceProperty(context, "dataDir");
			
			// build data service
			String dbUrlProperty = (String) ContextUtils.getServiceProperty(context, "makoHost");
			String collectionProperty = (String) ContextUtils.getServiceProperty(context, "makoCollection");
			String binaryDataDirProperty = (String) ContextUtils.getServiceProperty(context, "binaryDataDir");
			String cqlMapProperty = (String) ContextUtils.getServiceProperty(context, "cqlMapFile");
			if (dbUrlProperty != null && collectionProperty != null && binaryDataDirProperty != null && cqlMapProperty != null) {
				CqlNodeParser parser = new CqlNodeParser();
				rootNodes = parser.parse(new File(cqlMapProperty));
	
				ds = new RProteomicsDataServiceImpl(dbUrlProperty, collectionProperty, new File(binaryDataDirProperty));
				((RProteomicsDataServiceImpl) ds).startup();
				
				buildQNameTable();
			} else if (dataDirProperty != null) {
				System.out.println("Warning: RProteomicsDataServiceImpl null, using dataDir for RPDataImpl");
				this.dataDir = new File(dataDirProperty);
			} else {
				throw new IllegalArgumentException("RProteomicsDataServiceImpl and dataDir null for RPDataImpl");
			}
		} catch (Exception e) {
			if (e instanceof IllegalArgumentException) throw (IllegalArgumentException) e;
			e.printStackTrace();
			throw new IllegalArgumentException(e.getMessage());
		}
	}

	private void buildQNameTable()
	{
		qNameTable = new Hashtable();
		
		qNameTable.put(
			"edu.duke.cabig.scanFeatures.ScanFeatures",
			new QName("rproteomics.cabig.duke.edu/1/scanFeatures", "scanFeatures")
		);
		qNameTable.put(
			"edu.duke.cabig.scanFeatures.Attributes",
			new QName("rproteomics.cabig.duke.edu/1/scanFeatures", "attributes")
		);
		qNameTable.put(
			"edu.duke.cabig.scanFeatures.Feature",
			new QName("rproteomics.cabig.duke.edu/1/scanFeatures", "feature")
		);
		qNameTable.put(
			"edu.duke.cabig.scanFeatures.Base64",
			new QName("rproteomics.cabig.duke.edu/1/scanFeatures", "base64")
		);
	}
	
	public CQLQueryResultsType query(gov.nih.nci.cagrid.cql.CQLQueryType query) 
		throws RemoteException, gov.nih.nci.cagrid.rproteomics.stubs.MalformedQueryException 
	{
		try {
			if (ds == null && dataDir == null) return null;
			else if (ds == null) return getCachedResults();
			
			CqlXpathConverter converter = new CqlXpathConverter(rootNodes);
			String xpath = converter.toXpath(query);
			String[] xml = ds.query("scanFeatures", xpath);
			
			QName qName = (QName) qNameTable.get(query.getTarget().getName());
			if (qName == null) {
				throw new IllegalArgumentException("no QName found for Target " + query.getTarget().getName());
			}
			
			CQLQueryResultType[] results = new CQLQueryResultType[xml.length];
			for (int i = 0; i < xml.length; i++) {
				Document doc = XMLUtils.newDocument(new InputSource(new StringReader(xml[i])));

				MessageElement msg = new MessageElement(doc.getDocumentElement());
				msg.setType(qName);

				results[i] = new CQLQueryResultType(new MessageElement[]{msg});
			}
			
			return new CQLQueryResultsType(results);		
		} catch (Exception e) {
			throw new RemoteException("Error in query: ", e);
		}
	}
	
	private CQLQueryResultsType getCachedResults() 
		throws Exception
	{
		File[] files = dataDir.listFiles(new FileFilter() {
			public boolean accept(File file) {
				return file.isFile();
			}
		});

		CQLQueryResultType[] results = new CQLQueryResultType[files.length];
		for (int i = 0; i < files.length; i++) {
			System.out.println("RPDataImpl reading file: " + files[i]);

			String xml = Utils.fileToStringBuffer(files[i]).toString();
			Document doc = XMLUtils.newDocument(new InputSource(new StringReader(xml)));

			MessageElement msg = new MessageElement(doc.getDocumentElement());
			msg.setType(new QName("rproteomics.cabig.duke.edu/1/scanFeatures", "scanFeatures"));

			results[i] = new CQLQueryResultType(new MessageElement[]{msg});
		}

		return new CQLQueryResultsType(results);		
	}
}
