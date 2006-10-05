package gov.nih.nci.cagrid.antinstaller.utils;



import java.util.ArrayList;

import org.tp23.antinstaller.runtime.ReferenceVariable;

import bsh.Interpreter;


public class JavaExpressionEvaluator {

	private String expression;
	private ArrayList variables;
	
	
	
	public JavaExpressionEvaluator(String expression, ArrayList variables) {
		super();
		this.expression = expression;
		this.variables = variables;
	}

	public String getExpression() {
		return expression;
	}

	public void setExpression(String expression) {
		this.expression = expression;
	}

	public ArrayList getVariables() {
		return variables;
	}

	public void setVariables(ArrayList variables) {
		this.variables = variables;
	}

	private String getParsableString(String str, ArrayList variables){
		String rStr = str;
		for(int i=0;i<variables.size();i++){
			ReferenceVariable rf = (ReferenceVariable)variables.get(i);
			rStr = StringUtilities.replaceInString(rStr,rf.getName(),rf.getReplaceableKey());
			rStr = StringUtilities.replaceInString(rStr,"@","\\\"");
		}
		
		
		return rStr;
		
	}
   public boolean evaluate(){
	   Interpreter bsh = new Interpreter();
	   boolean retValue = true;
	   try{
		   for(int i=0;i<variables.size();i++){
			   ReferenceVariable rv = (ReferenceVariable)variables.get(i);
			   bsh.set(rv.getReplaceableKey(), rv.getValue());
		   }
		   String str = this.getParsableString(expression,variables);
		   Boolean val = (Boolean)(bsh.eval(str));
		   retValue = val.booleanValue();
	   }catch(Exception ex){
		   ex.printStackTrace();
	   }
	   return retValue;
   }

	
	
}
