<%@ include file="/WEB-INF/jsp/diagnostics/result_common.jsp" %>

<div class="row">
    <div class="label threeCols">
        Index:
    </div>
    <div id="idxDetailDiv" class="threeCols">
          ${result.message}
    </div>
    <div id="idxImgDiv" class="threeCols">
        <%@ include file="statusFlag.jspf"%>
    </div>
</div>
