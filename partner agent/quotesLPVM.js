//quotesLPVM.js
(function($, models, viewmodels) {
	QuotesLPVM = function QuotesLPVM(params) {
		var self = this
		self.urlQuoteList = params.urlQuoteList;
		self.qouteLable = params.qouteLable;
		self.quoteCheckoutUrl= params.quoteCheckoutUrl;
		self.quoteSummaryUrl = params.quoteSummaryUrl;
		self.urlPRCheckoutSummary = params.urlPRCheckoutSummary;
		self.urlPOCheckoutSummary = params.urlPOCheckoutSummary;
		self.urlCreatePR = params.urlCreatePR;
		self.urlCreatePO = params.urlCreatePO;
		self.urlCopyQuote = params.urlCopyQuote;
		self.urlReviseQuote = params.urlReviseQuote;
		self.urlDeleteQuote = params.urlDeleteQuote;
		self.urlCheckEmpRequestor = params.urlCheckEmpRequestor;
		self.regionCode = params.regionCode;
		self.quoteResultArray = ko.observableArray();
		self.userRole=ko.observable();
		self.showDeleteModalFlag = ko.observable();
		self.quoteUUID = ko.observable();
		self.quoteName = ko.observable();
		self.docStatus = ko.observable();
		self.docCurrency ='';
		self.serviceName = ko.observable(self.qouteLable);
		self.quoteVersionLength = ko.observable();
		self.personalUser = params.personalUser;
		viewmodels.BaseSortVM.call(this, params.columns);
		viewmodels.FilterVM.call(this);
		viewmodels.PaginationVM.call(this);
		viewmodels.Attachments.call(this,params);
		self.changedPageSize = ko.computed(this.changePageSize,this);
		self.changedFilter = ko.computed(this.changeFilter,this);
		self.callControllerName = params.callControllerName;
		self.controllerAction = params.controllerAction;
		self.createdIn = ko.observable(params.createdIn);
		self.quoteNameOrNumber = ko.observable();
		self.isTeleWebAgentEnabled = ko.observable(params.isTeleWebAgentEnabled);
		self.eliteSFDC = (params.eliteSFDC!=null && typeof params.eliteSFDC!= 'undefined' && params.eliteSFDC.trim()!="")?ko.observable(params.eliteSFDC):ko.observable("");
 	   	window.hpelite.languageChangeModal = "false";
 	    self.versionEnabledRegion = params.versionEnabledRegion;
 	    self.maxVersionAllowed = params.maxVersionAllowed;
		self.isFileAttachment  = ko.computed(function(){
			
		});
		self.defaultPlaceholder = params.defaultPlaceholder;
		
		this.showDetails = function(elements) {
			postFormEprime(elements);
		}
		this.formattedStatus = function(theStatus) {
			if (theStatus == "-") {
				return params.validStatusString;
			}
			return theStatus;
		}
	};

	ko.utils.extend(QuotesLPVM.prototype, {
		init: function(data) {
			this.quoteResultArray.map(data.result, models.WorkflowDocument);
			this.userRole(data.userRole);
			
			this.updateStatusFilter(data.filterResult);
			this.updatePagination(data.paginationResult);
			
			
		},
		load: function(data) {
			var self = this;
			var params = {};
			var sortBy = self.sortBy();
			if (sortBy != null) {
				params.sortby = sortBy;
			}
			if (self.pageSize() != undefined) {
				params.pageSize = self.pageSize();
				params.page = self.page();
			}
			if (self.statusFilter() != undefined) {
				params.statusFilter = self.statusFilter();
			}
			if (self.createdIn() != undefined) {
				params.createdIn = self.createdIn();
			}
			if (self.quoteNameOrNumber() != undefined && self.quoteNameOrNumber() !== self.defaultPlaceholder) {
				params.quoteNameOrNumber = self.quoteNameOrNumber();
			}
			if(data){
				self.init(data);
			}
			else{
				params.format = "json";
				showSpinner();
				$.getJSON(self.urlQuoteList, params, function(data) {
					self.init(data);
					hideSpinner();
				}).error(function(jqXhr, textStatus, error) {
	            	// TODO
	            	// ajaxError(jqXhr, textStatus, error)
	  			});
			}
		},
		updatePage: function() {
			this.load();
		},
		changeSort: function(data) {
			if (this.updateSorting(data)) {
				this.load();
			}
		},
		changePageSize:function(){
			var self = this;
			if(self.currentPageSize() != self.pageSize()&&self.pageSize()!=undefined && self.currentPageSize()!=undefined){
				this.load();
			}
		},
		changeFilter:function(){
			var self = this;
			if(self.currentStatusFilter() != self.statusFilter()&&self.statusFilter()!=undefined){
				this.load();
			}
		},
		changeCreatedFilter : function(){
			var self = this;
			if(self.createdIn()!=undefined){
				showSpinner();
				this.load();
			}
		},
		searchQuoteNameOrNumber : function(){
			var self = this;
			if (self.quoteNameOrNumber() != undefined) {
				showSpinner();
				this.load();
			}
		},
		deleteDocument:function(){
			var self = this;
			showSpinner();
			//console.log('delete document');
			var id=self.quoteUUID();
			var status=self.docStatus();
			var path = self.urlDeleteQuote;
			//console.log(path)
			var params={'documentId':id,'docStatus':status,'docType':'Quote'};
			//console.log(params)
			$.getJSON(path, params, function(data) {
				//console.log(data);
				if(undefined!=data.status && data.status=="success"){
					//console.log("delete success");	
					self.load();
					self.showDeleteModalFlag(false);	
				}else{
					//console.log("delete failure");
					self.showDeleteModalFlag(false);
					hideSpinner();
				}
			}).error(function(jqXhr, textStatus, error) {
				self.showDeleteModalFlag(false);
				hideSpinner();
				//console.log("delete ajax failed")
  			});
		},
		
		
		
	});

	viewmodels.QuotesLPVM = QuotesLPVM;
}(jQuery, models = window.hpelite.models, viewmodels = window.hpelite.viewmodels));