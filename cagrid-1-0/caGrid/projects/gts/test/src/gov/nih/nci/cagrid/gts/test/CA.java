package gov.nih.nci.cagrid.gts.test;

import gov.nih.nci.cagrid.gridca.common.CRLEntry;
import gov.nih.nci.cagrid.gridca.common.CertUtil;
import gov.nih.nci.cagrid.gridca.common.KeyUtil;
import gov.nih.nci.cagrid.gts.bean.X509CRL;

import java.security.KeyPair;
import java.security.PrivateKey;
import java.security.cert.X509Certificate;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

import org.bouncycastle.asn1.x509.X509Name;

/**
 * @author <A href="mailto:langella@bmi.osu.edu">Stephen Langella </A>
 * @author <A href="mailto:oster@bmi.osu.edu">Scott Oster </A>
 * @author <A href="mailto:hastings@bmi.osu.edu">Shannon Hastings </A>
 * @version $Id: ArgumentManagerTable.java,v 1.2 2004/10/15 16:35:16 langella
 *          Exp $
 */
public class CA {
	private X509Certificate cert;
	private PrivateKey key;
	private X509CRL crl;
	public final static String DEFAULT_CA_DN = "O=Organization ABC,OU=Unit XYZ,CN=Certificate Authority";


	public CA() throws Exception {
		this(DEFAULT_CA_DN);
	}


	public CA(String dn) throws Exception {
		Calendar c = new GregorianCalendar();
		Date now = c.getTime();
		c.add(Calendar.YEAR, 5);
		Date expires = c.getTime();
		KeyPair pair = KeyUtil.generateRSAKeyPair512();
		this.key = pair.getPrivate();
		cert = CertUtil.generateCACertificate(new X509Name(dn), now, expires, pair);

	}


	public CA(String dn, Date start, Date expires) throws Exception {
		KeyPair pair = KeyUtil.generateRSAKeyPair512();
		this.key = pair.getPrivate();
		cert = CertUtil.generateCACertificate(new X509Name(dn), start, expires, pair);
	}


	public CA(X509Certificate cert, PrivateKey key, X509CRL crl) {
		this.cert = cert;
		this.key = key;
		this.crl = crl;
	}


	public X509Certificate getCertificate() {
		return cert;
	}


	public X509CRL getCRL() {
		return crl;
	}


	public PrivateKey getPrivateKey() {
		return key;
	}


	public X509CRL updateCRL(CRLEntry entry) throws Exception {
		CRLEntry[] entries = new CRLEntry[1];
		entries[0] = entry;
		CertUtil.createCRL(cert, key, entries, cert.getNotAfter());
		return crl;
	}


	public X509CRL updateCRL(CRLEntry[] entries) throws Exception {
		CertUtil.createCRL(cert, key, entries, cert.getNotAfter());
		return crl;
	}

}

