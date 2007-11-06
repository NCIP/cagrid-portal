<%@ include file="/WEB-INF/jsp/include.jsp" %>

<script
	src="/js/script.js"
	type="text/javascript"></script>
	
<%@ include file="/WEB-INF/jsp/disc/tabs.jspf" %>
<%@ include file="/WEB-INF/jsp/include/table_styles.jspf" %>

<portlet:actionURL var="action"/>

<table>
<tr>
<td>
<form:form action="${action}" commandName="listCommand">
<%@ include file="/WEB-INF/jsp/disc/directory/directoriesSelect.jspf" %>
	<input type="hidden" name="operation" value="selectDirectoryList"/>
</form:form>
</td>
<td>
<form:form action="${action}" commandName="listCommand">
<%@ include file="/WEB-INF/jsp/disc/directory/searchResultsSelect.jspf" %>
	<input type="hidden" name="operation" value="selectResultsList"/>
</form:form>
</td>
</tr>
</table>

<c:choose>
	<c:when test="${empty listCommand}">
		Select a directory.
	</c:when>
	<c:when test="${fn:length(listCommand.scroller.page) == 0}">
		This directory is empty.
	</c:when>
	<c:otherwise>
	
		<span class="scrollerStyle2">
			Found <c:out value="${fn:length(listCommand.scroller.objects)}"/> 
			<c:choose>
				<c:when test="${listCommand.type == 'SERVICE'}">
					Services
				</c:when>
				<c:when test="${listCommand.type == 'PARTICIPANT'}">
					Participants
				</c:when>
				<c:otherwise> Points of Contact</c:otherwise>
			</c:choose>
			Displaying <c:out value="${listCommand.scroller.index + 1}"/> to <c:out value="${listCommand.scroller.endIndex}"/></span>
		</p>
		<br/>
		<c:set var="scroller" value="${listCommand.scroller}"/>
		<c:set var="scrollOperation" value="scrollDiscoveryList"/>
		<%@ include file="/WEB-INF/jsp/include/scroll_controls.jspf" %>
		
		<c:choose>
			<c:when test="${listCommand.type == 'SERVICE'}">
				<%@ include file="/WEB-INF/jsp/disc/directory/services.jspf" %>
			</c:when>
			<c:when test="${listCommand.type == 'PARTICIPANT'}">
				<%@ include file="/WEB-INF/jsp/disc/directory/participants.jspf" %>
			</c:when>
			<c:when test="${listCommand.type == 'POC'}">
				<%@ include file="/WEB-INF/jsp/disc/directory/pocs.jspf" %>
			</c:when>
			<c:otherwise>
				<span color="red">UNKNOWN TYPE '<c:out value="${listCommand.type}"/>'</span>
			</c:otherwise>
		</c:choose>
		
		<%@ include file="/WEB-INF/jsp/include/scroll_controls.jspf" %>
		
	</c:otherwise>
</c:choose>