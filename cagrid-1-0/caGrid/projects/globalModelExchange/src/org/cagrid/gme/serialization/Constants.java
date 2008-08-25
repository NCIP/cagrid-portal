package org.cagrid.gme.serialization;

import javax.xml.namespace.QName;

import org.cagrid.gme.domain.XMLSchema;
import org.cagrid.gme.domain.XMLSchemaBundle;


public interface Constants {

    // namespaces
    public static final String GME_DOMAIN_NAMESPACE = "gme://gme.cagrid.org/2.0/GlobalModelExchange/domain";

    public static final String XML_SCHEMA_NAME = XMLSchema.class.getSimpleName();
    public static final QName XML_SCHEMA_QNAME = new QName(GME_DOMAIN_NAMESPACE, XML_SCHEMA_NAME);

    public static final String XML_SCHEMA_BUNDLE_NAME = XMLSchemaBundle.class.getSimpleName();
    public static final QName XML_SCHEMA_BUNDLE_QNAME = new QName(GME_DOMAIN_NAMESPACE, XML_SCHEMA_BUNDLE_NAME);

}
