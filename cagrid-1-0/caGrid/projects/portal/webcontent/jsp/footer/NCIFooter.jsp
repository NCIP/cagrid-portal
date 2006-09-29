<%@ taglib prefix="h" uri="http://java.sun.com/jsf/html" %>
<%@ taglib prefix="f" uri="http://java.sun.com/jsf/core" %>


<f:subview id="footer">
    <h:panelGrid id="footerTable" align="center" border="0" cellspacing="0" cellpadding="0"
                 styleClass="ftrTable" columnClasses="footerColumns">
        <h:column>
            <h:outputLink id="cancer" value="http://www.cancer.gov/">
                <h:graphicImage id="img_cancer" url="images/footer_nci.gif" width="63" height="31"
                                alt="National Cancer Institute" style="border: none;"/>
            </h:outputLink>
            <h:outputLink id="dhhs" value="http://www.dhhs.gov/">
                <h:graphicImage id="img_dhhs" url="images/footer_nci.gif" width="39" height="31"
                                alt="Department of Health and Human Services" style="border: none;"/>
            </h:outputLink>
            <h:outputLink id="cancer2" value="http://www.cancer.gov/">
                <h:graphicImage id="img_cancer2" url="images/footer_nih.gif" width="46" height="31"
                                alt="National Cancer Institute" style="border: none;"/>
            </h:outputLink>
            <h:outputLink id="firstgov" value="http://www.firstgov.gov/">
                <h:graphicImage id="img_firstgov" url="images/footer_firstgov.gif" width="91" height="31"
                                alt="FirstGov.gov" style="border: none;"/>
            </h:outputLink>
        </h:column>
    </h:panelGrid>
</f:subview>