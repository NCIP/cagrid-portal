<%@ page language="java" contentType="text/xml" pageEncoding="UTF-8"%>

<%@ page import="java.util.*"%>
<%@ page import="gov.nih.nci.cagrid.portal.portlet.impromptu.ImpromptuQueryStorage"%>
<%@ page import="gov.nih.nci.cagrid.portal.portlet.impromptu.ImpromptuQuery"%>
<%@ page import="org.springframework.web.util.UrlPathHelper"%>

<%
ImpromptuQuery q = (ImpromptuQuery) request.getAttribute("impromptuQuery");

String s = request.getScheme() + "://" + request.getServerName();
s = s + ":" + request.getServerPort();
s = s + request.getContextPath() + "/xml/view/";
out.print(s + q.getUuid());
%>
