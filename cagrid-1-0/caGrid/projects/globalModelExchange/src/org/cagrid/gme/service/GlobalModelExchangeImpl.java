package org.cagrid.gme.service;

import java.rmi.RemoteException;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.config.PropertyPlaceholderConfigurer;
import org.springframework.beans.factory.xml.XmlBeanFactory;
import org.springframework.core.io.FileSystemResource;


/**
 * The service side implementation class of the GlobalModelExchange,managed by
 * Introduce.
 * 
 * @created by Introduce Toolkit version 1.0
 */
public class GlobalModelExchangeImpl extends GlobalModelExchangeImplBase {
    
    protected static Log LOG = LogFactory.getLog(GlobalModelExchangeImpl.class.getName());
    
    protected static final String GME_BEAN_NAME = "gme";
    protected GME gme = null;


    public GlobalModelExchangeImpl() throws RemoteException {
        super();
        try {
            String gmeConfigurationFile = getConfiguration().getGmeConfigurationFile();
            String gmeProperties = getConfiguration().getGmePropertiesFile();
            FileSystemResource gmeConfResource = new FileSystemResource(gmeConfigurationFile);
            FileSystemResource gmePropertiesResource = new FileSystemResource(gmeProperties);

            XmlBeanFactory factory = new XmlBeanFactory(gmeConfResource);
            PropertyPlaceholderConfigurer cfg = new PropertyPlaceholderConfigurer();
            cfg.setLocation(gmePropertiesResource);
            cfg.postProcessBeanFactory(factory);

            gme = (GME) factory.getBean(GME_BEAN_NAME, GME.class);

        } catch (Exception e) {
            String message = "Problem inititializing GME while loading configuration:" + e.getMessage();
            LOG.error(message, e);
            throw new RemoteException(message, e);
        }
    }

}
