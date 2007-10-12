<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ taglib prefix="m" tagdir="/WEB-INF/tags"%>


<m:tree node="${node}" prefix="${prefix}">
	<jsp:attribute name="contentFragment">
		<b>Content of <c:out value="${currNode.name}" />:</b><c:out value="${currNode.content.text}" /><br />
	</jsp:attribute>
	<jsp:attribute name="nodeFragment">
		Node: <c:out value="${currChildNode.name}" />
	</jsp:attribute>
</m:tree>