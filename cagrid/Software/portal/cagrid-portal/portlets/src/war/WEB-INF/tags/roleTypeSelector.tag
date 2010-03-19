<%@tag %>
<%@taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@taglib prefix="tags" tagdir="/WEB-INF/tags" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>


<%@attribute name="input_name" required="true" %>
<%@attribute name="entry_type" required="true" %>
<%@attribute name="id_prefix" required="false" %>

<c:set var="rtsId">${id_prefix}${fn:replace(entry_type, ".", "_")}</c:set>
<input type="radio" name="${input_name}" value="${entry_type}"/>

<a id="${rtsId}-infoPopup-control"
   class="infoPopupLink"
        >
    <spring:message code="${entry_type}"/>
</a>&nbsp;

        <span id="${rtsId}-infoPopup-content" class="infoPopup">
        <spring:message code="${entry_type}.description"/><br/>

<span class="infoPopup-pointer">&nbsp;</span></span>

<div id="${rtsId}_roleTypesContainer"></div>