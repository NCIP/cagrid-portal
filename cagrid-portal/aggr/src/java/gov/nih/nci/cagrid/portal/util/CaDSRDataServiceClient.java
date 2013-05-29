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
package gov.nih.nci.cagrid.portal.util;

import gov.nih.nci.cadsr.domain.ClassificationScheme;
import gov.nih.nci.cadsr.domain.Context;
import gov.nih.nci.cadsr.umlproject.domain.Project;
import gov.nih.nci.cagrid.common.Utils;
import gov.nih.nci.cagrid.cqlquery.*;
import gov.nih.nci.cagrid.cqlquery.Object;
import gov.nih.nci.cagrid.cqlresultset.CQLQueryResults;
import gov.nih.nci.cagrid.data.DataServiceConstants;
import gov.nih.nci.cagrid.data.client.DataServiceClient;
import gov.nih.nci.cagrid.data.utilities.CQLQueryResultsIterator;
import gov.nih.nci.cagrid.portal.PortalSystemException;
import gov.nih.nci.cagrid.portal.domain.metadata.dataservice.DomainModel;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import java.io.StringWriter;
import java.util.Iterator;

/**
 * User: kherm
 *
 * @author kherm manav.kher@semanticbits.com
 */
public class CaDSRDataServiceClient implements CaDSRClient {
    private String cadsrUrl;

    private Log logger = LogFactory.getLog(getClass().getName());

    public String getContext(DomainModel domainModel) throws Exception {

        //looking for a specific project
        Association projAssc = new Association();
        projAssc.setName(Project.class.getName());
        Attribute nameAttr = new Attribute();
        nameAttr.setName("shortName");
        nameAttr.setPredicate(Predicate.EQUAL_TO);
        nameAttr.setValue(domainModel.getProjectShortName());
        Attribute valueAttr = new Attribute();
        valueAttr.setName("version");
        valueAttr.setPredicate(Predicate.EQUAL_TO);
        valueAttr.setValue(domainModel.getProjectVersion());
        projAssc.setAttribute(nameAttr);
        projAssc.setAttribute(valueAttr);

        //project belongs to CS
        Association csAssc = new Association();
        csAssc.setName(ClassificationScheme.class.getName());
        csAssc.setAssociation(projAssc);

        //Search for the context for this CS
        CQLQuery query = new CQLQuery();
        gov.nih.nci.cagrid.cqlquery.Object target = new Object();
        target.setName(Context.class.getName());
        target.setAssociation(csAssc);
        query.setTarget(target);


        //Search cadsr data service
        DataServiceClient cadsrService = new DataServiceClient(cadsrUrl);
        logger.debug("Searching caDSR data service for Context");

        StringWriter cqlWriter = new StringWriter();
        Utils.serializeObject(query, DataServiceConstants.CQL_QUERY_QNAME, cqlWriter);


        CQLQueryResults results = cadsrService.query(query);

        Iterator iter = new CQLQueryResultsIterator(results, this.getClass().getResourceAsStream("client-config.wsdd"));
        if (iter.hasNext()) {
            Context ctx = (Context) iter.next();
            return ctx.getName();
        } else
            throw new PortalSystemException("Negative result count found");

    }


    public String getCadsrUrl() {
        return cadsrUrl;
    }

    public void setCadsrUrl(String cadsrUrl) {
        this.cadsrUrl = cadsrUrl;
    }
}
