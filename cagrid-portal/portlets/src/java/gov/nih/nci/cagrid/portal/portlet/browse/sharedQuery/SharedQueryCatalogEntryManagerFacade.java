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
/**
 *
 */
package gov.nih.nci.cagrid.portal.portlet.browse.sharedQuery;

import gov.nih.nci.cagrid.common.SchemaValidationException;
import gov.nih.nci.cagrid.common.SchemaValidator;
import gov.nih.nci.cagrid.common.Utils;
import gov.nih.nci.cagrid.portal.dao.QueryDao;
import gov.nih.nci.cagrid.portal.dao.catalog.GridServiceEndPointCatalogEntryDao;
import gov.nih.nci.cagrid.portal.dao.catalog.SharedQueryCatalogEntryDao;
import gov.nih.nci.cagrid.portal.domain.catalog.CatalogEntry;
import gov.nih.nci.cagrid.portal.domain.catalog.GridServiceEndPointCatalogEntry;
import gov.nih.nci.cagrid.portal.domain.catalog.SharedQueryCatalogEntry;
import gov.nih.nci.cagrid.portal.domain.dataservice.CQLQuery;
import gov.nih.nci.cagrid.portal.domain.dataservice.DCQLQuery;
import gov.nih.nci.cagrid.portal.domain.dataservice.Query;
import gov.nih.nci.cagrid.portal.portlet.browse.GridServiceEndpointDescriptorBean;
import gov.nih.nci.cagrid.portal.portlet.browse.ajax.ToolCatalogEntryManagerFacade;
import gov.nih.nci.cagrid.portal.portlet.util.PortletUtils;
import gov.nih.nci.cagrid.portal.util.PortalUtils;
import org.apache.axis.utils.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Required;
import org.springframework.transaction.annotation.Transactional;

import java.io.StringReader;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author <a href="mailto:joshua.phillips@semanticbits.com">Joshua Phillips</a>
 * @author kherm manav.kher@semanticbits.com
 */
