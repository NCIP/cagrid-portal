package org.test.client;

import java.io.InputStream;
import java.rmi.RemoteException;

import javax.xml.namespace.QName;

import org.apache.axis.EngineConfiguration;
import org.apache.axis.client.AxisClient;
import org.apache.axis.client.Stub;
import org.apache.axis.configuration.FileProvider;
import org.apache.axis.message.addressing.EndpointReferenceType;
import org.apache.axis.types.URI.MalformedURIException;

import org.oasis.wsrf.properties.GetResourcePropertyResponse;

import org.globus.gsi.GlobusCredential;

import org.test.stubs.IntroduceTestServicePortType;
import org.test.stubs.service.IntroduceTestServiceAddressingLocator;
import org.test.common.IntroduceTestServiceI;

import com.sun.java_cup.internal.production;

import gov.nih.nci.cagrid.introduce.security.client.ServiceSecurityClient;

/**
 * This class is autogenerated, DO NOT EDIT GENERATED GRID SERVICE ACCESS METHODS.
 *
 * This client is generated automatically by Introduce to provide a clean unwrapped API to the
 * service.
 *
 * On construction the class instance will contact the remote service and retrieve it's security
 * metadata description which it will use to configure the Stub specifically for each method call.
 * 
 * @created by Introduce Toolkit version 1.2
 */
public class IntroduceTestServiceClient extends IntroduceTestServiceClientBase implements IntroduceTestServiceI {   

    public IntroduceTestServiceClient(String url) throws MalformedURIException, RemoteException {
        this(url,null); 
    }

    public IntroduceTestServiceClient(String url, GlobusCredential proxy) throws MalformedURIException, RemoteException {
        super(url,proxy);
    }
    
    public IntroduceTestServiceClient(EndpointReferenceType epr) throws MalformedURIException, RemoteException {
        this(epr,null);
    }
    
    public IntroduceTestServiceClient(EndpointReferenceType epr, GlobusCredential proxy) throws MalformedURIException, RemoteException {
        super(epr,proxy);
    }

    public static void usage(){
        System.out.println(IntroduceTestServiceClient.class.getName() + " -url <service url>");
    }
    
    public static void main(String [] args){
        System.out.println("Running the Grid Service Client");
        try{
        if(!(args.length < 2)){
            if(args[0].equals("-url")){
              org.test.persistentnotification.resource.client.IntroduceTestPersistentNotificationResourceServiceClient client = new IntroduceTestServiceClient(args[1]).createIntroduceTestPersistentNotificationResourceService();
              gov.nih.nci.cagrid.common.Utils.serializeDocument("persistenceNotificationTestEPR.xml", client.getEndpointReference(), new javax.xml.namespace.QName("http://schemas.xmlsoap.org/ws/2004/03/addressing","EndpointReference"));
              // place client calls here if you want to use this main as a
              // test....
              projectmobius.org.BookType book = new projectmobius.org.BookType();
              book.setAuthor("Shannon Hastings");
              client.subscribe(org.test.persistentnotification.resource.common.IntroduceTestPersistentNotificationResourceServiceConstants.BOOK);
              client.setBook(book);
              Thread.sleep(10000);
              if(org.test.persistentnotification.resource.client.IntroduceTestPersistentNotificationResourceServiceClient.recievedNotificationCount==1){
                  System.out.println("recieved notification");
              } else {
                  System.err.println("did not recieve notification");
                  System.exit(1);
              }
            } else {
                usage();
                System.exit(1);
            }
        } else {
            usage();
            System.exit(1);
        }
        } catch (Exception e) {
            e.printStackTrace();
            System.exit(1);
        }
    }

  public org.test.persistentnotification.resource.client.IntroduceTestPersistentNotificationResourceServiceClient createIntroduceTestPersistentNotificationResourceService() throws RemoteException, org.apache.axis.types.URI.MalformedURIException {
    synchronized(portTypeMutex){
      configureStubSecurity((Stub)portType,"createIntroduceTestPersistentNotificationResourceService");
    org.test.stubs.CreateIntroduceTestPersistentNotificationResourceServiceRequest params = new org.test.stubs.CreateIntroduceTestPersistentNotificationResourceServiceRequest();
    org.test.stubs.CreateIntroduceTestPersistentNotificationResourceServiceResponse boxedResult = portType.createIntroduceTestPersistentNotificationResourceService(params);
    EndpointReferenceType ref = boxedResult.getIntroduceTestPersistentNotificationResourceServiceReference().getEndpointReference();
    return new org.test.persistentnotification.resource.client.IntroduceTestPersistentNotificationResourceServiceClient(ref);
    }
  }

}