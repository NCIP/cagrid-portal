<%@ include file="/WEB-INF/jsp/include/servlet_includes.jsp" %>

<div>
    <div>
        &nbsp;<a href="<c:url value="/export/query_results_table.xls?instanceId=${instance.id}"/>">Export to Excel</a>
        &nbsp;<a href="<c:url value="/export/query_results_table.xml?instanceId=${instance.id}"/>">Export to XML</a>
        &nbsp;<a onclick="deleteQueryInstance(${instance.id})">Delete</a>
    </div>
   
   <div id="${instance.id}resultTableContainer" style="overflow:auto; height:300px; width:900px;">
   		<div id="${instance.id}pageCtrlTop"></div>
   		<div id="${instance.id}resultTable"></div>
   		<div id="${instance.id}pageCtrlBottom"></div>
   </div>
   
</div>


<script>



</script>