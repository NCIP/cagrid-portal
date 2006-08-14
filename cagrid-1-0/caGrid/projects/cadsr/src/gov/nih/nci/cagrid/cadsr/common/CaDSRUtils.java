package gov.nih.nci.cagrid.cadsr.common;

import gov.nih.nci.cadsr.umlproject.domain.UMLAssociationMetadata;
import gov.nih.nci.cadsr.umlproject.domain.UMLClassMetadata;
import gov.nih.nci.cagrid.cadsr.domain.UMLAssociation;
import gov.nih.nci.cagrid.cadsr.domain.UMLAssociationSourceUMLClassMetadata;
import gov.nih.nci.cagrid.cadsr.domain.UMLAssociationTargetUMLClassMetadata;
import gov.nih.nci.common.util.HQLCriteria;
import gov.nih.nci.system.applicationservice.ApplicationException;
import gov.nih.nci.system.applicationservice.ApplicationService;

import java.util.Iterator;
import java.util.List;


/**
 * @author oster
 * 
 */
public class CaDSRUtils {

	public static UMLAssociation convertAssociation(ApplicationService appService, UMLAssociationMetadata association)
		throws ApplicationException {
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

			// Work around caCORE BUG: #1280
			HQLCriteria criteria = new HQLCriteria(
				"SELECT assoc.sourceUMLClassMetadata, assoc.targetUMLClassMetadata FROM UMLAssociationMetadata as assoc WHERE assoc.id ='"
					+ association.getId() + "'");

			List rList = appService.query(criteria, UMLAssociationMetadata.class.getName());
			Iterator iterator = rList.iterator();
			if (iterator == null || !iterator.hasNext()) {
				throw new ApplicationException("Unable to located source and target UMLClassMetadata for association!");
			}
			// should have length 2, with src, target
			Object[] ids = (Object[]) iterator.next();
			if (ids == null || ids.length != 2 || !(ids[0] instanceof UMLClassMetadata)
				|| !(ids[1] instanceof UMLClassMetadata)) {
				throw new ApplicationException("Unexpected result during query for association's UMLClassMetadatas!");
			}

			UMLClassMetadata source = (UMLClassMetadata) ids[0];
			if (source != null) {
				UMLAssociationSourceUMLClassMetadata src = new UMLAssociationSourceUMLClassMetadata(source);
				converted.setSourceUMLClassMetadata(src);
			}

			UMLClassMetadata target = (UMLClassMetadata) ids[1];
			if (target != null) {
				UMLAssociationTargetUMLClassMetadata targ = new UMLAssociationTargetUMLClassMetadata(target);
				converted.setTargetUMLClassMetadata(targ);
			}
		}

		return converted;
	}


	public static String getPackageName(UMLClassMetadata umlClass) {
		String pkg = "";
		String fqn = umlClass.getFullyQualifiedName();
		int ind = fqn.lastIndexOf(".");
		if (ind >= 0) {
			pkg = fqn.substring(0, ind);
		}

		return pkg;
	}
}
