package gov.nih.nci.cagrid.workflow.context.service.globus.resource;

import gov.nih.nci.cagrid.workflow.stubs.types.WMSInputType;

import java.util.Calendar;

import javax.xml.namespace.QName;

import org.globus.wsrf.InvalidResourceKeyException;
import org.globus.wsrf.NoSuchResourceException;
import org.globus.wsrf.RemoveNotSupportedException;
import org.globus.wsrf.Resource;
import org.globus.wsrf.ResourceException;
import org.globus.wsrf.ResourceKey;
import org.globus.wsrf.impl.ResourceHomeImpl;
import org.globus.wsrf.impl.SimpleResourceKey;

/**
 * This class implements a resource home
 */

public class WorkflowServiceHome extends ResourceHomeImpl {

	public Class getKeyTypeClass() {
		// TODO Auto-generated method stub
		return null;
	}


	public QName getKeyTypeName() {
		// TODO Auto-generated method stub
		return null;
	}


	public Resource find(ResourceKey arg0) throws ResourceException, NoSuchResourceException,
		InvalidResourceKeyException {
		// TODO Auto-generated method stub
		return null;
	}


	public void remove(ResourceKey arg0) throws ResourceException, NoSuchResourceException,
		InvalidResourceKeyException, RemoveNotSupportedException {
		// TODO Auto-generated method stub

	}
	public SimpleResourceKey create(Calendar terminationTime, 
			WMSInputType input) throws Exception {
		QName workflowQName =
			new QName("http://workflow.cagrid.nci.nih.gov/WorkflowServiceImpl", "WorkflowKey");
		SimpleResourceKey key = new SimpleResourceKey(workflowQName, input.getWorkflowName());
		WorkflowResource workflowResource = new WorkflowResource(input, terminationTime);
		this.add(key, workflowResource);
		return key;
	}


}