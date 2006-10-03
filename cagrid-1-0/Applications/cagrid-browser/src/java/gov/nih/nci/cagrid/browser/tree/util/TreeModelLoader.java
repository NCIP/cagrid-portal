package gov.nih.nci.cagrid.browser.tree.util;

import gov.nih.nci.cagrid.browser.beans.GSH;
import gov.nih.nci.cagrid.metadata.common.UMLClass;
import gov.nih.nci.cagrid.metadata.dataservice.DomainModel;

/**
 * Loads domain model to form a tree. Interface can be implemented to specific loading methods
 * Created by IntelliJ IDEA.
 * User: kherm
 * Date: Mar 22, 2006
 * Time: 1:24:58 PM
 * To change this template use File | Settings | File Templates.
 */
public interface TreeModelLoader {


    /**
     * @return The base object model that the tree is built on
     */
    public DomainModel getTargetModel();

    public GSH getTargetGSH();

    /**
     * @return Target object for the tree
     */
    public UMLClass getTargetObject();

}
