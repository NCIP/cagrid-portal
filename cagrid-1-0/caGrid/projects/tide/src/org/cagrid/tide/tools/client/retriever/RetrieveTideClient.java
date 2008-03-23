package org.cagrid.tide.tools.client.retriever;

import java.io.File;

import org.apache.axis.message.addressing.Address;
import org.apache.axis.message.addressing.EndpointReferenceType;
import org.apache.axis.types.URI.MalformedURIException;
import org.cagrid.tide.descriptor.TideReplicasDescriptor;
import org.cagrid.tide.replica.client.TideReplicaManagerClient;
import org.cagrid.tide.replica.context.client.TideReplicaManagerContextClient;
import org.cagrid.tide.replica.stubs.types.TideReplicaManagerReference;
import org.cagrid.tide.stubs.types.TideReference;


public class RetrieveTideClient {

    public static void retrieveTide(String tideID, TideReplicaManagerReference replicaServer, TideRetriever retreiver,
        File tideStorageFile) throws Exception {

        retreiver.retrieve();

    }


    public static void retieveTide(String tideID, TideReplicaManagerReference replicaServer, File tideStorageFile)
        throws Exception {

        TideReplicaManagerClient client = new TideReplicaManagerClient(replicaServer.getEndpointReference());
        TideReplicaManagerContextClient tclient = client.getTideReplicaManagerContext(tideID);
        final TideReplicasDescriptor replicas = tclient.getReplicas();

        RoundRobinRetiever retreiver = new RoundRobinRetiever(tideID, tideStorageFile, replicaServer, replicas);
        retreiver.retrieve();
    }


    public static void main(String[] args) {
        File f = new File("c:/apache-ant-1.7.0-bin.zip");
        File newFile = new File("c:/newapache-ant-1.7.0-bin.zip");
        try {
            TideReference tideRef = new TideReference(new EndpointReferenceType(new Address(
                "http://localhost:8080/wsrf/services/cagrid/Tide")));
            TideReplicaManagerReference tideRepRef = new TideReplicaManagerReference(new EndpointReferenceType(
                new Address("http://localhost:8080/wsrf/services/cagrid/TideReplicaManager")));
            // TideDescriptor tideD = PublishTideClient.publishNewTide(f,
            // tideRef, tideRepRef);
            //            
            RetrieveTideClient.retieveTide("9b13021d-d70d-4d80-8b46-122e1f0be22f", tideRepRef, newFile);
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
