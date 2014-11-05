
package com.hp.elite.partneragent;

import grails.converters.JSON
import groovy.json.JsonBuilder
import groovy.json.JsonSlurper
import spock.lang.Specification

import com.hp.elite.partneragent.PartnerAgentService

@TestFor(PartnerAgentController)
class PartnerAgentControllerSpec extends Specification {

	
	public void testRetriveUserOrganizations() {
		setup:
		
			
			def oragnizationMap = getResultJSON()
			def uuidMap = ["field":"uuid"]
			def jsonStr = new groovy.json.JsonBuilder(oragnizationMap).toString()
			def jsonObject = new JsonSlurper().parseText(jsonStr)
			def partnerAgentService = Mock(PartnerAgentService)
			partnerAgentService.getOrganizationDetails(_) >> jsonObject
			controller.partnerAgentService = partnerAgentService
			controller.metaClass.getPrincipal = {->[username:"coor0_pur@yahoo.com",uuid:"324r6y23794r2378df7823"]}
			//controller.metaClass.sec = {->uuidMap} 
			//controller.metaClass.buyerID = "12345678"
		when:
			controller.retriveUserOrganizations()
			def results = JSON.parse(response.contentAsString)
		
		then:
			results.size()>0
			assertNotNull results.OrganizationMap
		
	}

	
	public void testRetriveUsers() {
		
		setup:
		
			
			def oragnizationMap = countryUsersMap()
			def jsonStr = new groovy.json.JsonBuilder(oragnizationMap).toString()
			def jsonObject = new JsonSlurper().parseText(jsonStr)
			def partnerAgentService = Mock(PartnerAgentService)
			partnerAgentService.getPartnerAgentUserCountry(_) >> jsonObject
			controller.partnerAgentService = partnerAgentService
			controller.metaClass.getPrincipal = {->[username:"coor0_pur@yahoo.com"]}
			
		when:
			controller.retriveUsers()
			def results = JSON.parse(response.contentAsString)
		
		then:
			results.size()>0
		
	}
		

	
	public void testRetriveCurrency() {
		setup:
		
			params.organizationID = ""
			params.currencyCode =""
			def oragnizationMap = countryUsersMap()
			def jsonStr = new groovy.json.JsonBuilder(oragnizationMap).toString()
			def jsonObject = new JsonSlurper().parseText(jsonStr)
			def partnerAgentService = Mock(PartnerAgentService)
			partnerAgentService.getPartnerAgentUserCurrency(_,_) >> jsonObject
			controller.partnerAgentService = partnerAgentService
			controller.metaClass.getPrincipal = {->[username:"coor0_pur@yahoo.com"]}
			
		when:
			controller.retriveCurrency()
			def results = JSON.parse(response.contentAsString)
		
		then:
			results.size()>0
		
	}
	
	private def getResultJSON(){
		[
			"OrganizationMap": [
				[
					"organizationID": "CHRW20070509165746",
					"organizationName": "C H ROBINSON WORLDWIDE INC"
				],
				[
					"organizationID": "USB2B070522142248",
					"organizationName": "(United States)C H ROBINSON Server/Storage L4",
					"organizationUUID": "aPoQ6TRN0JMAAAESah5lZLfe"
				]
			],
			"PartnerAgentDetails": [
				"lastName": "purchaser",
				"partnerOrganization": "testpartnerorg",
				"logoUrl": "789675/PersonalLogo/test_logo.jpg",
				"firstName": "yugu"
			]
		]
	}
	
	private def countryUsersMap(){
		[
			"PDS": [
					"USUSDDP": "USD-Duty Paid",
					"UK": "EUR-Duty Paid"
				],
			"CountryNames": [
					"UK": "United Kingdom",
					"US": "United States"
					],
			"UserList": [
				[
					"buyerID": "siPAqPaMWoEAAAFFdrdU6eA5",
					"businessPartnerNo": "anc@gmail.com",
					"lastName": "abc",
					"firstName": "xyz"
				],
				[
					"buyerID": "IzoQ6TRMoKgAAAEcZRgoFv1N",
					"businessPartnerNo": "aquarius_pur2@yahoo.com",
					"lastName": "Tester",
					"firstName": "Aquarius"
				],
				[
					"buyerID": "tKQQ6TRMc5IAAAEYLq4hAjA0",
					"businessPartnerNo": "ch2m_mgr1@yahoo.com",
					"lastName": "mgr1",
					"firstName": "ch2m"
				]
			]
		]
	}

}
