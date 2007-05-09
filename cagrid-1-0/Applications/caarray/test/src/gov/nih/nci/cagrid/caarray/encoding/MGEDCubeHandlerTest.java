package gov.nih.nci.cagrid.caarray.encoding;

import gov.nih.nci.mageom.domain.BioAssayData.impl.BioDataCubeImpl;

import java.util.Arrays;

import junit.framework.TestCase;

public class MGEDCubeHandlerTest extends TestCase {

    public void testGetCubeAsString_WithEmptyParamShouldReturnEmptyString() {
        String cubeAsString = new MGEDCubeHandler().getCubeAsString(new Object[][][] {});
        assertEquals("Serialized cube not an empty string", "", cubeAsString);
    }

    public void testGetCubeAsString_1() {
        Object[][][] cube = new Object[1][1][1];
        cube[0][0][0] = null;

        String cubeAsString = new MGEDCubeHandler().getCubeAsString(cube);

        String expectedValue = MGEDCubeHandler.NAN + MGEDCubeHandler.LINE_DELIMITER + MGEDCubeHandler.LINE_DELIMITER;
        assertEquals("Serialized cube not an empty string", expectedValue, cubeAsString);
    }

    public void testGetCubeAsString_2() {
        Object[][][] cube = new Object[1][1][1];
        cube[0][0][0] = Double.NEGATIVE_INFINITY;

        String cubeAsString = new MGEDCubeHandler().getCubeAsString(cube);

        String expectedValue = MGEDCubeHandler.NAN + MGEDCubeHandler.LINE_DELIMITER + MGEDCubeHandler.LINE_DELIMITER;
        assertEquals("Serialized cube not an empty string", expectedValue, cubeAsString);
    }

    public void testGetCubeAsString_3() {
        Object[][][] cube = new Object[1][1][1];
        cube[0][0][0] = Double.POSITIVE_INFINITY;

        String cubeAsString = new MGEDCubeHandler().getCubeAsString(cube);

        String expectedValue = MGEDCubeHandler.NAN + MGEDCubeHandler.LINE_DELIMITER + MGEDCubeHandler.LINE_DELIMITER;
        assertEquals("Serialized cube not an empty string", expectedValue, cubeAsString);
    }

    public void testGetCubeAsString_4() {
        Object[][][] cube = new Object[1][1][1];
        cube[0][0][0] = Double.NaN;

        String cubeAsString = new MGEDCubeHandler().getCubeAsString(cube);

        String expectedValue = "NaN" + MGEDCubeHandler.LINE_DELIMITER + MGEDCubeHandler.LINE_DELIMITER;
        assertEquals("Serialized cube not an empty string", expectedValue, cubeAsString);
    }

    public void testGetCubeAsString_ShouldRemoveLastValueDelimiter() {
        Object[][][] cube = new Object[1][1][1];
        cube[0][0][0] = Double.MAX_VALUE;

        String cubeAsString = new MGEDCubeHandler().getCubeAsString(cube);

        String expectedValue = Double.MAX_VALUE + MGEDCubeHandler.LINE_DELIMITER + MGEDCubeHandler.LINE_DELIMITER;
        assertEquals("Serialized cube not an empty string", expectedValue, cubeAsString);
    }

    public void testGetCubeAsString_ShouldValuesSeperatedByValueDelims() {
        Object[][][] cube = new Object[1][1][3];
        cube[0][0][0] = Boolean.TRUE;
        cube[0][0][1] = "a";
        cube[0][0][2] = Double.MIN_VALUE;

        String cubeAsString = new MGEDCubeHandler().getCubeAsString(cube);

        String expectedValue = Boolean.TRUE + MGEDCubeHandler.VALUE_DELIMITER + "a" + MGEDCubeHandler.VALUE_DELIMITER
                + Double.MIN_VALUE + MGEDCubeHandler.LINE_DELIMITER + MGEDCubeHandler.LINE_DELIMITER;
        assertEquals("Serialized cube not an empty string", expectedValue, cubeAsString);
    }
    
