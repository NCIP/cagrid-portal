<%@ include file="/WEB-INF/jsp/include.jsp" %>

<script
	src="/js/script.js"
	type="text/javascript"></script>
	
<%@ include file="/WEB-INF/jsp/include/table_styles.jsp" %>

<portlet:actionURL var="action">
	<portlet:param name="action" value="change"/>
</portlet:actionURL>

<form:form action="${action}" commandName="directoryBean">
	<form:select onchange="submit()" path="category">
		<option value="-">Select a category</option>
		<%@ include file="/WEB-INF/jsp/include/categories_options.jsp" %>
		
		<c:if test="${!empty searchHistory}">
			<optgroup label="Search History">
				<c:forEach var="result" items="${searchHistory}">
					<option value="search:<c:out value="${result.id}"/>">
						<c:out value="${result.type}"/>&nbsp;[<c:out value="${fn:length(result.objects)}"/>]
						<fmt:formatDate value="${result.time}" type="both"/>
					</option>
				</c:forEach>
			</optgroup>
		</c:if>
		
	</form:select>
</form:form>
<p/>

<c:choose>
	<c:when test="${empty directoryBean}">
		Select a directory.
	</c:when>
	<c:when test="${fn:length(directoryBean.scroller.page) == 0}">
		This directory is empty.
	</c:when>
	<c:otherwise>
	
		<span class="scrollerStyle2">
			Found <c:out value="${fn:length(directoryBean.scroller.objects)}"/> 
			<c:choose>
				<c:when test="${directoryBean.type == 'SERVICE'}">
					Services
				</c:when>
				<c:when test="${directoryBean.type == 'PARTICIPANT'}">
					Participants
				</c:when>
				<c:otherwise> Points of Contact</c:otherwise>
			</c:choose>
			Displaying <c:out value="${directoryBean.scroller.index + 1}"/> to <c:out value="${directoryBean.scroller.endIndex}"/></span>
		</p>
		
		<c:set var="scroller" value="${directoryBean.scroller}"/>
		<%@ include file="/WEB-INF/jsp/include/scroll_controls.jsp" %>
		
		<c:choose>
			<c:when test="${directoryBean.type == 'SERVICE'}">
				<%@ include file="/WEB-INF/jsp/directory/services.jsp" %>
			</c:when>
			<c:when test="${directoryBean.type == 'PARTICIPANT'}">
				<%@ include file="/WEB-INF/jsp/directory/participants.jsp" %>
			</c:when>
			<c:when test="${directoryBean.type == 'POC'}">
				<%@ include file="/WEB-INF/jsp/directory/pocs.jsp" %>
			</c:when>
			<c:otherwise>
				<span color="red">UNKNOWN TYPE '<c:out value="${directoryBean.type}"/>'</span>
			</c:otherwise>
		</c:choose>
		
		<%@ include file="/WEB-INF/jsp/include/scroll_controls.jsp" %>
		
	</c:otherwise>
</c:choose>