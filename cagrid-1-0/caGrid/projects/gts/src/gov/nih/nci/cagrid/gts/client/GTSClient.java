package gov.nih.nci.cagrid.gts.client;

import gov.nih.nci.cagrid.gridca.common.CertUtil;
import gov.nih.nci.cagrid.gts.bean.AuthorityGTS;
import gov.nih.nci.cagrid.gts.bean.TrustLevel;
import gov.nih.nci.cagrid.gts.bean.TrustedAuthority;
import gov.nih.nci.cagrid.gts.bean.TrustedAuthorityFilter;
import gov.nih.nci.cagrid.gts.stubs.CertificateValidationFault;
import gov.nih.nci.cagrid.gts.stubs.GTSInternalFault;

import java.io.IOException;
import java.rmi.RemoteException;
import java.security.cert.X509Certificate;


/**
 * @author <A href="mailto:langella@bmi.osu.edu">Stephen Langella </A>
 * @author <A href="mailto:oster@bmi.osu.edu">Scott Oster </A>
 * @author <A href="mailto:hastings@bmi.osu.edu">Shannon Hastings </A>
 * @version $Id: ArgumentManagerTable.java,v 1.2 2004/10/15 16:35:16 langella
 *          Exp $
 */

public class GTSClient {

	private GridTrustServiceClient client;


	public GTSClient(String url) {
		this.client = new GridTrustServiceClient(url);
	}


	public TrustedAuthority[] findTrustedAuthorities(TrustedAuthorityFilter f) throws RemoteException, GTSInternalFault {
		return this.client.findTrustedAuthorities(f);
	}


	public boolean validate(X509Certificate cert, TrustedAuthorityFilter filter) throws IOException, RemoteException,
		GTSInternalFault, CertificateValidationFault {
		X509Certificate[] chain = new X509Certificate[1];
		chain[0] = cert;
		return validate(chain, filter);
	}


	public boolean validate(X509Certificate[] chain, TrustedAuthorityFilter filter) throws IOException,
		RemoteException, GTSInternalFault, CertificateValidationFault {
		if (chain != null) {
			gov.nih.nci.cagrid.gts.bean.X509Certificate[] certs = new gov.nih.nci.cagrid.gts.bean.X509Certificate[chain.length];
			for (int i = 0; i < chain.length; i++) {
				gov.nih.nci.cagrid.gts.bean.X509Certificate cert = new gov.nih.nci.cagrid.gts.bean.X509Certificate();
				cert.setCertificateEncodedString(CertUtil.writeCertificate(chain[i]));
				certs[i] = cert;
			}
			return client.validate(certs, filter);
		} else {
			return false;
		}
	}


	public TrustLevel[] getTrustLevels() throws RemoteException, GTSInternalFault {
		return this.client.getTrustLevels();
	}


	public AuthorityGTS[] getAuthorities() throws RemoteException, GTSInternalFault {
		return this.client.getAuthorities();
	}
}
