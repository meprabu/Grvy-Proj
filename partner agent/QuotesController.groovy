package com.hp.elite.quotes
import grails.converters.JSON
import groovy.json.JsonSlurper
import java.text.NumberFormat
import com.hp.elite.controller.ControllerBaseActions
import com.hp.elite.checkout.BaseCheckoutController
import com.hp.elite.currency.CurrencyFormatter

import org.apache.commons.io.FilenameUtils;
import org.codehaus.groovy.grails.web.json.JSONObject
import org.codehaus.groovy.runtime.EncodingGroovyMethods
import org.springframework.web.multipart.commons.CommonsMultipartFile

@Mixin(ControllerBaseActions)
@Mixin(BaseCheckoutController)
class QuotesController {

   def projectionModifier
	def quoteService
	def pkRetriever
	def grailsApplication
	def userRoleService
	def externalCacheService
	def jsonMockService
	def featureRestrictService

    def retrieveQuotesInfo() {
		def organizationID = getCurrentOrganizationId()
		def userId = getCurrentUserName()
		def hasAccess
		def sortby = params.sortby
		def page = params.page?:'1'
		def pagesize = params.pageSize?:'30'
		def typeFilter = 'Quotes'
		def homePage=params.homePage
		def statusFilter = params.statusFilter?:'MyQuotes'
		def createdIn  = params.createdIn?:'Store'
		def quoteNameOrNumber  = params.quoteNameOrNumber?:''
		def dateFilter = grailsApplication.config.quote.date.filter
		def userRole = userRoleService.getStorefrontUserRole(request)
		def currentRole=new String(userRole)
		def userLanguage=session.locale?:getCurrentLanguage();
		session.setAttribute("userlang",userLanguage)
		session.setAttribute("userrole",currentRole)
		if(userRole.equalsIgnoreCase('Purchaser')){
			userRole = 0
		}else if(userRole.equalsIgnoreCase('Manager')){
			userRole = 1
		}else if(userRole.equalsIgnoreCase('Requester')){
			userRole = 2
		}
		else{
			userRole = 3
		}
		def quotes = quoteService.getQuotes(organizationID, userId, sortby, page, pagesize, dateFilter, typeFilter,statusFilter,createdIn,quoteNameOrNumber,homePage)
		quotes = formatQuotePrice(quotes)

		def show = params.show?:''
		def pk = pkRetriever.pk
		def result=[:]
		result.put('result',projectionModifier.projection(quotes, show, pk))
		result.put('userRole', userRole)

		def filterResult=[:]
		filterResult.put('statusFilter',quotes.statusFilter?:statusFilter)
		filterResult.put('createdIn',quotes.createdIn)
		filterResult.put('quoteNameOrNumber',quotes.quoteNameOrNumber)
		filterResult.put('hasAccess',hasAccess)
		result.put('filterResult',filterResult)

		def paginationResult=[:]
		paginationResult.put('pageSize', quotes.pageSize)
		paginationResult.put('page', quotes.page)
		paginationResult.put('pageCount', quotes.pageCount)
		paginationResult.put('pageStart', quotes.pageStart)
		paginationResult.put('pageEnd', quotes.pageEnd)
		paginationResult.put('elementCount', quotes.elementCount)
		result.put('paginationResult',paginationResult)
		log.debug("-----------------------getQuotesInfoMethodResult:"+(result as JSON))
		withFormat {
			html {render (view: '/landingPages/quotes', model: [result: result as JSON,a:'asa',createdIn:createdIn,quoteNameOrNumber:quoteNameOrNumber]) }
			json {render result as JSON}
		}
   }

	def formatQuotePrice(def result) {
		def curFormat = new CurrencyFormatter().format
		def originalList = result['elements']?:[]
		originalList.each {
				it.totalPrice = curFormat(it.totalPrice?:0)				
				if(it.countryCode == "FR" ){
					it.docCurrency = it.docCurrency +'  '+grailsApplication.config.hp.catalog.fr.defaultTax					
				}	
		}
		result
	}
	
	
	def deleteQuote(){
		def result=[]
		try{
			result=quoteService.deleteQuote(params.id,params.docStatus)
		}catch(Exception e){
		    result['status']='failure';
		}finally{
			render result as JSON
		}
	}


