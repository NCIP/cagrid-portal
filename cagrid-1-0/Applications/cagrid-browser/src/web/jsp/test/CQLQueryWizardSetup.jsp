<%--
$Id: CQLQueryWizardSetup.jsp,v 1.1 2006-10-03 12:33:21 joshua Exp $
version $Revision: 1.1 $
author Joshua Phillips
--%>
<%@ taglib uri="http://java.sun.com/jsf/html" prefix="h"%>
<%@ taglib uri="http://java.sun.com/jsf/core" prefix="f"%>

<f:view locale="#{browserConfig.locale}">
<f:loadBundle basename="labels" var="labels"/>
<f:loadBundle basename="messages" var="messages"/>

<html>
	<head>
		<title>CQLQueryWizard Setup</title>
	</head>
	
	<body>
		
			<h:form id="setupForm">
				<table>
					<tr>
						<td>Server URL:</td>
						<td>
							<h:inputText id="serviceUrl" value="#{test_queryWizardInit.serviceUrl}"/>
						</td> 
					</tr>
					<tr>
						<td>Domain Object:</td>
						<td>
							<h:inputText id="domainObject" value="#{test_queryWizardInit.domainObject}"/>
						</td> 
					</tr>
					<tr>
						<td colspan="2">
							<h:commandButton id="submit" action="#{test_queryWizardInit.init}" value="Submit"/>
						</td>
					</tr>
				</table>
				<h:message 
					style="color: red; font-family: 'New Century Schoolbook', serif; font-style: oblique; text-decoration: overline" id="errors1" 
					for="submit"/>
			</h:form>
		
	</body>
</html>
</f:view>