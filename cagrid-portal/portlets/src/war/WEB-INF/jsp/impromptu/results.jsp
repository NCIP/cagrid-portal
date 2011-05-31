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

<script type="text/javascript" src="<c:url value="/js/yui/utilities/utilities.js"/>"></script>
<script type="text/javascript" src="<c:url value="/js/yui/resize/resize-min.js"/>"></script>
<script type="text/javascript" src="<c:url value="/js/yui/element/element-min.js"/>"></script>
<script type="text/javascript" src="<c:url value="/js/yui/paginator/paginator-min.js"/>"></script>
<script type="text/javascript" src="<c:url value="/js/yui/datatable/datatable-min.js"/>"></script>
<script type="text/javascript" src="<c:url value="/js/yui/datasource/datasource-min.js"/>"></script>


<script type="text/javascript">
  
 

function renderDataTable(jsonObj)
{	
	var jsonStr=eval('jsonObj');
	var jsonSplit = jsonStr.split('{',3);
	colsStr = jsonSplit[2].split(",");	
	var myColumnDefs = [];
	var fieldDefs=[];
	for(i=0;i<colsStr.length;i++){
		col = colsStr[i].split(":",1);
		var column = col[0].replace(/"/g, "");	
		if(column!=""){
			var obj = {
		    		key: column,
		    		sortable: true,
		    		resizeable: true
		    		};
			myColumnDefs.push(obj);			
			fieldDefs.push(column);
		}
	}	
	
	myDataSource = new YAHOO.util.LocalDataSource(jsonObj);
	myDataSource.responseType = YAHOO.util.DataSource.TYPE_JSON;	
	var oConfigs = { paginator: new YAHOO.widget.Paginator({ 
			         rowsPerPage: 20 
			                })	        };
	
	myDataSource.responseSchema = {
					resultsList: "rows",
					fields: fieldDefs };
	new YAHOO.widget.DataTable("resultsDiv",myColumnDefs, myDataSource,oConfigs);	

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
 	
	<script>renderDataTable('<%=json%>');</script>
<%	 
}else{ 	 
	 out.print("Query execution failed");
}

%>
 
 </body> 
 </html>    
    