	def retrieveQuoteReviewInfo(){
		
		// checking if this was a redirect request
		if (session.redirectMap?.quoteID && session.redirectMap.quoteRedirectCount < 1 ) {
			session.redirectMap.quoteRedirectCount ++
			log.debug("redirectMap contains")
		
			params.quoteID = session.redirectMap.quoteID
			params.quoteType = session.redirectMap.quoteType
			params.quoteVersionNumber = session.redirectMap.quoteVersionNumber
			
			// the following aren't needed by BMI quotes, but my test data isn't a BMI quote.. so here they are
			// maybe we'll need other fields in the future.
			params.approverFlag = session.redirectMap.approverFlag
			params.docStatus = session.redirectMap.docStatus
			params.quoteUUID = session.redirectMap.quoteUUID
			
			
			// a synthetic field to track whether we've displayed the page, and prevent looping
			params.quoteRedirectCount = session.redirectMap.quoteRedirectCount
			
		} 
		
		def quoteCreateBy = params.quoteCreateBy
		def homePageDeleteFlag = params.homePageDeleteFlag
		def approverFlag = params.approverFlag
		def copyFlag = params.copyFlag
		def isVersion = params.isVersion
		def quoteUUID = params.quoteUUID
		def docStatus = params.docStatus
		def userRole = userRoleService.getStorefrontUserRole(request)
		def loggedInUserId = getCurrentUser().uuid
		log.debug("--------------------${params.quoteType}")
		def quoteType = params.quoteType

		if(quoteType == "undefined" && session.EprocFlag=='true')
			quoteType="Catalog"

		def quoteVersionNumber = params.quoteVersionNumber
		def quoteMap=[:]
		if(userRole.equalsIgnoreCase('Purchaser')){
			userRole = 0
		}else if(userRole.equalsIgnoreCase('Manager')){
			userRole = 1
		}else if(userRole.equalsIgnoreCase('Requester')){
			userRole = 2
		}
		else{
			userRole = 3
		}
		def quoteCheckout
		
		quoteMap.put('approverFlag',approverFlag)
		quoteMap.put('QuoteUUID',quoteUUID)
		quoteMap.put('docStatus',docStatus)

		if('NonCatalog'.equalsIgnoreCase(quoteType)){
			quoteCheckout = quoteService.getQuoteSummaryNonCatalog(quoteUUID, quoteType)
		}else if('BMI'.equalsIgnoreCase(quoteType) || 'Watson'.equalsIgnoreCase(quoteType)){		
			quoteCheckout = quoteService.getQuoteSummaryBMIWatson(params.quoteID, quoteType, quoteVersionNumber)
		}else if(session.EprocFlag=="true" && 'Catalog'.equalsIgnoreCase(quoteType)){
			quoteMap.put('QuoteType',quoteType)
			quoteCheckout = quoteService.getQuoteSummary(quoteMap)
		}
		else{
			quoteCheckout = quoteService.getQuoteSummary(quoteMap)
		}
		
		//Formating for invalid products error
		if(quoteCheckout && quoteCheckout.sections && quoteCheckout.sections.OrderInformation && quoteCheckout.sections.OrderInformation.errros && quoteCheckout.sections.OrderInformation.errros.DocumentErrorMessages && quoteCheckout.sections.OrderInformation.errros.DocumentErrorMessages.length() >0){
			quoteCheckout.sections.OrderInformation.errros.DocumentErrorMessages = quoteCheckout.sections.OrderInformation.errros.DocumentErrorMessages.replace("\n", "<br><br>")
		}
		
		def result=[:]
		result.put('quoteCreateBy', quoteCreateBy)
		result.put('loggedInUserId', loggedInUserId)
		result.put('homePageDeleteFlag',homePageDeleteFlag)
		result.put('userRole',userRole)
		result.put('customerType', getCurrentCustomerType())
		result.put('copyFlag',copyFlag)
		result.put('isVersion',isVersion)
		result.put('approverFlag',approverFlag)
		result.put('docStatus', docStatus)
		result.put('quoteUUID', params.quoteUUID)
		result.put('docCurrency', params.docCurrency)
		result.put('quoteVersionNumber', quoteVersionNumber)
		result.put('quoteId', params.quoteID)
		result.put('quoteRedirectCount', params.quoteRedirectCount)
		result.put('checkoutInfo',quoteCheckout)
		log.debug("-----------------------getQuoteReviewInfoMethodResult:"+(result as JSON))
		
		// redirect to homepage if this was a deep linked URL that didn't work
			
		if ((result.checkoutInfo.Error =~ /technical problem/) && (result.quoteRedirectCount != null)) {
			log.debug("error retrieving quote")
			log.debug("redirecting to homepage")
			flash.quoteRedirectedError = true
			redirect(controller: "Homepage", action: "home")
			}
			
		
		withFormat {
			html {render (view: '/checkout/quoteCheckoutEdit', model: [result: result as JSON]) }
			json {render result as JSON}
		}
	}

	def retrieveQuoteSummaryInfo(){

		def approverFlag = params.approverFlag
		def quoteUUID = params.quoteUUID
		def docStatus = params.docStatus
		def userRole = userRoleService.getStorefrontUserRole(request).equalsIgnoreCase('Purchaser')?1:0
		def quoteVersionNumber = params.quoteVersionNumber
		def quoteType = params.quoteType
		if(quoteType == "undefined" && session.EprocFlag=='true'){
			quoteType="Catalog"
		}
		
		def quotePage = 'quoteCheckoutSummary'
		def quoteMap=[:]	
		quoteMap.put('approverFlag',approverFlag)
		quoteMap.put('QuoteUUID',quoteUUID)
		quoteMap.put('docStatus',docStatus)
		def quoteSummary
		
		if('NonCatalog'.equalsIgnoreCase(quoteType)){
			quoteSummary = quoteService.getQuoteSummaryNonCatalog(quoteUUID, quoteType)
		}else if('BMI'.equalsIgnoreCase(quoteType) || 'Watson'.equalsIgnoreCase(quoteType)){		
			quoteSummary = quoteService.getQuoteSummaryBMIWatson(params.quoteID, quoteType, quoteVersionNumber)
		}else if(session.EprocFlag=="true" && 'Catalog'.equalsIgnoreCase(quoteType)){
			quoteMap.put('QuoteType',quoteType)
			quoteSummary = quoteService.getQuoteSummary(quoteMap)
		}
		else{
			quoteSummary = quoteService.getQuoteSummary(quoteMap)
		}
		
		
		//Formating for invalid products error
		if(quoteSummary.sections.OrderInformation.errros && quoteSummary.sections.OrderInformation.errros.DocumentErrorMessages && quoteSummary.sections.OrderInformation.errros.DocumentErrorMessages.length() >0){
			quoteSummary.sections.OrderInformation.errros.DocumentErrorMessages = quoteSummary.sections.OrderInformation.errros.DocumentErrorMessages.replace("\n", "<br><br>")
		}
		def result=[:]		
		result.put('userRole',userRole)
		result.put('customerType', getCurrentCustomerType())
		result.put('quoteUUID', params.quoteUUID)
		result.put('docCurrency', params.docCurrency)
		result.put('quoteSummaryInfo',quoteSummary)
		log.debug("-----------------------getQuotesSummaryInfoMethodResult:"+(result as JSON))
		withFormat {
			html {render (view: '/checkout/'+quotePage, model: [result: result as JSON]) }
			json {render result as JSON}
		}
	}

