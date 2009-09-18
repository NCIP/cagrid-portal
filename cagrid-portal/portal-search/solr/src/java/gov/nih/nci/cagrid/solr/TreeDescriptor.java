package gov.nih.nci.cagrid.solr;

/**
 * User: kherm
 *
 * @author kherm manav.kher@semanticbits.com
 */
public class TreeDescriptor {

    private String type, label;

    public TreeDescriptor(String type, String label) {
        this.type = type;
        this.label = label;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getLabel() {
        return label;
    }

    public void setLabel(String label) {
        this.label = label;
    }


}
