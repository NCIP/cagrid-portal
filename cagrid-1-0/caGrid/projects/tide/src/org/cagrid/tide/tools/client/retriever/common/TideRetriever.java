package org.cagrid.tide.tools.client.retriever.common;

import java.io.File;

import org.cagrid.tide.descriptor.TideReplicasDescriptor;
import org.cagrid.tide.replica.stubs.types.TideReplicaManagerReference;

public abstract class TideRetriever implements FailedWriterCallback, FailedCollectorCallback {
    private String tideID;
    private TideReplicaManagerReference replicaServer;
    private TideReplicasDescriptor replicasDescriptor;
    private CurrentWriter writer;
    
    public TideRetriever(String tideID, File tideStorageFile, TideReplicaManagerReference replicaServer, TideReplicasDescriptor replicasDescriptor) throws Exception {
        this.tideID = tideID;
        this.replicaServer = replicaServer;
        this.replicasDescriptor = replicasDescriptor;
        writer = new CurrentWriter(tideStorageFile, replicasDescriptor.getTideDescriptor(), this);
    }
    
    public void retrieve() throws Exception{
        Thread th = new Thread(writer);
        th.start();
        executeRetrievalAlgothim();
        th.join();
    }
    
    public abstract void executeRetrievalAlgothim() throws Exception;

    public String getTideID() {
        return tideID;
    }

    public TideReplicaManagerReference getReplicaServer() {
        return replicaServer;
    }

    public TideReplicasDescriptor getReplicasDescriptor() {
        return replicasDescriptor;
    }

    public CurrentWriter getWriter() {
        return writer;
    }

}
