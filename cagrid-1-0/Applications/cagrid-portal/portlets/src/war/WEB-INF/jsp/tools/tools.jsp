<%@ include file="/WEB-INF/jsp/include/includes.jspf"%>

<script language="JavaScript">
function resize_iframe()
{
//resize the iframe according to the size of the

//window (all these should be on the same line)
document.getElementById("toolsView").height=document.body.offsetHeight - document.getElementById("toolsView").offsetTop-40;
}

// this will resize the iframe every
// time you change the size of the window.
window.onresize=resize_iframe;

</script>



<iframe
        id="toolsView"
        onload='resize_iframe()'
        src='<c:out value="${remoteView}"/>'
        scrolling="auto"
        frameborder="0"
        style="width:100%;min-height:250px;">
</iframe>

<script type="text/javascript">
    //resizes before iframe is loaded
resize_iframe();
</script>
