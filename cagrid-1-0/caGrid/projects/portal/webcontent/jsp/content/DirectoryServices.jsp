<%@ taglib prefix="h" uri="http://java.sun.com/jsf/html" %>
<%@ taglib prefix="f" uri="http://java.sun.com/jsf/core" %>


<f:subview id="servicesDirectory">

    <h:panelGrid style="height:100%;width:70%;" cellpadding="0" cellspacing="0" border="0"
                 headerClass="homepageTitle" columnClasses="homepageContent">
        <f:facet name="header">
            <h:column>
                <h:outputText value="Registered Service Directory"/>
            </h:column>
        </f:facet>

        <h:column>
            <h:dataTable var="service" value="#{portal.services}"
                         width="80%" columnClasses="dataCellText"
                         rowClasses="dataRowDark,dataRowLight">
                <h:column>


                    <h:panelGrid width="100%" rowClasses="dataRowDark,dataRowLight"
                                 headerClass="dataTableHeader" border="1" columns="2">
                        <f:facet name="header">
                            <h:column>
                                <h:outputText value="Service Details"/>
                            </h:column>

                        </f:facet>

                        <h:column>
                            <h:outputText value="Name"/>
                        </h:column>
                        <h:column>
                            <<h:outputText value="#{service.name}"/>
                        </h:column>

                        <h:column>
                            <h:outputText value="URL"/>
                        </h:column>
                        <h:column>
                            <<h:outputText value="#{service.handle}"/>
                        </h:column>

                        <h:column>
                            <h:outputText value="Description"/>
                        </h:column>
                        <h:column>
                            <<h:outputText value="#{service.description}"/>
                        </h:column>

                        <h:column/>
                        <h:column>
                            <h:outputLink value="More>>"/>
                        </h:column>

                    </h:panelGrid>
                </h:column>

            </h:dataTable>
        </h:column>


    </h:panelGrid>
</f:subview>