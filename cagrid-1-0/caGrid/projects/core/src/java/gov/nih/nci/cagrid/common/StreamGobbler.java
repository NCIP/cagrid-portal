package gov.nih.nci.cagrid.common;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.PrintWriter;


/**
 * StreamGobbler 
 * Reads input from a stream as long as more data is available 
 * and optionally writes it out to another stream (eg System.out or System.err);
 * 
 * @author David Ervin
 * 
 * @created Jun 21, 2007 11:03:06 AM
 * @version $Id: StreamGobbler.java,v 1.1 2007-06-21 15:07:59 dervin Exp $
 */
public class StreamGobbler extends Thread {
    public static final String TYPE_OUT = "OUT";
    public static final String TYPE_ERR = "ERR";

    private InputStream is;
    private String type;
    private OutputStream os;


    public StreamGobbler(InputStream is, String type) {
        this(is, type, null);
    }


    public StreamGobbler(InputStream is, String type, OutputStream redirect) {
        this.is = is;
        this.type = type;
        this.os = redirect;
    }


    /**
     * creates readers to handle the text created by the external program
     */
    public void run() {
        try {
            PrintWriter pw = null;
            if (os != null) {
                pw = new PrintWriter(os);
            }

            InputStreamReader isr = new InputStreamReader(is);
            BufferedReader br = new BufferedReader(isr);
            String line = null;
            while ((line = br.readLine()) != null) {
                if (pw != null) {
                    pw.println(type != null ? type + "> " + line : line);
                }
            }
            if (pw != null) {
                pw.flush();
            }
        } catch (IOException ioe) {
            ioe.printStackTrace();
        }
    }
}
