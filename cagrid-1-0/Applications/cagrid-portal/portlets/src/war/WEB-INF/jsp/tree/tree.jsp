<%@ include file="/WEB-INF/jsp/include.jsp"%>
<%@ taglib prefix="m" tagdir="/WEB-INF/tags"%>

<script type='text/javascript'
	src='<c:url value="/dwr/interface/TreeFacade.js"/>'></script>
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
			TreeFacade.openNode(id, { prefix: prefix },
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
    		TreeFacade.closeNode(id, { prefix: prefix },
			{
				callback:function(html){
					div.style.display = "none";
					node.className = "exp_node";					
				},
				errorHandler:function(errorString, exception){
					alert("Error closing node: " + errorString);
				}
			});     	
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

<c:set var="prefix"><portlet:namespace/></c:set>
<m:tree node="${rootNode}" prefix="${prefix}">
	<jsp:attribute name="contentFragment">
		<b>Content of <c:out value="${currNode.name}" />:</b><c:out value="${currNode.content.text}" /><br />

		

	</jsp:attribute>
	<jsp:attribute name="nodeFragment">
		Node: <c:out value="${currChildNode.name}" />
	</jsp:attribute>
</m:tree>

