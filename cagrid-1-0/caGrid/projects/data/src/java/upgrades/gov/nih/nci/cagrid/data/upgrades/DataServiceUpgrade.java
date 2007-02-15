package gov.nih.nci.cagrid.data.upgrades;

import java.io.File;

/** 
 *  DataServiceUpgrade
 *  Basic interface for an upgrade to a data service
 * 
 * @author <A HREF="MAILTO:ervin@bmi.osu.edu">David W. Ervin</A>  * 
 * @created Feb 8, 2007 
 * @version $Id: DataServiceUpgrade.java,v 1.1 2007-02-15 16:02:10 dervin Exp $ 
 */
public interface DataServiceUpgrade {

	public void upgrade(File inServiceDir, File outServiceDir) throws UpgradeException;
}
