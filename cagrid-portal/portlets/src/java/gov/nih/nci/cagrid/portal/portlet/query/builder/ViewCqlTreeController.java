/**
*============================================================================
*  The Ohio State University Research Foundation, The University of Chicago -
*  Argonne National Laboratory, Emory University, SemanticBits LLC, 
*  and Ekagra Software Technologies Ltd.
*
*  Distributed under the OSI-approved BSD 3-Clause License.
*  See http://ncip.github.com/cagrid-portal/LICENSE.txt for details.
*============================================================================
**/
/**
 * 
 */
package gov.nih.nci.cagrid.portal.portlet.query.builder;

import gov.nih.nci.cagrid.portal.dao.UMLClassDao;
import gov.nih.nci.cagrid.portal.domain.metadata.dataservice.UMLClass;
import gov.nih.nci.cagrid.portal.portlet.query.AbstractQueryRenderController;
import gov.nih.nci.cagrid.portal.portlet.query.cql.CQLQueryBean;
import gov.nih.nci.cagrid.portal.portlet.query.cql.CQLQueryTreeNodeListener;
import gov.nih.nci.cagrid.portal.portlet.tree.TreeFacade;
import gov.nih.nci.cagrid.portal.portlet.tree.TreeNode;

import java.util.HashMap;

import javax.portlet.RenderRequest;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Required;

/**
 * @author <a href="mailto:joshua.phillips@semanticbits.com">Joshua Phillips</a>
 * 
 */
public class ViewCqlTreeController extends AbstractQueryRenderController {

	private static final Log logger = LogFactory
			.getLog(ViewCqlTreeController.class);

	private TreeFacade cqlQueryTreeFacade;
	private CQLQueryTreeNodeListener cqlQueryTreeNodeListener;
	private UMLClassDao umlClassDao;

	/**
	 * 
	 */
	public ViewCqlTreeController() {

	}

	@Override
	protected Object getObject(RenderRequest request) {
		TreeNode rootNode = null;
		UMLClass umlClass = getUserModel().getSelectedUmlClass();
		if (umlClass == null) {
			logger.debug("no UMLClass selected");
		} else {
			logger.debug("UMLClass:" + umlClass.getId() + " selected");
			//Associate with current session
			umlClass = getUmlClassDao().getById(umlClass.getId());
			rootNode = getCqlQueryTreeFacade().getRootNode();
			if(rootNode == null || !umlClass.getId().equals(((CQLQueryBean)rootNode.getContent()).getUmlClass().getId())){
				logger.debug("Creating new tree for UMLClass: " + umlClass.getId());
				rootNode = createNode(umlClass);
			}
			getCqlQueryTreeFacade().setRootNode(rootNode);
		}

		return rootNode;
	}

	private TreeNode createNode(UMLClass umlClass) {
		TreeNode rootNode = new TreeNode(null, "UMLClass:" + umlClass.getId());
		rootNode.setLabel(umlClass.getClassName());
		CQLQueryBean bean = (CQLQueryBean) getApplicationContext().getBean("cqlQueryBeanPrototype");
		bean.setUmlClass(umlClass);
		rootNode.setContent(bean);
		getCqlQueryTreeNodeListener().onOpen(rootNode, new HashMap());
		return rootNode;
	}

	@Required
	public TreeFacade getCqlQueryTreeFacade() {
		return cqlQueryTreeFacade;
	}

	public void setCqlQueryTreeFacade(TreeFacade cqlQueryTreeFacade) {
		this.cqlQueryTreeFacade = cqlQueryTreeFacade;
	}

	@Required
	public CQLQueryTreeNodeListener getCqlQueryTreeNodeListener() {
		return cqlQueryTreeNodeListener;
	}

	public void setCqlQueryTreeNodeListener(
			CQLQueryTreeNodeListener cqlQueryTreeNodeListener) {
		this.cqlQueryTreeNodeListener = cqlQueryTreeNodeListener;
	}

	@Required
	public UMLClassDao getUmlClassDao() {
		return umlClassDao;
	}

	public void setUmlClassDao(UMLClassDao umlClassDao) {
		this.umlClassDao = umlClassDao;
	}
}
