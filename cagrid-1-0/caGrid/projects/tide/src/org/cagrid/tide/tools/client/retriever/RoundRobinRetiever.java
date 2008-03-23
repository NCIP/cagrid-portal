package org.cagrid.tide.tools.client.retriever;

import java.io.File;

import org.cagrid.tide.descriptor.Current;
import org.cagrid.tide.descriptor.TideDescriptor;
import org.cagrid.tide.descriptor.TideReplicaDescriptor;
import org.cagrid.tide.descriptor.TideReplicasDescriptor;
import org.cagrid.tide.replica.stubs.types.TideReplicaManagerReference;


public class RoundRobinRetiever extends TideRetriever {

    public RoundRobinRetiever(String tideID, File tideStorageFile, TideReplicaManagerReference replicaServer,
        TideReplicasDescriptor replicasDescriptor) throws Exception {
        super(tideID, tideStorageFile, replicaServer, replicasDescriptor);
        // TODO Auto-generated constructor stub
    }


    public void executeRetrievalAlgothim() throws Exception {
        // /*
        // for (int i = 0; i <
        // getReplicasDescriptor().getTideDescriptor().getChunks(); i = i+2) {
        // int nextHost = i %
        // getReplicasDescriptor().getTideReplicaDescriptor().length;
        //
        // TideReplicaDescriptor tideRep =
        // getReplicasDescriptor().getTideReplicaDescriptor(nextHost);
        // Current current =
        // getReplicasDescriptor().getTideDescriptor().getCurrents().getCurrent(i);
        // CurrentCollector collector = new CurrentCollector(new
        // Current[]{current}, getWriter(),
        // getReplicasDescriptor().getTideDescriptor(), tideRep,
        // RoundRobinRetiever.this);
        //
        // RetrieverWorkerPool.getInstance().submit(collector);
        // }*/
        CurrentCollector collector = new CurrentCollector(getReplicasDescriptor().getTideDescriptor().getCurrents().getCurrent(),getWriter(), getReplicasDescriptor().getTideDescriptor(), getReplicasDescriptor().getTideReplicaDescriptor(0), RoundRobinRetiever.this);

        RetrieverWorkerPool.getInstance().submit(collector);

    }


    public void failedCollector(CurrentCollector collector) {
        System.out.println("Failed Collector");
    }


    public void failedWriter(CurrentWriter writer) {
        System.out.println("Failed Writer");

    }


    public void failedCollector(Current current, TideDescriptor tideDescriptor, TideReplicaDescriptor tideRepDescriptor) {
        System.out.println("Failed Current in Collector");
    }

}