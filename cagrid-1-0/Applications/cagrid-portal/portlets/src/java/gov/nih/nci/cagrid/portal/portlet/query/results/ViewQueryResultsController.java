/**
 *
 */
package gov.nih.nci.cagrid.portal.portlet.query.results;

import gov.nih.nci.cagrid.portal.domain.dataservice.QueryInstance;
import gov.nih.nci.cagrid.portal.portlet.CaGridPortletApplicationException;
import gov.nih.nci.cagrid.portal.portlet.query.AbstractQueryRenderController;
import gov.nih.nci.cagrid.portal.portlet.query.cql.CQLQueryInstanceResultsBean;
import gov.nih.nci.cagrid.portal.portlet.util.PortletUtils;
import gov.nih.nci.cagrid.portal.portlet.util.Table;
import gov.nih.nci.cagrid.portal.portlet.util.TableScroller;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Required;
import org.springframework.core.io.Resource;

import javax.portlet.RenderRequest;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.stream.StreamResult;
import javax.xml.transform.stream.StreamSource;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;

/**
 * @author <a href="mailto:joshua.phillips@semanticbits.com">Joshua Phillips</a>
 */
public class ViewQueryResultsController extends AbstractQueryRenderController
        implements InitializingBean {

    private Resource xslResource;
    private Transformer xmlTransformer;
    private String resultsBeanSessionAttributeName;

    /**
     *
     */
    public ViewQueryResultsController() {

    }

    /*
      * (non-Javadoc)
      *
      * @see gov.nih.nci.cagrid.portal.portlet.AbstractViewObjectController#getObject(javax.portlet.RenderRequest)
      */
    @Override
    protected Object getObject(RenderRequest request) {
        QueryInstance instance = getQueryModel().getSelectedQueryInstance();
        CQLQueryInstanceResultsBean command = (CQLQueryInstanceResultsBean) request
                .getPortletSession().getAttribute(
                getResultsBeanSessionAttributeName());
        if (command != null && instance != null && command.getInstance() != null
                && command.getInstance().getId().equals(instance.getId())) {
            // Don't create new one
        } else {
            command = new CQLQueryInstanceResultsBean();
            if (instance != null) {
                String xml = instance.getResult();
                if (xml != null) {
                    Table table = null;
                    try {
                        table = PortletUtils
                                .buildTableFromCQLResults(new ByteArrayInputStream(
                                        xml.getBytes()));
                    } catch (Exception ex) {
                        throw new CaGridPortletApplicationException(
                                "Error build table from XML results: "
                                        + ex.getMessage(), ex);
                    }
                    if (table != null) {
                        command.setTableScroller(new TableScroller(table, 10));
                    }
                    String pretty;
                    try {
                        pretty = transformXML(xml);
                    } catch (TransformerException ex) {
                        throw new CaGridPortletApplicationException(
                                "Error transforming XML results: "
                                        + ex.getMessage(), ex);
                    }
                    command.setPrettyXml(pretty);
                }
                command.setInstance(instance);
            }
            request.getPortletSession().setAttribute(
                    getResultsBeanSessionAttributeName(), command);
        }
        return command;
    }

    private String transformXML(String xml) throws TransformerException {
        ByteArrayOutputStream buf = new ByteArrayOutputStream();
        xmlTransformer.transform(new StreamSource(new ByteArrayInputStream(xml
                .getBytes())), new StreamResult(buf));
        return buf.toString();
    }

    @Required
    public Resource getXslResource() {
        return xslResource;
    }

    public void setXslResource(Resource xslResource) {
        this.xslResource = xslResource;
    }

    public void afterPropertiesSet() throws Exception {
        if (getXslResource() == null) {
            throw new IllegalArgumentException(
                    "The xslResource property is required.");
        }

        this.xmlTransformer = TransformerFactory.newInstance().newTransformer(
                new StreamSource(getXslResource().getInputStream()));

    }

    @Required
    public String getResultsBeanSessionAttributeName() {
        return resultsBeanSessionAttributeName;
    }

    public void setResultsBeanSessionAttributeName(
            String resultsBeanSessionAttributeName) {
        this.resultsBeanSessionAttributeName = resultsBeanSessionAttributeName;
    }

}
