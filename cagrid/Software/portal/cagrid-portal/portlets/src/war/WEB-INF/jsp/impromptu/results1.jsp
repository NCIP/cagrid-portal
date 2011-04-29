<%@ page language="java" contentType="text/html" pageEncoding="UTF-8"%>

<%@ page import="java.util.*"%>
<%@ page import="gov.nih.nci.cagrid.portal.portlet.impromptu.ImpromptuQueryStorage"%>
<%@ page import="gov.nih.nci.cagrid.portal.portlet.impromptu.ImpromptuQuery"%>
<%@ page import="org.springframework.web.util.UrlPathHelper"%>
<%@ page import="java.io.ByteArrayInputStream"%>
<%@ page import="java.io.FileInputStream"%>
<%@ page import="java.io.InputStream"%>
<%@ page import="java.io.UnsupportedEncodingException"%>
<%@ page import="javax.xml.parsers.SAXParser"%>
<%@ page import="javax.xml.parsers.SAXParserFactory"%>
<%@ page import="org.json.JSONObject"%>
<%@ page import="gov.nih.nci.cagrid.portal.portlet.query.results.XMLQueryResultToJSONHandler"%>

<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<link rel="stylesheet" type="text/css" href="<c:url value="/js/yui/reset-fonts-grids/reset-fonts-grids.css"/>">
<link rel="stylesheet" type="text/css" href="<c:url value="/js/yui/base/base.css"/>">
<link rel="stylesheet" type="text/css" href="<c:url value="/js/yui/assets/skins/sam/skin.css"/>">

<link type="text/css" rel="stylesheet" href="<c:url value="/js/yui/resize/assets/resize-core.css"/>">
<link type="text/css" rel="stylesheet" href="<c:url value="/js/yui/datatable/assets/datatable.css"/>">

<script type="text/javascript" src="/html/js/jquery/jquery.js"></script>

<script type="text/javascript" src="<c:url value="/js/jqXMLUtils.js"/>"></script>
<script type="text/javascript" src="<c:url value="/js/yui/utilities/utilities.js"/>"></script>
<script type="text/javascript" src="<c:url value="/js/yui/resize/resize-min.js"/>"></script>
<script type="text/javascript" src="<c:url value="/js/yui/element/element-min.js"/>"></script>
<script type="text/javascript" src="<c:url value="/js/yui/paginator/paginator-min.js"/>"></script>
<script type="text/javascript" src="<c:url value="/js/yui/datatable/datatable-min.js"/>"></script>
<script type="text/javascript" src="<c:url value="/js/yui/datasource/datasource-min.js"/>"></script>


<script type="text/javascript">
  
 

function renderDataTable(queryType, jsonObj)
{	
	
	var myColumnDefs;
	myDataSource = new YAHOO.util.LocalDataSource(jsonObj);
	myDataSource.responseType = YAHOO.util.DataSource.TYPE_JSON;	
	var oConfigs = { paginator: new YAHOO.widget.Paginator({ 
			         rowsPerPage: 10 
			                })
	        };	
	
	if(queryType=='CountResult'){
		myColumnDefs = [ {key:"count", label:"count", resizeable:true} ];
		myDataSource.responseSchema = {
					resultsList: "rows",
					fields: ["count"]};
		new YAHOO.widget.DataTable("resultsDiv",myColumnDefs, myDataSource);
	}
	if(queryType=='AttributeResult'){
		myColumnDefs = [ {key:"id", label:"id", sortable:true, resizeable:true},
				{key:"bigid", label:"bigid", sortable:true, resizeable:true},
				{key:"fullName", label:"fullName", sortable:true, resizeable:true},
				{key:"clusterId", label:"clusterId", sortable:true, resizeable:true},
				{key:"symbol", label:"symbol", sortable:true, resizeable:true}
							 				];
		myDataSource.responseSchema = {
						resultsList: "rows",
						fields: ["id","bigid","fullName","clusterId","symbol"]};
		new YAHOO.widget.DataTable("resultsDiv",myColumnDefs, myDataSource,oConfigs);
	}
	if(queryType=='ObjectResult'){
		myColumnDefs = [ {key:"id", label:"Id", sortable:true, resizeable:true},
			 	{key:"bigid", label:"BigId", sortable:true, resizeable:true},
			 	{key:"fullName", label:"Name", sortable:true, resizeable:true},
			 	{key:"clusterId", label:"ClusterId", sortable:true, resizeable:true},
			 	{key:"symbol", label:"Symbol", sortable:true, resizeable:true}
			 				];
		myDataSource.responseSchema = {
						resultsList: "rows",
						fields: ["id","bigid","fullName","clusterId","symbol"]};
		new YAHOO.widget.DataTable("resultsDiv",myColumnDefs, myDataSource,oConfigs);
	}
	

}
</script>

<%
	UrlPathHelper h = new UrlPathHelper();
	String s = h.getOriginatingRequestUri(request);
	int pos = s.lastIndexOf("/");
	String key = s.substring(pos+1);
	JSONObject json = null;
	String queryType = null;
	String xmlString = ImpromptuQueryStorage.instance.getResult(key);
%>
	
<html><head><title>Impromptu Query Results</title></head><body class="yui-skin-sam">
  <b>Query Results:</b> <br>  
<% if (xmlString!=null) {
	if(xmlString.contains("ObjectResult>")){
		queryType = "ObjectResult";
	}else if(xmlString.contains("AttributeResult>")){
		queryType = "AttributeResult";
	}else if(xmlString.contains("CountResult")){
		queryType = "CountResult";
	}
	InputStream is = null;
	try {
	is = new ByteArrayInputStream(xmlString.getBytes("UTF-8"));
				} catch (UnsupportedEncodingException e) {
					e.printStackTrace();
				}
				if(is!=null){			    	
					    
					SAXParserFactory fact = SAXParserFactory.newInstance();
					fact.setNamespaceAware(true);
					SAXParser parser = fact.newSAXParser();
					XMLQueryResultToJSONHandler handler = new XMLQueryResultToJSONHandler();
					parser.parse(is,handler);
					json = handler.getTable();
				}
 %>	
	<div class="yui-content">        
	        <div id="resultsDiv">
	        </div>
 	</div>
 	
	<script>renderDataTable('<%=queryType%>','<%=json%>');</script>
<%	 
}else{ 	 
	 out.print("Query execution failed");
}

%>
 
 </body> 
 </html>    
    
