<%@ include file="/WEB-INF/jsp/diagnostics/result_common.jsp" %>

<div class="row">
    <div class="label threeCols">
        Ping Service:
    </div>
    <div id="pingDetailDiv" class="threeCols">
        Pinging Service
    </div>
    <div id="pingImgDiv" class="threeCols">
        <tags:diagnosticStatusFlag status="${result.status}"/>
    </div>
</div>


 