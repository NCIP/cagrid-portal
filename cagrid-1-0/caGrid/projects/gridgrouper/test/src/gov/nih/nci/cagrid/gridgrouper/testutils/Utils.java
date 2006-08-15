package gov.nih.nci.cagrid.gridgrouper.testutils;

import gov.nih.nci.cagrid.gridgrouper.bean.StemDescriptor;
import gov.nih.nci.cagrid.gridgrouper.bean.StemIdentifier;

public class Utils {

	public static StemIdentifier getStemIdentifier(StemDescriptor des) {
		StemIdentifier id = new StemIdentifier();
		id.setStemName(des.getName());
		return id;
	}

	public static StemIdentifier getRootStemIdentifier() {
		StemIdentifier id = new StemIdentifier();
		id
				.setStemName(gov.nih.nci.cagrid.gridgrouper.client.GridGrouper.ROOT_STEM);
		return id;
	}

}
