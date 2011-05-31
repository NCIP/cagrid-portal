<%@ include file="/WEB-INF/jsp/include/includes.jspf"%>

<script language="JavaScript">
    function resize_iframe()
    {
    //resize the iframe according to the size of the

    var browser = navigator.appName;
    var version = navigator.appVersion;
    var version1 = version.substring(22,23);

    if (browser== "Microsoft Internet Explorer" && version1=="6")
    {
        document.all['toolsView'].height="400px";
     }

    else{
        document.getElementById("toolsView").height=document.body.offsetHeight - document.getElementById("toolsView").offsetTop-40;
    }

    }

    // this will resize the iframe every
    // time you change the size of the window.
    window.onresize=resize_iframe;

</script>



<iframe title="caBIG Tools"
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
