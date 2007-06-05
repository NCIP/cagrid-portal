<%@ page contentType="text/html; charset=ISO-8859-1" isELIgnored="false" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="portlet" uri="http://java.sun.com/portlet" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@ taglib prefix="html" tagdir="/WEB-INF/tags/html" %>

<form method="post" action="<portlet:actionURL>
			<portlet:param name="action" value="performQuery"/>
		</portlet:actionURL>">
	<spring:hasBindErrors name="urlqueryobject">
	<p class="portlet-msg-error"><br/>Errors :</p>
	<ul>
		<spring:bind path="urlqueryobject.*">
			<c:forEach items="${status.errorMessages}" var="error">
				<li><font color="red">${error}"</font></li>
			</c:forEach>
		</spring:bind>
	</ul>
	</spring:hasBindErrors>	
	<table border="0" cellpadding="4">
		<tr>
			<td class="portlet-font">URL</td>
			<td>
				<spring:bind path="urlqueryobject.url">
					<textarea  name="${status.expression}" wrap="SOFT" rows="4" cols="50" type="_moz">${status.value}</textarea>
				<br/><span class="portlet-msg-error"><font class="red" >${status.errorMessage}</font></span>
				</spring:bind>
			</td>
		</tr>
		<tr>
			<td class="portlet-font">CQL Query</td>
			<td>
				<spring:bind path="urlqueryobject.cqlQuery">
				<textarea  name="${status.expression}" wrap="off" rows="7" cols="50" type="_moz">${status.value}</textarea>
				<br/><span class="portlet-msg-error"><font class="red" >${status.errorMessage}</font></span>
				</spring:bind>
			</td>
		</tr>
		<tr>
			<td class="portlet-font">Results :</td>
			<td class="portlet-font">
				<textarea  name="${status.expression}" wrap="off" rows="15" cols="50" readonly="true">${urlqueryobject.results}</textarea>
			</td>
		</tr>
		<tr>
			<th colspan="2">
				<button type="submit">Query</button>
			</th>
		</tr>
	</table>
</form>


