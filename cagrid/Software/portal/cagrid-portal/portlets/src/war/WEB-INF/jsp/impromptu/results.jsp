<%@ page language="java" contentType="text/html" pageEncoding="UTF-8"%>

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
<html><head><title>Impromptu Query Results</title></head><body>
  <b>Query Results:</b> <br>
<% if (ImpromptuQueryStorage.instance.getResult(key)!=null) { %>
	<label for="queryresults"/>
	<textarea name="queryresults" id="queryresults" readonly="true" cols="150" rows="25">
	<%=ImpromptuQueryStorage.instance.getResult(key)%> 
	</textarea>
<%	 
}else{ 	 
	 out.print("Query execution failed");
}

%>
 </body></html>    
    
