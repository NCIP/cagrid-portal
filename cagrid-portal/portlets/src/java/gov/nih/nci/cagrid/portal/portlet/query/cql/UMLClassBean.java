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
/**
 *
 */
package gov.nih.nci.cagrid.portal.portlet.query.cql;

import gov.nih.nci.cagrid.portal.domain.metadata.common.UMLAttribute;
import gov.nih.nci.cagrid.portal.domain.metadata.dataservice.UMLClass;
import gov.nih.nci.cagrid.portal.domain.ServiceInfo;

import java.util.ArrayList;
import java.util.List;
import java.util.SortedMap;
import java.util.TreeMap;

/**
 * @author <a href="mailto:joshua.phillips@semanticbits.com">Joshua Phillips</a>
 */
public class UMLClassBean {

    private UMLClass umlClass;
    private List<UMLAttribute> attributes = new ArrayList<UMLAttribute>();
    private ServiceInfo serviceInfo;

    /**
     *
     */
    public UMLClassBean() {

    }

    public UMLClassBean(UMLClass umlClass) {
        this.umlClass = umlClass;
        SortedMap<String, UMLAttribute> sorted = new TreeMap<String, UMLAttribute>();
        for (UMLAttribute att : umlClass.getUmlAttributeCollection()) {
            sorted.put(att.getName(), att);
        }
        getAttributes().addAll(sorted.values());
        serviceInfo = umlClass.getModel().getService().getServiceInfo();
    }

    public UMLClass getUmlClass() {
        return umlClass;
    }

    public void setUmlClass(UMLClass umlClass) {
        this.umlClass = umlClass;
    }

    public List<UMLAttribute> getAttributes() {
        return attributes;
    }

    public void setAttributes(List<UMLAttribute> attributes) {
        this.attributes = attributes;
    }

    public ServiceInfo getServiceInfo() {
        return serviceInfo;
    }

    public void setServiceInfo(ServiceInfo serviceInfo) {
        this.serviceInfo = serviceInfo;
    }
}
