package gov.nih.nci.cagrid.dorian.service.idp;

import gov.nih.nci.cagrid.common.Utils;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang.StringUtils;


public class DictionaryCheck {

    private static StringBuffer nounsBuffer = null;
    private static StringBuffer verbsBuffer = null;
    private static StringBuffer adjBuffer = null;
    private static StringBuffer advBuffer = null;


    public static boolean doesStringContainDictionaryWord(String string) {

        boolean noun = checkNoun(string);
        boolean adjective = checkAdjective(string);
        boolean adverb = checkAdverb(string);
        boolean verb = checkVerb(string);

        return noun || adjective || adverb || verb;
    }


    private static boolean checkNoun(String password) {
        if (nounsBuffer == null) {
            InputStream fileData = DictionaryCheck.class.getResourceAsStream("dictionary/index.noun");
            try {
                nounsBuffer = Utils.inputStreamToStringBuffer(fileData);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return lookForWord(nounsBuffer, password);
    }


    private static boolean checkAdjective(String password) {
        if (adjBuffer == null) {
            InputStream fileData = DictionaryCheck.class.getResourceAsStream("dictionary/index.adj");
            try {
                adjBuffer = Utils.inputStreamToStringBuffer(fileData);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return lookForWord(adjBuffer, password);
    }


    private static boolean checkAdverb(String password) {
        if (advBuffer == null) {
            InputStream fileData = DictionaryCheck.class.getResourceAsStream("dictionary/index.adv");
            try {
                advBuffer = Utils.inputStreamToStringBuffer(fileData);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return lookForWord(advBuffer, password);
    }


    private static boolean checkVerb(String password) {
        if (verbsBuffer == null) {
            InputStream fileData = DictionaryCheck.class.getResourceAsStream("dictionary/index.verb");
            try {
                verbsBuffer = Utils.inputStreamToStringBuffer(fileData);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return lookForWord(verbsBuffer, password);
    }


    private static boolean lookForWord(StringBuffer sb, String string) {

        List forwardSubStrings = buildSubStrings(string, 4);
        List backwardSubStrings = buildSubStrings(StringUtils.reverse(string), 4);
        List fullList = new ArrayList();
        fullList.addAll(forwardSubStrings);
        fullList.addAll(backwardSubStrings);

        for (int i = 0; i < fullList.size(); i++) {
            if (StringUtils.isAlpha((String) fullList.get(i)) && sb.indexOf("\n" + ((String) fullList.get(i)).toLowerCase() + "\n") >= 0) {
                return true;
            }
        }

        return false;
    }


    private static List buildSubStrings(String string, int minimumLength) {
        List list = new ArrayList();

        if (string.length() >= minimumLength) {
            for (int i = minimumLength; i <= string.length(); i++) {
                for (int j = 0; j <= string.length() - i; j++) {
                    String subst = string.substring(j, j + i);
                    list.add(subst);
                }
            }
        } else {
            list.add(string);
        }

        return list;
    }


    public static void main(String[] args) {
        System.out.println(doesStringContainDictionaryWord(args[0]));
    }

}
