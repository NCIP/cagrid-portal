<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="portlet" uri="http://java.sun.com/portlet" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>




<style type="text/css">
    <%@ include file="/css/base.css" %>
</style>



<div class="content">
    <form action="<portlet:actionURL/>" method="post">
        <span style="color:red"><form:errors path="*"/></span>

        <div class="input">
            Keyword:
            <input name="keyword" value="${cmd.keyword}" size="30"/>
            <input type="submit" value="Search"/>
        </div>

        <div class="result">
            <c:if test="${cmd.result != null}">
                <hr/>
                <span style="font-weight:bold">
                     <c:out value="${cmd.result}"/>
               </span>
            </c:if>
        </div>
    </form>

</div>