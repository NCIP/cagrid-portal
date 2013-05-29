/**
*============================================================================
*  The Ohio State University Research Foundation, The University of Chicago -
*  Argonne National Laboratory, Emory University, SemanticBits LLC, 
*  and Ekagra Software Technologies Ltd.
*
*  Distributed under the OSI-approved BSD 3-Clause License.
*  See http://ncip.github.com/cagrid-core/LICENSE.txt for details.
*============================================================================
**/
/**
 * 
 */
package gov.nih.nci.cagrid.portal.portlet.query.shared;

import gov.nih.nci.cagrid.portal.dao.QueryDao;
import gov.nih.nci.cagrid.portal.dao.catalog.SharedQueryCatalogEntryDao;
import gov.nih.nci.cagrid.portal.domain.PortalUser;
import gov.nih.nci.cagrid.portal.domain.catalog.SharedQueryCatalogEntry;
import gov.nih.nci.cagrid.portal.domain.dataservice.Query;
import gov.nih.nci.cagrid.portal.portlet.UserModel;
import gov.nih.nci.cagrid.portal.portlet.query.AbstractQueryActionController;
import gov.nih.nci.cagrid.portal.portlet.query.QueryService;
import gov.nih.nci.cagrid.portal.portlet.query.cql.CQLQueryCommand;
import gov.nih.nci.cagrid.portal.util.PortalUtils;

import javax.portlet.ActionRequest;
import javax.portlet.ActionResponse;

import org.springframework.validation.BindException;

/**
 * @author <a href="mailto:joshua.phillips@semanticbits.com">Joshua Phillips</a>
 * 
 */
public class ShareQueryController extends AbstractQueryActionController {

	private UserModel userModel;
	private SharedQueryCatalogEntryDao sharedQueryCatalogEntryDao;
	private QueryDao queryDao;
	private QueryService queryService;
	
	private String redirectUrl;

	/**
	 * 
	 */
	public ShareQueryController() {

	}

	/**
	 * @param commandClass
	 */
	public ShareQueryController(Class commandClass) {
		super(commandClass);

	}

	/**
	 * @param commandClass
	 * @param commandName
	 */
	public ShareQueryController(Class commandClass, String commandName) {
		super(commandClass, commandName);

	}

	@Override
	protected void doHandleAction(ActionRequest request,
			ActionResponse response, Object obj, BindException errors)
			throws Exception {
		if(true) throw new Exception("This method should not be called.");
	}
	
	
	@Override
	protected void handleAction(ActionRequest request,
			ActionResponse response, Object obj, BindException errors)
			throws Exception {
		
		if (errors.hasErrors()) {
			return;
		}
		CQLQueryCommand command = (CQLQueryCommand) obj;
		
		String xml = command.getCqlQuery();
		Query query = getQueryDao().getQueryByHash(PortalUtils.createHash(xml));
		if(query == null){
			query = getQueryService().createQueryFromXml(xml);
		}
		
		//Create the catalog entry
		PortalUser portalUser = getUserModel().getPortalUser();
		SharedQueryCatalogEntry entry = new SharedQueryCatalogEntry();
		entry.setAbout(query);
		entry.setAuthor(portalUser);
		entry.setContributor(portalUser.getCatalog());
		getSharedQueryCatalogEntryDao().save(entry);
		
		//Set it on the user model
		getUserModel().setCurrentCatalogEntry(entry);
		
		//Send a redirect to the browse page
		String url = getRedirectUrl() + entry.getId();
		response.sendRedirect(url);
	}

	public UserModel getUserModel() {
		return userModel;
	}

	public void setUserModel(UserModel userModel) {
		this.userModel = userModel;
	}

	public SharedQueryCatalogEntryDao getSharedQueryCatalogEntryDao() {
		return sharedQueryCatalogEntryDao;
	}

	public void setSharedQueryCatalogEntryDao(
			SharedQueryCatalogEntryDao sharedQueryCatalogEntryDao) {
		this.sharedQueryCatalogEntryDao = sharedQueryCatalogEntryDao;
	}

	public QueryService getQueryService() {
		return queryService;
	}

	public void setQueryService(QueryService queryService) {
		this.queryService = queryService;
	}

	public String getRedirectUrl() {
		return redirectUrl;
	}

	public void setRedirectUrl(String redirectUrl) {
		this.redirectUrl = redirectUrl;
	}

	public QueryDao getQueryDao() {
		return queryDao;
	}

	public void setQueryDao(QueryDao queryDao) {
		this.queryDao = queryDao;
	}


}
