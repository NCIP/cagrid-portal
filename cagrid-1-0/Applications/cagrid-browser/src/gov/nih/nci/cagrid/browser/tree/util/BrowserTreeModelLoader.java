package gov.nih.nci.cagrid.browser.tree.util;

import gov.nih.nci.cagrid.browser.beans.GSH;
import gov.nih.nci.cagrid.browser.beans.IndexService;
import gov.nih.nci.cagrid.browser.beans.KeywordSearchCriteria;
import gov.nih.nci.cagrid.browser.exception.GridServiceNotAvailableException;
import gov.nih.nci.cagrid.browser.tree.node.NonLeafTreeNode;
import gov.nih.nci.cagrid.browser.tree.node.ForeignObjectNode;
import gov.nih.nci.cagrid.browser.util.ApplicationCtx;
import gov.nih.nci.cagrid.browser.util.DSDiscoveryUtil;
import gov.nih.nci.cagrid.common.servicedata.DomainModelType;
import gov.nih.nci.cagrid.common.servicedata.DomainObjectType;
import gov.nih.nci.cagrid.common.servicedata.DomainObjectType_associationsList_associationElement;
import org.apache.axis.types.NonNegativeInteger;
import org.gridforum.ogsi.HandleType;

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


    public DomainModelType getTargetModel() {
        return getTargetGSH().getDomainModel();
    }

    public GSH getTargetGSH() {
        return (GSH) ApplicationCtx.getBean("#{discoveredServices.navigatedService}");
    }

    public DomainObjectType getTargetObject() {
        return this.getTargetGSH().getNavigatedObject();
    }

    /**
     * @return Collection of discovered GSH
     */
    public Collection discoverForeignServices(NonLeafTreeNode treeNode) {
        ArrayList models = new ArrayList();

        DomainObjectType_associationsList_associationElement[] associationList = getTargetObject().getAssociationsList().getAssociationElement();
        KeywordSearchCriteria keySC = (KeywordSearchCriteria) ApplicationCtx.getBean("#{keywordDiscoveryBean}");
        IndexService index = keySC.getBrowserConfig().getIndexService();
        DSDiscoveryUtil dataDiscUtil = new DSDiscoveryUtil(index);

        //todo convert to threaded model

        for (int i = 0; i < associationList.length; i++) {
            NonNegativeInteger objectID = associationList[i].getTarget().getDomainObjectRef().getId();

            Collection discoveryResult = dataDiscUtil.getServicesFromKeyword(objectID.toString());

            outer:
            for (Iterator iter = discoveryResult.iterator(); iter.hasNext();) {
                GSH externalGSH = new GSH((HandleType) iter.next());

                //Check to see its a foreign model
                if (!externalGSH.getURL().equals(getTargetGSH().getURL())) {
                    try {
                        externalGSH.fillInMetadata();
                    } catch (GridServiceNotAvailableException e) {
                        continue outer;
                    }
                    DomainObjectType foreignObject = TreeUtil.getObjectFromModel(externalGSH.getDomainModel(), objectID);

                    if (foreignObject != null) {
                        ForeignObjectNode foreignObjectNode = new ForeignObjectNode(externalGSH, foreignObject, treeNode);
                        treeNode.getChildren().add(foreignObjectNode);
                    }
                }

            }

        }
        return models;
    }
}




