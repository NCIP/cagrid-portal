package gov.nih.nci.cagrid.dorian.service.idp;

import gov.nih.nci.cagrid.common.Utils;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang.StringUtils;


public class DictionaryCheck {

    private static StringBuffer ten = null;
    private static StringBuffer twenty = null;
    private static StringBuffer thirtyfive = null;
    private static StringBuffer fourty = null;
    private static StringBuffer fifty = null;
    private static StringBuffer fiftyfive = null;
    private static StringBuffer sixty = null;
    private static StringBuffer seventy = null;
    
    private static final int MINUMUM_WORD_SEARCH_SIZE = 4;
    private static final int DICTIONARY_COMPEXITY = 70;


    public static boolean doesStringContainDictionaryWord(String string) {
        initialize();

        if (DICTIONARY_COMPEXITY >= 10) {
            if (lookForWord(ten, string))
                return true;
        }
        if (DICTIONARY_COMPEXITY >= 20) {
            if (lookForWord(twenty, string))
                return true;
        }
        if (DICTIONARY_COMPEXITY >= 35) {
            if (lookForWord(thirtyfive, string))
                return true;
        }
        if (DICTIONARY_COMPEXITY >= 40) {
            if (lookForWord(fourty, string))
                return true;
        }
        if (DICTIONARY_COMPEXITY >= 50) {
            if (lookForWord(fifty, string))
                return true;
        }
        if (DICTIONARY_COMPEXITY >= 55) {
            if (lookForWord(fiftyfive, string))
                return true;
        }
        if (DICTIONARY_COMPEXITY >= 60) {
            if (lookForWord(sixty, string))
                return true;
        }
        if (DICTIONARY_COMPEXITY >= 70) {
            if (lookForWord(seventy, string))
                return true;
        }

        return false;

    }


    private static void initialize() {
        if (ten == null) {
            InputStream fileData = DictionaryCheck.class.getResourceAsStream("dictionary/english-words.10");
            try {
                ten = Utils.inputStreamToStringBuffer(fileData);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        if (twenty == null) {
            InputStream fileData = DictionaryCheck.class.getResourceAsStream("dictionary/english-words.20");
            try {
                twenty = Utils.inputStreamToStringBuffer(fileData);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        if (thirtyfive == null) {
            InputStream fileData = DictionaryCheck.class.getResourceAsStream("dictionary/english-words.35");
            try {
                thirtyfive = Utils.inputStreamToStringBuffer(fileData);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        if (fourty == null) {
            InputStream fileData = DictionaryCheck.class.getResourceAsStream("dictionary/english-words.40");
            try {
                fourty = Utils.inputStreamToStringBuffer(fileData);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        if (fifty == null) {
            InputStream fileData = DictionaryCheck.class.getResourceAsStream("dictionary/english-words.50");
            try {
                fifty = Utils.inputStreamToStringBuffer(fileData);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        if (fiftyfive == null) {
            InputStream fileData = DictionaryCheck.class.getResourceAsStream("dictionary/english-words.55");
            try {
                fiftyfive = Utils.inputStreamToStringBuffer(fileData);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        if (sixty == null) {
            InputStream fileData = DictionaryCheck.class.getResourceAsStream("dictionary/english-words.60");
            try {
                sixty = Utils.inputStreamToStringBuffer(fileData);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        if (seventy == null) {
            InputStream fileData = DictionaryCheck.class.getResourceAsStream("dictionary/english-words.70");
            try {
                seventy = Utils.inputStreamToStringBuffer(fileData);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

    }


    private static boolean lookForWord(StringBuffer sb, String string) {

        List forwardSubStrings = buildSubStrings(string, MINUMUM_WORD_SEARCH_SIZE);
        List backwardSubStrings = buildSubStrings(StringUtils.reverse(string), MINUMUM_WORD_SEARCH_SIZE);
        List fullList = new ArrayList();
        fullList.addAll(forwardSubStrings);
        fullList.addAll(backwardSubStrings);

        for (int i = 0; i < fullList.size(); i++) {
            if (StringUtils.isAlpha((String) fullList.get(i))
                && sb.indexOf("\n" + ((String) fullList.get(i)).toLowerCase() + "\n") >= 0) {
                return true;
            }
        }

        return false;
    }


    public static List buildSubStrings(String string, int minimumLength) {
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
