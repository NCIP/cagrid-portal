package gov.nih.nci.cagrid.portal.security.service;

import gov.nih.nci.cagrid.portal.security.EncryptionService;
import gov.nih.nci.cagrid.portal.service.ZippedFileService;

import java.io.File;
import java.io.IOException;

/**
 * File service that encrypts/decrypts data
 * <p/>
 * User: kherm
 *
 * @author kherm manav.kher@semanticbits.com
 */
public class EncryptedZippedFileService extends ZippedFileService {

    private EncryptionService encryptionService;

    @Override
    public File write(byte[] data) throws IOException {
        return super.write(getEncryptionService().encrypt(data));
    }

    @Override
    public byte[] read(String fileName) throws IOException {
        byte[] encryptedByteArr = super.read(fileName);    //To change body of overridden methods use File | Settings | File Templates.
        return getEncryptionService().decrypt(encryptedByteArr);
    }

    public EncryptionService getEncryptionService() {
        return encryptionService;
    }

    public void setEncryptionService(EncryptionService encryptionService) {
        this.encryptionService = encryptionService;
    }
}
