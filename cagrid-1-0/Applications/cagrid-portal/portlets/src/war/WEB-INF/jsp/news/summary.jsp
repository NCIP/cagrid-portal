<%@ include file="/WEB-INF/jsp/include/includes.jspf" %>

<c:set var="resizablePrefix"><portlet:namespace/>summary</c:set>
<%@ include file="/WEB-INF/jsp/include/resizable_div.jspf" %>
<a alt="Subscribe to caGrid RSS News Feed" href="/cagridportlets/news.rss" style="text-decoration:none">
    <img alt="Subscribe to caGrid RSS News Feed" src="<c:url value="/images/rss.png"/>" align="absmiddle" height="16"/>
    &nbsp;Subscribe to caGrid News Feed
</a>

<hr/>
<c:choose>
    <c:when test="${empty items}">
        <div style="height:130px">
            No news items to display.
        </div>
    </c:when>
    <c:otherwise>

        <div id="<c:out value="${resizablePrefix}"/>" style="width:100%; height:415px; overflow-y:auto">
            <c:forEach var="item" items="${items}">
                <portlet:actionURL var="selectItemAction">
                    <portlet:param name="operation" value="selectItemForNews"/>
                    <portlet:param name="itemId" value="${item.id}"/>
                </portlet:actionURL>
                <b><a href="<c:out value="${selectItemAction}"/>"><c:out value="${item.title}"/></a></b>
                <br/>
                <i>[<fmt:formatDate value="${item.pubDate}" type="both"/>]</i>
                <br/>
                <c:out value="${item.description}" escapeXml="false"/><br/>
                <hr/>
            </c:forEach>
        </div>

    </c:otherwise>
</c:choose>