	def retrieveQuoteEditInfo(){
		def result=[:]
		def quoteUUID = params.quoteUUID
		def section = params.section
		def refreshLogisticInfo = params.refreshInfo
		def isTopSectionUpdated = params.isTopSectionUpdated

		// If Quotename is changed.. it needs to be updated to ePrime
		if(isTopSectionUpdated && isTopSectionUpdated.equalsIgnoreCase('true')){
			params.returnWay = 'noRender'
			params.section = 'AccountInformation'
			this.updateCheckoutInfo()
		}
		def quoteEditInfo
		if(refreshLogisticInfo)
			quoteEditInfo = quoteService.getQuoteEdit(refreshLogisticInfo)
		else
		 	quoteEditInfo = quoteService.getQuoteEdit(section, quoteUUID)
		if(section == 'LogisticalInformation'){
			quoteEditInfo = this.modifyLogisticsForElite(quoteEditInfo)
		}
		//Temp fix for eliminating '~' in FreightCountry.selectedvalue TODO -Remove this code after services are corrected
		if(quoteEditInfo.sections && quoteEditInfo.sections.ShippingInformation?.fields[0].FreightCountry && quoteEditInfo.sections.ShippingInformation?.fields[0].FreightCountry.selectedvalue){
			if(quoteEditInfo.sections.ShippingInformation.fields[0].FreightCountry.selectedvalue.indexOf("~") != -1){
				quoteEditInfo.sections.ShippingInformation.fields[0].FreightCountry.selectedvalue = quoteEditInfo.sections.ShippingInformation.fields[0].FreightCountry.selectedvalue.split("~")[0]
			}
		}
		result.put('checkoutInfo', quoteEditInfo)
		//log.debug("-----------------------getQuoteEditInfoMethodResult:"+(result as JSON))
		render result as JSON
	}

	def editQuoteCart(){
		def quoteUUID=params.quoteUUID;
	}

	def saveQuoteInfo(){
		def result=[:]
		def pageLocation='quoteCheckoutSummary'
		def quoteInfo
		if( params.quoteInfo)
			quoteInfo=JSON.parse(params.quoteInfo)		
		def quoteType =params.quoteType
		def quoteSaveInfo
		if('NonCatalog'.equalsIgnoreCase(quoteType)){
			//Calling update on a section to invoke validation
			params.returnWay = 'noRender'
			params.section = 'All'
			params.checkoutInfo = params.quoteInfo
			quoteSaveInfo = this.updateCheckoutInfo().checkoutInfo
			if(quoteSaveInfo && !quoteSaveInfo.sectionErrors){
				quoteSaveInfo = quoteService.saveNonCatalogQuoteInfo(params.quoteUUID, quoteType)
			}
		}else if('Catalog'.equalsIgnoreCase(quoteType)){
			def quoteMap=[:]
			params.returnWay = 'noRender'
			params.section='AccountInformation'
			quoteMap.put('QuoteUUID',params.quoteUUID)
			quoteMap.put('QuoteType',quoteType)
			params.checkoutInfo = params.quoteInfo
			quoteSaveInfo = this.updateCheckoutInfo().checkoutInfo
			if(quoteSaveInfo && !quoteSaveInfo.sectionErrors){
				quoteSaveInfo = quoteService.saveCatalogQuoteInfo(quoteMap)
			}
		}
		else{
			def udfList=[:]
			quoteInfo = this.filterDataForUpdate(quoteInfo, 'All', udfList)
			quoteSaveInfo = quoteService.saveQuoteInfo(quoteInfo)
		}
		
		def userRole = userRoleService.getStorefrontUserRole(request)

				if(userRole.equalsIgnoreCase('Purchaser')){
					userRole = 0
				}else if(userRole.equalsIgnoreCase('Manager')){
					userRole = 1
				}else if(userRole.equalsIgnoreCase('Requester')){
					userRole = 2
				}
				else{
					userRole = 3
				}


		if(quoteSaveInfo && quoteSaveInfo.sectionErrors)	{
			result.put('checkoutInfo', quoteSaveInfo)
			result.put('copyFlag',params.copyFlag)
			result.put('isVersion',params.isVersion)
			result.put('approverFlag',params.approverFlag)
			result.put('docStatus', params.docStatus)
			result.put('quoteUUID', params.quoteUUID)
			result.put('docCurrency', params.docCurrency)
			result.put('isNewQuote', params.isNewQuote.equalsIgnoreCase('true')?true:false)
			result.put('userRole',userRole)
			pageLocation='quoteCheckoutEdit'
		}
		else{
			result.put('docCurrency', params.docCurrency)
			result.put('isSaveQuote', true)
			result.put('userRole',userRole)
			result.put('approverFlag',params.approverFlag)
			result.put('quoteSummaryInfo',quoteSaveInfo)
			//Clearing cache of Quote list
			def organizationID = getCurrentOrganizationId()
			externalCacheService.hashDelete("QuoteList:${organizationID}", null)
		}
		log.debug("-----------------------saveQuoteInfoMethodResult:"+(result as JSON))
		withFormat{
			html{render(view: '/checkout/'+pageLocation,model:[result:result as JSON])}
		}
	}
	
