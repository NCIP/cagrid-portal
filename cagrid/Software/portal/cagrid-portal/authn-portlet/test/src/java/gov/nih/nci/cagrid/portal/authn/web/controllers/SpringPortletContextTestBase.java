package gov.nih.nci.cagrid.portal.authn.web.controllers;

import org.springframework.web.portlet.DispatcherPortlet;
import org.springframework.mock.web.portlet.MockRenderRequest;
import org.springframework.mock.web.portlet.MockRenderResponse;
import org.springframework.mock.web.portlet.MockPortletConfig;
import org.springframework.mock.web.portlet.MockPortletContext;
import org.junit.Before;

import javax.portlet.*;
import java.lang.reflect.Method;


/**
 * Base class to test portlet conrollers
 * within the Spring conext.
 * <p/>
 * It will initialize the Spring container, setup
 * a web context and invoke render and action methods
 * in the controller
 * <p/>
 * User: kherm
 *
 * @author kherm manav.kher@semanticbits.com
 */

public class SpringPortletContextTestBase<T extends TestPortletContextClassBase> {
    DispatcherPortlet simpleDispatcherPortlet;

    Class<T> contextClass;
    protected MockRenderRequest request = new MockRenderRequest();
    protected MockRenderResponse response = new MockRenderResponse();

    public SpringPortletContextTestBase(Class<T> contextClass) {
        this.contextClass = contextClass;
    }

    @Before
    public void initContext() throws Exception {
        PortletConfig simplePortletConfig = new MockPortletConfig(new MockPortletContext());
        simpleDispatcherPortlet = new DispatcherPortlet();
        simpleDispatcherPortlet.setContextClass(contextClass);
        simpleDispatcherPortlet.init(simplePortletConfig);

    }

    /**
     * Reflection to invoke protected methods in Spring Controllers
     *
     * @param req
     * @param res
     * @throws Exception
     */
    protected void doRender(RenderRequest req, RenderResponse res) throws Exception {
        Method m = simpleDispatcherPortlet.getClass().getDeclaredMethod("doRenderService", RenderRequest.class, RenderResponse.class);
        m.setAccessible(true);
        m.invoke(simpleDispatcherPortlet, req, res);

    }

    protected void doAction(ActionRequest req, ActionResponse res) throws Exception {
        Method m = simpleDispatcherPortlet.getClass().getDeclaredMethod("doActionService", ActionRequest.class, ActionResponse.class);
        m.setAccessible(true);
        m.invoke(simpleDispatcherPortlet, req, res);

    }
}
