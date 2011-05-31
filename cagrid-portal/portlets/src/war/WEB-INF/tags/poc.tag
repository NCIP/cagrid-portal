<%@tag %>
<%@taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@taglib prefix="tags" tagdir="/WEB-INF/tags" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<%@attribute name="poc" required="true" description="Point of Contact"
        type="gov.nih.nci.cagrid.portal.domain.metadata.common.PointOfContact" %>

<c:set var="currID" value="${poc.id}"/>

            <a id="${currID}-lnk" href="javascript:void(0)">${poc.person.firstName} ${poc.person.lastName}</a>

            <div class="yui-skin-sam">
                <div id="${currID}-Panel" style="visibility:collapse;">
                    <div class="hd">
                        Point of Contact
                    </div>
                    <div class="bd">
                        <tags:notEmptyRow rowValue="${poc.person.firstName}" rowLabel="First Name:"/>
                        <tags:notEmptyRow rowValue="${poc.person.lastName}" rowLabel="Last Name:"/>
                        <tags:notEmptyRow rowValue="${poc.role}" rowLabel="Role:"/>
                        <tags:notEmptyRow rowValue="${poc.affiliation}" rowLabel="Affiliation:"/>
                        <tags:notEmptyRow rowValue="${poc.person.phoneNumber}" rowLabel="Phone:"/>
                        <tags:notEmptyRow rowValue="${poc.person.emailAddress}" rowLabel="Email:"/>
                    </div>
                </div>
            </div>

            <script type="text/javascript">
                function init() {
                    myPanel = new YAHOO.widget.Panel("${currID}-Panel", { underlay:"none", visible:false, constraintoviewport:true });
                    myPanel.render();
                    myPanel.show;
                    YAHOO.util.Event.addListener("${currID}-lnk", "click", myPanel.show, myPanel, true);
                }

                YAHOO.util.Event.addListener(window, "load", init);

            </script>