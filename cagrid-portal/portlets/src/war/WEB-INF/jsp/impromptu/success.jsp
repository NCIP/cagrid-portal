<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>

<%@ page import="java.util.*"%>
<%@ page import="gov.nih.nci.cagrid.portal.portlet.impromptu.ImpromptuQueryStorage"%>
<%@ page import="gov.nih.nci.cagrid.portal.portlet.impromptu.ImpromptuQuery"%>
<%@ page import="org.springframework.web.util.UrlPathHelper"%>

<%
ImpromptuQuery q = (ImpromptuQuery) request.getAttribute("impromptuQuery");

String s = request.getScheme() + "://" + request.getServerName();
s = s + ":" + request.getServerPort();
s = s + request.getContextPath() + "/xml/view/";
s = s + q.getUuid();

if (q.isHtmlSuccessPage()) {
    %>
    <html>
    <head><title>Impromptu Query Results</title></head>
    <body><a href="<%=s%>"><%=s%></a></body>
    </html>
    <%
} else {	 
	 if (ImpromptuQueryStorage.instance.getResult(q.getUuid().toString())!=null) {
	 	response.setContentType("text/xml");
		out.print(ImpromptuQueryStorage.instance.getResult(q.getUuid().toString()));
	}else{
		out.print("Query execution failed");
	}
}
%> 
