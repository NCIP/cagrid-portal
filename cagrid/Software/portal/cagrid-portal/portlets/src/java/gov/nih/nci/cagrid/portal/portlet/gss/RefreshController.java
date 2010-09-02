package gov.nih.nci.cagrid.portal.portlet.gss;

import gov.nih.nci.cagrid.portal.portlet.discovery.DiscoveryModel;

import javax.portlet.RenderRequest;
import javax.portlet.RenderResponse;
import org.springframework.web.portlet.ModelAndView;
import org.springframework.web.portlet.mvc.AbstractController;

public class RefreshController extends AbstractController {

    private DiscoveryModel discoveryModel;

    public DiscoveryModel getDiscoveryModel() {
        return discoveryModel;
    }

    public void setDiscoveryModel(DiscoveryModel discoveryModel) {
        this.discoveryModel = discoveryModel;
    }

    @Override
    public ModelAndView handleRenderRequest(RenderRequest request, RenderResponse response) throws Exception {
        System.out.println("+++++++++++++++++++++++++++++++++++++++++++++");

        ModelAndView mav = new ModelAndView("refresh-started-ok");

        StringBuilder msg = new StringBuilder();
        
        //msg.append("+update stats process triggered? "+GridSummaryService.instance.triggerCalculator() + "</br>");
        
        if (getDiscoveryModel() != null) {
            if (getDiscoveryModel().getLiferayUser() != null) {
                msg.append("getDiscoveryModel().getLiferayUser().isAdmin()=" + getDiscoveryModel().getLiferayUser().isAdmin() + "<br/>");
                if (getDiscoveryModel().getLiferayUser().isAdmin()) {
                    boolean b = GridSummaryService.instance.triggerCalculator();
                    msg.append("update stats process triggered? "+new Boolean(b) + "<br/>");
                }
            } else {
                mav.addObject("message", "getDiscoveryModel().getLiferayUser() is null<br/>");
            }

        } else {
            mav.addObject("message", "getDiscoveryModel() is null<br/>");
        }

        mav.addObject("message", msg.toString());
        return mav;
    }
}
