package gov.nih.nci.cagrid.dorian.ifs;

import gov.nih.nci.cagrid.dorian.ifs.bean.ProxyLifetime;
import gov.nih.nci.cagrid.gridca.common.SecurityUtil;

import java.io.IOException;
import java.math.BigInteger;
import java.security.GeneralSecurityException;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.cert.X509Certificate;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.Random;
import java.util.TimeZone;

import org.bouncycastle.asn1.DERBitString;
import org.bouncycastle.asn1.x509.KeyUsage;
import org.bouncycastle.asn1.x509.TBSCertificateStructure;
import org.bouncycastle.asn1.x509.X509Extension;
import org.bouncycastle.asn1.x509.X509Extensions;
import org.bouncycastle.asn1.x509.X509Name;
import org.bouncycastle.jce.X509V3CertificateGenerator;
import org.globus.gsi.CertUtil;
import org.globus.gsi.GSIConstants;
import org.globus.gsi.bc.BouncyCastleUtil;
import org.globus.gsi.bc.X509NameHelper;
import org.globus.gsi.proxy.ext.ProxyCertInfo;
import org.globus.gsi.proxy.ext.ProxyPolicy;

/**
 * @author <A href="mailto:langella@bmi.osu.edu">Stephen Langella </A>
 * @author <A href="mailto:oster@bmi.osu.edu">Scott Oster </A>
 * @author <A href="mailto:hastings@bmi.osu.edu">Shannon Hastings </A>
 * @version $Id: ArgumentManagerTable.java,v 1.2 2004/10/15 16:35:16 langella
 *          Exp $
 */
public class ProxyUtil {
	
	public static X509Certificate[] createProxyCertificate(X509Certificate cert,
			PrivateKey privateKey, PublicKey proxyPublicKey,
			ProxyLifetime lifetime)
			throws GeneralSecurityException {
			return createProxyCertificate(new X509Certificate[] { cert },privateKey,proxyPublicKey,lifetime,GSIConstants.GSI_3_IMPERSONATION_PROXY,null);
	}

	public static X509Certificate[] createProxyCertificate(X509Certificate[] certs,
			PrivateKey privateKey, PublicKey proxyPublicKey,
			ProxyLifetime lifetime)
			throws GeneralSecurityException {
			return createProxyCertificate(certs,privateKey,proxyPublicKey,lifetime,GSIConstants.GSI_3_IMPERSONATION_PROXY,null);
	}


	public static X509Certificate[] createProxyCertificate(X509Certificate[] certs,
			PrivateKey privateKey, PublicKey proxyPublicKey,
			ProxyLifetime lifetime,
			int delegationMode, ProxyCertInfo proxyCertInfoExt)
			throws GeneralSecurityException {
		SecurityUtil.init();
		X509Certificate newCert = createProxyCertificate(certs[0], privateKey,
				proxyPublicKey, lifetime, delegationMode, proxyCertInfoExt);

		X509Certificate[] newCerts = new X509Certificate[certs.length + 1];
		newCerts[0] = newCert;
		System.arraycopy(certs, 0, newCerts, 1, certs.length);
		return newCerts;
	}

