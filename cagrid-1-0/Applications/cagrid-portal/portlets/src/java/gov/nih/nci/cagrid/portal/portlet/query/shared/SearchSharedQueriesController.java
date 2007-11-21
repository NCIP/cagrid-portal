/**
 * 
 */
package gov.nih.nci.cagrid.portal.portlet.query.shared;

import gov.nih.nci.cagrid.portal.portlet.discovery.DiscoveryResults;
import gov.nih.nci.cagrid.portal.portlet.discovery.list.ListBean;
import gov.nih.nci.cagrid.portal.portlet.discovery.search.KeywordSearchBean;
import gov.nih.nci.cagrid.portal.portlet.discovery.search.KeywordSearchService;
import gov.nih.nci.cagrid.portal.portlet.query.AbstractQueryActionController;

import javax.portlet.ActionRequest;
import javax.portlet.ActionResponse;

import org.springframework.validation.BindException;

/**
 * @author <a href="mailto:joshua.phillips@semanticbits.com">Joshua Phillips</a>
 *
 */
public class SearchSharedQueriesController extends
		AbstractQueryActionController {
	
	private KeywordSearchService keywordSearchService;
	private String listBeanSessionAttributeName;

	/**
	 * 
	 */
	public SearchSharedQueriesController() {

	}

	/**
	 * @param commandClass
	 */
	public SearchSharedQueriesController(Class commandClass) {
		super(commandClass);

	}

	/**
	 * @param commandClass
	 * @param commandName
	 */
	public SearchSharedQueriesController(Class commandClass, String commandName) {
		super(commandClass, commandName);

	}

	/* (non-Javadoc)
	 * @see gov.nih.nci.cagrid.portal.portlet.AbstractActionResponseHandlerCommandController#doHandleAction(javax.portlet.ActionRequest, javax.portlet.ActionResponse, java.lang.Object, org.springframework.validation.BindException)
	 */
	@Override
	protected void doHandleAction(ActionRequest request,
			ActionResponse response, Object obj, BindException errors)
			throws Exception {
		KeywordSearchBean command = (KeywordSearchBean)obj;
		DiscoveryResults results = getKeywordSearchService().search(command);
		ListBean listBean = (ListBean) getApplicationContext().getBean("listBeanPrototype");
		listBean.getScroller().setObjects(results.getObjects());
		listBean.setType(results.getType().toString());
		request.getPortletSession().setAttribute(getListBeanSessionAttributeName(), listBean);
	}

	public KeywordSearchService getKeywordSearchService() {
		return keywordSearchService;
	}

	public void setKeywordSearchService(KeywordSearchService keywordSearchService) {
		this.keywordSearchService = keywordSearchService;
	}

	public String getListBeanSessionAttributeName() {
		return listBeanSessionAttributeName;
	}

	public void setListBeanSessionAttributeName(String listBeanSessionAttributeName) {
		this.listBeanSessionAttributeName = listBeanSessionAttributeName;
	}

}
