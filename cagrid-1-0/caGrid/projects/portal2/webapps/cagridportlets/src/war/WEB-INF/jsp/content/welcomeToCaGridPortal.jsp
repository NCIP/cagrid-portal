<p>caGrid Portal provides an overview of services that are currently
registered with the caGrid Index service. </br>
The tool provides visusal display of services on the caGrid
infrastructure and also institutions that are participating in the caBIG
program. </br>
<p>Significant number of enhancements has been incorporated into the
caGrid 1.0 infrastructure. To mention a few highlights:</p>
<ul>
	<ul>
		<li>Migrating the underlying infrastructure for supporting
		services using standard web service resource framework (WSRF)
		specification</li>
		<li>Complete overhaul of federated security infrastructure to
		satisfy caBIG security needs, incorporating many of the recommedations
		made in the <a
			href="https://cabig.nci.nih.gov/workspaces/Architecture/Security_Tech_Eval_White_Paper/"
			target="blank">caBIG Security White Paper</a></li>
		<li>New workflow capabilities to enable orchestration of services
		using industry standard Business Process Execution Language (BPEL)</li>
		<li>New Federated Query Processing (FQP) capability built in
		collaboration with the Cancer Translational Research Informatics
		Platform (caTRIP) project, a caBIG funded project</li>
		<li>Performance and scalability improvements to the services by
		implementing specifications such as <a
			href="http://www.w3.org/Submission/WS-Enumeration/" target="_blank"><i>WS-Enumeration</i></a>
		into the underlying Globus Toolkit infrastructure</li>

		<li>Provision for grid wide object identifier support capability
		by integrating with The Handle System&#174; service from Corporation
		for National Research Initiatives</li>
		<li>Extensive enhacements made to the metadata infrastructure,
		including standard grid service APIs to Global Model Exchange (GME),
		Cancer Data Standards Repository (caDSR) and Enterprise Vocabulary
		Service (EVS)</li>
		<li>Tighter integration with NCICB components used by caBIG
		funded projects including Common Security Module (CSM) and caCORE
		Software Development Kit (SDK)</li>

		<li>Development of extensive automated system testing framework
		to validate various components of the infrastructure</li>
	</ul>
</ul>

<p>In addition to the above mentioned highlights, caGrid 1.0
infrastructure contains the following tools:</p>

<p><b>Introduce Toolkit</b>: is a service creation toolkit built by
caGrid team. It supports easy developement and deployment of caBIG
compatible grid enabled data and analytical services. Introduce toolkit
reduces the service developers needing to manage the low level details
of the WSRF specification and integration with the Globus Toolkit.</p>

<p><b>Grid Authentication and Authorization of Reliably
Distributed Services (GAARDS)</b>: provides services and tools for grid wide
administration and security enforcement for services that are deployed
on caGrid infrastructure. GAARDS consists of following security
components:</p>

<ul>
	<ul>
		<li><b>Dorian</b>: allows for the provision and management of
		user accounts, providing an integration point between external
		security domains and the grid.</li>
		<li><b>Grid Grouper</b>: provides a group-based authorization
		solution, wherein grid services and applications enforce authorization
		policy based upon group memberships defined and managed at the grid
		level.</li>
		<li><b>Grid Trust Services(GTS)</b>: provides a mechanism for
		maintaining and provisioning a federated trust fabric of certified
		authorities in caGrid</li>
	</ul>
</ul>
</p>
