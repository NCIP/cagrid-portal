<%@ include file="/WEB-INF/jsp/diagnostics/result_common.jsp" %>

<div class="row">
    <div class="label threeCols">
        Ping Service:
    </div>
    <div id="pingDetailDiv" class="threeCols">
        ${result.message}
    </div>
    <div id="pingImgDiv" class="threeCols">
        <%@ include file="statusFlag.jspf"%>
    </div>
</div>


 