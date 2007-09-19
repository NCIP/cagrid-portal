<%@ include file="/WEB-INF/jsp/include.jsp" %>

<portlet:actionURL var="action"/>

<form:form action="${action}" commandName="mapBean">
	<form:select onchange="submit()" path="category">
		<form:option value="all">All Services and Participants</form:option>
		<form:option value="services">All Services</form:option>
		<form:option value="dataServices">--Data Services</form:option>
		<form:option value="analyticalServices">--Analytical Services</form:option>
		<form:option value="participants">All Participants</form:option>
		<form:option value="CTMS">--Clinical Trials Management Systems</form:option>
		<form:option value="ICR">--Integrative Cancer Research</form:option>
		<form:option value="IMAGING">--In Vivo Imaging</form:option>
		<form:option value="TBPT">--Tissue Banks and Pathology Tools</form:option>
		<form:option value="ARCH">--Architecture</form:option>
		<form:option value="VCDE">--Vocabulary and Common Data Elements</form:option>
		<form:option value="DSIC">--Data Sharing and Intellectual Capitol</form:option>
		<form:option value="TRAINING">--Documentation and Training</form:option>
		<form:option value="SP">--Strategic Planning</form:option>
	</form:select>	
</form:form>

<script
	src="<c:out value="${mapBean.baseUrl}"/><c:out value="${mapBean.apiKey}"/>"
	type="text/javascript"></script>

<c:set var="mapNodeId"><portlet:namespace />-gmap</c:set>
<style type="text/css">
<!--
#<c:out value="${mapNodeId}"/> {
    border: 4px solid #5C5C5C;
    margin-left: auto;
    margin-right: auto;
    width: 700px;
    height: 400px;
}
-->
</style>
<div id="<c:out value="${mapNodeId}"/>"></div>

<script type="text/javascript">
    //<![CDATA[

	var baseIcon = new GIcon();
	baseIcon.iconSize = new GSize(32, 32);
	baseIcon.shadowSize = new GSize(59, 32);
	baseIcon.iconAnchor = new GPoint(9, 34);
	baseIcon.infoWindowAnchor = new GPoint(9, 2);
	baseIcon.infoShadowAnchor = new GPoint(18, 25);
	
    var pinIcon = new GIcon(baseIcon);
	pinIcon.shadow = "<c:url value="/images/pushpin_shadow.png"/>";
	var gPinIcon = new GIcon(pinIcon);
	gPinIcon.image = "<c:url value="/images/grn-pushpin.png"/>";
	var rPinIcon = new GIcon(pinIcon);
	rPinIcon.image = "<c:url value="/images/red-pushpin.png"/>";
	
    var manIcon = new GIcon(baseIcon);
    manIcon.image = "<c:url value="/images/man.png"/>";
	manIcon.shadow = "<c:url value="/images/man.shadow.png"/>";
	
    function load() {
      if (GBrowserIsCompatible()) {
        var map = new GMap2(document.getElementById("<c:out value="${mapNodeId}"/>"));
        map.setCenter(
        	new GLatLng(
        			<c:out value="${mapBean.centerLatitude}"/>, 
        			<c:out value="${mapBean.centerLongitude}"/>
        		), <c:out value="${mapBean.zoomLevel}"/>);
        
        map.enableDoubleClickZoom();
        map.addControl(new GSmallMapControl());
        
        <c:if test="${!empty mapBean.serviceNodes}">
        	<c:forEach 
        		items="${mapBean.serviceNodes}" 
        		var="svcNode" 
        		varStatus="svcNodeStatus">
        		
        		<c:set var="markerId">svcMarker<c:out value="${svcNodeStatus.index}"/></c:set>
	        
	        	var <c:out value="${markerId}"/> = 
	        		new GMarker(
	        			new GLatLng(
	        				<c:out value="${svcNode.latitude}"/>, 
	        				<c:out value="${svcNode.longitude}"/>),
	        				<c:choose>
	        					<c:when test="${svcNode.active}">
	        						{icon:gPinIcon}
	        					</c:when>
	        					<c:otherwise>
	        						{icon:rPinIcon}
	        					</c:otherwise>
	        				</c:choose>
	        				);
	        	GEvent.addListener(
	        		<c:out value="${markerId}"/>, 
	        		"click", 
	        		function() {
						<c:out value="${markerId}"/>
							.openInfoWindowHtml(
								"<div style=\"font-size: 0.9em; width: 200px; height: 75px; overflow: auto;\">" + 
								
								<c:forEach items="${svcNode.serviceInfos}" var="svcInfo">
								
									"<a href=\"<portlet:actionURL><portlet:param name="action" value="selectService"/><portlet:param name="sgs_url"><jsp:attribute name="value"><c:out value="${svcInfo.url}"/></jsp:attribute></portlet:param></portlet:actionURL>\" " +
                      			   	"alt=\"View Service Details\"><c:out value="${svcInfo.name}"/></a><br/>" +
									"Hosting Center: <c:out value="${svcInfo.center}"/><br/>" +
									"Status: <c:out value="${svcInfo.status}"/><br/><hr/>" +
									
								</c:forEach>
								
								"</div>");
					}
				);
				map.addOverlay(<c:out value="${markerId}"/>);
        	</c:forEach>
        </c:if>
        
        
		<c:if test="${!empty mapBean.participantNodes}">
        	<c:forEach 
        		items="${mapBean.participantNodes}" 
        		var="pNode" 
        		varStatus="pNodeStatus">
        		
        		<c:set var="markerId">pMarker<c:out value="${pNodeStatus.index}"/></c:set>
	        
	        	var <c:out value="${markerId}"/> = 
	        		new GMarker(
	        			new GLatLng(
	        				<c:out value="${pNode.latitude}"/>, 
	        				<c:out value="${pNode.longitude}"/>),
	        				{icon:manIcon}
	        				);
	        	GEvent.addListener(
	        		<c:out value="${markerId}"/>, 
	        		"click", 
	        		function() {
						<c:out value="${markerId}"/>
							.openInfoWindowHtml(
								"<div style=\"font-size: 0.9em; width: 200px; height: 75px; overflow: auto;\">" + 
								
								<c:forEach items="${pNode.participantInfos}" var="pInfo">
								
									"<b><c:out value="${pInfo.name}"/></b><br/>" +
									"Workspaces: <c:out value="${pInfo.workspaces}"/><br/>" +
									"Homepage: <a target=\"_blank\" href=\"<c:out value="${pInfo.homepageUrl}"/>\"><c:out value="${pInfo.homepageUrl}"/></a><br/>" +
									"Status: <c:out value="${pInfo.status}"/><br/><hr/>" +
									
								</c:forEach>
								
								"</div>");
					}
				);
				map.addOverlay(<c:out value="${markerId}"/>);
        	</c:forEach>
        </c:if>        
      }
    }
	load();
    //]]>
</script>