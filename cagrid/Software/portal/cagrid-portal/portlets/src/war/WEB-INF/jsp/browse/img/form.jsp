<%@ include file="/WEB-INF/jsp/include/servlet_includes.jsp" %>

<c:set var="ns" value="${param.ns}"/>

<html>
<head>
<script type="text/javascript" src="<c:url value="/js/upload/ajaxfileupload.js"/>"></script>
<script type="text/javascript">
 
	function ${ns}upload()
	{
		jQuery.ajaxFileUpload
		(
			{
				url:'<c:url value="/img/put.html"/>',
				secureuri:false,
				fileElementId:'image',
				dataType: 'json',
				beforeSend:function()
				{
					jQuery("#loading").show();
				},
				complete:function()
				{
					jQuery("#loading").hide();
				},				
				success: function (data, status)
				{
					if(typeof(data.error) != 'undefined')
					{
						if(data.error != '')
						{
							alert(data.error);
						}else
						{
							alert(data.msg);
						}
					}else{
						${ns}displayImage(data.result);
					}
				},
				error: function (data, status, e)
				{
					alert(e);
				}
			}
		)
		
		return false;

	}


</script>
</head>
<body>
<p>
Select an image and click the Upload button. Only PNG and JPEG files are supported.
</p>
<form action="" method="POST" enctype="multipart/form-data">
<label for="image">Image</label>
<input id="image" type="file" name="image"/>
<img id="loading" alt="Loading" src="<c:url value="/js/upload/loading.gif"/>" style="display:none;">
<input type="button" value="Upload" onclick="${ns}upload();"/>
<br/>

</form>

</body>
</html>
