package org.cagrid.websso.client.acegi;

import java.io.IOException;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.acegisecurity.ui.logout.LogoutFilter;
import org.acegisecurity.ui.logout.LogoutHandler;

public class WebSSOAcegiLogoutFilter extends LogoutFilter {

	private boolean useRelativePath;
	private WebSSOLogoutHandler handler;

	public WebSSOAcegiLogoutFilter(String logoutSuccessUrl,
			LogoutHandler[] handlers, boolean useRelativePath) {
		super(logoutSuccessUrl, handlers);
		for (LogoutHandler logoutHandler : handlers) {
			if (logoutHandler instanceof WebSSOLogoutHandler) {
				this.handler = (WebSSOLogoutHandler) handlers[0];
				break;
			}
		}
		this.useRelativePath = useRelativePath;
	}

	@Override
	protected void sendRedirect(HttpServletRequest request,
			HttpServletResponse response, String url) throws IOException {

		if (useRelativePath) {
			super.sendRedirect(request, response, url);
		} else {
			String logoutURL = handler.getLogoutURL();
			super.sendRedirect(request, response, logoutURL);
		}
	}
}
