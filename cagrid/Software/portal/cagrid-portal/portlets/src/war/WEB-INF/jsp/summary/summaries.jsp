<%@ page import="org.json.*" %>
<%@ page import="gov.nih.nci.cagrid.portal.portlet.gss.GridSummaryService" %>
<%@ page import="gov.nih.nci.cagrid.portal.portlet.gss.GSSRun" %>
<%@ page import="gov.nih.nci.cagrid.portal.portlet.gss.SummaryQueryResults" %>
<%@ page import="gov.nih.nci.cagrid.portal.portlet.gss.SummaryQueryWithLocations" %>
<%@ page import="java.util.Iterator" %>
<%@ page import="java.util.List" %>
<%@ page import="org.springframework.context.ApplicationContext" %>
<%@ page import="org.springframework.web.servlet.support.RequestContextUtils" %>


<div class="gss_section">caArray Statistics:</div>

<% 
//ApplicationContext context = RequestContextUtils.getWebApplicationContext(request);
GSSRun lastRun = GridSummaryService.instance.getHistory().getLast();
if (lastRun != null) {
	List<SummaryQueryResults> results = lastRun.getResults();
	
	for (SummaryQueryResults r : results) {
		%>
		<div class="gss_line"><span><%= r.getQuery().getCaption() %>:</span><span id="<%= r.getQuery().getCaption().replace(" ", "-") %>-summary"><%= r.getTotal() %></span></div>
		<%
	}
}
%>
    
<%
java.util.Date lastUpdated = GridSummaryService.lastUpdated;
java.text.Format formatter = new java.text.SimpleDateFormat("HH:mm z MMM dd, yyyy");
if (lastUpdated != null) {
	%><div class="gss_last_update">as of <%=formatter.format(lastUpdated)%></div><%
}
%>
	