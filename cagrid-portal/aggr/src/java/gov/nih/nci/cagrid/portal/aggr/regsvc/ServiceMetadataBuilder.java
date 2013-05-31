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
package gov.nih.nci.cagrid.portal.aggr.regsvc;

import gov.nih.nci.cagrid.common.Utils;
import gov.nih.nci.cagrid.metadata.common.ResearchCenterDescription;
import gov.nih.nci.cagrid.portal.domain.AbstractDomainObject;
import gov.nih.nci.cagrid.portal.domain.Address;
import gov.nih.nci.cagrid.portal.domain.DomainObject;
import gov.nih.nci.cagrid.portal.domain.GridService;
import gov.nih.nci.cagrid.portal.domain.Person;
import gov.nih.nci.cagrid.portal.domain.SemanticMetadataMapping;
import gov.nih.nci.cagrid.portal.domain.metadata.ServiceMetadata;
import gov.nih.nci.cagrid.portal.domain.metadata.common.*;
import gov.nih.nci.cagrid.portal.domain.metadata.service.*;
import gov.nih.nci.cagrid.portal.util.PortalUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.orm.hibernate3.HibernateTemplate;

import java.io.File;
import java.io.FileReader;
import java.util.Iterator;
import java.util.Stack;

/**
 * @author <a href="joshua.phillips@semanticbits.com">Joshua Phillips</a>
 * 
 */
public class ServiceMetadataBuilder {

	private static final Log logger = LogFactory
			.getLog(ServiceMetadataBuilder.class);

	private boolean persist;

	private HibernateTemplate hibernateTemplate;

	private GridService gridService;
	private Stack<String> path = new Stack<String>();

	// private String gmeUrl;

