package gov.nih.nci.cagrid.solr;

import org.apache.solr.common.SolrDocument;

/**
 * User: kherm
 *
 * @author kherm manav.kher@semanticbits.com
 */
public class TreeNode extends SolrDocument {

    private String name;
    private String label;
    private int count;

    public TreeNode(String name, String label) {
        setName(name);
        setLabel(label);
        setCount(count);
    }


    public String getName() {
        return name;
    }

    public void setName(String name) {
        setField("name", name);
        this.name = name;
    }

    public String getLabel() {
        return label;
    }

    public void setLabel(String label) {
        setField("label", label);
        this.label = label;
    }

    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        setField("count", String.valueOf(count));
        this.count = count;
    }
}
