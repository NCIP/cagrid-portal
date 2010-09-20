<%@ include file="/WEB-INF/jsp/include/includes.jspf" %>

<%@ include file="/WEB-INF/jsp/disc/tabs.jspf" %>

<portlet:actionURL var="action"/>
<c:set var="prefix"><portlet:namespace/></c:set>


<table>
    <thead>
    <tr>
        <td>&nbsp;</td>
        <td>&nbsp;</td>
    </tr>
    </thead>
    <tbody>
    <tr>
        <td>
            <form:form id="${prefix}directoryMap" action="${action}" commandName="mapCommand">
                <%@ include file="/WEB-INF/jsp/disc/directory/directoriesSelect.jspf" %>
                <input type="hidden" name="operation" value="selectDirectoryMap"/>
            </form:form>
        </td>
        <td>
            <form:form id="${prefix}resultsMap" action="${action}" commandName="mapCommand">
                <%@ include file="/WEB-INF/jsp/disc/directory/searchResultsSelect.jspf" %>
                <input type="hidden" name="operation" value="selectResultsMap"/>
            </form:form>
        </td>
    </tr>
    </tbody>
</table>

<c:set var="selectItemOperationName" value="selectItem"/>
<c:set var="selectItemsOperationName" value="selectItems"/>

<c:set var="mapBean" value="${mapCommand}"/>

<%@ include file="/WEB-INF/jsp/map/map.jspf" %>
