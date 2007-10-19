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
			<c:when test="${'domainModel' eq currNode.name}">
				<c:set var="domainModel" value="${currNode.content}"/>
				<%@ include file="/WEB-INF/jsp/discovery/domainModel_frag.jsp"%>
			</c:when>
			<c:when test="${fn:startsWith(currNode.name, 'umlClass_')}">
				<c:set var="umlClass" value="${currNode.content}"/>
				<%@ include file="/WEB-INF/jsp/discovery/umlClass_frag.jsp"%>
			</c:when>
			<c:when test="${fn:startsWith(currNode.name, 'att_')}">
				<c:set var="att" value="${currNode.content}"/>
				<%@ include file="/WEB-INF/jsp/discovery/attribute_frag.jsp"%>
			</c:when>
			<c:when test="${fn:startsWith(currNode.name, 'assoc_')}">
				<c:set var="assoc" value="${currNode.content}"/>
				<%@ include file="/WEB-INF/jsp/discovery/association_frag.jsp"%>
			</c:when>
			<c:when test="${'valueDomain' eq currNode.name}">
				<c:set var="valueDomain" value="${currNode.content}"/>
				<%@ include file="/WEB-INF/jsp/discovery/valueDomain_frag.jsp"%>
			</c:when>
			<c:when test="${fn:startsWith(currNode.name, 'enum_')}">
				<c:set var="enumeration" value="${currNode.content}"/>
				<%@ include file="/WEB-INF/jsp/discovery/enumeration_frag.jsp"%>
			</c:when>
			<c:when test="${fn:startsWith(currNode.name, 'param_')}">
				<c:set var="inputParam" value="${currNode.content}"/>
				<%@ include file="/WEB-INF/jsp/discovery/inputParam_frag.jsp"%>
			</c:when>
			<c:when test="${'output' eq currNode.name}">
				<c:set var="outputParam" value="${currNode.content}"/>
				<%@ include file="/WEB-INF/jsp/discovery/outputParam_frag.jsp"%>
			</c:when>
			<c:when test="${fn:startsWith(currNode.name, 'fault_')}">
				<c:set var="fault" value="${currNode.content}"/>
				<%@ include file="/WEB-INF/jsp/discovery/fault_frag.jsp"%>
			</c:when>
			
			<c:otherwise>
			</c:otherwise>
		</c:choose>	
	</jsp:attribute>
	
</m:tree>