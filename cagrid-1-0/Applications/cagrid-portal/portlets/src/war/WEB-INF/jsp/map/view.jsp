<%@ include file="/WEB-INF/jsp/include/includes.jspf" %>

<script type="text/javascript" src='<c:url value="/dwr/engine.js"/>'></script>
<script type="text/javascript" src='<c:url value="/dwr/interface/MapService.js"/>'></script>
<script src="<c:out value="${mapBean.baseUrl}" escapeXml="false"/><c:out value="${mapBean.apiKey}" escapeXml="false"/>"
        type="text/javascript"></script>

<style type="text/css">
    <!--
    <%@ include file="/css/map.css" %>
    -->
</style>

<c:set var="prefix"><portlet:namespace/></c:set>
<c:set var="mapNodeId">${prefix}-gmap</c:set>
<c:set var="selectItemOperationName" value="selectItemForDiscovery"/>
<c:set var="selectItemsOperationName" value="selectItemsForDiscovery"/>

<portlet:actionURL var="action"/>

<%@ include file="/WEB-INF/jsp/map/mapDirectory.jspf" %>

<div id="<c:out value="${mapNodeId}"/>" class="mapNode"><!-- for ie --></div>

<div style="display:none;">
    <div id="${prefix}loadingDiv" class="mapLoadingMsg">
        <%----%>
    </div>
</div>

<div id="${prefix}mapDiv" style="padding-bottom:10px;">
    <%----%>
</div>

<script type="text/javascript">

    //<![CDATA[
    var map;

    if (GBrowserIsCompatible()) {

        map= new GMap2(document.getElementById("${prefix}-gmap"));
        map.setCenter(
                new GLatLng(
                        <c:out value="${mapBean.centerLatitude}"/>,
                        <c:out value="${mapBean.centerLongitude}"/>
                        ), <c:out value="${mapBean.zoomLevel}"/>);

        map.enableDoubleClickZoom();
        map.addControl(new GSmallMapControl());

        function LoadingControl(){}
        LoadingControl.prototype = new GControl();

        LoadingControl.prototype.initialize = function(map) {
            var container = document.getElementById('${prefix}loadingDiv');
            map.getContainer().appendChild(container);
            return container;
        }

        LoadingControl.prototype.getDefaultPosition = function() {
            return new GControlPosition(G_ANCHOR_TOP_LEFT, new GSize(42, 5));
        }

        map.addControl(new LoadingControl());
        loadMap();
    }

    function loadMap(){
        if (GBrowserIsCompatible()) {
            map.clearOverlays();
            document.getElementById('${prefix}loadingDiv').innerHTML='Loading Map...';

            dwr.engine.beginBatch({timeout:30000});
            MapService.getMap($('${prefix}directory').value, function(result){
                document.getElementById('${prefix}loadingDiv').innerHTML='';
                $('${prefix}mapDiv').innerHTML=result;
                var x = $('${prefix}mapDiv').getElementsByTagName("script");
                for(var i=0;i<x.length;i++)
                {
                    eval(x[i].text);
                }
            });

            dwr.engine.endBatch({
                async:true,
                errorHandler:function(errorString, exception) {
                    $('${prefix}loadingDiv').innerHTML='Server Error. Please refresh the page'}
            });

            var bName = navigator.appName;
            var appVer = navigator.appVersion.toLowerCase();
            var iePos = appVer.indexOf('msie');
            if (iePos !=-1) {
                is_minor = parseFloat(appVer.substring(iePos+5,appVer.indexOf(';',iePos)))
                is_major = parseInt(is_minor);
            }

            if (bName == "Microsoft Internet Explorer" &&
                is_major <=6)
            {
                document.getElementById('${mapNodeId}').style.width="600px";
                document.getElementById('${mapNodeId}').style.height="350px";
            }
        }
    }

    var baseIcon = new GIcon();
    baseIcon.iconSize = new GSize(34, 24);
    baseIcon.shadowSize = new GSize(34, 24);
    baseIcon.iconAnchor = new GPoint(17, 12);
    baseIcon.infoWindowAnchor = new GPoint(9, 2);
    baseIcon.infoShadowAnchor = new GPoint(18, 25);
    baseIcon.shadow = "<c:url value="/images/shadow.png"/>";

    var aSvcUp = new GIcon(baseIcon);
    aSvcUp.image = "<c:url value="/images/analytical_services.gif"/>";
    var aSvcDown = new GIcon(baseIcon);
    aSvcDown.image = "<c:url value="/images/analytical_services-inactive.gif"/>";

    var dSvcUp = new GIcon(baseIcon);
    dSvcUp.image = "<c:url value="/images/data-services.gif"/>";
    var dSvcDown = new GIcon(baseIcon);
    dSvcDown.image = "<c:url value="/images/data-services-inactive.gif"/>";

    var ctrUp = new GIcon(baseIcon);
    ctrUp.image = "<c:url value="/images/hosting-research-center.gif"/>";
    var ctrPartial = new GIcon(baseIcon);
    ctrPartial.image = "<c:url value="/images/hosting-research-center-partial.gif"/>";
    var ctrDown = new GIcon(baseIcon);
    ctrDown.image = "<c:url value="/images/hosting-research-center-inactive.gif"/>";

    var partCtr = new GIcon(baseIcon);
    partCtr.image = "<c:url value="/images/participant_institute.gif"/>";

    var partPoc = new GIcon(baseIcon);
    partPoc.image = "<c:url value="/images/participant_POC.gif"/>";

</script>

<c:set var="formName">${prefix}mapNodeForm</c:set>
<form:form id="${formName}" action="${action}">
    <input type="hidden" name="operation"/>
    <input type="hidden" name="selectedId"/>
    <input type="hidden" name="type"/>
    <input type="hidden" name="selectedIds"/>
</form:form>


<script type="text/javascript">
    function selectItemForDiscovery(id,type){
        $('${prefix}mapNodeForm').operation.value = "${selectItemOperationName}";
        $('${prefix}mapNodeForm').selectedId.value = id;
        $('${prefix}mapNodeForm').type.value = type;
        $('${prefix}mapNodeForm').submit();
    }

    function selectItemsForDiscovery(ids,type){
        $('${prefix}mapNodeForm').operation.value = "${selectItemsOperationName}";
        $('${prefix}mapNodeForm').selectedIds.value = ids;
        $('${prefix}mapNodeForm').type.value = type;
        $('${prefix}mapNodeForm').submit();
    }

</script>

