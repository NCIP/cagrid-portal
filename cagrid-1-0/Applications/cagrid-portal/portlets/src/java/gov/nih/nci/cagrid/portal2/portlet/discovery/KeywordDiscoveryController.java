/**
 * 
 */
package gov.nih.nci.cagrid.portal2.portlet.discovery;

import gov.nih.nci.cagrid.portal2.domain.DomainObject;
import gov.nih.nci.cagrid.portal2.domain.GridService;
import gov.nih.nci.cagrid.portal2.domain.Participant;
import gov.nih.nci.cagrid.portal2.domain.metadata.common.PointOfContact;
import gov.nih.nci.cagrid.portal2.portlet.SharedApplicationModel;
import gov.nih.nci.cagrid.portal2.portlet.directory.DirectoryBean;
import gov.nih.nci.cagrid.portal2.util.PortalUtils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.Map.Entry;

import javax.portlet.ActionRequest;
import javax.portlet.ActionResponse;

import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Restrictions;
import org.springframework.orm.hibernate3.HibernateTemplate;
import org.springframework.web.portlet.mvc.AbstractController;

/**
 * @author <a href="mailto:joshua.phillips@semanticbits.com">Joshua Phillips</a>
 *
 */
public class KeywordDiscoveryController extends AbstractController {
	
	private HibernateTemplate hibernateTemplate;
	private SharedApplicationModel sharedApplicationModel;
	private String successAction;

	/**
	 * 
	 */
	public KeywordDiscoveryController() {

	}
	
	public void handleActionRequestInternal(ActionRequest request,
			ActionResponse response) throws Exception {
		
		//Do some validation
		String keywordsValue = request.getParameter("keywords");
		if(PortalUtils.isEmpty(keywordsValue)){
			throw new Exception("keywords must be specified");
		}
		String[] searchFields = request.getParameterValues("searchFields");
		if(searchFields == null || searchFields.length == 0){
			throw new Exception("searchFields must be specified");
		}
		String discoveryTypeStr = request.getParameter("discoveryType");
		DiscoveryResultsBean results = new DiscoveryResultsBean();
		Class klass = null;
		DiscoveryType discoveryType = null;
		if(DiscoveryType.SERVICE.toString().equals(discoveryTypeStr)){
			klass = GridService.class;
			results.setType(DiscoveryType.SERVICE);
		}else if(DiscoveryType.PARTICIPANT.toString().equals(discoveryTypeStr)){
			klass = Participant.class;
			results.setType(DiscoveryType.PARTICIPANT);
		}else if(DiscoveryType.POC.toString().equals(discoveryTypeStr)){
			klass = PointOfContact.class;
			results.setType(DiscoveryType.POC);
		}else{
			throw new Exception("invalid discoveryType: '" + discoveryTypeStr + "'");
		}
		
		//Run search
		List<DomainObject> objects = runSearch(klass, keywordsValue, searchFields);
		results.setObjects(objects);
		
		//Add results to shared model
		getSharedApplicationModel().addDiscoveryResults(results);
		DirectoryBean directory = (DirectoryBean)getApplicationContext().getBean("directoryBeanPrototype");
		directory.setCategory("search:" + results.getId());
		directory.setType(results.getType().toString());
		directory.getScroller().setObjects(results.getObjects());
		getSharedApplicationModel().setSelectedDirectoryBean(directory);
		
		logger.debug("setting action to " + getSuccessAction());
		response.setRenderParameter("action", getSuccessAction());
	}

	private List<DomainObject> runSearch(Class klass, String keywordsValue, String[] searchFields) {
		List<DomainObject> objects = new ArrayList<DomainObject>();
		
		// TODO: This is a pretty lame search implementation. Need to optimize.
		// TODO: Also, this logic should go into the service layer.
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
			Criteria crit = sess.createCriteria(klass);
			crit.setProjection(Projections.id());
			String path = entry.getKey();
			String value = entry.getValue();
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
		sess.close();
		
		return objects;
	}

	public HibernateTemplate getHibernateTemplate() {
		return hibernateTemplate;
	}

	public void setHibernateTemplate(HibernateTemplate hibernateTemplate) {
		this.hibernateTemplate = hibernateTemplate;
	}

	public SharedApplicationModel getSharedApplicationModel() {
		return sharedApplicationModel;
	}

	public void setSharedApplicationModel(
			SharedApplicationModel sharedApplicationModel) {
		this.sharedApplicationModel = sharedApplicationModel;
	}

	public String getSuccessAction() {
		return successAction;
	}

	public void setSuccessAction(String successAction) {
		this.successAction = successAction;
	}
}
