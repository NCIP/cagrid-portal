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
import gov.nih.nci.cagrid.gridgrouper.common.SubjectUtils;


/**
 * @author <A href="mailto:langella@bmi.osu.edu">Stephen Langella </A>
 * @author <A href="mailto:oster@bmi.osu.edu">Scott Oster </A>
 * @author <A href="mailto:hastings@bmi.osu.edu">Shannon Hastings </A>
 * @version $Id: ArgumentManagerTable.java,v 1.2 2004/10/15 16:35:16 langella
 *          Exp $
 */
public class GrouperPlay {

	public static final String GROUPER_ADMIN_STEM_NAME = "grouperadministration";
	public static final String GROUPER_ADMIN_STEM_DISPLAY_NAME = "Grouper Administration";
	public static final String GROUPER_ADMIN_GROUP_NAME_EXTENTION = "gridgrouperadministrators";
	public static final String GROUPER_ADMIN_GROUP_DISPLAY_NAME_EXTENTION = "Grid Grouper Administrators";


	/**
	 * @param args
	 */
	public static void main(String[] args) {

		try {
			RegistryReset.reset();
			GrouperSession ses1 = GrouperSession.start(SubjectFinder.findById("GrouperSystem"));
			Stem root = StemFinder.findRootStem(ses1);
			Stem adminStem = root.addChildStem(GROUPER_ADMIN_STEM_NAME, GROUPER_ADMIN_STEM_DISPLAY_NAME);
			Group admin = adminStem.addChildGroup(GROUPER_ADMIN_GROUP_NAME_EXTENTION,
				GROUPER_ADMIN_GROUP_DISPLAY_NAME_EXTENTION);
			System.out.println(admin.getName());
			GridSourceAdapter source = new GridSourceAdapter("grid", "Grid Grouper: Grid Source Adapter");
			Subject gs2 = source.getSubject("/O=OSU/OU=BMI/OU=caGrid/OU=Dorian/OU=cagrid05/OU=IdP [1]/CN=langella");
			Subject gs3 = source.getSubject("/O=OSU/OU=BMI/OU=caGrid/OU=Dorian/OU=cagrid05/OU=IdP [1]/CN=hastings");
			admin.addMember(gs2);

			GrouperSession ses2 = GrouperSession.start(gs2);
			Stem root2 = StemFinder.findRootStem(ses2);
			GrouperSession ses3 = GrouperSession.start(gs3);
			Stem root3 = StemFinder.findRootStem(ses2);

			Stem osu = root2.addChildStem("OSU", "Ohio State University");
			osu.grantPriv(gs3, NamingPrivilege.CREATE);
			Stem ub = root2.addChildStem("UB", "University at Buffalo");
			Stem osuCS = osu.addChildStem("CS", "Computer Science");
			
			Group osuCSFaculty = osuCS.addChildGroup("faculty", "Faculty");
			Stem ubCS = ub.addChildStem("CS", "Computer Science");
			printStems(root3, "");
			Stem found = StemFinder.findByName(ses1, osuCS.getName());
			System.out.println(found.getDisplayName());
			System.out.println(osu.hasCreate(gs2));
			// root.addChildStem(, displayExtension)
		} catch (Exception e) {
			e.printStackTrace();
		}
	}


	public static void printStems(Stem stem, String buffer) throws Exception {
		System.out.println(buffer + stem.getDisplayExtension() + " ("
				+ stem.getUuid() + ")");
		System.out.println(buffer + "  " + "Description:"
				+ stem.getDescription());
		try {
			System.out.println(buffer + "  " + "Parent:"
					+ stem.getParentStem().getDisplayExtension());
		} catch (Exception e) {
		}
		System.out.println(buffer + "  " + "Create Source:"
				+ stem.getCreateSource());
		System.out.println(buffer + "  " + "Create Subject Id:"
				+ stem.getCreateSubject().getId());
		System.out.println(buffer + "  " + "Create Time:"
				+ stem.getCreateTime());
		System.out.println(buffer + "  " + "Modify Time:"
				+ stem.getModifyTime());
		
		Set stemmers = stem.getStemmers();
		System.out.println(buffer + "  " + "Stemmers:");
		Iterator i2 = stemmers.iterator();
		while(i2.hasNext()){
			Subject sbj = (Subject)i2.next();
			System.out.println(buffer + "    " 
					+ sbj.getId());
		}
		
		Set creators = stem.getCreators();
		System.out.println(buffer + "  " + "Creators:");
		Iterator i1 = creators.iterator();
		while(i1.hasNext()){
			Subject sbj = (Subject)i1.next();
			System.out.println(buffer + "    " 
					+ sbj.getId());
		}
		Subject sub = SubjectUtils.getSubject("/O=OSU/OU=BMI/OU=caGrid/OU=Dorian/OU=cagrid05/OU=IdP [1]/CN=langella");
		Set privs = stem.getPrivs(sub);
		System.out.println(buffer + "  " + "Privileges for "+sub.getId()+":");
		Iterator i3= privs.iterator();
		while(i3.hasNext()){
			NamingPrivilege priv = (NamingPrivilege)i3.next();
			System.out.println(buffer + "    " 
					+ priv.toString());
		}
		
	
		
		
		Set s = stem.getChildStems();
		Iterator itr = s.iterator();
		while (itr.hasNext()) {
			System.out.println();
			System.out.println();
			printStems((Stem) itr.next(), buffer);
		
		}
	}

}
