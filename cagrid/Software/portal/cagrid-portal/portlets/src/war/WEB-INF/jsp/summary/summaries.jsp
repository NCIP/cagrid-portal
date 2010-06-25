<%@ page import="org.json.*" %>
<%@ page import="gov.nih.nci.cagrid.portal.portlet.gss.GridSummaryService" %>
<%@ page import="java.util.Iterator" %>
<%@ page import="java.util.Map" %>

<%--
	JSONObject result = new JSONObject();
	result.put("title", "Grid Statistics");
	JSONArray groups = new JSONArray();
	result.put("groups", groups);
	
	JSONObject group1 = new JSONObject();
	group1.put("title", "caBig Site Statistics");
	groups.put(group1);
	
	JSONObject group2 = new JSONObject();
	group2.put("title", "caArray Statistics");
	groups.put(group2);
	
--%>
<%--= result --%>

<div>caArray Statistics:</div>

<% 

Map<String, Long> summaryResults = GridSummaryService.getSummaryResults() ;

Iterator<String> i = summaryResults.keySet().iterator();
while (i.hasNext()) {
    String s = i.next();
    Long l = summaryResults.get(s);
	%>
	<div><span>&nbsp;&nbsp;&nbsp;&nbsp;<%= s %>:</span><span id="<%= s %>-summary"><%= l %></span></div>
	<%
}
%>
    	