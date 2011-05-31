<%@tag %>
<%@ include file="/WEB-INF/jsp/include/includes.jspf" %>

<%--does NOT include YUI scripts for faster loading. But make sure these are available--%>
<%@attribute name="helpURL" required="false" %>
<%@attribute name="id_prefix" required="false" %>
<%--alternate context element--%>
<%@attribute name="context" required="false" %>

<c:if test="${empty id_prefix}">
    <c:set var="id_prefix"><portlet:namespace/></c:set>
</c:if>


<span id="${id_prefix}helpTipContainer" class="yui-skin-sam helpLink"></span>


<c:if test="${empty context}">
 <span id="summaryHelpLink">
            <a id="${id_prefix}helpLink" href="${helpURL}" target="_blank">
                <tags:image name="help.gif" alt="Help Link"/>
            </a>
</span>
    <c:set var="context">${id_prefix}helpLink</c:set>
</c:if>

<script type="text/javascript">

    var ${id_prefix}catalogTip = new YAHOO.widget.Tooltip("helpTip", {
        context:"${context}",
        text:"Help documentation",
        container:"${id_prefix}helpTipContainer",
        showDelay:100,
        hideDelay:100

    });

</script>