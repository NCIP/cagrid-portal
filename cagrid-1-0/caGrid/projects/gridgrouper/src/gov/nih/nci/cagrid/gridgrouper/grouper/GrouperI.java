package gov.nih.nci.cagrid.gridgrouper.grouper;

import edu.internet2.middleware.grouper.GroupNotFoundException;
import edu.internet2.middleware.grouper.StemNotFoundException;
import edu.internet2.middleware.subject.Subject;

import java.util.Set;

/**
 * @author <A HREF="MAILTO:langella@bmi.osu.edu">Stephen Langella</A>
 * @author <A HREF="MAILTO:oster@bmi.osu.edu">Scott Oster</A>
 * @author <A HREF="MAILTO:hastings@bmi.osu.edu">Shannon Hastings</A>
 * @author <A HREF="MAILTO:ervin@bmi.osu.edu">David W. Ervin</A>
 * 
 * @version $Id: GridGrouperBaseTreeNode.java,v 1.1 2006/08/04 03:49:26 langella
 *          Exp $
 */
public interface GrouperI {
	public StemI getRootStem() throws StemNotFoundException;

	public StemI findStem(String name) throws StemNotFoundException;

	public Set getChildStems(String parentStemName);

	public StemI getParentStem(String childStemName)
			throws StemNotFoundException;

	public String getName();

	public GroupI findGroup(String name) throws GroupNotFoundException;
	
	public boolean isMemberOf(String subjectId, String groupName);
	
	public boolean isMemberOf(Subject subject, String groupName);

}
