<%@ include file="/WEB-INF/jsp/include/includes.jspf" %>

<script type='text/javascript' src="<c:url value="/dwr/interface/CommentsManagerFacade.js"/>"></script>
<script type="text/javascript" src="<c:url value="/dwr/engine.js"/>"></script>
<script type="text/javascript" src="<c:url value="/dwr/util.js"/>"></script>

<c:set var="ns" value="${param.ns}"/>

<style type="text/css">
    div.row div.tight {
        margin-left: 2em;
    }

</style>
<form name="addCommentForm">
    <div>
        <div class="row">
            <label for="${ns}commentText">
                Comment:
            </label>
            <div class="tight value">
                <textarea id="${ns}commentText" rows="4" cols="50"/>
            </div>
        </div>

        <c:if test="${!empty portalUser}">
            <div class="row">
                <div class="label">
                    User:
                </div>
                <div class="value">
                    <c:out value="${portalUser}"/>

                </div>
            </div>
        </c:if>


        <div class="flow-buttons">
            <span id="cancelButton"><input type="button" value="Cancel" alt="Cancel" id="${ns}cancelBtn"></span>
            <span id="submitButtonContainer"><input type="button" value="Add Comments" alt="Add Comments" disabled="disabled"
                                                    id="${ns}addCommentBtn"></span>
        </div>

    </div>

</form>

<script type="text/javascript">
    jQuery(document).ready(function() {

    jQuery('#${ns}commentText').bind("keyup", function(e){
        if(jQuery('#${ns}commentText').val().length>0)
            jQuery('#${ns}addCommentBtn').attr("disabled","");
        else
            jQuery('#${ns}addCommentBtn').attr("disabled","disabled");
    });
    });

    jQuery(
            function() {
                jQuery('#${ns}addCommentBtn').click(function(){
                    ${ns}saveComment();
                });

                jQuery('#${ns}cancelBtn').click(
                        function(){
                         	Liferay.Popup.close(${ns}addCommentDialog);                   
                        }
                        );
                
            }
            );

    function ${ns}saveComment() {

        var commentText = jQuery("#${ns}commentText").val();


        CommentsManagerFacade.addComment(commentText,
        {
            callback:function(response) {
                Liferay.Popup.close(${ns}addCommentDialog);
                ${ns}loadComments();
            },
            errorHandler:function(errorString, exception) {
                alert("Error rendering role types: " + errorString);
            }
        });

    }</script>