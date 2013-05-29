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
package gov.nih.nci.cagrid.portal.util;

import org.springframework.context.support.ClassPathXmlApplicationContext;

/**
 * User: kherm
 *
 * @author kherm manav.kher@semanticbits.com
 */
public class CreateCatalogs {


    /**
     * @param args
     */
    public static void main(String[] args) throws Exception {

        new ClassPathXmlApplicationContext(new String[]{
                 "classpath:applicationContext-db-aspects.xml",
                "classpath:applicationContext-service.xml",
                "classpath:applicationContext-aggr-catalog.xml"});

    }

}
 
