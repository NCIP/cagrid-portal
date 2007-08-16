package gov.nih.nci.cagrid.dorian.service.idp;

import junit.framework.TestCase;

public class TestDictionaryCheck extends TestCase {
    
    public void testGoodPasswords(){
       assertFalse(DictionaryCheck.doesStringContainDictionaryWord("23asaiuow2n341q2n"));
       assertFalse(DictionaryCheck.doesStringContainDictionaryWord("987-asfakshafs089aflk"));
       assertFalse(DictionaryCheck.doesStringContainDictionaryWord("124l3k5ha;lskdflkq24512345"));
       assertFalse(DictionaryCheck.doesStringContainDictionaryWord("987asdf98akjhaf0780"));
       assertFalse(DictionaryCheck.doesStringContainDictionaryWord(",anmdsf-09asdklaf-0"));
       assertFalse(DictionaryCheck.doesStringContainDictionaryWord("af9s87akshf90akh"));
    }
    
    
    public void testBadPasswords(){
        assertTrue(DictionaryCheck.doesStringContainDictionaryWord("23asaiuboatow2n341q2n"));
        assertTrue(DictionaryCheck.doesStringContainDictionaryWord("987-asfakshaswimfs089aflk"));
        assertTrue(DictionaryCheck.doesStringContainDictionaryWord("124ltest3k5ha;lskdflkq24512345"));
        assertTrue(DictionaryCheck.doesStringContainDictionaryWord("987asdf98akjhaf0trial780"));
        assertTrue(DictionaryCheck.doesStringContainDictionaryWord(",anmdsf-09asdburdenklaf-0"));
        assertTrue(DictionaryCheck.doesStringContainDictionaryWord("af9strong87akshf90akh"));   
    }

}
