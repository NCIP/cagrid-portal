<%@ include file="/WEB-INF/jsp/include/includes.jspf" %>
<%@ include file="/WEB-INF/jsp/browse/common/top.jspf" %>
<%
boolean userIsAdmin = request.isUserInRole("catalog-admin");
boolean userIsOwnerUser = false;
%>
<%@ include file="/WEB-INF/jsp/browse/related_items_content.jspf" %>