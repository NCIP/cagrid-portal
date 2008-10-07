package org.cagrid.metrics.common;

import java.rmi.RemoteException;

/** 
 * This class is autogenerated, DO NOT EDIT.
 * 
 * This interface represents the API which is accessable on the grid service from the client. 
 * 
 * @created by Introduce Toolkit version 1.1
 * 
 */
public interface MetricsI {

  /**
   * Enables the submission of event(s).
   *
   * @param submission
   */
  public void report(org.cagrid.metrics.common.EventSubmission submission) throws RemoteException ;

}
