<%@ include file="/WEB-INF/jsp/include/includes.jspf" %>
<%@ taglib prefix="tags" tagdir="/WEB-INF/tags"%>

<script type="text/javascript" src='<c:url value="/js/script.js"/>'></script>

<script type="text/javascript" src='<c:url value="/js/scriptaculous/prototype.js"/>'></script>
<script type="text/javascript" src='<c:url value="/dwr/engine.js"/>'></script>
<script type="text/javascript" src='<c:url value="/dwr/util.js"/>'></script>
<script type="text/javascript" src='<c:url value="/dwr/interface/portalStatusDiagnostic.js"/>'></script>
<script type="text/javascript" src='<c:url value="/dwr/interface/pingDiagnostic.js"/>'></script>
<script type="text/javascript" src='<c:url value="/dwr/interface/idxDiagnostic.js"/>'></script>
<script type="text/javascript" src='<c:url value="/dwr/interface/metadataDiagnostic.js"/>'></script>

 <c:set var="prefix"><portlet:namespace/></c:set>

<div class="row">
    <div class="label">
        URL:&nbsp;
    </div>
    <div>
        <input id="${prefix}url" type="text" name="url" size="50"/>
        <input  type="button" value="Diagnose" onclick="javascript:${prefix}beginDiagnose()"/>
    </div>
</div>

<div id="${prefix}outerDiv">
    <div id="${prefix}errorMsg" class="errorMsg">
    </div>
    <br/>

    <div id="${prefix}headerDiv" style="visibility:hidden;">
        <b>Diagnostics</b>
        <tags:image   id="statusIndicator"
                      name="indicator.gif"
                      alt="Please wait"
            />
        <hr/>
    </div>

    <div id="${prefix}portalStatusDiagnostic" class="hidden"></div>
    <div id="${prefix}pingDiagnostic" class="hidden"></div>
    <div id="${prefix}metadataDiagnostic" class="hidden"></div>
    <div id="${prefix}idxDiagnostic" class="hidden"></div>

</div>

<!--has to come after all the html-->
<script type="text/javascript">
    <!--record in memory the state of the portlet onLoad-->
    var _html = $("${prefix}outerDiv").innerHTML;

    function ${prefix}beginDiagnose(){
    <!--reset the portlet html-->
    $("${prefix}outerDiv").innerHTML = _html;

    <!--validate url. Exit on failure-->
    var url=$("${prefix}url").value.strip();
    if(!isValidURL(url)){
    $("${prefix}errorMsg").innerHTML="Invalid URL";
    return false;
    }

    $("${prefix}headerDiv").style.visibility='visible';


    <!--start diagnostics-->
    doDiagnose("portalStatusDiagnostic",portalStatusDiagnostic,url);
    doDiagnose("pingDiagnostic",pingDiagnostic,url);
    doDiagnose("metadataDiagnostic",metadataDiagnostic,url);

    idxDiagnostic.diagnose(url, function(result){
        var _div =  $("${prefix}idxDiagnostic");
        _div.innerHTML =  result;
        _div.style.visibility='visible';
        new Effect.SlideDown(_div);
        $("statusIndicator").style.visibility='hidden';
     });

    }


    function doDiagnose(divName,JScript,url){
    JScript.diagnose(url, function(result){
     
            var _div = $("${prefix}"+ divName);
        _div.innerHTML =  result;
        new Effect.SlideDown(_div);
    <!--is causing flickering-->
    <!--_div.style.visibility =  'visible';-->
    
    });
    }


</script>


