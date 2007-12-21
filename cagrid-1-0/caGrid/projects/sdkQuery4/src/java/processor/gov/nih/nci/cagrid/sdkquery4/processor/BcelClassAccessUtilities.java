package gov.nih.nci.cagrid.sdkquery4.processor;

import gov.nih.nci.cacoresdk.domain.inheritance.multiplechild.Student;
import gov.nih.nci.cagrid.cqlquery.Association;
import gov.nih.nci.cagrid.data.QueryProcessingException;
import gov.nih.nci.cagrid.introduce.common.FileFilters;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.bcel.classfile.Field;
import org.apache.bcel.classfile.JavaClass;
import org.apache.bcel.classfile.LocalVariable;
import org.apache.bcel.classfile.LocalVariableTable;
import org.apache.bcel.classfile.Method;
import org.apache.bcel.classfile.Utility;
import org.apache.bcel.util.ClassPath;
import org.apache.bcel.util.Repository;
import org.apache.bcel.util.SyntheticRepository;
import org.apache.log4j.Logger;

/** 
 *  BcelClassAccessUtilities
 *  Utilities for determining information about classes
 *  using the BCEL API
 * 
 * @author David Ervin
 * 
 * @created Dec 20, 2007 11:40:22 AM
 * @version $Id: BcelClassAccessUtilities.java,v 1.2 2007-12-21 20:09:53 dervin Exp $ 
 */
public class BcelClassAccessUtilities {
    private static Logger LOG = Logger.getLogger(BcelClassAccessUtilities.class);

    private Repository repository = null;
    private Map<String, String> fieldTypes = null;
    
    public BcelClassAccessUtilities(File beansJar) {
        this.fieldTypes = new HashMap<String, String>();
        List<File> libs = getJavaLibs();
        libs.add(beansJar);
        
        StringBuilder path = new StringBuilder();
        Iterator<File> pathIter = libs.iterator();
        while (pathIter.hasNext()) {
            File element = pathIter.next();
            path.append(element.getAbsolutePath());
            if (pathIter.hasNext()) {
                path.append(File.pathSeparator);
            }
        }
        ClassPath classpath = new ClassPath(path.toString());
        repository = SyntheticRepository.getInstance(classpath);
    }
    
    
    public JavaClass getFieldType(String className, String fieldName) throws ClassNotFoundException {
        String qualifiedFieldName = className + "." + fieldName;
        LOG.debug("Determining type of field " + qualifiedFieldName);
        String fieldClassName = fieldTypes.get(qualifiedFieldName);
        if (fieldClassName == null) {
            LOG.debug("Discovering field type from class repository");
            JavaClass clazz = repository.loadClass(className);
            Field field = null;
            while (clazz != null) {
                Field[] fields = clazz.getFields();
                for (Field f : fields) {
                    if (f.getName().equals(fieldName)) {
                        field = f;
                        break;
                    }
                }
                clazz = clazz.getSuperClass();
            }
            if (field != null) {
                fieldClassName = Utility.signatureToString(field.getSignature(), false);
                fieldTypes.put(qualifiedFieldName, fieldClassName);
            }
        }
        
        if (fieldClassName != null) {
            LOG.debug("Field is of type " + fieldClassName);
            JavaClass fieldClass = repository.loadClass(fieldClassName);
            return fieldClass;
        }
        
        // no field found in type heirarchy
        LOG.debug("No field found");
        return null;
    }
    
    
    public String getRoleName(String parentName, Association assoc) throws QueryProcessingException {
        String roleName = assoc.getRoleName();
        if (roleName == null) {
            String associationTypeName = assoc.getName();
            
            // search the fields of the right type
            Field[] typedFields = null;
            try {
                typedFields = getFieldsOfType(parentName, associationTypeName);
            } catch (ClassNotFoundException ex) {
                throw new QueryProcessingException("Error getting fields from " 
                    + parentName + ": " + ex.getMessage(), ex);
            }
            if (typedFields.length == 1) {
                // found one and only one field
                roleName = typedFields[0].getName();
            } else if (typedFields.length > 1) {
                // more than one association found
                throw new QueryProcessingException("Association from " + parentName + 
                    " to " + associationTypeName + " is ambiguous: Specify a role name");
            }
            
            if (roleName == null) {
                // search for a setter method
                Method[] setters = null;
                try {
                    setters = getSettersForType(parentName, associationTypeName);
                } catch (ClassNotFoundException ex) {
                    throw new QueryProcessingException("Error getting methods from "
                        + parentName + ": " + ex.getMessage(), ex);
                }
                if (setters.length == 1) {
                    String temp = setters[0].getName().substring(3);
                    if (temp.length() == 1) {
                        roleName = String.valueOf(Character.toLowerCase(temp.charAt(0)));
                    } else {
                        roleName = String.valueOf(Character.toLowerCase(temp.charAt(0))) 
                            + temp.substring(1);
                    }
                } else if (setters.length > 1) {
                    // more than one association found
                    throw new QueryProcessingException("Association from " + parentName +  
                        " to " + associationTypeName + " is ambiguous: Specify a role name");
                }
            }
        }
        return roleName;
    }
    
    
    public Field[] getFieldsOfType(String className, String typeName) throws ClassNotFoundException {
        JavaClass clazz = repository.loadClass(className);
        List<Field> fields = new ArrayList<Field>();
        while (clazz != null) {
            Field[] classFields = clazz.getFields();
            for (Field f : classFields) {
                String fieldType = Utility.signatureToString(f.getSignature(), false);
                if (fieldType.equals(typeName)) {
                    fields.add(f);
                }
            }
            clazz = clazz.getSuperClass();
        }
        return fields.toArray(new Field[0]);
    }
    
    
    public Method[] getSettersForType(String className, String typeName) throws ClassNotFoundException {
        Set<Method> validSetterMethods = new HashSet<Method>();
        JavaClass checkClass = repository.loadClass(className);
        while (checkClass != null) {
            Method[] classMethods = checkClass.getMethods();
            for (Method method : classMethods) {
                if (method.getName().startsWith("set") && method.isPublic()) {
                    LocalVariableTable varTable = method.getLocalVariableTable();
                    LocalVariable[] vars = varTable.getLocalVariableTable();
                    if (vars.length == 2 && vars[0].getName().equals("this")) {
                        String parameterType = Utility.signatureToString(
                            vars[1].getSignature(), false);
                        if (parameterType.equals(typeName)) {
                            validSetterMethods.add(method);
                        }
                    }
                }
            }
            // walk up the inheritance tree
            checkClass = checkClass.getSuperClass();
        }
        Method[] methodArray = new Method[validSetterMethods.size()];
        validSetterMethods.toArray(methodArray);
        return methodArray;
    }
    
    
    private static List<File> getJavaLibs() {
        String javaHome = System.getProperty("java.home");
        File javaLib = new File(javaHome, "lib");
        File[] libs = javaLib.listFiles(new FileFilters.JarFileFilter());
        List<File> justTheLibs = new ArrayList<File>();
        for (File f : libs) {
            if (f.isFile()) {
                justTheLibs.add(f);
            }
        }
        return justTheLibs;
    }
    
    
    public static void main(String[] args) {
        BcelClassAccessUtilities utils = new BcelClassAccessUtilities(
            new File("ext/lib/sdk/remote-client/lib/example40-beans.jar"));
        
        try {
            Method[] methods = utils.getSettersForType(Student.class.getName(), Integer.class.getName());
            for (Method m : methods) {
                System.out.println(m.toString());
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }
}
