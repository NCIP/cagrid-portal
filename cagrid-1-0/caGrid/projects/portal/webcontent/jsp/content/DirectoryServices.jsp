<%@ taglib prefix="h" uri="http://java.sun.com/jsf/html" %>
<%@ taglib prefix="f" uri="http://java.sun.com/jsf/core" %>
<%@ taglib uri="http://myfaces.apache.org/tomahawk" prefix="t" %>


<f:subview id="servicesDirectory">

<h:panelGrid style="height:100%;width:100%;" cellpadding="0" cellspacing="0" border="0"
             headerClass="homepageTitle"
             columns="1">
<f:facet name="header">
    <h:column>
        <h:outputText value="Registered Service Directory"/>
    </h:column>
</f:facet>


<f:verbatim><br/></f:verbatim>
<!-- Scroller to scroll through results -->
<h:column>
    <h:panelGrid columns="1">
        <t:dataScroller id="scroller1"
                        for="serviceData"
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
                    for="serviceData"
                    rowsCountVar="rowsCount"
                    displayedRowsCountVar="displayedRowsCountVar"
                    firstRowIndexVar="firstRowIndex"
                    lastRowIndexVar="lastRowIndex"
                    pageCountVar="pageCount"
                    immediate="true"
                    pageIndexVar="pageIndex"
            >
        <h:outputText styleClass="scrollerStyle2"
                      value="Found #{rowsCount} services. Displaying #{displayedRowsCountVar} from
                                    #{firstRowIndex} to #{lastRowIndex}"/>
    </t:dataScroller>
</h:column>

<h:column>
    <f:verbatim><br/><br/></f:verbatim>
</h:column>


<h:column>
    <t:dataTable id="serviceData" var="service" value="#{portal.services}"
                 rows="6" width="80%">

        <h:column>
            <h:panelGrid width="100%" border="1" cellpadding="3" cellspacing="0"
                         rowClasses="dataRowLight,dataRowDark"
                         columnClasses="dataCellTextBold,dataCellText"
                         headerClass="dataTableHeader" columns="2">
                <f:facet name="header">
                    <h:column>
                        <h:outputText value="Service Details"/>
                    </h:column>

                </f:facet>

                <h:column>
                    <h:outputText value="Name"/>
                </h:column>
                <h:column>
                    <h:outputText value="#{service.name}"/>
                </h:column>

                <h:column>
                    <h:outputText value="URL"/>
                </h:column>
                <h:column>
                    <h:outputText value="#{service.EPR}"/>
                </h:column>

                <h:column>
                    <h:outputText value="Description"/>
                </h:column>
                <h:column>
                    <h:outputText value="#{service.description}"/>
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