package org.cagrid.tide.context.service.fetcher;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.RandomAccessFile;
import java.rmi.RemoteException;

import org.cagrid.tide.descriptor.TideDescriptor;
import org.cagrid.tide.descriptor.WaveDescriptor;
import org.cagrid.tide.descriptor.WaveRequest;
import org.cagrid.tide.service.TideConfiguration;
import org.cagrid.transfer.context.service.helper.TransferServiceHelper;
import org.cagrid.transfer.context.stubs.types.TransferServiceContextReference;


public class FilePersistenceWaveFetcher implements WaveFetcher {

    public abstract class CountInputStream extends InputStream {
        abstract public long getRead();
    }


    public WaveDescriptor getWave(WaveRequest waveRequest, TideDescriptor tideDescriptor) throws Exception {
        try {
            final WaveRequest waveR = waveRequest;
            final File tideFile = new File(TideConfiguration.getConfiguration().getTideStorageDir() + File.separator
                + tideDescriptor.getName() + "_" + tideDescriptor.getId() + ".tide");
            final RandomAccessFile fis = new RandomAccessFile(tideFile, "r");
            final long chunkSize = tideDescriptor.getChunkSize();
            if (waveRequest.getCurrent(0).getChunkNum() > 0) {
                fis.seek(waveRequest.getCurrent(0).getChunkNum() * chunkSize);
            }
            // only lets reading from the seeked to position to the size
            CountInputStream is = new CountInputStream() {
                long waveRead = 0;


                public long getRead() {
                    return waveRead;
                }


                public int read() throws IOException {
                    if (waveRead < chunkSize) {
                        int b = fis.read();
                        if (b != -1) {
                            waveRead++;
                        }
                        return b;
                    } else {
                        return -1;
                    }
                }

            };

            TransferServiceContextReference tref = TransferServiceHelper.createTransferContext(is, null);
            WaveDescriptor wave = new WaveDescriptor(null, is.getRead(), waveRequest.getCurrent(0).getChunkNum() * chunkSize,
                waveRequest.getCurrent(0).getChunkNum() * chunkSize + is.getRead() - 1, waveRequest.getTideId(), tref);

            return wave;
        } catch (Exception e) {
            throw new RemoteException("ERROR", e);
        }
    }

}
