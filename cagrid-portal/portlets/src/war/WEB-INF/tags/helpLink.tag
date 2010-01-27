<%@tag %>
<%@ include file="/WEB-INF/jsp/include/includes.jspf" %>
<tags:yui-minimum/>

<link rel="stylesheet" type="text/css" href="<c:url value="/js/yui/container/assets/skins/sam/container.css"/>"/>
<script type="text/javascript" src="<c:url value="/js/yui/container/container-min.js"/>"></script>

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
    jQuery(document).ready(function() {

        ${ns}catalogTip = new YAHOO.widget.Tooltip("helpTip", {
            context:"${id}",
            text:"Help documentation",
            container:"${ns}helpTipContainer",
            showDelay:100,
            hideDelay:100

        });
    });
</script>