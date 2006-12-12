package gov.nih.nci.cagrid.caarray;

import java.io.FileWriter;
import java.io.InputStream;
import java.io.StringWriter;

import javax.xml.parsers.DocumentBuilderFactory;

import org.apache.axis.message.MessageElement;
import org.exolab.castor.mapping.Mapping;
import org.exolab.castor.xml.Marshaller2;
import org.globus.wsrf.utils.XmlUtils;
import org.w3c.dom.Document;
import org.xml.sax.EntityResolver;
import org.xml.sax.InputSource;

import gov.nih.nci.common.search.SearchResult;
import gov.nih.nci.common.search.session.SecureSession;
import gov.nih.nci.common.search.session.SecureSessionFactory;
import gov.nih.nci.mageom.domain.BioAssay.BioAssay;
import gov.nih.nci.mageom.domain.BioAssay.MeasuredBioAssay;
import gov.nih.nci.mageom.domain.BioAssayData.BioDataCube;
import gov.nih.nci.mageom.domain.BioAssayData.MeasuredBioAssayData;
import gov.nih.nci.mageom.domain.Experiment.Experiment;
import gov.nih.nci.mageom.search.SearchCriteriaFactory;
import gov.nih.nci.mageom.search.BioAssayData.BioDataCubeSearchCriteria;
import gov.nih.nci.mageom.search.BioAssayData.MeasuredBioAssayDataSearchCriteria;
import gov.nih.nci.mageom.search.Experiment.ExperimentSearchCriteria;

public class TestMAGEOMClient {
	public static void main(String[] args) throws Exception {
		try {

			String usr = "PUBLIC";
			String pwd = "";
			SecureSession sess = SecureSessionFactory.defaultSecureSession();
			sess.start(usr, pwd);
			String sessId = sess.getSessionId();
			System.out.println("sessId=" + sessId);

			ExperimentSearchCriteria sc = SearchCriteriaFactory
					.new_EXPERIMENT_EXPERIMENT_SC();
			// BioDataCubeSearchCriteria sc =
			// SearchCriteriaFactory.new_BIOASSAYDATA_BIODATACUBE_SC();
			
			
			sc.setSessionId(sessId);
			sc.setMaxRecordset(10);
			// sc
			// .setIdentifier("gov.nih.nci.ncicb.caarray:Experiment:1015897558050098:1");

			sc
					.setIdentifier("gov.nih.nci.ncicb.caarray:Experiment:1015897536136716:1");
			SearchResult sr = sc.search();
			Experiment[] results = (Experiment[]) sr.getResultSet();
			// BioDataCubeSearchCriteria[] results =
			// (BioDataCubeSearchCriteria[])sr.getResultSet();

//			FileWriter w = new FileWriter("out.txt");
			System.out.println("results.length=" + results.length);

			BioAssay[] assays = results[0].getBioAssays();
			for (int i = 0; i < assays.length; i++) {
				if (assays[i] instanceof MeasuredBioAssay) {
					MeasuredBioAssay mAssay = (MeasuredBioAssay) assays[i];
					
					MeasuredBioAssayData[] data = mAssay
							.getMeasuredBioAssayData();
					
					if (data != null && data.length == 1) {
						
						System.out.println("MeasuredBioAssayData.identifier: " + data[0].getIdentifier());
						
						
						
						BioDataCube cube = (BioDataCube) data[0]
						      								.getBioDataValues();
						int bLength = data[0].getBioAssayDimension().getBioAssays().length;
						int qLength = data[0].getQuantitationTypeDimension().getQuantitationTypes().length;
						String order = cube.getOrder();
						Object[][][] oCube = cube.getCube();
						
						int firstLength = oCube.length;
						int secondLength = oCube[0].length;
						int thirdLength = oCube[0][0].length;
						
						System.out.println("bLength = " + bLength);
						System.out.println("qLength = " + qLength);
						System.out.println("firstLength = " + firstLength);
						System.out.println("secondLength = " + secondLength);
						System.out.println("thirdLength = " + thirdLength);
						System.out.println("order = " + order);
						
//						String str = getCubeAsString(oCube);
						// System.out.println(str);
//						w.write(str);
						break;
					} else {
						// System.out.println("data.length");
					}
				}
			}
//			w.flush();
//			w.close();

			// FileWriter w = new
			// FileWriter("experiment-1015897558050098-1.xml");
			// String xml = toXML(results[0]);
			// w.write(xml);
			// w.flush();
			// w.close();
			sess.end();
		} catch (Exception ex) {
			throw new RuntimeException("Error: " + ex.getMessage(), ex);
		}
	}

	public static String getCubeAsString(Object[][][] cube) {
		StringBuffer sb = new StringBuffer();
		Object value = null;
		for (int i = 0; i < cube.length; i++) {
			for (int j = 0; j < cube[i].length; j++) {
				for (int k = 0; k < cube[i][j].length; k++) {
					value = cube[i][j][k];
					if (value == null
							|| (value instanceof Double && ((Double) value)
									.isInfinite())) {
						// if it's an INFINITY or null, it's a flag for invalid
						// or blank value. Just write blank if so. tranp
						sb.append("");
					} else {
						sb.append(value);
					}
					sb.append("\t");
				}
				sb.setLength(sb.length() - 1); // Remove the last tab
				sb.append("\n");
			}
		}
		return sb.toString();
	}

	public static String toXML(Object beanObject) throws Exception {

		StringWriter w = new StringWriter();

		EntityResolver resolver = new EntityResolver() {
			public InputSource resolveEntity(String publicId, String systemId) {
				if (publicId
						.equals("-//EXOLAB/Castor Object Mapping DTD Version 1.0//EN")) {
					InputStream in = Thread.currentThread()
							.getContextClassLoader().getResourceAsStream(
									"mapping.dtd");
					return new InputSource(in);
				}
				return null;
			}
		};
		org.xml.sax.InputSource mappIS = new org.xml.sax.InputSource(Thread
				.currentThread().getContextClassLoader().getResourceAsStream(
						"caarray-xml-mapping.xml"));
		Mapping mapping = new Mapping();
		mapping.setEntityResolver(resolver);
		mapping.loadMapping(mappIS);

		Marshaller2 marshaller = null;
		Marshaller2.enableDebug = true;

		Document doc = DocumentBuilderFactory.newInstance()
				.newDocumentBuilder().newDocument();
		marshaller = new Marshaller2(doc);
		marshaller.getXpaths().add("\\/\\w+\\/\\w+");
		marshaller.getXpaths().add("\\/\\w+\\/\\w+\\/\\w+\\/@.*");
		marshaller.getXpaths().add("\\/\\w+\\/@.*");
		marshaller.getXpaths().add("\\/\\w+\\/descriptions.*");

		marshaller.setMapping(mapping);
		marshaller.setMarshalAsDocument(false);
		marshaller.setValidation(false);
		marshaller.marshal(beanObject);

		MessageElement me = new MessageElement(doc.getDocumentElement());

		return XmlUtils.toString(me);

	}
}
