<%@ include file="/WEB-INF/jsp/include.jsp" %>

<h3>An Error Occurred</h3>

message: <c:out value="${exception.message}"/>

<p style="text-align:center;">
<a href="<portlet:renderURL>
			<portlet:param name="action" value="edit"/>
		</portlet:renderURL>">Back</a></p>