/*
 * Created on Jul 14, 2006
 */
package gov.nci.nih.cagrid.validator.steps;

import org.apache.axis.message.addressing.Address;
import org.apache.axis.message.addressing.EndpointReferenceType;

import com.atomicobject.haste.framework.Step;
import gov.nih.nci.cagrid.metadata.*;

/**
 * This step authenticates a user with dorian and saves the proxy as the globus
 * default proxy.
 * 
 * @author Rakesh Dhaval
 */
public class TestServiceMetaData extends Step {

    private String serviceURL;    

    public TestServiceMetaData(String serviceURL) {    	
       this.serviceURL = serviceURL;
       System.out.println("Checking Service @ " + serviceURL);
       //connect to the url and validate a return
       EndpointReferenceType serviceMetaDataEPR = new EndpointReferenceType();       
       
       try{
    	   Address address = new Address(serviceURL);
    	   serviceMetaDataEPR.setAddress(address);   
   	   
    	   ServiceMetadata serviceMetaData = MetadataUtils.getServiceMetadata(serviceMetaDataEPR);	   
    	   System.out.println("   Service Name: "+serviceMetaData.getServiceDescription().getService().getName().toString());
    	   System.out.println("   POC: "+serviceMetaData.getServiceDescription().getService().getPointOfContactCollection().getPointOfContact(0).getFirstName() + " " +serviceMetaData.getServiceDescription().getService().getPointOfContactCollection().getPointOfContact(0).getLastName());
    	   System.out.println("   Hosting Research Center: "+serviceMetaData.getHostingResearchCenter().getResearchCenter().getDisplayName());
    	   
       }
       catch(Exception e){
    	   //e.printStackTrace();
    	   System.out.println("Error getting resource property. Please check the URL "+ serviceURL+" you entered.");
       }
    	    	          
    }

    @Override
    public void runStep() throws Throwable {
      
    }

}