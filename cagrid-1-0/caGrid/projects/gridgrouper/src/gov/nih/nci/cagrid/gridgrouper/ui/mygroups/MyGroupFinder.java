package gov.nih.nci.cagrid.gridgrouper.ui.mygroups;

import gov.nih.nci.cagrid.gridgrouper.client.GridGrouper;

import org.globus.gsi.GlobusCredential;
import org.projectmobius.common.MobiusRunnable;


/**
 * @author <A HREF="MAILTO:langella@bmi.osu.edu">Stephen Langella</A>
 * @author <A HREF="MAILTO:oster@bmi.osu.edu">Scott Oster</A>
 * @author <A HREF="MAILTO:hastings@bmi.osu.edu">Shannon Hastings</A>
 * @author <A HREF="MAILTO:ervin@bmi.osu.edu">David W. Ervin</A>
 * @version $Id: GridGrouperBaseTreeNode.java,v 1.1 2006/08/04 03:49:26 langella
 *          Exp $
 */
public class MyGroupFinder extends MobiusRunnable {

	private String gridGrouperURI;
	private GlobusCredential proxy;
	private MyGroupsTable groupsTable;


	public MyGroupFinder(String gridGrouperURI, GlobusCredential proxy, MyGroupsTable groupsTable) {
		this.gridGrouperURI = gridGrouperURI;
		this.proxy = proxy;
		this.groupsTable = groupsTable;

	}


	public void execute() {
		try {
			String targetIdentity = proxy.getIdentity();
			GridGrouper gridGrouper = new GridGrouper(gridGrouperURI, proxy);
			groupsTable.addGroups(gridGrouper.getMembersGroups(targetIdentity));
			System.out.println("Got groups from "+gridGrouperURI);
		} catch (Exception e) {
			//e.printStackTrace();
			System.err.println("Could not get groups from "+gridGrouperURI);
		}
	}
}
