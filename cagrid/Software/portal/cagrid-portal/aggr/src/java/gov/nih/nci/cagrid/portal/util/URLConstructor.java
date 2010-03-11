package gov.nih.nci.cagrid.portal.util;

import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLEncoder;

/**
 * Created by IntelliJ IDEA. User: kherm Date: Oct 25, 2006 Time: 11:21:53 AM To
 * change this template use File | Settings | File Templates.
 */
public class URLConstructor {
	private StringBuffer query = new StringBuffer();

	public URLConstructor(String url) {
		query.append(url);
	}

	public void add(String name, String value) {
		query.append('&');
		if (!PortalUtils.isEmpty(name) && !PortalUtils.isEmpty(value)) {
			encode(name, value);
		}
	}

	private void encode(String name, String value) {
		try {
			query.append(URLEncoder.encode(name, "UTF-8"));
			query.append('=');
			query.append(URLEncoder.encode(value, "UTF-8"));
		} catch (Exception ex) {
			throw new RuntimeException("Error encoding: " + ex.getMessage(), ex);
		}
	}

	public URL toURL() throws MalformedURLException {
		return new URL(query.toString());
	}

	public String toString() {
		return query.toString();
	}

}