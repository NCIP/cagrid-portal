<%@ page language="java" contentType="text/xml" pageEncoding="UTF-8"%>

<%@ page import="java.util.*"%>
<%@ page import="gov.nih.nci.cagrid.portal.portlet.impromptu.ImpromptuQueryViewController"%>
<%@ page import="gov.nih.nci.cagrid.portal.portlet.impromptu.ImpromptuQuery"%>
<%@ page import="org.springframework.web.util.UrlPathHelper"%>

<%--
	UrlPathHelper h = new UrlPathHelper();
	String s = h.getOriginatingRequestUri(request);
	int pos = s.lastIndexOf("/");
	String key = s.substring(pos+1);
--%>
<%--= ImpromptuQueryViewController.results.get(key) --%>

<%--@ page language="java" contentType="text/plain; charset=UTF-8" pageEncoding="UTF-8" --%>

<%--
Enumeration e = request.getAttributeNames();
while (e.hasMoreElements()) {
    String s1 = (String) e.nextElement();
    out.println(s1 + " = " + request.getAttribute(s1));
}
--%>
<%
ImpromptuQuery q = (ImpromptuQuery) request.getAttribute("impromptuQuery");

if (q.isRunSync()) {
    UrlPathHelper h = new UrlPathHelper();
	String orig = h.getOriginatingRequestUri(request);
	int pos = orig.lastIndexOf("/");
	String key = orig.substring(pos+1);
	out.print(ImpromptuQueryViewController.results.get(key));
} else {
    String s = request.getScheme() + "://" + request.getServerName();
    s = s + ":" + request.getServerPort();
    s = s + request.getContextPath() + "/xml/view/";
	out.print(s + q.getUuid());
}
%>
 

<%--
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>

<html>
	<head>
		<title>Success Page</title>
	</head>
	<body>
		<h3>Query Submitted Successfully.</h3> 
		The results will be shown in this URL: <a href="view/${impromptuQuery.uuid}">view/${impromptuQuery.uuid}</a>
		
		<% - -
		<table>
			<tr>
				<td valign="top" style="font-weight:bold" >Query :</td>
				<td><textarea path="query"  cols="80" rows="17"/>${impromptuQuery.query}</textarea></td>
			</tr>
			<tr>
				<td valign="top" style="font-weight:bold" >Endpoint Url :</td>
				<td>${impromptuQuery.endpointUrl}</td>
			</tr>
		</table>
	    - - %>
	     
	</body>
</html>
 --%>