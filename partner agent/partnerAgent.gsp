<!DOCTYPE html>
<html>
<head>
	<meta name="layout" content="main"/>	
</head>
<body>
	<script type="text/javascript">
	window.hpelite.params.partnerAgentVM = {
	
		elementId: "PartnerAgent",
		userOrganizationsUrl: '<g:createLink mapping="partnerAgentOrgs"/>',
		partnerAgentCountryUrl: '<g:createLink mapping="partnerAgentCountry"/>',
		partnerAgentCurrencyUrl: '<g:createLink mapping="partnerAgentCurrency"/>',
		partnerAgentSearchUrl: '<g:createLink mapping = "partnerAgentSearch"/>',
		impersonatePartnerAgentMap:'<%=session.getAttribute("IMPERSONATEPARTNERAGENTMAP")%>',
		columns:[
				{id:'accountName',name:"<g:message code='default.account.name.text'/>"},
		 		{id:'lastname',name:"<g:message code='default.last.name.text'/>, <g:message code='default.first.name.text'/>",sortOrder:'desc'}, 
		 		{id:'userID',name:"<g:message code='default.user.id.text'/>"},
		 		{id:'userRole',name:"<g:message code='default.user.role.text'/>"},
		 		{id:'action',name:"<g:message code='default.action.text'/>",sortable:false} 
		 		]
	};
	</script>
<div class="main">
	<div class="wrapper">
		<div id="PartnerAgent" class=" row relative t-margin2 b-border b-padding2 contentFields">
		<div id="partnerAgentMain">
		<h1 class="row b-margin12px b-margin125"><g:message code='partnerAgent.page.heading' /></h1>
		<ul class="col60 mobile100 partnerAgentFields">	
			<li class="row b-margin12px">		
				<div class="mobile100 tablet100 fieldNames">								
					<span><g:message code='account.message.account'/></span>
				</div>
				
				<div data-bind="visible: !showAccountDropdown(), foreach:$root.accountList">
					<span data-bind="text: organizationName"></span>
				</div>
				
				<div data-bind="visible: showAccountDropdown()" class="tablet100 mobile100 accountFields">			
					<div class="solid relative inline-block width100">
						<select id="partnerAgent_account" class="col100 tablet100 mobile100" data-bind="disable: $root.disableAccountDropdown, options: $root.accountList,
							 optionsCaption: '<g:message code="partnerAgent.please.select"/>', optionsText: 'organizationName',  optionsValue: 'organizationID', value:selectedAccountId, event:{change:loadCountryUsers}">
						</select>
					</div>
				</div>	
			</li>
			<li class="row b-margin12px">
				<div class="mobile100 tablet100 fieldNames">								
					<span><g:message code='partnerAgent.country.catalog.text'/></span>
				</div>
				
				<div data-bind="visible: !showCountryDropdown(), foreach:$root.countryListArray">
					<span data-bind="text: ctryName"></span>
				</div>
				
				<div class="tablet100 mobile100 accountFields">	
					<div data-bind="visible: showCountryDropdown()" class="tablet100 mobile100 accountFields">		
						<div class="solid relative inline-block width100">
							<select id="partnerAgent_country" class="col100 tablet100 mobile100" name="selectedCountry" data-bind="disable: $root.disableCountryDropdown, options: $root.countryListArray(), optionsText: 'ctryName',
							  	optionsValue: 'ctryId', value: $root.selectedCountryId, optionsCaption: '<g:message code="partnerAgent.please.select"/>', event:{change:loadCurrency}">
							 </select>
						 </div>
					</div>
				</div>
			</li>
			<li class="row b-margin12px">
				<div class="mobile100 tablet100 fieldNames">								
					<span><g:message code='partnerAgent.currency.text' /></span>
				</div>
				
				<div data-bind="visible: !showCurrencyDropdown(), foreach:$root.currencyListArray">
					<span data-bind="text: currName"></span>
				</div>
				
				<div class="tablet100 mobile100 accountFields">
					<div data-bind="visible: showCurrencyDropdown()" class="tablet100 mobile100 accountFields">			
						<div class="solid relative inline-block width100">
							<select id="partnerAgent_currency" class="col100 tablet100 mobile100" name="selectedCurrency" data-bind=" disable: $root.disableCurrencyDropdown, options: $root.currencyListArray(),
							 	optionsText: 'currName', optionsValue: 'currId', value:selectedCurrencyId, optionsCaption: '<g:message code="partnerAgent.please.select"/>' ,event:{change:loadUsers}" class="width20em">
							 </select>
						</div>
					</div>
				</div>
			</li>
			<li class="row b-padding5px">
				<div class="mobile100 tablet100 fieldNames">								
					<span><g:message code='partnerAgent.user.text' /></span>
				</div>
				
				<div data-bind="visible: !showUserDropdown()">
					<span data-bind="text:$root.displayUser"></span>
				</div>
				
				<div class="tablet100 mobile100 accountFields">	
					<div data-bind="visible: showUserDropdown()" class="tablet100 mobile100 accountFields">		
						<div class="solid relative inline-block width100">
							<select id="partnerAgent_user" class="col100 tablet100 mobile100" data-bind="disable: $root.disableUserDropdown, options: $root.userListArray,
								 optionsCaption: '<g:message code="partnerAgent.please.select"/>', optionsText: function(item) { return getUserDisplayName(item);},  
								 	optionsValue: 'businessPartnerNo', value:selectedUserId, event:{change:showImpersonate}">
							</select>
						</div>
					</div>
				</div>
			</li>
			<li class="row">
				<div class="mobile100 tablet100 invisible fieldNames">								
					 <g:message code='partnerAgent.or.text' />
				</div>
				<div class="tablet100 mobile100 accountFields">			
					<div class="solid relative inline-block width100 center">
					<g:message code='partnerAgent.or.text' />
					</div>
				</div>
			</li>
			<li class="row b-margin12px">
		        <div class="mobile100 tablet100 fieldNames">								
				    <span><g:message code='partnerAgent.search.user.text' /></span>
			    </div> 
		         <div class="tablet100 mobile100 accountFields">			
					<div class="solid relative inline-block width100">
						<input class="width100 searchTextBox xsmall p-visible p-noborder p-width3in" type="text" data-bind="value: searchText">
						<a href="javascript:void(0)" class="search-user right" data-bind="click:function(){loadPagination();}"></a>
					</div>
				</div>
		     </li>
		     <li class="row">
			    	 <div class="mobile100 tablet100 invisible fieldNames">								
						 <g:message code='partnerAgent.start.impersonating' />
					</div>
			     	<div class="tablet100 mobile100 accountFields" >			
						<div class="solid relative inline-block width100 text-right">
							<input id="startImpersonating" type="button" class="primary" value="<g:message code='partnerAgent.start.impersonating' />" data-bind="click: impersonateUser">
						</div>
					</div>
		     </li>	
		</ul>
		</div>
		<!-- ko if: ($root.errorDetails!=undefined && $root.errorDetails != "" )  -->
			<div class="notification col100" style="display: none;" data-bind="visible:($root.errorDetails!=undefined && $root.errorDetails != '' )">
				<ul class="error" style="display: none;" data-bind="visible:($root.errorDetails )">
						<li class="no-bullets" data-bind="text:$root.errorDetails()"> </li>
				</ul>
			</div>
		<!-- /ko -->
		<g:render template="/partial-temps/partnerAgentSearchResults"></g:render> 
		</div>
	</div>
	
</div>
<r:require module="partnerAgentPage"/>
<g:javascript src="postForm.js"/>
</body>
</html>