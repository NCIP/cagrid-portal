/**
 * 
 */
package gov.nih.nci.cagrid.portal.aggr.regsvc;

import gov.nih.nci.cagrid.common.Utils;
import gov.nih.nci.cagrid.metadata.common.ResearchCenterDescription;
import gov.nih.nci.cagrid.portal.domain.AbstractDomainObject;
import gov.nih.nci.cagrid.portal.domain.Address;
import gov.nih.nci.cagrid.portal.domain.Person;
import gov.nih.nci.cagrid.portal.domain.metadata.ServiceMetadata;
import gov.nih.nci.cagrid.portal.domain.metadata.common.Enumeration;
import gov.nih.nci.cagrid.portal.domain.metadata.common.PointOfContact;
import gov.nih.nci.cagrid.portal.domain.metadata.common.ResearchCenter;
import gov.nih.nci.cagrid.portal.domain.metadata.common.ResearchCenterPointOfContact;
import gov.nih.nci.cagrid.portal.domain.metadata.common.SemanticMetadata;
import gov.nih.nci.cagrid.portal.domain.metadata.common.UMLAttribute;
import gov.nih.nci.cagrid.portal.domain.metadata.common.UMLClass;
import gov.nih.nci.cagrid.portal.domain.metadata.common.ValueDomain;
import gov.nih.nci.cagrid.portal.domain.metadata.dataservice.XMLSchema;
import gov.nih.nci.cagrid.portal.domain.metadata.service.CaDSRRegistration;
import gov.nih.nci.cagrid.portal.domain.metadata.service.ContextProperty;
import gov.nih.nci.cagrid.portal.domain.metadata.service.Fault;
import gov.nih.nci.cagrid.portal.domain.metadata.service.InputParameter;
import gov.nih.nci.cagrid.portal.domain.metadata.service.Operation;
import gov.nih.nci.cagrid.portal.domain.metadata.service.Output;
import gov.nih.nci.cagrid.portal.domain.metadata.service.Service;
import gov.nih.nci.cagrid.portal.domain.metadata.service.ServiceContext;
import gov.nih.nci.cagrid.portal.domain.metadata.service.ServicePointOfContact;
import gov.nih.nci.cagrid.portal.util.PortalUtils;

import java.io.File;
import java.io.FileReader;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.orm.hibernate3.HibernateTemplate;

/**
 * @author <a href="joshua.phillips@semanticbits.com">Joshua Phillips</a>
 * 
 */
public class ServiceMetadataBuilder {

	private static final Log logger = LogFactory
			.getLog(ServiceMetadataBuilder.class);

	private boolean persist;

	private HibernateTemplate hibernateTemplate;
	
	private String gmeUrl;

	public ServiceMetadata build(
			gov.nih.nci.cagrid.metadata.ServiceMetadata sMetaIn) {

		ServiceMetadata sMetaOut = new ServiceMetadata();

		try {

			if (sMetaIn.getHostingResearchCenter() != null) {

				gov.nih.nci.cagrid.metadata.common.ResearchCenter rCtrIn = sMetaIn
						.getHostingResearchCenter().getResearchCenter();
				if (rCtrIn != null) {
					ResearchCenter rCtr = buildResearchCenter(rCtrIn);
					sMetaOut.setHostingResearchCenter(rCtr);
					rCtr.setServiceMetadata(sMetaOut);
				}
			}

			if (sMetaIn.getServiceDescription() != null) {
				gov.nih.nci.cagrid.metadata.service.Service svcIn = sMetaIn
						.getServiceDescription().getService();
				if (svcIn != null) {
					Service svc = buildService(svcIn);
					sMetaOut.setServiceDescription(svc);
					svc.setServiceMetadata(sMetaOut);
				}
			}
			handlePersist(sMetaOut);

		} catch (Exception ex) {
			String msg = "Error building service metadata: " + ex.getMessage();
			logger.error(msg, ex);
			throw new RuntimeException(msg, ex);
		}
		return sMetaOut;
	}

	protected AbstractDomainObject handlePersist(AbstractDomainObject obj) {
		AbstractDomainObject result = obj;
		if (isPersist()) {
			getHibernateTemplate().saveOrUpdate(result);
		}

		return result;
	}

