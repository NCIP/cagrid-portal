package gov.nih.nci.cagrid.portal.web;


import javax.faces.context.FacesContext;
import javax.servlet.ServletException;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.Map;

public class ErrorDisplayServlet {
    private boolean showError = true;
    private int recurseCount = 0;


    public String getStackTrace() {
        FacesContext context = FacesContext.getCurrentInstance();
        Map requestMap = context.getExternalContext().getRequestMap();
        Throwable ex = (Throwable) requestMap.get("javax.servlet.error.exception");
        StringWriter writer = new StringWriter();
        PrintWriter pw = new PrintWriter(writer);

        recurseCount = 0;


        fillStackTrace(ex, pw);

        return writer.toString();
    }

    private void fillStackTrace(Throwable ex, PrintWriter pw) {
        /** prevent infinte loops **/
        if (recurseCount > 10)
            return;
        else //increment
            recurseCount++;
        if (null == ex || pw == null) {
            return;
        }

        ex.printStackTrace(pw);

        if (ex instanceof ServletException) {
            Throwable cause = ((ServletException) ex).getRootCause();

            if (null != cause) {
                pw.println("Root Cause:");
                fillStackTrace(cause, pw);
            }
        } else {
            Throwable cause = ex.getCause();

            if (null != cause) {
                pw.println("Cause:");
                fillStackTrace(cause, pw);
            }
        }
    }

    public boolean isShowError() {
        return showError;
    }

}