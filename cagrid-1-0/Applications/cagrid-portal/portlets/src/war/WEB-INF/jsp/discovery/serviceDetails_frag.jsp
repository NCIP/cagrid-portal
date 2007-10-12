<m:tree node="${node}" prefix="${prefix}">

	<jsp:attribute name="nodeFragment">
		<b><c:out value="${currChildNode.label}"/></b>
	</jsp:attribute>

	<jsp:attribute name="contentFragment">
		<c:choose>

			<c:when test="${'semanticMetadata' eq currNode.name}">
				<c:set var="sms" value="${currNode.content}"/>
				<%@ include file="/WEB-INF/jsp/discovery/semanticMetadata_frag.jsp"%>
			</c:when>
			<c:when test="${'contextPropertyCollection' eq currNode.name}">
				<c:set var="cps" value="${currNode.content}"/>
				<%@ include file="/WEB-INF/jsp/discovery/contextProperty_frag.jsp"%>
			</c:when>
			<c:when test="${'gridService' eq currNode.name}">
				<c:set var="gridService" value="${currNode.content}"/>
				<%@ include file="/WEB-INF/jsp/discovery/gridService_frag.jsp"%>
			</c:when>
			<c:when test="${'hostingResearchCenter' eq currNode.name}">
				<c:set var="researchCenter" value="${currNode.content}"/>
				<%@ include file="/WEB-INF/jsp/discovery/researchCenter_frag.jsp"%>
			</c:when>
			<c:when test="${fn:startsWith(currNode.name, 'poc_')}">
				<c:set var="poc" value="${currNode.content}"/>
				<%@ include file="/WEB-INF/jsp/discovery/pointOfContact_frag.jsp"%>
			</c:when>
			<c:when test="${'serviceContext' eq currNode.name}">
				<c:set var="serviceContext" value="${currNode.content}"/>
				<%@ include file="/WEB-INF/jsp/discovery/serviceContext_frag.jsp"%>
			</c:when>
			<c:when test="${fn:startsWith(currNode.name, 'op_')}">
				<c:set var="operation" value="${currNode.content}"/>
				<%@ include file="/WEB-INF/jsp/discovery/operation_frag.jsp"%>
			</c:when>
			
			<c:otherwise>
			</c:otherwise>
		</c:choose>	
	</jsp:attribute>
	
</m:tree>