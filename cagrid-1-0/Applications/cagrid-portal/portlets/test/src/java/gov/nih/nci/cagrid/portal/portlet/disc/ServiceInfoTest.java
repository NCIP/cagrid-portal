package gov.nih.nci.cagrid.portal.portlet.disc;

import gov.nih.nci.cagrid.portal.domain.ServiceInfo;
import junit.framework.TestCase;

/**
 * User: kherm
 *
 * @author kherm manav.kher@semanticbits.com
 */
public class ServiceInfoTest extends TestCase {

    public void testServiceInfo() {
        String _path = "long_path";
        String _baseURL = "http://short.url";
        String _longBaseURL = "http://really.really.really.really.really.long.url";

        String _port = "1234";
        String _url = _baseURL + ":" + _port + "/" + _path;
        String _urlWithNoPort = _baseURL + "/" + _path;

        String _longUrl = _longBaseURL + ":" + _port + "/" + _path;

        ServiceInfo info = new ServiceInfo();

        info.setUrl(_url);
        assertNotSame(_url, info.getUrlAbbrv());
        assertTrue(info.getUrlAbbrv().indexOf(_baseURL) > -1);
        assertTrue(info.getUrlAbbrv().indexOf(_path) < 1);
        assertTrue(info.getUrlAbbrv().indexOf(_port) > -1);
        assertTrue(info.getUrlAbbrv().indexOf("..") < 1);
        assertTrue(info.getUrlAbbrv().length() <= ServiceInfo.URL_MAX_LENGTH_ALLOWED);

        info.setUrl(_longUrl);
        assertNotSame(info.getUrlAbbrv(), _longUrl);
        assertTrue(info.getUrlAbbrv().indexOf(_path) < 1);
        assertTrue(info.getUrlAbbrv().indexOf("..") > -1);
        System.out.println(info.getUrlAbbrv());
        assertTrue(info.getUrlAbbrv().length() <= ServiceInfo.URL_MAX_LENGTH_ALLOWED);

        info.setUrl(_urlWithNoPort);
        assertEquals(info.getUrlAbbrv(), _urlWithNoPort);
        assertTrue(info.getUrlAbbrv().indexOf(":") < 5);
        assertTrue(info.getUrlAbbrv().length() <= ServiceInfo.URL_MAX_LENGTH_ALLOWED);

    }
}
