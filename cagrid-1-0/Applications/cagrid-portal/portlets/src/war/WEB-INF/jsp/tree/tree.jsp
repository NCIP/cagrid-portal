<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>
<%@ taglib prefix="m" tagdir="/WEB-INF/tags"%>


<html>
<head>
<title>Tree</title>
</head>
<body>

<script type='text/javascript'
	src='<c:url value="/dwr/interface/TreeFacade.js"/>'></script>
<script type='text/javascript' src='<c:url value="/dwr/engine.js"/>'></script>
<script type='text/javascript' src='<c:url value="/dwr/util.js"/>'></script>

<script type="text/javascript">
	//<![CDATA[
    function toggleDiv(id){
    
    	var nodeId = id + "Node";
    	var divId = id + "Div";
    	
    	var node = document.getElementById(nodeId);
    	var div = document.getElementById(divId);

    	if(div.style.display == "none"){
    	
    		alert("Opening: '" + divId + "'");
    	
			TreeFacade.openNode(id,
			{
				callback:function(html){
					div.style.display = "";				
					DWRUtil.setValue(divId, html, {escapeHtml:false});
					node.className = "coll_node";
				},
				errorHandler:function(errorString, exception){
					alert(errorString);
				}
			});

    	}else{
    		alert("Closing: '" + divId + "'");
    	
    		TreeFacade.closeNode(id,
			{
				callback:function(html){
					div.style.display = "none";
					node.className = "exp_node";					
				},
				errorHandler:function(errorString, exception){
					alert(errorString);
				}
			});     	
    	}
    }
    

    // ]]>
</script>

<style type="text/css">
<!--
.exp_node{
	list-style-image: url(<c:url value="/images/expandbtn2.gif"/>);
}
.coll_node{
	list-style-image: url(<c:url value="/images/collapsebtn2.gif"/>);
}
.leaf_node{
	list-style-image: none;
	list-style-type: circle;
}
-->
</style>
<p />

<m:tree node="${rootNode}"/>

</body>
</html>
