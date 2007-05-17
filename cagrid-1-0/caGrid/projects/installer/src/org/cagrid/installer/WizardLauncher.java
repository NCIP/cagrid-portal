/**
 * 
 */
package org.cagrid.installer;

import org.pietschy.wizard.Wizard;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

/**
 * @author <a href="mailto:joshua.phillips@semanticbits.com">Joshua Phillips</a>
 *
 */
public class WizardLauncher {
	
	

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		String resource = System.getProperty("cagrid.installer.config", "/wizard.xml");
		ApplicationContext ctx = new ClassPathXmlApplicationContext(resource);
		CaGridWizard wizard = (CaGridWizard)ctx.getBean("wizard");
		wizard.show();
	}

}
