package gov.nih.nci.cagrid.dorian.service.idp;

import junit.framework.TestCase;

public class TestDictionaryCheck extends TestCase {
    
    public void testGoodPasswords(){
       assertFalse(DictionaryCheck.isDictionaryWordRooted("23asaiuow2n341q2n"));
       assertFalse(DictionaryCheck.isDictionaryWordRooted("987-asfakshafs089aflk"));
       assertFalse(DictionaryCheck.isDictionaryWordRooted("124l3k5ha;lskdflkq24512345"));
       assertFalse(DictionaryCheck.isDictionaryWordRooted("987asdf98akjhaf0780"));
       assertFalse(DictionaryCheck.isDictionaryWordRooted(",anmdsf-09asdklaf-0"));
       assertFalse(DictionaryCheck.isDictionaryWordRooted("af9s87akshf90akh"));
    }
    
    
    public void testBadPasswords(){
        assertTrue(DictionaryCheck.isDictionaryWordRooted("23asaiuboatow2n341q2n"));
        assertTrue(DictionaryCheck.isDictionaryWordRooted("987-asfakshaswimfs089aflk"));
        assertTrue(DictionaryCheck.isDictionaryWordRooted("124ltest3k5ha;lskdflkq24512345"));
        assertTrue(DictionaryCheck.isDictionaryWordRooted("987asdf98akjhaf0trial780"));
        assertTrue(DictionaryCheck.isDictionaryWordRooted(",anmdsf-09asdburdenklaf-0"));
        assertTrue(DictionaryCheck.isDictionaryWordRooted("af9strong87akshf90akh"));   
    }

}
