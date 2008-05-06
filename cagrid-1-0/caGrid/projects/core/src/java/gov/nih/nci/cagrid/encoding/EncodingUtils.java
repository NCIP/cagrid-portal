package gov.nih.nci.cagrid.encoding;

import java.io.IOException;
import java.io.InputStream;
import java.util.Hashtable;
import java.util.Map;

import org.apache.axis.MessageContext;
import org.apache.axis.utils.ClassUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.exolab.castor.mapping.Mapping;
import org.exolab.castor.mapping.MappingException;
import org.xml.sax.EntityResolver;
import org.xml.sax.InputSource;


public class EncodingUtils {
    public static final String CASTOR_MAPPING_DTD = "mapping.dtd";
    public static final String CASTOR_MAPPING_DTD_ENTITY = "-//EXOLAB/Castor Object Mapping DTD Version 1.0//EN";
    public static final String DEFAULT_XML_MAPPING = "/xml-mapping.xml";
    public static final String CASTOR_MAPPING_PROPERTY = "castorMapping";

    protected static Log LOG = LogFactory.getLog(EncodingUtils.class.getName());

    // maps <mapping location> to <Mapping>
    // using Hashtable for synchronization correctness
    protected static Map<String, Mapping> mappingCacheMap = new Hashtable<String, Mapping>();


    public static Mapping getMapping(MessageContext context) {
        long startTime = System.currentTimeMillis();

        EntityResolver resolver = new EntityResolver() {
            public InputSource resolveEntity(String publicId, String systemId) {
                if (publicId.equals(CASTOR_MAPPING_DTD_ENTITY)) {
                    InputStream in = getClass().getResourceAsStream(CASTOR_MAPPING_DTD);
                    return new InputSource(in);
                }
                return null;
            }
        };

        // extract mapping file from message context, such that multiple
        // services can use this code using different mappings.
        String mappingLocation = DEFAULT_XML_MAPPING;
        if (context != null) {
            String prop = (String) context.getProperty(CASTOR_MAPPING_PROPERTY);
            if (prop != null && !prop.trim().equals("")) {
                mappingLocation = prop;
                LOG.debug("Loading castor mapping from message context property[" + CASTOR_MAPPING_PROPERTY + "]");
            } else {
                try {
                    prop = (String) context.getAxisEngine().getConfig().getGlobalOptions().get(CASTOR_MAPPING_PROPERTY);
                } catch (Exception e) {
                    LOG.debug("Error reading global configuration:" + e.getMessage(), e);
                }
                if (prop != null && !prop.trim().equals("")) {
                    mappingLocation = prop;
                    LOG.debug("Loading castor mapping from globalConfiguration property[" + CASTOR_MAPPING_PROPERTY
                        + "]");
                } else {
                    LOG.debug("Unable to locate castor mapping property[" + CASTOR_MAPPING_PROPERTY
                        + "], using default mapping location:" + DEFAULT_XML_MAPPING);
                }
            }
        } else {
            LOG.debug("Unable to determine message context, using default mapping location:" + DEFAULT_XML_MAPPING);
        }

        Mapping mapping = null;
        if (mappingCacheMap.containsKey(mappingLocation)) {
            LOG.debug("Loading Mapping from cache for location:" + mappingLocation);
            mapping = mappingCacheMap.get(mappingLocation);
        } else {
            LOG.debug("Unable to loading Mapping from cache for location:" + mappingLocation);
            LOG.debug("Attempting to load mapping from mapping location:" + mappingLocation);
            InputStream mappingStream = ClassUtils.getResourceAsStream(EncodingUtils.class, mappingLocation);
            if (mappingStream == null) {
                LOG.error("Mapping file [" + mappingLocation + "] was null!");
            } else {
                InputSource mappIS = new org.xml.sax.InputSource(mappingStream);
                mapping = new Mapping();
                mapping.setEntityResolver(resolver);
                try {
                    mapping.loadMapping(mappIS);
                    mappingCacheMap.put(mappingLocation, mapping);
                } catch (IOException e) {
                    LOG.error("Unable to load mapping file:" + mappingLocation, e);
                    mapping = null;
                } catch (MappingException e) {
                    LOG.error("Problem with mapping!", e);
                    mapping = null;
                }
            }

        }
        long duration = System.currentTimeMillis() - startTime;
        LOG.debug("Time to load mapping file:" + duration + " ms.");

        return mapping;
    }
}
