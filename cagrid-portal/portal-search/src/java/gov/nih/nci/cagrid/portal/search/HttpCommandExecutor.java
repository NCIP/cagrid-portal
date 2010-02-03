package gov.nih.nci.cagrid.portal.search;

import org.apache.commons.httpclient.HttpMethod;

import java.io.IOException;

/**
 * User: kherm
 *
 * @author kherm manav.kher@semanticbits.com
 */
public interface HttpCommandExecutor {

    public void execute(HttpMethod httpMethod) throws IOException;
    
}
