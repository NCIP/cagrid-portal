package gov.nih.nci.cagrid.gridgrouper.service;

import java.rmi.RemoteException;
import java.util.Iterator;
import java.util.Set;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import edu.internet2.middleware.grouper.Group;
import edu.internet2.middleware.grouper.GroupFinder;
import edu.internet2.middleware.grouper.GroupNotFoundException;
import edu.internet2.middleware.grouper.GrouperSession;
import edu.internet2.middleware.grouper.Stem;
import edu.internet2.middleware.grouper.StemFinder;
import edu.internet2.middleware.grouper.StemNotFoundException;
import edu.internet2.middleware.grouper.SubjectFinder;
import edu.internet2.middleware.subject.Subject;
import gov.nih.nci.cagrid.common.FaultHelper;
import gov.nih.nci.cagrid.gridgrouper.beans.StemDescriptor;
import gov.nih.nci.cagrid.gridgrouper.stubs.GridGrouperRuntimeFault;
import gov.nih.nci.cagrid.gridgrouper.stubs.StemNotFoundFault;
import gov.nih.nci.cagrid.gridgrouper.subject.GridSourceAdapter;

/**
 * @author <A href="mailto:langella@bmi.osu.edu">Stephen Langella </A>
 * @author <A href="mailto:oster@bmi.osu.edu">Scott Oster </A>
 * @author <A href="mailto:hastings@bmi.osu.edu">Shannon Hastings </A>
 * @version $Id: ArgumentManagerTable.java,v 1.2 2004/10/15 16:35:16 langella
 *          Exp $
 */
public class GridGrouper {

	public static final String GROUPER_SUPER_USER = "GrouperSystem";

	public static final String GROUPER_ADMIN_STEM_NAME = "grouperadministration";

	public static final String GROUPER_ADMIN_STEM_DISPLAY_NAME = "Grouper Administration";

	public static final String GROUPER_ADMIN_GROUP_NAME_EXTENTION = "gridgrouperadministrators";

	public static final String GROUPER_ADMIN_GROUP_DISPLAY_NAME_EXTENTION = "Grid Grouper Administrators";

	public static final String GROUPER_ADMIN_GROUP_NAME = "grouperadministration:gridgrouperadministrators";

	public static final String UNKNOWN_SUBJECT = "Unknown";

	private Group adminGroup;

	private GridSourceAdapter source;

	private Log log;

	public GridGrouper() throws GridGrouperRuntimeFault {
		try {
			log = LogFactory.getLog(this.getClass().getName());
			this.source = new GridSourceAdapter("grid",
					"Grid Grouper: Grid Source Adapter");
			GrouperSession session = GrouperSession.start(SubjectFinder
					.findById(GROUPER_SUPER_USER));
			Stem adminStem = null;
			try {
				adminStem = StemFinder.findByName(session,
						GROUPER_ADMIN_STEM_NAME);
			} catch (StemNotFoundException e) {
				Stem root = StemFinder.findRootStem(session);
				adminStem = root.addChildStem(GROUPER_ADMIN_STEM_NAME,
						GROUPER_ADMIN_STEM_DISPLAY_NAME);
			}
			try {
				adminGroup = GroupFinder.findByName(session,
						GROUPER_ADMIN_GROUP_NAME);
			} catch (GroupNotFoundException gne) {
				adminGroup = adminStem.addChildGroup(
						GROUPER_ADMIN_GROUP_NAME_EXTENTION,
						GROUPER_ADMIN_GROUP_DISPLAY_NAME_EXTENTION);
			}
		} catch (Exception e) {
			log.error(e.getMessage(), e);
			GridGrouperRuntimeFault fault = new GridGrouperRuntimeFault();
			fault.setFaultString("Error occurred initializing Grid Grouper: "
					+ e.getMessage());
			FaultHelper helper = new FaultHelper(fault);
			helper.addFaultCause(e);
			fault = (GridGrouperRuntimeFault) helper.getFault();
			throw fault;
		}
	}

	public StemDescriptor getStem(String gridIdentity, String stemName)
			throws GridGrouperRuntimeFault, StemNotFoundFault {
		GrouperSession session = null;
		try {
			Subject subject = source.getSubject(gridIdentity);
			session = GrouperSession.start(subject);
			StemDescriptor des = null;
			Stem stem = StemFinder.findByName(session, stemName);
			des = stemtoStemDescriptor(stem);

			return des;
		} catch (StemNotFoundException e) {
			StemNotFoundFault fault = new StemNotFoundFault();
			fault.setFaultString("The stem, " + stemName + "was not found.");
			FaultHelper helper = new FaultHelper(fault);
			helper.addFaultCause(e);
			fault = (StemNotFoundFault) helper.getFault();
			throw fault;
		} catch (Exception e) {
			log.error(e.getMessage(), e);
			GridGrouperRuntimeFault fault = new GridGrouperRuntimeFault();
			fault.setFaultString("Error occurred getting the stem " + stemName
					+ ": " + e.getMessage());
			FaultHelper helper = new FaultHelper(fault);
			helper.addFaultCause(e);
			fault = (GridGrouperRuntimeFault) helper.getFault();
			throw fault;
		} finally {
			if (session == null) {
				try {
					session.stop();
				} catch (Exception e) {
					log.error(e.getMessage(), e);
				}
			}
		}
	}

