<!DOCTYPE html>
<html>
<head>
	<meta name="layout" content="main"/>
	<title><g:message code="quotes.landingpage.title.txt"/></title>
</head>

<body>

	<script type="text/javascript">
		window.hpelite.params.quotesLPVM = {
			elementId: "Quotes",
			quoteCheckoutUrl: '<g:createLink mapping="quoteCheckout"/>',
			quoteSummaryUrl: '<g:createLink mapping="quoteSummary"/>',
			attachmentDownloadUrl: '<g:createLink mapping="quoteAttachment"/>',
			urlPRCheckoutSummary: '<g:createLink mapping ="prCheckoutSummaryPage"/>',
			urlPOCheckoutSummary: '<g:createLink mapping ="purchaseOrderSummary"/>',
			urlCreatePR: '<g:createLink mapping ="createPRFromQuote"/>',
			urlCreatePO: '<g:createLink mapping ="createPOFromQuote"/>',
			urlDeleteQuote: '<g:createLink mapping ="removeDocument"/>',
			urlCheckEmpRequestor: "<g:createLink mapping ='checkOrderForEmpoweredRequester'/>",
			urlCopyQuote: '<g:createLink mapping ="copyQuote"/>',
			urlReviseQuote: '<g:createLink mapping ="reviseQuote"/>',
			validStatusString:'<g:message code="favorites.dropdown.validfaves.label"/>',
			qouteLable:'<g:message code="quotes.h1.text"/>',
	 		regionCode:'<%=session.getAttribute("regionCode") %>',
			listQuoteData: ${result},
			createdIn:'${createdIn}',
			callControllerName:'quotes',
			controllerAction:'retrieveQuotesInfo',
			personalUser: '<%=session.getAttribute("PersonalUser") %>',
			urlQuoteList: '<g:createLink mapping="quotes"/>',
			defaultPlaceholder: "<g:message code='quotes.search.text'/>",
			isTeleWebAgentEnabled:'<%=session.getAttribute("isTeleWebAgentEnabled")%>',
			eliteSFDC:'<%=session.getAttribute("EliteSFDC")%>',
			versionEnabledRegion:'<%= session.currentOrganization.versionEnabledRegion%>',
			maxVersionAllowed:'<%=session.currentOrganization.maxVersionAllowed %>'
		};

		window.hpelite.params.quotesLPVM.columns = [
			{id:'date',name:'<g:message code="featurecards.quotes.col.quote.date.text"/>',sortOrder:'desc'}, 
			{id:'docExpiresOn',name:'<g:message  encodeAs="JavaScript" code="default.expires.text"/>'},
			{id:'userName',name:'<g:message code="featurecards.quotes.col.created.by.text"/>'},		                                		
			{id:'number',name:"<g:message code='featurecards.quotes.col.quote.number.text'/>"},  
			{id:'quoteName',name:"<g:message code='featurecards.quotes.col.quote.name.text'/>"},
			{id:'amount',name:'<g:message code="default.amount.text"/>'},
			{id:'action',name:'<g:message code="default.action.text"/>',sortable: false}
		];
	</script>


	<div id="Quotes" class="main">
		<div class="wrapper">
			<!-- Start the quotes wrapper -->
			<div class="main-inner quotes-list-lp">
				
				<div class="js-parent spinner-overlay" ></div>
				<div class="js-parent spinner-inner" ></div>
				
				<div class="row header">
					<g:if test="${session.EprocFlag != 'true' || (session.EprocFlag == 'true' && session.B2BiGenericUser == 'true')}" >
						<h1><g:message code="quotes.h1.text"/></h1>
					</g:if>
					<g:else>
						<h1><g:message code="featurecards.quotes.title.text"/></h1>
					</g:else>
				</div>

