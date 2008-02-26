package org.cagrid.gaards.websso.authentication;

import org.cagrid.gaards.websso.utils.ObjectFactory;
import org.cagrid.gaards.websso.utils.WebSSOConstants;
import org.cagrid.gaards.websso.utils.WebSSOProperties;
import org.jasig.cas.web.flow.AuthenticationViaFormAction;
import org.springframework.webflow.execution.Event;
import org.springframework.webflow.execution.RequestContext;

public class CaGridWebSSOAuthenticationViaFormAction extends AuthenticationViaFormAction
{
	@Override
	public Event referenceData(RequestContext context) throws Exception
	{
		WebSSOProperties webSSOProperties = (WebSSOProperties)ObjectFactory.getObject(WebSSOConstants.WEBSSO_PROPERTIES);
		context.getRequestScope().put("authenticationServiceInformationList",webSSOProperties.getAuthenticationServiceInformationList());
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
