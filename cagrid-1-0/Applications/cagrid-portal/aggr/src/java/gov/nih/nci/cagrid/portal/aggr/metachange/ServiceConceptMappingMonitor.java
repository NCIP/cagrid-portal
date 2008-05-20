/**
 * 
 */
package gov.nih.nci.cagrid.portal.aggr.metachange;

import gov.nih.nci.cagrid.portal.dao.ConceptHierarchyNodeDao;
import gov.nih.nci.cagrid.portal.dao.GridServiceDao;
import gov.nih.nci.cagrid.portal.domain.ConceptHierarchyNode;
import gov.nih.nci.cagrid.portal.domain.GridDataService;
import gov.nih.nci.cagrid.portal.domain.GridService;
import gov.nih.nci.cagrid.portal.domain.metadata.common.Enumeration;
import gov.nih.nci.cagrid.portal.domain.metadata.common.SemanticMetadata;
import gov.nih.nci.cagrid.portal.domain.metadata.common.UMLAttribute;
import gov.nih.nci.cagrid.portal.domain.metadata.common.UMLClass;
import gov.nih.nci.cagrid.portal.domain.metadata.common.ValueDomain;
import gov.nih.nci.cagrid.portal.domain.metadata.dataservice.DomainModel;
import gov.nih.nci.cagrid.portal.domain.metadata.service.InputParameter;
import gov.nih.nci.cagrid.portal.domain.metadata.service.Operation;
import gov.nih.nci.cagrid.portal.domain.metadata.service.Service;
import gov.nih.nci.cagrid.portal.domain.metadata.service.ServiceContext;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.transaction.annotation.Transactional;

/**
 * @author <a href="mailto:joshua.phillips@semanticbits.com">Joshua Phillips</a>
 * 
 */
@Transactional
public class ServiceConceptMappingMonitor {

	private static final Log logger = LogFactory
			.getLog(ServiceConceptMappingMonitor.class);

	private GridServiceDao gridServiceDao;
	private ConceptHierarchyNodeDao conceptHierarchyNodeDao;

	/**
	 * 
	 */
	public ServiceConceptMappingMonitor() {

	}

	public void indexServices() {
		try {
			for (GridService gridService : getGridServiceDao().getAll()) {
				try {
					logger.debug("Indexing service: " + gridService.getUrl());
					Set<Integer> mappedConceptIds = new HashSet<Integer>();
					for (ConceptHierarchyNode node : gridService.getConcepts()) {
						mappedConceptIds.add(node.getId());
					}
					Service svcDesc = gridService.getServiceMetadata()
							.getServiceDescription();
					doMapping(svcDesc.getSemanticMetadata(), gridService,
							mappedConceptIds);
					for (ServiceContext svcCtx : svcDesc
							.getServiceContextCollection()) {
						for (Operation op : svcCtx.getOperationCollection()) {
							doMapping(op.getSemanticMetadata(), gridService,
									mappedConceptIds);
							for (InputParameter param : op
									.getInputParameterCollection()) {
								UMLClass umlClass = param.getUMLClass();
								handleUMLClass(umlClass, gridService,
										mappedConceptIds);
							}
						}
					}
					if (gridService instanceof GridDataService) {
						GridDataService dataService = (GridDataService) gridService;
						DomainModel domainModel = dataService.getDomainModel();
						if (domainModel != null) {
							for (UMLClass umlClass : domainModel.getClasses()) {
								handleUMLClass(umlClass, gridService,
										mappedConceptIds);
							}
						}
					}
					getGridServiceDao().save(gridService);
					getGridServiceDao().getHibernateTemplate().flush();
				} catch (Exception ex) {
					String msg = "Error processing concepts for "
							+ gridService.getUrl() + ": " + ex.getMessage();
					logger.error(msg, ex);
				}
			}
		} catch (Exception ex) {
			String msg = "Error processing new concepts: " + ex.getMessage();
			logger.error(msg, ex);
			throw new RuntimeException(msg, ex);
		}
	}

	private void handleUMLClass(UMLClass umlClass, GridService gridService,
			Set<Integer> mappedConceptIds) {
		if (umlClass != null) {
			doMapping(umlClass.getSemanticMetadata(), gridService,
					mappedConceptIds);
			for (UMLAttribute umlAtt : umlClass.getUmlAttributeCollection()) {
				doMapping(umlAtt.getSemanticMetadata(), gridService,
						mappedConceptIds);
				ValueDomain valueDomain = umlAtt.getValueDomain();
				if (valueDomain != null) {
					doMapping(valueDomain.getSemanticMetadata(), gridService,
							mappedConceptIds);
					for (Enumeration e : valueDomain.getEnumerationCollection()) {
						doMapping(e.getSemanticMetadata(), gridService,
								mappedConceptIds);
					}
				}
			}
		}
	}

	private void doMapping(List<SemanticMetadata> smeta,
			GridService gridService, Set<Integer> mappedConceptIds) {
		for (SemanticMetadata sm : smeta) {
			doMapping(sm, gridService, mappedConceptIds);
		}
	}

	private void doMapping(SemanticMetadata smeta, GridService gridService,
			Set<Integer> mappedConceptIds) {
		for (ConceptHierarchyNode node : getConceptHierarchyNodeDao()
				.getByConceptCode(smeta.getConceptCode())) {
			if (!mappedConceptIds.contains(node.getId())) {
				// If this concept has already been mapped, then ancestors will
				// also have been mapped. No need to navigate up the hierarchy.
				gridService.getConcepts().add(node);
				mappedConceptIds.add(node.getId());
				for (ConceptHierarchyNode ancestor : node.getAncestors()) {
					// Should be able to exit loop once known ancestor
					// encountered,
					// but that relies on ordering, which someone might break
					// in the future.
					if (!mappedConceptIds.contains(ancestor.getId())) {
						gridService.getConcepts().add(ancestor);
						mappedConceptIds.add(ancestor.getId());
					}
				}
			}
		}
	}

	public GridServiceDao getGridServiceDao() {
		return gridServiceDao;
	}

	public void setGridServiceDao(GridServiceDao gridServiceDao) {
		this.gridServiceDao = gridServiceDao;
	}

	public ConceptHierarchyNodeDao getConceptHierarchyNodeDao() {
		return conceptHierarchyNodeDao;
	}

	public void setConceptHierarchyNodeDao(
			ConceptHierarchyNodeDao conceptHierarchyNodeDao) {
		this.conceptHierarchyNodeDao = conceptHierarchyNodeDao;
	}

}
