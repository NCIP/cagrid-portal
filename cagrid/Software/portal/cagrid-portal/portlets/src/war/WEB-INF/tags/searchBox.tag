<%@tag %>
<%@taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@taglib prefix="tags" tagdir="/WEB-INF/tags" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<%@attribute name="searchCallback" required="true" description="Callback function to run the search" %>
<%@attribute name="id" required="true" %>


<link rel="stylesheet" type="text/css" href="<c:url value="/js/yui/container/assets/skins/sam/container.css"/>"/>
<script type="text/javascript" src="<c:url value="/js/yui/yahoo-dom-event/yahoo-dom-event.js"/>"></script>
<script type="text/javascript" src="<c:url value="/js/yui/container/container-min.js"/>"></script>
<script type="text/javascript" src="<c:url value="/js/yui/animation/animation-min.js"/>"></script>


<div class="searchBox" id="searchBox">
    <div class="L-endcap"></div>
    <input id="<c:out value="${id}"/>"  alt="Search" class="search" type="text" size="19"
           value="Catalog Search" style="color:#afafaf;"
           onkeypress="return <c:out value="${searchCallback}(event,this.value);"/>
    "/>

    <div class="R-endcap">

    </div>
    <div class="R-tip">
        <a href="tips" onclick="showHelpTips();return false;"><tags:image name="help.gif" alt="Help"/></a>
    </div>
</div>
<span id="tipWindow" class="yui-skin-sam">
</span>


<script type="text/javascript">
    document.getElementById("<c:out value="${id}"/>").onfocus = function() {
        this.value = "";
        this.style.color = "#000000";
        return false;
    };
    document.getElementById("<c:out value="${id}"/>").onblur = function() {
        this.style.color = "#AFAFAF";
        this.value = "Search";
    };

    function showHelpTips() {

        jQuery("#helpTipsContainer").load('<c:url value="/browse/comment/searchTips.html"/>', {});


        myPanel = new YAHOO.widget.Panel("win", {
            width: "400px",
            constraintoviewport: true,
            underlay: "shadow",
            close: true,
            visible: true,
            context:["searchBox","tr","br"],
            modal: true,
            draggable: true
        });

        myPanel.setHeader("Search Tips");
        myPanel.setBody(jQuery("#helpTipsContainer").html());
        myPanel.render("tipWindow");
        return false;

    }
</script>

<div id="helpTipsContainer" style="display:none;">
    <div id="helpTipsContent">
        <div>
            <h4>Boolean Operators and Grouping</h4>
        </div>
        <div>
            Boolean operators allow terms to be combined through logic operators. Portal Search supports AND, OR, NOT,
            "+", and "-" as
            Boolean operators.
        </div>
        <div>
            <b>Note: Boolean operators AND, OR, NOT must be in ALL CAPS otherwise they are interpreted as search
                terms.</b>
        </div>
        <div>
            <span>
            <b>Example:</b>
             </span>
            <span>
                <div>
                    grid AND service
                </div>
                <div>grid OR service
                </div>
                 <div>grid AND service NOT cabio
                 </div>
            </span>
        </div>
        <br/>

        <div>
            <div>
                <h4>Search Terms and Phrases</h4>
            </div>

            <div>When you are entering a search item (for instance data in a keyword search field), you will want to
                think
                of
                your search as being comprised of two components: terms and operators.
            </div>

            <div>There are two types of terms: Single Terms and Phrases:</div>
            <ul>
                <li>A
                    Single Term is a single word such as <i>test</i> or <i>hello</i>.
                </li>
            </ul>

            <ul>
                <li>A Phrase is a group of words surrounded by double quotes such as <i>"grid service"</i>. Multiple
                    terms can be combined together with Boolean operators to form a more complex query.
                </li>
            </ul>

        </div>

    </div>

</div>
