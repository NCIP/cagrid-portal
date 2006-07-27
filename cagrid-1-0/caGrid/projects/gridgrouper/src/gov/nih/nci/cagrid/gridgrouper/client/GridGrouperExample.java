package gov.nih.nci.cagrid.gridgrouper.client;

import gov.nih.nci.cagrid.gridgrouper.grouper.Stem;

import java.util.Iterator;
import java.util.Set;

public class GridGrouperExample {

	public static void main(String[] args) {
		System.out.println("Running the Grid Service Client");
		try {

			GridGrouper grouper = new GridGrouper(
					"https://localhost:8443/wsrf/services/cagrid/GridGrouper");
			Stem stem = grouper.getRootStem();
			printStems(stem, "");
			
			System.out.println();
			System.out.println();
			System.out.println("Updating.............");
			System.out.println();
			System.out.println();
			updateStems(stem, " [Updated]", "This is a stem!!!");
			printStems(stem, "");
			
			System.out.println();
			System.out.println();
			System.out.println("Reseting.............");
			System.out.println();
			System.out.println();
			updateStems(stem, "", "");
			printStems(stem, "");
			
		} catch (Exception e) {
			e.printStackTrace();
			System.exit(1);
		}
	}

	public static void updateStems(Stem stem, String displayExtension, String desrciption) throws Exception {
		String dn = stem.getDisplayExtension();
		int index = dn.indexOf(" [Updated]");
		if(index != -1){
			dn = dn.substring(0,index);
		}
		stem.setDisplayExtension(dn+displayExtension);
		stem.setDescription(desrciption);
		Set s = stem.getChildStems();
		Iterator itr = s.iterator();
		while (itr.hasNext()) {
			updateStems((Stem) itr.next(), displayExtension,desrciption);
		}
	}

	public static void printStems(Stem stem, String buffer) throws Exception {
		System.out.println(buffer + stem.getDisplayExtension() + " ("
				+ stem.getUuid() + ")");
		System.out.println(buffer + "  " + "[Description:"
				+ stem.getDescription() + "]");
		try {
			System.out.println(buffer + "  " + "[Parent:"
					+ stem.getParentStem().getDisplayExtension() + "]");
		} catch (Exception e) {
		}
		System.out.println(buffer + "  " + "[Create Source:"
				+ stem.getCreateSource() + "]");
		System.out.println(buffer + "  " + "[Create Subject Id:"
				+ stem.getCreateSubject().getId() + "]");
		System.out.println(buffer + "  " + "[Create Time:"
				+ stem.getCreateTime() + "]");
		System.out.println(buffer + "  " + "[Modify Time:"
				+ stem.getModifyTime() + "]");
		Set s = stem.getChildStems();
		Iterator itr = s.iterator();
		while (itr.hasNext()) {
			printStems((Stem) itr.next(), buffer + "    ");
		}
	}

}
