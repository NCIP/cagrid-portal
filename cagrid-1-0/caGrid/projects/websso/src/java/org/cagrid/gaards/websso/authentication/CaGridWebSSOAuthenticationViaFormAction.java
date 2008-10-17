package org.cagrid.gaards.websso.authentication;

import javax.servlet.ServletContext;

import org.cagrid.gaards.websso.utils.WebSSOConstants;
import org.jasig.cas.web.flow.AuthenticationViaFormAction;
import org.jasig.cas.web.support.WebUtils;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.context.support.WebApplicationContextUtils;
import org.springframework.webflow.execution.Event;
import org.springframework.webflow.execution.RequestContext;

public class CaGridWebSSOAuthenticationViaFormAction extends AuthenticationViaFormAction
{
	@Override
	public Event referenceData(RequestContext context) throws Exception
	{
		ServletContext servletContext = WebUtils.getHttpServletRequest(context)
				.getSession().getServletContext();
		WebApplicationContext ctx =
			WebApplicationContextUtils.getRequiredWebApplicationContext(servletContext);
		AuthenticationProfileServiceManager serviceManager=(AuthenticationProfileServiceManager)ctx.getBean(WebSSOConstants.SERVICE_MANAGER);
		context.getRequestScope().put("authenticationServiceInformationList",serviceManager.getAuthenticationServiceInformationList());
		return super.referenceData(context);
	}

	@Override
	protected void initAction()
	{
        if (this.getFormObjectClass() == null)
        {
            this.setFormObjectClass(UsernamePasswordAuthenticationServiceURLCredentials.class);
            this.setFormObjectName("credentials");
            this.setValidator(new UsernamePasswordAuthenticationServiceURLCredentialsValidator());
        }
		super.initAction();
	}
}
