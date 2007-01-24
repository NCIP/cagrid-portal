package gov.nih.nci.cagrid.annualdemo.common;

import java.rmi.RemoteException;

/** 
 * This class is autogenerated, DO NOT EDIT.
 * 
 * This interface represents the API which is accessable on the grid service from the client. 
 * 
 * @created by Introduce Toolkit version 1.0
 * 
 */
public interface MageTranslationServicesI {

    public gov.nih.nci.cagrid.metadata.security.ServiceSecurityMetadata getServiceSecurityMetadata() throws RemoteException ;

    public edu.columbia.geworkbench.cagrid.microarray.MicroarraySet mageToMicroArray(gov.nih.nci.cagrid.cqlresultset.CQLQueryResults cQLQueryResultCollection) throws RemoteException ;

    public gridextensions.Data mageToStatML(gov.nih.nci.cagrid.cqlresultset.CQLQueryResults cQLQueryResultCollection) throws RemoteException ;

}

