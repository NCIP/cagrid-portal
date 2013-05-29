/**
*============================================================================
*  The Ohio State University Research Foundation, The University of Chicago -
*  Argonne National Laboratory, Emory University, SemanticBits LLC, 
*  and Ekagra Software Technologies Ltd.
*
*  Distributed under the OSI-approved BSD 3-Clause License.
*  See http://ncip.github.com/cagrid-core/LICENSE.txt for details.
*============================================================================
**/
package gov.nih.nci.cagrid.solr;

import org.apache.solr.common.SolrDocument;

import java.util.Iterator;
import java.util.Set;
import java.util.TreeSet;

/**
 * User: kherm
 *
 * @author kherm manav.kher@semanticbits.com
 */
public class TreeBean extends SolrDocument {

    private Set<TreeNode> nodeList = new TreeSet<TreeNode>();
    public static final String NODE_LABEL = "node";

    public TreeBean(TreeDescriptor desc) {
        setField("label", desc.getLabel());
        setField("type", desc.getType());
        setField("nodes", nodeList);
    }

    public void addNode(TreeNode n) {
        this.nodeList.add(n);
    }

    public TreeNode getByName(String name) {
        TreeNode returnNode = null;
        for (Iterator iter = this.nodeList.iterator(); iter.hasNext();) {
            TreeNode node = (TreeNode) iter.next();
            if (node.getName().equalsIgnoreCase(name))
                returnNode = node;

        }

        return returnNode;
    }

    public TreeNode getByLabel(String label) {
        TreeNode returnNode = null;
        for (Iterator iter = this.nodeList.iterator(); iter.hasNext();) {
            TreeNode node = (TreeNode) iter.next();
            if (node.getLabel().equalsIgnoreCase(label))
                returnNode = node;

        }

        return returnNode;
    }

}
