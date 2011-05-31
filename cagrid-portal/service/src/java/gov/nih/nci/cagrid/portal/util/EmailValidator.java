package gov.nih.nci.cagrid.portal.util;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * User: kherm
 *
 * @author kherm manav.kher@semanticbits.com
 */
public class EmailValidator {

    public static boolean validateEmail(String email) {
        //Set the email pattern string
        Pattern p = Pattern.compile(".+@.+\\.[a-z]+");

        //Match the given string with the pattern
        Matcher m = p.matcher(email);

        //check whether match is found
        return m.matches();
    }
}
