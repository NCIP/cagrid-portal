package gov.nih.nci.cagrid.gums.ca;

import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.Reader;
import java.math.BigInteger;
import java.security.GeneralSecurityException;
import java.security.InvalidKeyException;
import java.security.KeyPair;
import java.security.NoSuchAlgorithmException;
import java.security.NoSuchProviderException;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.SignatureException;
import java.security.cert.X509Certificate;
import java.util.Date;

import org.bouncycastle.asn1.x509.BasicConstraints;
import org.bouncycastle.asn1.x509.KeyUsage;
import org.bouncycastle.asn1.x509.X509Extensions;
import org.bouncycastle.asn1.x509.X509Name;
import org.bouncycastle.jce.PKCS10CertificationRequest;
import org.bouncycastle.jce.X509Principal;
import org.bouncycastle.jce.X509V1CertificateGenerator;
import org.bouncycastle.jce.X509V3CertificateGenerator;
import org.bouncycastle.openssl.PEMReader;
/**
 * @author <A HREF="MAILTO:langella@bmi.osu.edu">Stephen Langella </A>
 * @author <A HREF="MAILTO:oster@bmi.osu.edu">Scott Oster </A>
 * @author <A HREF="MAILTO:hastings@bmi.osu.edu">Shannon Langella </A>
 * @version $Id: CertUtil.java,v 1.1 2005-09-27 18:31:18 langella Exp $
 */
public class CertUtil {
	

	public static PKCS10CertificationRequest generateCertficateRequest(
			String subject, KeyPair pair) throws Exception {
		SecurityUtil.init();
		return new PKCS10CertificationRequest("MD5WithRSAEncryption", new X509Principal(
				subject), pair.getPublic(), null, pair.getPrivate());
	}

	public static X509Certificate signCertificateRequest(
			PKCS10CertificationRequest request, X509Name issuer, Date start, Date expired,
			PrivateKey signerKey) throws InvalidKeyException,
			NoSuchProviderException, SignatureException,
			NoSuchAlgorithmException {
		return generateCertificate(request.getCertificationRequestInfo()
				.getSubject(), issuer, start, expired, request.getPublicKey("BC"), signerKey);
	}
	

    public static X509Certificate generateCACertificate(X509Name subject,Date start, Date expired,KeyPair pair)
        throws InvalidKeyException, NoSuchProviderException, SignatureException
    {
    	SecurityUtil.init();
        // generate the certificate
        X509V1CertificateGenerator  certGen = new X509V1CertificateGenerator();

        certGen.setSerialNumber(BigInteger.valueOf(System.currentTimeMillis()));
        certGen.setIssuerDN(subject);
        certGen.setNotBefore(start);
        certGen.setNotAfter(expired);
        certGen.setSubjectDN(subject);
        certGen.setPublicKey(pair.getPublic());
        certGen.setSignatureAlgorithm("md5WithRSAEncryption");

        return certGen.generateX509Certificate(pair.getPrivate(), "BC");
    }

	public static X509Certificate generateCertificate(X509Name subject,
			X509Name issuer, Date start, Date expired,PublicKey publicKey, PrivateKey signerKey)
			throws InvalidKeyException, NoSuchProviderException,
			SignatureException {
		SecurityUtil.init();
		// create the certificate using the information in the request
		X509V3CertificateGenerator certGen = new X509V3CertificateGenerator();

		certGen.setSerialNumber(BigInteger.valueOf(System.currentTimeMillis()));
		certGen.setIssuerDN(issuer);
		certGen.setNotBefore(start);
		certGen.setNotAfter(expired);
		certGen.setSubjectDN(subject);
		certGen.setPublicKey(publicKey);
		certGen.setSignatureAlgorithm("md5WithRSAEncryption");
		certGen.addExtension(X509Extensions.BasicConstraints, true,
				new BasicConstraints(false));
		certGen.addExtension(X509Extensions.KeyUsage, true, new KeyUsage(
				KeyUsage.digitalSignature | KeyUsage.keyEncipherment));
		X509Certificate issuedCert = certGen.generateX509Certificate(signerKey);
		return issuedCert;
	}

	public static void writeCertificate(X509Certificate cert, String path)
			throws IOException {
		SecurityUtil.init();
		PEMWriter pem = new PEMWriter(new FileWriter(new File(path)));
		pem.writeObject(cert);
		pem.close();
	}

	public static void writeCertificateRequest(PKCS10CertificationRequest cert,
			String path) throws IOException {
		SecurityUtil.init();
		PEMWriter pem = new PEMWriter(new FileWriter(new File(path)));
		pem.writeObject(cert);
		pem.close();
	}	
	
	public static X509Certificate loadCertificate(String certLocation) throws IOException,
	GeneralSecurityException {
		return loadCertificate(new FileReader(new File(certLocation)));
	}

	public static X509Certificate loadCertificate(Reader in) throws IOException,
			GeneralSecurityException {
		SecurityUtil.init();
		PEMReader reader = new PEMReader(in,null,"BC");
		return (X509Certificate)reader.readObject();
	}
}
