/**
 * 
 */
package gov.nih.nci.cagrid.portal.util;

import gov.nih.nci.cagrid.common.Utils;
import gov.nih.nci.cagrid.portal.aggr.regsvc.QueryThread;

import java.io.File;
import java.io.FileWriter;
import java.io.StringWriter;

import javax.xml.namespace.QName;

import org.apache.axis.message.addressing.EndpointReferenceType;

/**
 * @author <a href="mailto:joshua.phillips@semanticbits.com">Joshua Phillips</a>
 * 
 */
public class DownloadMetadata {

	/**
	 * @param args
	 */
	public static void main(String[] args) throws Exception {

		MetadataUtils metaUtil = new MetadataUtils();
		long timeout = 30000L;

		// String indexServiceUrl = args[0];
		// String outDirPath = args[1];
		String indexServiceUrl = "http://cagrid-index.nci.nih.gov:8080/wsrf/services/DefaultIndexService";
		String outDirPath = "out";

		QueryThread t = new QueryThread(indexServiceUrl, true);
		t.start();
		try {
			t.join(timeout);
		} catch (InterruptedException ex) {
			throw new RuntimeException("Index query thread interrupted");
		}

		if (t.getEx() != null) {
			throw new RuntimeException("Error querying index service: "
					+ t.getEx().getMessage(), t.getEx());
		}

		if (!t.isFinished()) {
			throw new RuntimeException(
					"Index query thread timed out (timeout = " + timeout + ").");
		}

		File outDir = new File(outDirPath);
		if (!outDir.exists()) {
			outDir.mkdirs();
		}
		File eprsDir = new File(outDir.getAbsolutePath() + "/eprs");
		if (!eprsDir.exists()) {
			eprsDir.mkdirs();
		}
		File metaDir = new File(outDir.getAbsolutePath() + "/meta");
		if (!metaDir.exists()) {
			metaDir.mkdirs();
		}

		EndpointReferenceType[] eprs = t.getEprs();

		if (eprs != null && eprs.length > 0) {
			System.out.println("Got " + eprs.length + " services.");
			int idx = 1;
			for (EndpointReferenceType epr : eprs) {

				String url = epr.getAddress().toString();
				System.out.println("Service[" + idx++ + "]: " + url);

				Metadata meta = null;
				try {
					meta = metaUtil.getMetadata(url, timeout);
				} catch (Exception ex) {
					ex.printStackTrace();
					continue;
				}

				StringWriter sw = new StringWriter();
				Utils.serializeObject(epr, new QName(
						"http://schemas.xmlsoap.org/ws/2004/03/addressing",
						"EndpointReferenceType"), sw);
				String xml = sw.getBuffer().toString();
				String hash = PortalUtils.createHash(xml);

				System.out.println();

				FileWriter w = new FileWriter(eprsDir.getAbsolutePath() + "/"
						+ hash + ".xml");
				w.write(xml);
				w.flush();
				w.close();

				w = new FileWriter(metaDir.getAbsolutePath() + "/" + hash
						+ "-smeta.xml");
				gov.nih.nci.cagrid.metadata.MetadataUtils
						.serializeServiceMetadata(meta.smeta, w);
				w.flush();
				w.close();

				if (meta.dmodel != null) {
					w = new FileWriter(metaDir.getAbsolutePath() + "/" + hash
							+ "-dmeta.xml");
					gov.nih.nci.cagrid.metadata.MetadataUtils
							.serializeDomainModel(meta.dmodel, w);
					w.flush();
					w.close();
				}
			}

		} else {
			System.out.println("No EPRs retrieved");
		}

	}

}
