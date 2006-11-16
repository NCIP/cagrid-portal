<%@ taglib prefix="h" uri="http://java.sun.com/jsf/html" %>
<%@ taglib prefix="f" uri="http://java.sun.com/jsf/core" %>
<%@ taglib uri="http://myfaces.apache.org/tomahawk" prefix="t" %>


<f:subview id="keywordSearch">


    <h:panelGrid styleClass="contentMainTable" headerClass="contentMainTitle" columns="1">

        <f:facet name="header">
            <h:column>
                <h:outputText value="#{labels.keywordSearchTitle}"/>
            </h:column>
        </f:facet>


        <f:verbatim><br/></f:verbatim>
        <%/*-- Scroller to scroll through results */%>
        <h:column>
            <h:panelGrid styleClass="contentInnerTable"
                         rowClasses="dataRowLight,dataRowDark"

                         cellpadding="2" cellspacing="2"
                         headerClass="contentTableHeader" columns="2">
                <f:facet name="header">
                    <h:column>
                        <h:outputText value="#{labels.search}"/>
                    </h:column>
                </f:facet>

                <h:column>
                    <h:outputText value="#{labels.keyword}" styleClass="dataCellTextBold"/>
                </h:column>
                <h:column>
                    <h:inputText id="searchKeyword" required="true" value="#{keywordSearch.searchKeyword}"
                                 size="50"/>
                    <h:outputText value="#{labels.searchExample}" styleClass="mainContent"/>
                </h:column>

                <h:column>
                    <h:outputText value="search category" styleClass="dataCellText"/>
                </h:column>

                <h:column>
                    <t:selectManyCheckbox value="#{keywordSearch.searchCategorySelected}" styleClass="dataCellText">
                        <f:selectItems value="#{keywordSearch.searchCategoryItems}"/>
                    </t:selectManyCheckbox>
                </h:column>


                <h:column/>
                <h:column>
                    <h:commandButton value="#{labels.search}"
                                     action="#{keywordSearch.navigateToKeywordSearchResults}"
                                     styleClass="searchButton"/>
                </h:column>

            </h:panelGrid>


        </h:column>

    </h:panelGrid>

</f:subview>