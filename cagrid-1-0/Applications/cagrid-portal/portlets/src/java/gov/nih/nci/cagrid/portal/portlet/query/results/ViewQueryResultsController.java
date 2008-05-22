/**
 *
 */
package gov.nih.nci.cagrid.portal.portlet.query.results;

import gov.nih.nci.cagrid.portal.dao.DCQLQueryDao;
import gov.nih.nci.cagrid.portal.dao.DomainModelDao;
import gov.nih.nci.cagrid.portal.domain.GridDataService;
import gov.nih.nci.cagrid.portal.domain.dataservice.CQLQueryInstance;
import gov.nih.nci.cagrid.portal.domain.dataservice.DCQLQuery;
import gov.nih.nci.cagrid.portal.domain.dataservice.DCQLQueryInstance;
import gov.nih.nci.cagrid.portal.domain.dataservice.QueryInstance;
import gov.nih.nci.cagrid.portal.domain.metadata.common.UMLAttribute;
import gov.nih.nci.cagrid.portal.domain.metadata.dataservice.DomainModel;
import gov.nih.nci.cagrid.portal.domain.metadata.dataservice.UMLClass;
import gov.nih.nci.cagrid.portal.portlet.CaGridPortletApplicationException;
import gov.nih.nci.cagrid.portal.portlet.query.AbstractQueryRenderController;
import gov.nih.nci.cagrid.portal.portlet.query.cql.CQLQueryInstanceResultsBean;
import gov.nih.nci.cagrid.portal.portlet.util.PortletUtils;
import gov.nih.nci.cagrid.portal.portlet.util.Table;
import gov.nih.nci.cagrid.portal.portlet.util.TableScroller;

import java.io.ByteArrayInputStream;
import java.util.ArrayList;
import java.util.List;

import javax.portlet.RenderRequest;

import org.springframework.beans.factory.annotation.Required;

/**
 * @author <a href="mailto:joshua.phillips@semanticbits.com">Joshua Phillips</a>
 */
public class ViewQueryResultsController extends AbstractQueryRenderController {

	private String resultsBeanSessionAttributeName;
	private DomainModelDao domainModelDao;
	private DCQLQueryDao dcqlQueryDao;

	/**
	 * 
	 */
	public ViewQueryResultsController() {

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see gov.nih.nci.cagrid.portal.portlet.AbstractViewObjectController#getObject(javax.portlet.RenderRequest)
	 */
	@Override
	protected Object getObject(RenderRequest request) {
		QueryInstance instance = getQueryModel().getSelectedQueryInstance();
		CQLQueryInstanceResultsBean command = (CQLQueryInstanceResultsBean) request
				.getPortletSession().getAttribute(
						getResultsBeanSessionAttributeName());
		if (command != null && instance != null
				&& command.getInstance() != null
				&& command.getInstance().getId().equals(instance.getId())) {
			// Don't create new one
		} else {
			command = new CQLQueryInstanceResultsBean();
			if (instance != null) {

				String xml = instance.getResult();

				if (xml != null) {

					List<String> colNames = getColumnNames(instance);
					getQueryModel().setQueryResultsColumnNames(colNames);

					Table table = null;
					try {
						table = PortletUtils.buildTableFromCQLResults(colNames,
								new ByteArrayInputStream(xml.getBytes()));
					} catch (Exception ex) {
						throw new CaGridPortletApplicationException(
								"Error build table from XML results: "
										+ ex.getMessage(), ex);
					}
					if (table != null) {
						command.setTableScroller(new TableScroller(table, 10));
					}
				}
				command.setInstance(instance);
			}
			request.getPortletSession().setAttribute(
					getResultsBeanSessionAttributeName(), command);
		}
		return command;
	}

	private List<String> getColumnNames(QueryInstance instance) {
		List<String> colNames = new ArrayList<String>();
		String umlClassName = PortletUtils.getTargetUMLClassName(instance.getQuery().getXml());
		logger.debug("Looking for UMLClass: " + umlClassName);
		DomainModel domainModel = null;
		if (instance instanceof CQLQueryInstance) {
			CQLQueryInstance cqlQueryInstance = (CQLQueryInstance) instance;
			domainModel = cqlQueryInstance.getDataService().getDomainModel();
		} else {
			DCQLQueryInstance dcqlQueryInstance = (DCQLQueryInstance) instance;
			DCQLQuery query = (DCQLQuery) dcqlQueryInstance.getQuery();
			query = getDcqlQueryDao().getById(query.getId());
			domainModel = query.getTargetServices().get(0).getDomainModel();
		}
		domainModel = getDomainModelDao().getById(domainModel.getId());
		UMLClass umlClass = getQueryModel().getUmlClassDao()
				.getUmlClassFromModel(domainModel, umlClassName);
		for (UMLAttribute att : umlClass.getUmlAttributeCollection()) {
			colNames.add(att.getName());
		}
		return colNames;
	}

	@Required
	public String getResultsBeanSessionAttributeName() {
		return resultsBeanSessionAttributeName;
	}

	public void setResultsBeanSessionAttributeName(
			String resultsBeanSessionAttributeName) {
		this.resultsBeanSessionAttributeName = resultsBeanSessionAttributeName;
	}

	public DomainModelDao getDomainModelDao() {
		return domainModelDao;
	}

	public void setDomainModelDao(DomainModelDao domainModelDao) {
		this.domainModelDao = domainModelDao;
	}

	public DCQLQueryDao getDcqlQueryDao() {
		return dcqlQueryDao;
	}

	public void setDcqlQueryDao(DCQLQueryDao dcqlQueryDao) {
		this.dcqlQueryDao = dcqlQueryDao;
	}

}
