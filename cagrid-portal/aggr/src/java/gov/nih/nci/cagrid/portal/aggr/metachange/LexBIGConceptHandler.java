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
import gov.nih.nci.cagrid.portal.domain.ConceptHierarchy;
import gov.nih.nci.cagrid.portal.domain.ConceptHierarchyNode;
import gov.nih.nci.system.applicationservice.EVSApplicationService;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.LexGrid.LexBIG.DataModel.Collections.AssociatedConceptList;
import org.LexGrid.LexBIG.DataModel.Collections.ResolvedConceptReferenceList;
import org.LexGrid.LexBIG.DataModel.Core.AssociatedConcept;
import org.LexGrid.LexBIG.DataModel.Core.Association;
import org.LexGrid.LexBIG.DataModel.Core.CodingSchemeVersionOrTag;
import org.LexGrid.LexBIG.DataModel.Core.ResolvedConceptReference;
import org.LexGrid.LexBIG.Extensions.Generic.LexBIGServiceConvenienceMethods;
import org.LexGrid.LexBIG.LexBIGService.CodedNodeSet;
import org.LexGrid.concepts.Definition;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.orm.hibernate3.HibernateTemplate;

/**
 * @author <a href="mailto:joshua.phillips@semanticbits.com">Joshua Phillips</a>
 * 
 */
public class LexBIGConceptHandler implements ConceptHandler {

	private static final Log logger = LogFactory
			.getLog(LexBIGConceptHandler.class);

	private HibernateTemplate hibernateTemplate;
	private EVSApplicationService evsApplicationService;
	private CodingSchemeVersionOrTag version;
	private String codingSchemeUri;
	private ConceptHierarchyNodeDao conceptHierarchyNodeDao;

