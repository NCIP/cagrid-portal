<%@ include file="/WEB-INF/jsp/include/includes.jspf" %>
<%@ include file="/WEB-INF/jsp/query/tabs.jspf" %>

<c:set var="formName"><portlet:namespace/>submitQueryForm</c:set>
<script type="text/javascript">
    //<![CDATA[
    function <portlet:namespace/>shareQuery(){
    var form = document.<c:out value="${formName}"/>;
    form.action = '<portlet:actionURL/>';
    form.operation.value = 'shareQuery';
    form.submit();
    }
    //]]>
</script>

<c:set var="resizablePrefix"><portlet:namespace/>cqlXml</c:set>
<%@ include file="/WEB-INF/jsp/include/resizable_div.jspf" %>

<portlet:actionURL var="action"/>
<form:form id="${formName}" name="${formName}" action="${action}" commandName="cqlQueryCommand">
    <input type="hidden" name="operation" value="submitQuery"/>
    <span style="color:red"><form:errors path="*"/></span>
    <table>
        <!--don't render address if dcql-->
        <c:if test="${cqlQueryCommand.dcql == 'false'}">
            <tr>
                <td style="padding-right:5px"><b>URL:</b></td>
                <td><form:input id="${formName}Url" path="dataServiceUrl" size="80"/><br/>
                    <span style="color:red"><form:errors path="dataServiceUrl"/></span></td>
            </tr>
        </c:if>
        <tr>
            <td valign="top" style="padding-right:5px;"><b>Query:</b></td>
            <td>
                <form:textarea id="${resizablePrefix}" cssStyle="width:420px; height:200px" path="cqlQuery"/>
                <br/>
                <span style="color:red"><form:errors path="cqlQuery"/></span>
            </td>
        </tr>
        <tr>
            <td colspan="2">
                <input type="submit" id="${formName}Submit" alt="Submit Query" value="Submit Query"/>

                <!--no sharing if query is dcql-->

                <c:if test="${!empty portalUser}">
                    <input type="button" id="${formName}query" alt="Share Query" value="Share Query" onclick="<portlet:namespace/>shareQuery()"/>
                </c:if>


            </td>
        </tr>
    </table>
</form:form>
