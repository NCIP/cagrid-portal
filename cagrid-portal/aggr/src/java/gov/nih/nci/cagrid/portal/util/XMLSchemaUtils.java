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
package gov.nih.nci.cagrid.portal.util;

import gov.nih.nci.cadsr.umlproject.domain.Project;
import gov.nih.nci.cagrid.portal.domain.metadata.dataservice.DomainModel;
import gov.nih.nci.cagrid.portal.domain.metadata.dataservice.UMLClass;
import gov.nih.nci.cagrid.portal.domain.metadata.dataservice.XMLSchema;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.cagrid.gme.client.GlobalModelExchangeClient;
import org.cagrid.gme.domain.XMLSchemaNamespace;
import org.springframework.orm.hibernate3.HibernateTemplate;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * User: kherm
 *
 * @author kherm manav.kher@semanticbits.com
 */

public class XMLSchemaUtils {
    private String gmeUrl;
    private CaDSRClient caDSRClient;

    private static final Log logger = LogFactory.getLog(XMLSchemaUtils.class);


    public List<XMLSchema> getXMLSchemas(DomainModel domainModel) {
        List<XMLSchema> schemas = new ArrayList<XMLSchema>();

        Project proj = new Project();
        proj.setShortName(domainModel.getProjectShortName());
        proj.setVersion(domainModel.getProjectVersion());
        String context = "caBIG";
        try {
            context = caDSRClient.getContext(domainModel);
        } catch (Exception ex) {
            logger.warn("Coudn't get context from project '"
                    + proj.getShortName() + "': " + ex.getMessage()
                    + ". Using '" + context + "'.", ex);
        }
        String projectVersion = proj.getVersion();
        if (projectVersion == null) {
            projectVersion = "1.0";
        }
        if (projectVersion.indexOf(".") < 0) {
            projectVersion += ".0";
        }
        Set<String> packageNames = new HashSet<String>();
        for (UMLClass klass : domainModel.getClasses()) {
            packageNames.add(klass.getPackageName());
        }
        for (String packageName : packageNames) {
            String schemaUrl = "gme://" + proj.getShortName() + "." + context
                    + "/" + projectVersion + "/" + packageName;
            String schemaContents = getXmlSchemaContent(schemaUrl);
            if (schemaContents != null) {
                XMLSchema xmlSchema = new XMLSchema();
                xmlSchema.setNamespace(schemaUrl);
                xmlSchema.setSchemaContent(schemaContents);
                schemas.add(xmlSchema);
            }
        }
        return schemas;
    }

    public String getXmlSchemaContent(String namespace) {
        String content = null;
        try {
            XMLSchemaNamespace ns = new XMLSchemaNamespace(namespace);
            GlobalModelExchangeClient client = new GlobalModelExchangeClient(gmeUrl);
            org.cagrid.gme.domain.XMLSchema schema = client.getXMLSchema(ns);
            content = schema.getRootDocument().getSchemaText();
        } catch (Exception ex) {
            logger.info("Error getting XML schema with namespace '" +
                    namespace
                    + "': " + ex.getMessage());
        }
        return content;
    }

    public XMLSchema getXMLSchemaForQName(HibernateTemplate templ,
                                          String qName) {
        XMLSchema xmlSchema = null;
        try {
            int idx = qName.indexOf("{");
            if (idx != -1) {
                String namespace = qName.substring(idx + 1, qName.indexOf("}",
                        idx + 1));
                XMLSchema eg = new XMLSchema();
                eg.setNamespace(namespace);
                eg = (XMLSchema) PortalUtils.getByExample(templ, eg);
                if (eg == null) {
                    String content = getXmlSchemaContent(namespace);

                    if (content != null) {
                        xmlSchema = new XMLSchema();
                        xmlSchema.setNamespace(namespace);
                        xmlSchema.setSchemaContent(content);
                    }
                } else {
                    xmlSchema = eg;
                }
            }
        } catch (Exception ex) {
            logger.info("Couldn't get XMLSchema for QName '" + qName + "': "
                    + ex.getMessage());
        }
        if (xmlSchema != null) {
            XMLSchemaUtils.logger.debug("######### Found schema for QName: " + qName);
        }
        return xmlSchema;
    }

    public String getGmeUrl() {
        return gmeUrl;
    }

    public void setGmeUrl(String gmeUrl) {
        this.gmeUrl = gmeUrl;
    }

    public CaDSRClient getCaDSRClient() {
        return caDSRClient;
    }

    public void setCaDSRClient(CaDSRClient caDSRClient) {
        this.caDSRClient = caDSRClient;
    }
}