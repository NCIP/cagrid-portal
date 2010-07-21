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

        if (q.isClearPrevious() && ImpromptuQueryViewController.submited.containsKey(q)) {
            ImpromptuQueryViewController.results.remove(q.getUuid());
            ImpromptuQueryViewController.submited.remove(q);
        }
        
        if (!ImpromptuQueryViewController.submited.containsKey(q)) {
            q.setUuid(UUID.randomUUID());
            ImpromptuQueryViewController.submited.put(q, q.getUuid());
            
            logger.info("q.isRunAsync() = " + q.isRunSync());
            ImpromptuQueryRunner runner = new ImpromptuQueryRunner(q);
            if (q.isRunSync()) {
                runner.run();
            } else {
                Thread t = new Thread(runner);
                t.start();
            }
            logger.info("doSubmitAction done");
            
        } else {
            q.setUuid(ImpromptuQueryViewController.submited.get(q));
        }
    }

}