	/**
	 * Creates a proxy certificate. A <code>ProxyCertInfo</code> extension can
	 * be optionally included in the new proxy certificate. <BR>
	 * If a GSI-2 proxy is created, the serial number of the proxy certificate
	 * will be the same as of the issuing certificate. Also, none of the
	 * extensions in the issuing certificate will be copied into the proxy
	 * certificate.<BR>
	 * If a GSI-3 proxy is created, the serial number of the proxy certificate
	 * will be picked randomly. If the issuing certificate contains a
	 * <i>ExtendedKeyUsage</i> extension, the extension will be copied as is
	 * into the proxy certificate. If the issuing certificate contains a
	 * <i>KeyUsage</i> extension, the extension will be copied into the proxy
	 * certificate with <i>keyCertSign</i> and <i>nonRepudiation</i> bits
	 * turned off. No other extensions are currently copied.
	 * 
	 * @param issuerCert
	 *            the issuing certificate
	 * @param issuerKey
	 *            private key matching the public key of issuer certificate. The
	 *            new proxy certificate will be signed by that key.
	 * @param publicKey
	 *            the public key of the new certificate
	 * @param lifetime
	 *            lifetime of the new certificate in seconds. If 0 (or less
	 *            then) the new certificate will have the same lifetime as the
	 *            issuing certificate.
	 * @param proxyType
	 *            can be one of {@link GSIConstants#DELEGATION_LIMITED
	 *            GSIConstants.DELEGATION_LIMITED},
	 *            {@link GSIConstants#DELEGATION_FULL
	 *            GSIConstants.DELEGATION_FULL},
	 *            {@link GSIConstants#GSI_2_LIMITED_PROXY
	 *            GSIConstants.GSI_2_LIMITED_PROXY},
	 *            {@link GSIConstants#GSI_2_PROXY GSIConstants.GSI_2_PROXY},
	 *            {@link GSIConstants#GSI_3_IMPERSONATION_PROXY
	 *            GSIConstants.GSI_3_IMPERSONATION_PROXY},
	 *            {@link GSIConstants#GSI_3_LIMITED_PROXY
	 *            GSIConstants.GSI_3_LIMITED_PROXY},
	 *            {@link GSIConstants#GSI_3_INDEPENDENT_PROXY
	 *            GSIConstants.GSI_3_INDEPENDENT_PROXY},
	 *            {@link GSIConstants#GSI_3_RESTRICTED_PROXY
	 *            GSIConstants.GSI_3_RESTRICTED_PROXY}. If
	 *            {@link GSIConstants#DELEGATION_LIMITED
	 *            GSIConstants.DELEGATION_LIMITED} and if
	 *            {@link CertUtil#isGsi3Enabled() CertUtil.isGsi3Enabled}
	 *            returns true then a GSI-3 limited proxy will be created. If
	 *            not, a GSI-2 limited proxy will be created. If
	 *            {@link GSIConstants#DELEGATION_FULL
	 *            GSIConstants.DELEGATION_FULL} and if
	 *            {@link CertUtil#isGsi3Enabled() CertUtil.isGsi3Enabled}
	 *            returns true then a GSI-3 impersonation proxy will be created.
	 *            If not, a GSI-2 full proxy will be created.
	 * @param proxyCertInfo
	 *            the <code>ProxyCertInfo</code> extension to included in the
	 *            new proxy certificate. Applies only for GSI-3 restricted
	 *            proxies. Can be null.
	 * @return <code>X509Certificate</code> the new proxy certificate.
	 * @exception GeneralSecurityException
	 *                if a security error occurs.
	 */
	protected static X509Certificate createProxyCertificate(
			X509Certificate issuerCert, PrivateKey issuerKey,
			PublicKey publicKey, ProxyLifetime lifetime, int proxyType,
			ProxyCertInfo proxyCertInfo) throws GeneralSecurityException {
		SecurityUtil.init();
		if (proxyType == GSIConstants.DELEGATION_LIMITED) {
			proxyType = (CertUtil.isGsi3Enabled()) ? GSIConstants.GSI_3_LIMITED_PROXY
					: GSIConstants.GSI_2_LIMITED_PROXY;
		} else if (proxyType == GSIConstants.DELEGATION_FULL) {
			proxyType = (CertUtil.isGsi3Enabled()) ? GSIConstants.GSI_3_IMPERSONATION_PROXY
					: GSIConstants.GSI_2_PROXY;
		}

		X509V3CertificateGenerator certGen = new X509V3CertificateGenerator();

		BigInteger serialNum = null;
		String delegDN = null;

		if (CertUtil.isGsi3Proxy(proxyType)) {
			Random rand = new Random();
			delegDN = String.valueOf(Math.abs(rand.nextInt()));
			serialNum = new BigInteger(20, rand);

			// add ProxyCertInfo extension
			if (proxyCertInfo == null) {
				ProxyPolicy policy = null;
				if (proxyType == GSIConstants.GSI_3_IMPERSONATION_PROXY) {
					policy = new ProxyPolicy(ProxyPolicy.IMPERSONATION);
				} else if (proxyType == GSIConstants.GSI_3_INDEPENDENT_PROXY) {
					policy = new ProxyPolicy(ProxyPolicy.INDEPENDENT);
				} else if (proxyType == GSIConstants.GSI_3_LIMITED_PROXY) {
					policy = new ProxyPolicy(ProxyPolicy.LIMITED);
				} else if (proxyType == GSIConstants.GSI_3_RESTRICTED_PROXY) {
					throw new IllegalArgumentException(
							"Restricted proxy requires ProxyCertInfo");
				} else {
					throw new IllegalArgumentException("Invalid proxyType");
				}
				proxyCertInfo = new ProxyCertInfo(policy);
			}

			certGen.addExtension(ProxyCertInfo.OID, true, proxyCertInfo);

			try {

				TBSCertificateStructure crt = BouncyCastleUtil
						.getTBSCertificateStructure(issuerCert);

				X509Extensions extensions = crt.getExtensions();
				if (extensions != null) {
					X509Extension ext;

					// handle extended key usage ext
					ext = extensions
							.getExtension(X509Extensions.ExtendedKeyUsage);
					if (ext != null) {
						certGen.addExtension(X509Extensions.ExtendedKeyUsage,
								ext.isCritical(), ext.getValue());
					}

					// handle key usage ext
					ext = extensions.getExtension(X509Extensions.KeyUsage);
					if (ext != null) {
						DERBitString bits = (DERBitString) BouncyCastleUtil
								.getExtensionObject(ext);

						byte[] bytes = bits.getBytes();

						// make sure they are disabled
						if ((bytes[0] & KeyUsage.nonRepudiation) != 0) {
							bytes[0] ^= KeyUsage.nonRepudiation;
						}

						if ((bytes[0] & KeyUsage.keyCertSign) != 0) {
							bytes[0] ^= KeyUsage.keyCertSign;
						}

						bits = new DERBitString(bytes, bits.getPadBits());

						certGen.addExtension(X509Extensions.KeyUsage, true,
								bits);
					}
				}

			} catch (IOException e) {
				// but this should not happen
				throw new GeneralSecurityException(e.getMessage());
			}

		} else if (proxyType == GSIConstants.GSI_2_LIMITED_PROXY) {
			delegDN = "limited proxy";
			serialNum = issuerCert.getSerialNumber();
		} else if (proxyType == GSIConstants.GSI_2_PROXY) {
			delegDN = "proxy";
			serialNum = issuerCert.getSerialNumber();
		} else {
			throw new IllegalArgumentException("Unsupported proxyType : "
					+ proxyType);
		}

		X509Name issuerDN = (X509Name) issuerCert.getSubjectDN();

		X509NameHelper issuer = new X509NameHelper(issuerDN);

		X509NameHelper subject = new X509NameHelper(issuerDN);
		subject.add(X509Name.CN, delegDN);

		certGen.setSubjectDN(subject.getAsName());
		certGen.setIssuerDN(issuer.getAsName());

		certGen.setSerialNumber(serialNum);
		certGen.setPublicKey(publicKey);
		certGen.setSignatureAlgorithm(issuerCert.getSigAlgName());

		GregorianCalendar date = new GregorianCalendar(TimeZone
				.getTimeZone("GMT"));
		/* Allow for a five minute clock skew here. */
		date.add(Calendar.MINUTE, -5);
		certGen.setNotBefore(date.getTime());

		/* If hours = 0, then cert lifetime is set to user cert */
		if (lifetime == null) {
			certGen.setNotAfter(issuerCert.getNotAfter());
		} else {
			Date d = IFSUtils.getProxyValid(lifetime);
			if(issuerCert.getNotAfter().before(d)){
				throw new GeneralSecurityException("Cannot create a proxy that expires after issuing certificate.");
			}
			certGen.setNotAfter(d);
		}
		
		


		return certGen.generateX509Certificate(issuerKey);
	}

}
