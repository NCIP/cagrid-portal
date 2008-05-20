package gov.nih.nci.cagrid.portal.portlet.query.dcql;

import gov.nih.nci.cagrid.portal.domain.metadata.common.UMLAttribute;
import gov.nih.nci.cagrid.portal.domain.metadata.dataservice.UMLClass;
import gov.nih.nci.cagrid.portal.portlet.query.cql.UMLClassBean;

/**
 * User: kherm
 *
 * @author kherm manav.kher@semanticbits.com
 */
public class ForeignUMLClassBean extends UMLClassBean {

    private JoinCondition join = new JoinCondition();


    public ForeignUMLClassBean(UMLClass umlClass) {
        super(umlClass);
    }

    public ForeignUMLClassBean(UMLClass umlClass, UMLAttribute leftJoinAttr) {
        this(umlClass);
        if (super.getAttributes().size() > 0)
            join = new JoinCondition(leftJoinAttr.getName(), getAttributes().get(0).getName(), "EQUAL_TO");
    }

    public JoinCondition getJoin() {
        return join;
    }

    public void setJoin(JoinCondition join) {
        this.join = join;
    }
}
