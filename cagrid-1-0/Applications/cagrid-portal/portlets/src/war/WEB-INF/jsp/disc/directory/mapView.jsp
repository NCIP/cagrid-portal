<%@ include file="/WEB-INF/jsp/include.jsp" %>

<%@ include file="/WEB-INF/jsp/disc/tabs.jspf" %>

<portlet:actionURL var="action"/>

<table>
<tr>
<td>
<form:form action="${action}" commandName="mapCommand">
<%@ include file="/WEB-INF/jsp/disc/directory/directoriesSelect.jspf" %>
	<input type="hidden" name="operation" value="selectDirectoryMap"/>
</form:form>
</td>
<td>
<form:form action="${action}" commandName="mapCommand">
<%@ include file="/WEB-INF/jsp/disc/directory/searchResultsSelect.jspf" %>
	<input type="hidden" name="operation" value="selectResultsMap"/>
</form:form>
</td>
</tr>
</table>

<c:set var="selectItemOperationName" value="selectItem"/>
<c:set var="selectItemsOperationName" value="selectItems"/>

<c:set var="mapBean" value="${mapCommand}"/>

<%@ include file="/WEB-INF/jsp/map/map.jspf" %>
