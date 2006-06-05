package gov.nih.nci.cagrid.introduce.common;


public class GlobusTools {

	public static String getGlobusLocation() {
		return (String)System.getenv().get("GLOBUS_LOCATION");
	}

}
