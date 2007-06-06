<%@ include file="/WEB-INF/jsp/include.jsp"%>
<script src="<c:url value="/js/scriptaculous/prototype.js"/>"
	type="text/javascript"></script>
<script src="<c:url value="/js/scriptaculous/scriptaculous.js"/>"
	type="text/javascript"></script>
<script type="text/javascript">
	//<![CDATA[
    function toggleDiv(divId){
    	//new Effect.toggle(divId);
    	var node = document.getElementById(divId);
    	if(node.style.display == ""){
    		new Effect.BlindUp(node, {duration: 0.2});
    	}else{
    		new Effect.BlindDown(node, {duration: 0.2});
    	}
    }
    // ]]>
</script>
<c:choose>
	<c:when test="${!empty gridService}">
		<c:set var="sd"
			value="${gridService.serviceMetadata.serviceDescription}" />
		<c:set var="rc"
			value="${gridService.serviceMetadata.hostingResearchCenter}" />
		<div class="serviceDetails">
		<ul>
			<li>Summary
			<ul>
				<li>Name (version):&nbsp; <c:out value="${sd.name}" />&nbsp; <c:out
					value="${sd.version}" /></li>
				<li>Status:&nbsp; <c:out value="${gridService.status}" /></li>
				<li>URL:&nbsp; <c:out value="${gridService.url}" /></li>
				<li>Description:&nbsp; <c:out value="${gridService.status}" />
				</li>
			</ul>
			</li>
			
			<c:set var="rcInfoNode">
				<portlet:namespace />-rcInfo</c:set>
			<!-- BEGIN HOSTING RESEARCH CENTER -->
			<li>Hosting Research Center <a
				href="javascript:toggleDiv('<c:out value="${rcInfoNode}"/>')">View/Hide</a>
			<div id="${rcInfoNode}" style="display: none">
			<ul>
				<li>Name:&nbsp; <c:out value="${rc.displayName}" /></li>
				<li>Description: <c:out value="${rc.description}" /></li>
				<li>Homepage: <a href="<c:out value="${rc.homepageUrl}"/>"
					target="_blank"> <c:out value="${rc.homepageUrl}" /> </a></li>
				<li>RSS Feed: <a href="<c:out value="${rc.rssNewsUrl}"/>"
					target="_blank"> <c:out value="${rc.rssNewsUrl}" /> </a></li>
				<li>Address
				<ul>
					<li>Street 1: <c:out value="${rc.address.street1}" /></li>
					<li>Street 2: <c:out value="${rc.address.street2}" /></li>
					<li>Locality: <c:out value="${rc.address.locality}" /></li>
					<li>State/Province: <c:out value="${rc.address.stateProvince}" />

					</li>
					<li>Country: <c:out value="${rc.address.country}" /></li>
				</ul>

				<c:set var="rcPocsInfoNode">
					<portlet:namespace />-rcPocsInfo</c:set>
				<li>Points of Contact <a
					href="javascript:toggleDiv('<c:out value="${rcPocsInfoNode}"/>')">View/Hide</a>
				<div id="${rcPocsInfoNode}" style="display: none">
				<ul>
					<c:forEach items="${rc.pointOfContactCollection}" var="rcPoc">

						<li>Name:&nbsp; <c:out value="${rcPoc.firstName}" />&nbsp;<c:out
							value="${rcPoc.lastName}" />
						<ul>
							<li>Phone:&nbsp; <c:out value="${rcPoc.phoneNumber}" /></li>
							<li>Email:&nbsp; <c:out value="${rcPoc.emailAddress}" /></li>
							<li>Affiliation:&nbsp; <c:out value="${rcPoc.affiliation}" /></li>
							<li>Role:&nbsp; <c:out value="${rcPoc.role}" /></li>
						</ul>
						</li>
					</c:forEach>
				</ul>
				</div>
				</li>
			</div>
			</li>
			<!-- END HOSTING RESEARCH CENTER -->
			<c:set var="sdInfoNode"><portlet:namespace/>-sdInfo</c:set>
			<!-- BEGIN SERVICE DESCRIPTION -->
			<li>Service Description
				<a href="javascript:toggleDiv('<c:out value="${sdInfoNode}"/>')">View/Hide</a>
				<div id="<c:out value="${sdInfoNode}"/>" style="display: none">
				<ul>
					<c:set var="sdPocsInfoNode">
						<portlet:namespace />-sdPocsInfo</c:set>
					<li>Points Of Contact
						<a href="javascript:toggleDiv('<c:out value="${sdPocsInfoNode}"/>')">View/Hide</a>
						<div id="<c:out value="${sdPocsInfoNode}"/>" style="display: none">
							<ul>
								<c:forEach items="${sd.pointOfContactCollection}" var="sdPoc">
			
									<li>Name:&nbsp; <c:out value="${sdPoc.firstName}" />&nbsp;<c:out
										value="${sdPoc.lastName}" />
									<ul>
										<li>Phone:&nbsp; <c:out value="${sdPoc.phoneNumber}" /></li>
										<li>Email:&nbsp; <c:out value="${sdPoc.emailAddress}" /></li>
										<li>Affiliation:&nbsp; <c:out value="${sdPoc.affiliation}" /></li>
										<li>Role:&nbsp; <c:out value="${sdPoc.role}" /></li>
									</ul>
									</li>
								</c:forEach>
							</ul>						
						</div>
					</li>
					<c:set var="sdSemMetInfoNode">
						<portlet:namespace />-sdSemMetInfo</c:set>
					<li>Semantic Metadata
						<a href="javascript:toggleDiv('<c:out value="${sdSemMetInfoNode}"/>')">View/Hide</a>
						<div id="<c:out value="${sdSemMetInfoNode}"/>" style="display: none">
							<ul>
								<c:forEach items="${sd.semanticMetadata}" var="sdSemMet">
			
									<li>Concept Name:&nbsp; <c:out value="${sdSemMet.conceptName}" />
									<ul>
										<li>Definition:&nbsp; <c:out value="${sdSemMet.conceptDefinition}" /></li>
										<li>Code:&nbsp; <c:out value="${sdSemMet.conceptCode}" /></li>
										<%-- 
										<li>Order:&nbsp; <c:out value="${sdSemMet.order}" /></li>
										<li>Order Level:&nbsp; <c:out value="${sdSemMet.orderLevel}" /></li>
										--%>
									</ul>
									</li>
								</c:forEach>
							</ul>						
						</div>
					</li>
					<c:set var="sdSvcCtxsInfoNode">
						<portlet:namespace />-sdSvcCtxsInfo</c:set>					
					<li>Service Contexts
						<a href="javascript:toggleDiv('<c:out value="${sdSvcCtxsInfoNode}"/>')">View/Hide</a>
						<div id="<c:out value="${sdSvcCtxsInfoNode}"/>" style="display: none">
							<ul>
								<c:forEach items="${sd.serviceContextCollection}" var="sdSvcCtx" varStatus="sdSvcCtxStatus">
			
									<li>Context Name:&nbsp; <c:out value="${sdSvcCtx.name}" />
									<ul>
										<li>Description:&nbsp; <c:out value="${sdSvcCtx.description}" /></li>
										<c:set var="sdOpsInfoNode">
											<portlet:namespace />-sdOpsInfo<c:out value="${sdSvcCtxStatus.index}"/></c:set>										
										<li>Operations
											<a href="javascript:toggleDiv('<c:out value="${sdOpsInfoNode}"/>')">View/Hide</a>
											<div id="<c:out value="${sdOpsInfoNode}"/>" style="display: none">
												<ul>
													<c:forEach items="${sdSvcCtx.operationCollection}" var="op" varStatus="opStatus">
								
														<li>Operation Name:&nbsp; <c:out value="${op.name}" />
														<ul>
															<li>Description:&nbsp; <c:out value="${op.description}" /></li>
															<c:set var="sdOpsInputInfoNode">
																<portlet:namespace />-sdOpsInputInfo<c:out value="${sdSvcCtxStatus.index}"/>-<c:out value="${opStatus.index}"/></c:set>										
															<li>Input Parameters
																<a href="javascript:toggleDiv('<c:out value="${sdOpsInputInfoNode}"/>')">View/Hide</a>
																<div id="<c:out value="${sdOpsInputInfoNode}"/>" style="display: none">
																	<ul>
																		<c:forEach items="${op.inputParameterCollection}" var="inputParam">
													
																			<li>Parameter Name:&nbsp; <c:out value="${inputParam.name}" />
																			<ul>
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
															</li>															
														</ul>
														</li>
													</c:forEach>
												</ul>						
											</div>
										</li>
									</ul>
									</li>
								</c:forEach>
							</ul>						
						</div>
					</li>
				</ul>
				</div>
			</li>
			<!-- BEGIN SERVICE DESCRIPTION -->
		</ul>
		</div>
	</c:when>
	<c:otherwise>
No service is currently selected.
</c:otherwise>
</c:choose>
