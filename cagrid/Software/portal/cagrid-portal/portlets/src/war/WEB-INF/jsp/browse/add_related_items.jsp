<%@ include file="/WEB-INF/jsp/include/servlet_includes.jsp" %>


<c:set var="ns" value="${param.ns}"/>

<script type="text/javascript"
        src="<c:url value="/dwr/interface/CatalogEntryManagerFacade.js"/>"></script>

<div style="overflow:auto; height:400px;">

    <div style="text-align:left;" class="yui-skin-sam">

        <h3>
            Add a Related Item
        </h3>

        <p>
            First, choose the kind of catalog entry for which you would like to describe a relationship.
            Then, select the kind of relationship that you would like to describe.
        </p>

        <p>

        <div id="${ns}addRelationshipButtonContainer"></div>
        </p>
        <br/>

        <form name="${ns}addRelatedItemsForm">

            <%@ include file="/WEB-INF/jsp/browse/entry_types.jspf" %>

            <b>Kinds of Catalog Entries</b><br/>

            <c:forEach var="entryType" items="${entryTypes}">
                <tags:roleTypeSelector
                        input_name="entryType"
                        id_prefix="${ns}"
                        entry_type="${entryType[0]}"/>
            </c:forEach>

        </form>

    </div>
</div>

<script type="text/javascript">

    var ${ns}prevDisplayDiv = null;
    function ${ns}populateLists(entryType) {
        CatalogEntryManagerFacade.renderRoleTypesForType(entryType, "${ns}",
        {
            callback:function(html) {
                var theId = "#${ns}" + entryType.replace(/\./g, "_") + "_roleTypesContainer";
                if (${ns}prevDisplayDiv != null) {
                    jQuery(${ns}prevDisplayDiv).html("");
                }
                ${ns}prevDisplayDiv = theId;
                jQuery(theId).html(html);
                if (jQuery("form[name='${ns}addRelatedItemsForm']  :input[name='roleType']").get().length > 0) {
                    ${ns}addRelationshipButton.set("disabled", false);
                } else {
                    ${ns}addRelationshipButton.set("disabled", true);
                }
            },
            errorHandler:function(errorString, exception) {
                alert("Error rendering role types: " + errorString);
            }
        });
    }

    var ${ns}addRelationshipButton = null;

    jQuery(document).ready(function() {


        ${ns}populateLists('gov.nih.nci.cagrid.portal.domain.catalog.CommunityCatalogEntry');

        jQuery("form[name='${ns}addRelatedItemsForm']  :input[name='entryType']").bind('change', function(evt) {
            ${ns}populateLists(evt.target.value);
        });

        ${ns}addRelationshipButton = new YAHOO.widget.Button({
            label: "Add",
            id: "${ns}addRelationshipButton",
            container: "${ns}addRelationshipButtonContainer"
        });
        ${ns}addRelationshipButton.set("disabled", true);

        ${ns}addRelationshipButton.on("click", function(evt) {
            ${ns}addRelationship(jQuery("form[name='${ns}addRelatedItemsForm']  :input[name='roleType']").val());
        });


    });
</script>

