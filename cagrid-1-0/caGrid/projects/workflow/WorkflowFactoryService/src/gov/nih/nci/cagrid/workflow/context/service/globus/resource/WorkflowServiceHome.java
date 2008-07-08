package gov.nih.nci.cagrid.workflow.context.service.globus.resource;

import gov.nih.nci.cagrid.workflow.stubs.types.WMSInputType;

import java.util.Calendar;

import javax.xml.namespace.QName;

import org.globus.wsrf.impl.ResourceHomeImpl;
import org.globus.wsrf.impl.SimpleResourceKey;

/**
 * This class implements a resource home
 */

public class WorkflowServiceHome extends ResourceHomeImpl {
	public static QName workflowQName =
		new QName("http://workflow.cagrid.nci.nih.gov/WorkflowServiceImpl", "WorkflowKey");
	public Class getKeyTypeClass() {
		// TODO Auto-generated method stub
		return String.class;
	}

	public QName getKeyTypeName() {
		// TODO Auto-generated method stub
		return this.workflowQName;
	}

	public SimpleResourceKey create(Calendar terminationTime, 
			WMSInputType input) throws Exception {
		SimpleResourceKey key = new SimpleResourceKey(this.workflowQName, input.getWorkflowName());
		WorkflowResource workflowResource = new WorkflowResource(input, terminationTime);
		this.add(key, workflowResource);
		return key;
	}


}