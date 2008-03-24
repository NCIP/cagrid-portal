package org.cagrid.tide.context.service.fetcher;

import gov.nih.nci.cagrid.common.FixedPortionFileInputStream;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.RandomAccessFile;
import java.rmi.RemoteException;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.NoSuchElementException;

import org.cagrid.tide.descriptor.Current;
import org.cagrid.tide.descriptor.TideDescriptor;
import org.cagrid.tide.descriptor.WaveDescriptor;
import org.cagrid.tide.descriptor.WaveRequest;
import org.cagrid.tide.service.TideConfiguration;
import org.cagrid.transfer.context.service.helper.TransferServiceHelper;
import org.cagrid.transfer.context.stubs.types.TransferServiceContextReference;


public class FilePersistenceWaveFetcher implements WaveFetcher {

    public class CurrentInputStream extends InputStream {
        RandomAccessFile raf;
        Current[] currents;
        long chunkSize;
        long waveBytesRead = 0;
        int currentChunk = 0;
        LinkedList<FixedPortionFileInputStream> inputStreams;
        List<FixedPortionFileInputStream> readStreams;
        FixedPortionFileInputStream ris;


        public CurrentInputStream(File dataFile, Current[] currents, long chunkSize) throws Exception {
            this.currents = currents;
            this.chunkSize = chunkSize;
            readStreams = new ArrayList<FixedPortionFileInputStream>();
            inputStreams = new LinkedList<FixedPortionFileInputStream>();
            for (int i = 0; i < currents.length; i++) {
                System.out.println(currents[i].getChunkNum());
                FixedPortionFileInputStream fileIS = new FixedPortionFileInputStream(dataFile, chunkSize
                    * currents[i].getChunkNum(), chunkSize);
                inputStreams.addLast(fileIS);
            }
            ris = inputStreams.removeFirst();
        }


        public long getRead() {
            return waveBytesRead;
        }


        public int read() throws IOException {
            if (waveBytesRead < chunkSize * currents.length && ris != null) {
                int b = ris.read();
                if (b != -1) {
                    waveBytesRead++;
                } else {
                    try {
                        readStreams.add(ris);
                        ris = inputStreams.removeFirst();
                        b = ris.read();
                        waveBytesRead++;
                    } catch (NoSuchElementException e) {
                        b = -1;
                    }
                }
                return b;
            } else {
                if (ris != null) {
                    readStreams.add(ris);
                }
                return -1;
            }
        }


        @Override
        public void close() throws IOException {
            super.close();
            for (int i = 0; i < readStreams.size(); i++) {
                readStreams.get(i).close();
            }
        }

    }


    public WaveDescriptor getWave(WaveRequest waveRequest, TideDescriptor tideDescriptor) throws Exception {
        try {
            WaveRequest waveR = waveRequest;
            File tideFile = new File(TideConfiguration.getConfiguration().getTideStorageDir() + File.separator
                + tideDescriptor.getName() + "_" + tideDescriptor.getId() + ".tide");

            InputStream is = new CurrentInputStream(tideFile, waveR.getCurrent(), tideDescriptor.getChunkSize());
            TransferServiceContextReference tref = TransferServiceHelper.createTransferContext(is, null);
            WaveDescriptor wave = new WaveDescriptor(waveR.getCurrent(), waveR.getTideId(), tref);

            return wave;
        } catch (Exception e) {
            throw new RemoteException("ERROR", e);
        }
    }

}
