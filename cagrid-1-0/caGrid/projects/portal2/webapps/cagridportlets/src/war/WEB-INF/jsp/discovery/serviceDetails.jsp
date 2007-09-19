<%@ include file="/WEB-INF/jsp/include.jsp"%>
<script src="<c:url value="/js/scriptaculous/prototype.js"/>"
	type="text/javascript"></script>
<script src="<c:url value="/js/scriptaculous/scriptaculous.js"/>"
	type="text/javascript"></script>
<script type="text/javascript">
	//<![CDATA[
    function toggleDiv(divId, nodeId){
    	var node = document.getElementById(nodeId);
    	var div = document.getElementById(divId);
    	if(div.style.display == ""){
    		new Effect.BlindUp(div, {duration: 0.2});
    		node.className = "exp_node";
    	}else{
    		new Effect.BlindDown(div, {duration: 0.2});
    		node.className = "coll_node";
    	}
    	
    }
    // ]]>
</script>

<style type="text/css">
<!--
.exp_node{
	list-style-image: url(<c:url value="/images/expandbtn2.gif"/>);
}
.coll_node{
	list-style-image: url(<c:url value="/images/collapsebtn2.gif"/>);
}
.leaf_node{
	/*list-style-image: url(<c:url value="/images/iconarro.gif"/>);*/
	list-style-image: none;
	list-style-type: circle;
}
-->
</style>

<portlet:actionURL var="action"/>

<form:form action="${action}">
	<c:if test="${empty gridServiceUrl}">Enter a </c:if>Grid Service URL:
	<input name="gridServiceUrl" type="text" value="<c:out value="${gridServiceUrl}"/>"/>
	<input type="submit" value="Show"/>
</form:form>