	protected Service buildService(
			gov.nih.nci.cagrid.metadata.service.Service svcIn) {

		Service svcOut = new Service();

		svcOut.setName(svcIn.getName());
		svcOut.setDescription(svcIn.getDescription());
		svcOut.setVersion(svcIn.getVersion());

		if (svcIn.getPointOfContactCollection() != null) {
			gov.nih.nci.cagrid.metadata.common.PointOfContact[] pocIns = svcIn
					.getPointOfContactCollection().getPointOfContact();
			if (pocIns != null) {
				for (gov.nih.nci.cagrid.metadata.common.PointOfContact pocIn : pocIns) {
					if (!PortalUtils.isEmpty(pocIn.getEmail())) {
						ServicePointOfContact pocOut = new ServicePointOfContact();
						pocOut.setServiceDescription(svcOut);
						pocOut = (ServicePointOfContact)buildPointOfContact(pocIn, pocOut);
						svcOut.getPointOfContactCollection().add(pocOut);
					}
				}
			}
		}
		if (svcIn.getSemanticMetadata() != null) {
			for (gov.nih.nci.cagrid.metadata.common.SemanticMetadata semMeta : svcIn
					.getSemanticMetadata()) {
				svcOut.getSemanticMetadata()
						.add(buildSemanticMetadata(semMeta));
			}
		}
		if (svcIn.getServiceContextCollection() != null) {
			for (gov.nih.nci.cagrid.metadata.service.ServiceContext svcCtxIn : svcIn
					.getServiceContextCollection().getServiceContext()) {
				ServiceContext svcCtxOut = buildServiceContext(svcCtxIn);
				svcCtxOut.setService(svcOut);
				svcOut.getServiceContextCollection().add(svcCtxOut);
			}
		}
		if (svcIn.getCaDSRRegistration() != null) {
			CaDSRRegistration regOut = buildCaDSRRegistration(svcIn
					.getCaDSRRegistration());
			regOut.setService(svcOut);
			svcOut.setCaDSRRegistration(regOut);
		}
		return (Service) handlePersist(svcOut);
	}

	protected CaDSRRegistration buildCaDSRRegistration(
			gov.nih.nci.cagrid.metadata.service.CaDSRRegistration regIn) {
		CaDSRRegistration regOut = new CaDSRRegistration();
		regOut.setRegistrationStatus(regIn.getRegistrationStatus());
		regOut.setWorkflowStatus(regIn.getWorkflowStatus());

		return (CaDSRRegistration) handlePersist(regOut);
	}

	protected ServiceContext buildServiceContext(
			gov.nih.nci.cagrid.metadata.service.ServiceContext svcCtxIn) {

		ServiceContext svcCtxOut = new ServiceContext();

		if (svcCtxIn.getContextPropertyCollection() != null) {
			gov.nih.nci.cagrid.metadata.service.ContextProperty[] ctxPropIns = svcCtxIn
					.getContextPropertyCollection().getContextProperty();
			if (ctxPropIns != null) {
				for (gov.nih.nci.cagrid.metadata.service.ContextProperty ctxPropIn : ctxPropIns) {
					ContextProperty ctxPropOut = buildContextProperty(ctxPropIn);
					ctxPropOut.setServiceContext(svcCtxOut);
					svcCtxOut.getContextPropertyCollection().add(ctxPropOut);
				}
			}
		}
		if (svcCtxIn.getOperationCollection() != null) {
			for (gov.nih.nci.cagrid.metadata.service.Operation operIn : svcCtxIn
					.getOperationCollection().getOperation()) {
				Operation operOut = buildOperation(operIn);
				operOut.setServiceContext(svcCtxOut);
				svcCtxOut.getOperationCollection().add(operOut);
			}
		}

		svcCtxOut.setDescription(svcCtxIn.getDescription());
		svcCtxOut.setName(svcCtxIn.getName());

		return (ServiceContext) handlePersist(svcCtxOut);
	}

	protected Operation buildOperation(
			gov.nih.nci.cagrid.metadata.service.Operation operIn) {
		Operation operOut = new Operation();
		operOut.setDescription(operIn.getDescription());
		operOut.setName(operIn.getName());

		if (operIn.getFaultCollection() != null) {
			gov.nih.nci.cagrid.metadata.service.Fault[] faultIns = operIn
					.getFaultCollection().getFault();
			if (faultIns != null) {
				for (gov.nih.nci.cagrid.metadata.service.Fault faultIn : faultIns) {
					Fault faultOut = buildFault(faultIn);
					faultOut.setOperation(operOut);
					operOut.getFaultCollection().add(faultOut);
				}
			}
		}

		if (operIn.getInputParameterCollection() != null) {
			gov.nih.nci.cagrid.metadata.service.InputParameter[] paramIns = operIn
					.getInputParameterCollection().getInputParameter();
			if (paramIns != null) {
				for (gov.nih.nci.cagrid.metadata.service.InputParameter paramIn : paramIns) {
					InputParameter paramOut = buildInputParameter(paramIn);
					paramOut.setOperation(operOut);
					operOut.getInputParameterCollection().add(paramOut);
				}
			}
		}

		if (operIn.getOutput() != null) {
			Output outputOut = buildOutput(operIn.getOutput());
			outputOut.setOperation(operOut);
			operOut.setOutput(outputOut);
		}

		if (operIn.getSemanticMetadata() != null) {
			for (gov.nih.nci.cagrid.metadata.common.SemanticMetadata sMetaIn : operIn
					.getSemanticMetadata()) {
				operOut.getSemanticMetadata().add(
						buildSemanticMetadata(sMetaIn));
			}
		}

		return (Operation) handlePersist(operOut);
	}

