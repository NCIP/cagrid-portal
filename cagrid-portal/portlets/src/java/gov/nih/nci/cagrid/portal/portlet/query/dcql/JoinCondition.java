package gov.nih.nci.cagrid.portal.portlet.query.dcql;

/**
 * User: kherm
 *
 * @author kherm manav.kher@semanticbits.com
 */
public class JoinCondition {

    private String localAttributeName;
    private String foreignAttributeName;
    private String predicate;


    public JoinCondition() {
    }

    public JoinCondition(String localAttributeName, String foreignAttributeName, String predicate) {
        this.localAttributeName = localAttributeName;
        this.foreignAttributeName = foreignAttributeName;
        this.predicate = predicate;
    }

    public String getLocalAttributeName() {
        return localAttributeName;
    }

    public void setLocalAttributeName(String localAttributeName) {
        this.localAttributeName = localAttributeName;
    }

    public String getForeignAttributeName() {
        return foreignAttributeName;
    }

    public void setForeignAttributeName(String foreignAttributeName) {
        this.foreignAttributeName = foreignAttributeName;
    }

    public String getPredicate() {
        return predicate;
    }

    public void setPredicate(String predicate) {
        this.predicate = predicate;
    }
}
