<%@tag%>
<%@taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@taglib prefix="tags" tagdir="/WEB-INF/tags"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>

<%@attribute name="id" required="true" %>
<%@attribute name="popup_href" required="true" description="Hyperlink for the popup" %>
<%@attribute name="popup_name" required="true" description="Text to display for the popup" %>
<%@attribute name="popup_text" required="true" description="Information appearing in the popup" %>



<a id="${id}-infoPopup-control"
   class="infoPopupLink"
   onmouseover="$('${id}-infoPopup-content').style.display='inline'"
   onmouseout="$('${id}-infoPopup-content').style.display='none'"
   href='${popup_href}'>${popup_name}</a>
&nbsp;

        <span id="${id}-infoPopup-content" class="infoPopup">
                ${popup_text}


<span class="infoPopup-pointer">&nbsp;</span></span>
