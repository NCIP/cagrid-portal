<%@tag %>
<%@ include file="/WEB-INF/jsp/include/includes.jspf" %>


<%@attribute name="popupContent" required="true" %>
<%@attribute name="label" required="true" %>
<%@attribute name="id" required="true" %>

<span id="${id}-popupContainer" class="yui-skin-sam">
</span>

 <a id="${id}-popupContext" class="infoPopupLink">
     ${label}
</a>


<script type="text/javascript">
            var catalogTip = new YAHOO.widget.Tooltip("popupTip", {
            context:"${id}-popupContext",
            text:"${popupContent}",
            container:"${id}-popupContainer"


        });

</script>
