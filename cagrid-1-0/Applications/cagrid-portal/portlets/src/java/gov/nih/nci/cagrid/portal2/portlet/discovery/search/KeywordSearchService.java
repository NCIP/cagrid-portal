/**
 * 
 */
package gov.nih.nci.cagrid.portal2.portlet.discovery.search;

import gov.nih.nci.cagrid.portal2.domain.DomainObject;
import gov.nih.nci.cagrid.portal2.domain.GridDataService;
import gov.nih.nci.cagrid.portal2.domain.GridService;
import gov.nih.nci.cagrid.portal2.domain.Participant;
import gov.nih.nci.cagrid.portal2.domain.Person;
import gov.nih.nci.cagrid.portal2.domain.metadata.common.PointOfContact;
import gov.nih.nci.cagrid.portal2.portlet.CaGridPortletApplicationException;
import gov.nih.nci.cagrid.portal2.portlet.discovery.DiscoveryResults;
import gov.nih.nci.cagrid.portal2.portlet.discovery.DiscoveryType;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.Map.Entry;

import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Restrictions;
import org.springframework.beans.factory.annotation.Required;
import org.springframework.orm.hibernate3.HibernateTemplate;

/**
 * @author <a href="mailto:joshua.phillips@semanticbits.com">Joshua Phillips</a>
 *
 */
public class KeywordSearchService {
	
	private HibernateTemplate hibernateTemplate;

	/**
	 * 
	 */
	public KeywordSearchService() {

	}
	
	public DiscoveryResults search(KeywordSearchBean searchBean){
		Class klass = null;
		String keywordsValue = searchBean.getKeywords();
		String[] searchFields = searchBean.getSearchFields();
		if(DiscoveryType.SERVICE.equals(searchBean.getDiscoveryType())){
			klass = GridService.class;
		}else if(DiscoveryType.PARTICIPANT.equals(searchBean.getDiscoveryType())){
			klass = Participant.class;
		}else if(DiscoveryType.POC.equals(searchBean.getDiscoveryType())){
			klass = PointOfContact.class;
		}else{
			throw new CaGridPortletApplicationException("invalid discoveryType: '" + searchBean.getDiscoveryType() + "'");
		}
		
		List<DomainObject> objects = new ArrayList<DomainObject>();
		
		// TODO: This is a pretty lame search implementation. Need to optimize.
		Map<String,String> criteria = new HashMap<String,String>();
		
		String[] keywords = keywordsValue.split(" ");
		for(String keyword : keywords){
			for(String searchField : searchFields){
				criteria.put(searchField, "%" + keyword + "%");
			}
		}
		Session sess = getHibernateTemplate().getSessionFactory().openSession();
		Set<Integer> allIds = new HashSet<Integer>();
		for(Entry<String,String> entry : criteria.entrySet()){
			
			String path = entry.getKey();
			String value = entry.getValue();
			
			Criteria crit = null;
			if(path.startsWith("domainModel")){
				crit = sess.createCriteria(GridDataService.class);
			}else{
				crit = sess.createCriteria(klass);
			}
			crit.setProjection(Projections.id());
			
			if(path.indexOf(".") == -1){
				crit.add(Restrictions.like(path, value));
			}else{
				Criteria currCrit = crit;
				String[] paths = path.split("\\.");
				for(int i = 0; i < paths.length; i++){
					String currPath = paths[i];
					if(i + 1 < paths.length){
						currCrit = currCrit.createCriteria(currPath);
					}else{
						currCrit.add(Restrictions.like(currPath, value));
					}
				}
			}
			List<Integer> currIds = crit.list();
			for(Integer id : currIds){
				allIds.add(id);
			}	
		}
		for(Integer id : allIds){
			objects.add((DomainObject)sess.get(klass, id));
		}
		
		if(DiscoveryType.POC.equals(searchBean.getDiscoveryType())){
			Set<Person> persons = new HashSet<Person>();
			for(Iterator i = objects.iterator(); i.hasNext();){
				PointOfContact poc = (PointOfContact)i.next();
				persons.add(poc.getPerson());
			}
			objects.clear();
			objects.addAll(persons);
		}
		
		sess.close();
		
		DiscoveryResults results = new DiscoveryResults();
		results.setObjects(objects);
		results.setType(searchBean.getDiscoveryType());
		return results;
	}

	@Required
	public HibernateTemplate getHibernateTemplate() {
		return hibernateTemplate;
	}

	public void setHibernateTemplate(HibernateTemplate hibernateTemplate) {
		this.hibernateTemplate = hibernateTemplate;
	}

}
