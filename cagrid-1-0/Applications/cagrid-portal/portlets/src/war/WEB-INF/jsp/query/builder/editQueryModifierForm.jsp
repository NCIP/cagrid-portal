<%@ include file="/WEB-INF/jsp/include/includes.jspf"%>
<%@ include file="/WEB-INF/jsp/query/tabs.jspf" %>

<c:set var="objectInputId"><portlet:namespace/>objectInput</c:set>
<c:set var="countOnlyInputId"><portlet:namespace/>countOnlyInput</c:set>
<c:set var="distinctAttributeInputId"><portlet:namespace/>distinctAttributeInput</c:set>
<c:set var="selectedAttributesInputId"><portlet:namespace/>selectedAttributesInput</c:set>

<script type="text/javascript">
	//<![CDATA[

	function <portlet:namespace/>doQueryModifierOp(editOp){
		var form = document.<portlet:namespace/>editQueryModifierForm;
		var somethingSelected = true;
		if('update' == editOp){
			var numSelected = 0;
			jQuery("#<portlet:namespace/>selectedAttributesDiv").find("input").each(function(){
				if(this.checked){
					numSelected++;
				}
			});
			var distinctAttributeInputEl = document.getElementById('<c:out value="${distinctAttributeInputId}"/>');
			var selectedAttributesInputEl = document.getElementById('<c:out value="${selectedAttributesInputId}"/>');
			if(distinctAttributeInputEl.checked && numSelected == 0){
				somethingSelected = false;
				alert("Please select an attribute.");
			}else if(selectedAttributesInputEl.checked && numSelected == 0){
				somethingSelected = false;
				alert("Please select at least one attribute.");
			}
		}
		if(somethingSelected){
			form.editOperation.value = editOp;
			form.submit();
		}
	}
	var <portlet:namespace/>selectedAttributesMap = {
		<c:set var="numSelectedAtts" value="${fn:length(editQueryModifierCommand.selectedAttributes)}"/>
		<c:forEach var="att" items="${editQueryModifierCommand.selectedAttributes}" varStatus="status">
			<c:out value="${att}"/>: true<c:if test="${status.count < numSelectedAtts}">,</c:if>
		</c:forEach>
	};
	var <portlet:namespace/>availableAttributes = new Array();
	<c:forEach var="att" items="${availableAttributes}">
		<portlet:namespace/>availableAttributes.push('<c:out value="${att}"/>');
	</c:forEach>

	function <portlet:namespace/>addSelectedAttributesInputElements(inputType){
		for(var i = 0; i < <portlet:namespace/>availableAttributes.length; i++){
			var attName = <portlet:namespace/>availableAttributes[i];
			var inputEl = "<input type='" + inputType + "' name='selectedAttributes' value='" + attName + "' ";
			if(<portlet:namespace/>selectedAttributesMap[attName]){
				inputEl += " checked ";
			}
			inputEl += "/>" + attName + "<br/>";
			jQuery("#<portlet:namespace/>selectedAttributesDiv").append(inputEl);
		}
	}


	jQuery(document).ready(function(){

	 	jQuery("#<c:out value="${objectInputId}"/>").click(function(){
   			jQuery("#<portlet:namespace/>selectedAttributesDiv").html("");
        });
        jQuery("#<c:out value="${countOnlyInputId}"/>").click(function(){
   			jQuery("#<portlet:namespace/>selectedAttributesDiv").html("");
        });
        jQuery("#<c:out value="${distinctAttributeInputId}"/>").click(function(){
       		jQuery("#<portlet:namespace/>selectedAttributesDiv").html("");
			<portlet:namespace/>addSelectedAttributesInputElements("radio");
        });
        jQuery("#<c:out value="${selectedAttributesInputId}"/>").click(function(){
        	jQuery("#<portlet:namespace/>selectedAttributesDiv").html("");
        	<portlet:namespace/>addSelectedAttributesInputElements("checkbox");
        });

        <c:set var="modifierType"><c:out value="${editQueryModifierCommand.modifierType}"/></c:set>
        <c:choose>
        	<c:when test="${'DISTINCT_ATTRIBUTE' eq modifierType}">
        		<portlet:namespace/>addSelectedAttributesInputElements("radio");
        	</c:when>
        	<c:when test="${'SELECTED_ATTRIBUTES' eq modifierType}">
        		<portlet:namespace/>addSelectedAttributesInputElements("checkbox");
        	</c:when>
        </c:choose>
    });

	//]]>
</script>

<portlet:actionURL var="editQueryModifierFormAction"/>
<c:set var="editQueryModifierFormName"><portlet:namespace/>editQueryModifierForm</c:set>
<form:form commandName="editQueryModifierCommand" name="${editQueryModifierFormName}" action="${editQueryModifierFormAction}">

<br/>
<form:radiobutton id="${objectInputId}" value="OBJECT" path="modifierType"/>Object&nbsp;&nbsp;
<form:radiobutton id="${countOnlyInputId}" value="COUNT_ONLY" path="modifierType"/>Count Only&nbsp;&nbsp;
<form:radiobutton id="${distinctAttributeInputId}" value="DISTINCT_ATTRIBUTE" path="modifierType"/>Distinct Attribute&nbsp;&nbsp;
<form:radiobutton id="${selectedAttributesInputId}" value="SELECTED_ATTRIBUTES" path="modifierType"/>Selected Attributes&nbsp;&nbsp;
<br/>
<br/>

<input type="button" value="Update" onclick="<portlet:namespace/>doQueryModifierOp('update')"/>
<input type="button" value="Cancel" onclick="<portlet:namespace/>doQueryModifierOp('cancel')"/>
<input type="hidden" name="operation" value="updateQueryModifier"/>
<input type="hidden" name="editOperation" value=""/>
<br/>

<div id="<portlet:namespace/>selectedAttributesDiv" style="border:3px">
</div>

</form:form>