<%-- ::::::::::::::::::::::: PAGINATION BAR ::::::::::::::::::::::: --%>

				<section class="row table-options pagination">
					<div class="t-right width100  b-border b-padding1">
						<span class="m-none p-inline" data-set="text2">
							<i class="append-around padding0 r-margin075"><g:message code='default.show.text'/></i>
						</span>
						
						
						<div class="inline-block relative p-inline">
							<select name="select_quotes" class="selecter_basic scrollbar" data-bind="value:statusFilter">
								<g:if test="${session.EprocFlag != 'true'}">
									<sec:ifAllGranted roles="ALL_QUOTES_VIEWER">
										<option value="AllQuotes"><g:message code="default.all.quotes"/></option>
									</sec:ifAllGranted>
								</g:if>	
								<g:if test="${session.EprocFlag == 'true'}">
									<!-- ko if: navViewModel.AllQuotesCapabilityInEProc() == true -->
										<option value="AllQuotes"><g:message code="default.all.quotes"/></option>
									<!-- /ko -->
								</g:if>
								<g:if test="${session.EprocFlag != 'true' || session.B2BiGenericUser != 'true'}" >
									<option value="MyQuotes"><g:message code="default.myquotes.text"/></option>
								</g:if>
							</select>
						</div>
						
						<span class="m-none p-inline" data-set="text2">
							<i class="append-around"><g:message code="quotes.created.text"/></i>
						</span>
					
						<div class="inline-block relative p-inline">
							<select name="select_quotes_createdBy" class="selecter_basic scrollbar" data-bind="value:createdIn,event:{change:changeCreatedFilter}">
								<option value="StoreAndAgent"><g:message code="quotes.store.and.agent.text"/></option>
								<option value="Store"><g:message code="quotes.store.text"/></option>
								<option value="Agent"><g:message code="quotes.agent.text"/></option>
							</select>
						</div>
						
						<span class="m-none" data-set="text2">
							<i class="append-around p-normal"><g:message code="quotes.containing.text"/></i>
						</span>
						<span class="wrapper width20 t-width25 m-width100 searchOuter">
							<input class="width85 searchTextBox xsmall p-visible p-noborder p-width3in" placeholder="<g:message code="quotes.search.text"/>" type="text" data-bind="value:quoteNameOrNumber, valueUpdate:['afterkeydown','propertychange','input'], searchFieldKeyHandler:{action:'searchQuoteNameOrNumber'}"/>
							<a href="javascript:void(0)" class="quote-search right" data-bind="event:{click:searchQuoteNameOrNumber}"></a>
						</span>
					</div>

					<!-- ko if: quoteResultArray().length > 0  -->

					<div class="t-right width100 t-margin1">
						<div class="screenonly" data-set="displaying"><g:render template="/partial-temps/displaying" /></div>
						<div class="tabletonly" data-set="displaying"><g:render template="/partial-temps/displaying" /></div>
					
						<g:render template="/landingPages/view_per_page" />
					
						<div class="screenonly right" data-set="pagination"><g:render template="/landingPages/pagination" /></div>
					</div>
					<!-- /ko -->
				</section>

