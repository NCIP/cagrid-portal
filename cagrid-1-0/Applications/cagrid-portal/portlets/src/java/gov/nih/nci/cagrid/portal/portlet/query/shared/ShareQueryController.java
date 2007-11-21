/**
 * 
 */
package gov.nih.nci.cagrid.portal.portlet.query.shared;

import gov.nih.nci.cagrid.portal.dao.GridServiceDao;
import gov.nih.nci.cagrid.portal.domain.GridDataService;
import gov.nih.nci.cagrid.portal.domain.dataservice.SharedCQLQuery;
import gov.nih.nci.cagrid.portal.domain.metadata.dataservice.UMLClass;
import gov.nih.nci.cagrid.portal.portlet.PortletConstants;
import gov.nih.nci.cagrid.portal.portlet.query.AbstractQueryActionController;
import gov.nih.nci.cagrid.portal.portlet.query.cql.CQLQueryCommand;

import java.io.ByteArrayInputStream;

import javax.portlet.ActionRequest;
import javax.portlet.ActionResponse;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathFactory;

import org.springframework.validation.BindException;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

/**
 * @author <a href="mailto:joshua.phillips@semanticbits.com">Joshua Phillips</a>
 * 
 */
public class ShareQueryController extends AbstractQueryActionController {

	private GridServiceDao gridServiceDao;

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
		if (errors.hasErrors()) {
			return;
		}
		CQLQueryCommand command = (CQLQueryCommand) obj;
		getQueryModel().setWorkingQuery(command);

		GridDataService targetService = (GridDataService) getGridServiceDao()
				.getByUrl(command.getDataServiceUrl());
		UMLClass targetClass = null;
		String targetClassName = getTargetClassName(command.getCqlQuery());
		for (UMLClass klass : targetService.getDomainModel().getClasses()) {
			String className = klass.getPackageName() + "."
					+ klass.getClassName();
			if (className.equals(targetClassName)) {
				targetClass = klass;
				break;
			}
		}

		if (targetClass == null) {
			errors.rejectValue("cqlQuery", PortletConstants.INVALID_UML_CLASS,
					null, "No such UMLClass in DomainModel: '"
							+ targetClassName + "'");
			return;
		}

		SharedCQLQuery sharedQuery = new SharedCQLQuery();
		sharedQuery.setOwner(getQueryModel().getPortalUser());
		sharedQuery.setTargetService(targetService);
		sharedQuery.setTargetClass(targetClass);
		SharedQueryBean sharedQueryBean = new SharedQueryBean();
		sharedQueryBean.setQuery(sharedQuery);
		sharedQueryBean.setQueryCommand(command);
		getQueryModel().setWorkingSharedQuery(sharedQueryBean);
	}

	private String getTargetClassName(String cqlQuery) {
		String targetClassName = null;
		try {

			Document doc = DocumentBuilderFactory.newInstance()
					.newDocumentBuilder().parse(
							new ByteArrayInputStream(cqlQuery.getBytes()));
			XPathFactory xpFact = XPathFactory.newInstance();
			Element targetEl = (Element) xpFact.newXPath().compile(
					"/CQLQuery/Target").evaluate(doc, XPathConstants.NODE);
			if (targetEl != null) {
				targetClassName = targetEl.getAttribute("name");
			}
		} catch (Exception ex) {
			logger.error("Error getting target class name: " + ex.getMessage(),
					ex);
		}
		return targetClassName;
	}

	public GridServiceDao getGridServiceDao() {
		return gridServiceDao;
	}

	public void setGridServiceDao(GridServiceDao gridServiceDao) {
		this.gridServiceDao = gridServiceDao;
	}

}
