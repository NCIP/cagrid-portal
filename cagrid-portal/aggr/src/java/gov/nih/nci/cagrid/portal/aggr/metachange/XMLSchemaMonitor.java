/**
*============================================================================
*  The Ohio State University Research Foundation, The University of Chicago -
*  Argonne National Laboratory, Emory University, SemanticBits LLC, 
*  and Ekagra Software Technologies Ltd.
*
*  Distributed under the OSI-approved BSD 3-Clause License.
*  See http://ncip.github.com/cagrid-core/LICENSE.txt for details.
*============================================================================
**/
/**
 *
 */
package gov.nih.nci.cagrid.portal.aggr.metachange;

import gov.nih.nci.cagrid.portal.aggr.TrackableMonitor;
import gov.nih.nci.cagrid.portal.domain.GridDataService;
import gov.nih.nci.cagrid.portal.domain.GridService;
import gov.nih.nci.cagrid.portal.domain.metadata.dataservice.DomainModel;
import gov.nih.nci.cagrid.portal.domain.metadata.dataservice.XMLSchema;
import gov.nih.nci.cagrid.portal.domain.metadata.service.*;
import gov.nih.nci.cagrid.portal.util.TimestampProvider;
import gov.nih.nci.cagrid.portal.util.XMLSchemaUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.orm.hibernate3.HibernateTemplate;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;

/**
 * @author <a href="mailto:joshua.phillips@semanticbits.com">Joshua Phillips</a>
 */
@Transactional
public class XMLSchemaMonitor implements TrackableMonitor {

    private HibernateTemplate hibernateTemplate;
    private static final Log logger = LogFactory
            .getLog(XMLSchemaMonitor.class);

    private TimestampProvider timestampProvider;
    private XMLSchemaUtils xmlSchemaUtils;

    /**
     *
     */
    public XMLSchemaMonitor() {

    }

    public void checkForXMLSchemas() {
        logger.debug("Checking for XML Schemas");
        List gridServices = getHibernateTemplate().find("from GridService");
        for (Iterator i = gridServices.iterator(); i.hasNext();) {
            GridService gridService = (GridService) i.next();
            if (gridService instanceof GridDataService) {
                List<String> namespaces = new ArrayList<String>();
                GridDataService dataService = (GridDataService) gridService;
                DomainModel domainModel = dataService.getDomainModel();
                for (XMLSchema xmlSchema : domainModel.getXmlSchemas()) {
                    namespaces.add(xmlSchema.getNamespace());
                }
                List<XMLSchema> xmlSchemas = xmlSchemaUtils.getXMLSchemas(
                        domainModel);
                for (XMLSchema xmlSchema : xmlSchemas) {
                    if (!namespaces.contains(xmlSchema.getNamespace())) {
                        getHibernateTemplate().save(xmlSchema);
                        domainModel.getXmlSchemas().add(xmlSchema);
                    }
                }
                getHibernateTemplate().save(domainModel);
            }
            List<ServiceContext> contexts = gridService.getServiceMetadata()
                    .getServiceDescription().getServiceContextCollection();
            for (ServiceContext context : contexts) {
                for (ContextProperty contextProperty : context
                        .getContextPropertyCollection()) {
                    if (contextProperty.getXmlSchema() == null) {
                        XMLSchema xmlSchema = xmlSchemaUtils.getXMLSchemaForQName(
                                getHibernateTemplate(), contextProperty
                                        .getName());
                        if (xmlSchema != null) {
                            if (xmlSchema.getId() == null) {
                                getHibernateTemplate().save(xmlSchema);
                            }
                            contextProperty.setXmlSchema(xmlSchema);
                            getHibernateTemplate().save(contextProperty);
                        }
                    }
                }
                for (Operation op : context.getOperationCollection()) {
                    for (InputParameter input : op.getInputParameterCollection()) {
                        if (input.getXmlSchema() == null) {
                            XMLSchema xmlSchema = xmlSchemaUtils.getXMLSchemaForQName(
                                    getHibernateTemplate(), input.getQName());
                            if (xmlSchema != null) {
                                if (xmlSchema.getId() == null) {
                                    getHibernateTemplate().save(xmlSchema);
                                }
                                input.setXmlSchema(xmlSchema);
                                getHibernateTemplate().save(input);
                            }
                        }
                    }

                    Output output = op.getOutput();
                    if (output != null && output.getXmlSchema() == null) {
                        XMLSchema xmlSchema = xmlSchemaUtils.getXMLSchemaForQName(
                                getHibernateTemplate(), output.getQName());
                        if (xmlSchema != null) {
                            if (xmlSchema.getId() == null) {
                                getHibernateTemplate().save(xmlSchema);
                            }
                            output.setXmlSchema(xmlSchema);
                            getHibernateTemplate().save(output);
                        }
                    }
                }
            }

        }
        logger.debug("Finished checking for XML Schemas");

        timestampProvider.createTimestamp();

    }

    public static void main(String[] args) {
        new XMLSchemaMonitor().checkForXMLSchemas();
    }

    public Date getLastExecutedOn() throws RuntimeException {
        return timestampProvider.getTimestamp();
    }

    public HibernateTemplate getHibernateTemplate() {
        return hibernateTemplate;
    }

    public void setHibernateTemplate(HibernateTemplate hibernateTemplate) {
        this.hibernateTemplate = hibernateTemplate;
    }

    public XMLSchemaUtils getXmlSchemaUtils() {
        return xmlSchemaUtils;
    }

    public void setXmlSchemaUtils(XMLSchemaUtils xmlSchemaUtils) {
        this.xmlSchemaUtils = xmlSchemaUtils;
    }

    public TimestampProvider getTimestampProvider() {
        return timestampProvider;
    }

    public void setTimestampProvider(TimestampProvider timestampProvider) {
        this.timestampProvider = timestampProvider;
    }
}
