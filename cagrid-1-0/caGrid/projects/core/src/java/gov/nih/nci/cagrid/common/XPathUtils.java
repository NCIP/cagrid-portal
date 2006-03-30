package gov.nih.nci.cagrid.common;

import java.util.Iterator;

import org.projectmobius.common.Namespace;
import org.projectmobius.common.NamespaceManager;


public class XPathUtils {

	/*
	 * [28] ExprToken ::= '(' | ')' | '[' | ']' | '.' | '..' | '@' | ',' | '::' |
	 * NameTest | NodeType | Operator | FunctionName | AxisName | Literal |
	 * Number | VariableReference [29] Literal ::= '"' [^"]* '"' | "'" [^']* "'"
	 * [30] Number ::= Digits ('.' Digits?)? | '.' Digits [31] Digits ::= [0-9]+
	 * [32] Operator ::= OperatorName | MultiplyOperator | '/' | '//' | '|' |
	 * '+' | '-' | '=' | '!=' | '<' | '<=' | '>' | '>=' [33] OperatorName ::=
	 * 'and' | 'or' | 'mod' | 'div' [34] MultiplyOperator ::= '*' [35]
	 * FunctionName ::= QName - NodeType [36] VariableReference ::= '$' QName
	 * [37] NameTest ::= '*' | NCName ':' '*' | QName [38] NodeType ::=
	 * 'comment' | 'text' | 'processing-instruction' | 'node' [39]
	 * ExprWhitespace ::= S
	 */

	public static String translateXPath(String prefixedXpath, NamespaceManager namespaces) {
		// don't process an empty Xpath, or one with not ns prefixes
		if (prefixedXpath == null || prefixedXpath.trim().length() == 0 || prefixedXpath.indexOf(":") < 0) {
			return prefixedXpath;
		} else if (namespaces == null
			|| (namespaces.getDefaultNamespace() == null && namespaces.getAllPrefixes().size() == 0)) {
			throw new IllegalArgumentException(
				"You specified an XPath with prefixes, yet didn't define any prefix mappings.");
		}

		// process all the replacements based on prefixes
		Iterator iterator = namespaces.getAllPrefixes().iterator();
		while (iterator.hasNext()) {
			String prefix = (String) iterator.next();
			Namespace ns = namespaces.lookupNamespace(prefix);

			// replace the /a:A stuff
			prefixedXpath = prefixedXpath.replaceAll("/" + prefix + ":(\\w)?[\\s]*$", "/*[namespace-uri()='" + ns.getRaw()
				+ "' and local-name()='$1']");

			// replace the /a:A/ stuff
			prefixedXpath = prefixedXpath.replaceAll("/" + prefix + ":(\\w)?/", "/*[namespace-uri()='" + ns.getRaw()
				+ "' and local-name()='$1']/");

			// replace the /a:A[]* stuff
			prefixedXpath = prefixedXpath.replaceAll("/" + prefix + ":(\\w)?\\[([^\\]\\]])", "/*[namespace-uri()='"
				+ ns.getRaw() + "' and local-name()='$1' and $2");


		}
		// process the default namespace
		if (namespaces.getDefaultNamespace() != null) {
			// prefixedXpath = prefixedXpath.replaceAll("(\\w)?",
			// "*[namespace-uri()='"
			// + namespaces.getDefaultNamespace().getRaw() + "' and
			// local-name()='$1']");
		} else {
			// TODO: check for need and throw exception
		}

		return prefixedXpath;
	}
}
