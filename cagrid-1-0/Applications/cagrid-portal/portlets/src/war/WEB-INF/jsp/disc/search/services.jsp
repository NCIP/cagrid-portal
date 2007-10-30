<%@ include file="/WEB-INF/jsp/disc/search/header.jspf" %>

<portlet:actionURL var="formAction">
    <portlet:param name="operation" value="searchServices"/>
</portlet:actionURL>

<c:set var="serviceDiscoveryControlsFormName"><portlet:namespace/>serviceDiscoveryControlsForm</c:set>
<c:set var="onSubmitAction"><portlet:namespace/>doDiscoverySearch('<c:out value="${serviceDiscoveryControlsFormName}"/>')</c:set>
<form:form action="${formAction}" name="${serviceDiscoveryControlsFormName}" 
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
				<option value="url">URL</option>
				<option value="serviceMetadata.serviceDescription.name">Name</option>
				<option value="serviceMetadata.serviceDescription.description">Description</option>
				<optgroup label="&nbsp; Semantic Metadata">
					<option value="serviceMetadata.serviceDescription.semanticMetadata.conceptCode">&nbsp;&nbsp; Concept Code</option>
					<option value="serviceMetadata.serviceDescription.semanticMetadata.conceptCode">&nbsp;&nbsp; Concept Definition</option>
					<option value="serviceMetadata.serviceDescription.semanticMetadata.conceptCode">&nbsp;&nbsp; Concept Name</option>
				</optgroup>
				<optgroup label="&nbsp; Service Contexts">
					<option value="serviceMetadata.serviceDescription.serviceContextCollection.name">&nbsp;&nbsp; Name</option>
					<option value="serviceMetadata.serviceDescription.serviceContextCollection.description">&nbsp;&nbsp; Description</option>
				</optgroup>
				<optgroup label="&nbsp;&nbsp; Context Properties">
					<option value="serviceMetadata.serviceDescription.serviceContextCollection.contextPropertyCollection.name">&nbsp;&nbsp;&nbsp; QName</option>
					<option value="serviceMetadata.serviceDescription.serviceContextCollection.contextPropertyCollection.description">&nbsp;&nbsp;&nbsp; Description</option>
				</optgroup>
				<optgroup label="&nbsp;&nbsp; Operations">
					<option value="serviceMetadata.serviceDescription.serviceContextCollection.operationCollection.name">&nbsp;&nbsp;&nbsp; Name</option>
					<option value="serviceMetadata.serviceDescription.serviceContextCollection.operationCollection.description">&nbsp;&nbsp;&nbsp; Description</option>
				</optgroup>
				<optgroup label="&nbsp;&nbsp;&nbsp; Semantic Metadata">
					<option value="serviceMetadata.serviceDescription.serviceContextCollection.operationCollection.semanticMetadata.conceptCode">&nbsp;&nbsp;&nbsp;&nbsp; Concept Code</option>
					<option value="serviceMetadata.serviceDescription.serviceContextCollection.operationCollection.semanticMetadata.conceptDefinition">&nbsp;&nbsp;&nbsp;&nbsp; Concept Definition</option>
					<option value="serviceMetadata.serviceDescription.serviceContextCollection.operationCollection.semanticMetadata.conceptName">&nbsp;&nbsp;&nbsp;&nbsp; Concept Name</option>
				</optgroup>
				<optgroup label="&nbsp;&nbsp;&nbsp; Input Parameters">
					<option value="serviceMetadata.serviceDescription.serviceContextCollection.operationCollection.inputParameterCollection.QName">&nbsp;&nbsp;&nbsp;&nbsp; QName</option>
				</optgroup>
				<optgroup label="&nbsp;&nbsp;&nbsp;&nbsp; UML Class">
					<option value="serviceMetadata.serviceDescription.serviceContextCollection.operationCollection.inputParameterCollection.UMLClass.cadsrId">&nbsp;&nbsp;&nbsp;&nbsp;&nbsp; caDSR ID</option>
					<option value="serviceMetadata.serviceDescription.serviceContextCollection.operationCollection.inputParameterCollection.UMLClass.className">&nbsp;&nbsp;&nbsp;&nbsp;&nbsp; Class Name</option>
					<option value="serviceMetadata.serviceDescription.serviceContextCollection.operationCollection.inputParameterCollection.UMLClass.packageName">&nbsp;&nbsp;&nbsp;&nbsp;&nbsp; Package Name</option>
					<option value="serviceMetadata.serviceDescription.serviceContextCollection.operationCollection.inputParameterCollection.UMLClass.projectName">&nbsp;&nbsp;&nbsp;&nbsp;&nbsp; Project Name</option>
				</optgroup>
				<optgroup label="&nbsp;&nbsp;&nbsp;&nbsp;&nbsp; Semantic Metadata">
					<option value="serviceMetadata.serviceDescription.serviceContextCollection.operationCollection.inputParameterCollection.UMLClass.semanticMetadata.conceptCode">&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp; Concept Code</option>
					<option value="serviceMetadata.serviceDescription.serviceContextCollection.operationCollection.inputParameterCollection.UMLClass.semanticMetadata.conceptDefinition">&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp; Concept Definition</option>
					<option value="serviceMetadata.serviceDescription.serviceContextCollection.operationCollection.inputParameterCollection.UMLClass.semanticMetadata.conceptName">&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp; Concept Name</option>
				</optgroup>
				<optgroup label="&nbsp;&nbsp;&nbsp; Output">
					<option value="serviceMetadata.serviceDescription.serviceContextCollection.operationCollection.output.QName">&nbsp;&nbsp;&nbsp;&nbsp; QName</option>
				</optgroup>
				<optgroup label="&nbsp;&nbsp;&nbsp;&nbsp; UML Class">
					<option value="serviceMetadata.serviceDescription.serviceContextCollection.operationCollection.output.UMLClass.cadsrId">&nbsp;&nbsp;&nbsp;&nbsp;&nbsp; caDSR ID</option>
					<option value="serviceMetadata.serviceDescription.serviceContextCollection.operationCollection.output.UMLClass.className">&nbsp;&nbsp;&nbsp;&nbsp;&nbsp; Class Name</option>
					<option value="serviceMetadata.serviceDescription.serviceContextCollection.operationCollection.output.UMLClass.packageName">&nbsp;&nbsp;&nbsp;&nbsp;&nbsp; Package Name</option>
					<option value="serviceMetadata.serviceDescription.serviceContextCollection.operationCollection.output.UMLClass.projectName">&nbsp;&nbsp;&nbsp;&nbsp;&nbsp; Project Name</option>
				</optgroup>
				<optgroup label="&nbsp;&nbsp;&nbsp;&nbsp;&nbsp; Semantic Metadata">
					<option value="serviceMetadata.serviceDescription.serviceContextCollection.operationCollection.output.UMLClass.semanticMetadata.conceptCode">&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp; Concept Code</option>
					<option value="serviceMetadata.serviceDescription.serviceContextCollection.operationCollection.output.UMLClass.semanticMetadata.conceptDefinition">&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp; Concept Definition</option>
					<option value="serviceMetadata.serviceDescription.serviceContextCollection.operationCollection.output.UMLClass.semanticMetadata.conceptName">&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp; Concept Name</option>
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
<input type="hidden" name="discoveryType" value="SERVICE"/>
</form:form>
