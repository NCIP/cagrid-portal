package gov.nih.nci.cagrid.syncgts.core;

import gov.nih.nci.cagrid.gts.bean.TrustedAuthorityFilter;

import java.util.ArrayList;
import java.util.List;


/**
 * @author <A href="mailto:langella@bmi.osu.edu">Stephen Langella </A>
 * @author <A href="mailto:oster@bmi.osu.edu">Scott Oster </A>
 * @author <A href="mailto:hastings@bmi.osu.edu">Shannon Hastings </A>
 * @version $Id: ArgumentManagerTable.java,v 1.2 2004/10/15 16:35:16 langella
 *          Exp $
 */
public class SyncDescriptor {
	private String gtsServiceURI;
	private List filters;


	public SyncDescriptor(String gtsServiceURI) {
		this.gtsServiceURI = gtsServiceURI;
		this.filters = new ArrayList();
	}


	public void addFilter(TrustedAuthorityFilter filter) {
		filters.add(filter);
	}


	public int getFilterCount() {
		return filters.size();
	}


	public TrustedAuthorityFilter getFilter(int index) {
		return (TrustedAuthorityFilter) filters.get(index);
	}


	public String getGTSServiceURI() {
		return gtsServiceURI;
	}

}
