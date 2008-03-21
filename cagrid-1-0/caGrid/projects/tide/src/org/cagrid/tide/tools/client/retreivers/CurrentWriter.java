package org.cagrid.tide.tools.client.retreivers;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.util.Iterator;
import java.util.concurrent.ArrayBlockingQueue;

import org.cagrid.tide.descriptor.Current;
import org.cagrid.tide.descriptor.TideDescriptor;

import com.twmacinta.util.MD5InputStream;


public class CurrentWriter implements Runnable {
    private File outputFile = null;
    private ArrayBlockingQueue<CurrentCollector> currents = new ArrayBlockingQueue<CurrentCollector>(4);
    private RandomAccessFile raf = null;
    private final TideDescriptor tide;
    private boolean failed = false;
    private int chunksProcessed = 0;
    private long totalDataWriteTime = 0;
    private FailedWriterCallback callback;
    CurrentCollector currentCurrentCollector = null;


    public CurrentWriter(File outputFile, final TideDescriptor tide, FailedWriterCallback callback) throws Exception {
        this.outputFile = outputFile;
        this.tide = tide;
        this.callback = callback;
        raf = new RandomAccessFile(outputFile, "rw");
    }


    public void addCurrentCollector(CurrentCollector current) throws Exception {
        this.currents.put(current);
    }
    
    public CurrentCollector getCurrentCurrentCollector(){
     return currentCurrentCollector;   
    }


    public void run() {
        while (chunksProcessed < tide.getChunks()) {
            currentCurrentCollector = currents.poll();
            if (currentCurrentCollector != null) {
                System.out.println("Writting chunk " +  currentCurrentCollector.getCurrent().getChunkNum());
                Current current = this.tide.getCurrents().getCurrent(currentCurrentCollector.getCurrent().getChunkNum());
                long start = System.currentTimeMillis();
                try {
                    raf.seek(this.tide.getChunkSize() * current.getChunkNum());
                    Iterator<byte[]> it = currentCurrentCollector.getDataArrays().iterator();
                    while (it.hasNext()) {
                        raf.write(it.next());
                    }
                    chunksProcessed++;
                } catch (IOException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }
                long stop = System.currentTimeMillis();
                this.totalDataWriteTime += (stop-start);
            } else {
                try {
                    System.out.println("Waiting for data to write");
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    failed = true;
                    e.printStackTrace();
                    callback.failedWriter(this);
                }
            }
        }

        // check the md5sum of the new file
        try {
            MD5InputStream mis = new MD5InputStream(new FileInputStream(outputFile));
            byte[] buf = new byte[65536];
            int num_read;
            while ((num_read = mis.read(buf)) != -1);
            mis.getMD5().asHex();
            if (!mis.getMD5().asHex().equals(tide.getMd5Sum())) {
                throw new Exception("File download was corrupted!");
            }
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
            callback.failedWriter(this);
        }

    }

}
