package org.cagrid.transfer.context.client.helper;

import java.io.InputStream;
import java.net.URL;
import java.net.URLConnection;

import org.cagrid.transfer.descriptor.DataTransferDescriptor;
import org.globus.net.GSIHttpURLConnection;


public class TransferClientHelper {

    public InputStream getDataStream(DataTransferDescriptor desc) throws Exception {
        URL url = new URL(desc.getUrl());
        if (url.getProtocol().equals("http")) {
            URLConnection conn = url.openConnection();
            conn.connect();
            return conn.getInputStream();
        } else if (url.getProtocol().equals("https")) {
            GSIHttpURLConnection connection = new GSIHttpURLConnection(url);
            connection.connect();
            return connection.getInputStream();
        }
        throw new Exception("Protocol " + url.getProtocol() + " not supported.");
    }
}
