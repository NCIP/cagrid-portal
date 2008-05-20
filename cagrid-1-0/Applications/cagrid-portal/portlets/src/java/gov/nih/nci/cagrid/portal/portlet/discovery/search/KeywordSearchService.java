/**
 * 
 */
package gov.nih.nci.cagrid.portal.portlet.discovery.search;

import gov.nih.nci.cagrid.portal.domain.*;
import gov.nih.nci.cagrid.portal.domain.dataservice.SharedCQLQuery;
import gov.nih.nci.cagrid.portal.domain.metadata.common.PointOfContact;
import gov.nih.nci.cagrid.portal.domain.metadata.common.ResearchCenterPointOfContact;
import gov.nih.nci.cagrid.portal.domain.metadata.service.ServicePointOfContact;
import gov.nih.nci.cagrid.portal.portlet.CaGridPortletApplicationException;
import gov.nih.nci.cagrid.portal.portlet.discovery.DiscoveryResults;
import gov.nih.nci.cagrid.portal.portlet.discovery.DiscoveryType;
import gov.nih.nci.cagrid.portal.portlet.util.PortletUtils;
import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Restrictions;
import org.springframework.beans.factory.annotation.Required;
import org.springframework.orm.hibernate3.HibernateTemplate;

import java.util.*;
import java.util.Map.Entry;

/**
 * @author <a href="mailto:joshua.phillips@semanticbits.com">Joshua Phillips</a>
 * 
 */
public class KeywordSearchService {

	private HibernateTemplate hibernateTemplate;
	private Map<String,List<String>> namedServiceKeywordCriteria;

	/**
	 * 
	 */
	public KeywordSearchService() {

	}

	public DiscoveryResults search(KeywordSearchBean searchBean) {
		Class klass = null;
		String keywordsValue = searchBean.getKeywords();
		String[] searchFields = searchBean.getSearchFields();
		if (DiscoveryType.SERVICE.equals(searchBean.getDiscoveryType())) {
			klass = GridService.class;
		} else if (DiscoveryType.PARTICIPANT.equals(searchBean
				.getDiscoveryType())) {
			klass = Participant.class;
		} else if (DiscoveryType.POC.equals(searchBean.getDiscoveryType())) {
			klass = PointOfContact.class;
		} else if (DiscoveryType.CQL_QUERY.equals(searchBean.getDiscoveryType())) {
			klass = SharedCQLQuery.class;
		} else {
			throw new CaGridPortletApplicationException(
					"invalid discoveryType: '" + searchBean.getDiscoveryType()
							+ "'");
		}

		List<DomainObject> objects = new ArrayList<DomainObject>();

		// TODO: This is a pretty lame search implementation. Need to optimize.
		Map<String, String> criteria = new HashMap<String, String>();

		String[] keywords = keywordsValue.split(" ");
		for (String keyword : keywords) {
			for (String searchField : searchFields) {
				List<String> namedCriteria = getNamedServiceKeywordCriteria().get(searchField);
				if(namedCriteria != null){
					for(String namedCriterion : namedCriteria){
						criteria.put(namedCriterion, "%" + keyword + "%");
					}
				}else{
					criteria.put(searchField, "%" + keyword + "%");
				}
			}
		}
		Session sess = getHibernateTemplate().getSessionFactory().openSession();
		Set<Integer> allIds = new HashSet<Integer>();
		for (Entry<String, String> entry : criteria.entrySet()) {

			String path = entry.getKey();
			String value = entry.getValue();

			Criteria crit = null;
			if (path.startsWith("domainModel")) {
				crit = sess.createCriteria(GridDataService.class);
			} else if (DiscoveryType.POC.equals(searchBean.getDiscoveryType())
					&& path.startsWith("serviceDescription")) {
				crit = sess.createCriteria(ServicePointOfContact.class);
			} else if (DiscoveryType.POC.equals(searchBean.getDiscoveryType())
					&& path.startsWith("researchCenter")) {
				crit = sess.createCriteria(ResearchCenterPointOfContact.class);
			} else {
				crit = sess.createCriteria(klass);
			}
			crit.setProjection(Projections.id());

			if (path.indexOf(".") == -1) {
				crit.add(Restrictions.like(path, value));
			} else {
				Criteria currCrit = crit;
				String[] paths = path.split("\\.");
				for (int i = 0; i < paths.length; i++) {
					String currPath = paths[i];
					if (i + 1 < paths.length) {
						currCrit = currCrit.createCriteria(currPath);
					} else {
						currCrit.add(Restrictions.like(currPath, value));
					}
				}
			}
			List<Integer> currIds = crit.list();
			for (Integer id : currIds) {
				allIds.add(id);
			}
		}
		for (Integer id : allIds) {
			objects.add((DomainObject) sess.get(klass, id));
		}

		if (DiscoveryType.POC.equals(searchBean.getDiscoveryType())) {
			Set<Person> persons = new HashSet<Person>();
			for (Iterator i = objects.iterator(); i.hasNext();) {
				PointOfContact poc = (PointOfContact) i.next();
				persons.add(poc.getPerson());
			}
			objects.clear();
			objects.addAll(persons);
		}else if(DiscoveryType.SERVICE.equals(searchBean.getDiscoveryType())){
			List<GridService> svcs = new ArrayList<GridService>();
			for(DomainObject obj : objects){
				svcs.add((GridService)obj);
			}
			svcs = PortletUtils.filterServicesByInvalidMetadata(PortletUtils.filterDormantServices(PortletUtils.filterBannedServices(svcs)));
			if(searchBean.isActiveServicesOnly()){
				svcs = PortletUtils.filterServicesByStatus(svcs, ServiceStatus.INACTIVE, ServiceStatus.UNKNOWN, ServiceStatus.INVALID);
			}
			objects.clear();
			objects.addAll(svcs);
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

	public Map<String, List<String>> getNamedServiceKeywordCriteria() {
		return namedServiceKeywordCriteria;
	}

	public void setNamedServiceKeywordCriteria(
			Map<String, List<String>> namedServiceKeywordCriteria) {
		this.namedServiceKeywordCriteria = namedServiceKeywordCriteria;
	}

}
