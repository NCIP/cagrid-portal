package gov.nih.nci.cagrid.data.service.auditing;

import gov.nih.nci.cagrid.common.Utils;
import gov.nih.nci.cagrid.data.DataServiceConstants;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.text.DateFormat;
import java.util.Date;
import java.util.Properties;

/** 
 *  FileDataServiceAuditor
 *  A simple data service auditor which logs information to a file
 * 
 * @author David Ervin
 * 
 * @created May 21, 2007 9:53:51 AM
 * @version $Id: FileDataServiceAuditor.java,v 1.1 2007-05-24 16:11:00 dervin Exp $ 
 */
public class FileDataServiceAuditor extends DataServiceAuditor {
    
    public static final String AUDIT_FILE = "auditingFileName";
    public static final String DEFAULT_AUDIT_FILE = "dataServiceAuditing.log";
    public static final String PRINT_FULL_RESULTS = "printFullResults";
    public static final String DEFAULT_PRINT_FULL_RESULTS = Boolean.FALSE.toString();
    
    private BufferedWriter writer = null;
   
    public FileDataServiceAuditor() {
        super();
    }
    
    
    public Properties getDefaultConfigurationProperties() {
        Properties props = new Properties();
        props.setProperty(AUDIT_FILE, DEFAULT_AUDIT_FILE);
        return props;
    }


    public void auditQueryBegin(QueryBeginAuditingEvent event) {
        try {
            getWriter().write("Query Begins at " + getCurrentTime() + "\n");
            getWriter().write(getBaseEventInformation(event));
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }


    public void auditQueryProcessingFailed(QueryProcessingFailedAuditingEvent event) {
        try {
            getWriter().write("Query Processing Failed at " + getCurrentTime() + "\n");
            getWriter().write(getBaseEventInformation(event));
            StringWriter exceptionStringWriter = new StringWriter();
            PrintWriter exceptionPrintWriter = new PrintWriter(exceptionStringWriter);
            event.getQueryProcessingException().printStackTrace(exceptionPrintWriter);
            getWriter().write("\tException follows:\n");
            getWriter().write(exceptionStringWriter.getBuffer().toString());
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }


    public void auditQueryResults(QueryResultsAuditingEvent event) {
        try {
            boolean fullResults = Boolean.valueOf(
                getConfiguredProperties().getProperty(PRINT_FULL_RESULTS)).booleanValue();
            
            getWriter().write("Query Results at " + getCurrentTime() + "\n");
            getWriter().write(getBaseEventInformation(event));
            if (fullResults) {
                getWriter().write("Query Results follow:\n");
                StringWriter resultsWriter = new StringWriter();
                try {
                    Utils.serializeObject(event.getResults(), 
                        DataServiceConstants.CQL_RESULT_SET_QNAME, resultsWriter);
                } catch (Exception ex) {
                    resultsWriter.append("ERROR SERIALIZING CQL RESULTS: " + ex.getMessage());
                    ex.printStackTrace();
                }
                getWriter().write(resultsWriter.getBuffer().toString());
            } else {
                String resultType = null;
                if (event.getResults().getAttributeResult() != null) {
                    resultType = "Attribute";
                } else if (event.getResults().getCountResult() != null) {
                    resultType = "Count";
                } else {
                    resultType = "Object"; 
                }
                getWriter().write("Query Returned " + resultType + " results");
            }
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }


    public void auditValidation(ValidationAuditingEvent event) {
        try {
            getWriter().write("Validation Failed at " + getCurrentTime() + "\n");
            getWriter().write(getBaseEventInformation(event));
            StringWriter exceptionStringWriter = new StringWriter();
            PrintWriter exceptionPrintWriter = new PrintWriter(exceptionStringWriter);
            if (event.getCqlStructureException() != null) {
                getWriter().write("\tCQL Structure Validation failed:\n");
                event.getCqlStructureException().printStackTrace(exceptionPrintWriter);
            } else {
                getWriter().write("\tDomain Validation failed:\n");
                event.getDomainValidityException().printStackTrace(exceptionPrintWriter);
            }
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }
    
    
    private String getCurrentTime() {
        return DateFormat.getDateInstance(DateFormat.MEDIUM).format(new Date());
    }
    
    
    private String getBaseEventInformation(BaseAuditingEvent event) {
        StringBuffer buff = new StringBuffer();
        buff.append("\t").append("Caller: ").append(event.getCallerId()).append("\n");
        StringWriter cqlWriter = new StringWriter();
        try {
            Utils.serializeObject(event.getQuery(), DataServiceConstants.CQL_QUERY_QNAME, cqlWriter);
        } catch (Exception ex) {
            cqlWriter.append("ERROR SERIALIZING QUERY: " + ex.getMessage());
            ex.printStackTrace();
        }
        buff.append("\t").append(cqlWriter.getBuffer().toString()).append("\n");
        return buff.toString();
    }

    
    private BufferedWriter getWriter() throws IOException {
        if (writer == null) {
            String outputFilename = getConfiguredProperties().getProperty(AUDIT_FILE);
            writer = new BufferedWriter(new FileWriter(outputFilename));
        }
        return writer;
    }
    
    
    public void finalize() {
        try {
            getWriter().flush();
            getWriter().close();
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }
}
