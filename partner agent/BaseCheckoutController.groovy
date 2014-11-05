package com.hp.elite.checkout

import grails.converters.JSON
import org.codehaus.groovy.grails.web.json.JSONObject
import com.hp.elite.controller.ControllerBaseActions
import com.hp.elite.error.BaseErrorHandlingController;
import org.apache.commons.lang.StringEscapeUtils
import groovy.json.JsonSlurper
import java.text.NumberFormat
import org.apache.commons.lang.StringEscapeUtils;

@Mixin(ControllerBaseActions)
class BaseCheckoutController {

    def baseCheckoutService
	def baseDocumentService
	def externalCacheService
	def userRoleService
	
	BaseErrorHandlingController baseErrorHandlingController = new BaseErrorHandlingController()
	
    def updateCheckoutInfo(){
		def result=[:]	
		def udfList =[:]		
		def quoteUUID = params.quoteUUID
		def section = params.section
		def quoteType = params.quoteType
		def checkoutInfo
		if(params.checkoutInfo!=null){
			checkoutInfo = StringEscapeUtils.unescapeHtml(params.checkoutInfo);
		    checkoutInfo = JSON.parse(checkoutInfo)
		}
		log.debug("------------------checkoutInfo:"+checkoutInfo)
		//Handle UDF fields
		if((section == 'AccountInformation' || section == 'BillingInformation') && checkoutInfo?.fields[1]){
			udfList = retrieveUDFields(checkoutInfo.fields[1])
		}		
		if(section != 'LogisticalInformation' && section != 'CarePackInformation'){
			if('NonCatalog'.equalsIgnoreCase(quoteType)){
				calculateNonCatalogTotalQuote(checkoutInfo)
			}
			if(section == 'All')
				checkoutInfo = filterDataForUpdate(checkoutInfo, 'All', udfList)
			else
				checkoutInfo = filterDataForUpdate(checkoutInfo?.fields[0], section, udfList)
		}			
		def checkoutUpdateInfo = baseCheckoutService.updateCheckoutInfo(checkoutInfo)		
		result.put('checkoutInfo', checkoutUpdateInfo)		
		if(params.returnWay && params.returnWay.equalsIgnoreCase('noRender')){
			log.debug("-----------------------ifUpdateQuoteInfoMethodResult:"+(result as JSON))
			result
		}else{
			log.debug("-----------------------elseUpdateQuoteInfoMethodResult:"+(result as JSON))
			render result as JSON
		}
	}

	def retrieveUDFields(udfArray){
		def udfList = [:]
		udfArray.each{
			def tmpVar = it.value
			log.debug("itmpVar--"+tmpVar)
			udfList.put(tmpVar.inputFieldName, tmpVar.values[0]==null?'':tmpVar.values[0])
		}
		//log.debug("udfList ----"+udfList)
		udfList
	}

	def updatePaymentMethod(){
		def result=[:]
		def paymentRequest =[:]
		def selectedOption = params.selectedOption
		def selectedKey = params.selectedKey
		
		//To save all other unsaved fields before calling update Payment Method
		params.returnWay = 'noRender'
		params.section = 'AccountInformation'
		this.updateCheckoutInfo()

		paymentRequest["PaymentInfo"] = selectedKey
		if(selectedKey =='2'){
			paymentRequest["old_PaymentInfo"] = '3'
		}

		def checkoutInfo = baseCheckoutService.updatePaymentMethod(paymentRequest)
		log.debug('updatePaymentMethod result ------------------'+(checkoutInfo as JSON))
		result.put('checkoutInfo', checkoutInfo)
		render result as JSON
	}
	
	
	def docServiceGetAddressList(){
		
		def getAddress= [:]
		
		
		if(!params.BuyerID){
			getAddress['TransientID'] = params.TransientID
			getAddress['Key'] = params.Key
			//getAddress['SearchName'] = params.SearchName
			//getAddress['page'] = params.page?:'1'
			//getAddress['pageSize'] = params.pageSize?:'10'
			
		}else{
			getAddress['TransientID'] = params.TransientID
			getAddress['Key'] = params.Key
			getAddress['pageSize'] = params.pageSize?:'10'
			getAddress['page'] = params.page?:'1'
			getAddress['fromPForm'] = 'true'
			getAddress['BuyerID'] = params.BuyerID
			getAddress['UserID'] = params.UserID
			getAddress['Name'] = params.Name
			getAddress['Dir'] = params.Dir
			getAddress['SearchFor'] = params.SearchFor
			getAddress['SearchName'] = params.SearchName
			
		}
		
		def results=baseCheckoutService.getAddress(getAddress)
		

		render(contentType: 'text/json') {[
			'result': results
		]}
	}
	
