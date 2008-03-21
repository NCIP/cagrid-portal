package org.cagrid.tide.tools.client.retriever;

import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;


public class RetrieverWorkerPool {
    private ThreadPoolExecutor executor = null;
    private static RetrieverWorkerPool pool = null;


    private RetrieverWorkerPool() {
        executor = new ThreadPoolExecutor(20, 40, 10, TimeUnit.SECONDS, new ArrayBlockingQueue<Runnable>(80));
    }


    public static RetrieverWorkerPool getInstance() {
        if (pool == null) {
            pool = new RetrieverWorkerPool();
        }
        return pool;
    }


    public void submit(Runnable r) {
        executor.submit(r);
    }


    public void shutdown() throws Exception {
        executor.shutdown();
        executor.awaitTermination(60 * 60 * 24, TimeUnit.SECONDS);
    }

}