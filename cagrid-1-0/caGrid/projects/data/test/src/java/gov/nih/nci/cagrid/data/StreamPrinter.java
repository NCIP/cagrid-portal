package gov.nih.nci.cagrid.data;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

/** 
 *  StreamPrinter
 *  TODO:DOCUMENT ME
 * 
 * @author David Ervin
 * 
 * @created May 14, 2007 9:25:42 AM
 * @version $Id: StreamPrinter.java,v 1.1 2007-05-14 16:03:12 dervin Exp $ 
 */
public class StreamPrinter extends Thread {

    private InputStream in;
    private OutputStream out;
    
    public StreamPrinter(InputStream in, OutputStream out) {
        this.in = in;
        this.out = out;
    }
    
    
    public void run() {
        byte[] buff = new byte[1024];
        int len = -1;
        try {
            while ((len = in.read(buff)) != -1) {
                out.write(buff, 0, len);
                out.flush();
            }
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }
}