	def docSaveAddressInfo(){
		
		def saveAddress= [:]
		saveAddress['TransientID'] = params.TransientID
		saveAddress['Key'] = params.Key
		saveAddress['BuyerID'] = params.BuyerID
		saveAddress['UserID'] = params.UserID
		saveAddress['PAddress'] = params.PAddress
		saveAddress['DAddress'] = params.DAddress
		saveAddress['EliteReturnURL'] = params.EliteReturnURL
		saveAddress['Accept'] = params.Accept
		saveAddress['DefaultAddress'] = params.DefaultAddress
		saveAddress['SelAddress'] = params.SelAddress
		saveAddress['Cancel'] = params.Cancel
		saveAddress['NewAddress'] = params.NewAddress

		def result=baseCheckoutService.saveAddressInfo(saveAddress)
		render(contentType: 'text/json') {[
			'result': result
		]}

	}

	def refreshOrderExemptSelection(){
		def result=[:]
		def quoteUUID = params.quoteUUID
		def selectedOption = params.selectedOption
		
		//To save all other unsaved fields before calling refresh Order ExemptSelection
		params.returnWay = 'noRender'
		params.section = 'ShippingInformation'
		this.updateCheckoutInfo()
		
		def checkoutInfo = baseCheckoutService.refreshOrderExemptSelection(quoteUUID, 'refresh', selectedOption)
		log.debug('refreshOrderExemptSelection result ------------------'+(checkoutInfo as JSON))
		result.put('checkoutInfo', checkoutInfo)
		render result as JSON
	}
	
	def filterDataForUpdate(def quoteInfo, section,udfList){
		def sections = new ArrayList()
		
		if(section.equalsIgnoreCase('All')){
			quoteInfo.each {
				def sectionName = it.key
				def fieldsList = it.value['fields']				
				def filteredFieldsList = filterSectionDataForUpdate(fieldsList[0], sectionName)
				//Handle UDF fields
				if((sectionName == 'AccountInformation' || sectionName == 'BillingInformation') && fieldsList[1]){
					udfList = retrieveUDFields(fieldsList[1])
					filteredFieldsList.putAll(udfList)
				}
				def tmpSection = [:]
				tmpSection.put('name',sectionName)				
				tmpSection.put('fields',filteredFieldsList)
				sections.add(tmpSection)
			}
		}else{
			def tmpSection = [:]
			def tmpField = filterSectionDataForUpdate(quoteInfo, section)
			if((section == 'AccountInformation' || section == 'BillingInformation') && udfList.size()>0){
				tmpField.putAll(udfList)
			}
			tmpSection.put('name',section)
			tmpSection.put('fields',tmpField)
			sections.add(tmpSection)
		}
		section=[:]
		section.put('sections', sections)
		if('BMI'.equalsIgnoreCase(params.quoteType) || 'Watson'.equalsIgnoreCase(params.quoteType) || 'NonCatalog'.equalsIgnoreCase(params.quoteType)){
			section.put('QuoteType', params.quoteType)
		}else if('Catalog'.equalsIgnoreCase(params.quoteType)){
			section.put('QuoteType', params.quoteType)
			section.put('IsRequestForCopyCreateCatalogQuote', '')
		}
		section as JSON
	}
	
