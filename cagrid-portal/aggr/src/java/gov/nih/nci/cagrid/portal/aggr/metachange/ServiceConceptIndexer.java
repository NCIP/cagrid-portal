/**
*============================================================================
*  The Ohio State University Research Foundation, The University of Chicago -
*  Argonne National Laboratory, Emory University, SemanticBits LLC, 
*  and Ekagra Software Technologies Ltd.
*
*  Distributed under the OSI-approved BSD 3-Clause License.
*  See http://ncip.github.com/cagrid-portal/LICENSE.txt for details.
*============================================================================
**/
/**
 * 
 */
package gov.nih.nci.cagrid.portal.aggr.metachange;

import gov.nih.nci.cagrid.portal.dao.ConceptHierarchyNodeDao;
import gov.nih.nci.cagrid.portal.dao.GridServiceDao;
import gov.nih.nci.cagrid.portal.dao.SemanticMetadataMappingDao;
import gov.nih.nci.cagrid.portal.domain.ConceptHierarchyNode;
import gov.nih.nci.cagrid.portal.domain.GridService;
import gov.nih.nci.cagrid.portal.domain.SemanticMetadataMapping;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.transaction.annotation.Transactional;

/**
 * @author <a href="mailto:joshua.phillips@semanticbits.com">Joshua Phillips</a>
 *
 */
@Transactional
public class ServiceConceptIndexer {

	private static final Log logger = LogFactory.getLog(ServiceConceptIndexer.class);
	
	private ConceptHandler conceptHandler;
	private GridServiceDao gridServiceDao;
	private ConceptHierarchyNodeDao conceptHierarchyNodeDao;
	private SemanticMetadataMappingDao semanticMetadataMappingDao;
	
	/**
	 * 
	 */
	public ServiceConceptIndexer() {
		
	}
	
	public void indexService(String url, Set<String> unresolvable){

		try{
			GridService gridService = getGridServiceDao().getByUrl(url);
			if(gridService == null){
				throw new Exception("No service found for " + url);
			}
			
			Map<String,ConceptHierarchyNode> mapped = new HashMap<String,ConceptHierarchyNode>();
			for(SemanticMetadataMapping mapping : gridService.getSemanticMetadataMappings()){
				String code = mapping.getSemanticMetadata().getConceptCode();
				if(!unresolvable.contains(code) && mapping.getConcept() == null){
					ConceptHierarchyNode concept = mapped.get(code);
					if(concept == null){
						concept = getConceptHierarchyNodeDao().getByConceptCode(code);
					}
					if(concept == null){
						concept = getConceptHandler().handleConcept(code);
					}
					if(concept != null){
						mapped.put(code, concept);
						concept = getConceptHierarchyNodeDao().getById(concept.getId());
						mapping.setConcept(concept);
						getSemanticMetadataMappingDao().save(mapping);
					}else{
						unresolvable.add(code);
					}
				}
			}
		}catch(Exception ex){
			throw new RuntimeException("Error indexing service: " + ex.getMessage(), ex);
		}
	}

	public ConceptHandler getConceptHandler() {
		return conceptHandler;
	}

	public void setConceptHandler(ConceptHandler conceptHandler) {
		this.conceptHandler = conceptHandler;
	}

	public GridServiceDao getGridServiceDao() {
		return gridServiceDao;
	}

	public void setGridServiceDao(GridServiceDao gridServiceDao) {
		this.gridServiceDao = gridServiceDao;
	}

	public SemanticMetadataMappingDao getSemanticMetadataMappingDao() {
		return semanticMetadataMappingDao;
	}

	public void setSemanticMetadataMappingDao(
			SemanticMetadataMappingDao semanticMetadataMappingDao) {
		this.semanticMetadataMappingDao = semanticMetadataMappingDao;
	}

	public ConceptHierarchyNodeDao getConceptHierarchyNodeDao() {
		return conceptHierarchyNodeDao;
	}

	public void setConceptHierarchyNodeDao(
			ConceptHierarchyNodeDao conceptHierarchyNodeDao) {
		this.conceptHierarchyNodeDao = conceptHierarchyNodeDao;
	}

}
