package gov.nih.nci.cagrid.gridgrouper.client;

import edu.internet2.middleware.grouper.GrouperRuntimeException;
import edu.internet2.middleware.grouper.StemNotFoundException;
import gov.nih.nci.cagrid.gridgrouper.beans.StemDescriptor;
import gov.nih.nci.cagrid.gridgrouper.grouper.Grouper;
import gov.nih.nci.cagrid.gridgrouper.grouper.Stem;
import gov.nih.nci.cagrid.gridgrouper.stubs.StemNotFoundFault;

import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

import org.globus.gsi.GlobusCredential;


/**
 * @author <A href="mailto:langella@bmi.osu.edu">Stephen Langella </A>
 * @author <A href="mailto:oster@bmi.osu.edu">Scott Oster </A>
 * @author <A href="mailto:hastings@bmi.osu.edu">Shannon Hastings </A>
 * @version $Id: ArgumentManagerTable.java,v 1.2 2004/10/15 16:35:16 langella
 *          Exp $
 */
public class GridGrouper extends GridGrouperObject implements Grouper {

	protected static final String ROOT_STEM = "";


	public GridGrouper(String serviceURI) {
		this(serviceURI, null);
	}


	public GridGrouper(String serviceURI, GlobusCredential cred) {
		try {
			this.setClient(new GridGrouperClient(serviceURI, cred));
		} catch (Exception e) {
			getLog().error(e.getMessage(), e);
			throw new GrouperRuntimeException(e.getMessage());
		}
	}


	public Stem getRootStem() throws StemNotFoundException {
		return findStem(ROOT_STEM);
	}


	public Stem findStem(String name) throws StemNotFoundException {
		try {
			StemDescriptor des = getClient().getStem(name);
			return new GridGrouperStem(this, des);
		} catch (StemNotFoundFault f) {
			throw new StemNotFoundException(f.getFaultString());
		} catch (Exception e) {
			getLog().error(e.getMessage(), e);
			throw new GrouperRuntimeException(e.getMessage());
		}
	}
	public Set getChildStems(String stemName) {
		try {
			StemDescriptor[] children = getClient().getChildStems(stemName);
			Set set = new HashSet();
			if (children != null) {
				for (int i = 0; i < children.length; i++) {
					set.add(new GridGrouperStem(this, children[i]));
				}
			}
			return set;
		} catch (Exception e) {
			getLog().error(e.getMessage(), e);
			throw new GrouperRuntimeException(e.getMessage());
		}
	}
	
	
	public static void main(String[] args) {
		System.out.println("Running the Grid Service Client");
		try {

			GridGrouper grouper = new GridGrouper("https://localhost:8443/wsrf/services/cagrid/GridGrouper");
			Stem stem = grouper.getRootStem();
			printStems(stem, "");
		} catch (Exception e) {
			e.printStackTrace();
			System.exit(1);
		}
	}
	public static void printStems(Stem stem, String buffer) throws Exception {
		System.out.println(buffer + stem.getDisplayExtension() + " (" + stem.getUuid() + ")");
		System.out.println(buffer + "  " + "[Description:" + stem.getDescription() + "]");
		System.out.println(buffer + "  " + "[Create Source:" + stem.getCreateSource() + "]");
		System.out.println(buffer+  "  " + "[Create Subject Id:"+stem.getCreateSubject().getId()+"]");
		System.out.println(buffer+  "  " + "[Create Subject Name:"+stem.getCreateSubject().getName()+"]");
		System.out.println(buffer+  "  " + "[Create Subject Source:"+stem.getCreateSubject().getSource().getClass().getName()+"]");
		System.out.println(buffer + "  " + "[Create Time:" + stem.getCreateTime() + "]");
		System.out.println(buffer + "  " + "[Modify Time:" + stem.getModifyTime() + "]");
		System.out.println(buffer + "  " + "[Modify Source:" + stem.getModifySource() + "]");
	    //System.out.println(buffer+"  "+" [Modify Subject:"+stem.getModifySubject().getName()+"]");
		Set s = stem.getChildStems();
		Iterator itr = s.iterator();
		while (itr.hasNext()) {
			printStems((Stem) itr.next(), buffer + "    ");
		}
	}


}
