<%@ include file="/WEB-INF/jsp/include/includes.jspf" %>
<%@ include file="/WEB-INF/jsp/query/tabs.jspf" %>


<portlet:renderURL var="backUrl">
    <portlet:param name="selectedTabPath" value="/query/builder/cqlQuery"/>
</portlet:renderURL>
<a href="<c:out value="${backUrl}"/>">&lt;&lt;&nbsp;Back</a>
<br>
<br>

<portlet:actionURL var="aggregateTargetsFormAction"/>
<c:set var="aggregateTargetsFormName"><portlet:namespace/>aggregateTargetsFormName</c:set>
<form:form commandName="aggregateTargetsCmd" name="${aggregateTargetsFormName}"
           action="${aggregateTargetsFormAction}">

    <c:choose>
        <c:when test="${empty aggregateTargets}">
            <div class="row">
                <div class="label">
                    No Services found that have the same UML Class
                </div>
            </div>
        </c:when>
        <c:otherwise>
            <div class="row">
                <div class="label">
                    Following Services have the chosen UML Class. Select services to aggregate
                    data from.
                </div>
            </div>
            <c:forEach var="aggregateTarget" items="${aggregateTargets}">
                <div class="row">
                    <div class="label">
                        <input type="checkbox" value="${aggregateTarget.model.service.url}"/>
                        &nbsp;
                    </div>
                    <div class="vale">
                        <c:out value="${aggregateTarget.model.service.url}"/>
                    </div>
                </div>
            </c:forEach>
            <br>

            <div class="row">
                <div class="label">
                    <input type="submit" value="Add Targets"/>
                </div>
            </div>
        </c:otherwise>

    </c:choose>

    <input type="hidden" name="operation" value="selectAggregateTargets"/>


</form:form>