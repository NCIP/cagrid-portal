<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ taglib prefix="m" tagdir="/WEB-INF/tags"%>

<%@ attribute name="node" required="true" type="gov.nih.nci.cagrid.portal.portlet.tree.TreeNode"%>
<%@ attribute name="prefix" required="true"%>
<%@ attribute name="contentFragment" required="true" fragment="true"%>
<%@ attribute name="nodeFragment" required="true" fragment="true"%>

<%@ variable name-given="currNode"%>
<%@ variable name-given="currChildNode"%>

<c:set var="currNode" value="${node}"/>
<div class="node_content">
<jsp:invoke fragment="contentFragment"/>
<c:if test="${fn:length(node.children) > 0}">
<ul class="leaf_node">
	<c:forEach var="childNode" items="${node.children}">
		<c:set var="currChildNode" value="${childNode}"/>
		<c:choose>
			<c:when test="${childNode.state == 'OPEN'}">
				<c:set var="nodeClass" value="coll_node" />
			</c:when>
			<c:otherwise>
				<c:set var="nodeClass" value="exp_node" />
			</c:otherwise>
		</c:choose>

		<div style="margin-left:10px">
		<li class="<c:out value="${nodeClass}"/>"
			id="<c:out value="${prefix}"/><c:out value="${childNode.path}"/>Node"
			onclick="<c:out value="${prefix}"/>toggleDiv('<c:out value="${childNode.path}"/>')">
			<jsp:invoke fragment="nodeFragment"/>
		</li>
		</div>
		
		<c:choose>
			<c:when test="${childNode.state == 'OPEN'}">
				<div id="<c:out value="${prefix}"/><c:out value="${childNode.path}"/>Div" style="display: block">
						<m:tree node="${childNode}" prefix="${prefix}">
							<jsp:attribute name="contentFragment" trim="true">
								<jsp:invoke fragment="contentFragment"/>
							</jsp:attribute>
							<jsp:attribute name="nodeFragment" trim="true">
								<jsp:invoke fragment="nodeFragment"/>
							</jsp:attribute>
						</m:tree>
				</div>
			</c:when>
			<c:otherwise>
				<div id="<c:out value="${prefix}"/><c:out value="${childNode.path}"/>Div" style="display: none">

				</div>			
			</c:otherwise>
		</c:choose>
	</c:forEach>
</ul>
</c:if>
</div>