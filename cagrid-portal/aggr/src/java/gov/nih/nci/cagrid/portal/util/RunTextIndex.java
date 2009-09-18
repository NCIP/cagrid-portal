/**
 * 
 */
package gov.nih.nci.cagrid.portal.util;

import org.springframework.context.support.ClassPathXmlApplicationContext;

/**
 * @author <a href="mailto:joshua.phillips@semanticbits.com">Joshua Phillips</a>
 * 
 */
public class RunTextIndex {

	/**
	 * @param args
	 */
	public static void main(String[] args) throws Exception {

		new ClassPathXmlApplicationContext(new String[] {
				"classpath:applicationContext-db.xml",
				"classpath:applicationContext-aggr-text.xml" });

	}

}
