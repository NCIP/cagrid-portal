<%@ include file="/WEB-INF/jsp/include/includes.jspf" %>
<%@ include file="/WEB-INF/jsp/include/liferay-includes.jspf" %>
<c:set var="ns"><portlet:namespace/></c:set>

<style type="text/css">
#cagrid-search {
	text-align: center;
}
#cagrid-search .title {
	font-family: Arial, Helvetica, sans-serif;
	font-size: 36px;
}
#cagrid-search .impl-message {
	margin-left: 60em;
	color: red;
}
</style>

<div id="cagrid-search">
	<span class="impl-message">Not yet implemented.</span><br/>
	<span class="title">caGrid Search</span>
	<p>
	<form>
		
		<input type="text" size="50" style="margin-left:8em;"/>
		<a href="#">Advanced Search</a><br/>
		<input type="button" value="Search" alt="Search" style="margin-top:10px;"/>
	</form>
	</p>
	
</div>