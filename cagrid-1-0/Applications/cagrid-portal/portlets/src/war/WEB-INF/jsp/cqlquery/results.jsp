<%@ include file="/WEB-INF/jsp/include.jsp"%>

<%@ include file="/WEB-INF/jsp/include/table_styles.jsp" %>

<style type="text/css">
<!--
<%@ include file="/css/xmlverbatim.css" %>
-->
</style>

<script type="text/javascript">
	//<![CDATA[
	
	jQuery(document).ready(function(){
		jQuery("#<portlet:namespace/>cqlQueryResults").tabs();
	});
	
	// ]]>
</script>

<c:if test="${!empty resultsCommand.error}">
<span style="color:red">${resultsCommand.error}</span>
</c:if>
<c:choose>
<c:when test="${empty resultsCommand.instance.result}">
No results to display.
</c:when>
<c:otherwise>
<c:set var="scroller" value="${resultsCommand.tableScroller}"/>
<c:choose>
	<c:when test="${fn:length(scroller.page) == 0}">
		Results are empty.
	</c:when>
	<c:otherwise>
	
		<div id="<portlet:namespace/>cqlQueryResults">
		
			<ul class="tabs">
                <li><a href="#<portlet:namespace/>resultsTable"><span>Tablular</span></a></li>
                <li><a href="#<portlet:namespace/>resultsXml"><span>XML</span></a></li>
            </ul>
            <div id="<portlet:namespace/>resultsTable">
	
		<span class="scrollerStyle2">
			Displaying <c:out value="${scroller.index + 1}"/> to <c:out value="${scroller.endIndex}"/></span> of <c:out value="${fn:length(scroller.objects)}"/> results.
		
		<br/>		
		<c:set var="scroller" value="${scroller}"/>
		<%@ include file="/WEB-INF/jsp/include/scroll_controls.jsp" %>

		<%@ include file="/WEB-INF/jsp/cqlquery/cqlresults_table.jsp" %>
		
		<%@ include file="/WEB-INF/jsp/include/scroll_controls.jsp" %>
		
            </div>		
 			<div id="<portlet:namespace/>resultsXml">
 				<div id="<portlet:namespace/>resultsXmlInner">        
<c:out value="${resultsCommand.prettyXml}" escapeXml="false"/>
				</div>
			</div>
		</div>
		
	</c:otherwise>
</c:choose>


</c:otherwise>
</c:choose>