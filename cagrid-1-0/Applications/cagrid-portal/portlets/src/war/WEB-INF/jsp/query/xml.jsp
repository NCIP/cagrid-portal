<%@ include file="/WEB-INF/jsp/include.jsp"%>
<%@ include file="/WEB-INF/jsp/query/tabs.jspf" %>

<script type="text/javascript"><!--
/*
 * resizehandle.js (c) Fil 2007, plugin pour jQuery ecrit
 * a partir du fichier resize.js du projet DotClear
 * (c) 2005 Nicolas Martin & Olivier Meunier and contributors
 */
jQuery.fn.resizehandle = function() {
  return this.each(function() {
    var me = jQuery(this);
    me.after(
      jQuery('<div class="resizehandle"></div>')
      .bind('mousedown', function(e) {
        var h = me.height();
        var y = e.clientY;
        var moveHandler = function(e) {
          me
          .height(Math.max(20, e.clientY + h - y));
        };
        var upHandler = function(e) {
          jQuery('html')
          .unbind('mousemove',moveHandler)
          .unbind('mouseup',upHandler);
        };
        jQuery('html')
        .bind('mousemove', moveHandler)
        .bind('mouseup', upHandler);
      })
    );
  });
}


jQuery(document).ready(function(){
  jQuery("#<portlet:namespace/>cqlXml").resizehandle();
});
// --></script>

<style>
.resizehandle {
	background:transparent url("http://www.jquery.info/images/resizer.png") no-repeat scroll 45%;
	cursor:s-resize;
	font-size:0.1em;
	height:16px;
	width:100%;
}
</style>

<portlet:actionURL var="action" />
<c:if test="${!empty cqlQueryCommand.cqlQuerySubmitError}">
	<span style="color:red"><c:out value="${cqlQueryCommand.cqlQuerySubmitError}"/></span>
	<br/>
</c:if>
<form:form action="${action}">
	<table>
		<tr>
			<td>URL</td>
			<td><input type="text" name="dataServiceUrl" value="<c:out value="${cqlQueryCommand.dataServiceUrl}"/>"/></td>
			<td>
				<c:if test="${!empty cqlQueryCommand.dataServiceUrlError}">
					<span style="color:red"/><c:out value="${cqlQueryCommand.dataServiceUrlError}"/></span>
				</c:if>
			</td>
		</tr>
		<tr>
			<td>Query</td>
			<td>
				<div style="width:640px;">
				<textarea id="<portlet:namespace/>cqlXml" style="width:100%; height:200px">
<c:out value="${cqlQueryCommand.cqlQuery}"/>			
				</textarea>
				</div>
			</td>
			<td>
				<c:if test="${!empty cqlQueryCommand.cqlQueryError}">
					<span style="color:red"/><c:out value="${cqlQueryCommand.cqlQueryError}"/></span>
				</c:if>
			</td>				
		</tr>
		<tr>
			<td colspan="3">
				<input type="submit" value="Query" />
				<c:if test="${!empty cqlQueryCommand.confirmationMessage}">
					<br/>
					<span style="color:green"/><c:out value="${cqlQueryCommand.confirmationMessage}"/></span>
				</c:if>
			</td>
		</tr>
	</table>
</form:form>
