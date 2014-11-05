package com.hp.elite.checkout
import grails.converters.JSON

class BaseCheckoutService {

	boolean transactional = false
	def jsonDataService
	def grailsApplication

	def updateCheckoutInfo(def checkoutInfo){
        def data=checkoutInfo.toString()
        def path = grailsApplication.config.hp.page.secure.service.pathwithoutelitestart
        def url= grailsApplication.config.hp.account.server.url
        def result=jsonDataService.callRemoteSecureServiceWithoutEliteStart('DocumentService-UpdateDocumentSection',data,url,path)
        result
    }

    def updatePaymentMethod(def paymentRequest){
        def data = (paymentRequest as JSON).toString()
        def path = grailsApplication.config.hp.page.secure.service.pathwithoutelitestart
        def url= grailsApplication.config.hp.account.server.url
        def result=jsonDataService.callRemoteSecureServiceWithoutEliteStart('DocumentService-SelectPaymentMethod',data,url,path)
        result
    }
	
	def getAddress(def getAddress){
		def data=getAddress
		def path = grailsApplication.config.hp.page.secure.service.pathwithoutelitestart
		def url= grailsApplication.config.hp.account.server.url
		def result=jsonDataService.callRemoteSecureServiceWithoutEliteStart('DocumentService-GetAddressList',data,url,path)
		result
	}
	
	def saveAddressInfo(def saveAddress){
		def data=saveAddress
		def path = grailsApplication.config.hp.page.secure.service.pathwithoutelitestart
		def url= grailsApplication.config.hp.account.server.url
		def result=jsonDataService.callRemoteSecureServiceWithoutEliteStart('AddressBookManagement-EditAddressFromElite',data,url,path)
		result
	}

    def refreshOrderExemptSelection(def quoteUUID, def refresh, def orderExemptSelection){
        def data=(['QuoteUUID':quoteUUID,'refresh':refresh,'OrderExemptSelection':orderExemptSelection]as JSON).toString()
        def path = grailsApplication.config.hp.page.secure.service.pathwithoutelitestart
        def url= grailsApplication.config.hp.account.server.url
        def result=jsonDataService.callRemoteSecureServiceWithoutEliteStart('DocumentService-SalesTaxInfoUpdateSection',data,url,path)
        result
    }

	def refreshShippingState(def refresh, def shippingState){
		def data=(['refresh':refresh,'ShippingState':shippingState]as JSON).toString()
		def path = grailsApplication.config.hp.page.secure.service.pathwithoutelitestart
		def url= grailsApplication.config.hp.account.server.url
		def result=jsonDataService.callRemoteSecureServiceWithoutEliteStart('DocumentService-StateRefresh',data,url,path)
		result
	}

    def refreshState(def section, def stateAbbreviation, boolean freight){
        def data
        def ePrimeService = 'DocumentService-StateRefresh'
        if(section == 'ShippingInformation'){
            if(freight){
                data=(['FreightStateAbbreviation':stateAbbreviation]as JSON).toString()
            }else{
                data=(['ShippingStateAbbreviation':stateAbbreviation]as JSON).toString()
            }           
        }else if(section == 'BillingInformation'){
            data=(['BillingStateAbbreviation':stateAbbreviation]as JSON).toString()
            ePrimeService='DocumentService-BillingStateRefresh'
        }else if(section == 'PayerInformation'){
            data=(['PayerStateAbbreviation':stateAbbreviation]as JSON).toString()
        }
        def path = grailsApplication.config.hp.page.secure.service.pathwithoutelitestart
        def url= grailsApplication.config.hp.account.server.url
        def result=jsonDataService.callRemoteSecureServiceWithoutEliteStart(ePrimeService,data,url,path)
        result
    }
    
    def refreshCountry(def section, def countryId, boolean freight){
        def data
        def ePrimeService = 'DocumentService-CountryRefresh'
        if(section == 'ShippingInformation'){
            if(freight){
                data =(['FreightCountry':countryId]as JSON).toString()
            }else{
                data =(['ShippingCountry':countryId]as JSON).toString()
            }
        }else if(section == 'BillingInformation'){
            data =(['BillingCountry':countryId]as JSON).toString()
            ePrimeService='DocumentService-BillingCountryRefresh'
        }else if(section == 'PayerInformation'){
            data =(['PayerCountry':countryId]as JSON).toString()
        }
        def path = grailsApplication.config.hp.page.secure.service.pathwithoutelitestart
        def url= grailsApplication.config.hp.account.server.url
        def result=jsonDataService.callRemoteSecureServiceWithoutEliteStart(ePrimeService,data,url,path)
        result
    }
    
    def refreshBillingType(def billingType, def billFromStateCode){
        def data
        if(billFromStateCode){
            data =(['BillingType':billingType,'BillFromStateCode':billFromStateCode]as JSON).toString()
        }else{
            data =(['BillingType':billingType]as JSON).toString()
        }   
        def path = grailsApplication.config.hp.page.secure.service.pathwithoutelitestart
        def url= grailsApplication.config.hp.account.server.url
        def result=jsonDataService.callRemoteSecureServiceWithoutEliteStart('DocumentService-ChangeBillingType',data,url,path)
        result
    }
	
	def removeFromCartSummary(data) {		
		def url = grailsApplication.config.hp.tx.app.server.url
		def path = grailsApplication.config.hp.page.secure.service.pathwithoutelitestart
		data = (data as JSON).toString()		
		jsonDataService.callRemoteSecureServiceWithoutEliteStart('ViewCart-Remove', data, url, path)
	}
	
	def updateFromCartSummary(data) {		
		def url = grailsApplication.config.hp.tx.app.server.url
		def path = grailsApplication.config.hp.page.secure.service.pathwithoutelitestart
		data = (data as JSON).toString()
		jsonDataService.callRemoteSecureServiceWithoutEliteStart('ViewCart-Update', data, url, path)
	}
	
	def completeCartSummaryUpdate(data) {
		def url = grailsApplication.config.hp.tx.app.server.url
		def path = grailsApplication.config.hp.page.secure.service.pathwithoutelitestart
		data = (data as JSON).toString()
		jsonDataService.callRemoteSecureServiceWithoutEliteStart('DocumentService-CreateDocument', data, url, path)
	}

	def shopForAdditionalItems() {
		def url = grailsApplication.config.hp.tx.app.server.url
		def path = grailsApplication.config.hp.page.secure.service.pathwithoutelitestart
		def result = jsonDataService.callRemoteSecureServiceWithoutEliteStart('DocumentService-ShopForAdditionalItems', null, url, path)
		result
	}
	
}
