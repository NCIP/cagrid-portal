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
package gov.nih.nci.cagrid.portal.portlet.discovery.search;

import gov.nih.nci.cagrid.portal.dao.ConceptHierarchyNodeDao;
import gov.nih.nci.cagrid.portal.domain.*;
import gov.nih.nci.cagrid.portal.domain.dataservice.SharedCQLQuery;
import gov.nih.nci.cagrid.portal.domain.metadata.common.PointOfContact;
import gov.nih.nci.cagrid.portal.domain.metadata.common.ResearchCenterPointOfContact;
import gov.nih.nci.cagrid.portal.domain.metadata.service.ServicePointOfContact;
import gov.nih.nci.cagrid.portal.portlet.CaGridPortletApplicationException;
import gov.nih.nci.cagrid.portal.portlet.discovery.DiscoveryResults;
import gov.nih.nci.cagrid.portal.portlet.discovery.DiscoveryType;
import gov.nih.nci.cagrid.portal.util.filter.BaseServiceFilter;
import gov.nih.nci.cagrid.portal.util.filter.ServiceFilter;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
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
 */
public class KeywordSearchService {

    private static final Log logger = LogFactory.getLog(KeywordSearchService.class);

    private HibernateTemplate hibernateTemplate;
    private Map<String, List<String>> namedServiceKeywordCriteria;

    private ConceptHierarchyNodeDao conceptHierarchyNodeDao;
    private ServiceFilter servicefilter;

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
        } else if (DiscoveryType.CONCEPT.equals(searchBean.getDiscoveryType())) {
            //not important
        } else {
            throw new CaGridPortletApplicationException(
                    "invalid discoveryType: '" + searchBean.getDiscoveryType()
                            + "'");
        }

        List<DomainObject> objects = new ArrayList<DomainObject>();

        if (DiscoveryType.CONCEPT.equals(searchBean.getDiscoveryType())) {
            List<GridService> svcs = conceptHierarchyNodeDao.getServicesByCode(searchBean.getKeywords());
            svcs = servicefilter.filter(svcs);
            if (searchBean.isActiveServicesOnly()) {
                svcs = BaseServiceFilter.filterServicesByStatus(svcs, ServiceStatus.INACTIVE, ServiceStatus.UNKNOWN, ServiceStatus.INVALID);
            }
            objects.addAll(svcs);
            searchBean.setDiscoveryType(DiscoveryType.SERVICE);
        } else {
            // TODO: This is a pretty lame search implementation. Need to optimize.
            Map<String, String> criteria = new HashMap<String, String>();

            String[] keywords = keywordsValue.split(" ");
            for (String keyword : keywords) {
                for (String searchField : searchFields) {
                    List<String> namedCriteria = getNamedServiceKeywordCriteria().get(searchField);
                    if (namedCriteria != null) {
                        for (String namedCriterion : namedCriteria) {
                            criteria.put(namedCriterion, getKeywordExpression(namedCriterion, keyword));
                        }
                    } else {
                        criteria.put(searchField, getKeywordExpression(searchField, keyword));
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
                    addCriterion(crit, path, value);
                } else {
                    Criteria currCrit = crit;
                    String[] paths = path.split("\\.");
                    for (int i = 0; i < paths.length; i++) {
                        String currPath = paths[i].trim();
                        if (i + 1 < paths.length) {
                            currCrit = currCrit.createCriteria(currPath);
                        } else {
                            addCriterion(currCrit, currPath, value);
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
            } else if (DiscoveryType.SERVICE.equals(searchBean.getDiscoveryType())) {
                List<GridService> svcs = new ArrayList<GridService>();
                for (DomainObject obj : objects) {
                    svcs.add((GridService) obj);
                }
                svcs = servicefilter.filter(svcs);
                if (searchBean.isActiveServicesOnly()) {
                    svcs = BaseServiceFilter.filterServicesByStatus(svcs, ServiceStatus.INACTIVE, ServiceStatus.UNKNOWN, ServiceStatus.INVALID);
                }
                objects.clear();
                objects.addAll(svcs);
            }

            sess.close();
        }
        DiscoveryResults results = new DiscoveryResults();
        results.setObjects(objects);
        results.setType(searchBean.getDiscoveryType());
        return results;
    }

    private void addCriterion(Criteria crit, String path, String value) {
        //TODO: This is a hack.
        if (path.endsWith("publicID")) {
            try {
                Long _cdeValue = Long.parseLong(value.trim());
                crit.add(Restrictions.eq(path, _cdeValue));
            } catch (Exception ex) {
                logger.warn("Couldn't parse publicID value: " + value);
                crit.add(Restrictions.eq(path, Long.parseLong("-1")));
            }
        } else {
            crit.add(Restrictions.like(path, value));
        }
    }

    private String getKeywordExpression(String criterionName, String keyword) {
        String expression = "%" + keyword.trim() + "%";
        // TODO: This is a hack.
        if (criterionName.endsWith("publicID")) {
            expression = keyword.trim();
        }
        return expression;
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

    public ConceptHierarchyNodeDao getConceptHierarchyNodeDao() {
        return conceptHierarchyNodeDao;
    }

    public void setConceptHierarchyNodeDao(ConceptHierarchyNodeDao conceptHierarchyNodeDao) {
        this.conceptHierarchyNodeDao = conceptHierarchyNodeDao;
    }

    public ServiceFilter getServicefilter() {
        return servicefilter;
    }

    public void setServicefilter(ServiceFilter servicefilter) {
        this.servicefilter = servicefilter;
    }
}