	def createNonCatalogQuote(){
		def result=[:]
		def pageLocation='quoteCheckoutSummary'
		
		//Calling update on a section to invoke validation
		params.returnWay = 'noRender'
		params.section = 'All'
		params.quoteType = 'NonCatalog'
		def quoteSaveInfo = this.updateCheckoutInfo().checkoutInfo
		if(quoteSaveInfo && !quoteSaveInfo.sectionErrors){
			quoteSaveInfo = quoteService.createNonCatalogQuote()
		}

		def userRole = userRoleService.getStorefrontUserRole(request)

				if(userRole.equalsIgnoreCase('Purchaser')){
					userRole = 0
				}else if(userRole.equalsIgnoreCase('Manager')){
					userRole = 1
				}else if(userRole.equalsIgnoreCase('Requester')){
					userRole = 2
				}
				else{
					userRole = 3
				}


		if(quoteSaveInfo && quoteSaveInfo.sectionErrors)	{
			result.put('checkoutInfo', quoteSaveInfo)
			result.put('copyFlag',params.copyFlag)
			result.put('isVersion',params.isVersion)
			result.put('approverFlag',params.approverFlag)
			result.put('docStatus', params.docStatus)
			result.put('quoteUUID', params.quoteUUID)
			result.put('docCurrency', params.docCurrency)
			result.put('isNewQuote', params.isNewQuote.equalsIgnoreCase('true')?true:false)
			result.put('userRole',userRole)
			pageLocation='quoteCheckoutEdit'
		}
		else{
			result.put('docCurrency', params.docCurrency)
			result.put('isSaveQuote', true)
			result.put('userRole',userRole)
			result.put('approverFlag',params.approverFlag)
			result.put('quoteSummaryInfo',quoteSaveInfo)
			//Clearing cache of Quote list
			def organizationID = getCurrentOrganizationId()
			externalCacheService.hashDelete("QuoteList:${organizationID}", null)
			//Cart is emptied when ever a new non-catalog quote is saved
			clearSessionVariablesOnEmptyCart()
		}
		log.debug("-----------------------saveQuoteInfoMethodResult:"+(result as JSON))
		withFormat{
			html{render(view: '/checkout/'+pageLocation,model:[result:result as JSON])}
		}
	}


	def saveQuoteCopy(){
		def result=[:]
		def quoteSaveInfo
		def pageLocation = 'quoteCheckoutSummary'
		//Calling update on a section to invoke validation
		params.returnWay = 'noRender'
		params.section = 'AccountInformation'
		def quoteType = params.quoteType
		quoteSaveInfo = this.updateCheckoutInfo().checkoutInfo
		if(quoteSaveInfo && !quoteSaveInfo.sectionErrors){
			if(session.EprocFlag == "true" && "Catalog".equalsIgnoreCase(quoteType))
				quoteSaveInfo = quoteService.createCatalogQuote()
			else{
				quoteSaveInfo = quoteService.finalSaveCreateQuote()
			}
			def organizationID = getCurrentOrganizationId()
			externalCacheService.hashDelete("QuoteList:${organizationID}", null)
			//Cart is emptied when ever a new quote is saved
			clearSessionVariablesOnEmptyCart()
		}

		def userRole = userRoleService.getStorefrontUserRole(request)

				if(userRole.equalsIgnoreCase('Purchaser')){
					userRole = 0
				}else if(userRole.equalsIgnoreCase('Manager')){
					userRole = 1
				}else if(userRole.equalsIgnoreCase('Requester')){
					userRole = 2
				}
				else{
					userRole = 3
				}

		if(quoteSaveInfo && quoteSaveInfo.sectionErrors)	{
			result.put('checkoutInfo', quoteSaveInfo)
			result.put('copyFlag',params.copyFlag)
			result.put('isVersion',params.isVersion)
			result.put('approverFlag',params.approverFlag)
			result.put('docStatus', params.docStatus)
			result.put('quoteUUID', params.quoteUUID)
			result.put('isNewQuote', params.isNewQuote.equalsIgnoreCase('true')?true:false)
			result.put('userRole',userRole)
			result.put('docCurrency', params.docCurrency)
			pageLocation='quoteCheckoutEdit'
		}
		else{
			result.put('approverFlag',params.approverFlag)
			result.put('isSaveQuote', true)
			result.put('quoteSummaryInfo',quoteSaveInfo)
			result.put('docCurrency', params.docCurrency)
			result.put('userRole',userRole)
		}

		log.debug("-----------------------saveQuoteCopyMethodResult:"+(result as JSON))
		withFormat{
			html{render(view: '/checkout/'+pageLocation,model:[result:result as JSON])}
		}
	}

	def createQuote(){
		def result=[:]
		def createQuoteRequest= [:]
		//organizationUUID as value for BuyerID parameter is needed to create catalogQuotes for B2bi
		def buyerID= session.currentOrganization?.uUID
		createQuoteRequest["toCreate"] = "quote"
		createQuoteRequest["step"] = "create"
		
		if(params.QuoteType){
			createQuoteRequest['QuoteType']=params.QuoteType?:session.documentQuoteType
			createQuoteRequest['BuyerID']= buyerID
		}
		if(session.partnerAgent.equals('true')){
			def partnerAgentInfluencerID = session.influencerID.toString()
			createQuoteRequest['influencerID'] = partnerAgentInfluencerID
		}
		def createQuoteInfo = quoteService.createQuote(createQuoteRequest)		
		result.put('customerType', getCurrentCustomerType())		
		def userRole = userRoleService.getStorefrontUserRole(request).equalsIgnoreCase('Purchaser')?1:0
		//TODO- Need approverFlag and quoteUUID
		//result.put('approverFlag', params.approverFlag)
		//result.put('quoteUUID', params.quoteUUID)
		result.put('isNewQuote', true)
		result.put('userRole', userRole)
		result.put('checkoutInfo', createQuoteInfo)
		result.put('isDocumentNew', 'true')
		session.documentDocType='Quote'
		log.debug("createQuoteInfo: ------ "+(createQuoteInfo as JSON))

		withFormat {
			html {render (view: '/checkout/quoteCheckoutEdit', model: [result: result as JSON]) }
			json {render result as JSON}
		}
	}
		
