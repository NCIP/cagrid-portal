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

import gov.nih.nci.cagrid.portal.dao.GridServiceDao;
import gov.nih.nci.cagrid.portal.domain.GridService;

import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

/**
 * @author <a href="mailto:joshua.phillips@semanticbits.com">Joshua Phillips</a>
 * 
 */
public class ServiceBanManager {

	/**
	 * 
	 */
	public ServiceBanManager() {

	}

	/**
	 * @param args
	 */
	public static void main(String[] args) throws Exception {

		if (args.length != 2) {
			System.out.println("Usage: ban|unban <url>");
		} else {
			String banOp = args[0];
			String url = args[1];
			ApplicationContext ctx = new ClassPathXmlApplicationContext(
					new String[] { "classpath:applicationContext-db.xml" });
			GridServiceDao dao = (GridServiceDao) ctx.getBean("gridServiceDao");
			GridService svc = dao.getByUrl(url);
			if (svc == null) {
				System.err.println("NO SERVICE FOUND FOR URL: " + url);
				return;
			} else {
				if ("ban".equals(banOp)) {
					dao.banService(svc);
					System.out.println("Successfully banned " + url);
				}else{
					dao.unbanService(svc);
					System.out.println("Successfully un-banned " + url);
				}
			}
		}
	}

}
