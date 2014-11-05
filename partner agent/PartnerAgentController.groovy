package com.hp.elite.partneragent

import grails.converters.JSON

class PartnerAgentController {
	
	def partnerAgentService
	
	def retriveUserOrganizations(){
		def buyerID = getPrincipal().uuid
		def result = partnerAgentService.getOrganizationDetails(buyerID)
		//def result =getNewOrgJSON()
		if(result?.ErrorCode){
			if(result.ErrorCode.equals("PA001") || result.ErrorCode.equals("PA002")){
				result = ["ErrorCode":message(code: 'partnerAgent.error.user.not.found')]
			}
		}
		if(result?.PartnerAgentDetails){
			if(result?.PartnerAgentDetails?.logoUrl){
				def orgLogoUrl = result.PartnerAgentDetails.logoUrl
				def b2burl = grailsApplication.config.nav.eprime.url?:''
				def b2bpath = grailsApplication.config.nav.eprime.path?:''
				orgLogoUrl = b2burl+b2bpath+orgLogoUrl
				result.PartnerAgentDetails.logoUrl = orgLogoUrl
			}
		}
		render (result as JSON)
	}
	
	def retriveUsers(){
		def organizationID = params.organizationID
		def result = partnerAgentService.getPartnerAgentUserCountry(organizationID)
		//def result = countryUsersMap()
		render (result as JSON)
	}
	
	def retriveCurrency(){
		def organizationID = params.organizationID
		def currencyCode = params.currencyCode
		def result = partnerAgentService.getPartnerAgentUserCurrency(organizationID,currencyCode)
		//def result = countryUsersMap()
		render (result as JSON)
	}
	
	def retriveSearchResults(){
		def searchUserMap=[:]
		def page = params.page?:'1' 
		def pageSize = params.pageSize?:'20'
		def sortby = params.sortby
		def region = params.region 
		def assignmentType = params.assignmentType 
		def eliteFlag = "true"
		def filterRequired = params.filterRequired?:'false'
		def OrganizationID = params.organizationID
		def PartnerAgentSearchTerm = params.partnerAgentSearchTerm?:''
		searchUserMap = ["page":page,"pageSize":pageSize,"sortby":sortby,"assignmentType":assignmentType,"eliteFlag":eliteFlag,"filterRequired":filterRequired,
			"OrganizationID":OrganizationID,"PartnerAgentSearchTerm":PartnerAgentSearchTerm]
		def result = partnerAgentService.getPartnerAgentSearchResults(searchUserMap)
		//def result = searchJSON()
		render (result as JSON)
	}
	
	private def getResultJSON(){
		[
			"OrganizationMap": [
				[
					"organizationID": "CHRW20070509165746",
					"organizationName": "C H ROBINSON WORLDWIDE INC",
					"organizationUUID": "aPoQ6TRN0JMAAAESah5lZLqw"
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
				"partnerOrganizationID" : "444567",
				"logoUrl": "789675/PersonalLogo/test_logo.jpg",
				"firstName": "yugu"
			]
		]
	}
	
	private def getNewOrgJSON(){
		[
			"OrganizationMap": [
				[
					"organizationName": "National Geographic",
					"organizationID": "kt0PiqlphN8AAAELlI4.7hsO"
				],
				[
					"organizationName": "C H ROBINSON Server/Storage L4",
					"organizationID": "aPoQ6TRN0JMAAAESah5lZLfe"
				],
				[
					"organizationName": "Nestle - EB (fusion)",
					"organizationID": "q0EQ6mNSKwMAAAEzEoE8cPwt"
				],
				[
					"organizationName": "National Geographic Magazine",
					"organizationID": "JHkPiqlprtAAAAELg19ksx8r"
				]
			],
			"PartnerAgentDetails": [
				"lastName": "purchaser",
				"partnerOrganization": "testpartnerorg",
				"partnerOrganizationID" : "444567",
				"logoUrl": "789675/PersonalLogo/test_logo.jpg",
				"firstName": "yugu"
			]
		]
	}
	
	private def getPartnerAgentDetails(){
		[
			"PartnerAgentDetails": [
				"lastName": "purchaser",
				"partnerOrganization": "testpartnerorg",
				"partnerOrganizationID" : "444567",
				"logoUrl": "789675/PersonalLogo/test_logo.jpg",
				"firstName": "yugu"
			]
		]
	}
	
	private def countryUsersMap(){
		[
			"CurrencyList": [
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
	
	private def searchJSON(){
		[
			"FilterData": [
			  "RegionFilterData": [
				"US"
			  ],
			  "AccountFilterData": [
				"C H ROBINSON Server/Storage L4"
			  ]
			],
			"AccountSearchDetails": [
			  "pageSize": 20,
			  "pageStart": 1,
			  "elementCount": 7,
			  "pageEnd": 20,
			  "page": 1,
			  "pageCount": 1,
			  "elements": [
				  [
					"accountPin": "USB2B070522142248",
					"accountName": "C H ROBINSON Server/Storage L4",
					"hostRegion": "US",
					"firstName": "Chrob",
					"lastName": "S2S",
					"userId": "chrob_stos_pur@yahoo.com",
					"email": "chrob_stos_pur@yahoo.com",
					"role": "Purchaser"
				  ],
				  [
					"accountPin": "USB2B070522142248",
					"accountName": "C H ROBINSON Server/Storage L4",
					"hostRegion": "US",
					"firstName": "CHRob",
					"lastName": "Req",
					"userId": "chrob_req@yahoo.com",
					"email": "chrob_req@yahoo.com",
					"role": "Requester"
				  ],
				  [
					"accountPin": "USB2B070522142248",
					"accountName": "C H ROBINSON Server/Storage L4",
					"hostRegion": "US",
					"firstName": "Pete",
					"lastName": "Purchaser",
					"userId": "chrob_puradm@yahoo.com",
					"email": "chrob_puradm@yahoo.com",
					"role": "Purchaser"
				  ],
				  [
					"accountPin": "USB2B070522142248",
					"accountName": "C H ROBINSON Server/Storage L4",
					"hostRegion": "US",
					"firstName": "CHRob",
					"lastName": "Purch",
					"userId": "chrob_pur@yahoo.com",
					"email": "chrob_pur@yahoo.com",
					"role": "Purchaser"
				  ],
				  [
					"accountPin": "USB2B070522142248",
					"accountName": "C H ROBINSON Server/Storage L4",
					"hostRegion": "US",
					"firstName": "CHRob",
					"lastName": "Mngr",
					"userId": "chrob_mgr@yahoo.com",
					"email": "chrob_mgr@yahoo.com",
					"role": "Manager"
				  ],
				  [
					"accountPin": "USB2B070522142248",
					"accountName": "C H ROBINSON Server/Storage L4",
					"hostRegion": "US",
					"firstName": "Charles",
					"lastName": "Chapman",
					"userId": "chrob_pur3@yahoo.com",
					"email": "chrob_pur3@yahoo.com",
					"role": "Purchaser"
				  ],
				  [
					"accountPin": "USB2B070522142248",
					"accountName": "C H ROBINSON Server/Storage L4",
					"hostRegion": "US",
					"firstName": "Cathy",
					"lastName": "Capella",
					"userId": "chrob_pur5@yahoo.com",
					"email": "chrob_pur5@yahoo.com",
					"role": "Purchaser"
				  ]
				],
			  "hasMoreRecords": "false"
			]
		  ]
		  
	}

	
}
