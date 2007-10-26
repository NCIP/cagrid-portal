package gov.nih.nci.cagrid.common;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

import org.apache.log4j.Logger;
import org.apache.log4j.Priority;


/**
 * StreamGobbler Reads input from a stream as long as more data is available
 * 
 * @author David Ervin
 * @author Shannon Hastings
 * @created Jun 21, 2007 11:03:06 AM
 * @version $Id: StreamGobbler.java,v 1.3 2007-10-26 17:58:28 hastings Exp $
 */
public class StreamGobbler extends Thread {

    private Logger logger = null;
    private Priority priority = null;

    public static final String TYPE_OUT = "OUT";
    public static final String TYPE_ERR = "ERR";

    private InputStream is;
    private String type;


    public StreamGobbler(InputStream is, String type) {
        this.is = is;
        this.type = type;
    }


    public StreamGobbler(InputStream is, String type, Logger logger, Priority priority) {
        this.is = is;
        this.type = type;
        this.logger = logger;
        this.priority = priority;
    }


    /**
     * creates readers to handle the text created by the external program
     */
    public void run() {
        try {

            InputStreamReader isr = new InputStreamReader(is);
            BufferedReader br = new BufferedReader(isr);
            String line = null;
            while ((line = br.readLine()) != null) {
                if (logger != null && priority !=null) {
                    logger.log(priority,line);
                }
            }

        } catch (IOException ioe) {
            ioe.printStackTrace();
        }
    }
}
