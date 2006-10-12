<%@ taglib prefix="h" uri="http://java.sun.com/jsf/html" %>
<%@ taglib prefix="f" uri="http://java.sun.com/jsf/core" %>

<f:subview id="appSupport">

    <h:panelGrid styleClass="ftrTable"
                 headerClass="ftrHeader" columnClasses="ftrContent">
        <f:facet name="header">
            <h:column>
                <h:outputText value="#{labels.appSupportTitle}"/>
            </h:column>
        </f:facet>

        <h:column>
            <f:verbatim><br/></f:verbatim>
        </h:column>

        <h:column>
            <h:outputText value="#{labels.furtherQuestions}"/>
        </h:column>

        <h:column>
            <f:verbatim><br/></f:verbatim>
        </h:column>

        <h:column>
            <h:outputText value="#{labels.ncicbAppSupport}" styleClass="h2"/>
        </h:column>

        <h:column>
            <h:outputLink value="mailto:ncicb@pop.nci.nih.gov">
                <h:outputText value="ncicb@pop.nci.nih.gov"/>
            </h:outputLink>
        </h:column>

        <h:column>
            <h:outputText value="Local: 301-451-4384 "/>
        </h:column>

        <h:column>
            <h:outputText value="Toll-free: 888-478-4423"/>
        </h:column>

        <h:column>
            <h:outputText value="Web:"/>
            <h:outputLink value="http://ncicbsupport.nci.nih.gov" target="new">
                <h:outputText value="http://ncicbsupport.nci.nih.gov"/>
            </h:outputLink>
        </h:column>


    </h:panelGrid>

</f:subview>