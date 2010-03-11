package gov.nih.nci.cagrid.portal.util;

import gov.nih.nci.cagrid.portal.aggr.regsvc.QueryThread;
import gov.nih.nci.cagrid.portal.aggr.regsvc.RegisteredServiceEvent;
import gov.nih.nci.cagrid.portal.aggr.regsvc.RegisteredServiceListener;
import gov.nih.nci.cagrid.portal.dao.GridServiceDao;
import gov.nih.nci.cagrid.portal.dao.IndexServiceDao;

import org.apache.axis.message.addressing.EndpointReferenceType;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.FileSystemXmlApplicationContext;
import org.springframework.transaction.annotation.Transactional;

@Transactional
public class LoadDynamicMetadata {

	private static final Log logger = LogFactory
			.getLog(LoadDynamicMetadata.class);

	public static void main(String[] args) throws Exception {

		ApplicationContext ctx = new FileSystemXmlApplicationContext(
				new String[] { "../db/etc/applicationContext-db-aspects.xml",
						"../db/etc/applicationContext-db-relationships.xml",
						"test/etc/applicationContext-aggr-util.xml",
						"etc/applicationContext-aggr.xml" });
		LoadDynamicMetadata l = (LoadDynamicMetadata) ctx
				.getBean("loadDynamicMetadataPrototype");
		l.load(ctx, args[0]);
	}

	public void load(ApplicationContext ctx, String indexServiceUrl)
			throws Exception {

		long timeout = 30000L;
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

		RegisteredServiceListener rsl = new RegisteredServiceListener();
		rsl.setApplicationContext(ctx);
		rsl.setGridServiceDao((GridServiceDao) ctx.getBean("gridServiceDao"));
		rsl
				.setIndexServiceDao((IndexServiceDao) ctx
						.getBean("indexServiceDao"));
		rsl.setMetadataUtils(new MetadataUtils());

		for (EndpointReferenceType epr : t.getEprs()) {
			String serviceUrl = epr.getAddress().toString();
			RegisteredServiceEvent evt = new RegisteredServiceEvent("yadda");
			evt.setIndexServiceUrl(indexServiceUrl);
			evt.setServiceUrl(serviceUrl);
			try {
				rsl.persistService(evt);
			} catch (Exception ex) {
				logger.error("Error persisting: " + evt.getServiceUrl() + ": "
						+ ex.getMessage(), ex);
			}
		}

	}

}
