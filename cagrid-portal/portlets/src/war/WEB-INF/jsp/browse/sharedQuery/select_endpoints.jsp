<%@ include file="/WEB-INF/jsp/include/servlet_includes.jsp" %>

<link rel="stylesheet" type="text/css" href="http://yui.yahooapis.com/2.7.0/build/button/assets/skins/sam/button.css" />
<link rel="stylesheet" type="text/css" href="<c:url value="/js/yui/container/assets/skins/sam/container.css"/>" />

<script type="text/javascript" src="<c:url value="/js/yui/yahoo-dom-event/yahoo-dom-event.js"/>"></script>
<script type="text/javascript" src="<c:url value="/js/yui/datasource/datasource-min.js"/>"></script>
<script type="text/javascript" src="<c:url value="/js/yui/element/element-min.js"/>"></script>
<script type="text/javascript" src="<c:url value="/js/yui/button/button-min.js"/>"></script>
<script type="text/javascript" src="<c:url value="/js/yui/logger/logger-min.js"/>"></script>

<c:set var="ns" value="${param.ns}"/>

<script type="text/javascript"
	src="<c:url value="/dwr/interface/SharedQueryCatalogEntryManagerFacade.js"/>"></script>
<script type="text/javascript" src="<c:url value="/dwr/engine.js"/>"></script>
<script type="text/javascript" src="<c:url value="/dwr/util.js"/>"></script>

<div align="left" style="overflow:auto; height:270px;padding-bottom:10px;" class="yui-skin-sam">


<h3>
Available Data Sources
</h3>
<p>
Select all data sources you would like to query.
</p>
<p>
<div id="${ns}selectEndpointsButtonContainer"></div>
</p>
</br>
<p>
<form name="${ns}selectEndpointsForm">

</form>
</p>
</div>

<script type="text/javascript">

function ${ns}renderAvailableEndpoints(){
	
	var cql = jQuery("#input-query").val();
	SharedQueryCatalogEntryManagerFacade.renderAvailableEndpointsFormContent(cql, "${ns}",
	{
		callback:function(html){
			jQuery("form[name='${ns}selectEndpointsForm']").html(html);
			jQuery("form[name='${ns}selectEndpointsForm'] :checkbox[name='endpoints']").bind("click", function(evt){
				${ns}checkEnableSelectEndpoints();
			});
		},
		errorHandler:function(errorString, exception){
			alert(errorString);
		}
	});
}

function ${ns}checkEnableSelectEndpoints(){
	if(jQuery().length > 0){
		${ns}selectEndpointsButton.set("disabled", false);
	}else{
		${ns}selectEndpointsButton.set("disabled", true);
	}
}


var ${ns}selectEndpointsButton = null;

jQuery(document).ready(function() {

	${ns}renderAvailableEndpoints();

	${ns}selectEndpointsButton = new YAHOO.widget.Button({
		label: "Select",
		id: "${ns}selectEndpointsButton",
		container: "${ns}selectEndpointsButtonContainer"
	});
	${ns}selectEndpointsButton.set("disabled", true);

	${ns}selectEndpointsButton.on("click", function(evt){
		var endpoints = new Array();
		var l = jQuery("form[name='${ns}selectEndpointsForm'] :input[name='endpoints']:checked");
		for(var i = 0; i < l.length; i++){
			endpoints.push({url:jQuery(l[i]).val(), name:jQuery(l[i]).next().text()});
		}
		${ns}selectEndpoints(endpoints);
	});
	
	
});

</script>