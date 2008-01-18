package org.cagrid.transfer.context.client.helper;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;

import org.cagrid.transfer.descriptor.DataTransferDescriptor;
import org.globus.axis.gsi.GSIConstants;
import org.globus.gsi.GlobusCredential;
import org.globus.gsi.GlobusCredentialException;
import org.globus.gsi.gssapi.GlobusGSSCredentialImpl;
import org.globus.net.GSIHttpURLConnection;
import org.ietf.jgss.GSSCredential;
import org.ietf.jgss.GSSException;


public class TransferClientHelper {

    
    public static InputStream getData(DataTransferDescriptor desc, GlobusCredential creds) throws Exception {
        URL url = new URL(desc.getUrl());
        if (url.getProtocol().equals("http")) {
            URLConnection conn = url.openConnection();
            conn.connect();
            return conn.getInputStream();
        } else if (url.getProtocol().equals("https")) {
            GlobusGSSCredentialImpl cred = new GlobusGSSCredentialImpl(creds,
                GSSCredential.INITIATE_AND_ACCEPT);
            GSIHttpURLConnection connection = new GSIHttpURLConnection(url);
            connection.setGSSMode(GSIConstants.MODE_SSL);
            connection.setCredentials(cred);
            return connection.getInputStream();
        }
        throw new Exception("Protocol " + url.getProtocol() + " not supported.");
    }
    
    public static InputStream getData(DataTransferDescriptor desc) throws Exception {
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
    
    /**
     * Get the output stream to put the data.  Be sure to close the stream
     * when done writing the data.
     * @param desc
     * @param creds
     * @return
     * @throws Exception
     */
    public static OutputStream putData(DataTransferDescriptor desc, GlobusCredential creds) throws Exception {
        URL url = new URL(desc.getUrl());
        if (url.getProtocol().equals("http")) {
            URLConnection conn = url.openConnection();
            conn.connect();
            return conn.getOutputStream();
        } else if (url.getProtocol().equals("https")) {
            GlobusGSSCredentialImpl cred = new GlobusGSSCredentialImpl(creds,
                GSSCredential.INITIATE_AND_ACCEPT);
            GSIHttpURLConnection connection = new GSIHttpURLConnection(url);
            connection.setGSSMode(GSIConstants.MODE_SSL);
            connection.setCredentials(cred);
            return connection.getOutputStream();
        }
        throw new Exception("Protocol " + url.getProtocol() + " not supported.");
    }
    
    /**
     * Gets an output stream to put the data.  Be sure to close the stream
     * when done writing to it
     * 
     * @param desc
     * @return
     * @throws Exception
     */
    public static OutputStream putData(DataTransferDescriptor desc) throws Exception {
        URL url = new URL(desc.getUrl());
        if (url.getProtocol().equals("http")) {
            URLConnection conn = url.openConnection();
            conn.connect();
            return conn.getOutputStream();
        } else if (url.getProtocol().equals("https")) {
            throw new Exception("To use the https protocol you must call the secure method:  getDataStream(DataTransferDescriptor desc, GSSCredential creds)");
        }
        throw new Exception("Protocol " + url.getProtocol() + " not supported.");
    }
 
}
