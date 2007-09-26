<%@ include file="/WEB-INF/jsp/include.jsp" %>

<script
	src="/js/script.js"
	type="text/javascript"></script>
	
<style type="text/css">
<!--
.contentInnerTable {
    width: 90%;
    height: 100%;
    vertical-align: top;
    padding: 0.1em;
    border: 1px solid #CCCCCC; /* constant: medium gray */
}
.contentTableHeader {
/* for the horizontal column headers */
    font-family: arial, helvetica, verdana, sans-serif; /*background-color: #CCCCCC; /* constant: medium gray */
    font-size: 0.9em;
    background-color: #797C9C; /* constant: light purple */
    color: #FFFFFF; /* constant: black */
    font-weight: bold;
    padding: 0.1cm;
    border-right: 1px solid #CCCCCC; /* constant: dark gray */
    border-bottom: 1px solid #CCCCCC; /* constant: dark gray */
    border-top: 1px solid #CCCCCC; /* constant: dark gray */
    border-left: 1px solid #CCCCCC; /* constant: dark gray */
    text-align: left;
}
.dataRowLight {
/* for the light color of alternating rows */
    background-color: #EEEEE0; /* constant: blue */
    color: #000000; /* constant: black */
}

.dataRowDark {
/* for the dark color of alternating rows */
    background-color: #D3D3D3; /* constant: dark blue */
    color: #000000; /* constant: black */
}

.dataCellText {
/* for text output cells */
    font-size: 0.9em;
    padding: 0em;
    line-height: 1.5em;
    vertical-align: middle;
}

.dataCellTextBold {
/* for text output cells */
    font-size: 0.8em;
    padding: 0em;
    padding-left: 0.1cm;
    line-height: 1.5em;
    vertical-align: middle;
    font-weight: bold;
    width: 25%;
}
.txtHighlight {
/* adds emphasis to text */
    color: #797C9C;
    font-weight: bold;
}

.txtHighlightOn {
/* adds emphasis to text */
    color: #9FA2BA;
    font-weight: bold;
}
.leftColumn{
	width: 25%;
}
-->
</style>	

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
		<%@ include file="/WEB-INF/jsp/directory/scroll_controls.jsp" %>
		
		<c:choose>
			<c:when test="${directoryBean.type == 'services'}">
				<%@ include file="/WEB-INF/jsp/directory/services.jsp" %>
			</c:when>
			<c:otherwise>
				<%@ include file="/WEB-INF/jsp/directory/participants.jsp" %>
			</c:otherwise>
		</c:choose>
		
		<%@ include file="/WEB-INF/jsp/directory/scroll_controls.jsp" %>
		
	</c:otherwise>
</c:choose>