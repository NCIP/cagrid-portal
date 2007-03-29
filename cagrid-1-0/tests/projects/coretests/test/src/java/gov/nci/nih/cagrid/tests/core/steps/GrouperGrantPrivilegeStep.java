/*
 * Created on Sep 21, 2006
 */
package gov.nci.nih.cagrid.tests.core.steps;

import gov.nih.nci.cagrid.gridgrouper.bean.GroupIdentifier;
import gov.nih.nci.cagrid.gridgrouper.bean.GroupPrivilegeType;
import gov.nih.nci.cagrid.gridgrouper.bean.StemIdentifier;
import gov.nih.nci.cagrid.gridgrouper.bean.StemPrivilegeType;
import gov.nih.nci.cagrid.gridgrouper.client.GridGrouperClient;
import gov.nih.nci.cagrid.gridgrouper.stubs.types.GridGrouperRuntimeFault;
import gov.nih.nci.cagrid.gridgrouper.stubs.types.StemNotFoundFault;

import java.rmi.RemoteException;

import org.apache.axis.types.URI.MalformedURIException;

import com.atomicobject.haste.framework.Step;


public class GrouperGrantPrivilegeStep extends Step {
    private String endpoint;
    private String path;
    private String subject;
    private String priv;


    public GrouperGrantPrivilegeStep(String path, String subject, String priv, String endpoint) {
        super();

        this.endpoint = endpoint;
        this.path = path;
        this.subject = subject;
        this.priv = priv;
    }


    @Override
    public void runStep() throws GridGrouperRuntimeFault, StemNotFoundFault, RemoteException, MalformedURIException {
        GridGrouperClient grouper = new GridGrouperClient(this.endpoint);

        // group or stem?
        boolean isGroup = false;
        try {
            grouper.getGroup(new GroupIdentifier(null, this.path));
            isGroup = true;
        } catch (Exception e) {
            isGroup = false;
        }

        // grant
        if (isGroup) {
            grouper.grantGroupPrivilege(new GroupIdentifier(null, this.path), this.subject, GroupPrivilegeType
                .fromString(this.priv));
        } else {
            grouper.grantStemPrivilege(new StemIdentifier(null, this.path), this.subject, StemPrivilegeType
                .fromString(this.priv));
        }
    }
}