<%-- ::::::::::::::::::::::: PAGINATION BAR ::::::::::::::::::::::: --%>
		<!-- ko if: quoteResultArray().length > 0  -->


				<!-- start the table -->
				<article class="tabular special-table">
					<i class="bar"></i>
					
					<table class="flipscroll">
						<thead>
							<tr>
								<!-- ko foreach: columns-->
								<th data-bind="attr: {id: id, 'class': id() + '-width'}, css: {'selectedBackground': isSorted()}">
									<div>
										<!-- ko if: $data.id()=='amount' -->
										<div class="note tooltip tooltip-hover fixtop t-fixright m-fixright inline-block">
											<a class="black" data-bind="html:name"></a>
											<div class="row fly-away" style="display: none;">
												<p class="row">
													<g:message code="not.including.taxes.shipping.handling.fees.text"/>
												</p>
												<span class="arrow_pointer"><g:message code="default.arrow.text"/></span>
												<span class="arrow_pointer-new"><g:message code="default.arrow.text"/></span>
											</div>
										</div>
										<!-- /ko -->
									
										<!-- ko ifnot: $data.id()=='amount' -->
										<a class="black p-small" data-bind="html:name"></a>
										<!-- /ko -->
										<!-- ko if: sortable -->
										<span class="sorting" data-bind="css: {'off': !isSorted(), 'ascending': sortOrder()=='asc', 'descending': sortOrder()=='desc'}"></span>
										<!-- /ko -->
									</div>
								</th>
								<!-- /ko -->
							</tr>
						</thead>								

						<tbody data-bind="foreach: quoteResultArray">
							<tr>
								<td data-bind="text:creationDate, attr:{title:creationDate}" class="p-small2"></td>

								<td>									
									<span data-bind="text:docExpiresOn, attr:{title:docExpiresOn}, css:{'red': $data.documentExpired()}" class="p-small2"></span>
									<g:if test="${session.EprocFlag != 'true' || session.B2BiGenericUser != 'true'}" >
									 <!-- ko if: ($data.userId() == userID) && (quoteType() != 'BMI') && (quoteType() != 'Watson')-->									  
										<div>
											<a href="#" data-bind="click:function(){$root.showDeleteModalFlag(true);$root.quoteUUID(uuid());$root.quoteName(quoteName());$root.docStatus(docStatus());$root.quoteVersionLength(quoteVersions().length)}" class="noprint"><g:message code="account.message.delete.selected.text"/></a>
										</div>
									 <!-- /ko -->
									 </g:if>
										
								</td>
								<td>
								<!-- ko if: (userName() != 'null null')-->
									<p class="ellipsis p-small2" data-bind="text:userName, attr:{title:userName}" class="p-small2"></p>
								<!-- /ko -->
								</td>
								<td class="ellipsis visible" data-bind="css:{visible : quoteVersions().length > 1 }">
									<div class="relative p-small2">
										<div data-bind="if:quoteType() !='BMI'" class="p-small2"><span data-bind="text:quoteNumber()" class="p-small2"></span></div>
										<div data-bind="if:quoteType() =='BMI'" class="p-small2"><span data-bind="text:quoteNumber()+'-'+quoteVersionNumber()" class="p-small2"></span></div>
										<g:if test="${session.EprocFlag != 'true' || session.B2BiGenericUser != 'true'}" >
										<!-- ko if: ((quoteType() != 'BMI') && (quoteType() != 'Watson'))-->
										<a class="small2 left inline-block instantLoad noprint" href=".html" data-bind="click:function(){getEliteUrl({'approverFlag':approverFlag(),'quoteUUID':uuid(),'docStatus':docStatus(),'copyFlag':true,'docCurrency':docCurrency(),'quoteType':quoteType()},$root.urlCopyQuote)}"><g:message code="default.save.as.text" /></a>
										<!-- ko if: ($root.versionEnabledRegion == "true" && ($root.maxVersionAllowed > quoteVersionNumber()))-->
										<a class="small2 left inline-block instantLoad noprint" href=".html" data-bind="click:function(){getEliteUrl({'quoteUUID':uuid(),'createVersion':true},$root.urlReviseQuote)}"><g:message code="default.revise.quote.text" /></a>
										</g:if>
										<!-- /ko -->
										<!-- /ko -->
										
										<!-- ko if: quoteVersions().length > 1 -->
										<div class="row tooltip tooltip-hover fixright inline-block left p-small2" >
											<a href="#" class="buffer" data-bind="click:function(data,event){landingEvents.popup(event)}"></a>
											
											<div class="flyout buffer-wrapper" style="display: none;">
												<div class="row flyout-wrapper">
													<div class="row flyout-content">
													
														<div class="row">
															<span class="col20 medium normal b-border b-padding025 r-margin05"><g:message code="featurecards.quotes.col.quote.date.text"/></span> 
															<span class="col25 medium normal b-border b-padding025 r-margin05"><g:message code="featurecards.quotes.col.quote.number.text"/></span>
															<span class="col50 medium normal b-border b-padding025"><g:message code="featurecards.quotes.col.quote.name.text"/></span>
														</div>
														<ul class="row popup-list">
															<!-- ko foreach: quoteVersions -->
															<li class="row line-height2">
																<div class="col20 r-margin05 medium">
																	<span data-bind="text:creationDate"></span>
																</div>
																<div class="col25 r-margin05 medium">
																	<div data-bind="if:$data.quoteType !='BMI'"><span data-bind="text:quoteNumber"></span></div>
																	<div data-bind="if:$data.quoteType =='BMI'"><span data-bind="text:quoteNumber+'-'+quoteVersionNumber"></span></div>
																	<g:if test="${session.EprocFlag != 'true' || session.B2BiGenericUser != 'true'}" >
																		<!-- ko if: (($data.quoteType != 'BMI') && ($data.quoteType != 'Watson'))-->
																			<a class="instantLoad" href="javascript:void(0);" data-bind="click:function(){getEliteUrl({'approverFlag':approverFlag,'quoteUUID':$data.uuid,'docStatus':docStatus,'copyFlag':true,'isVersion':true,'docCurrency':$root.docCurrency,'quoteType':$data.quoteType},$root.urlCopyQuote)}"><g:message code="default.save.as.text" /></a>
																		<!-- /ko -->
																	</g:if>
																</div>
		
																<div class="col50 ellipsis medium">
																	<!-- ko if: (headerData.userName == userName || $data.quoteType == 'BMI' || $data.quoteType == 'Watson')-->
																	<a class="instantLoad" href="javascript:void(0);" data-bind="click:function(){getEliteUrl({'approverFlag':approverFlag,'quoteUUID':$data.uuid,'docStatus':docStatus,'copyFlag':false,'isVersion':true,'docCurrency':$root.docCurrency,'quoteVersionNumber':quoteVersionNumber,'quoteType':$data.quoteType,'quoteID':quoteNumber},$root.quoteCheckoutUrl)}"><g:message code="default.version.text"/> &nbsp;<!-- ko text: quoteVersionNumber --><!-- /ko -->:&nbsp;<!-- ko text:quoteName --><!-- /ko --></a>
																	<ul class="row popup-list">
																	<!-- ko foreach: $data.attachments -->
																	<li>
																	<a href="#" data-bind="text:$data,click:function(){$root.fileAttachmentDownload({fileName:$data,uuid:$parent.uuid})}" ></a>
																	</li>
																	<!-- /ko -->
																		</ul>
																	<!-- /ko -->
		
																	<!-- ko if: headerData.userName != userName && $data.quoteType != 'BMI' && $data.quoteType != 'Watson'-->
																	<a class="instantLoad" href="javascript:void(0);" data-bind="click:function(){getEliteUrl({'approverFlag':approverFlag,'quoteUUID':$data.uuid,'docStatus':docStatus,'copyFlag':false,'isVersion':true,'docCurrency':$root.docCurrency,'quoteVersionNumber':quoteVersionNumber,'quoteType':$data.quoteType,'quoteID':quoteNumber},$root.quoteSummaryUrl)}"><g:message code="default.version.text"/> &nbsp;<!-- ko text: quoteVersionNumber --><!-- /ko -->:&nbsp;<!-- ko text:quoteName --><!-- /ko --></a>
																	<!-- /ko -->
																</div>
															</li>
															<!-- /ko -->
														</ul>

													</div>
													<span class="arrow_pointer"></span>
													<i class="close">x</i>
												</div>
											</div>	
										</div>
										<!-- /ko -->
										
									</div>						
								</td>
								
								
								
								<td class="visible">
									<div class="y-padding0 width85 left btn_height_lock blue ellipsis instantLoad">
										<!-- ko if: (headerData.userName == userName() || $data.quoteType() == 'BMI' || $data.quoteType() == 'Watson') -->
											<a href="#" data-bind="text:quoteName,attr:{title:quoteName},click:function(){getEliteUrl({'quoteCreateBy':userId(),'approverFlag':approverFlag(),'quoteUUID':uuid(),'docStatus':docStatus(),'isVersion':false,'copyFlag':false,'quoteVersionNumber':quoteVersionNumber(),'quoteType':quoteType(),'quoteID':quoteNumber()},$root.quoteCheckoutUrl)}" class="p-small2"></a>
										<!-- /ko -->
										<!-- ko if: headerData.userName != userName() && $data.quoteType() != 'BMI' && $data.quoteType() != 'Watson'-->
											<a href="#" data-bind="text:quoteName,attr:{title:quoteName},click:function(){getEliteUrl({'quoteCreateBy':userId(),'approverFlag':approverFlag(),'quoteUUID':uuid(),'docStatus':docStatus(),'quoteVersionNumber':quoteVersionNumber(),'quoteType':quoteType(),'quoteID':quoteNumber()},$root.quoteSummaryUrl)}" class="p-small2"></a>
										<!-- /ko -->
									</div>
									<div class="width1em right r-margin05"><g:render template="/landingPages/attachedFilestoolTip"></g:render></div>
								</td>
								
								<td class="align-right" class="p-small2"><span class="currency p-xxsmall spacefix" data-bind="text:docCurrency"></span> <!-- ko text:totalPrice()--><!-- /ko --></td>

								<td class="action">		
								<g:if test="${session.EprocFlag != 'true'}">						
									<!-- ko ifnot: $data.documentExpired() -->
										<!-- ko if: $root.userRole() == 0 -->
										<sec:ifAnyGranted roles="ORDER_VIEWER">
										<!-- ko if: ($root.eliteSFDC() == "" || ($root.isTeleWebAgentEnabled() != "true" && $root.eliteSFDC() == "true"))-->
											<input type="button" class="primary approval instantLoad" data-bind="click:function(){getEliteUrl({'approverFlag':approverFlag(),'quoteUUID':uuid(),'docStatus':docStatus(),'copyFlag':true, 'docCurrency':docCurrency(),'quoteVersionNumber':quoteVersionNumber(),'quoteType':quoteType(),'quoteID':quoteNumber()},$root.urlCreatePO)}" value='<g:message code='featurecards.quotes.submit.button.text'/>' />
										<!-- /ko -->
										<!-- ko if: ($root.isTeleWebAgentEnabled() == "true" && $root.eliteSFDC() == "true")-->
											<input type="button" class="primary approval" data-bind="css:{'disabled-bg':($root.isTeleWebAgentEnabled() && $root.eliteSFDC())},disable:($root.isTeleWebAgentEnabled() && $root.eliteSFDC())" value='<g:message code="featurecards.quotes.submit.button.text"/>' />
										<!-- /ko -->
										</sec:ifAnyGranted>
										<!-- /ko -->
										<!-- ko if: $root.userRole() == 1 || $root.userRole() == 2 -->
										<input type="button" data-bind="click:function(){getEliteUrl({'approverFlag':approverFlag(),'quoteUUID':uuid(),'docStatus':docStatus(),'copyFlag':true,'docCurrency':docCurrency(),'quoteVersionNumber':quoteVersionNumber(),'quoteType':quoteType(),'quoteID':quoteNumber()},$root.urlCheckEmpRequestor)}" class="primary approval" value='<g:message code="featurecards.quotes.request.button.text"/>' />
										<!-- /ko -->
										<!-- ko if: $root.userRole() == 3 -->
										<input type="button" data-bind="click:function(){getEliteUrl({'approverFlag':approverFlag(),'quoteUUID':uuid(),'docStatus':docStatus(),'copyFlag':false,'isVersion':false,'docCurrency':docCurrency(),'quoteVersionNumber':quoteVersionNumber(),'quoteType':quoteType(),'quoteID':quoteNumber()},$root.quoteCheckoutUrl)}"class="primary approval" value='<g:message code="default.button.edit.label"/>' />
										<!-- /ko -->					
									<!-- /ko -->	
								</g:if>	
								<g:if test="${session.EprocFlag == 'true'}">
									<input type="button" data-bind="click:function(){getEliteUrl({'approverFlag':approverFlag(),'quoteUUID':uuid(),'docStatus':docStatus(),'copyFlag':false,'isVersion':false,'docCurrency':docCurrency(),'quoteVersionNumber':quoteVersionNumber(),'quoteType':quoteType(),'quoteID':quoteNumber()},$root.quoteCheckoutUrl)}"class="primary approval" value='<g:message code="default.view.text"/>' />
								</g:if>									
								</td>
							</tr>	
						</tbody>
					</table>
				</article>
				<!-- end the table -->
					<!-- start the lower pagination -->
	
					<section class="pagination right t-margin1">
						<g:render template="/landingPages/pagination" />
					</section>
		
					<!-- end the lower pagination -->
						
				<!-- /ko -->				
				<div class="modal row noprint" data-bind="css:{'show':$root.showDeleteModalFlag()}">
					<section class="dialog noprint">
						<div class="row">						
							<i class="close noprint" data-bind="click:function(){$root.showDeleteModalFlag(false)}">X</i>
							<div class="row padding1">
								<!-- ko if: $root.quoteVersionLength()>0 -->
									<p class="t-padding1 large thin noprint"><g:message code='quotes.deleteAllVerions.confirm.text'/></p>
									<div class="b-padding1 large thin">
										<span data-bind="text:$root.quoteName"></span> 
										<span>?</span>
		                            </div>
										
								<!-- /ko  -->
								<!-- ko ifnot: $root.quoteVersionLength()>0 -->	
									<p class="t-padding1 large thin noprint"><g:message encodeAs="JavaScript" code='quotes.delete.confirm.text'/></p>
									<div class="b-padding1 large thin">
		                                  <span data-bind="text:$root.quoteName"></span> 
		                                  <span>?</span>
		                            </div>
								<!-- /ko -->									
								<aside class="col100 align-right t-padding1 t-border">
									<input type="button" class="secondary noprint" value="<g:message code='default.button.cancel.label'/>" data-bind="click:function(){$root.showDeleteModalFlag(false)}"/>
										<!-- ko if: $root.quoteVersionLength()>0 -->
											<input type="button" class="primary l-margin05" value="<g:message code='default.quote.deleteAllVersions.text'/>" data-bind="click:function(){deleteDocument()}"/>
										<!-- /ko  -->
										<!-- ko ifnot: $root.quoteVersionLength()>0 -->	
											<input type="button" class="primary l-margin05" value="<g:message code='default.button.delete.label'/>" data-bind="click:function(){deleteDocument()}"/>
										<!-- /ko -->	
								</aside>
							</div>								
						</div>	
					</section>
				</div>		
				<!-- ko if: quoteResultArray().length < 1  -->
					<article class="tabular noresults clear">
						<p class="error">
							<g:message code="featurecards.quotes.noQuotes.text" />
						</p>							
					</article>
				<!-- /ko -->									
				
			</div>
			<!-- end the quotes wrapper -->
		</div>
	</div>
	<r:require module="landingPage"/>
	<g:javascript src="postForm.js"/>
	<script>
		var userID='<%=session.getAttribute("usercode") %>';  
		var userApp='<%=session.getAttribute("userrole").toLowerCase() %>';
		var userState='<%=session.getAttribute("userstate") %>';
		var userAccount='${session.currentOrganization?.name}' + ':' + '<%= session.getAttribute("country") %>';
		var userAccountId='${session.currentOrganization?.organizationId}';
		var productCategory="";
		var productCategorySeries='';
		var elitepublishID='${grailsApplication.config.omniture.b2b.websectionid}';
		var countryCode='<%=session.getAttribute("countrycode") %>';
		var currencyCode='<%=session.getAttribute("currencycode") %>';
		var busOrgId ='${session.currentOrganization?.eboOrganizationID}';
		var ncrFlvl=<%=session.currentOrganization?.ncrfLevel %>;
		var omniType='${grailsApplication.config.omniture.b2b.pagetype}';  
		var userLanguage='<%=session.getAttribute("userlang") %>';
		var userLanguage = userLanguage.replace('_','-');
		var rootorg_id='<%=session.getAttribute("rootorg_id") %>';
		var rootorg_name='<%=session.getAttribute("rootorg_name") %>';
		var root_geographic='<%=session.getAttribute("root_geographic") %>';
		
		document.write( '<html lang="' + userLanguage.toLowerCase() + '" encoding="on" >' );  
	 	$('head').append( '<meta name="target_country" content="' + countryCode + '">' );
	 	$('head').append( '<meta name="segment" content="commercial.large">' );
		$('head').append( '<meta name="lifecycle" content="buy.checkout">' );
	    $('head').append( '<meta name="web_section_id" content="' + elitepublishID + '">' );
		$('head').append( '<meta name="business_unit" content="shared">' );
			
			
		var hpmmd = window.hpmmd || {
			type : omniType,
			"page" : {		
				"site" : "elite",
				"section" : 'documents',
				"subsection" : 'quotes',
				"page_function" : 'checkout',
				"name" : 'quote'
			},
			"user" : {
				"id": userID,
				"account" : userAccount,
				"account_id" : userAccountId,
				"role" : userApp,
				"account_eboid" : busOrgId, 
				"account_ncrflevel" : ncrFlvl,
				"state" : userState,
				"rootorg_id": rootorg_id,
				"rootorg_name": rootorg_name,
				"root_geographic": root_geographic
			},
			product : {},
			search : {},
			legacy : {},
			promo : {},
			version : "WASH"
		};	
	</script>   
   <!-- <script type="text/javascript" src="https://ssl.www8.hp.com/h10000/cma/ng/lib/bootstrap/metrics.js"></script> -->
</body>
</html>
