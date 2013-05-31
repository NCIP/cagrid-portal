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
package gov.nih.nci.cagrid.portal.portlet.discovery.list;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.springframework.orm.hibernate3.HibernateTemplate;

import gov.nih.nci.cagrid.portal.domain.DomainObject;
import gov.nih.nci.cagrid.portal.portlet.discovery.dir.AbstractDirectoryBean;
import gov.nih.nci.cagrid.portal.portlet.util.Scroller;

/**
 * @author <a href="mailto:joshua.phillips@semanticbits.com">Joshua Phillips</a>
 *
 */
public class ListBean extends AbstractDirectoryBean {
	
	private String type;
	private Scroller scroller;
	private HibernateTemplate hibernateTemplate;
	

	/**
	 * 
	 */
	public ListBean() {

	}
	
	public void refresh() {

		List refreshed = new ArrayList();
		for(Iterator i = getScroller().getObjects().iterator(); i.hasNext();){
			DomainObject obj = (DomainObject)i.next();
			obj = (DomainObject)getHibernateTemplate().get(obj.getClass(), obj.getId());
			if(obj != null){
				refreshed.add(obj);
			}
		}
		getScroller().setObjects(refreshed);
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public Scroller getScroller() {
		return scroller;
	}

	public void setScroller(Scroller scroller) {
		this.scroller = scroller;
	}

	public HibernateTemplate getHibernateTemplate() {
		return hibernateTemplate;
	}

	public void setHibernateTemplate(HibernateTemplate hibernateTemplate) {
		this.hibernateTemplate = hibernateTemplate;
	}

}