    public void testGetCubeAsString_ShouldSeperateByLineDelims() {
        //[[[a,b,c],[d,e]], [[f],[g]], [[h]]]
        Object[][][] cube = new Object[][][] {{{"a", "b", "c"}, {"d", "e"}}, {{"f"}, {"g"}}, {{"h"}}};
        assertEquals("[[[a, b, c], [d, e]], [[f], [g]], [[h]]]", Arrays.deepToString(cube));
        
        String cubeAsString = new MGEDCubeHandler().getCubeAsString(cube);
        
        String expectedValue = "a|b|c" + MGEDCubeHandler.LINE_DELIMITER + "d|e" 
        + MGEDCubeHandler.LINE_DELIMITER + MGEDCubeHandler.LINE_DELIMITER +
        "f" + MGEDCubeHandler.LINE_DELIMITER +
        "g" + MGEDCubeHandler.LINE_DELIMITER + MGEDCubeHandler.LINE_DELIMITER +
        "h" + MGEDCubeHandler.LINE_DELIMITER + MGEDCubeHandler.LINE_DELIMITER;
        
        assertEquals("Serialized cube not an empty string", expectedValue, cubeAsString);
    }

    public void testGetCubeFromString_1() {
        String cubeAsString = "a" + MGEDCubeHandler.LINE_DELIMITER + MGEDCubeHandler.LINE_DELIMITER;
        Object[][][] cube = MGEDCubeHandler.getCubeFromString(cubeAsString, MGEDCubeHandler.LINE_DELIMITER_PATTERN, MGEDCubeHandler.VALUE_DELIMITER_PATTERN);
        
        Object[][][] expected = new Object[][][] {{{"a"}}};
        assertTrue("Cubes are not equal", Arrays.deepEquals(expected, cube));
    }
    
    public void testGetCubeFromString_2() {
        String cubeAsString = "a" + MGEDCubeHandler.LINE_DELIMITER;
        Object[][][] cube = MGEDCubeHandler.getCubeFromString(cubeAsString, MGEDCubeHandler.LINE_DELIMITER_PATTERN, MGEDCubeHandler.VALUE_DELIMITER_PATTERN);
        
        Object[][][] expected = new Object[][][] {{{"a"}}};
        assertTrue("Cubes are not equal", Arrays.deepEquals(expected, cube));
    }
    
    public void testGetCubeFromString_3() {
        String cubeAsString = "a";
        Object[][][] cube = MGEDCubeHandler.getCubeFromString(cubeAsString, MGEDCubeHandler.LINE_DELIMITER_PATTERN, MGEDCubeHandler.VALUE_DELIMITER_PATTERN);
        
        Object[][][] expected = new Object[][][] {{{"a"}}};
        assertTrue("Cubes are not equal", Arrays.deepEquals(expected, cube));
    }
    
    public void testGetCubeFromString_4() {
        String cubeAsString = "a" + MGEDCubeHandler.LINE_DELIMITER + "a" + MGEDCubeHandler.LINE_DELIMITER + MGEDCubeHandler.LINE_DELIMITER +
        "b" +  MGEDCubeHandler.LINE_DELIMITER + "b" + MGEDCubeHandler.LINE_DELIMITER + MGEDCubeHandler.LINE_DELIMITER ;
        Object[][][] cube = MGEDCubeHandler.getCubeFromString(cubeAsString, MGEDCubeHandler.LINE_DELIMITER_PATTERN, MGEDCubeHandler.VALUE_DELIMITER_PATTERN);
        
        Object[][][] expected = new Object[][][] {{{"a"}, {"a"}}, {{"b"}, {"b"}}};
        assertEquals("[[[a], [a]], [[b], [b]]]", Arrays.deepToString(expected));
        assertTrue("Cubes are not equal", Arrays.deepEquals(expected, cube));
    }
    
    public void testSetValue_ShouldAcceptNullObject() {
    	new MGEDCubeHandler().setValue(null, "");
    }
    
