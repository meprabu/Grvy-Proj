//partnerAgentVM.js
(function($, models, viewmodels) {
	PartnerAgent = function PartnerAgent(params) {
  		var self = this;
  		self.userOrganizationsUrl= params.userOrganizationsUrl;
  		self.partnerAgentCountryUrl= params.partnerAgentCountryUrl;
  		self.partnerAgentCurrencyUrl= params.partnerAgentCurrencyUrl;
  		self.partnerAgentSearchUrl= params.partnerAgentSearchUrl;
  		self.impersonatePartnerAgentMap = ko.observable(params.impersonatePartnerAgentMap);
  		viewmodels.BaseSortVM.call(this, params.columns);
		viewmodels.FilterVM.call(this);
		viewmodels.PaginationVM.call(this);
  		self.accountList = ko.observableArray();
  		self.countryListArray = ko.observableArray();
  		self.currencyListArray = ko.observableArray();
  		self.userListArray = ko.observableArray();
  		self.errorDetails = ko.observable();
  		self.selectedAccount = ko.observable();
  		self.showCountryDropdown  = ko.observable(true);
  		self.showCurrencyDropdown  = ko.observable(true);
  		self.showUserDropdown  = ko.observable(true);
  		self.showAccountDropdown =  ko.observable(true);
  		self.selectedAccountId = ko.observable();
  		self.selectedCountryId = ko.observable();
  		self.selectedCurrencyId = ko.observable();
  		self.selectedUserId = ko.observable();
  		self.displayUser = ko.observable();
  		self.partnerAgentDetails = ko.observable();
  		self.searchText = ko.observable();
  		self.disableAccountDropdown = ko.observable(false);
  		self.disableCountryDropdown = ko.observable(false);
  		self.disableCurrencyDropdown = ko.observable(false);
  		self.disableUserDropdown = ko.observable(false);
  		self.showSearchModalFlag = ko.observable(false);
  		self.userSearchResultsArray = ko.observableArray();
  		self.userSearchDetails = ko.observable();
	};
	
	
	ko.utils.extend(PartnerAgent.prototype, {
		
		init: function(data){
			var self = this;
			if(data.OrganizationMap){
				console.log("Ajax call OrganizationMap-------");
				self.setInitOrganizationValues(data);
			}else if(data.ErrorCode){
				$( "#partnerAgentMain" ).hide();
				self.errorDetails(data.ErrorCode);
				navViewModel.partnerOrglogo (self.partnerAgentDetails().logoUrl);
				navViewModel.partnerOrgName(self.partnerAgentDetails().partnerOrganization);
			}
			 selectMenuActivate();
		},
		 load: function() {
			 var self = this;
			 var path = self.userOrganizationsUrl;
			 var params = {};
			 params.format = "json";
			 console.log("Ajax call Organizations-------");
				$.getJSON(path, params, function(data) {
					console.log(data);
					self.init(data);
					
				}).error(function(jqXhr, textStatus, error) {
	            	
	            	
	  			});
		 },
		 loadPagination: function(){
		 	var self =this;
		 	var params ={};
		 	var url = self.partnerAgentSearchUrl;
		 	params.organizationID = self.selectedAccountId();
		 	params.searchTerm = self.searchText();
		 	var sortBy = self.sortBy();
			if (self.sortBy() != null) {
				params.sortby = self.sortBy();
			}
			if (self.pageSize() != undefined) {
				params.pageSize = self.pageSize();
				params.page = self.page();
			}

		 	params.format = "json";
		 	params.assignmentType = "8";
			params.eliteFlag = "true";
		 	$.getJSON(url, params, function(data) {
					console.log(data);
					self.showSearchModalFlag(true);
					self.updatePagination(data.paginationResult);
					self.userSearchResultsArray(data.AccountSearchDetails.elements);
					self.userSearchDetails(data.AccountSearchDetails);
					
				}).error(function(jqXhr, textStatus, error) {
	            	
	            	
	  			});

		 },
		 loadCountryUsers: function(){
			 if(this.selectedAccountId() != undefined)
				{ 
					var self = this;
					var path = self.partnerAgentCountryUrl;
					 var params = {};
					 params.organizationID = self.selectedAccountId;
					 params.format = "json";
					 console.log("Ajax call params country-------");
					 $.getJSON(path, params, function(data) {
						console.log(data);
						if(data.CountryNames)
						{
							 var ctryValues = data.CountryNames;
							 var ctrytempArray = [];
						     for (var key in ctryValues){
						    	ctrytempArray.push({'ctryId': key, 'ctryName': ctryValues[key]});
						    }
						    self.countryListArray(ctrytempArray);
						    self.setCountryValues();
						}
						if(data.UserList)
						{
							 self.userListArray(data.UserList);
						}
					 }).error(function(jqXhr, textStatus, error) {
		            	
		  			 });
				}
			 else
				 this.noAccountSelected();
		 },
		 loadCurrency: function(){
				if(this.selectedCountryId() != undefined){
					var self = this;
					 var path = self.partnerAgentCurrencyUrl;
					 var params = {};
					 params.organizationID = self.selectedAccountId;
					 params.currencyCode = self.selectedCountryId;
					 params.format = "json";
					 console.log("Ajax call params currency -------");
					 $.getJSON(path, params, function(data) {
						console.log(data);
						if(data){	
							var currValues = data;
						    var currtempArray = [];
						    	for (var key in currValues){
						    	currtempArray.push({'currId': key, 'currName': currValues[key]});
						    	}
						    self.currencyListArray(currtempArray);
						    self.setCurrencyValues();
						}
					}).error(function(jqXhr, textStatus, error) {
		            	
		  			});
				}
				else
					this.noCountrySelected();
			 },
			 loadUsers: function(){
					if(this.selectedCurrencyId() != undefined){
						console.log("params users -------");
						var self = this;
								if(self.userListArray()){	
								    if(self.userListArray().length == 1){
								    	self.showUserDropdown(false);
								    	self.displayUser(self.getUserDisplayName(partnerAgentVM.userListArray()[0]));
								    	self.selectedUserId(self.userListArray()[0].businessPartnerNo);
								    	self.showImpersonate();
								    }else{
								    	if(self.impersonatePartnerAgentMap() != ""){
								    		console.log("else if if users -------");
								    		selectedUser = $.parseJSON(self.impersonatePartnerAgentMap());
											console.log("selectedUser" + selectedUser['username']);
											self.disableUserDropdown(false);
											self.selectedUserId(selectedUser['username']);
											self.impersonatePartnerAgentMap("");
											self.showImpersonate();
								    	}else{
								    		console.log("else if else users -------");
								    		self.disableUserDropdown(false);
									    	self.showUserDropdown(true);
									    	self.showImpersonate();
								    	}
								    }
								    selectMenuActivate();
								}
					}
					else
						this.noCurrencySelected();
				},
				getUserDisplayName: function(obj){
					if(obj){
						var fullDisplayName = obj.firstName + " " + obj.lastName+ " " + obj.businessPartnerNo;
						console.log(fullDisplayName);
					}
					return fullDisplayName;
				},
				 showImpersonate: function(){
					 if(this.selectedUserId() != undefined)
						 $("#startImpersonating").prop("disabled", false);
					 else
						 $("#startImpersonating").prop("disabled", true);
				 },
				 impersonateUser:function(){
						var elements = new Array();
						var self = this;
						elements['impersonateFlag'] = "impersonate";
						elements['username'] = self.selectedUserId();
						elements['accountName'] = $("#partnerAgent_account option:selected").text();
						elements['countryID'] = self.selectedCountryId();
						elements['currencyID'] = self.selectedCurrencyId();
						elements['accountUUID'] = self.selectedAccountId();
						elements['influencerID'] = self.partnerAgentDetails().partnerOrganizationID;
						elements['selectedimpersonateOrgID'] = self.selectedAccountId();
						elements['partnerAgentImpersonate'] = 'true';
						console.log("elements "+elements);
						postForm(elements, "impersonateUser/impersonate");
				},
				 getAccountIDFromUUID : function(accountUUID){
					 console.log("accountUUID"+accountUUID);
					 var accountuuidLocal = accountUUID;
					 var selectedimpersonateOrgID = null;
					 if(accountuuidLocal){
						 var obj = this.accountList();
						 console.log("accountUUID"+obj);
						   for (var key in obj) {
						        console.log(key+': '+obj[key].organizationID);
						        if(obj[key].organizationUUID == accountUUID){
						        	selectedimpersonateOrgID = obj[key].organizationID;
						        	 console.log('selectedimpersonateOrgID'+selectedimpersonateOrgID);
						        	break;
						        }
						    }
					 }
					 return selectedimpersonateOrgID;
				 },
				 openSearchResults: function(){
					 var self = this;
					 console.log(self.searchText());
					 var path = "Elite/partnerAgentSearch";
					 var params = {};
					 params.page = "1";
					 params.pageSize = "20";
					 params.sortby = "lastname.desc";
					 params.region = "";
					 params.assignmentType = "8";
					 params.eliteFlag = "true";
					 params.filterRequired = "true";
					 params.OrganizationUUID = self.selectedAccountId();
					 params.SearchBuyer =self.searchText();
					 $.getJSON(path, params, function(data) {
						 console.log(data);
						 self.showSearchModalFlag(true);
						 self.userSearchResultsArray(data.AccountSearchDetails.elements);
						 self.userSearchDetails(data.AccountSearchDetails);
						}).error(function(jqXhr, textStatus, error) {
			            	// TODO
			            	// ajaxError(jqXhr, textStatus, error)
			  			});
				 },
				 
				 setInitOrganizationValues: function(data){
					 console.log("setInitOrganizationValues -------");
					 var self = this;
					 self.accountList(data.OrganizationMap);
						self.partnerAgentDetails(data.PartnerAgentDetails);
						navViewModel.partnerOrglogo (self.partnerAgentDetails().logoUrl);
						navViewModel.partnerOrgName(self.partnerAgentDetails().partnerOrganization);
						 	if(self.accountList().length == 1){
							 self.showAccountDropdown(false);
							 self.selectedAccountId(self.accountList()[0].organizationUUID);
							 self.loadCountryUsers();
						    }else{
						    	if(self.impersonatePartnerAgentMap() != ""){
						    		console.log("else if if orgs -------");
						    		selectedAccount = $.parseJSON(self.impersonatePartnerAgentMap());
									console.log("selectedUser" + selectedAccount['accountUUID']);
									self.selectedAccountId(selectedAccount['accountUUID']);
									self.loadCountryUsers();
						    	}else{
						    		console.log("else if else orgs -------");
						    		self.showAccountDropdown(true);
						    	}
						    }
						 	selectMenuActivate();
				 },
				 setCountryValues: function(){
					 var self = this;
					 if(self.countryListArray().length == 1){
					    	console.log("if country -------");
					    	self.showCountryDropdown(false);
					    	self.selectedCountryId(self.countryListArray()[0].ctryId);
					    	self.loadCurrency();
					    }else{
					    	if(self.impersonatePartnerAgentMap() != ""){
					    		console.log("else if if country -------");
								selectedCountry = $.parseJSON(self.impersonatePartnerAgentMap());
								console.log("selectedCountry" + selectedCountry['countryID']);
								self.selectedCountryId(selectedCountry['countryID']);
								self.loadCurrency();kin
							}else{
								console.log("else if else country-------");
						    	self.showCountryDropdown(true);
								self.disableCountryDropdown(false);
						    	self.showCurrencyDropdown(true);
							}
					    }
					    if(self.countryListArray().length == 0){
					    	
					    }
					    selectMenuActivate();
				 },
			setCurrencyValues: function(){
				  var self = this;
				  if(self.currencyListArray().length == 1){
				    	self.showCurrencyDropdown(false);
				    	self.selectedCurrencyId(self.currencyListArray()[0].currId);
				    	self.loadUsers();
				    }else{
				    	if(self.impersonatePartnerAgentMap() != ""){
				    		console.log("else if if currency -------");
				    		selectedCurrency = $.parseJSON(self.impersonatePartnerAgentMap());
							console.log("selectedAccount" + selectedCurrency['currencyID']);
							self.selectedCurrencyId(selectedCurrency['currencyID']);
							self.loadUsers();
				    	}else{
				    		console.log("else if else currency -------");
					    	self.showCurrencyDropdown(true);
				    		self.disableCurrencyDropdown(false);
					    	self.showUserDropdown(true);
				    	}
				    }
				    selectMenuActivate();
			},
			noAccountSelected: function(){
				var self = this;
				self.selectedCountryId("");
  				self.showCountryDropdown(true);
  				self.disableCountryDropdown(true);
  				self.selectedCurrencyId("");
  				self.showCurrencyDropdown(true);
  				self.disableCurrencyDropdown(true);
  				self.selectedUserId("");
  				self.showUserDropdown(true);
  				self.disableUserDropdown(true);
  				selectMenuActivate();
			},
			noCountrySelected: function(){
				var self = this;
				console.log("noCountrySelected");
				self.selectedCurrencyId("");
  				self.showCurrencyDropdown(true);
  				self.disableCurrencyDropdown(true);
  				self.selectedUserId("");
  				self.showUserDropdown(true);
  				self.disableUserDropdown(true);
  				selectMenuActivate();
			},
			noCurrencySelected: function(){
				var self = this;
				console.log("noCurrencySelected");
				self.selectedUserId("");
  				self.showUserDropdown(true);
  				self.disableUserDropdown(true);
  				selectMenuActivate();
			},
			changeSort: function(data) {
				if (this.updateSorting(data)) {
					this.loadPagination();
				}
			},
			updatePage: function() {
				this.loadPagination();
			}
	});
	
	 viewmodels.PartnerAgentVM = PartnerAgent;	
}(jQuery, models = window.hpelite.models, viewmodels = window.hpelite.viewmodels));
