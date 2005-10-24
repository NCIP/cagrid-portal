package gov.nih.nci.cagrid.gums.common;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

/**
 * @author <A href="mailto:langella@bmi.osu.edu">Stephen Langella </A>
 * @author <A href="mailto:oster@bmi.osu.edu">Scott Oster </A>
 * @author <A href="mailto:hastings@bmi.osu.edu">Shannon Hastings </A>
 * @version $Id: ArgumentManagerTable.java,v 1.2 2004/10/15 16:35:16 langella
 *          Exp $
 */
public class USStates {

	private static Map abrToState = new HashMap();

	
	public static  boolean isValidStateAbbreviation(String s){
		if(abrToState.containsKey(s)){
			return true;
		}else{
			return false;
		}
	}
	
	static {
		abrToState.put("AK", "Alaska");
		abrToState.put("AL", "Alabama");
		abrToState.put("AR", "Arkansas");
		abrToState.put("AZ", "Arizona");
		abrToState.put("CA", "California");
		abrToState.put("CO", "Colorado");
		abrToState.put("CT", "Connecticut");
		abrToState.put("DC", "Dist of Columbia");
		abrToState.put("DE", "Delaware");
		abrToState.put("FL", "Florida");
		abrToState.put("GA", "Georgia");
		abrToState.put("GU", "Guam");
		abrToState.put("HI", "Hawaii");
		abrToState.put("IA", "Iowa");
		abrToState.put("ID", "Idaho");
		abrToState.put("IL", "Illinois");
		abrToState.put("IN", "Indiana");
		abrToState.put("KS", "Kansas");
		abrToState.put("KY", "Kentucky");
		abrToState.put("LA", "Louisiana");
		abrToState.put("MA", "Massachusetts");
		abrToState.put("MD", "Maryland");
		abrToState.put("ME", "Maine");
		abrToState.put("MI", "Michigan");
		abrToState.put("MN", "Minnesota");
		abrToState.put("MO", "Missouri");
		abrToState.put("MS", "Mississippi");
		abrToState.put("MT", "Montana");
		abrToState.put("NC", "North Carolina");
		abrToState.put("ND", "North Dakota");
		abrToState.put("NE", "Nebraska");
		abrToState.put("NH", "New Hampshire");
		abrToState.put("NJ", "New Jersey");
		abrToState.put("NM", "New Mexico");
		abrToState.put("NV", "Nevada");
		abrToState.put("NY", "New York");
		abrToState.put("OH", "Ohio");
		abrToState.put("OK", "Oklahoma");
		abrToState.put("OR", "Oregon");
		abrToState.put("PA", "Pennsylvania");
		abrToState.put("PR", "Puerto Rico");
		abrToState.put("RI", "Rhode Island");
		abrToState.put("SC", "South Carolina");
		abrToState.put("SD", "South Dakota");
		abrToState.put("TN", "Tennessee");
		abrToState.put("TX", "Texas");
		abrToState.put("UT", "Utah");
		abrToState.put("VA", "Virginia");
		abrToState.put("VI", "Virgin Islands");
		abrToState.put("VT", "Vermont");
		abrToState.put("WA", "Washington");
		abrToState.put("WI", "Wisconsin");
		abrToState.put("WV", "West Virginia");
		abrToState.put("WY", "Wyoming");
	}
	
	public static List getStateAbbreviations(){
		List l = new ArrayList();
		Iterator itr = abrToState.keySet().iterator();
		while(itr.hasNext()){
			l.add(itr);
		}
		return l;
	}


}
