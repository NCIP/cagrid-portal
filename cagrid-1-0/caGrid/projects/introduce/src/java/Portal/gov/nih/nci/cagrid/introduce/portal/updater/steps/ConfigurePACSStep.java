package gov.nih.nci.cagrid.introduce.portal.updater.steps;

import java.util.HashMap;
import java.util.Map;


public class ConfigurePACSStep extends BasePropretyConfigureStep {

	/**
	 * This method initializes 
	 * 
	 */
	public ConfigurePACSStep(Map globalMap) {
		super(globalMap);
		this.addOption("cqlQueryProcessorConfig_serverAE","RIDERPACS1", "DICOM PACS Name");
		this.addOption("cqlQueryProcessorConfig_serverip","localhost", "DICOM PACS Host");
		this.addOption("cqlQueryProcessorConfig_serverport","40081", "DICOM PACS Port");
		this.addBooleanOption("useCMOVE",false,"Use CMOVE Operation");
		this.setComplete(true);
	}
	
	public String getName(){
		return "PACS";
	}
	
	public String getSummary(){
		return "Configure PACS Options";
	}

}  //  @jve:decl-index=0:visual-constraint="10,10"
