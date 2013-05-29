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
package gov.nih.nci.cagrid.xml;

import javax.xml.namespace.QName;

import org.apache.axis.encoding.ser.BaseSerializerFactory;

public class CastorSerializerFactory extends BaseSerializerFactory {

	public CastorSerializerFactory(Class javaType, QName xmlType) {
		super(CastorSerializer.class, xmlType, javaType);
	}
}
