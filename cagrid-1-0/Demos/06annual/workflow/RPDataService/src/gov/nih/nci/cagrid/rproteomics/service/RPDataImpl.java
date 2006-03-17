package gov.nih.nci.cagrid.rproteomics.service;

import gov.nih.nci.cagrid.common.Utils;
import gov.nih.nci.cagrid.cql.CQLQueryResultType;
import gov.nih.nci.cagrid.cql.CQLQueryResultsType;
import gov.nih.nci.cagrid.rproteomics.common.RPDataI;

import java.io.File;
import java.io.FileFilter;
import java.io.StringReader;
import java.rmi.RemoteException;

import javax.xml.namespace.QName;

import org.apache.axis.MessageContext;
import org.apache.axis.message.MessageElement;
import org.apache.axis.utils.XMLUtils;
import org.globus.wsrf.utils.ContextUtils;
import org.w3c.dom.Document;
import org.xml.sax.InputSource;


/**
 * gov.nih.nci.cagrid.rproteomicsI
 * 
 * @created by Introduce Toolkit version 1.0
 */
public class RPDataImpl implements RPDataI {
	private File dataDir;


	public RPDataImpl() {
		// get the config params for the backend tools
		MessageContext context = MessageContext.getCurrentContext();

		// point to a temp dir
		String dataDirProperty = (String) ContextUtils.getServiceProperty(context, "dataDir");
		this.dataDir = new File(dataDirProperty);
	}


	public CQLQueryResultsType query(gov.nih.nci.cagrid.cql.CQLQueryType query) throws RemoteException,
		gov.nih.nci.cagrid.rproteomics.stubs.MalformedQueryException {
		try {
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
		} catch (Exception e) {
			throw new RemoteException("Error in query: ", e);
		}
	}

}
