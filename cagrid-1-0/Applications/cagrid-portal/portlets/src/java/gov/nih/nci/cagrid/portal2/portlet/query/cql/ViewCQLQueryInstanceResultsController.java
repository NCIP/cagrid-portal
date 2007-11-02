/**
 * 
 */
package gov.nih.nci.cagrid.portal2.portlet.query.cql;

import gov.nih.nci.cagrid.portal2.domain.dataservice.CQLQueryInstance;
import gov.nih.nci.cagrid.portal2.portlet.PortletConstants;
import gov.nih.nci.cagrid.portal2.portlet.SharedApplicationModel;
import gov.nih.nci.cagrid.portal2.portlet.util.PortletUtils;
import gov.nih.nci.cagrid.portal2.portlet.util.Table;
import gov.nih.nci.cagrid.portal2.portlet.util.TableScroller;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.util.Locale;

import javax.portlet.ActionRequest;
import javax.portlet.ActionResponse;
import javax.portlet.PortletRequest;
import javax.portlet.RenderRequest;
import javax.portlet.RenderResponse;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.stream.StreamResult;
import javax.xml.transform.stream.StreamSource;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.core.io.Resource;
import org.springframework.web.portlet.ModelAndView;
import org.springframework.web.portlet.mvc.AbstractController;

/**
 * @author <a href="mailto:joshua.phillips@semanticbits.com">Joshua Phillips</a>
 * 
 */
public class ViewCQLQueryInstanceResultsController extends AbstractController
		implements InitializingBean {

	private static final Log logger = LogFactory
			.getLog(ViewCQLQueryInstanceResultsController.class);
	private String commandName;
	private String viewName;
	private SharedApplicationModel sharedApplicationModel;
	private Resource xslResource;
	private Transformer xmlTransformer;

	/**
	 * 
	 */
	public ViewCQLQueryInstanceResultsController() {

	}

	public void handleActionRequestInternal(ActionRequest request,
			ActionResponse response) throws Exception {

		CQLQueryInstanceResultsBean command = getCommand(request);
		TableScroller scroller = command.getTableScroller();
		PortletUtils.doScrollOp(request, scroller);

	}

	protected ModelAndView handleRenderRequestInternal(RenderRequest request,
			RenderResponse response) throws Exception {

		logger.debug("Handling render request");

		ModelAndView mav = new ModelAndView(getViewName());
		CQLQueryInstanceResultsBean command = getCommand(request);

		Integer instanceId = getSharedApplicationModel()
				.getSelectedCqlQueryInstanceId();

		CQLQueryInstance instance = null;
		if (instanceId != null) {

			logger.debug("Going to view CQLQueryInstance:" + instanceId);
			if (command.getInstance() != null
					&& instanceId.equals(command.getInstance().getId())) {
				// No need to fetch the instance.
				logger.debug("Instance hasn't changed. Not fetching.");
			} else {

				command.clear();

				try {
					logger.debug("Fetching CQLQueryInstance:" + instanceId);
					instance = getSelectedInstance(instanceId);
				} catch (Exception ex) {
					String msg = getMessage(PortletConstants.CQL_QUERY_INSTANCE_RETRIEVE_ERROR_MSG);
					command.setError(msg);
				}
				if (instance == null) {
					logger.debug("didn't get it");
					String msg = getMessage(PortletConstants.NONEXISTING_CQL_QUERY_INSTANCE_MSG);
					command.setError(msg);
				} else {
					logger.debug("got it");
					// logger.debug("Results: " + instance.getResult());
					String xml = instance.getResult();
					if (xml != null) {
						Table table = PortletUtils
								.buildTableFromCQLResults(new ByteArrayInputStream(
										xml.getBytes()));
						if (table != null) {
							command.setTableScroller(new TableScroller(table,
									10));
						}
						String pretty = transformXML(xml);
						command.setPrettyXml(pretty);
					}
					command.setInstance(instance);

				}
			}
		} else {
			logger.debug("No CQLQueryInstance has been selected.");
		}
		mav.addObject(getCommandName(), command);
		return mav;
	}

	private String transformXML(String xml) throws TransformerException {
		ByteArrayOutputStream buf = new ByteArrayOutputStream();
		xmlTransformer.transform(new StreamSource(new ByteArrayInputStream(xml.getBytes())), new StreamResult(buf));
		return buf.toString();
	}

	private CQLQueryInstance getSelectedInstance(Integer instanceId) {
		CQLQueryInstance selected = null;
		for (CQLQueryInstance instance : getSharedApplicationModel()
				.getSubmittedCqlQueries()) {
			if (instance.getId().equals(instanceId)) {
				selected = instance;
				break;
			}
		}
		return selected;
	}

	private String getMessage(String msgName) {
		return getApplicationContext().getMessage(msgName, null,
				Locale.getDefault());
	}

	private CQLQueryInstanceResultsBean getCommand(PortletRequest request) {

		CQLQueryInstanceResultsBean command = (CQLQueryInstanceResultsBean) request
				.getPortletSession().getAttribute(getCommandName());
		if (command == null) {
			command = new CQLQueryInstanceResultsBean();
			request.getPortletSession().setAttribute(getCommandName(), command);
		}
		return command;
	}

	public SharedApplicationModel getSharedApplicationModel() {
		return sharedApplicationModel;
	}

	public void setSharedApplicationModel(
			SharedApplicationModel sharedApplicationModel) {
		this.sharedApplicationModel = sharedApplicationModel;
	}

	public String getViewName() {
		return viewName;
	}

	public void setViewName(String viewName) {
		this.viewName = viewName;
	}

	public String getCommandName() {
		return commandName;
	}

	public void setCommandName(String commandName) {
		this.commandName = commandName;
	}

	public Resource getXslResource() {
		return xslResource;
	}

	public void setXslResource(Resource xslResource) {
		this.xslResource = xslResource;
	}

	public void afterPropertiesSet() throws Exception {
		if (getXslResource() == null) {
			throw new IllegalArgumentException(
					"The xslResource property is required.");
		}

		this.xmlTransformer = TransformerFactory.newInstance().newTransformer(
				new StreamSource(getXslResource().getInputStream()));

	}

}
