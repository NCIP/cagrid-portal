<%@ page language="java" contentType="text/xml" pageEncoding="UTF-8"%>

<%@ page import="java.util.*"%>
<%@ page import="gov.nih.nci.cagrid.portal.portlet.impromptu.ImpromptuQueryViewController"%>
<%@ page import="gov.nih.nci.cagrid.portal.portlet.impromptu.ImpromptuQuery"%>
<%@ page import="org.springframework.web.util.UrlPathHelper"%>

<%
	UrlPathHelper h = new UrlPathHelper();
	String s = h.getOriginatingRequestUri(request);
	int pos = s.lastIndexOf("/");
	String key = s.substring(pos+1);
%>
<%--= key --%>
<%= ImpromptuQueryViewController.results.get(key) %>

<%--
	Iterator i = ImpromptuQueryViewController.results.keySet().iterator();
	while (i.hasNext()) {
	    String k = (String)i.next();
	    out.println("==="+k+"===" + " => " + ImpromptuQueryViewController.results.get(k));
	}

--%>