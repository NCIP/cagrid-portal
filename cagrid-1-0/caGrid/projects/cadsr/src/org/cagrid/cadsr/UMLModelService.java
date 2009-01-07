package org.cagrid.cadsr;

import java.rmi.RemoteException;


public interface UMLModelService {

    /**
     * Returns all Projects registered in caDSR.
     * 
     * @return e.g caCore
     */
    public gov.nih.nci.cadsr.umlproject.domain.Project[] findAllProjects() throws RemoteException;
//
//
//    /**
//     * Returns all Projects registered in caDSR under the given context.
//     * 
//     * @param context
//     * @return e.g caCore
//     */
//    public gov.nih.nci.cadsr.umlproject.domain.Project[] findProjects(java.lang.String context) throws RemoteException;


    /**
     * Returns all packages in the given Project.
     * 
     * @param project
     *            e.g caCore
     * @return CSI type = UMLPACKAGE
     * @throws InvalidProjectException
     *             Thrown if the given Project is null, not valid, or ambiquous.
     */
    public gov.nih.nci.cadsr.umlproject.domain.UMLPackageMetadata[] findPackagesInProject(
        gov.nih.nci.cadsr.umlproject.domain.Project project) throws RemoteException;


//    /**
//     * Returns the Classes in the given Project.
//     * 
//     * @param project
//     *            e.g caCore
//     * @return caDSR properties of the UML Class in a domain model.
//     * @throws InvalidProjectException
//     *             Thrown if the given Project is null, not valid, or ambiquous.
//     */
//    public gov.nih.nci.cadsr.umlproject.domain.UMLClassMetadata[] findClassesInProject(
//        gov.nih.nci.cadsr.umlproject.domain.Project project) throws RemoteException;


    /**
     * Returns the Classes in the given Package.
     * 
     * @param project
     *            e.g caCore
     * @param packageName
     * @return caDSR properties of the UML Class in a domain model.
     * @throws InvalidProjectException
     *             Thrown if the given Project is null, not valid, or ambiquous.
     */
    public gov.nih.nci.cadsr.umlproject.domain.UMLClassMetadata[] findClassesInPackage(
        gov.nih.nci.cadsr.umlproject.domain.Project project, java.lang.String packageName) throws RemoteException;


//
//    /**
//     * Returns the Associations of the given Class.
//     * 
//     * @param project
//     *            e.g caCore
//     * @param clazz
//     *            caDSR properties of the UML Class in a domain model.
//     * @throws InvalidProjectException
//     *             Thrown if the given Project is null, not valid, or ambiquous.
//     */
//    public gov.nih.nci.cagrid.cadsrservice.UMLAssociation[] findAssociationsForClass(
//        gov.nih.nci.cadsr.umlproject.domain.Project project, gov.nih.nci.cadsr.umlproject.domain.UMLClassMetadata clazz)
//        throws RemoteException;
//
//
//    /**
//     * Returns all Associations in the given Package.
//     * 
//     * @param project
//     *            e.g caCore
//     * @param packageName
//     * @throws InvalidProjectException
//     *             Thrown if the given Project is null, not valid, or ambiguous.
//     */
//    public gov.nih.nci.cagrid.cadsrservice.UMLAssociation[] findAssociationsInPackage(
//        gov.nih.nci.cadsr.umlproject.domain.Project project, java.lang.String packageName) throws RemoteException;
//
//
//    /**
//     * Returns all Associations in the given Project.
//     * 
//     * @param project
//     *            e.g caCore
//     * @throws InvalidProjectException
//     *             Thrown if the given Project is null, not valid, or ambiguous.
//     */
//    public gov.nih.nci.cagrid.cadsrservice.UMLAssociation[] findAssociationsInProject(
//        gov.nih.nci.cadsr.umlproject.domain.Project project) throws RemoteException;

}
