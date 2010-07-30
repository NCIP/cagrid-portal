package gov.nih.nci.cagrid.portal.portlet.impromptu;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.validation.BindException;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.SimpleFormController;

public class ImpromptuQueryFormController extends SimpleFormController {

    static private ThreadLocal<HttpServletResponse> res = new ThreadLocal<HttpServletResponse>();

    private final Log logger = LogFactory.getLog(getClass());

    private long minMillisecondsBetweenQuerySubmissions;
    private int maxCachedQueriesCount;

    @Override
    protected ModelAndView onSubmit(HttpServletRequest request, HttpServletResponse response, Object command, BindException errors) throws Exception {

        ImpromptuQueryFormController.res.set(response);

        return super.onSubmit(request, response, command, errors);
    }

    @Override
    protected void doSubmitAction(Object command) throws Exception {

        ImpromptuQuery q = (ImpromptuQuery) command;

        try {
            q = ImpromptuQueryStorage.instance.submit(q, this.maxCachedQueriesCount, this.minMillisecondsBetweenQuerySubmissions);
        } catch (ImpromptuQueryStorageFullException ef) {
            ImpromptuQueryFormController.res.get().sendError(HttpServletResponse.SC_FORBIDDEN);
        } catch (ImpromptuQueryStorageSubmissiosTooCloseException ef) {
            ImpromptuQueryFormController.res.get().sendError(HttpServletResponse.SC_FORBIDDEN);
        }

    }

    public long getMinMillisecondsBetweenQuerySubmissions() {
        return minMillisecondsBetweenQuerySubmissions;
    }

    public void setMinMillisecondsBetweenQuerySubmissions(long minMillisecondsBetweenQuerySubmissions) {
        this.minMillisecondsBetweenQuerySubmissions = minMillisecondsBetweenQuerySubmissions;
    }

    public int getMaxCachedQueriesCount() {
        return maxCachedQueriesCount;
    }

    public void setMaxCachedQueriesCount(int maxCachedQueriesCount) {
        this.maxCachedQueriesCount = maxCachedQueriesCount;
    }
}
