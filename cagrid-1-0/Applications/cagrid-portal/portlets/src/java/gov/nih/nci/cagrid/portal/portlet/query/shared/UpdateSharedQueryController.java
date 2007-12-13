/**
 * 
 */
package gov.nih.nci.cagrid.portal.portlet.query.shared;

import gov.nih.nci.cagrid.portal.dao.CQLQueryDao;
import gov.nih.nci.cagrid.portal.dao.GridDataServiceDao;
import gov.nih.nci.cagrid.portal.dao.PortalUserDao;
import gov.nih.nci.cagrid.portal.dao.SharedCQLQueryDao;
import gov.nih.nci.cagrid.portal.dao.UMLClassDao;
import gov.nih.nci.cagrid.portal.domain.GridDataService;
import gov.nih.nci.cagrid.portal.domain.PortalUser;
import gov.nih.nci.cagrid.portal.domain.dataservice.CQLQuery;
import gov.nih.nci.cagrid.portal.domain.dataservice.SharedCQLQuery;
import gov.nih.nci.cagrid.portal.domain.metadata.dataservice.UMLClass;
import gov.nih.nci.cagrid.portal.portlet.query.AbstractQueryActionController;
import gov.nih.nci.cagrid.portal.portlet.query.cql.CQLQueryCommand;
import gov.nih.nci.cagrid.portal.portlet.util.XSSFilterEditor;
import gov.nih.nci.cagrid.portal.util.PortalUtils;

import javax.portlet.ActionRequest;
import javax.portlet.ActionResponse;
import javax.portlet.PortletRequest;

import org.springframework.validation.BindException;
import org.springframework.web.portlet.bind.PortletRequestDataBinder;

/**
 * @author <a href="mailto:joshua.phillips@semanticbits.com">Joshua Phillips</a>
 * 
 */
public class UpdateSharedQueryController extends AbstractQueryActionController {

	private CQLQueryDao cqlQueryDao;
	private GridDataServiceDao gridDataServiceDao;
	private PortalUserDao portalUserDao;
	private UMLClassDao umlClassDao;
	private SharedCQLQueryDao sharedCqlQueryDao;

	/**
	 * 
	 */
	public UpdateSharedQueryController() {

	}

	/**
	 * @param commandClass
	 */
	public UpdateSharedQueryController(Class commandClass) {
		super(commandClass);

	}

	/**
	 * @param commandClass
	 * @param commandName
	 */
	public UpdateSharedQueryController(Class commandClass, String commandName) {
		super(commandClass, commandName);

	}

	protected void initBinder(PortletRequest request,
			PortletRequestDataBinder binder) throws Exception {
		binder.registerCustomEditor(String.class, "query.name",
				new XSSFilterEditor(binder.getBindingResult(), "query.name"));
		binder.registerCustomEditor(String.class, "query.description",
				new XSSFilterEditor(binder.getBindingResult(), "query.description"));
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
		
		if(errors.hasErrors()){
			return;
		}

		String editOp = request.getParameter("editOp");
		SharedQueryBean bean = (SharedQueryBean) obj;

		if ("save".equals(editOp)) {
			if (bean.getQuery().getId() == null) {
				String cql = bean.getQueryCommand().getCqlQuery();
				String hash = PortalUtils.createHash(cql);
				CQLQuery query = getCqlQueryDao().getByHash(hash);
				if (query == null) {
					query = new CQLQuery();
					query.setXml(cql);
					query.setHash(hash);
					getCqlQueryDao().save(query);
					getCqlQueryDao().getHibernateTemplate().flush();
				}
				bean.getQuery().setCqlQuery(query);

				PortalUser owner = null;
				if (getQueryModel().getPortalUser() != null) {
					owner = getPortalUserDao().getById(
							getQueryModel().getPortalUser().getId());
					bean.getQuery().setOwner(owner);
				}
				GridDataService targetService = getGridDataServiceDao()
						.getById(bean.getQuery().getTargetService().getId());
				bean.getQuery().setTargetService(targetService);
				UMLClass targetClass = getUmlClassDao().getById(
						bean.getQuery().getTargetClass().getId());
				bean.getQuery().setTargetClass(targetClass);

				getSharedCqlQueryDao().save(bean.getQuery());
				getSharedCqlQueryDao().getHibernateTemplate().flush();

				if (owner != null) {
					owner.getSharedQueries().add(bean.getQuery());
					getPortalUserDao().save(owner);
				}
			} else {
				getSharedCqlQueryDao().save(bean.getQuery());
				getSharedCqlQueryDao().getHibernateTemplate().flush();
			}

			response.setRenderParameter("confirmMessage",
					"The shared query has been successfully saved.");
		} else if ("delete".equals(editOp)) {
			PortalUser portalUser = bean.getQuery().getOwner();
			portalUser = getPortalUserDao().getById(portalUser.getId());
			portalUser.getSharedQueries().remove(bean.getQuery());
			bean.getQuery().setOwner(null);
			SharedCQLQuery query = getSharedCqlQueryDao().getById(
					bean.getQuery().getId());
			getSharedCqlQueryDao().delete(query);
			getPortalUserDao().save(portalUser);
			response.setRenderParameter("confirmMessage",
					"The shared query has been successfully deleted.");
			getQueryModel().setWorkingSharedQuery(null);
		}

	}

	protected Object getCommand(PortletRequest request) throws Exception {
		return getQueryModel().getWorkingSharedQuery();
	}

	public CQLQueryDao getCqlQueryDao() {
		return cqlQueryDao;
	}

	public void setCqlQueryDao(CQLQueryDao cqlQueryDao) {
		this.cqlQueryDao = cqlQueryDao;
	}

	public GridDataServiceDao getGridDataServiceDao() {
		return gridDataServiceDao;
	}

	public void setGridDataServiceDao(GridDataServiceDao gridDataServiceDao) {
		this.gridDataServiceDao = gridDataServiceDao;
	}

	public PortalUserDao getPortalUserDao() {
		return portalUserDao;
	}

	public void setPortalUserDao(PortalUserDao portalUserDao) {
		this.portalUserDao = portalUserDao;
	}

	public UMLClassDao getUmlClassDao() {
		return umlClassDao;
	}

	public void setUmlClassDao(UMLClassDao umlClassDao) {
		this.umlClassDao = umlClassDao;
	}

	public SharedCQLQueryDao getSharedCqlQueryDao() {
		return sharedCqlQueryDao;
	}

	public void setSharedCqlQueryDao(SharedCQLQueryDao sharedCQLQueryDao) {
		this.sharedCqlQueryDao = sharedCQLQueryDao;
	}

}
