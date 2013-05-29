/**
*============================================================================
*  The Ohio State University Research Foundation, The University of Chicago -
*  Argonne National Laboratory, Emory University, SemanticBits LLC, 
*  and Ekagra Software Technologies Ltd.
*
*  Distributed under the OSI-approved BSD 3-Clause License.
*  See http://ncip.github.com/cagrid-core/LICENSE.txt for details.
*============================================================================
**/
/**
 * 
 */
package gov.nih.nci.cagrid.portal.util;

import gov.nih.nci.cagrid.common.Utils;
import gov.nih.nci.cagrid.metadata.ServiceMetadata;
import gov.nih.nci.cagrid.metadata.dataservice.DomainModel;
import gov.nih.nci.cagrid.portal.aggr.regsvc.RegisteredServiceEvent;
import gov.nih.nci.cagrid.portal.aggr.regsvc.RegisteredServiceListener;
import gov.nih.nci.cagrid.portal.dao.GridServiceDao;
import gov.nih.nci.cagrid.portal.dao.IndexServiceDao;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FilenameFilter;
import java.io.StringReader;
import java.util.HashMap;
import java.util.Map;

import org.apache.axis.message.addressing.EndpointReferenceType;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.FileSystemXmlApplicationContext;
import org.springframework.transaction.annotation.Transactional;

/**
 * @author <a href="mailto:joshua.phillips@semanticbits.com">Joshua Phillips</a>
 * 
 */
@Transactional
public class LoadStaticMetadata {
	
	private static final Log logger = LogFactory.getLog(LoadStaticMetadata.class);

	public static void main(String[] args) throws Exception {

		final String indexServiceUrl = "http://cagrid-index.nci.nih.gov:8080/wsrf/services/DefaultIndexService";
		String inDirPath = "test/data/metadata";

		ApplicationContext ctx = new FileSystemXmlApplicationContext(
				new String[] { "../db/etc/applicationContext-db-aspects.xml",
						"../db/etc/applicationContext-db-relationships.xml",
						"test/etc/applicationContext-aggr-util.xml",
						"etc/applicationContext-aggr.xml" });
		LoadStaticMetadata l = (LoadStaticMetadata) ctx.getBean("loadStaticMetadataPrototype");
		l.load(ctx, inDirPath, indexServiceUrl);
	}

	public void load(final ApplicationContext ctx, final String inDirPath,
			final String indexServiceUrl) {
		final Map<String, String> url2HashMap = new HashMap<String, String>();

		File inDir = new File(inDirPath);
		final File eprDir = new File(inDir.getAbsolutePath() + "/eprs");
		final File metaDir = new File(inDir.getAbsolutePath() + "/meta");
		File[] eprFiles = eprDir.listFiles(new FilenameFilter() {
			public boolean accept(File dir, String filename) {
				return filename.endsWith(".xml");
			}
		});
		for (File eprFile : eprFiles) {
			try {
				BufferedReader r = new BufferedReader(new FileReader(eprFile));
				StringBuilder sb = new StringBuilder();
				String line = null;
				while ((line = r.readLine()) != null) {
					sb.append(line);
				}
				r.close();
				// See here:
				// http://bugzilla.globus.org/globus/show_bug.cgi?format=multiple&id=1848
				String eprXml = sb.toString().replace(
						"xsi:type=\"xsd:anyURI\"", "");
				
				EndpointReferenceType epr = (EndpointReferenceType) Utils
						.deserializeObject(new StringReader(eprXml),
								EndpointReferenceType.class);

				String eprFileName = eprFile.getName();
				String hash = eprFileName
						.substring(0, eprFileName.indexOf("."));
				String url = epr.getAddress().toString();
				url2HashMap.put(url, hash);
			} catch (Exception ex) {
				throw new RuntimeException("Error reading EPR file: "
						+ eprFile.getAbsolutePath() + ": " + ex.getMessage(),
						ex);
			}
		}

		final RegisteredServiceListener rsl = new RegisteredServiceListener();
		rsl.setApplicationContext(ctx);
		rsl.setGridServiceDao((GridServiceDao) ctx.getBean("gridServiceDao"));
		rsl
				.setIndexServiceDao((IndexServiceDao) ctx
						.getBean("indexServiceDao"));
		rsl.setMetadataUtils(new MetadataUtils() {
			@Override
			public Metadata getMetadata(String serviceUrl, long timeout) {
				Metadata meta = new Metadata();

				String hash = url2HashMap.get(serviceUrl);
				File smetaFile = new File(metaDir.getAbsolutePath() + "/"
						+ hash + "-smeta.xml");
				File dmodelFile = new File(metaDir.getAbsolutePath() + "/"
						+ hash + "-dmeta.xml");

				try {
					FileReader r = new FileReader(smetaFile);
					meta.smeta = (ServiceMetadata) Utils.deserializeObject(r,
							ServiceMetadata.class);
					r.close();
					if (dmodelFile.exists()) {
						r = new FileReader(dmodelFile);
						meta.dmodel = (DomainModel) Utils.deserializeObject(r,
								DomainModel.class);
						r.close();
					}
				} catch (Exception ex) {
					throw new RuntimeException("Error deserializing metadata: "
							+ ex.getMessage(), ex);
				}
				return meta;
			}
		});

		for (String serviceUrl : url2HashMap.keySet()) {
			RegisteredServiceEvent evt = new RegisteredServiceEvent("yadda");
			evt.setIndexServiceUrl(indexServiceUrl);
			evt.setServiceUrl(serviceUrl);
			try {
				rsl.persistService(evt);
			} catch (Exception ex) {
				logger.error("Error persisting: " + evt.getServiceUrl() + ": " + ex.getMessage(), ex);
			}
		}

	}
}
