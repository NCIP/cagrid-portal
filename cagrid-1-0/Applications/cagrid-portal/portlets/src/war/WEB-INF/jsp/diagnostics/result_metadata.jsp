<%@ include file="/WEB-INF/jsp/diagnostics/result_common.jsp" %>

<div class="row">
        <div class="label threeCols">
            Service Metadata:
        </div>
        <div id="metadataDetailDiv" class="threeCols">
            ${result.detail}
        </div>
        <div id="metadataImgDiv" class="threeCols">
            <img src='<c:url value="/images/diagnostic_passed_icon.jpg"/>'/> 
            ${result.status}
        </div>
    </div>



 