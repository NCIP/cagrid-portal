package org.cagrid.transfer.context.service.globus.resource;

import gov.nih.nci.cagrid.introduce.servicetools.security.SecurityUtils;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.util.Calendar;
import java.util.GregorianCalendar;

import javax.xml.namespace.QName;

import org.cagrid.transfer.descriptor.DataDescriptor;
import org.cagrid.transfer.service.TransferServiceConfiguration;
import org.globus.wsrf.ResourceException;


/**
 * The implementation of this TransferServiceContextResource type.
 * 
 * @created by Introduce Toolkit version 1.2
 */
public class TransferServiceContextResource extends TransferServiceContextResourceBase {

    @Override
    public void initialize(Object resourceBean, QName resourceElementQName, Object id) throws ResourceException {
        super.initialize(resourceBean, resourceElementQName, id);
        Calendar cal = GregorianCalendar.getInstance();
        cal.roll(Calendar.MINUTE, 30);
        this.setTerminationTime(cal);
    }


    public void stage(byte[] data) throws Exception {
        File storageFile = new File(getStorageDirectory().getAbsolutePath() + File.separator + (String) getID()
            + ".cache");
        FileOutputStream fw = new FileOutputStream(storageFile);
        fw.write(data);
        fw.close();
        DataDescriptor desc = new DataDescriptor();
        desc.setLocation(storageFile.getAbsolutePath());
        if (SecurityUtils.getCallerIdentity() != null) {
            desc.setUserDN(SecurityUtils.getCallerIdentity());
        }
        setDataDescriptor(desc);
    }


    public void stage(InputStream is) throws Exception {
        File storageFile = new File(getStorageDirectory().getAbsolutePath() + File.separator + (String) getID()
            + ".cache");
        FileOutputStream fw = new FileOutputStream(storageFile);
        int data = is.read();
        while (data > 0) {
            fw.write(data);
            data = is.read();
        }
        fw.close();
        DataDescriptor desc = new DataDescriptor();
        desc.setLocation(storageFile.getAbsolutePath());
        if (SecurityUtils.getCallerIdentity() != null) {
            desc.setUserDN(SecurityUtils.getCallerIdentity());
        }
        setDataDescriptor(desc);
    }


    public void stage(File file) throws Exception {
        DataDescriptor desc = new DataDescriptor();
        desc.setLocation(file.getAbsolutePath());
        if (SecurityUtils.getCallerIdentity() != null) {
            desc.setUserDN(SecurityUtils.getCallerIdentity());
        }
        setDataDescriptor(desc);
    }


    private void removeDataFile() throws Exception {
        if (getDataDescriptor() != null && getDataDescriptor().getLocation() != null) {
            String location = getDataDescriptor().getLocation();
            if (location.startsWith(getStorageDirectory().getAbsolutePath())) {
                File dataFile = new File(location);
                dataFile.delete();
            }
        }
    }


    private File getStorageDirectory() throws Exception {
        String storageDirS = TransferServiceConfiguration.getConfiguration().getStorageDirectory();
        File storageDir = new File(storageDirS);
        if (!storageDir.exists()) {
            storageDir.mkdirs();
        }
        return storageDir;
    }


    @Override
    public void remove() throws ResourceException {
        super.remove();
        try {
            removeDataFile();
        } catch (Exception e) {
            e.printStackTrace();
            throw new ResourceException(e);
        }
    }
    
    
}