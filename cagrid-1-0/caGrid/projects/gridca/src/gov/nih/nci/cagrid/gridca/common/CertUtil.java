package gov.nih.nci.cagrid.gridca.common;

import java.io.ByteArrayInputStream;
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
import java.security.cert.X509CRL;
import java.security.cert.X509Certificate;
import java.util.Date;

import org.bouncycastle.asn1.ASN1Sequence;
import org.bouncycastle.asn1.DERInputStream;
import org.bouncycastle.asn1.x509.AuthorityKeyIdentifier;
import org.bouncycastle.asn1.x509.BasicConstraints;
import org.bouncycastle.asn1.x509.CRLNumber;
import org.bouncycastle.asn1.x509.KeyUsage;
import org.bouncycastle.asn1.x509.SubjectKeyIdentifier;
import org.bouncycastle.asn1.x509.SubjectPublicKeyInfo;
import org.bouncycastle.asn1.x509.X509Extensions;
import org.bouncycastle.asn1.x509.X509Name;
import org.bouncycastle.jce.PKCS10CertificationRequest;
import org.bouncycastle.jce.X509Principal;
import org.bouncycastle.jce.X509V2CRLGenerator;
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

	public static String getHashCode(X509Certificate cert){
		byte[] bytes = cert.getSubjectDN().getName().getBytes();
		byte[] hashBytes = new byte[4];
		int count = 0;
		for(int i = (bytes.length-1); i>=0; i--){
			hashBytes[count] = bytes[i];
			count = count +1;
		}
		return new String(hashBytes);
	}
	
	public static PKCS10CertificationRequest generateCertficateRequest(String subject, KeyPair pair) throws Exception {
		SecurityUtil.init();
		return new PKCS10CertificationRequest("MD5WithRSAEncryption", new X509Principal(subject), pair.getPublic(),
			null, pair.getPrivate());
	}


	public static X509Certificate signCertificateRequest(PKCS10CertificationRequest request, Date start, Date expired,
		X509Certificate cacert, PrivateKey signerKey) throws InvalidKeyException, NoSuchProviderException,
		SignatureException, NoSuchAlgorithmException, IOException {
		return generateCertificate(request.getCertificationRequestInfo().getSubject(), start, expired, request
			.getPublicKey("BC"), cacert, signerKey);
	}


	public static X509Certificate generateCACertificate(X509Name subject, Date start, Date expired, KeyPair pair)
		throws InvalidKeyException, NoSuchProviderException, SignatureException, IOException {
		return generateCACertificate(subject, start, expired, pair, 1);
	}


	public static X509Certificate generateIntermediateCACertificate(X509Certificate cacert, PrivateKey signerKey,
		X509Name subject, Date start, Date expired, PublicKey publicKey) throws InvalidKeyException,
		NoSuchProviderException, SignatureException, IOException {
		SecurityUtil.init();

		int constraints = cacert.getBasicConstraints();
		if (constraints <= 1) {
			throw new SignatureException(
				"The CA Certificate specified cannot generate an intermediate CA certificate (Basic Constraints :"
					+ constraints + ")");
		}
		constraints = constraints - 1;

		// generate the certificate
		X509V3CertificateGenerator certGen = new X509V3CertificateGenerator();

		certGen.setSerialNumber(BigInteger.valueOf(System.currentTimeMillis()));
		certGen.setIssuerDN(new X509Name(cacert.getSubjectDN().toString()));
		certGen.setNotBefore(start);
		certGen.setNotAfter(expired);
		certGen.setSubjectDN(subject);
		certGen.setPublicKey(publicKey);
		certGen.setSignatureAlgorithm("md5WithRSAEncryption");
		certGen.addExtension(X509Extensions.BasicConstraints, true, new BasicConstraints(constraints));
		certGen.addExtension(X509Extensions.KeyUsage, true, new KeyUsage(KeyUsage.digitalSignature
			| KeyUsage.keyEncipherment | KeyUsage.keyCertSign));

		SubjectPublicKeyInfo spki = new SubjectPublicKeyInfo((ASN1Sequence) new DERInputStream(
			new ByteArrayInputStream(publicKey.getEncoded())).readObject());
		certGen.addExtension(X509Extensions.SubjectKeyIdentifier, false, new SubjectKeyIdentifier(spki));

		SubjectPublicKeyInfo apki = new SubjectPublicKeyInfo((ASN1Sequence) new DERInputStream(
			new ByteArrayInputStream(cacert.getPublicKey().getEncoded())).readObject());
		certGen.addExtension(X509Extensions.AuthorityKeyIdentifier, false, new AuthorityKeyIdentifier(apki));
		return certGen.generateX509Certificate(signerKey, "BC");
	}


	public static X509Certificate generateCACertificate(X509Name subject, Date start, Date expired, KeyPair pair,
		int numberOfCAs) throws InvalidKeyException, NoSuchProviderException, SignatureException, IOException {
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
		certGen.addExtension(X509Extensions.BasicConstraints, true, new BasicConstraints(numberOfCAs));
		certGen.addExtension(X509Extensions.KeyUsage, true, new KeyUsage(KeyUsage.digitalSignature
			| KeyUsage.keyEncipherment | KeyUsage.keyCertSign));

		SubjectPublicKeyInfo spki = new SubjectPublicKeyInfo((ASN1Sequence) new DERInputStream(
			new ByteArrayInputStream(pair.getPublic().getEncoded())).readObject());
		certGen.addExtension(X509Extensions.SubjectKeyIdentifier, false, new SubjectKeyIdentifier(spki));

		SubjectPublicKeyInfo apki = new SubjectPublicKeyInfo((ASN1Sequence) new DERInputStream(
			new ByteArrayInputStream(pair.getPublic().getEncoded())).readObject());
		certGen.addExtension(X509Extensions.AuthorityKeyIdentifier, false, new AuthorityKeyIdentifier(apki));
		return certGen.generateX509Certificate(pair.getPrivate(), "BC");
	}


	public static X509Certificate generateCertificate(X509Name subject, Date start, Date expired, PublicKey publicKey,
		X509Certificate cacert, PrivateKey signerKey) throws InvalidKeyException, NoSuchProviderException,
		SignatureException, IOException {
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

		SubjectPublicKeyInfo spki = new SubjectPublicKeyInfo((ASN1Sequence) new DERInputStream(
			new ByteArrayInputStream(publicKey.getEncoded())).readObject());
		certGen.addExtension(X509Extensions.SubjectKeyIdentifier, false, new SubjectKeyIdentifier(spki));

		SubjectPublicKeyInfo apki = new SubjectPublicKeyInfo((ASN1Sequence) new DERInputStream(
			new ByteArrayInputStream(cacert.getPublicKey().getEncoded())).readObject());
		certGen.addExtension(X509Extensions.AuthorityKeyIdentifier, false, new AuthorityKeyIdentifier(apki));

		X509Certificate issuedCert = certGen.generateX509Certificate(signerKey);
		return issuedCert;
	}


	public static void writeCertificate(X509Certificate cert, File path) throws IOException {
		SecurityUtil.init();
		PEMWriter pem = new PEMWriter(new FileWriter(path));
		pem.writeObject(cert);
		pem.close();
	}


	public static String writeCertificate(X509Certificate cert) throws IOException {
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


	public static X509Certificate loadCertificate(File certLocation) throws IOException, GeneralSecurityException {
		return loadCertificate(new FileReader(certLocation));
	}


	public static X509Certificate loadCertificate(InputStream certLocation) throws IOException,
		GeneralSecurityException {
		return loadCertificate(new InputStreamReader(certLocation));
	}


	public static X509Certificate loadCertificate(String str) throws IOException, GeneralSecurityException {
		StringReader reader = new StringReader(str);
		return CertUtil.loadCertificate(reader);

	}


	public static X509Certificate loadCertificate(Reader in) throws IOException, GeneralSecurityException {
		SecurityUtil.init();
		PEMReader reader = new PEMReader(in, null, "BC");
		X509Certificate cert = (X509Certificate) reader.readObject();
		reader.close();
		return cert;
	}


	public static X509CRL createCRL(X509Certificate caCert, PrivateKey caKey, CRLEntry[] entries, Date expires)
		throws Exception {
		X509V2CRLGenerator crlGen = new X509V2CRLGenerator();
		Date now = new Date();
		crlGen.setIssuerDN(new X509Name(caCert.getSubjectDN().getName()));
		crlGen.setThisUpdate(now);
		crlGen.setNextUpdate(expires);
		crlGen.setSignatureAlgorithm("md5WithRSAEncryption");
		for (int i = 0; i < entries.length; i++) {
			crlGen.addCRLEntry(entries[i].getCertificateSerialNumber(), now, entries[i].getReason());
		}
		SubjectPublicKeyInfo apki = new SubjectPublicKeyInfo((ASN1Sequence) new DERInputStream(
			new ByteArrayInputStream(caCert.getPublicKey().getEncoded())).readObject());
		crlGen.addExtension(X509Extensions.AuthorityKeyIdentifier, false, new AuthorityKeyIdentifier(apki));
		crlGen.addExtension(X509Extensions.CRLNumber, false, new CRLNumber(BigInteger.valueOf(System
			.currentTimeMillis())));
		return crlGen.generateX509CRL(caKey, "BC");
	}


	public static void writeCRL(X509CRL crl, File path) throws IOException {
		SecurityUtil.init();
		PEMWriter pem = new PEMWriter(new FileWriter(path));
		pem.writeObject(crl);
		pem.close();
	}


	public static String writeCRL(X509CRL crl) throws IOException {
		SecurityUtil.init();
		StringWriter sw = new StringWriter();
		PEMWriter pem = new PEMWriter(sw);
		pem.writeObject(crl);
		pem.close();
		return sw.toString();
	}


	public static X509CRL loadCRL(File crlLocation) throws IOException, GeneralSecurityException {
		return loadCRL(new FileReader(crlLocation));
	}


	public static X509CRL loadCRL(InputStream crlLocation) throws IOException, GeneralSecurityException {
		return loadCRL(new InputStreamReader(crlLocation));
	}


	public static X509CRL loadCRL(String str) throws IOException, GeneralSecurityException {
		StringReader reader = new StringReader(str);
		return CertUtil.loadCRL(reader);

	}


	public static X509CRL loadCRL(Reader in) throws IOException, GeneralSecurityException {
		SecurityUtil.init();
		CRLReader reader = new CRLReader(in, "BC");
		X509CRL crl = reader.readCRL();
		reader.close();
		return crl;
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
