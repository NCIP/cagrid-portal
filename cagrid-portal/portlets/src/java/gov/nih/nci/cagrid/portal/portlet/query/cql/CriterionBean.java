/**
 *
 */
package gov.nih.nci.cagrid.portal.portlet.query.cql;

import gov.nih.nci.cagrid.portal.domain.metadata.common.UMLAttribute;
import gov.nih.nci.cagrid.portal.portlet.query.dcql.JoinCondition;

/**
 * @author <a href="mailto:joshua.phillips@semanticbits.com">Joshua Phillips</a>
 */
public class CriterionBean {

    //This is the full path, not just the umlAttribute path
    private String path;

    private UMLAttribute umlAttribute;
    private String predicate;
    private String value;
    private JoinCondition join;

    /**
     *
     */
    public CriterionBean() {

    }

    public UMLAttribute getUmlAttribute() {
        return umlAttribute;
    }

    public void setUmlAttribute(UMLAttribute umlAttribute) {
        this.umlAttribute = umlAttribute;
    }

    public String getPredicate() {
        return predicate;
    }

    public void setPredicate(String predicate) {
        this.predicate = predicate;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public boolean equals(Object o) {
        boolean eq = false;
        if (o instanceof CriterionBean) {
            CriterionBean b = (CriterionBean) o;
            eq = b.toString().equals(toString());
        }
        return eq;
    }

    public String toString() {
        String attName = null;
        int attId = -1;
        if (getUmlAttribute() != null) {
            attName = getUmlAttribute().getName();
            attId = getUmlAttribute().getId();
        }
        return "[" + attName + ":" + attId + "][" + getPredicate() + "][" + getValue() + "]";
    }

    public int hash() {
        return toString().hashCode();
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public JoinCondition getJoin() {
        return join;
    }

    public void setJoin(JoinCondition join) {
        this.join = join;
    }
}
