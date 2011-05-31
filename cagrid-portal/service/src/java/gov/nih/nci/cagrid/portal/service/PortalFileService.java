package gov.nih.nci.cagrid.portal.service;

import java.io.File;
import java.io.IOException;

/**
 * User: kherm
 *
 * @author kherm manav.kher@semanticbits.com
 */
public interface PortalFileService {
    public File write(byte[] data) throws IOException;

    public boolean delete(String fileName);

    public byte[] read(String fileName) throws IOException;
}
