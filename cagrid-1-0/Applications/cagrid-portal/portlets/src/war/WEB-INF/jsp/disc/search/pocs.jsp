<%@ include file="/WEB-INF/jsp/disc/search/header.jspf" %>

<portlet:actionURL var="formAction">
    <portlet:param name="operation" value="searchPocs"/>
</portlet:actionURL>


<c:set var="pocDiscoveryControlsFormName"><portlet:namespace/>pocDiscoveryControlsForm</c:set>
<c:set var="onSubmitAction"><portlet:namespace/>doDiscoverySearch('<c:out value="${pocDiscoveryControlsFormName}"/>')</c:set>
<form:form action="${formAction}" name="${pocDiscoveryControlsFormName}" 
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
			<input type="submit" value="Search"/>
		</td>
	</tr>
</table>
<input type="hidden" name="discoveryType" value="POC"/>
</form:form>
