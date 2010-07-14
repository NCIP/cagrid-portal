package gov.nih.nci.cagrid.portal.portlet.impromptu;

import java.util.UUID;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.web.servlet.mvc.SimpleFormController;

public class ImpromptuQueryFormController extends SimpleFormController {

    private final Log logger = LogFactory.getLog(getClass());

    @Override
    protected void doSubmitAction(Object command) throws Exception {
        ImpromptuQuery q = (ImpromptuQuery) command;

        if (!ImpromptuQueryViewController.submited.containsKey(q)) {
            q.setUuid(UUID.randomUUID());
            ImpromptuQueryViewController.submited.put(q, q.getUuid());
            Thread t = new Thread(new ImpromptuQueryRunner(q));
            t.start();
        } else {
            q.setUuid(ImpromptuQueryViewController.submited.get(q));
        }
    }

}
