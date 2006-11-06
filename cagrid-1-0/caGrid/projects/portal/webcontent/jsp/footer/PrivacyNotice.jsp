<%@ taglib prefix="h" uri="http://java.sun.com/jsf/html" %>
<%@ taglib prefix="f" uri="http://java.sun.com/jsf/core" %>

<f:subview id="appSupport">

    <h:panelGrid styleClass="ftrTable"
                 headerClass="ftrHeader" columnClasses="ftrContent">
        <f:facet name="header">
            <h:column>
                <h:outputText value="#{labels.privacyNoticeTitle}"/>
            </h:column>
        </f:facet>

        <h:column>
            <h:outputText value="#{labels.privacyNoticeDescription1}"/>
        </h:column>

        <h:column>
            <h:outputText value="#{labels.privacyNoticeDescription2}"/>
        </h:column>

        <h:column>
            <h:outputText value="#{labels.privacyNoticeDescription3}"/>
        </h:column>

        <h:column>
            <h:outputText value="#{labels.privacyNoticeDescription4}"/>
        </h:column>

        <h:column>
            <h:outputText value="#{labels.privacyNoticeDescription5}"/>
        </h:column>

        <h:column>
            <h:outputText value="#{labels.privacyNoticeDescription6}"/>
            <h:outputLink value="http://www.nih.gov/about/asci.htm">
                <h:outputText value="#{labels.privacyNoticeCookiesLink}"/>
            </h:outputLink>
        </h:column>

        <h:column>
            <h:outputText value="#{labels.privacyNoticeDescription7} "/>

            <h:outputLink value="mailto:NIHPrivacyActOfficer@od.nih.gov">
                <h:outputText value="#{labels.privacyNoticeQuestionsLink}"/>
            </h:outputLink>
        </h:column>


    </h:panelGrid>

</f:subview>