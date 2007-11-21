/**
 * 
 */
package gov.nih.nci.cagrid.portal.aggr.metachange;

import gov.nih.nci.cagrid.portal.domain.GridDataService;
import gov.nih.nci.cagrid.portal.domain.GridService;
import gov.nih.nci.cagrid.portal.domain.metadata.ServiceMetadata;
import gov.nih.nci.cagrid.portal.domain.metadata.dataservice.DomainModel;
import gov.nih.nci.cagrid.portal.domain.metadata.dataservice.XMLSchema;
import gov.nih.nci.cagrid.portal.domain.metadata.service.ContextProperty;
import gov.nih.nci.cagrid.portal.domain.metadata.service.InputParameter;
import gov.nih.nci.cagrid.portal.domain.metadata.service.Operation;
import gov.nih.nci.cagrid.portal.domain.metadata.service.Output;
import gov.nih.nci.cagrid.portal.domain.metadata.service.ServiceContext;
import gov.nih.nci.cagrid.portal.util.PortalUtils;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.springframework.orm.hibernate3.HibernateTemplate;
import org.springframework.transaction.annotation.Transactional;

/**
 * @author <a href="mailto:joshua.phillips@semanticbits.com">Joshua Phillips</a>
 * 
 */
@Transactional
public class XMLSchemaMonitor {

	private HibernateTemplate hibernateTemplate;
	private String cadsrUrl;
	private String gmeUrl;

	/**
	 * 
	 */
	public XMLSchemaMonitor() {

	}

	public void checkForXMLSchemas() {

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
				List<XMLSchema> xmlSchemas = PortalUtils.getXMLSchemas(
						domainModel, getCadsrUrl(), getGmeUrl());
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
						XMLSchema xmlSchema = PortalUtils.getXMLSchemaForQName(
								getHibernateTemplate(), contextProperty
										.getName(), getGmeUrl());
						if (xmlSchema != null) {
							if (xmlSchema.getId() == null) {
								getHibernateTemplate().save(xmlSchema);
							}
							contextProperty.setXmlSchema(xmlSchema);
							getHibernateTemplate().save(contextProperty);
						}
					}
				}
				for(Operation op : context.getOperationCollection()){
					for(InputParameter input : op.getInputParameterCollection()){
						if(input.getXmlSchema() == null){
							XMLSchema xmlSchema = PortalUtils.getXMLSchemaForQName(
									getHibernateTemplate(), input.getQName(), getGmeUrl());
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
					if(output != null && output.getXmlSchema() == null){
						XMLSchema xmlSchema = PortalUtils.getXMLSchemaForQName(
								getHibernateTemplate(), output.getQName(), getGmeUrl());
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

	}

	public HibernateTemplate getHibernateTemplate() {
		return hibernateTemplate;
	}

	public void setHibernateTemplate(HibernateTemplate hibernateTemplate) {
		this.hibernateTemplate = hibernateTemplate;
	}

	public String getCadsrUrl() {
		return cadsrUrl;
	}

	public void setCadsrUrl(String cadsrUrl) {
		this.cadsrUrl = cadsrUrl;
	}

	public String getGmeUrl() {
		return gmeUrl;
	}

	public void setGmeUrl(String gmeUrl) {
		this.gmeUrl = gmeUrl;
	}

}
