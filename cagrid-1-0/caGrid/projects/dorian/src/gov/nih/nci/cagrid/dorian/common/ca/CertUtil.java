package gov.nih.nci.cagrid.dorian.common.ca;

import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.io.StringReader;
import java.io.StringWriter;
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
import org.bouncycastle.asn1.x509.SubjectKeyIdentifier;
import org.bouncycastle.asn1.x509.X509Extensions;
import org.bouncycastle.asn1.x509.X509Name;
import org.bouncycastle.jce.PKCS10CertificationRequest;
import org.bouncycastle.jce.X509Principal;
import org.bouncycastle.jce.X509V1CertificateGenerator;
import org.bouncycastle.jce.X509V3CertificateGenerator;
import org.bouncycastle.openssl.PEMReader;


/**
 * @author <A href="mailto:langella@bmi.osu.edu">Stephen Langella </A>
 * @author <A href="mailto:oster@bmi.osu.edu">Scott Oster </A>
 * @author <A href="mailto:hastings@bmi.osu.edu">Shannon Hastings </A>
 * @version $Id: ArgumentManagerTable.java,v 1.2 2004/10/15 16:35:16 langella
 *          Exp $
 */
public class CertUtil {

	public static PKCS10CertificationRequest generateCertficateRequest(String subject, KeyPair pair) throws Exception {
		SecurityUtil.init();
		return new PKCS10CertificationRequest("MD5WithRSAEncryption", new X509Principal(subject), pair.getPublic(),
			null, pair.getPrivate());
	}


	public static X509Certificate signCertificateRequest(PKCS10CertificationRequest request,
		Date start, Date expired, X509Certificate cacert, PrivateKey signerKey) throws InvalidKeyException, NoSuchProviderException,
		SignatureException, NoSuchAlgorithmException {
		return generateCertificate(request.getCertificationRequestInfo().getSubject(), start, expired, request
			.getPublicKey("BC"), cacert, signerKey);
	}


	public static X509Certificate generateCACertificate3(X509Name subject, Date start, Date expired, KeyPair pair)
		throws InvalidKeyException, NoSuchProviderException, SignatureException {
		SecurityUtil.init();
		// generate the certificate
		X509V1CertificateGenerator certGen = new X509V1CertificateGenerator();

		certGen.setSerialNumber(BigInteger.valueOf(System.currentTimeMillis()));
		certGen.setIssuerDN(subject);
		certGen.setNotBefore(start);
		certGen.setNotAfter(expired);
		certGen.setSubjectDN(subject);
		certGen.setPublicKey(pair.getPublic());
		certGen.setSignatureAlgorithm("md5WithRSAEncryption");

		return certGen.generateX509Certificate(pair.getPrivate(), "BC");
	}


	public static X509Certificate generateCACertificate(X509Name subject, Date start, Date expired, KeyPair pair)
		throws InvalidKeyException, NoSuchProviderException, SignatureException {
		SecurityUtil.init();
		// generate the certificate
		X509V3CertificateGenerator certGen = new X509V3CertificateGenerator();

		certGen.setSerialNumber(BigInteger.valueOf(System.currentTimeMillis()));
		certGen.setIssuerDN(subject);
		certGen.setNotBefore(start);
		certGen.setNotAfter(expired);
		certGen.setSubjectDN(subject);
		certGen.setPublicKey(pair.getPublic());
		certGen.setSignatureAlgorithm("md5WithRSAEncryption");
		certGen.addExtension(X509Extensions.BasicConstraints, true, new BasicConstraints(20));
		certGen.addExtension(X509Extensions.KeyUsage, true, new KeyUsage(KeyUsage.digitalSignature
			| KeyUsage.keyEncipherment | KeyUsage.keyCertSign));
		certGen.addExtension(X509Extensions.SubjectKeyIdentifier, false, new SubjectKeyIdentifier(pair.getPublic().getEncoded()));
        certGen.addExtension(X509Extensions.AuthorityKeyIdentifier,false,new SubjectKeyIdentifier(pair.getPublic().getEncoded()));
		return certGen.generateX509Certificate(pair.getPrivate(), "BC");
	}


	public static X509Certificate generateCertificate(X509Name subject, Date start, Date expired,
		PublicKey publicKey, X509Certificate cacert, PrivateKey signerKey) throws InvalidKeyException, NoSuchProviderException,
		SignatureException {
		SecurityUtil.init();
		// create the certificate using the information in the request
		X509V3CertificateGenerator certGen = new X509V3CertificateGenerator();

		certGen.setSerialNumber(BigInteger.valueOf(System.currentTimeMillis()));
		certGen.setIssuerDN(new X509Name(cacert.getSubjectDN().getName()));
		certGen.setNotBefore(start);
		certGen.setNotAfter(expired);
		certGen.setSubjectDN(subject);
		certGen.setPublicKey(publicKey);
		certGen.setSignatureAlgorithm("md5WithRSAEncryption");
		certGen.addExtension(X509Extensions.BasicConstraints, true, new BasicConstraints(false));
		certGen.addExtension(X509Extensions.KeyUsage, true, new KeyUsage(KeyUsage.digitalSignature
			| KeyUsage.keyEncipherment));
		certGen.addExtension(X509Extensions.SubjectKeyIdentifier, false, new SubjectKeyIdentifier(publicKey.getEncoded()));
        certGen.addExtension(X509Extensions.AuthorityKeyIdentifier,false,new SubjectKeyIdentifier(cacert.getPublicKey().getEncoded()));
		X509Certificate issuedCert = certGen.generateX509Certificate(signerKey);
		return issuedCert;
	}


	public static void writeCertificate(X509Certificate cert, String path) throws IOException {
		SecurityUtil.init();
		PEMWriter pem = new PEMWriter(new FileWriter(new File(path)));
		pem.writeObject(cert);
		pem.close();
	}


	public static String writeCertificateToString(X509Certificate cert) throws IOException {
		SecurityUtil.init();
		StringWriter sw = new StringWriter();
		PEMWriter pem = new PEMWriter(sw);
		pem.writeObject(cert);
		pem.close();
		return sw.toString();
	}


	public static void writeCertificateRequest(PKCS10CertificationRequest cert, String path) throws IOException {
		SecurityUtil.init();
		PEMWriter pem = new PEMWriter(new FileWriter(new File(path)));
		pem.writeObject(cert);
		pem.close();
	}


	public static X509Certificate loadCertificate(String certLocation) throws IOException, GeneralSecurityException {
		return loadCertificate(new FileReader(new File(certLocation)));
	}
	
	public static X509Certificate loadCertificate(InputStream certLocation) throws IOException, GeneralSecurityException {
		return loadCertificate(new InputStreamReader(certLocation));
	}


	public static X509Certificate loadCertificateFromString(String str) throws IOException, GeneralSecurityException {
		StringReader reader = new StringReader(str);
		return CertUtil.loadCertificate(reader);

	}


	public static X509Certificate loadCertificate(Reader in) throws IOException, GeneralSecurityException {
		SecurityUtil.init();
		PEMReader reader = new PEMReader(in, null, "BC");
		return (X509Certificate) reader.readObject();
	}


	public static boolean isExpired(X509Certificate cert) {
		Date now = new Date();
		if (now.before(cert.getNotBefore()) || (now.after(cert.getNotAfter()))) {
			return true;
		} else {
			return false;
		}
	}
	
}