	def filterSectionDataForUpdate(fieldsList, section){
		def tmpField = [:]
		fieldsList.each {
			def name = it.key
			def attributes = it.value
			if(attributes.ignoreMe){
				// Do not send this field to services
			}else if(attributes.overridevalue){
				tmpField.put(name, attributes.overridevalue)
			}else if(attributes.selectedvalue){
				tmpField.put(name, attributes.selectedvalue)
			}else{
				tmpField.put(name, attributes.values[0]==null?'':attributes.values[0])
			}
		}
		tmpField
	}

	def modifyLogisticsForElite(documentEditInfo){
		//log.debug("modifyLogisticsForElite:"+documentEditInfo)
		try{
				if(documentEditInfo){
					documentEditInfo.sections.CartSummary.logisticalInformation?.LSUiModel?.PropertyGroup?.each{
						pg->
						//def flag = pg.asBoolean()
						//log.debug("----------------pg is:"+flag+":pg:"+pg)
						if(pg.asBoolean()){
							if(pg.Property.asBoolean()){
								if(!pg.Property.length){
									def tempArray = new ArrayList()
									tempArray.add(pg.Property)
									pg.put('Property',tempArray)

								}
								pg.Property.each{ p->
									if(p.asBoolean()){
										p.put('value',false)
										def pID = p.ID
										def submitID='PREFIX_PROPERTY_ID_'+p.ID
										def submitInput='PREFIX_PROPERTY_ID_'+p.ID+'_INPUT_'
										def triggered='TriggeredPropertyID'
										def triggeredPV= 'TriggeredPropertyValueID'
										def validation=p.CausesValidation
										def checkbox=p.MultiValue
										p.put('submitID',submitID)
										p.put('visibleChildren',0)
										p.put('isVisible',false)
										p.put('isInput',false)
										p.put('checkbox',false)
										p.put('radio',false)
										if(!p.PropertyValues.PropertyValue.length){
											def tempArray = new ArrayList()
											tempArray.add(p.PropertyValues.PropertyValue)
											p.PropertyValues.put('PropertyValue',tempArray)
										}
										p.PropertyValues.PropertyValue.each{pv->
											if(pv instanceof JSONObject){
												pv.put('submitInputID',submitInput+pv.ID)
												pv.put('inputValue','')
												pv.put('isVisible',false)
												pv.put('submitID',submitID)
												pv.put(triggered, pID)
												pv.put(triggeredPV, pv.ID)
												pv.put('validation',validation)
												pv.put('checkbox',checkbox)
											}

										}
									}
								}
							}
						}
					}
				}
			}
			catch(Exception e){	
				log.error("modifyLogisticsForElite:"+e.getMessage())
			}
			//log.debug("-----------------------modifyLogisticsForEliteMethodResult:"+(documentEditInfo as JSON))
			documentEditInfo
	}
	
	def refreshShippingState(){
		def result=[:]
		def shippingState = params.shippingState
		
		//To save all other unsaved fields before calling refresh state
		params.returnWay = 'noRender'
		params.section = 'ShippingInformation'
		this.updateCheckoutInfo()
		
		def checkoutInfo = baseCheckoutService.refreshShippingState('false', shippingState)
		log.debug('refreshShippingState result ------------------'+(checkoutInfo as JSON))
		result.put('checkoutInfo', checkoutInfo)
		render result as JSON
	}
	
