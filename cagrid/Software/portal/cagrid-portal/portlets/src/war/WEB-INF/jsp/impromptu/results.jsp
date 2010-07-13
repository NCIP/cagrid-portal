<%@ page language="java" contentType="text/html; charset=ISO-8859-1" pageEncoding="ISO-8859-1"%>

<%@ page import="java.util.*"%>
<%@ page import="gov.nih.nci.cagrid.portal.portlet.gss.ImpromptuQueryViewController"%>
<%@ page import="gov.nih.nci.cagrid.portal.portlet.gss.ImpromptuQuery"%>
<%@ page import="org.springframework.web.util.UrlPathHelper"%>

<html>
	<head>
		<title>View Page</title>
	</head>
	<body>
		A : ${a}
		
		<%=  request.getRequestURI().toString() %>
		
		<%
		UrlPathHelper h = new UrlPathHelper();
		String s = h.getOriginatingRequestUri(request);
        int pos = s.lastIndexOf("/");
		String key = s.substring(pos);
		
		%>
		
		<%= s %>
		
		<hr/>
		<%
		Iterator i = ImpromptuQueryViewController.results.keySet().iterator();
		while (i.hasNext()) {
		    String k = (String)i.next();
		    %><%= k %> => <%= ImpromptuQueryViewController.results.get(k) %> <br/><%
		}
		%>
		<%-- = ImpromptuQueryViewController.results.get(key) --%>
		<hr/>
		<%
		Iterator j = ImpromptuQueryViewController.submited.keySet().iterator();
		while (j.hasNext()) {
		    ImpromptuQuery kk = (ImpromptuQuery)j.next();
		    %><%= kk %> => <%= ImpromptuQueryViewController.submited.get(kk) %> <br/><%
		}
		%>
	</body>
</html>