<%@ include file="/WEB-INF/jsp/diagnostics/result_common.jsp" %>

<div class="row">
    <div class="label threeCols">
        Service Metadata:
    </div>
    <div id="metadataDetailDiv" class="threeCols">
        ${result.detail}
    </div>
    <div id="metadataImgDiv" class="threeCols">
        <tags:diagnosticStatusFlag status="${result.status}"/>
    </div>
</div>



 