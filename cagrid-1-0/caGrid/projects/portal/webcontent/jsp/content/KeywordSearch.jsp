<%@ taglib prefix="h" uri="http://java.sun.com/jsf/html" %>
<%@ taglib prefix="f" uri="http://java.sun.com/jsf/core" %>
<%@ taglib uri="http://myfaces.apache.org/tomahawk" prefix="t" %>


<f:subview id="keywordSearch">
    <h:form>

        <h:panelGrid styleClass="contentMainTable" headerClass="contentMainTitle" columns="1">

            <f:facet name="header">
                <h:column>
                    <h:outputText value="Keyword Search"/>
                </h:column>
            </f:facet>


            <f:verbatim><br/></f:verbatim>
            <%/*-- Scroller to scroll through results */%>
            <h:column>
                <h:panelGrid styleClass="contentInnerTable"
                             rowClasses="dataRowLight,dataRowDark"
                             columnClasses="dataCellTextBold,dataCellText"
                             headerClass="dataTableHeader" columns="2">
                    <f:facet name="header">
                        <h:column>
                            <h:outputText value="Search"/>
                        </h:column>
                    </f:facet>

                    <h:column>
                        <h:outputText value="Keyword:"/>
                    </h:column>
                    <h:column>
                        <h:inputText id="searchKeyword" required="true" value="#{keywordSearch.searchKeyword}"
                                     size="50"/>
                    </h:column>

                    <h:column>
                        <f:verbatim><br/><br/></f:verbatim>
                    </h:column>

                    <h:column>
                        <f:verbatim>&nbsp;&nbsp;</f:verbatim>
                        <h:commandButton value="Search"
                                         action="#{keywordSearch.navigateToKeywordSearchResults}"
                                         styleClass="searchButton"/>
                    </h:column>

                </h:panelGrid>


            </h:column>

        </h:panelGrid>
    </h:form>
</f:subview>