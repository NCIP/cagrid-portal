/**
 * 
 */
package org.cagrid.installer;


/**
 * @author <a href="mailto:joshua.phillips@semanticbits.com">Joshua Phillips</a>
 *
 */
public class Main {

	/**
	 * @param args
	 */
	public static void main(String[] args) {

		Installer installer = new Installer();
		installer.initialize();
		installer.run();
	}

}
