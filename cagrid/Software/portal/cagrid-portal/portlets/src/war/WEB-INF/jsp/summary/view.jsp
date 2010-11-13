<%@ include file="/WEB-INF/jsp/include/includes.jspf" %>
<%@ include file="/WEB-INF/jsp/include/liferay-includes.jspf" %>

<c:set var="ns"><portlet:namespace/></c:set>
<script type="text/javascript" src="<c:url value="/js/yui/yahoo-dom-event/yahoo-dom-event.js"/>"></script>
<script type="text/javascript" src="<c:url value="/js/yui/element/element-min.js"/>"></script>
<script type="text/javascript" src="<c:url value="/js/yui/datasource/datasource-min.js"/>"></script>
<script type="text/javascript" src="<c:url value="/js/yui/json/json-min.js"/>"></script>
<script type="text/javascript" src="<c:url value="/js/yui/connection/connection-min.js"/>"></script>
<script src="<c:url value="/js/browse-catalog.js"/>"></script>

<liferay-portlet:renderURL var="dataSetLnk" portletName="BrowsePortlet_WAR_cagridportlets"
                           portletMode="view">
    <liferay-portlet:param name="operation" value="view"/>
    <liferay-portlet:param name="aof" value="AOFVALUE"/>
</liferay-portlet:renderURL>


<script type="text/javascript">
    //maximum number of items to display
    var MAX_ITEMS = 7;
    
    // Get Area of Focus tree for Data Set catalog type
    var ${ns}solrDatasource = new YAHOO.util.XHRDataSource("<c:out value="${solrUrl}"/>/select?", {responseType:YAHOO.util.XHRDataSource.JSON});
    var ${ns}wildcard = "*:*";
    var ${ns}query = new solrQuery(${ns}wildcard);
    ${ns}query.setTree(true);
    ${ns}query.addFacet("catalog_type", "information_model");

    function ${ns}navigateToDataSet(aof) {
        var searchLink = "${dataSetLnk}";
        searchLink = searchLink.replace("/guest/home", "/guest/catalog/datasets");
        searchLink = searchLink.replace("AOFVALUE", aof);

        window.location = searchLink;
    }

    jQuery(document).ready(function() {
        ${ns}solrDatasource.sendRequest(${ns}query.getQuery(), {
            success : ${ns}updateCategories,
            cache:false,
            failure : ${ns}handlefailure,
            scope : this
        });

    });

    var ${ns}updateCategories = function (oRequest, oParsedResponse, oPayload) {
        try {
            jQuery("#${ns}categories").html("");
            var solrJSON = YAHOO.lang.JSON.parse(oParsedResponse.results.responseText);
            var aofTree = solrJSON.tree;
            if (aofTree.length > 1) {
                var aofs = aofTree[1].nodes;
                var counter = aofs.length>MAX_ITEMS?MAX_ITEMS:aofs.length;

                for (var i = 0; i < counter; i++) {
                    var aof = aofs[i];

                    var resultDiv = document.createElement('div');
                    var aofLnk = document.createElement('a');
                    aofLnk.setAttribute('href', 'javascript:${ns}navigateToDataSet("' + aof.name + '")');
                    aofLnk.innerHTML = aof.name;
                    resultDiv.appendChild(aofLnk);
                    jQuery("#${ns}categories").append(resultDiv);
                }
            }
        }
        catch (x) {
        <%--do nothing--%>

        }
    };

    var ${ns}handlefailure = function (oRequest, oParsedResponse, oPayload) {
         jQuery("#${ns}categories").html("");
    };

</script>

<script>
    jQuery(document).ready(function() {
        dwr.engine.beginBatch({timeout:30000});

        MapService.getSummary(function(summary) {
            $('data-services-summary').innerHTML = summary.dataServices;
            $('analytical-services-summary').innerHTML = summary.analyticalServices;
            $('participant-institute-summary').innerHTML = summary.participants;
            $('class-count-stats-summary').innerHTML = summary.classCountStats;
        });

        dwr.engine.endBatch({
            async:true
        });

    });
</script>

<div id="dataSetsContent">
<%--
    <div id="summaryTitle">
     Data Set Categories
        <tags:helpLink helpURL="${usersGuideUrl}-DataSetCategories"/>
    </div>
 --%>

    <div id="summaryTitle">
    	Grid Statistics <tags:helpLink helpURL="${usersGuideUrl}-GridStatistics"/>
    </div>
     
    <div>
    	<div class="gss_section">caBIG Site Statistics:</div>
    	<div class="gss_line"><span>Data Services:  </span><span id="data-services-summary">data services count</span></div>
    	<div class="gss_line"><span>Analytical Services:</span><span id="analytical-services-summary">analytical services count</span></div>
    	<div class="gss_line"><span>Participant Institutes:</span><span id="participant-institute-summary">participant institutes count</span></div>
    	
    	

		<span id="class-count-stats-summary"> counts </span>
	</div>
	
    <%--
    <div id="${ns}categories" class="row">
        <tags:image name="loading_animation.gif" cssStyle="padding:40px;"/>
    </div>
    <br/>
    <a href="/web/guest/catalog/datasets">More...</a>
    --%>
</div>


