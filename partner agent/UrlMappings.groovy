class UrlMappings {

		static mappings = {
			name terms: "/terms"(view:"/checkout/terms")
			"/print"(view:"/cto/print")

			"/$controller/$action?/$id?"{
				constraints {
					// apply constraints here
				}
			}

			"/"(controller:'homepage',action:'home')
			"/rest/v1/cdb/$id?"(controller:"cdb", action:"retrieveCDBinfo")
			"/rest/v1/cto/$id?"(controller:"cto", action:"ctoInfo")
			"/rest/v1/stdcfg/$id?"(controller:"standardConfig", action:"retrieveStandardConfigInfo")
			"/rest/v1/nav/$id?"(controller:"navigation", action:"retrieveNavigationInfo")
			"/rest/v1/activity/$id?"(controller:"workFlowStatus", action:"retrieveActivityInfo")
			"/rest/products/$id?"(controller:"catalog", action:"retrieveProducts")
			"/rest/search/$id?"(controller:"SearchResults", action:"retrieveSearchResults")
			"/rest/org/%id?"(controller:"Home", action:"home")

			"/test"(view:"/errors/error500")
			"/favorites"(view:"landingpages/favoriteDetails")
			"/synccache"(controller:"syncCache", action:"index")

			//"/terms"(view:"/Elite/checkout/terms")
			
			name login:"/login"{
				controller = 'login'
				action = 'index'
			}
			
			name eliteEprocLogin:"/eliteEprocLogin"{
				controller='EprocData'
				action='eliteEprocLogin'
			}
			name sfdcEliteEntry:"/sfdcEliteEntry"{
				controller='EprocData'
				action='sfdcEliteEntry'
			}
			name bgSearch:"/bgSearch"{
				controller='EprocData'
				action='bgSearch'
			}
			name downloadJson:"/downloadJson"{
				controller='EprocData'
				action='downloadJson'
			}
			name createUser : "/createUser"{
				controller = 'UserProfile'
				action= 'createUser'
			}

			name manageUser : "/manageUser"{
				controller = 'UserProfile'
				action= 'manageUser'
			}

			name cancelUser : "/cancelUser"{
				controller = 'UserProfile'
				action= 'cancelUser'
			}

			name addUser : "/addUser"{
				controller = 'UserProfile'
				action= 'addUser'
			}

			name copyUser : "/copyUser"{
				controller = 'UserProfile'
				action= 'copyUser'
			}

			name saveAndCreateNew : "/saveAndCreateNew"{
				controller = 'UserProfile'
				action= 'saveAndCreateNew'
			}

			name modifyUser : "/modifyUser"{
				controller = 'UserProfile'
				action= 'modifyUser'
			}

			name successUserRegistration : "/successUserRegistration"{
				controller = 'UserProfile'
				action= 'successUserRegistration'
			}

			name getNCRFOrgHierarchy : "/getNCRFOrgHierarchy"{
				controller = 'UserProfile'
				action= 'retrieveNCRFOrgHierarchy'
			}

			name deleteUsers : "/deleteUsers"{
				controller = 'UserProfile'
				action= 'deleteUsers'
			}

			name editUser : "/editUser"{
				controller = 'UserProfile'
				action= 'editUser'
			}
			
			name redirectUser : "/redirectUser"{
				controller = 'UserProfile'
				action= 'resendRegistrationMail'
			}

			name reviewUser : "/reviewUser"{
				controller = 'UserProfile'
				action= 'reviewUser'
			}

			name searchUsers: "/searchUsers"{
				controller = 'UserProfile'
				action = 'searchUsers'
			}

			name updateRecentOrg: "/updateRecentOrg"{
				controller = 'UserProfile'
				action = 'updateRecentOrg'
			}

			name getRecentOrg: "/getRecentOrg"{
				controller = 'UserProfile'
				action = 'retrieveRecentOrg'
			}

			name selfRegister: "/selfRegister"{
				controller = 'CompleteRegistration'
				action = 'selfRegister'
			}

			name cacheClear: "/rest/v1/cache/clear"{
				controller = 'ExternalCache'
				action = 'clear'
			}

			name cacheClearKey: "/rest/v1/cache/clear/$key"{
				controller = 'ExternalCache'
				action = 'clear'
			}
			
			name cacheBypass: "/cache/settings"{
				controller = 'ExternalCache'
				action = 'settings'
			}

			name roles: "/rest/v1/role/$roleName?"{
				controller = 'Homepage'
				action = 'home'
			}
			name orgswitch: "/organizationSwitch"{
				controller = 'organizationSwitch'
				action = 'index'
			}
			name orderStatus: "/rest/v1/orderStatus"{
				controller = 'OrderStatus'
				action = 'EprimeOrderStatusInfo'
			}
			name orderStatusLP: "/orderStatus"{
				controller = 'OrderStatus'
				action = 'EprimeOrderStatusInfo'
				orderStatusFlag = true
			}
			name purchaseOrderPage: "/purchaseOrders"{
				controller = 'OrderStatus'
				action = 'EprimeOrderStatusInfo'
				LPFlag = true
			}

			name cdb: "/cdb" {
				controller = 'GenericList'
				action = 'index'
				type="companyStandards"
				page='1'
				pageSize='10'
			}

			name ctoExport : "/ctoExport"{
				controller = 'cto'
				action= 'exportToExcel'
			}

			name ctoEmailRequest : "/ctoEmailRequest"{
				controller = 'cto'
				action= 'ctoEmailRequest'
			}

			name ctoCarePackRequest : "/ctoCarePackRequest"{
				controller = 'cto'
				action= 'retrieveCarePackProducts'
			}

			name ctoPrintRequest : "/ctoPrintRequest"{
				controller = 'cto'
				action= 'ctoPrintRequest'
			}
			name programs: "/rest/v1/programs"{
				controller = 'programProduct'
				action = 'listPrograms'
			}

			name programProducts: "/rest/v1/programProducts" {
				controller = 'programProduct'
				action = 'listProgramProducts'
			}

			name standards: "/standards"{
				controller = 'programProduct'
				action = 'listPrograms'
				withProducts = 'true'
			}

			name workflowStatus: "/rest/v1/workflowStatus" {
				controller='workFlowStatus'
				action='retrieveActivityInfo'
				page='1'
			}
			//Purchase Requests-----------------------------------------
			name purchaseRequestsPage: "/purchaseRequests" {
				controller='workFlowStatus'
				action='retrieveActivityInfo'
			}

			name prCheckoutReviewPage: "/prCheckoutReview" {
				controller='workFlowStatus'
				action='retrievePRReviewInfo'
			}

			name prCheckoutSummaryPage: "/prCheckoutSummary" {
				controller='workFlowStatus'
				action='retrievePRSummaryInfo'
			}

			name createPR: "/createPR" {
				controller='workFlowStatus'
				action='createPR'
			}

			name forwardPR: "/forwardPR" {
				controller='workFlowStatus'
				action='forwardPR'
			}

			name approvePR: "/approvePR" {
				controller='workFlowStatus'
				action='approvePR'
			}
			
			

			// name updatePaymentMethodPR: "/rest/v1/updatePaymentMethodPR" {
			// 	controller='workFlowStatus'
			// 	action='updatePaymentMethodPR'
			// }

			// name prRefreshOrderExemptSelection: "/rest/v1/prRefreshOrderExemptSelection" {
			// 	controller='workFlowStatus'
			// 	action='prRefreshOrderExemptSelection'
			// }

			// name refreshShippingStatePR: "/rest/v1/refreshShippingStatePR" {
			// 	controller='workFlowStatus'
			// 	action='refreshShippingStatePR'
			// }

			// name refreshStatePR: "/rest/v1/refreshStatePR" {
			// 	controller='workFlowStatus'
			// 	action='refreshStatePR'
			// }

			// name refreshCountryPR: "/rest/v1/refreshCountryPR" {
			// 	controller='workFlowStatus'
			// 	action='refreshCountryPR'
			// }

			// name refreshBillingTypePR: "/rest/v1/refreshBillingTypePR" {
			// 	controller='workFlowStatus'
			// 	action='refreshBillingTypePR'
			// }

			name prCheckoutEdit: "/rest/v1/prEdit" {
				controller='workFlowStatus'
				action='retrievePREditInfo'
			}

			// name prCheckoutUpdate: "/rest/v1/prUpdate" {
			// 	controller='workFlowStatus'
			// 	action='updatePRInfo'
			// }

			name prCheckoutSave: "/rest/v1/prSave" {
				controller='workFlowStatus'
				action='savePRInfo'
			}

			name prReturnFromEprime: "/prReturnFromEprime"{
				controller='workFlowStatus'
				action='returnToPR'
			}
			//-----------------------------------------------------

			//Purchase Order --------------------------------------
			name poCheckoutEdit: "/rest/v1/poEdit" {
				controller='orderStatus'
				action='retrievePOEditInfo'
			}

			name poCheckoutSave: "/rest/v1/poSave" {
				controller='orderStatus'
				action='savePOInfo'
			}

			name createPOfromPR: "/createPOfromPR" {
				controller='workFlowStatus'
				action='createPOfromPR'
			}

			name poSummary: "/purchaseOrderSummary" {
				controller='orderStatus'
				action='retrievePOSummaryInfo'
			}

			name poReview: "/purchaseOrderReview"{
				controller= 'orderStatus'
				action = 'retrievePOReviewInfo'
			}

			name createPO: "/createPO" {
				controller='orderStatus'
				action='createPO'
			}

			// name poCheckoutUpdate: "/poCheckoutUpdate" {
			// 	controller= 'orderStatus'
			// 	action= 'updatePOInfo'
			// }

			name submitPO: "/submitPO" {
				controller= 'orderStatus'
				action= 'submitPO'
			}

			name poCheckoutCopy: "/purchaseOrderCopy"{
				controller ='orderStatus'
				action= 'copyPO'
			}

			// name poUpdatePaymentMethod: "/rest/v1/updatePaymentMethodPO" {
			// 	controller='orderStatus'
			// 	action='updatePaymentMethodPO'
			// }

			// name poRefreshOrderExemptSelection: "/rest/v1/poRefreshOrderExemptSelection" {
			// 	controller='orderStatus'
			// 	action='poRefreshOrderExemptSelection'
			// }

			// name refreshShippingStatePO: "/rest/v1/refreshShippingStatePO" {
			// 	controller='orderStatus'
			// 	action='refreshShippingStatePO'
			// }

			// name refreshStatePO: "/rest/v1/refreshStatePO" {
			// 	controller='orderStatus'
			// 	action='refreshStatePO'
			// }

			// name refreshCountryPO: "/rest/v1/refreshCountryPO" {
			// 	controller='orderStatus'
			// 	action='refreshCountryPO'
			// }

			// name refreshBillingTypePO: "/rest/v1/refreshBillingTypePO" {
			// 	controller='orderStatus'
			// 	action='refreshBillingTypePO'
			// }

			name poReturnFromEprime: "/poReturnFromEprime"{
				controller='orderStatus'
				action='returnToPO'
			}
			// -----------------------------------------------------

			//Quotes----------------------------------------------
			name quotes: "/rest/v1/quotes" {
				controller='quotes'
				action='retrieveQuotesInfo'
			}

			name quotesPage: "/quotes" {
				controller='quotes'
				action='retrieveQuotesInfo'
			}

			name quoteCheckout: "/quoteCheckout"{
				controller='quotes'
				action='retrieveQuoteReviewInfo'
			}

			name quoteSummary: "/quoteSummary"{
				controller='quotes'
				action='retrieveQuoteSummaryInfo'
			}

			name quoteCheckoutEdit: "/rest/v1/quoteEdit"{
				controller='quotes'
				action='retrieveQuoteEditInfo'
			}

			// name quoteCheckoutUpdate: "/rest/v1/quoteUpdate" {
			// 	controller='quotes'
			// 	action='updateQuoteInfo'
			// }

			name createNonCatalogQuote: "/createNonCatalogQuote"{
				controller='quotes'
				action='createNonCatalogQuote'
			}
			
			name quoteCheckoutSave: "/rest/v1/quoteSave"{
				controller='quotes'
				action='saveQuoteInfo'
			}

			name createQuote: "/createQuote" {
				controller='quotes'
				action='createQuote'
			}
			
			name quoteDownloadPDF: "/quoteDownloadPDF" {
				controller='quotes'
				action='downloadPDF'
				
			}
			
			name getAddressList: "/getAddressList" {
				controller='baseCheckout'
				action='docServiceGetAddressList'
			}
			
			name saveAddressInfo: "/saveAddressInfo" {
				controller='baseCheckout'
				action='docSaveAddressInfo'
			}
				
			name removeDocumentReDirect: "/removeDocumentReDirect" {
				controller='baseCheckout'
				action='removeDocumentReDirect'
			}
			name removeDocument: "/removeDocument" {
				controller='baseCheckout'
				action='removeDocument'
			}
			

			name quoteReturnFromEprime: "/quoteCheckoutReturn"{
				controller='quotes'
				action='returnToQuote'
			}

			name createPRFromQuote: "/prFromQuote"{
				controller='quotes'
				action='createPRFromQuote'
			}

			name createPOFromQuote: "/poFromQuote"{
				controller='quotes'
				action='createPOFromQuote'
			}

			name copyQuote: "/quoteCopy"{
				controller='quotes'
				action='copyQuote'
			}
			name reviseQuote:"/reviseQuote"{
				controller='quotes'
				action='reviseQuote'
			}

			name quoteSaveCopy: "/quoteSaveCopy"{
				controller ='quotes'
				action= 'saveQuoteCopy'
			}

			// name quoteUpdatePaymentMethod: "/rest/v1/updatePaymentMethod" {
			// 	controller='quotes'
			// 	action='updatePaymentMethod'
			// }

			// name quoteRefreshOrderExemptSelection: "/rest/v1/refreshOrderExemptSelection" {
			// 	controller='quotes'
			// 	action='refreshOrderExemptSelection'
			// }

			// name refreshShippingStateQuote: "/rest/v1/refreshShippingStateQuote" {
			// 	controller='quotes'
			// 	action='refreshShippingStateQuote'
			// }

			// name refreshStateQuote: "/rest/v1/refreshStateQuote" {
			// 	controller='quotes'
			// 	action='refreshStateQuote'
			// }

			// name refreshCountryQuote: "/rest/v1/refreshCountryQuote" {
			// 	controller='quotes'
			// 	action='refreshCountryQuote'
			// }

			// name refreshBillingTypeQuote: "/rest/v1/refreshBillingTypeQuote" {
			// 	controller='quotes'
			// 	action='refreshBillingTypeQuote'
			// }

			name checkOrderForEmpoweredRequester: "/documentFromQuote"{
				controller='quotes'
				action='checkOrderForEmpoweredRequester'
			}
	//----------------------------------------------------
	//------Shared mapping between quote,po,pr -------------
			name documentCartEdit: "/documentCartEdit"{
				controller = 'cart'
				action= 'documentCartEditView'
			}

			name handleMixedCartForB2Bi: "/handleMixedCartForB2Bi"{
				controller = 'cart'
				action= 'handleMixedCartForB2Bi'
			}
			
			name updateDocumentCart: "/documentCartUpdate"{
				controller = 'cart'
				action= 'updateOrder'
			}
			
			name updateRequisition: "/updateRequisition"{
				controller = 'cart'
				action= 'updateRequisition'
			}

			name emailDocument: "/rest/v1/emailDocument"{
				controller = 'emailDocument'
				action= 'emailDocument'
			}

			name updateCheckoutInfo: "/rest/v1/updateCheckoutInfo"{
				controller = 'baseCheckout'
				action = 'updateCheckoutInfo'
			}

			name updatePaymentMethod: "/rest/v1/updatePaymentMethod" {
				controller='baseCheckout'
				action='updatePaymentMethod'
			}

			name refreshOrderExemptSelection: "/rest/v1/refreshOrderExemptSelection" {
				controller='baseCheckout'
				action='refreshOrderExemptSelection'
			}

			name refreshShippingState: "/rest/v1/refreshShippingState" {
				controller='baseCheckout'
				action='refreshShippingState'
			}

			name refreshState: "/rest/v1/refreshState" {
				controller='baseCheckout'
				action='refreshState'
			}

			name refreshCountry: "/rest/v1/refreshCountry" {
				controller='baseCheckout'
				action='refreshCountry'
			}

			name refreshBillingType: "/rest/v1/refreshBillingType" {
				controller='baseCheckout'
				action='refreshBillingType'
			}
	//-------------------------------------------------------
			name catalogsPage: "/catalogCategory" {
				controller='catalogCategory'
				action='index'
			}
			
			name nonCatalogsPage: "/nonCatalogCategory" {
				controller='catalogCategory'
				action='nonCatalogIndex'
			}
			
			name nonCatalogToCart: "/nonCatalogToCart" {
				controller='catalogCategory'
				action='addNonCatalogToCart'
			}

			name productCatalogSearchPage: "/product/searchPaging" {
				controller='product'
				action='searchPaging'
			}
			name productEprocSearch: "/product/eprocSearch" {
				controller='product'
				action='eprocSearch'
			}
			name displayingItems: "/product/displayProducts" {
				controller='product'
				action='displayProducts'
			}
			name productCatalogPage: "/product/list" {
				controller='product'
				action='list'
			}
			name productCatalogPage: "/product/listCache" {
				controller='product'
				action='listCache'
			}
			name prefetchMultipleFacet: "/product/prefetchMultipleFacet" {
				controller='product'
				action='prefetchMultipleFacet'
			}
			name prefetchMultipleList: "/product/prefetchMultipleList" {
				controller='product'
				action='prefetchMultipleList'
			}
			name catalogImages: "/catalogCategory/topCategoryImages" {
				controller='catalogCategory'
				action='topCategoryImages'
			}

			name productAccessories: "/rest/v1/accessories"{
				controller='productDetails'
				action='retrieveProductFeatures'
			}

			name productOverview : "/rest/v1/overview/$sku?"{
				controller='productDetails'
				action='overviewDetails'
			}

			name productFeatures: "/rest/v1/features"{
				controller='productDetails'
				action='retrieveProductFeatures'
			}

			name productSupplies: "/rest/v1/supplies/$sku?"{
				controller='productSupplies'
				action='list'
			}

			name productTopSelling: "/rest/v1/topSelling"{
				controller='TopSellingProducts'
				action='list'
			}

			name productCrossSell: "/rest/v1/crossSell/$sku?"{
				controller='CrossSellProducts'
				action='retrieveCrossSellProducts'
			}

			name productInfo: "/rest/v1/productinfo/$sku?"{
				controller='ProductDetails'
				action='callRetrieveProductInfo'
			}

			name activity: "/activity" {
				controller = 'GenericList'
				action = 'index'
				type="activity"
				page='1'
				pageSize='10'
				sortby='date.desc'
			}
			name viewCart : "/viewCart"{
				controller = 'Cart'
				action= 'viewCart'
			}

			name miniCart : "/rest/v1/miniCart"{
				controller = 'Cart'
				action= 'miniCart'
			}
			
			name ctoDetails : "/ctoDetails"{
				controller = 'ProductDetails'
				action= 'details'
			}
			name cdbDetails : "/cdbDetails"{
				controller = 'Cdb'
				action= 'CDBDetails'
			}
			name updateCTO : "/rest/v1/updateCTO/$jsonStr?"{
				controller = 'cto'
				action= 'updateCTO'
			}
			name checkoutDiscard : "/checkout/discard"{
				controller = 'BaseCheckout'
				action= 'discardCheckout'
			}
			name updateCTONGC : "/rest/v1/updateCTONGC/$jsonStr?"{
				controller = 'cto'
				action= 'updateCTONGC'
			}
			name ctoAdd : "/ctoAdd"{
				controller = 'Cto'
				action= 'addCTO'
			}
			name customizeCTO : "/customizeCTO"{
				controller = 'Cto'
				action= 'customizeCTO'
			}
			name dummyCTO : "/cto"{
				controller = 'Cto'
				action= 'retrieveDummyCTO'
			}
			name customizeCTONGC : "/customizeCTONGC"{
				controller = 'cto'
				action= 'renderCTONgc'
			}
			name resetCTO : "/resetCTO"{
				controller = 'Cto'
				action= 'resetCTO'
			}
			name validateCTO : "/validateCTO"{
				controller = 'Cto'
				action= 'validateCTO'
			}
			name productDetails:"/productDetails/details"{
				controller = 'ProductsDetails'
				action= 'details'
			}
			name cdbItem: "/cdbItem"{
				controller = 'Cdb'
				action= 'CDBItemInfo'
			}
			name documents: "/documents"{
				controller = 'GenericList'
				action= 'index'
				type='documents'
				page='1'
				pageSize='10'
				sortby='title.asc'
			}
			name compare: "/compare"{
				controller = 'ProductCompare'
				action= 'compareItems'
			}
			name export: "/export"{
				controller = 'exportCatalog'
				action= 'index'	}
			name suggest: "/suggest"{
				controller = 'product'
				action = 'suggest'
			}
			name quickbuyUpload: "/quickbuyUpload"{
				controller = 'quickBuy'
				action = 'uploadFile'
			}
			name quickbuy : "/rest/v1/quickbuy/$skus?"{
				controller = 'Product'
				action= 'quickBuy'
			}
			name reports: "/rest/v1/reports"{
				controller = 'documents'
				action = 'retrieveDocuments'
				page='1'
				pageSize='10'
				sortby='title.asc'
			}
			name notifications: "/rest/v1/notifications"{
				controller = 'notification'
				action = 'showNotification'
			}

			//messages--------------------------------
			name accountMessages: "/rest/v1/message"{
				controller = 'accountMessage'
				action = 'showMessage'
			}

			name accountCommLP: "/accountMessages"{
				controller = 'AccountMessage'
				action = 'showMessage'
				format = 'html'
			}

			name accountMessageCount: "/rest/v1/messageCount"{
				controller = 'baseMessage'
				action = 'retrieveUnreadMessageCount'
			}

			name updateReadMessage: "/rest/v1/updateReadMessage"{
				controller = 'baseMessage'
				action = 'updateReadMessage'
			}

			name accountMessageSoftDelete: "/rest/v1/softMessageDelete"{
				controller = 'accountMessage'
				action= 'softDeleteMessage'
			}

			name targetAccountsList: "/rest/v1/targetAccountList"{
				controller = 'manageAccountMessage'
				action = 'targetAccountList'
			}

			name publishHistory: "/rest/v1/publishHistory"{
				controller = 'manageAccountMessage'
				action = 'adminMessagePublishHistory'
			}

			name accountMessageSearch: "/rest/v1/accountSearch"{
				controller = 'manageAccountMessage'
				action= 'accountSearch'
			}

			name accountMessageFilter: "/rest/v1/accountFilter"{
				controller = 'manageAccountMessage'
				action= 'filterAccountMessages'
			}
			//--------------------------------------------------

			name favorites: "/rest/v1/favorites"{
				controller = 'cdb'
				action = 'CDBinfo'
			}

			name favoritesPage: "/favorites" {
				controller='cdb'
				action='CDBinfo'
			}
			name favoriteDetails: "/cdb/CDBDetails" {
				controller='cdb'
				action='CDBDetails'
			}
			name createFavorite: "/createFavorite" {
				controller='cdb'
				action='CDBRequest'
			}

			name myLinks: "/myLinks"{
				controller = 'MyLinks'
				action = 'retrieveMyLinksInfo'
			}

			name generic: "/genericPage/index"{
				controller = 'GenericPage'
				action = 'index'
			}

			name generic: "/genericPage/$pageName"{
				controller = 'GenericPage'
				action = 'page'
			}

			name saveCTOAjax : "/saveCTOAjax"{
				controller = 'Cto'
				action= 'saveCTOAjax'
			}

			name saveCTO : "/saveCTO"{
				controller = 'Cto'
				action= 'saveCTO'
			}

			"/orderStatusList"(view:"/orderStatusListView/orderStatusListView")
			"403"(view:"/errors/error403")
			"404"(view:"/errors/error404")
			"500"(view:"/errors/error500")

			//Organization Listing
			"/orgList"(view:"/organization/organizationStatus")

			name orgListing: "/orgListing"{
				controller = 'Organization'
				action = 'retrieveOrgInfo'
			}

			name orgAutocomplete: "/orgAutocomplete"{
				controller = 'Organization'
				action = 'orgAutocomplete'
			}


			name admin: "/admin"{
				controller = 'Admin'
				action = 'home'
			}

			name orgUsers: "/organisation/users"{
				controller = 'userlist'
				action = 'index'
			}
			
			name b2bEprocData: "/b2bEprocData"{
				controller = 'EprocData'
				action = 'b2bEprocData'
			}
			name b2bEprocData: "/eprocEntry"{
				controller = 'EprocData'
				action = 'eprocEntry'
			}
			name addToRequisition: '/addToRequisition'{
				controller='cart'
				action='addToRequisition'
			}
			
			name termsAndConditionsText: "/termsAndConditionsText" {
				controller='cart'
				action='termsAndConditions'
			}

			name impersonatePage: "/impersonate"{
				controller = 'Impersonate'
				action = 'impersonateInfo'
			}
			
			name unimpersonatePage: "/unimpersonate"{
				controller = 'ImpersonateUser'
				action = 'unimpersonate'
			}
			
			name dumpImpersonateRestrictions: "/dumprestrictions"{
				controller = 'ImpersonateUser'
				action ='dumprestrictions'
			}
			
			name manageAccountMessage: "/manageAccountMessage"{
				controller = 'ManageAccountMessage'
				action = 'showListMessages'
			}

			name updateImpersonatedAccounts: "/rest/v1/updateImpersonatedAccounts"{
				controller = 'Impersonate'
				action = 'updateImpersonatedAccounts'
			}

			name searchImpersonationAccount: "/rest/v1/searchImpersonationAccount"{
				controller = 'Impersonate'
				action = 'searchImpersonationAccount'
			}

			name filterImpersonationAccount: "/rest/v1/filterImpersonationAccount"{
				controller = 'Impersonate'
				action = 'filterImpersonationAccount'
				filterFlag = 'true'
			}

			name retrieveImpersonateSubOrgInfo: "/rest/v1/getSubOrgInfo"{
				controller = 'Impersonate'
				action ='retrieveImpersonateSubOrgInfo'
			}

			name headerFavorites: "/headerFavorites"{
				controller = 'cdb'
				action = 'cdbNamesForDropdown'
			}

			name headerCategories: "/headerCategories"{
				controller = 'catalogCategory'
				action = 'navCategories'
			}

			name headerStandards: "/headerStandards"{
				controller = 'programProduct'
				action = 'listStandards'
			}

			name headerMyLinks: "/headerMyLinks"{
				controller = "myLinks"
				action = "retrieveMyLinks"
			}

			name headerMessageCount: '/headerMessageCount'{
				controller = "baseMessage"
				action = "retrieveUnreadMessageCount"
			}

			name headerAjaxData: '/headerJsonData'{
				controller = "navigation"
				action = "retrieveAjaxNavContent"
			}
			
			name supportInfo: '/rest/v1/support'{
				controller = "support"
				action = "retrieveSupportInfo"
			}
				
			name getDisplayCurrency:"/getDisplayCurrency"{
				controller='navigation'
				action='retrieveSecondryCurrencyList'
			}
			name setDisplayCurrency:"/setDisplayCurrency"{
				controller='navigation'
				action='saveDisplayCurrencies'
			}
			name getDefaultDisplayCurrency:"/getDefaultDisplayCurrency"{
				controller='navigation'
				action='retrieveDefaultDisplayCurrency'
			}
			
			
			name displayLangURL:"/displayLangURL"{
				controller='navigation'
				action='saveDisplayLanguage'
			}
			
			name getLanguageURL:"/getLanguageURL"{
				controller='navigation'
				action='fetchAllLanguage'
			}

			name error500: "/error"{
				controller='baseErrorHandling'
				action='renderErrorPage'
			}

			name errorAjax: "/error/ajax"{
				controller='baseErrorHandling'
				action='renderErrorHandlingPage'
			}
			
			name quoteAttachment:"/quoteAttachment"{
				controller = 'quotes'
				action = "attachmentDownload"
			}
			
			name removeFromCartSummary:"/removeFromCartSummary"{
				controller = 'baseCheckout'
				action = "removeFromCartSummary"
			}
			
			name updateFromCartSummary:"/updateFromCartSummary"{
				controller = 'baseCheckout'
				action = "updateFromCartSummary"
			}
			
			name completeCartSummaryUpdate:"/completeCartSummaryUpdate"{
				controller = 'baseCheckout'
				action = "completeCartSummaryUpdate"
			}
			
			name updateCheckoutDocument:"/updateCheckoutDocument"{
				controller = 'baseCheckout'
				action = "updateCheckoutDocument"
			}
			
			name changePurchasingView : "/changePurchasingView"{
				controller = "userCatalog"
				action = "changePurchasingView"
			}
			
			name userCatalogLoad : '/userCatalogLoad'{
				controller = "userCatalog"
				action = "userCatalogLoad"
			}
			
			name shopMoreItems:"/shopMoreItems"{
				controller='baseCheckout'
				action='shopMoreItems'
			}
			
			name shopForAdditionalItems:"/shopForAdditionalItems"{
				controller='baseCheckout'
				action='shopForAdditionalItems'
			}
			
			name partnerAgentOrgs: "/partnerAgentOrgs" {
				controller='partnerAgent'
				action='retriveUserOrganizations'
			}
			
			name partnerAgentCountry: "/partnerAgentCountry" {
				controller='partnerAgent'
				action='retriveUsers'
			}
			name partnerAgentCurrency: "/partnerAgentCurrency" {
				controller='partnerAgent'
				action='retriveCurrency'
			}
			name partnerAgentSearch: "/Elite/partnerAgentSearch" {
				controller='partnerAgent'
				action='retriveSearchResults'
			}
			
			name extendSession:"/extendSession"{
				controller='logout'
				action='extendSession'
			}

			name bundleUpdateCart:"/bundleUpdateCart"{
				controller='cart'
				action='bundleUpdateCart'
			}
			
			"/getUserDetails"(view:"/userlist/displayUserList")
			"/userCard"(view:"/userCart/allCarts")
			"/getAllUsers"(view:"/userlist/allUsers")
			"/error"(view:"/errors/error500")


		}
	}
