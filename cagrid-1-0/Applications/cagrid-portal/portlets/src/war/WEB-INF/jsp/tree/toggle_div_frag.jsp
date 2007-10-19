    function <portlet:namespace/>toggleDiv(id){
    
    	var prefix = "<portlet:namespace/>";
    	var nodeId = prefix + id + "Node";
    	var divId = prefix + id + "Div";
    	
    	var node = document.getElementById(nodeId);
    	var div = document.getElementById(divId);

    	if(div.style.display == "none"){
    		var wasEmpty = jQuery.trim(DWRUtil.getValue(divId)).length == 0;
			<c:out value="${treeFacadeName}"/>.openNode(id, 
			{ 
				prefix: prefix, 
			 	render: wasEmpty, 
			 	path: id 
			},
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