package gov.nih.nci.cagrid.security.commstyle;


import org.apache.axis.client.Stub;
/**
 * @author <A HREF="MAILTO:langella@bmi.osu.edu">Stephen Langella </A>
 * @author <A HREF="MAILTO:oster@bmi.osu.edu">Scott Oster </A>
 * @author <A HREF="MAILTO:hastings@bmi.osu.edu">Shannon Langella </A>
 * @version $Id: CommunicationStyle.java,v 1.1 2005-09-27 20:09:50 langella Exp $
 */
public interface CommunicationStyle {
   public void configure(Stub stub) throws CommunicationStyleException;
}
