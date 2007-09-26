/**
 * 
 */
package gov.nih.nci.cagrid.portal2.portlet.directory;

import gov.nih.nci.cagrid.portal2.domain.DomainObject;
import gov.nih.nci.cagrid.portal2.portlet.util.Scroller;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.springframework.orm.hibernate3.HibernateTemplate;

/**
 * @author <a href="mailto:joshua.phillips@semanticbits.com">Joshua Phillips</a>
 *
 */
public class DirectoryBean {
	
	private String type;
	private String category;
	private Scroller scroller;
	private HibernateTemplate hibernateTemplate;

	/**
	 * 
	 */
	public DirectoryBean() {
	}

	public Scroller getScroller() {
		return scroller;
	}

	public void setScroller(Scroller scroller) {
		this.scroller = scroller;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public String getCategory() {
		return category;
	}

	public void setCategory(String category) {
		this.category = category;
	}

	public void refresh() {

		List refreshed = new ArrayList();
		for(Iterator i = getScroller().getObjects().iterator(); i.hasNext();){
			DomainObject obj = (DomainObject)i.next();
			refreshed.add(getHibernateTemplate().get(obj.getClass(), obj.getId()));
		}
		getScroller().setObjects(refreshed);
	}

	public HibernateTemplate getHibernateTemplate() {
		return hibernateTemplate;
	}

	public void setHibernateTemplate(HibernateTemplate hibernateTemplate) {
		this.hibernateTemplate = hibernateTemplate;
	}
}
