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
package gov.nih.nci.cagrid.portal.search;

import org.junit.Test;
import org.springframework.beans.factory.xml.XmlBeanDefinitionReader;
import org.springframework.context.support.GenericApplicationContext;
import org.springframework.core.io.ClassPathResource;

/**
 * User: kherm
 *
 * @author kherm manav.kher@semanticbits.com
 */
public class ContextInit {
    GenericApplicationContext ctx;

    @Test
    public void init() {
        ctx = new GenericApplicationContext();

        XmlBeanDefinitionReader xmlReader = new XmlBeanDefinitionReader(ctx);
        xmlReader.loadBeanDefinitions(new ClassPathResource("applicationContext-db.xml"));
        xmlReader.loadBeanDefinitions(new ClassPathResource("applicationContext-portal-search.xml"));

        ctx.refresh();


    }
}
