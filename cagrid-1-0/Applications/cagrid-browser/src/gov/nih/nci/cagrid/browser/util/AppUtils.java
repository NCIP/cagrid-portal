/**
 * $Id $
 */

package gov.nih.nci.cagrid.browser.util;

import gov.nih.nci.cagrid.browser.beans.UMLAssociationBean;
import gov.nih.nci.cagrid.browser.beans.UMLClassBean;
import gov.nih.nci.cagrid.metadata.dataservice.DomainModel;
import gov.nih.nci.cagrid.metadata.dataservice.UMLAssociation;
import gov.nih.nci.cagrid.metadata.dataservice.UMLClass;
import gov.nih.nci.cagrid.metadata.dataservice.UMLClassReference;
import gov.nih.nci.cagrid.metadata.dataservice.UMLGeneralization;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.ResourceBundle;

import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import javax.faces.el.PropertyNotFoundException;

import org.apache.log4j.Logger;

/**
 * @author <a href="mailto:joshua.phillips@semanticbits.com">Joshua Phillips</a>
 * 
 */
public class AppUtils {

	private static Logger logger = Logger.getLogger(AppUtils.class);

	public static final String SUCCESS_METHOD = "success";

	public static final String FAILED_METHOD = "failed";

	private static final String INVALID_STATE_CODE = "registrationFailed.invalidStateCode";

	private static final String INVALID_COUNTRY_CODE = "registrationFailed.invalidCountryCode";

	public static final String KEYWORD_DELIMITER = ",";

	public static final String SAMPLE_QUERIES = "sampleQueries";

	private static ResourceBundle messages = ResourceBundle
			.getBundle("messages");

	private static ResourceBundle labels = ResourceBundle.getBundle("labels");

	public static String getMessage(String name) {
		return messages.getString(name);
	}

	public static String getLabel(String name) {
		return labels.getString(name);
	}

	public static InputStream loadResourceAsStream(String fileName) {
		ExternalContext cont = FacesContext.getCurrentInstance()
				.getExternalContext();
		InputStream stream = cont.getResourceAsStream(fileName);

		return stream;
	}

	public static Object getBean(String beanName)
			throws PropertyNotFoundException {
		FacesContext ctx = FacesContext.getCurrentInstance();
		return ctx.getApplication().getVariableResolver().resolveVariable(ctx,
				beanName);
	}

	public static Object getParameter(String paramName) {
		Map reqMap = FacesContext.getCurrentInstance().getExternalContext()
				.getRequestMap();
		return reqMap.get(paramName);
	}

	public static UMLClassBean buildUMLClassBean(UMLClass klass,
			DomainModel domainModel) {
		UMLClassBean classBean = new UMLClassBean();

		Map classIdx = new HashMap();
		UMLClass[] allClasses = domainModel.getExposedUMLClassCollection()
				.getUMLClass();
		for (int i = 0; i < allClasses.length; i++) {
			classIdx.put(allClasses[i].getId(), allClasses[i]);
		}

		classBean.setUmlClass(klass);

		List assocs = new ArrayList();
		classBean.setAssociationBeans(assocs);

		UMLAssociation[] allAssocs = domainModel
				.getExposedUMLAssociationCollection().getUMLAssociation();
		if (allAssocs != null) {
			for (int i = 0; i < allAssocs.length; i++) {
				UMLClassReference source = allAssocs[i]
						.getSourceUMLAssociationEdge().getUMLAssociationEdge()
						.getUMLClassReference();
				UMLClassReference target = allAssocs[i]
						.getTargetUMLAssociationEdge().getUMLAssociationEdge()
						.getUMLClassReference();
				if (source.getRefid().equals(klass.getId())) {
					UMLAssociationBean assocBean = new UMLAssociationBean();
					UMLClass assocClass = (UMLClass) classIdx.get(target
							.getRefid());
					assocBean.setUmlClass(assocClass);
					assocBean.setUmlAssociation(allAssocs[i]);
					assocs.add(assocBean);
				}
			}
		}

		UMLGeneralization[] allGens = domainModel
				.getUmlGeneralizationCollection().getUMLGeneralization();
		if (allGens != null) {
			for (int i = 0; i < allGens.length; i++) {
				UMLClassReference subClassRef = allGens[i]
						.getSubClassReference();
				UMLClassReference superClassRef = allGens[i]
						.getSuperClassReference();
				if (subClassRef.equals(klass.getId())) {
					UMLClass superClass = (UMLClass) classIdx.get(superClassRef
							.getRefid());
					classBean.setUmlSuperClass(superClass);
					break;
				}
			}
		}
		return classBean;
	}

	// public static void validateStateCode(FacesContext context,
	// UIComponent toValidate, Object value) {
	//		
	// try {
	// StateCode.fromValue((String) value);
	// } catch (Exception ex) {
	// String msg = AppUtils.getMessage(INVALID_STATE_CODE);
	// logger.error(msg, ex);
	// ((UIInput) toValidate).setValid(false);
	// context.addMessage(toValidate.getClientId(context),
	// new FacesMessage(msg));
	// }
	// }
	//
	// public static void validateCountryCode(FacesContext context,
	// UIComponent toValidate, Object value) {
	// try {
	// CountryCode.fromValue((String) value);
	// } catch (Exception ex) {
	// String msg = AppUtils.getMessage(INVALID_COUNTRY_CODE);
	// logger.error(msg, ex);
	// ((UIInput) toValidate).setValid(false);
	// context.addMessage(toValidate.getClientId(context),
	// new FacesMessage(msg));
	// }
	// }

}
