package gov.nih.nci.cagrid.gridgrouper.client;

import edu.internet2.middleware.subject.Subject;
import gov.nih.nci.cagrid.gridgrouper.common.SubjectUtils;
import gov.nih.nci.cagrid.gridgrouper.grouper.NamingPrivilege;
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
		System.out.println(buffer + "  " + "Description:"
				+ stem.getDescription());
		try {
			System.out.println(buffer + "  " + "Parent:"
					+ stem.getParentStem().getDisplayExtension());
		} catch (Exception e) {
		}
		System.out.println(buffer + "  " + "Create Source:"
				+ stem.getCreateSource());
		System.out.println(buffer + "  " + "Create Subject Id:"
				+ stem.getCreateSubject().getId());
		System.out.println(buffer + "  " + "Create Time:"
				+ stem.getCreateTime());
		System.out.println(buffer + "  " + "Modify Time:"
				+ stem.getModifyTime());
		
		Set stemmers = stem.getStemmers();
		System.out.println(buffer + "  " + "Stemmers:");
		Iterator i2 = stemmers.iterator();
		while(i2.hasNext()){
			Subject sbj = (Subject)i2.next();
			System.out.println(buffer + "    " 
					+ sbj.getId());
		}
		
		Set creators = stem.getCreators();
		System.out.println(buffer + "  " + "Creators:");
		Iterator i1 = creators.iterator();
		while(i1.hasNext()){
			Subject sbj = (Subject)i1.next();
			System.out.println(buffer + "    " 
					+ sbj.getId());
		}
		Subject sub = SubjectUtils.getSubject("/O=OSU/OU=BMI/OU=caGrid/OU=Dorian/OU=cagrid05/OU=IdP [1]/CN=langella");
		Set privs = stem.getPrivs(sub);
		System.out.println(buffer + "  " + "Privileges for "+sub.getId()+":");
		Iterator i3= privs.iterator();
		while(i3.hasNext()){
			NamingPrivilege priv = (NamingPrivilege)i3.next();
			System.out.println(buffer + "    " 
					+ priv.toString());
		}
		
	
		
		
		Set s = stem.getChildStems();
		Iterator itr = s.iterator();
		while (itr.hasNext()) {
			System.out.println();
			System.out.println();
			printStems((Stem) itr.next(), buffer);
		
		}
	}

}
