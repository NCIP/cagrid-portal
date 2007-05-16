<%@ include file="/WEB-INF/jsp/include.jsp"%>

<h1>Yahoo Map View Preferences</h1>


<table>
	<tr>
		<td>Yahoo App ID:</td>
		<td><input type="text" name="appid"
			value="<c:out value="${appid}"/>" /></td>
	</tr>
	<tr>
		<td>GeoRSS URLs:</td>
		<td>
		<form name="<portlet:namespace/>GeoRSSUrlForm" method="post" action="<portlet:actionURL/>">
		<table>
			<tr>
				<td><a href="javascript:document.<portlet:namespace/>GeoRSSUrlForm.submit()">Add</a></td>
				<td><input type="text" name="urlToAdd" />
					<input type="hidden" name="action" value="addUrl"/>
				</td>
			</tr>
			<c:forEach items="${geoRSSUrls}" var="url">
				<tr>
					<td><a
						href="<portlet:actionURL>
                         <portlet:param name="action" value="deleteUrl"/>
                         <portlet:param name="urlToDelete">
                             <jsp:attribute name="value">
                                 <c:out value="${url}"/>
                             </jsp:attribute>
                         </portlet:param>
                      </portlet:actionURL>">
					Remove </a></td>
					<td>
						<c:out value="${url}"/>
					</td>
				</tr>
			</c:forEach>
		</table>
		</form>
		</td>
	</tr>
</table>
<br />

<br />
Back to
<a href="<portlet:renderURL portletMode="view"/>">View Mode</a>
