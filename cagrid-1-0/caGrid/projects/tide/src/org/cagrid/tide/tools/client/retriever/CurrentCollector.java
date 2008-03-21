package org.cagrid.tide.tools.client.retriever;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import org.cagrid.tide.context.client.TideContextClient;
import org.cagrid.tide.descriptor.Current;
import org.cagrid.tide.descriptor.TideDescriptor;
import org.cagrid.tide.descriptor.TideReplicaDescriptor;
import org.cagrid.tide.descriptor.WaveDescriptor;
import org.cagrid.tide.descriptor.WaveRequest;
import org.cagrid.transfer.context.client.TransferServiceContextClient;
import org.cagrid.transfer.context.client.helper.TransferClientHelper;

import com.twmacinta.util.MD5InputStream;


public class CurrentCollector implements Runnable {
    private TideReplicaDescriptor tideRep = null;
    private TideDescriptor tideDescriptor = null;
    private Current current = null;
    private List<byte[]> byteArrays = new ArrayList<byte[]>();
    private FailedCollectorCallback failedCallback = null;
    private boolean failed = false;
    private CurrentWriter writer = null;
    private long collectionTimeWithOverhead = 0;
    private long collectionTime = 0;
    private long bytesRead = 0;


    public CurrentCollector(Current current, CurrentWriter writer, TideDescriptor tideDescriptor,
        TideReplicaDescriptor tideRep, FailedCollectorCallback callback) throws Exception {
        this.current = current;
        this.writer = writer;
        this.tideDescriptor = tideDescriptor;
        this.tideRep = tideRep;
        this.failedCallback = callback;
    }


    public long getCollectionTimeInMillis() {
        return collectionTime;
    }


    public long getCollectionTimeWithOverheadInMillis() {
        return collectionTimeWithOverhead;
    }


    private void collect() throws Exception {

        long start1 = System.currentTimeMillis();
        TideContextClient tideClient = new TideContextClient(tideRep.getEndpointReference());
        WaveDescriptor wave = tideClient.getWave(new WaveRequest(new Current[]{current}, tideDescriptor.getId()));
        TransferServiceContextClient transClient = new TransferServiceContextClient(wave
            .getTransferServiceContextReference().getEndpointReference());
        InputStream is = TransferClientHelper.getData(transClient.getDataTransferDescriptor());
        MD5InputStream mis = new MD5InputStream(is);

        long start2 = System.currentTimeMillis();

        byte[] bytes = new byte[65536];

        int currentAmmountRead = 0;
        byte[] readBytes = new byte[65536];
        int read = mis.read(bytes);
        while (read != -1) {
            bytesRead += read;
            if (currentAmmountRead + read > 65536) {
                byteArrays.add(readBytes);
                readBytes = new byte[65536];
                currentAmmountRead = 0;
            }

            System.arraycopy(bytes, 0, readBytes, currentAmmountRead, read);
            currentAmmountRead += read;
            read = mis.read(bytes);
        }

        long stop = System.currentTimeMillis();
        collectionTime = stop - start2;
        collectionTimeWithOverhead = stop - start1;
        System.out.println("Read chunk " + this.current.getChunkNum() + " in " + collectionTime
            + " milliseconds for data read and " + collectionTimeWithOverhead + " in total");
        if (!this.current.getMd5Sum().equals(mis.getMD5().asHex())) {
            this.failed = true;
        } else {
            writer.addCurrentCollector(this);
        }

    }


    public Current getCurrent() {
        return this.current;
    }


    public List<byte[]> getDataArrays() {
        return this.byteArrays;
    }


    public void run() {
        try {
            collect();
        } catch (Exception e) {
            failed = true;
            e.printStackTrace();
            if (failedCallback != null) {
                failedCallback.failedCollector(this);
            }
        }
    }
}