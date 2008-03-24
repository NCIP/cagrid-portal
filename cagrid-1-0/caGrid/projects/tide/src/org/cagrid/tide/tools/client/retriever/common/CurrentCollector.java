package org.cagrid.tide.tools.client.retriever.common;

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
    private Current[] currents = null;
    private List<byte[]>[] byteArrays = null;
    private FailedCollectorCallback failedCallback = null;
    private boolean failed[] = null;
    private CurrentWriter writer = null;
    private long[] collectionTime = null;
    private long[] bytesRead = null;
    private long connectionTime;


    public CurrentCollector(Current[] currents, CurrentWriter writer, TideDescriptor tideDescriptor,
        TideReplicaDescriptor tideRep, FailedCollectorCallback callback) throws Exception {
        this.currents = currents;
        this.writer = writer;
        this.tideDescriptor = tideDescriptor;
        this.tideRep = tideRep;
        this.failedCallback = callback;
        this.byteArrays = new ArrayList[currents.length];
        this.collectionTime = new long[currents.length];
        this.bytesRead = new long[currents.length];
        this.failed = new boolean[currents.length];
    }


    public List getCurrentByteArrayData(int index) {
        return byteArrays[index];
    }


    public Current getCurrent(int index) {
        return currents[index];
    }


    private void collect() throws Exception {

        long startConnection = System.currentTimeMillis();
        TideContextClient tideClient = new TideContextClient(tideRep.getEndpointReference());
        WaveDescriptor wave = tideClient.getWave(new WaveRequest(currents, tideDescriptor.getId()));
        TransferServiceContextClient transClient = new TransferServiceContextClient(wave
            .getTransferServiceContextReference().getEndpointReference());
        // this stream currently only has one current in it...........
        InputStream is = TransferClientHelper.getData(transClient.getDataTransferDescriptor());
       
        long finishedConnection = System.currentTimeMillis();
        this.connectionTime = finishedConnection - startConnection;

        for (int i = 0; i < currents.length; i++) {
            System.out.println("bout to read data from chunk " + currents[i].getChunkNum());
            MD5InputStream mis = new MD5InputStream(is);
            long start = System.currentTimeMillis();

            this.byteArrays[i] = new ArrayList<byte[]>();

            byte[] bytes = new byte[65536];

            int currentAmmountRead = 0;
            bytesRead[i] = 0;
            long needToRead = currents[i].getActualSize();
            byte[] readBytes = new byte[65536];
            int read = -2;
            if (needToRead > 65536) {
                read = mis.read(bytes);
            } else {
                read = mis.read(bytes, 0, (int) needToRead);
            }
            while (read != -1 && bytesRead[i] != currents[i].getActualSize()) {
                bytesRead[i] += read;
                if (currentAmmountRead + read > 65536) {
                    byteArrays[i].add(readBytes);
                    readBytes = new byte[65536];
                    currentAmmountRead = 0;
                }

                System.arraycopy(bytes, 0, readBytes, currentAmmountRead, read);
                currentAmmountRead += read;
                needToRead -= read;

                if (needToRead > 65536) {
                    read = mis.read(bytes);
                } else {
                    read = mis.read(bytes, 0, (int) needToRead);
                }
            }

            long stop = System.currentTimeMillis();
            collectionTime[i] = stop - start;

            System.out.println("Read chunk " + this.currents[i].getChunkNum() + " in " + collectionTime[i] + "milliseconds");

            if (!this.currents[i].getMd5Sum().equals(mis.getMD5().asHex())) {
                System.out.println("expect : " + this.currents[i].getMd5Sum() + " but got : " +  mis.getMD5().asHex());
                this.failed[i] = true;
                this.failedCallback.failedCollector(currents[i], this.tideDescriptor, this.tideRep);
            } else {
                writer.addCurrentCollector(this, i);
            }
        }

    }


    public void run() {
        try {
            collect();
        } catch (Exception e) {
            e.printStackTrace();
            if (failedCallback != null) {
                failedCallback.failedCollector(this);
            }
        }
    }
}