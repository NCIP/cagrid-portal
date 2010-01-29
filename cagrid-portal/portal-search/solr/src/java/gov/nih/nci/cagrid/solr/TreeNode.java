package gov.nih.nci.cagrid.solr;

import org.apache.solr.common.SolrDocument;

/**
 * User: kherm
 *
 * @author kherm manav.kher@semanticbits.com
 */
public class TreeNode extends SolrDocument implements Comparable {

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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof TreeNode)) return false;

        TreeNode treeNode = (TreeNode) o;

        if (count != treeNode.count) return false;
        if (!label.equals(treeNode.label)) return false;
        if (name != null ? !name.equals(treeNode.name) : treeNode.name != null) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = name != null ? name.hashCode() : 0;
        result = 31 * result + label.hashCode();
        result = 31 * result + count;
        return result;
    }

    public int compareTo(Object o) {
        TreeNode treeNode = (TreeNode) o;
        return this.label.compareTo(treeNode.getLabel());
    }
}
