/**
 * 
 */
package gov.nih.nci.cagrid.portal2.portlet.query.cql;

import gov.nih.nci.cagrid.portal2.dao.GridDataServiceDao;
import gov.nih.nci.cagrid.portal2.domain.GridDataService;
import gov.nih.nci.cagrid.portal2.portlet.SharedApplicationModel;
import gov.nih.nci.cagrid.portal2.portlet.tree.TreeFacade;

import javax.portlet.ActionRequest;
import javax.portlet.ActionResponse;

import org.springframework.web.portlet.mvc.AbstractController;

/**
 * @author <a href="mailto:joshua.phillips@semanticbits.com">Joshua Phillips</a>
 *
 */
public class BuildCQLQueryController extends AbstractController {
	
	private TreeFacade cqlQueryTreeFacade;
	private SharedApplicationModel sharedApplicationModel;
	private String successOperation;
	private GridDataServiceDao gridDataServiceDao;

	/**
	 * 
	 */
	public BuildCQLQueryController() {

	}
	
	public void handleActionRequestInternal(ActionRequest request,
			ActionResponse response) throws Exception {
		CQLQueryBean cqlQueryBean = (CQLQueryBean)getCqlQueryTreeFacade().getRootNode().getContent();
		CQLQueryCommand cqlQueryCommand = getSharedApplicationModel().getWorkingCqlQuery();
		if(cqlQueryCommand == null){
			cqlQueryCommand = new CQLQueryCommand();
			getSharedApplicationModel().setWorkingCqlQuery(cqlQueryCommand);
		}
		GridDataService service = getGridDataServiceDao().getById(getSharedApplicationModel().getSelectedGridDataServiceId());
		cqlQueryCommand.setCqlQuery(cqlQueryBean.toXml());
		cqlQueryCommand.setDataServiceUrl(service.getUrl());
	}

	public TreeFacade getCqlQueryTreeFacade() {
		return cqlQueryTreeFacade;
	}

	public void setCqlQueryTreeFacade(TreeFacade cqlQueryTreeFacade) {
		this.cqlQueryTreeFacade = cqlQueryTreeFacade;
	}

	public SharedApplicationModel getSharedApplicationModel() {
		return sharedApplicationModel;
	}

	public void setSharedApplicationModel(
			SharedApplicationModel sharedApplicationModel) {
		this.sharedApplicationModel = sharedApplicationModel;
	}

	public String getSuccessOperation() {
		return successOperation;
	}

	public void setSuccessOperation(String successOperation) {
		this.successOperation = successOperation;
	}

	public GridDataServiceDao getGridDataServiceDao() {
		return gridDataServiceDao;
	}

	public void setGridDataServiceDao(GridDataServiceDao gridServiceDao) {
		this.gridDataServiceDao = gridServiceDao;
	}

}
