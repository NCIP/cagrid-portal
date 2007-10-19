<%@ include file="/WEB-INF/jsp/include.jsp"%>
<%@ taglib prefix="m" tagdir="/WEB-INF/tags"%>

<script type='text/javascript'
	src='<c:url value="/dwr/interface/CQLQueryTreeFacade.js"/>'></script>
<script type='text/javascript' src='<c:url value="/dwr/engine.js"/>'></script>
<script type='text/javascript' src='<c:url value="/dwr/util.js"/>'></script>

<script type="text/javascript">
	//<![CDATA[
<c:set var="treeFacadeName" value="CQLQueryTreeFacade"/>
<%@ include file="/WEB-INF/jsp/tree/toggle_div_frag.jsp"%>
    // ]]>
</script>

<style type="text/css">
<!--
<%@ include file="/WEB-INF/jsp/tree/tree_node_styles_frag.jsp"%>
-->
</style>
<p />
<portlet:actionURL var="action"/>

<c:choose>
	<c:when test="${!empty rootNode}">

<c:set var="prefix"><portlet:namespace/></c:set>
<c:set var="node" value="${rootNode}"/>
<%@ include file="/WEB-INF/jsp/cqlquery/cqlQuery_frag.jsp"%>
	
	</c:when>
	<c:otherwise>
No UMLClass is currently selected.
	</c:otherwise>
</c:choose>

