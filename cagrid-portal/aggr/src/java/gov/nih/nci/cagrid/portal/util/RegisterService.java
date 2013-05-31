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
package gov.nih.nci.cagrid.portal.util;

import gov.nih.nci.cagrid.portal.aggr.regsvc.RegisteredServiceEvent;
import gov.nih.nci.cagrid.portal.aggr.regsvc.RegisteredServiceListener;

import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

/**
 * @author <a href="mailto:joshua.phillips@semanticbits.com">Joshua Phillips</a>
 *
 */
public class RegisterService {

	/**
	 * @param args
	 */
	public static void main(String[] args) throws Exception {
		
		ApplicationContext ctx = new ClassPathXmlApplicationContext(new String[] {
				"classpath:applicationContext-db.xml",
				"classpath:applicationContext-aggr-regsvcman.xml" });		

		RegisteredServiceListener listener = (RegisteredServiceListener)ctx.getBean("registeredServiceListener");
		RegisteredServiceEvent event = new RegisteredServiceEvent("");
		event.setIndexServiceUrl(args[0]);
		event.setServiceUrl(args[1]);
		listener.persistService(event);
		
	}

}
