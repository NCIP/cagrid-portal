package gov.nih.nci.cagrid.portal.domain.catalog;

import java.util.Date;

public interface MutableTemporal extends Temporal {

    public Date getUpdatedAt();
}