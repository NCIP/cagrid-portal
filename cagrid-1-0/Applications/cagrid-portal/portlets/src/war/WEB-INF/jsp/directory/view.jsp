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
	</form:select>
</form:form>
<p/>

<c:choose>
	<c:when test="${fn:length(directoryBean.scroller.page) == 0}">
		Nothing to display.
	</c:when>
	<c:otherwise>
	
		<span class="scrollerStyle2">
			Found <c:out value="${fn:length(directoryBean.scroller.objects)}"/> 
			<c:choose>
				<c:when test="${directoryBean.type == 'services'}">
					Services
				</c:when>
				<c:otherwise> Participants.</c:otherwise>
			</c:choose>
			Displaying <c:out value="${directoryBean.scroller.index + 1}"/> to <c:out value="${directoryBean.scroller.endIndex}"/></span>
		</p>
		
		<c:set var="scroller" value="${directoryBean.scroller}"/>
		<%@ include file="/WEB-INF/jsp/include/scroll_controls.jsp" %>
		
		<c:choose>
			<c:when test="${directoryBean.type == 'services'}">
				<%@ include file="/WEB-INF/jsp/directory/services.jsp" %>
			</c:when>
			<c:otherwise>
				<%@ include file="/WEB-INF/jsp/directory/participants.jsp" %>
			</c:otherwise>
		</c:choose>
		
		<%@ include file="/WEB-INF/jsp/include/scroll_controls.jsp" %>
		
	</c:otherwise>
</c:choose>