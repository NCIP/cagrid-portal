package org.cagrid.rav;

public class TestCode {

	public static void main (String []args){
		TestCode tc = new TestCode();
		String []arguments = tc.packArgs();
		tc.solitaire(arguments);
	}

	public String [] packArgs(){
		String [] args = new String [6];
		args[0] = "-gui";
		args[1] = "t";
		args[2] = "-size";
		args[3] = "50";
		args[4] = "-bob";
		return args;
	}

	public boolean solitaire(java.lang.String[] arguments)  {
		System.out.println("Attempting to execute ");
		try{
			
			String execString = "C:\\ANL\\Solitaire.exe";
			for (int i = 0; i < arguments.length; i++){
				if (arguments [i] != null){
					execString += " " + arguments[i];
				}
			}
			//Runtime.getRuntime().exec(execString);
			System.out.println(execString);
		}catch(Exception e){
			System.out.println("ERR " + e + " returning fail");
			return false;
			//Should do something here
		}
		System.out.println("EXECUTED");
		return true;
	}
}