	protected Output buildOutput(
			gov.nih.nci.cagrid.metadata.service.Output outputIn) {
		Output outputOut = new Output();
		outputOut.setArray(outputIn.isIsArray());
		outputOut.setDimensionality(outputIn.getDimensionality());
		outputOut.setQName(outputIn.getQName().toString());
		outputOut.setXmlSchema(getXMLSchemaForQName(outputOut.getQName()));

		if (outputIn.getUMLClass() != null) {
			outputOut.setUMLClass(buildUMLClass(outputIn.getUMLClass()));
		}

		return (Output) handlePersist(outputOut);
	}
	
	protected XMLSchema getXMLSchemaForQName(String qName){
		XMLSchema xmlSchema = PortalUtils.getXMLSchemaForQName(getHibernateTemplate(), qName, getGmeUrl());
		if(xmlSchema != null && xmlSchema.getId() == null){
			xmlSchema = (XMLSchema)handlePersist(xmlSchema);
		}
		return xmlSchema;
	}
	
	

	protected InputParameter buildInputParameter(
			gov.nih.nci.cagrid.metadata.service.InputParameter paramIn) {
		InputParameter paramOut = new InputParameter();
		paramOut.setArray(paramIn.isIsArray());
		paramOut.setDimensionality(paramIn.getDimensionality());
		paramOut.setIndex(paramIn.getIndex());
		paramOut.setName(paramIn.getName());
		paramOut.setQName(paramIn.getQName().toString());
		paramOut.setRequired(paramIn.isIsRequired());
		paramOut.setXmlSchema(getXMLSchemaForQName(paramOut.getQName()));

		if (paramIn.getUMLClass() != null) {
			paramOut.setUMLClass(buildUMLClass(paramIn.getUMLClass()));
		}

		return (InputParameter) handlePersist(paramOut);
	}

	protected UMLClass buildUMLClass(
			gov.nih.nci.cagrid.metadata.common.UMLClass umlClassIn) {
		UMLClass umlClassOut = new UMLClass();
		if (umlClassIn.getSemanticMetadata() != null) {
			for (gov.nih.nci.cagrid.metadata.common.SemanticMetadata sMetaIn : umlClassIn
					.getSemanticMetadata()) {
				umlClassOut.getSemanticMetadata().add(
						buildSemanticMetadata(sMetaIn));
			}
		}
		if (umlClassIn.getUmlAttributeCollection() != null) {
			for (gov.nih.nci.cagrid.metadata.common.UMLAttribute umlAttrIn : umlClassIn
					.getUmlAttributeCollection().getUMLAttribute()) {
				UMLAttribute umlAttrOut = buildUMLAttribute(umlAttrIn);
				umlAttrOut.setUmlClass(umlClassOut);
				umlClassOut.getUmlAttributeCollection().add(umlAttrOut);
			}
		}
		umlClassOut.setCadsrId(umlClassIn.getId());
		umlClassOut.setClassName(umlClassIn.getClassName());
		umlClassOut.setDescription(umlClassIn.getDescription());
		umlClassOut.setPackageName(umlClassIn.getPackageName());
		umlClassOut.setProjectName(umlClassIn.getProjectName());
		umlClassOut.setProjectVersion(umlClassIn.getProjectVersion());

		return (UMLClass) handlePersist(umlClassOut);
	}

