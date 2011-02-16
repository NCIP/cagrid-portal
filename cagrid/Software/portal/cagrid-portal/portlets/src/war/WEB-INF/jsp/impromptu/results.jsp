<%@ page language="java" contentType="text/xml" pageEncoding="UTF-8"%>

<%@ page import="java.util.*"%>
<%@ page import="gov.nih.nci.cagrid.portal.portlet.impromptu.ImpromptuQueryStorage"%>
<%@ page import="gov.nih.nci.cagrid.portal.portlet.impromptu.ImpromptuQuery"%>
<%@ page import="org.springframework.web.util.UrlPathHelper"%>

<%
	UrlPathHelper h = new UrlPathHelper();
	String s = h.getOriginatingRequestUri(request);
	int pos = s.lastIndexOf("/");
	String key = s.substring(pos+1);
%>
<% if (ImpromptuQueryStorage.instance.getResult(key)!=null) {
	 out.println(ImpromptuQueryStorage.instance.getResult(key));
}else{
	 out.println("<QueryResults>Query execution failed</QueryResults>");
}

%>
