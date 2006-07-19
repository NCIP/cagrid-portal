package gov.nih.nci.cagrid.data.cql.cacore;

import gov.nih.nci.cagrid.cqlquery.CQLQuery;
import gov.nih.nci.cagrid.cqlquery.Object;
import gov.nih.nci.cagrid.data.QueryProcessingException;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

/** 
 *  CQL2HQL
 *  Translates a CQL query to Hibernate v3 HQL
 * 
 * @author <A HREF="MAILTO:ervin@bmi.osu.edu">David W. Ervin</A>
 * 
 * @created Jul 19, 2006 
 * @version $Id$ 
 */
public class CQL2HQL {

	public static String translate(CQLQuery query) throws QueryProcessingException {
		Map aliases = new HashMap();
		StringBuilder hql = new StringBuilder();
		processObject(hql, aliases, query.getTarget());
		// IMPL
		return hql.toString();
	}
	
	
	private static void processObject(StringBuilder hql, Map aliases, Object obj) throws QueryProcessingException {
		hql.append("From ").append(obj.getName()).append(" as ").append(alias(aliases, obj.getName()));
	}
	
	
	private static String alias(Map aliases, String fullName) {
		String alias = (String) aliases.get(fullName);
		if (alias == null) {
			// new alias
			alias = createShortName(aliases.values(), fullName);
			aliases.put(fullName, alias);
		}
		return alias;
	}
	
	
	private static String createShortName(Collection takenNames, String fullName) {
		int suffix = 0;
		int dotIndex = fullName.lastIndexOf('.');
		String alias = fullName.substring(dotIndex + 1);
		while (takenNames.contains(alias + suffix)) {
			suffix++;
		}
		return alias + suffix;
	}
}
