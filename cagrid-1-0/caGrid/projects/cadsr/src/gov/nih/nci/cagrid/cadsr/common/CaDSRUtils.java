/**
 * 
 */
package gov.nih.nci.cagrid.cadsr.common;

import gov.nih.nci.cadsr.umlproject.domain.UMLAssociationMetadata;
import gov.nih.nci.cadsr.umlproject.domain.UMLClassMetadata;
import gov.nih.nci.cagrid.cadsr.domain.UMLAssociation;
import gov.nih.nci.cagrid.cadsr.domain.UMLAssociationSourceUMLClassMetadata;
import gov.nih.nci.cagrid.cadsr.domain.UMLAssociationTargetUMLClassMetadata;


/**
 * @author oster
 * 
 */
public class CaDSRUtils {

	public static UMLAssociation convertAssociation(UMLAssociationMetadata association) {
		UMLAssociation converted = null;
		if (association != null) {
			converted = new UMLAssociation();
			converted.setId(association.getId());
			converted.setIsBidirectional(association.getIsBidirectional().booleanValue());
			converted.setSourceMaxCardinality(association.getSourceHighCardinality().intValue());
			converted.setSourceMinCardinality(association.getSourceLowCardinality().intValue());
			converted.setSourceRoleName(association.getSourceRoleName());
			converted.setTargetMaxCardinality(association.getTargetHighCardinality().intValue());
			converted.setTargetMinCardinality(association.getTargetLowCardinality().intValue());
			converted.setTargetRoleName(association.getTargetRoleName());

			UMLClassMetadata source = association.getSourceUMLClassMetadata();
			if (source != null) {
				UMLAssociationSourceUMLClassMetadata src = new UMLAssociationSourceUMLClassMetadata(source);
				converted.setSourceUMLClassMetadata(src);
			}

			UMLClassMetadata target = association.getTargetUMLClassMetadata();
			if (target != null) {
				UMLAssociationTargetUMLClassMetadata targ = new UMLAssociationTargetUMLClassMetadata(target);
				converted.setTargetUMLClassMetadata(targ);
			}
		}

		return converted;

	}
}