<c:choose>
	<c:when test="${!empty gridServiceUrl and empty gridService}">
		No grid service found for <c:out value="${gridServiceUrl}"/>
	</c:when>
	<c:when test="${!empty gridService}">
		<c:set var="sd"
			value="${gridService.serviceMetadata.serviceDescription}" />
		<c:set var="rc"
			value="${gridService.serviceMetadata.hostingResearchCenter}" />
		<div class="serviceDetails">
		<ul>
			<li class="coll_node">Summary</li>
			<ul class="leaf_node">
				<li>Name (version):&nbsp; <c:out value="${sd.name}" />&nbsp; <c:out
					value="${sd.version}" /></li>
				<li>Status:&nbsp; <c:out value="${gridService.currentStatus}" /></li>
				<li>URL:&nbsp; <c:out value="${gridService.url}" /></li>
				<li>Description:&nbsp; <c:out value="${gridService.serviceMetadata.serviceDescription.description}" />
				</li>
			</ul>
			
			
			<c:set var="rcInfoNode">
				<portlet:namespace />-rcInfo</c:set>
			<!-- BEGIN HOSTING RESEARCH CENTER -->
			<li class="exp_node" id="<c:out value="${rcInfoNode}"/>-node"
				onclick="javascript:toggleDiv('<c:out value="${rcInfoNode}"/>', '<c:out value="${rcInfoNode}"/>-node')">Hosting Research Center
			</li>
			<div id="${rcInfoNode}" style="display: none">
			<ul class="leaf_node">
				<li>Name:&nbsp; <c:out value="${rc.displayName}" /></li>
				<li>Description: <c:out value="${rc.description}" /></li>
				<li>Homepage: <a href="<c:out value="${rc.homepageUrl}"/>"
					target="_blank"> <c:out value="${rc.homepageUrl}" /> </a></li>
				<li>RSS Feed: <a href="<c:out value="${rc.rssNewsUrl}"/>"
					target="_blank"> <c:out value="${rc.rssNewsUrl}" /> </a></li>
				<li class="leaf_node">Address</li>
				<ul class="leaf_node">
					<li>Street 1: <c:out value="${rc.address.street1}" /></li>
					<li>Street 2: <c:out value="${rc.address.street2}" /></li>
					<li>Locality: <c:out value="${rc.address.locality}" /></li>
					<li>State/Province: <c:out value="${rc.address.stateProvince}" />

					</li>
					<li>Country: <c:out value="${rc.address.country}" /></li>
				</ul>
				
				<c:if test="${fn:length(rc.pointOfContactCollection) > 0}">
				<c:set var="rcPocsInfoNode">
					<portlet:namespace />-rcPocsInfo</c:set>
				<li class="exp_node" id="<c:out value="${rcPocsInfoNode}"/>-node"
					onclick="javascript:toggleDiv('<c:out value="${rcPocsInfoNode}"/>', '<c:out value="${rcPocsInfoNode}"/>-node')">Points of Contact</li>
				<div id="${rcPocsInfoNode}" style="display: none">
				<ul class="leaf_node">
					<c:forEach items="${rc.pointOfContactCollection}" var="rcPoc">

						<li class="leaf_node">Name:&nbsp; <c:out value="${rcPoc.person.firstName}" />&nbsp;<c:out
							value="${rcPoc.person.lastName}" />
						<ul class="leaf_node">
							<li>Phone:&nbsp; <c:out value="${rcPoc.person.phoneNumber}" /></li>
							<li>Email:&nbsp; <c:out value="${rcPoc.person.emailAddress}" /></li>
							<li>Affiliation:&nbsp; <c:out value="${rcPoc.affiliation}" /></li>
							<li>Role:&nbsp; <c:out value="${rcPoc.role}" /></li>
						</ul>
						</li>
					</c:forEach>
				</ul>
				</div>
				</c:if>
				
			</div>
			
			<!-- END HOSTING RESEARCH CENTER -->
			<c:set var="sdInfoNode"><portlet:namespace/>-sdInfo</c:set>
			<!-- BEGIN SERVICE DESCRIPTION -->
			<li class="exp_node" id="<c:out value="${sdInfoNode}"/>-node"
				onclick="javascript:toggleDiv('<c:out value="${sdInfoNode}"/>', '<c:out value="${sdInfoNode}"/>-node')">Service Description</li>
				<div id="<c:out value="${sdInfoNode}"/>" style="display: none">
				<ul class="leaf_node">
					
					<c:if test="${fn:length(sd.pointOfContactCollection) > 0}">
					<c:set var="sdPocsInfoNode">
						<portlet:namespace />-sdPocsInfo</c:set>
					<li class="exp_node" id="<c:out value="${sdPocsInfoNode}"/>-node"
						onclick="javascript:toggleDiv('<c:out value="${sdPocsInfoNode}"/>', '<c:out value="${sdPocsInfoNode}"/>-node')">Points Of Contact</li>
						<div id="<c:out value="${sdPocsInfoNode}"/>" style="display: none">
							<ul class="leaf_node">
								<c:forEach items="${sd.pointOfContactCollection}" var="sdPoc">
			
									<li>Name:&nbsp; <c:out value="${sdPoc.person.firstName}" />&nbsp;<c:out
										value="${sdPoc.person.lastName}" /></li>
									<ul class="leaf_node">
										<li>Phone:&nbsp; <c:out value="${sdPoc.person.phoneNumber}" /></li>
										<li>Email:&nbsp; <c:out value="${sdPoc.person.emailAddress}" /></li>
										<li>Affiliation:&nbsp; <c:out value="${sdPoc.affiliation}" /></li>
										<li>Role:&nbsp; <c:out value="${sdPoc.role}" /></li>
									</ul>

								</c:forEach>
							</ul>						
						</div>
					</c:if>
					
					<c:if test="${fn:length(sd.semanticMetadata) > 0}">
					<c:set var="sdSemMetInfoNode">
						<portlet:namespace />-sdSemMetInfo</c:set>
					<li class="exp_node" id="<c:out value="${sdSemMetInfoNode}"/>-node"
						onclick="javascript:toggleDiv('<c:out value="${sdSemMetInfoNode}"/>', '<c:out value="${sdSemMetInfoNode}"/>-node')">Semantic Metadata</li>
						<div id="<c:out value="${sdSemMetInfoNode}"/>" style="display: none">
							<ul class="leaf_node">
								<c:forEach items="${sd.semanticMetadata}" var="sdSemMet">
			
									<li>Concept Name:&nbsp; <c:out value="${sdSemMet.conceptName}" /></li>
									<ul class="leaf_node">
										<li>Definition:&nbsp; <c:out value="${sdSemMet.conceptDefinition}" /></li>
										<li>Code:&nbsp; <c:out value="${sdSemMet.conceptCode}" /></li>
									</ul>
									
								</c:forEach>
							</ul>						
						</div>
					</c:if>
					
					<c:if test="${fn:length(sd.serviceContextCollection) > 0}">
					<c:set var="sdSvcCtxsInfoNode">
						<portlet:namespace />-sdSvcCtxsInfo</c:set>					
					<li class="exp_node" id="<c:out value="${sdSvcCtxsInfoNode}"/>-node"
						onclick="javascript:toggleDiv('<c:out value="${sdSvcCtxsInfoNode}"/>', '<c:out value="${sdSvcCtxsInfoNode}"/>-node')">Service Contexts</li>
						<div id="<c:out value="${sdSvcCtxsInfoNode}"/>" style="display: none">
							<ul class="leaf_node">
								<c:forEach items="${sd.serviceContextCollection}" var="sdSvcCtx" varStatus="sdSvcCtxStatus">
			
									<li>Context Name:&nbsp; <c:out value="${sdSvcCtx.name}" />
									<ul class="leaf_node">
										<li>Description:&nbsp; <c:out value="${sdSvcCtx.description}" /></li>
										
										<c:if test="${fn:length(sdSvcCtx.operationCollection) > 0}">
										<c:set var="sdOpsInfoNode">
											<portlet:namespace />-sdOpsInfo<c:out value="${sdSvcCtxStatus.index}"/></c:set>										
										<li class="exp_node" id="<c:out value="${sdOpsInfoNode}"/>-node"
											onclick="javascript:toggleDiv('<c:out value="${sdOpsInfoNode}"/>', '<c:out value="${sdOpsInfoNode}"/>-node')">Operations</li>
											<div id="<c:out value="${sdOpsInfoNode}"/>" style="display: none">
												<ul class="leaf_node">
													<c:forEach items="${sdSvcCtx.operationCollection}" var="op" varStatus="opStatus">
								
														<li>Operation Name:&nbsp; <c:out value="${op.name}" />
														<ul class="leaf_node">
															<li>Description:&nbsp; <c:out value="${op.description}" /></li>
															
															<c:if test="${fn:length(op.inputParameterCollection) > 0}">
															<c:set var="sdOpsInputInfoNode">
																<portlet:namespace />-sdOpsInputInfo<c:out value="${sdSvcCtxStatus.index}"/>-<c:out value="${opStatus.index}"/></c:set>										
															<li class="exp_node" id="<c:out value="${sdOpsInputInfo}"/>-node"
																onclick="javascript:toggleDiv('<c:out value="${sdOpsInputInfoNode}"/>', '<c:out value="${sdOpsInputInfoNode}"/>-node')">Input Parameters</li>
																<div id="<c:out value="${sdOpsInputInfoNode}"/>" style="display: none">
																	<ul class="leaf_node">
																		<c:forEach items="${op.inputParameterCollection}" var="inputParam">
													
																			<li>Parameter Name:&nbsp; <c:out value="${inputParam.name}" />
																			<ul class="leaf_node">
																				<li>Index:&nbsp; <c:out value="${inputParam.index}" /></li>
																				<li>Is Array?:&nbsp; <c:out value="${inputParam.array}" /></li>
																				<c:if test="${inputParam.array}">
																					<li>Dimensionality:&nbsp; <c:out value="${inputParam.dimensionality}" /></li>
																				</c:if>
																				<li>QName:&nbsp; <c:out value="${inputParam.QName}" /></li>
																			</ul>
																			</li>
																		</c:forEach>
																	</ul>						
																</div>
															</c:if>
															
															<c:if test="${!empty op.output}">
																<li>Output:</li>
																<ul class="leaf_node">
																	<li>Is Array?:&nbsp; <c:out value="${op.output.array}" /></li>
																	<c:if test="${op.output.array}">
																		<li>Dimensionality:&nbsp; <c:out value="${op.output.dimensionality}" /></li>
																	</c:if>
																	<li>QName:&nbsp; <c:out value="${op.output.QName}" /></li>
																</ul>
															</c:if>															
																												
														</ul>
														</li>
													</c:forEach>
												</ul>						
											</div>
										
										</c:if>
										

										
									</ul>
									</li>
								</c:forEach>
							</ul>						
						</div>
					
					</c:if>
				</ul>
				</div>
			
			<!-- END SERVICE DESCRIPTION -->
		</ul>
		</div>
	</c:when>
	<c:otherwise>
No service is currently selected.
	</c:otherwise>
</c:choose>
