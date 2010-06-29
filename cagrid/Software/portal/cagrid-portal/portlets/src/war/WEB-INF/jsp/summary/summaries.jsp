<%@ page import="org.json.*" %>
<%@ page import="gov.nih.nci.cagrid.portal.portlet.gss.GridSummaryService" %>
<%@ page import="gov.nih.nci.cagrid.portal.portlet.gss.SummaryQueryWithLocations" %>
<%@ page import="java.util.Iterator" %>
<%@ page import="java.util.List" %>
<%@ page import="org.springframework.context.ApplicationContext" %>
<%@ page import="org.springframework.web.servlet.support.RequestContextUtils" %>


<div>caArray Statistics:</div>

<% 
ApplicationContext context = RequestContextUtils.getWebApplicationContext(request);
List<SummaryQueryWithLocations> queries = ((GridSummaryService) context.getBean("gridSummaryService")).getQueries();

for (SummaryQueryWithLocations q : queries) {
	%>
	<div><span>&nbsp;&nbsp;&nbsp;&nbsp;<%= q.getCaption() %>:</span><span id="<%= q.getCaption().replace(" ", "-") %>-summary"><%= q.getSum() %></span></div>
	<%
}
%>
    	