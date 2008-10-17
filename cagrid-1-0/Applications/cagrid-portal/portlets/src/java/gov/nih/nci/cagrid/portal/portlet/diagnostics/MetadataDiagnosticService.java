package gov.nih.nci.cagrid.portal.portlet.diagnostics;

import gov.nih.nci.cagrid.metadata.ServiceMetadata;
import gov.nih.nci.cagrid.portal.aggr.geocode.Geocoder;
import gov.nih.nci.cagrid.portal.aggr.geocode.GeocodingException;
import gov.nih.nci.cagrid.portal.domain.Address;
import gov.nih.nci.cagrid.portal.util.Metadata;
import gov.nih.nci.cagrid.portal.util.PortalUtils;
import org.directwebremoting.annotations.Param;
import org.directwebremoting.annotations.RemoteProxy;
import org.directwebremoting.spring.SpringCreator;
import org.springframework.beans.factory.annotation.Required;

/**
 * User: kherm
 *
 * @author kherm manav.kher@semanticbits.com
 */
@RemoteProxy(name = "metadataDiagnostic",
        creator = SpringCreator.class,
        creatorParams = @Param(name = "beanName",
                value = "metadataDiagnosticService"))
public class MetadataDiagnosticService extends AbstractDiagnosticService {

    private long defaultTimeout;
    private Geocoder geocoder;

    public DiagnosticResult diagnoseInternal(String Url) {
        DiagnosticResult _result = new DiagnosticResult(DiagnosticType.METADATA, DiagnosticResultStatus.FAILED);
        Metadata _meta;
        try {
            _meta = PortalUtils.getMetadata(Url, defaultTimeout);

            _result.setStatus(DiagnosticResultStatus.PASSED);
            logger.debug("Retrieved metadata sucessfully for " + Url);
            _result.setMessage("Service Metadata retrieved sucessfully");
            try {
                ServiceMetadata _sMeta = _meta.smeta;
                Address _address = new Address();
                //Might throug NPE
                _address.setPostalCode(_sMeta.getHostingResearchCenter().getResearchCenter().getAddress().getPostalCode());
                if (geocoder.getGeocode(_address) == null)
                    throw new GeocodingException();
            } catch (Exception e) {
                logger.warn("Geocoder threw an exception" + e.getMessage());
                _result.setStatus(DiagnosticResultStatus.PROBLEM);
                _result.setMessage("Service Metadata is present, but has problems");
                _result.setDetail("Metadata is missing Research center location information. Your service will not appear on the Portal Map");
            }
        } catch (Exception e) {
            _result.setStatus(DiagnosticResultStatus.FAILED);
            _result.setMessage("Could not retreive Metadata");
            logger.debug("Failed to retreive METADATA for " + Url);
        }
        return _result;
    }

    protected DiagnosticType getDiangosticType() {
        return DiagnosticType.METADATA;
    }

    public long getDefaultTimeout() {
        return defaultTimeout;
    }

    public void setDefaultTimeout(long defaultTimeout) {
        this.defaultTimeout = defaultTimeout;
    }

    @Required
    public Geocoder getGeocoder() {
        return geocoder;
    }

    public void setGeocoder(Geocoder geocoder) {
        this.geocoder = geocoder;
    }
}
