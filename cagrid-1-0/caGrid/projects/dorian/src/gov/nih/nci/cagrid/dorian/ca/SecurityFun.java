package gov.nih.nci.cagrid.gums.ca;
import java.security.KeyPair;
import java.security.PrivateKey;
import java.security.Security;
import java.security.cert.X509Certificate;
import java.util.Date;

import org.bouncycastle.asn1.x509.X509Name;
import org.bouncycastle.jce.PKCS10CertificationRequest;
/**
 * @author <A HREF="MAILTO:langella@bmi.osu.edu">Stephen Langella </A>
 * @author <A HREF="MAILTO:oster@bmi.osu.edu">Scott Oster </A>
 * @author <A HREF="MAILTO:hastings@bmi.osu.edu">Shannon Langella </A>
 * @version $Id: SecurityFun.java,v 1.1 2005-09-27 18:31:18 langella Exp $
 */
public class SecurityFun {
	

	public static X509Certificate[] buildChainNewRootCA() throws Exception {
		// create a root certificate
		KeyPair rootPair = KeyUtil.generateRSAKeyPair1024();
		String rootSub = "O=Ohio State University,OU=BMI,OU=MSCL,CN=Certificate Authority";
		X509Name rootSubject = new X509Name(rootSub);
		X509Certificate rootCert = CertUtil.generateCACertificate(rootSubject,new Date(System.currentTimeMillis()),new Date(System.currentTimeMillis() + 500000000),rootPair);
		KeyUtil.writePrivateKey(rootPair.getPrivate(), "cakey.pem");
		// create the certification request
		KeyPair pair = KeyUtil.generateRSAKeyPair1024();
		KeyUtil.writePrivateKey(pair.getPrivate(), "userkey.pem");
		String sub = "O=Ohio State University,OU=BMI,OU=MSCL,CN=Steve Langella";
		PKCS10CertificationRequest request = CertUtil
				.generateCertficateRequest(sub, pair);

		// validate the certification request
		if (!request.verify("BC")) {
			System.out.println("request failed to verify!");
			System.exit(1);
		}

		X509Certificate issuedCert = CertUtil.signCertificateRequest(request,
				rootSubject, new Date(System.currentTimeMillis()),new Date(System.currentTimeMillis() + 500000000),rootPair.getPrivate());

		return new X509Certificate[] { issuedCert, rootCert };
	}
	
	public static X509Certificate[] buildChainSpecifyRootCA(String certLocation, String keyLocation, String keyPassword) throws Exception {
		// create a root certificate
		PrivateKey rootKey = KeyUtil.loadPrivateKey(keyLocation,keyPassword);
		X509Certificate rootCert = CertUtil.loadCertificate(certLocation);
		String rootSub = rootCert.getSubjectDN().toString();
		X509Name rootSubject = new X509Name(rootSub);
		
	
		// create the certification request
		KeyPair pair = KeyUtil.generateRSAKeyPair1024();
		KeyUtil.writePrivateKey(pair.getPrivate(), "userkey.pem");
		int index = rootSub.lastIndexOf(",");
		String sub = rootSub.substring(0,index)+",CN=Stephen Langella";
		PKCS10CertificationRequest request = CertUtil
				.generateCertficateRequest(sub, pair);

		// validate the certification request
		if (!request.verify("BC")) {
			System.out.println("request failed to verify!");
			System.exit(1);
		}

		X509Certificate issuedCert = CertUtil.signCertificateRequest(request,
				rootSubject, new Date(System.currentTimeMillis()),new Date(System.currentTimeMillis() + 500000000),rootKey);

		return new X509Certificate[] { issuedCert, rootCert };
	}
	



	public static void main(String[] args) {
		try {

			Security.addProvider(new org.bouncycastle.jce.provider.BouncyCastleProvider());

			X509Certificate[] chain = buildChainNewRootCA();
			//X509Certificate[] chain = buildChainSpecifyRootCA("dc01cacert.pem","dc01cakey.pem","gomets123");

			
			CertUtil.writeCertificate(chain[0], "usercert.pem");
			CertUtil.writeCertificate(chain[1], "cacert.pem");

		} catch (Exception e) {
			e.printStackTrace();
		}
	}

}
