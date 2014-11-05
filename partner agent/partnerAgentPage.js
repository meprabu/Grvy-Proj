//partnerAgent.js
(function (params, viewmodels) {
	if (params.partnerAgentVM != null && params.partnerAgentVM.elementId != null) {
		
		var args ={
				userOrganizationsUrl: params.partnerAgentVM.userOrganizationsUrl,
				partnerAgentCountryUrl: params.partnerAgentVM.partnerAgentCountryUrl,
				partnerAgentCurrencyUrl: params.partnerAgentVM.partnerAgentCurrencyUrl,
				impersonatePartnerAgentMap: params.partnerAgentVM.impersonatePartnerAgentMap,
				columns: params.partnerAgentVM.columns,
				partnerAgentSearchUrl: params.partnerAgentVM.partnerAgentSearchUrl
		}
		partnerAgentVM = new viewmodels.PartnerAgentVM(args);
		partnerAgentVM.load();
		$(document).ready(function() {
			
			ko.applyBindings(partnerAgentVM, document.getElementById(params.partnerAgentVM.elementId));
    		
			partnerAgentVM.disableCountryDropdown(true);
			partnerAgentVM.disableCurrencyDropdown(true);
			partnerAgentVM.disableUserDropdown(true);
			$("#startImpersonating").prop("disabled", true);
		});

		var colSelector='#PartnerAgent table th';
		var pageSelector='#PartnerAgent #paginationTags';
		var nextPage='#PartnerAgent .next';
		var prevPage='#PartnerAgent .prev';
		
	    $(document).on("click", colSelector, function() {
			partnerAgentVM.changeSort(ko.dataFor(this));
	    });

	    $(document).on("click", pageSelector, function() {
	    	if(partnerAgentVM.currentPage() != partnerAgentVM.page()){
				partnerAgentVM.updatePage();
	    	}
	    });
	
	    $(document).on("click", nextPage, function() {
			if(partnerAgentVM.page()<partnerAgentVM.pageCount()){
					partnerAgentVM.page(partnerAgentVM.page()+1);
					partnerAgentVM.updatePage();
				}
	    	
	    });
	    
	    $(document).on("click", prevPage, function() {
	    	if(partnerAgentVM.page()>1){
		    		partnerAgentVM.page(partnerAgentVM.page()-1);
		    		partnerAgentVM.updatePage();
	    		}
			
	    });
	}
	
}(params = window.hpelite.params, viewmodels = window.hpelite.viewmodels));