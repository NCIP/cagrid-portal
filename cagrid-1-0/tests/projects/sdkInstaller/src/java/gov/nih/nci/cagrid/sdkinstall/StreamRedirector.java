package gov.nih.nci.cagrid.sdkinstall;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

/** 
 *  StreamRedirector
 *  Redirects one stream to another as bytes are available in the first
 * 
 * @author David Ervin
 * 
 * @created Jun 18, 2007 11:10:47 AM
 * @version $Id: StreamRedirector.java,v 1.1 2007-06-18 15:29:39 dervin Exp $ 
 */
public class StreamRedirector extends Thread {
    private InputStream in;
    private OutputStream out;

    public StreamRedirector(InputStream in, OutputStream out) {
        this.in = in;
        this.out = out;
    }
    
    
    public void run() {
        byte[] buff = new byte[1024];
        int len = -1;
        try {
            while ((len = in.read(buff)) != -1) {
                out.write(buff, 0, len);
            }
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }
}
