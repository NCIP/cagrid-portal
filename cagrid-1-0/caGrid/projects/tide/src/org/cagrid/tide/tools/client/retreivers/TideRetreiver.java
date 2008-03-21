package org.cagrid.tide.tools.client.retreivers;

import java.io.File;

import org.cagrid.tide.replica.stubs.types.TideReplicaManagerReference;

public interface TideRetreiver {
    
    public void retreive(String tideID, TideReplicaManagerReference replicaServer, TideRetreiver retreiver,
        File tideStorageFile) throws Exception;

}
