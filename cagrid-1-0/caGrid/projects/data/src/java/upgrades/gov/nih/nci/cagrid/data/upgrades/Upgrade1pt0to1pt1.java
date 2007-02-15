package gov.nih.nci.cagrid.data.upgrades;

import java.io.File;

import org.jdom.Element;
import org.jdom.Namespace;
import org.projectmobius.common.MobiusException;
import org.projectmobius.common.XMLUtilities;

/** 
 *  Upgrade1pt0to1pt1
 *  Utility to upgrade a caGrid 1.0 data service to a 1.1 data service
 * 
 * @author <A HREF="MAILTO:ervin@bmi.osu.edu">David W. Ervin</A>  * 
 * @created Feb 8, 2007 
 * @version $Id: Upgrade1pt0to1pt1.java,v 1.1 2007-02-15 16:02:10 dervin Exp $ 
 */
public class Upgrade1pt0to1pt1 implements DataServiceUpgrade {
	public static final String EXTENSIONS_NAMESPACE = "gme://gov.nih.nci.cagrid.introduce/1/Extension";
	public static final String DATA_NAMESPACE = "http://CQL.caBIG/1/gov.nih.nci.cagrid.data.extension";
	
	public void upgrade(File inServiceDir, File outServiceDir) throws UpgradeException {
		// load the introduce.xml from the input service
		Element inputModel = null;
		try {
			inputModel = XMLUtilities.fileNameToDocument(
				inServiceDir.getAbsolutePath() + File.separator + "introduce.xml").getRootElement();
		} catch (MobiusException ex) {
			throw new UpgradeException(ex);
		}
		// start with the old model
		Element outputModel = (Element) inputModel.clone();
		// locate and remove the data service extension data element
		Element extensionsElement = outputModel.getChild("Extensions", Namespace.getNamespace(EXTENSIONS_NAMESPACE));
		Element dataExtensionElement = extensionsElement.getChild("data", extensionsElement.getNamespace());
		if (dataExtensionElement == null) {
			throw new UpgradeException("No data service extension found in service model");
		}
		// get the extension data
		Element extensionDataContainer = dataExtensionElement.getChild("ExtensionData");
		Element extensionData = extensionDataContainer.getChild("Data", Namespace.getNamespace(DATA_NAMESPACE));
		
	}
	

	public static void main(String[] args) {
		if (args.length != 2) {
			usage();
			System.exit(1);
		}
		File inputServiceDir = new File(args[0]);
		File outputServiceDir = new File(args[1]);
		if (!inputServiceDir.isDirectory() || !inputServiceDir.exists()) {
			System.err.println("Error: Input directory is not valid");
		}
		if (!outputServiceDir.exists()) {
			System.out.println("Output directory will be created");
			outputServiceDir.mkdirs();
		}
		if (!outputServiceDir.isDirectory()) {
			System.err.println("Error: Output directory is not valid");
		}
		
		DataServiceUpgrade upgrade = new Upgrade1pt0to1pt1();
		try {
			upgrade.upgrade(inputServiceDir, outputServiceDir);
		} catch (UpgradeException ex) {
			System.err.println("Error performing upgrade:");
			System.err.println(ex.getMessage());
			ex.printStackTrace();
		}
	}
	
	
	private static void usage() {
		System.out.println("Usage: ");
		System.out.println(Upgrade1pt0to1pt1.class.getName() + " <oldServiceDir> <newServiceDir>");
	}
}
