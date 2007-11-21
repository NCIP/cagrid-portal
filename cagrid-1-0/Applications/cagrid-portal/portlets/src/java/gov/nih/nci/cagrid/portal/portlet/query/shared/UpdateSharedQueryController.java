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
import gov.nih.nci.cagrid.portal.domain.metadata.dataservice.UMLClass;
import gov.nih.nci.cagrid.portal.portlet.query.AbstractQueryActionController;
import gov.nih.nci.cagrid.portal.portlet.query.cql.CQLQueryCommand;
import gov.nih.nci.cagrid.portal.util.PortalUtils;

import javax.portlet.ActionRequest;
import javax.portlet.ActionResponse;

import org.springframework.validation.BindException;

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

				if (owner != null) {
					owner.getSharedQueries().add(bean.getQuery());
					getPortalUserDao().save(owner);
				}
			} else {
				getSharedCqlQueryDao().save(bean.getQuery());
			}

			response.setRenderParameter("confirmMessage",
					"The shared query has been successfully saved.");
		} else if ("delete".equals(editOp)) {
			PortalUser portalUser = bean.getQuery().getOwner();
			portalUser.getSharedQueries().remove(bean.getQuery());
			bean.getQuery().setOwner(null);
			getSharedCqlQueryDao().delete(bean.getQuery());
			getPortalUserDao().save(portalUser);
			response.setRenderParameter("confirmMessage",
					"The shared query has been successfully deleted.");
			getQueryModel().setWorkingSharedQuery(null);
		}

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