	protected UMLAttribute buildUMLAttribute(
			gov.nih.nci.cagrid.metadata.common.UMLAttribute umlAttrIn) {
		UMLAttribute umlAttrOut = new UMLAttribute();
		if (umlAttrIn.getSemanticMetadata() != null) {
			for (gov.nih.nci.cagrid.metadata.common.SemanticMetadata sMetaIn : umlAttrIn
					.getSemanticMetadata()) {
				umlAttrOut.getSemanticMetadata().add(
						buildSemanticMetadata(sMetaIn));
			}
		}
		if (umlAttrIn.getValueDomain() != null) {
			umlAttrOut.setValueDomain(buildValueDomain(umlAttrIn
					.getValueDomain()));
		}
		umlAttrOut.setDataTypeName(umlAttrIn.getDataTypeName());
		umlAttrOut.setDescription(umlAttrIn.getDescription());
		umlAttrOut.setVersion(umlAttrIn.getVersion());
		umlAttrOut.setName(umlAttrIn.getName());

		return (UMLAttribute) handlePersist(umlAttrOut);
	}

	protected ValueDomain buildValueDomain(
			gov.nih.nci.cagrid.metadata.common.ValueDomain valDomIn) {
		ValueDomain valDomOut = new ValueDomain();

		valDomOut.setLongName(valDomIn.getLongName());
		valDomOut.setUnitOfMeasure(valDomIn.getUnitOfMeasure());

		if (valDomIn.getEnumerationCollection() != null) {
			gov.nih.nci.cagrid.metadata.common.Enumeration[] enumIns = valDomIn
					.getEnumerationCollection().getEnumeration();
			if (enumIns != null) {
				for (gov.nih.nci.cagrid.metadata.common.Enumeration enumIn : enumIns) {
					Enumeration enumOut = buildEnumeration(enumIn);
					enumOut.setValueDomain(valDomOut);
					valDomOut.getEnumerationCollection().add(enumOut);
				}
			}
		}
		if (valDomIn.getSemanticMetadata() != null) {
			gov.nih.nci.cagrid.metadata.common.SemanticMetadata[] sMetaIns = valDomIn
					.getSemanticMetadata();
			if (sMetaIns != null) {
				for (gov.nih.nci.cagrid.metadata.common.SemanticMetadata sMetaIn : sMetaIns) {
					valDomOut.getSemanticMetadata().add(
							buildSemanticMetadata(sMetaIn));
				}
			}
		}

		return (ValueDomain) handlePersist(valDomOut);
	}

	protected Enumeration buildEnumeration(
			gov.nih.nci.cagrid.metadata.common.Enumeration enumIn) {
		Enumeration enumOut = new Enumeration();
		enumOut.setPermissibleValue(enumIn.getPermissibleValue());
		enumOut.setValueMeaning(enumIn.getValueMeaning());
		if (enumOut.getSemanticMetadata() != null) {
			gov.nih.nci.cagrid.metadata.common.SemanticMetadata[] sMetaIns = enumIn
					.getSemanticMetadata();
			if (sMetaIns != null) {
				for (gov.nih.nci.cagrid.metadata.common.SemanticMetadata sMetaIn : sMetaIns) {
					enumOut.getSemanticMetadata().add(
							buildSemanticMetadata(sMetaIn));
				}
			}
		}
		return (Enumeration) handlePersist(enumOut);
	}

	protected Fault buildFault(gov.nih.nci.cagrid.metadata.service.Fault faultIn) {
		Fault faultOut = new Fault();
		faultOut.setDescription(faultIn.getDescription());
		faultOut.setName(faultIn.getName());
		handlePersist(faultOut);
		return faultOut;
	}

	protected ContextProperty buildContextProperty(
			gov.nih.nci.cagrid.metadata.service.ContextProperty ctxPropIn) {

		ContextProperty ctxPropOut = new ContextProperty();
		ctxPropOut.setDescription(ctxPropIn.getDescription());
		ctxPropOut.setName(ctxPropIn.getName());
		ctxPropOut.setXmlSchema(getXMLSchemaForQName(ctxPropOut.getName()));
		return (ContextProperty) handlePersist(ctxPropOut);
	}

	protected SemanticMetadata buildSemanticMetadata(
			gov.nih.nci.cagrid.metadata.common.SemanticMetadata semMetaIn) {
		SemanticMetadata semMetaOut = new SemanticMetadata();

		semMetaOut.setConceptCode(semMetaIn.getConceptCode());
		semMetaOut.setConceptDefinition(semMetaIn.getConceptDefinition());
		semMetaOut.setConceptName(semMetaIn.getConceptName());
		semMetaOut.setOder(semMetaIn.getOrder());
		semMetaOut.setOrderLevel(semMetaIn.getOrderLevel());

		return (SemanticMetadata) handlePersist(semMetaOut);
	}

