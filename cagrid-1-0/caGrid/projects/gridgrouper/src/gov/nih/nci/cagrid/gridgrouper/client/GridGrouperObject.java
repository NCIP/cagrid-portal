package gov.nih.nci.cagrid.gridgrouper.client;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 * @author <A href="mailto:langella@bmi.osu.edu">Stephen Langella </A>
 * @author <A href="mailto:oster@bmi.osu.edu">Scott Oster </A>
 * @author <A href="mailto:hastings@bmi.osu.edu">Shannon Hastings </A>
 * @version $Id: ArgumentManagerTable.java,v 1.2 2004/10/15 16:35:16 langella
 *          Exp $
 */
public abstract class GridGrouperObject {
	
	private Log log;

	public GridGrouperObject(){
		this.log = LogFactory.getLog(this.getClass().getName());
	}

	protected Log getLog() {
		return log;
	}

}
