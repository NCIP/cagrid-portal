<%@ include file="/WEB-INF/jsp/include.jsp" %>
<%@ include file="/WEB-INF/jsp/query/tabs.jspf" %>

<script type="text/javascript"><!--
/*
 * resizehandle.js (c) Fil 2007, plugin pour jQuery ecrit
 * a partir du fichier resize.js du projet DotClear
 * (c) 2005 Nicolas Martin & Olivier Meunier and contributors
 */
jQuery.fn.<portlet:namespace/>resultsXmlresizehandle = function() {
  return this.each(function() {
    var me = jQuery(this);
    me.after(
      jQuery('<div class="<portlet:namespace/>resultsXmlresizehandle"></div>')
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
  jQuery("#<portlet:namespace/>resultsXml").<portlet:namespace/>resultsXmlresizehandle();
});
// --></script>

<style>
.<portlet:namespace/>resultsXmlresizehandle {
	background:transparent url("http://www.jquery.info/images/resizer.png") no-repeat scroll 45%;
	cursor:s-resize;
	font-size:0.1em;
	height:16px;
	width:100%;
}
</style>

<style type="text/css">
<!--
<%@ include file="/css/xmlverbatim.css" %>
-->
</style>
<c:choose>
<c:when test="${empty resultsCommand.instance.result}">
No results to display.
</c:when>
<c:otherwise>
<c:set var="scroller" value="${resultsCommand.tableScroller}"/>
<c:choose>
	<c:when test="${fn:length(scroller.page) == 0}">
		Results are empty.
	</c:when>
	<c:otherwise>
		<div style="width:500px;">
		<div id="<portlet:namespace/>resultsXml" style="width:100%; height:200px; overflow:scroll">
<c:out value="${resultsCommand.prettyXml}" escapeXml="false"/>
		</div>
		</div>
	</c:otherwise>
</c:choose>
</c:otherwise>
</c:choose>