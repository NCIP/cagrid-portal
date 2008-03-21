package org.cagrid.tide.tools.common;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.RandomAccessFile;

public class RandomPortionFileInputStream extends InputStream {
    File file;
    long offset;
    long length;
    RandomAccessFile randFile;
    long amountRead = 0;
    
    public RandomPortionFileInputStream(File file, long offset, long length) throws Exception {
        this.file = file;
        this.offset = offset;
        this.length = length;
        randFile = new RandomAccessFile(this.file,"r");
        randFile.seek(offset);
    }
    
    public long getAmountRead(){
        return this.amountRead;
    }
    
    public int read() throws IOException {
        if (amountRead < length) {
            int b = randFile.read();
            if (b != -1) {
                amountRead++;
            }
            return b;
        } else {
            return -1;
        }
    }
}