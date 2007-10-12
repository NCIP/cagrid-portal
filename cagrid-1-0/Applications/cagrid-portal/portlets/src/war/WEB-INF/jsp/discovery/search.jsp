<%@ include file="/WEB-INF/jsp/include.jsp" %>

<style type="text/css">
<%@ include file="/css/light.tabs.css" %>
</style>

<script type="text/javascript">
	//<![CDATA[
	
	function <portlet:namespace/>doDiscoverySearch(formName){
		var form = document[formName];
		
		//If nothing is selected, select all.
		if(jQuery(form).find("option:selected").size() == 0){
			jQuery(form).find("option").each(function(){this.selected = true;});
		}
		
		//Make sure a keywords value has been given
		if(jQuery.trim(form.keywords.value).length == 0){
			alert("Please enter one or more key words.");
			return false;
		}
		return true;
	}
	
	jQuery(document).ready(function(){
		jQuery("#<portlet:namespace/>searchControls").tabs();
	});
	
	// ]]>
</script>

<portlet:actionURL var="formAction">
    <portlet:param name="action" value="search"/>
</portlet:actionURL>



<div id="<portlet:namespace/>searchControls">

<ul class="tabs">
	<li><a href="#<portlet:namespace/>serviceDiscoveryControls"><span>Discover Services</span></a></li>
	<li><a href="#<portlet:namespace/>participantDiscoveryControls"><span>Discover Participants</span></a></li>
	<li><a href="#<portlet:namespace/>pocDiscoveryControls"><span>Discover POCs</span></a></li>
</ul>

<div id="<portlet:namespace/>serviceDiscoveryControls">
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

<!-- End serviceDiscoveryControls -->
</div>

<div id="<portlet:namespace/>participantDiscoveryControls">
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
</div>

<div id="<portlet:namespace/>pocDiscoveryControls">
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
</div>


<!-- End searchControls -->
</div>