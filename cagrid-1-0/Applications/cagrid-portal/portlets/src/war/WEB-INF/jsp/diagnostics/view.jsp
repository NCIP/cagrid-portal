<%@ include file="/WEB-INF/jsp/include/includes.jspf" %>
<%@ taglib prefix="tags" tagdir="/WEB-INF/tags" %>

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
        Service URL:&nbsp;
    </div>
    <div>
        <input id="${prefix}url" type="text" name="url" value="${diagnosticsBean.url}" size="50"/>
        <input id="${prefix}diagnoseBtn" type="button" value="Diagnose" onclick="javascript:${prefix}beginDiagnose(true)" />
        <tags:infoPopup id="${prefix}diagnosticsInfo" popup_text="Enter URL of caGrid service to run diagnostics on."/>
    </div>
</div>

<div id="${prefix}outerDiv">
    <div id="${prefix}errorMsg" class="errorMsg">
    </div>
    <br/>

    <div id="${prefix}headerDiv" style="visibility:hidden;font-style:italic;">
        <span id="${prefix}diagnosisLabel" style="font-weight:bold;">
            <!---->
        </span>

        <tags:image id="statusIndicator"
                    name="indicator.gif"
                    alt="Please wait"
                />
        <hr/>
    </div>

    <div id="${prefix}resultsDiv">
        <!---->
    </div>

    <br/>

    <div id="${prefix}disclaimerDiv" class="versionStamp" style="float:left;">
        <!---->
    </div>

</div>

<!--has to come after all the html-->
<script type="text/javascript">
    <!--record in memory the state of the portlet onLoad-->
    var cachedHTML = $("${prefix}outerDiv").innerHTML;
    var totalTests = 4;
    var counter = 0;
    $("${prefix}diagnoseBtn").enable();

    function ${prefix}beginDiagnose(validateUrl){

    <!--reset the portlet html-->
    $("${prefix}outerDiv").innerHTML = cachedHTML;

    <!--validate url. Exit on failure-->
    var url=$("${prefix}url").value.strip();

    if(validateUrl){
    if(!isValidURL(url)){
    $("${prefix}errorMsg").innerHTML="Invalid URL";
    $("${prefix}errorMsg").innerHTML+=" &nbsp; ";
    $("${prefix}errorMsg").innerHTML+='<input type="checkbox" onclick="${prefix}beginDiagnose(false)">';
    $("${prefix}errorMsg").innerHTML+=" Ignore Warning!";
    $("${prefix}errorMsg").innerHTML+="</input>";
    return false;
    }
    }

    $("${prefix}diagnoseBtn").disable();
    $("${prefix}diagnoseBtn").value='Running...';
    $("${prefix}diagnoseBtn").style.background='#38A6C1';

    $("${prefix}diagnosisLabel").innerHTML = 'Running Diagnostics';
    counter=0;
    $("${prefix}headerDiv").style.visibility='visible';


    dwr.engine.setErrorHandler(${prefix}failedDiagnosis);
    <!--setting timeout to 2 minutes-->
    DWREngine.setTimeout(120000);

    <!--start diagnostics-->
    doDiagnose("idxDiagnostic",idxDiagnostic,url);
    doDiagnose("portalStatusDiagnostic",portalStatusDiagnostic,url);
    doDiagnose("pingDiagnostic",pingDiagnostic,url);
    doDiagnose("metadataDiagnostic",metadataDiagnostic,url);
    }

    function ${prefix}failedDiagnosis(){
        $("${prefix}errorMsg").innerHTML="Encountered error running diagnosis. Please retry";
        ${prefix}resetBtns()
    }

    function doDiagnose(divName,JScript,url){
    JScript.diagnose(url,function(result){
    document.getElementById("${prefix}resultsDiv").innerHTML+="<div id='${prefix}" + divName + "'>" + result + "</div>";

    if(counter++>=totalTests-1)
    ${prefix}finishDiagnose();


    });
    }

    function ${prefix}finishDiagnose(){
        ${prefix}resetBtns();

        $("${prefix}disclaimerDiv").innerHTML="Index service results can be delayed up to 5 minutes.";
        $("${prefix}diagnosisLabel").innerHTML = 'Diagnostic Results';    


        document.getElementById("${prefix}resultsDiv").innerHTML+="<br/>";
        document.getElementById("${prefix}resultsDiv").innerHTML+="<hr/>";
        document.getElementById("${prefix}resultsDiv").innerHTML+="<div>";
        document.getElementById("${prefix}resultsDiv").innerHTML+="See <a href='http://www.cagrid.org/display/knowledgebase/Troubleshoot+Index+Service+Registration' target='_blank'>this guide</a> to troubleshoot potential problems";
        document.getElementById("${prefix}resultsDiv").innerHTML+="</div>";
    }

    function ${prefix}resetBtns(){
        $("statusIndicator").style.visibility='hidden';

        $("${prefix}diagnoseBtn").enable();
        $("${prefix}diagnoseBtn").style.background='#3876C1';
        $("${prefix}diagnoseBtn").value='Diagnose';
    }


</script>


