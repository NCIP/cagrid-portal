<c:choose>
	<c:when test="${scroller.previousAvailable}"><a href="<portlet:actionURL><portlet:param name="scrollOp" value="first"/></portlet:actionURL>"><img alt="Scroll to first" border="1" src="<c:url value="/images/scroller/arrow-first.gif"/>"/></a></c:when>
	<c:otherwise><img alt="Scroll to first" border="1" src="<c:url value="/images/scroller/arrow-first.gif"/>"/></c:otherwise>
</c:choose>
<c:choose>
	<c:when test="${scroller.previousAvailable}"><a href="<portlet:actionURL><portlet:param name="scrollOp" value="previous"/></portlet:actionURL>"><img alt="Scroll to previous" border="1" src="<c:url value="/images/scroller/arrow-previous.gif"/>"/></a></c:when>
	<c:otherwise><img alt="Scroll to previous" border="1" src="<c:url value="/images/scroller/arrow-previous.gif"/>"/></c:otherwise>
</c:choose>
<c:choose>
	<c:when test="${scroller.nextAvailable}"><a href="<portlet:actionURL><portlet:param name="scrollOp" value="next"/></portlet:actionURL>"/><img alt="Scroll to next" border="1" src="<c:url value="/images/scroller/arrow-next.gif"/>"/></a></c:when>
	<c:otherwise><img alt="Scroll to next" border="1" src="<c:url value="/images/scroller/arrow-next.gif"/>"/></c:otherwise>
</c:choose>
<c:choose>
	<c:when test="${scroller.nextAvailable}"><a href="<portlet:actionURL><portlet:param name="scrollOp" value="last"/></portlet:actionURL>"/><img alt="Scroll to last" border="1" src="<c:url value="/images/scroller/arrow-last.gif"/>"/></a></c:when>
	<c:otherwise><img alt="Scroll to last" border="1" src="<c:url value="/images/scroller/arrow-last.gif"/>"/></c:otherwise>
</c:choose>