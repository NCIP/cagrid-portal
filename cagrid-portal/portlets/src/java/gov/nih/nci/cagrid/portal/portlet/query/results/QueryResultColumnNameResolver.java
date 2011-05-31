/**
 * 
 */
package gov.nih.nci.cagrid.portal.portlet.query.results;

import gov.nih.nci.cagrid.portal.dao.DCQLQueryDao;
import gov.nih.nci.cagrid.portal.dao.DomainModelDao;
import gov.nih.nci.cagrid.portal.dao.QueryInstanceDao;
import gov.nih.nci.cagrid.portal.dao.UMLAttributeDao;
import gov.nih.nci.cagrid.portal.dao.UMLClassDao;
import gov.nih.nci.cagrid.portal.domain.dataservice.CQLQueryInstance;
import gov.nih.nci.cagrid.portal.domain.dataservice.DCQLQuery;
import gov.nih.nci.cagrid.portal.domain.dataservice.DCQLQueryInstance;
import gov.nih.nci.cagrid.portal.domain.dataservice.QueryInstance;
import gov.nih.nci.cagrid.portal.domain.metadata.common.UMLAttribute;
import gov.nih.nci.cagrid.portal.domain.metadata.dataservice.DomainModel;
import gov.nih.nci.cagrid.portal.domain.metadata.dataservice.UMLClass;
import gov.nih.nci.cagrid.portal.portlet.util.PortletUtils;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.transaction.annotation.Transactional;

/**
 * @author <a href="mailto:joshua.phillips@semanticbits.com">Joshua Phillips</a>
 * 
 */
@Transactional
public class QueryResultColumnNameResolver {

	private static final Log logger = LogFactory
			.getLog(QueryResultColumnNameResolver.class);

	private QueryInstanceDao queryInstanceDao;
	private DCQLQueryDao dcqlQueryDao;
	private DomainModelDao domainModelDao;
	private UMLClassDao umlClassDao;
	private UMLAttributeDao umlAttributeDao;

	public UMLClass getTargetUMLClass(int instanceId) {
		QueryInstance instance = getQueryInstanceDao().getById(instanceId);
		String umlClassName = PortletUtils.getTargetUMLClassName(instance
				.getQuery().getXml());
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
		UMLClass umlClass = getUmlClassDao().getUmlClassFromModel(domainModel,
				umlClassName);
		return umlClass;
	}

	public List<String> getColumnNames(int instanceId) {
		List<String> colNames = new ArrayList<String>();
		UMLClass umlClass = getTargetUMLClass(instanceId);
		for (UMLAttribute att : getUmlAttributeDao().getAttributesForClass(
				umlClass.getId())) {
			colNames.add(att.getName());
		}
		return colNames;
	}

	public QueryInstanceDao getQueryInstanceDao() {
		return queryInstanceDao;
	}

	public void setQueryInstanceDao(QueryInstanceDao queryInstanceDao) {
		this.queryInstanceDao = queryInstanceDao;
	}

	public DCQLQueryDao getDcqlQueryDao() {
		return dcqlQueryDao;
	}

	public void setDcqlQueryDao(DCQLQueryDao dcqlQueryDao) {
		this.dcqlQueryDao = dcqlQueryDao;
	}

	public DomainModelDao getDomainModelDao() {
		return domainModelDao;
	}

	public void setDomainModelDao(DomainModelDao domainModelDao) {
		this.domainModelDao = domainModelDao;
	}

	public UMLClassDao getUmlClassDao() {
		return umlClassDao;
	}

	public void setUmlClassDao(UMLClassDao umlClassDao) {
		this.umlClassDao = umlClassDao;
	}

	public UMLAttributeDao getUmlAttributeDao() {
		return umlAttributeDao;
	}

	public void setUmlAttributeDao(UMLAttributeDao umlAttributeDao) {
		this.umlAttributeDao = umlAttributeDao;
	}

}