	def copyQuote(){

		def approverFlag = params.approverFlag
		def copyFlag = params.copyFlag
		def isVersion = params.isVersion
		def quoteUUID = params.quoteUUID
		def docStatus = params.docStatus
		def quoteType=params.quoteType
		if(quoteType == "undefined" && session.EprocFlag=='true')
		quoteType="Catalog"
		def userRole = userRoleService.getStorefrontUserRole(request).equalsIgnoreCase('Purchaser')?1:0
		def quoteCheckout = quoteService.copyQuote(quoteUUID,quoteType)
		
		// AGM 5141 - because we do NOT include attachment bodies as part of the copy on the backend, we need to omit their names from the displayed summary
		// TODO revisit the implementation, either optionally copy the attachment bodies, or omit their names by design
		
		try {
			
			if(quoteCheckout && quoteCheckout.sections?.AccountInformation?.fields[0]?.Attachments) {
				quoteCheckout.sections.AccountInformation.fields[0].Attachments.isNull = true;
				quoteCheckout.sections.AccountInformation.fields[0].Attachments.values = [];
				log.info('AGM5141 quoteCopy - removing display names for non-copied attachments');
			}
			
		} catch (Exception q) {
			log.error('error removing display names of non-copied attachments from quote summary ' + q.getMessage());
		}
		
		
		//Formating for invalid products error
		log.debug("***************************"+quoteCheckout.sections);
		if(quoteCheckout && quoteCheckout.sections && quoteCheckout.sections.OrderInformation && quoteCheckout.sections.OrderInformation.errros && quoteCheckout.sections.OrderInformation.errros.DocumentErrorMessages && quoteCheckout.sections.OrderInformation.errros.DocumentErrorMessages.length() >0){
			quoteCheckout.sections.OrderInformation.errros.DocumentErrorMessages = quoteCheckout.sections.OrderInformation.errros.DocumentErrorMessages.replace("\n", "<br><br>")
		}
		//Cart is emptied when ever a quote is copied
		clearSessionVariablesOnEmptyCart()
		
		def result=[:]
		result.put('userRole',userRole)
		result.put('customerType', getCurrentCustomerType())
		result.put('copyFlag',copyFlag)
		result.put('isVersion',isVersion)
		result.put('approverFlag',approverFlag)
		result.put('docStatus', docStatus)
		result.put('quoteUUID', params.quoteUUID)
		result.put('quoteType', quoteType)
		result.put('checkoutInfo',quoteCheckout)
		result.put('docCurrency', params.docCurrency)
		result.put('isDocumentNew', 'true')
		log.debug("-----------------------copyQuoteMethodResult:"+(result as JSON))
		withFormat {
			html {render (view: '/checkout/quoteCheckoutEdit', model: [result: result as JSON]) }
			json {render result as JSON}
		}
	}
	
	def reviseQuote(){
		
				def quoteUUID = params.quoteUUID
				def createVersion=params.createVersion
				def userRole = userRoleService.getStorefrontUserRole(request).equalsIgnoreCase('Purchaser')?1:0
				def quoteCheckout = quoteService.reviseQuote(quoteUUID,createVersion)

				try {					
					if(quoteCheckout && quoteCheckout.sections?.AccountInformation?.fields[0]?.Attachments) {
						quoteCheckout.sections.AccountInformation.fields[0].Attachments.isNull = true;
						quoteCheckout.sections.AccountInformation.fields[0].Attachments.values = [];
					}					
				} catch (Exception q) {
					log.error('error removing display names of non-copied attachments from quote summary ' + q.getMessage());
				}
				if(quoteCheckout && quoteCheckout.sections && quoteCheckout.sections.OrderInformation && quoteCheckout.sections.OrderInformation.errros && quoteCheckout.sections.OrderInformation.errros.DocumentErrorMessages && quoteCheckout.sections.OrderInformation.errros.DocumentErrorMessages.length() >0){
					quoteCheckout.sections.OrderInformation.errros.DocumentErrorMessages = quoteCheckout.sections.OrderInformation.errros.DocumentErrorMessages.replace("\n", "<br><br>")
				}
				//Cart is emptied when ever a quote is copied
				clearSessionVariablesOnEmptyCart()
				
				def result=[:]
				result.put('userRole',userRole)
				result.put('customerType', getCurrentCustomerType())
				result.put('createVersion',createVersion)
				result.put('quoteUUID', params.quoteUUID)
				result.put('checkoutInfo',quoteCheckout)				
				log.debug("-----------------------reviseQuoteMethodResult:"+(result as JSON))
				withFormat {
					html {render (view: '/checkout/quoteCheckoutEdit', model: [result: result as JSON]) }
					json {render result as JSON}
				}
	}
		
