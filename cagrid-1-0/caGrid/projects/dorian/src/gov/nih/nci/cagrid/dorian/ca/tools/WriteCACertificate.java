package gov.nih.nci.cagrid.gums.ca.tools;

import gov.nih.nci.cagrid.gums.ca.GUMSCertificateAuthority;
import gov.nih.nci.cagrid.gums.ca.GUMSCertificateAuthorityConf;
import gov.nih.nci.cagrid.gums.common.ca.CertUtil;
import gov.nih.nci.cagrid.gums.service.GUMS;

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
		
				String configFile = "etc/gums-conf.xml";
				GUMS jm = new GUMS(configFile, "localhost");
				GUMSCertificateAuthorityConf conf = (GUMSCertificateAuthorityConf) jm
						.getResource(GUMSCertificateAuthorityConf.RESOURCE);
				GUMSCertificateAuthority ca = new GUMSCertificateAuthority(jm
						.getDatabase(), conf);
				CertUtil.writeCertificate(ca.getCACertificate(), "cacert.pem");
				
		} catch (Exception e) {
			e.printStackTrace();
			System.exit(1);
		}

	}
}