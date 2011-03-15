<%@ include file="/WEB-INF/jsp/disc/search/header.jspf" %>

<portlet:actionURL var="formAction">
    <portlet:param name="operation" value="searchPocs"/>
</portlet:actionURL>


<c:set var="pocDiscoveryControlsFormName"><portlet:namespace/>pocDiscoveryControlsForm</c:set>
<c:set var="onSubmitAction"><portlet:namespace/>doDiscoverySearch('<c:out
        value="${pocDiscoveryControlsFormName}"/>')</c:set>
<form:form action="${formAction}" name="${pocDiscoveryControlsFormName}"
           onsubmit="return ${onSubmitAction}"
        >
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
                <label for="keywords">
                    Keyword:
                </label>
            </td>
            <td>
                <input type="text" alt="Keywords" name="keywords" id="keywords"/>
            </td>
        </tr>
        <tr>
            <td><label for="searchFields" /> Search Fields:</td>
            <td>
                <select id="searchFields" name="searchFields" multiple size="8">
                    <option value="person.firstName">First Name</option>
                    <option value="person.lastName">Last Name</option>
                    <option value="role">Role</option>
                    <option value="affiliation">Affiliation</option>
                    <option value="serviceDescription.name">Service Name</option>
                    <option value="researchCenter.displayName">Research Center Name</option>
                </select>
            </td>
        </tr>
        <tr>
            <td colspan="2">
                <input type="submit" alt="Search" value="Search"/>
            </td>
        </tr>
        </tbody>
    </table>
    <input type="hidden" name="discoveryType" alt="Hidden" value="POC"/>
</form:form> 
