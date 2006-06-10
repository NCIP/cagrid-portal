/*
 * Created on Jun 8, 2006
 */
package gov.nci.nih.cagrid.tests.core.util;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

import org.apache.ws.jaxme.js.JavaMethod;
import org.apache.ws.jaxme.js.JavaSource;
import org.apache.ws.jaxme.js.JavaSourceFactory;
import org.apache.ws.jaxme.js.util.JavaParser;

import antlr.RecognitionException;
import antlr.TokenStreamException;

public class SourceUtils
{
	/**
	 * Don't use this method - it is based on JAXME and does not work.
	 */
	public static void replaceMethodBodyWithJAXME(File inFile, String methodName, File outFile) 
		throws RecognitionException, TokenStreamException, IOException
	{
		JavaSourceFactory inFactory = new JavaSourceFactory();
		JavaSourceFactory outFactory = new JavaSourceFactory();
		JavaParser inParser = new JavaParser(inFactory);
		JavaParser outParser = new JavaParser(outFactory);

		inParser.parse(inFile);
		JavaSource inSource = (JavaSource) inFactory.getJavaSources().next();
		outParser.parse(inFile);
		JavaSource outSource = (JavaSource) outFactory.getJavaSources().next();
		//sourceI.setForcingFullyQualifiedName(true);

		JavaMethod inMethod = findMethod(inSource, methodName);
		if (inMethod == null) throw new IllegalArgumentException("method " + methodName + " not found in " + inFile);
		JavaMethod outMethod = findMethod(outSource, methodName);
		if (inMethod == null) throw new IllegalArgumentException("method " + methodName + " not found in " + outFile);
		
		outMethod.clear();
		for (String line : inMethod.getLines(-1)) outMethod.addLine(line); 
		
		BufferedWriter out = new BufferedWriter(new FileWriter(outFile));
		outSource.write(out);
		out.flush();
		out.close();
	}
	
	public static JavaMethod findMethod(JavaSource source, String methodName)
	{
		JavaMethod[] methods = source.getMethods();
		for (int j = 0; j < methods.length; j++) {
			if (methods[j].getName().equals(methodName)) {
				return methods[j];
			}
		}
		return null;
	}
}
