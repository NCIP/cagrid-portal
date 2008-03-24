package org.cagrid.tide.tools.client.retriever.impl;

import java.io.File;

import org.cagrid.tide.descriptor.Current;
import org.cagrid.tide.descriptor.TideDescriptor;
import org.cagrid.tide.descriptor.TideReplicaDescriptor;
import org.cagrid.tide.descriptor.TideReplicasDescriptor;
import org.cagrid.tide.replica.stubs.types.TideReplicaManagerReference;
import org.cagrid.tide.tools.client.retriever.common.CurrentCollector;
import org.cagrid.tide.tools.client.retriever.common.CurrentWriter;
import org.cagrid.tide.tools.client.retriever.common.RetrieverWorkerPool;
import org.cagrid.tide.tools.client.retriever.common.TideRetriever;


public class BalancedRetiever extends TideRetriever {

    public BalancedRetiever(String tideID, File tideStorageFile, TideReplicaManagerReference replicaServer,
        TideReplicasDescriptor replicasDescriptor) throws Exception {
        super(tideID, tideStorageFile, replicaServer, replicasDescriptor);
    }


    public void executeRetrievalAlgothim() throws Exception {

        for (int i = 0; i < getReplicasDescriptor().getTideDescriptor().getChunks(); i++) {
            int nextHost = i % getReplicasDescriptor().getTideReplicaDescriptor().length;

            TideReplicaDescriptor tideRep = getReplicasDescriptor().getTideReplicaDescriptor(nextHost);
            Current current = getReplicasDescriptor().getTideDescriptor().getCurrents().getCurrent(i);
            CurrentCollector collector = new CurrentCollector(new Current[]{current}, getWriter(),
                getReplicasDescriptor().getTideDescriptor(), tideRep, BalancedRetiever.this);

            RetrieverWorkerPool.getInstance().submit(collector);
        }

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