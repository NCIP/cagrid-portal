package org.cagrid.tide.tools.client.retreivers;

import java.io.File;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

import org.cagrid.tide.descriptor.Current;
import org.cagrid.tide.descriptor.TideReplicaDescriptor;
import org.cagrid.tide.descriptor.TideReplicasDescriptor;
import org.cagrid.tide.replica.client.TideReplicaManagerClient;
import org.cagrid.tide.replica.context.client.TideReplicaManagerContextClient;
import org.cagrid.tide.replica.stubs.types.TideReplicaManagerReference;


public class RoundRobinReteiver implements TideRetreiver, FailedCollectorCallback, FailedWriterCallback {

    public synchronized void retreive(String tideID, TideReplicaManagerReference replicaServer,
        TideRetreiver retreiver, File tideStorageFile) throws Exception {

        TideReplicaManagerClient client = new TideReplicaManagerClient(replicaServer.getEndpointReference());
        TideReplicaManagerContextClient tclient = client.getTideReplicaManagerContext(tideID);
        final TideReplicasDescriptor replicas = tclient.getReplicas();

        ThreadPoolExecutor executor = new ThreadPoolExecutor(5, 10, 10, TimeUnit.SECONDS,
            new ArrayBlockingQueue<Runnable>(20));

        final CurrentWriter writer = new CurrentWriter(tideStorageFile, replicas.getTideDescriptor(), this);
        Thread th = new Thread(writer);
        th.start();

        for (int i = 0; i < replicas.getTideDescriptor().getChunks(); i++) {
            int nextHost = i % replicas.getTideReplicaDescriptor().length;

            TideReplicaDescriptor tideRep = replicas.getTideReplicaDescriptor(nextHost);
            Current current = replicas.getTideDescriptor().getCurrents().getCurrent(i);

            CurrentCollector collector = new CurrentCollector(current, writer, replicas.getTideDescriptor(), tideRep,
                RoundRobinReteiver.this);

            executor.submit(collector);
        }

        executor.shutdown();
        while(!executor.awaitTermination(60 * 60 * 24, TimeUnit.SECONDS));
        th.join();
    }


    public void failedCollector(CurrentCollector collector) {
        System.out.println("Failed Collector");
    }


    public void failedWriter(CurrentWriter writer) {
        System.out.println("Failed Writer");
        
    }

}