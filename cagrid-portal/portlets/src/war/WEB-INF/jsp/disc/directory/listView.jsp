<%@ include file="/WEB-INF/jsp/include/includes.jspf" %>

<script type="text/javascript" src="<c:url value="/js/scriptaculous/prototype.js"/>"></script>

<script src="<c:url value="/js/script.js"/>" type="text/javascript"></script>
<script src="<c:url value="/js/tablesort.js"/>" type="text/javascript"></script>
<script src="<c:url value="/js/pagination.js"/>" type="text/javascript"></script>

<%@ include file="/WEB-INF/jsp/disc/tabs.jspf" %>

<c:set var="prefix"><portlet:namespace/></c:set>
<style type="text/css">

    <!--
    <%@ include file="/css/styleSheet.css" %>
    -->

    #${prefix}mainTable {
        width: 100%;
        visibility: collapse;
        border:antiquewhite thin;
    }

</style>

<portlet:actionURL var="action"/>


<table>
    <tr>
        <td style="padding-right: 5px;">
            <b>Directories:</b>
            <form:form id="${prefix}listDirectories" action="${action}" commandName="listCommand">
                <%@ include file="/WEB-INF/jsp/disc/directory/directoriesSelect.jspf" %>
                <input type="hidden" name="operation" value="selectDirectoryList" alt="Hidden"/>
            </form:form>
        </td>
        <td style="padding-right: 5px;">
            <b>Search Results:</b>
            <form:form id="${prefix}listResults" action="${action}" commandName="listCommand">
                <%@ include file="/WEB-INF/jsp/disc/directory/searchResultsSelect.jspf" %>
                <input type="hidden" name="operation" value="selectResultsList" alt="Hidden"/>
            </form:form>
        </td>
    </tr>
</table>

<c:choose>
    <c:when test="${empty listCommand}">
        <p>
            Select an item from either the <i>Directories</i> or <i>Search Resutls</i> drop-down lists, above.
        </p>
    </c:when>

    <c:otherwise>
	
		<span class="scrollerStyle1">
			Found <c:out value="${fn:length(listCommand.scroller.objects)}"/> 
			<c:choose>
                <c:when test="${listCommand.type == 'SERVICE'}">
                    Services
                </c:when>
                <c:when test="${listCommand.type == 'PARTICIPANT'}">
                    Participants
                </c:when>
                <c:otherwise> Points of Contact</c:otherwise>
            </c:choose>
        </span>
        </p>
        <br/>
        <c:set var="scroller" value="${listCommand.scroller}"/>
        <c:set var="scrollOperation" value="scrollDiscoveryList"/>

        <c:choose>
            <c:when test="${listCommand.type == 'SERVICE'}">
                <%@ include file="/WEB-INF/jsp/disc/directory/services.jspf" %>
            </c:when>
            <c:when test="${listCommand.type == 'PARTICIPANT'}">
                <%@ include file="/WEB-INF/jsp/disc/directory/participants.jspf" %>
            </c:when>
            <c:when test="${listCommand.type == 'POC'}">
                <%@ include file="/WEB-INF/jsp/disc/directory/pocs.jspf" %>
            </c:when>
            <c:otherwise>
                <span color="red">UNKNOWN TYPE '<c:out value="${listCommand.type}"/>'</span>
            </c:otherwise>
        </c:choose>
    </c:otherwise>
</c:choose>

<br/><br/>


<script type="text/javascript">

    function sortCompleteCallback(tableId) {
        $("${prefix}mainTable").style.visibility = 'visible';
    }

</script>