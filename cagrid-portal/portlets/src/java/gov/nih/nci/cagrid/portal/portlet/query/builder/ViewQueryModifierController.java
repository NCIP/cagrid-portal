/**
 * 
 */
package gov.nih.nci.cagrid.portal.portlet.query.builder;

import gov.nih.nci.cagrid.portal.dao.UMLClassDao;
import gov.nih.nci.cagrid.portal.domain.metadata.common.UMLAttribute;
import gov.nih.nci.cagrid.portal.domain.metadata.dataservice.UMLClass;
import gov.nih.nci.cagrid.portal.portlet.query.AbstractQueryRenderController;
import gov.nih.nci.cagrid.portal.portlet.query.cql.CQLQueryBean;
import gov.nih.nci.cagrid.portal.portlet.tree.TreeFacade;

import java.util.SortedSet;
import java.util.TreeSet;

import javax.portlet.RenderRequest;

import org.springframework.web.portlet.ModelAndView;

/**
 * @author <a href="mailto:joshua.phillips@semanticbits.com">Joshua Phillips</a>
 *
 */
public class ViewQueryModifierController extends AbstractQueryRenderController {
	
	private TreeFacade cqlQueryTreeFacade;
	private UMLClassDao umlClassDao;

	/**
	 * 
	 */
	public ViewQueryModifierController() {

	}

	/* (non-Javadoc)
	 * @see gov.nih.nci.cagrid.portal.portlet.AbstractViewObjectController#getObject(javax.portlet.RenderRequest)
	 */
	@Override
	protected Object getObject(RenderRequest request) {
		EditQueryModifierCommand command = new EditQueryModifierCommand();
		CQLQueryBean cqlQueryBean = (CQLQueryBean)getCqlQueryTreeFacade().getRootNode().getContent();
		command.setModifierType(cqlQueryBean.getModifierType());
		command.getSelectedAttributes().addAll(cqlQueryBean.getSelectedAttributes());
		return command;
	}
	
	protected void addData(RenderRequest request, ModelAndView mav){
		SortedSet<String> attSet = new TreeSet<String>();
		UMLClass umlClass = ((CQLQueryBean)getCqlQueryTreeFacade().getRootNode().getContent()).getUmlClass();
		umlClass = getUmlClassDao().getById(umlClass.getId());
		for (UMLAttribute att : umlClass.getUmlAttributeCollection()) {
			attSet.add(att.getName());
		}
		mav.addObject("availableAttributes", attSet);
	}

	public TreeFacade getCqlQueryTreeFacade() {
		return cqlQueryTreeFacade;
	}

	public void setCqlQueryTreeFacade(TreeFacade cqlQueryTreeFacade) {
		this.cqlQueryTreeFacade = cqlQueryTreeFacade;
	}

	public UMLClassDao getUmlClassDao() {
		return umlClassDao;
	}

	public void setUmlClassDao(UMLClassDao umlClassDao) {
		this.umlClassDao = umlClassDao;
	}

}
