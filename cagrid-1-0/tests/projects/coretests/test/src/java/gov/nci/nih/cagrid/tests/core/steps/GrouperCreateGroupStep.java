/*
 * Created on Sep 21, 2006
 */
package gov.nci.nih.cagrid.tests.core.steps;

import gov.nih.nci.cagrid.gridgrouper.bean.StemIdentifier;
import gov.nih.nci.cagrid.gridgrouper.client.GridGrouperClient;
import gov.nih.nci.cagrid.gridgrouper.stubs.types.GridGrouperRuntimeFault;
import gov.nih.nci.cagrid.gridgrouper.stubs.types.StemNotFoundFault;
import gov.nih.nci.cagrid.gridgrouper.testutils.Utils;

import java.rmi.RemoteException;

import org.apache.axis.types.URI.MalformedURIException;

import com.atomicobject.haste.framework.Step;


public class GrouperCreateGroupStep extends Step {
    private String endpoint;
    private String path;


    public GrouperCreateGroupStep(String path, String endpoint) {
        super();

        this.endpoint = endpoint;
        this.path = path;
    }


    @Override
    public void runStep() throws GridGrouperRuntimeFault, StemNotFoundFault, RemoteException, MalformedURIException {
        GridGrouperClient grouper = new GridGrouperClient(this.endpoint);

        // get paths
        int index = this.path.lastIndexOf(':');
        StemIdentifier stem = Utils.getRootStemIdentifier();
        if (index != -1) {
            stem = new StemIdentifier(null, this.path.substring(0, index));
        }
        String group = this.path.substring(this.path.lastIndexOf(':') + 1);

        // create group
        grouper.addChildGroup(stem, group, group);
    }

}
