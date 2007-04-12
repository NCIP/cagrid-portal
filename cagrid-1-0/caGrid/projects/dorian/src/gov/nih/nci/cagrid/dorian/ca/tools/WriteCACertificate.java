package gov.nih.nci.cagrid.dorian.ca.tools;

import gov.nih.nci.cagrid.common.Utils;
import gov.nih.nci.cagrid.dorian.ca.DorianCertificateAuthority;
import gov.nih.nci.cagrid.dorian.common.Database;
import gov.nih.nci.cagrid.dorian.conf.DorianConfiguration;
import gov.nih.nci.cagrid.gridca.common.CertUtil;
import gov.nih.nci.cagrid.gridca.common.KeyUtil;

import java.io.File;


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

			String configFile = "etc/dorian-configuration.xml";
			DorianConfiguration c = (DorianConfiguration) Utils.deserializeDocument(configFile,
				gov.nih.nci.cagrid.dorian.conf.DorianConfiguration.class);
			Database db = new Database(c.getDatabase(), c.getDorianInternalId());
			db.createDatabaseIfNeeded();
			DorianCertificateAuthority ca = new DorianCertificateAuthority(db, c.getDorianCAConfiguration());
			CertUtil.writeCertificate(ca.getCACertificate(), new File("dorian-ca-cert.pem"));
			KeyUtil.writePrivateKey(ca.getCAPrivateKey(), new File("dorian-ca-key.pem"));
		} catch (Exception e) {
			e.printStackTrace();
			System.exit(1);
		}

	}
}