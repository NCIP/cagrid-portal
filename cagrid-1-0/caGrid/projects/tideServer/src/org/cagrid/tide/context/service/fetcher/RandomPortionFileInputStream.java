package org.cagrid.tide.context.service.fetcher;

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
        this.offset = offset;
        this.length = length;
        randFile = new RandomAccessFile(file,"r");
        randFile.seek(offset);
    }
    
    public long getOffset(){
        return offset;
    }
    
    public long getAmountRead(){
        return this.amountRead;
    }
    
    public int read() throws IOException {
        if (amountRead < length) {
            int b = randFile.read();
            if (b != -1) {
                amountRead++;
            } else {
                //must be at the end of file so i want to make sure if 
                //someone calls this again it returns -1
                amountRead = length;
            }
            return b;
        } else {
            return -1;
        }
    }
}