package org.cagrid.transfer.context.service.globus.resource;

import gov.nih.nci.cagrid.introduce.servicetools.security.SecurityUtils;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Calendar;
import java.util.GregorianCalendar;

import javax.xml.namespace.QName;

import org.cagrid.transfer.context.service.helper.DataStagedCallback;
import org.cagrid.transfer.descriptor.DataDescriptor;
import org.cagrid.transfer.descriptor.DataStorageDescriptor;
import org.cagrid.transfer.descriptor.Status;
import org.cagrid.transfer.service.TransferServiceConfiguration;
import org.globus.wsrf.ResourceException;


/**
 * The implementation of this TransferServiceContextResource type.
 * 
 * @created by Introduce Toolkit version 1.2
 */
public class TransferServiceContextResource extends TransferServiceContextResourceBase {

    private DataStagedCallback callback = null;
    
    
    @Override
    public void initialize(Object resourceBean, QName resourceElementQName, Object id) throws ResourceException {
        super.initialize(resourceBean, resourceElementQName, id);
        Calendar cal = GregorianCalendar.getInstance();
        cal.roll(Calendar.MINUTE, 30);
        this.setTerminationTime(cal);
    }
    
    
    public DataStagedCallback getDataStagedCallback(){
        return this.callback;
    }
    
    
    
    public void stage(DataDescriptor dd, DataStagedCallback callback) throws Exception {
        this.callback = callback;
        
        File storageFile = new File(getStorageDirectory().getAbsolutePath() + File.separator + (String) getID()
            + ".cache");
        DataStorageDescriptor desc = new DataStorageDescriptor();
        desc.setLocation(storageFile.getAbsolutePath());
        if (SecurityUtils.getCallerIdentity() != null) {
            desc.setUserDN(SecurityUtils.getCallerIdentity());
        }
        desc.setDataDescriptor(dd);
        desc.setStatus(Status.Staging);
        setDataStorageDescriptor(desc);
    }


    public void stage(final byte[] data, final DataDescriptor dd) throws Exception {
        Thread th = new Thread(new Runnable() {
        
            public void run() {
                try {
                    DataStorageDescriptor desc = new DataStorageDescriptor();
                    File storageFile = new File(getStorageDirectory().getAbsolutePath() + File.separator + (String) getID()
                        + ".cache");
                    desc.setLocation(storageFile.getAbsolutePath());
                    if (SecurityUtils.getCallerIdentity() != null) {
                        desc.setUserDN(SecurityUtils.getCallerIdentity());
                    }
                    desc.setDataDescriptor(dd);
                    desc.setStatus(Status.Staging);
                    setDataStorageDescriptor(desc);
                    
                    
                    FileOutputStream fw = new FileOutputStream(storageFile);
                    fw.write(data);
                    fw.close();
                    
                    
                    desc.setStatus(Status.Staged);
                    setDataStorageDescriptor(desc);
                    
                } catch (ResourceException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                } catch (FileNotFoundException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                } catch (IOException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                } catch (Exception e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }
            }
        
        });
        
        th.start();
        
    }


    public void stage(final InputStream is, final DataDescriptor dd) throws Exception {
        Thread th = new Thread(new Runnable() {
        
            public void run() {
                try {
                    
                    DataStorageDescriptor desc = new DataStorageDescriptor();
                    File storageFile = new File(getStorageDirectory().getAbsolutePath() + File.separator + (String) getID()
                        + ".cache");
                    desc.setLocation(storageFile.getAbsolutePath());
                    if (SecurityUtils.getCallerIdentity() != null) {
                        desc.setUserDN(SecurityUtils.getCallerIdentity());
                    }
                    desc.setDataDescriptor(dd);
                    desc.setStatus(Status.Staging);
                    setDataStorageDescriptor(desc);
                    
            
                    FileOutputStream fw = new FileOutputStream(storageFile);
                    byte[] data = new byte[1024];
                    int length = is.read(data);
                    while (length >= 0) {
                        fw.write(data);
                        length = is.read(data);
                    }
                    fw.write(data,0,length);
                    fw.close();
                    
                    desc.setStatus(Status.Staged);
                    setDataStorageDescriptor(desc);
                    
                } catch (ResourceException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                } catch (FileNotFoundException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                } catch (IOException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                } catch (Exception e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }
        
            }
        
        });
        
        th.start();

    }


    public void stage(File file, DataDescriptor dd) throws Exception {
        DataStorageDescriptor desc = new DataStorageDescriptor();
        desc.setLocation(file.getAbsolutePath());
        if (SecurityUtils.getCallerIdentity() != null) {
            desc.setUserDN(SecurityUtils.getCallerIdentity());
        }
        desc.setDataDescriptor(dd);
        desc.setStatus(Status.Staged);
        setDataStorageDescriptor(desc);
    }


    private void removeDataFile() throws Exception {
        if (getDataStorageDescriptor() != null && getDataStorageDescriptor().getLocation() != null) {
            String location = getDataStorageDescriptor().getLocation();
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
