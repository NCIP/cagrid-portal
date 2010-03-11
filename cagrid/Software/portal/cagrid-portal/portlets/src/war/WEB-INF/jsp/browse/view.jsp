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
    <tags:helpLink helpURL="${usersGuideUrl}-WorkingwithCatalogEntries"/>
</div>

<%@ include file="catalogCreateDialog.jspf" %>
<script type="text/javascript"
        src="<c:url value="/dwr/interface/CatalogEntryManagerFacade.js"/>"></script>


<liferay-portlet:actionURL var="catalogLink"
                           portletMode="view">
    <liferay-portlet:param name="operation" value="viewDetails"/>

</liferay-portlet:actionURL>


<script type="text/javascript">

    var wildcard = "*:*";


    function ${ns}viewDetails(id) {

        $("${ns}entryId").value = id;
           $("${ns}selectedCatalogType").value = g_catalogType;
           $("${ns}selectedCatalogLabel").value = g_catalogType_label;
           $("${ns}catalogType").value = "${catalogType}";
         $("${ns}aof").value = g_aof;
         $("${ns}searchKeyword").value = wildcard;
        $("${ns}selectedIds").value = g_selectedids;


        $("${ns}catalogDetailsForm").action = "${catalogLink}";
        $("${ns}catalogDetailsForm").submit();
        return false;
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
        wildcard = unescape(wildcard);
        $("${ns}keyword").value = wildcard;
    </c:if>
        ${ns}search(wildcard);
    });

    function ${ns}search(keyword) {
        ${ns}resetPage();

        if (keyword.length < 1) {
            keyword = wildcard;
        }
        else {
            if (keyword.indexOf("http:") > -1 || keyword.indexOf("www.") > -1) {
            <%-- if URL surround keyword with ""--%>
                if (keyword.indexOf('"') != 0)
                    keyword = '"' + keyword;
                if (keyword.lastIndexOf('"') != keyword.length - 1)
                    keyword = keyword + '"';
            }
            else {
                keyword = unescape(keyword);
            }
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

<form:form id="${ns}catalogDetailsForm" method="post">
    <input type="hidden" name="entryId" id="${ns}entryId"value="">

    <input type="hidden" name="selectedCatalogType"  id="${ns}selectedCatalogType" value="">
    <input type="hidden" name="selectedCatalogLabel" id="${ns}selectedCatalogLabel" value="">
    <input type="hidden" name="catalogType" id="${ns}catalogType" value="">
    <input type="hidden" name="aof" id="${ns}aof" value="">
    <input type="hidden" name="searchKeyword" id="${ns}searchKeyword" value="search">
    <input type="hidden" name="selectedIds"  id="${ns}selectedIds" value="">


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