    public void testSetValue_ShouldAcceptNullValue() {
    	new MGEDCubeHandler().setValue(new BioDataCubeImpl(), null);
    }
    
    public void testSetValue_ShouldSetCubeValue_1() {
    	BioDataCubeImpl bdc = new BioDataCubeImpl();
		new MGEDCubeHandler().setValue(bdc, "a\\b");
		
		assertTrue(Arrays.deepEquals(new Object[][][]{{{"a"}, {"b"}}}, bdc.getCube()));
    }
    
    public void testSetValue_ShouldSetCubeValue_2() {
    	BioDataCubeImpl bdc = new BioDataCubeImpl();
    	String cubeAsString = "a" + MGEDCubeHandler.LINE_DELIMITER + "a" + MGEDCubeHandler.LINE_DELIMITER + MGEDCubeHandler.LINE_DELIMITER +
        "b" +  MGEDCubeHandler.LINE_DELIMITER + "b" + MGEDCubeHandler.LINE_DELIMITER + MGEDCubeHandler.LINE_DELIMITER ;
    	new MGEDCubeHandler().setValue(bdc, cubeAsString);
    	
    	Object[][][] expected = new Object[][][] {{{"a"}, {"a"}}, {{"b"}, {"b"}}};
        assertEquals("[[[a], [a]], [[b], [b]]]", Arrays.deepToString(expected));
        
    	assertTrue(Arrays.deepEquals(expected, bdc.getCube()));
    }
    
    public void testSetValue_ShouldThrowIllegalArgExceptionForIncorrectInstanceTypeProvidedForParam1BioDataCubeImplExpected() {
    	Object obj = new Object();
    	try {
			new MGEDCubeHandler().setValue(obj, null);
			fail("should throw exception");
    	} catch (IllegalArgumentException e) {
			assertEquals("Object not instance of "
					+ BioDataCubeImpl.class.getName() + ": "
					+ obj.getClass().getName(), e.getMessage());
		}
    }
    
    public void testSetValue_ShouldThrowIllegalArgExceptionForIncorrectInstanceTypeProvidedForParam2StringExpected() {
    	Object value = 1;
    	try {
			new MGEDCubeHandler().setValue(new BioDataCubeImpl(), value);
			fail("should throw exception");
    	} catch (IllegalArgumentException e) {
			assertEquals("Value not instanceof String: "
					+ value.getClass().getName(), e.getMessage());
		}
    }
    
    
    public void testGetValue_ShouldAcceptNullObject() {
    	assertNull(new MGEDCubeHandler().getValue(null));
    }
    
    public void testGetValue_ShouldThrowIllegalStateExceptionForIncorrectInstanceTypeProvided_BioDataCubeImplExpected() {
    	Object obj = new Object();
    	try {
			new MGEDCubeHandler().getValue(obj);
			fail("should throw exception");
    	} catch (IllegalStateException e) {
			assertEquals("Object not instanceof: "
					+ BioDataCubeImpl.class.getName()
					+ obj.getClass().getName(), e.getMessage());
		}
    }

    public void testConstructorInitializedUsingSystemPropValues() {
    	try {
    		System.setProperty(MGEDCubeHandler.LINE_DELIMITER_PROP, "a");
    		System.setProperty(MGEDCubeHandler.LINE_DELIMITER_PATTERN_PROP, "b");
    		System.setProperty(MGEDCubeHandler.VALUE_DELIMITER_PROP, "c");
    		System.setProperty(MGEDCubeHandler.VALUE_DELIMITER_PATTERN_PROP, "d");
    		
    		MGEDCubeHandler handler = new MGEDCubeHandler();
    		
    		assertEquals("a", handler.getLineDelimiter());
    		assertEquals("b", handler.getLineDelimiterPattern());
    		assertEquals("c", handler.getValueDelimiter());
    		assertEquals("d", handler.getValueDelimiterPattern());
    	} finally {
    		System.setProperty(MGEDCubeHandler.LINE_DELIMITER_PROP, MGEDCubeHandler.LINE_DELIMITER);
    		System.setProperty(MGEDCubeHandler.LINE_DELIMITER_PATTERN_PROP, MGEDCubeHandler.LINE_DELIMITER_PATTERN);
    		System.setProperty(MGEDCubeHandler.VALUE_DELIMITER_PROP, MGEDCubeHandler.VALUE_DELIMITER);
    		System.setProperty(MGEDCubeHandler.VALUE_DELIMITER_PATTERN_PROP, MGEDCubeHandler.VALUE_DELIMITER_PATTERN);
    	}
    }
    
