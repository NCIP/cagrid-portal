<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="portlet" uri="http://java.sun.com/portlet_2_0" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>


<c:set var="ns"><portlet:namespace/></c:set>

<portlet:renderURL var="portalAuthnUrl"/>

<portlet:actionURL var="loginAction">
    <portlet:param name="operation" value="login"/>
</portlet:actionURL>

<div align="left" class="yui-skin-sam">

    <form:form id="${ns}LoginForm" commandName="command" action="${loginAction}" method="POST">

        <c:if test="${!empty loginErrors}">
		<c:forEach var="error" items="${loginErrors.allErrors}">
		   <span style="color:red"><c:out value="${error.defaultMessage}"/></span>
		</c:forEach>
    </c:if>

        <span style="color:red"><form:errors path="*"/></span>

        <table>
            <tr>
                <td style="padding-right:5px; text-align:right">Username:</td>
                <td><form:input path="username" size="29"/></td>
            </tr>
            <tr>
                <td style="padding-right:5px; text-align:right;">Password:</td>
                <td><form:password path="password" size="29"/></td>
            </tr>
            <tr>
                <td style="padding-right:5px; text-align:right;">Identity Provider:</td>
                <td>
                    <input name="idpUrl" id="idpUrl" type="hidden"/>
                    <form:select id="${ns}idpSelect" path="idpUrl" items="${listIdPsFromDorian}"
                                 itemValue="url" itemLabel="label"/>
                </td>
            </tr>
            <tr>
                <td></td>
                <td style="padding-top:10px;">

                    <span id="${ns}loginButtonContainer"></span>

                </td>
            </tr>
        </table>
        <input type="hidden" name="portalAuthnUrl" value="<c:out value="${portalAuthnUrl}"/>"/>

        <input type="submit" value="Login"/>
    </form:form>


</div>

