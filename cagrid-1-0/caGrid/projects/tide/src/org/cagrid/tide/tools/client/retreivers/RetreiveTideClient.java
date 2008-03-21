package org.cagrid.tide.tools.client.retreivers;

import java.io.File;

import org.apache.axis.message.addressing.Address;
import org.apache.axis.message.addressing.EndpointReferenceType;
import org.apache.axis.types.URI.MalformedURIException;
import org.cagrid.tide.descriptor.TideDescriptor;
import org.cagrid.tide.replica.stubs.types.TideReplicaManagerReference;
import org.cagrid.tide.stubs.types.TideReference;
import org.cagrid.tide.tools.client.publishers.PublishTideClient;


public class RetreiveTideClient {

    public static void retreiveTide(String tideID, TideReplicaManagerReference replicaServer, TideRetreiver retreiver,
        File tideStorageFile)  throws Exception {
        retreiver.retreive(tideID, replicaServer, retreiver, tideStorageFile);
    }


    public static void reteiveTide(String tideID, TideReplicaManagerReference replicaServer, File tideStorageFile) throws Exception {
        RoundRobinReteiver retreiver = new RoundRobinReteiver();
        retreiveTide(tideID, replicaServer, retreiver, tideStorageFile);
    }
    
    
    
    public static void main(String [] args){
        File f = new File("c:/apache-ant-1.7.0-bin.zip");
        File newFile = new File("c:/newapache-ant-1.7.0-bin.zip");
        try {
            TideReference tideRef = new TideReference(new EndpointReferenceType(new Address("http://localhost:8080/wsrf/services/cagrid/Tide")));
            TideReplicaManagerReference tideRepRef = new TideReplicaManagerReference(new EndpointReferenceType(new Address("http://localhost:8080/wsrf/services/cagrid/TideReplicaManager")));
//            TideDescriptor tideD = PublishTideClient.publishNewTide(f, tideRef, tideRepRef);
//            
            RetreiveTideClient.reteiveTide("391b8f0e-cbfe-4fb6-992f-c9e218694539", tideRepRef, newFile);
        } catch (MalformedURIException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
       
    }

}
