package gov.nih.nci.cagrid.dorian.service;

import gov.nih.nci.cagrid.dorian.bean.Metadata;
import gov.nih.nci.cagrid.dorian.stubs.types.DorianInternalFault;


/**
 * @author <A href="mailto:langella@bmi.osu.edu">Stephen Langella </A>
 * @author <A href="mailto:oster@bmi.osu.edu">Scott Oster </A>
 * @author <A href="mailto:hastings@bmi.osu.edu">Shannon Hastings </A>
 * @version $Id: ArgumentManagerTable.java,v 1.2 2004/10/15 16:35:16 langella
 *          Exp $
 */
public class PropertyManager {

	private static String TABLE = "properties";
	private static String VERSION_PROPERTY = "version";
	public static float CURRENT_VERSION = 1.1F;
	private MetadataManager manager;
	private Metadata version;


	public PropertyManager(Database db) throws DorianInternalFault {
		this.manager = new MetadataManager(db, TABLE);
		version = manager.get(VERSION_PROPERTY);
		if (version == null) {
			version = new Metadata();
			version.setName("version");
			version.setDescription("The software version of this Dorian!!!");
		}
	}


	public void setCurrentVersion() throws DorianInternalFault {
		this.setVersion(CURRENT_VERSION);
	}


	public void setVersion(float version) throws DorianInternalFault {
		this.version.setValue(String.valueOf(version));
		this.manager.update(this.version);
	}


	public float getVersion() {
		String s = this.version.getValue();
		if (s == null) {
			return 1.0F;
		} else {
			return Float.valueOf(s).floatValue();
		}
	}

}
