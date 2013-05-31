/**
*============================================================================
*  The Ohio State University Research Foundation, The University of Chicago -
*  Argonne National Laboratory, Emory University, SemanticBits LLC, 
*  and Ekagra Software Technologies Ltd.
*
*  Distributed under the OSI-approved BSD 3-Clause License.
*  See http://ncip.github.com/cagrid-portal/LICENSE.txt for details.
*============================================================================
**/
package gov.nih.nci.cagrid.portal.security.service;

import gov.nih.nci.cagrid.portal.authn.EncryptionService;
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