	/**
	 * 
	 */
	public LexBIGConceptHandler() {

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see gov.nih.nci.cagrid.portal.aggr.metachange.ConceptHandler#handleConcept(java.lang.String)
	 */
	public ConceptHierarchyNode handleConcept(String code) {
		ConceptHierarchyNode node = null;
		try {
			node = handleCode(getHierarchy(), code);
		} catch (Exception ex) {
			throw new RuntimeException("Error handling concept: "
					+ ex.getMessage(), ex);
		}
		return node;
	}

	private ConceptHierarchyNode handleCode(ConceptHierarchy hierarchy,
			String code) throws Exception {

		ConceptHierarchyNode leaf = null;
		ResolvedConceptReference[] pathFromRoot = getPathFromRoot(code);
		List<ConceptHierarchyNode> pathFromRootNodes = new ArrayList<ConceptHierarchyNode>();
		for (int idx = 0; idx < pathFromRoot.length; idx++) {
			ResolvedConceptReference ac = pathFromRoot[idx];
			logger.debug("ac = " + ac.getEntityDescription().getContent());
			ConceptHierarchyNode node = getConceptHierarchyNodeDao()
					.getByConceptInHierarchy(hierarchy, pathFromRootNodes,
							ac.getConceptCode());

			if (node == null) {
				logger.debug("creating new node");
				node = createNode(hierarchy, idx, ac);

				if (idx > 0) {
					// Then we're on some child

					// Add current node as child to parent
					ConceptHierarchyNode parentNode = pathFromRootNodes
							.get(idx - 1);
					logger.debug("adding node as child to "
							+ parentNode.getId());
					node.setParent(parentNode);

					// Add current node as descendant to each ancestor
					for (ConceptHierarchyNode ancestorNode : pathFromRootNodes) {
						logger.debug("adding node as descendant to "
								+ ancestorNode.getId());
						node.getAncestors().add(ancestorNode);
					}
					getConceptHierarchyNodeDao().save(node);
				}
				getHibernateTemplate().flush();
			}
			pathFromRootNodes.add(node);
		}
		if(pathFromRootNodes.size() > 0){
			leaf = pathFromRootNodes.get(pathFromRoot.length - 1);
		}
		return leaf;
	}

	private ConceptHierarchyNode createNode(ConceptHierarchy hierarchy,
			int level, ResolvedConceptReference ac) {
		ConceptHierarchyNode node = new ConceptHierarchyNode();
		node.setHierarchy(hierarchy);
		node.setLevel(level);
		node.setName(ac.getEntityDescription().getContent());
		node.setCode(ac.getConceptCode());
		Definition[] defs = ac.getReferencedEntry().getDefinition();
		if (defs.length > 0) {
			node.setDescription(defs[0].getText().getContent());
		}
		getConceptHierarchyNodeDao().save(node);
		return node;
	}

	private ConceptHierarchy getHierarchy() throws Exception {

		ConceptHierarchy h = getConceptHierarchyNodeDao().getHierarchyByUri(
				getCodingSchemeUri());
		if (h == null) {
			h = new ConceptHierarchy();
			h.setUri(getCodingSchemeUri());
			getHibernateTemplate().save(h);
		}
		return h;
	}

	private ResolvedConceptReference[] getPathFromRoot(String code)
			throws Exception {

		ArrayList<ResolvedConceptReference> pathList = new ArrayList<ResolvedConceptReference>();
		String childCode = code;
		String parentCode = null;
		LexBIGServiceConvenienceMethods lbscm = (LexBIGServiceConvenienceMethods) getEvsApplicationService()
				.getGenericExtension("LexBIGServiceConvenienceMethods");
		do {
			AssociatedConcept ac = null;
			parentCode = null;

			Association assoc = lbscm.getParentsOf(childCode, null,
					"hasSubtype", getCodingSchemeUri(), getVersion(), true);
			if (assoc != null) {
				AssociatedConceptList l = assoc.getAssociatedConcepts();
				if (l != null) {
					AssociatedConcept[] acl = l.getAssociatedConcept();
					if (acl.length > 1) {
						logger.warn(acl.length + " parent concepts for "
								+ childCode + ". Taking first.");
						for (AssociatedConcept c : acl) {
							logger.warn("PARENT: " + c.getConceptCode() + ":"
									+ c.getEntityDescription().getContent());
						}
						acl = new AssociatedConcept[] { acl[0] };
					}
					if (acl.length == 1) {
						ac = acl[0];
						parentCode = acl[0].getConceptCode();
						childCode = parentCode;
					}
				}
			}
			if (parentCode != null) {
				pathList.add(0, ac);
			}
		} while (parentCode != null);

		// Get the actual leaf concept
		CodedNodeSet cns = lbscm.createCodeNodeSet(new String[] { code },
				getCodingSchemeUri(), getVersion());
		ResolvedConceptReferenceList rcrl = cns.resolveToList(null, null, null,
				1);
		ResolvedConceptReference[] rcr = rcrl.getResolvedConceptReference();
		if (rcr.length == 0) {
			logger.debug("Couldn't resolve concept code: " + code);
		} else {
			pathList.add(rcr[0]);
		}

		return (ResolvedConceptReference[]) pathList
				.toArray(new ResolvedConceptReference[pathList.size()]);
	}

	public void processNewConcepts() {
		try {

			ConceptHierarchy hierarchy = getHierarchy();

			String getNewConceptCodes = "select distinct(conceptCode) as code "
					+ "from SemanticMetadata " + "where conceptCode not in ("
					+ "select distinct(code) from ConceptHierarchyNode)";
			List conceptCodes = getHibernateTemplate().find(getNewConceptCodes);
			for (Iterator i = conceptCodes.iterator(); i.hasNext();) {
				String code = (String) i.next();
				handleCode(hierarchy, code);
			}
		} catch (Exception ex) {
			String msg = "Error processing new concepts: " + ex.getMessage();
			logger.error(msg, ex);
			throw new RuntimeException(msg, ex);
		}
	}

	public HibernateTemplate getHibernateTemplate() {
		return hibernateTemplate;
	}

	public void setHibernateTemplate(HibernateTemplate hibernateTemplate) {
		this.hibernateTemplate = hibernateTemplate;
	}

	public EVSApplicationService getEvsApplicationService() {
		return evsApplicationService;
	}

	public void setEvsApplicationService(
			EVSApplicationService evsApplicationService) {
		this.evsApplicationService = evsApplicationService;
	}

	public CodingSchemeVersionOrTag getVersion() {
		return version;
	}

	public void setVersion(CodingSchemeVersionOrTag version) {
		this.version = version;
	}

	public String getCodingSchemeUri() {
		return codingSchemeUri;
	}

	public void setCodingSchemeUri(String codingSchemeUri) {
		this.codingSchemeUri = codingSchemeUri;
	}

	public ConceptHierarchyNodeDao getConceptHierarchyNodeDao() {
		return conceptHierarchyNodeDao;
	}

	public void setConceptHierarchyNodeDao(
			ConceptHierarchyNodeDao conceptHierarchyNodeDao) {
		this.conceptHierarchyNodeDao = conceptHierarchyNodeDao;
	}

}
