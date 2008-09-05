<%@ include file="/WEB-INF/jsp/diagnostics/result_common.jsp" %>


<div class="row">
    <div class="label threeCols">
        Status in Portal:
    </div>
    <div id="portalStatusDetailDiv" class="threeCols">
        ${result.detail}
    </div>
    <div id="portalStatusImgDiv" class="threeCols" >
        <img src='<c:url value="/images/diagnostic_passed_icon.jpg"/>'/>
        ${result.status}
    </div>
</div>
 