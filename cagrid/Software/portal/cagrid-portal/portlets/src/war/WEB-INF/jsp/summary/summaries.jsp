<%@ page import="org.json.*" %>
<%@ page import="gov.nih.nci.cagrid.portal.portlet.gss.GridSummaryService" %>
<%@ page import="gov.nih.nci.cagrid.portal.portlet.gss.SummaryQueryWithLocations" %>
<%@ page import="java.util.Iterator" %>
<%@ page import="java.util.List" %>
<%@ page import="org.springframework.context.ApplicationContext" %>
<%@ page import="org.springframework.web.servlet.support.RequestContextUtils" %>


<div class="gss_section">caArray Statistics:</div>

<% 
ApplicationContext context = RequestContextUtils.getWebApplicationContext(request);
List<SummaryQueryWithLocations> queries = ((GridSummaryService) context.getBean("gridSummaryService")).getQueries();

for (SummaryQueryWithLocations q : queries) {
	%>
	<div class="gss_line"><span><%= q.getCaption() %>:</span><span id="<%= q.getCaption().replace(" ", "-") %>-summary"><%= q.getSum() %></span></div>
	<%
}
%>
    
<%
java.util.Date lastUpdated = GridSummaryService.lastUpdated;
java.text.Format formatter = new java.text.SimpleDateFormat("HH:mm z MMM dd, yyyy");
if (lastUpdated != null) {
	%><div class="gss_last_update">as of <%=formatter.format(lastUpdated)%></div><%
}
%>
	