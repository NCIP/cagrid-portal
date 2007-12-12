/**
 * 
 */
package gov.nih.nci.cagrid.portal.aggr.status;

import gov.nih.nci.cagrid.portal.domain.GridService;
import gov.nih.nci.cagrid.portal.domain.ServiceStatus;
import gov.nih.nci.cagrid.portal.domain.StatusChange;

import java.util.Calendar;
import java.util.Date;

import junit.framework.TestCase;

/**
 * @author <a href="mailto:joshua.phillips@semanticbits.com">Joshua Phillips</a>
 *
 */
public class ServiceStatusTest extends TestCase {

	/**
	 * 
	 */
	public ServiceStatusTest() {

	}

	/**
	 * @param name
	 */
	public ServiceStatusTest(String name) {
		super(name);

	}
	
	public void testBanningPolicy(){

		SimpleServiceStatusPolicy policy = new SimpleServiceStatusPolicy();
		policy.setMaxDowntimeHours(1);
		
		Calendar cal = Calendar.getInstance();
		cal.add(Calendar.HOUR, -2);
		Date wentDown = cal.getTime();
		cal.add(Calendar.HOUR, -4);
		Date started = cal.getTime();
		
		GridService svc1 = new GridService();
		svc1.setUrl("http://one");
		
		StatusChange status = new StatusChange();
		status.setTime(started);
		status.setStatus(ServiceStatus.ACTIVE);
		status.setService(svc1);
		svc1.getStatusHistory().add(status);
		
		status = new StatusChange();
		status.setTime(wentDown);
		status.setStatus(ServiceStatus.INACTIVE);
		status.setService(svc1);
		svc1.getStatusHistory().add(status);
		
		assertTrue("not banning service that should be banned", policy.shouldBanService(svc1.getStatusHistory()));
		
	}

}
