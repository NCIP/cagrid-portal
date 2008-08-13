package org.cagrid.gaards.websso.client.filter;

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.SSLSession;

/**
 *Grid security has certificate CN=host/localhost instead of CN=localhost.
 *Websso client throws mismatch hostname exception.To avoid we needs to set default hostname verifier in 
 *for HttpsUrlConnection
 */

public class WebssoHostNameVerifier implements HostnameVerifier {

	public boolean verify(String s, SSLSession sslsession) {
		return true;
	}
}
