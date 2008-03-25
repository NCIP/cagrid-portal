/*
 * Created on Jul 14, 2006
 */
package gov.nih.nci.cagrid.validator.steps.base;

import gov.nih.nci.cagrid.testing.system.haste.Step;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLSession;


/**
 * This step connects to a grid service and verifies that it is running
 * 
 * @author Rakesh Dhaval
 */
public class TestServiceUpStep extends Step {

    private String serviceURL;
    String resp = "";
    String str = "";


    public TestServiceUpStep(String serviceURL) {
        this.serviceURL = serviceURL;
    }


    private void trustAllHttpsCertificates() throws Exception {
        // Trust manager does not validate certificate chains:
        javax.net.ssl.TrustManager[] trustAllCerts = new javax.net.ssl.TrustManager[1];
        javax.net.ssl.TrustManager trustManager = new MyTrustManager();
        trustAllCerts[0] = trustManager;
        javax.net.ssl.SSLContext sslContext = javax.net.ssl.SSLContext.getInstance("SSL");
        sslContext.init(null, trustAllCerts, null);
        javax.net.ssl.HttpsURLConnection.setDefaultSSLSocketFactory(sslContext.getSocketFactory());
    }


    @Override
    public void runStep() throws Throwable {
        // connect to the url and validate a return
        try {
            // tell the JRE to ignore the hostname
            HostnameVerifier hv = new HostnameVerifier() {
                public boolean verify(String urlHostName, SSLSession session) {
                    System.out.println("URL Host: " + urlHostName + " vs. " + session.getPeerHost());
                    return true;
                }
            };

            // Tell the JRE to trust any https server.
            trustAllHttpsCertificates();
            HttpsURLConnection.setDefaultHostnameVerifier(hv);

            URL url = new URL(serviceURL);
            URLConnection connection = url.openConnection();
            connection.setConnectTimeout(5000);

            if (connection instanceof HttpURLConnection) {
                HttpURLConnection httpConnection = (HttpURLConnection) connection;

                try {
                    System.out.println("----------");
                    System.out.println("Trying to connect: " + serviceURL);
                    System.out.println("URL Host: " + url.getHost());
                    httpConnection.connect();
                } catch (Exception e) {
                    e.printStackTrace();
                    fail("The URL specified was unable to connect. Connection Timed out");
                    return;
                }

                int response = httpConnection.getResponseCode();
                System.out.println("Response Code: " + response);
                // print header fields

                int n = 1;
                String key;
                while ((key = connection.getHeaderFieldKey(n)) != null) {
                    String value = connection.getHeaderField(n);
                    System.out.println(key + ": " + value);
                    n++;
                }

                System.out.println("ContentEncoding: " + connection.getContentEncoding());
                System.out.println("Date: " + connection.getDate());
                System.out.println("Expiration: " + connection.getExpiration());
                System.out.println("LastModifed: " + connection.getLastModified());
            } else if (connection instanceof HttpsURLConnection) {
                HttpsURLConnection httpsConnection = (HttpsURLConnection) connection;
                try {
                    System.out.println("----------");
                    System.out.println("Trying to connect: " + serviceURL);
                    httpsConnection.connect();
                    int response = httpsConnection.getResponseCode();
                    System.out.println("Response Code: " + response);
                } catch (Exception e) {
                    e.printStackTrace();
                    fail("The URL specified was unable to connect. Connection Timed out");
                    return;
                }
                int n = 1;
                String key;
                while ((key = connection.getHeaderFieldKey(n)) != null) {
                    String value = connection.getHeaderField(n);
                    System.out.println(key + ": " + value);
                    n++;
                }

                System.out.println("ContentLength: " + connection.getContentLength());
                System.out.println("ContentEncoding: " + connection.getContentEncoding());
                System.out.println("Expiration: " + connection.getExpiration());
                System.out.println("LastModifed: " + connection.getLastModified());
                System.out.println("----------");

                BufferedReader reader = new BufferedReader(new InputStreamReader(connection.getInputStream()));
                while (null != ((str = reader.readLine()))) {
                    if (str.length() > 0) {
                        str = str.trim();
                        if (!str.equals("")) {
                            // System.out.println(str);
                            resp += str;
                        }
                    }
                }
                reader.close();
            }
            //
        } catch (MalformedURLException mue) {
            mue.printStackTrace();
            fail(mue.getMessage());
        } catch (IOException ioe) {
            ioe.printStackTrace();
            fail(ioe.getMessage());
        } catch (Exception exception) {
            System.out.println("Error: " + exception);
            exception.printStackTrace();
            fail(exception.getMessage());
        }
    }


    public class MyTrustManager implements javax.net.ssl.TrustManager, javax.net.ssl.X509TrustManager {
        public java.security.cert.X509Certificate[] getAcceptedIssuers() {
            return null;
        }


        public boolean isServerTrusted(java.security.cert.X509Certificate[] certs) {
            return true;
        }


        public boolean isClientTrusted(java.security.cert.X509Certificate[] certs) {
            return true;
        }


        public void checkServerTrusted(java.security.cert.X509Certificate[] certs, String authType)
            throws java.security.cert.CertificateException {
            return;
        }


        public void checkClientTrusted(java.security.cert.X509Certificate[] certs, String authType)
            throws java.security.cert.CertificateException {
            return;
        }
    }
}