//checkout.js
(function ($, params, viewmodels) {
    var args = {}; 
    if(params.prCheckoutVM != null && params.prCheckoutVM.elementId != null){
        args ={ 
        	attachmentDownloadUrl :params.prCheckoutVM.attachmentDownloadUrl,
            urlPrCheckoutEdit: params.prCheckoutVM.urlPrCheckoutEdit,
            urlPRCheckoutReview: params.prCheckoutVM.urlPRCheckoutReview,
            urlCheckoutUpdate: params.prCheckoutVM.urlCheckoutUpdate,
            urlPrCheckoutSave: params.prCheckoutVM.urlPrCheckoutSave,
            urlSubmitPO: params.prCheckoutVM.urlSubmitPO,
            urlForwardPR: params.prCheckoutVM.urlForwardPR,
            urlAprrovePR: params.prCheckoutVM.urlAprrovePR,
            urlUpdatePaymentMethod: params.prCheckoutVM.urlUpdatePaymentMethod,
            urlRefreshOrderExemptSelection: params.prCheckoutVM.urlRefreshOrderExemptSelection,
            urlPrCheckoutSummaryPage: params.prCheckoutVM.urlPrCheckoutSummaryPage,
            urlRefreshShippingState: params.prCheckoutVM.urlRefreshShippingState,         
            urlRefreshState: params.prCheckoutVM.urlRefreshState,
            urlRefreshCountry: params.prCheckoutVM.urlRefreshCountry,
            urlRefreshBillingType: params.prCheckoutVM.urlRefreshBillingType,
            urlCustomizeCTO: params.prCheckoutVM.urlCustomizeCTO,
            cartEditUrl: params.prCheckoutVM.cartEditUrl,
            carepackErrorMsg: params.prCheckoutVM.carepackErrorMsg,
            urlprReturnFromEprime: params.prCheckoutVM.urlprReturnFromEprime,
            urlRemoveDocument: params.prCheckoutVM.urlRemoveDocument,
            urlRemoveFromCartSummary:params.prCheckoutVM.urlRemoveFromCartSummary,
            urlCompleteCartSummaryUpdate: params.prCheckoutVM.urlCompleteCartSummaryUpdate,
            urlUpdateFromCartSummary: params.prCheckoutVM.urlUpdateFromCartSummary,
            homePageDeleteFlag:  params.prCheckoutVM.homePageDeleteFlag,
            shopMoreItems:  params.prCheckoutVM.shopMoreItems,
            deleteDocumentControllerName: params.prCheckoutVM.deleteDocumentControllerName,
            deleteDocumentControllerAction: params.prCheckoutVM.deleteDocumentControllerAction,
            docType: params.prCheckoutVM.docType,
            deleteConfirmationMessage: params.prCheckoutVM.deleteConfirmationMessage,
            shopMoreItemsMessage: params.prCheckoutVM.shopMoreItemsMessage
        };
        prCheckoutVM = new viewmodels.PrCheckoutVM(args);

        prCheckoutVM.init(params.prCheckoutVM.checkoutInfo);
        
        $(document).on('click','.secondary.js-cancel',function(){
            var prBackup=$.extend(true,{},prCheckoutVM.backupCheckoutInfo);
            prCheckoutVM.resetEdit(prCheckoutVM.checkoutInfo,prBackup);
        });

        $(document).on('change','input[name="default"]',function(){
            //console.log('default pushed');
            prCheckoutVM.defaultLogistics();
            
        });


        $(document).on('click','input.log',function(){
            var data=ko.dataFor(this);
            //console.log("validate:"+data.validation());
            //console.log('selected'+data.Selected());

            if(data.validation()){
                prCheckoutVM.prepareDataForUpdate(true,data);
            }

        });

        $(document).on('click', '.updateLog',function(){
            prCheckoutVM.prepareDataForUpdate();
        });
        
        //Checkout edit links for IE8 Fix
        $(document).on('click', '.accountInformation',function(){
            prCheckoutVM.toggleFlag(prCheckoutVM.accountEditFlag,'AccountInformation');
        });
        
        $(document).on('click', '.shippingInformation',function(){
            prCheckoutVM.toggleFlag(prCheckoutVM.shippingEditFlag, 'ShippingInformation');
        });
        
        $(document).on('click', '.billingInformation',function(){
            prCheckoutVM.toggleFlag(prCheckoutVM.billingEditFlag, 'BillingInformation');
        });
        
        $(document).on('click', '.payerInformation',function(){
            prCheckoutVM.toggleFlag(prCheckoutVM.payerEditFlag, 'PayerInformation');
        });

        $(document).on('click', '.cartSummaryInformation',function(){
            prCheckoutVM.toggleFlag(prCheckoutVM.cartSummaryEditFlag, 'CartSummary');
        });
        
        $(document).on('click', '.completeCartSummaryUpdate',function(){
        	prCheckoutVM.completeCartSummaryUpdate();
        });

        
        $(document).on('change','input.radioPaymentInfo',function(){
            prCheckoutVM.updatePaymentMethod(ko.dataFor(this));
        });
        
        $(document).on('change','input.radioSalesTax',function(){
            prCheckoutVM.refreshOrderExemptSelection(ko.dataFor(this));
        });
        
        $(document).ready(function() {
            $('.shop-more-items').html(prCheckoutVM.shopMoreItemsMessage);
       });

        $(document).on('change','input.refreshStateFlag',function(){
            prCheckoutVM.refreshShippingState(ko.dataFor(this));
        });

        $(document).on('click', '#forwardprbutton',function(){
            prCheckoutVM.forwardPR();
        });

        $(document).on('click', '#saveprbutton',function(){
            prCheckoutVM.saveData();
        });

        $(document).on('click','.removeProductFromCartSummary',function(){
        	prCheckoutVM.removeProductFromCartSummary(ko.dataFor(this).lineItemID);
        });
        
        $(document).on('click','.updateFromCartSummary',function(){
        	var cart = ko.dataFor(this);
        	prCheckoutVM.updateFromCartSummary(cart.lineItemID(), cart.quantity());
        });

        updateTextarea(prCheckoutVM);
        
        
        /*$(document).on('blur','input.quoteName',function(){
            $(".edit").hide();
            prCheckoutVM.updateData('AccountInformation',true);

        });*/

        $(document).on('click','input.copyCarePack',function(){
            ko.dataFor(this).copyNextSelected(!ko.dataFor(this).copyNextSelected());
            var parentIndex=ko.contextFor(this).$parentContext.$index();
            var index = ko.contextFor(this).$index();
            //console.log('Next copy parentIndex:'+parentIndex+':index:'+index);
            if(ko.dataFor(this).copyNextSelected())
                prCheckoutVM.copyCarePackToNext(parentIndex,index);
        });

        $(document).on('click','input.copyAllCarePack',function(){
            ko.dataFor(this).copyAllSelected(!ko.dataFor(this).copyAllSelected());
            var parentIndex=ko.contextFor(this).$parentContext.$index();
            var index = ko.contextFor(this).$index();
            //console.log('All copy parentIndex:'+parentIndex+':index:'+index);
            if(ko.dataFor(this).copyAllSelected())
                prCheckoutVM.copyAllCarePacks(parentIndex,index);
        });

        $(document).on('blur','.addressField',function(){
            //console.log("copy next:"+ko.contextFor(this).$parent.copyNextSelected());
            //console.log("copy All:"+ko.contextFor(this).$parent.copyAllSelected());
            var parentIndex=ko.contextFor(this).$parentContext.$parentContext.$index();
            var index = ko.contextFor(this).$index();
            //console.log('blur parentIndex:'+parentIndex+':index:'+index);
            if(ko.contextFor(this).$parent.copyAllSelected())
                prCheckoutVM.copyAllCarePacks(parentIndex,index);
            else if(ko.contextFor(this).$parent.copyNextSelected())
                prCheckoutVM.copyCarePackToNext(parentIndex,index);
            
        });

        $(document).on('change','.addressFieldSelect',function(){
            //console.log("copy next:"+ko.contextFor(this).$parents[1].copyNextSelected());
            //console.log("copy All:"+ko.contextFor(this).$parents[1].copyAllSelected());
            var parentIndex=ko.contextFor(this).$parentContext.$parentContext.$index();
            var index = ko.contextFor(this).$index();
            //console.log('blur parentIndex:'+parentIndex+':index:'+index);
            if(ko.contextFor(this).$parents[1].copyAllSelected())
                prCheckoutVM.copyAllCarePacks(parentIndex,index);
            else if(ko.contextFor(this).$parents[1].copyNextSelected())
                prCheckoutVM.copyCarePackToNext(parentIndex,index);
            
        });

        $(document).on('click', '.updateCarePack',function(){
            if(!prCheckoutVM.carePackValidate()){
                prCheckoutVM.prepareCarePackForUpdate();
            }
        });

        if(params.cartSummaryPage){
            prCheckoutVM.logSelection(params.cartSummaryPage.logSelection);
        }
        
        $(document).ready(function() {
            if(typeof ko.dataFor(document.getElementById(params.prCheckoutVM.elementId)) == 'undefined'){
                ko.applyBindings(prCheckoutVM, document.getElementById(params.prCheckoutVM.elementId));
            }
        });

        $(document).on("keyup",".quoteName", function(){
            prCheckoutVM.updateTopSectionEmptyFlag();
        });
    }

    if(params.quoteCheckoutVM != null && params.quoteCheckoutVM.elementId != null){
        args ={
        	attachmentDownloadUrl:params.quoteCheckoutVM.attachmentDownloadUrl,
            quoteSaveUrl:params.quoteCheckoutVM.quoteSaveUrl,
            urlCustomizeCTO: params.quoteCheckoutVM.urlCustomizeCTO,
            quoteEditUrl: params.quoteCheckoutVM.quoteEditUrl,
            urlCheckoutUpdate: params.quoteCheckoutVM.urlCheckoutUpdate,
            quoteSaveCopyUrl: params.quoteCheckoutVM.quoteSaveCopyUrl,
            quoteSummaryUrl: params.quoteCheckoutVM.quoteSummaryUrl,
            cartEditUrl: params.quoteCheckoutVM.cartEditUrl,
            urlAddToRequisition: params.quoteCheckoutVM.urlAddToRequisition,
            urlDeleteQuote: params.quoteCheckoutVM.urlDeleteQuote,
            urlRefreshOrderExemptSelection: params.quoteCheckoutVM.urlRefreshOrderExemptSelection,
            urlRefreshShippingState: params.quoteCheckoutVM.urlRefreshShippingState,
            urlRefreshState: params.quoteCheckoutVM.urlRefreshState,
            urlRefreshCountry: params.quoteCheckoutVM.urlRefreshCountry,
            urlRefreshBillingType: params.quoteCheckoutVM.urlRefreshBillingType,
            urlquoteReturnFromEprime: params.quoteCheckoutVM.urlquoteReturnFromEprime,
            urlCheckEmpRequestor: params.quoteCheckoutVM.urlCheckEmpRequestor,
            urlCopyQuote: params.quoteCheckoutVM.urlCopyQuote,
            callControllerName:params.quoteCheckoutVM.callControllerName,
            controllerAction:params.quoteCheckoutVM.controllerAction,
            urlRemoveDocument: params.quoteCheckoutVM.urlRemoveDocument,
            urlRemoveFromCartSummary:params.quoteCheckoutVM.urlRemoveFromCartSummary,
            urlCompleteCartSummaryUpdate: params.quoteCheckoutVM.urlCompleteCartSummaryUpdate,
            urlUpdateFromCartSummary: params.quoteCheckoutVM.urlUpdateFromCartSummary,
            urlProductDetails: params.quoteCheckoutVM.urlProductDetails,
            homePageDeleteFlag:  params.quoteCheckoutVM.homePageDeleteFlag,
            shopMoreItems:  params.quoteCheckoutVM.shopMoreItems,
            shopForAdditionalItems: params.quoteCheckoutVM.shopForAdditionalItems,
            deleteDocumentControllerName: params.quoteCheckoutVM.deleteDocumentControllerName,
            deleteDocumentControllerAction: params.quoteCheckoutVM.deleteDocumentControllerAction,
            docType: params.quoteCheckoutVM.docType,
            deleteConfirmationMessage: params.quoteCheckoutVM.deleteConfirmationMessage,
            controllerAction:params.quoteCheckoutVM.controllerAction,
            urlCreateNonCatalogQuote: params.quoteCheckoutVM.urlCreateNonCatalogQuote,
            estimatedFreightErroressage: params.quoteCheckoutVM.estimatedFreightErroressage,
            specialHandlingErroressage: params.quoteCheckoutVM.specialHandlingErroressage,
            taxErroressage: params.quoteCheckoutVM.taxErroressage,
            quoteCheckoutUrl: params.quoteCheckoutVM.quoteCheckoutUrl,
            shopMoreItemsMessage: params.quoteCheckoutVM.shopMoreItemsMessage,
            urlGetAddressList: params.quoteCheckoutVM.urlGetAddressList
        };
        quoteCheckoutVM = new viewmodels.QuoteCheckoutVM(args);

        quoteCheckoutVM.init(params.quoteCheckoutVM.checkoutInfo);
        
        //Checkout edit links for IE8 Fix
        $(document).on('click', '.accountInformation',function(){
            quoteCheckoutVM.toggleFlag(quoteCheckoutVM.accountEditFlag,'AccountInformation');
        });
        
        $(document).on('click', '.shippingInformation',function(){
            quoteCheckoutVM.toggleFlag(quoteCheckoutVM.shippingEditFlag, 'ShippingInformation');
        });
        
        $(document).on('click', '.billingInformation',function(){
            quoteCheckoutVM.toggleFlag(quoteCheckoutVM.billingEditFlag, 'BillingInformation');
        });
        
        $(document).on('click', '.payerInformation',function(){
            quoteCheckoutVM.toggleFlag(quoteCheckoutVM.payerEditFlag, 'PayerInformation');
        });
        
        $(document).on('click', '.contactInformation',function(){
            quoteCheckoutVM.toggleFlag(quoteCheckoutVM.contactEditFlag, 'HPContactInformation');
        });
        $(document).on('click', '.nonCatalogcartValuesInfo',function(){
            quoteCheckoutVM.toggleFlag(quoteCheckoutVM.nonCatalogCartEditFlag, 'CartValues');
        });

        $(document).on('click', '.cartSummaryInformation',function(){
        	quoteCheckoutVM.editCartMergeProducts();
        });
        
        $(document).on('click', '.completeCartSummaryUpdate',function(){
        	quoteCheckoutVM.completeCartSummaryUpdate();
        });
        
        $(document).on('click','.secondary.js-cancel',function(){
            quoteCheckoutVM.resetData();
        });
        

        $(document).ready(function() {
             $('.shop-more-items').html(quoteCheckoutVM.shopMoreItemsMessage);
        });
        
        $(document).on('change','input[name="default"]',function(){
            //console.log('default pushed');
            quoteCheckoutVM.defaultLogistics();
            
        });
        
        $(document).on('click', '.updateLog',function(){
            quoteCheckoutVM.prepareDataForUpdate();
        });

        $(document).on('click', '#submitquotebutton',function(){
            quoteCheckoutVM.saveData();
        });

        $(document).on('click','input.log',function(){
            var data=ko.dataFor(this);
            //console.log("validate:"+data.validation());
            //console.log('selected'+data.Selected());

            if(data.validation()){
                quoteCheckoutVM.prepareDataForUpdate(true,data);
            }

        });

        /*$(document).on('blur','input.quoteName',function(){
            $(".edit").hide();
            quoteCheckoutVM.updateData('AccountInformation',true);

        });*/

        $(document).on('change','input.radioPaymentInfo',function(){
            quoteCheckoutVM.updatePaymentMethod(ko.dataFor(this));
        });
        
        $(document).on('change','input.radioSalesTax',function(){
            quoteCheckoutVM.refreshOrderExemptSelection(ko.dataFor(this));
        });
        
        $(document).on('change','input.refreshStateFlag',function(){
            quoteCheckoutVM.refreshShippingState(ko.dataFor(this));
        });

        $(document).on('click','input.cancel',function(){
            //console.log('clicked');
            if(quoteCheckoutVM.isNewQuote){
                window.location=navViewModel.currentNavs().viewCartPath;
            }else if((quoteCheckoutVM.homePageDeleteFlag()!= 'null' && quoteCheckoutVM.homePageDeleteFlag()== 'true')){
            	 window.location=navViewModel.currentNavs().eliteUrl;
            }else{
                window.location=navViewModel.currentNavs().urlQuotes;
            }
        });
        
        $(document).on('click','.removeProductFromCartSummary',function(){
        	quoteCheckoutVM.removeProductFromCartSummary(ko.dataFor(this).lineItemID);
        });
        
        $(document).on('click','.updateFromCartSummary',function(){
        	var cart = ko.dataFor(this);
        	quoteCheckoutVM.updateFromCartSummary(cart.lineItemID(), cart.quantity());
        });

        updateTextarea(quoteCheckoutVM);

        if(params.cartSummaryPage){
            quoteCheckoutVM.logSelection(params.cartSummaryPage.logSelection);
        }

        $(document).ready(function() {
            if(typeof ko.dataFor(document.getElementById(params.quoteCheckoutVM.elementId)) == 'undefined'){
                ko.applyBindings(quoteCheckoutVM, document.getElementById(params.quoteCheckoutVM.elementId));
            }
        });
        
        $(document).on("keyup",".quoteName", function(){
            quoteCheckoutVM.updateTopSectionEmptyFlag();
        });
    }

    if(params.poCheckoutVM != null && params.poCheckoutVM.elementId != null){
        args ={
            poCheckoutVM:params.poCheckoutVM.url,
            attachmentDownloadUrl :params.poCheckoutVM.attachmentDownloadUrl,
            urlPoCheckoutEdit: params.poCheckoutVM.urlPoCheckoutEdit,
            urlCheckoutUpdate: params.poCheckoutVM.urlCheckoutUpdate,
            urlPoCheckoutSave : params.poCheckoutVM.urlPoCheckoutSave,
            urlSubmitPO : params.poCheckoutVM.urlSubmitPO,
            poSaveDraftUrl : params.poCheckoutVM.poSaveDraftUrl,
            urlRemoveDocument: params.poCheckoutVM.urlRemoveDocument,            
            urlPoCheckoutCopy : params.poCheckoutVM.urlPoCheckoutCopy,
            urlPoSummary : params.poCheckoutVM.urlPoSummary,
            urlPoCreate : params.poCheckoutVM.urlPoCreate,
            urlUpdatePaymentMethod: params.poCheckoutVM.urlUpdatePaymentMethod,
            urlRefreshOrderExemptSelection: params.poCheckoutVM.urlRefreshOrderExemptSelection,
            urlRefreshShippingState: params.poCheckoutVM.urlRefreshShippingState,
            urlRefreshState: params.poCheckoutVM.urlRefreshState,
            urlRefreshCountry: params.poCheckoutVM.urlRefreshCountry,
            urlRefreshBillingType: params.poCheckoutVM.urlRefreshBillingType,
            urlCustomizeCTO: params.poCheckoutVM.urlCustomizeCTO,
            cartEditUrl: params.poCheckoutVM.cartEditUrl,
            carepackErrorMsg: params.poCheckoutVM.carepackErrorMsg,
            urlpoReturnFromEprime: params.poCheckoutVM.urlpoReturnFromEprime,
            urlDeletePurchaseRequest: params.poCheckoutVM.urlDeletePurchaseRequest,
            urlRemoveFromCartSummary:params.poCheckoutVM.urlRemoveFromCartSummary,
            urlCompleteCartSummaryUpdate: params.poCheckoutVM.urlCompleteCartSummaryUpdate,
            urlUpdateFromCartSummary: params.poCheckoutVM.urlUpdateFromCartSummary,
            callControllerName: params.poCheckoutVM.callControllerName,
            controllerAction: params.poCheckoutVM.controllerAction,
            deleteDocumentControllerName:  params.poCheckoutVM.deleteDocumentControllerName,
            deleteDocumentControllerAction:   params.poCheckoutVM.deleteDocumentControllerAction,
            homePageDeleteFlag:  params.poCheckoutVM.homePageDeleteFlag,
            shopMoreItems:  params.poCheckoutVM.shopMoreItems,
            shopForAdditionalItems: params.poCheckoutVM.shopForAdditionalItems,
            urlDeletePR: params.poCheckoutVM.urlDeletePR,
            docType: params.poCheckoutVM.docType,
            deleteConfirmationMessage: params.poCheckoutVM.deleteConfirmationMessage,
            poCheckoutUrl: params.poCheckoutVM.poCheckoutUrl,
            shopMoreItemsMessage: params.poCheckoutVM.shopMoreItemsMessage,
            isTeleWebAgentEnabled: params.poCheckoutVM.isTeleWebAgentEnabled 

        };
        poCheckoutVM = new viewmodels.POCheckoutVM(args);
        poCheckoutVM.init(params.poCheckoutVM.checkoutInfo);
        
        //Checkout edit links for IE8 Fix
        $(document).on('click', '.accountInformation',function(){
            poCheckoutVM.toggleFlag(poCheckoutVM.accountEditFlag,'AccountInformation');
        });
        
        $(document).on('click', '.shippingInformation',function(){
            poCheckoutVM.toggleFlag(poCheckoutVM.shippingEditFlag, 'ShippingInformation');
        });
        
        $(document).on('click', '.billingInformation',function(){
            poCheckoutVM.toggleFlag(poCheckoutVM.billingEditFlag, 'BillingInformation');
        });
        
        $(document).on('click', '.payerInformation',function(){
            poCheckoutVM.toggleFlag(poCheckoutVM.payerEditFlag, 'PayerInformation');
        });

        $(document).on('click', '.cartSummaryInformation',function(){        	
        	poCheckoutVM.editCartMergeProducts();
        });
        
        $(document).on('click', '.completeCartSummaryUpdate',function(){
        	poCheckoutVM.completeCartSummaryUpdate();
        });

        $(document).on('click','.updateFromCartSummary',function(){
        	var cart = ko.dataFor(this);
        	poCheckoutVM.updateFromCartSummary(cart.lineItemID(), cart.quantity());
        });

        $(document).on('click','.secondary.js-cancel',function(){
            poCheckoutVM.resetData();
        });

        $(document).on('change','input[name="default"]',function(){
            //console.log('default pushed');
            poCheckoutVM.defaultLogistics();
            
        });
        
        $(document).ready(function() {
            $('.shop-more-items').html(poCheckoutVM.shopMoreItemsMessage);
       });

        $(document).on('click', '.updateLog',function(){
            poCheckoutVM.prepareDataForUpdate();
        });

        $(document).on('click','input.log',function(){
            var data=ko.dataFor(this);
            //console.log("validate:"+data.validation());
            //console.log('selected'+data.Selected());

            if(data.validation()){
                poCheckoutVM.prepareDataForUpdate(true,data);
            }

        });

        $(document).on('change','input.radioPaymentInfo',function(){
            poCheckoutVM.updatePaymentMethod(ko.dataFor(this));
        });

        $(document).on('click', '#submitpobutton',function(){
          //  poCheckoutVM.submitPO();
        });
        
        $(document).on('change','input.radioSalesTax',function(){
            poCheckoutVM.refreshOrderExemptSelection(ko.dataFor(this));
        });
        
        $(document).on('change','input.refreshStateFlag',function(){
            poCheckoutVM.refreshShippingState(ko.dataFor(this));
        });

        /*$(document).on('blur','input.poName, input.poNumber, input.poAgent',function(){
            $(".edit").hide();
            poCheckoutVM.updateData('AccountInformation',true);

        });*/

        $(document).on('change','input.copyCarePack',function(){
            ko.dataFor(this).copyNextSelected(!ko.dataFor(this).copyNextSelected());
            var parentIndex=ko.contextFor(this).$parentContext.$index();
            var index = ko.contextFor(this).$index();
            //console.log('Next copy parentIndex:'+parentIndex+':index:'+index);
            if(ko.dataFor(this).copyNextSelected())
                poCheckoutVM.copyCarePackToNext(parentIndex,index);
        });

        $(document).on('change','input.copyAllCarePack',function(){
            ko.dataFor(this).copyAllSelected(!ko.dataFor(this).copyAllSelected());
            var parentIndex=ko.contextFor(this).$parentContext.$index();
            var index = ko.contextFor(this).$index();
            //console.log('All copy parentIndex:'+parentIndex+':index:'+index);
            if(ko.dataFor(this).copyAllSelected())
                poCheckoutVM.copyAllCarePacks(parentIndex,index);
        });

        $(document).on('blur','.addressField',function(){
            //console.log("copy next:"+ko.contextFor(this).$parent.copyNextSelected());
            //console.log("copy All:"+ko.contextFor(this).$parent.copyAllSelected());
            var parentIndex=ko.contextFor(this).$parentContext.$parentContext.$index();
            var index = ko.contextFor(this).$index();
            //console.log('blur parentIndex:'+parentIndex+':index:'+index);
            if(ko.contextFor(this).$parent.copyAllSelected())
                poCheckoutVM.copyAllCarePacks(parentIndex,index);
            else if(ko.contextFor(this).$parent.copyNextSelected())
                poCheckoutVM.copyCarePackToNext(parentIndex,index);
            
        });

        $(document).on('change','.addressFieldSelect',function(){
            //console.log("copy next:"+ko.contextFor(this).$parents[1].copyNextSelected());
            //console.log("copy All:"+ko.contextFor(this).$parents[1].copyAllSelected());
            var parentIndex=ko.contextFor(this).$parentContext.$parentContext.$index();
            var index = ko.contextFor(this).$index();
            //console.log('blur parentIndex:'+parentIndex+':index:'+index);
            if(ko.contextFor(this).$parents[1].copyAllSelected())
                poCheckoutVM.copyAllCarePacks(parentIndex,index);
            else if(ko.contextFor(this).$parents[1].copyNextSelected())
                poCheckoutVM.copyCarePackToNext(parentIndex,index);
            
        });

        $(document).on('click', '.updateCarePack',function(){
            if(!poCheckoutVM.carePackValidate()){
                poCheckoutVM.prepareCarePackForUpdate();
            }
        });

        updateTextarea(poCheckoutVM);

        if(params.cartSummaryPage){
            poCheckoutVM.logSelection(params.cartSummaryPage.logSelection);
        }
        
        $(document).ready(function() {
            if(typeof ko.dataFor(document.getElementById(params.poCheckoutVM.elementId)) == 'undefined'){
                ko.applyBindings(poCheckoutVM, document.getElementById(params.poCheckoutVM.elementId));
            }
        });
        
        $(document).on("keyup",".poName", function(){
            poCheckoutVM.updateTopSectionEmptyFlag();
        });
        
        $(document).on("keyup",".poNumber", function(){
            poCheckoutVM.updateTopSectionEmptyFlag();
        });

        $(document).on('click','.removeProductFromCartSummary',function(){
        	poCheckoutVM.removeProductFromCartSummary(ko.dataFor(this).lineItemID);
        });

    }

	$(document).ready(function(){
		navViewModel.OrdersQuotesSelectFlag(true);
	});

    //the reason why we adore IE8 so much.
    $(document).on("click", ".carepack-section-head", function(event){
        var e = window.event || event;
        if(!e.currentTarget){
            $(this).next(".carePackHack").toggle();
            $(this).hasClass("h3active")?$(this).removeClass("h3active").addClass("h3inactive"):$(this).removeClass("h3inactive").addClass("h3active");
            ($(this).find("#js-expandtxt").text() == "Expand")?$(this).find("#js-expandtxt").text("Collapse"):$(this).find("#js-expandtxt").text("Expand");
        }
    });
    
	function updateTextarea(vm){
		$(document).on("blur",".textarea-notificationcomment", function(){
			vm.accountInfo().fields()[0].NotificationComment.values()[0] = $(this).html();
		});
		$(document).on("blur",".textarea-ordercompletioncomments", function(){
			vm.shippingInfo().fields()[0].OrderCompletionComments.values()[0] = $(this).html();
		});
		$(document).on("blur",".textarea-shippingInstruction", function(){
			vm.shippingInfo().fields()[0].ShippingInstruction.values()[0] = $(this).html();
		});
		$(document).on("blur",".textarea-invoiceinstructions", function(){
			vm.billingInfo().fields()[0].InvoiceInstructions.values()[0] = $(this).html();
		});
		$(document).on("blur",".textarea-specialinvoiceinstructions", function(){
			vm.billingInfo().fields()[0][Invoice/SpecialInstructions].values()[0] = $('.textarea-specialinvoiceinstructions').html();
		});
		$(document).on("blur",".textarea-Comments", function(){
			vm.contactInfo().fields()[0].Comment.values()[0] = $('.textarea-Comments').html();
		});
        
        $(document).on("focus",".textarea", function(){
            $(".textarea").limitText();
        });
    }

}(jQuery, params = window.hpelite.params, viewmodels = window.hpelite.viewmodels));