	def refreshState(){
		def result=[:]
		
		//To save all other unsaved fields before calling refresh state
		params.returnWay = 'noRender'		
		this.updateCheckoutInfo()
		
		boolean freight = false;
		if(params.freight && params.freight.equalsIgnoreCase("true")){
			freight = true
		}
		
		def checkoutInfo = baseCheckoutService.refreshState(params.section, params.stateAbbreviation, freight)
		//Temp fix for eliminating '~' in FreightCountry.selectedvalue TODO -Remove this code after services are corrected
		if(checkoutInfo.sections && checkoutInfo.sections.ShippingInformation.fields[0].FreightCountry && checkoutInfo.sections.ShippingInformation.fields[0].FreightCountry.selectedvalue){
			if(checkoutInfo.sections.ShippingInformation.fields[0].FreightCountry.selectedvalue.indexOf("~") != -1){
				checkoutInfo.sections.ShippingInformation.fields[0].FreightCountry.selectedvalue = checkoutInfo.sections.ShippingInformation.fields[0].FreightCountry.selectedvalue.split("~")[0]
			}
		}
		//log.debug('refreshState result ------------------'+(checkoutInfo as JSON))
		result.put('checkoutInfo', checkoutInfo)
		render result as JSON
	}
	
	def refreshCountry(){
		def result=[:]
		
		//To save all other unsaved fields before calling refresh country
		params.returnWay = 'noRender'		
		this.updateCheckoutInfo()
		
		boolean freight = false;
		if(params.freight && params.freight.equalsIgnoreCase("true")){
			freight = true
		}
		
		def checkoutInfo = baseCheckoutService.refreshCountry(params.section, params.countryId, freight)
		//Temp fix for eliminating '~' in FreightCountry.selectedvalue TODO -Remove this code after services are corrected
		if(checkoutInfo.sections && checkoutInfo.sections.ShippingInformation.fields[0].FreightCountry && checkoutInfo.sections.ShippingInformation.fields[0].FreightCountry.selectedvalue){
			if(checkoutInfo.sections.ShippingInformation.fields[0].FreightCountry.selectedvalue.indexOf("~") != -1){
				checkoutInfo.sections.ShippingInformation.fields[0].FreightCountry.selectedvalue = checkoutInfo.sections.ShippingInformation.fields[0].FreightCountry.selectedvalue.split("~")[0]
			}
		}
		//log.debug('refreshCountry result ------------------'+(checkoutInfo as JSON))
		result.put('checkoutInfo', checkoutInfo)
		render result as JSON
	}
	
	def refreshBillingType(){
		def result=[:]
		
		//To save all other unsaved fields before calling refresh billing type
		params.returnWay = 'noRender'
		this.updateCheckoutInfo()
		
		def checkoutInfo = baseCheckoutService.refreshBillingType( params.billingType, params.billFromStateCode)
		//log.debug('refreshBillingType result ------------------'+(checkoutInfo as JSON))
		result.put('checkoutInfo', checkoutInfo)
		render result as JSON
	}
	
	def removeDocumentReDirect() {
		def callControllerName = params.callControllerName
		def controllerAction = params.controllerAction
		def approverFlag = params.approverFlag
		def homePageDeleteFlag = params.homePageDeleteFlag
		def quoteUUID = params.quoteUUID
		def docStatus=params.docStatus
		def organizationID = getCurrentOrganizationId()
		def uuidStatusMap=[:]
		uuidStatusMap[quoteUUID]=docStatus					
		def quote = baseDocumentService.removeDocument(uuidStatusMap)
		externalCacheService.hashDelete(params.docType+"List:${organizationID}", null)
		if("true".equalsIgnoreCase(homePageDeleteFlag)){
			redirect(url: "/")
		}else{
			redirect(controller: callControllerName,action: controllerAction)
		}
	}
	
	def removeDocument(){
	
			def documentId = params.documentId
			def docStatus=params.docStatus 
			def uuidStatusMap=[:]
			uuidStatusMap[documentId]=docStatus
			def result = baseDocumentService.removeDocument(uuidStatusMap)
			def organizationID = getCurrentOrganizationId()			
			externalCacheService.hashDelete(params.docType+"List:${organizationID}", null)
		
			render result as JSON	
	}
	
