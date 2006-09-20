<%@ taglib prefix="h" uri="http://java.sun.com/jsf/html" %>
<%@ taglib prefix="f" uri="http://java.sun.com/jsf/core" %>
<%@ taglib uri="http://myfaces.apache.org/tomahawk" prefix="t" %>


<f:subview id="rcDirectory">

<h:panelGrid styleClass="contentMainTable" headerClass="contentMainTitle" columns="1">

<f:facet name="header">
    <h:column>
        <h:outputText value="Research Center Directory"/>
    </h:column>
</f:facet>

<f:verbatim><br/></f:verbatim>

<!-- Scroller to scroll through results -->
<h:column>
    <h:panelGrid columns="1">
        <t:dataScroller id="scroller1"
                        for="rcData"
                        fastStep="6"
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
    </h:panelGrid>
</h:column>

<h:column>
    <t:dataScroller id="scroller2"
                    for="rcData"
                    rowsCountVar="rowsCount"
                    displayedRowsCountVar="displayedRowsCountVar"
                    firstRowIndexVar="firstRowIndex"
                    lastRowIndexVar="lastRowIndex"
                    pageCountVar="pageCount"
                    immediate="true"
                    pageIndexVar="pageIndex"
            >
        <h:outputText styleClass="scrollerStyle2"
                      value="Found #{rowsCount} Research Centers. Displaying #{displayedRowsCountVar} from
                                    #{firstRowIndex} to #{lastRowIndex}"/>
    </t:dataScroller>
</h:column>
<f:verbatim><br/></f:verbatim>

<h:column>
    <t:dataTable styleClass="contentInnerTable" id="rcData" var="rc" value="#{portal.rc}"
                 rows="6">

        <h:column>
            <h:panelGrid styleClass="contentMainTable" border="1" cellpadding="3" cellspacing="0"
                         rowClasses="dataRowLight,dataRowDark"
                         columnClasses="dataCellTextBold,dataCellText"
                         headerClass="dataTableHeader" columns="2">
                <f:facet name="header">
                    <h:column>
                        <h:outputText value="Center Details"/>
                    </h:column>

                </f:facet>

                <h:column>
                    <h:outputText value="Name"/>
                </h:column>
                <h:column>
                    <h:outputText value="#{rc.displayName} (#{rc.shortName})"/>
                </h:column>

                <h:column>
                    <h:outputText value="Description"/>
                </h:column>
                <h:column>
                    <h:outputText value="#{rc.description}"/>
                </h:column>

                <h:column>
                    <h:outputText value="Homepage URL"/>
                </h:column>
                <h:column>
                    <h:outputLink target="new" value="#{rc.homepageURL}"/>
                </h:column>


                <h:column/>
                <h:column>
                    <h:outputLink>
                        <h:outputText value="More Details>>"/>
                    </h:outputLink>
                </h:column>

            </h:panelGrid>
            <f:verbatim><br/><br/></f:verbatim>
        </h:column>

    </t:dataTable>


</h:column>


</h:panelGrid>
</f:subview>