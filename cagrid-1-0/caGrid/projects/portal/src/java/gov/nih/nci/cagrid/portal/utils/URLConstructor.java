package gov.nih.nci.cagrid.portal.utils;

import gov.nih.nci.cagrid.portal.exception.PortalRuntimeException;

import java.io.UnsupportedEncodingException;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLEncoder;

/**
 * Created by IntelliJ IDEA.
 * User: kherm
 * Date: Oct 25, 2006
 * Time: 11:21:53 AM
 * To change this template use File | Settings | File Templates.
 */
class URLConstructor {


    private StringBuffer query = new StringBuffer();

    public URLConstructor(String url) {
        query.append(url);
    }

    public synchronized void add(String name, String value) throws PortalRuntimeException {
        query.append('&');
        encode(name, value);
    }

    private synchronized void encode(String name, String value) throws PortalRuntimeException

    {
        try {
            query.append(URLEncoder.encode(name, "UTF-8"));
            query.append('=');
            query.append(URLEncoder.encode(value, "UTF-8"));
        }
        catch (UnsupportedEncodingException ex) {
            throw new PortalRuntimeException("Broken VM does not support UTF-8");
        }
    }

    public URL toURL() throws MalformedURLException {
        return new URL(query.toString());
    }

    public String toString() {
        return query.toString();
    }

}