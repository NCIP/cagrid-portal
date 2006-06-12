package gov.nih.nci.cagrid.graph.domainmodelapplication;
import java.util.Vector;

public class MultiMapElement implements Comparable
{
	public String head;
	public Vector list;
	
	public int compareTo(Object e)
	{
		MultiMapElement m = (MultiMapElement)e;
		
		return m.head.trim().toUpperCase().compareTo(this.head.trim().toUpperCase());
	}
	
}