<%@ include file="/WEB-INF/jsp/include/includes.jspf" %>
<%@ include file="/WEB-INF/jsp/include/liferay-includes.jspf" %>

<c:set var="ns"><portlet:namespace/></c:set>
<script type="text/javascript" src="<c:url value="/js/yui/yahoo-dom-event/yahoo-dom-event.js"/>"></script>
<script type="text/javascript" src="<c:url value="/js/yui/element/element-min.js"/>"></script>
<script type="text/javascript" src="<c:url value="/js/yui/datasource/datasource-min.js"/>"></script>
<script type="text/javascript" src="<c:url value="/js/yui/json/json-min.js"/>"></script>
<script type="text/javascript" src="<c:url value="/js/yui/connection/connection-min.js"/>"></script>
<script type="text/javascript" src="<c:url value="/js/yui/get/get-min.js"/>"></script>
<script src="<c:url value="/js/browse-catalog.js"/>"></script>

<liferay-portlet:renderURL var="dataSetLnk" portletName="BrowsePortlet_WAR_cagridportlets"
                           portletMode="view">
    <liferay-portlet:param name="operation" value="view"/>
    <liferay-portlet:param name="aof" value="AOFVALUE"/>
</liferay-portlet:renderURL>


<script type="text/javascript">

    // Get Area of Focus tree for Data Set catalog type
    var ${ns}solrDatasource = new YAHOO.util.XHRDataSource("<c:out value="${solrServiceUrl}"/>/select?", {responseType:YAHOO.util.XHRDataSource.JSON});
    var  ${ns}wildcard = "*:*";
    var  ${ns}query = new solrQuery(${ns}wildcard);
     ${ns}query.setTree(true);
     ${ns}query.addFacet("catalog_type", "dataset");


    function ${ns}navigateToDataSet(aof) {
        var searchLink = "${dataSetLnk}";
        searchLink = searchLink.replace("/guest/home", "/guest/catalog/datasets");
        searchLink = searchLink.replace("AOFVALUE",aof);

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

            var solrJSON = YAHOO.lang.JSON.parse(oParsedResponse.results.responseText);
            var aofTree = solrJSON.tree;
            if (aofTree.length > 1) {
                var aofs = aofTree[1].nodes;
                for (var i = 0; i < aofs.length; i++) {
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
        alert("failed");
    };

</script>

<div>
    <div>
        <h2>Data Set Categories</h2>
    </div>

    <div id="${ns}categories" class="row">
        <%--ajax--%>
    </div>

</div>


