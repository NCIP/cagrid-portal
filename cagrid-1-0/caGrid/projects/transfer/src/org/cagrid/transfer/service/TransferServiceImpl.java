package org.cagrid.transfer.service;

import java.rmi.RemoteException;

/** 
 * TODO:I am the service side implementation class.  IMPLEMENT AND DOCUMENT ME
 * 
 * @created by Introduce Toolkit version 1.2
 * 
 */
public class TransferServiceImpl extends TransferServiceImplBase {

	
	public TransferServiceImpl() throws RemoteException {
		super();
		try {
            TransferServiceConfiguration.getConfiguration();
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
	}
	

}

