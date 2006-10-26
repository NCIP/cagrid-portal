<%@ taglib prefix="h" uri="http://java.sun.com/jsf/html" %>
<%@ taglib prefix="f" uri="http://java.sun.com/jsf/core" %>
<%@ taglib uri="http://myfaces.apache.org/tomahawk" prefix="t" %>

<f:loadBundle basename="Portal-Labels" var="labels"/>

<f:subview id="participantDirectory">

<h:panelGrid styleClass="contentMainTable" headerClass="contentMainTitle" columns="1">

<f:facet name="header">
    <h:column>
        <h:outputText value="#{labels.participantDirectory}"/>
    </h:column>
</f:facet>

<%/* Scroller to scroll through results */%>
<h:column>
    <h:panelGrid columns="1" align="right">
        <h:column>
            <t:dataScroller id="scroller1"
                            for="participantData"
                            fastStep="3"
                            pageCountVar="pageCount"
                            pageIndexVar="pageIndex"
                            paginator="true"
                            paginatorMaxPages="9"
                            paginatorTableClass="paginator"
                            paginatorActiveColumnStyle="font-weight:bold;"
                            paginatorColumnClass="scrollerStyle1"
                    >
                <f:facet name="first">
                    <t:graphicImage url="images/scroller/arrow-first.gif" alt="First" border="1"/>
                </f:facet>
                <f:facet name="last">
                    <t:graphicImage url="images/scroller/arrow-last.gif" alt="Last" border="1"/>
                </f:facet>
                <f:facet name="previous">
                    <t:graphicImage url="images/scroller/arrow-previous.gif" alt="Previous" border="1"/>
                </f:facet>
                <f:facet name="next">
                    <t:graphicImage url="images/scroller/arrow-next.gif" alt="Next" border="1"/>
                </f:facet>
                <f:facet name="fastforward">
                    <t:graphicImage url="images/scroller/arrow-ff.gif" alt="Fast Forward" border="1"/>
                </f:facet>
                <f:facet name="fastrewind">
                    <t:graphicImage url="images/scroller/arrow-fr.gif" alt="Fast Rewind" border="1"/>
                </f:facet>
            </t:dataScroller>

        </h:column>

        <h:column>
            <t:dataScroller id="scroller2"
                            for="participantData"
                            rowsCountVar="rowsCount"
                            displayedRowsCountVar="displayedRowsCountVar"
                            firstRowIndexVar="firstRowIndex"
                            lastRowIndexVar="lastRowIndex"
                            pageCountVar="pageCount"
                            immediate="true"
                            pageIndexVar="pageIndex"
                    >
                <h:outputText styleClass="scrollerStyle2"
                              value="Found #{rowsCount} People. Displaying #{firstRowIndex} to #{lastRowIndex}"/>
            </t:dataScroller>
        </h:column>
    </h:panelGrid>
</h:column>

<h:column>
    <t:dataTable styleClass="contentMainTable" id="participantData" var="participant"
                 value="#{participants.list}"
                 rows="5">

        <h:column>
            <h:panelGrid styleClass="contentInnerTable" border="0"
                         rowClasses="dataRowLight,dataRowDark"
                         columnClasses="dataCellTextBold,dataCellText"
                         headerClass="contentTableHeader" columns="2">
                <f:facet name="header">
                    <h:column>
                        <h:outputText value="#{labels.participantsDetails}"/>
                    </h:column>

                </f:facet>

                <h:column>
                    <h:outputText value="#{labels.name}"/>
                </h:column>
                <h:column>
                    <h:outputText value="#{participant.name}"/>
                </h:column>

                <h:column>
                    <h:outputText value="#{labels.participantInstitute}"/>
                </h:column>
                <h:column>
                    <h:outputText value="#{participant.institute}"/>
                </h:column>


                <h:column>
                    <h:outputText value="#{labels.workspace}"/>
                </h:column>
                <h:column>
                    <t:dataTable var="workspace" value="#{participant.workspaceCollection}">
                        <h:outputText value="#{workspace.shortName}"/>
                    </t:dataTable>
                </h:column>


                <h:column/>
                <h:column>
                    <h:commandLink action="#{participants.navigateToParticipant}">
                        <h:outputText styleClass="txtHighlight"
                                      onmouseover="changeMenuStyle(this,'txtHighlightOn'),showCursor()"
                                      onmouseout="changeMenuStyle(this,'txtHighlight'),hideCursor()"
                                      value="#{labels.moreDetails}"/>
                        />
                        <f:param id="navigateToParticipantPk" name="navigateToParticipantPk" value="#{participant.pk}"/>
                    </h:commandLink>
                </h:column>

            </h:panelGrid>
            <f:verbatim><br/></f:verbatim>
        </h:column>

    </t:dataTable>

</h:column>


</h:panelGrid>
</f:subview>