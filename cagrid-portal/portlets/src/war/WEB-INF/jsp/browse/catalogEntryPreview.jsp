<%@ include file="/WEB-INF/jsp/include/includes.jspf" %>

<div class="photoRow">
    <div class="photo">
        <tags:catalogEntryImage entry="${catalogEntry}" thumbnail="false"
                                title="${catalogEntry.name}" alt="Image of ${catalogEntry.name}"/>
    </div>
    <div class="photoRightContent">
        <h1 class="entryName"><c:out value="${catalogEntry.name}"/></h1>
        <i>
        <b><spring:message code="${catalogEntry.class.name}"/> Catalog Entry</b>
        [Created by
			<c:choose>
				<c:when test="${!empty catalogEntry.author}">
					<c:out value="${catalogEntry.author.catalog.firstName}"/> <c:out value="${catalogEntry.author.catalog.lastName}"/>
				</c:when>
				<c:otherwise>caGrid Portal</c:otherwise>
			</c:choose>
        ]</i>

        <p>
            <b>Average Rating</b><%--<span id="${ns}averageRating"></span>--%>
        </p><br/>

        <p class="entryDescription">
            <c:out value="${catalogEntry.description}"/>
        </p>
    </div>
</div>
