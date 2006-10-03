package gov.nih.nci.cagrid.browser.tree.util;

import gov.nih.nci.cagrid.browser.beans.GSH;
import gov.nih.nci.cagrid.browser.exception.GridServiceNotAvailableException;
import gov.nih.nci.cagrid.browser.tree.node.NonLeafTreeNode;
import gov.nih.nci.cagrid.browser.util.ApplicationCtx;
import gov.nih.nci.cagrid.metadata.common.UMLClass;
import gov.nih.nci.cagrid.metadata.dataservice.DomainModel;

import org.apache.axis.types.NonNegativeInteger;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;

/**
 * Tree Model loader for the cagrid-browser
 * 
 * Created by IntelliJ IDEA.
 * User: kherm
 * Date: Mar 22, 2006
 * Time: 1:27:38 PM
 * To change this template use File | Settings | File Templates.
 */
public class BrowserTreeModelLoader implements TreeModelLoader {


    public BrowserTreeModelLoader() {

    }


    public DomainModel getTargetModel() {
        return getTargetGSH().getDomainModel();
    }

    public GSH getTargetGSH() {
        return (GSH) ApplicationCtx.getBean("#{discoveredServices.navigatedService}");
    }

    public UMLClass getTargetObject() {
        return this.getTargetGSH().getNavigatedObject();
    }
}




