package gov.nih.nci.cagrid.portal.portlet.gss;

import gov.nih.nci.cagrid.portal.portlet.discovery.DiscoveryModel;

import javax.portlet.RenderRequest;
import javax.portlet.RenderResponse;
import org.springframework.web.portlet.ModelAndView;
import org.springframework.web.portlet.mvc.AbstractController;

public class RefreshController extends AbstractController {

    private DiscoveryModel discoveryModel;
    
    @Override
    protected ModelAndView handleRenderRequestInternal(RenderRequest request, RenderResponse response) throws Exception {
        
        System.out.println("+++++++++++++++++++++++++++++++++++++++++++++");
        System.out.println("getDiscoveryModel().getLiferayUser().isAdmin()="+getDiscoveryModel().getLiferayUser().isAdmin());
        
        ModelAndView mav = new ModelAndView("refresh-started-ok");
        /*Principal p = request.getUserPrincipal();
        System.out.println("Principal = " + p);
        if (p != null) {
            System.out.println("RefreshController: Principal name = " + p.getName());
        }*/
        /*if (request.isUserInRole("Administrator")) {
            boolean b = GridSummaryService.instance.triggerCalculator();
            mav.addObject("triggered", new Boolean(b));
        } else {
            mav.addObject("triggered", "User not in role ");
        }*/
        boolean b = GridSummaryService.instance.triggerCalculator();
        mav.addObject("triggered", new Boolean(b));

        return mav;
    }

    public DiscoveryModel getDiscoveryModel() {
        return discoveryModel;
    }

    public void setDiscoveryModel(DiscoveryModel discoveryModel) {
        this.discoveryModel = discoveryModel;
    }
}
