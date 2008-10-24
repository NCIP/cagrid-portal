package gov.nih.nci.cagrid.introduce.extensions.metadata.deployment.validator;

import java.io.File;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import gov.nih.nci.cagrid.introduce.servicetasks.deployment.validator.DeploymentValidator;
import gov.nih.nci.cagrid.metadata.MetadataUtils;
import gov.nih.nci.cagrid.metadata.ServiceMetadata;
import gov.nih.nci.cagrid.metadata.common.PointOfContact;


public class MetadataDeploymentValidator extends DeploymentValidator {

    @Override
    public void validate() throws Exception {
        List messages = new ArrayList();
        ServiceMetadata metadata = MetadataUtils.deserializeServiceMetadata(new FileReader("etc" + File.separator
            + "serviceMetadata.xml"));

        if (metadata.getServiceDescription() != null && metadata.getServiceDescription().getService() != null) {
            if (metadata.getServiceDescription().getService().getPointOfContactCollection() != null
                && metadata.getServiceDescription().getService().getPointOfContactCollection().getPointOfContact() != null) {
                for (int poci = 0; poci < metadata.getServiceDescription().getService().getPointOfContactCollection()
                    .getPointOfContact().length; poci++) {
                    PointOfContact contact = metadata.getServiceDescription().getService()
                        .getPointOfContactCollection().getPointOfContact(poci);
                    if (contact.getFirstName() == null || contact.getFirstName().trim().length() <= 0) {
                        messages
                            .add("Error: unpopulated ServiceDescription PointOfContact (POC must have a First Name)");
                    }
                    if (contact.getLastName() == null || contact.getLastName().trim().length() <= 0) {
                        messages
                            .add("Error: unpopulated ServiceDescription PointOfContact (POC must have a Last Name)");
                    }
                    if (contact.getEmail() == null || contact.getEmail().trim().length() <= 0) {
                        messages
                            .add("Error: unpopulated ServiceDescription PointOfContact (POC must have an Email address)");
                    }
                    if (contact.getAffiliation() == null || contact.getAffiliation().trim().length() <= 0) {
                        messages
                            .add("Error: unpopulated ServiceDescription PointOfContact (POC must have an Affiliation)");
                    }
                    if (contact.getRole() == null || contact.getRole().trim().length() <= 0) {
                        messages.add("Error: unpopulated ServiceDescription PointOfContact (POC must have a Role)");
                    }
                }
            } else {
                messages.add("Error: unpopulated ServiceDescription PointOfContact (must have at least one contact)");
            }
        } else {
            messages.add("Error: unpopulated ServiceDescription");
        }

        if (metadata.getHostingResearchCenter() != null) {
            if (metadata.getHostingResearchCenter().getResearchCenter() != null) {
                if (metadata.getHostingResearchCenter().getResearchCenter().getDisplayName() == null
                    || metadata.getHostingResearchCenter().getResearchCenter().getDisplayName().trim().length() <= 0) {
                    messages.add("Error: unpopulated ResearchCenter DisplayName");
                }
                if (metadata.getHostingResearchCenter().getResearchCenter().getShortName() == null
                    || metadata.getHostingResearchCenter().getResearchCenter().getShortName().trim().length() <= 0) {
                    messages.add("Error: unpopulated ResearchCenter ShortName");
                }
                if (metadata.getHostingResearchCenter().getResearchCenter().getPointOfContactCollection() != null
                    && metadata.getHostingResearchCenter().getResearchCenter().getPointOfContactCollection()
                        .getPointOfContact() != null) {
                    for (int poci = 0; poci < metadata.getHostingResearchCenter().getResearchCenter()
                        .getPointOfContactCollection().getPointOfContact().length; poci++) {
                        PointOfContact contact = metadata.getHostingResearchCenter().getResearchCenter()
                            .getPointOfContactCollection().getPointOfContact(poci);
                        if (contact.getFirstName() == null || contact.getFirstName().trim().length() <= 0) {
                            messages
                                .add("Error: unpopulated ResearchCenter PointOfContact (POC must have a First Name)");
                        }
                        if (contact.getLastName() == null || contact.getLastName().trim().length() <= 0) {
                            messages
                                .add("Error: unpopulated ResearchCenter PointOfContact (POC must have a Last Name)");
                        }
                        if (contact.getEmail() == null || contact.getEmail().trim().length() <= 0) {
                            messages
                                .add("Error: unpopulated ResearchCenter PointOfContact (POC must have an Email address)");
                        }
                        if (contact.getAffiliation() == null || contact.getAffiliation().trim().length() <= 0) {
                            messages
                                .add("Error: unpopulated ResearchCenter PointOfContact (POC must have an Affiliation)");
                        }
                        if (contact.getRole() == null || contact.getRole().trim().length() <= 0) {
                            messages.add("Error: unpopulated ResearchCenter PointOfContact (POC must have a Role)");
                        }
                    }
                } else {
                    messages.add("Error: unpopulated ResearchCenter PointOfContact (must have at least one contact)");
                }
                if (metadata.getHostingResearchCenter().getResearchCenter().getAddress() != null) {
                    if (metadata.getHostingResearchCenter().getResearchCenter().getAddress().getLocality() == null
                        || metadata.getHostingResearchCenter().getResearchCenter().getAddress().getLocality().trim()
                            .length() <= 0) {
                        messages.add("Error: unpopulated ResearchCenter Address Locality");
                    }
                    if (metadata.getHostingResearchCenter().getResearchCenter().getAddress().getPostalCode() == null
                        || metadata.getHostingResearchCenter().getResearchCenter().getAddress().getPostalCode().trim()
                            .length() <= 0) {
                        messages.add("Error: unpopulated ResearchCenter Address Postal Code");
                    }
                    if (metadata.getHostingResearchCenter().getResearchCenter().getAddress().getStreet1() == null
                        || metadata.getHostingResearchCenter().getResearchCenter().getAddress().getStreet1().trim()
                            .length() <= 0) {
                        messages.add("Error: unpopulated ResearchCenter Address Street 1");
                    }
                    if (metadata.getHostingResearchCenter().getResearchCenter().getAddress().getCountry() == null
                        || metadata.getHostingResearchCenter().getResearchCenter().getAddress().getCountry().trim()
                            .length() <= 0) {
                        messages.add("Error: unpopulated ResearchCenter Address Country");
                    }
                } else {
                    messages.add("Error: unpopulated Research Center Address");
                }
            } else {
                messages.add("Error: unpopulated ResearchCenter");
            }
        } else {
            messages.add("Error: unpopulated HostingResearchCenter");
        }

        if (messages.size() > 0) {
            StringBuffer buffer = new StringBuffer();
            Iterator it = messages.iterator();
            while (it.hasNext()) {
                buffer.append("\t" + (String) it.next() + "\n");
            }
            throw new Exception(
                "\n  CAGRID SERVICE METADATA VALIDATION ERROR:\n"
                    + buffer.toString()
                    + "\n  In order to fix these problems either edit the etc/serviceMetadata.xml file or open the service with Introduce to edit the service metadata resource property.\n  To skip deployment validation simply set the no.deployment.validation property when calling ant.  (ant -Dno.deployment.validation=true deployTomcat)");
        }
    }

}
