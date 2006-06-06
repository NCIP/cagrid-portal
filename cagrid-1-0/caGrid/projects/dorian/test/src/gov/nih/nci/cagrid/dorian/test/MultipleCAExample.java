package gov.nih.nci.cagrid.dorian.test;

import gov.nih.nci.cagrid.dorian.ifs.bean.ProxyLifetime;
import gov.nih.nci.cagrid.dorian.service.ifs.IFSProxyCreator;
import gov.nih.nci.cagrid.gridca.common.CertUtil;
import gov.nih.nci.cagrid.gridca.common.KeyUtil;

import java.io.File;
import java.security.KeyPair;
import java.security.cert.X509Certificate;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

import org.bouncycastle.asn1.x509.X509Name;
import org.globus.gsi.GlobusCredential;

public class MultipleCAExample {

	public static void main(String[] args) {
		try {
			String dnPrefix = "O=Organization ABC,OU=Unit XYZ";
			X509Name rootcaDN = new X509Name(dnPrefix+",CN=Root Certificate Authority");
			X509Name midcaDN = new X509Name(dnPrefix+",CN=Intermediate Certificate Authority");
			X509Name userDN = new X509Name(dnPrefix+",CN=User X");
			String store = "c:/certificates/multiple-ca-test/";
			
			KeyPair rootcaKeys = KeyUtil.generateRSAKeyPair512();
			KeyUtil.writePrivateKey(rootcaKeys.getPrivate(),new File(store+"root-ca-key.pem"));
			KeyPair midcaKeys = KeyUtil.generateRSAKeyPair512();
			KeyUtil.writePrivateKey(midcaKeys.getPrivate(),new File(store+"intermediate-ca-key.pem"));
			KeyPair userKeys = KeyUtil.generateRSAKeyPair512();
			KeyUtil.writePrivateKey(userKeys.getPrivate(),new File(store+"userx-key.pem"));
			
			Calendar c = new GregorianCalendar();
			Date now = c.getTime();
			c.add(Calendar.YEAR,5);
			Date end = c.getTime();
			
			X509Certificate rootcacert = CertUtil.generateCACertificate(rootcaDN,now,end,rootcaKeys,2);
			CertUtil.writeCertificate(rootcacert,new File(store+"root-ca-cert.pem"));
		
			X509Certificate midcacert = CertUtil.generateIntermediateCACertificate(rootcacert,rootcaKeys.getPrivate(),midcaDN,now,end,midcaKeys.getPublic());
			CertUtil.writeCertificate(midcacert,new File(store+"intermediate-ca-cert.pem"));
			
			X509Certificate usercert = CertUtil.generateCertificate(userDN,now,end,userKeys.getPublic(),midcacert,midcaKeys.getPrivate());
			CertUtil.writeCertificate(usercert,new File(store+"userx-cert.pem"));
			
			ProxyLifetime lifetime = new ProxyLifetime(10000,0,0);
			
			KeyPair proxy1Pair = KeyUtil.generateRSAKeyPair512();
			GlobusCredential proxy1 = new GlobusCredential(proxy1Pair.getPrivate(),IFSProxyCreator.createImpersonationProxyCertificate(usercert,userKeys.getPrivate(),proxy1Pair.getPublic(),lifetime));
			gov.nih.nci.cagrid.common.security.ProxyUtil.saveProxy(proxy1,store+"userx-proxy-1.proxy");
			
			KeyPair proxy2Pair = KeyUtil.generateRSAKeyPair512();
			X509Certificate[] proxy2certs = new X509Certificate[2];
			proxy2certs[0] = usercert;
			proxy2certs[1] = midcacert;
			GlobusCredential proxy2 = new GlobusCredential(proxy2Pair.getPrivate(),IFSProxyCreator.createImpersonationProxyCertificate(proxy2certs,userKeys.getPrivate(),proxy2Pair.getPublic(),lifetime));
			gov.nih.nci.cagrid.common.security.ProxyUtil.saveProxy(proxy2,store+"userx-proxy-2.proxy");
			
			KeyPair proxy3Pair = KeyUtil.generateRSAKeyPair512();
			X509Certificate[] proxy3certs = new X509Certificate[3];
			proxy3certs[0] = usercert;
			proxy3certs[1] = midcacert;
			proxy3certs[2] = rootcacert;
			GlobusCredential proxy3 = new GlobusCredential(proxy3Pair.getPrivate(),IFSProxyCreator.createImpersonationProxyCertificate(proxy3certs,userKeys.getPrivate(),proxy3Pair.getPublic(),lifetime));
			gov.nih.nci.cagrid.common.security.ProxyUtil.saveProxy(proxy3,store+"userx-proxy-3.proxy");
			
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
