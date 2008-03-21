package org.cagrid.tide.tools.client.retriever;

import java.io.File;

import org.apache.axis.message.addressing.Address;
import org.apache.axis.message.addressing.EndpointReferenceType;
import org.apache.axis.types.URI.MalformedURIException;
import org.cagrid.tide.descriptor.TideDescriptor;
import org.cagrid.tide.replica.stubs.types.TideReplicaManagerReference;
import org.cagrid.tide.stubs.types.TideReference;
import org.cagrid.tide.tools.client.publishers.PublishTideClient;


public class RetrieveTideClient {

    public static void retrieveTide(String tideID, TideReplicaManagerReference replicaServer, TideRetriever retreiver,
        File tideStorageFile)  throws Exception {
        retreiver.retrieve(tideID, replicaServer, retreiver, tideStorageFile);
    }


    public static void retieveTide(String tideID, TideReplicaManagerReference replicaServer, File tideStorageFile) throws Exception {
        RoundRobinRetiever retreiver = new RoundRobinRetiever();
        retrieveTide(tideID, replicaServer, retreiver, tideStorageFile);
    }
    
    
    
    public static void main(String [] args){
        File f = new File("c:/apache-ant-1.7.0-bin.zip");
        File newFile = new File("c:/newapache-ant-1.7.0-bin.zip");
        try {
            TideReference tideRef = new TideReference(new EndpointReferenceType(new Address("http://localhost:8080/wsrf/services/cagrid/Tide")));
            TideReplicaManagerReference tideRepRef = new TideReplicaManagerReference(new EndpointReferenceType(new Address("http://localhost:8080/wsrf/services/cagrid/TideReplicaManager")));
//            TideDescriptor tideD = PublishTideClient.publishNewTide(f, tideRef, tideRepRef);
//            
            RetrieveTideClient.retieveTide("391b8f0e-cbfe-4fb6-992f-c9e218694539", tideRepRef, newFile);
            RetrieverWorkerPool.getInstance().shutdown();
        } catch (MalformedURIException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
       
    }

}
