<%@ include file="/WEB-INF/jsp/include/includes.jspf" %>
<%@ include file="/WEB-INF/jsp/include/liferay-includes.jspf" %>

<c:set var="ns"><portlet:namespace/></c:set>
<link rel="stylesheet" type="text/css" href="<c:url value="/css/catalog.css"/>"/>
<script type="text/javascript" src="<c:url value="/js/yui/yahoo-dom-event/yahoo-dom-event.js"/>"></script>
<script type="text/javascript" src="<c:url value="/js/yui/element/element-min.js"/>"></script>
<script type="text/javascript" src="<c:url value="/js/yui/datasource/datasource-min.js"/>"></script>
<script type="text/javascript" src="<c:url value="/js/yui/json/json-min.js"/>"></script>
<script type="text/javascript" src="<c:url value="/js/yui/connection/connection-min.js"/>"></script>
<script type="text/javascript" src="<c:url value="/js/yui/get/get-min.js"/>"></script>
<script src="<c:url value="/js/browse-catalog.js"/>"></script>

<liferay-portlet:renderURL var="dataSetLnk" portletName="BrowsePortlet_WAR_cagridportlets"
                           portletMode="view">
    <liferay-portlet:param name="operation" value="viewDetails"/>
    <portlet:param name="entryId" value="CATALOGID"/>
</liferay-portlet:renderURL>


<script type="text/javascript">
    var ${ns}solrDatasource = new YAHOO.util.XHRDataSource("<c:out value="${solrServiceUrl}"/>/select?&version=2.2&sort=updatedAt+desc&", {responseType:YAHOO.util.XHRDataSource.JSON});
    var ${ns}wildcard = "*:*";
    var ${ns}query = new solrQuery(${ns}wildcard);
    ${ns}query.setRows(7);

    function ${ns}navigateToCatalog(id) {
        var searchLink = "${dataSetLnk}";
        searchLink = searchLink.replace("/guest/home", "/guest/catalog/all");
        searchLink = searchLink.replace("CATALOGID", id);
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
            var catalogs = solrJSON.response.docs;

            for (var i = 0; i < catalogs.length; i++) {
                var catalog = catalogs[i];
                var name = catalog.name;
                if (name.length > 30) {
                    name = name.substring(0, 30);
                }

                var resultDiv = document.createElement('div');

                var thumbImgDiv = document.createElement('span');
                thumbImgDiv.setAttribute('style','padding-right:5px');
                     var thumbImg = document.createElement('img');
                thumbImg.setAttribute('src', '/cagridportlets/images/catalog_icons/'+catalog.catalog_type+'.png');
                thumbImg.setAttribute('align','middle');
                thumbImgDiv.appendChild(thumbImg);
                resultDiv.appendChild(thumbImgDiv);

                var link = document.createElement('a');
                link.setAttribute('href', 'javascript:${ns}navigateToCatalog("' + catalog.id + '")');
                link.innerHTML = name;
                resultDiv.appendChild(link);
                jQuery("#${ns}categories").append(resultDiv);
            }
        }
        catch (x) {
        <%--do nothing--%>

        }
    };

    var ${ns}handlefailure = function (oRequest, oParsedResponse, oPayload) {
        jQuery("#${ns}categories").append("Failed to get results");
    };

</script>

<div id="summaryContent">
    <div id="summaryTitle">
             Latest Content
        <tags:helpLink  helpURL="${userGuideUrl}-LatestEntries"/>
    </div>

    <div id="${ns}categories" class="row">
        <tags:image name="loading_animation.gif" cssStyle="padding:40px;"/>
    </div>
    <br/>
    <a href="/web/guest/catalog/all">More...</a>
</div>


