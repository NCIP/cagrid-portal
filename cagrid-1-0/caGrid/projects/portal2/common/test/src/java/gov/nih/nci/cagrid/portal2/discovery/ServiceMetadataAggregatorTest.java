/**
 * 
 */
package gov.nih.nci.cagrid.portal2.discovery;

import gov.nih.nci.cagrid.portal2.dao.ServiceMetadataDao;
import gov.nih.nci.cagrid.portal2.util.ServiceMetadataBuilder;

import java.util.Iterator;
import java.util.List;

import junit.framework.TestCase;

import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.springframework.orm.hibernate3.HibernateTemplate;

/**
 * @author <a href="joshua.phillips@semanticbits.com">Joshua Phillips</a>
 * 
 */
public class ServiceMetadataAggregatorTest extends TestCase {

	private ApplicationContext ctx;

	public ServiceMetadataAggregatorTest() {
		super();
		init();
	}

	public ServiceMetadataAggregatorTest(String name) {
		super(name);
		init();
	}

	private void init() {
		try {
			// Init application context.
			this.ctx = new ClassPathXmlApplicationContext(
					new String[] { "classpath*:applicationContext-*.xml" });
		} catch (Exception ex) {
			throw new RuntimeException(
					"Error initializing: " + ex.getMessage(), ex);
		}
	}

	public void testAggregateMetadata() {
		ServiceMetadataDao dao = (ServiceMetadataDao) this.ctx
				.getBean("serviceMetadataDao");
		ServiceMetadataBuilder builder = new ServiceMetadataBuilder();
		builder.setHibernateTemplate((HibernateTemplate)this.ctx.getBean("hibernateTemplate"));
		builder.setPersist(true);
		ServiceMetadataAggregator agg = new ServiceMetadataAggregator();
		agg.setServiceMetadataDao(dao);
		agg.setMetadataCompliance(true);
		agg.setServiceMetadataBuilder(builder);
		List urls = (List) this.ctx.getBean("defaultIndexServiceUrls");
		System.out.println("Got " + urls.size() + " urls.");
		for (Iterator i = urls.iterator(); i.hasNext();) {
			String url = (String) i.next();
			agg.setIndexServiceUrl(url);
			try {
				agg.execute();
			} catch (Exception ex) {
				ex.printStackTrace();
				fail("Error encountered: " + ex.getMessage());
			}
		}
	}

}
