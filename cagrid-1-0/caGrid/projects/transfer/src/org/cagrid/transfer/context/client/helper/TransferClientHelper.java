package org.cagrid.transfer.context.client.helper;

import java.io.IOException;
import java.io.InputStream;
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

    public static InputStream getDataStream(DataTransferDescriptor desc, GlobusCredential creds) throws Exception {
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
    
    
    public static void main(String [] args) throws IOException{
        GlobusGSSCredentialImpl cred = null;
        try {
            cred = new GlobusGSSCredentialImpl(GlobusCredential.getDefaultCredential(),
                GSSCredential.INITIATE_AND_ACCEPT);
        } catch (GlobusCredentialException e1) {
            // TODO Auto-generated catch block
            e1.printStackTrace();
        } catch (GSSException e1) {
            // TODO Auto-generated catch block
            e1.printStackTrace();
        }
        GSIHttpURLConnection connection = null;
        try {
            connection = new GSIHttpURLConnection(new URL("https://localhost:8443/caGridTransfer/TransferServlet"));
            connection.setGSSMode(GSIConstants.MODE_SSL);
        } catch (MalformedURLException e) {
     
            e.printStackTrace();
        }
        connection.setCredentials(cred);
        InputStream stream = connection.getInputStream();
        
        int letter = stream.read();
        while(letter!=-1){
            System.out.println(String.valueOf(letter));
            letter = stream.read();
        }
        System.out.println(String.valueOf(letter));
    }
}
