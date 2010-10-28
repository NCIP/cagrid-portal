<%@ include file="/WEB-INF/jsp/include/includes.jspf" %>
<%@ taglib prefix="tags" tagdir="/WEB-INF/tags" %>

<script type="text/javascript" src='<c:url value="/dwr/engine.js"/>'></script>
<script type="text/javascript" src='<c:url value="/dwr/interface/StatusService.js"/>'></script>
<script type="text/javascript" src="<c:url value="/js/scriptaculous/prototype.js"/>"></script>

<portlet:actionURL var="action">
    <portlet:param name="operation" value="selectItemForDiscovery"/>
    <portlet:param name="type" value="SERVICE"/>
    <portlet:param name="selectedId" value="121"/>
</portlet:actionURL>
<c:set var="prefix"><portlet:namespace/></c:set>

<div id="${prefix}errorDiv" style="display:none">
    <span class="errorMsg">
        Error communicating with Server. Please refresh the webpage.
    </span>
</div>

<div id="${prefix}loadingDiv" style="height:500px;">
    <div id="${prefix}loadingDivInner"  style="height:100%;padding-left:30px;padding-top:20px;">
        <tags:image name="loading_animation.gif"  cssStyle="align:center;"/>
    </div>
</div>

<div style="height:500px;display:none;" id="${prefix}statusDiv">
    <c:set var="formName">${prefix}serviceDetailsForm</c:set>

    <div style="float:right;padding-right:5px">
        <a href="javascript:${prefix}reloadStatus();">
            <tags:image name="refresh.png"
                        title="Refresh"/>
        </a>
    </div>
    <form:form id="${formName}" action="${action}">
        <input type="hidden" alt="Hidden" name="selectedId"/>

        <div class="label" id="numberNew">
            Here are the newest services...
        </div>

        <div class="row">
            <div class="label">
                Name
            </div>
            <div class="value" style="font-weight:bold;">
                Type
            </div>
        </div>

        <div id="${prefix}latestServices"><%----%></div>

    </form:form>

    <div class="row">
        <div class="label">
            There are currently...
        </div>

        <table cellpadding="5">
            <tr>
                <td style="padding-right:5px;text-align:left;">
                    <portlet:actionURL var="participantsAction">
                        <portlet:param name="operation" value="selectDirectoryForDiscovery"/>
                        <portlet:param name="selectedDirectory" value="${statusBean.participantsDirectory.id}"/>
                    </portlet:actionURL>
                    <a style="text-align:left;" href="<c:out value="${participantsAction}"/>">
                        <span id="${prefix}participantCount"><%----%></span>
                    </a>
                </td>
                <td>caBIG Participants,</td>
            </tr>
            <tr>
                <td style="padding-right:5px;">
                    <portlet:actionURL var="servicessAction">
                        <portlet:param name="operation" value="selectDirectoryForDiscovery"/>
                        <portlet:param name="selectedDirectory" value="${statusBean.servicesDirectory.id}"/>
                    </portlet:actionURL>
                    <a href="<c:out value="${servicessAction}"/>">
                        <span id="${prefix}servicesCount"><%----%></span>
                    </a>
                </td>
                <td>grid services, which include</td>
            </tr>
            <tr>

                <td style="padding-right:5px;">
                    <portlet:actionURL var="dataServicesAction">
                        <portlet:param name="operation" value="selectDirectoryForDiscovery"/>
                        <portlet:param name="selectedDirectory" value="${statusBean.dataServicesDirectory.id}"/>
                    </portlet:actionURL>
                    <a href="<c:out value="${dataServicesAction}"/>">
                        <span id="${prefix}dataServicesCount"><%----%></span>
                    </a>
                </td>
                <td>data services, and</td>
            </tr>
            <tr>

                <td style="padding-right:5px;">
                    <portlet:actionURL var="analyticalServicesAction">
                        <portlet:param name="operation" value="selectDirectoryForDiscovery"/>
                        <portlet:param name="selectedDirectory" value="${statusBean.analyticalServicesDirectory.id}"/>
                    </portlet:actionURL>
                    <a href="<c:out value="${analyticalServicesAction}"/>">
                        <span id="${prefix}analServicesCount"><%----%></span>
                    </a>
                </td>
                <td>analytical services.</td>
            </tr>
        </table>
    </div>



    <div style="padding-top:10px;">
        <div>
            <tags:infoPopup id="<portlet:namespace>status" popup_href="javascript:void();"
                            popup_name="Last Updated:"
                            popup_text="Portal frequently searches for
                            new services and updates its database."/>
            <span id="${prefix}lastUpdated"><%----%></span>
        </div>
    </div>


    <div style="padding-top:10px;">
        <tags:infoPopup id="<portlet:namespace>diagnosticLnk" popup_href="${servicessAction}"
                        popup_name="Where is my service?"
                        popup_text="Search for your service in the Discovery Portlet or use the Service Diagnostics Portlet to get details on your service advertisement"/>
    </div>

</div>


<script type="text/javascript">
    var _html =$('${prefix}loadingDivInner').innerHTML;

    function navigateToService(id){
        $('${prefix}serviceDetailsForm').selectedId.value = id;
        $('${prefix}serviceDetailsForm').submit();
    }

    function ${prefix}reloadStatus(){
        setInnerHTML($('${prefix}loadingDiv'),_html);
        setTimeout("${prefix}loadStatus()", 100);

    }

    <%--for safari--%>
    function setInnerHTML( element, html, count ) {
        element.innerHTML = html;

        if( ! count )
            count = 1;

        if( html != '' && element.innerHTML == '' && count < 5 ) {
            ++count;
            setTimeout( function() {
                setInnerHTML( element, html, count );
            }, 50 );
        }
    }

    function ${prefix}loadStatus(){
        dwr.engine.beginBatch({timeout:90000});
        StatusService.latestServices(function(latestServices){$('${prefix}latestServices').innerHTML=latestServices;});
        StatusService.servicesCount(function(count){$('${prefix}servicesCount').innerHTML=count;});
        StatusService.dataServicesCount(function(count){$('${prefix}dataServicesCount').innerHTML=count;});
        StatusService.analServicesCount(function(count){$('${prefix}analServicesCount').innerHTML=count;});
        StatusService.participantCount(function(count){$('${prefix}participantCount').innerHTML=count;});
        StatusService.lastUpdated(function(lastUpdated){$('${prefix}lastUpdated').innerHTML=lastUpdated;});

        dwr.engine.endBatch({
            async:false,
            errorHandler:function(errorString, exception) {
                $('${prefix}loadingDiv').innerHTML=$('${prefix}errorDiv').innerHTML}
        });

        $('${prefix}loadingDiv').innerHTML=$('${prefix}statusDiv').innerHTML;
    }

    setTimeout("${prefix}loadStatus()", 100);

</script>

