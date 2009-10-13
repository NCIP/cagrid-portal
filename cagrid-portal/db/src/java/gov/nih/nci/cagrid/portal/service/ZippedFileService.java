package gov.nih.nci.cagrid.portal.service;

import gov.nih.nci.cagrid.portal.util.PortalDBRuntimeException;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.InitializingBean;

import java.io.*;
import java.util.UUID;
import java.util.zip.GZIPInputStream;
import java.util.zip.GZIPOutputStream;

/**
 * Service to read/write/delete files on disk.
 * Uses an init parameter for filestore path. filestore path is the
 * directory that files are written in. Is configurable throug build.properties
 * <p/>
 * <p/>
 * User: kherm
 *
 * @author kherm manav.kher@semanticbits.com
 */
public class ZippedFileService implements InitializingBean, PortalFileService {

    private String fileStorePath;
    private Log logger = LogFactory.getLog(getClass());
    private String extension = "zip";


    /**
     * Will write out a file at filestorePath + filename as
     * a compressed (Gzipped) file
     *
     * @param data
     * @return
     * @throws IOException
     */
    public File write(byte[] data) throws IOException {

        String fileName = UUID.randomUUID().toString() + "." + extension;
        logger.debug("Using random filename. Will create " + fileName);

        File file = new File(getFileStorePath(), fileName);
        FileOutputStream fout = new FileOutputStream(file);
        GZIPOutputStream gOut = new GZIPOutputStream(fout);
        gOut.write(data);
        gOut.close();

        return file;
    }

    public boolean delete(String fileName) {
        boolean deleted = false;
        File file = new File(getFileStorePath(), fileName);
        if (file.exists()) {
            logger.debug("Filename exists. Will delete file");
            deleted = file.delete();
        }

        return deleted;
    }


    /**
     * Will read the file as a byte[]. File is fileStorePath + fileName
     *
     * @param fileName
     * @return
     * @throws java.io.IOException
     */
    public byte[] read(String fileName) throws IOException {
        return readBytes(fileName).toByteArray();
    }


    protected GZIPInputStream readStream(String fileName) throws IOException {
        File file = new File(getFileStorePath(), fileName);
        FileInputStream fIn = new FileInputStream(file);
        return new GZIPInputStream(fIn);
    }

    protected ByteArrayOutputStream readBytes(String fileName) throws IOException {

        GZIPInputStream in = readStream(fileName);
        byte[] buffer = new byte[8192];
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        int read = 0;
        do {
            read = in.read(buffer, 0, buffer.length);
            if (read > 0) {
                out.write(buffer, 0, read);
            }
        } while (read >= 0);
        out.flush();

        return out;
    }

    public void afterPropertiesSet() throws Exception {

        if (getFileStorePath() == null) {

            String property = "java.io.tmpdir";
            String tmpPath = System.getProperty(property);
            logger.warn("File store path not set. Will use temp directory " + tmpPath);
            setFileStorePath(tmpPath);
        }
        File file = new File(getFileStorePath());
        file.mkdir();
        if (!file.isDirectory() && file.canWrite())
            throw new PortalDBRuntimeException("File store path is not a valid directory or not writable. Please check system property filestore.path");
    }

    public String getFileStorePath() {
        return fileStorePath;
    }

    public void setFileStorePath(String fileStorePath) {
        this.fileStorePath = fileStorePath;
    }

    public String getExtension() {
        return extension;
    }

    public void setExtension(String extension) {
        this.extension = extension;
    }
}