package org.cagrid.gme.service;

import java.io.File;
import java.rmi.RemoteException;

import org.apache.commons.configuration.Configuration;
import org.apache.commons.configuration.PropertiesConfiguration;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.cagrid.gme.common.ConfigurationInitilizable;
import org.cagrid.gme.persistence.SchemaPersistenceI;


/**
 * The service side implementation class of the GlobalModelExchange,managed by
 * Introduce.
 * 
 * @created by Introduce Toolkit version 1.0
 */
public class GlobalModelExchangeImpl extends GlobalModelExchangeImplBase {
    protected static Log LOG = LogFactory.getLog(GlobalModelExchangeImpl.class.getName());

    protected GME gme = null;


    public GlobalModelExchangeImpl() throws RemoteException {
        super();
        Class schemaPersistenceImplClass;
        try {
            String classNameProp = getConfiguration().getSchemaPersistenceImplementationClassname();
            LOG.info("Attempting to load SchemaPersistenceI implementation Class:" + classNameProp);
            schemaPersistenceImplClass = Class.forName(classNameProp);
            SchemaPersistenceI schemaPersistence = (SchemaPersistenceI) schemaPersistenceImplClass.newInstance();
            if (schemaPersistence instanceof ConfigurationInitilizable) {
                Configuration configuration = null;
                String props = getConfiguration().getSchemaPersistenceImplementationPropertiesFile();
                LOG.info(classNameProp + " implements ConfigurationInitilizable so trying to load properties file:"
                    + props);
                File propsFile = new File(props);
                if (propsFile.exists() && propsFile.canRead()) {
                    configuration = new PropertiesConfiguration(propsFile);
                    ((ConfigurationInitilizable) schemaPersistence).setConfiguration(configuration);
                } else {
                    String message = "SchemaPersistenceI implementation class [" + classNameProp
                        + "] implements ConfigurationInitilizable, but unable to load intializing properties file:"
                        + props;
                    LOG.error(message);
                    throw new RemoteException(message);
                }
            } else {
                LOG
                    .info("Initializing GME Service with non-ConfigurationInitilizable SchemaPersistenceI implementation class:"
                        + schemaPersistence.getClass().getName());
            }

            this.gme = new GME(schemaPersistence);
        } catch (Exception e) {
            String message = "Cannot find or initialize SchemaPersistenceI implementation class";
            LOG.error(message, e);

            throw new RemoteException(message, e);
        }

    }

}
