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
package gov.nih.nci.cagrid.portal.portlet.tree;

import gov.nih.nci.cagrid.portal.domain.DomainObject;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.orm.hibernate3.HibernateTemplate;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.ParameterizableViewController;

/**
 * The purpose of this class is to associate the DomainObject in the selected
 * TreeNode with the current Hibernate Session so that lazy-loading works in
 * the JSP views.
 * 
 * @author <a href="mailto:joshua.phillips@semanticbits.com">Joshua Phillips</a>
 *
 */
public class TreeNodeDomainObjectSessionViewController extends
		ParameterizableViewController {
	
	private static final Log logger = LogFactory.getLog(TreeNodeDomainObjectSessionViewController.class);
	
	private TreeFacade treeFacade;
	private String pathParamName;
	private HibernateTemplate hibernateTemplate;
	private TreeNodeDomainObjectRetriever domainObjectRetriever = new DefaultTreeNodeDomainObjectRetriever();

	/**
	 * 
	 */
	public TreeNodeDomainObjectSessionViewController() {

	}
	
	protected ModelAndView handleRequestInternal(HttpServletRequest request,
            HttpServletResponse response)
     throws Exception{
		
		logger.debug("handling request");
		
		String path = (String)request.getAttribute(getPathParamName());
		if(path == null){
			logger.debug("No path found under " + getPathParamName());
		}else{
			logger.debug("Fetching node " + path);
			TreeNode node = getTreeFacade().getRootNode().find(path);
			
			if(node == null){
				logger.debug("No node found for path " + path);
			}else{
				
				DomainObject domainObject = getDomainObjectRetriever().retrieve(node);
				if(domainObject == null){
					logger.debug("No DomainObject instance found in " + path);
				}else{
					
					logger.debug("refreshing " + domainObject.getClass().getName() + ":" + domainObject.getId());
					node.setContent(getHibernateTemplate().get(
							domainObject.getClass(), domainObject.getId()));
				}
				
			}
		}
		
		return super.handleRequestInternal(request, response);
	}

	public String getPathParamName() {
		return pathParamName;
	}

	public void setPathParamName(String pathParamName) {
		this.pathParamName = pathParamName;
	}

	public HibernateTemplate getHibernateTemplate() {
		return hibernateTemplate;
	}

	public void setHibernateTemplate(HibernateTemplate hibernateTemplate) {
		this.hibernateTemplate = hibernateTemplate;
	}

	public TreeFacade getTreeFacade() {
		return treeFacade;
	}

	public void setTreeFacade(TreeFacade treeFacade) {
		this.treeFacade = treeFacade;
	}

	public TreeNodeDomainObjectRetriever getDomainObjectRetriever() {
		return domainObjectRetriever;
	}

	public void setDomainObjectRetriever(
			TreeNodeDomainObjectRetriever domainObjectRetriever) {
		this.domainObjectRetriever = domainObjectRetriever;
	}

}
