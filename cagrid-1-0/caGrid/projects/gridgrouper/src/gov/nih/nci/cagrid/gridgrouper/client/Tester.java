package gov.nih.nci.cagrid.gridgrouper.client;

import gov.nih.nci.cagrid.gridgrouper.grouper.GroupI;
import gov.nih.nci.cagrid.gridgrouper.grouper.GrouperI;

import java.util.Iterator;
import java.util.Set;

public class Tester {
	public static void main(String[] args) {
		try {
			String uri = "https://localhost:8443/wsrf/services/cagrid/GridGrouper";
			String user = "/O=OSU/OU=BMI/OU=caGrid/OU=Dorian/OU=cagrid05/CN=jdoe";
			String group = "MyStem:MyGroup";

			// Create a Grid Grouper Instance
			GrouperI grouper = new GridGrouper(uri);

			// Obtain a handle to the group object.
			GroupI mygroup = grouper.findGroup(group);
			
			
			Set s = mygroup.getMembers();
			Iterator itr = s.iterator();
			//Iterate over and print out the members of the group
			while (itr.hasNext()) {
				Member m = (Member) itr.next();
				System.out.println("The user " + m.getSubjectId()
						+ " is a member of " + mygroup.getDisplayExtension());
			}
		} catch (Exception e) {
			e.printStackTrace();
		}

	}
}