	private void calculateNonCatalogTotalQuote(def checkoutInfo){		
		if(checkoutInfo.CartValues){			
			def tax = 0
			def specialHandling = 0
			def freight = 0
			def subTotal = 0

			NumberFormat nf = NumberFormat.getInstance(Locale.US);// Defaulting it to US as we do not handle regional based currency formats
			try{
				tax = nf.parse(checkoutInfo.CartValues.fields[0].Tax.values[0])
			}catch(Exception e){
				//Ignore this- Expecting services to handle
			}
			try{
				specialHandling = nf.parse(checkoutInfo.CartValues.fields[0].SpecialHandling.values[0])
			}catch(Exception e){
			//Ignore this- Expecting services to handle
			}
			try{
				freight = nf.parse(checkoutInfo.CartValues.fields[0].Freight.values[0])
			}catch(Exception e){
				//Ignore this- Expecting services to handle
			}
			try{
				if(checkoutInfo.CartSummary && checkoutInfo.CartSummary.fields.subTotal)
					subTotal = nf.parse(checkoutInfo.CartSummary.fields.subTotal.values.replaceAll(",", ""))
			}catch(Exception e){
				//Ignore this- Expecting services to handle
			}
			checkoutInfo.CartValues.fields[0].TotalQuote.values[0] = (tax+specialHandling+freight+subTotal).toString()
		}
	}

	def removeFromCartSummary() {		
		def cartData = [:]		
		cartData.put('postalCode', '')
		cartData.put('stateCode', '')
		cartData.put('customTaxFeeSet','')
		cartData.put('LineItemID', params.lineItemID)
		cartData.put('EditCartFromSummary', 'true')		
		def output = baseCheckoutService.removeFromCartSummary(cartData)		
		render output  as JSON
	}
	
	def updateFromCartSummary() {		
		def cartData = [:]		
		cartData.put('LineItemID', params.lineItemID)
		cartData.put('Quantity', params.quantity?.toString())
		cartData.put('postalCode', '')
		cartData.put('stateCode', '')
		cartData.put('customTaxFeeSet','')	
		cartData.put('EditCartFromSummary', 'true')
		def output = baseCheckoutService.updateFromCartSummary(cartData)		
		render output  as JSON
	}
	
	def completeCartSummaryUpdate() {
		def cartData = [:]
		def result =[:]
		def output = [:]
		def docType=params.docType		
		if('Quote'.equalsIgnoreCase(docType)){
			cartData.put('toCreate','quote')
		}
		else if('PR'.equalsIgnoreCase(docType)){
			cartData.put('toCreate','request')
		}
		else if('PO'.equalsIgnoreCase(docType)){
			cartData.put('toCreate','purchaseorder')
		}
		cartData.put('step','consolidate')
		cartData.put('EditCartFromSummary','true')
		
		output = baseCheckoutService.completeCartSummaryUpdate(cartData)	
		result.put('checkoutInfo', output)
		render result  as JSON
	}
	
	def shopMoreItems(){
		session.setAttribute("shopMoreItems", true);	
		redirect(url: "/")		
	}
	
	def shopForAdditionalItems(){
		def output = [:]
		def result =[:]
		def existingDocument = params.existingDocument
		//def cartSummaryEditFlag = params.cartSummaryEditFlag
				
		try{
			if(existingDocument)
				output = baseCheckoutService.shopForAdditionalItems()
		}
		catch(Exception e){
			def stackTrace = e.printStackTrace()?:''
			baseErrorHandlingController.handleException(stackTrace, 
					this.getMetaClass().getTheClass(),'shopForAdditionalItems',false)
		}
		
		if(output != null)
			result.put("shopForAdditionalItems", output)
			
		render result as JSON
	}
	
