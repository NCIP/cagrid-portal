package org.cagrid.transfer.context.client.helper;

import java.io.InputStream;
import java.net.URL;
import java.net.URLConnection;

import org.cagrid.transfer.descriptor.DataTransferDescriptor;
import org.globus.net.GSIHttpURLConnection;
import org.ietf.jgss.GSSCredential;


public class TransferClientHelper {

    public static InputStream getDataStream(DataTransferDescriptor desc, GSSCredential creds) throws Exception {
        URL url = new URL(desc.getUrl());
        if (url.getProtocol().equals("http")) {
            URLConnection conn = url.openConnection();
            conn.connect();
            return conn.getInputStream();
        } else if (url.getProtocol().equals("https")) {
            GSIHttpURLConnection connection = new GSIHttpURLConnection(url);
            connection.setCredentials(creds);
            connection.connect();
            return connection.getInputStream();
        }
        throw new Exception("Protocol " + url.getProtocol() + " not supported.");
    }
    
    public static InputStream getDataStream(DataTransferDescriptor desc) throws Exception {
        URL url = new URL(desc.getUrl());
        if (url.getProtocol().equals("http")) {
            URLConnection conn = url.openConnection();
            conn.connect();
            return conn.getInputStream();
        } else if (url.getProtocol().equals("https")) {
            throw new Exception("To use the https protocol you must call the secure method:  getDataStream(DataTransferDescriptor desc, GSSCredential creds)");
        }
        throw new Exception("Protocol " + url.getProtocol() + " not supported.");
    }
}