@Transactional
public class SharedQueryCatalogEntryManagerFacade extends
        ToolCatalogEntryManagerFacade {

    private int maxActiveQueries;
    private GridServiceEndPointCatalogEntryDao gridServiceEndPointCatalogEntryDao;
    private String queryXML;
    private SharedQueryCatalogEntryDao sharedQueryCatalogEntryDao;
    private QueryDao queryDao;
    private String selectEndpointsFormContentViewName;
    String cqlSchema, dcqlSchema;


    private static final Log logger = LogFactory
            .getLog(SharedQueryCatalogEntryManagerFacade.class);

    /**
     *
     */
    public SharedQueryCatalogEntryManagerFacade() {
    }

    public String setQuery(String queryXML) {
        this.queryXML = queryXML.trim();
        return null;

    }

    /**
     * for autocompleterer. Will only get services that are associated with CE's
     *
     * @param partialUrl
     * @return
     */
    public List<GridServiceEndpointDescriptorBean> getCatalogEntriesForPartialUrl(
            String partialUrl) {
        List<GridServiceEndpointDescriptorBean> result = new ArrayList<GridServiceEndpointDescriptorBean>();
        for (GridServiceEndPointCatalogEntry entry : gridServiceEndPointCatalogEntryDao
                .getByPartialUrl(partialUrl)) {
            GridServiceEndpointDescriptorBean descriptor = new GridServiceEndpointDescriptorBean(
                    String.valueOf(entry.getId()), String.valueOf(entry
                            .getAbout().getId()), entry.getAbout().getUrl());
            result.add(descriptor);
        }
        return result;
    }

    public List<GridServiceEndpointDescriptorBean> getAppropriateEndpointsForPartialUrl(
            String xml, String partialUrl) {
        List<GridServiceEndpointDescriptorBean> result = new ArrayList<GridServiceEndpointDescriptorBean>();
        if (StringUtils.isEmpty(xml)) {
            logger.debug("xml is null, ignoring request");
        } else {
            List<GridServiceEndPointCatalogEntry> endpointCes = null;
            if (PortletUtils.isCQL(xml)) {
                logger.debug("this is a CQL query");
                String umlClassName = PortletUtils.getTargetUMLClassName(xml);
                logger.debug("UMLClass: " + umlClassName);
                int idx = umlClassName.lastIndexOf(".");
                String packageName = umlClassName.substring(0, idx);
                String className = umlClassName.substring(idx + 1);
                endpointCes = getGridServiceEndPointCatalogEntryDao()
                        .getByUmlClassNameAndPartialUrl(packageName, className,
                                partialUrl);
            } else {
                logger.debug("this is a DCQL query");
                endpointCes = getGridServiceEndPointCatalogEntryDao()
                        .getByPartialUrl(partialUrl);
            }
            for (GridServiceEndPointCatalogEntry entry : endpointCes) {
                GridServiceEndpointDescriptorBean descriptor = new GridServiceEndpointDescriptorBean(
                        String.valueOf(entry.getId()), String.valueOf(entry
                                .getAbout().getId()), entry.getAbout().getUrl());
                result.add(descriptor);
            }
        }
        return result;
    }

    @Override
    public String validate() {
        String message = null;
        /** must validate as CQL or DCQL **/
        try {
            URL schemaPath = this.getClass().getClassLoader().getResource(getCqlSchema());
            SchemaValidator validator = new SchemaValidator(schemaPath.getFile());
            validator.validate(this.queryXML);
        } catch (SchemaValidationException e) {
            try {
                URL dcqlSchema = this.getClass().getClassLoader().getResource(getDcqlSchema());
                SchemaValidator validator = new SchemaValidator(dcqlSchema.getFile());
                validator.validate(this.queryXML);
            } catch (SchemaValidationException e1) {
                logger.warn("Saving invalid query " + e1.getMessage());
                message = "The query is invalid. Please check the syntax.";
            }
        }
        return message;
    }

    public String getTargetClass() {
        String queryXML = ((SharedQueryCatalogEntry) getUserModel()
                .getCurrentCatalogEntry()).getAbout().getXml();

        StringReader reader = new StringReader(queryXML);
        try {
            gov.nih.nci.cagrid.cqlquery.CQLQuery query = (gov.nih.nci.cagrid.cqlquery.CQLQuery) Utils
                    .deserializeObject(reader,
                            gov.nih.nci.cagrid.cqlquery.CQLQuery.class);
            return query.getTarget().getName();
        } catch (Exception e) {
            return "Could not be determined";
        }

    }

    public String renderAvailableEndpointsFormContent(String cql, String ns) {

        Map<String, Object> reqAttributes = new HashMap<String, Object>();
        reqAttributes.put("ns", ns);

        try {

            reqAttributes.put("endpoints", getAvailableEndpoints(cql));

            return getView(getSelectEndpointsFormContentViewName(),
                    reqAttributes);
        } catch (Exception ex) {
            String msg = "Error rendering select endpoints form content: "
                    + ex.getMessage();
            logger.error(msg, ex);
            throw new RuntimeException(msg, ex);
        }
    }

    public List<GridServiceEndPointCatalogEntry> getAvailableEndpoints(String cql) {
        List<GridServiceEndPointCatalogEntry> endpointCes = null;
        if (PortletUtils.isCQL(cql)) {

            String umlClassName = PortletUtils.getTargetUMLClassName(cql);
            int idx = umlClassName.lastIndexOf(".");
            String packageName = umlClassName.substring(0, idx);
            String className = umlClassName.substring(idx + 1);
            endpointCes = getGridServiceEndPointCatalogEntryDao()
                    .getByUmlClassNameAndPartialUrl(packageName, className,
                            "%");
        } else {
            endpointCes = getGridServiceEndPointCatalogEntryDao()
                    .getByPartialUrl("FederatedQueryProcessor");
        }
        return endpointCes;
    }

    @Override
    protected Integer saveInternal(CatalogEntry catalogEntry) {

        SharedQueryCatalogEntry sqCe = (SharedQueryCatalogEntry) catalogEntry;

        if (this.queryXML != null) {

            String xml = null;
            if (PortletUtils.isCQL(queryXML)) {
                xml = PortletUtils.normalizeCQL(queryXML);
            } else {
                xml = PortletUtils.normalizeDCQL(queryXML);
            }
            String hash = PortalUtils.createHash(xml);

            Query aboutQuery = sqCe.getAbout();
            if (aboutQuery == null) {

                aboutQuery = getQueryDao().getQueryByHash(hash);

                if (aboutQuery == null) {

                    logger.debug("Will create a new query");
                    if (PortletUtils.isCQL(queryXML)) {
                        aboutQuery = new CQLQuery();
                    } else {
                        aboutQuery = new DCQLQuery();
                    }
                }

                sqCe.setAbout(aboutQuery);
            } else {
                aboutQuery = getQueryDao().getById(aboutQuery.getId());
            }

            aboutQuery.setHash(hash);
            aboutQuery.setXml(xml);
            getQueryDao().save(aboutQuery);
            getSharedQueryCatalogEntryDao().save(sqCe);
        }

        return sqCe.getId();
    }


    public String getCqlSchema() {
        return cqlSchema;
    }

    public void setCqlSchema(String cqlSchema) {
        this.cqlSchema = cqlSchema;
    }

    public String getDcqlSchema() {
        return dcqlSchema;
    }

    public void setDcqlSchema(String dcqlSchema) {
        this.dcqlSchema = dcqlSchema;
    }

    public GridServiceEndPointCatalogEntryDao getGridServiceEndPointCatalogEntryDao() {
        return gridServiceEndPointCatalogEntryDao;
    }

    @Required
    public void setGridServiceEndPointCatalogEntryDao(
            GridServiceEndPointCatalogEntryDao gridServiceEndPointCatalogEntryDao) {
        this.gridServiceEndPointCatalogEntryDao = gridServiceEndPointCatalogEntryDao;
    }

    public SharedQueryCatalogEntryDao getSharedQueryCatalogEntryDao() {
        return sharedQueryCatalogEntryDao;
    }

    @Required
    public void setSharedQueryCatalogEntryDao(
            SharedQueryCatalogEntryDao sharedQueryCatalogEntryDao) {
        this.sharedQueryCatalogEntryDao = sharedQueryCatalogEntryDao;
    }

    public QueryDao getQueryDao() {
        return queryDao;
    }

    @Required
    public void setQueryDao(QueryDao queryDao) {
        this.queryDao = queryDao;
    }

    public String getSelectEndpointsFormContentViewName() {
        return selectEndpointsFormContentViewName;
    }

    public void setSelectEndpointsFormContentViewName(
            String selectEndpointsFormContentViewName) {
        this.selectEndpointsFormContentViewName = selectEndpointsFormContentViewName;
    }

    public int getMaxActiveQueries() {
        return maxActiveQueries;
    }


    @Required
    public void setMaxActiveQueries(int maxActiveQueries) {
        this.maxActiveQueries = maxActiveQueries;
    }
}
