package gov.nih.nci.cagrid.gridgrouper.subject;

import java.util.Iterator;
import java.util.Set;

import edu.internet2.middleware.grouper.Group;
import edu.internet2.middleware.grouper.GrouperSession;
import edu.internet2.middleware.grouper.NamingPrivilege;
import edu.internet2.middleware.grouper.RegistryReset;
import edu.internet2.middleware.grouper.Stem;
import edu.internet2.middleware.grouper.StemFinder;
import edu.internet2.middleware.grouper.SubjectFinder;
import edu.internet2.middleware.subject.Subject;


/**
 * @author <A href="mailto:langella@bmi.osu.edu">Stephen Langella </A>
 * @author <A href="mailto:oster@bmi.osu.edu">Scott Oster </A>
 * @author <A href="mailto:hastings@bmi.osu.edu">Shannon Hastings </A>
 * @version $Id: ArgumentManagerTable.java,v 1.2 2004/10/15 16:35:16 langella
 *          Exp $
 */
public class GrouperPlay {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		
		try {
			RegistryReset.reset();
			GrouperSession ses1 = GrouperSession.start(SubjectFinder.findById("GrouperSystem"));
			Stem root = StemFinder.findRootStem(ses1);
			GridUserSubjectSource source = new GridUserSubjectSource("localhost");
			Subject gs2 = source.getSubject("/O=OSU/OU=BMI/OU=caGrid/OU=Dorian/OU=localhost/OU=IdP [1]/CN=langella");
			Subject gs3 = source.getSubject("/O=OSU/OU=BMI/OU=caGrid/OU=Dorian/OU=localhost/OU=IdP [1]/CN=hastings");
			root.grantPriv(gs2, NamingPrivilege.STEM);
			root.grantPriv(gs2, NamingPrivilege.CREATE);
			
			GrouperSession ses2 = GrouperSession.start(gs2);
			Stem root2 = StemFinder.findRootStem(ses2);
			GrouperSession ses3 = GrouperSession.start(gs3);
			Stem root3 = StemFinder.findRootStem(ses2);
			
			
			Stem osu = root2.addChildStem("OSU", "Ohio State University");
			Stem ub = root2.addChildStem("UB", "University at Buffalo");
			Stem osuCS = osu.addChildStem("CS", "Computer Science");
			osuCS.grantPriv(gs3, NamingPrivilege.CREATE);
			Group osuCSFaculty = osuCS.addChildGroup("faculty", "Faculty");
			Stem ubCS = ub.addChildStem("CS", "Computer Science");
			printStems(root3, "");
			Stem found = StemFinder.findByName(ses1, osuCS.getName());
			System.out.println(found.getDisplayName());
			// root.addChildStem(, displayExtension)
		} catch (Exception e) {
			e.printStackTrace();
		}
	}


	public static void printStems(Stem stem, String buffer) {
		System.out.println(buffer + stem.getDisplayExtension() + " (" + stem.getUuid() + ")");
		Set s = stem.getChildStems();
		Iterator itr = s.iterator();
		while (itr.hasNext()) {
			printStems((Stem) itr.next(), buffer + "    ");
		}
	}

}
