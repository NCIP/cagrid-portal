package gov.nih.nci.cagrid.data;

import gov.nih.nci.cagrid.common.Utils;
import gov.nih.nci.cagrid.metadata.dataservice.DomainModel;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.Reader;
import java.io.StringReader;
import java.io.StringWriter;
import java.io.Writer;

/** 
 *  DomainModelSerializer
 *  Because the caCORE team has placed incorrect namespaces in their
 *  classes, I have to write this class to correctly serialize domain
 *  models containing SemanticMetadata classes
 * 
 * @author <A HREF="MAILTO:ervin@bmi.osu.edu">David W. Ervin</A>
 * 
 * @created Jun 23, 2006 
 * @version $Id$ 
 */
public class DomainModelSerializer {

	public static final String WRONG_NS = "gme://caCORE.cabig/3.0/gov.nih.nci.cadsr.umlproject.domain";
	public static final String RIGHT_NS = "gme://caCORE.caBIG/3.0/gov.nih.nci.cadsr.umlproject.domain";
	
	public static void serializeDomainModel(DomainModel model, 
		Writer outputWriter, String wsddLocation) throws Exception {
		StringWriter writer = new StringWriter();
		Utils.serializeObject(model, DataServiceConstants.DOMAIN_MODEL_QNAME, writer,
			new FileInputStream(wsddLocation));
		StringBuffer xml = writer.getBuffer();
		fixWrongNs(xml);
		outputWriter.write(xml.toString());
	}
	
	
	public static DomainModel deserializeDomainModel(Reader inputReader, String wsddLocation) throws Exception {
		BufferedReader inStringReader = new BufferedReader(inputReader);
		StringBuffer buffer = new StringBuffer();
		String line = null;
		while ((line = inStringReader.readLine()) != null) {
			buffer.append(line);
		}
		fixWrongNs(buffer);
		DomainModel model = (DomainModel) Utils.deserializeObject(
			new StringReader(buffer.toString()), DomainModel.class, new FileInputStream(wsddLocation));
		return model;
	}
	
	
	private static void fixWrongNs(StringBuffer xml) {
		int index = 0;
		while ((index = xml.indexOf(WRONG_NS, index)) != -1) {
			xml.replace(index, index + WRONG_NS.length(), RIGHT_NS);
		}
	}
}
