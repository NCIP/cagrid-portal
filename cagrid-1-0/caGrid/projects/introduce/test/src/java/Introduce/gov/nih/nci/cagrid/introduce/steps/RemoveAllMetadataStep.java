package gov.nih.nci.cagrid.introduce.steps;

import gov.nih.nci.cagrid.common.Utils;
import gov.nih.nci.cagrid.introduce.TestCaseInfo;
import gov.nih.nci.cagrid.introduce.beans.ServiceDescription;
import gov.nih.nci.cagrid.introduce.beans.resource.ResourcePropertiesListType;
import gov.nih.nci.cagrid.introduce.codegen.SyncTools;
import gov.nih.nci.cagrid.introduce.common.CommonTools;

import java.io.File;

import javax.xml.namespace.QName;

import com.atomicobject.haste.framework.Step;


public class RemoveAllMetadataStep extends BaseStep {
	private TestCaseInfo tci;


	public RemoveAllMetadataStep(TestCaseInfo tci, boolean build) throws Exception {
		super(tci.getDir(),build);
		this.tci = tci;
	}


	public void runStep() throws Throwable {
		System.out.println("Adding metadata.");

		ServiceDescription introService = (ServiceDescription) Utils.deserializeDocument(getBaseDir() + File.separator
			+ tci.getDir() + File.separator + "introduce.xml", ServiceDescription.class);
		ResourcePropertiesListType metadatasType = CommonTools.getService(introService.getServices(),tci.getName()).getResourcePropertiesList();
		metadatasType.setResourceProperty(null);

		Utils.serializeDocument(getBaseDir() + File.separator + tci.getDir() + File.separator + "introduce.xml",
			introService, new QName("gme://gov.nih.nci.cagrid/1/Introduce", "ServiceSkeleton"));

		try {
			SyncTools sync = new SyncTools(new File(getBaseDir() + File.separator + tci.getDir()));
			sync.sync();
		} catch (Exception e) {
			e.printStackTrace();
			fail(e.getMessage());
		}

		buildStep();
	}

}
