package com.hp.elite.quotes

import grails.converters.JSON
import org.codehaus.groovy.grails.web.json.JSONObject
import sun.misc.BASE64Decoder

class QuoteService{

    boolean transactional = false
	def jsonDataService
	def grailsApplication

	def externalCacheService

	def getQuotes(def organizationID, def userID, def sortby, def page, def pageSize, def dateFilter, def typeFilter,def statusFilter,def createdIn,def quoteNameOrNumber,def homePage) {
		def data=(['organizationId':organizationID,'userId':userID,'sortBy':sortby,'page':page,'pageSize':pageSize, 'dateFilter':dateFilter,
			'typeFilter':typeFilter,'statusFilter':statusFilter,'createdIn':createdIn,'quoteNameOrNumber':quoteNameOrNumber,'homePage':homePage] as JSON).toString()
		//def path= grailsApplication.config.quotes.path
		def path = grailsApplication.config.hp.page.secure.service.pathwithoutelitestart
		def url= grailsApplication.config.hp.account.server.url

		def hashField = "${organizationID}:${userID}:${sortby}:${page}:${pageSize}:${dateFilter}:${typeFilter}:${statusFilter}:${createdIn}:${quoteNameOrNumber}"
		def quotesStr = externalCacheService.hash("QuoteList:${organizationID}", hashField, grailsApplication.config.grails.redis.ttlDocs) {
			def quotes = jsonDataService.callRemoteSecureServiceWithoutEliteStart('DocumentService-List',data,url,path)
			return quotes?.toString()
		}
		def result = new JSONObject(quotesStr?:'{}')

        //def url = grailsApplication.config.nav.content.url
        //def path = "/Elite/json/fakeservices/quoteLP.json"
        //def result = jsonDataService.getService(url,path)
        log.debug("DEBUGGER" + (result as JSON))
		result
    }

    def getQuoteSummary(def map){
    	def data=(map as JSON).toString()
    	def path = grailsApplication.config.hp.page.secure.service.pathwithoutelitestart
    	def url= grailsApplication.config.hp.account.server.url
    	def result=jsonDataService.callRemoteSecureServiceWithoutEliteStart('DocumentService-ViewDocumentSummary',data,url,path)
		log.debug("getQuoteSummary result is "+result);
    	result
    }
	
	def getQuoteSummaryNonCatalog(def quoteUUID, def quoteType){
    	def data=(['QuoteType':quoteType,'QuoteUUID':quoteUUID,'quoteHasValidPriceDescriptor':'true']as JSON).toString()
    	def path = grailsApplication.config.hp.page.secure.service.pathwithoutelitestart
    	def url= grailsApplication.config.hp.account.server.url
    	def result=jsonDataService.callRemoteSecureServiceWithoutEliteStart('DocumentService-ViewDocumentSummary',data,url,path)
		log.debug("getQuoteSummaryNonCatalog - getQuoteSummary result is "+result);
    	result
    }
	
	def getQuoteSummaryBMIWatson(def quoteID, def quoteType, def quoteVersion){
    	def data=(['QuoteID':quoteID,'QuoteType':quoteType,'QuoteVersion':quoteVersion]as JSON).toString()
    	def path = grailsApplication.config.hp.page.secure.service.pathwithoutelitestart
    	def url= grailsApplication.config.hp.account.server.url
    	def result=jsonDataService.callRemoteSecureServiceWithoutEliteStart('DocumentService-ViewDocumentSummary',data,url,path)
		log.debug("getQuoteSummaryBMIWatson - getQuoteSummary result is "+result);
    	result
    }

    def getQuoteEdit(def section, def quoteUUID){
		def data=(['section':section]as JSON).toString()
    	def path = grailsApplication.config.hp.page.secure.service.pathwithoutelitestart
    	def url= grailsApplication.config.hp.account.server.url
        //def url = grailsApplication.config.nav.content.url
        //def path = "/Elite/json/logisticTest.json"
        //def result = jsonDataService.getService(url,path)
    	 def result=jsonDataService.callRemoteSecureServiceWithoutEliteStart('DocumentService-EditDocumentSection',data,url,path)
    	result
	}
    