    public void testConstructorInitializedUsingDefaultValues() {
    	MGEDCubeHandler handler = new MGEDCubeHandler();
    	
    	assertEquals(MGEDCubeHandler.LINE_DELIMITER, handler.getLineDelimiter());
    	assertEquals(MGEDCubeHandler.LINE_DELIMITER_PATTERN, handler.getLineDelimiterPattern());
    	assertEquals(MGEDCubeHandler.VALUE_DELIMITER, handler.getValueDelimiter());
    	assertEquals(MGEDCubeHandler.VALUE_DELIMITER_PATTERN, handler.getValueDelimiterPattern());
    }
    
    public void testSerializeThenDeserialize_ShouldMatchOriginal_1() {
        Object[][][] cube = new Object[1][1][1];
        cube[0][0][0] = null;

        String cubeAsString = new MGEDCubeHandler().getCubeAsString(cube);

        String expectedValue = MGEDCubeHandler.NAN +  MGEDCubeHandler.LINE_DELIMITER + MGEDCubeHandler.LINE_DELIMITER;
        assertEquals("Serialized cube not an empty string", expectedValue, cubeAsString);
    	
        Object[][][] cubeResult = MGEDCubeHandler.getCubeFromString(cubeAsString, MGEDCubeHandler.LINE_DELIMITER_PATTERN, MGEDCubeHandler.VALUE_DELIMITER_PATTERN);
        
        assertTrue("Cubes are not equal", Arrays.deepEquals(cube, cubeResult));
    }
    
    public void testSerializeThenDeserialize_ShouldMatchOriginal_2() {
    	Object[][][] cube = new Object[1][1][1];
    	cube[0][0][0] = Double.NEGATIVE_INFINITY;
    	
    	String cubeAsString = new MGEDCubeHandler().getCubeAsString(cube);
    	
    	String expectedValue = MGEDCubeHandler.NAN + MGEDCubeHandler.LINE_DELIMITER + MGEDCubeHandler.LINE_DELIMITER;
    	assertEquals("Serialized cube not an empty string", expectedValue, cubeAsString);
    	
    	Object[][][] cubeResult = MGEDCubeHandler.getCubeFromString(cubeAsString, MGEDCubeHandler.LINE_DELIMITER_PATTERN, MGEDCubeHandler.VALUE_DELIMITER_PATTERN);
    	
    	cube[0][0][0] = null;
    	
    	assertTrue("Cubes are not equal", Arrays.deepEquals(cube, cubeResult));
    }
    
    public void testSerializeThenDeserialize_ShouldMatchOriginal_3() {
    	Object[][][] cube = new Object[1][1][1];
    	cube[0][0][0] = Double.POSITIVE_INFINITY;
    	
    	String cubeAsString = new MGEDCubeHandler().getCubeAsString(cube);
    	
    	String expectedValue = MGEDCubeHandler.NAN + MGEDCubeHandler.LINE_DELIMITER + MGEDCubeHandler.LINE_DELIMITER;
    	assertEquals("Serialized cube not an empty string", expectedValue, cubeAsString);
    	
    	Object[][][] cubeResult = MGEDCubeHandler.getCubeFromString(cubeAsString, MGEDCubeHandler.LINE_DELIMITER_PATTERN, MGEDCubeHandler.VALUE_DELIMITER_PATTERN);
    	
    	//simplify the comparison
    	cube[0][0][0] = null;
    	
    	assertTrue("Cubes are not equal", Arrays.deepEquals(cube, cubeResult));
    }
    
