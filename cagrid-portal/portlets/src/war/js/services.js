function
<portlet:namespace/>selectUMLClass(umlClassId){
var form = document.
<portlet:namespace/>selectUMLClassForm;
form.selectedId.value = umlClassId;
form.submit();
}

function
<portlet:namespace/>diagnoseService(url) {
var form = document.
<portlet:namespace/>selectUMLClassForm;
form.operation.value = 'selectServiceForDiagnosis';
form.url.value = url;
form.submit();
}


function  ${prefix}toggleDiv(id) {

var prefix = "${prefix}";
var nodeId = prefix + id + "Node";
var divId = prefix + id + "Div";

var node = document.getElementById(nodeId);
var div = document.getElementById(divId);

if (div.style.display == "none") {
var wasEmpty = jQuery.trim(DWRUtil.getValue(divId)).length == 0;
ServiceDetailsTreeFacade.openNode(id,
{
prefix: prefix,
render: wasEmpty,
path: id,
selectXmlSchemaUrl: '
<c:out value="${selectXmlSchemaUrl}" escapeXml="false"/>'
},
{
callback:function(html) {
div.style.display = "";
if (wasEmpty) {
DWRUtil.setValue(divId, html, {escapeHtml:false});
}
node.className = "coll_node";
},
errorHandler:function(errorString, exception) {
alert("Error opening node: " + errorString);
}
});

} else {
ServiceDetailsTreeFacade.closeNode(id, { prefix: prefix },
{
errorHandler:function(errorString, exception) {
alert("Error closing node: " + errorString);
}
});
div.style.display = "none";
node.className = "exp_node";
}
}


function ${prefix}submitBanUnbanRequest(serviceId) {
ServiceStatusManager.banUnbanService(serviceId, function(status) {
$("serviceStatusSpan").innerHTML = status;
new Effect.Highlight('serviceStatusSpan');

var btnValue = $("banUnbanBtn").value;
if (btnValue == "Ban Service") {
$("banUnbanBtn").value = "Unban Service";
}
else {
$("banUnbanBtn").value = "Ban Service";
}
});
}

function ${prefix}reloadMetadata(serviceId) {
ServiceStatusManager.reloadMetadata(serviceId, function(sucess) {
$("${prefix}metaReload").innerHTML = "Reloading Metadata....";
});

}



