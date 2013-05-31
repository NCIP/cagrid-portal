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
package gov.nih.nci.cagrid.portal.domain.metadata.service;

import gov.nih.nci.cagrid.portal.domain.AbstractDomainObject;
import gov.nih.nci.cagrid.portal.domain.metadata.common.UMLClass;
import gov.nih.nci.cagrid.portal.domain.metadata.dataservice.XMLSchema;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.DiscriminatorColumn;
import javax.persistence.DiscriminatorType;
import javax.persistence.Entity;
import javax.persistence.Inheritance;
import javax.persistence.InheritanceType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToOne;
import javax.persistence.Table;

import org.hibernate.annotations.ForceDiscriminator;
import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.Parameter;

/**
 * @author <a href="joshua.phillips@semanticbits.com">Joshua Phillips</a>
 *
 */
@Entity
@Table(name = "params")
@GenericGenerator(name = "id-generator", strategy = "native", parameters = { @Parameter(name = "sequence", value = "seq_params") })
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
@DiscriminatorColumn(name = "param_type", discriminatorType = DiscriminatorType.STRING)
@ForceDiscriminator
public abstract class AbstractParameter extends AbstractDomainObject {

	private XMLSchema xmlSchema;
	private String qName;
	private UMLClass uMLClass;
	private boolean isArray;
	private int dimensionality;

	
	public int getDimensionality() {
		return dimensionality;
	}
	public void setDimensionality(int dimensionality) {
		this.dimensionality = dimensionality;
	}
	
	@Column(name = "array_flag")
	public boolean isArray() {
		return isArray;
	}
	public void setArray(boolean isArray) {
		this.isArray = isArray;
	}
	
	public String getQName() {
		return qName;
	}
	public void setQName(String name) {
		qName = name;
	}
	
	@OneToOne(cascade = CascadeType.ALL)
	@JoinColumn(name = "uml_class_id")
	public UMLClass getUMLClass() {
		return uMLClass;
	}
	public void setUMLClass(UMLClass class1) {
		uMLClass = class1;
	}
	
	@ManyToOne
	@JoinColumn(name = "schema_id")
	public XMLSchema getXmlSchema() {
		return xmlSchema;
	}
	public void setXmlSchema(XMLSchema xmlSchema) {
		this.xmlSchema = xmlSchema;
	}
}
