/**
 *
 */
package gov.nih.nci.cagrid.portal.portlet.query.builder;

import gov.nih.nci.cagrid.portal.domain.metadata.common.UMLAttribute;
import gov.nih.nci.cagrid.portal.portlet.query.AbstractQueryActionController;
import gov.nih.nci.cagrid.portal.portlet.query.cql.CriteriaBean;
import gov.nih.nci.cagrid.portal.portlet.query.cql.CriterionBean;
import gov.nih.nci.cagrid.portal.portlet.query.cql.UMLClassBean;
import gov.nih.nci.cagrid.portal.portlet.query.dcql.ForeignUMLClassBean;
import gov.nih.nci.cagrid.portal.portlet.tree.TreeFacade;
import gov.nih.nci.cagrid.portal.portlet.tree.TreeNode;
import org.springframework.beans.factory.annotation.Required;
import org.springframework.validation.BindException;

import javax.portlet.ActionRequest;
import javax.portlet.ActionResponse;

/**
 * @author <a href="mailto:joshua.phillips@semanticbits.com">Joshua Phillips</a>
 */
public class SelectCriterionController extends
        AbstractQueryActionController {

    private TreeFacade umlClassTreeFacade;
    private TreeFacade cqlQueryTreeFacade;

    /**
     *
     */
    public SelectCriterionController() {

    }

    /**
     * @param commandClass
     */
    public SelectCriterionController(Class commandClass) {
        super(commandClass);

    }

    /**
     * @param commandClass
     * @param commandName
     */
    public SelectCriterionController(Class commandClass, String commandName) {
        super(commandClass, commandName);
    }

    /*
      * (non-Javadoc)
      *
      * @see gov.nih.nci.cagrid.portal.portlet.AbstractActionResponseHandlerCommandController#doHandleAction(javax.portlet.ActionRequest,
      *      javax.portlet.ActionResponse, java.lang.Object,
      *      org.springframework.validation.BindException)
      */
    @Override
    protected void doHandleAction(ActionRequest request,
                                  ActionResponse response, Object obj, BindException errors)
            throws Exception {

        SelectCriterionCommand command = (SelectCriterionCommand) obj;

        CriterionBean bean = null;

        String path = command.getPath();


        int sepIdx = path.lastIndexOf("/");
        String umlClassPath = path.substring(0, sepIdx);
        String umlAttName = path.substring(sepIdx + 1);

        // First check the working query to see if we already have it
        TreeNode node = getCqlQueryTreeFacade().getRootNode()
                .find(umlClassPath);
        if (node != null) {
            if (!(node.getContent() instanceof CriteriaBean)) {
                throw new Exception("node content for "
                        + path
                        + " not instance of CriteriaBean. got "
                        + (node.getContent() == null ? null : node.getContent()
                        .getClass().getName()));
            }
            logger.debug("Found existing node for path " + path);
            //Check if criterion has been specified
            CriteriaBean criteria = (CriteriaBean) node.getContent();
            for (CriterionBean crit : criteria.getCriteria()) {
                if (umlAttName.equals(crit.getUmlAttribute().getName())) {
                    logger.debug("Found existing criterion for " + umlAttName);
                    bean = crit;
                    break;
                }
            }
        }

        if (bean == null) {

            logger.debug("No existing criterion bean found for " + path);
            bean = new CriterionBean();
            node = getUmlClassTreeFacade().getRootNode().find(umlClassPath);
            if (node == null) {
                throw new Exception("Couldn't find node for " + path);
            }
            UMLAttribute selectedAtt = null;

            // if foreign association then save the join           
            if (node.getContent() instanceof ForeignUMLClassBean) {
                ForeignUMLClassBean fumlClassBean = (ForeignUMLClassBean) node.getContent();
                bean.setJoin(fumlClassBean.getJoin());
            }

            UMLClassBean umlClassBean = (UMLClassBean) node.getContent();
            for (UMLAttribute att : umlClassBean.getAttributes()) {
                if (umlAttName.equals(att.getName())) {
                    selectedAtt = att;
                    break;
                }
            }
            if (selectedAtt == null) {
                throw new Exception("No attribute found for " + path);
            }
            logger.debug("selectedAtt = " + selectedAtt.getName() + ":"
                    + selectedAtt.getId());

            bean.setUmlAttribute(selectedAtt);
        }

        bean.setPath(path);
        getQueryModel().setSelectedCriterion(bean);
    }

    @Required
    public TreeFacade getUmlClassTreeFacade() {
        return umlClassTreeFacade;
    }

    public void setUmlClassTreeFacade(TreeFacade umlClassTreeFacade) {
        this.umlClassTreeFacade = umlClassTreeFacade;
    }

    @Required
    public TreeFacade getCqlQueryTreeFacade() {
        return cqlQueryTreeFacade;
    }

    public void setCqlQueryTreeFacade(TreeFacade cqlQueryTreeFacade) {
        this.cqlQueryTreeFacade = cqlQueryTreeFacade;
    }

}