	def updateCheckoutDocument() {
		def cartData = [:]
		def result =[:]
		def output = [:]
		def docType=params.docType
		final String STRING_ADD = "Add"
		def existingDocument = params.existingDocument
		existingDocument = existingDocument.substring(0, 3)
				
		if('Quote'.equalsIgnoreCase(docType)){
			cartData.put('toCreate','quote')
			if(existingDocument.equalsIgnoreCase(STRING_ADD))
				cartData.put('merge', 'true')
		}
		else if('PR'.equalsIgnoreCase(docType)){
			cartData.put('toCreate','request')
		}
		else if('PO'.equalsIgnoreCase(docType)){
			cartData.put('toCreate','purchaseorder')
			if(existingDocument.equalsIgnoreCase(STRING_ADD))
				cartData.put('merge', 'true')
		}
		cartData.put('step','consolidate')
		cartData.put('EditCartFromSummary','true')
		
		output = baseCheckoutService.completeCartSummaryUpdate(cartData)	
		
		def userRole = userRoleService.getStorefrontUserRole(request).equalsIgnoreCase('Purchaser')?1:0
		
		if(output.sections && output.sections.CartSummary && output.sections.CartSummary.fields && output.sections.CartSummary.fields.showLogisticalServicesMessage && output.sections.CartSummary.fields.showLogisticalServicesMessage.values){
			output.put('isDisplayLogisticsError', true);
		}
		result.put('checkoutInfo', output)
		if(existingDocument.equals(STRING_ADD)){
			result.put('isNewQuote', false)
			result.put('createVersion','true')
			result.put('isDocumentNew', 'false')
			result.put('isNewPO', false)
		}
		else{
			result.put('isNewQuote', true)
			result.put('isDocumentNew', 'true')
			result.put('isNewPR', true)
			result.put('isNewPO', true)		
		}
		if(params.docType == "Quote"){
			withFormat {
					html {render (view: '/checkout/quoteCheckoutEdit', model: [result: result as JSON]) }
			}
		}
		else if(params.docType == "PR"){
			withFormat {
					html {render (view: '/checkout/prCheckoutEdit', model: [result: result as JSON]) }
			}
		}
		else if(params.docType == "PO"){
			withFormat {
					html {render (view: '/checkout/poCheckoutEdit', model: [result: result as JSON]) }
			}
		}
		
		
	}

	def updateCheckoutDocumentFromCartSummary(def data) {
		def cartData = [:]
		def result =[:]
		def output = [:]
		cartData.put('step','consolidate')
		cartData.put('EditCartFromSummary','true')
		def docType = session.documentDocType?.toLowerCase()
		if(data != null && !data.basketCount)
		{
			output = data
		}
		else
		{
			log.debug('---------------------Using backup service call because eprime service is sending back bad data or data is null')
			output = baseCheckoutService.completeCartSummaryUpdate(cartData)
		}
		def userRole = userRoleService.getStorefrontUserRole(request).equalsIgnoreCase('Purchaser')?1:0

		if(output.sections && output.sections.CartSummary && output.sections.CartSummary.fields && output.sections.CartSummary.fields.showLogisticalServicesMessage && output.sections.CartSummary.fields.showLogisticalServicesMessage.values){
			output.put('isDisplayLogisticsError', true);
		}
		result.put('checkoutInfo', output)
		result.put('isNewQuote', true)
		result.put('isDocumentNew', 'true')
		result.put('isNewPR', true)
		result.put('isNewPO', true)
		result.put('isVersion',false)
		result.put('copyFlag',false)
		result.put('editSection','CartSummary')		
		session.documentDocType = null //session no longer needed after this
		if(docType.contains("Quote".toLowerCase())){
			withFormat {
					html {render (view: '/checkout/quoteCheckoutEdit', model: [result: result as JSON]) }
			}
		}
		else if(docType.contains("PR".toLowerCase())){
			withFormat {
					html {render (view: '/checkout/prCheckoutEdit', model: [result: result as JSON]) }
			}
		}
		else if(docType.contains("PO".toLowerCase())){
			withFormat {
					html {render (view: '/checkout/poCheckoutEdit', model: [result: result as JSON]) }
			}
		}		
	}

	def discardCheckout(){
		updateCheckoutDocumentFromCartSummary(null)
	}

}
