package gov.nih.nci.cagrid.wsenum.utils;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

import javax.xml.namespace.QName;

import org.globus.ws.enumeration.EnumIterator;
import org.globus.ws.enumeration.IndexedObjectFileEnumIterator;
import org.globus.ws.enumeration.SimpleEnumIterator;
import org.globus.wsrf.utils.io.IndexedObjectFileUtils;

/** 
 *  EnumIteratorFactory
 *  Creates instances of EnumIterator implementations 
 * 
 * @author David Ervin
 * 
 * @created Apr 30, 2007 12:18:15 PM
 * @version $Id: EnumIteratorFactory.java,v 1.1 2007-04-30 17:53:01 dervin Exp $ 
 */
public class EnumIteratorFactory {

    public static enum IterImpl {
        GLOBUS_SIMPLE, GLOBUS_INDEXED_FILE, 
        CAGRID_SIMPLE, CAGRID_THREADED_COMPLETE, CAGRID_CONCURRENT_COMPLETE;
        
        public String getShortDescription() {
            switch (this) {
                case GLOBUS_SIMPLE:
                    return "The Globus-provided simple enum iterator";
                case GLOBUS_INDEXED_FILE:
                    return "The Globus-provided indexed file enum iterator";
                case CAGRID_SIMPLE:
                    return "A simple iterator which persists objects to disk";
                case CAGRID_THREADED_COMPLETE:
                    return "An implementation using threads to respect maxTime constraints";
                case CAGRID_CONCURRENT_COMPLETE:
                    return "A complete implementation using Java 5's concurrent package";
            }
            throw new AssertionError("Unknown IterImpl: " + this);
        }
        
        
        public String getNotes() {
            switch (this) {
                case GLOBUS_SIMPLE:
                    return "A concrete implementation " +
                        "of the EnumIterator interface. It is a very simple implementation " +
                        "that can enumerate over in-memory data passed either as an array " +
                        "of objects or a list (java.util.List). The enumeration contents can " +
                        "be of javax.xml.soap.SOAPElement type, simple types such as " +
                        "java.lang.Integer, etc. or Axis generated Java beans. The SimpleEnumIterator " +
                        "can only be used with transient type of enumerations.";
                case GLOBUS_INDEXED_FILE:
                    return "A memory efficient implementation that can enumerate over " +
                        "data stored in an indexed file created by IndexedObjectFileWriter. " +
                        "The indexed file format is optimized for retrieving objects in a " +
                        "sequential and random manner. The IndexedObjectFileEnumIterator " +
                        "uses the IndexedObjectFileReader to read the indexed file and " +
                        "quickly locate and retrieve the next set of objects of the enumeration." +
                        "The IndexedObjectFileEnumIterator can be used with transient and " +
                        "persistent types of enumerations.";
                case CAGRID_SIMPLE:
                    return "This iterator makes no attempt to respect any values of " +
                        "IterationConstraints except for maxElements.";
                case CAGRID_THREADED_COMPLETE:
                    return "This iterator uses threads to respect maxTime constraints, " +
                        "as well as respecting maxCharacters.  Elements overflowing either " +
                        "of these constraints, however, are lost, and waits for threads " +
                        "are not optimized.";
                case CAGRID_CONCURRENT_COMPLETE:
                    return "This iterator uses the Java 5 java.util.concurrent package to " +
                        "fully support the WS-Enumeration specification for an EnumIterator " +
                        "implementation.  All iteration constraints are respected, and " +
                        "elements which cause maxCharacters to be exceded are queued for " +
                        "later retrieval.";
            }
            throw new AssertionError("Unknown IterImpl: " + this);
        }
    }
    
    
    public static EnumIterator createIterator(IterImpl iterType, List objects, QName objectQName, InputStream wsddInput) throws Exception {
        switch (iterType) {
            case GLOBUS_SIMPLE:
                return getGlobusSimpleIterator(objects, objectQName);
            case GLOBUS_INDEXED_FILE:
                return getGlobusIndexedFileIterator(objects, objectQName, null);
            case CAGRID_SIMPLE:
                SimplePersistantSDKObjectIterator.createIterator(objects, objectQName, wsddInput);
            case CAGRID_THREADED_COMPLETE:
                PersistantSDKObjectIterator.createIterator(objects, objectQName, wsddInput);
            case CAGRID_CONCURRENT_COMPLETE:
                ConcurrenPersistantObjectEnumIterator.createIterator(objects, objectQName, wsddInput);
        }
        throw new EnumIteratorInitializationException("Unknown enum iter implementation: " + iterType.toString());
    }
    
    
    /**
     * Serializes a List of caCORE SDK generated objects to a temp file on
     * the local disk, then creates an EnumIterator which can return
     * those objects.
     * 
     * <b><i>NOTE:</b></i> The temp file is created in the current user's 
     * home directory /.cagrid/SDKEnumIterator directory.  For security
     * reasons, access to this location must be controlled in a production
     * data environment. 
     * 
     * @param objectIter
     *      An iterator to a collection of caCORE SDK objects to be enumerated
     * @param objectQName
     *      The QName of the objects
     * @param wsddInput
     *      An input stream to the WSDD configuration file
     * @return
     *      An enum iterator instance to iterate the given objects
     * @throws Exception
     */
    public static EnumIterator createIterator(IterImpl iterType, Iterator objectIter, QName objectQName, InputStream wsddInput) throws Exception {
        switch (iterType) {
            case GLOBUS_SIMPLE:
                return getGlobusSimpleIterator(iteratorToList(objectIter), objectQName);
            case GLOBUS_INDEXED_FILE:
                return getGlobusIndexedFileIterator(iteratorToList(objectIter), objectQName, null);
            case CAGRID_SIMPLE:
                return SimplePersistantSDKObjectIterator.createIterator(iteratorToList(objectIter), objectQName, wsddInput);
            case CAGRID_THREADED_COMPLETE:
                return PersistantSDKObjectIterator.createIterator(objectIter, objectQName, wsddInput);
            case CAGRID_CONCURRENT_COMPLETE:
                return ConcurrenPersistantObjectEnumIterator.createIterator(objectIter, objectQName, wsddInput);
        }
        throw new EnumIteratorInitializationException("Unknown enum iter implementation: " + iterType.toString());
    }
    
    
    /**
     * Serializes a List of caCORE SDK generated objects to a specified file on
     * the local disk, then creates an EnumIterator which can return
     * those objects.
     * 
     * @param objects
     *      The list of caCORE SDK objects to be enumerated
     * @param objectQName
     *      The QName of the objects
     * @param tempFilename
     *      The name of the file to serialize objects into.
     *      <b><i>NOTE:</b></i> For security reasons, access to this location 
     *      must be controlled in a production data environment.
     * @param wsddInput
     *      An input stream of the WSDD configuration file
     * @return
     *      An enum iterator instance to iterate the given objects
     * @throws Exception
     */
    public static EnumIterator createIterator(IterImpl iterType, List objects, QName objectQName, InputStream wsddInput, String tempFilename) throws Exception {
        switch (iterType) {
            case GLOBUS_SIMPLE:
                return getGlobusSimpleIterator(objects, objectQName);
            case GLOBUS_INDEXED_FILE:
                return getGlobusIndexedFileIterator(objects, objectQName, tempFilename);
            case CAGRID_SIMPLE:
                return SimplePersistantSDKObjectIterator.createIterator(objects, objectQName, wsddInput, tempFilename);
            case CAGRID_THREADED_COMPLETE:
                return PersistantSDKObjectIterator.createIterator(objects, objectQName, wsddInput, tempFilename);
            case CAGRID_CONCURRENT_COMPLETE:
                return ConcurrenPersistantObjectEnumIterator.createIterator(objects, objectQName, wsddInput, tempFilename);
        }
        throw new EnumIteratorInitializationException("Unknown enum iter implementation: " + iterType.toString());
    }
    
    
    /**
     * Serializes a List of caCORE SDK generated objects to a specified file on
     * the local disk, then creates an EnumIterator which can return
     * those objects.
     * 
     * @param objectIter
     *      An iterator to a collection of caCORE SDK objects to be enumerated
     * @param objectQName
     *      The QName of the objects
     * @param tempFilename
     *      The name of the file to serialize objects into.
     *      <b><i>NOTE:</b></i> For security reasons, access to this location 
     *      must be controlled in a production data environment.
     * @param wsddInput
     *      An input stream of the WSDD configuration file
     * @return
     *      An enum iterator instance to iterate the given objects
     * @throws Exception
     */
    public static EnumIterator createIterator(IterImpl iterType, Iterator objectIter, QName objectQName, InputStream wsddInput, String tempFilename) throws Exception {
        switch (iterType) {
            case GLOBUS_SIMPLE:
                return getGlobusSimpleIterator(iteratorToList(objectIter), objectQName);
            case GLOBUS_INDEXED_FILE:
                return getGlobusIndexedFileIterator(iteratorToList(objectIter), objectQName, tempFilename);
            case CAGRID_SIMPLE:
                return SimplePersistantSDKObjectIterator.createIterator(iteratorToList(objectIter), objectQName, wsddInput, tempFilename);
            case CAGRID_THREADED_COMPLETE:
                PersistantSDKObjectIterator.createIterator(objectIter, objectQName, wsddInput, tempFilename);
            case CAGRID_CONCURRENT_COMPLETE:
                PersistantSDKObjectIterator.createIterator(objectIter, objectQName, wsddInput, tempFilename);
        }
        throw new EnumIteratorInitializationException("Unknown enum iter implementation: " + iterType.toString());
    }
    
    
    private static List iteratorToList(Iterator iter) {
        List l = new LinkedList();
        while (iter.hasNext()) {
            l.add(iter.next());
        }
        return l;
    }
    
    
    private static SimpleEnumIterator getGlobusSimpleIterator(List items, QName typeName) {
        return new SimpleEnumIterator(items, typeName);
    }
    
    
    private static IndexedObjectFileEnumIterator getGlobusIndexedFileIterator(List items, QName typeName, String filename) 
        throws EnumIteratorInitializationException {
        try {
            File indexedFile = null;
            if (filename == null) {
                indexedFile = IndexedObjectFileUtils.createIndexedObjectFile(items);
            } else {
                IndexedObjectFileUtils.createIndexedObjectFile(filename, items);
                indexedFile = new File(filename);
            }
            return new IndexedObjectFileEnumIterator(indexedFile, typeName);
        } catch (IOException ex) {
            throw new EnumIteratorInitializationException(ex.getMessage(), ex);
        }
    }
}
