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
    <portlet:param name="entryId" value="PERSONID"/>
</liferay-portlet:renderURL>


<script type="text/javascript">
    // Get Area of Focus tree for Data Set catalog type
    var ${ns}solrDatasource = new YAHOO.util.XHRDataSource("<c:out value="${solrUrl}"/>/select?facet=true&facet.field=author&", {responseType:YAHOO.util.XHRDataSource.JSON});
    var ${ns}wildcard = "*:*";
    var ${ns}query = new solrQuery(${ns}wildcard);
    ${ns}query.setRows(7);
    ${ns}query.addFacet("catalog_type", "person");


    function ${ns}navigateToPeople(id) {
        var searchLink = "${dataSetLnk}";
        searchLink = searchLink.replace("/guest/home", "/guest/catalog/people");
        searchLink = searchLink.replace("PERSONID", id);

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
            var persons = solrJSON.response.docs;
            for (var i = 0; i < persons.length; i++) {
                var person = persons[i];
                var firstName = person.firstName;
                var lastName = person.lastName;
                var resultDiv = document.createElement('div');
                var personLnk = document.createElement('a');
                personLnk.setAttribute('href', 'javascript:${ns}navigateToPeople("' + person.id + '")');
                personLnk.innerHTML = firstName + ' ' + lastName;
                resultDiv.appendChild(personLnk);
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
    Top Contributors
    <tags:helpLink  helpURL="${usersGuideUrl}-TopContributors"/>
</div>

    <div id="${ns}categories" class="row">
        <tags:image name="loading_animation.gif" cssStyle="padding:40px;"/>
    </div>
    <br/>
    <a href="/web/guest/catalog/people">More...</a>
</div>


