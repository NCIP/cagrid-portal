/**
 * 
 */
package gov.nih.nci.cagrid.portal.portlet.query.shared;

import gov.nih.nci.cagrid.portal.dao.CQLQueryDao;
import gov.nih.nci.cagrid.portal.dao.SharedCQLQueryDao;
import gov.nih.nci.cagrid.portal.domain.dataservice.CQLQuery;
import gov.nih.nci.cagrid.portal.domain.dataservice.SharedCQLQuery;
import gov.nih.nci.cagrid.portal.portlet.CaGridPortletApplicationException;
import gov.nih.nci.cagrid.portal.portlet.query.AbstractQueryRenderController;

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
public class ViewSharedQueryController extends AbstractQueryRenderController
		implements InitializingBean {

	private Resource xslResource;
	private Transformer xmlTransformer;
	private SharedCQLQueryDao sharedCqlQueryDao;

	/**
	 * 
	 */
	public ViewSharedQueryController() {

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see gov.nih.nci.cagrid.portal.portlet.AbstractViewObjectController#getObject(javax.portlet.RenderRequest)
	 */
	@Override
	protected Object getObject(RenderRequest request) {
		SharedQueryBean bean = getQueryModel().getWorkingSharedQuery();
		if (bean == null) {
			//Couldn't be null if was deleted before page refresh.
			return null;
		}
		SharedCQLQuery sharedQuery = bean.getQuery();
		if (sharedQuery == null) {
			//Should not be null if bean is not null, but just in case...
			return null;
		}
		sharedQuery = getSharedCqlQueryDao().getById(sharedQuery.getId());
		if (sharedQuery == null) {
			// Could be null if it was just now deleted.
			return null;
		}

		bean.setQuery(sharedQuery);
		try {
			bean.setPrettyXml(transformXML(sharedQuery.getCqlQuery().getXml()));
		} catch (Exception ex) {
			throw new CaGridPortletApplicationException(
					"Error transforming query: " + ex.getMessage(), ex);
		}

		return bean;
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

	public SharedCQLQueryDao getSharedCqlQueryDao() {
		return sharedCqlQueryDao;
	}

	public void setSharedCqlQueryDao(SharedCQLQueryDao sharedCqlQueryDao) {
		this.sharedCqlQueryDao = sharedCqlQueryDao;
	}
}
