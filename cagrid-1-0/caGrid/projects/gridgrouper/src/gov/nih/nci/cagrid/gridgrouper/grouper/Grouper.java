package gov.nih.nci.cagrid.gridgrouper.grouper;

import edu.internet2.middleware.grouper.Privilege;
import edu.internet2.middleware.grouper.StemNotFoundException;
import edu.internet2.middleware.subject.Subject;

import java.util.Set;

/**
 * @author <A href="mailto:langella@bmi.osu.edu">Stephen Langella </A>
 * @author <A href="mailto:oster@bmi.osu.edu">Scott Oster </A>
 * @author <A href="mailto:hastings@bmi.osu.edu">Shannon Hastings </A>
 * @version $Id: ArgumentManagerTable.java,v 1.2 2004/10/15 16:35:16 langella
 *          Exp $
 */
public interface Grouper {
	public Stem getRootStem() throws StemNotFoundException;

	public Stem findStem(String name) throws StemNotFoundException;

	public Set getChildStems(String parentStemName);

	public Stem getParentStem(String childStemName)
			throws StemNotFoundException;

	public Set getStemPrivileges(String stemName, Subject subject)
			throws StemNotFoundException;

	public Set getSubjectsWithStemPrivilege(String stemName, Privilege privilege)
			throws StemNotFoundException;

	public boolean hasStemPrivilege(String stemName, Subject subject,
			Privilege privilege) throws StemNotFoundException;;

}
