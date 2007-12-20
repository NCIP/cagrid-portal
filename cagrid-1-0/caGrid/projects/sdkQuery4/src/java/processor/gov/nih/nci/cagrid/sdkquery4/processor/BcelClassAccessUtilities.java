package gov.nih.nci.cagrid.sdkquery4.processor;

import gov.nih.nci.cacoresdk.domain.inheritance.multiplechild.Student;
import gov.nih.nci.cagrid.introduce.common.FileFilters;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.apache.bcel.classfile.Field;
import org.apache.bcel.classfile.JavaClass;
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
 * @version $Id: BcelClassAccessUtilities.java,v 1.1 2007-12-20 17:55:01 dervin Exp $ 
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
            JavaClass fieldClass = utils.getFieldType(Student.class.getName(), "name");
            System.out.println("Class: " + fieldClass.getClassName());
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }
}