	def returnToQuote(){
		def approverFlag
		def quoteUUID
		def docStatus
		def returnData
		def copyFlag
		def isNewQuote
		def isVersion
		def token = ""
		def token2 = ""
		def section
		def docCurrency
		def quoteType
		def createVersion
		def shipToAddressChanged = params.ShipToAddressChanged
		StringTokenizer stringTokenizer = null
		StringTokenizer stringTokenizer2 = null
		def quoteCheckout
		def editSection
		log.debug("request from Elite:"+params.EliteRequest)
		if(params.EliteRequest){
			returnData = params.EliteRequest
		}

		if(returnData != null){
			returnData = returnData.substring(1, returnData.length()-1)
			stringTokenizer = new StringTokenizer(returnData,",")
		}

		Map<String, String> map = new HashMap<String, String>()

		if(stringTokenizer != null){
			while(stringTokenizer.hasMoreTokens()){
				token = stringTokenizer.nextToken()
				stringTokenizer2 = new StringTokenizer(token,"=")
				while(stringTokenizer2.hasMoreTokens()){
					map.put(stringTokenizer2.nextToken().trim(), stringTokenizer2.hasMoreTokens()?stringTokenizer2.nextToken().trim():'')
				}
			}
		}

		if(map.size()>0){
			approverFlag = map.get("approverFlag")
			quoteUUID = map.get("QuoteUUID")
			docStatus = map.get("docStatus")
			copyFlag = map.get('copyflag')
			isVersion = map.get("isVersion")
			isNewQuote = map.get("isNewQuote")
			section = map.get("section")
			editSection = map.get("editSection")
			docCurrency = map.get("docCurrency")
			quoteType = map.get("quoteType")
			createVersion=map.get("createVersion")
		}

		def userRole = userRoleService.getStorefrontUserRole(request).equalsIgnoreCase('Purchaser')?1:0
		def quoteRequest = [:]		
		if(quoteType){
			quoteRequest["QuoteType"] = quoteType
		}
		if(quoteUUID){
			quoteRequest['TransientID'] = quoteUUID
		}		
		if(shipToAddressChanged && shipToAddressChanged.equalsIgnoreCase('true') && section && section.equalsIgnoreCase("ShippingInformation")){
			quoteRequest["section"] = 'LogisticalInformation'
			quoteRequest = quoteRequest as JSON
			quoteCheckout = quoteService.getQuoteEdit(quoteRequest)
			quoteCheckout = modifyLogisticsForElite(quoteCheckout)
		}else if(editSection && editSection.length()>0){
			quoteRequest["section"] = editSection
			quoteRequest = quoteRequest as JSON
			quoteCheckout = quoteService.getQuoteEdit(quoteRequest)
		}else{
			quoteRequest["section"] = 'None'	
			quoteRequest = quoteRequest as JSON
			quoteCheckout = quoteService.getQuoteEdit(quoteRequest)
		}
		def result=[:]
		result.put('userRole',userRole)
		result.put('customerType', getCurrentCustomerType())
		result.put('isVersion',isVersion)
		result.put('copyFlag',copyFlag)
		result.put('approverFlag',approverFlag)
		result.put('docStatus', docStatus)
		result.put('quoteUUID',quoteUUID)
		result.put('section',section)
		result.put('editSection',editSection)
		result.put('docCurrency', docCurrency)
		result.put('quoteType', quoteType)
		result.put('createVersion', createVersion)
		if(isNewQuote){
			result.put('isNewQuote',isNewQuote.equalsIgnoreCase('true')?true:false)
		}
		if(shipToAddressChanged){
			result.put('shipToAddressChanged',shipToAddressChanged.equalsIgnoreCase('true')?true:false)
		}
		log.debug("returnToQuote quotecheckout:"+(quoteCheckout as JSON))
		result.put('checkoutInfo',quoteCheckout)
		withFormat {
			html {render (view: '/checkout/quoteCheckoutEdit', model: [result: result as JSON]) }
			json {render result as JSON}
		}

	}

	def createPRFromQuote(){
		def approverFlag = params.approverFlag
		def copyFlag = params.copyFlag
		def isVersion = params.isVersion
		def quoteUUID = params.quoteUUID
		def docStatus = params.docStatus
		def quoteType = params.quoteType
		def quoteVersionNumber = params.quoteVersionNumber
		def userRole = userRoleService.getStorefrontUserRole(request).equalsIgnoreCase('Purchaser')?1:0
		def prCheckoutInfo
		if('BMI'.equalsIgnoreCase(quoteType) || 'Watson'.equalsIgnoreCase(quoteType)){
			def createPRRequest= [:]
			if(quoteType)		
				createPRRequest["QuoteType"] = quoteType
			if( params.quoteID)	
				createPRRequest["QuoteID"] = params.quoteID
			if(quoteVersionNumber)
				createPRRequest["QuoteVersion"] = quoteVersionNumber
			prCheckoutInfo = quoteService.createPRFromBMIWatsonQuote(createPRRequest)
		}else{
			prCheckoutInfo = quoteService.createPRFromQuote(quoteUUID)
		}		
		
		def pageLocation
		def result=[:]
		result.put('userRole',userRole)
		result.put('customerType', getCurrentCustomerType())
		result.put('isNewPR',true)
		//result.put('approverFlag',approverFlag)
		result.put('docStatus', docStatus)
		result.put('quoteUUID', params.quoteUUID)
		result.put('isDocumentNew', 'true')
		result.put('docCurrency', params.docCurrency)
		//log.debug("quotecheckout:"+quoteCheckout.copyFlag)
		if(prCheckoutInfo!=null && prCheckoutInfo.documentLevelError=='true'){
			//Formating for invalid products error
			if(prCheckoutInfo.sections.OrderInformation.errros && prCheckoutInfo.sections.OrderInformation.errros.DocumentErrorMessages && prCheckoutInfo.sections.OrderInformation.errros.DocumentErrorMessages.length() >0){
				prCheckoutInfo.sections.OrderInformation.errros.DocumentErrorMessages = prCheckoutInfo.sections.OrderInformation.errros.DocumentErrorMessages.replace("\n", "<br><br>")
			}
			result.put('checkoutInfo',prCheckoutInfo)
			pageLocation = 'quoteCheckoutEdit'
		}
		else{
			result.put('checkoutInfo',prCheckoutInfo)
			pageLocation = 'prCheckoutEdit'
		}
		log.debug("-----------------------createPRFromQuoteMethodResult:"+(result as JSON))
		withFormat {
			html {render (view: '/checkout/'+pageLocation, model: [result: result as JSON]) }
			json {render result as JSON}
		}

	}

