<%@ include file="/WEB-INF/jsp/include/includes.jspf" %>
<%@ include file="/WEB-INF/jsp/include/liferay-includes.jspf" %>

<link type="text/css" rel="stylesheet"
      href="<c:url value="/css/latest-content.css"/>"/>
<script type="text/javascript" src='<c:url value="/dwr/interface/LatestContentService.js"/>'></script>


<portlet:defineObjects/>
<liferay-theme:defineObjects/>
<c:set var="ns"><portlet:namespace/></c:set>

<liferay-portlet:renderURL var="catalogLink" portletName="BrowsePortlet_WAR_cagridportlets"
                           portletMode="view">
    <liferay-portlet:param name="operation" value="viewDetails"/>
    <liferay-portlet:param name="entryId" value="ENTRYIDTOREPLACE"/>
</liferay-portlet:renderURL>

<div id="latestContent">
    <h3 id="latestContentTitle">Latest Content</h3>

    <div id="${ns}errorDiv" style="display:none">
        <span class="errorMsg">
            Error communicating with Server. Please refresh the webpage.
        </span>
    </div>

    <div id="${ns}loadingDiv">
        <div id="${ns}loadingDivInner">
            <tags:image name="loading_animation.gif" cssStyle="padding:40px;"/>
        </div>
    </div>

    <div id="${ns}statusDiv">
        <c:set var="formName">${ns}serviceDetailsForm</c:set>

        <form:form id="${formName}" action="${action}">
            <input type="hidden" name="selectedId"/>

            <div id="${ns}latestContent"><%----%></div>
        </form:form>
    </div>

</div>


<script type="text/javascript">
    var _html = $('${ns}loadingDivInner').innerHTML;

       var theLink = "${catalogLink}";
    
    function viewDetails(id) {
        var newLink = theLink.replace("guest/home", "guest/catalog/all").replace("ENTRYIDTOREPLACE", id);
        window.location = newLink;
    }

    function ${ns}reloadStatus() {
        ${ns}setInnerHTML($('${ns}loadingDiv'), _html);
        setTimeout("${ns}loadStatus()", 100);

    }

    <%--for safari--%>
    function ${ns}setInnerHTML(element, html, count) {
        element.innerHTML = html;

        if (! count)
            count = 1;

        if (html != '' && element.innerHTML == '' && count < 5) {
            ++count;
            setTimeout(function() {
                ${ns}setInnerHTML(element, html, count);
            }, 50);
        }
    }

    function ${ns}loadStatus() {
        dwr.engine.beginBatch({timeout:90000});

        LatestContentService.getContent(function(latestContent) {
            $('${ns}latestContent').innerHTML = latestContent;
        });

        dwr.engine.endBatch({
            async:true,
            errorHandler:function(errorString, exception) {
                $('${ns}loadingDiv').innerHTML = $('${ns}errorDiv').innerHTML
            }
        });

        $('${ns}loadingDiv').innerHTML = $('${ns}statusDiv').innerHTML;
    }

    jQuery(document).ready(function() {

        ${ns}loadStatus();
    });

</script>

