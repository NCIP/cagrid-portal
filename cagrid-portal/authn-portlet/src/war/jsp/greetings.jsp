<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="portlet" uri="http://java.sun.com/portlet_2_0" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<c:set var="ns"><portlet:namespace/></c:set>

<portlet:renderURL var="loginUrl">
    <portlet:param name="operation" value="login"/>
</portlet:renderURL>

<portlet:actionURL var="logoutUrl">
    <portlet:param name="operation" value="logout"/>
</portlet:actionURL>


<c:choose>
    <c:when test="${portalUser !=null}">
        Hello <c:out value="${portalUser.username}"/>
        <a href="${logoutUrl}">Logout</a>
    </c:when>
    <c:otherwise>
       Please <a href="${loginUrl}">login</a>
    </c:otherwise>
</c:choose>

