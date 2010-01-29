<%@tag %>
<%@ include file="/WEB-INF/jsp/include/includes.jspf" %>

<%--does NOT include YUI scripts for faster loading. But make sure these are available--%>
<%@attribute name="helpURL" required="true" %>

<c:set var="ns"><portlet:namespace/></c:set>
<c:set var="id" value="${ns}helpLink"/>

<span id="${ns}helpTipContainer" class="yui-skin-sam helpLink"></span>

 <span id="summaryHelpLink">
            <a id="${id}" href="${helpURL}" target="_blank">
                <tags:image name="help.gif"/>
            </a>
</span>

<script type="text/javascript">

        ${ns}catalogTip = new YAHOO.widget.Tooltip("helpTip", {
            context:"${id}",
            text:"Help documentation",
            container:"${ns}helpTipContainer",
            showDelay:100,
            hideDelay:100

        });
  
</script>