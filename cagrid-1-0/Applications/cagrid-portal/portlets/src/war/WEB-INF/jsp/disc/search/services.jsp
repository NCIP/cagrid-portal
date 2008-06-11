<%@ include file="/WEB-INF/jsp/disc/search/header.jspf" %>
<%@ taglib prefix="tags" tagdir="/WEB-INF/tags" %>

<link type="text/css" href="<c:url value="/css/service-discovery.css"/>"/>
<style type="text/css">
    <!--
    <%@ include file="/css/service-discovery.css" %>
    -->
</style>

<script type="text/javascript" src='<c:url value="/dwr/engine.js"/>'></script>
<script type="text/javascript" src='<c:url value="/dwr/util.js"/>'></script>

<script type="text/javascript" src='<c:url value="/js/scriptaculous/prototype.js"/>'></script>
<script type="text/javascript" src='<c:url value="/js/scriptaculous/effects.js"/>'></script>
<script type="text/javascript" src='<c:url value="/js/scriptaculous/controls.js"/>'></script>

<script type="text/javascript" src='<c:url value="/dwr/interface/EVSAutomcompleter.js"/>'></script>
<script type="text/javascript" src='<c:url value="/js/autocomplete.js"/>'></script>


<portlet:actionURL var="formAction">
    <portlet:param name="operation" value="searchServices"/>
</portlet:actionURL>

<table>
<tr>
<td>

