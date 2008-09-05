<%@ include file="/WEB-INF/jsp/diagnostics/result_common.jsp" %>
<%@ taglib prefix="tags" tagdir="/WEB-INF/tags"%>

<div class="row">
      <div class="label threeCols">
          Index:
      </div>
      <div id="idxDetailDiv" class="threeCols">
         ${result.detail}
      </div>
      <div id="idxImgDiv" class="threeCols" >
      <img src='<c:url value="/images/diagnostic_passed_icon.jpg"/>'/> 
           
      </div>
  </div>
 