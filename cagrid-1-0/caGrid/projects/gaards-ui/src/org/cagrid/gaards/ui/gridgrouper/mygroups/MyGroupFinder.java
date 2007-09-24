package org.cagrid.gaards.ui.gridgrouper.mygroups;

import gov.nih.nci.cagrid.common.Runner;
import gov.nih.nci.cagrid.common.Utils;
import gov.nih.nci.cagrid.gridgrouper.client.GridGrouper;

import org.globus.gsi.GlobusCredential;


/**
 * @author <A HREF="MAILTO:langella@bmi.osu.edu">Stephen Langella</A>
 * @author <A HREF="MAILTO:oster@bmi.osu.edu">Scott Oster</A>
 * @author <A HREF="MAILTO:hastings@bmi.osu.edu">Shannon Hastings</A>
 * @author <A HREF="MAILTO:ervin@bmi.osu.edu">David W. Ervin</A>
 * @version $Id: GridGrouperBaseTreeNode.java,v 1.1 2006/08/04 03:49:26 langella
 *          Exp $
 */
public class MyGroupFinder extends Runner {

	private String gridGrouperURI;
	private GlobusCredential proxy;
	private MyGroupsTable groupsTable;
	private boolean isSuccessful = false;
	private String error = null;


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
			isSuccessful = true;
		} catch (Exception e) {
			error = Utils.getExceptionMessage(e);
			if ((error.indexOf("Operation name could not be determined") >= 0)) {
				error = "The Grid Grouper service maybe an older version which does not support looking up a member's groups.";
			}
		}
	}


	public String getGridGrouperURI() {
		return gridGrouperURI;
	}


	public boolean isSuccessful() {
		return isSuccessful;
	}


	public String getError() {
		return error;
	}

}
