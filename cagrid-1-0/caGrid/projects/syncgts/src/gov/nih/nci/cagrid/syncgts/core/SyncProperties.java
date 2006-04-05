package gov.nih.nci.cagrid.syncgts.core;

import java.util.ArrayList;
import java.util.List;


/**
 * @author <A href="mailto:langella@bmi.osu.edu">Stephen Langella </A>
 * @author <A href="mailto:oster@bmi.osu.edu">Scott Oster </A>
 * @author <A href="mailto:hastings@bmi.osu.edu">Shannon Hastings </A>
 * @version $Id: ArgumentManagerTable.java,v 1.2 2004/10/15 16:35:16 langella
 *          Exp $
 */
public class SyncProperties {

	private List gtsServices;
	private boolean deleteUnknownFiles;
	private boolean errorOnConflicts;
	private String filePrefix;


	public SyncProperties() {
		this.gtsServices = new ArrayList();
		this.deleteUnknownFiles = false;
		this.errorOnConflicts = true;
		this.filePrefix = "gts";
	}


	public void addSyncDescriptor(SyncDescriptor gts) {
		this.gtsServices.add(gts);
	}


	public int getSyncDescriptorCount() {
		return this.gtsServices.size();
	}


	public SyncDescriptor getSyncDescriptor(int index) {
		return (SyncDescriptor) this.gtsServices.get(index);
	}


	public boolean deleteUnknownFiles() {
		return deleteUnknownFiles;
	}


	public void setDeleteUnknownFiles(boolean deleteUnknownFiles) {
		this.deleteUnknownFiles = deleteUnknownFiles;
	}


	public boolean errorOnConflicts() {
		return errorOnConflicts;
	}


	public void setErrorOnConflicts(boolean errorOnConflicts) {
		this.errorOnConflicts = errorOnConflicts;
	}


	public String getFilePrefix() {
		return filePrefix;
	}

}
