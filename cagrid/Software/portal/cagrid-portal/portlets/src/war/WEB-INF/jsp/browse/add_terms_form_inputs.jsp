<%@ include file="/WEB-INF/jsp/include/servlet_includes.jsp" %>
<c:set var="ns" value="${namespace}"/>
<c:set var="count" scope="page" value="0" />

<c:forEach var="term" items="${terms}">
	<c:set var="count" scope="page" value="${count + 1}" />
</c:forEach>
<c:set var="halfcount" scope="page" value="${count / 2}"/>

<style>
	#areasOfFocusTerms div.row label{
		display:inline;
		float:none;
		font-weight:normal;
		width:auto;
		margin-right:0;
		text-align:left;
	}
	#areasOfFocusTerms div.row div.value {
		margin-left:20px;
	}
</style>

<div class="row" id="areasOfFocusTerms">
	<div class="leftpanel">
		<c:forEach var="term" items="${terms}" begin="1" end="${halfcount}">
			<tags:termSelector input_name="terms" term="${term}" id_prefix="${ns}"/>
		</c:forEach>
	</div>
	<div class="rightpanel">
		<c:forEach var="term" items="${terms}" begin="${halfcount+1}" end="${count}">
			<tags:termSelector input_name="terms" term="${term}" id_prefix="${ns}"/>
		</c:forEach>
	</div>
</div>