<div id="<portlet:namespace/>advancedDiv" style="display:none">
<c:set var="advancedDiscoveryFormName"><portlet:namespace/>advancedDiscoveryForm</c:set>
<c:set var="onSubmitAction"><portlet:namespace/>doDiscoverySearch('<c:out
        value="${advancedDiscoveryFormName}"/>')</c:set>
<form:form action="${formAction}" name="${advancedDiscoveryFormName}"
           onsubmit="return ${onSubmitAction}"
        >
<table>
<tr>
    <td valign="top" style="padding-right:5px; text-align:right"><b>Keyword:</b></td>
    <td valign="top">
        <input type="text" name="keywords" class="searchField"/>
    </td>
</tr>
<tr>
<td valign="top" style="padding-right:5px; align:right"><b>Search Fields:</b></td>
<td valign="top">
<select name="searchFields" multiple size="8">
<option value="url">URL</option>
<option value="serviceMetadata.serviceDescription.name">Name</option>
<option value="serviceMetadata.serviceDescription.description">Description</option>
<optgroup label="&nbsp;&nbsp; Semantic Metadata">
    <option value="serviceMetadata.serviceDescription.semanticMetadata.conceptCode">&nbsp;&nbsp;&nbsp;&nbsp;
        Concept Code
    </option>
    <option value="serviceMetadata.serviceDescription.semanticMetadata.conceptCode">&nbsp;&nbsp;&nbsp;&nbsp;
        Concept Definition
    </option>
    <option value="serviceMetadata.serviceDescription.semanticMetadata.conceptCode">&nbsp;&nbsp;&nbsp;&nbsp;
        Concept Name
    </option>
</optgroup>
<optgroup label="&nbsp;&nbsp; Service Contexts">
    <option value="serviceMetadata.serviceDescription.serviceContextCollection.name">&nbsp;&nbsp;&nbsp;&nbsp;
        Name
    </option>
    <option value="serviceMetadata.serviceDescription.serviceContextCollection.description">&nbsp;&nbsp;&nbsp;&nbsp;
        Description
    </option>
</optgroup>
<optgroup label="&nbsp;&nbsp;&nbsp;&nbsp; Context Properties">
    <option value="serviceMetadata.serviceDescription.serviceContextCollection.contextPropertyCollection.name">
        &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp; QName
    </option>
    <option value="serviceMetadata.serviceDescription.serviceContextCollection.contextPropertyCollection.description">
        &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp; Description
    </option>
</optgroup>
<optgroup label="&nbsp;&nbsp;&nbsp;&nbsp; Operations">
    <option value="serviceMetadata.serviceDescription.serviceContextCollection.operationCollection.name">
        &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp; Name
    </option>
    <option value="serviceMetadata.serviceDescription.serviceContextCollection.operationCollection.description">
        &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp; Description
    </option>
</optgroup>
<optgroup label="&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp; Semantic Metadata">
    <option value="serviceMetadata.serviceDescription.serviceContextCollection.operationCollection.semanticMetadata.conceptCode">
        &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp; Concept Code
    </option>
    <option value="serviceMetadata.serviceDescription.serviceContextCollection.operationCollection.semanticMetadata.conceptDefinition">
        &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp; Concept Definition
    </option>
    <option value="serviceMetadata.serviceDescription.serviceContextCollection.operationCollection.semanticMetadata.conceptName">
        &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp; Concept Name
    </option>
</optgroup>
<optgroup label="&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp; Input Parameters">
    <option value="serviceMetadata.serviceDescription.serviceContextCollection.operationCollection.inputParameterCollection.QName">
        &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp; QName
    </option>
</optgroup>
<optgroup label="&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp; UML Class">
    <option value="serviceMetadata.serviceDescription.serviceContextCollection.operationCollection.inputParameterCollection.UMLClass.className">
        &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp; Class Name
    </option>
    <option value="serviceMetadata.serviceDescription.serviceContextCollection.operationCollection.inputParameterCollection.UMLClass.packageName">
        &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp; Package Name
    </option>
    <option value="serviceMetadata.serviceDescription.serviceContextCollection.operationCollection.inputParameterCollection.UMLClass.projectName">
        &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp; Project Name
    </option>
</optgroup>
<optgroup label="&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp; Semantic Metadata">
    <option value="serviceMetadata.serviceDescription.serviceContextCollection.operationCollection.inputParameterCollection.UMLClass.semanticMetadata.conceptCode">
        &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp; Concept Code
    </option>
    <option value="serviceMetadata.serviceDescription.serviceContextCollection.operationCollection.inputParameterCollection.UMLClass.semanticMetadata.conceptDefinition">
        &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp; Concept Definition
    </option>
    <option value="serviceMetadata.serviceDescription.serviceContextCollection.operationCollection.inputParameterCollection.UMLClass.semanticMetadata.conceptName">
        &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp; Concept Name
    </option>
</optgroup>

<optgroup label="&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp; Attributes">
    <option value="serviceMetadata.serviceDescription.serviceContextCollection.operationCollection.inputParameterCollection.UMLClass.umlAttributeCollection.publicID">
        &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp; caDSR Public ID (CDE)
    </option>
    <option value="serviceMetadata.serviceDescription.serviceContextCollection.operationCollection.inputParameterCollection.UMLClass.umlAttributeCollection.name">
        &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp; Name
    </option>
    <option value="serviceMetadata.serviceDescription.serviceContextCollection.operationCollection.inputParameterCollection.UMLClass.umlAttributeCollection.description">
        &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp; Description
    </option>
    <option value="serviceMetadata.serviceDescription.serviceContextCollection.operationCollection.inputParameterCollection.UMLClass.umlAttributeCollection.dataTypeName">
        &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp; Data Type Name
    </option>
</optgroup>
<optgroup label="&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp; Semantic Metadata">
    <option value="serviceMetadata.serviceDescription.serviceContextCollection.operationCollection.inputParameterCollection.UMLClass.umlAttributeCollection.semanticMetadata.conceptCode">
        &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp; Concept Code
    </option>
    <option value="serviceMetadata.serviceDescription.serviceContextCollection.operationCollection.inputParameterCollection.UMLClass.umlAttributeCollection.semanticMetadata.conceptDefinition">
        &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp; Concept Definition
    </option>
    <option value="serviceMetadata.serviceDescription.serviceContextCollection.operationCollection.inputParameterCollection.UMLClass.umlAttributeCollection.semanticMetadata.conceptName">
        &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp; Concept Name
    </option>
</optgroup>

<optgroup label="&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp; Output">
    <option value="serviceMetadata.serviceDescription.serviceContextCollection.operationCollection.output.QName">
        &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp; QName
    </option>
</optgroup>
<optgroup label="&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp; UML Class">
    <option value="serviceMetadata.serviceDescription.serviceContextCollection.operationCollection.output.UMLClass.className">
        &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp; Class Name
    </option>
    <option value="serviceMetadata.serviceDescription.serviceContextCollection.operationCollection.output.UMLClass.packageName">
        &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp; Package Name
    </option>
    <option value="serviceMetadata.serviceDescription.serviceContextCollection.operationCollection.output.UMLClass.projectName">
        &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp; Project Name
    </option>
</optgroup>
<optgroup label="&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp; Semantic Metadata">
    <option value="serviceMetadata.serviceDescription.serviceContextCollection.operationCollection.output.UMLClass.semanticMetadata.conceptCode">
        &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp; Concept Code
    </option>
    <option value="serviceMetadata.serviceDescription.serviceContextCollection.operationCollection.output.UMLClass.semanticMetadata.conceptDefinition">
        &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp; Concept Definition
    </option>
    <option value="serviceMetadata.serviceDescription.serviceContextCollection.operationCollection.output.UMLClass.semanticMetadata.conceptName">
        &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp; Concept Name
    </option>
</optgroup>

<optgroup label="&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp; Attributes">
    <option value="serviceMetadata.serviceDescription.serviceContextCollection.operationCollection.output.UMLClass.umlAttributeCollection.publicID">
        &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp; caDSR Public ID (CDE)
    </option>
    <option value="serviceMetadata.serviceDescription.serviceContextCollection.operationCollection.output.UMLClass.umlAttributeCollection.name">
        &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp; Name
    </option>
    <option value="serviceMetadata.serviceDescription.serviceContextCollection.operationCollection.output.UMLClass.umlAttributeCollection.description">
        &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp; Description
    </option>
    <option value="serviceMetadata.serviceDescription.serviceContextCollection.operationCollection.output.UMLClass.umlAttributeCollection.dataTypeName">
        &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp; Data Type Name
    </option>
</optgroup>
<optgroup label="&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp; Semantic Metadata">
    <option value="serviceMetadata.serviceDescription.serviceContextCollection.operationCollection.output.UMLClass.umlAttributeCollection.semanticMetadata.conceptCode">
        &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp; Concept Code
    </option>
    <option value="serviceMetadata.serviceDescription.serviceContextCollection.operationCollection.output.UMLClass.umlAttributeCollection.semanticMetadata.conceptDefinition">
        &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp; Concept Definition
    </option>
    <option value="serviceMetadata.serviceDescription.serviceContextCollection.operationCollection.output.UMLClass.umlAttributeCollection.semanticMetadata.conceptName">
        &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp; Concept Name
    </option>
</optgroup>


<optgroup label="&nbsp;&nbsp; Domain Model">
    <option value="domainModel.projectLongName">&nbsp;&nbsp;&nbsp;&nbsp; Project Long Name</option>
    <option value="domainModel.projectShortName">&nbsp;&nbsp;&nbsp;&nbsp; Project Short Name
    </option>
</optgroup>
<optgroup label="&nbsp;&nbsp;&nbsp;&nbsp; UML Classes">
    <option value="domainModel.classes.className">&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
        Class Name
    </option>
    <option value="domainModel.classes.packageName">&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
        Package Name
    </option>
    <option value="domainModel.classes.projectName">&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
        Project Name
    </option>
</optgroup>
<optgroup label="&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp; Semantic Metadata">
    <option value="domainModel.classes.semanticMetadata.conceptCode">&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
        Concept Code
    </option>
    <option value="domainModel.classes.semanticMetadata.conceptDefinition">&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
        Concept Definition
    </option>
    <option value="domainModel.classes.semanticMetadata.conceptName">&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
        Concept Name
    </option>
</optgroup>
<optgroup label="&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp; Attributes">
    <option value="domainModel.classes.umlAttributeCollection.publicID">&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
        caDSR Public ID (CDE)
    </option>
    <option value="domainModel.classes.umlAttributeCollection.name">&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
        Name
    </option>
    <option value="domainModel.classes.umlAttributeCollection.description">&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
        Description
    </option>
    <option value="domainModel.classes.umlAttributeCollection.dataTypeName">&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
        Data Type Name
    </option>
</optgroup>
<optgroup label="&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp; Semantic Metadata">
    <option value="domainModel.classes.umlAttributeCollection.semanticMetadata.conceptCode">&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
        Concept Code
    </option>
    <option value="domainModel.classes.umlAttributeCollection.semanticMetadata.conceptDefinition">
        &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp; Concept Definition
    </option>
    <option value="domainModel.classes.umlAttributeCollection.semanticMetadata.conceptName">&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
        Concept Name
    </option>
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
</div>

<div id="<portlet:namespace/>simpleDiv">

    <c:set var="simpleDiscoveryFormName"><portlet:namespace/>simpleDiscoveryForm</c:set>
    <c:set var="onSubmitAction"><portlet:namespace/>doDiscoverySearch('<c:out
            value="${simpleDiscoveryFormName}"/>')</c:set>
    <form:form action="${formAction}" name="${simpleDiscoveryFormName}"
               onsubmit="return ${onSubmitAction}">

        <table style="cellpadding:5px;" valign="top">
            <tr>
                <td valign="top" style="padding-right:5px; text-align:right"><b>Keyword:</b></td>
                <td valign="top">
                    <input type="text" name="keywords" class="searchField"/>
                </td>
            </tr>
            <tr>
                <td valign="top" style="padding-right:5px; text-align:right"><b>Search Fields:</b></td>
                <td valign="top">
                    <select name="searchFields" multiple size="8">
                        <option value="publicID">caDSR Public ID (CDE)</option>
                        <option value="conceptCode">Concept Code</option>
                        <option value="conceptName">Concept Name</option>
                        <option value="serviceUrl">URL</option>
                        <option value="serviceName">Name</option>
                        <option value="serviceDescription">Description</option>
                        <option value="inputOutput">Input/Output Class Names</option>
                        <option value="operationNames">Operation Names</option>
                        <option value="domainModel">Domain Model Class Names</option>
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
</div>

<div id="<portlet:namespace/>evsDiv" style="display:none">

<c:set var="evsDiscoveryFormName"><portlet:namespace/>evsDiscoveryForm</c:set>
<c:set var="onSubmitAction"><portlet:namespace/>doDiscoverySearch('<c:out
        value="${evsDiscoveryFormName}"/>')</c:set>

<script type="text/javascript">
    function <portlet:namespace/>submitEVSForm(){
    $("${evsDiscoveryFormName}").action ="${formAction}";
    $("${evsDiscoveryFormName}").submit();
    var form = document.evsDiscoveryForm;
    }

    function <portlet:namespace/>updateList(autocompleter, token) {
    EVSAutomcompleter.autoCompleteConcept(token, function(data) {
    autocompleter.setChoices(data)
    });
    }

    function <portlet:namespace/>nameValueSelector(tag){
    return tag.name;
    }
</script>
<form:form id="${evsDiscoveryFormName}" name="${evsDiscoveryFormName}"
           onsubmit="return ${onSubmitAction}">

    <table style="cellpadding:5px;" valign="top">
        <tr>
            <td valign="top" style="padding-right:5px; text-align:right"><b>Keyword:</b></td>
                         <td valign="top">
                <input id="evsConcept" type="text" class="searchField auto_complete"/>

                <tags:infoPopup id="evs"
                                popup_text="NCI Thesaurus based service discovery.Enter concept name in the auto-completer text field. The auto-completer will load relevant concepts first."/>
                <tags:image id="indicator1"
                            name="indicator.gif"
                            alt="Please wait"
                            cssStyle="display:none;"
                        />
                  

                <input type="hidden" name="keywords" id="keywords"/>

                <div id="evsConceptList" class="auto_complete"></div>
                <div id="evsConceptDefinition" style="display:none;">
                    <div class="row">
                        <div>
                            <b>Definition:</b>
                            <span id="evsConceptDefinitionValue">
                                <!--cannot be empty in IE-->
                                </span>
                        </div>
                    </div>
                    <div class="row">
                        <div>
                            <b>Concept Code:</b>
                            <span id="evsConceptCodeValue">
                                <!--cannot be empty in IE-->
                                </span>
                        </div>
                    </div>
                </div>

                <div class="row" id="evsConceptResultDiv"
                     style="font-size:12px;display:none;">
                    <div class="label">
                        Found&nbsp;
                            <span id="discoveryResultDiv">
                                <!--cannot be empty in IE-->
                                </span>
                        &nbsp;services
                    </div>
                </div>

                <script type="text/javascript">
                    new Autocompleter.DWR('evsConcept', 'evsConceptList', <portlet:namespace/>updateList,{
                    valueSelector:
                    <portlet:namespace/>nameValueSelector, partialChars: 2, indicator: 'indicator1',
                    afterUpdateElement: function(inputField, selectedItem,selectedTag){
                    $('${evsDiscoveryFormName}').keywords.value= selectedTag.code;
                    $('evsConceptDefinition').style.display= "";
                    $('evsConceptDefinitionValue').innerHTML= selectedTag.definitation;
                    $('keywords').value=selectedTag.code;
                    $('evsConceptCodeValue').innerHTML= selectedTag.code;
                    $("discoveryResultDiv").innerHTML = '<img src="<c:url value="/images/indicator.gif"/>"/>';
                    EVSAutomcompleter.resultCount(selectedTag.code,<portlet:namespace/>handleResultCount);
                    }});

                    function <portlet:namespace/>handleResultCount(str){
                    $("evsConceptResultDiv").style.display="";
                    var _innerHtml = "";
                    if(str>0){
                    _innerHtml=_innerHtml +"<a href='javascript:<portlet:namespace/>submitEVSForm()'>";
                    }
                    _innerHtml=_innerHtml+ str+" services";

                    if(str>0){
                    _innerHtml=_innerHtml + "</a>";
                    }
                    $("discoveryResultDiv").innerHTML=_innerHtml;
                    }

                </script>
            </td>
        </tr>
    </table>
    <input type="hidden" name="discoveryType" value="CONCEPT"/>
</form:form>
</div>
</td>

<td valign="top">
    <form>
        &nbsp;&nbsp;&nbsp;&nbsp;
        <input name="level" value="simple" type="radio" checked
               onclick="jQuery('#<portlet:namespace/>evsDiv').hide();jQuery('#<portlet:namespace/>advancedDiv').hide();jQuery('#<portlet:namespace/>simpleDiv').show();"/>Simple&nbsp;
        <input name="level" value="advanced" type="radio"
               onclick="jQuery('#<portlet:namespace/>simpleDiv').hide();jQuery(document).find('#<portlet:namespace/>evsDiv').hide();jQuery('#<portlet:namespace/>advancedDiv').show();"/>Advanced&nbsp;
        <input name="level" value="evs" type="radio"
               onclick="jQuery('#<portlet:namespace/>simpleDiv').hide();jQuery('#<portlet:namespace/>advancedDiv').hide();jQuery('#<portlet:namespace/>evsDiv').show();"/>EVS&nbsp;
    </form>
</td>
</tr>
</table>