    def getQuoteEdit(def quoteInfo){
        def data=quoteInfo.toString()
        def path = grailsApplication.config.hp.page.secure.service.pathwithoutelitestart
        def url= grailsApplication.config.hp.account.server.url
        def result=jsonDataService.callRemoteSecureServiceWithoutEliteStart('DocumentService-EditDocumentSection',data,url,path)
        result
    }

	//For Returning Case No need to Change the method "callRemoteService" to "callRemoteSecureServiceWithoutEliteStart"
    def getQuoteReturnEdit(def quoteUUID){
        def data=(['TransientID':quoteUUID,'section':"None"] as JSON).toString()
        def path = grailsApplication.config.hp.page.secure.service.path
        def url= grailsApplication.config.hp.account.server.url
        def result=jsonDataService.callRemoteSecureService('DocumentService-EditDocumentSection',data,url,path)
        result
    }

    def saveQuoteInfo(def quoteInfo){
        def data=quoteInfo.toString()
        def path = grailsApplication.config.hp.page.secure.service.pathwithoutelitestart
        def url= grailsApplication.config.hp.account.server.url
        def result=jsonDataService.callRemoteSecureServiceWithoutEliteStart('DocumentService-SaveDocument',data,url,path)
        result
    }
	
	def saveNonCatalogQuoteInfo(def quoteUUID, def quoteType){
		def data=(['QuoteUUID':quoteUUID,'SaveDraft':"true", "TransientStatus":"2","QuoteType":quoteType] as JSON).toString()
		def path = grailsApplication.config.hp.page.secure.service.pathwithoutelitestart
		def url= grailsApplication.config.hp.account.server.url
		def result=jsonDataService.callRemoteSecureServiceWithoutEliteStart('DocumentService-SaveDocument',data,url,path)
		result
	}

	def saveCatalogQuoteInfo(def map){
		def data=(map as JSON).toString()
		def path = grailsApplication.config.hp.page.secure.service.pathwithoutelitestart
		def url= grailsApplication.config.hp.account.server.url
		def result=jsonDataService.callRemoteSecureServiceWithoutEliteStart('DocumentService-SaveDocument',data,url,path)
		result
	}


	def createNonCatalogQuote(){
		def data=null
		def path = grailsApplication.config.hp.page.secure.service.pathwithoutelitestart
		def url= grailsApplication.config.hp.account.server.url
		def result=jsonDataService.callRemoteSecureServiceWithoutEliteStart('DocumentService-CreateQuote?QuoteType=NonCatalog',data,url,path)
		result
	}
	
    def createQuote(def createQuoteRequest){
        def data=createQuoteRequest
        def path = grailsApplication.config.hp.page.secure.service.pathwithoutelitestart
        def url= grailsApplication.config.hp.account.server.url
        def result=jsonDataService.callRemoteSecureServiceWithoutEliteStart('DocumentService-CreateDocument',data,url,path)
        result
    }

    def createCatalogQuote(){
    	def data = null
    	def path = grailsApplication.config.hp.page.secure.service.pathwithoutelitestart
        def url= grailsApplication.config.hp.account.server.url
        def result=jsonDataService.callRemoteSecureServiceWithoutEliteStart('DocumentService-CreateQuote?QuoteType=Catalog',data,url,path)
    }

    def copyQuote(def quoteUUID,def quoteType){
        def data=(['QuoteUUID':quoteUUID,'Context':'QUOTE','QuoteType':quoteType]as JSON).toString()
        def path = grailsApplication.config.hp.page.secure.service.pathwithoutelitestart
        def url= grailsApplication.config.hp.account.server.url
        def result=jsonDataService.callRemoteSecureServiceWithoutEliteStart('DocumentService-CopyDocument',data,url,path)
        result
    }
	
