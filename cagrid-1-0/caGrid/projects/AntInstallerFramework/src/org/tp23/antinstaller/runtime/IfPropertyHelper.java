/* 
 * Copyright 2005 Paul Hinds
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.tp23.antinstaller.runtime;

import org.tp23.antinstaller.InstallerContext;
import org.tp23.antinstaller.page.Page;
import org.tp23.antinstaller.page.SimpleInputPage;


/**
 * <p>Encapsulates code fo the ifProperty feature</p>
 * N.B.  It is the installer generator's responsibility to ensure that properties passed
 * to the less than or greater than test are valid Numbers.
 * The internal Java format used is a Double so avalid regex would be
 * something like [0-9]+\.*[0-9]*  or [0-9]+ for an Integer.
 * The rather strange -=  and += syntax is used because &gt; and &lt;
 * must be escaped to &amp;gt; and &amp;lt; in XML attributes and the legibility
 * of the configuration files would be impared.
 * REF: 1145496
 * @author Paul Hinds
 * @version $Id: IfPropertyHelper.java,v 1.2 2006-09-11 02:18:07 kumarvi Exp $
 */
public class IfPropertyHelper {
	
	InstallerContext ctx = null;
	protected IfPropertyHelper(InstallerContext ctx){
		this.ctx = ctx;
	}

	/**
	 * Returns true if the ifProperty is set and the test fails so the page should
	 * be skipped
	 * @return boolean true to SHOW the page
	 */
	public boolean ifProperty(Page next){
		System.out.println("IFProperty1:"+"called!!!!!!!");
		// show page if ifProperty is specified and property is correct
		if(next instanceof SimpleInputPage){
			SimpleInputPage conditionalPage = (SimpleInputPage) next;
			String ifProperty = conditionalPage.getIfProperty();
			System.out.println("IFProperty2:"+ifProperty);
			if (ifProperty != null) {
				String propExpectedValue = null;
				
				// this big if list is to support the old syntax it can be optimised
				// when the old syntax is finally depretated
				
				int operatorIndex = ifProperty.indexOf("!=null");
				if(operatorIndex!=-1){
					return ifProperty(ifProperty, operatorIndex, "!=null");
				}
				operatorIndex = ifProperty.indexOf("==null");
				if(operatorIndex!=-1){
					return ifProperty(ifProperty, operatorIndex, "==null");
				} 
				operatorIndex = ifProperty.indexOf("$="); // ends with
				if (operatorIndex!=-1){
					return ifProperty(ifProperty, operatorIndex, "$="); // ends with
				}
				operatorIndex = ifProperty.indexOf("^=");  // starts with
				if (operatorIndex!=-1){
					return ifProperty(ifProperty, operatorIndex, "^="); // starts with
				}
				operatorIndex = ifProperty.indexOf("==");  // Java syntax equals for those that prefer it
				if (operatorIndex!=-1){
					return ifProperty(ifProperty, operatorIndex, "==");
				}
				operatorIndex = ifProperty.indexOf("!=");  
				if (operatorIndex!=-1){
					return ifProperty(ifProperty, operatorIndex, "!=");
				}
				operatorIndex = ifProperty.indexOf("-=");  // Java syntax equals for thos that prefer it
				if (operatorIndex!=-1){
					return ifProperty(ifProperty, operatorIndex, "-=");
				}
				operatorIndex = ifProperty.indexOf("+=");  // Java syntax equals for thos that prefer it
				if (operatorIndex!=-1){
					return ifProperty(ifProperty, operatorIndex, "+=");
				}
				operatorIndex = ifProperty.indexOf("=");  // normal equals (NOT ASSIGNMENT)
				if (operatorIndex!=-1){
					return ifProperty(ifProperty, operatorIndex, "=");
				}
			}
		}
		return true; // show the page by default
	}
	
	private boolean ifProperty(String ifProperty, int operatorIndex, String operator){
		System.out.println("IFProperty:"+ifProperty);
		int operatorLen = operator.length();
		String propKey = ifProperty.substring(0, operatorIndex);
		String propExpectedValue = ifProperty.substring(operatorIndex + operatorLen);
		// resolve ${references} 
		String propValue = null;
		// this test will be removed when the old name=value syntax is deprecated
		if(propKey.startsWith("${")){
			propValue = ctx.getInstaller().getResultContainer().getDefaultValue(propKey);
		} else {
			propValue = ctx.getInstaller().getResultContainer().getProperty(propKey);
		}
		if(operator.equals("="))          return ifPropertyEquals (propValue,propExpectedValue);
		if(operator.equals("=="))         return ifPropertyEquals (propValue,propExpectedValue);
		if(operator.equals("!="))         return ifPropertyNotEquals (propValue,propExpectedValue);
		else if(operator.equals("$="))    return ifPropertyEndsWith (propValue,propExpectedValue);
		else if(operator.equals("^="))    return ifPropertyStartsWith (propValue,propExpectedValue);
		else if(operator.equals("-="))    return ifPropertyLessThanOrEqual (propValue,propExpectedValue);
		else if(operator.equals("+="))    return ifPropertyGreaterThanOrEqual (propValue,propExpectedValue);
		else if(operator.equals("!=null"))return ifPropertyNotNull (propValue);
		else if(operator.equals("==null"))return ifPropertyIsNull (propValue);
		else return false;
	}
	
	private boolean ifPropertyEquals(String propValue,String propExpectedValue){
		if(propValue!=null && propValue.equals(propExpectedValue))return true; // show
		return false;// skip		
	}
	private boolean ifPropertyNotEquals(String propValue,String propExpectedValue){
		return propValue==null || !propValue.equals(propExpectedValue);	
	}
	private boolean ifPropertyEndsWith(String propValue,String propExpectedValue){
		return propValue!=null && propValue.endsWith(propExpectedValue);	
	}
	private boolean ifPropertyStartsWith(String propValue,String propExpectedValue){
		return propValue!=null && propValue.startsWith(propExpectedValue); 
	}
	private boolean ifPropertyLessThanOrEqual(String propValue,String propExpectedValue){
		return Double.parseDouble(propValue) <= Double.parseDouble(propExpectedValue); 	
	}
	private boolean ifPropertyGreaterThanOrEqual(String propValue,String propExpectedValue){
		return Double.parseDouble(propValue) >= Double.parseDouble(propExpectedValue); 	
	}
	private boolean ifPropertyNotNull(String propValue){
		return propValue!=null && !propValue.equals("");
	}
	private boolean ifPropertyIsNull(String propValue){
		return propValue==null || propValue.equals("");
	}
}
