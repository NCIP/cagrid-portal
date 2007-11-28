<%@ include file="/WEB-INF/jsp/include.jsp" %>
<div style="margin-left:15px">
<portlet:actionURL var="action"/>
<form:form action="${action}" commandName="mapBean">
	<b>Categories:</b>&nbsp;
	<form:select onchange="submit()" path="selectedDirectory">
		<form:option value="">----</form:option>
		<%@ include file="/WEB-INF/jsp/disc/directory/directories.jspf" %>
	</form:select>
	<input type="hidden" name="operation" value="selectDirectory"/>
</form:form>
</div>
<br/>
<c:set var="selectItemOperationName" value="selectItemForDiscovery"/>
<c:set var="selectItemsOperationName" value="selectItemsForDiscovery"/>
<%@ include file="/WEB-INF/jsp/map/map.jspf" %>

