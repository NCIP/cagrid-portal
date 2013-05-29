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
package gov.nih.nci.cagrid.portal.domain.catalog;

public interface Geolocatable {

    public String getStreet1();

    public String getStreet2();

    public String getLocality();

    public String getPostalCode();

    public String getCountryCode();

    public Float getLatitude();

    public Float getLongitude();

    public String getStateProvince();
}