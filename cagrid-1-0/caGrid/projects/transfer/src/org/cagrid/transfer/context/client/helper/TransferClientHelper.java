package org.cagrid.transfer.context.client.helper;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.PipedInputStream;
import java.io.PipedOutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLConnection;

import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.HttpException;
import org.apache.commons.httpclient.methods.InputStreamRequestEntity;
import org.apache.commons.httpclient.methods.PostMethod;
import org.apache.commons.httpclient.methods.RequestEntity;
import org.cagrid.transfer.descriptor.DataTransferDescriptor;
import org.globus.axis.gsi.GSIConstants;
import org.globus.gsi.GlobusCredential;
import org.globus.gsi.gssapi.GlobusGSSCredentialImpl;
import org.globus.net.GSIHttpURLConnection;
import org.ietf.jgss.GSSCredential;


public class TransferClientHelper {

    /**
     * Returns a handle to the input stream of the socket which is returning the
     * data referred to by the descriptor. This method can make an https
     * connection if desired using the credentials passed in.
     * 
     * @param desc
     * @param creds
     * @return
     * @throws Exception
     */
    public static InputStream getData(DataTransferDescriptor desc, GlobusCredential creds) throws Exception {
        URL url = new URL(desc.getUrl());
        if (url.getProtocol().equals("http")) {
            URLConnection conn = url.openConnection();
            conn.connect();
            return conn.getInputStream();
        } else if (url.getProtocol().equals("https")) {
            GlobusGSSCredentialImpl cred = new GlobusGSSCredentialImpl(creds, GSSCredential.INITIATE_AND_ACCEPT);
            GSIHttpURLConnection connection = new GSIHttpURLConnection(url);
            connection.setGSSMode(GSIConstants.MODE_SSL);
            connection.setCredentials(cred);
            return connection.getInputStream();
        }
        throw new Exception("Protocol " + url.getProtocol() + " not supported.");
    }


    /**
     * Returns a handle to the input stream of the socket which is returning the
     * data referred to by the descriptor. This method cannot make secure https
     * connects and only works with http.
     * 
     * @param desc
     * @return
     * @throws Exception
     */
    public static InputStream getData(DataTransferDescriptor desc) throws Exception {
        URL url = new URL(desc.getUrl());
        if (url.getProtocol().equals("http")) {
            URLConnection conn = url.openConnection();
            conn.connect();
            return conn.getInputStream();
        } else if (url.getProtocol().equals("https")) {
            throw new Exception(
                "To use the https protocol you must call the secure method:  getDataStream(DataTransferDescriptor desc, GSSCredential creds)");
        }
        throw new Exception("Protocol " + url.getProtocol() + " not supported.");
    }


    /**
     * Get the output stream to put the data. Be sure to close the stream when
     * done writing the data. This method can use http and https if the
     * credentials are provided.
     * 
     * @param desc
     * @param creds
     * @return
     * @throws Exception
     */
    public static void putData(InputStream is, int contentLength, DataTransferDescriptor desc, GlobusCredential creds) throws Exception {
        URL url = new URL(desc.getUrl());
        if (url.getProtocol().equals("http")) {
            final PostMethod post = new PostMethod(desc.getUrl());
            InputStreamRequestEntity re = new InputStreamRequestEntity(is, contentLength);
            post.setRequestEntity(re);
            HttpClient client = new HttpClient();
            int status = client.executeMethod(post);
            return;
        } else if (url.getProtocol().equals("https")) {
            GlobusGSSCredentialImpl cred = new GlobusGSSCredentialImpl(creds, GSSCredential.INITIATE_AND_ACCEPT);
            GSIHttpURLConnection connection = new GSIHttpURLConnection(url);
            connection.setGSSMode(GSIConstants.MODE_SSL);
            connection.setCredentials(cred);
            try {
                int l;
                byte[] buffer = new byte[1024];
                while ((l = is.read(buffer)) != -1) {
                    connection.getOutputStream().write(buffer, 0, l);
                }
            } finally {
                is.close();
            }
            connection.getOutputStream().close();
            return;
        }
        throw new Exception("Protocol " + url.getProtocol() + " not supported.");
    }


    /**
     * Gets an output stream to put the data. This method can only put to an
     * http connection and not an https one. Be sure to close the stream when
     * done writing to it.
     * 
     * @param desc
     * @return
     * @throws Exception
     */
    public static void putData(InputStream is, int contentLength, DataTransferDescriptor desc) throws Exception {
        URL url = new URL(desc.getUrl());
        if (url.getProtocol().equals("http")) {
            final PostMethod post = new PostMethod(desc.getUrl());
            InputStreamRequestEntity re = new InputStreamRequestEntity(is, contentLength);
            post.setRequestEntity(re);

            HttpClient client = new HttpClient();
            int status = client.executeMethod(post);
            return;
        } else if (url.getProtocol().equals("https")) {
            throw new Exception(
                "To use the https protocol you must call the secure method:  getDataStream(DataTransferDescriptor desc, GSSCredential creds)");
        }
        throw new Exception("Protocol " + url.getProtocol() + " not supported.");
    }


    /**
     * Gets an output stream to put the data. This method can only put to an
     * http connection and not an https one. Be sure to close the stream when
     * done writing to it.
     * 
     * @param desc
     * @return
     * @throws Exception
     */
    public static void putData(byte[] data, DataTransferDescriptor desc) throws Exception {
        URL url = new URL(desc.getUrl());
        if (url.getProtocol().equals("http")) {
            PostMethod post = new PostMethod(desc.getUrl());
            ByteArrayInputStream istream = new ByteArrayInputStream(data);
            InputStreamRequestEntity isreq = new InputStreamRequestEntity(istream);
            post.setRequestEntity(isreq);
            // execute the POST
            HttpClient client = new HttpClient();
            int status = client.executeMethod(post);
            return;
        } else if (url.getProtocol().equals("https")) {
            throw new Exception(
                "To use the https protocol you must call the secure method:  getDataStream(DataTransferDescriptor desc, GSSCredential creds)");
        }
        throw new Exception("Protocol " + url.getProtocol() + " not supported.");
    }

}