	public ServiceMetadata build(
			gov.nih.nci.cagrid.metadata.ServiceMetadata sMetaIn) {

		pushPath("serviceMetadata");
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
		popPath();
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
		pushPath("serviceDescription");
		Service svcOut = new Service();
		handlePersist(svcOut);
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
						pocOut = (ServicePointOfContact) buildPointOfContact(
								pocIn, pocOut);
						svcOut.getPointOfContactCollection().add(pocOut);
					}
				}
			}
		}
		if (svcIn.getSemanticMetadata() != null) {
			for (gov.nih.nci.cagrid.metadata.common.SemanticMetadata semMeta : svcIn
					.getSemanticMetadata()) {
				svcOut.getSemanticMetadata().add(
						buildSemanticMetadata(getObjectIdentifier(svcOut),
								semMeta));
			}
		}
		if (svcIn.getServiceContextCollection() != null && svcIn
				.getServiceContextCollection().getServiceContext() != null) {
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
		popPath();
		return (Service) handlePersist(svcOut);
	}

	protected String getObjectIdentifier(DomainObject obj) {
		return obj.getClass().getName() + ":" + obj.getId();
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
		pushPath("serviceContextCollection");
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
		if (svcCtxIn.getOperationCollection() != null && svcCtxIn
				.getOperationCollection().getOperation() != null) {
			for (gov.nih.nci.cagrid.metadata.service.Operation operIn : svcCtxIn
					.getOperationCollection().getOperation()) {
				Operation operOut = buildOperation(operIn);
				operOut.setServiceContext(svcCtxOut);
				svcCtxOut.getOperationCollection().add(operOut);
			}
		}

		svcCtxOut.setDescription(svcCtxIn.getDescription());
		svcCtxOut.setName(svcCtxIn.getName());
		popPath();
		return (ServiceContext) handlePersist(svcCtxOut);
	}

	protected Operation buildOperation(
			gov.nih.nci.cagrid.metadata.service.Operation operIn) {
		pushPath("operationCollection");
		Operation operOut = new Operation();
		handlePersist(operOut);
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
				pushPath("inputParameterCollection");
				for (gov.nih.nci.cagrid.metadata.service.InputParameter paramIn : paramIns) {
					InputParameter paramOut = buildInputParameter(paramIn);
					paramOut.setOperation(operOut);
					operOut.getInputParameterCollection().add(paramOut);
				}
				popPath();
			}
		}

		if (operIn.getOutput() != null) {
			Output outputOut = buildOutput(operIn.getOutput());
			outputOut.setOperation(operOut);
			operOut.setOutput(outputOut);
		}

		if (operIn.getSemanticMetadata() != null) {
			String objectIdent = getObjectIdentifier(operOut);
			for (gov.nih.nci.cagrid.metadata.common.SemanticMetadata sMetaIn : operIn
					.getSemanticMetadata()) {
				operOut.getSemanticMetadata().add(
						buildSemanticMetadata(objectIdent, sMetaIn));
			}
		}
		popPath();
		return (Operation) handlePersist(operOut);
	}

	protected Output buildOutput(
			gov.nih.nci.cagrid.metadata.service.Output outputIn) {
		Output outputOut = new Output();
		outputOut.setArray(outputIn.isIsArray());
		outputOut.setDimensionality(outputIn.getDimensionality());
		outputOut.setQName(outputIn.getQName().toString());

		if (outputIn.getUMLClass() != null) {
			outputOut.setUMLClass(buildUMLClass(outputIn.getUMLClass()));
		}

		return (Output) handlePersist(outputOut);
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

		if (paramIn.getUMLClass() != null) {
			pushPath("UMLClass");
			paramOut.setUMLClass(buildUMLClass(paramIn.getUMLClass()));
			popPath();
		}

		return (InputParameter) handlePersist(paramOut);
	}

	protected UMLClass buildUMLClass(
			gov.nih.nci.cagrid.metadata.common.UMLClass umlClassIn) {
		UMLClass umlClassOut = new UMLClass();
		handlePersist(umlClassOut);
		if (umlClassIn.getSemanticMetadata() != null) {
			String objectIdentifier = getObjectIdentifier(umlClassOut);
			for (gov.nih.nci.cagrid.metadata.common.SemanticMetadata sMetaIn : umlClassIn
					.getSemanticMetadata()) {
				umlClassOut.getSemanticMetadata().add(
						buildSemanticMetadata(objectIdentifier, sMetaIn));
			}
		}
		if (umlClassIn.getUmlAttributeCollection() != null && umlClassIn
				.getUmlAttributeCollection().getUMLAttribute() != null) {
			pushPath("umlAttributeCollection");
			for (gov.nih.nci.cagrid.metadata.common.UMLAttribute umlAttrIn : umlClassIn
					.getUmlAttributeCollection().getUMLAttribute()) {
				UMLAttribute umlAttrOut = buildUMLAttribute(umlAttrIn);
				umlAttrOut.setUmlClass(umlClassOut);
				umlClassOut.getUmlAttributeCollection().add(umlAttrOut);
			}
			popPath();
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
		handlePersist(umlAttrOut);
		if (umlAttrIn.getSemanticMetadata() != null) {
			String objectIdentifier = getObjectIdentifier(umlAttrOut);
			for (gov.nih.nci.cagrid.metadata.common.SemanticMetadata sMetaIn : umlAttrIn
					.getSemanticMetadata()) {
				umlAttrOut.getSemanticMetadata().add(
						buildSemanticMetadata(objectIdentifier, sMetaIn));
			}
		}
		if (umlAttrIn.getValueDomain() != null) {
			pushPath("valueDomain");
			umlAttrOut.setValueDomain(buildValueDomain(umlAttrIn
					.getValueDomain()));
			popPath();
		}
		umlAttrOut.setDataTypeName(umlAttrIn.getDataTypeName());
		umlAttrOut.setDescription(umlAttrIn.getDescription());
		umlAttrOut.setVersion(umlAttrIn.getVersion());
		umlAttrOut.setName(umlAttrIn.getName());
		umlAttrOut.setPublicID(umlAttrIn.getPublicID());

		return (UMLAttribute) handlePersist(umlAttrOut);
	}

	protected ValueDomain buildValueDomain(
			gov.nih.nci.cagrid.metadata.common.ValueDomain valDomIn) {
		ValueDomain valDomOut = new ValueDomain();
		handlePersist(valDomOut);
		valDomOut.setLongName(valDomIn.getLongName());
		valDomOut.setUnitOfMeasure(valDomIn.getUnitOfMeasure());

		if (valDomIn.getEnumerationCollection() != null) {
			gov.nih.nci.cagrid.metadata.common.Enumeration[] enumIns = valDomIn
					.getEnumerationCollection().getEnumeration();
			if (enumIns != null) {
				pushPath("enumerationCollection");
				for (gov.nih.nci.cagrid.metadata.common.Enumeration enumIn : enumIns) {
					Enumeration enumOut = buildEnumeration(enumIn);
					enumOut.setValueDomain(valDomOut);
					valDomOut.getEnumerationCollection().add(enumOut);
				}
				popPath();
			}
		}
		if (valDomIn.getSemanticMetadata() != null) {
			gov.nih.nci.cagrid.metadata.common.SemanticMetadata[] sMetaIns = valDomIn
					.getSemanticMetadata();
			
				String objectIdentifier = getObjectIdentifier(valDomOut);
				for (gov.nih.nci.cagrid.metadata.common.SemanticMetadata sMetaIn : sMetaIns) {
					valDomOut.getSemanticMetadata().add(
							buildSemanticMetadata(objectIdentifier, sMetaIn));
				}
			
		}

		return (ValueDomain) handlePersist(valDomOut);
	}

	protected Enumeration buildEnumeration(
			gov.nih.nci.cagrid.metadata.common.Enumeration enumIn) {
		Enumeration enumOut = new Enumeration();
		handlePersist(enumOut);
		enumOut.setPermissibleValue(enumIn.getPermissibleValue());
		enumOut.setValueMeaning(enumIn.getValueMeaning());
		if (enumIn.getSemanticMetadata() != null) {
			gov.nih.nci.cagrid.metadata.common.SemanticMetadata[] sMetaIns = enumIn
					.getSemanticMetadata();

				String objectIdentifier = getObjectIdentifier(enumOut);
				for (gov.nih.nci.cagrid.metadata.common.SemanticMetadata sMetaIn : sMetaIns) {
					enumOut.getSemanticMetadata().add(
							buildSemanticMetadata(objectIdentifier, sMetaIn));
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
		// ctxPropOut.setXmlSchema(getXMLSchemaForQName(ctxPropOut.getName()));
		return (ContextProperty) handlePersist(ctxPropOut);
	}

	protected SemanticMetadata buildSemanticMetadata(String objectIdentifier,
			gov.nih.nci.cagrid.metadata.common.SemanticMetadata semMetaIn) {
		pushPath("semanticMetadata");
		SemanticMetadata semMetaOut = new SemanticMetadata();

		semMetaOut.setConceptCode(semMetaIn.getConceptCode());
		semMetaOut.setConceptDefinition(semMetaIn.getConceptDefinition());
		semMetaOut.setConceptName(semMetaIn.getConceptName());
		semMetaOut.setOder(semMetaIn.getOrder());
		semMetaOut.setOrderLevel(semMetaIn.getOrderLevel());

		handlePersist(semMetaOut);

		SemanticMetadataMapping mapping = new SemanticMetadataMapping();
		mapping.setGridService(getGridService());
		mapping.setObjectIdentifier(objectIdentifier);
		mapping.setObjectPath(getPath());
		mapping.setSemanticMetadata(semMetaOut);
        semMetaOut.setSemanticMetadataMapping(mapping);
        handlePersist(mapping);
		popPath();
		return semMetaOut;
	}

	protected PointOfContact buildPointOfContact(
			gov.nih.nci.cagrid.metadata.common.PointOfContact pocIn,
			PointOfContact pocOut) {

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
		handlePersist(rCtrOut);
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
						pocOut = (ResearchCenterPointOfContact) buildPointOfContact(
								pocIn, pocOut);
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

	public GridService getGridService() {
		return gridService;
	}

	public void setGridService(GridService gridService) {
		this.gridService = gridService;
	}

	public void pushPath(String pathElement) {
		path.push(pathElement);
	}

	public String popPath() {
		return path.pop();
	}

	public String getPath() {
		StringBuilder sb = new StringBuilder();
		for (Iterator i = path.iterator(); i.hasNext();) {
			String pathElement = (String) i.next();
			sb.append(pathElement);
			if (i.hasNext()) {
				sb.append(".");
			}
		}
		return sb.toString();
	}

}
