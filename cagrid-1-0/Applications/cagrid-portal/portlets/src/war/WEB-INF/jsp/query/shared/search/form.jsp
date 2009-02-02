<%@ include file="/WEB-INF/jsp/include/includes.jspf"%>
<%@ include file="/WEB-INF/jsp/query/tabs.jspf" %>

<script type="text/javascript">
	//<![CDATA[
	
	function <portlet:namespace/>doSharedQuerySearch(formName){
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
	
	// ]]>
</script>


<portlet:actionURL var="formAction">
    <portlet:param name="operation" value="searchSharedQueries"/>
    <portlet:param name="discoveryType" value="CQL_QUERY"/>
</portlet:actionURL>


<c:set var="formName"><portlet:namespace/>searchSharedQueriesForm</c:set>
<c:set var="onSubmitAction"><portlet:namespace/>doSharedQuerySearch('<c:out value="${formName}"/>')</c:set>
<form:form action="${formAction}" id="${formName}" name="${formName}"
	onsubmit="return ${onSubmitAction}"
>
<table>
	<tr>
		<td style="padding-right:5px;"><b>Keyword:</b></td>
		<td>
			<input type="text" id="${formName}Keywords" name="keywords"/>
		</td>
	</tr>
	<tr>
		<td style="padding-right:5px;" valign="top"><b>Search Fields:</b></td>
		<td>
			<select name="searchFields" multiple size="8">
				<option id="${formName}Name" value="name">Query Name</option>
				<option id="${formName}Desc" value="description">Query Description</option>
				<option id="${formName}Url" value="targetService.url">Target Service URL</option>
				<option id="${formName}Name" value="targetService.serviceMetadata.serviceDescription.name">Target Service Name</option>
				<option id="${formName}TName" value="targetClass.className">Target UML Class Name</option>
				<option id="${formName}FName" value="owner.person.firstName">Query Creator First Name</option>
				<option id="${formName}LName" value="owner.person.lastName">Query Creator Last Name</option>
				<option id="${formName}Email" value="owner.person.emailAddress">Query Creator Email</option>
			</select>
		</td>
	</tr>
	<tr>
		<td colspan="2">
			<input type="submit" id="${formName}Search" alt="Search value="Search"/>
		</td>
	</tr>
</table>

</form:form>
