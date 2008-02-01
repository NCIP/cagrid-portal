package org.cagrid.metrics.service;

import java.net.InetAddress;
import java.rmi.RemoteException;

import org.apache.axis.MessageContext;
import org.globus.wsrf.security.SecurityManager;

/**
 * TODO:I am the service side implementation class. IMPLEMENT AND DOCUMENT ME
 * 
 * @created by Introduce Toolkit version 1.1
 * 
 */
public class MetricsImpl extends MetricsImplBase {

	public MetricsImpl() throws RemoteException {
		super();
	}

	private String getCallerIdentity() {
		return SecurityManager.getManager().getCaller();
	}

  public void reportEvent() throws RemoteException {
		MessageContext context = MessageContext.getCurrentContext();
		String callerIP = (String)context.getProperty("remoteaddr");
		String hostname = "Unkown";
		try{
		InetAddress addr = InetAddress.getByName(callerIP);
		hostname = addr.getHostName();
		}catch (Exception e) {
			// TODO: handle exception
		}
		
		String chostname = "Unkown";
		try{
		InetAddress addr = InetAddress.getByName(callerIP);
		chostname = addr.getCanonicalHostName();
		}catch (Exception e) {
			// TODO: handle exception
		}
		System.out.println("IP Address: "+callerIP);
		System.out.println("Hostname: "+hostname);
		System.out.println("Canonical Hostname: "+chostname);
	}

}