    public void testSerializeThenDeserialize_ShouldMatchOriginal_4() {
    	Object[][][] cube = new Object[1][1][1];
    	cube[0][0][0] = Double.NaN;
    	
    	String cubeAsString = new MGEDCubeHandler().getCubeAsString(cube);
    	
    	String expectedValue = MGEDCubeHandler.NAN + MGEDCubeHandler.LINE_DELIMITER + MGEDCubeHandler.LINE_DELIMITER;
    	assertEquals("Serialized cube not an empty string", expectedValue, cubeAsString);
    	
    	Object[][][] cubeResult = MGEDCubeHandler.getCubeFromString(cubeAsString, MGEDCubeHandler.LINE_DELIMITER_PATTERN, MGEDCubeHandler.VALUE_DELIMITER_PATTERN);
    	
    	//simplify the comparison
    	cube[0][0][0] = null;

    	assertTrue("Cubes are not equal", Arrays.deepEquals(cube, cubeResult));
    }
    
    public void testSerializeThenDeserialize_ShouldMatchOriginal_5() {
        Object[][][] cube = new Object[1][1][4];
        cube[0][0][0] = Boolean.TRUE;
        cube[0][0][1] = "a";
        cube[0][0][2] = Double.MIN_VALUE;
        cube[0][0][3] = Integer.MIN_VALUE;

        String cubeAsString = new MGEDCubeHandler().getCubeAsString(cube);

        String expectedValue = Boolean.TRUE + MGEDCubeHandler.VALUE_DELIMITER + "a" + MGEDCubeHandler.VALUE_DELIMITER
                + Double.MIN_VALUE + MGEDCubeHandler.VALUE_DELIMITER + Integer.MIN_VALUE + MGEDCubeHandler.LINE_DELIMITER + MGEDCubeHandler.LINE_DELIMITER;
        assertEquals("Serialized cube not an empty string", expectedValue, cubeAsString);
    	
    	Object[][][] cubeResult = MGEDCubeHandler.getCubeFromString(cubeAsString, MGEDCubeHandler.LINE_DELIMITER_PATTERN, MGEDCubeHandler.VALUE_DELIMITER_PATTERN);
    	
        cube[0][0][0] = "" + Boolean.TRUE;
        cube[0][0][1] = "a";
        cube[0][0][2] = "" + Double.MIN_VALUE;
        cube[0][0][3] = "" + Integer.MIN_VALUE;
    	
    	assertTrue("Cubes are not equal", Arrays.deepEquals(cube, cubeResult));
    }
    
    public void testDeserializeThenSerializeCube_ShouldMatchOriginal() {
        String cubeAsString = "a" + MGEDCubeHandler.LINE_DELIMITER + "a" + MGEDCubeHandler.LINE_DELIMITER + MGEDCubeHandler.LINE_DELIMITER +
        "b" +  MGEDCubeHandler.LINE_DELIMITER + "b" + MGEDCubeHandler.LINE_DELIMITER + MGEDCubeHandler.LINE_DELIMITER ;
        Object[][][] cube = MGEDCubeHandler.getCubeFromString(cubeAsString, MGEDCubeHandler.LINE_DELIMITER_PATTERN, MGEDCubeHandler.VALUE_DELIMITER_PATTERN);
        
        Object[][][] expected = new Object[][][] {{{"a"}, {"a"}}, {{"b"}, {"b"}}};
        assertEquals("[[[a], [a]], [[b], [b]]]", Arrays.deepToString(expected));
        assertTrue("Cubes are not equal", Arrays.deepEquals(expected, cube));
        
        String cubeAsStringResult = new MGEDCubeHandler().getCubeAsString(cube);
        
        assertEquals("Parsed doesn't match original", cubeAsString, cubeAsStringResult);
    }
    
}
