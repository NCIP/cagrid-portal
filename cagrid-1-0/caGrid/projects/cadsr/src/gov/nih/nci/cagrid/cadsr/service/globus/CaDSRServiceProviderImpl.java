package gov.nih.nci.cagrid.cadsr.service.globus;

import gov.nih.nci.cagrid.cadsr.common.CaDSRServiceI;
import gov.nih.nci.cagrid.cadsr.service.CaDSRServiceImpl;

import java.rmi.RemoteException;


/**
 * DO NOT EDIT: This class is autogenerated!
 * 
 * @created by caGrid toolkit version 1.0
 */
public class CaDSRServiceProviderImpl {

	CaDSRServiceI impl;


	public CaDSRServiceProviderImpl() {
		impl = new CaDSRServiceImpl();
	}



























































































































	public gov.nih.nci.cagrid.cadsr.stubs.FindAllProjectsResponse findAllProjects(gov.nih.nci.cagrid.cadsr.stubs.FindAllProjects params) throws RemoteException {
		gov.nih.nci.cagrid.cadsr.stubs.FindAllProjectsResponse boxedResult = new gov.nih.nci.cagrid.cadsr.stubs.FindAllProjectsResponse();
		boxedResult.setProject(impl.findAllProjects());
		return boxedResult;
	}
	public gov.nih.nci.cagrid.cadsr.stubs.FindProjectsResponse findProjects(gov.nih.nci.cagrid.cadsr.stubs.FindProjects params) throws RemoteException {
		gov.nih.nci.cagrid.cadsr.stubs.FindProjectsResponse boxedResult = new gov.nih.nci.cagrid.cadsr.stubs.FindProjectsResponse();
		boxedResult.setProject(impl.findProjects(params.getContext()));
		return boxedResult;
	}
	public gov.nih.nci.cagrid.cadsr.stubs.FindPackagesInProjectResponse findPackagesInProject(gov.nih.nci.cagrid.cadsr.stubs.FindPackagesInProject params) throws RemoteException {
		gov.nih.nci.cagrid.cadsr.stubs.FindPackagesInProjectResponse boxedResult = new gov.nih.nci.cagrid.cadsr.stubs.FindPackagesInProjectResponse();
		boxedResult.setUMLPackageMetadata(impl.findPackagesInProject(params.getProject().getProject()));
		return boxedResult;
	}
	public gov.nih.nci.cagrid.cadsr.stubs.FindClassesInProjectResponse findClassesInProject(gov.nih.nci.cagrid.cadsr.stubs.FindClassesInProject params) throws RemoteException {
		gov.nih.nci.cagrid.cadsr.stubs.FindClassesInProjectResponse boxedResult = new gov.nih.nci.cagrid.cadsr.stubs.FindClassesInProjectResponse();
		boxedResult.setUMLClassMetadata(impl.findClassesInProject(params.getProject().getProject()));
		return boxedResult;
	}
	public gov.nih.nci.cagrid.cadsr.stubs.FindClassesInPackageResponse findClassesInPackage(gov.nih.nci.cagrid.cadsr.stubs.FindClassesInPackage params) throws RemoteException {
		gov.nih.nci.cagrid.cadsr.stubs.FindClassesInPackageResponse boxedResult = new gov.nih.nci.cagrid.cadsr.stubs.FindClassesInPackageResponse();
		boxedResult.setUMLClassMetadata(impl.findClassesInPackage(params.getPkg().getUMLPackageMetadata()));
		return boxedResult;
	}
	public gov.nih.nci.cagrid.cadsr.stubs.GenerateMetadataExtractForProjectResponse generateMetadataExtractForProject(gov.nih.nci.cagrid.cadsr.stubs.GenerateMetadataExtractForProject params) throws RemoteException {
		gov.nih.nci.cagrid.cadsr.stubs.GenerateMetadataExtractForProjectResponse boxedResult = new gov.nih.nci.cagrid.cadsr.stubs.GenerateMetadataExtractForProjectResponse();
		boxedResult.setResponse(impl.generateMetadataExtractForProject(params.getProject().getProject()));
		return boxedResult;
	}
	public gov.nih.nci.cagrid.cadsr.stubs.GenerateMetadataExtractForPackagesResponse generateMetadataExtractForPackages(gov.nih.nci.cagrid.cadsr.stubs.GenerateMetadataExtractForPackages params) throws RemoteException {
		gov.nih.nci.cagrid.cadsr.stubs.GenerateMetadataExtractForPackagesResponse boxedResult = new gov.nih.nci.cagrid.cadsr.stubs.GenerateMetadataExtractForPackagesResponse();
		boxedResult.setResponse(impl.generateMetadataExtractForPackages(params.getPackages().getUMLPackageMetadata()));
		return boxedResult;
	}
	public gov.nih.nci.cagrid.cadsr.stubs.GenerateMetadataExtractForClassesResponse generateMetadataExtractForClasses(gov.nih.nci.cagrid.cadsr.stubs.GenerateMetadataExtractForClasses params) throws RemoteException {
		gov.nih.nci.cagrid.cadsr.stubs.GenerateMetadataExtractForClassesResponse boxedResult = new gov.nih.nci.cagrid.cadsr.stubs.GenerateMetadataExtractForClassesResponse();
		boxedResult.setResponse(impl.generateMetadataExtractForClasses(params.getClasses().getUMLClassMetadata()));
		return boxedResult;
	}
	public gov.nih.nci.cagrid.cadsr.stubs.FindAttributesInClassResponse findAttributesInClass(gov.nih.nci.cagrid.cadsr.stubs.FindAttributesInClass params) throws RemoteException {
		gov.nih.nci.cagrid.cadsr.stubs.FindAttributesInClassResponse boxedResult = new gov.nih.nci.cagrid.cadsr.stubs.FindAttributesInClassResponse();
		boxedResult.setUMLAttributeMetadata(impl.findAttributesInClass(params.getClazz().getUMLClassMetadata()));
		return boxedResult;
	}

}