	public StemDescriptor[] getChildStems(String gridIdentity,
			String parentStemName) throws RemoteException,
			GridGrouperRuntimeFault, StemNotFoundFault {
		GrouperSession session = null;
		try {
			Subject subject = source.getSubject(gridIdentity);
			session = GrouperSession.start(subject);
			StemDescriptor[] children = null;
			Stem parent = StemFinder.findByName(session, parentStemName);
			Set set = parent.getChildStems();
			children = new StemDescriptor[set.size()];
			Iterator itr = set.iterator();
			int count = 0;
			while (itr.hasNext()) {
				children[count] = stemtoStemDescriptor((Stem) itr.next());
				count++;
			}

			return children;
		} catch (StemNotFoundException e) {
			StemNotFoundFault fault = new StemNotFoundFault();
			fault.setFaultString("The parent stem, " + parentStemName
					+ "was not found.");
			FaultHelper helper = new FaultHelper(fault);
			helper.addFaultCause(e);
			fault = (StemNotFoundFault) helper.getFault();
			throw fault;
		} catch (Exception e) {
			log.error(e.getMessage(), e);
			GridGrouperRuntimeFault fault = new GridGrouperRuntimeFault();
			fault
					.setFaultString("Error occurred getting the child stems for the parent stem, "
							+ parentStemName + ": " + e.getMessage());
			FaultHelper helper = new FaultHelper(fault);
			helper.addFaultCause(e);
			fault = (GridGrouperRuntimeFault) helper.getFault();
			throw fault;
		} finally {
			if (session == null) {
				try {
					session.stop();
				} catch (Exception e) {
					log.error(e.getMessage(), e);
				}
			}
		}
	}

	public StemDescriptor getParentStem(String gridIdentity,
			String childStemName) throws RemoteException,
			GridGrouperRuntimeFault, StemNotFoundFault {
		GrouperSession session = null;
		try {
			Subject subject = source.getSubject(gridIdentity);
			session = GrouperSession.start(subject);
			StemDescriptor parent = null;
			Stem child = StemFinder.findByName(session, childStemName);
			Stem s = child.getParentStem();
			parent = stemtoStemDescriptor(s);

			return parent;
		} catch (StemNotFoundException e) {
			StemNotFoundFault fault = new StemNotFoundFault();
			fault.setFaultString("The parent stem for the child "
					+ childStemName + " could not be found!!!");
			FaultHelper helper = new FaultHelper(fault);
			helper.addFaultCause(e);
			fault = (StemNotFoundFault) helper.getFault();
			throw fault;
		} catch (Exception e) {
			log.error(e.getMessage(), e);
			GridGrouperRuntimeFault fault = new GridGrouperRuntimeFault();
			fault
					.setFaultString("Error occurred getting the parent stem for the child stem, "
							+ childStemName + ": " + e.getMessage());
			FaultHelper helper = new FaultHelper(fault);
			helper.addFaultCause(e);
			fault = (GridGrouperRuntimeFault) helper.getFault();
			throw fault;
		} finally {
			if (session == null) {
				try {
					session.stop();
				} catch (Exception e) {
					log.error(e.getMessage(), e);
				}
			}
		}
	}

	private StemDescriptor stemtoStemDescriptor(Stem stem) throws Exception {
		StemDescriptor des = new StemDescriptor();
		des.setCreateSource(stem.getCreateSource());
		des.setCreateSubject(stem.getCreateSubject().getId());
		des.setCreateTime(stem.getCreateTime().getTime());
		des.setDescription(stem.getDescription());
		des.setDisplayExtension(stem.getDisplayExtension());
		des.setDisplayName(stem.getDisplayName());
		des.setExtension(stem.getExtension());
		des.setModifySource(stem.getModifySource());
		try {
			des.setModifySubject(stem.getModifySubject().getId());
		} catch (Exception ex) {
			if (ex.getMessage().indexOf("has not been modified") != -1) {
				des.setModifySubject("");
			} else {
				throw ex;
			}
		}
		des.setModifyTime(stem.getModifyTime().getTime());
		des.setName(stem.getName());
		des.setUUID(stem.getUuid());
		return des;
	}

	public Group getAdminGroup() {
		return adminGroup;
	}

}
