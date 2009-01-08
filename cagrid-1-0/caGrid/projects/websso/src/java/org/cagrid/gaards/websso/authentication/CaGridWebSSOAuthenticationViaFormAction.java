package org.cagrid.gaards.websso.authentication;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import javax.servlet.ServletContext;
import javax.xml.namespace.QName;

import org.cagrid.gaards.websso.beans.AuthenticationServiceInformation;
import org.cagrid.gaards.websso.utils.WebSSOConstants;
import org.jasig.cas.web.flow.AuthenticationViaFormAction;
import org.jasig.cas.web.support.WebUtils;
import org.springframework.validation.DataBinder;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.context.support.WebApplicationContextUtils;
import org.springframework.webflow.execution.Event;
import org.springframework.webflow.execution.RequestContext;

public class CaGridWebSSOAuthenticationViaFormAction extends
		AuthenticationViaFormAction {

	@Override
	public Event referenceData(RequestContext context) throws Exception{
		ServletContext servletContext = WebUtils.getHttpServletRequest(context)
				.getSession().getServletContext();
		WebApplicationContext ctx =
			WebApplicationContextUtils.getRequiredWebApplicationContext(servletContext);
		AuthenticationProfileServiceManager serviceManager=(AuthenticationProfileServiceManager)ctx.getBean(WebSSOConstants.SERVICE_MANAGER);
		context.getFlowScope().put("doriansInformationList",serviceManager.getDorianInformationList());
		
		if(context.getFlowScope().get("authenticationServiceInformationList")==null)
			context.getFlowScope().put("authenticationServiceInformationList",new ArrayList<AuthenticationServiceInformation>());
		if(context.getFlowScope().get("authenticationServiceProfileInformationList")==null)
		context.getFlowScope().put("authenticationServiceProfileInformationList",new ArrayList<QName>());
		return super.referenceData(context);
	}
	
	public Event authenticationServicesInformation(RequestContext context)
			throws Exception {
		
		UsernamePasswordAuthenticationServiceURLCredentials credentials = getUserCredentials(context);
		ServletContext servletContext = WebUtils.getHttpServletRequest(context).getSession().getServletContext();
		WebApplicationContext ctx = WebApplicationContextUtils.getRequiredWebApplicationContext(servletContext);
		AuthenticationProfileServiceManager serviceManager = (AuthenticationProfileServiceManager) ctx.getBean(WebSSOConstants.SERVICE_MANAGER);
		context.getFlowScope().put("authenticationServiceInformationList",serviceManager.getAuthenticationServiceInformationList(credentials.getDorianName()));
		context.getFlowScope().put("authenticationServiceProfileInformationList",new ArrayList<QName>());
		return super.referenceData(context);
	}
	
	@SuppressWarnings("unchecked")
	public Event authenticationServiceProfilesInformation(RequestContext context) throws Exception{
		UsernamePasswordAuthenticationServiceURLCredentials credentials = getUserCredentials(context);
		ServletContext servletContext = WebUtils.getHttpServletRequest(context)
				.getSession().getServletContext();
		WebApplicationContext ctx =
			WebApplicationContextUtils.getRequiredWebApplicationContext(servletContext);
		List<AuthenticationServiceInformation> authenticationServices=(List<AuthenticationServiceInformation>)context.getFlowScope().get("authenticationServiceInformationList");
		AuthenticationProfileServiceManager serviceManager=(AuthenticationProfileServiceManager)ctx.getBean(WebSSOConstants.SERVICE_MANAGER);
		Set<QName> authenticationServiceProfiles=serviceManager.getAuthenticationProfilesList(authenticationServices,credentials.getAuthenticationServiceURL());
		context.getFlowScope().put("authenticationServiceProfileInformationList",authenticationServiceProfiles);
		return super.referenceData(context);
	}
	
	private UsernamePasswordAuthenticationServiceURLCredentials getUserCredentials(
			RequestContext context) throws Exception {
		Object formObject = getFormObject(context);
		DataBinder binder = createBinder(context, formObject);
		doBind(context, binder);
		
		UsernamePasswordAuthenticationServiceURLCredentials credentials=(UsernamePasswordAuthenticationServiceURLCredentials)formObject;
		return credentials;
	}
}
