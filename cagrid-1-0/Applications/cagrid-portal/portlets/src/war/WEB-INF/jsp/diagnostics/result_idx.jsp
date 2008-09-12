<%@ include file="/WEB-INF/jsp/diagnostics/result_common.jsp" %>

<div class="row">
    <div class="label threeCols">
        Index:
    </div>
    <div id="idxDetailDiv" class="threeCols">
        ${result.detail}
    </div>
    <div id="idxImgDiv" class="threeCols">
        <tags:diagnosticStatusFlag status="${result.status}"/>
    </div>
</div>
