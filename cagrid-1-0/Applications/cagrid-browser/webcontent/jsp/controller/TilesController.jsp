<%@ page contentType="text/html;charset=UTF-8" language="java" %>

<%@ taglib prefix="t" uri="http://jakarta.apache.org/struts/tags-tiles" %>

<%
    String tilePath = request.getRequestURI();

    int slashIndex = tilePath.lastIndexOf('/');
    int extIndex = tilePath.lastIndexOf(".");

    if (slashIndex >= 0) {
        tilePath = tilePath.substring(slashIndex + 1, extIndex);
    }
%>

<t:insert definition="<%=tilePath%>" flush="false"/>
