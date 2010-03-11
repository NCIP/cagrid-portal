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
    <liferay-portlet:param name="operation" value="viewDetails"/>
    <portlet:param name="entryId" value="CATALOGID"/>
</liferay-portlet:renderURL>


<script type="text/javascript">
    // Get Area of Focus tree for Data Set catalog type
    var ${ns}solrDatasource = new YAHOO.util.XHRDataSource("<c:out value="${solrUrl}"/>/select?&version=2.2&sort=rating+desc&", {responseType:YAHOO.util.XHRDataSource.JSON});
    var ${ns}wildcard = "*:*";
    var ${ns}query = new solrQuery(${ns}wildcard);
    ${ns}query.setRows(7);
    ${ns}query.addFacet("catalog_type", "!poc !person");


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
            var institutions = solrJSON.response.docs;
            for (var i = 0; i < institutions.length; i++) {
                var institution = institutions[i];
                var name = institution.name;

                if (name.length > 30) {
                    name = name.substring(0, 30) + "..";
                }

                var resultDiv = document.createElement('div');

                var thumbImgDiv = document.createElement('span');
                thumbImgDiv.setAttribute('style','padding-right:5px');
                var thumbImg = document.createElement('img');
                thumbImg.setAttribute('src', '/cagridportlets/images/catalog_icons/'+institution.catalog_type+'.png');
                thumbImg.setAttribute('align','middle');
                thumbImgDiv.appendChild(thumbImg);
                resultDiv.appendChild(thumbImgDiv);
                
                var link = document.createElement('a');
                link.setAttribute('href', 'javascript:${ns}navigateToCatalog("' + institution.id + '")');
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
        jQuery("#${ns}categories").html("");
    };

</script>

<div id="summaryContent">
    <div id="summaryTitle">
    Top Ranked Content
             <tags:helpLink helpURL="${usersGuideUrl}-TopRankedEntries"/>
        </div>

    <div id="${ns}categories" class="row" style="vertical-align:middle;">
        <tags:image name="loading_animation.gif" cssStyle="padding:40px;"/>
    </div>
    <br/>
    <a href="/web/guest/catalog/all">More...</a>
</div>


