package gov.nih.nci.cagrid.browser.ajax;

//~--- JDK imports ------------------------------------------------------------

import javax.faces.FacesException;
import javax.faces.FactoryFinder;
import javax.faces.context.FacesContext;
import javax.faces.context.FacesContextFactory;
import javax.faces.lifecycle.Lifecycle;
import javax.faces.lifecycle.LifecycleFactory;
import javax.faces.webapp.FacesServlet;
import javax.servlet.ServletConfig;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

//~--- classes ----------------------------------------------------------------

/**
 * Created by the caGrid Team
 * User: kherm
 * Date: Jul 10, 2005
 * Time: 3:18:45 PM
 * To change this template use File | Settings | File Templates.
 */
public class AutoCompleteDiscoveryServlet extends HttpServlet {
    private static final int MAX_RESULTS_RETURNED = 3;

    //~--- fields -------------------------------------------------------------

    private ServletContext      context;
    private FacesContextFactory facesContextFactory;
    private Lifecycle           lifecycle;
    private ServletConfig       servletConfig;

    //~--- methods ------------------------------------------------------------

    public void destroy() {
        super.destroy();

        facesContextFactory = null;
        lifecycle           = null;
        servletConfig       = null;
        context             = null;
    }

    public void doGet(HttpServletRequest request, HttpServletResponse response)
            throws IOException, ServletException {

        // Acquire the FacesContext instance for this request
        FacesContext context = facesContextFactory.getFacesContext(
                                   servletConfig.getServletContext(), request,
                                   response, lifecycle);
        DiscoveryCompletetionBean application =
            (DiscoveryCompletetionBean) getBean(context,
                "DiscoveryCompletetionBean");

        if (application == null) {
            log("DiscoveryCompletionBean not found!");

            return;
        }

        String       action     = request.getParameter("action");
        String       targetId   = request.getParameter("id");
        StringBuffer sb         = new StringBuffer();
        boolean      namesAdded = false;

        if ("complete".equals(action)) {
            String[] keywords = application.getKeywords();
            int      index;

            if (targetId.length() > 0) {
                index = findClosestIndex(keywords, targetId, true);
            } else {

                // If you're trying to complete and have nothing entered,
                // just show the beginning of the list
                index = 0;
            }

            if (index == -1) {

                // Nothing found...
                index = 0;
            }

            sb.append("<discovery-keywords>");

            for (int i = 0; i < MAX_RESULTS_RETURNED; i++) {
                if (index >= keywords.length) {
                    break;
                }

                sb.append("<keyword>");
                sb.append("<name>");    // includes state.
                sb.append(keywords[index]);
                sb.append("</name>");
                sb.append("</keyword>");

                namesAdded = true;

                index++;
            }

            sb.append("</discovery-keywords>");

            if (namesAdded) {
                response.setContentType("text/xml");
                response.setHeader("Cache-Control", "no-cache");
                response.getWriter().write(sb.toString());
            } else {

                // nothing to show
                response.setStatus(HttpServletResponse.SC_NO_CONTENT);
            }
        }
    }

    public int findClosestIndex(String[] data, String key,
                                boolean ignoreCase) {
        int low    = 0;
        int high   = data.length - 1;
        int middle = -1;

        while (high > low) {
            middle = (low + high) / 2;

            int result;

            if (ignoreCase) {
                result = key.compareToIgnoreCase(data[middle]);
            } else {
                result = key.compareTo(data[middle]);
            }

            if (result == 0) {
                return middle;
            } else if (result < 0) {
                high = middle;
            } else if (low != middle) {
                low = middle;
            } else {
                break;
            }
        }

        return middle;
    }

    public void init(ServletConfig config) throws ServletException {
        this.servletConfig = config;
        this.context       = config.getServletContext();

        try {
            facesContextFactory =
                (FacesContextFactory) FactoryFinder.getFactory(
                    FactoryFinder.FACES_CONTEXT_FACTORY);
        } catch (FacesException e) {
            Throwable rootCause = e.getCause();

            if (rootCause == null) {
                throw e;
            } else {
                throw new ServletException(e.getMessage(), rootCause);
            }
        }

        try {
            LifecycleFactory lifecycleFactory =
                (LifecycleFactory) FactoryFinder.getFactory(
                    FactoryFinder.LIFECYCLE_FACTORY);
            String lifecycleId =
                servletConfig.getServletContext().getInitParameter(
                    FacesServlet.LIFECYCLE_ID_ATTR);

            if (lifecycleId == null) {
                lifecycleId = LifecycleFactory.DEFAULT_LIFECYCLE;
            }

            lifecycle = lifecycleFactory.getLifecycle(lifecycleId);
        } catch (FacesException e) {
            Throwable rootCause = e.getCause();

            if (rootCause == null) {
                throw e;
            } else {
                throw new ServletException(e.getMessage(), rootCause);
            }
        }
    }

    //~--- get methods --------------------------------------------------------

    protected Object getBean(FacesContext context, String name) {
        return context.getApplication().getVariableResolver().resolveVariable(
            context, name);
    }
}


//~ Formatted by Jindent --- http://www.jindent.com