	def reviseQuote(def quoteUUID,def createVersion){
		def data=(['QuoteUUID':quoteUUID,'createVersion':createVersion]as JSON).toString()
		def path = grailsApplication.config.hp.page.secure.service.pathwithoutelitestart
		def url= grailsApplication.config.hp.account.server.url
		def result=jsonDataService.callRemoteSecureServiceWithoutEliteStart('DocumentService-CopyDocument',data,url,path)
		result
	}

    def createPRFromQuote(def quoteUUID){
        def data=(['QuoteUUID':quoteUUID]as JSON).toString()
        def path = grailsApplication.config.hp.page.secure.service.pathwithoutelitestart
        def url= grailsApplication.config.hp.account.server.url
        def result=jsonDataService.callRemoteSecureServiceWithoutEliteStart('DocumentService-CreatePRFromQuote',data,url,path)
        result

    }

    def createPOFromQuote(def quoteUUID){
        def data=(['QuoteUUID':quoteUUID]as JSON).toString()
        def path = grailsApplication.config.hp.page.secure.service.pathwithoutelitestart
        def url= grailsApplication.config.hp.account.server.url
        def result=jsonDataService.callRemoteSecureServiceWithoutEliteStart('DocumentService-CreatePOFromQuote',data,url,path)
        result

    }
	
	def createPOFromBMIWatsonQuote(def createPORequest){
		def data=createPORequest
		def path = grailsApplication.config.hp.page.secure.service.pathwithoutelitestart
		def url= grailsApplication.config.hp.account.server.url
		def result=jsonDataService.callRemoteSecureServiceWithoutEliteStart('DocumentService-CreatePOFromQuote',data,url,path)
		result

	}
	
	def createPRFromBMIWatsonQuote(def createPRRequest){
		def data=createPRRequest
		def path = grailsApplication.config.hp.page.secure.service.pathwithoutelitestart
		def url= grailsApplication.config.hp.account.server.url
		def result=jsonDataService.callRemoteSecureServiceWithoutEliteStart('DocumentService-CreatePRFromQuote',data,url,path)
		result

	}

    def finalSaveCreateQuote(){
        def data = null
        def path = grailsApplication.config.hp.page.secure.service.pathwithoutelitestart
        def url= grailsApplication.config.hp.account.server.url
        def result=jsonDataService.callRemoteSecureServiceWithoutEliteStart('DocumentService-CreateQuote',data,url,path)
        result

    }

    def checkOrderForDocumentCreation(def quoteUUID){
        def data=(['QuoteUUID':quoteUUID]as JSON).toString()
        def path = grailsApplication.config.hp.page.secure.service.pathwithoutelitestart
        def url= grailsApplication.config.hp.account.server.url
        def result=jsonDataService.callRemoteSecureServiceWithoutEliteStart('DocumentService-CheckOrderCapable',data,url,path)
        result
    }
    
    def deleteQuote(def quoteUUID, def docStatus){
	def data=[:]
	data[quoteUUID]=docStatus;
	data=data as JSON;
	log.debug data;	
	def path = grailsApplication.config.hp.page.secure.service.pathwithoutelitestart
	def url= grailsApplication.config.hp.account.server.url
	def result=jsonDataService.callRemoteSecureServiceWithoutEliteStart('DocumentService-RemoveDocument',data,url,path)
	log.debug result;
	result
    }
	
	def downloadPDF(String quoteUUID) {
		
		def requestMap = [:]
		requestMap["QuoteUUID"] = quoteUUID;
		def data = (requestMap as JSON)
		def path = grailsApplication.config.hp.page.secure.service.pathwithoutelitestart
		def url= grailsApplication.config.hp.account.server.url
		def result=jsonDataService.callRemoteSecureServiceWithoutEliteStart('DocumentService-ExportToPDF',data,url,path)
		if (result !=null) {
			log.debug("downloaded PDF  " + result.fileName)
			return result
		} else {
			log.error("error calling DocumentService-ExportToPDF for QuoteUUID + quoteUUID");
		}
		
	}
	
