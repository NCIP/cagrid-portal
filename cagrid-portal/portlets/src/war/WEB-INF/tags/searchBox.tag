<%@tag %>
<%@taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@taglib prefix="tags" tagdir="/WEB-INF/tags" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<%@attribute name="searchCallback" required="true" description="Callback function to run the search" %>
<%@attribute name="id" required="true" %>

<div class="searchBox">
    <div class="L-endcap"></div>
    <input id="<c:out value="${id}"/>" class="search" type="text" size="20"
           value="Search" style="color:#afafaf;"
           onkeypress="return <c:out value="${searchCallback}(event,this.value);"/>
    "/>

    <div class="R-endcap"></div>
</div>

<script>
    document.getElementById("<c:out value="${id}"/>").onclick = function() {
        this.value = "";
        return false;
    };

</script>

