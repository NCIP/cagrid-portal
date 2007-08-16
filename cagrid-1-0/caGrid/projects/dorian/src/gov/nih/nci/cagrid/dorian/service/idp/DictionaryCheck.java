package gov.nih.nci.cagrid.dorian.service.idp;

import gov.nih.nci.cagrid.common.Utils;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.apache.commons.lang.StringUtils;

import sun.security.provider.certpath.BuildStep;


public class DictionaryCheck {

    public static boolean isDictionaryWordRooted(String password) {

        boolean noun = checkNoun(password);
        boolean adjective = checkAdjective(password);
        boolean adverb = checkAdverb(password);
        boolean verb = checkVerb(password);

        return noun || adjective || adverb || verb;
    }


    private static boolean checkNoun(String password) {
        String indexFile = "index.noun";
        return lookForWord(indexFile, password);
    }


    private static boolean checkAdjective(String password) {
        String indexFile = "index.adj";
        return lookForWord(indexFile, password);
    }


    private static boolean checkAdverb(String password) {
        String indexFile = "index.adv";
        return lookForWord(indexFile, password);
    }


    private static boolean checkVerb(String password) {
        String indexFile = "index.verb";
        return lookForWord(indexFile, password);
    }


    private static boolean lookForWord(String indexFile, String password) {
        InputStream fileData = DictionaryCheck.class.getResourceAsStream("dictionary/" + indexFile);
        StringBuffer sb = null;
        try {
            sb = Utils.inputStreamToStringBuffer(fileData);
        } catch (IOException e) {
            e.printStackTrace();
        }
        List forwardSubStrings = buildSubStrings(password, 4);
        List backwardSubStrings = buildSubStrings(StringUtils.reverse(password), 4);
        List fullList = new ArrayList();
        fullList.addAll(forwardSubStrings);
        fullList.addAll(backwardSubStrings);

        for (int i = 0; i < fullList.size(); i++) {
            if (sb.indexOf("\n"+(String) fullList.get(i)+"\n") >= 0) {
                System.out.println("Found " + (String) fullList.get(i) + " in file " + indexFile);
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
        System.out.println(isDictionaryWordRooted(args[0]));
    }

}
