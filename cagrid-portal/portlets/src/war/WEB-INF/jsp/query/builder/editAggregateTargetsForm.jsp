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
<form:form commandName="aggregateTargetsCmd" id="${aggregateTargetsFormName}" name="${aggregateTargetsFormName}"
           action="${aggregateTargetsFormAction}">

    <c:choose>
        <c:when test="${empty aggregateTargetsCmd.available}">
            <div class="row">
                <div class="label">
                    Cannot form aggregate query. No other Active Service
                    has syntactically equivalent classes.
                </div>
            </div>
        </c:when>

        <c:otherwise>
            <div class="row">
                <div class="label">
                    Following Active Services have the chosen UML Class. Select services to aggregate
                    data from.
                </div>
            </div>

            <div class="row">
                <div class="label">
                    <input type="checkbox" id="${aggregateTargetsFormName}PUrl" alt="Primary Service Url" checked="true" value="${primary.model.service.url}" DISABLED/>
                    &nbsp;
                </div>
                <div>
                    <c:out value="${primary.model.service.url}"/>
                </div>
            </div>

            <c:forEach var="aggregateTarget" items="${aggregateTargetsCmd.available}">
                <div class="row">
                    <div class="label">
                        <form:checkbox path="selected" id="${aggregateTargetsFormName}AggTargets" value="${aggregateTarget.model.service.url}"/>
                        &nbsp;
                    </div>
                    <div>
                        <c:out value="${aggregateTarget.model.service.url}"/>
                    </div>
                </div>
            </c:forEach>
            <br>

            <div class="row">
                <div class="label">
                    <input type="hidden" name="operation" value="selectAggregateTargets"/>
                    <input type="submit" id="${aggregateTargetsFormName}Add" alt="Add Targets" value="Add Targets"/>
                </div>
            </div>


        </c:otherwise>
    </c:choose>

</form:form>