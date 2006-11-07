
/**
 * Created by the caGrid Team
 * User: kherm
 * Date: Jul 13, 2005
 * Time: 12:13:04 PM
 * To change this template use File | Settings | File Templates.
 */
package gov.nih.nci.cagrid.browser.util;

//~--- JDK imports ------------------------------------------------------------

import javax.faces.application.Application;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import java.io.InputStream;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

//~--- classes ----------------------------------------------------------------

public class ApplicationCtx {
    public static final String CABIG_XML_QUERY_ACTIVITY =
        "caBIGXMLQueryActivity";
    private static ApplicationCtx ourInstance = new ApplicationCtx();

    //~--- constructors -------------------------------------------------------

    private ApplicationCtx() {}

    //~--- methods ------------------------------------------------------------

    public static InputStream loadResourceAsStream(String fileName) {
        ExternalContext cont =
            FacesContext.getCurrentInstance().getExternalContext();
        InputStream stream = cont.getResourceAsStream(fileName);

        return stream;
    }

    public static void logError(String message) {
        Level level = Level.SEVERE;

        getAppLogger().log(level, message);
    }

    public static void logInfo(String message) {
        Level level = Level.INFO;

        getAppLogger().log(level, message);
    }

    //~--- get methods --------------------------------------------------------

    public static Logger getAppLogger() {
        return Logger.global;
    }

    public static Object getBean(String beanName) {
        FacesContext ctx = FacesContext.getCurrentInstance();
        Application  app = ctx.getApplication();

        return app.createValueBinding(beanName).getValue(ctx);
    }

    public static Object getParameter(String paramName) {
        Map reqMap =
            FacesContext.getCurrentInstance().getExternalContext()
                .getRequestMap();
        return reqMap.get(paramName);
    }
}


//~ Formatted by Jindent --- http://www.jindent.com
