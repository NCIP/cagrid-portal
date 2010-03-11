/**
 * 
 */
package gov.nih.nci.cagrid.portal.portlet.discovery.details;

import gov.nih.nci.cagrid.portal.portlet.CaGridPortletApplicationException;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;

import javax.portlet.RenderRequest;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.stream.StreamResult;
import javax.xml.transform.stream.StreamSource;

import org.springframework.beans.factory.InitializingBean;
import org.springframework.core.io.Resource;

/**
 * @author <a href="mailto:joshua.phillips@semanticbits.com">Joshua Phillips</a>
 * 
 */
public class ViewXmlSchemaController extends
		AbstractDiscoveryViewObjectController implements InitializingBean {

	private Resource xslResource;
	private Transformer xmlTransformer;

	/**
	 * 
	 */
	public ViewXmlSchemaController() {

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see gov.nih.nci.cagrid.portal.portlet.AbstractViewObjectController#getObject(javax.portlet.RenderRequest)
	 */
	@Override
	protected Object getObject(RenderRequest request) {
		String xml = null;
		try {
			xml = transformXML(getDiscoveryModel().getSelectedXmlSchema()
					.getSchemaContent());
		} catch (Exception ex) {
			throw new CaGridPortletApplicationException(
					"Error transforming schema: " + ex.getMessage(), ex);
		}
		return xml;
	}
	
	

	public void afterPropertiesSet() throws Exception {
		if (getXslResource() == null) {
			throw new IllegalArgumentException(
					"The xslResource property is required.");
		}

		this.xmlTransformer = TransformerFactory.newInstance().newTransformer(
				new StreamSource(getXslResource().getInputStream()));

	}

	private String transformXML(String xml) throws TransformerException {
		ByteArrayOutputStream buf = new ByteArrayOutputStream();
		xmlTransformer.transform(new StreamSource(new ByteArrayInputStream(xml
				.getBytes())), new StreamResult(buf));
		return buf.toString();
	}

	public Resource getXslResource() {
		return xslResource;
	}

	public void setXslResource(Resource xslResource) {
		this.xslResource = xslResource;
	}

}
