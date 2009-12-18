<%@ include file="/WEB-INF/jsp/include/includes.jspf" %>
<%@ include file="/WEB-INF/jsp/include/liferay-includes.jspf" %>

<%
    response.setHeader("Cache-Control", "no-cache"); //HTTP 1.1
    response.setHeader("Pragma", "no-cache"); //HTTP 1.0
    response.setDateHeader("Expires", 0); //prevents caching at the proxy server
%>

<%@ include file="browse-search-includes.jspf" %>
<%@ taglib uri="http://liferay.com/tld/theme" prefix="liferay-theme" %>

<div style="text-align:right;">
    <a href="/web/guest/help/-/wiki/Help/Browsing+the+Catalog">Help</a>
</div>

<%@ include file="catalogCreateDialog.jspf" %>
<script type="text/javascript"
        src="<c:url value="/dwr/interface/CatalogEntryManagerFacade.js"/>"></script>


<liferay-portlet:renderURL var="catalogLink"
                           portletMode="view">
    <liferay-portlet:param name="operation" value="viewDetails"/>
    <liferay-portlet:param name="entryId" value="ENTRYIDTOREPLACED"/>
    <liferay-portlet:param name="selectedCatalogType" value="SELECTEDCATALOGTYPETOREPLACE"/>
    <liferay-portlet:param name="selectedCatalogLabel" value="SELECTEDCATALOGLABELTOREPLACE"/>
    <liferay-portlet:param name="catalogType" value="CATALOGTYPETOREPLACE"/>
    <liferay-portlet:param name="aof" value="AOFTOREPLACE"/>
    <liferay-portlet:param name="searchKeyword" value="SEARCHKEYWORDTOREPLACE"/>
    <liferay-portlet:param name="selectedIds" value="SELECTEDIDSTOREPLACE"/>
</liferay-portlet:renderURL>


