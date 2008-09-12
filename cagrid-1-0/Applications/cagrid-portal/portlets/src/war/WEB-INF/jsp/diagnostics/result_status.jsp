<%@ include file="/WEB-INF/jsp/diagnostics/result_common.jsp" %>


<div class="row">
    <div class="label threeCols">
        Status in Portal:
    </div>
    <div id="portalStatusDetailDiv" class="threeCols">
        ${result.message}
    </div>
    <div id="portalStatusImgDiv" class="threeCols">
        <%@ include file="statusFlag.jspf"%>
    </div>
</div>
 