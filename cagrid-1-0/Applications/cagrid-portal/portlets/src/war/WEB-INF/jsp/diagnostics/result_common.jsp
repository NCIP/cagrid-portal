<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="tags" tagdir="/WEB-INF/tags" %>


<%@ page import="gov.nih.nci.cagrid.portal.portlet.diagnostics.DiagnosticResult" %>
<%
    DiagnosticResult result = (DiagnosticResult) request.getAttribute("result");
%>