	def createPOFromQuote(){
		
		String restrictionsError = ''
		boolean isImpersonated = false;
		isImpersonated = checkImpersonationRestrictions()
		
		if (isImpersonated) {
			String adminID = session.IMPERSONATEUSERDATAMAP.userID
			boolean exempt = featureRestrictService.isExempt('poSubmit', adminID);
			
			if (exempt == false) {
				restrictionsError = "Submit PO button disabled for impersonated session"
			}
		
		}
		
		def approverFlag = params.approverFlag
		def copyFlag = params.copyFlag
		def isVersion = params.isVersion
		def quoteUUID = params.quoteUUID
		def docStatus = params.docStatus
		def quoteType = params.quoteType
		def quoteVersionNumber = params.quoteVersionNumber
		def userRole = userRoleService.getStorefrontUserRole(request).equalsIgnoreCase('Purchaser')?1:0
		def poCheckoutInfo
		if('BMI'.equalsIgnoreCase(quoteType) || 'Watson'.equalsIgnoreCase(quoteType)){
			def createPORequest= [:]
			if(quoteType)
				createPORequest["QuoteType"] = quoteType
			if( params.quoteID)
				createPORequest["QuoteID"] = params.quoteID
			if(quoteVersionNumber)
				createPORequest["QuoteVersion"] = quoteVersionNumber
			poCheckoutInfo = quoteService.createPOFromBMIWatsonQuote(createPORequest)
		}else{
			poCheckoutInfo = quoteService.createPOFromQuote(quoteUUID)
		}
		def pageLocation
		def result=[:]
		result.put('userRole',userRole)
		result.put('customerType', getCurrentCustomerType())
		result.put('isNewPO',true)
		result.put('approverFlag',approverFlag)
		result.put('docStatus', docStatus)
		result.put('quoteUUID', params.quoteUUID)
		result.put('isDocumentNew', 'true')
		result.put('isFromQuote', true)
		result.put('docCurrency', params.docCurrency)
		result.put('restrictionsError', restrictionsError);
		//log.debug("quotecheckout:"+quoteCheckout.copyFlag)
		if(poCheckoutInfo.documentLevelError=='true'){
			//Formating for invalid products error
			if(poCheckoutInfo.sections.OrderInformation.errros && poCheckoutInfo.sections.OrderInformation.errros.DocumentErrorMessages && poCheckoutInfo.sections.OrderInformation.errros.DocumentErrorMessages.length() >0){
				poCheckoutInfo.sections.OrderInformation.errros.DocumentErrorMessages = poCheckoutInfo.sections.OrderInformation.errros.DocumentErrorMessages.replace("\n", "<br><br>")
			}
			result.put('checkoutInfo',poCheckoutInfo)
			pageLocation = 'quoteCheckoutEdit'
		}
		else{
			result.put('checkoutInfo',poCheckoutInfo)
			pageLocation = 'poCheckoutEdit'
		}
		result.put('checkoutInfo',poCheckoutInfo)
		log.debug("-----------------------createPOFromQuoteMethodResult:"+(result as JSON))
		withFormat {
			html {render (view: '/checkout/'+pageLocation, model: [result: result as JSON]) }
			json {render result as JSON}
		}

	}

	def checkOrderForEmpoweredRequester(){
		def quoteUUID=params.quoteUUID
		def quoteType=params.quoteType
		if('BMI'.equalsIgnoreCase(quoteType) || 'Watson'.equalsIgnoreCase(quoteType)){
			def userRole = userRoleService.getStorefrontUserRole(request)
			if(userRole.equalsIgnoreCase('Purchaser')){
				createPOFromQuote()
			}else if(userRole.equalsIgnoreCase('Manager') || userRole.equalsIgnoreCase('Requester')){
				createPRFromQuote()
			}
		}else{
			def empoweredRequestor = quoteService.checkOrderForDocumentCreation(quoteUUID)
			if(empoweredRequestor && empoweredRequestor.OrderCapable=='true')
				createPOFromQuote()
			else
				createPRFromQuote()
		}
	}	
	
	def attachmentDownload(){
		def fileName = URLDecoder.decode(params.fileName,"UTF-8")
		/*
		 * IE8 Image file having extension in lower case not get download hence change to Upper case
		 */
		 
		def reqInfo = request.getHeader("user-agent").toUpperCase();
		log.debug("user agent info---"+reqInfo)
		def ext = FilenameUtils.getExtension(fileName)
		def name  = FilenameUtils.removeExtension(fileName)
		if(reqInfo.contains("MSIE 8") && ext.matches("jpg|jpeg|png|gif/i")){
			log.debug("request is from IE8")
			fileName = name+"."+ext.toUpperCase()
			params.fileName = fileName
	}
		
		def res = quoteService.getAttachmentStreamContent(params)
		
		log.debug("resunt info attachments -- "+res['mimeType']+" ext "+res['ext'])
		def encodeFile  = URLEncoder.encode(fileName,"UTF-8")
		response.setHeader("Content-disposition", "attachment; filename=${fileName}")
		response.contentType =  res['mimeType']
		response.outputStream << res['decodedBytes']
		response.outputStream.flush()

	}
	
	def requestQuote() {
		session.setAttribute("newQuoteRequestCreated", false)
		
		def manufactureNames=session.currentOrganization.manufacturerNames
		if(manufactureNames.size()<1)
		{
		   return  redirect (controller:'homepage',action:'home')
		}
		else
		{
		 render (view:'/landingPages/quoteRequest')
		}
	}
	
	def showQuoteRequests() {
		//def orgID1= session.currentOrganization?.organizationId?:''
		def orgID= session.currentOrganization?.manufacturerNames[0].organizationId
		def buyerID=sec.loggedInUserInfo(field:'uuid');
		def sortby=params.sortby?params.sortby:'creationdate.desc'
		def page= params.page?params.page:'1'
		def pagesize=params.pageSize?params.pageSize:'20'
		def userLanguage=session.locale?:getCurrentLanguage();
		session.setAttribute("userlang",userLanguage)
		def userRole = userRoleService.getStorefrontUserRole(request)
		session.setAttribute("userrole",userRole)
		def result = quoteService.getAllQuoteRequests(orgID,buyerID,sortby,page,pagesize);
 		render (view:'/landingPages/quoteRequestsStatus',model:[quotesList:result,orgID:orgID,buyerID:buyerID])
	}
	
	def retrieveAjaxQuoteRequests(){
		def orgID= session.currentOrganization?.manufacturerNames[0].organizationId
		def buyerID=sec.loggedInUserInfo(field:'uuid');
		def sortby=params.sortby?params.sortby:'creationdate.desc'
		def page= params.page?params.page:'1'
		def pagesize=params.pageSize?params.pageSize:'20'
		def result = quoteService.getAllQuoteRequests(orgID,buyerID,sortby,page,pagesize);
		render result
	}
	
