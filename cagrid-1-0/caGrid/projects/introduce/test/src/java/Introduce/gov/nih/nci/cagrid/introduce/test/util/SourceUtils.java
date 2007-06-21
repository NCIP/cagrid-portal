/*
 * Created on Jun 8, 2006
 */
package gov.nih.nci.cagrid.introduce.test.util;

import gov.nih.nci.cagrid.common.Utils;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.StringReader;

import org.apache.ws.jaxme.js.JavaMethod;
import org.apache.ws.jaxme.js.JavaSource;


public class SourceUtils {
    public static void modifyImpl(File sourceJava, File targetJava, String methodName) throws IOException {
        FileInputStream sourceInput = new FileInputStream(sourceJava);
        FileInputStream targetInput = new FileInputStream(targetJava);
        StringBuffer sourceContent = new StringBuffer(Utils.inputStreamToStringBuffer(sourceInput));
        StringBuffer targetContent = new StringBuffer(Utils.inputStreamToStringBuffer(targetInput));
        sourceInput.close();
        targetInput.close();

        // SyncSource ss = new SyncSource(new File("junk"), null, null);

        int sourceStartOfMethod = startOfSignature(sourceContent, methodName);
        int sourceEndOfSignature = endOfSignature(sourceContent, sourceStartOfMethod);
        int sourceEndOfImplementation = endOfMethod(sourceContent, sourceEndOfSignature);

        int targetStartOfMethod = startOfSignature(targetContent, methodName);
        int targetEndOfSignature = endOfSignature(targetContent, targetStartOfMethod);
        int targetEndOfImplementation = endOfMethod(targetContent, targetEndOfSignature);

        targetContent.delete(targetEndOfSignature, targetEndOfImplementation);
        targetContent.insert(targetEndOfSignature, sourceContent.substring(sourceEndOfSignature,
            sourceEndOfImplementation));

        FileWriter out = new FileWriter(targetJava);
        out.write(targetContent.toString());
        out.flush();
        out.close();
    }


    public static int endOfMethod(StringBuffer sb, int startingIndex) {
        int index = startingIndex;
        if (index < 0)
            return index;

        int openCount = 0;
        do {
            char ch = sb.charAt(index);
            if (ch == '{') {
                openCount++;
            } else if (ch == '}') {
                openCount--;
            }
            index++;
        } while (openCount >= 0);
        return index - 1;
    }


    public static int endOfSignature(StringBuffer sb, int startingIndex) {
        int index = startingIndex;
        if (index < 0) {
            return index;
        }
        boolean found = false;
        while (!found) {
            char ch = sb.charAt(index);
            if (ch == '{') {
                found = true;
            }
            index++;
        }
        return index;
    }


    public static int startOfSignature(StringBuffer sb, String methodName) throws IOException {
        BufferedReader br = new BufferedReader(new StringReader(sb.toString()));
        String line = null;
        while ((line = br.readLine()) != null) {
            int index = line.indexOf(methodName);
            if (index == -1)
                continue;
            index = line.indexOf("public");
            if (index == -1)
                continue;
            br.close();
            return sb.indexOf(line);
        }
        br.close();
        return -1;
    }


    public static JavaMethod findMethod(JavaSource source, String methodName) {
        JavaMethod[] methods = source.getMethods();
        for (int j = 0; j < methods.length; j++) {
            if (methods[j].getName().equals(methodName)) {
                return methods[j];
            }
        }
        return null;
    }
}
