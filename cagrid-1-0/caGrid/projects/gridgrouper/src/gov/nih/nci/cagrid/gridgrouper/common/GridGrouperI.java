package gov.nih.nci.cagrid.gridgrouper.common;

import java.rmi.RemoteException;

/**
 * This class is autogenerated, DO NOT EDIT.
 * 
 * @created by Introduce Toolkit version 1.0
 * 
 */
public interface GridGrouperI {

public gov.nih.nci.cagrid.metadata.security.ServiceSecurityMetadata getServiceSecurityMetadata() throws RemoteException ;
public gov.nih.nci.cagrid.gridgrouper.beans.StemDescriptor getStem(gov.nih.nci.cagrid.gridgrouper.beans.StemIdentifier stem) throws RemoteException, gov.nih.nci.cagrid.gridgrouper.stubs.GridGrouperRuntimeFault, gov.nih.nci.cagrid.gridgrouper.stubs.StemNotFoundFault ;
public gov.nih.nci.cagrid.gridgrouper.beans.StemDescriptor[] getChildStems(gov.nih.nci.cagrid.gridgrouper.beans.StemIdentifier parentStem) throws RemoteException, gov.nih.nci.cagrid.gridgrouper.stubs.GridGrouperRuntimeFault, gov.nih.nci.cagrid.gridgrouper.stubs.StemNotFoundFault ;
public gov.nih.nci.cagrid.gridgrouper.beans.StemDescriptor getParentStem(gov.nih.nci.cagrid.gridgrouper.beans.StemIdentifier childStem) throws RemoteException, gov.nih.nci.cagrid.gridgrouper.stubs.GridGrouperRuntimeFault, gov.nih.nci.cagrid.gridgrouper.stubs.StemNotFoundFault ;
public gov.nih.nci.cagrid.gridgrouper.beans.StemDescriptor updateStemDescription(gov.nih.nci.cagrid.gridgrouper.beans.StemIdentifier stem,java.lang.String description) throws RemoteException, gov.nih.nci.cagrid.gridgrouper.stubs.GridGrouperRuntimeFault, gov.nih.nci.cagrid.gridgrouper.stubs.InsufficientPrivilegeFault, gov.nih.nci.cagrid.gridgrouper.stubs.StemModifyFault ;
public gov.nih.nci.cagrid.gridgrouper.beans.StemDescriptor updateStemDisplayExtension(gov.nih.nci.cagrid.gridgrouper.beans.StemIdentifier stem,java.lang.String displayExtension) throws RemoteException, gov.nih.nci.cagrid.gridgrouper.stubs.GridGrouperRuntimeFault, gov.nih.nci.cagrid.gridgrouper.stubs.InsufficientPrivilegeFault, gov.nih.nci.cagrid.gridgrouper.stubs.StemModifyFault ;

}
