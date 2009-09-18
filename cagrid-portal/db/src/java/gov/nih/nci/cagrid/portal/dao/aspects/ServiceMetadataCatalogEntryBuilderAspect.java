/**
 * 
 */
package gov.nih.nci.cagrid.portal.dao.aspects;

import gov.nih.nci.cagrid.portal.dao.catalog.ServiceMetadataCatalogEntryBuilder;
import gov.nih.nci.cagrid.portal.domain.GridService;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;

/**
 * @author <a href="mailto:joshua.phillips@semanticbits.com">Joshua Phillips</a>
 * 
 */
@Aspect
public class ServiceMetadataCatalogEntryBuilderAspect {

	private static final Log logger = LogFactory
			.getLog(ServiceMetadataCatalogEntryBuilderAspect.class);

	private ServiceMetadataCatalogEntryBuilder serviceMetadataCatalogEntryBuilder;

	@AfterReturning("execution(* gov.nih.nci.cagrid.portal.dao.GridServiceDao.save*(gov.nih.nci.cagrid.portal.domain.GridService)) && !within(gov.nih.nci.cagrid.portal.dao.catalog.ServiceMetadataCatalogEntryBuilder)  && args(service)")
	public void onSave(GridService service) throws Throwable {
		try {
			getServiceMetadataCatalogEntryBuilder().build(service);
		} catch (Exception ex) {
			logger
					.error("Error creating catalog entry: " + ex.getMessage(),
							ex);
		}
	}

	public ServiceMetadataCatalogEntryBuilder getServiceMetadataCatalogEntryBuilder() {
		return serviceMetadataCatalogEntryBuilder;
	}

	public void setServiceMetadataCatalogEntryBuilder(
			ServiceMetadataCatalogEntryBuilder serviceMetadataCatalogEntryBuilder) {
		this.serviceMetadataCatalogEntryBuilder = serviceMetadataCatalogEntryBuilder;
	}

}
