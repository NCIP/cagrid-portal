package gov.nih.nci.cagrid.portal.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Target;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Retention;

/**
 * User: kherm
 *
 * Annotation denotes that the catalogs are updated in the DB
 *
 * @author kherm manav.kher@semanticbits.com
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.METHOD)

public @interface UpdatesCatalogs {
}
