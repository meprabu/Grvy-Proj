modules = {

	maincss{
		defaultBundle 'core'

		resource url: 'css/reset.css', disposition: 'head'
		resource url: 'css/grid.css', disposition: 'head'
		resource url: 'css/buttons.css', disposition: 'head'
		resource url: 'css/settings.css', disposition: 'head'
		resource url: 'css/stylesheet.css', disposition: 'head'
		resource url: 'css/mobile.css', attrs:[media:'screen and (min-width: 0) and (max-width: 48em)'], disposition: 'head'
		resource url: 'css/tablet.css', attrs:[media:'screen and (min-width: 48.063em) and (max-width: 64em)'], disposition: 'head'
		resource url: 'css/screen_small.css', attrs:[media:'only screen and (min-width: 64.063em) and (max-width: 75em)'], disposition: 'head'
		resource url: 'css/screen.css', attrs:[media:'only screen and (min-width: 64.063em)'], disposition: 'head'
		resource url: 'css/print.css', attrs:[media:'print'], disposition: 'head'
	}

	jqueryui{ resource url: 'css/jquery-ui.css' }

	mobile_elite{
		defaultBundle 'mobile_elite'

		resource url: 'js/plugins/min/fixed.js'
		resource url: 'js/plugins/min/hide-address-bar.js'
		resource url: 'js/plugins/min/overthrow.js'
	}

	ajaxFormjs{ resource url: 'js/plugins/min/jquery-form.js' }
	
	ie7docCheck{ resource url: 'js/helpers/ie7docCheck.min.js', disposition:'head' }

	html5shivprintshiv{ resource url: 'js/helpers/html5shiv-printshiv.min.js', disposition:'head' }
	
	appjs {
		defaultBundle 'core'

		resource url: 'js/app.js', disposition: 'head'
	}

	knockout{
		defaultBundle 'core'

		resource url: 'js/vendor/knockout-3.0.0.js', disposition: 'head'
		// resource url: 'js/vendor/knockout-3.0.0.debug.js', disposition: 'head'
		
		resource url: 'js/vendor/knockout.reactor.debug.js', disposition: 'head'
		resource url: 'js/vendor/knockout.mapping-latest.debug.js', disposition: 'head'
		//resource url: 'js/vendor/knockout.wrap.min.js', disposition: 'head'
		resource url: 'js/extensions/koExtensions.js', disposition: 'head'
		resource url: 'js/custombindings/customBindings.js', disposition: 'head'
	}

	jqueryext {
		defaultBundle 'core'
		dependsOn 'maincss'

		resource url: 'js/vendor/jquery-1.9.1.min.js', disposition:'head'
		// resource url: 'js/vendor/jquery-1.9.1.js', disposition:'head'
	}

	mainjs {
		defaultBundle 'core'
		dependsOn 'jqueryext'

		resource url: 'js/main/main.js'
		// resource url: 'js/main/mediaQueries.js'
	}

	utilsjs{
		defaultBundle 'core'
		dependsOn 'jqueryext'
		dependsOn 'appjs'

		resource url: 'js/helpers/utils.js'
	}

	cookiejs {
		defaultBundle 'core'
		dependsOn 'jqueryext'

		resource url: 'js/plugins/min/jquery-cookie.js'
	}

	cookieHandlerjs{
		defaultBundle 'core'
		dependsOn 'cookiejs'

		resource url: 'js/jquery.widget.elite.js'
	}

	jquery_plugins{
		defaultBundle 'core'
		dependsOn 'jqueryext'

		resource url: 'js/plugins/min/jquery-ui.js'
		// resource url: 'js/plugins/enquire.js'
		resource url: 'js/plugins/min/jquery-checkbox.js'
		resource url: 'js/plugins/min/accounting.js'
		resource url: 'js/plugins/min/jquery-easing.js'
		resource url: 'js/plugins/min/jquery-html5-placeholder-shim.js'
		resource url: 'js/plugins/min/slideToggle.js'
		resource url: 'js/plugins/min/uploadUi.js'
		resource url: 'js/plugins/min/scroll-to.js'
		resource url: 'js/plugins/min/jquery-fs-selecter.js'
		resource url: 'js/plugins/min/appendAround.js'
		resource url: 'js/plugins/min/jquery-ellipsis.js'
		resource url: 'js/plugins/min/owl-carousel.js'
		resource url: 'js/plugins/autoresize.js'
		//resource url: 'js/plugins/min/masonry.js'
	}

	omniture{
		defaultBundle 'core'

		resource url: 'js/pages/omnitureSerialGenerator.js'
	}

	core {
		defaultBundle 'core'
		dependsOn 'maincss, appjs, jqueryext, knockout, jquery_plugins, cookiejs, cookieHandlerjs, mainjs, utilsjs, omniture'
	}

	loginPage{
		defaultBundle 'loginPage'

		resource url: 'js/viewmodels/loginVM.js'
		resource url: 'js/pages/login.js'
	}

	header{
		defaultBundle 'header'

		resource url: 'js/cache/prefetch.js'
		resource url: 'js/viewmodels/navVM.js'
		resource url: 'js/pages/header.js'
	}

	sort{
		resource url: 'js/viewmodels/baseSortVM.js'
		resource url: 'js/models/columnModel.js'
	}

	product{
		resource url: 'js/models/productModel.js'
		resource url: 'js/models/categoryModel.js'
	}

	homepage {
		defaultBundle 'homepage'
		dependsOn 'sort, product'

		//resource url: 'js/models/columnModel.js'//, bundle: 'sort'
		//resource url: 'js/viewmodels/baseSortVM.js'//, bundle: 'sort'
		resource url: 'js/models/urlModel.js'
		resource url: 'js/viewmodels/urlListViewModel.js'
		resource url: 'js/viewmodels/attachmentsVM.js'
		// resource url: 'js/models/productModel.js'//, bundle: 'product'
		// resource url: 'js/models/categoryModel.js'//, bundle: 'product'
		resource url: 'js/viewmodels/baseCTOVM.js'
		resource url: 'js/viewmodels/standardsVM.js'
		resource url: 'js/viewmodels/catalogsVM.js'
		resource url: 'js/models/workflowDocModel.js'
		resource url: 'js/viewmodels/workflowVM.js'
		resource url: 'js/models/cdbModel.js'
		resource url: 'js/viewmodels/favoritesVM.js'
		resource url: 'js/viewmodels/quotesVM.js'
		resource url: 'js/models/orderStatusDocModel.js'
		resource url: 'js/viewmodels/orderStatusVM.js'
		resource url: 'js/models/messageModel.js'
		resource url: 'js/viewmodels/notificationsVM.js'
		resource url: 'js/viewmodels/supportVM.js'
		resource url: 'js/viewmodels/accountCommunicationVM.js'
		resource url: 'js/models/accountMessageModel.js'
		resource url: 'js/viewmodels/baseMessageVM.js'
		resource url: 'js/viewmodels/userCatalogVM.js'
		resource url: 'js/cache/prefetch.js'
		resource url: 'js/pages/home.js'
		resource url: 'js/viewmodels/myLinksVM.js'
		resource url: 'js/pages/omnitureHome.js'
		resource url: 'js/pages/omnitureFavorites.js'
		resource url: 'js/pages/omnitureStandards.js'
	}

	// quotes{
	// 	dependsOn 'sort, product'
	// }

	// favorites{
	// 	dependsOn 'sort, product'
	// }

	// purchaseRequests{
	// 	dependsOn 'sort, product'
	// }

	// purchaseOrder{
	// 	dependsOn 'sort, product'
	// }

	// orderStatus{
	// 	dependsOn 'sort, product'
	// }

	// accountCommunication{
	// 	dependsOn 'sort, product'
	// }



	landingPage{
		defaultBundle 'landingPage'
		dependsOn 'header'
		resource url: 'js/viewmodels/baseCTOVM.js'//, bundle: 'product'
		resource url: 'js/models/productModel.js'//, bundle: 'product'
		resource url: 'js/models/categoryModel.js'//, bundle: 'product'
		resource url: 'js/viewmodels/standardsVM.js'
		resource url: 'js/viewmodels/quotesLPVM.js'
		resource url: 'js/viewmodels/favoritesLPVM.js'
		resource url: 'js/models/cdbModel.js'
		resource url: 'js/models/hpSupportModel.js'
		resource url: 'js/models/workflowDocModel.js'
		resource url: 'js/viewmodels/purchaseRequestsLPVM.js'
		resource url: 'js/viewmodels/purchaseOrderLPVM.js'
		resource url: 'js/models/orderStatusDocModel.js'
		resource url: 'js/models/orderStatusDocModel.js'
		resource url: 'js/viewmodels/orderStatusLPVM.js'
		resource url: 'js/models/columnModel.js'//, bundle: 'sort'
		resource url: 'js/viewmodels/baseSortVM.js'//, bundle: 'sort'
		resource url: 'js/viewmodels/paginationVM.js'//, bundle: 'sort'
		resource url: 'js/viewmodels/solrPaginationVM.js'//, bundle: 'solr'
		resource url: 'js/viewmodels/filterVM.js'//, bundle: 'solr'
		resource url: 'js/viewmodels/baseMessageVM.js'//, bundle: 'messages'
		resource url: 'js/models/accountMessageModel.js'//, bundle: 'messages'
		resource url: 'js/viewmodels/accountCommunicationLPVM.js'//, bundle: 'messages'
		resource url: 'js/models/messageModel.js'//, bundle: 'messages'
		resource url: 'js/viewmodels/catalogsVM.js'
		resource url: 'js/models/facetValueModel.js'//, bundle: 'solr'
		resource url: 'js/models/facetModel.js'//, bundle: 'solr'
		resource url: 'js/viewmodels/facetFiltersVM.js'//, bundle: 'solr'
		resource url: 'js/viewmodels/productCatalogVM.js'
		resource url: 'js/viewmodels/searchResultsVM.js'
		resource url: 'js/viewmodels/hpSupportVM.js'
		resource url: 'js/viewmodels/attachmentsVM.js'
		resource url: 'js/pages/landingPage.js'
	}

	b2biLandingPage {
		defaultBundle 'b2biLandingPage'
		dependsOn 'header'
		resource url: 'js/viewmodels/nonCatalogVM.js'

		resource url: 'js/pages/b2biLandingPage.js'
	}

	ctopage{
		dependsOn 'core'
		resource url: 'js/models/productModel.js'//, bundle: 'product'
		resource url: 'js/models/chunkModel.js'//, bundle: 'product'
		resource url: 'js/models/imageModel.js'//, bundle: 'product'
		resource url: 'js/models/productContentModel.js'//, bundle: 'product'
		resource url: 'js/models/facetValueModel.js'//, bundle: 'facet'
		resource url: 'js/models/facetModel.js'//, bundle: 'facet'
		resource url: 'js/viewmodels/facetFiltersVM.js'//, bundle: 'facet'
		resource url: 'js/models/bundleModel.js'
		// resource url: 'js/plugins/deep-diff.js'
		resource url: 'js/plugins/min/jquery-stepper.js'
		resource url: 'js/viewmodels/ctoVM.js'
		resource url: 'js/pages/cto.js'
		resource url: 'js/pages/omnitureCTO.js'
	}

	cartpage{
		defaultBundle 'cartpage'
		dependsOn 'core'

		resource url: 'js/models/basketButtonModel.js'
		resource url: 'js/models/fieldModel.js'
		resource url: 'js/models/fieldDataModel.js'
		resource url: 'js/models/cartModel.js'
		resource url: 'js/viewmodels/baseCTOVM.js'
		resource url: 'js/models/udfModel.js'
		resource url: 'js/models/basketMessageListModel.js'
		resource url: 'js/models/basketMessageModel.js'
		resource url: 'js/viewmodels/cartVM.js'
		resource url: 'js/pages/cart.js'
		resource url: 'js/pages/omnitureCart.js'		
	}

	manageuserpage{
		defaultBundle 'manageuserpage'
		dependsOn 'core'

		resource url: 'js/models/columnModel.js'//, bundle: 'sort'
		resource url: 'js/viewmodels/baseSortVM.js'//, bundle: 'sort'
		resource url: 'js/models/manageUserDataModel.js'
		resource url: 'js/models/configModel.js'
		resource url: 'js/viewmodels/searchUserVM.js'
		resource url: 'js/pages/searchUser.js'
	}

	testpage{
		defaultBundle 'testpage'
		dependsOn 'core'
	}

	confirmuserpage{
		defaultBundle 'confirmuserpage'
		dependsOn 'core'

		resource url: 'js/models/columnModel.js'//, bundle: 'sort'
		resource url: 'js/viewmodels/baseSortVM.js'//, bundle: 'sort'
		resource url: 'js/models/orgDocModel.js'
		resource url: 'js/models/configModel.js'
		resource url: 'js/models/ncrfOrgModel.js'
		resource url: 'js/models/userInformationModel.js'
		resource url: 'js/models/userRoleModel.js'
		resource url: 'js/models/userPermissionModel.js'
		resource url: 'js/viewmodels/orgUsersVM.js'
		resource url: 'js/viewmodels/confirmUserVM.js'
		resource url: 'js/plugins/min/jquery-checktree.js'
		resource url: 'js/pages/confirmUser.js'
	}

	reviewuserpage{
		defaultBundle 'reviewuserpage'
		dependsOn 'core'

		resource url: 'js/models/columnModel.js'//, bundle: 'sort'
		resource url: 'js/viewmodels/baseSortVM.js'//, bundle: 'sort'
		resource url: 'js/models/orgDocModel.js'
		resource url: 'js/models/configModel.js'
		resource url: 'js/models/ncrfOrgModel.js'
		resource url: 'js/models/userInformationModel.js'
		resource url: 'js/models/visitedOrgModel.js'
		resource url: 'js/models/userRoleModel.js'
		resource url: 'js/models/userPermissionModel.js'
		resource url: 'js/viewmodels/orgUsersVM.js'
		resource url: 'js/viewmodels/reviewUserVM.js'
		resource url: 'js/plugins/min/jquery-checktree.js'
		resource url: 'js/pages/reviewUser.js'
	}

	reviewuserpage{
		defaultBundle 'reviewuserpage'
		dependsOn 'core'

		resource url: 'js/models/columnModel.js'//, bundle: 'sort'
		resource url: 'js/viewmodels/baseSortVM.js'//, bundle: 'sort'
		resource url: 'js/models/orgDocModel.js'
		resource url: 'js/models/configModel.js'
		resource url: 'js/models/ncrfOrgModel.js'
		resource url: 'js/models/userInformationModel.js'
		resource url: 'js/models/visitedOrgModel.js'
		resource url: 'js/models/userRoleModel.js'
		resource url: 'js/models/userPermissionModel.js'
		resource url: 'js/viewmodels/orgUsersVM.js'
		resource url: 'js/viewmodels/reviewUserVM.js'
		resource url: 'js/plugins/min/jquery-checktree.js'
		resource url: 'js/pages/reviewUser.js'
	}

	edituserpage{
		defaultBundle 'edituserpage'
		dependsOn 'core'

		resource url: 'js/models/columnModel.js'//, bundle: 'sort'
		resource url: 'js/viewmodels/baseSortVM.js'//, bundle: 'sort'
		resource url: 'js/models/orgDocModel.js'
		resource url: 'js/models/configModel.js'
		resource url: 'js/models/ncrfOrgModel.js'
		resource url: 'js/models/userInformationModel.js'
		resource url: 'js/models/visitedOrgModel.js'
		resource url: 'js/models/userRoleModel.js'
		resource url: 'js/models/userPermissionModel.js'
		resource url: 'js/viewmodels/orgUsersVM.js'
		resource url: 'js/viewmodels/editUserVM.js'
		resource url: 'js/plugins/min/jquery-checktree.js'
		resource url: 'js/pages/editUser.js'
	}

	createuserpage{
		defaultBundle 'createuserpage'
		dependsOn 'core'

		resource url: 'js/models/columnModel.js'//, bundle: 'sort'
		resource url: 'js/viewmodels/baseSortVM.js'//, bundle: 'sort'
		resource url: 'js/models/orgDocModel.js'
		resource url: 'js/models/configModel.js'
		resource url: 'js/models/ncrfOrgModel.js'
		resource url: 'js/models/userInformationModel.js'
		resource url: 'js/models/visitedOrgModel.js'
		resource url: 'js/models/userRoleModel.js'
		resource url: 'js/models/userPermissionModel.js'
		resource url: 'js/viewmodels/orgUsersVM.js'
		resource url: 'js/viewmodels/createUserVM.js'
		resource url: 'js/plugins/min/jquery-checktree.js'
		resource url: 'js/pages/createUser.js'
	}

	completeregistrationpage{
		defaultBundle 'completeregistrationpage'
		dependsOn 'core'

		resource url: 'js/models/configModel.js'
		resource url: 'js/models/ncrfOrgModel.js'
		resource url: 'js/models/userInformationModel.js'
		resource url: 'js/models/userRoleModel.js'
		resource url: 'js/models/userPermissionModel.js'
		resource url: 'js/viewmodels/completeRegistrationVM.js'
		resource url: 'js/pages/completeRegistration.js'
	}

	productDetails {
		defaultBundle 'productDetails'
		dependsOn 'core'

		resource url: 'js/viewmodels/baseCTOVM.js'//, bundle: 'product'
		resource url: 'js/models/productModel.js'//, bundle: 'product'
		resource url: 'js/models/chunkModel.js'//, bundle :'product'
		resource url: 'js/models/imageModel.js'//, bundle: 'product'
		resource url: 'js/models/productContentModel.js'//, bundle: 'product'
		resource url: 'js/models/facetValueModel.js'//, bundle: 'sort'
		resource url: 'js/models/facetModel.js'//, bundle: 'sort'
		resource url: 'js/viewmodels/facetFiltersVM.js'//, bundle: 'sort'
		resource url: 'js/models/bundleModel.js'
		resource url: 'js/viewmodels/productDetailsVM.js'
		resource url: 'js/pages/productDetails.js'
		resource url: 'js/plugins/min/imageGallery.js'
		resource url: 'js/pages/omnitureProduct.js'
	}

	standards {
		defaultBundle 'standards'
		dependsOn 'core'

		resource url: 'js/plugins/min/sliderCarousel.js'
	}

	orgpage{
		defaultBundle 'orgpage'
		dependsOn 'core'

		resource url: 'js/models/columnModel.js'//, bundle: 'sort'
		resource url: 'js/viewmodels/baseSortVM.js'//, bundle: 'sort'
		resource url: 'js/models/orgDocModel.js'
		resource url: 'js/viewmodels/orgVM.js'
		resource url: 'js/pages/orghome.js'
	}


	orgUserPage {

		defaultBundle 'userHome'
		dependsOn 'core'

		resource url: 'js/models/columnModel.js'//, bundle: 'sort'
		resource url: 'js/viewmodels/baseSortVM.js'//, bundle: 'sort'
		resource url: 'js/models/orgUserDataModel.js'
		resource url: 'js/viewmodels/orgUsersVM.js'
		resource url: 'js/pages/orgUsersHome.js'
	}

	quoteCheckout{
		defaultBundle 'quoteCheckout'
		dependsOn 'core'

		resource url: 'js/viewmodels/baseCTOVM.js'
		resource url: 'js/models/checkoutCartLineItemsModel.js'
		resource url: 'js/models/productModel.js'
		resource url: 'js/viewmodels/editCheckoutVM.js'
		resource url: 'js/viewmodels/baseCheckoutVM.js'
		resource url: 'js/viewmodels/baseDocumentVM.js'
		resource url: 'js/viewmodels/logisticsCheckoutVM.js'
		resource url: 'js/viewmodels/quoteCheckoutVM.js'
		resource url:'js/viewmodels/attachmentsVM.js'
		resource url:'js/viewmodels/downloadAttachmentVM.js'
		resource url: 'js/pages/checkout.js'
		
		//	resource url: 'js/pages/omnitureCheckout.js'
		resource url: "js/models/columnModel.js"
		resource url: "js/viewmodels/baseSortVM.js"
		resource url: "js/viewmodels/filterVM.js"
		resource url: 'js/viewmodels/paginationVM.js'//, bundle: 'sort'
		resource url: 'js/viewmodels/solrPaginationVM.js'//, bundle: 'solr'
		resource url: 'js/viewmodels/quotesLPVM.js'
		resource url:'js/viewmodels/locationSelectVM.js'
		resource url: 'js/pages/locationSelect.js'
		
		
		
		
		

	}

	prCheckout{
		defaultBundle 'prCheckout'
		dependsOn 'core'

		resource url: 'js/viewmodels/baseCTOVM.js'
		resource url: 'js/models/checkoutCartLineItemsModel.js'
		resource url: 'js/models/productModel.js'
		resource url: 'js/viewmodels/editCheckoutVM.js'
		resource url: 'js/viewmodels/baseCheckoutVM.js'
		resource url: 'js/viewmodels/logisticsCheckoutVM.js'
		resource url: 'js/models/carePackModel.js'
		resource url: 'js/models/carePackAddressModel.js'
		resource url: 'js/models/carePackFieldsModel.js'
		resource url: 'js/viewmodels/carePackCheckoutVM.js'
		resource url: 'js/viewmodels/prCheckoutVM.js'
		resource url:'js/viewmodels/attachmentsVM.js'
		resource url:'js/viewmodels/downloadAttachmentVM.js'
		resource url: 'js/pages/checkout.js'
		//resource url: 'js/pages/omnitureCheckout.js'
		resource url: "js/models/columnModel.js"
		resource url: "js/viewmodels/baseSortVM.js"
		resource url: "js/viewmodels/filterVM.js"
		resource url: 'js/viewmodels/paginationVM.js'//, bundle: 'sort'
		resource url: 'js/viewmodels/solrPaginationVM.js'//, bundle: 'solr'
		resource url: 'js/viewmodels/quotesLPVM.js'
		resource url:'js/viewmodels/locationSelectVM.js'
		resource url: 'js/pages/locationSelect.js'
		
		
		
		
		
	}

	poCheckout{
		defaultBundle 'poCheckout'
		dependsOn 'core'
		
		resource url: 'js/viewmodels/baseCTOVM.js'
		resource url: 'js/models/checkoutCartLineItemsModel.js'
		resource url: 'js/viewmodels/editCheckoutVM.js'
		resource url: 'js/viewmodels/baseCheckoutVM.js'
		resource url: 'js/viewmodels/logisticsCheckoutVM.js'
		resource url: 'js/models/carePackModel.js'
		resource url: 'js/models/carePackAddressModel.js'
		resource url: 'js/models/carePackFieldsModel.js'
		resource url: 'js/viewmodels/carePackCheckoutVM.js'
		resource url: 'js/viewmodels/poCheckoutVM.js'
		resource url:'js/viewmodels/attachmentsVM.js'
		resource url:'js/viewmodels/downloadAttachmentVM.js'
		resource url: 'js/pages/checkout.js'
		//resource url: 'js/pages/omnitureCheckout.js'
		resource url: "js/viewmodels/baseSortVM.js"	
		resource url: "js/models/columnModel.js"
		resource url: "js/viewmodels/filterVM.js"
		resource url: 'js/viewmodels/paginationVM.js'//, bundle: 'sort'
		resource url: 'js/viewmodels/solrPaginationVM.js'//, bundle: 'solr'
		resource url: 'js/viewmodels/quotesLPVM.js'
		resource url:'js/viewmodels/locationSelectVM.js'
		resource url: 'js/pages/locationSelect.js'
		
	
		
		
		
	}

	summary{
		defaultBundle 'summary'
		dependsOn 'core'
		resource url: 'js/viewmodels/baseDocumentVM.js'
		resource url: 'js/viewmodels/quoteSummaryVM.js'
		resource url: 'js/viewmodels/prSummaryVM.js'
		resource url: 'js/viewmodels/poSummaryVM.js'
		resource url: 'js/viewmodels/attachmentsVM.js'
		resource url:'js/viewmodels/downloadAttachmentVM.js'
		resource url: 'js/pages/summary.js'
	}

	searchPage{
		defaultBundle 'search'

		resource url:'js/viewmodels/searchResultsVM.js'
		resource url:'js/viewmodels/solrPaginationVM.js'
		resource url:'js/models/facetValueModel.js'
		resource url:'js/models/facetModel.js'
		resource url:'js/viewmodels/facetFiltersVM.js'
		resource url:'js/models/productModel.js'
		resource url:'js/pages/searchResults.js'
		resource url:'js/pages/omnitureSearch.js'
	}

	emptyResultsSearchPage{
		defaultBundle 'noSearch'

		resource url: 'js/viewmodels/catalogsVM.js'
		resource url: 'js/viewmodels/productCatalogVM.js'
		resource url: 'js/pages/omnitureProductCatalog.js'
	}

	favoriteDetails{
		defaultBundle 'favoriteDetails'
		resource url:'js/models/favoriteProductModel.js'
		dependsOn	'core'

		resource url:'js/viewmodels/favoriteDetailsVM.js'
		resource url:'js/pages/favoriteDetails.js'
	}

	adminAccountMessagePage{
		defaultBundle 'adminAccountMessage'

		dependsOn	'core'

		resource url: 'js/models/ncrfOrgModel.js'
		resource url: "js/models/columnModel.js"
		resource url: "js/models/userDetModel.js"
		resource url: "js/viewmodels/baseSortVM.js"
		resource url: "js/viewmodels/filterVM.js"
		resource url: "js/viewmodels/paginationVM.js"
		resource url: "js/viewmodels/userSearchVM.js"
		resource url: "js/pages/userSearch.js"
		resource url: 'js/viewmodels/baseMessageVM.js'
		resource url: 'js/models/adminAccountMessageDocumentModel.js'
		resource url: 'js/models/adminAccountMessageAudienceModel.js'
		resource url: 'js/models/adminAccountMessagePubOrgsModel.js'
		resource url: 'js/models/adminAccountMessageModel.js'
		resource url: 'js/viewmodels/adminAccountMessageVM.js'
		resource url: 'js/viewmodels/showOrHideSpinnerVM.js'
		resource url: 'js/viewmodels/accountSelectVM.js'
		resource url: 'js/models/adminAccountListMessageModel.js'
		resource url: 'js/viewmodels/adminAccountListMessagesLPVM.js'
		resource url: 'js/pages/adminAccountListMessage.js'
		resource url: 'js/pages/accountSelect.js'
		resource url: 'js/pages/createAccountMessage.js'

	}

	impersonatePage{
		defaultBundle 'impersonatePage'

		dependsOn	'core'
		resource url: "js/models/columnModel.js"
		resource url: "js/models/userDetModel.js"
		resource url: "js/viewmodels/baseSortVM.js"
		resource url: "js/viewmodels/filterVM.js"
		resource url: "js/viewmodels/paginationVM.js"
		resource url: "js/models/impersonateModel.js"
		resource url: "js/viewmodels/impersonateVM.js"
		resource url: "js/pages/impersonate.js"
	}

	punchoutPage {

		defaultBundle 'punchoutPage'

		dependsOn 'punchoutMain'
		resource url: "js/viewmodels/punchoutLoginVM.js"
		resource url: "js/pages/punchoutLogin.js"
		resource url: 'css/stylesheet.css', disposition: 'head'


	}
	punchoutMain{
		defaultBundle 'punchoutMain'
		dependsOn 'maincss, appjs, jqueryext, knockout, jquery_plugins, cookiejs, cookieHandlerjs, utilsjs, mainjs'
	}

	quoteRequestPage{
		defaultBundle 'quoteRequestPage'
		dependsOn	'core'
		resource url: 'js/models/columnModel.js'//, bundle: 'sort'
		resource url: 'js/viewmodels/showOrHideSpinnerVM.js'
		resource url: "js/viewmodels/baseSortVM.js"
		resource url: "js/models/userDetModel.js"
		resource url: "js/viewmodels/filterVM.js"
		resource url: "js/viewmodels/paginationVM.js"
		resource url: "js/viewmodels/quoteRequestStatusVM.js"
		resource url:'js/viewmodels/attachmentsVM.js'
		resource url:'js/viewmodels/quoteRequestVM.js'
		resource url: "js/pages/quoteRequestStatus.js"
		resource url: "js/pages/quoteRequest.js"
	}
	
	 changePurchasingView{
		 defaultBundle 'changePurchasingView'
		 dependsOn 'core'
		 resource url:'js/viewmodels/changePurchasingViewVM.js'
		 resource url:'js/pages/changePurchasingView.js'
		
	 }
	 
	 partnerAgentPage{
		 defaultBundle 'partnerAgentPage'
		 dependsOn 'header'
		 resource url: 'js/models/columnModel.js'
		 resource url:  'js/viewmodels/baseSortVM.js'
		 resource url: 'js/viewmodels/paginationVM.js'
		 resource url: 'js/viewmodels/filterVM.js'
		 resource url:'js/viewmodels/partnerAgentVM.js'
		 resource url: 'js/pages/partnerAgentPage.js'
		
	 }

	unitTest{
	 resource url: 'test/runner.js', disposition: 'defer'
	 }

}