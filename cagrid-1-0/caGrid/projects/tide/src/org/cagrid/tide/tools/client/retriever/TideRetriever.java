package org.cagrid.tide.tools.client.retriever;

import java.io.File;

import org.cagrid.tide.replica.stubs.types.TideReplicaManagerReference;

public interface TideRetriever {
    
    public void retrieve(String tideID, TideReplicaManagerReference replicaServer, TideRetriever retreiver,
        File tideStorageFile) throws Exception;

}