<script type="text/javascript">

    var wildcard = "*:*";


    function ${ns}viewDetails(id) {
        var theLink = "${catalogLink}";
        var newLink = theLink.replace("ENTRYIDTOREPLACED", id);
        newLink = newLink.replace("SELECTEDCATALOGTYPETOREPLACE", g_catalogType);
        newLink = newLink.replace("SELECTEDCATALOGLABELTOREPLACE", g_catalogType_label);
        newLink = newLink.replace("CATALOGTYPETOREPLACE", "${catalogType}");
        newLink = newLink.replace("SEARCHKEYWORDTOREPLACE", wildcard);
        newLink = newLink.replace("AOFTOREPLACE", g_aof);
        newLink = newLink.replace("SELECTEDIDSTOREPLACE", g_selectedids);

        window.location = newLink;
    }

    function ${ns}resetPage() {
        $("featuredDiv").setStyle({display: 'none'});
        $("jsFillerFeaturedResults").innerHTML = "";
        $("regularResults").setStyle({display: 'none'});
        $("jsFillerRegularResults").innerHTML = "";
        $("${ns}suggestions").innerHTML = "";
    }

    <%--suggest--%>
    function ${ns}addSuggestions(suggestions) {
    <%--traverse the suggestion object--%>
        if (suggestions.length > 0) {
            suggestions = suggestions[1].suggestion;
            if (suggestions.length > 0) {

                var suggestionDiv = document.createElement('div');
                suggestionDiv.appendChild(document.createTextNode("Did you mean:"));

                for (var i = 0; i < suggestions.length; i++) {

                    var suggestLnk = document.createElement('a');
                    var suggestWord = suggestions[i];
                    suggestLnk.setAttribute('href', 'javascript:${ns}search("' + suggestWord + '")');
                    suggestLnk.className = "suggestLink";
                    suggestLnk.innerHTML = suggestWord + " ";
                    suggestionDiv.appendChild(suggestLnk);
                }
                $("${ns}suggestions").appendChild(suggestionDiv);
            }
        }
    }


    function ${ns}pageCallback(type, args) {

        var resultList = args[0];
        ${ns}resetPage();

        if (resultList != null) {
            YAHOO.log("Received results of size:" + resultList.length);
            if (resultList < 1) {
                $("regularResults").setStyle({display: 'block'});
                var noResultMsg = document.createElement('div');
                noResultMsg.appendChild(document.createTextNode("Your search did not match any catalog entries."));
                $("jsFillerRegularResults").appendChild(noResultMsg);
            }
            else {
                for (var i = 0, len = resultList.length; i < len; ++i) {
                    $("regularResults").setStyle({display: 'block'});
                    var result = resultList[i];

                    var resultDiv = document.createElement('div');
                    resultDiv.className = "oneResultDiv";
                    var detailsLnk = document.createElement('a');
                    detailsLnk.setAttribute('id', '${ns}cat_name' + result.id)
                    detailsLnk.setAttribute('href', 'javascript:${ns}viewDetails(' + result.id + ')');
                    detailsLnk.setAttribute('name', 'Details');
                    detailsLnk.innerHTML = result.name;

                <%--represents the popup with CE details--%>
                    var popupLnk = document.createElement('span');
                    popupLnk.className = "yui-skin-sam";
                    popupLnk.setAttribute('id', '${ns}catalogPopupDiv' + result.id);

                    var iconLnk = document.createElement('a');
                    iconLnk.setAttribute('href', 'javascript:${ns}viewDetails(' + result.id + ')');
                    iconLnk.setAttribute('name', 'Details');
                    iconLnk.className = "oneResultIcon";
                    var icon = document.createElement('img');
                    icon.setAttribute('src', '<c:url value="/images/catalog_icons/"/>' + result.catalog_type + '.png');
                    icon.setAttribute('alt', '');
                    iconLnk.appendChild(icon);
                    resultDiv.appendChild(iconLnk);

                    var nameDiv = document.createElement('div');
                    nameDiv.appendChild(detailsLnk);
                    nameDiv.appendChild(popupLnk);
                    resultDiv.appendChild(nameDiv);


                    var descDiv = document.createElement('div');
                    descDiv.setAttribute("id", "${ns}cat_desc" + result.id);
                    if (result.description == null || result.description == undefined || result.description == "") {
                        descDiv.className = "oneResultNoDescription";
                        descDiv.appendChild(document.createTextNode("No information available"));
                    } else {
                        descDiv.className = "oneResultDescription";
                        descDiv.appendChild(document.createTextNode((result.description).truncate(80, "...")));
                    }
                    resultDiv.appendChild(descDiv);

                    if (result.featured) {
                        $("featuredDiv").setStyle({display: 'block'});
                        $("jsFillerFeaturedResults").appendChild(resultDiv);
                        continue;
                    }
                    $("jsFillerRegularResults").appendChild(resultDiv);

                }
            }
        }
    }


    function checkEnter(e, keyword) { //e is event object passed from function invocation
        var characterCode //literal character code will be stored in this variable
        if (e && e.which) { //if which property of event object is supported (NN4)
            e = e;
            characterCode = e.which;//character code is contained in NN4's which property
        }
        else {
            e = event
            characterCode = e.keyCode; //character code is contained in IE's keyCode property
        }
        if (characterCode == 13) { //if generated character code is equal to ascii 13 (if enter key)
            if (keyword.length > 0)
                ${ns}search(keyword);

            return false;
        }
        else {
            return true;
        }
    }

    jQuery(document).ready(function() {
    <c:if test="${not empty searchKeyword && searchKeyword != '*:*'}">
        wildcard = "<c:out value="${searchKeyword}"/>";
        $("${ns}keyword").value = wildcard;
    </c:if>
        ${ns}search(wildcard);
    });

    function ${ns}search(keyword) {
        ${ns}resetPage();

        if (keyword.length < 1) {
            keyword = wildcard;
        }
        wildcard = keyword;

        new Catalogs({
            keyword: keyword,
            catalogType: "${catalogType}",
            selectedCatalogType:"${selectedCatalogType}",
            selectedCatalogLabel:"${selectedCatalogLabel}",
            paginatorDiv: "${ns}paginatorDiv",
            searchBar:"${ns}searchBar",
            treeDiv: "${ns}tree",
            <c:if test="${selectedIds!=null}">
            ids:'<c:out value="${selectedIds}"/>',
            </c:if>
            <c:if test="${aof !=null}">
            aof:'<c:out value="${aof}"/>',
            </c:if>
            sortField:$("${ns}sortList").value

        });
        //subscribe to results so that this page can self update (see pageCallback())
        resultEvent.subscribe(${ns}pageCallback);
    }

</script>

<form:form id="${ns}catalogDetailsForm" name="catalogDetailsForm">
    <input type="hidden" name="entryId" value=""><input type="hidden" name="operation" value="viewDetails">
    <input type="hidden" name="query" value="">

    <div>
        <tags:searchBox searchCallback="checkEnter" id="${ns}keyword"/>

        <div>
            <div id="${ns}tree" class="tree-container"></div>
            <div class="searchResults">
                <%@ include file="/WEB-INF/jsp/browse/sort.jspf" %>
                <div id="${ns}searchBar" class="searchBar">
                </div>

                <div id="${ns}suggestions">
                        <%--add suggestions here--%>
                </div>


                <div id="catalogResult">
                    <div id="${ns}catalogs">
                        <div id="featuredDiv" style="display:none;">
                            <h3>Featured Catalog Entries</h3>

                            <div id="jsFillerFeaturedResults"><!-- Featured search results will go here --></div>
                        </div>
                        <div id="regularResults" style="display:none;">
                            <h3>Catalog Search Results</h3>

                            <div id="jsFillerRegularResults"><!-- Not featured search results will go here --></div>
                        </div>
                    </div>
                </div>

                <div class="yui-skin-sam">
                    <div id="${ns}paginatorDiv" class="pagination">
                        <!-- pagination controls will go here -->
                    </div>
                </div>
            </div>
            <br style="clear:both;"/>
        </div>
    </div>
</form:form>