	protected PointOfContact buildPointOfContact(
			gov.nih.nci.cagrid.metadata.common.PointOfContact pocIn, PointOfContact pocOut) {

		Person person = new Person();
		person.setFirstName(pocIn.getFirstName());
		person.setLastName(pocIn.getLastName());
		person.setPhoneNumber(pocIn.getPhoneNumber());
		person.setEmailAddress(pocIn.getEmail());
		Person existing = null;
		try {
			existing = (Person) PortalUtils.getByExample(
					getHibernateTemplate(), person);
		} catch (Exception ex) {
			logger.warn(ex);
		}
		if (existing != null) {
			person = existing;
		}
		person = (Person) handlePersist(person);

		pocOut.setAffiliation(pocIn.getAffiliation());
		pocOut.setRole(pocIn.getRole());
		pocOut.setPerson(person);

		return (PointOfContact) handlePersist(pocOut);
	}

	protected ResearchCenter buildResearchCenter(
			gov.nih.nci.cagrid.metadata.common.ResearchCenter rCtrIn) {
		ResearchCenter rCtrOut = new ResearchCenter();

		if (rCtrIn.getAddress() != null) {
			rCtrOut.setAddress(buildAddress(rCtrIn.getAddress()));
		}
		ResearchCenterDescription desc = rCtrIn.getResearchCenterDescription();
		if (desc != null) {
			rCtrOut.setDescription(desc.getDescription());
			rCtrOut.setHomepageUrl(desc.getHomepageURL());
			rCtrOut.setRssNewsUrl(desc.getRssNewsURL());
		}
		rCtrOut.setDisplayName(rCtrIn.getDisplayName());
		rCtrOut.setShortName(rCtrIn.getShortName());
		if (rCtrIn.getPointOfContactCollection() != null) {
			gov.nih.nci.cagrid.metadata.common.PointOfContact[] pocIns = rCtrIn
					.getPointOfContactCollection().getPointOfContact();
			if (pocIns != null) {
				for (gov.nih.nci.cagrid.metadata.common.PointOfContact pocIn : pocIns) {
					if (!PortalUtils.isEmpty(pocIn.getEmail())) {
						ResearchCenterPointOfContact pocOut = new ResearchCenterPointOfContact();
						pocOut.setResearchCenter(rCtrOut);
						pocOut = (ResearchCenterPointOfContact) buildPointOfContact(pocIn, pocOut);
						rCtrOut.getPointOfContactCollection().add(pocOut);
					}
				}
			}
		}
		return (ResearchCenter) handlePersist(rCtrOut);
	}

	protected Address buildAddress(
			gov.nih.nci.cagrid.metadata.common.Address addrIn) {
		Address addrOut = new Address();
		addrOut.setCountry(addrIn.getCountry());
		addrOut.setLocality(addrIn.getLocality());
		addrOut.setPostalCode(addrIn.getPostalCode());
		addrOut.setStateProvince(addrIn.getStateProvince());
		addrOut.setStreet1(addrIn.getStreet1());
		addrOut.setStreet2(addrIn.getStreet2());

		Address existing = null;
		try {
			existing = (Address) PortalUtils.getByExample(
					getHibernateTemplate(), addrOut);
		} catch (Exception ex) {
			logger.warn(ex);
		}
		if (existing != null) {
			addrOut = existing;
		}

		return (Address) handlePersist(addrOut);
	}

	public static void main(String[] args) throws Exception {

		ServiceMetadataBuilder builder = new ServiceMetadataBuilder();
		File dir = new File("common/test/resources/serviceMetadata");
		File[] files = dir.listFiles();
		for (File file : files) {
			gov.nih.nci.cagrid.metadata.ServiceMetadata svcMetaIn = (gov.nih.nci.cagrid.metadata.ServiceMetadata) Utils
					.deserializeObject(new FileReader(file),
							gov.nih.nci.cagrid.metadata.ServiceMetadata.class);
			ServiceMetadata svcMetaOut = builder.build(svcMetaIn);
		}

	}

	public boolean isPersist() {
		return persist;
	}

	public void setPersist(boolean persist) {
		this.persist = persist;
	}

	public HibernateTemplate getHibernateTemplate() {
		return hibernateTemplate;
	}

	public void setHibernateTemplate(HibernateTemplate hibernateTemplate) {
		this.hibernateTemplate = hibernateTemplate;
	}

	public String getGmeUrl() {
		return gmeUrl;
	}

	public void setGmeUrl(String gmeUrl) {
		this.gmeUrl = gmeUrl;
	}

}
