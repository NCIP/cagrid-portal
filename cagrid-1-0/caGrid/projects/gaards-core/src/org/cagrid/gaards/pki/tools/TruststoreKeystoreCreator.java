package org.cagrid.gaards.pki.tools;

import gov.nih.nci.cagrid.common.IOUtils;
import gov.nih.nci.cagrid.common.Utils;

import java.io.File;
import java.io.FileOutputStream;
import java.security.KeyStore;
import java.security.PrivateKey;

import org.cagrid.gaards.pki.CertUtil;
import org.cagrid.gaards.pki.KeyUtil;

public class TruststoreKeystoreCreator {

	public static void main(String args[]) {
		try {
			String keystoreLocation = IOUtils
					.readLine("Enter a location and name for your keystore");
			KeyStore keyStore = KeyStore.getInstance("jks");
			keyStore.load(null);
			String certLocation = IOUtils
					.readLine("Enter the location of the certificate (PEM format)");

			java.security.cert.Certificate[] chain = { CertUtil
					.loadCertificate(new File(certLocation)) };


			keyStore.setEntry("tomcat", new KeyStore.TrustedCertificateEntry(chain[0]), null);

			FileOutputStream fos = new FileOutputStream(keystoreLocation);
			keyStore.store(fos,"changeit".toCharArray());
			fos.close();
		}

		catch (Exception e) {
			e.printStackTrace();
			System.exit(1);
		}
	}
}