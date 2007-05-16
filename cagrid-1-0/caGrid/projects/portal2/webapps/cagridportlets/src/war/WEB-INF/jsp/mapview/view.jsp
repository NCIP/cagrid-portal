<%@ include file="/WEB-INF/jsp/include.jsp" %>

<script type="text/javascript"
src="http://api.maps.yahoo.com/ajaxymap?v=3.0&appid=<c:out value="${appid}"/>"></script>
<style type="text/css">
#mapContainer {
height: 450px;
width: 100%;
}
</style>

<div id="mapContainer"></div>

<script type="text/javascript">
var map = new YMap(document.getElementById('mapContainer'));
map.addTypeControl();
map.addPanControl();
map.addZoomLong();
map.setMapType(YAHOO_MAP_SAT);

<c:forEach items="${geoRSSUrls}" var="url">
map.addOverlay(new YGeoRSS('<c:out value="${url}"/>'));
</c:forEach>

</script>
