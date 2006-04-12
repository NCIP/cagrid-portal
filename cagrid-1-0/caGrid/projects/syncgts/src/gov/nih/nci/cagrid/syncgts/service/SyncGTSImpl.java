package gov.nih.nci.cagrid.syncgts.service;

import java.math.BigInteger;

import gov.nih.nci.cagrid.gts.bean.Status;
import gov.nih.nci.cagrid.gts.bean.TrustedAuthorityFilter;
import gov.nih.nci.cagrid.syncgts.bean.SyncDescription;
import gov.nih.nci.cagrid.syncgts.bean.SyncDescriptor;
import gov.nih.nci.cagrid.syncgts.common.SyncGTSI;
import gov.nih.nci.cagrid.syncgts.core.SyncGTS;

/** 
 *  gov.nih.nci.cagrid.syncgtsI
 *  TODO:DOCUMENT ME
 * 
 * @created by Introduce Toolkit version 1.0
 * 
 */
public class SyncGTSImpl implements SyncGTSI{
	
	public SyncGTSImpl(){
		try{
		SyncDescription description = new SyncDescription();
		SyncDescriptor[] des = new SyncDescriptor[1];
		des[0] = new SyncDescriptor();
		des[0].setGtsServiceURI("https://irondale.bmi.ohio-state.edu:8443/wsrf/services/cagrid/GridTrustService");
		TrustedAuthorityFilter[] taf = new TrustedAuthorityFilter[1];
		taf[0] = new TrustedAuthorityFilter();
		taf[0].setStatus(Status.Trusted);
		des[0].setTrustedAuthorityFilters(taf);
		description.setSyncDescriptors(des);
		description.setFilePrefix("gts");
		description.setDeleteInvalidFiles(false);
		description.setNextSync(new BigInteger("300"));
		description.setDeleteExistingTrustedRoots(false);
		SyncGTS sync = SyncGTS.getInstance();
		sync.syncAndResyncInBackground(description);
		}catch(Exception e){
			e.printStackTrace();
		}
	}


}

