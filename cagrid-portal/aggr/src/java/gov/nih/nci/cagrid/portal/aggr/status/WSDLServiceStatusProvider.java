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
package gov.nih.nci.cagrid.portal.aggr.status;

import gov.nih.nci.cagrid.portal.domain.ServiceStatus;
import org.apache.axis.wsdl.gen.Parser;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 * Will try and parse service wsdl and return status on
 * the validity of service wsdl
 * <p/>
 * User: kherm
 *
 * @author kherm manav.kher@semanticbits.com
 */
public class WSDLServiceStatusProvider implements ServiceStatusProvider {
    private static final Log logger = LogFactory
            .getLog(WSDLServiceStatusProvider.class);

    public static final String WSDL_HTTP_PARAM = "?wsdl";
    private long timeout = 10000;

    public ServiceStatus getStatus(String serviceUrl) {
        String _wsdlurl = formulateWsdlUrl(serviceUrl);

        ServiceStatus _result = ServiceStatus.INACTIVE;
        org.apache.axis.wsdl.gen.Parser _wsdlParser = new Parser();
        _wsdlParser.setTimeout(getTimeout());
        try {
            _wsdlParser.run(_wsdlurl);
            _result = ServiceStatus.ACTIVE;
        } catch (Exception e) {
            logger.debug("WSDL Diagnostic failed for " + _wsdlurl);
        }
        return _result;
    }

    private String formulateWsdlUrl(String Url) {
        if (!Url.endsWith(WSDL_HTTP_PARAM))
            return Url.concat(WSDL_HTTP_PARAM);
        return Url;
    }

    public long getTimeout() {
        return timeout;
    }

    public void setTimeout(long timeout) {
        this.timeout = timeout;
    }
}

