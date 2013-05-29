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
package gov.nih.nci.cagrid.portal.portlet.browse;

import gov.nih.nci.cagrid.portal.domain.catalog.CatalogEntry;
import gov.nih.nci.cagrid.portal.portlet.util.PortletUtils;

import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;

/**
 * @author <a href="mailto:joshua.phillips@semanticbits.com>Joshua Phillips</a>
 * 
 */
public class CatalogEntryViewBeanFactory implements ApplicationContextAware {

	private ApplicationContext applicationContext;
	private Map<String, String> entryTypeViewBeanMap = new HashMap<String, String>();

	public CatalogEntryViewBean newCatalogEntryViewBean(
			CatalogEntry catalogEntry) {
		String beanName = (String) PortletUtils.getMapValueForType(catalogEntry
				.getClass(), getEntryTypeViewBeanMap());
		if (beanName == null) {
			throw new RuntimeException("No bean name found for "
					+ catalogEntry.getClass().getName());
		}
		CatalogEntryViewBean vb = (CatalogEntryViewBean) applicationContext.getBean(beanName);
		vb.setCatalogEntry(catalogEntry);
		vb.initialize();
		return vb;
	}

	public void setApplicationContext(ApplicationContext applicationContext)
			throws BeansException {
		this.applicationContext = applicationContext;
	}

	public Map<String, String> getEntryTypeViewBeanMap() {
		return entryTypeViewBeanMap;
	}

	public void setEntryTypeViewBeanMap(Map<String, String> entryTypeViewBeanMap) {
		this.entryTypeViewBeanMap = entryTypeViewBeanMap;
	}

}
