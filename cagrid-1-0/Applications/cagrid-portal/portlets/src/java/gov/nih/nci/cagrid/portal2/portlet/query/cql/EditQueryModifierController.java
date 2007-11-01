/**
 * 
 */
package gov.nih.nci.cagrid.portal2.portlet.query.cql;

import gov.nih.nci.cagrid.portal2.domain.metadata.common.UMLAttribute;
import gov.nih.nci.cagrid.portal2.domain.metadata.dataservice.UMLClass;
import gov.nih.nci.cagrid.portal2.portlet.query.builder.EditQueryModifierCommand;
import gov.nih.nci.cagrid.portal2.portlet.tree.TreeFacade;

import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;
import java.util.SortedSet;
import java.util.TreeSet;

import javax.portlet.ActionRequest;
import javax.portlet.ActionResponse;
import javax.portlet.PortletRequest;

import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.springframework.beans.factory.annotation.Required;
import org.springframework.orm.hibernate3.HibernateCallback;
import org.springframework.orm.hibernate3.HibernateTemplate;
import org.springframework.validation.BindException;
import org.springframework.validation.Errors;
import org.springframework.web.portlet.mvc.SimpleFormController;

/**
 * @author <a href="mailto:joshua.phillips@semanticbits.com">Joshua Phillips</a>
 * 
 */
public class EditQueryModifierController extends SimpleFormController {

	private TreeFacade cqlQueryTreeFacade;
	private HibernateTemplate hibernateTemplate;
	private String successOperation;

	/**
	 * 
	 */
	public EditQueryModifierController() {

	}
	
	protected Object formBackingObject(PortletRequest request) throws Exception {
		EditQueryModifierCommand command = new EditQueryModifierCommand();
		CQLQueryBean cqlQueryBean = (CQLQueryBean)getCqlQueryTreeFacade().getRootNode().getContent();
		command.setModifierType(cqlQueryBean.getModifierType());
		command.getSelectedAttributes().addAll(cqlQueryBean.getSelectedAttributes());
		return command;
	}

	protected Map referenceData(PortletRequest request, Object command,
			Errors errors) throws Exception {
		Map data = new HashMap();

		final SortedSet<String> attSet = new TreeSet<String>();
		final CQLQueryBean cqlQueryBean = (CQLQueryBean) getCqlQueryTreeFacade()
				.getRootNode().getContent();
		getHibernateTemplate().execute(new HibernateCallback() {
			public Object doInHibernate(Session session)
					throws HibernateException, SQLException {
				UMLClass umlClass = (UMLClass) getHibernateTemplate().get(
						cqlQueryBean.getUmlClass().getClass(),
						cqlQueryBean.getUmlClass().getId());
				for (UMLAttribute att : umlClass.getUmlAttributeCollection()) {
					attSet.add(att.getName());
				}
				return null;
			}
		});
		data.put("availableAttributes", attSet);

		return data;
	}

	protected void onSubmitAction(ActionRequest request,
			ActionResponse response, Object obj, BindException errors)
			throws Exception {
		EditQueryModifierCommand command = (EditQueryModifierCommand) obj;
		CQLQueryBean cqlQueryBean = (CQLQueryBean) getCqlQueryTreeFacade()
				.getRootNode().getContent();
		if ("update".equals(command.getEditOperation())) {
			cqlQueryBean.setModifierType(command.getModifierType());
			cqlQueryBean.getSelectedAttributes().clear();
			if (cqlQueryBean.getSelectedAttributes() != null) {
				cqlQueryBean.getSelectedAttributes().addAll(
						command.getSelectedAttributes());
			}
		} else if ("delete".equals(command.getEditOperation())) {
			cqlQueryBean.setModifierType(null);
			cqlQueryBean.getSelectedAttributes().clear();
		} else {
			// cancelling, do nothing
		}
		response.setRenderParameter("operation", getSuccessOperation());
	}

	@Required
	public TreeFacade getCqlQueryTreeFacade() {
		return cqlQueryTreeFacade;
	}

	public void setCqlQueryTreeFacade(TreeFacade cqlQueryTreeFacade) {
		this.cqlQueryTreeFacade = cqlQueryTreeFacade;
	}

	@Required
	public HibernateTemplate getHibernateTemplate() {
		return hibernateTemplate;
	}

	public void setHibernateTemplate(HibernateTemplate hibernateTemplate) {
		this.hibernateTemplate = hibernateTemplate;
	}

	@Required
	public String getSuccessOperation() {
		return successOperation;
	}

	public void setSuccessOperation(String successOperation) {
		this.successOperation = successOperation;
	}

}
