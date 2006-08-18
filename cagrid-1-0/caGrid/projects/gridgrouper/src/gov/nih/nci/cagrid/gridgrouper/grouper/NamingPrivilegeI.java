package gov.nih.nci.cagrid.gridgrouper.grouper;

import edu.internet2.middleware.subject.Subject;

/**
 * @author <A href="mailto:langella@bmi.osu.edu">Stephen Langella </A>
 * @author <A href="mailto:oster@bmi.osu.edu">Scott Oster </A>
 * @author <A href="mailto:hastings@bmi.osu.edu">Shannon Hastings </A>
 * @author <A href="mailto:ervin@bmi.osu.edu">David Ervin </A>
 * @version $Id: ArgumentManagerTable.java,v 1.2 2004/10/15 16:35:16 langella
 *          Exp $
 */
public interface NamingPrivilegeI {
	  public String getStemName();
	  public String getName();
	  public Subject getSubject();
	  public String getImplementationName();
	  public boolean isRevokable();
	  public Subject getOwner();    
}
