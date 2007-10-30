<%@ include file="/WEB-INF/jsp/disc/search/header.jspf" %>

<portlet:actionURL var="formAction">
    <portlet:param name="operation" value="searchParticipants"/>
</portlet:actionURL>

<c:set var="participantDiscoveryControlsFormName"><portlet:namespace/>participantDiscoveryControlsForm</c:set>
<c:set var="onSubmitAction"><portlet:namespace/>doDiscoverySearch('<c:out value="${participantDiscoveryControlsFormName}"/>')</c:set>
<form:form action="${formAction}" name="${participantDiscoveryControlsFormName}"
	onsubmit="return ${onSubmitAction}"
>
<table>
	<tr>
		<td>Keyword:</td>
		<td>
			<input type="text" name="keywords"/>
		</td>
	</tr>
	<tr>
		<td>Search Fields:</td>
		<td>
			<select name="searchFields" multiple size="8">
				<option value="name">Name</option>
				<option value="institution">Institution</option>
				<option value="phone">Phone</option>
				<option value="emailAddress">Email</option>
				<option value="homepageUrl">Homepage URL</option>
				<optgroup label="&nbsp; Address">
					<option value="address.country">Country</option>
					<option value="address.locality">Locality</option>
					<option value="address.postalCode">Postal Code</option>
					<option value="address.stateProvince">State/Province</option>
					<option value="address.street1">Street (line 1)</option>
					<option value="address.street2">Street (line 2)</option>
				</optgroup>
				<optgroup label="&nbsp; Workspace">
					<option value="participation.workspace.name">Name</option>
					<option value="participation.workspace.abbreviation">Abbreviation</option>
					<option value="participation.workspace.description">Description</option>
				</optgroup>
			</select>
		</td>
	</tr>
	<tr>
		<td colspan="2">
			<input type="submit" value="Search"/>
		</td>
	</tr>
</table>
<input type="hidden" name="discoveryType" value="PARTICIPANT"/>
</form:form>
