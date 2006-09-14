package gov.nih.nci.cagrid.fqp.common;

import gov.nih.nci.cagrid.common.Utils;
import gov.nih.nci.cagrid.cqlquery.CQLQuery;
import gov.nih.nci.cagrid.dcql.DCQLQuery;

import java.io.InputStream;
import java.io.Writer;

import org.apache.axis.utils.XMLUtils;
import org.globus.wsrf.encoding.ObjectDeserializer;


/**
 * @author oster
 * 
 */
public class SerializationUtils {

	/**
	 * Write the XML representation of the specified metadata to the specified
	 * writer. If either are null, an IllegalArgumentException will be thown.
	 * 
	 * @param domainModel
	 * @param writer
	 * @throws Exception
	 */
	public static void serializeCQLQuery(CQLQuery cqlQuery, Writer writer) throws Exception {
		if (cqlQuery == null || writer == null) {
			throw new IllegalArgumentException("Null is not a valid argument");
		}
		Utils.serializeObject(cqlQuery, DCQLConstants.CQL_QUERY_QNAME, writer);
	}


	/**
	 * Create an instance of CQLQuery from the specified inputstream. The stream
	 * must contains an XML representation of the CQLQuery. If the reader is
	 * null, an IllegalArgumentException will be thown.
	 * 
	 * @param xmlStream
	 * @return an instance of CQLQuery from the specified inputstream.
	 * @throws Exception
	 *             on null argument or deserialization failure
	 */
	public static CQLQuery deserializeCQLQuery(InputStream xmlStream) throws Exception {
		if (xmlStream == null) {
			throw new IllegalArgumentException("Null is not a valid argument");
		}

		org.w3c.dom.Document doc = XMLUtils.newDocument(xmlStream);
		return (CQLQuery) ObjectDeserializer.toObject(doc.getDocumentElement(), CQLQuery.class);
	}


	/**
	 * Write the XML representation of the specified metadata to the specified
	 * writer. If either are null, an IllegalArgumentException will be thown.
	 * 
	 * @param domainModel
	 * @param writer
	 * @throws Exception
	 */
	public static void serializeDCQLQuery(DCQLQuery dcqlQuery, Writer writer) throws Exception {
		if (dcqlQuery == null || writer == null) {
			throw new IllegalArgumentException("Null is not a valid argument");
		}
		Utils.serializeObject(dcqlQuery, DCQLConstants.DCQL_QUERY_QNAME, writer);
	}


	/**
	 * Create an instance of DCQLQuery from the specified inputstream. The
	 * stream must contains an XML representation of the DCQLQuery. If the
	 * reader is null, an IllegalArgumentException will be thown.
	 * 
	 * @param xmlStream
	 * @return an instance of DCQLQuery from the specified inputstream.
	 * @throws Exception
	 *             on null argument or deserialization failure
	 */
	public static DCQLQuery deserializeDCQLQuery(InputStream xmlStream) throws Exception {
		if (xmlStream == null) {
			throw new IllegalArgumentException("Null is not a valid argument");
		}

		org.w3c.dom.Document doc = XMLUtils.newDocument(xmlStream);
		return (DCQLQuery) ObjectDeserializer.toObject(doc.getDocumentElement(), DCQLQuery.class);
	}
}