	def downloadPDF() {
		Map json = [:]
		byte[] b64bytes = ''
		int mockPDF = 0
		if (params.QuoteUUID == 'mock' || mockPDF == 1) {
			String filename = 'web-app/json/QuoteDownloadPDF_ngeo_pur1.json'
			json = jsonMockService.loadFile(filename)
			response.contentType = json.documentType
			response.setHeader("Content-disposition", "attachment; filename=$json.fileName")
			b64bytes = returnB64decodedBytes(json.fileAttachment)
			response.outputStream.write(b64bytes as byte[])
			
		} else {
		
			String QuoteUUID = params.QuoteUUID
			json = quoteService.downloadPDF(QuoteUUID)
			
			if (json != null && json.fileAttachment != null) {
				response.contentType = json.documentType
				response.setHeader("Content-disposition", "attachment; filename=$json.fileName")
				b64bytes = returnB64decodedBytes(json.fileAttachment)
				response.outputStream.write(b64bytes as byte[])
				response.outputStream.flush()
				
			} else {
				json = ['error':'file not found']
				render json as JSON
			}
		
		}
	}
	
	def createNewQuoteRequest()
	{
		def productType = params.productType
		def fname = params.firstName
		def lname = params.lastName
		def phone = params.phoneNo
		def email = params.email
		def details = params.details
		def quoteRef = params.priorQuote
		def fileIndexList = params.fileIndexList
		def sortby=params.sortby?params.sortby:'creationdate.desc'
		def page= params.page?params.page:'1'
		def pagesize=params.pageSize?params.pageSize:'20'
		String fileNames = params.filesList
		//def orgID= session.currentOrganization?.organizationId?:''
		def orgID= session.currentOrganization?.manufacturerNames[0].organizationId
		//def buyerID = getCurrentUserName()?:''
		def buyerID=sec.loggedInUserInfo(field:'uuid');
		String[] filesList = fileNames.length() > 0 ?fileNames.split(',') : new String[0];
		String[] fileIndex = fileIndexList.length() > 0 ? fileIndexList.split(',') : new String[0];
		def fileContentsList = []
		if(session.getAttribute("newQuoteRequestCreated"))
		{
			def result = quoteService.getAllQuoteRequests(orgID,buyerID,sortby,page,pagesize);
			render (view:'/landingPages/quoteRequestsStatus',model:[quotesList:result,orgID:orgID,buyerID:buyerID,showModal:true,emailId:email])
			return;
		}
		for(int i=0;i<fileIndex.length;i++)
		{
			def fileContentsMap = [:]
			def file = request.getFile("file_"+fileIndex[i])
			def fileContents = file?.getBytes();
			def encoded=retrieveBase64EncodedString(fileContents);
			fileContentsMap[filesList[i]] = encoded?.toString();
			fileContentsList.push(fileContentsMap)
		}
		log.debug("In createNewQuoteRequest before service call -- orgId="+orgID+" buyerID="+buyerID);
		def result = quoteService.createNewQuote(productType,fname,lname,phone,email,quoteRef,details,orgID,buyerID,fileContentsList);
		session.setAttribute("newQuoteRequestCreated", true);
		log.debug("In createNewQuoteRequest after service call result = "+ (result as JSON));
		//redirect (controller:"homepage",action:"home")
		render (view:'/landingPages/quoteRequestsStatus',model:[quotesList:result,orgID:orgID,buyerID:buyerID,showModal:true,emailId:email])
	}
	
		
	def deleteQuoteRequests()
	{
		def orgID= session.currentOrganization?.manufacturerNames[0].organizationId
		def buyerID=sec.loggedInUserInfo(field:'uuid');
		def uuidList = "[]"
		uuidList=new JsonSlurper().parseText(params.uuidList);
		def result = quoteService.deleteQuotes(orgID,buyerID,uuidList);
		render result
	}
	
	def validateFileSize()
	{
		String fileNames = params.filesList
		String[] filesList = fileNames.length() > 0 ?fileNames.split(',') : new String[0];
		def result=[:]
		for(int i=0;i<filesList.length;i++)
		{
			try{
			CommonsMultipartFile file = request.getFile("file_"+i);
			assert file.size <= 1048576
			}
			catch(Error ex)
			{
				result.status = "error";
				result.filename = filesList[i];
				render (text: result as JSON,contentType:"text/html");
				return
			}
		}
		result.status = "success"
		 render (text: result as JSON,contentType:"text/html");
	}
	
	private def retrieveBase64EncodedString(def bytes)
	{
	   EncodingGroovyMethods encoder = new EncodingGroovyMethods() ;
	   def encodeddata = (encoder.encodeBase64(bytes)).toString();
	   return encodeddata
	}
	
	private byte[] returnB64decodedBytes(def b64str) {
		EncodingGroovyMethods decoder = new EncodingGroovyMethods();
		byte[] b64bytes = '';
		b64bytes = decoder.decodeBase64(b64str);
		return b64bytes;
	}
	
	private def fixQuoteVersionNumber(def quoteVersionNumber){
		if(quoteVersionNumber && quoteVersionNumber.length() ==1){
			quoteVersionNumber ='0'+quoteVersionNumber
		}
		quoteVersionNumber
	}
	
	private def clearSessionVariablesOnEmptyCart(){
		session.removeAttribute("nonCatalogType")
		session.removeAttribute("productTypeInCart")
		session.removeAttribute("productCountInCart")
		session.setAttribute('cartFFSID','')
	}
	
	boolean checkImpersonationRestrictions() {
		if (session.userstate =~ /IMPERSONATE/ ) {
			return true
		} else {
			return false;
		}
	}
}
