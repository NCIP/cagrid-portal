<%@ include file="/WEB-INF/jsp/disc/search/header.jspf" %>

<portlet:actionURL var="formAction">
    <portlet:param name="operation" value="searchParticipants"/>
</portlet:actionURL>

<c:set var="participantDiscoveryControlsFormName"><portlet:namespace/>participantDiscoveryControlsForm</c:set>
<c:set var="onSubmitAction"><portlet:namespace/>doDiscoverySearch('<c:out
        value="${participantDiscoveryControlsFormName}"/>')</c:set>
<form:form id="${participantDiscoveryControlsFormName}" action="${formAction}"
           name="${participantDiscoveryControlsFormName}"
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
                <input type="text" name="keywords"/>
            </td>
        </tr>
        <tr>
            <td>Search Fields:</td>
            <td>
                <select id="${participantDiscoveryControlsFormName}Search" name="searchFields" multiple size="8">
                    <option id="${participantDiscoveryControlsFormName}Name" value="name">Name</option>
                    <option id="${participantDiscoveryControlsFormName}Institution" value="institution">Institution
                    </option>
                    <option id="${participantDiscoveryControlsFormName}Phone" value="phone">Phone</option>
                    <option id="${participantDiscoveryControlsFormName}Email" value="emailAddress">Email</option>
                    <option id="${participantDiscoveryControlsFormName}Homepage" value="homepageUrl">Homepage URL
                    </option>
                    <optgroup id="${participantDiscoveryControlsFormName}Address" label="&nbsp; Address">
                        <option id="${participantDiscoveryControlsFormName}Country" value="address.country">Country
                        </option>
                        <option id="${participantDiscoveryControlsFormName}Country" value="address.locality">Locality
                        </option>
                        <option id="${participantDiscoveryControlsFormName}Postal" value="address.postalCode">Postal
                            Code
                        </option>
                        <option id="${participantDiscoveryControlsFormName}State" value="address.stateProvince">
                            State/Province
                        </option>
                        <option id="${participantDiscoveryControlsFormName}Street1" value="address.street1">Street (line
                            1)
                        </option>
                        <option id="${participantDiscoveryControlsFormName}Street2" value="address.street2">Street (line
                            2)
                        </option>
                    </optgroup>
                    <optgroup id="${participantDiscoveryControlsFormName}Workspace" label="&nbsp; Workspace">
                        <option id="${participantDiscoveryControlsFormName}Wname" value="participation.workspace.name">
                            Name
                        </option>
                        <option id="${participantDiscoveryControlsFormName}Wabbr"
                                value="participation.workspace.abbreviation">Abbreviation
                        </option>
                        <option id="${participantDiscoveryControlsFormName}Wdesc"
                                value="participation.workspace.description">Description
                        </option>
                    </optgroup>
                </select>
            </td>
        </tr>
        <tr>
            <td colspan="2">
                <input id="${participantDiscoveryControlsFormName}" alt="Search" type="submit" value="Search"/>
            </td>
        </tr>
        </tbody>
    </table>
    <input type="hidden" name="discoveryType" value="PARTICIPANT"/>
</form:form>
