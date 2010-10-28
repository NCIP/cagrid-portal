<%@ include file="/WEB-INF/jsp/include/includes.jspf" %>
<%@ include file="/WEB-INF/jsp/include/liferay-includes.jspf" %>

<%@ taglib prefix="tags" tagdir="/WEB-INF/tags" %>

<script type="text/javascript" src='<c:url value="/dwr/engine.js"/>'></script>
<script type="text/javascript" src="<c:url value="/js/scriptaculous/prototype.js"/>"></script>

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

<liferay-portlet:renderURL var="viewDetailsLink" portletName="BrowsePortlet_WAR_cagridportlets"
                           portletMode="view">
    <liferay-portlet:param name="operation" value="viewDetails"/>
</liferay-portlet:renderURL>

<liferay-portlet:renderURL var="viewCatalogs" portletName="BrowsePortlet_WAR_cagridportlets"
                           portletMode="view">
    <liferay-portlet:param name="operation" value="view"/>
</liferay-portlet:renderURL>


<div class="mapContainer">

    <div id="<c:out value="${mapNodeId}"/>" class="mapNode"><!-- for ie --></div>
</div>

<div style="display:none;">
    <div id="${prefix}loadingDiv" class="mapLoadingMsg">
        <%----%>
    </div>
</div>

<div id="${prefix}mapDiv">
    <%----%>
</div>
<%@ include file="/WEB-INF/jsp/summary/view.jsp" %>
<%@ include file="/WEB-INF/jsp/map/mapDirectory.jspf" %>
<script type="text/javascript">

    //<![CDATA[
    var map;
    var baseIcon = new GIcon();
    baseIcon.iconAnchor = new GPoint(9, 28);
    baseIcon.infoWindowAnchor = new GPoint(9, 28);
    baseIcon.infoShadowAnchor = new GPoint(0, 0);
    baseIcon.shadow = "<c:url value="/images/shadow.png"/>";

    var aSvcUp = new GIcon(baseIcon);
    aSvcUp.image = "<c:url value="/images/analytical_services.png"/>";
    var aSvcDown = new GIcon(baseIcon);
    aSvcDown.image = "<c:url value="/images/analytical_services-inactive.png"/>";

    var dSvcUp = new GIcon(baseIcon);
    dSvcUp.image = "<c:url value="/images/data-services.png"/>";
    var dSvcDown = new GIcon(baseIcon);
    dSvcDown.image = "<c:url value="/images/data-services-inactive.png"/>";

    var ctrUp = new GIcon(baseIcon);
    ctrUp.image = "<c:url value="/images/hosting-research-center.png"/>";
    var ctrPartial = new GIcon(baseIcon);
    ctrPartial.image = "<c:url value="/images/hosting-research-center-partial.gif"/>";
    var ctrDown = new GIcon(baseIcon);
    ctrDown.image = "<c:url value="/images/hosting-research-center-inactive.png"/>";

    var partCtr = new GIcon(baseIcon);
    partCtr.image = "<c:url value="/images/participant_institute.png"/>";


    jQuery(document).ready(function() {

        if (GBrowserIsCompatible()) {

            map = new GMap2(document.getElementById("${prefix}-gmap"));
            map.setCenter(
                    new GLatLng(
                            <c:out value="${mapBean.centerLatitude}"/>,
                            <c:out value="${mapBean.centerLongitude}"/>
                            ), <c:out value="${mapBean.zoomLevel}"/>);

            map.enableDoubleClickZoom();
            map.addControl(new GSmallMapControl());

            function LoadingControl() {
            }

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
    });

    function loadMap() {
        map.clearOverlays();
        document.getElementById('${prefix}loadingDiv').innerHTML = 'Loading Map...';

        dwr.engine.beginBatch({timeout:90000});
        MapService.getMap($('${prefix}directory').value, function(result) {
            document.getElementById('${prefix}loadingDiv').innerHTML = '';
            var temp = result;

            while (true) {
                var sindex = temp.indexOf("<script" + ">");
                if (sindex < 0) break;
                var eindex = temp.indexOf("</" + "script>", sindex);
                var js = temp.substring(sindex + 8, eindex);
                eval(js);
                temp = temp.substring(eindex + 9);
            }


        });

        dwr.engine.endBatch({
            async:true,
            errorHandler:function(errorString, exception) {
                $('${prefix}loadingDiv').innerHTML = 'Server Error. Please refresh the page'
            }
        });

        var bName = navigator.appName;
        var appVer = navigator.appVersion.toLowerCase();
        var iePos = appVer.indexOf('msie');
        if (iePos != -1) {
            is_minor = parseFloat(appVer.substring(iePos + 5, appVer.indexOf(';', iePos)))
            is_major = parseInt(is_minor);
        }

        if (bName == "Microsoft Internet Explorer" &&
            is_major <= 6)
        {
            document.getElementById('${mapNodeId}').style.width = "600px";
            document.getElementById('${mapNodeId}').style.height = "350px";
        }
    }


</script>

<c:set var="formName">${prefix}mapNodeForm</c:set>
<form:form id="${formName}">
    <input type="hidden" alt="Hidden" name="entryId"/>
    <input type="hidden" alt="Hidden" name="selectedIds"/>
</form:form>


<script type="text/javascript">
    function selectItemForDiscovery(id, type) {
        $('${prefix}mapNodeForm').entryId.value = id;
        var viewDetailsLink = "${viewDetailsLink}";
        viewDetailsLink = viewDetailsLink.replace("/guest/home", "/guest/catalog/all");
        $("${prefix}mapNodeForm").action = viewDetailsLink;
        $('${prefix}mapNodeForm').submit();
    }

    function selectItemsForDiscovery(ids, type) {
        ids = ids.replace(/,/g, " ");
        $('${prefix}mapNodeForm').selectedIds.value = ids;
        var viewCatalogsLink = "${viewCatalogs}";
        viewCatalogsLink = viewCatalogsLink.replace("/guest/home", "/guest/catalog/all");
        $("${prefix}mapNodeForm").action = viewCatalogsLink;
        $('${prefix}mapNodeForm').submit();
    }

</script>

<liferay-portlet:renderURL var="refreshStatsUrl">
    <liferay-portlet:param name="operation" value="refreshStats"/>
</liferay-portlet:renderURL>

<% if (request.isUserInRole("Administrator")) {  %>
	<a href="${refreshStatsUrl}">Refresh Stats</a>
<% } %>


