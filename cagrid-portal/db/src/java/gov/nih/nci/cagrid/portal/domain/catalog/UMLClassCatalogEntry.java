package gov.nih.nci.cagrid.portal.domain.catalog;

import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;

@Entity
@DiscriminatorValue("umlClass")

public class UMLClassCatalogEntry extends CatalogEntry {
}