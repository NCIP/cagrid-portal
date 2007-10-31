package gov.nih.nci.cagrid.common;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.PrintStream;

import org.apache.log4j.Logger;
import org.apache.log4j.Priority;


/**
 * StreamGobbler 
 * Reads input from a stream as long as more data is available
 * 
 * @author David Ervin
 * @author Shannon Hastings
 * @created Jun 21, 2007 11:03:06 AM
 * @version $Id: StreamGobbler.java,v 1.4 2007-10-31 18:35:24 dervin Exp $
 */
public class StreamGobbler extends Thread {

    public static final String TYPE_OUT = "OUT";
    public static final String TYPE_ERR = "ERR";

    private Logger logger = null;
    private Priority priority = null;
    private InputStream gobble;
    private String type;
    private PrintStream redirect;
    
    /**
     * Creates a stream gobbler which will just read the input stream until it's gone
     * 
     * @param is
     * @param type
     */
    public StreamGobbler(InputStream is, String type) {
        this(is, type, null);
    }
    

    /**
     * Creates a stream gobbler to consume an input stream and redirect its 
     * contents to an output stream
     * 
     * @param is
     * @param type
     * @param redrirect
     */
    public StreamGobbler(InputStream is, String type, OutputStream redrirect) {
        this.gobble = is;
        this.type = type;
        if (redrirect != null) {
            this.redirect = new PrintStream(redrirect);
        }
    }


    /**
     * Creates a stream gobbler to consume an input stream and redirect its
     * contents to a Log4J logger
     * 
     * @param is
     * @param type
     * @param logger
     * @param priority
     */
    public StreamGobbler(InputStream is, String type, Logger logger, Priority priority) {
        this.gobble = is;
        this.type = type;
        this.logger = logger;
        this.priority = priority;
    }


    /**
     * creates readers to handle the text created by the external program
     */
    public void run() {
        try {
            InputStreamReader isr = new InputStreamReader(gobble);
            BufferedReader br = new BufferedReader(isr);
            String line = null;
            while ((line = br.readLine()) != null) {
                if (redirect != null || (logger != null && priority != null)) {
                    line = type + "> " + line;
                    if (redirect != null) {
                        redirect.println(line);
                    } else { // if (logger != null && priority !=null) {
                        logger.log(priority, line);
                    }
                }
            }

        } catch (IOException ioe) {
            ioe.printStackTrace();
        }
    }
}
