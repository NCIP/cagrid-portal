package gov.nih.nci.cagrid.gridgrouper.client;

import edu.internet2.middleware.grouper.GrouperRuntimeException;
import edu.internet2.middleware.grouper.StemNotFoundException;
import gov.nih.nci.cagrid.gridgrouper.beans.StemDescriptor;
import gov.nih.nci.cagrid.gridgrouper.beans.StemIdentifier;
import gov.nih.nci.cagrid.gridgrouper.grouper.Grouper;
import gov.nih.nci.cagrid.gridgrouper.grouper.Stem;
import gov.nih.nci.cagrid.gridgrouper.stubs.StemNotFoundFault;

import java.util.HashSet;
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

	private GridGrouperClient client;

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
			StemDescriptor des = getClient().getStem(getStemIdentifier(name));
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
			StemDescriptor[] children = getClient().getChildStems(
					getStemIdentifier(stemName));
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

	public Stem getParentStem(String childStemName)
			throws StemNotFoundException {
		try {
			StemDescriptor des = getClient().getParentStem(
					getStemIdentifier(childStemName));
			return new GridGrouperStem(this, des);
		} catch (StemNotFoundFault f) {
			throw new StemNotFoundException(f.getFaultString());
		} catch (Exception e) {
			getLog().error(e.getMessage(), e);
			throw new GrouperRuntimeException(e.getMessage());
		}
	}

	protected GridGrouperClient getClient() {
		return client;
	}

	protected void setClient(GridGrouperClient client) {
		this.client = client;
	}

	protected StemIdentifier getStemIdentifier(String stemName) {
		StemIdentifier id = new StemIdentifier();
		id.setGridGrouperURL(getClient().getEndpointReference().toString());
		id.setStemName(stemName);
		return id;
	}

}
