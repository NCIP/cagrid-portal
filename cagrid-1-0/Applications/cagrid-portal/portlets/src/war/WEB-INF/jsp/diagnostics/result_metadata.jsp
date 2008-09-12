<%@ include file="/WEB-INF/jsp/diagnostics/result_common.jsp" %>

<div class="row">
    <div class="label threeCols">
        Service Metadata:
    </div>
    <div id="metadataDetailDiv" class="threeCols">
        ${result.message}
    </div>
    <div id="metadataImgDiv" class="threeCols">
        <%@ include file="statusFlag.jspf"%>
    </div>
</div>



 