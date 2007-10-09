<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>
<%@ taglib prefix="m" tagdir="/WEB-INF/tags"%>

<%@ attribute name="node" required="true" type="test.TreeNode"%>

<b>Text of node <c:out value="${node.name}" />:</b>
<c:out value="${node.content.text}" />
<br />
<ul class="leaf_node">
	<c:forEach var="childNode" items="${node.children}">

		<c:choose>
			<c:when test="${childNode.state == 'OPEN'}">
				<c:set var="nodeClass" value="coll_node" />
			</c:when>
			<c:otherwise>
				<c:set var="nodeClass" value="exp_node" />
			</c:otherwise>
		</c:choose>

		<li class="<c:out value="${nodeClass}"/>"
			id="<c:out value="${childNode.path}"/>Node"
			onclick="toggleDiv('<c:out value="${childNode.path}"/>')">
				Node: <c:out value="${childNode.name}" />
		</li>
		
		<c:choose>
			<c:when test="${childNode.state == 'OPEN'}">
				<div id="<c:out value="${childNode.path}"/>Div" style="display: block">
						<m:tree node="${childNode}" />
				</div>
			</c:when>
			<c:otherwise>
				<div id="<c:out value="${childNode.path}"/>Div" style="display: none">

				</div>			
			</c:otherwise>
		</c:choose>
	</c:forEach>
</ul>
