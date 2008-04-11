package gov.nih.nci.cagrid.dorian.service.upgrader;

import gov.nih.nci.cagrid.dorian.stubs.types.DorianInternalFault;

import java.util.ArrayList;

import org.springframework.beans.factory.xml.XmlBeanFactory;
import org.springframework.core.io.AbstractResource;

public class UpgradeBeanUtils {

	public final static String UPGRADE_LIST_BEAN = "UpgradeList";

	private XmlBeanFactory factory;

	public UpgradeBeanUtils(AbstractResource upgraderConf) throws DorianInternalFault {
		this.factory = new XmlBeanFactory(upgraderConf);
	}

	public ArrayList<Upgrade> getUpgradeList() throws Exception {
		return (ArrayList<Upgrade>) factory.getBean(UPGRADE_LIST_BEAN);
	}

}
