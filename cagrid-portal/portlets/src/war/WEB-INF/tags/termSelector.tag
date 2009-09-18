<%@tag%>
<%@taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@taglib prefix="tags" tagdir="/WEB-INF/tags"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>


<%@attribute name="input_name" required="true" %>
<%@attribute name="term" required="true" type="gov.nih.nci.cagrid.portal.portlet.terms.TermBean" %>
<%@attribute name="id_prefix" required="false" %>

<c:set var="rtsId">${id_prefix}${term.uri}</c:set>
<input type="checkbox" name="${input_name}" value="${term.uri}"/>

<a id="${rtsId}-infoPopup-control"
   class="infoPopupLink"
   onmouseover="$('${rtsId}-infoPopup-content').style.display='inline'"
   onmouseout="$('${rtsId}-infoPopup-content').style.display='none'">
   <c:out value="${term.label}"/>
</a>&nbsp;

        <span id="${rtsId}-infoPopup-content" class="infoPopup">
        <c:out value="${term.comment}"/>


<span class="infoPopup-pointer">&nbsp;</span></span>

<div id="${rtsId}_subTermsContainer"></div>