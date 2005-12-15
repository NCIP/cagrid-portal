package gov.nih.nci.cagrid.dorian.ca.tools;

import gov.nih.nci.cagrid.dorian.ca.DorianCertificateAuthority;
import gov.nih.nci.cagrid.dorian.ca.DorianCertificateAuthorityConf;
import gov.nih.nci.cagrid.dorian.common.Database;
import gov.nih.nci.cagrid.dorian.common.SimpleResourceManager;
import gov.nih.nci.cagrid.dorian.common.ca.CertUtil;
import gov.nih.nci.cagrid.dorian.service.DorianConfiguration;

/**
 * @author <A href="mailto:langella@bmi.osu.edu">Stephen Langella </A>
 * @author <A href="mailto:oster@bmi.osu.edu">Scott Oster </A>
 * @author <A href="mailto:hastings@bmi.osu.edu">Shannon Hastings </A>
 * @version $Id: ArgumentManagerTable.java,v 1.2 2004/10/15 16:35:16 langella
 *          Exp $
 */
public class WriteCACertificate {


	public static void main(String[] args) {


		try {
		
				String configFile = "etc/dorian-conf.xml";
				SimpleResourceManager rm = new SimpleResourceManager(configFile);
				DorianCertificateAuthorityConf conf = (DorianCertificateAuthorityConf) rm
						.getResource(DorianCertificateAuthorityConf.RESOURCE);
				DorianConfiguration c = (DorianConfiguration) rm
				.getResource(DorianConfiguration.RESOURCE);
				Database db = new Database(c
						.getConnectionManager(), c
						.getDorianInternalId());
				db.createDatabaseIfNeeded();
				DorianCertificateAuthority ca = new DorianCertificateAuthority(db, conf);
				CertUtil.writeCertificate(ca.getCACertificate(), "cacert.pem");
				
		} catch (Exception e) {
			e.printStackTrace();
			System.exit(1);
		}

	}
}