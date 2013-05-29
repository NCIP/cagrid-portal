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
package gov.nih.nci.cagrid.portal.aggr.status;

import gov.nih.nci.cagrid.portal.domain.GridService;
import gov.nih.nci.cagrid.portal.domain.ServiceStatus;
import gov.nih.nci.cagrid.portal.domain.StatusChange;
import junit.framework.TestCase;

import java.util.Calendar;
import java.util.Date;

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
	
	public void testDormantPolicy(){

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
		
		assertTrue("not setting service dormant that should be dormant", policy.shouldSetServiceDormant(svc1.getStatusHistory()));
		
	}

}
