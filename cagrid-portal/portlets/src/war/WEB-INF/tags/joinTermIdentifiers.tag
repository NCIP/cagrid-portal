<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@attribute name="items" required="true" type="java.util.Collection"%>
<%@attribute name="delimiter" required="true"%>
<c:forEach var="item" varStatus="status" items="${items}"><c:out value="${item.identifier}"/><c:if test="${!status.last}"><c:out value="${delimiter}"/></c:if></c:forEach>