	def getAttachmentStreamContent(params){
		def data 
		def result
		def path = grailsApplication.config.hp.page.secure.service.pathwithoutelitestart
		def url = grailsApplication.config.hp.account.server.url
		def serviceName = "DocumentService-GetQuoteAttachment"
		def   fileContent=''
		def fileInfo = [:]
		def decodedBytes = null
		def mimeType = ''
		def ext
		def fileName = params.fileName
		/**
		 *  Get Base64 encrypted byte array data of attachment file
		 *
		 */
		 if(params.serviceName)
		 	serviceName = params.serviceName;
		if(serviceName.equals("DocumentService-GetQuoteRequestAttachment"))
			 data = (['UUID':params.uuid,'filename':params.fileName] as JSON).toString()
		else
			 data = (['quoteUUID':params.uuid,'fileName':params.fileName] as JSON).toString()
		 result = jsonDataService.callRemoteSecureServiceWithoutEliteStart(serviceName, data, url, path)
		 try{
			BASE64Decoder decoder = new BASE64Decoder()
			if(result!=null)
			{
				fileContent =  result['fileAttachment']
				decodedBytes = decoder.decodeBuffer(fileContent) 
			}
			 
		
		}
		catch(Exception e){
			log.error("attachments errors "+e.getMessage())
		}
		fileInfo['decodedBytes']  = decodedBytes
		
		
		/*
		 * Setting content type and header information
		 */
		
		//move this to controller
		try{
			def mimeTypeMap = grailsApplication.config.hp.admin.accountMessage.document.MIMEType
			ext = fileName?.lastIndexOf(".")!=-1?fileName?.substring(fileName?.lastIndexOf(".")+1, fileName.size()):'default'
			mimeType = mimeTypeMap[ext]?mimeTypeMap[ext]:mimeTypeMap['default']
			
		}
		catch(Exception e){
			log.error("Admin lits message file containt "+e.getMessage())
		}
			
		fileInfo['mimeType'] = mimeType
		fileInfo['ext']	=ext
		
		return fileInfo
	}
	
	def createNewQuote(def goodsAndServ,def fname,def lname,def phone,def email,def quoteRef,def details,def orgId,def buyerId,def fileContentsList)
	{
		def path = grailsApplication.config.hp.eproc.server.path
		def url= grailsApplication.config.hp.account.server.url
		def result
		def data = (['GoodsAndServices':goodsAndServ,'PersonalFirstName':fname,'PersonalLastName':lname,'PersonalPhone':phone,
					'PersonalEMail':email,'PriorQuoteReference':quoteRef,'OpenTextRequest':details,
					'organizationID':orgId,'BuyerID':buyerId,'IsQuoteRequest':'true','PoAttachment':fileContentsList,'sortBy':'creationdate.desc'] as JSON).toString();
		result=jsonDataService.callRemoteSecureServiceWithJSON("DocumentService-SaveQuoteRequest",data,url,path)
		return result
	}
	
	def getAllQuoteRequests(def orgId,def buyerId,def sortby,def page,def pagesize)
	{
		def path = grailsApplication.config.hp.page.secure.service.pathwithoutelitestart
		def url = grailsApplication.config.hp.account.server.url
		def data = (['BuyerID':buyerId,'organizationID':orgId,'sortBy':sortby,'page':page,'pageSize':pagesize] as JSON).toString();
		def result = jsonDataService.callRemoteSecureServiceWithoutEliteStart("DocumentService-QuoteRequestList", data, url, path);
		return result
	}
	
		
	def deleteQuotes(def orgId,def buyerId,def uuidList)
	{
		def path = grailsApplication.config.hp.page.secure.service.pathwithoutelitestart
		def url = grailsApplication.config.hp.account.server.url
		def data = (['UUID':uuidList,'organizationID':orgId,'BuyerID':buyerId] as JSON).toString();
		def result = jsonDataService.callRemoteSecureServiceWithoutEliteStart("DocumentService-DeleteQouteRequest", data, url, path);
		return result
	}
	
}
