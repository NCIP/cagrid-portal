<%@tag%>
<%@taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@taglib prefix="tags" tagdir="/WEB-INF/tags"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>


<%@attribute name="input_name" required="true" %>
<%@attribute name="term" required="true" type="gov.nih.nci.cagrid.portal.portlet.terms.TermBean" %>
<%@attribute name="id_prefix" required="false" %>

<c:set var="rtsId">${id_prefix}${term.uri}</c:set>
<div class="row">
	<div class="label">
<input id="${rtsId}" type="checkbox" name="${input_name}" alt="Term" value="${term.uri}">
        <span style="display:none;">${term.label}</span>
        <span style="display:none;">${term.comment}</span>
  </input>
<label for="${rtsId}"/>
</div>
<div class="value">
    <tags:yuiPopup popupContent="Area of Focus: ${term.comment}" label="${term.label}" id="${rtsId}-infoPopup-control"/>
</div>
    </div>