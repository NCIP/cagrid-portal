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
package gov.nih.nci.cagrid.portal.portlet.query.dcql;

import gov.nih.nci.cagrid.portal.domain.metadata.common.UMLAttribute;
import gov.nih.nci.cagrid.portal.domain.metadata.dataservice.UMLClass;
import gov.nih.nci.cagrid.portal.portlet.query.cql.UMLClassBean;

/**
 * User: kherm
 *
 * @author kherm manav.kher@semanticbits.com
 */
public class ForeignUMLClassBean extends UMLClassBean {

    private JoinCondition join = new JoinCondition();


    public ForeignUMLClassBean(UMLClass umlClass) {
        super(umlClass);
    }

    public ForeignUMLClassBean(UMLClass umlClass, UMLAttribute leftJoinAttr) throws IllegalArgumentException {
        this(umlClass);
        if (super.getAttributes().size() > 0)
            join = new JoinCondition(leftJoinAttr.getName(), getAttributes().get(0).getName(), "EQUAL_TO");
        else
            throw new IllegalArgumentException("Could not create valid join for class with ID" + umlClass.getId());
    }

    public JoinCondition getJoin() {
        return join;
    }

    public void setJoin(JoinCondition join) {
        this.join = join;
    }
}
