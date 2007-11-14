/**
 * 
 */
package gov.nih.nci.cagrid.portal2.portlet.query.results;

import javax.portlet.ActionRequest;
import javax.portlet.ActionResponse;

import org.springframework.beans.factory.annotation.Required;
import org.springframework.validation.BindException;

import gov.nih.nci.cagrid.portal2.dao.CQLQueryInstanceDao;
import gov.nih.nci.cagrid.portal2.dao.PortalUserDao;
import gov.nih.nci.cagrid.portal2.domain.PortalUser;
import gov.nih.nci.cagrid.portal2.domain.dataservice.CQLQueryInstance;
import gov.nih.nci.cagrid.portal2.portlet.query.AbstractQueryActionController;

/**
 * @author <a href="mailto:joshua.phillips@semanticbits.com">Joshua Phillips</a>
 *
 */
public class DeleteQueryInstanceController extends
		AbstractQueryActionController {

	private CQLQueryInstanceDao cqlQueryInstanceDao;
	private PortalUserDao portalUserDao;
	
	/**
	 * 
	 */
	public DeleteQueryInstanceController() {

	}

	/**
	 * @param commandClass
	 */
	public DeleteQueryInstanceController(Class commandClass) {
		super(commandClass);

	}

	/**
	 * @param commandClass
	 * @param commandName
	 */
	public DeleteQueryInstanceController(Class commandClass, String commandName) {
		super(commandClass, commandName);

	}

	/* (non-Javadoc)
	 * @see gov.nih.nci.cagrid.portal2.portlet.AbstractActionResponseHandlerCommandController#doHandleAction(javax.portlet.ActionRequest, javax.portlet.ActionResponse, java.lang.Object, org.springframework.validation.BindException)
	 */
	@Override
	protected void doHandleAction(ActionRequest request,
			ActionResponse response, Object obj, BindException errors)
			throws Exception {
		SelectQueryInstanceCommand command = (SelectQueryInstanceCommand)obj;
		CQLQueryInstance instance = getQueryModel().getQueryInstance(command.getInstanceId());
		if(instance != null){
			//Will be null if it was created in previous http session.
			getQueryModel().deleteQueryInstance(instance.getId());
		}
		instance = getCqlQueryInstanceDao().getById(command.getInstanceId());
		if(getQueryModel().getPortalUser() != null){
			PortalUser user = getQueryModel().getPortalUser(); 
			user = getPortalUserDao().getById(user.getId());
			user.getQueryInstances().remove(instance);
			getPortalUserDao().save(user);
			getPortalUserDao().getHibernateTemplate().flush();
		}
		getCqlQueryInstanceDao().delete(instance);
		getCqlQueryInstanceDao().getHibernateTemplate().flush();
	}

	@Required
	public CQLQueryInstanceDao getCqlQueryInstanceDao() {
		return cqlQueryInstanceDao;
	}

	public void setCqlQueryInstanceDao(CQLQueryInstanceDao cqlQueryInstanceDao) {
		this.cqlQueryInstanceDao = cqlQueryInstanceDao;
	}

	@Required
	public PortalUserDao getPortalUserDao() {
		return portalUserDao;
	}

	public void setPortalUserDao(PortalUserDao portalUserDao) {
		this.portalUserDao = portalUserDao;
	}

}
