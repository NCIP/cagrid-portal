<%@ include file="/WEB-INF/jsp/include.jsp"%>
<%@ taglib prefix="m" tagdir="/WEB-INF/tags"%>

<script type='text/javascript'
	src='<c:url value="/dwr/interface/ServiceDetailsTreeFacade.js"/>'></script>
<script type='text/javascript' src='<c:url value="/dwr/engine.js"/>'></script>
<script type='text/javascript' src='<c:url value="/dwr/util.js"/>'></script>

<script type="text/javascript">
	//<![CDATA[
    function <portlet:namespace/>toggleDiv(id){
    
    	var prefix = "<portlet:namespace/>";
    	var nodeId = prefix + id + "Node";
    	var divId = prefix + id + "Div";
    	
    	var node = document.getElementById(nodeId);
    	var div = document.getElementById(divId);

    	if(div.style.display == "none"){
    		var wasEmpty = jQuery.trim(DWRUtil.getValue(divId)).length == 0;
			ServiceDetailsTreeFacade.openNode(id, { prefix: prefix, render: wasEmpty },
			{
				callback:function(html){
					div.style.display = "";
					if(wasEmpty){				
						DWRUtil.setValue(divId, html, {escapeHtml:false});
					}
					node.className = "coll_node";
				},
				errorHandler:function(errorString, exception){
					alert("Error opening node: " + errorString);
				}
			});

    	}else{
    		ServiceDetailsTreeFacade.closeNode(id, { prefix: prefix },
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
.exp_node{
	list-style: circle url('<c:url value="/images/expandbtn2.gif"/>') inside;
	cursor: pointer;
	cursor: hand;	
}
.coll_node{
	list-style: circle url('<c:url value="/images/collapsebtn2.gif"/>') inside;
	cursor: pointer;
	cursor: hand;	
}
.leaf_node{
	list-style-image: none;
	list-style-type: circle;
	list-style-position: inside;
}
.node_content{
	margin-left: 16px;
}
-->
</style>
<p />
<portlet:actionURL var="action"/>

<form:form name="selectGridServiceForm" action="${action}">
	<c:if test="${empty gridServiceUrl}">Enter a </c:if>Grid Service URL:
	<input name="gridServiceUrl" type="text" value="<c:out value="${gridServiceUrl}"/>"/>
	<input type="submit" value="Show"/>
</form:form>


<c:choose>
	<c:when test="${!empty gridServiceUrl and empty rootNode}">
No grid service found for <c:out value="${gridServiceUrl}"/>
	</c:when>
	<c:when test="${!empty rootNode}">

<c:set var="prefix"><portlet:namespace/></c:set>
<c:set var="node" value="${rootNode}"/>
<%@ include file="/WEB-INF/jsp/discovery/serviceDetails_frag.jsp"%>
	
	</c:when>
	<c:otherwise>
No service is currently selected.	
	</c:otherwise>
</c:choose>

