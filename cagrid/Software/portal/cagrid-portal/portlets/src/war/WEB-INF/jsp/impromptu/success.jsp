<%@ page language="java" contentType="text/xml" pageEncoding="UTF-8"%>

<%@ page import="java.util.*"%>
<%@ page import="gov.nih.nci.cagrid.portal.portlet.impromptu.ImpromptuQueryViewController"%>
<%@ page import="gov.nih.nci.cagrid.portal.portlet.impromptu.ImpromptuQuery"%>
<%@ page import="org.springframework.web.util.UrlPathHelper"%>

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
