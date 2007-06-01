<%@ include file="/WEB-INF/jsp/include.jsp"%>
<script src="<c:url value="/js/scriptaculous/prototype.js"/>"
	type="text/javascript"></script>
<script src="<c:url value="/js/scriptaculous/scriptaculous.js"/>"
	type="text/javascript"></script>
<script type="text/javascript">
	//<![CDATA[
    function toggleDiv(divId){
    	new Effect.toggle(divId);
    }
    // ]]>
</script>
<c:choose>
	<c:when test="${!empty gridService}">
		<c:set var="sd"
			value="${gridService.serviceMetadata.serviceDescription}" />
		<c:set var="rc"
			value="${gridService.serviceMetadata.hostingResearchCenter}" />
		<div class="serviceDetails">
		<ul>
			<li>Summary
			<ul>
				<li>
				<div class="label">Name (version):</div>
				<div class="value"><c:out value="${sd.name}" />&nbsp; <c:out
					value="${sd.version}" /></div>
				</li>
				<li>
				<div class="label">Status:</div>
				<div class="value"><c:out value="${gridService.status}" /></div>
				</li>
				<li>
				<div class="label">URL:</div>
				<div class="value"><c:out value="${gridService.url}" /></div>
				</li>
				<li>
				<div class="label">Description:</div>
				<div class="value longText"><c:out
					value="${gridService.status}" /></div>
				</li>
			</ul>
			</li>
			<li>Hosting Research Center <a
				href="toggleDiv(<portlet:namespace/>-rcInfo)">View/Hide</a>
			<div id="<portlet:namespace/>-rfInfo" style="display: none">
			<ul>
				<li>
				<div class="label">Name:</div>
				<div class="value longText"><c:out value="${rc.displayName}" />
				</div>
				</li>
				<li>
				<div class="label">Description:</div>
				<div class="value longText"><c:out value="${rc.description}" />
				</div>
				</li>
				<li>
				<div class="label">Homepage:</div>
				<div class="value longText"><a
					href="<c:out value="${rc.homepageUrl}"/>" target="_blank"> <c:out
					value="${rc.homepageUrl}" /> </a></div>
				</li>
				<li>
				<div class="label">RSS Feed:</div>
				<div class="value longText"><a
					href="<c:out value="${rc.rssNewsUrl}"/>" target="_blank"> <c:out
					value="${rc.rssNewsUrl}" /> </a></div>
				</li>
			</ul>
			</div>
			</li>
		</ul>
		</div>
	</c:when>
	<c:otherwise>
No service is currently selected.
</c:otherwise>
</c:choose>
