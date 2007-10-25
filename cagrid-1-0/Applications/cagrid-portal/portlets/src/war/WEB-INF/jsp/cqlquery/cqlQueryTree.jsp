<%@ include file="/WEB-INF/jsp/include.jsp"%>
<%@ taglib prefix="m" tagdir="/WEB-INF/tags"%>

<script type='text/javascript'
	src='<c:url value="/dwr/interface/CQLQueryTreeFacade.js"/>'></script>
<script type='text/javascript' src='<c:url value="/dwr/engine.js"/>'></script>
<script type='text/javascript' src='<c:url value="/dwr/util.js"/>'></script>

<portlet:renderURL var="editCriterionUrl">
	<portlet:param name="operation" value="editCriterion"/>
	<portlet:param name="path" value="ATT_PATH"/>
</portlet:renderURL>

<script type="text/javascript">
	//<![CDATA[
<c:set var="treeFacadeName" value="CQLQueryTreeFacade"/>
    function <portlet:namespace/>toggleDiv(id){
    
    	var prefix = "<portlet:namespace/>";
    	var nodeId = prefix + id + "Node";
    	var divId = prefix + id + "Div";
    	
    	var node = document.getElementById(nodeId);
    	var div = document.getElementById(divId);

    	if(div.style.display == "none"){
    		
			<c:out value="${treeFacadeName}"/>.openNode(id, 
			{ 
				prefix: prefix, 
			 	path: id,
			 	editCriterionUrl: '<c:out value="${editCriterionUrl}" escapeXml="false"/>' 
			},
			{
				callback:function(html){
					div.style.display = "";
					DWRUtil.setValue(divId, html, {escapeHtml:false});
					node.className = "coll_node";
				},
				errorHandler:function(errorString, exception){
					alert("Error opening node: " + errorString);
				}
			});

    	}else{
    		<c:out value="${treeFacadeName}"/>.closeNode(id, { prefix: prefix },
			{
				errorHandler:function(errorString, exception){
					alert("Error closing node: " + errorString);
				}
			});
			div.style.display = "none";
			node.className = "exp_node";
    	}
    }
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
