package com.hp.elite.partneragent

import grails.converters.JSON
import org.codehaus.groovy.grails.web.json.JSONObject
import sun.misc.BASE64Decoder

class PartnerAgentService {
	
	boolean transactional = false
	def jsonDataService
	def grailsApplication
	
	def getOrganizationDetails(def buyerID)
	{
		def path = grailsApplication.config.hp.page.secure.service.pathwithoutelitestart
		def url = grailsApplication.config.hp.account.server.url
		def data = (['buyerID':buyerID] as JSON).toString();
		def result = jsonDataService.callRemoteSecureServiceWithoutEliteStart("UserService-GetOrganizationDetails", data, url, path);
		return result
	}
	
	def getPartnerAgentUserCountry(def OrganizationID)
	{
		def path = grailsApplication.config.hp.page.secure.service.pathwithoutelitestart
		def url = grailsApplication.config.hp.account.server.url
		def data = (['organizationID':OrganizationID] as JSON).toString();
		def result = jsonDataService.callRemoteSecureServiceWithoutEliteStart("UserService-GetUserDetails", data, url, path);
		return result
	}
	//UserService-GetCurrencyDetails?jsonString={"OrganizationID":"55gPiNVOgPMAAAD4lbPs2Yn3","ChangeCountry":"FR"}
	def getPartnerAgentUserCurrency(def organizationID, def changeCountry)
	{
		def path = grailsApplication.config.hp.page.secure.service.pathwithoutelitestart
		def url = grailsApplication.config.hp.account.server.url
		def data = (["organizationID":organizationID,"ChangeCountry":changeCountry] as JSON).toString();
		def result = jsonDataService.callRemoteSecureServiceWithoutEliteStart("UserService-GetCurrencyDetails", data, url, path);
		return result
	}
	def getPartnerAgentSearchResults(def map)
	{
		def path = grailsApplication.config.hp.page.secure.service.pathwithoutelitestart
		def url = grailsApplication.config.hp.account.server.url
		def data = (map as JSON).toString()
		def result = jsonDataService.callRemoteSecureServiceWithoutEliteStart("UserService-SearchAccountWithFilter", data, url, path);
		return result
	}
}
