<%@ include file="/WEB-INF/jsp/include/servlet_includes.jsp" %>


<div class="yui-skin-sam">


<c:set var="ns" value="${namespace}"/>

<span id="${ns}saveNewRelatedItemButtonContainer"></span>&nbsp;<span id="${ns}cancelNewRelatedItemButtonContainer"></span>
<div id="${ns}newRelatedItemFormMsg"></div>
<form name="${ns}newRelatedItemForm">

<div class="row">
How does the selected <spring:message code="${targetRoleType.type}"/> relate to 
this <spring:message code="${sourceRoleType.type}"/>?<br/>
<label for="sourceRoleDescription">Source Description</label>
<textarea name="sourceRoleDescription" id="${ns}sourceRoleDescription" class="required"></textarea>
</div>

<div class="row">
How does this <spring:message code="${sourceRoleType.type}"/> relate to 
the selected <spring:message code="${targetRoleType.type}"/>?<br/>
<label for="targetRoleDescription">Target Description</label>
<textarea name="targetRoleDescription" id="${ns}targetRoleDescription" class="required"></textarea>
</div>

<div class="row">
Enter the name of the related catalog entry.<br/>
<label for="relatedEntryName">Related Entry</label>
<input type="text" name="relatedEntryName" id="${ns}relatedEntryName" style="width:50em;"/>

<input type="hidden" name="relatedEntryId" id="${ns}relatedEntryId" value="" class="required"/>
<input type="hidden" name="targetRoleType" id="${ns}targetRoleType" value="${targetRoleType.type}"/>
</div>
<div class="row">
<label>&nbsp;</label>
<div id="${ns}relatedEntryDiv"></div>
</div>
<div class="row">
<label>&nbsp;</label>
<div id="${ns}relatedEntrySearchMsg"></div>
</div>


</form>

</div>
