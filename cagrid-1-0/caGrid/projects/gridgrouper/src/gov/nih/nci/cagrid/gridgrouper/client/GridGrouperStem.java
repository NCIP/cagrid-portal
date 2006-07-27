package gov.nih.nci.cagrid.gridgrouper.client;

import edu.internet2.middleware.grouper.GrouperRuntimeException;
import edu.internet2.middleware.subject.Source;
import edu.internet2.middleware.subject.Subject;
import edu.internet2.middleware.subject.SubjectNotFoundException;
import edu.internet2.middleware.subject.provider.SourceManager;
import gov.nih.nci.cagrid.gridgrouper.beans.StemDescriptor;
import gov.nih.nci.cagrid.gridgrouper.grouper.Stem;

import java.util.Collection;
import java.util.Date;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

import org.apache.commons.lang.builder.ToStringBuilder;


/**
 * @author <A href="mailto:langella@bmi.osu.edu">Stephen Langella </A>
 * @author <A href="mailto:oster@bmi.osu.edu">Scott Oster </A>
 * @author <A href="mailto:hastings@bmi.osu.edu">Shannon Hastings </A>
 * @version $Id: ArgumentManagerTable.java,v 1.2 2004/10/15 16:35:16 langella
 *          Exp $
 */
public class GridGrouperStem extends GridGrouperObject implements Stem {

	private StemDescriptor des;


	protected GridGrouperStem(GridGrouperClient client, StemDescriptor des) {
		this.setClient(client);
		this.des = des;
	}


	public String getCreateSource() {
		return des.getCreateSource();
	}


	public Subject getCreateSubject() throws SubjectNotFoundException {
		return buildSubject(des.getCreateSubject());
	}


	private Subject buildSubject(String id) throws SubjectNotFoundException {
		try {
			Collection sources = SourceManager.getInstance().getSources();
			Iterator itr = sources.iterator();
			while (itr.hasNext()) {
				Source src = (Source) itr.next();
				try {
					Subject sub = src.getSubject(id);
					return sub;
				} catch (SubjectNotFoundException ex) {

				}
			}
		} catch (Exception e) {
			getLog().error(e.getMessage(), e);
		}
		throw new SubjectNotFoundException("The subject " + id + " could not be found.");
	}


	public Date getCreateTime() {
		return new Date(des.getCreateTime());
	}


	public String getDescription() {
		return des.getDescription();
	}


	public String getDisplayExtension() {
		return des.getDisplayExtension();
	}


	public String getDisplayName() {
		return des.getDisplayName();
	}


	public String getExtension() {
		return des.getExtension();
	}


	public String getModifySource() {
		return des.getModifySource();
	}


	public Subject getModifySubject() throws SubjectNotFoundException {
		return buildSubject(des.getModifySubject());
	}


	public Date getModifyTime() {
		if (des.getModifyTime() == 0) {
			return getCreateTime();
		} else {
			return new Date(des.getModifyTime());
		}
	}


	public String getName() {
		return des.getName();
	}


	public String getUuid() {
		return des.getUUID();
	}


	public Set getChildStems() {
		try {
			StemDescriptor[] children = getClient().getChildStems(this.getName());
			Set set = new HashSet();
			if (children != null) {
				for (int i = 0; i < children.length; i++) {
					set.add(new GridGrouperStem(getClient(), children[i]));
				}
			}
			return set;
		} catch (Exception e) {
			getLog().error(e.getMessage(), e);
			throw new GrouperRuntimeException(e.getMessage());
		}
	}


	public String toString() {
		return new ToStringBuilder(this).append("displayName", getDisplayName()).append("name", getName()).append(
			"uuid", getUuid()).append("created", getCreateTime()).append("modified", getModifyTime()).toString();
	}

}
