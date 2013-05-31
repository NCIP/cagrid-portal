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
/**
 * 
 */
package gov.nih.nci.cagrid.xml;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

/**
 * @author <a href="mailto:joshua.phillips@semanticbits.com">Joshua Phillips</a>
 *
 */
public class ObjectPath {

	private String name;
	private Map<String,ObjectPath> childPaths = new HashMap<String,ObjectPath>();
	
	/**
	 * 
	 */
	public ObjectPath() {
	}
	
	public ObjectPath(String name, Collection<ObjectPath> childPaths){
		this.name = name;
		setChildPaths(childPaths);
		
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public Collection<ObjectPath> getChildPaths() {
		return childPaths.values();
	}

	public void setChildPaths(Collection<ObjectPath> childPaths) {
		for(ObjectPath path : childPaths){
			this.childPaths.put(path.getName(), path);
		}
	}
	
	public void addPath(String path){
		if(path != null){
			String childName = null;
			String remainingPath = null;
			int idx = path.indexOf(".");
			if(idx != -1){
				childName = path.substring(0, idx);
				remainingPath = path.substring(idx + 1);
			}else{
				childName = path;
			}
			if(childName.equals(getName())){
				new IllegalArgumentException("Child can not have the same name as parent: " + childName);
			}
			ObjectPath childPath = childPaths.get(childName);
			if(childPath == null){
				childPath = new ObjectPath();
				childPaths.put(childName, childPath);
			}
			if(remainingPath != null){
				childPath.addPath(remainingPath);
			}
		}
	}
	public static ObjectPath parsePaths(String[] paths){
		ObjectPath root = new ObjectPath();
		for(String path : paths){
			root.addPath(path);
		}
		return root;
	}
}
