var config = {env: '_proddc2split', imageBase: 'http://mlb.mlb.com/mlb/images/flagstaff', appletvBase: 'http://lws.mlb.com/appletvv2', bamServiceBase: 'https://mlb-ws.mlb.com', gamedayBase: 'http://gdx.mlb.com', mlbBase: 'http://www.mlb.com', secureMlbBase: 'https://secure.mlb.com'};

// ***************************************************

var EMAIL_IDENTITY_POINT_KEY = "email.identityPointId";
var EMAIL_FINGERPRINT_KEY = "email.fingerprint";

// atv.Element extensions
atv.Element.prototype.getElementsByTagName = function(tagName) {
	return this.ownerDocument.evaluateXPath("descendant::" + tagName, this);
}

// Extend atv.ProxyDocument to load errors from a message and description.
atv.ProxyDocument.prototype.loadError = function(message, description) {
	var doc = atvutils.makeErrorDocument(message, description);
	this.loadXML(doc);
}

// Extend atv.ProxyDocument to load errors from a message and description.
atv.ProxyDocument.prototype.swapXML = function(xml) {
	var me = this;
    try 
    {
        me.loadXML(xml, function(success) {
            if ( success ) {
                atv.unloadPage();
            } else {
                console.log("swapXML failed to load " + xml);
                me.loadXML(atvutils.siteUnavailableError(), function(success) {
                	console.log("swapXML site unavailable error load success " + success);
                    if ( success ) {
                        atv.unloadPage();
                    }
                });
            }
        });
    } 
    catch (e) 
    {
        console.error("swapXML caught exception while loading " + xml + ". " + e);
        me.loadXML(atvutils.siteUnavailableError(), function(success) {
            if ( success ) {
                atv.unloadPage();
            }
        });
    }
}

// atv.Document extensions
atv.Document.prototype.getElementById = function(id) {
 var elements = this.evaluateXPath("//*[@id='" + id + "']", this);
 if ( elements && elements.length > 0 ) {
     return elements[0];
 }
 return undefined;
}

// atv.Element extensions
atv.Element.prototype.getElementsByTagName = function(tagName) {
 return this.ownerDocument.evaluateXPath("descendant::" + tagName, this);
}

atv.Element.prototype.getElementByTagName = function(tagName) {
 var elements = this.getElementsByTagName(tagName);
 if ( elements && elements.length > 0 ) {
     return elements[0];
 }
 return undefined;
}

// ATVUtils - a JavaScript helper library for Apple TV
ATVUtils = function() {};

ATVUtils.prototype.onGenerateRequest = function (request) {
	
    console.log('atvutils.js atv.onGenerateRequest() oldUrl: ' + request.url);

    console.debug("atvutils.js atv.onGenerateRequest() atv.localStorage[mediaMetaRequestParams] " + atv.localStorage[MEDIA_META_REQUEST_PARAMS_KEY]);
	if ( request.url.indexOf("encode(MEDIA_META_REQUEST_PARAMS)") > -1 && atv.localStorage[MEDIA_META_REQUEST_PARAMS_KEY])
	{
		request.url = request.url.replace("encode(MEDIA_META_REQUEST_PARAMS)", encodeURIComponent(atv.localStorage[MEDIA_META_REQUEST_PARAMS_KEY]));
	}
	console.debug("atvutils.js atv.onGenerateRequest() atv.localStorage[mediaMetaRequestParams] " + atv.localStorage[MEDIA_META_REQUEST_PARAMS_KEY]);
	if ( request.url.indexOf("MEDIA_META_REQUEST_PARAMS") > -1 && atv.localStorage[MEDIA_META_REQUEST_PARAMS_KEY])
	{
		request.url = request.url.replace("MEDIA_META_REQUEST_PARAMS", atv.localStorage[MEDIA_META_REQUEST_PARAMS_KEY]);
	}
	if ( request.url.indexOf("IPID") > -1 && atv.localStorage[BEST_IDENTITY_POINT_KEY])
	{
		request.url = request.url.replace("IPID", atv.localStorage[BEST_IDENTITY_POINT_KEY]);
	}
	if ( request.url.indexOf("FINGERPRINT") > -1 && atv.localStorage[BEST_FINGERPRINT_KEY])
	{
		request.url = request.url.replace("FINGERPRINT", encodeURIComponent(atv.localStorage[BEST_FINGERPRINT_KEY]));
	}
	if ( request.url.indexOf("SESSION_KEY") > -1 )
	{
		var sessionKey = atv.localStorage[SESSION_KEY]; 
		console.debug("atvutils.js atv.onGenerateRequest sessionKey=" + sessionKey);
		request.url = request.url.replace("SESSION_KEY", (sessionKey) ? encodeURIComponent(sessionKey) : "");
	}
	if ( request.url.indexOf("encode(LINK_PROXY_REQUEST)") > -1 && atv.localStorage[EMAIL_IDENTITY_POINT_KEY] != null && atv.localStorage[EMAIL_FINGERPRINT_KEY] != null)
	{
		var linkRequest = serviceClient.createLinkRequest();
		console.debug("atvutils.js atv.onGenerateRequest() linkRequest " + linkRequest);
		if (linkRequest != null)
		{
			// WARNING: encode twice because the xs:uri type does not suppot curly brackets so encode an extra time. The LinkProxy class will be responsible for url unencoding the json 
			request.url = request.url.replace("encode(LINK_PROXY_REQUEST)", encodeURIComponent(encodeURIComponent(JSON.stringify(linkRequest))));
		}
	}
	if ( request.url.indexOf("$$showScores") > -1 )
	{
		var showScores = atv.localStorage[SHOW_SCORES_KEY]; 
		console.log("showScores=" + showScores);
		request.url = request.url.replace("$$showScores", (showScores) ? showScores : "true");
	}
	var authorized = atv.localStorage[AUTHORIZED_KEY]; 
	if ( request.url.indexOf("AUTH_MESSAGE_CODE") > -1 && authorized != null)
	{
		console.debug("atvutils.js AUTH_MESSAGE_CODE atv.onGenerateRequest() authorized " + authorized);
//		request.url = request.url.replace("AUTH_MESSAGE_CODE", ((authorized == "true") ? "ALREADY_PURCHASED" : "NOT_AUHTORIZED"));
	}

	console.log('atvutils.js atv.onGenerateRequest() newUrl: ' + request.url);
}


ATVUtils.prototype.makeRequest = function(url, method, headers, body, callback) {
    if ( !url ) {
        throw "loadURL requires a url argument";
    }

	var method = method || "GET";
	var	headers = headers || {};
	var body = body || "";
	
    var xhr = new XMLHttpRequest();
    xhr.onreadystatechange = function() {
        try {
            if (xhr.readyState == 4 ) {
                if ( xhr.status == 200) {
                	console.log("makeRequest() getAllResponseHeaders(): " + xhr.getAllResponseHeaders());
                    callback(xhr.responseXML, xhr.responseText);
                } else {
                    console.log("makeRequest() received HTTP status " + xhr.status + " for " + url);
                    callback(xhr.responseXML, xhr.responseText);
                }
            }
        } catch (e) {
            console.error('makeRequest caught exception while processing request for ' + url + '. Aborting. Exception: ' + e);
            xhr.abort();
            callback(null, null);
        }
    }
    
    var request = {url: url};
    this.onGenerateRequest(request);
    
    console.debug("makeRequest() url " + request.url);

    xhr.open(method, request.url, true);

    for(var key in headers) {
        xhr.setRequestHeader(key, headers[key]);
    }

    console.debug("sending body in the send() method");
    xhr.send(body);
    return xhr;
}

ATVUtils.prototype.getRequest = function(url, callback) {
	return this.makeRequest(url, "GET", null, null, callback);
}

ATVUtils.prototype.postRequest = function(url, body, callback) {
	return this.makeRequest(url, "POST", null, body, callback);
}

ATVUtils.prototype.makeErrorDocument = function(message, description) {
    if ( !message ) {
        message = "";
    }
    if ( !description ) {
        description = "";
    }

    var errorXML = '<?xml version="1.0" encoding="UTF-8"?> \
    <atv> \
        <body> \
            <dialog id="com.bamnetworks.errorDialog"> \
                <title><![CDATA[' + message + ']]></title> \
                <description><![CDATA[' + description + ']]></description> \
            </dialog> \
        </body> \
    </atv>';

    return atv.parseXML(errorXML);
}

ATVUtils.prototype.makeOkDialog = function(message, description, onSelect) {
	if ( !message ) {
		message = "";
	}
	if ( !description ) {
		description = "";
	}
	
	if (!onSelect)
	{
		onSelect = "atv.unloadPage();";
	}
	
	var errorXML = '<?xml version="1.0" encoding="UTF-8"?> \
		<atv> \
		<head> \
			<script src="'+config.appletvBase+'/views/config.js" />\
			<script src="'+config.appletvBase+'/js/atvutils.js" />\
			<script src="'+config.appletvBase+'/js/service.js" />\
			<script src="'+config.appletvBase+'/js/main.js" />\
		</head>\
	
2000
	<body> \
		<optionDialog id="com.bamnetowrks.optionDialog"> \
		<header><simpleHeader><title>' + message + '</title></simpleHeader></header> \
		<description><![CDATA[' + description + ']]></description> \
		<menu><sections><menuSection><items> \
			<oneLineMenuItem id="lineMenu" accessibilityLabel="OK" onSelect="' + onSelect + '">\
				<label>OK</label>\
			</oneLineMenuItem>\
		</items></menuSection></sections></menu>\
		</optionDialog> \
		</body> \
		</atv>';
	
	var doc = atv.parseXML(errorXML);
	return doc;
}

ATVUtils.prototype.printObject = function(object) {
	for (property in object)
	{
		console.log(property + ": " + object[property]);
	}
}


ATVUtils.prototype.siteUnavailableError = function() {
    // TODO: localize
	console.debug("siteUnavailableError()");
    return this.makeOkDialog("MLB.TV error", "MLB.TV is currently unavailable. Please try again later.");
}

ATVUtils.prototype.loadError = function(message, description) {
    atv.loadXML(this.makeErrorDocument(message, description));
}

ATVUtils.prototype.loadAndSwapError = function(message, description) {
    atv.loadAndSwapXML(this.makeErrorDocument(message, description));
}

ATVUtils.prototype.loadURLInternal = function(url, method, headers, body, loader) {
    var me = this;
    var xhr;
    var proxy = new atv.ProxyDocument;
    proxy.show();
    proxy.onCancel = function() {
        if ( xhr ) {
            xhr.abort();
        }
    };
    
    xhr = me.makeRequest(url, method, headers, body, function(xml, xhr) {
        try {
        	console.log("loadURLInternal() proxy=" + proxy + " xml=" + xml + " xhr=" + xhr);
            loader(proxy, xml, xhr);
        } catch(e) {
            console.error("Caught exception loading " + url + ". " + e);
            loader(proxy, me.siteUnavailableError(), xhr);
        }
    });
}

ATVUtils.prototype.loadGet = function(url, loader) {
	this.loadURLInternal(url, "GET", null, null, loader);
}

// TODO:: change to a url string or hash
ATVUtils.prototype.loadURL = function(url, method, headers, body, processXML) {
    var me = this;
    this.loadURLInternal(url, method, headers, body, function(proxy, xml) {
        if(typeof(processXML) == "function") processXML.call(this, xml);
		try {
            proxy.loadXML(xml, function(success) {
                if ( !success ) {
                    console.log("loadURL failed to load " + url);
                    proxy.loadXML(me.siteUnavailableError());
                }
            });
        } catch (e) {
            console.log("loadURL caught exception while loading " + url + ". " + e);
            proxy.loadXML(me.siteUnavailableError());
        }
    });
}

ATVUtils.prototype.swapXML = function(proxy, xml) {
	var me = this;
    try {
        proxy.loadXML(xml, function(success) {
            if ( success ) {
                atv.unloadPage();
            } else {
                console.log("swapXML failed to load " + xml);
                proxy.loadXML(me.siteUnavailableError(), function(success) {
                    if ( success ) {
                        atv.unloadPage();
                    }
                });
            }
        });
    } catch (e) {
        console.error("swapXML caught exception while loading " + xml + ". " + e);
        proxy.loadXML(me.siteUnavailableError(), function(success) {
            if ( success ) {
                atv.unloadPage();
            }
        });
    }
}

// TODO:: change to a url string or hash
// loadAndSwapURL can only be called from page-level JavaScript of the page that wants to be swapped out.
ATVUtils.prototype.loadAndSwapURL = function(url, method, headers, body, processXML) {
    var me = this;
    this.loadURLInternal(url, method, headers, body, function(proxy, xml) { 
	    if(typeof(processXML) == "function") processXML.call(this, xml);
        me.swapXML(proxy, xml);
    });
}

/** Load URL and apply changes to the xml */
ATVUtils.prototype.loadAndApplyURL = function(url, processXML) {
    var me = this;
	this.loadURLInternal(url, 'GET', null, null, function(proxy, xml, xhr) {
		var xmlToLoad = xml;
        if(typeof(processXML) == "function") xmlToLoad = processXML.call(this, xml);
        if (!xmlToLoad) xmlToLoad = xml;
		try {
            proxy.loadXML(xmlToLoad, function(success) {
                if ( !success ) {
                    console.log("loadURL failed to load " + url);
                    proxy.loadXML(me.siteUnavailableError());
                }
            });
        } catch (e) {
            console.log("loadURL caught exception while loading " + url + ". " + e);
            proxy.loadXML(me.siteUnavailableError());
        }
    });
}

var atvutils = new ATVUtils;

console.log("atvutils initialized " + atvutils);

/**
 * This is an AXR handler. It handles most of tediousness of the XHR request and keeps track of onRefresh XHR calls so that we don't end up with multiple page refresh calls.
 * 
 * You can see how I call it on the handleRefresh function below.
 * 
 * 
 * @params object (hash) $h
 * @params string $h.url - url to be loaded
 * @params string $h.method - "GET", "POST", "PUT", "DELTE"
 * @params bool $h.type - false = "Sync" or true = "Async" (You should always use true)
 * @params func $h.success - Gets called on readyState 4 & status 200
 * @params func $h.failure - Gets called on readyState 4 & status != 200
 * @params func $h.callback - Gets called after the success and failure on readyState 4
 * @params string $h.data - data to be sent to the server
 * @params bool $h.refresh - Is this a call from the onRefresh event.
 */
function Ajax($h)
{
	var me = this;
	$h = $h || {}

	// Setup properties
	this.url = $h.url || false;
	this.method = $h.method || "GET";
	this.type = ($h.type === false) ? false : true;
	this.success = $h.success || null;
	this.failure = $h.failure || null;
	this.data = $h.data || null;
	this.complete = $h.complete || null;
	this.refresh = $h.refresh || false;

	if (!this.url)
	{
		console.error('\nAjax Object requires a url to be passed in: e.g. { "url": "some string" }\n')
		return undefined;
	}

	this.createRequest();
	this.req.onreadystatechange = this.stateChange;
	this.req.object = this;
	this.open();
	this.send();
}

Ajax.currentlyRefreshing = false;

Ajax.prototype.stateChange = function()
{
	var me = this.object;

	// console.log("this in stateChage is: "+ this);
	// console.log("object in stateChange is: "+ this.object);

	switch (this.readyState)
	{

	case 1:
		if (typeof (me.connection) === "function")
			me.connection(this, me);
		break;
	case 2:
		if (typeof (me.received) === "function")
			me.received(this, me);
		break;
	case 3:
		if (typeof (me.processing) === "function")
			me.processing(this, me);
		break;
	case 4:
		if (this.status == "200")
		{
			if (typeof (me.success) === "function")
				me.success(this, me);
		}
		else
		{
			if (typeof (me.failure) === "function")
				me.failure(this.status, this, me);
		}
		if (typeof (me.complete) === "function")
			me.complete(this, me);
		if (me.refresh)
			Ajax.currentlyRefreshing = false;
		break;
	default:
		console.log("I don't think I should be here.");
		break;
	}
}

Ajax.prototype.createRequest = function()
{
	try
	{
		this.req = new XMLHttpRequest();
		if (this.refresh)
			Ajax.currentlyRefreshing = true;
	}
	catch (error)
	{
		alert("The request could not be created: </br>" + error);
		console.error("failed to create request: " + error);
	}

}

Ajax.prototype.open = function()
{
	try
	{
		this.req.open(this.method, this.url, this.type);
	}
	catch (error)
	{
		console.error("failed to open request: " + error);
	}
}

Ajax.prototype.send = function()
{
	var data = this.data || null;
	try
	{
		this.req.send(data);
	}
	catch (error)
	{
		console.error("failed to send request: " + error);
	}
}


// End ATVUtils
// ***************************************************



FlowManager = function()
{
};

FlowManager.prototype.setTargetPage = function(pageId)
{
	atv.localStorage[TARGET_PAGEID_KEY] = pageId;
	console.debug("flowManager.setTargetPage() atv.localStorage[TARGET_PAGEID_KEY] " + atv.localStorage[TARGET_PAGEID_KEY]);
}

FlowManager.prototype.clearTargetPage = function()
{
	atv.localStorage.rem
2000
oveItem(TARGET_PAGEID_KEY);
	console.debug("flowManager.clearTargetPage() atv.localStorage[TARGET_PAGEID_KEY] " + atv.localStorage[TARGET_PAGEID_KEY]);
}

FlowManager.prototype.startPurchaseFlow = function()
{
	atv.localStorage[PURCHASE_FLOW_KEY] = "true";
	console.debug("flowManager.startPurchaseFlow() atv.localStorage[PURCHASE_FLOW_KEY] " + atv.localStorage[PURCHASE_FLOW_KEY]);
}

FlowManager.prototype.endPurchaseFlow = function()
{
	atv.localStorage.removeItem(PURCHASE_FLOW_KEY);
	atv.localStorage.removeItem(PURCHASE_PRODUCT_ID_KEY);
	console.debug("flowManager.endPurchaseFlow() atv.localStorage[PURCHASE_FLOW_KEY] " + atv.localStorage[PURCHASE_FLOW_KEY]);
}

FlowManager.prototype.inPurchaseFlow = function()
{
	console.debug("flowManager.inPurchaseFlow() atv.localStorage[PURCHASE_FLOW_KEY] " + atv.localStorage[PURCHASE_FLOW_KEY]);
	return (atv.localStorage[PURCHASE_FLOW_KEY] == "true");
}

FlowManager.prototype.inPurchaseFlow = function()
{
	console.debug("flowManager.inPurchaseFlow() atv.localStorage[PURCHASE_FLOW_KEY] " + atv.localStorage[PURCHASE_FLOW_KEY]);
	return (atv.localStorage[PURCHASE_FLOW_KEY] == "true");
}

FlowManager.prototype.inMediaFlow = function()
{
	console.debug("flowManager.inMediaFlow() atv.localStorage[MEDIA_META_KEY] " + atv.localStorage[MEDIA_META_KEY]);
	return atv.localStorage.getItem(MEDIA_META_KEY) != null && atv.localStorage.getItem(MEDIA_META_REQUEST_PARAMS_KEY) != null;
}

FlowManager.prototype.endMediaFlow = function()
{
	atv.localStorage.removeItem(MEDIA_META_KEY);
	atv.localStorage.removeItem(MEDIA_META_REQUEST_PARAMS_KEY);
	console.debug("flowManager.endMediaFlow() atv.localStorage[MEDIA_META_KEY] " + atv.localStorage[MEDIA_META_KEY]);
}

var flowManager = new FlowManager;

console.debug("atvutils.js flowManager created");

console.log("~~~~~ MLB.TV service.js START");

var PRODUCT_LIST = {
	"IOSATBAT2012GDATVMON" : {
		"image" : "monthlyPremium.jpg",
		"period" : "month"
	}, 
	"IOSMLBTVYEARLY2012" : {
		"image" : "monthlyPremium.jpg",
		"period" : "year"
	}
};

var MLBTV_FEATURE_CONTEXT = "MLBTV2012.INAPPPURCHASE";
var MLBTV_FEATURE_NAME = "MLBALL";

var IOS_IDENTITY_POINT_KEY = "ios.identityPointId";
var IOS_FINGERPRINT_KEY = "ios.fingerprint";
var USERNAME_KEY = "username";
var EMAIL_IDENTITY_POINT_KEY = "email.identityPointId";
var EMAIL_FINGERPRINT_KEY = "email.fingerprint";
var BEST_IDENTITY_POINT_KEY = "best.identityPointId";
var BEST_FINGERPRINT_KEY = "best.fingerprint";
var EMAIL_LINKED_KEY = "email.linked";
var AUTHORIZED_KEY = "authorized";
var SHOW_LINK_STATUS = "showLinkStatus";
var MEDIA_META_KEY = "media.meta";
var MEDIA_META_REQUEST_PARAMS_KEY = "media.meta.requestParams";
var OFFER_URI_AFTER_AUTH_KEY = "offer.uriToLoadAfterAuth";
var OFFER_PURCHASE_FUNCTION_KEY = "offer.purchaseFunction";
var OFFER_SWAP_PAGE_KEY = "offer.swapPage";

ServiceClient = function()
{
};

ServiceClient.prototype.login = function(username, password, success, error)
{
	var url = config.bamServiceBase + "/pubajaxws/services/IdentityPointService";
	var identifyBody = "<soapenv:Envelope xmlns:soapenv=\"http://schemas.xmlsoap.org/soap/envelope/\" xmlns:ns=\"http://services.bamnetworks.com/registration/types/1.0\"><soapenv:Body><ns:identityPoint_identify_request><ns:identification type=\"email-password\"> \
			<ns:email><ns:address>" + username + "</ns:address></ns:email> \
			<ns:password>" + password + "</ns:password> \
		</ns:identification></ns:identityPoint_identify_request></soapenv:Body></soapenv:Envelope>";
	var headers = {
		"SOAPAction" : "http://services.bamnetworks.com/registration/identityPoint/identify",
		"Content-Type" : "text/xml"
	};

	console.log('Trying to authenticate with ' + url);

	atvutils.makeRequest(url, "POST", headers, identifyBody, function(document)
	{
		var identityPointId, fingerprint;
		var statusCode = (document && document.rootElement) ? document.rootElement.childElements[1].getElementByName("identityPoint_identify_response").getElementByName("status").getElementByName("code").textContent : -1;
		console.log('status code is ' + statusCode);

		var errorMessage;
		if (statusCode == 1)
		{
			var identification = document.rootElement.childElements[1].getElementByName("identityPoint_identify_response").getElementByName("identification");
			identityPointId = identification.getElementByName("id").textContent;
			fingerprint = identification.getElementByName("fingerprint").textContent;
		}
		else if (statusCode == -1000)
		{
			errorMessage = "The account name or password was incorrect. Please try again.";
		}
		else
		{
			errorMessage = "MLB.com is curently undergoing maintenance. Please try again.";
		}

		if (identityPointId && fingerprint)
		{
			atv.localStorage[USERNAME_KEY] = username;
			atv.localStorage[EMAIL_IDENTITY_POINT_KEY] = identityPointId;
			atv.localStorage[EMAIL_FINGERPRINT_KEY] = fingerprint;

			if (!atv.localStorage[BEST_IDENTITY_POINT_KEY] && !atv.localStorage[BEST_IDENTITY_POINT_KEY])
			{
				console.debug("saving the email as the best identity point id");
				atv.localStorage[BEST_IDENTITY_POINT_KEY] = identityPointId;
				atv.localStorage[BEST_FINGERPRINT_KEY] = fingerprint;
			}
			console.debug("best identity point id: " + atv.localStorage[BEST_IDENTITY_POINT_KEY]);

			console.debug("determine whether the user is authorized " + serviceClient.loadUserAuthorized);
			serviceClient.loadUserAuthorized(function()
			{
				if (success)
					success();
			});
		}
		else
		{
			if (error)
				error(errorMessage);
			console.log('error message ' + errorMessage);
		}
	});
}

ServiceClient.prototype.loadUserAuthorized = function(callback)
{
	var url = config.bamServiceBase + "/pubajaxws/services/FeatureService";
	var body = "<soapenv:Envelope xmlns:soapenv=\"http://schemas.xmlsoap.org/soap/envelope/\" xmlns:ns=\"http://services.bamnetworks.com/registration/types/1.6\">\
			<soapenv:Body>\
				<ns:feature_findEntitled_request>\
					<ns:identification type=\"fingerprint\">\
						<ns:id>" + atv.localStorage[BEST_IDENTITY_POINT_KEY] + "</ns:id>\
						<ns:fingerprint>" + atv.localStorage[BEST_FINGERPRINT_KEY] + "</ns:fingerprint>\
					</ns:identification>\
					<ns:featureContextName>" + MLBTV_FEATURE_CONTEXT + "</ns:featureContextName>\
				</ns:feature_findEntitled_request>\
			</soapenv:Body>\
		</soapenv:Envelope>";

	var headers = {
		"SOAPAction" : "http://services.bamnetworks.com/registration/feature/findEntitledFeatures",
		"Content-Type" : "text/xml"
	};

	console.debug('loadUserAuthorized() Feature find entitlement request body ' + body);
	console.debug('loadUserAuthorized() Finding entitled features with ' + url);

	atvutils.makeRequest(url, "POST", headers, body, function(document, text)
	{
		console.debug("loadUserAuthorized() Feature.findEntitled response " + text);
		var authorized = (text.indexOf(MLBTV_FEATURE_NAME) > -1);
		console.debug("loadUserAuthorized() authorized " + authorized);
		atv.localStorage[AUTHORIZED_KEY] = authorized.toString();
		console.debug("loadUserAuthorized() set atv.localStorage[AUTHORIZED_KEY] to " + atv.localStorage[AUTHORIZED_KEY]);
		callback(authorized);
	});
}

ServiceClient.prototype.addRestoreObserver = function(successCallback, errorCallback)
{
	// Setup a transaction observer to see when transactions are restored and when new payments are made.

	var transReceived = [];
	
	var restoreObserver = {
		updatedTransactions : function(transactions)
		{
			console.debug("addRestoreObserver() updatedTransactions " + (transactions) ? transactions.length : null);

			if (transactions)
			{
				for ( var i = 0; i < transactions.length; ++i)
				{
					transaction = transactions[i];
					if (transaction.transactionState == atv.SKPaymentTransactionStateRestored)
					{
						console.debug("addRestoreObserver() Found restored transaction: " + GetLogStringForTransaction(transaction) + "\n---> Original Transaction: " + GetLogStringForTransaction(transaction.originalTransaction));
						
						// add
						transReceived.push(transaction);
						
						// finish the transaction
						atv.SKDefaultPaymentQueue.finishTransaction(transaction);
					}
				}
			}
			console.debug("addRestoreObserver() updatedTransactions " + transact
2000
ions.length);
		},

		restoreCompletedTransactionsFailedWithError : function(error)
		{
			console.debug("addRestoreObserver() restoreCompletedTransactionsFailedWithError start " + error + " code: " + error.code);
			
			atv.SKDefaultPaymentQueue.removeTransactionObserver(this);
			if (typeof (errorCallback) == "function")
				errorCallback(error);
			
			console.debug("addRestoreObserver() restoreCompletedTransactionsFailedWithError end " + error + " code: " + error.code);
		},

		restoreCompletedTransactionsFinished : function()
		{
			console.debug("addRestoreObserver() restoreCompletedTransactionsFinished start");
			
			atv.SKDefaultPaymentQueue.removeTransactionObserver(this);
			
			console.log("addRestoreObserver() transReceived " + transReceived);
			if (typeof (successCallback) == "function")
			{
				successCallback(transReceived);
			}	
			
			console.debug("addRestoreObserver() restoreCompletedTransactionsFinished end");
		}
	};

	atv.SKDefaultPaymentQueue.addTransactionObserver(restoreObserver);
	return restoreObserver;
}

function GetLogStringForTransaction(t)
{
	var result = "Transaction: " + "transactionIdentifier=" + ((t.transactionIdentifier) ? t.transactionIdentifier : "") + ", transactionState=" + ((t.transactionState) ? t.transactionState : "") + ", transactionReceipt=" + ((t.transactionReceipt) ? t.transactionReceipt : "") + ", originalTransaction= " + ((t.originalTransaction) ? t.originalTransaction : "") + ", transactionDate=" + ((t.transactionDate) ? t.transactionDate : "");

	if (t.payment)
	{
		result += ", payment.productIdentifier=" + t.payment.productIdentifier + ", payment.quantity=" + t.payment.quantity;
	}

	if (t.error)
	{
		result += ", error.domain=" + t.error.domain + ", error.code=" + t.error.code + ", error.userInfo=" + t.error.userInfo;
	}

	return result;
}

ServiceClient.prototype.doRequestProduct = function(productIds, successCallback, errorCallback)
{
	console.debug("doRequestProduct() productIds " + productIds);
	// make another products request to get product information
	var request = new atv.SKProductsRequest(productIds);

	request.onRequestDidFinish = function()
	{
		console.debug("doRequestProduct() Products request succeeded");
		if (typeof (successCallback) == "function")
			successCallback();
	};

	request.onRequestDidFailWithError = function(error)
	{
		console.error("doRequestProduct() Products request failed. " + ErrorToDebugString(error));
		if (typeof (errorCallback) == "function")
			errorCallback(error);
	};

	request.onProductsRequestDidReceiveResponse = function(productsResponse)
	{
		console.log("doRequestProduct() Products request received response " + productsResponse);
		console.debug("There are " + productsResponse.products.length + " offers.");

		if (typeof (successCallback) == "function")
			successCallback(productsResponse);
	};

	console.debug("Calling doRequestProduct() start");
	request.start();
}

ServiceClient.prototype.startPurchase = function()
{
	var productID = atv.localStorage["purchase.productId"];

	if (!productID)
	{
		console.debug("startPurchase(): not making a purchase because productID was not provided");
	}
	else
	{
		// make another products request to get product information
		var request = new atv.SKProductsRequest([ productID ]);

		request.onRequestDidFailWithError = function(error)
		{
			console.error("startPurchase() Products request failed. " + ErrorToDebugString(error));
		};

		request.onProductsRequestDidReceiveResponse = function(productsResponse)
		{
			console.log("startPurchase() Products request received response " + productsResponse);
			console.debug("There are " + productsResponse.products.length + " offers.");

			if (productsResponse && productsResponse.products && productsResponse.products.length == 1)
			{
				var product = productsResponse.products[0];
				console.debug("Found available product for ID " + product);
				var payment = {
					product : product,
					quantity : 1
				};
				atv.SKDefaultPaymentQueue.addPayment(payment);
			}
		};

		console.debug("Calling doRequestProduct() start");
		request.start();
	}
}

ServiceClient.prototype.addPurchaseObserver = function(successCallback, errorCallback)
{
	console.debug("addPurchaseObserver(): Purchase product with identifier " + productID);

	var productID = atv.localStorage["purchase.productId"];

	var observer = {
		updatedTransactions : function(transactions)
		{
			console.debug("addPurchaseObserver(): purchase.updatedTransactions: " + transactions.length);

			for ( var i = 0; i < transactions.length; ++i)
			{
				var transaction = transactions[i];

				console.debug("addPurchaseObserver(): Processing transaction " + i + ": " + GetLogStringForTransaction(transaction));

				if (transaction.payment && transaction.payment.productIdentifier == productID)
				{
					console.debug("addPurchaseObserver(): transaction is for product ID " + productID);

					// This is the product ID this transaction observer was created for.
					switch (transaction.transactionState)
					{
					case atv.SKPaymentTransactionStatePurchasing:
						console.debug("addPurchaseObserver(): SKPaymentTransactionStatePurchasing");
						break;
					case atv.SKPaymentTransactionStateRestored:
					case atv.SKPaymentTransactionStatePurchased:
						var reason = transaction.transactionState == atv.SKPaymentTransactionStatePurchased ? "Purchased" : "Restored";
						console.debug("addPurchaseObserver(): SKPaymentTransactionStatePurchased: Purchase Complete!!! Reason: " + reason);
						atv.SKDefaultPaymentQueue.finishTransaction(transaction);
						atv.SKDefaultPaymentQueue.removeTransactionObserver(this);
						if (typeof (successCallback) == "function")
							successCallback(transaction);
						break;
					case atv.SKPaymentTransactionStateFailed:
						console.debug("addPurchaseObserver(): SKPaymentTransactionStateFailed. " + ErrorToDebugString(transaction.error));
						atv.SKDefaultPaymentQueue.finishTransaction(transaction);
						atv.SKDefaultPaymentQueue.removeTransactionObserver(this);
						if (typeof (errorCallback) == "function")
							errorCallback(transaction);
						break;
					default:
						if (typeof (errorCallback) == "function")
							errorCallback(transaction);
						console.error("addPurchaseObserver(): Unknown transaction state: " + transaction.transactionState);
						break;
					}
				}
			}
		}
	}

	atv.SKDefaultPaymentQueue.addTransactionObserver(observer);
	return observer;
}

ServiceClient.prototype.startMediaFlow = function(mediaMeta)
{
	console.debug("startMediaFlow() mediaMeta " + mediaMeta);

	if (mediaMeta)
	{
		// set media meta to the local storage
		atv.localStorage[MEDIA_META_KEY] = JSON.stringify(mediaMeta);

		mediaMeta.title = mediaMeta.awayTeamCity + " " + mediaMeta.awayTeamName + " vs " + mediaMeta.homeTeamCity + " " + mediaMeta.homeTeamName;
		mediaMeta.description = mediaMeta.feed;
		if (mediaMeta.awayTeamId && mediaMeta.homeTeamId)
		{
			mediaMeta.imageUrl = config.imageBase + "/recapThumbs/" + mediaMeta.awayTeamId + "_v_" + mediaMeta.homeTeamId + ".jpg";
		}

		// set the session key from the local storage
		mediaMeta.sessionKey = atv.localStorage["sessionKey"];

		var mediaMetaRequestParams = "";
		mediaMetaRequestParams += '&identityPointId=' + ((atv.localStorage[BEST_IDENTITY_POINT_KEY]) ? atv.localStorage[BEST_IDENTITY_POINT_KEY] : 'IPID');
		mediaMetaRequestParams += '&fingerprint=' + ((atv.localStorage[BEST_FINGERPRINT_KEY]) ? atv.localStorage[BEST_FINGERPRINT_KEY] : 'FINGERPRINT');
		mediaMetaRequestParams += "&contentId=" + mediaMeta.contentId;
		mediaMetaRequestParams += '&sessionKey=' + ((mediaMeta.sessionKey) ? encodeURIComponent(mediaMeta.sessionKey) : "SESSION_KEY");
		mediaMetaRequestParams += '&calendarEventId=' + mediaMeta.calendarEventId;
		mediaMetaRequestParams += "&audioContentId=" + ((mediaMeta.audioContentId) ? mediaMeta.audioContentId.replace(/\s/g, ',') : "");
		mediaMetaRequestParams += '&day=' + mediaMeta.day
		mediaMetaRequestParams += '&month=' + mediaMeta.month;
		mediaMetaRequestParams += '&year=' + mediaMeta.year;
		mediaMetaRequestParams += "&feedType=" + mediaMeta.feedType;
		mediaMetaRequestParams += "&playFromBeginni
2000
ng=" + mediaMeta.playFromBeginning;
		mediaMetaRequestParams += "&title=" + encodeURIComponent(mediaMeta.title);
		mediaMetaRequestParams += "&description=" + encodeURIComponent(mediaMeta.description);
		mediaMetaRequestParams += (mediaMeta.imageUrl) ? ("&imageUrl=" + encodeURIComponent(mediaMeta.imageUrl)) : "";

		// save in local storage (should be in session storage but session storage is not yet supported)
		atv.localStorage[MEDIA_META_KEY] = JSON.stringify(mediaMeta);
		atv.localStorage[MEDIA_META_REQUEST_PARAMS_KEY] = mediaMetaRequestParams;
		console.debug("storing into local storage mediaMetaRequestParams " + atv.localStorage[MEDIA_META_REQUEST_PARAMS_KEY]);
	}
}

ServiceClient.prototype.loadMediaPage = function(callback)
{
	if (atv.localStorage[MEDIA_META_REQUEST_PARAMS_KEY])
	{
		var mediaProxyUrl = config.appletvBase + "/views/MediaServiceProxy?";
		// mediaProxyUrl += '&identityPointId=' + atv.localStorage[BEST_IDENTITY_POINT_KEY];
		// mediaProxyUrl += '&fingerprint=' + encodeURIComponent(atv.localStorage[BEST_FINGERPRINT_KEY]);
		mediaProxyUrl += atv.localStorage[MEDIA_META_REQUEST_PARAMS_KEY];
		console.debug("loadMediaPage mediaProxyUrl " + mediaProxyUrl);

		// make request
		atvutils.getRequest(mediaProxyUrl, callback);
	}
	else if (callback)
	{
		callback();
	}
}

function applyOffer(document)
{
	console.debug("applyOffer() atv.localStorage[PURCHASE_FLOW_KEY] " + atv.localStorage[PURCHASE_FLOW_KEY]);
	console.debug("applyOffer() document " + document);
	var mlbSignIn = (document) ? document.getElementById("mlbSignIn") : null;
	if (mlbSignIn)
	{
		var label = mlbSignIn.getElementByName("label");
		console.debug("applyOffer() atv.localStorage[EMAIL_IDENTITY_POINT_KEY] " + atv.localStorage[EMAIL_FINGERPRINT_KEY]);
		var signedIn = atv.localStorage[EMAIL_IDENTITY_POINT_KEY] != null && atv.localStorage[EMAIL_FINGERPRINT_KEY] != null;
		console.debug("applyOffer() signedIn " + signedIn);

		var uriToLoadAfterAuth = atv.localStorage[OFFER_URI_AFTER_AUTH_KEY];

		if (signedIn)
		{
			label.removeFromParent();
			label = atv.parseXML("<label>Sign In with different user</label>").rootElement;
			label.removeFromParent();
			mlbSignIn.appendChild(label);

			mlbSignIn.setAttribute("onSelect", "atv.logout();atvutils.loadAndSwapURL('" + config.appletvBase + "/views/Login?urlToLoadAfterAuth=" + uriToLoadAfterAuth + "');");
			mlbSignIn.setAttribute("onPlay", "atv.logout();atvutils.loadAndSwapURL('" + config.appletvBase + "/views/Login?urlToLoadAfterAuth=" + uriToLoadAfterAuth + "');");
		}
		else
		{
			console.debug("applyOffer() uriToLoadAfterAuth " + uriToLoadAfterAuth);
			label.textContent = "MLB.TV Premium Subscriber Login";
			console.debug("applyOffer() mlbSignIn.getAttribute('onSelect') " + mlbSignIn.getAttribute('onSelect'));
			mlbSignIn.setAttribute("onSelect", "atvutils.loadAndSwapURL('" + config.appletvBase + "/views/Login?urlToLoadAfterAuth=" + uriToLoadAfterAuth + "');");
			mlbSignIn.setAttribute("onPlay", "atvutils.loadAndSwapURL('" + config.appletvBase + "/views/Login?urlToLoadAfterAuth=" + uriToLoadAfterAuth + "');");
			console.debug("applyOffer() mlbSignIn.getAttribute('onSelect') " + mlbSignIn.getAttribute('onSelect'));
		}
	}
}

ServiceClient.prototype.loadOfferPage = function(newPage)
{
	console.debug("loadOfferPage() enter");

	newPage
	
	// set purchase flow to true
	flowManager.startPurchaseFlow();

	var swap = (atv.localStorage[OFFER_SWAP_PAGE_KEY] == "true");
	console.debug("loadOfferPage() atv.localStorage[OFFER_SWAP_PAGE_KEY] " + atv.localStorage[OFFER_SWAP_PAGE_KEY]);
	console.debug("loadOfferPage() swap " + swap);

	// Use StoreKit to get the list of product identifiers.
	var request = new atv.SKProductsRequest(Object.keys(PRODUCT_LIST));
	request.onRequestDidFailWithError = function(error)
	{
		console.error("Products request failed. " + ErrorToDebugString(error));
		var xml = atvutils.makeOkDialog("MLB.TV", "MLB.TV Products could not be found. Please check again at a later time.");
		if (swap)
			newPage.swapXML(xml)
		else
			newPage.loadXML(xml);
	};
	request.onProductsRequestDidReceiveResponse = function(productsResponse)
	{
		// success
		console.debug("loadOfferPage() Products request received response " + productsResponse);
		console.debug("loadOfferPage() There are " + productsResponse.products.length + " offers.");

		// add image
		console.debug("loadOfferPage() checking for image location");
		for ( var i = 0; i < productsResponse.products.length; i++)
		{
			var product = productsResponse.products[i];
			console.debug("loadOfferPage() product.productIdentifier " + product.productIdentifier);
			if (PRODUCT_LIST[product.productIdentifier] != null)
			{
				product.image = PRODUCT_LIST[product.productIdentifier].image;
				console.debug("loadOfferPage() product.image " + product.image);

				product.period = PRODUCT_LIST[product.productIdentifier].period;
				console.debug("loadOfferPage() product.period " + product.period);
			}
		}

		// This page will display the offers, and call purchaseProduct below with the productId to purchase in an onSelect handler.
		var offerBody = "";
		offerBody += "productMeta=" + JSON.stringify(productsResponse);
		offerBody += "&purchaseFunction=" + atv.localStorage[OFFER_PURCHASE_FUNCTION_KEY];
		if (atv.localStorage[OFFER_URI_AFTER_AUTH_KEY])
		{
			offerBody += "&uriToLoadAfterAuth=" + atv.localStorage[OFFER_URI_AFTER_AUTH_KEY];
		}

		console.debug("loadOfferPage() offerBody: " + offerBody);

		atvutils.postRequest(config.appletvBase + '/views/Offer', offerBody, function(xml)
		{
			try
			{
				console.debug("loadOfferPage() postRequest xml=" + xml);
				applyOffer(xml);
				if (swap)
					newPage.swapXML(xml)
				else
					newPage.loadXML(xml);
			}
			catch (e)
			{
				console.error("Caught exception for the Offer page " + e);
				var errorXML = atvutils.makeOkDialog("MLB.TV Error", "MLB.TV Products could not be found. Please check again at a later time.");
				if (swap)
					newPage.swapXML(errorXML)
				else
					newPage.loadXML(errorXML);
			}
		});
	};

	console.debug("Calling loadOfferPage() start");
	request.start();
}
ServiceClient.prototype.isMediaResponseUnauthorized = function(xml)
{
	return (xml) ? this.isMediaUnauthorized(this.getMediaStatusCode(xml)) : true;
}

ServiceClient.prototype.isMediaUnauthorized = function(mediaStatusCode)
{
	return "" == mediaStatusCode || "UNDETERMINED" == mediaStatusCode || "NOT_AUHTORIZED" == mediaStatusCode || "LOGIN_REQUIRED" == mediaStatusCode || "INVALID_USER_CREDENTIALS_STATUS" == mediaStatusCode;
}

ServiceClient.prototype.isMediaPlayback = function(mediaStatusCode)
{
	return "VIDEO_PLAYBACK" == mediaStatusCode || "AUDIO_PLAYBACK" == mediaStatusCode;
}

ServiceClient.prototype.getMediaStatusCode = function(xml)
{
	var mediaStatusCodeElem = (xml && xml.rootElement) ? xml.rootElement.getElementByTagName("mediaStatusCode") : null;
	console.debug("getMediaStatusCode() mediaStatusCodeElem " + mediaStatusCodeElem);
	var mediaStatusCode = (mediaStatusCodeElem != null) ? mediaStatusCodeElem.textContent : "UNDETERMINED";
	console.debug("getMediaStatusCode() mediaStatusCode " + mediaStatusCode);
	return mediaStatusCode;
}

ServiceClient.prototype.createFingerprintIdpt = function(ipid, fingerprint)
{
	return {
		"type" : "fingerprint",
		"id" : ipid,
		"fingerprint" : fingerprint
	};
}

ServiceClient.prototype.createIosIdentityPoint = function(transaction)
{
	return {
		"type" : "iosInAppPurchase",
		"receipt" : transaction.transactionReceipt
	};
}

ServiceClient.prototype.createLinkRequest = function(transactions)
{
	var identityPoints = [];

	// email-password fingerprint identity point
	if (atv.localStorage[EMAIL_FINGERPRINT_KEY] && atv.localStorage[EMAIL_IDENTITY_POINT_KEY])
	{
		identityPoints.push(this.createFingerprintIdpt(atv.localStorage[EMAIL_IDENTITY_POINT_KEY], atv.localStorage[EMAIL_FINGERPRINT_KEY]));
	}

	if (transactions)
	{
		console.debug("createLinkRequest() filtering out for unique original transactions");
		
		// filter out unique original transactions
		var originalTransMap = {};
		
		for (var t = 0; t < transactions.length; t++)
		{
			var currTrans = transactions[t];
			var origTrans =
2000
 currTrans.originalTransaction;
			var transId = (origTrans == null) ? currTrans.transactionIdentifier : origTrans.transactionIdentifier;  
			
			console.debug("createLinkRequest() transId " + transId);
			
			if (originalTransMap[transId] == null)
			{
				// transaction identity point
				identityPoints.push(this.createIosIdentityPoint(currTrans));
				
				originalTransMap[transId] = currTrans;
			}
		}
	}
	// best fingerprint identity point
	else if (atv.localStorage[BEST_FINGERPRINT_KEY] && atv.localStorage[BEST_IDENTITY_POINT_KEY])
	{
		identityPoints.push(this.createFingerprintIdpt(atv.localStorage[BEST_IDENTITY_POINT_KEY], atv.localStorage[BEST_FINGERPRINT_KEY]));
	}

	var linkRequest = null;
	if (identityPoints.length > 0)
	{
		// link request
		linkRequest = {
			"identityPoints" : identityPoints,
			"namespace" : "mlb",
			"key" : "h-Qupr_3eY"
		};
	}

	console.debug("createLinkRequest() linkRequest " + JSON.stringify(linkRequest));

	return linkRequest;
}

ServiceClient.prototype.doLink = function(transactions, successCallback, errorCallback)
{
	var linkRequest = this.createLinkRequest(transactions);

	if (linkRequest == null)
	{
		if (typeof (successCallback) == "function")
			successCallback();

		console.debug("doLink() link not called since nothing to link");
	}
	else
	{
		// link request
		var linkRequestBody = JSON.stringify(linkRequest);
		var req = new XMLHttpRequest();
		var url = config.mlbBase + "/mobile/LinkAll2.jsp";

		req.onreadystatechange = function()
		{
			try
			{
				if (req.readyState == 4)
				{
					console.log('link() Got link service http status code of ' + req.status);
					if (req.status == 200)
					{
						console.log('link() Response text is ' + req.responseText);
						var linkResponseJson = JSON.parse(req.responseText);

						if (linkResponseJson.status == 1)
						{
							console.debug("link() bestIdentityPointToUse: " + linkResponseJson.bestIdentityPointToUse);
							console.debug("link() messages: " + linkResponseJson.messages);

							if (linkResponseJson.bestIdentityPointToUse && linkResponseJson.bestIdentityPointToUse.id && linkResponseJson.bestIdentityPointToUse.fingerprint)
							{
								console.debug("link() saving the best identity point to use");
								atv.localStorage[BEST_IDENTITY_POINT_KEY] = linkResponseJson.bestIdentityPointToUse.id;
								atv.localStorage[BEST_FINGERPRINT_KEY] = linkResponseJson.bestIdentityPointToUse.fingerprint;
								console.debug("link() identityPointId " + atv.localStorage[BEST_IDENTITY_POINT_KEY]);

								if (linkResponseJson.bestIdentityPointToUse.id.toLowerCase().indexOf("ios") >= 0)
								{
									atv.localStorage[IOS_IDENTITY_POINT_KEY] = linkResponseJson.bestIdentityPointToUse.id;
									atv.localStorage[IOS_FINGERPRINT_KEY] = linkResponseJson.bestIdentityPointToUse.fingerprint;
								}
							}

							var linked = false;// , auth = false;
							for ( var m = 0; m < linkResponseJson.messages.length; m++)
							{
								var msg = linkResponseJson.messages[m];
								console.debug("doLink() msg " + msg);
								if (msg == "linked")
								{
									linked = true;
								}
							}
							linked = linked && atv.localStorage[EMAIL_IDENTITY_POINT_KEY] != null && atv.localStorage[EMAIL_FINGERPRINT_KEY] != null;
							// atv.localStorage[EMAIL_LINKED_KEY] = linked.toString();
							// atv.localStorage[AUTHORIZED_KEY] = auth.toString();

							// console.debug("doLink() atv.localStorage[EMAIL_LINKED_KEY] " + atv.localStorage[EMAIL_LINKED_KEY];
							console.debug("atv.localStorage[AUTHORIZED_KEY] " + atv.localStorage[AUTHORIZED_KEY]);

							if (typeof (successCallback) == "function")
								successCallback();

							console.log("doLink() getAllResponseHeaders: " + this.getAllResponseHeaders());
						}
						else
						{

							if (typeof (errorCallback) == "function")
								errorCallback();
							console.error("link response json has an invalid status code: " + linkResponseJson.status);
						}
					}
				}
			}
			catch (e)
			{
				// Specify a copyedited string because this will be displayed to
				// the user.
				console.error('Caught exception while processing request. Aborting. Exception: ' + e);
				if (typeof (errorCallback) == "function")
					errorCallback(e);

				req.abort();
			}
		}

		req.open("POST", url, true);
		req.setRequestHeader("Content-Type", "text/xml");

		console.log('Trying to link with ' + url);
		console.log("Link Service POST body is " + linkRequestBody);
		req.send(linkRequestBody);
	}
}

var serviceClient = new ServiceClient;

console.log("~~~~~ MLB.TV service.js END");

console.log("MLB.TV application.js begin");

var BEST_IDENTITY_POINT_KEY = "best.identityPointId";
var BEST_FINGERPRINT_KEY = "best.fingerprint";
var IOS_IDENTITY_POINT_KEY = "ios.identityPointId";
var IOS_FINGERPRINT_KEY = "ios.fingerprint";
var EMAIL_IDENTITY_POINT_KEY = "email.identityPointId";
var EMAIL_FINGERPRINT_KEY = "email.fingerprint";
var USERNAME_KEY = "username";

var SHOW_SCORES_KEY = "showScores";
var FAVORITE_TEAMS_KEY = "favoriteTeamIds";

var MEDIA_META_KEY = "media.meta";
var MEDIA_META_REQUEST_PARAMS_KEY = "media.meta.requestParams";
var SESSION_KEY = "sessionKey";

var PURCHASE_PRODUCT_ID_KEY = "purchase.productId";

var EMAIL_LINKED_KEY = "email.linked";
var AUTHORIZED_KEY = "authorized";

var PURCHASE_FLOW_KEY = "flow.purchase";
var TARGET_PAGEID_KEY = "targetPage";
var OFFER_PAGE_ID = "com.bamnetworks.appletv.offer";


atv.config = 
{
    // If doesJavaScriptLoadRoot is true, then atv.onAppEntry must load the root URL; otherwise, root-url from the bag is used.
    doesJavaScriptLoadRoot: true
};

// atv.onGenerateRequest
// Called when Apple TV is about to make a URL request. Use this method to make
// changes to the URL. For example, use it
// to decorate the URL with auth tokens or signatures.
atv.onGenerateRequest = function (request) {
    console.log('application.js atv.onGenerateRequest() oldUrl: ' + request.url);

    console.debug("main.js atv.onGenerateRequest() atv.localStorage[mediaMetaRequestParams] " + atv.localStorage[MEDIA_META_REQUEST_PARAMS_KEY]);
	if ( request.url.indexOf("encode(MEDIA_META_REQUEST_PARAMS)") > -1 && atv.localStorage[MEDIA_META_REQUEST_PARAMS_KEY])
	{
		request.url = request.url.replace("encode(MEDIA_META_REQUEST_PARAMS)", encodeURIComponent(atv.localStorage[MEDIA_META_REQUEST_PARAMS_KEY]));
	}
	if ( request.url.indexOf("MEDIA_META_REQUEST_PARAMS") > -1 && atv.localStorage[MEDIA_META_REQUEST_PARAMS_KEY])
	{
		console.debug("application.js atv.onGenerateRequest() atv.localStorage[mediaMetaRequestParams] " + atv.localStorage[MEDIA_META_REQUEST_PARAMS_KEY]);
		request.url = request.url.replace("MEDIA_META_REQUEST_PARAMS", atv.localStorage[MEDIA_META_REQUEST_PARAMS_KEY]);
	}
	if ( request.url.indexOf("IPID") > -1 && atv.localStorage[BEST_IDENTITY_POINT_KEY])
	{
		request.url = request.url.replace("IPID", atv.localStorage[BEST_IDENTITY_POINT_KEY]);
	}
	if ( request.url.indexOf("FINGERPRINT") > -1 && atv.localStorage[BEST_FINGERPRINT_KEY])
	{
		request.url = request.url.replace("FINGERPRINT", encodeURIComponent(atv.localStorage[BEST_FINGERPRINT_KEY]));
	}
	if ( request.url.indexOf("SESSION_KEY") > -1 )
	{
		var sessionKey = atv.localStorage[SESSION_KEY]; 
		console.log("sessionKey=" + sessionKey);
		request.url = request.url.replace("SESSION_KEY", (sessionKey) ? encodeURIComponent(sessionKey) : "");
	}
	console.debug("application.js atv.onGenerateRequest() atv.localStorage[EMAIL_IDENTITY_POINT_KEY] " + atv.localStorage[EMAIL_IDENTITY_POINT_KEY]);
	if ( request.url.indexOf("encode(LINK_PROXY_REQUEST)") > -1 && atv.localStorage[EMAIL_IDENTITY_POINT_KEY] != null && atv.localStorage[EMAIL_FINGERPRINT_KEY] != null)
	{
		var linkRequest = serviceClient.createLinkRequest();
		console.debug("application.js atv.onGenerateRequest() linkRequest " + linkRequest);
		if (linkRequest != null)
		{
			// WARNING: encode twice because the xs:uri type does not suppot curly brackets so encode an extra time. The LinkProxy class will be responsible for url unencoding the json 
			request.url = request.url.replace("encode(LINK_PROXY_REQUEST)", encodeURIComponent(encodeURIComponent(J
2000
SON.stringify(linkRequest))));
		}
	}
	if ( request.url.indexOf("$$showScores") > -1 )
	{
		var showScores = atv.localStorage[SHOW_SCORES_KEY]; 
		console.log("showScores=" + showScores);
		request.url = request.url.replace("$$showScores", (showScores) ? showScores : "true");
	}
	if ( request.url.indexOf("AUTH_MESSAGE_CODE") > -1 )
	{
		var authorized = atv.localStorage[AUTHORIZED_KEY]; 
		console.debug("application.js AUTH_MESSAGE_CODE atv.onGenerateRequest() authorized " + authorized);
		request.url = request.url.replace("AUTH_MESSAGE_CODE", ((authorized == "true") ? "ALREADY_PURCHASED" : "NOT_AUHTORIZED"));
	}

	console.log('application.js atv.onGenerateRequest() newUrl: ' + request.url);
}

// atv.onAppEntry
// Called when you enter an application but before the root plist is requested.
// This method should not return until
// application initialization is complete. In other words, once this method has
// returned Apple TV will assume it can call
// into any other callback. For instance, after this returns, the next method
// likely to be called is atv.onGenerateRequest
// on the root plist.
atv.onAppEntry = function() {
	console.log('MLB.TV app entered');	
	atvutils.loadAndApplyURL(config.appletvBase + "/views/Main", applyMain)
}

function applyMain(document)
{
	console.log('applyMain ' + document);	

	// update logo image resolution
	updateImageResolution(document);
	
	// check items link is present in bag.plist
	checkItemsLink(document);
	
	// check valid subscription
	// (1) via iTunes on this box and iTunes account signed in. Receipt is present and stored on the box
	// (2) via Vendor and is signed in with Vendor account.
	var validSubscription = (atv.localStorage[IOS_IDENTITY_POINT_KEY] != null && atv.localStorage[IOS_FINGERPRINT_KEY] != null) || (atv.localStorage[AUTHORIZED_KEY] == "true" && atv.localStorage[EMAIL_IDENTITY_POINT_KEY] != null && atv.localStorage[EMAIL_FINGERPRINT_KEY] != null);
	
	console.log("applyMain() validSubscription " + validSubscription);
	console.log("applyMain() atv.localStorage[AUTHORIZED_KEY] " + atv.localStorage[AUTHORIZED_KEY]);
	if (validSubscription)
	{
		var subscribeMenu = document.getElementById("subscribeMlbTv");
		if (subscribeMenu)
		{
			subscribeMenu.removeFromParent();
		}
	}
	
	return document;
}

/**
 * Updates the logo according to the screen resolution
 * 
 * @param document
 * @returns
 */
function updateImageResolution(document)
{
	// update logos according to screen resolution
	var frame = atv.device.screenFrame; 
	console.debug("frame size: " + frame.height);
	if (frame.height == 1080) 
	{
		var images = document.rootElement.getElementsByTagName('image');
		for (var i = 0; i < images.length; i++) 
		{
			images[i].textContent = images[i].textContent.replace('720p', '1080p');
		}
	}	
	
	return document;
}


/**
 * Assumes the presence of a oneLineMenuItem with ID of 'itemsLink' placed in the menu by the vendor. When the placeholder is found the 'onSelect' attribute is set.
 */
function checkItemsLink(xml) {
	try
    {
		var itemsLinkElem = xml.getElementById('itemsLink');
		if (itemsLinkElem)
		{
			var itemsLinkUrl = atv.localStorage.getItem('itms-link');
			console.debug("itemsLinkUrl=" + itemsLinkUrl);
			if (itemsLinkUrl) 
			{
				itemsLinkElem.setAttribute('onSelect', "atv.loadURL('"+ itemsLinkUrl +"')");
			}
			else
			{
				console.debug("removing menu item: " + itemsLinkElem);
				itemsLinkElem.removeFromParent();
			}
		}
    }
    catch(error)
    {
        console.error("Caught exception checking the items link: " + error);
    }
	
	return xml;
}


// atv.onAppExit
// Called when the application exits. The application doesn't exit when the user
// goes to the main menu because the application
// is still required to display it's top shelf. Rather, the application exits
// when an application is entered, even
// if this application is the one that is entered. For example:
// 1. User enters this application: atv.onAppEntry called
// 2. User goes back to the main menu (nothing happens yet)
// 3. User enters this application: atv.onAppExit called, then atv.onAppEntry is
// called
atv.onAppExit = function() {
	console.log('MLB.TV onAppExit()');
	
	flowManager.endMediaFlow();
	flowManager.endPurchaseFlow();
	
	console.log("deleted session variables  mediaMeta and sessionKey " + atv.localStorage[PURCHASE_PRODUCT_ID_KEY] + " " + atv.localStorage[MEDIA_META_REQUEST_PARAMS_KEY] + " " + atv.localStorage[MEDIA_META_KEY] + " " + atv.localStorage[SESSION_KEY]);
	
	console.log('MLB.TV onAppExit() ' + ((typeof(config) != "undefined") ? config.env.indexOf('prod') : "" ));
	if (isQaEnv())
	{
		console.debug("Logging out..");
		atv.logout();
		atv.onLogout();
	}
}

function isQaEnv()
{
	return typeof(config) != "undefined" && config.env != null && config.env.indexOf('prod') == -1;
}

// atv.onPageLoad
// Called when a plist is loaded.
// id - The page identifier
atv.onPageLoad = function(id) {
	console.log("application.js atv.onPageLoad() id: " + id);
	
	// page loaded, reached target page 
	atv.localStorage.removeItem(TARGET_PAGEID_KEY); 
	
	atv._debugDumpControllerStack();
}

//atv.onPageUnload
atv.onPageUnload = function(id)
{
	console.debug("application.js atv.onPageUnload() id " + id);
	console.debug("application.js atv.onPageUnload() atv.localStorage[PURCHASE_FLOW_KEY] " + atv.localStorage[PURCHASE_FLOW_KEY]);
	console.debug("application.js atv.onPageUnload() atv.localStorage[BEST_IDENTITY_POINT_KEY] " +  atv.localStorage[BEST_IDENTITY_POINT_KEY]);
	console.debug("application.js atv.onPageUnload() undefined == null " +  (undefined == null));
	if (flowManager.inPurchaseFlow() && (("com.bamnetworks.appletv.authenticate" == id && atv.localStorage[BEST_IDENTITY_POINT_KEY] == null && atv.localStorage[BEST_FINGERPRINT_KEY] == null) || id.indexOf("Not Authorized") > -1 ))
	{
		console.debug("application.js atv.onPageUnload() showing offer page");
		
		atv.localStorage[TARGET_PAGEID_KEY] = OFFER_PAGE_ID;
		var newPage = new atv.ProxyDocument();
		newPage.show();
		serviceClient.loadOfferPage(newPage);
	}
}

// atv.onAuthenticate
// Called when the user needs to be authenticated. Some events that would call
// this are:
// - the user has explicitly tried to login via a sign-in-sign-out menu item
// - the server returned a 401 and silent authentication is occuring
// - non-silent authentication is occuring (because there are no credentials or
// silent auth failed)
//
// This method should not block. If it makes an XMLHttpRequest, it should do so
// asynchronously. When authentication is complete, you must notify
// Apple TV of success or failure by calling callback.success() or
// callback.failure(msg).
//
// Do not save the username or password in atv.localStorage or
// atv.sessionStorage; Apple TV will manage the user's credentials.
//
// username - The username to authenticate with
// password - The password to authenticate with
// callback - Called to indicate success or failure to Apple TV. Either
// callback.success() or callback.failure(msg) must be called
// in all situations.
//
atv.onAuthenticate = function (username, password, callback) {
	
	try
	{
		serviceClient.login(username, password, 
			function()
			{
				callback.success();
			}, 
			function(errorMessage)
			{
				callback.failure(errorMessage);
			}
		);
	}
	catch (e)
	{
        // Specify a copyedited string because this will be displayed to the
		// user.
		callback.failure('Caught exception authenticating user. Exception: ' + e);
	}
}

// atv.onLogout
atv.onLogout = function () {
	
	console.log('Notified that our account has logged out, clearing sessionStorage.');
	
	try
	{		
		// clear the user data
		atv.localStorage.removeItem(BEST_IDENTITY_POINT_KEY);
		atv.localStorage.removeItem(BEST_FINGERPRINT_KEY);
		atv.localStorage.removeItem(IOS_IDENTITY_POINT_KEY);
		atv.localStorage.removeItem(IOS_FINGERPRINT_KEY);
		atv.localStorage.removeItem(EMAIL_IDENTITY_POINT_KEY);
		atv.localStorage.removeItem(EMAIL_FINGERPRINT_KEY);
		atv.localStorage.removeItem(FAVORITE_TEAMS_KEY);
		atv.localStorage.removeItem(SHOW_SCORES_KEY);
		atv.localStorage.removeItem(SESSION_KEY);
		atv.localStorage.removeItem(EMAIL_LINKED_KEY);
		
1efc
atv.localStorage.removeItem(USERNAME_KEY);
		atv.localStorage.removeItem(AUTHORIZED_KEY);
		
		console.debug("onLogout(): atv.localStorage.removeItem(AUTHORIZED_KEY) " + atv.localStorage[AUTHORIZED_KEY]);
	}
	catch (e)
	{
		console.log('Caught exception trying to clear sessionStorage. Exception: ' + e);
	}
}

function logPlayerAssetAndEventGroups()
{
        console.log('Logging Player Asset ---------------------------------');
        
        // Test out asset and event groups on player
        var asset = atv.player.asset;
        if ( asset != null )
        {
				var title = asset.getElementByName("title");
	
                console.log('The current asset is ' + title.textContent);
    
                var eventGroups = atv.player.eventGroups;
                if ( eventGroups != null )
                {
                        console.log('There are ' + eventGroups.length + ' current event groups');
                        for( var i=0, len = eventGroups.length; i < len; ++i ) {
                                var group = eventGroups[i];
								var groupTitle = group.getElementByName("title");
                                console.log('event group title: ' + groupTitle.textContent);
                
                                var events = group.getElementsByName("event");
                                for( var j=0, eventLen=events.length; j < eventLen; ++j ) {
                                        var event = events[j];
										var eventTitle = event.getElementByName("title");
										console.log('event title: ' + eventTitle.textContent);
                                }
                        }
                }
        }
        
        console.log('END ---------------------------------');
}

var logTimer = null;

// atv.player.onStartBuffering
// Called when the playhead has moved to a new location (including the initial
// load) and buffering starts.
// playheadLocation - The location of the playhead in seconds from the beginning
atv.player.onStartBuffering = function(playheadLocation) {
	gDateBufferingStarted = new Date(); 
	console.log('onStartBuffering at location ' + playheadLocation + ' at this time: ' + gDateBufferingStarted);
//	logPlayerAssetAndEventGroups();
	console.log('end ---------------------');
}

// atv.player.onBufferSufficientToPlay
// Called when enough data have buffered to begin playing without interruption.
atv.player.onBufferSufficientToPlay = function() {
	var dateBufferBecameSufficientToPlay = new Date();
	var elapsed = dateBufferBecameSufficientToPlay - gDateBufferingStarted;
	console.log('onBufferSufficientToPlay: it took ' + elapsed + ' milliseconds to buffer enough data to start playback');
}

// atv.player.onStallDuringPlayback
// Called when there is a buffer underrun during normal speed video playback
// (i.e. not fast-forward or rewind).
atv.player.onStallDuringPlayback = function(playheadLocation) {
	var now = new Date();
	console.log("onStallDuringPlayback: stall occurred at location " + playheadLocation + " at this time: " + now);
}

// atv.player.onPlaybackError
// Called when an error occurred that terminated playback.
// debugMessage - A debug message for development and reporting purposes only.
// Not for display to the user.
atv.player.onPlaybackError = function (debugMessage) {
	// debugMessage is only intended for debugging purposes. Don't rely on
	// specific values.
	console.log('onPlaybackError: error message is ' + debugMessage);
}

// atv.player.onQualityOfServiceReport
// Called when a quality of service report is available.
atv.player.onQualityOfServiceReport = function(report) {
	console.log("QoS report is\n" + report);
	
	// accessLog and errorLog are not gaurenteed to be present, so check for
	// them before using.
	
	if ( 'accessLog' in report ) {
		console.log("Acces Log:\n" + report.accessLog + "\----------------------------\n");
	}
	
	if ( 'errorLog' in report ) {
		console.log("Error Log:\n" + report.errorLog + "\----------------------------\n");
	}
}

atv.player.playerStateChanged = function(newState, timeIntervalSec) {	
	/*
	 * state constants are: atv.player.states.FastForwarding atv.player.states.Loading atv.player.states.Paused atv.player.states.Playing atv.player.states.Rewinding atv.player.states.Stopped
	 */
	
	console.log("Player state changed to " + newState + " at this time " + timeIntervalSec);
}

// TODO - only show event callbacks example for media asset with a known
// asset-id, for now, control for all player items via this flag
SHOW_EVENT_EXAMPLE = false;

// atv.player.playerWillSeekToTime
// Called after the user stops fast forwarding, rewinding, or skipping in the
// stream
// timeIntervalSec - The elapsed time, in seconds, where the user stopped
// seeking in the stream
// Returns: the adjusted time offset for the player. If no adjustment is needed,
// return timeIntervalSec.
// Clients can check whether the playback is within an unskippable event and
// reset the playhead to the start of that event.
atv.player.playerWillSeekToTime = function(timeIntervalSec) {
	
	console.log('playerWillSeekToTime: ' + timeIntervalSec);
	
	if (!SHOW_EVENT_EXAMPLE) {
		return timeIntervalSec;
	}
	
	// TODO - replace example using event group config
	// Example of event from offset 10-15 sec that is unskippable. If the user
	// seeks within or past, reset to beginning of event
	if (timeIntervalSec >= 10 && !atv.sessionStorage["already-watched-event"]) {
		if (timeIntervalSec > 15) {
			atv.sessionStorage["resume-time"] = timeIntervalSec;
		}
		atv.sessionStorage["in-event"] = true;
		return 10;
	}
	return timeIntervalSec;
}

// atv.player.playerShouldHandleEvent
// Called to check if the given event should be allowed given the current player
// time and state.
// event - One of: atv.player.events.FFwd, atv.player.events.Pause,
// atv.player.events.Play, atv.player.events.Rew, atv.player.events.SkipBack,
// atv.player.events.SkipFwd
// timeIntervalSec - The elapsed time, in seconds, where the event would be
// fired
// Returns: true if the event should be allowed, false otherwise
atv.player.playerShouldHandleEvent = function(event, timeIntervalSec) {
	
	console.log('playerShouldHandleEvent: ' + event + ', timeInterval: ' + timeIntervalSec);

	if (!SHOW_EVENT_EXAMPLE) {
		return true;
	}
	
	// TODO - replace example using event group config
	// Disallow all player events while in the sample event
	if (timeIntervalSec >= 10 && timeIntervalSec < 15 && !atv.sessionStorage["already-watched-event"]) {
		return false;
	}
	
	return true;
}

// atv.player.playerTimeDidChange
// Called whenever the playhead time changes for the currently playing asset.
// timeIntervalSec - The elapsed time, in seconds, of the current playhead
// position
atv.player.playerTimeDidChange = function(timeIntervalSec) {
	
	var netTime = atv.player.convertGrossToNetTime(timeIntervalSec);
	var andBackToGross = atv.player.convertNetToGrossTime(netTime);
	// console.log('playerTimeDidChange: ' + timeIntervalSec + " net time " +
	// netTime + " and back to gross " + andBackToGross);
	
	if (!SHOW_EVENT_EXAMPLE) {
		return;
	}
	
	// TODO - replace example using event group config
	// If we are currently in the sample event, and are about to exit, clear our
	// flag, mark that the event was watched, and resume if needed
	if (atv.sessionStorage["in-event"] && timeIntervalSec > 15) {
		atv.sessionStorage["in-event"] = false;
		atv.sessionStorage["already-watched-event"] = true;
		if (atv.sessionStorage["resume-time"]) {
			atv.player.playerSeekToTime(atv.sessionStorage["resume-time"]);
			atv.sessionStorage.removeItem("resume-time");
		}
	}
}

// atv.player.didStopPlaying
// Called at some point after playback stops. Use this to to per-playback
// teardown or reporting.
atv.player.didStopPlaying = function() {
	console.log('didStopPlaying');
    
	atv.clearInterval(logTimer);
	logTimer = null;
}

console.log("MLB.TV application.js end");

0

HTTP/1.1 200 OK
Server: Oracle-iPlanet-Web-Server/7.0
Date: Wed, 08 Aug 2012 16:14:20 GMT
Content-type: text/html
Cache-control: max-age=300
Edge-control: max-age=300
Last-modified: Wed, 08 Aug 2012 16:14:20 GMT
Content-type: application/xml
Transfer-encoding: chunked

127d
<?xml version="1.0" encoding="UTF-8"?>
<atv>
	<head>
		<script src="http://lws.mlb.com/appletvv2/views/config.js" />
		<script src="http://lws.mlb.com/appletvv2/js/atvutils.js" />
		<script src="http://lws.mlb.com/appletvv2/js/service.js" />
		<script src="http://lws.mlb.com/appletvv2/js/main.js" />
	</head>
	<body>
		<listWithPreview id="com.bamnetworks.appletv.main" volatile="true" onVolatileReload="handleMainVoltileReload();">
			<header>
				<simpleHeader accessibilityLabel="MLB.TV Settings">
					<image required="true">http://mlb.mlb.com/mlb/images/flagstaff/logo/720p/Logo.png</image>
				</simpleHeader>
			</header>
			<preview>
				<paradePreview shuffle="true">
					<image>http://mlb.mlb.com/mlb/images/flagstaff/600x600/118.png</image>
					<image>http://mlb.mlb.com/mlb/images/flagstaff/600x600/145.png</image>
					<image>http://mlb.mlb.com/mlb/images/flagstaff/600x600/142.png</image>
					<image>http://mlb.mlb.com/mlb/images/flagstaff/600x600/116.png</image>
					<image>http://mlb.mlb.com/mlb/images/flagstaff/600x600/114.png</image>
					<image>http://mlb.mlb.com/mlb/images/flagstaff/600x600/158.png</image>
					<image>http://mlb.mlb.com/mlb/images/flagstaff/600x600/141.png</image>
					<image>http://mlb.mlb.com/mlb/images/flagstaff/600x600/139.png</image>
					<image>http://mlb.mlb.com/mlb/images/flagstaff/600x600/147.png</image>
					<image>http://mlb.mlb.com/mlb/images/flagstaff/600x600/110.png</image>
					<image>http://mlb.mlb.com/mlb/images/flagstaff/600x600/111.png</image>
					<image>http://mlb.mlb.com/mlb/images/flagstaff/600x600/140.png</image>
					<image>http://mlb.mlb.com/mlb/images/flagstaff/600x600/136.png</image>
					<image>http://mlb.mlb.com/mlb/images/flagstaff/600x600/108.png</image>
					<image>http://mlb.mlb.com/mlb/images/flagstaff/600x600/133.png</image>
					<image>http://mlb.mlb.com/mlb/images/flagstaff/600x600/113.png</image>
					<image>http://mlb.mlb.com/mlb/images/flagstaff/600x600/117.png</image>
					<image>http://mlb.mlb.com/mlb/images/flagstaff/600x600/112.png</image>
					<image>http://mlb.mlb.com/mlb/images/flagstaff/600x600/138.png</image>
					<image>http://mlb.mlb.com/mlb/images/flagstaff/600x600/134.png</image>
					<image>http://mlb.mlb.com/mlb/images/flagstaff/600x600/121.png</image>
					<image>http://mlb.mlb.com/mlb/images/flagstaff/600x600/143.png</image>
					<image>http://mlb.mlb.com/mlb/images/flagstaff/600x600/120.png</image>
					<image>http://mlb.mlb.com/mlb/images/flagstaff/600x600/144.png</image>
					<image>http://mlb.mlb.com/mlb/images/flagstaff/600x600/146.png</image>
					<image>http://mlb.mlb.com/mlb/images/flagstaff/600x600/109.png</image>
					<image>http://mlb.mlb.com/mlb/images/flagstaff/600x600/115.png</image>
					<image>http://mlb.mlb.com/mlb/images/flagstaff/600x600/137.png</image>
					<image>http://mlb.mlb.com/mlb/images/flagstaff/600x600/119.png</image>
					<image>http://mlb.mlb.com/mlb/images/flagstaff/600x600/135.png</image>
				</paradePreview>
			</preview>
			<menu>
				<sections>
					<menuSection>
						<items>
							<oneLineMenuItem id="todaysGames" accessibilityLabel="Today's Games" onSelect="atvutils.loadAndApplyURL('http://gdx.mlb.com/components/game/mlb/year_2012/month_08/day_08/atv_scoreboard.xml', applyTodaysGames);">
								<label>Today&apos;s Games</label>
							</oneLineMenuItem>
							<oneLineMenuItem id="standings" accessibilityLabel="Standings" onSelect="atvutils.loadAndApplyURL('http://lws.mlb.com/appletvv2/views/Standings', updateImageResolution);">
								<label>Standings</label>
							</oneLineMenuItem>
							<oneLineMenuItem id="gameRecaps" accessibilityLabel="Game Recaps" onSelect="atvutils.loadAndApplyURL('http://lws.mlb.com/appletvv2/views/Recaps?showScores=$$showScores', updateImageResolution);">
								<label>Game Recaps</label>
							</oneLineMenuItem>
							<oneLineMenuItem id="teamByTeam" accessibilityLabel="Team By Team" onSelect="atvutils.loadAndApplyURL('http://lws.mlb.com/appletvv2/views/Teams', applyTeamsPage);">
								<label>Team By Team</label>
							</oneLineMenuItem>
							<oneLineMenuItem id="itemsLink" accessibilityLabel="MLB.com on iTunes">
								<label>MLB.com on iTunes</label>
							</oneLineMenuItem>
							<oneLineMenuItem id="subscribeMlbTv" accessibilityLabel="Subscribe To MLB.TV" onSelect="subscribeToMlbTv()">
								<label>Subscribe To MLB.TV</label>
							</oneLineMenuItem>
							<oneLineMenuItem id="settings" accessibilityLabel="Settings" onSelect="atvutils.loadAndApplyURL('http://lws.mlb.com/appletvv2/views/Settings', applySettingsPage);">
								<label>Settings</label>
							</oneLineMenuItem>
						</items>
					</menuSection>
				</sections>
			</menu>
		</listWithPreview>
	</body>
</atv>
0

HTTP/1.1 200 OK
Server: Oracle-iPlanet-Web-Server/7.0
Date: Wed, 08 Aug 2012 16:14:20 GMT
Content-type: text/html
Content-type: application/json
Transfer-encoding: chunked

11c
var config = {env: '_proddc2split', imageBase: 'http://mlb.mlb.com/mlb/images/flagstaff', appletvBase: 'http://lws.mlb.com/appletvv2', bamServiceBase: 'https://mlb-ws.mlb.com', gamedayBase: 'http://gdx.mlb.com', mlbBase: 'http://www.mlb.com', secureMlbBase: 'https://secure.mlb.com'};
0

HTTP/1.1 200 OK
Server: Oracle-iPlanet-Web-Server/7.0
Date: Wed, 08 Aug 2012 16:14:20 GMT
Content-type: application/x-javascript
Last-modified: Mon, 23 Apr 2012 14:41:39 GMT
Content-length: 17889
Etag: "45e1-4f956a23"
Accept-ranges: bytes

// ***************************************************

var EMAIL_IDENTITY_POINT_KEY = "email.identityPointId";
var EMAIL_FINGERPRINT_KEY = "email.fingerprint";

// atv.Element extensions
atv.Element.prototype.getElementsByTagName = function(tagName) {
	return this.ownerDocument.evaluateXPath("descendant::" + tagName, this);
}

// Extend atv.ProxyDocument to load errors from a message and description.
atv.ProxyDocument.prototype.loadError = function(message, description) {
	var doc = atvutils.makeErrorDocument(message, description);
	this.loadXML(doc);
}

// Extend atv.ProxyDocument to load errors from a message and description.
atv.ProxyDocument.prototype.swapXML = function(xml) {
	var me = this;
    try 
    {
        me.loadXML(xml, function(success) {
            if ( success ) {
                atv.unloadPage();
            } else {
                console.log("swapXML failed to load " + xml);
                me.loadXML(atvutils.siteUnavailableError(), function(success) {
                	console.log("swapXML site unavailable error load success " + success);
                    if ( success ) {
                        atv.unloadPage();
                    }
                });
            }
        });
    } 
    catch (e) 
    {
        console.error("swapXML caught exception while loading " + xml + ". " + e);
        me.loadXML(atvutils.siteUnavailableError(), function(success) {
            if ( success ) {
                atv.unloadPage();
            }
        });
    }
}

// atv.Document extensions
atv.Document.prototype.getElementById = function(id) {
 var elements = this.evaluateXPath("//*[@id='" + id + "']", this);
 if ( elements && elements.length > 0 ) {
     return elements[0];
 }
 return undefined;
}

// atv.Element extensions
atv.Element.prototype.getElementsByTagName = function(tagName) {
 return this.ownerDocument.evaluateXPath("descendant::" + tagName, this);
}

atv.Element.prototype.getElementByTagName = function(tagName) {
 var elements = this.getElementsByTagName(tagName);
 if ( elements && elements.length > 0 ) {
     return elements[0];
 }
 return undefined;
}

// ATVUtils - a JavaScript helper library for Apple TV
ATVUtils = function() {};

ATVUtils.prototype.onGenerateRequest = function (request) {
	
    console.log('atvutils.js atv.onGenerateRequest() oldUrl: ' + request.url);

    console.debug("atvutils.js atv.onGenerateRequest() atv.localStorage[mediaMetaRequestParams] " + atv.localStorage[MEDIA_META_REQUEST_PARAMS_KEY]);
	if ( request.url.indexOf("encode(MEDIA_META_REQUEST_PARAMS)") > -1 && atv.localStorage[MEDIA_META_REQUEST_PARAMS_KEY])
	{
		request.url = request.url.replace("encode(MEDIA_META_REQUEST_PARAMS)", encodeURIComponent(atv.localStorage[MEDIA_META_REQUEST_PARAMS_KEY]));
	}
	console.debug("atvutils.js atv.onGenerateRequest() atv.localStorage[mediaMetaRequestParams] " + atv.localStorage[MEDIA_META_REQUEST_PARAMS_KEY]);
	if ( request.url.indexOf("MEDIA_META_REQUEST_PARAMS") > -1 && atv.localStorage[MEDIA_META_REQUEST_PARAMS_KEY])
	{
		request.url = request.url.replace("MEDIA_META_REQUEST_PARAMS", atv.localStorage[MEDIA_META_REQUEST_PARAMS_KEY]);
	}
	if ( request.url.indexOf("IPID") > -1 && atv.localStorage[BEST_IDENTITY_POINT_KEY])
	{
		request.url = request.url.replace("IPID", atv.localStorage[BEST_IDENTITY_POINT_KEY]);
	}
	if ( request.url.indexOf("FINGERPRINT") > -1 && atv.localStorage[BEST_FINGERPRINT_KEY])
	{
		request.url = request.url.replace("FINGERPRINT", encodeURIComponent(atv.localStorage[BEST_FINGERPRINT_KEY]));
	}
	if ( request.url.indexOf("SESSION_KEY") > -1 )
	{
		var sessionKey = atv.localStorage[SESSION_KEY]; 
		console.debug("atvutils.js atv.onGenerateRequest sessionKey=" + sessionKey);
		request.url = request.url.replace("SESSION_KEY", (sessionKey) ? encodeURIComponent(sessionKey) : "");
	}
	if ( request.url.indexOf("encode(LINK_PROXY_REQUEST)") > -1 && atv.localStorage[EMAIL_IDENTITY_POINT_KEY] != null && atv.localStorage[EMAIL_FINGERPRINT_KEY] != null)
	{
		var linkRequest = serviceClient.createLinkRequest();
		console.debug("atvutils.js atv.onGenerateRequest() linkRequest " + linkRequest);
		if (linkRequest != null)
		{
			// WARNING: encode twice because the xs:uri type does not suppot curly brackets so encode an extra time. The LinkProxy class will be responsible for url unencoding the json 
			request.url = request.url.replace("encode(LINK_PROXY_REQUEST)", encodeURIComponent(encodeURIComponent(JSON.stringify(linkRequest))));
		}
	}
	if ( request.url.indexOf("$$showScores") > -1 )
	{
		var showScores = atv.localStorage[SHOW_SCORES_KEY]; 
		console.log("showScores=" + showScores);
		request.url = request.url.replace("$$showScores", (showScores) ? showScores : "true");
	}
	var authorized = atv.localStorage[AUTHORIZED_KEY]; 
	if ( request.url.indexOf("AUTH_MESSAGE_CODE") > -1 && authorized != null)
	{
		console.debug("atvutils.js AUTH_MESSAGE_CODE atv.onGenerateRequest() authorized " + authorized);
//		request.url = request.url.replace("AUTH_MESSAGE_CODE", ((authorized == "true") ? "ALREADY_PURCHASED" : "NOT_AUHTORIZED"));
	}

	console.log('atvutils.js atv.onGenerateRequest() newUrl: ' + request.url);
}


ATVUtils.prototype.makeRequest = function(url, method, headers, body, callback) {
    if ( !url ) {
        throw "loadURL requires a url argument";
    }

	var method = method || "GET";
	var	headers = headers || {};
	var body = body || "";
	
    var xhr = new XMLHttpRequest();
    xhr.onreadystatechange = function() {
        try {
            if (xhr.readyState == 4 ) {
                if ( xhr.status == 200) {
                	console.log("makeRequest() getAllResponseHeaders(): " + xhr.getAllResponseHeaders());
                    callback(xhr.responseXML, xhr.responseText);
                } else {
                    console.log("makeRequest() received HTTP status " + xhr.status + " for " + url);
                    callback(xhr.responseXML, xhr.responseText);
                }
            }
        } catch (e) {
            console.error('makeRequest caught exception while processing request for ' + url + '. Aborting. Exception: ' + e);
            xhr.abort();
            callback(null, null);
        }
    }
    
    var request = {url: url};
    this.onGenerateRequest(request);
    
    console.debug("makeRequest() url " + request.url);

    xhr.open(method, request.url, true);

    for(var key in headers) {
        xhr.setRequestHeader(key, headers[key]);
    }

    console.debug("sending body in the send() method");
    xhr.send(body);
    return xhr;
}

ATVUtils.prototype.getRequest = function(url, callback) {
	return this.makeRequest(url, "GET", null, null, callback);
}

ATVUtils.prototype.postRequest = function(url, body, callback) {
	return this.makeRequest(url, "POST", null, body, callback);
}

ATVUtils.prototype.makeErrorDocument = function(message, description) {
    if ( !message ) {
        message = "";
    }
    if ( !description ) {
        description = "";
    }

    var errorXML = '<?xml version="1.0" encoding="UTF-8"?> \
    <atv> \
        <body> \
            <dialog id="com.bamnetworks.errorDialog"> \
                <title><![CDATA[' + message + ']]></title> \
                <description><![CDATA[' + description + ']]></description> \
            </dialog> \
        </body> \
    </atv>';

    return atv.parseXML(errorXML);
}

ATVUtils.prototype.makeOkDialog = function(message, description, onSelect) {
	if ( !message ) {
		message = "";
	}
	if ( !description ) {
		description = "";
	}
	
	if (!onSelect)
	{
		onSelect = "atv.unloadPage();";
	}
	
	var errorXML = '<?xml version="1.0" encoding="UTF-8"?> \
		<atv> \
		<head> \
			<script src="'+config.appletvBase+'/views/config.js" />\
			<script src="'+config.appletvBase+'/js/atvutils.js" />\
			<script src="'+config.appletvBase+'/js/service.js" />\
			<script src="'+config.appletvBase+'/js/main.js" />\
		</head>\
		<body> \
		<optionDialog id="com.bamnetowrks.optionDialog"> \
		<header><simpleHeader><title>' + message + '</title></simpleHeader></header> \
		<description><![CDATA[' + description + ']]></description> \
		<menu><sections><menuSection><items> \
			<oneLineMenuItem id="lineMenu" accessibilityLabel="OK" onSelect="' + onSelect + '">\
				<label>OK</label>\
			</oneLineMenuItem>\
		</items></menuSection></sections></menu>\
		</optionDialog> \
		</body> \
		</atv>';
	
	var doc = atv.parseXML(errorXML);
	return doc;
}

ATVUtils.prototype.printObject = function(object) {
	for (property in object)
	{
		console.log(property + ": " + object[property]);
	}
}


ATVUtils.prototype.siteUnavailableError = function() {
    // TODO: localize
	console.debug("siteUnavailableError()");
    return this.makeOkDialog("MLB.TV error", "MLB.TV is currently unavailable. Please try again later.");
}

ATVUtils.prototype.loadError = function(message, description) {
    atv.loadXML(this.makeErrorDocument(message, description));
}

ATVUtils.prototype.loadAndSwapError = function(message, description) {
    atv.loadAndSwapXML(this.makeErrorDocument(message, description));
}

ATVUtils.prototype.loadURLInternal = function(url, method, headers, body, loader) {
    var me = this;
    var xhr;
    var proxy = new atv.ProxyDocument;
    proxy.show();
    proxy.onCancel = function() {
        if ( xhr ) {
            xhr.abort();
        }
    };
    
    xhr = me.makeRequest(url, method, headers, body, function(xml, xhr) {
        try {
        	console.log("loadURLInternal() proxy=" + proxy + " xml=" + xml + " xhr=" + xhr);
            loader(proxy, xml, xhr);
        } catch(e) {
            console.error("Caught exception loading " + url + ". " + e);
            loader(proxy, me.siteUnavailableError(), xhr);
        }
    });
}

ATVUtils.prototype.loadGet = function(url, loader) {
	this.loadURLInternal(url, "GET", null, null, loader);
}

// TODO:: change to a url string or hash
ATVUtils.prototype.loadURL = function(url, method, headers, body, processXML) {
    var me = this;
    this.loadURLInternal(url, method, headers, body, function(proxy, xml) {
        if(typeof(processXML) == "function") processXML.call(this, xml);
		try {
            proxy.loadXML(xml, function(success) {
                if ( !success ) {
                    console.log("loadURL failed to load " + url);
                    proxy.loadXML(me.siteUnavailableError());
                }
            });
        } catch (e) {
            console.log("loadURL caught exception while loading " + url + ". " + e);
            proxy.loadXML(me.siteUnavailableError());
        }
    });
}

ATVUtils.prototype.swapXML = function(proxy, xml) {
	var me = this;
    try {
        proxy.loadXML(xml, function(success) {
            if ( success ) {
                atv.unloadPage();
            } else {
                console.log("swapXML failed to load " + xml);
                proxy.loadXML(me.siteUnavailableError(), function(success) {
                    if ( success ) {
                        atv.unloadPage();
                    }
                });
            }
        });
    } catch (e) {
        console.error("swapXML caught exception while loading " + xml + ". " + e);
        proxy.loadXML(me.siteUnavailableError(), function(success) {
            if ( success ) {
                atv.unloadPage();
            }
        });
    }
}

// TODO:: change to a url string or hash
// loadAndSwapURL can only be called from page-level JavaScript of the page that wants to be swapped out.
ATVUtils.prototype.loadAndSwapURL = function(url, method, headers, body, processXML) {
    var me = this;
    this.loadURLInternal(url, method, headers, body, function(proxy, xml) { 
	    if(typeof(processXML) == "function") processXML.call(this, xml);
        me.swapXML(proxy, xml);
    });
}

/** Load URL and apply changes to the xml */
ATVUtils.prototype.loadAndApplyURL = function(url, processXML) {
    var me = this;
	this.loadURLInternal(url, 'GET', null, null, function(proxy, xml, xhr) {
		var xmlToLoad = xml;
        if(typeof(processXML) == "function") xmlToLoad = processXML.call(this, xml);
        if (!xmlToLoad) xmlToLoad = xml;
		try {
            proxy.loadXML(xmlToLoad, function(success) {
                if ( !success ) {
                    console.log("loadURL failed to load " + url);
                    proxy.loadXML(me.siteUnavailableError());
                }
            });
        } catch (e) {
            console.log("loadURL caught exception while loading " + url + ". " + e);
            proxy.loadXML(me.siteUnavailableError());
        }
    });
}

var atvutils = new ATVUtils;

console.log("atvutils initialized " + atvutils);

/**
 * This is an AXR handler. It handles most of tediousness of the XHR request and keeps track of onRefresh XHR calls so that we don't end up with multiple page refresh calls.
 * 
 * You can see how I call it on the handleRefresh function below.
 * 
 * 
 * @params object (hash) $h
 * @params string $h.url - url to be loaded
 * @params string $h.method - "GET", "POST", "PUT", "DELTE"
 * @params bool $h.type - false = "Sync" or true = "Async" (You should always use true)
 * @params func $h.success - Gets called on readyState 4 & status 200
 * @params func $h.failure - Gets called on readyState 4 & status != 200
 * @params func $h.callback - Gets called after the success and failure on readyState 4
 * @params string $h.data - data to be sent to the server
 * @params bool $h.refresh - Is this a call from the onRefresh event.
 */
function Ajax($h)
{
	var me = this;
	$h = $h || {}

	// Setup properties
	this.url = $h.url || false;
	this.method = $h.method || "GET";
	this.type = ($h.type === false) ? false : true;
	this.success = $h.success || null;
	this.failure = $h.failure || null;
	this.data = $h.data || null;
	this.complete = $h.complete || null;
	this.refresh = $h.refresh || false;

	if (!this.url)
	{
		console.error('\nAjax Object requires a url to be passed in: e.g. { "url": "some string" }\n')
		return undefined;
	}

	this.createRequest();
	this.req.onreadystatechange = this.stateChange;
	this.req.object = this;
	this.open();
	this.send();
}

Ajax.currentlyRefreshing = false;

Ajax.prototype.stateChange = function()
{
	var me = this.object;

	// console.log("this in stateChage is: "+ this);
	// console.log("object in stateChange is: "+ this.object);

	switch (this.readyState)
	{

	case 1:
		if (typeof (me.connection) === "function")
			me.connection(this, me);
		break;
	case 2:
		if (typeof (me.received) === "function")
			me.received(this, me);
		break;
	case 3:
		if (typeof (me.processing) === "function")
			me.processing(this, me);
		break;
	case 4:
		if (this.status == "200")
		{
			if (typeof (me.success) === "function")
				me.success(this, me);
		}
		else
		{
			if (typeof (me.failure) === "function")
				me.failure(this.status, this, me);
		}
		if (typeof (me.complete) === "function")
			me.complete(this, me);
		if (me.refresh)
			Ajax.currentlyRefreshing = false;
		break;
	default:
		console.log("I don't think I should be here.");
		break;
	}
}

Ajax.prototype.createRequest = function()
{
	try
	{
		this.req = new XMLHttpRequest();
		if (this.refresh)
			Ajax.currentlyRefreshing = true;
	}
	catch (error)
	{
		alert("The request could not be created: </br>" + error);
		console.error("failed to create request: " + error);
	}

}

Ajax.prototype.open = function()
{
	try
	{
		this.req.open(this.method, this.url, this.type);
	}
	catch (error)
	{
		console.error("failed to open request: " + error);
	}
}

Ajax.prototype.send = function()
{
	var data = this.data || null;
	try
	{
		this.req.send(data);
	}
	catch (error)
	{
		console.error("failed to send request: " + error);
	}
}


// End ATVUtils
// ***************************************************



FlowManager = function()
{
};

FlowManager.prototype.setTargetPage = function(pageId)
{
	atv.localStorage[TARGET_PAGEID_KEY] = pageId;
	console.debug("flowManager.setTargetPage() atv.localStorage[TARGET_PAGEID_KEY] " + atv.localStorage[TARGET_PAGEID_KEY]);
}

FlowManager.prototype.clearTargetPage = function()
{
	atv.localStorage.removeItem(TARGET_PAGEID_KEY);
	console.debug("flowManager.clearTargetPage() atv.localStorage[TARGET_PAGEID_KEY] " + atv.localStorage[TARGET_PAGEID_KEY]);
}

FlowManager.prototype.startPurchaseFlow = function()
{
	atv.localStorage[PURCHASE_FLOW_KEY] = "true";
	console.debug("flowManager.startPurchaseFlow() atv.localStorage[PURCHASE_FLOW_KEY] " + atv.localStorage[PURCHASE_FLOW_KEY]);
}

FlowManager.prototype.endPurchaseFlow = function()
{
	atv.localStorage.removeItem(PURCHASE_FLOW_KEY);
	atv.localStorage.removeItem(PURCHASE_PRODUCT_ID_KEY);
	console.debug("flowManager.endPurchaseFlow() atv.localStorage[PURCHASE_FLOW_KEY] " + atv.localStorage[PURCHASE_FLOW_KEY]);
}

FlowManager.prototype.inPurchaseFlow = function()
{
	console.debug("flowManager.inPurchaseFlow() atv.localStorage[PURCHASE_FLOW_KEY] " + atv.localStorage[PURCHASE_FLOW_KEY]);
	return (atv.localStorage[PURCHASE_FLOW_KEY] == "true");
}

FlowManager.prototype.inPurchaseFlow = function()
{
	console.debug("flowManager.inPurchaseFlow() atv.localStorage[PURCHASE_FLOW_KEY] " + atv.localStorage[PURCHASE_FLOW_KEY]);
	return (atv.localStorage[PURCHASE_FLOW_KEY] == "true");
}

FlowManager.prototype.inMediaFlow = function()
{
	console.debug("flowManager.inMediaFlow() atv.localStorage[MEDIA_META_KEY] " + atv.localStorage[MEDIA_META_KEY]);
	return atv.localStorage.getItem(MEDIA_META_KEY) != null && atv.localStorage.getItem(MEDIA_META_REQUEST_PARAMS_KEY) != null;
}

FlowManager.prototype.endMediaFlow = function()
{
	atv.localStorage.removeItem(MEDIA_META_KEY);
	atv.localStorage.removeItem(MEDIA_META_REQUEST_PARAMS_KEY);
	console.debug("flowManager.endMediaFlow() atv.localStorage[MEDIA_META_KEY] " + atv.localStorage[MEDIA_META_KEY]);
}

var flowManager = new FlowManager;

console.debug("atvutils.js flowManager created");HTTP/1.1 200 OK
Server: Oracle-iPlanet-Web-Server/7.0
Date: Wed, 08 Aug 2012 16:14:21 GMT
Content-type: application/x-javascript
Last-modified: Thu, 14 Jun 2012 17:05:42 GMT
Content-length: 27397
Etag: "6b05-4fda19e6"
Accept-ranges: bytes

console.log("~~~~~ MLB.TV service.js START");

var PRODUCT_LIST = {
	"IOSATBAT2012GDATVMON" : {
		"image" : "monthlyPremium.jpg",
		"period" : "month"
	}, 
	"IOSMLBTVYEARLY2012" : {
		"image" : "monthlyPremium.jpg",
		"period" : "year"
	}
};

var MLBTV_FEATURE_CONTEXT = "MLBTV2012.INAPPPURCHASE";
var MLBTV_FEATURE_NAME = "MLBALL";

var IOS_IDENTITY_POINT_KEY = "ios.identityPointId";
var IOS_FINGERPRINT_KEY = "ios.fingerprint";
var USERNAME_KEY = "username";
var EMAIL_IDENTITY_POINT_KEY = "email.identityPointId";
var EMAIL_FINGERPRINT_KEY = "email.fingerprint";
var BEST_IDENTITY_POINT_KEY = "best.identityPointId";
var BEST_FINGERPRINT_KEY = "best.fingerprint";
var EMAIL_LINKED_KEY = "email.linked";
var AUTHORIZED_KEY = "authorized";
var SHOW_LINK_STATUS = "showLinkStatus";
var MEDIA_META_KEY = "media.meta";
var MEDIA_META_REQUEST_PARAMS_KEY = "media.meta.requestParams";
var OFFER_URI_AFTER_AUTH_KEY = "offer.uriToLoadAfterAuth";
var OFFER_PURCHASE_FUNCTION_KEY = "offer.purchaseFunction";
var OFFER_SWAP_PAGE_KEY = "offer.swapPage";

ServiceClient = function()
{
};

ServiceClient.prototype.login = function(username, password, success, error)
{
	var url = config.bamServiceBase + "/pubajaxws/services/IdentityPointService";
	var identifyBody = "<soapenv:Envelope xmlns:soapenv=\"http://schemas.xmlsoap.org/soap/envelope/\" xmlns:ns=\"http://services.bamnetworks.com/registration/types/1.0\"><soapenv:Body><ns:identityPoint_identify_request><ns:identification type=\"email-password\"> \
			<ns:email><ns:address>" + username + "</ns:address></ns:email> \
			<ns:password>" + password + "</ns:password> \
		</ns:identification></ns:identityPoint_identify_request></soapenv:Body></soapenv:Envelope>";
	var headers = {
		"SOAPAction" : "http://services.bamnetworks.com/registration/identityPoint/identify",
		"Content-Type" : "text/xml"
	};

	console.log('Trying to authenticate with ' + url);

	atvutils.makeRequest(url, "POST", headers, identifyBody, function(document)
	{
		var identityPointId, fingerprint;
		var statusCode = (document && document.rootElement) ? document.rootElement.childElements[1].getElementByName("identityPoint_identify_response").getElementByName("status").getElementByName("code").textContent : -1;
		console.log('status code is ' + statusCode);

		var errorMessage;
		if (statusCode == 1)
		{
			var identification = document.rootElement.childElements[1].getElementByName("identityPoint_identify_response").getElementByName("identification");
			identityPointId = identification.getElementByName("id").textContent;
			fingerprint = identification.getElementByName("fingerprint").textContent;
		}
		else if (statusCode == -1000)
		{
			errorMessage = "The account name or password was incorrect. Please try again.";
		}
		else
		{
			errorMessage = "MLB.com is curently undergoing maintenance. Please try again.";
		}

		if (identityPointId && fingerprint)
		{
			atv.localStorage[USERNAME_KEY] = username;
			atv.localStorage[EMAIL_IDENTITY_POINT_KEY] = identityPointId;
			atv.localStorage[EMAIL_FINGERPRINT_KEY] = fingerprint;

			if (!atv.localStorage[BEST_IDENTITY_POINT_KEY] && !atv.localStorage[BEST_IDENTITY_POINT_KEY])
			{
				console.debug("saving the email as the best identity point id");
				atv.localStorage[BEST_IDENTITY_POINT_KEY] = identityPointId;
				atv.localStorage[BEST_FINGERPRINT_KEY] = fingerprint;
			}
			console.debug("best identity point id: " + atv.localStorage[BEST_IDENTITY_POINT_KEY]);

			console.debug("determine whether the user is authorized " + serviceClient.loadUserAuthorized);
			serviceClient.loadUserAuthorized(function()
			{
				if (success)
					success();
			});
		}
		else
		{
			if (error)
				error(errorMessage);
			console.log('error message ' + errorMessage);
		}
	});
}

ServiceClient.prototype.loadUserAuthorized = function(callback)
{
	var url = config.bamServiceBase + "/pubajaxws/services/FeatureService";
	var body = "<soapenv:Envelope xmlns:soapenv=\"http://schemas.xmlsoap.org/soap/envelope/\" xmlns:ns=\"http://services.bamnetworks.com/registration/types/1.6\">\
			<soapenv:Body>\
				<ns:feature_findEntitled_request>\
					<ns:identification type=\"fingerprint\">\
						<ns:id>" + atv.localStorage[BEST_IDENTITY_POINT_KEY] + "</ns:id>\
						<ns:fingerprint>" + atv.localStorage[BEST_FINGERPRINT_KEY] + "</ns:fingerprint>\
					</ns:identification>\
					<ns:featureContextName>" + MLBTV_FEATURE_CONTEXT + "</ns:featureContextName>\
				</ns:feature_findEntitled_request>\
			</soapenv:Body>\
		</soapenv:Envelope>";

	var headers = {
		"SOAPAction" : "http://services.bamnetworks.com/registration/feature/findEntitledFeatures",
		"Content-Type" : "text/xml"
	};

	console.debug('loadUserAuthorized() Feature find entitlement request body ' + body);
	console.debug('loadUserAuthorized() Finding entitled features with ' + url);

	atvutils.makeRequest(url, "POST", headers, body, function(document, text)
	{
		console.debug("loadUserAuthorized() Feature.findEntitled response " + text);
		var authorized = (text.indexOf(MLBTV_FEATURE_NAME) > -1);
		console.debug("loadUserAuthorized() authorized " + authorized);
		atv.localStorage[AUTHORIZED_KEY] = authorized.toString();
		console.debug("loadUserAuthorized() set atv.localStorage[AUTHORIZED_KEY] to " + atv.localStorage[AUTHORIZED_KEY]);
		callback(authorized);
	});
}

ServiceClient.prototype.addRestoreObserver = function(successCallback, errorCallback)
{
	// Setup a transaction observer to see when transactions are restored and when new payments are made.

	var transReceived = [];
	
	var restoreObserver = {
		updatedTransactions : function(transactions)
		{
			console.debug("addRestoreObserver() updatedTransactions " + (transactions) ? transactions.length : null);

			if (transactions)
			{
				for ( var i = 0; i < transactions.length; ++i)
				{
					transaction = transactions[i];
					if (transaction.transactionState == atv.SKPaymentTransactionStateRestored)
					{
						console.debug("addRestoreObserver() Found restored transaction: " + GetLogStringForTransaction(transaction) + "\n---> Original Transaction: " + GetLogStringForTransaction(transaction.originalTransaction));
						
						// add
						transReceived.push(transaction);
						
						// finish the transaction
						atv.SKDefaultPaymentQueue.finishTransaction(transaction);
					}
				}
			}
			console.debug("addRestoreObserver() updatedTransactions " + transactions.length);
		},

		restoreCompletedTransactionsFailedWithError : function(error)
		{
			console.debug("addRestoreObserver() restoreCompletedTransactionsFailedWithError start " + error + " code: " + error.code);
			
			atv.SKDefaultPaymentQueue.removeTransactionObserver(this);
			if (typeof (errorCallback) == "function")
				errorCallback(error);
			
			console.debug("addRestoreObserver() restoreCompletedTransactionsFailedWithError end " + error + " code: " + error.code);
		},

		restoreCompletedTransactionsFinished : function()
		{
			console.debug("addRestoreObserver() restoreCompletedTransactionsFinished start");
			
			atv.SKDefaultPaymentQueue.removeTransactionObserver(this);
			
			console.log("addRestoreObserver() transReceived " + transReceived);
			if (typeof (successCallback) == "function")
			{
				successCallback(transReceived);
			}	
			
			console.debug("addRestoreObserver() restoreCompletedTransactionsFinished end");
		}
	};

	atv.SKDefaultPaymentQueue.addTransactionObserver(restoreObserver);
	return restoreObserver;
}

function GetLogStringForTransaction(t)
{
	var result = "Transaction: " + "transactionIdentifier=" + ((t.transactionIdentifier) ? t.transactionIdentifier : "") + ", transactionState=" + ((t.transactionState) ? t.transactionState : "") + ", transactionReceipt=" + ((t.transactionReceipt) ? t.transactionReceipt : "") + ", originalTransaction= " + ((t.originalTransaction) ? t.originalTransaction : "") + ", transactionDate=" + ((t.transactionDate) ? t.transactionDate : "");

	if (t.payment)
	{
		result += ", payment.productIdentifier=" + t.payment.productIdentifier + ", payment.quantity=" + t.payment.quantity;
	}

	if (t.error)
	{
		result += ", error.domain=" + t.error.domain + ", error.code=" + t.error.code + ", error.userInfo=" + t.error.userInfo;
	}

	return result;
}

ServiceClient.prototype.doRequestProduct = function(productIds, successCallback, errorCallback)
{
	console.debug("doRequestProduct() productIds " + productIds);
	// make another products request to get product information
	var request = new atv.SKProductsRequest(productIds);

	request.onRequestDidFinish = function()
	{
		console.debug("doRequestProduct() Products request succeeded");
		if (typeof (successCallback) == "function")
			successCallback();
	};

	request.onRequestDidFailWithError = function(error)
	{
		console.error("doRequestProduct() Products request failed. " + ErrorToDebugString(error));
		if (typeof (errorCallback) == "function")
			errorCallback(error);
	};

	request.onProductsRequestDidReceiveResponse = function(productsResponse)
	{
		console.log("doRequestProduct() Products request received response " + productsResponse);
		console.debug("There are " + productsResponse.products.length + " offers.");

		if (typeof (successCallback) == "function")
			successCallback(productsResponse);
	};

	console.debug("Calling doRequestProduct() start");
	request.start();
}

ServiceClient.prototype.startPurchase = function()
{
	var productID = atv.localStorage["purchase.productId"];

	if (!productID)
	{
		console.debug("startPurchase(): not making a purchase because productID was not provided");
	}
	else
	{
		// make another products request to get product information
		var request = new atv.SKProductsRequest([ productID ]);

		request.onRequestDidFailWithError = function(error)
		{
			console.error("startPurchase() Products request failed. " + ErrorToDebugString(error));
		};

		request.onProductsRequestDidReceiveResponse = function(productsResponse)
		{
			console.log("startPurchase() Products request received response " + productsResponse);
			console.debug("There are " + productsResponse.products.length + " offers.");

			if (productsResponse && productsResponse.products && productsResponse.products.length == 1)
			{
				var product = productsResponse.products[0];
				console.debug("Found available product for ID " + product);
				var payment = {
					product : product,
					quantity : 1
				};
				atv.SKDefaultPaymentQueue.addPayment(payment);
			}
		};

		console.debug("Calling doRequestProduct() start");
		request.start();
	}
}

ServiceClient.prototype.addPurchaseObserver = function(successCallback, errorCallback)
{
	console.debug("addPurchaseObserver(): Purchase product with identifier " + productID);

	var productID = atv.localStorage["purchase.productId"];

	var observer = {
		updatedTransactions : function(transactions)
		{
			console.debug("addPurchaseObserver(): purchase.updatedTransactions: " + transactions.length);

			for ( var i = 0; i < transactions.length; ++i)
			{
				var transaction = transactions[i];

				console.debug("addPurchaseObserver(): Processing transaction " + i + ": " + GetLogStringForTransaction(transaction));

				if (transaction.payment && transaction.payment.productIdentifier == productID)
				{
					console.debug("addPurchaseObserver(): transaction is for product ID " + productID);

					// This is the product ID this transaction observer was created for.
					switch (transaction.transactionState)
					{
					case atv.SKPaymentTransactionStatePurchasing:
						console.debug("addPurchaseObserver(): SKPaymentTransactionStatePurchasing");
						break;
					case atv.SKPaymentTransactionStateRestored:
					case atv.SKPaymentTransactionStatePurchased:
						var reason = transaction.transactionState == atv.SKPaymentTransactionStatePurchased ? "Purchased" : "Restored";
						console.debug("addPurchaseObserver(): SKPaymentTransactionStatePurchased: Purchase Complete!!! Reason: " + reason);
						atv.SKDefaultPaymentQueue.finishTransaction(transaction);
						atv.SKDefaultPaymentQueue.removeTransactionObserver(this);
						if (typeof (successCallback) == "function")
							successCallback(transaction);
						break;
					case atv.SKPaymentTransactionStateFailed:
						console.debug("addPurchaseObserver(): SKPaymentTransactionStateFailed. " + ErrorToDebugString(transaction.error));
						atv.SKDefaultPaymentQueue.finishTransaction(transaction);
						atv.SKDefaultPaymentQueue.removeTransactionObserver(this);
						if (typeof (errorCallback) == "function")
							errorCallback(transaction);
						break;
					default:
						if (typeof (errorCallback) == "function")
							errorCallback(transaction);
						console.error("addPurchaseObserver(): Unknown transaction state: " + transaction.transactionState);
						break;
					}
				}
			}
		}
	}

	atv.SKDefaultPaymentQueue.addTransactionObserver(observer);
	return observer;
}

ServiceClient.prototype.startMediaFlow = function(mediaMeta)
{
	console.debug("startMediaFlow() mediaMeta " + mediaMeta);

	if (mediaMeta)
	{
		// set media meta to the local storage
		atv.localStorage[MEDIA_META_KEY] = JSON.stringify(mediaMeta);

		mediaMeta.title = mediaMeta.awayTeamCity + " " + mediaMeta.awayTeamName + " vs " + mediaMeta.homeTeamCity + " " + mediaMeta.homeTeamName;
		mediaMeta.description = mediaMeta.feed;
		if (mediaMeta.awayTeamId && mediaMeta.homeTeamId)
		{
			mediaMeta.imageUrl = config.imageBase + "/recapThumbs/" + mediaMeta.awayTeamId + "_v_" + mediaMeta.homeTeamId + ".jpg";
		}

		// set the session key from the local storage
		mediaMeta.sessionKey = atv.localStorage["sessionKey"];

		var mediaMetaRequestParams = "";
		mediaMetaRequestParams += '&identityPointId=' + ((atv.localStorage[BEST_IDENTITY_POINT_KEY]) ? atv.localStorage[BEST_IDENTITY_POINT_KEY] : 'IPID');
		mediaMetaRequestParams += '&fingerprint=' + ((atv.localStorage[BEST_FINGERPRINT_KEY]) ? atv.localStorage[BEST_FINGERPRINT_KEY] : 'FINGERPRINT');
		mediaMetaRequestParams += "&contentId=" + mediaMeta.contentId;
		mediaMetaRequestParams += '&sessionKey=' + ((mediaMeta.sessionKey) ? encodeURIComponent(mediaMeta.sessionKey) : "SESSION_KEY");
		mediaMetaRequestParams += '&calendarEventId=' + mediaMeta.calendarEventId;
		mediaMetaRequestParams += "&audioContentId=" + ((mediaMeta.audioContentId) ? mediaMeta.audioContentId.replace(/\s/g, ',') : "");
		mediaMetaRequestParams += '&day=' + mediaMeta.day
		mediaMetaRequestParams += '&month=' + mediaMeta.month;
		mediaMetaRequestParams += '&year=' + mediaMeta.year;
		mediaMetaRequestParams += "&feedType=" + mediaMeta.feedType;
		mediaMetaRequestParams += "&playFromBeginning=" + mediaMeta.playFromBeginning;
		mediaMetaRequestParams += "&title=" + encodeURIComponent(mediaMeta.title);
		mediaMetaRequestParams += "&description=" + encodeURIComponent(mediaMeta.description);
		mediaMetaRequestParams += (mediaMeta.imageUrl) ? ("&imageUrl=" + encodeURIComponent(mediaMeta.imageUrl)) : "";

		// save in local storage (should be in session storage but session storage is not yet supported)
		atv.localStorage[MEDIA_META_KEY] = JSON.stringify(mediaMeta);
		atv.localStorage[MEDIA_META_REQUEST_PARAMS_KEY] = mediaMetaRequestParams;
		console.debug("storing into local storage mediaMetaRequestParams " + atv.localStorage[MEDIA_META_REQUEST_PARAMS_KEY]);
	}
}

ServiceClient.prototype.loadMediaPage = function(callback)
{
	if (atv.localStorage[MEDIA_META_REQUEST_PARAMS_KEY])
	{
		var mediaProxyUrl = config.appletvBase + "/views/MediaServiceProxy?";
		// mediaProxyUrl += '&identityPointId=' + atv.localStorage[BEST_IDENTITY_POINT_KEY];
		// mediaProxyUrl += '&fingerprint=' + encodeURIComponent(atv.localStorage[BEST_FINGERPRINT_KEY]);
		mediaProxyUrl += atv.localStorage[MEDIA_META_REQUEST_PARAMS_KEY];
		console.debug("loadMediaPage mediaProxyUrl " + mediaProxyUrl);

		// make request
		atvutils.getRequest(mediaProxyUrl, callback);
	}
	else if (callback)
	{
		callback();
	}
}

function applyOffer(document)
{
	console.debug("applyOffer() atv.localStorage[PURCHASE_FLOW_KEY] " + atv.localStorage[PURCHASE_FLOW_KEY]);
	console.debug("applyOffer() document " + document);
	var mlbSignIn = (document) ? document.getElementById("mlbSignIn") : null;
	if (mlbSignIn)
	{
		var label = mlbSignIn.getElementByName("label");
		console.debug("applyOffer() atv.localStorage[EMAIL_IDENTITY_POINT_KEY] " + atv.localStorage[EMAIL_FINGERPRINT_KEY]);
		var signedIn = atv.localStorage[EMAIL_IDENTITY_POINT_KEY] != null && atv.localStorage[EMAIL_FINGERPRINT_KEY] != null;
		console.debug("applyOffer() signedIn " + signedIn);

		var uriToLoadAfterAuth = atv.localStorage[OFFER_URI_AFTER_AUTH_KEY];

		if (signedIn)
		{
			label.removeFromParent();
			label = atv.parseXML("<label>Sign In with different user</label>").rootElement;
			label.removeFromParent();
			mlbSignIn.appendChild(label);

			mlbSignIn.setAttribute("onSelect", "atv.logout();atvutils.loadAndSwapURL('" + config.appletvBase + "/views/Login?urlToLoadAfterAuth=" + uriToLoadAfterAuth + "');");
			mlbSignIn.setAttribute("onPlay", "atv.logout();atvutils.loadAndSwapURL('" + config.appletvBase + "/views/Login?urlToLoadAfterAuth=" + uriToLoadAfterAuth + "');");
		}
		else
		{
			console.debug("applyOffer() uriToLoadAfterAuth " + uriToLoadAfterAuth);
			label.textContent = "MLB.TV Premium Subscriber Login";
			console.debug("applyOffer() mlbSignIn.getAttribute('onSelect') " + mlbSignIn.getAttribute('onSelect'));
			mlbSignIn.setAttribute("onSelect", "atvutils.loadAndSwapURL('" + config.appletvBase + "/views/Login?urlToLoadAfterAuth=" + uriToLoadAfterAuth + "');");
			mlbSignIn.setAttribute("onPlay", "atvutils.loadAndSwapURL('" + config.appletvBase + "/views/Login?urlToLoadAfterAuth=" + uriToLoadAfterAuth + "');");
			console.debug("applyOffer() mlbSignIn.getAttribute('onSelect') " + mlbSignIn.getAttribute('onSelect'));
		}
	}
}

ServiceClient.prototype.loadOfferPage = function(newPage)
{
	console.debug("loadOfferPage() enter");

	newPage
	
	// set purchase flow to true
	flowManager.startPurchaseFlow();

	var swap = (atv.localStorage[OFFER_SWAP_PAGE_KEY] == "true");
	console.debug("loadOfferPage() atv.localStorage[OFFER_SWAP_PAGE_KEY] " + atv.localStorage[OFFER_SWAP_PAGE_KEY]);
	console.debug("loadOfferPage() swap " + swap);

	// Use StoreKit to get the list of product identifiers.
	var request = new atv.SKProductsRequest(Object.keys(PRODUCT_LIST));
	request.onRequestDidFailWithError = function(error)
	{
		console.error("Products request failed. " + ErrorToDebugString(error));
		var xml = atvutils.makeOkDialog("MLB.TV", "MLB.TV Products could not be found. Please check again at a later time.");
		if (swap)
			newPage.swapXML(xml)
		else
			newPage.loadXML(xml);
	};
	request.onProductsRequestDidReceiveResponse = function(productsResponse)
	{
		// success
		console.debug("loadOfferPage() Products request received response " + productsResponse);
		console.debug("loadOfferPage() There are " + productsResponse.products.length + " offers.");

		// add image
		console.debug("loadOfferPage() checking for image location");
		for ( var i = 0; i < productsResponse.products.length; i++)
		{
			var product = productsResponse.products[i];
			console.debug("loadOfferPage() product.productIdentifier " + product.productIdentifier);
			if (PRODUCT_LIST[product.productIdentifier] != null)
			{
				product.image = PRODUCT_LIST[product.productIdentifier].image;
				console.debug("loadOfferPage() product.image " + product.image);

				product.period = PRODUCT_LIST[product.productIdentifier].period;
				console.debug("loadOfferPage() product.period " + product.period);
			}
		}

		// This page will display the offers, and call purchaseProduct below with the productId to purchase in an onSelect handler.
		var offerBody = "";
		offerBody += "productMeta=" + JSON.stringify(productsResponse);
		offerBody += "&purchaseFunction=" + atv.localStorage[OFFER_PURCHASE_FUNCTION_KEY];
		if (atv.localStorage[OFFER_URI_AFTER_AUTH_KEY])
		{
			offerBody += "&uriToLoadAfterAuth=" + atv.localStorage[OFFER_URI_AFTER_AUTH_KEY];
		}

		console.debug("loadOfferPage() offerBody: " + offerBody);

		atvutils.postRequest(config.appletvBase + '/views/Offer', offerBody, function(xml)
		{
			try
			{
				console.debug("loadOfferPage() postRequest xml=" + xml);
				applyOffer(xml);
				if (swap)
					newPage.swapXML(xml)
				else
					newPage.loadXML(xml);
			}
			catch (e)
			{
				console.error("Caught exception for the Offer page " + e);
				var errorXML = atvutils.makeOkDialog("MLB.TV Error", "MLB.TV Products could not be found. Please check again at a later time.");
				if (swap)
					newPage.swapXML(errorXML)
				else
					newPage.loadXML(errorXML);
			}
		});
	};

	console.debug("Calling loadOfferPage() start");
	request.start();
}
ServiceClient.prototype.isMediaResponseUnauthorized = function(xml)
{
	return (xml) ? this.isMediaUnauthorized(this.getMediaStatusCode(xml)) : true;
}

ServiceClient.prototype.isMediaUnauthorized = function(mediaStatusCode)
{
	return "" == mediaStatusCode || "UNDETERMINED" == mediaStatusCode || "NOT_AUHTORIZED" == mediaStatusCode || "LOGIN_REQUIRED" == mediaStatusCode || "INVALID_USER_CREDENTIALS_STATUS" == mediaStatusCode;
}

ServiceClient.prototype.isMediaPlayback = function(mediaStatusCode)
{
	return "VIDEO_PLAYBACK" == mediaStatusCode || "AUDIO_PLAYBACK" == mediaStatusCode;
}

ServiceClient.prototype.getMediaStatusCode = function(xml)
{
	var mediaStatusCodeElem = (xml && xml.rootElement) ? xml.rootElement.getElementByTagName("mediaStatusCode") : null;
	console.debug("getMediaStatusCode() mediaStatusCodeElem " + mediaStatusCodeElem);
	var mediaStatusCode = (mediaStatusCodeElem != null) ? mediaStatusCodeElem.textContent : "UNDETERMINED";
	console.debug("getMediaStatusCode() mediaStatusCode " + mediaStatusCode);
	return mediaStatusCode;
}

ServiceClient.prototype.createFingerprintIdpt = function(ipid, fingerprint)
{
	return {
		"type" : "fingerprint",
		"id" : ipid,
		"fingerprint" : fingerprint
	};
}

ServiceClient.prototype.createIosIdentityPoint = function(transaction)
{
	return {
		"type" : "iosInAppPurchase",
		"receipt" : transaction.transactionReceipt
	};
}

ServiceClient.prototype.createLinkRequest = function(transactions)
{
	var identityPoints = [];

	// email-password fingerprint identity point
	if (atv.localStorage[EMAIL_FINGERPRINT_KEY] && atv.localStorage[EMAIL_IDENTITY_POINT_KEY])
	{
		identityPoints.push(this.createFingerprintIdpt(atv.localStorage[EMAIL_IDENTITY_POINT_KEY], atv.localStorage[EMAIL_FINGERPRINT_KEY]));
	}

	if (transactions)
	{
		console.debug("createLinkRequest() filtering out for unique original transactions");
		
		// filter out unique original transactions
		var originalTransMap = {};
		
		for (var t = 0; t < transactions.length; t++)
		{
			var currTrans = transactions[t];
			var origTrans = currTrans.originalTransaction;
			var transId = (origTrans == null) ? currTrans.transactionIdentifier : origTrans.transactionIdentifier;  
			
			console.debug("createLinkRequest() transId " + transId);
			
			if (originalTransMap[transId] == null)
			{
				// transaction identity point
				identityPoints.push(this.createIosIdentityPoint(currTrans));
				
				originalTransMap[transId] = currTrans;
			}
		}
	}
	// best fingerprint identity point
	else if (atv.localStorage[BEST_FINGERPRINT_KEY] && atv.localStorage[BEST_IDENTITY_POINT_KEY])
	{
		identityPoints.push(this.createFingerprintIdpt(atv.localStorage[BEST_IDENTITY_POINT_KEY], atv.localStorage[BEST_FINGERPRINT_KEY]));
	}

	var linkRequest = null;
	if (identityPoints.length > 0)
	{
		// link request
		linkRequest = {
			"identityPoints" : identityPoints,
			"namespace" : "mlb",
			"key" : "h-Qupr_3eY"
		};
	}

	console.debug("createLinkRequest() linkRequest " + JSON.stringify(linkRequest));

	return linkRequest;
}

ServiceClient.prototype.doLink = function(transactions, successCallback, errorCallback)
{
	var linkRequest = this.createLinkRequest(transactions);

	if (linkRequest == null)
	{
		if (typeof (successCallback) == "function")
			successCallback();

		console.debug("doLink() link not called since nothing to link");
	}
	else
	{
		// link request
		var linkRequestBody = JSON.stringify(linkRequest);
		var req = new XMLHttpRequest();
		var url = config.mlbBase + "/mobile/LinkAll2.jsp";

		req.onreadystatechange = function()
		{
			try
			{
				if (req.readyState == 4)
				{
					console.log('link() Got link service http status code of ' + req.status);
					if (req.status == 200)
					{
						console.log('link() Response text is ' + req.responseText);
						var linkResponseJson = JSON.parse(req.responseText);

						if (linkResponseJson.status == 1)
						{
							console.debug("link() bestIdentityPointToUse: " + linkResponseJson.bestIdentityPointToUse);
							console.debug("link() messages: " + linkResponseJson.messages);

							if (linkResponseJson.bestIdentityPointToUse && linkResponseJson.bestIdentityPointToUse.id && linkResponseJson.bestIdentityPointToUse.fingerprint)
							{
								console.debug("link() saving the best identity point to use");
								atv.localStorage[BEST_IDENTITY_POINT_KEY] = linkResponseJson.bestIdentityPointToUse.id;
								atv.localStorage[BEST_FINGERPRINT_KEY] = linkResponseJson.bestIdentityPointToUse.fingerprint;
								console.debug("link() identityPointId " + atv.localStorage[BEST_IDENTITY_POINT_KEY]);

								if (linkResponseJson.bestIdentityPointToUse.id.toLowerCase().indexOf("ios") >= 0)
								{
									atv.localStorage[IOS_IDENTITY_POINT_KEY] = linkResponseJson.bestIdentityPointToUse.id;
									atv.localStorage[IOS_FINGERPRINT_KEY] = linkResponseJson.bestIdentityPointToUse.fingerprint;
								}
							}

							var linked = false;// , auth = false;
							for ( var m = 0; m < linkResponseJson.messages.length; m++)
							{
								var msg = linkResponseJson.messages[m];
								console.debug("doLink() msg " + msg);
								if (msg == "linked")
								{
									linked = true;
								}
							}
							linked = linked && atv.localStorage[EMAIL_IDENTITY_POINT_KEY] != null && atv.localStorage[EMAIL_FINGERPRINT_KEY] != null;
							// atv.localStorage[EMAIL_LINKED_KEY] = linked.toString();
							// atv.localStorage[AUTHORIZED_KEY] = auth.toString();

							// console.debug("doLink() atv.localStorage[EMAIL_LINKED_KEY] " + atv.localStorage[EMAIL_LINKED_KEY];
							console.debug("atv.localStorage[AUTHORIZED_KEY] " + atv.localStorage[AUTHORIZED_KEY]);

							if (typeof (successCallback) == "function")
								successCallback();

							console.log("doLink() getAllResponseHeaders: " + this.getAllResponseHeaders());
						}
						else
						{

							if (typeof (errorCallback) == "function")
								errorCallback();
							console.error("link response json has an invalid status code: " + linkResponseJson.status);
						}
					}
				}
			}
			catch (e)
			{
				// Specify a copyedited string because this will be displayed to
				// the user.
				console.error('Caught exception while processing request. Aborting. Exception: ' + e);
				if (typeof (errorCallback) == "function")
					errorCallback(e);

				req.abort();
			}
		}

		req.open("POST", url, true);
		req.setRequestHeader("Content-Type", "text/xml");

		console.log('Trying to link with ' + url);
		console.log("Link Service POST body is " + linkRequestBody);
		req.send(linkRequestBody);
	}
}

var serviceClient = new ServiceClient;

console.log("~~~~~ MLB.TV service.js END");HTTP/1.1 200 OK
Server: Oracle-iPlanet-Web-Server/7.0
Date: Wed, 08 Aug 2012 16:14:22 GMT
Content-type: application/x-javascript
Last-modified: Tue, 31 Jul 2012 21:32:23 GMT
Content-length: 42766
Etag: "a70e-50184ee7"
Accept-ranges: bytes

console.log("~~~~~ MLB.TV main.js start");

var IOS_IDENTITY_POINT_KEY = "ios.identityPointId";
var IOS_FINGERPRINT_KEY = "ios.fingerprint";
var BEST_IDENTITY_POINT_KEY = "best.identityPointId";
var BEST_FINGERPRINT_KEY = "best.fingerprint";
var USERNAME_KEY = "username";

var SHOW_SCORES_KEY = "showScores";
var FAVORITE_TEAMS_KEY = "favoriteTeamIds";

var SESSION_KEY = "sessionKey";
var MEDIA_META_KEY = "media.meta";
var MEDIA_META_REQUEST_PARAMS_KEY = "media.meta.requestParams";

var PURCHASE_FLOW_KEY = "flow.purchase";
var PURCHASE_PRODUCT_ID_KEY = "purchase.productId";

var EMAIL_LINKED_KEY = "email.linked";
var AUTHORIZED_KEY = "authorized";

var OFFER_URI_AFTER_AUTH_KEY = "offer.uriToLoadAfterAuth";
var OFFER_PURCHASE_FUNCTION_KEY = "offer.purchaseFunction";
var OFFER_SWAP_PAGE_KEY = "offer.swapPage";

var TARGET_PAGEID_KEY = "targetPage";
var PURCHASE_FLOW_KEY = "flow.purchase";
var OFFER_PAGE_ID = "com.bamnetworks.appletv.offer";

var TODAYS_GAMES_REFRESH_URL_KEY = "todaysGames.refreshUrl";
var GAMES_PAGE_REFRESH_URL_KEY = "games.refreshUrl";

// atv.onPageLoad
atv.onPageLoad = function(id)
{
	console.log("main.js atv.onPageLoad() id: " + id);

	// <mlbtv>
	var headElem = document.rootElement.getElementByName("head");
	if (headElem && headElem.getElementByName("mlbtv"))
	{
		var mlbtvElem = headElem.getElementByName("mlbtv");
		console.log("mlbtv exists");

		// store local variables
		storeLocalVars(mlbtvElem);

		// ping urls
		pingUrls(mlbtvElem);
	}

	// games by team
	if (id && id.indexOf("com.bamnetworks.appletv.gamesByTeam.") >= 0)
	{
		applyGamesPage(document);
	}

	if (id && id.indexOf("com.bamnetworks.appletv.media") >= 0 && typeof (flowManager) != "undefined")
	{
		flowManager.endPurchaseFlow();
		flowManager.endMediaFlow();
		console.debug("main.js onPageLoad() atv.localStorage[PURCHASE_FLOW_KEY] " + atv.localStorage[PURCHASE_FLOW_KEY]);
	}

	atv._debugDumpControllerStack();
}

// atv.onPageUnload
atv.onPageUnload = function(id)
{
	console.debug("main.js atv.onPageUnload() id " + id);
	console.debug("main.js atv.onPageUnload() atv.localStorage[PURCHASE_FLOW_KEY] " + atv.localStorage[PURCHASE_FLOW_KEY]);
	if (flowManager.inPurchaseFlow() && ("com.bamnetworks.appletv.authenticate" == id || id.indexOf("Not Authorized") > -1))
	{
		console.debug("main.js atv.onPageUnload() showing offer page");
		flowManager.setTargetPage(OFFER_PAGE_ID);

		var newPage = new atv.ProxyDocument();
		newPage.show();
		serviceClient.loadOfferPage(newPage);

		console.debug("main.js atv.onPageUnload() showing offer page");
	}
}

function storeLocalVars(mlbtvElem)
{
	// check local storage params
	var localStorageElems = mlbtvElem.getElementsByName("localStorage");
	console.debug("localStorageElems=" + localStorageElems);
	if (localStorageElems && localStorageElems.length > 0)
	{
		var index = 0;
		for (; index < localStorageElems.length; index++)
		{
			var key = localStorageElems[index].getAttribute("key");
			var value = localStorageElems[index].getAttribute("value");
			if (value)
			{
				atv.localStorage[key] = value;
			}
			else
			{
				atv.localStorage.removeItem(key);
			}
			console.log("localStorage[" + key + "] = " + atv.localStorage[key]);
		}
	}
}

function pingUrls(mlbtvElem)
{
	// check ping urls
	var pingElems = mlbtvElem.getElementsByName("ping");
	console.log("pingElems=" + pingElems);
	if (pingElems && pingElems.length > 0)
	{
		for ( var pingIndex = 0; pingIndex < pingElems.length; pingIndex++)
		{
			var url = pingElems[pingIndex].getAttribute('url');
			url = (url == null || url.trim().length() <= 0) ? pingElems[pingIndex].textContent : url;
			console.debug("url=" + url);
			if (url)
			{
				try
				{
					console.log('pinging reporting url ' + url);
					var req = new XMLHttpRequest();
					req.open("GET", url, true);
					req.send();
				}
				catch (e)
				{
					console.error("failed to ping the url " + url + " error " + e);
				}
			}
		}
	}
}

/**
 * Updates the logo according to the screen resolution
 * 
 * @param document
 * @returns
 */
function updateImageResolution(document)
{
	// update logos according to screen resolution
	var frame = atv.device.screenFrame;
	console.log("frame size: " + frame.height);
	if (frame.height == 1080)
	{
		var images = document.rootElement.getElementsByTagName('image');
		for ( var i = 0; i < images.length; i++)
		{
			images[i].textContent = images[i].textContent.replace('720p', '1080p');
		}
	}

	return document;
}

// ------------------------------ load and apply -------------------------------
function applyMain(document)
{
	console.log('applyMain ' + document);

	// end all flows when you're on the main page
	flowManager.endMediaFlow();
	flowManager.endPurchaseFlow();

	// check valid subscription
	// (1) via iTunes on this box and iTunes account signed in. Receipt is present and stored on the box
	// (2) via Vendor and is signed in with Vendor account.
	var validSubscription = (atv.localStorage[IOS_IDENTITY_POINT_KEY] != null && atv.localStorage[IOS_FINGERPRINT_KEY] != null) || (atv.localStorage[AUTHORIZED_KEY] == "true" && atv.localStorage[EMAIL_IDENTITY_POINT_KEY] != null && atv.localStorage[EMAIL_FINGERPRINT_KEY] != null);

	console.log("applyMain() validSubscription " + validSubscription);
	console.log("applyMain() atv.localStorage[AUTHORIZED_KEY] " + atv.localStorage[AUTHORIZED_KEY]);

	var subscribeMenu = document.getElementById("subscribeMlbTv");
	if (validSubscription)
	{
		if (subscribeMenu)
		{
			subscribeMenu.removeFromParent();
			console.debug("applyMain() removing the Subscribe To MLB.TV");
		}
	}
	else
	{
		// add the menu if it's not there
		if (!subscribeMenu)
		{
			subscribeMenu = atv.parseXML('<oneLineMenuItem id="subscribeMlbTv" accessibilityLabel="Subscribe To MLB.TV" onSelect="subscribeToMlbTv()"><label>Subscribe To MLB.TV</label></oneLineMenuItem>').rootElement;
			subscribeMenu.removeFromParent();

			var parent = document.rootElement.getElementByTagName("items");
			console.debug("applyMain() parent " + parent);
			if (parent && parent.childElements)
			{
				console.debug("applyMain() before parent.childElements " + parent.childElements);
				var settingsMenu = document.getElementById("settings");
				settingsMenu.removeFromParent();
				parent.appendChild(subscribeMenu);
				parent.appendChild(settingsMenu);
				console.debug("applyMain() after parent.childElements " + parent.childElements);
			}
		}
	}

	return document;
}

function handleMainVoltileReload()
{
	console.debug("handleMainVoltileReload() atv.localStorage[TARGET_PAGEID_KEY] " + atv.localStorage[TARGET_PAGEID_KEY]);
	if (atv.localStorage[TARGET_PAGEID_KEY] == null || atv.localStorage[TARGET_PAGEID_KEY] == "com.bamnetworks.appletv.main")
	{
		applyMain(document);
		atv.loadAndSwapXML(document);
	}
}

function handleOfferVoltileReload(document)
{
	console.debug("handleOfferVoltileReload() atv.localStorage[AUTHORIZED_KEY] " + atv.localStorage[AUTHORIZED_KEY]);
	if (atv.localStorage[AUTHORIZED_KEY] == "true")
	{
		console.debug("handleOfferVoltileReload() unloading the page because already authorized");
		atv.unloadPage();
	}
	else
	{
		applyOffer(document);
		atv.loadAndSwapXML(document);
	}
}

function applyTodaysGames(responseXML, refresh)
{
	console.log("applyTodaysGames() responseXML: " + responseXML);

	try
	{
		// elements
		var listByNavigation = responseXML.rootElement.getElementByTagName("listByNavigation");
		var imageMenuItems = responseXML.rootElement.getElementsByTagName("imageTextImageMenuItem");
		console.log("applyTodaysGames() navigation=" + navigation);

		// add behavior
		if (atv.localStorage[TODAYS_GAMES_REFRESH_URL_KEY] == null || !refresh)
		{
			var navigation = responseXML.rootElement.getElementByTagName("navigation");
			var currentIndex = navigation.getAttribute("currentIndex");
			console.debug("applyTodaysGames() currentIndex " + currentIndex);
			var refreshUrl = navigation.childElements[currentIndex].getElementByTagName("url").textContent;
			console.debug("applyTodaysGames() refreshUrl " + refreshUrl);
			atv.localStorage[TODAYS_GAMES_REFRESH_URL_KEY] = refreshUrl;
		}
		listByNavigation.setAttribute("refreshInterval", "60");
		listByNavigation.setAttribute("onRefresh", "console.log('refreshing todays games');handleTodaysGamesRefresh();");
		listByNavigation.setAttribute("onNavigate", "console.log('navigating todays games');handleTodaysGamesNavigate(event);");

		// sort
		var favTeamIds = getFavoriteTeamIds();
		function sort(menuOne, menuTwo)
		{
			var menuOneTeamIds = menuOne.getAttribute("id").split("-");
			var menuTwoTeamIds = menuTwo.getAttribute("id").split("-");

			var menuOneOrder = (favTeamIds.indexOf(menuOneTeamIds[0]) >= 0 || favTeamIds.indexOf(menuOneTeamIds[1]) >= 0) ? 1 : 0;
			var menuTwoOrder = (favTeamIds.indexOf(menuTwoTeamIds[0]) >= 0 || favTeamIds.indexOf(menuTwoTeamIds[1]) >= 0) ? 1 : 0;

			// console.log("favTeamIds.indexOf(menuOneTeamIds[0])=" + favTeamIds.indexOf(menuOneTeamIds[0]) + " favTeamIds.indexOf(menuOneTeamIds[1])=" + favTeamIds.indexOf(menuOneTeamIds[1]));
			// console.log(menuOneOrder + "-" + menuTwoOrder);

			return menuTwoOrder - menuOneOrder;
		}
		var orderedMenuList = imageMenuItems.sort(sort);

		// 1. re-append the sorted children
		// 2. set the favorite team logos
		// 3. hide scores on the page
		// 4. set the link to no scores preview
		var index = 0;
		for (; index < orderedMenuList.length; index++)
		{
			// 1. reappend child for sorting
			var menu = orderedMenuList[index];
			var parent = menu.parent;
			menu.removeFromParent();
			parent.appendChild(menu);

			// 2. set the favorite team logos
			console.debug("applyTodaysGames() menu item id " + menu.getAttribute("id"));
			var teamIds = menu.getAttribute("id").split("-");
			var isAwayFav = favTeamIds.indexOf(teamIds[0]) >= 0;
			var isHomeFav = favTeamIds.indexOf(teamIds[1]) >= 0;
			console.debug("applyTodaysGames() isAwayFav " + isAwayFav);
			console.debug("applyTodaysGames() isHomeFav " + isHomeFav);
			var leftImageElem = menu.getElementByName("leftImage");
			if (isAwayFav && leftImageElem.textContent.indexOf("-fav.png") < 0)
			{
				leftImageElem.textContent = leftImageElem.textContent.replace(".png", "-fav.png");
			}
			var rightImageElem = menu.getElementByName("rightImage");
			if (isHomeFav && rightImageElem.textContent.indexOf("-fav.png") < 0)
			{
				rightImageElem.textContent = rightImageElem.textContent.replace(".png", "-fav.png");
			}

			// 3. Hide scores
			// 4. Use the preview link with no scores
			var showScores = (atv.localStorage[SHOW_SCORES_KEY]) ? atv.localStorage[SHOW_SCORES_KEY] : "true";
			var rightLabelElem = menu.getElementByName("rightLabel");
			if (showScores == "false")
			{
				var previewLinkElem = menu.getElementByTagName("link");
				if (rightLabelElem && rightLabelElem.textContent.indexOf("Final") >= 0)
				{
					rightLabelElem.textContent = "Final";
				}
				if (previewLinkElem)
				{
					previewLinkElem.textContent = previewLinkElem.textContent.replace(".xml", "_noscores.xml");
				}
			}

			// 5. Check for double header 3:33 AM ET
			if (rightLabelElem && rightLabelElem.textContent.indexOf("3:33 AM") >= 0)
			{
				rightLabelElem.textContent = "Game 2";
			}
		}

		return responseXML;
	}
	catch (error)
	{
		console.error("unable to apply todays games " + error);
		atvutils.loadError("An error occurred loading today's games", "An error occurred loading today's games");
	}
}

/**
 * Should be called by the onRefresh event, I am also calling this from the handleNavigate function below to share code. Essentially this function: 1. Loads the current Navigation file 2. Pulls the
 * menu items out of the loaded file and dumps them into the current file 3. Checks to see if an onRefresh AJAX call is already being made and if so does nothing.
 * 
 * 
 * @params string url - url to be loaded
 */
function handleTodaysGamesRefresh()
{
	var url = atv.localStorage[TODAYS_GAMES_REFRESH_URL_KEY];
	console.log("handleTodaysGamesRefresh() url " + url);

	console.log("We are about to check the value of Ajax.currentlyRefreshing: --> " + Ajax.currentlyRefreshing);
	// check the Ajax object and see if a refresh event is happening
	if (!Ajax.currentlyRefreshing)
	{
		// if a refresh event is not happening, make another one.
		console.log("handleTodaysGamesRefresh()About to make the AJAX call. --> ")

		var refresh = new Ajax({
			"url" : url,
			"refresh" : true,
			"success" : function()
			{
				var req = arguments[0];

				console.log("handleTodaysGamesRefresh() Ajax url " + url);

				var newDocument = atv.parseXML(req.responseText); // Instead of using the responseXML you should parse the responseText. I know it seems redundent but trust me.

				// update the content page
				var newMenuSections = newDocument.rootElement.getElementByTagName('sections');
				applyTodaysGames(newDocument, true);
				newMenuSections.removeFromParent();

				var menu = document.rootElement.getElementByTagName('menu');
				console.log("handleTodaysGamesRefresh() menu=" + menu);
				if (menu)
				{
					var oldMenuSections = menu.getElementByTagName('sections');
					menu.replaceChild(oldMenuSections, newMenuSections);
				}
				else
				{
					menu = atv.parseXML("<menu></menu>").rootElement;
					menu.removeFromParent();
					menu.appendChild(newMenuSections);

					var listByNavigation = document.rootElement.getElementByTagName("listByNavigation");
					listByNavigation.appendChild(menu);
				}
				
				// replace # Games
				var newTumbler = newDocument.rootElement.getElementByTagName('tumblerWithSubtitle');
				newTumbler.removeFromParent();
				var header = document.rootElement.getElementByTagName('header');
				var oldTumbler = document.rootElement.getElementByTagName('tumblerWithSubtitle');
				header.replaceChild(oldTumbler, newTumbler);
			}
		});
		console.log("handleTodaysGamesRefresh() I have made the AJAX call. <--");
	}
	console.log("handleTodaysGamesRefresh() I am on the other side of the if. <--");
}

/**
 * This function works very similar to the handleRefresh Essentially we are manually loading the data and updating the page in Javascript This means we have to handle a bit more of the magic, but we
 * also get to control what is updated and update more of the page, like headers, previews, etc.
 * 
 * In this example, I am: 1. Getting the new URL from the Navigation Item 2. Changing the onRefresh URL 3. Calling handleRefresh and passing the new url to let handleRefresh do the magic.
 */
function handleTodaysGamesNavigate(e)
{
	console.log("handleTodaysGamesNavigate() " + e + ", e.navigationItemId = " + e.navigationItemId + " document = " + document);
	console.log("e=" + e);
	atvutils.printObject(e);
	console.log("document.getElementById(e.navigationItemId)=" + document.getElementById(e.navigationItemId));
	console.log("document.getElementById(e.navigationItemId).getElementByTagName=" + document.getElementById(e.navigationItemId).getElementByTagName);

	// get the url
	var url = document.getElementById(e.navigationItemId).getElementByTagName('url').textContent;
	atv.localStorage[TODAYS_GAMES_REFRESH_URL_KEY] = url;

	handleTodaysGamesRefresh();

	e.onCancel = function()
	{
		// Cancel any outstanding requests related to the navigation event.
		console.log("Navigation got onCancel.");
	}
}

function applyFeedSelectionPage(document)
{
	console.log("applyFeedSelectionPage() document: " + document);

	// add behavior by adding javascript file reference
	var head = document.rootElement.getElementByTagName("head");
	if (head)
	{
		var serviceJs = atv.parseXML('<script src="' + config.appletvBase + '/js/service.js" />').rootElement;
		serviceJs.removeFromParent();
		console.debug("adding service.js " + serviceJs);

		var children = head.childElements;
		children.unshift(serviceJs);

		for ( var i = 0; i < children.length; i++)
		{
			children[i].removeFromParent();
			head.appendChild(children[i]);
		}
	}

	flowManager.endMediaFlow();
	flowManager.endPurchaseFlow();
	console.log("purchase and media flow ended " + atv.localStorage[PURCHASE_PRODUCT_ID_KEY] + " " + atv.localStorage[MEDIA_META_REQUEST_PARAMS_KEY] + " " + atv.localStorage[MEDIA_META_KEY] + " " + atv.localStorage[SESSION_KEY]);

	return document;
}

// This is called from the feed_selection.xml
function loadFeed(mediaMeta)
{
	atv._debugDumpControllerStack();

	console.debug("showing new page..");
	var newPage = new atv.ProxyDocument();
	newPage.show();

	atv._debugDumpControllerStack();

	// store the media meta into the local storage for later use
	serviceClient.startMediaFlow(mediaMeta);
	atv.localStorage[OFFER_URI_AFTER_AUTH_KEY] = config.appletvBase + "/views/MediaServiceProxy?encode(MEDIA_META_REQUEST_PARAMS)";
	atv.localStorage[OFFER_PURCHASE_FUNCTION_KEY] = "restorePurchasePlay";
	atv.localStorage[OFFER_SWAP_PAGE_KEY] = "true";

	// load the media page
	serviceClient.loadMediaPage(function(document)
	{
		// media proxy returns a playback page, display
		var authorized = !serviceClient.isMediaResponseUnauthorized(document);
		// atv.localStorage.setItem(AUTHORIZED_KEY, (authorized) ? "true" : "false");
		if (authorized)
		{
			// if authorized, load the page
			console.debug("loadFeed() showing the media page");
			newPage.swapXML(document);
		}
		else
		{
			// if unauthorized, load the offer page
			console.debug("loadFeed() showing the offer page swap " + atv.localStorage[OFFER_SWAP_PAGE_KEY]);
			serviceClient.loadOfferPage(newPage);
		}
	});
}
// deprecated function name but it's still being generated by the gameday mule
var loadLiveGameVideoAsset = loadFeed;

function loadOfferPageAndPlay()
{
	var newPage = new atv.ProxyDocument();
	newPage.show();
	serviceClient.loadOfferPage(newPage);
}

function subscribeToMlbTv()
{
	console.debug("subscribeToMlbTv() enter");

	flowManager.endMediaFlow();

	// create new page where offer page will be loaded onto
	var newPage = new atv.ProxyDocument();
	newPage.show();

	// load the offer page onto the new page
	atv.localStorage[OFFER_URI_AFTER_AUTH_KEY] = config.appletvBase + "/views/Dialog?messageCode=AUTH_MESSAGE_CODE";
	atv.localStorage[OFFER_PURCHASE_FUNCTION_KEY] = "restoreAndPurchase";
	atv.localStorage[OFFER_SWAP_PAGE_KEY] = "false";
	serviceClient.loadOfferPage(newPage);
}

function ErrorToDebugString(error)
{
	var localizedDescription = error.userInfo["NSLocalizedDescription"];
	localizedDescription = localizedDescription ? localizedDescription : "Unknown";
	return error.domain + "(" + error.code + "): " + localizedDescription;
}

function linkManually()
{
	atv.loadURL(config.appletvBase + "/views/Login?urlToLoadAfterAuth=" + config.appletvBase + "/views/LinkProxy?request=encode(LINK_PROXY_REQUEST)");
}

function purchase(newPage)
{
	var observer = serviceClient.addPurchaseObserver(function(purchasedTransaction)
	{
		// PURCHASE success
		console.debug("purchase(): success purchased transactions " + purchasedTransaction);

		// link()
		serviceClient.doLink([ purchasedTransaction ], function()
		{
			console.debug("purchase(): link success");
			serviceClient.loadUserAuthorized(function()
			{
				console.debug("purchase(): load user authorized");
				newPage.loadXML(atvutils.makeOkDialog("MLB.TV Purchased", "You have successfully purchased MLB.TV."));
				atv.unloadPage();
			});
		}, function()
		{
			console.debug("purchase(): link error after purchase");
			newPage.cancel();
			atv.unloadPage();
			// do nothing -- go back to previous page
			// atv.loadAndSwapXML(atvutils.makeOkDialog("MLB.TV Error", "Failed to determine your MLB.TV subscription after purchase"));
		})
	}, function()
	{
		// PURCHASE error
		console.debug("purchase(): error ");
		newPage.cancel();
		atv.unloadPage();
	});

	newPage.onCancel = function()
	{
		atv.SKDefaultPaymentQueue.removeTransactionObserver(observer);
	};

	serviceClient.startPurchase();

}

function restoreAndPurchase(productId)
{
	atv._debugDumpControllerStack();

	// store product id into the cart
	atv.localStorage[PURCHASE_PRODUCT_ID_KEY] = productId;

	// create and show new page where dialogs will be loaded
	var newPage = new atv.ProxyDocument();
	newPage.show();

	atv._debugDumpControllerStack();

	var restoreObserver = serviceClient.addRestoreObserver(function(transactions)
	{
		// link()
		serviceClient.doLink(transactions, function()
		{
			// LINK success -- determine authorization thru feature service
			atv._debugDumpControllerStack();
			serviceClient.loadUserAuthorized(function()
			{
				console.debug("restoreAndPurchase() atv.localStorage[AUTHORIZED_KEY] " + atv.localStorage[AUTHORIZED_KEY]);
				if (atv.localStorage[AUTHORIZED_KEY] == "true")
				{
					console.debug("restoreAndPurchase() authorized or error page, showing Success page...");
					atv._debugDumpControllerStack();
					newPage.cancel();
					atv.loadAndSwapXML(atvutils.makeOkDialog("Success", "You are now authorized to watch MLB.TV.  Please select a game from Today's Games on the main menu."));
					console.debug("restoreAndPurchase() after showing Success page...");
					atv._debugDumpControllerStack();
				}
				else
				{
					// unauthorized, purchase
					console.debug("restoreAndPurchase() unauthorized, purchasing...");
					purchase(newPage);
					atv._debugDumpControllerStack();
				}
			});
		}, function()
		{
			// LINK error -- attempt to purchase now
			console.debug("restoreAndPurchase() link error - make the purchase ");
			purchase(newPage);
			atv._debugDumpControllerStack();
		});
	}, function(error)
	{
		newPage.cancel();
		if (error && error.code == -500)
		{
			atv.loadAndSwapXML(atvutils.makeOkDialog("MLB.TV Error", "Failed to determine your subscription."));
		}
		else
		{
			newPage.swapXML(document);
		}
		console.error("restoreAndPurchase() restore error - failed to restore " + document);
		atv._debugDumpControllerStack();
	});

	newPage.onCancel = function()
	{
		atv.SKDefaultPaymentQueue.removeTransactionObserver(restoreObserver);
	};

	// Restore completed transactions to get transactions purchased on other devices for this account.
	console.debug("restoreAndPurchase() Restoring completed transactions...");
	atv.SKDefaultPaymentQueue.restoreCompletedTransactions();
}

function purchaseAndPlay(newPage)
{
	atv._debugDumpControllerStack();

	var observer = serviceClient.addPurchaseObserver(function(purchasedTransaction)
	{
		// PURCHASE success
		console.debug("purchase(): success purchased transactions " + purchasedTransaction);
		atv._debugDumpControllerStack();

		// link()
		serviceClient.doLink([ purchasedTransaction ], function()
		{
			console.debug("purchase(): link success");
			atv._debugDumpControllerStack();

			serviceClient.loadMediaPage(function(mediaDoc)
			{
				console.debug("purchase() loadMediaPage");
				newPage.swapXML(mediaDoc);
				atv._debugDumpControllerStack();
			});

		}, function()
		{
			console.debug("purchase(): link error after purchase");
			newPage.swapXML(atvutils.makeOkDialog("MLB.TV Link Error", "Failed to link your MLB.TV account properly after purchase"));
			atv._debugDumpControllerStack();
		})

	}, function(transaction)
	{
		// PURCHASE error
		console.debug("purchase(): error " + (transaction) ? transaction.error : null);
		if (!transaction || transaction.error != 2)
		{
			newPage.swapXML(atvutils.makeOkDialog("MLB.TV Purchase Error", "The purchase failed.  Please try again at a later time."));
		}
		atv._debugDumpControllerStack();
	});

	newPage.onCancel = function()
	{
		atv.SKDefaultPaymentQueue.removeTransactionObserver(observer);
	};

	serviceClient.startPurchase();
}

function restorePurchasePlay(productId)
{
	// store product id into the cart
	atv.localStorage[PURCHASE_PRODUCT_ID_KEY] = productId;

	var newPage = new atv.ProxyDocument();
	newPage.show();

	atv._debugDumpControllerStack();

	var restoreObserver = serviceClient.addRestoreObserver(function(transactions)
	{
		// link()
		serviceClient.doLink(transactions, function()
		{
			// LINK success -- determine whether to purchase
			serviceClient.loadMediaPage(function(document)
			{
				atv._debugDumpControllerStack();
				var authorized = !serviceClient.isMediaResponseUnauthorized(document);
				console.debug("restorePurchasePlay() authorized " + authorized);
				atv.localStorage.setItem(AUTHORIZED_KEY, (authorized) ? "true" : "false");
				console.debug("restorePurchasePlay() atv.localStorage[AUTHORIZED] " + atv.localStorage[AUTHORIZED_KEY]);
				if (authorized)
				{
					// authorized or media playback error (session key problem, media unavailable, etc.), display the page
					console.debug("restorePurchasePlay() authorized or error page, showing media proxy page...");
					newPage.swapXML(atvutils.makeOkDialog("MLB.TV Subscriber", "You have purchased this content before and already have access to watch MLB.TV content.", "play();"));
					atv._debugDumpControllerStack();
					console.debug("restorePurchasePlay() after showing media proxy page...");
				}
				else
				{
					// unauthorized, purchase
					console.debug("restorePurchasePlay() unauthorized, purchasing...");
					purchaseAndPlay(newPage);
					atv._debugDumpControllerStack();
				}
			});
		}, function()
		{
			// LINK error -- attempt to purchase now
			console.debug("restorePurchasePlay() link error - make the purchase ");
			purchaseAndPlay(newPage);
		});
	}, function(error)
	{
		if (error && error.code == -500)
		{
			newPage.swapXML(atvutils.makeOkDialog("MLB.TV Error", "Failed to determine your subscription."));
		}
		else
		{
			newPage.swapXML(document);
		}
	});

	newPage.onCancel = function()
	{
		atv.SKDefaultPaymentQueue.removeTransactionObserver(restoreObserver);
	};

	// Restore completed transactions to get transactions purchased on other devices for this account.
	console.debug("restorePurchasePlay() Restoring completed transactions...");
	atv.SKDefaultPaymentQueue.restoreCompletedTransactions();
}

function play(newPage)
{
	serviceClient.loadMediaPage(function(document)
	{
		console.debug("play() show the media proxy page..." + newPage);
		atv._debugDumpControllerStack();
		if (newPage)
		{
			newPage.swapXML(document);
			atv.unloadPage();
		}
		else
		{
			atv.loadAndSwapXML(document);
		}
		atv._debugDumpControllerStack();
		console.debug("play() after showing media proxy page...");
	});
}

function restoreManually()
{
	console.debug("restore receipts from iTunes account manually");

	var newPage = new atv.ProxyDocument();
	newPage.show();

	var restoreObserver = serviceClient.addRestoreObserver(function(transactions)
	{
		// restore transaction callback
		console.log("restoreManually() transactions " + transactions);
		if (transactions)
		{
			console.debug("restoreManually() successfully restored receipts from iTunes account manually");
			serviceClient.doLink(transactions, function()
			{
				console.debug("restoreManually() link success callback - determine authorization from feature service");
				serviceClient.loadUserAuthorized(function()
				{
					newPage.loadXML(atvutils.makeOkDialog("Restore Successful", "Your subscription has been restored."));
				});
				console.debug("restoreManually() link service success");
			}, function()
			{
				console.debug("restoreManually() link service error");
			});
		}
		else
		{
			console.debug("restoreManually() missing transaction - determine authorization from feature service");
			newPage.loadXML(atvutils.makeOkDialog("Not an MLB.TV subscriber", "You have not purchased a MLB.TV subscription on AppleTV."));
		}
	}, function(error)
	{
		// restore error callback
		console.debug("restoreManually() error attempting to restore receipts from iTunes account manually");

		if (!error || error.code != -500)
		{
			newPage.cancel();
		}
		else
		{
			newPage.loadXML(atvutils.makeOkDialog("Restore Unsuccessful", "An error occurred attempting to restore your MLB.TV account."));
		}
	});

	newPage.onCancel = function()
	{
		atv.SKDefaultPaymentQueue.removeTransactionObserver(restoreObserver);
	};

	// Restore completed transactions to get transactions purchased on other devices for this account.
	console.debug("restoreManually() Restoring completed transactions...");
	atv.SKDefaultPaymentQueue.restoreCompletedTransactions();
}

function restoreOnOffer()
{
	console.debug("restoreOnOffer() restore receipts from iTunes account manually");

	var newPage = new atv.ProxyDocument();
	newPage.show();

	var restoreObserver = serviceClient.addRestoreObserver(function(transactions)
	{
		// restore success callback
		console.log("restoreManually() transaction " + transactions);
		if (transactions)
		{
			console.debug("successfully restored receipts from iTunes account manually");
			serviceClient.doLink(transactions, function()
			{
				// link success callback
				if (flowManager.inMediaFlow())
				{
					console.debug("restoreOnOffer() in media flow, playing media");
					play(newPage);
				}
				else
				{
					console.debug("restoreOnOffer() NOT in media flow, display info message");
					serviceClient.loadUserAuthorized(function()
					{
						console.debug("restoreOnOffer(): load user authorized");
						newPage.swapXML(atvutils.makeOkDialog("Restore Successful", "Your subscription has been restored."));
						atv.unloadPage();
					});
				}
			}, function()
			{
				// link error callback
				newPage.loadXML(atvutils.makeOkDialog("Restore Unsuccessful", "There was a problem restoring your iTunes account. Please try again at a later time."));
			});
		}
		else
		{
			newPage.loadXML(atvutils.makeOkDialog("Not an MLB.TV subscriber", "You have not purchased a MLB.TV subscription on AppleTV."));
		}
	}, function(error)
	{
		// restore error callback
		console.debug("error attempting to restore receipts from iTunes account manually");

		if (!error || error.code != -500)
		{
			newPage.cancel();
		}
		else
		{
			newPage.loadXML(atvutils.makeOkDialog("Restore Unsuccessful", "An error occurred attempting to restore your MLB.TV account."));
		}
	});

	newPage.onCancel = function()
	{
		atv.SKDefaultPaymentQueue.removeTransactionObserver(restoreObserver);
	};

	// Restore completed transactions to get transactions purchased on other devices for this account.
	console.debug("restoreManually() Restoring completed transactions...");
	atv.SKDefaultPaymentQueue.restoreCompletedTransactions();
}

function applySettingsPage(document)
{
	// update logo image resolution
	updateImageResolution(document);

	// show scores menu item
	var showScores = atv.localStorage[SHOW_SCORES_KEY];
	var menuItem = document.getElementById("showScoresMenuItem");
	if (menuItem)
	{
		var label = menuItem.getElementByName('rightLabel');
		label.textContent = (!showScores || showScores == "true") ? "Show" : "Hide";
		console.debug("label.textContent: " + label.textContent);
	}

	// menu items
	var parent = document.rootElement.getElementByTagName("items");
	var signInMenu = document.getElementById("signInOutMenuItem");
	var restoreMenu = document.getElementById("restoreSub");
	var linkAcctMenu = document.getElementById("linkAcct");
	var showScoresMenu = document.getElementById("showScoresMenuItem");
	console.debug("applySettingsPage() parent " + parent);

	// restore menu item
	var showRestoreMenu = atv.localStorage[IOS_IDENTITY_POINT_KEY] == null && atv.localStorage[IOS_FINGERPRINT_KEY] == null;
	console.debug("applySettingsPage() showRestoreMenu " + showRestoreMenu + " restoreMenu " + restoreMenu + " atv.localStorage[IOS_IDENTITY_POINT_KEY] " + atv.localStorage[IOS_IDENTITY_POINT_KEY]);
	if (showRestoreMenu)
	{
		if (!restoreMenu)
		{
			var parent = document.rootElement.getElementByTagName("items");
			console.debug("applySettingsPage() parent " + parent);
			if (parent && parent.childElements)
			{
				restoreMenu = atv.parseXML('<oneLineMenuItem id="restoreSub" accessibilityLabel="Restore Subscription" onSelect="restoreManually();"><label>Restore Subscription</label></oneLineMenuItem>').rootElement;
				console.debug("applySettingsPage() adding restoreMenu " + restoreMenu);
				restoreMenu.removeFromParent();
				parent.appendChild(restoreMenu);
				if (linkAcctMenu)
				{
					linkAcctMenu.removeFromParent();
					parent.appendChild(linkAcctMenu);
				}
				showScoresMenu.removeFromParent();
				parent.appendChild(showScoresMenu);
			}
		}
	}
	else if (restoreMenu)
	{
		restoreMenu.removeFromParent();
	}

	// link accounts menu item
	var showLinkMenu = atv.localStorage[EMAIL_LINKED_KEY] != "true" && atv.localStorage[IOS_IDENTITY_POINT_KEY] != null && atv.localStorage[IOS_FINGERPRINT_KEY] != null;
	console.debug("applySettingsPage() atv.localStorage[EMAIL_LINKED_KEY] " + atv.localStorage[EMAIL_LINKED_KEY] + " showLinkMenu " + showLinkMenu + " atv.localStorage[IOS_IDENTITY_POINT_KEY] " + atv.localStorage[IOS_IDENTITY_POINT_KEY] + " linkAcctMenu " + linkAcctMenu);
	if (showLinkMenu)
	{
		// add the menu if it's not there
		if (!linkAcctMenu)
		{
			if (parent && parent.childElements)
			{
				console.debug("applySettingsPage() before parent.childElements " + parent.childElements);

				linkAcctMenu = atv.parseXML('<oneLineMenuItem id="linkAcct" accessibilityLabel="Link MLB.TV Account" onSelect="linkManually();"><label>Link Account</label></oneLineMenuItem>').rootElement;
				linkAcctMenu.removeFromParent();

				showScoresMenu.removeFromParent();
				parent.appendChild(linkAcctMenu);
				parent.appendChild(showScoresMenu);

				console.debug("applySettingsPage() after parent.childElements " + parent.childElements);
			}
		}

		if (signInMenu)
		{
			signInMenu.removeFromParent();
		}
	}
	else
	{
		// remove the menu
		if (linkAcctMenu)
		{
			linkAcctMenu.removeFromParent();
		}

		if (!signInMenu)
		{
			signInMenu = atv.parseXML('\
				<signInSignOutMenuItem id=\"signInOutMenuItem\" accessibilityLabel=\"Sign out\" signOutExitsApp=\"false\">\
					<signInPageURL>' + config.appletvBase + '/views/Login</signInPageURL>\
					<confirmation>\
						<title>Are You Sure?</title>\
						<description>The account %@ will be logged out of the service.</description>\
					</confirmation>\
				</signInSignOutMenuItem>').rootElement;
			signInMenu.removeFromParent();

			parent.appendChild(signInMenu);
			if (showRestoreMenu && restoreMenu)
			{
				parent.appendChild(restoreMenu);
			}
			showScoresMenu.removeFromParent();
			parent.appendChild(showScoresMenu);
		}
	}

	return document;
}

function applyRecapsPage(responseXML)
{
	// update logo image resolution
	updateImageResolution(responseXML);

	return responseXML;
}

function applyTeamsPage(responseXML)
{
	try
	{
		// update logo image resolution
		updateImageResolution(responseXML);

		var favTeamIds = getFavoriteTeamIds();

		console.debug('sorting teams.. ' + responseXML.rootElement);
		var gridList = responseXML.rootElement.getElementByName("body").getElementByName("scroller").getElementByName("items").getElementsByName("grid");
		var gridIndex = 0;
		for (; gridIndex < gridList.length; gridIndex++)
		{
			// function to sort by title
			function sortByTitle(poster1, poster2)
			{
				var favTeamIds = (atv.localStorage[FAVORITE_TEAMS_KEY]) ? atv.localStorage[FAVORITE_TEAMS_KEY] : [];

				var team1Id = poster1.getAttribute("id");
				var is1Fav = favTeamIds.indexOf(team1Id) >= 0;
				var title1 = poster1.getElementByName("title").textContent;
				title1 = (is1Fav) ? title1.toUpperCase() : title1.toLowerCase();

				var team2Id = poster2.getAttribute("id");
				var is2Fav = favTeamIds.indexOf(team2Id) >= 0;
				var title2 = poster2.getElementByName("title").textContent;
				title2 = (is2Fav) ? title2.toUpperCase() : title2.toLowerCase();

				var result = (title1 > title2) ? 1 : -1;
				// console.debug('result: ' + title1 + ">" + title2 + " = " + result);

				return result;
			}

			// parent
			var parent = gridList[gridIndex].getElementByName("items");

			// sort the posters
			var orderedPosterList = parent.childElements.sort(sortByTitle);

			// re-append the child
			var index = 0;
			for (; index < orderedPosterList.length; index++)
			{
				var poster = orderedPosterList[index];
				var teamId = poster.getAttribute("id");
				poster.removeFromParent();
				parent.appendChild(poster);

				var isFav = favTeamIds.indexOf(teamId) >= 0;

				var imageElem = poster.getElementByName("image");
				if (isFav && imageElem.textContent.indexOf("-fav.png") < 0)
				{
					imageElem.textContent = imageElem.textContent.replace(".png", "-fav.png");
				}
				else if (!isFav && imageElem.textContent.indexOf("-fav.png") > 0)
				{
					imageElem.textContent = imageElem.textContent.replace("-fav.png", ".png");
				}
			}

		}

		console.debug('teams are sorted and favorite team logos in place');
		return responseXML;

	}
	catch (error)
	{
		console.debug("Unable to sort the teams: " + error);
	}
}

function applyGamesPage(responseXML)
{
	console.debug("applyeGamesPage() responseXML = " + responseXML);
	try
	{
		// determine if favorite team
		var listElem = responseXML.rootElement.getElementByName("body").getElementByName("listByNavigation");
		var pageId = listElem.getAttribute("id");
		var teamId = pageId.substring(pageId.lastIndexOf(".") + 1);
		var isFavorite = getFavoriteTeamIds().indexOf(teamId) > -1;
		console.debug("applyGamesPage() teamId: " + teamId + " isFavorite: " + isFavorite);

		var favButton = responseXML.getElementById("favoriteButton");
		var favoriteButtonLabel = favButton.getElementByName("label");
		console.debug("favoriteButtonLabel=" + favoriteButtonLabel);
		if (isFavorite)
		{
			var newLabel = "Remove from Favorites";
			favButton.setAttribute('accessibilityLabel', newLabel);
			favoriteButtonLabel.textContent = newLabel;
			console.debug("changing to Remove From Favorites favoriteButtonLabel.textContent=" + favoriteButtonLabel.textContent);
		}
		else
		{
			var newLabel = "Add to Favorites";
			favButton.setAttribute('accessibilityLabel', newLabel);
			favoriteButtonLabel.textContent = newLabel;
			console.debug("changing to Add to Favorites favoriteButtonLabel.textContent=" + favoriteButtonLabel.textContent);
		}

		return responseXML;
	}
	catch (error)
	{
		console.log("Unable to apply changes to the games page: " + error);
	}
}

/**
 * Should be called by the onRefresh event, I am also calling this from the handleNavigate function below to share code. Essentially this function: 1. Loads the current Navigation file 2. Pulls the
 * menu items out of the loaded file and dumps them into the current file 3. Checks to see if an onRefresh AJAX call is already being made and if so does nothing.
 * 
 * 
 * @params string url - url to be loaded
 */
function handleGamesPageRefresh(urlParam)
{
	var url = atv.localStorage[GAMES_PAGE_REFRESH_URL_KEY];
	if (url == null)
	{
		url = urlParam;
	}
	
	console.log("Handle refresh event called");

	console.log("We are about to check the value of Ajax.currentlyRefreshing: --> " + Ajax.currentlyRefreshing);
	// check the Ajax object and see if a refresh event is happening
	if (!Ajax.currentlyRefreshing)
	{

		// if a refresh event is not happening, make another one.
		console.log("About to make the AJAX call. --> ")

		var refresh = new Ajax({
			"url" : url,
			"refresh" : true,
			"success" : function()
			{
				var req = arguments[0];

				// update the content page
				var newDocument = atv.parseXML(req.responseText), // Instead of using the responseXML you should parse the responseText. I know it seems redundent but trust me.
				menu = document.rootElement.getElementByTagName('menu'), oldMenuSections = menu.getElementByTagName('sections'), newMenuSections = newDocument.rootElement.getElementByTagName('sections'), now = new Date();

				applyGamesPage(newDocument);

				newMenuSections.removeFromParent();
				menu.replaceChild(oldMenuSections, newMenuSections);

			}
		});

		console.debug("I have made the AJAX call. <--");
	}

	console.debug("I am on the other side of the if. <--");

}

/**
 * This function works very similar to the handleRefresh Essentially we are manually loading the data and updating the page in Javascript This means we have to handle a bit more of the magic, but we
 * also get to control what is updated and update more of the page, like headers, previews, etc.
 * 
 * In this example, I am: 1. Getting the new URL from the Navigation Item 2. Changing the onRefresh URL 3. Calling handleRefresh and passing the new url to let handleRefresh do the magic.
 */
function handleGamesPageNavigate(e)
{
	console.log("Handle navigate event called: " + e + ", e.navigationItemId = " + e.navigationItemId + " document = " + document);

	// get the url
	var url = document.getElementById(e.navigationItemId).getElementByTagName('url').textContent;
	atv.localStorage[GAMES_PAGE_REFRESH_URL_KEY] = url;

	handleGamesPageRefresh(url);

	e.onCancel = function()
	{
		// Cancel any outstanding requests related to the navigation event.
		console.log("Navigation got onCancel.");
	}
}

function getFavoriteTeamIds()
{
	var favTeamIds = atv.localStorage[FAVORITE_TEAMS_KEY];
	console.debug("favoriteTeamIds=" + favTeamIds);
	return (favTeamIds != null) ? favTeamIds : [];
}

function toggleFavoriteTeam(teamId)
{
	try
	{
		console.log("toggleFavoriteTeams " + teamId);
		var favoriteTeamIds = getFavoriteTeamIds();

		var isFavorite;
		if (favoriteTeamIds == null || favoriteTeamIds.length == 0)
		{
			// first favorite team
			favoriteTeamIds = [ teamId ];
			isFavorite = true;
		}
		else
		{
			var teamIdIndex = favoriteTeamIds.indexOf(teamId);
			if (teamIdIndex > -1)
			{
				// remove
				favoriteTeamIds.splice(teamIdIndex, 1);
				isFavorite = false;
			}
			else
			{
				// add
				favoriteTeamIds.push(teamId);
				isFavorite = true;
			}
		}

		atv.localStorage[FAVORITE_TEAMS_KEY] = favoriteTeamIds;

		var favoriteButton = document.getElementById("favoriteButton");
		if (favoriteButton)
		{
			var newLabel = (isFavorite) ? "Remove from Favorites" : "Add to Favorites";
			var label = favoriteButton.getElementByName("label");
			label.textContent = newLabel;
			console.debug("after label.textContent: " + label.textContent);
		}
	}
	catch (error)
	{
		console.error("Caught exception trying to toggle DOM element: " + error);
	}

	console.debug("toggleFavoriteTeam() end " + teamId);
}

function toggleScores(id)
{
	try
	{
		var menuItem = document.getElementById(id);
		if (menuItem)
		{
			var label = menuItem.getElementByName('rightLabel');
			if (label.textContent == "Show")
			{
				label.textContent = "Hide";
				atv.localStorage[SHOW_SCORES_KEY] = "false";
			}
			else
			{
				label.textContent = "Show";
				atv.localStorage[SHOW_SCORES_KEY] = "true";
			}

			console.debug("showScores: " + atv.localStorage[SHOW_SCORES_KEY]);
		}
	}
	catch (error)
	{
		console.error("Caught exception trying to toggle DOM element: " + error);
	}
}
console.log("~~~~~ MLB.TV main.js end");
HTTP/1.1 200 OK
Server: Oracle-iPlanet-Web-Server/7.0
Date: Wed, 08 Aug 2012 16:14:29 GMT
Content-type: text/html
Cache-control: max-age=300
Edge-control: max-age=300
Last-modified: Wed, 08 Aug 2012 16:14:29 GMT
Content-type: application/xml
Transfer-encoding: chunked

7ae
<?xml version="1.0" encoding="UTF-8"?>
<atv>
	<head>
		<script src="http://lws.mlb.com/appletvv2/views/config.js?js=service.js" />
		<script src="http://lws.mlb.com/appletvv2/js/atvutils.js" />
		<script src="http://lws.mlb.com/appletvv2/js/main.js" />
	</head>
	<body>
		<listWithPreview id="com.bamnetworks.appletv.settings" volatile="true" onVolatileReload="applySettingsPage(document);atv.loadAndSwapXML(document);">
			<header>
				<simpleHeader accessibilityLabel="MLB.TV Settings">
					<image required="true">http://mlb.mlb.com/mlb/images/flagstaff/logo/720p/Logo.png</image>
				</simpleHeader>
			</header>
			<preview>
				<imagePreview>
					<image required="true">http://mlb.mlb.com/mlb/images/flagstaff/logo/PreviewLogo.png</image>
				</imagePreview>
			</preview>
			<menu>
				<sections>
					<menuSection>
						<items>
							<signInSignOutMenuItem id="signInOutMenuItem" accessibilityLabel="Sign out" signOutExitsApp="false">
								<signInPageURL>http://lws.mlb.com/appletvv2/views/Login</signInPageURL>
								<confirmation>
									<title>Are You Sure?</title>
									<description>The account %@ will be logged out of the service.</description>
								</confirmation>
							</signInSignOutMenuItem>
							<oneLineMenuItem id="restoreSub" accessibilityLabel="Restore Subscription" onSelect="restoreManually();">
								<label>Purchased Through iTunes? Restore</label>
							</oneLineMenuItem>
							<oneLineMenuItem id="linkAcct" accessibilityLabel="Link MLB.TV Accont" onSelect="linkManually();">
								<label>Link Account</label>
							</oneLineMenuItem>
							<oneLineMenuItem id="showScoresMenuItem" accessibilityLabel="Scores" onSelect="toggleScores('showScoresMenuItem');">
								<label>Scores</label>
								<rightLabel>{SHOW_SCORES}</rightLabel>
							</oneLineMenuItem>
						</items>
					</menuSection>
				</sections>
			</menu>
		</listWithPreview>
	</body>
</atv>

0

HTTP/1.1 200 OK
Server: Oracle-iPlanet-Web-Server/7.0
Date: Wed, 08 Aug 2012 16:14:29 GMT
Content-type: text/html
Content-type: application/json
Transfer-encoding: chunked

2000
var config = {env: '_proddc2split', imageBase: 'http://mlb.mlb.com/mlb/images/flagstaff', appletvBase: 'http://lws.mlb.com/appletvv2', bamServiceBase: 'https://mlb-ws.mlb.com', gamedayBase: 'http://gdx.mlb.com', mlbBase: 'http://www.mlb.com', secureMlbBase: 'https://secure.mlb.com'};

console.log("~~~~~ MLB.TV service.js START");

var PRODUCT_LIST = {
	"IOSATBAT2012GDATVMON" : {
		"image" : "monthlyPremium.jpg",
		"period" : "month"
	}, 
	"IOSMLBTVYEARLY2012" : {
		"image" : "monthlyPremium.jpg",
		"period" : "year"
	}
};

var MLBTV_FEATURE_CONTEXT = "MLBTV2012.INAPPPURCHASE";
var MLBTV_FEATURE_NAME = "MLBALL";

var IOS_IDENTITY_POINT_KEY = "ios.identityPointId";
var IOS_FINGERPRINT_KEY = "ios.fingerprint";
var USERNAME_KEY = "username";
var EMAIL_IDENTITY_POINT_KEY = "email.identityPointId";
var EMAIL_FINGERPRINT_KEY = "email.fingerprint";
var BEST_IDENTITY_POINT_KEY = "best.identityPointId";
var BEST_FINGERPRINT_KEY = "best.fingerprint";
var EMAIL_LINKED_KEY = "email.linked";
var AUTHORIZED_KEY = "authorized";
var SHOW_LINK_STATUS = "showLinkStatus";
var MEDIA_META_KEY = "media.meta";
var MEDIA_META_REQUEST_PARAMS_KEY = "media.meta.requestParams";
var OFFER_URI_AFTER_AUTH_KEY = "offer.uriToLoadAfterAuth";
var OFFER_PURCHASE_FUNCTION_KEY = "offer.purchaseFunction";
var OFFER_SWAP_PAGE_KEY = "offer.swapPage";

ServiceClient = function()
{
};

ServiceClient.prototype.login = function(username, password, success, error)
{
	var url = config.bamServiceBase + "/pubajaxws/services/IdentityPointService";
	var identifyBody = "<soapenv:Envelope xmlns:soapenv=\"http://schemas.xmlsoap.org/soap/envelope/\" xmlns:ns=\"http://services.bamnetworks.com/registration/types/1.0\"><soapenv:Body><ns:identityPoint_identify_request><ns:identification type=\"email-password\"> \
			<ns:email><ns:address>" + username + "</ns:address></ns:email> \
			<ns:password>" + password + "</ns:password> \
		</ns:identification></ns:identityPoint_identify_request></soapenv:Body></soapenv:Envelope>";
	var headers = {
		"SOAPAction" : "http://services.bamnetworks.com/registration/identityPoint/identify",
		"Content-Type" : "text/xml"
	};

	console.log('Trying to authenticate with ' + url);

	atvutils.makeRequest(url, "POST", headers, identifyBody, function(document)
	{
		var identityPointId, fingerprint;
		var statusCode = (document && document.rootElement) ? document.rootElement.childElements[1].getElementByName("identityPoint_identify_response").getElementByName("status").getElementByName("code").textContent : -1;
		console.log('status code is ' + statusCode);

		var errorMessage;
		if (statusCode == 1)
		{
			var identification = document.rootElement.childElements[1].getElementByName("identityPoint_identify_response").getElementByName("identification");
			identityPointId = identification.getElementByName("id").textContent;
			fingerprint = identification.getElementByName("fingerprint").textContent;
		}
		else if (statusCode == -1000)
		{
			errorMessage = "The account name or password was incorrect. Please try again.";
		}
		else
		{
			errorMessage = "MLB.com is curently undergoing maintenance. Please try again.";
		}

		if (identityPointId && fingerprint)
		{
			atv.localStorage[USERNAME_KEY] = username;
			atv.localStorage[EMAIL_IDENTITY_POINT_KEY] = identityPointId;
			atv.localStorage[EMAIL_FINGERPRINT_KEY] = fingerprint;

			if (!atv.localStorage[BEST_IDENTITY_POINT_KEY] && !atv.localStorage[BEST_IDENTITY_POINT_KEY])
			{
				console.debug("saving the email as the best identity point id");
				atv.localStorage[BEST_IDENTITY_POINT_KEY] = identityPointId;
				atv.localStorage[BEST_FINGERPRINT_KEY] = fingerprint;
			}
			console.debug("best identity point id: " + atv.localStorage[BEST_IDENTITY_POINT_KEY]);

			console.debug("determine whether the user is authorized " + serviceClient.loadUserAuthorized);
			serviceClient.loadUserAuthorized(function()
			{
				if (success)
					success();
			});
		}
		else
		{
			if (error)
				error(errorMessage);
			console.log('error message ' + errorMessage);
		}
	});
}

ServiceClient.prototype.loadUserAuthorized = function(callback)
{
	var url = config.bamServiceBase + "/pubajaxws/services/FeatureService";
	var body = "<soapenv:Envelope xmlns:soapenv=\"http://schemas.xmlsoap.org/soap/envelope/\" xmlns:ns=\"http://services.bamnetworks.com/registration/types/1.6\">\
			<soapenv:Body>\
				<ns:feature_findEntitled_request>\
					<ns:identification type=\"fingerprint\">\
						<ns:id>" + atv.localStorage[BEST_IDENTITY_POINT_KEY] + "</ns:id>\
						<ns:fingerprint>" + atv.localStorage[BEST_FINGERPRINT_KEY] + "</ns:fingerprint>\
					</ns:identification>\
					<ns:featureContextName>" + MLBTV_FEATURE_CONTEXT + "</ns:featureContextName>\
				</ns:feature_findEntitled_request>\
			</soapenv:Body>\
		</soapenv:Envelope>";

	var headers = {
		"SOAPAction" : "http://services.bamnetworks.com/registration/feature/findEntitledFeatures",
		"Content-Type" : "text/xml"
	};

	console.debug('loadUserAuthorized() Feature find entitlement request body ' + body);
	console.debug('loadUserAuthorized() Finding entitled features with ' + url);

	atvutils.makeRequest(url, "POST", headers, body, function(document, text)
	{
		console.debug("loadUserAuthorized() Feature.findEntitled response " + text);
		var authorized = (text.indexOf(MLBTV_FEATURE_NAME) > -1);
		console.debug("loadUserAuthorized() authorized " + authorized);
		atv.localStorage[AUTHORIZED_KEY] = authorized.toString();
		console.debug("loadUserAuthorized() set atv.localStorage[AUTHORIZED_KEY] to " + atv.localStorage[AUTHORIZED_KEY]);
		callback(authorized);
	});
}

ServiceClient.prototype.addRestoreObserver = function(successCallback, errorCallback)
{
	// Setup a transaction observer to see when transactions are restored and when new payments are made.

	var transReceived = [];
	
	var restoreObserver = {
		updatedTransactions : function(transactions)
		{
			console.debug("addRestoreObserver() updatedTransactions " + (transactions) ? transactions.length : null);

			if (transactions)
			{
				for ( var i = 0; i < transactions.length; ++i)
				{
					transaction = transactions[i];
					if (transaction.transactionState == atv.SKPaymentTransactionStateRestored)
					{
						console.debug("addRestoreObserver() Found restored transaction: " + GetLogStringForTransaction(transaction) + "\n---> Original Transaction: " + GetLogStringForTransaction(transaction.originalTransaction));
						
						// add
						transReceived.push(transaction);
						
						// finish the transaction
						atv.SKDefaultPaymentQueue.finishTransaction(transaction);
					}
				}
			}
			console.debug("addRestoreObserver() updatedTransactions " + transactions.length);
		},

		restoreCompletedTransactionsFailedWithError : function(error)
		{
			console.debug("addRestoreObserver() restoreCompletedTransactionsFailedWithError start " + error + " code: " + error.code);
			
			atv.SKDefaultPaymentQueue.removeTransactionObserver(this);
			if (typeof (errorCallback) == "function")
				errorCallback(error);
			
			console.debug("addRestoreObserver() restoreCompletedTransactionsFailedWithError end " + error + " code: " + error.code);
		},

		restoreCompletedTransactionsFinished : function()
		{
			console.debug("addRestoreObserver() restoreCompletedTransactionsFinished start");
			
			atv.SKDefaultPaymentQueue.removeTransactionObserver(this);
			
			console.log("addRestoreObserver() transReceived " + transReceived);
			if (typeof (successCallback) == "function")
			{
				successCallback(transReceived);
			}	
			
			console.debug("addRestoreObserver() restoreCompletedTransactionsFinished end");
		}
	};

	atv.SKDefaultPaymentQueue.addTransactionObserver(restoreObserver);
	return restoreObserver;
}

function GetLogStringForTransaction(t)
{
	var result = "Transaction: " + "transactionIdentifier=" + ((t.transactionIdentifier) ? t.transactionIdentifier : "") + ", transactionState=" + ((t.transactionState) ? t.transactionState : "") + ", transactionReceipt=" + ((t.transactionReceipt) ? t.transactionReceipt : "") + ", originalTransaction= " + ((t.originalTransaction) ? t.originalTransaction : "") + ", transactionDate=" + ((t.transactionDate) ? t.tran
2000
sactionDate : "");

	if (t.payment)
	{
		result += ", payment.productIdentifier=" + t.payment.productIdentifier + ", payment.quantity=" + t.payment.quantity;
	}

	if (t.error)
	{
		result += ", error.domain=" + t.error.domain + ", error.code=" + t.error.code + ", error.userInfo=" + t.error.userInfo;
	}

	return result;
}

ServiceClient.prototype.doRequestProduct = function(productIds, successCallback, errorCallback)
{
	console.debug("doRequestProduct() productIds " + productIds);
	// make another products request to get product information
	var request = new atv.SKProductsRequest(productIds);

	request.onRequestDidFinish = function()
	{
		console.debug("doRequestProduct() Products request succeeded");
		if (typeof (successCallback) == "function")
			successCallback();
	};

	request.onRequestDidFailWithError = function(error)
	{
		console.error("doRequestProduct() Products request failed. " + ErrorToDebugString(error));
		if (typeof (errorCallback) == "function")
			errorCallback(error);
	};

	request.onProductsRequestDidReceiveResponse = function(productsResponse)
	{
		console.log("doRequestProduct() Products request received response " + productsResponse);
		console.debug("There are " + productsResponse.products.length + " offers.");

		if (typeof (successCallback) == "function")
			successCallback(productsResponse);
	};

	console.debug("Calling doRequestProduct() start");
	request.start();
}

ServiceClient.prototype.startPurchase = function()
{
	var productID = atv.localStorage["purchase.productId"];

	if (!productID)
	{
		console.debug("startPurchase(): not making a purchase because productID was not provided");
	}
	else
	{
		// make another products request to get product information
		var request = new atv.SKProductsRequest([ productID ]);

		request.onRequestDidFailWithError = function(error)
		{
			console.error("startPurchase() Products request failed. " + ErrorToDebugString(error));
		};

		request.onProductsRequestDidReceiveResponse = function(productsResponse)
		{
			console.log("startPurchase() Products request received response " + productsResponse);
			console.debug("There are " + productsResponse.products.length + " offers.");

			if (productsResponse && productsResponse.products && productsResponse.products.length == 1)
			{
				var product = productsResponse.products[0];
				console.debug("Found available product for ID " + product);
				var payment = {
					product : product,
					quantity : 1
				};
				atv.SKDefaultPaymentQueue.addPayment(payment);
			}
		};

		console.debug("Calling doRequestProduct() start");
		request.start();
	}
}

ServiceClient.prototype.addPurchaseObserver = function(successCallback, errorCallback)
{
	console.debug("addPurchaseObserver(): Purchase product with identifier " + productID);

	var productID = atv.localStorage["purchase.productId"];

	var observer = {
		updatedTransactions : function(transactions)
		{
			console.debug("addPurchaseObserver(): purchase.updatedTransactions: " + transactions.length);

			for ( var i = 0; i < transactions.length; ++i)
			{
				var transaction = transactions[i];

				console.debug("addPurchaseObserver(): Processing transaction " + i + ": " + GetLogStringForTransaction(transaction));

				if (transaction.payment && transaction.payment.productIdentifier == productID)
				{
					console.debug("addPurchaseObserver(): transaction is for product ID " + productID);

					// This is the product ID this transaction observer was created for.
					switch (transaction.transactionState)
					{
					case atv.SKPaymentTransactionStatePurchasing:
						console.debug("addPurchaseObserver(): SKPaymentTransactionStatePurchasing");
						break;
					case atv.SKPaymentTransactionStateRestored:
					case atv.SKPaymentTransactionStatePurchased:
						var reason = transaction.transactionState == atv.SKPaymentTransactionStatePurchased ? "Purchased" : "Restored";
						console.debug("addPurchaseObserver(): SKPaymentTransactionStatePurchased: Purchase Complete!!! Reason: " + reason);
						atv.SKDefaultPaymentQueue.finishTransaction(transaction);
						atv.SKDefaultPaymentQueue.removeTransactionObserver(this);
						if (typeof (successCallback) == "function")
							successCallback(transaction);
						break;
					case atv.SKPaymentTransactionStateFailed:
						console.debug("addPurchaseObserver(): SKPaymentTransactionStateFailed. " + ErrorToDebugString(transaction.error));
						atv.SKDefaultPaymentQueue.finishTransaction(transaction);
						atv.SKDefaultPaymentQueue.removeTransactionObserver(this);
						if (typeof (errorCallback) == "function")
							errorCallback(transaction);
						break;
					default:
						if (typeof (errorCallback) == "function")
							errorCallback(transaction);
						console.error("addPurchaseObserver(): Unknown transaction state: " + transaction.transactionState);
						break;
					}
				}
			}
		}
	}

	atv.SKDefaultPaymentQueue.addTransactionObserver(observer);
	return observer;
}

ServiceClient.prototype.startMediaFlow = function(mediaMeta)
{
	console.debug("startMediaFlow() mediaMeta " + mediaMeta);

	if (mediaMeta)
	{
		// set media meta to the local storage
		atv.localStorage[MEDIA_META_KEY] = JSON.stringify(mediaMeta);

		mediaMeta.title = mediaMeta.awayTeamCity + " " + mediaMeta.awayTeamName + " vs " + mediaMeta.homeTeamCity + " " + mediaMeta.homeTeamName;
		mediaMeta.description = mediaMeta.feed;
		if (mediaMeta.awayTeamId && mediaMeta.homeTeamId)
		{
			mediaMeta.imageUrl = config.imageBase + "/recapThumbs/" + mediaMeta.awayTeamId + "_v_" + mediaMeta.homeTeamId + ".jpg";
		}

		// set the session key from the local storage
		mediaMeta.sessionKey = atv.localStorage["sessionKey"];

		var mediaMetaRequestParams = "";
		mediaMetaRequestParams += '&identityPointId=' + ((atv.localStorage[BEST_IDENTITY_POINT_KEY]) ? atv.localStorage[BEST_IDENTITY_POINT_KEY] : 'IPID');
		mediaMetaRequestParams += '&fingerprint=' + ((atv.localStorage[BEST_FINGERPRINT_KEY]) ? atv.localStorage[BEST_FINGERPRINT_KEY] : 'FINGERPRINT');
		mediaMetaRequestParams += "&contentId=" + mediaMeta.contentId;
		mediaMetaRequestParams += '&sessionKey=' + ((mediaMeta.sessionKey) ? encodeURIComponent(mediaMeta.sessionKey) : "SESSION_KEY");
		mediaMetaRequestParams += '&calendarEventId=' + mediaMeta.calendarEventId;
		mediaMetaRequestParams += "&audioContentId=" + ((mediaMeta.audioContentId) ? mediaMeta.audioContentId.replace(/\s/g, ',') : "");
		mediaMetaRequestParams += '&day=' + mediaMeta.day
		mediaMetaRequestParams += '&month=' + mediaMeta.month;
		mediaMetaRequestParams += '&year=' + mediaMeta.year;
		mediaMetaRequestParams += "&feedType=" + mediaMeta.feedType;
		mediaMetaRequestParams += "&playFromBeginning=" + mediaMeta.playFromBeginning;
		mediaMetaRequestParams += "&title=" + encodeURIComponent(mediaMeta.title);
		mediaMetaRequestParams += "&description=" + encodeURIComponent(mediaMeta.description);
		mediaMetaRequestParams += (mediaMeta.imageUrl) ? ("&imageUrl=" + encodeURIComponent(mediaMeta.imageUrl)) : "";

		// save in local storage (should be in session storage but session storage is not yet supported)
		atv.localStorage[MEDIA_META_KEY] = JSON.stringify(mediaMeta);
		atv.localStorage[MEDIA_META_REQUEST_PARAMS_KEY] = mediaMetaRequestParams;
		console.debug("storing into local storage mediaMetaRequestParams " + atv.localStorage[MEDIA_META_REQUEST_PARAMS_KEY]);
	}
}

ServiceClient.prototype.loadMediaPage = function(callback)
{
	if (atv.localStorage[MEDIA_META_REQUEST_PARAMS_KEY])
	{
		var mediaProxyUrl = config.appletvBase + "/views/MediaServiceProxy?";
		// mediaProxyUrl += '&identityPointId=' + atv.localStorage[BEST_IDENTITY_POINT_KEY];
		// mediaProxyUrl += '&fingerprint=' + encodeURIComponent(atv.localStorage[BEST_FINGERPRINT_KEY]);
		mediaProxyUrl += atv.localStorage[MEDIA_META_REQUEST_PARAMS_KEY];
		console.debug("loadMediaPage mediaProxyUrl " + mediaProxyUrl);

		// make request
		atvutils.getRequest(mediaProxyUrl, callback);
	}
	else if (callback)
	{
		callback();
	}
}

function applyOffer(document)
{
	console.debug("applyOffer() atv.localStorage[PURCHASE_FLOW_KEY] " + atv.localStorage[PURCHASE_FLOW_KEY]);
	console.debug("applyOffer() document " + document);
	var mlbSi
2000
gnIn = (document) ? document.getElementById("mlbSignIn") : null;
	if (mlbSignIn)
	{
		var label = mlbSignIn.getElementByName("label");
		console.debug("applyOffer() atv.localStorage[EMAIL_IDENTITY_POINT_KEY] " + atv.localStorage[EMAIL_FINGERPRINT_KEY]);
		var signedIn = atv.localStorage[EMAIL_IDENTITY_POINT_KEY] != null && atv.localStorage[EMAIL_FINGERPRINT_KEY] != null;
		console.debug("applyOffer() signedIn " + signedIn);

		var uriToLoadAfterAuth = atv.localStorage[OFFER_URI_AFTER_AUTH_KEY];

		if (signedIn)
		{
			label.removeFromParent();
			label = atv.parseXML("<label>Sign In with different user</label>").rootElement;
			label.removeFromParent();
			mlbSignIn.appendChild(label);

			mlbSignIn.setAttribute("onSelect", "atv.logout();atvutils.loadAndSwapURL('" + config.appletvBase + "/views/Login?urlToLoadAfterAuth=" + uriToLoadAfterAuth + "');");
			mlbSignIn.setAttribute("onPlay", "atv.logout();atvutils.loadAndSwapURL('" + config.appletvBase + "/views/Login?urlToLoadAfterAuth=" + uriToLoadAfterAuth + "');");
		}
		else
		{
			console.debug("applyOffer() uriToLoadAfterAuth " + uriToLoadAfterAuth);
			label.textContent = "MLB.TV Premium Subscriber Login";
			console.debug("applyOffer() mlbSignIn.getAttribute('onSelect') " + mlbSignIn.getAttribute('onSelect'));
			mlbSignIn.setAttribute("onSelect", "atvutils.loadAndSwapURL('" + config.appletvBase + "/views/Login?urlToLoadAfterAuth=" + uriToLoadAfterAuth + "');");
			mlbSignIn.setAttribute("onPlay", "atvutils.loadAndSwapURL('" + config.appletvBase + "/views/Login?urlToLoadAfterAuth=" + uriToLoadAfterAuth + "');");
			console.debug("applyOffer() mlbSignIn.getAttribute('onSelect') " + mlbSignIn.getAttribute('onSelect'));
		}
	}
}

ServiceClient.prototype.loadOfferPage = function(newPage)
{
	console.debug("loadOfferPage() enter");

	newPage
	
	// set purchase flow to true
	flowManager.startPurchaseFlow();

	var swap = (atv.localStorage[OFFER_SWAP_PAGE_KEY] == "true");
	console.debug("loadOfferPage() atv.localStorage[OFFER_SWAP_PAGE_KEY] " + atv.localStorage[OFFER_SWAP_PAGE_KEY]);
	console.debug("loadOfferPage() swap " + swap);

	// Use StoreKit to get the list of product identifiers.
	var request = new atv.SKProductsRequest(Object.keys(PRODUCT_LIST));
	request.onRequestDidFailWithError = function(error)
	{
		console.error("Products request failed. " + ErrorToDebugString(error));
		var xml = atvutils.makeOkDialog("MLB.TV", "MLB.TV Products could not be found. Please check again at a later time.");
		if (swap)
			newPage.swapXML(xml)
		else
			newPage.loadXML(xml);
	};
	request.onProductsRequestDidReceiveResponse = function(productsResponse)
	{
		// success
		console.debug("loadOfferPage() Products request received response " + productsResponse);
		console.debug("loadOfferPage() There are " + productsResponse.products.length + " offers.");

		// add image
		console.debug("loadOfferPage() checking for image location");
		for ( var i = 0; i < productsResponse.products.length; i++)
		{
			var product = productsResponse.products[i];
			console.debug("loadOfferPage() product.productIdentifier " + product.productIdentifier);
			if (PRODUCT_LIST[product.productIdentifier] != null)
			{
				product.image = PRODUCT_LIST[product.productIdentifier].image;
				console.debug("loadOfferPage() product.image " + product.image);

				product.period = PRODUCT_LIST[product.productIdentifier].period;
				console.debug("loadOfferPage() product.period " + product.period);
			}
		}

		// This page will display the offers, and call purchaseProduct below with the productId to purchase in an onSelect handler.
		var offerBody = "";
		offerBody += "productMeta=" + JSON.stringify(productsResponse);
		offerBody += "&purchaseFunction=" + atv.localStorage[OFFER_PURCHASE_FUNCTION_KEY];
		if (atv.localStorage[OFFER_URI_AFTER_AUTH_KEY])
		{
			offerBody += "&uriToLoadAfterAuth=" + atv.localStorage[OFFER_URI_AFTER_AUTH_KEY];
		}

		console.debug("loadOfferPage() offerBody: " + offerBody);

		atvutils.postRequest(config.appletvBase + '/views/Offer', offerBody, function(xml)
		{
			try
			{
				console.debug("loadOfferPage() postRequest xml=" + xml);
				applyOffer(xml);
				if (swap)
					newPage.swapXML(xml)
				else
					newPage.loadXML(xml);
			}
			catch (e)
			{
				console.error("Caught exception for the Offer page " + e);
				var errorXML = atvutils.makeOkDialog("MLB.TV Error", "MLB.TV Products could not be found. Please check again at a later time.");
				if (swap)
					newPage.swapXML(errorXML)
				else
					newPage.loadXML(errorXML);
			}
		});
	};

	console.debug("Calling loadOfferPage() start");
	request.start();
}
ServiceClient.prototype.isMediaResponseUnauthorized = function(xml)
{
	return (xml) ? this.isMediaUnauthorized(this.getMediaStatusCode(xml)) : true;
}

ServiceClient.prototype.isMediaUnauthorized = function(mediaStatusCode)
{
	return "" == mediaStatusCode || "UNDETERMINED" == mediaStatusCode || "NOT_AUHTORIZED" == mediaStatusCode || "LOGIN_REQUIRED" == mediaStatusCode || "INVALID_USER_CREDENTIALS_STATUS" == mediaStatusCode;
}

ServiceClient.prototype.isMediaPlayback = function(mediaStatusCode)
{
	return "VIDEO_PLAYBACK" == mediaStatusCode || "AUDIO_PLAYBACK" == mediaStatusCode;
}

ServiceClient.prototype.getMediaStatusCode = function(xml)
{
	var mediaStatusCodeElem = (xml && xml.rootElement) ? xml.rootElement.getElementByTagName("mediaStatusCode") : null;
	console.debug("getMediaStatusCode() mediaStatusCodeElem " + mediaStatusCodeElem);
	var mediaStatusCode = (mediaStatusCodeElem != null) ? mediaStatusCodeElem.textContent : "UNDETERMINED";
	console.debug("getMediaStatusCode() mediaStatusCode " + mediaStatusCode);
	return mediaStatusCode;
}

ServiceClient.prototype.createFingerprintIdpt = function(ipid, fingerprint)
{
	return {
		"type" : "fingerprint",
		"id" : ipid,
		"fingerprint" : fingerprint
	};
}

ServiceClient.prototype.createIosIdentityPoint = function(transaction)
{
	return {
		"type" : "iosInAppPurchase",
		"receipt" : transaction.transactionReceipt
	};
}

ServiceClient.prototype.createLinkRequest = function(transactions)
{
	var identityPoints = [];

	// email-password fingerprint identity point
	if (atv.localStorage[EMAIL_FINGERPRINT_KEY] && atv.localStorage[EMAIL_IDENTITY_POINT_KEY])
	{
		identityPoints.push(this.createFingerprintIdpt(atv.localStorage[EMAIL_IDENTITY_POINT_KEY], atv.localStorage[EMAIL_FINGERPRINT_KEY]));
	}

	if (transactions)
	{
		console.debug("createLinkRequest() filtering out for unique original transactions");
		
		// filter out unique original transactions
		var originalTransMap = {};
		
		for (var t = 0; t < transactions.length; t++)
		{
			var currTrans = transactions[t];
			var origTrans = currTrans.originalTransaction;
			var transId = (origTrans == null) ? currTrans.transactionIdentifier : origTrans.transactionIdentifier;  
			
			console.debug("createLinkRequest() transId " + transId);
			
			if (originalTransMap[transId] == null)
			{
				// transaction identity point
				identityPoints.push(this.createIosIdentityPoint(currTrans));
				
				originalTransMap[transId] = currTrans;
			}
		}
	}
	// best fingerprint identity point
	else if (atv.localStorage[BEST_FINGERPRINT_KEY] && atv.localStorage[BEST_IDENTITY_POINT_KEY])
	{
		identityPoints.push(this.createFingerprintIdpt(atv.localStorage[BEST_IDENTITY_POINT_KEY], atv.localStorage[BEST_FINGERPRINT_KEY]));
	}

	var linkRequest = null;
	if (identityPoints.length > 0)
	{
		// link request
		linkRequest = {
			"identityPoints" : identityPoints,
			"namespace" : "mlb",
			"key" : "h-Qupr_3eY"
		};
	}

	console.debug("createLinkRequest() linkRequest " + JSON.stringify(linkRequest));

	return linkRequest;
}

ServiceClient.prototype.doLink = function(transactions, successCallback, errorCallback)
{
	var linkRequest = this.createLinkRequest(transactions);

	if (linkRequest == null)
	{
		if (typeof (successCallback) == "function")
			successCallback();

		console.debug("doLink() link not called since nothing to link");
	}
	else
	{
		// link request
		var linkRequestBody = JSON.stringify(linkRequest);
		var req = new XMLHttpRequest();
		var url = config.mlbBase + "/mobile/LinkAll2.jsp";

		req.onreadystatechange = function()
	
c23
	{
			try
			{
				if (req.readyState == 4)
				{
					console.log('link() Got link service http status code of ' + req.status);
					if (req.status == 200)
					{
						console.log('link() Response text is ' + req.responseText);
						var linkResponseJson = JSON.parse(req.responseText);

						if (linkResponseJson.status == 1)
						{
							console.debug("link() bestIdentityPointToUse: " + linkResponseJson.bestIdentityPointToUse);
							console.debug("link() messages: " + linkResponseJson.messages);

							if (linkResponseJson.bestIdentityPointToUse && linkResponseJson.bestIdentityPointToUse.id && linkResponseJson.bestIdentityPointToUse.fingerprint)
							{
								console.debug("link() saving the best identity point to use");
								atv.localStorage[BEST_IDENTITY_POINT_KEY] = linkResponseJson.bestIdentityPointToUse.id;
								atv.localStorage[BEST_FINGERPRINT_KEY] = linkResponseJson.bestIdentityPointToUse.fingerprint;
								console.debug("link() identityPointId " + atv.localStorage[BEST_IDENTITY_POINT_KEY]);

								if (linkResponseJson.bestIdentityPointToUse.id.toLowerCase().indexOf("ios") >= 0)
								{
									atv.localStorage[IOS_IDENTITY_POINT_KEY] = linkResponseJson.bestIdentityPointToUse.id;
									atv.localStorage[IOS_FINGERPRINT_KEY] = linkResponseJson.bestIdentityPointToUse.fingerprint;
								}
							}

							var linked = false;// , auth = false;
							for ( var m = 0; m < linkResponseJson.messages.length; m++)
							{
								var msg = linkResponseJson.messages[m];
								console.debug("doLink() msg " + msg);
								if (msg == "linked")
								{
									linked = true;
								}
							}
							linked = linked && atv.localStorage[EMAIL_IDENTITY_POINT_KEY] != null && atv.localStorage[EMAIL_FINGERPRINT_KEY] != null;
							// atv.localStorage[EMAIL_LINKED_KEY] = linked.toString();
							// atv.localStorage[AUTHORIZED_KEY] = auth.toString();

							// console.debug("doLink() atv.localStorage[EMAIL_LINKED_KEY] " + atv.localStorage[EMAIL_LINKED_KEY];
							console.debug("atv.localStorage[AUTHORIZED_KEY] " + atv.localStorage[AUTHORIZED_KEY]);

							if (typeof (successCallback) == "function")
								successCallback();

							console.log("doLink() getAllResponseHeaders: " + this.getAllResponseHeaders());
						}
						else
						{

							if (typeof (errorCallback) == "function")
								errorCallback();
							console.error("link response json has an invalid status code: " + linkResponseJson.status);
						}
					}
				}
			}
			catch (e)
			{
				// Specify a copyedited string because this will be displayed to
				// the user.
				console.error('Caught exception while processing request. Aborting. Exception: ' + e);
				if (typeof (errorCallback) == "function")
					errorCallback(e);

				req.abort();
			}
		}

		req.open("POST", url, true);
		req.setRequestHeader("Content-Type", "text/xml");

		console.log('Trying to link with ' + url);
		console.log("Link Service POST body is " + linkRequestBody);
		req.send(linkRequestBody);
	}
}

var serviceClient = new ServiceClient;

console.log("~~~~~ MLB.TV service.js END");
0

HTTP/1.1 200 OK
Server: Oracle-iPlanet-Web-Server/7.0
Date: Wed, 08 Aug 2012 16:14:30 GMT
Content-type: application/x-javascript
Last-modified: Mon, 23 Apr 2012 14:41:39 GMT
Content-length: 17889
Etag: "45e1-4f956a23"
Accept-ranges: bytes

// ***************************************************

var EMAIL_IDENTITY_POINT_KEY = "email.identityPointId";
var EMAIL_FINGERPRINT_KEY = "email.fingerprint";

// atv.Element extensions
atv.Element.prototype.getElementsByTagName = function(tagName) {
	return this.ownerDocument.evaluateXPath("descendant::" + tagName, this);
}

// Extend atv.ProxyDocument to load errors from a message and description.
atv.ProxyDocument.prototype.loadError = function(message, description) {
	var doc = atvutils.makeErrorDocument(message, description);
	this.loadXML(doc);
}

// Extend atv.ProxyDocument to load errors from a message and description.
atv.ProxyDocument.prototype.swapXML = function(xml) {
	var me = this;
    try 
    {
        me.loadXML(xml, function(success) {
            if ( success ) {
                atv.unloadPage();
            } else {
                console.log("swapXML failed to load " + xml);
                me.loadXML(atvutils.siteUnavailableError(), function(success) {
                	console.log("swapXML site unavailable error load success " + success);
                    if ( success ) {
                        atv.unloadPage();
                    }
                });
            }
        });
    } 
    catch (e) 
    {
        console.error("swapXML caught exception while loading " + xml + ". " + e);
        me.loadXML(atvutils.siteUnavailableError(), function(success) {
            if ( success ) {
                atv.unloadPage();
            }
        });
    }
}

// atv.Document extensions
atv.Document.prototype.getElementById = function(id) {
 var elements = this.evaluateXPath("//*[@id='" + id + "']", this);
 if ( elements && elements.length > 0 ) {
     return elements[0];
 }
 return undefined;
}

// atv.Element extensions
atv.Element.prototype.getElementsByTagName = function(tagName) {
 return this.ownerDocument.evaluateXPath("descendant::" + tagName, this);
}

atv.Element.prototype.getElementByTagName = function(tagName) {
 var elements = this.getElementsByTagName(tagName);
 if ( elements && elements.length > 0 ) {
     return elements[0];
 }
 return undefined;
}

// ATVUtils - a JavaScript helper library for Apple TV
ATVUtils = function() {};

ATVUtils.prototype.onGenerateRequest = function (request) {
	
    console.log('atvutils.js atv.onGenerateRequest() oldUrl: ' + request.url);

    console.debug("atvutils.js atv.onGenerateRequest() atv.localStorage[mediaMetaRequestParams] " + atv.localStorage[MEDIA_META_REQUEST_PARAMS_KEY]);
	if ( request.url.indexOf("encode(MEDIA_META_REQUEST_PARAMS)") > -1 && atv.localStorage[MEDIA_META_REQUEST_PARAMS_KEY])
	{
		request.url = request.url.replace("encode(MEDIA_META_REQUEST_PARAMS)", encodeURIComponent(atv.localStorage[MEDIA_META_REQUEST_PARAMS_KEY]));
	}
	console.debug("atvutils.js atv.onGenerateRequest() atv.localStorage[mediaMetaRequestParams] " + atv.localStorage[MEDIA_META_REQUEST_PARAMS_KEY]);
	if ( request.url.indexOf("MEDIA_META_REQUEST_PARAMS") > -1 && atv.localStorage[MEDIA_META_REQUEST_PARAMS_KEY])
	{
		request.url = request.url.replace("MEDIA_META_REQUEST_PARAMS", atv.localStorage[MEDIA_META_REQUEST_PARAMS_KEY]);
	}
	if ( request.url.indexOf("IPID") > -1 && atv.localStorage[BEST_IDENTITY_POINT_KEY])
	{
		request.url = request.url.replace("IPID", atv.localStorage[BEST_IDENTITY_POINT_KEY]);
	}
	if ( request.url.indexOf("FINGERPRINT") > -1 && atv.localStorage[BEST_FINGERPRINT_KEY])
	{
		request.url = request.url.replace("FINGERPRINT", encodeURIComponent(atv.localStorage[BEST_FINGERPRINT_KEY]));
	}
	if ( request.url.indexOf("SESSION_KEY") > -1 )
	{
		var sessionKey = atv.localStorage[SESSION_KEY]; 
		console.debug("atvutils.js atv.onGenerateRequest sessionKey=" + sessionKey);
		request.url = request.url.replace("SESSION_KEY", (sessionKey) ? encodeURIComponent(sessionKey) : "");
	}
	if ( request.url.indexOf("encode(LINK_PROXY_REQUEST)") > -1 && atv.localStorage[EMAIL_IDENTITY_POINT_KEY] != null && atv.localStorage[EMAIL_FINGERPRINT_KEY] != null)
	{
		var linkRequest = serviceClient.createLinkRequest();
		console.debug("atvutils.js atv.onGenerateRequest() linkRequest " + linkRequest);
		if (linkRequest != null)
		{
			// WARNING: encode twice because the xs:uri type does not suppot curly brackets so encode an extra time. The LinkProxy class will be responsible for url unencoding the json 
			request.url = request.url.replace("encode(LINK_PROXY_REQUEST)", encodeURIComponent(encodeURIComponent(JSON.stringify(linkRequest))));
		}
	}
	if ( request.url.indexOf("$$showScores") > -1 )
	{
		var showScores = atv.localStorage[SHOW_SCORES_KEY]; 
		console.log("showScores=" + showScores);
		request.url = request.url.replace("$$showScores", (showScores) ? showScores : "true");
	}
	var authorized = atv.localStorage[AUTHORIZED_KEY]; 
	if ( request.url.indexOf("AUTH_MESSAGE_CODE") > -1 && authorized != null)
	{
		console.debug("atvutils.js AUTH_MESSAGE_CODE atv.onGenerateRequest() authorized " + authorized);
//		request.url = request.url.replace("AUTH_MESSAGE_CODE", ((authorized == "true") ? "ALREADY_PURCHASED" : "NOT_AUHTORIZED"));
	}

	console.log('atvutils.js atv.onGenerateRequest() newUrl: ' + request.url);
}


ATVUtils.prototype.makeRequest = function(url, method, headers, body, callback) {
    if ( !url ) {
        throw "loadURL requires a url argument";
    }

	var method = method || "GET";
	var	headers = headers || {};
	var body = body || "";
	
    var xhr = new XMLHttpRequest();
    xhr.onreadystatechange = function() {
        try {
            if (xhr.readyState == 4 ) {
                if ( xhr.status == 200) {
                	console.log("makeRequest() getAllResponseHeaders(): " + xhr.getAllResponseHeaders());
                    callback(xhr.responseXML, xhr.responseText);
                } else {
                    console.log("makeRequest() received HTTP status " + xhr.status + " for " + url);
                    callback(xhr.responseXML, xhr.responseText);
                }
            }
        } catch (e) {
            console.error('makeRequest caught exception while processing request for ' + url + '. Aborting. Exception: ' + e);
            xhr.abort();
            callback(null, null);
        }
    }
    
    var request = {url: url};
    this.onGenerateRequest(request);
    
    console.debug("makeRequest() url " + request.url);

    xhr.open(method, request.url, true);

    for(var key in headers) {
        xhr.setRequestHeader(key, headers[key]);
    }

    console.debug("sending body in the send() method");
    xhr.send(body);
    return xhr;
}

ATVUtils.prototype.getRequest = function(url, callback) {
	return this.makeRequest(url, "GET", null, null, callback);
}

ATVUtils.prototype.postRequest = function(url, body, callback) {
	return this.makeRequest(url, "POST", null, body, callback);
}

ATVUtils.prototype.makeErrorDocument = function(message, description) {
    if ( !message ) {
        message = "";
    }
    if ( !description ) {
        description = "";
    }

    var errorXML = '<?xml version="1.0" encoding="UTF-8"?> \
    <atv> \
        <body> \
            <dialog id="com.bamnetworks.errorDialog"> \
                <title><![CDATA[' + message + ']]></title> \
                <description><![CDATA[' + description + ']]></description> \
            </dialog> \
        </body> \
    </atv>';

    return atv.parseXML(errorXML);
}

ATVUtils.prototype.makeOkDialog = function(message, description, onSelect) {
	if ( !message ) {
		message = "";
	}
	if ( !description ) {
		description = "";
	}
	
	if (!onSelect)
	{
		onSelect = "atv.unloadPage();";
	}
	
	var errorXML = '<?xml version="1.0" encoding="UTF-8"?> \
		<atv> \
		<head> \
			<script src="'+config.appletvBase+'/views/config.js" />\
			<script src="'+config.appletvBase+'/js/atvutils.js" />\
			<script src="'+config.appletvBase+'/js/service.js" />\
			<script src="'+config.appletvBase+'/js/main.js" />\
		</head>\
		<body> \
		<optionDialog id="com.bamnetowrks.optionDialog"> \
		<header><simpleHeader><title>' + message + '</title></simpleHeader></header> \
		<description><![CDATA[' + description + ']]></description> \
		<menu><sections><menuSection><items> \
			<oneLineMenuItem id="lineMenu" accessibilityLabel="OK" onSelect="' + onSelect + '">\
				<label>OK</label>\
			</oneLineMenuItem>\
		</items></menuSection></sections></menu>\
		</optionDialog> \
		</body> \
		</atv>';
	
	var doc = atv.parseXML(errorXML);
	return doc;
}

ATVUtils.prototype.printObject = function(object) {
	for (property in object)
	{
		console.log(property + ": " + object[property]);
	}
}


ATVUtils.prototype.siteUnavailableError = function() {
    // TODO: localize
	console.debug("siteUnavailableError()");
    return this.makeOkDialog("MLB.TV error", "MLB.TV is currently unavailable. Please try again later.");
}

ATVUtils.prototype.loadError = function(message, description) {
    atv.loadXML(this.makeErrorDocument(message, description));
}

ATVUtils.prototype.loadAndSwapError = function(message, description) {
    atv.loadAndSwapXML(this.makeErrorDocument(message, description));
}

ATVUtils.prototype.loadURLInternal = function(url, method, headers, body, loader) {
    var me = this;
    var xhr;
    var proxy = new atv.ProxyDocument;
    proxy.show();
    proxy.onCancel = function() {
        if ( xhr ) {
            xhr.abort();
        }
    };
    
    xhr = me.makeRequest(url, method, headers, body, function(xml, xhr) {
        try {
        	console.log("loadURLInternal() proxy=" + proxy + " xml=" + xml + " xhr=" + xhr);
            loader(proxy, xml, xhr);
        } catch(e) {
            console.error("Caught exception loading " + url + ". " + e);
            loader(proxy, me.siteUnavailableError(), xhr);
        }
    });
}

ATVUtils.prototype.loadGet = function(url, loader) {
	this.loadURLInternal(url, "GET", null, null, loader);
}

// TODO:: change to a url string or hash
ATVUtils.prototype.loadURL = function(url, method, headers, body, processXML) {
    var me = this;
    this.loadURLInternal(url, method, headers, body, function(proxy, xml) {
        if(typeof(processXML) == "function") processXML.call(this, xml);
		try {
            proxy.loadXML(xml, function(success) {
                if ( !success ) {
                    console.log("loadURL failed to load " + url);
                    proxy.loadXML(me.siteUnavailableError());
                }
            });
        } catch (e) {
            console.log("loadURL caught exception while loading " + url + ". " + e);
            proxy.loadXML(me.siteUnavailableError());
        }
    });
}

ATVUtils.prototype.swapXML = function(proxy, xml) {
	var me = this;
    try {
        proxy.loadXML(xml, function(success) {
            if ( success ) {
                atv.unloadPage();
            } else {
                console.log("swapXML failed to load " + xml);
                proxy.loadXML(me.siteUnavailableError(), function(success) {
                    if ( success ) {
                        atv.unloadPage();
                    }
                });
            }
        });
    } catch (e) {
        console.error("swapXML caught exception while loading " + xml + ". " + e);
        proxy.loadXML(me.siteUnavailableError(), function(success) {
            if ( success ) {
                atv.unloadPage();
            }
        });
    }
}

// TODO:: change to a url string or hash
// loadAndSwapURL can only be called from page-level JavaScript of the page that wants to be swapped out.
ATVUtils.prototype.loadAndSwapURL = function(url, method, headers, body, processXML) {
    var me = this;
    this.loadURLInternal(url, method, headers, body, function(proxy, xml) { 
	    if(typeof(processXML) == "function") processXML.call(this, xml);
        me.swapXML(proxy, xml);
    });
}

/** Load URL and apply changes to the xml */
ATVUtils.prototype.loadAndApplyURL = function(url, processXML) {
    var me = this;
	this.loadURLInternal(url, 'GET', null, null, function(proxy, xml, xhr) {
		var xmlToLoad = xml;
        if(typeof(processXML) == "function") xmlToLoad = processXML.call(this, xml);
        if (!xmlToLoad) xmlToLoad = xml;
		try {
            proxy.loadXML(xmlToLoad, function(success) {
                if ( !success ) {
                    console.log("loadURL failed to load " + url);
                    proxy.loadXML(me.siteUnavailableError());
                }
            });
        } catch (e) {
            console.log("loadURL caught exception while loading " + url + ". " + e);
            proxy.loadXML(me.siteUnavailableError());
        }
    });
}

var atvutils = new ATVUtils;

console.log("atvutils initialized " + atvutils);

/**
 * This is an AXR handler. It handles most of tediousness of the XHR request and keeps track of onRefresh XHR calls so that we don't end up with multiple page refresh calls.
 * 
 * You can see how I call it on the handleRefresh function below.
 * 
 * 
 * @params object (hash) $h
 * @params string $h.url - url to be loaded
 * @params string $h.method - "GET", "POST", "PUT", "DELTE"
 * @params bool $h.type - false = "Sync" or true = "Async" (You should always use true)
 * @params func $h.success - Gets called on readyState 4 & status 200
 * @params func $h.failure - Gets called on readyState 4 & status != 200
 * @params func $h.callback - Gets called after the success and failure on readyState 4
 * @params string $h.data - data to be sent to the server
 * @params bool $h.refresh - Is this a call from the onRefresh event.
 */
function Ajax($h)
{
	var me = this;
	$h = $h || {}

	// Setup properties
	this.url = $h.url || false;
	this.method = $h.method || "GET";
	this.type = ($h.type === false) ? false : true;
	this.success = $h.success || null;
	this.failure = $h.failure || null;
	this.data = $h.data || null;
	this.complete = $h.complete || null;
	this.refresh = $h.refresh || false;

	if (!this.url)
	{
		console.error('\nAjax Object requires a url to be passed in: e.g. { "url": "some string" }\n')
		return undefined;
	}

	this.createRequest();
	this.req.onreadystatechange = this.stateChange;
	this.req.object = this;
	this.open();
	this.send();
}

Ajax.currentlyRefreshing = false;

Ajax.prototype.stateChange = function()
{
	var me = this.object;

	// console.log("this in stateChage is: "+ this);
	// console.log("object in stateChange is: "+ this.object);

	switch (this.readyState)
	{

	case 1:
		if (typeof (me.connection) === "function")
			me.connection(this, me);
		break;
	case 2:
		if (typeof (me.received) === "function")
			me.received(this, me);
		break;
	case 3:
		if (typeof (me.processing) === "function")
			me.processing(this, me);
		break;
	case 4:
		if (this.status == "200")
		{
			if (typeof (me.success) === "function")
				me.success(this, me);
		}
		else
		{
			if (typeof (me.failure) === "function")
				me.failure(this.status, this, me);
		}
		if (typeof (me.complete) === "function")
			me.complete(this, me);
		if (me.refresh)
			Ajax.currentlyRefreshing = false;
		break;
	default:
		console.log("I don't think I should be here.");
		break;
	}
}

Ajax.prototype.createRequest = function()
{
	try
	{
		this.req = new XMLHttpRequest();
		if (this.refresh)
			Ajax.currentlyRefreshing = true;
	}
	catch (error)
	{
		alert("The request could not be created: </br>" + error);
		console.error("failed to create request: " + error);
	}

}

Ajax.prototype.open = function()
{
	try
	{
		this.req.open(this.method, this.url, this.type);
	}
	catch (error)
	{
		console.error("failed to open request: " + error);
	}
}

Ajax.prototype.send = function()
{
	var data = this.data || null;
	try
	{
		this.req.send(data);
	}
	catch (error)
	{
		console.error("failed to send request: " + error);
	}
}


// End ATVUtils
// ***************************************************



FlowManager = function()
{
};

FlowManager.prototype.setTargetPage = function(pageId)
{
	atv.localStorage[TARGET_PAGEID_KEY] = pageId;
	console.debug("flowManager.setTargetPage() atv.localStorage[TARGET_PAGEID_KEY] " + atv.localStorage[TARGET_PAGEID_KEY]);
}

FlowManager.prototype.clearTargetPage = function()
{
	atv.localStorage.removeItem(TARGET_PAGEID_KEY);
	console.debug("flowManager.clearTargetPage() atv.localStorage[TARGET_PAGEID_KEY] " + atv.localStorage[TARGET_PAGEID_KEY]);
}

FlowManager.prototype.startPurchaseFlow = function()
{
	atv.localStorage[PURCHASE_FLOW_KEY] = "true";
	console.debug("flowManager.startPurchaseFlow() atv.localStorage[PURCHASE_FLOW_KEY] " + atv.localStorage[PURCHASE_FLOW_KEY]);
}

FlowManager.prototype.endPurchaseFlow = function()
{
	atv.localStorage.removeItem(PURCHASE_FLOW_KEY);
	atv.localStorage.removeItem(PURCHASE_PRODUCT_ID_KEY);
	console.debug("flowManager.endPurchaseFlow() atv.localStorage[PURCHASE_FLOW_KEY] " + atv.localStorage[PURCHASE_FLOW_KEY]);
}

FlowManager.prototype.inPurchaseFlow = function()
{
	console.debug("flowManager.inPurchaseFlow() atv.localStorage[PURCHASE_FLOW_KEY] " + atv.localStorage[PURCHASE_FLOW_KEY]);
	return (atv.localStorage[PURCHASE_FLOW_KEY] == "true");
}

FlowManager.prototype.inPurchaseFlow = function()
{
	console.debug("flowManager.inPurchaseFlow() atv.localStorage[PURCHASE_FLOW_KEY] " + atv.localStorage[PURCHASE_FLOW_KEY]);
	return (atv.localStorage[PURCHASE_FLOW_KEY] == "true");
}

FlowManager.prototype.inMediaFlow = function()
{
	console.debug("flowManager.inMediaFlow() atv.localStorage[MEDIA_META_KEY] " + atv.localStorage[MEDIA_META_KEY]);
	return atv.localStorage.getItem(MEDIA_META_KEY) != null && atv.localStorage.getItem(MEDIA_META_REQUEST_PARAMS_KEY) != null;
}

FlowManager.prototype.endMediaFlow = function()
{
	atv.localStorage.removeItem(MEDIA_META_KEY);
	atv.localStorage.removeItem(MEDIA_META_REQUEST_PARAMS_KEY);
	console.debug("flowManager.endMediaFlow() atv.localStorage[MEDIA_META_KEY] " + atv.localStorage[MEDIA_META_KEY]);
}

var flowManager = new FlowManager;

console.debug("atvutils.js flowManager created");HTTP/1.1 200 OK
Server: Oracle-iPlanet-Web-Server/7.0
Date: Wed, 08 Aug 2012 16:14:31 GMT
Content-type: application/x-javascript
Last-modified: Tue, 31 Jul 2012 21:32:23 GMT
Content-length: 42766
Etag: "a70e-50184ee7"
Accept-ranges: bytes

console.log("~~~~~ MLB.TV main.js start");

var IOS_IDENTITY_POINT_KEY = "ios.identityPointId";
var IOS_FINGERPRINT_KEY = "ios.fingerprint";
var BEST_IDENTITY_POINT_KEY = "best.identityPointId";
var BEST_FINGERPRINT_KEY = "best.fingerprint";
var USERNAME_KEY = "username";

var SHOW_SCORES_KEY = "showScores";
var FAVORITE_TEAMS_KEY = "favoriteTeamIds";

var SESSION_KEY = "sessionKey";
var MEDIA_META_KEY = "media.meta";
var MEDIA_META_REQUEST_PARAMS_KEY = "media.meta.requestParams";

var PURCHASE_FLOW_KEY = "flow.purchase";
var PURCHASE_PRODUCT_ID_KEY = "purchase.productId";

var EMAIL_LINKED_KEY = "email.linked";
var AUTHORIZED_KEY = "authorized";

var OFFER_URI_AFTER_AUTH_KEY = "offer.uriToLoadAfterAuth";
var OFFER_PURCHASE_FUNCTION_KEY = "offer.purchaseFunction";
var OFFER_SWAP_PAGE_KEY = "offer.swapPage";

var TARGET_PAGEID_KEY = "targetPage";
var PURCHASE_FLOW_KEY = "flow.purchase";
var OFFER_PAGE_ID = "com.bamnetworks.appletv.offer";

var TODAYS_GAMES_REFRESH_URL_KEY = "todaysGames.refreshUrl";
var GAMES_PAGE_REFRESH_URL_KEY = "games.refreshUrl";

// atv.onPageLoad
atv.onPageLoad = function(id)
{
	console.log("main.js atv.onPageLoad() id: " + id);

	// <mlbtv>
	var headElem = document.rootElement.getElementByName("head");
	if (headElem && headElem.getElementByName("mlbtv"))
	{
		var mlbtvElem = headElem.getElementByName("mlbtv");
		console.log("mlbtv exists");

		// store local variables
		storeLocalVars(mlbtvElem);

		// ping urls
		pingUrls(mlbtvElem);
	}

	// games by team
	if (id && id.indexOf("com.bamnetworks.appletv.gamesByTeam.") >= 0)
	{
		applyGamesPage(document);
	}

	if (id && id.indexOf("com.bamnetworks.appletv.media") >= 0 && typeof (flowManager) != "undefined")
	{
		flowManager.endPurchaseFlow();
		flowManager.endMediaFlow();
		console.debug("main.js onPageLoad() atv.localStorage[PURCHASE_FLOW_KEY] " + atv.localStorage[PURCHASE_FLOW_KEY]);
	}

	atv._debugDumpControllerStack();
}

// atv.onPageUnload
atv.onPageUnload = function(id)
{
	console.debug("main.js atv.onPageUnload() id " + id);
	console.debug("main.js atv.onPageUnload() atv.localStorage[PURCHASE_FLOW_KEY] " + atv.localStorage[PURCHASE_FLOW_KEY]);
	if (flowManager.inPurchaseFlow() && ("com.bamnetworks.appletv.authenticate" == id || id.indexOf("Not Authorized") > -1))
	{
		console.debug("main.js atv.onPageUnload() showing offer page");
		flowManager.setTargetPage(OFFER_PAGE_ID);

		var newPage = new atv.ProxyDocument();
		newPage.show();
		serviceClient.loadOfferPage(newPage);

		console.debug("main.js atv.onPageUnload() showing offer page");
	}
}

function storeLocalVars(mlbtvElem)
{
	// check local storage params
	var localStorageElems = mlbtvElem.getElementsByName("localStorage");
	console.debug("localStorageElems=" + localStorageElems);
	if (localStorageElems && localStorageElems.length > 0)
	{
		var index = 0;
		for (; index < localStorageElems.length; index++)
		{
			var key = localStorageElems[index].getAttribute("key");
			var value = localStorageElems[index].getAttribute("value");
			if (value)
			{
				atv.localStorage[key] = value;
			}
			else
			{
				atv.localStorage.removeItem(key);
			}
			console.log("localStorage[" + key + "] = " + atv.localStorage[key]);
		}
	}
}

function pingUrls(mlbtvElem)
{
	// check ping urls
	var pingElems = mlbtvElem.getElementsByName("ping");
	console.log("pingElems=" + pingElems);
	if (pingElems && pingElems.length > 0)
	{
		for ( var pingIndex = 0; pingIndex < pingElems.length; pingIndex++)
		{
			var url = pingElems[pingIndex].getAttribute('url');
			url = (url == null || url.trim().length() <= 0) ? pingElems[pingIndex].textContent : url;
			console.debug("url=" + url);
			if (url)
			{
				try
				{
					console.log('pinging reporting url ' + url);
					var req = new XMLHttpRequest();
					req.open("GET", url, true);
					req.send();
				}
				catch (e)
				{
					console.error("failed to ping the url " + url + " error " + e);
				}
			}
		}
	}
}

/**
 * Updates the logo according to the screen resolution
 * 
 * @param document
 * @returns
 */
function updateImageResolution(document)
{
	// update logos according to screen resolution
	var frame = atv.device.screenFrame;
	console.log("frame size: " + frame.height);
	if (frame.height == 1080)
	{
		var images = document.rootElement.getElementsByTagName('image');
		for ( var i = 0; i < images.length; i++)
		{
			images[i].textContent = images[i].textContent.replace('720p', '1080p');
		}
	}

	return document;
}

// ------------------------------ load and apply -------------------------------
function applyMain(document)
{
	console.log('applyMain ' + document);

	// end all flows when you're on the main page
	flowManager.endMediaFlow();
	flowManager.endPurchaseFlow();

	// check valid subscription
	// (1) via iTunes on this box and iTunes account signed in. Receipt is present and stored on the box
	// (2) via Vendor and is signed in with Vendor account.
	var validSubscription = (atv.localStorage[IOS_IDENTITY_POINT_KEY] != null && atv.localStorage[IOS_FINGERPRINT_KEY] != null) || (atv.localStorage[AUTHORIZED_KEY] == "true" && atv.localStorage[EMAIL_IDENTITY_POINT_KEY] != null && atv.localStorage[EMAIL_FINGERPRINT_KEY] != null);

	console.log("applyMain() validSubscription " + validSubscription);
	console.log("applyMain() atv.localStorage[AUTHORIZED_KEY] " + atv.localStorage[AUTHORIZED_KEY]);

	var subscribeMenu = document.getElementById("subscribeMlbTv");
	if (validSubscription)
	{
		if (subscribeMenu)
		{
			subscribeMenu.removeFromParent();
			console.debug("applyMain() removing the Subscribe To MLB.TV");
		}
	}
	else
	{
		// add the menu if it's not there
		if (!subscribeMenu)
		{
			subscribeMenu = atv.parseXML('<oneLineMenuItem id="subscribeMlbTv" accessibilityLabel="Subscribe To MLB.TV" onSelect="subscribeToMlbTv()"><label>Subscribe To MLB.TV</label></oneLineMenuItem>').rootElement;
			subscribeMenu.removeFromParent();

			var parent = document.rootElement.getElementByTagName("items");
			console.debug("applyMain() parent " + parent);
			if (parent && parent.childElements)
			{
				console.debug("applyMain() before parent.childElements " + parent.childElements);
				var settingsMenu = document.getElementById("settings");
				settingsMenu.removeFromParent();
				parent.appendChild(subscribeMenu);
				parent.appendChild(settingsMenu);
				console.debug("applyMain() after parent.childElements " + parent.childElements);
			}
		}
	}

	return document;
}

function handleMainVoltileReload()
{
	console.debug("handleMainVoltileReload() atv.localStorage[TARGET_PAGEID_KEY] " + atv.localStorage[TARGET_PAGEID_KEY]);
	if (atv.localStorage[TARGET_PAGEID_KEY] == null || atv.localStorage[TARGET_PAGEID_KEY] == "com.bamnetworks.appletv.main")
	{
		applyMain(document);
		atv.loadAndSwapXML(document);
	}
}

function handleOfferVoltileReload(document)
{
	console.debug("hand "console.log('refreshing todays games');handleTodaysGamesRefresh();");
		listByNavigation.setAttribute("onNavigate", "console.log('navigating todays games');handleTodaysGamesNavigate(event);");

		// sort
		var favTeamIds = getFavoriteTeamIds();
		function sort(menuOne, menuTwo)
		{
			var menuOneTleOfferVoltileReload() atv.localStorage[AUTHORIZED_KEY] " + atv.localStorage[AUTHORIZED_KEY]);
	if (atv.localStorage[AUTHORIZED_KEY] == "true")
	{
		console.debug("handleOfferVoltileReload() unloading the page because already authorized");
		atv.unloadPage();
	}
	else
	{
		applyOffer(document);
		atv.loadAndSwapXML(document);
	}
}

function applyTodaysGames(responseXML, refresh)
{
	console.log("applyTodaysGames() responseXML: " + responseXML);

	try
	{
		// elements
		var listByNavigation = responseXML.rootElement.getElementByTagName("listByNavigation");
		var imageMenuItems = responseXML.rootElement.getElementsByTagName("imageTextImageMenuItem");
		console.log("applyTodaysGames() navigation=" + navigation);

		// add behavior
		if (atv.localStorage[TODAYS_GAMES_REFRESH_URL_KEY] == null || !refresh)
		{
			var navigation = responseXML.rootElement.getElementByTagName("navigation");
			var currentIndex = navigation.getAttribute("currentIndex");
			console.debug("applyTodaysGames() currentIndex " + currentIndex);
			var refreshUrl = navigation.childElements[currentIndex].getElementByTagName("url").textContent;
			console.debug("applyTodaysGames() refreshUrl " + refreshUrl);
			atv.localStorage[TODAYS_GAMES_REFRESH_URL_KEY] = refreshUrl;
		}
		listByNavigation.setAttribute("refreshInterval", "60");
		listByNavigation.setAttribute("onRefresh",eamIds = menuOne.getAttribute("id").split("-");
			var menuTwoTeamIds = menuTwo.getAttribute("id").split("-");

			var menuOneOrder = (favTeamIds.indexOf(menuOneTeamIds[0]) >= 0 || favTeamIds.indexOf(menuOneTeamIds[1]) >= 0) ? 1 : 0;
			var menuTwoOrder = (favTeamIds.indexOf(menuTwoTeamIds[0]) >= 0 || favTeamIds.indexOf(menuTwoTeamIds[1]) >= 0) ? 1 : 0;

			// console.log("favTeamIds.indexOf(menuOneTeamIds[0])=" + favTeamIds.indexOf(menuOneTeamIds[0]) + " favTeamIds.indexOf(menuOneTeamIds[1])=" + favTeamIds.indexOf(menuOneTeamIds[1]));
			// console.log(menuOneOrder + "-" + menuTwoOrder);

			return menuTwoOrder - menuOneOrder;
		}
		var orderedMenuList = imageMenuItems.sort(sort);

		// 1. re-append the sorted children
		// 2. set the favorite team logos
		// 3. hide scores on the page
		// 4. set the link to no scores preview
		var index = 0;
		for (; index < orderedMenuList.length; index++)
		{
			// 1. reappend child for sorting
			var menu = orderedMenuList[index];
			var parent = menu.parent;
			menu.removeFromParent();
			parent.appendChild(menu);

			// 2. set the favorite team logos
			console.debug("applyTodaysGames() menu item id " + menu.getAttribute("id"));
			var teamIds = menu.getAttribute("id").split("-");
			var isAwayFav = favTeamIds.indexOf(teamIds[0]) >= 0;
			var isHomeFav = favTeamIds.indexOf(teamIds[1]) >= 0;
			console.debug("applyTodaysGames() isAwayFav " + isAwayFav);
			console.debug("applyTodaysGames() isHomeFav " + isHomeFav);
			var leftImageElem = menu.getElementByName("leftImage");
			if (isAwayFav && leftImageElem.textContent.indexOf("-fav.png") < 0)
			{
				leftImageElem.textContent = leftImageElem.textContent.replace(".png", "-fav.png");
			}
			var rightImageElem = menu.getElementByName("rightImage");
			if (isHomeFav && rightImageElem.textContent.indexOf("-fav.png") < 0)
			{
				rightImageElem.textContent = rightImageElem.textContent.replace(".png", "-fav.png");
			}

			// 3. Hide scores
			// 4. Use the preview link with no scores
			var showScores = (atv.localStorage[SHOW_SCORES_KEY]) ? atv.localStorage[SHOW_SCORES_KEY] : "true";
			var rightLabelElem = menu.getElementByName("rightLabel");
			if (showScores == "false")
			{
				var previewLinkElem = menu.getElementByTagName("link");
				if (rightLabelElem && rightLabelElem.textContent.indexOf("Final") >= 0)
				{
					rightLabelElem.textContent = "Final";
				}
				if (previewLinkElem)
				{
					previewLinkElem.textContent = previewLinkElem.textContent.replace(".xml", "_noscores.xml");
				}
			}

			// 5. Check for double header 3:33 AM ET
			if (rightLabelElem && rightLabelElem.textContent.indexOf("3:33 AM") >= 0)
			{
				rightLabelElem.textContent = "Game 2";
			}
		}

		return responseXML;
	}
	catch (error)
	{
		console.error("unable to apply todays games " + error);
		atvutils.loadError("An error occurred loading today's games", "An error occurred loading today's games");
	}
}

/**
 * Should be called by the onRefresh event, I am also calling this from the handleNavigate function below to share code. Essentially this function: 1. Loads the current Navigation file 2. Pulls the
 * menu items out of the loaded file and dumps them into the current file 3. Checks to see if an onRefresh AJAX call is already being made and if so does nothing.
 * 
 * 
 * @params string url - url to be loaded
 */
function handleTodaysGamesRefresh()
{
	var url = atv.localStorage[TODAYS_GAMES_REFRESH_URL_KEY];
	console.log("handleTodaysGamesRefresh() url " + url);

	console.log("We are about to check the value of Ajax.currentlyRefreshing: --> " + Ajax.currentlyRefreshing);
	// check the Ajax object and see if a refresh event is happening
	if (!Ajax.currentlyRefreshing)
	{
		// if a refresh event is not happening, make another one.
		console.log("handleTodaysGamesRefresh()About to make the AJAX call. --> ")

		var refresh = new Ajax({
			"url" : url,
			"refresh" : true,
			"success" : function()
			{
				var req = arguments[0];

				console.log("handleTodaysGamesRefresh() Ajax url " + url);

				var newDocument = atv.parseXML(req.responseText); // Instead of using the responseXML you should parse the responseText. I know it seems redundent but trust me.

				// update the content page
				var newMenuSections = newDocument.rootElement.getElementByTagName('sections');
				applyTodaysGames(newDocument, true);
				newMenuSections.removeFromParent();

				var menu = document.rootElement.getElementByTagName('menu');
				console.log("handleTodaysGamesRefresh() menu=" + menu);
				if (menu)
				{
					var oldMenuSections = menu.getElementByTagName('sections');
					menu.replaceChild(oldMenuSections, newMenuSections);
				}
				else
				{
					menu = atv.parseXML("<menu></menu>").rootElement;
					menu.removeFromParent();
					menu.appendChild(newMenuSections);

					var listByNavigation = document.rootElement.getElementByTagName("listByNavigation");
					listByNavigation.appendChild(menu);
				}
				
				// replace # Games
				var newTumbler = newDocument.rootElement.getElementByTagName('tumblerWithSubtitle');
				newTumbler.removeFromParent();
				var header = document.rootElement.getElementByTagName('header');
				var oldTumbler = document.rootElement.getElementByTagName('tumblerWithSubtitle');
				header.replaceChild(oldTumbler, newTumbler);
			}
		});
		console.log("handleTodaysGamesRefresh() I have made the AJAX call. <--");
	}
	console.log("handleTodaysGamesRefresh() I tion applyFeedSelectionPage(document)
{
	console.log("applyFeedSelectionPage() document: " + document);

	// add behavior by adding javascript file reference
	var head = document.rootElement.getElementByTagName("head");
	if (head)
	{
		var serviceJs = atv.parseXML('<script src="' + config.appletvBase + '/js/service.js" />').rootElement;
		serviceJs.removeFromParent();
		console.debug("adding service.js " + serviceJs);

		var children = head.childElements;
		children.unshift(serviceJs);

		for ( var i = 0; i < children.length; i++)
		{
			children[i].removeFromParent();
			head.appendChild(children[i]);
		}
	}

	flowManager.endMediaFlow();
	flowManager.endPurchaseFlow();
	console.log("purchase and media flow ended " + atv.localStorage[PURCHASE_PRODUCT_ID_KEY] + " " + atv.localStorage[MEDIA_META_REQUEST_PARAMS_KEY] + " " + atv.localStorage[MEDIA_META_KEY] + " " + atv.localStorage[SESSION_KEY]);

	return document;
}

// This is called from the feed_selection.xml
function loadFeed(mediaMeta)
{
	atv._debugDumpControllerStack();

	console.debug("showing new page..");
	var newPage = new atv.ProxyDocument();
	newPage.show();

	atv._debugDumpControllerStack();

	// store the media meta into the local storage for later use
	serviceClient.startMediaFlow(mediaMeta);
	atv.localStorage[OFFER_URI_AFTER_AUTH_KEY] = config.appletvBase + "/views/MediaServiam on the other side of the if. <--");
}

/**
 * This function works very similar to the handleRefresh Essentially we are manually loading the data and updating the page in Javascript This means we have to handle a bit more of the magic, but we
 * also get to control what is updated and update more of the page, like headers, previews, etc.
 * 
 * In this example, I am: 1. Getting the new URL from the Navigation Item 2. Changing the onRefresh URL 3. Calling handleRefresh and passing the new url to let handleRefresh do the magic.
 */
function handleTodaysGamesNavigate(e)
{
	console.log("handleTodaysGamesNavigate() " + e + ", e.navigationItemId = " + e.navigationItemId + " document = " + document);
	console.log("e=" + e);
	atvutils.printObject(e);
	console.log("document.getElementById(e.navigationItemId)=" + document.getElementById(e.navigationItemId));
	console.log("document.getElementById(e.navigationItemId).getElementByTagName=" + document.getElementById(e.navigationItemId).getElementByTagName);

	// get the url
	var url = document.getElementById(e.navigationItemId).getElementByTagName('url').textContent;
	atv.localStorage[TODAYS_GAMES_REFRESH_URL_KEY] = url;

	handleTodaysGamesRefresh();

	e.onCancel = function()
	{
		// Cancel any outstanding requests related to the navigation event.
		console.log("Navigation got onCancel.");
	}
}

funcceProxy?encode(MEDIA_META_REQUEST_PARAMS)";
	atv.localStorage[OFFER_PURCHASE_FUNCTION_KEY] = "restorePurchasePlay";
	atv.localStorage[OFFER_SWAP_PAGE_KEY] = "true";

	// load the media page
	serviceClient.loadMediaPage(function(document)
	{
		// media proxy returns a playback page, display
		var authorized = !serviceClient.isMediaResponseUnauthorized(document);
		// atv.localStorage.setItem(AUTHORIZED_KEY, (authorized) ? "true" : "false");
		if (authorized)
		{
			// if authorized, load the page
			console.debug("loadFeed() showing the media page");
			newPage.swapXML(document);
		}
		else
		{
			// if unauthorized, load the offer page
			console.debug("loadFeed() showing the offer page swap " + atv.localStorage[OFFER_SWAP_PAGE_KEY]);
			serviceClient.loadOfferPage(newPage);
		}
	});
}
// deprecated function name but it's still being generated by the gameday mule
var loadLiveGameVideoAsset = loadFeed;

function loadOfferPageAndPlay()
{
	var newPage = new atv.ProxyDocument();
	newPage.show();
	serviceClient.loadOfferPage(newPage);
}

function subscribeToMlbTv()
{
	console.debug("subscribeToMlbTv() enter");

	flowManager.endMediaFlow();

	// create new page where offer page will be loaded onto
	var newPage = new atv.ProxyDocument();
	newPage.show();

	// load the offer page onto the new page
	atv.localStorage[OFFER_URI_AFTER_AUTH_KEY] = contv.unloadPage();
			// do nothing -- go back to previous page
			// atv.loadAndSwapXML(atvutils.makeOkDialog("MLB.TV Error", "Failed to determine your MLB.TV subscription after purchase"));
		})
	}, function()
	{
		// PURCHASE error
		console.debug("purchase(): error ");
		newPage.cancel();
		atv.unloadPage();
	});

	newPage.onCancel = function()
	{
		atv.SKDefaultPaymentQueue.removeTransactionObserver(observer);
	};

	serviceClient.startPurchase();

}

function restoreAndPurchase(productId)
{
	atv._debugDumpControllerStack();

	// store product id into the cart
	atv.localStorage[PURCHASE_PRODUCT_ID_KEY] = productId;

	// create and show new page where dialogs will be loaded
	var newPage = new atv.ProxyDocument();
	newPage.show();

	atv._debugDumpControllerStack();

	var restoreObserver = serviceClient.addRestoreObserver(function(transactions)
	{
		// link()
		serviceClient.doLink(transactions, function()
		{
			// LINK success -- determine authorization thru feature service
			atv._debugDumpControllerStack();
			serviceClient.loadUserAuthorized(function()
			{
				console.debug("restoreAndPurchase() atv.localStorage[AUTHORIZED_KEY] " + atv.localStorage[AUTHORIZED_KEY]);
				if (atv.localStorage[AUTHORIZED_KEY] == "true")
				{
					console.debug("restoreAndPurchase() authorized or error page, showing Success page...");
					atv._debugDumfig.appletvBase + "/views/Dialog?messageCode=AUTH_MESSAGE_CODE";
	atv.localStorage[OFFER_PURCHASE_FUNCTION_KEY] = "restoreAndPurchase";
	atv.localStorage[OFFER_SWAP_PAGE_KEY] = "false";
	serviceClient.loadOfferPage(newPage);
}

function ErrorToDebugString(error)
{
	var localizedDescription = error.userInfo["NSLocalizedDescription"];
	localizedDescription = localizedDescription ? localizedDescription : "Unknown";
	return error.domain + "(" + error.code + "): " + localizedDescription;
}

function linkManually()
{
	atv.loadURL(config.appletvBase + "/views/Login?urlToLoadAfterAuth=" + config.appletvBase + "/views/LinkProxy?request=encode(LINK_PROXY_REQUEST)");
}

function purchase(newPage)
{
	var observer = serviceClient.addPurchaseObserver(function(purchasedTransaction)
	{
		// PURCHASE success
		console.debug("purchase(): success purchased transactions " + purchasedTransaction);

		// link()
		serviceClient.doLink([ purchasedTransaction ], function()
		{
			console.debug("purchase(): link success");
			serviceClient.loadUserAuthorized(function()
			{
				console.debug("purchase(): load user authorized");
				newPage.loadXML(atvutils.makeOkDialog("MLB.TV Purchased", "You have successfully purchased MLB.TV."));
				atv.unloadPage();
			});
		}, function()
		{
			console.debug("purchase(): link error after purchase");
			newPage.cancel();
			apControllerStack();
					newPage.cancel();
					atv.loadAndSwapXML(atvutils.makeOkDialog("Success", "You are now authorized to watch MLB.TV.  Please select a game from Today's Games on the main menu."));
					console.debug("restoreAndPurchase() after showing Success page...");
					atv._debugDumpControllerStack();
				}
				else
				{
					// unauthorized, purchase
					console.debug("restoreAndPurchase() unauthorized, purchasing...");
					purchase(newPage);
					atv._debugDumpControllerStack();
				}
			});
		}, function()
		{
			// LINK error -- attempt to purchase now
			console.debug("restoreAndPurchase() link error - make the purchase ");
			purchase(newPage);
			atv._debugDumpControllerStack();
		});
	}, function(error)
	{
		newPage.cancel();
		if (error && error.code == -500)
		{
			atv.loadAndSwapXML(atvutils.makeOkDialog("MLB.TV Error", "Failed to determine your subscription."));
		}
		else
		{
			newPage.swapXML(document);
		}
		console.error("restoreAndPurchase() restore error - failed to restore " + document);
		atv._debugDumpControllerStack();
	});

	newPage.onCancel = function()
	{
		atv.SKDefaultPaymentQueue.removeTransactionObserver(restoreObserver);
	};

	// Restore completed transactions to get transactions purchased on other devices for this account.
	console.debug("restoreAndPurchase() Restoring completed transactionsllerStack();
					console.debug("restorePurchasePlay() after showing media proxy page...");
				}
				else
				{
					// unauthorized, purchase
					console.debug("restorePurchasePlay() unauthorized, purchasing...");
					purchaseAndPlay(newPage);
					atv._debugDumpControllerStack();
				}
			});
		}, function()
		{
			// LINK error -- attempt to purchase now
			console.debug("restorePurchasePlay() link error - make the purchase ");
			purchaseAndPlay(newPage);
		});
	}, function(error)
	{
		if (error && error.code == -500)
		{
			newPage.swapXML(atvutils.makeOkDialog("MLB.TV Error", "Failed to determine your subscription."));
		}
		else
		{
			newPage.swapXML(document);
		}
	});

	newPage.onCancel = function()
	{
		atv.SKDefaultPaymentQueue.removeTransactionObserver(restoreObserver);
	};

	// Restore completed transactions to get transactions purchased on other devices for this account.
	console.debug("restorePurchasePlay() Restoring completed transactions...");
	atv.SKDefaultPaymentQueue.restoreCompletedTransactions();
}

function play(newPage)
{
	serviceClient.loadMediaPage(function(document)
	{
		console.debug("play() show the media proxy page..." + newPage);
		atv._debugDumpControllerStack();
		if (newPage)
		{
			newPage.swapXML(document);
			atv.unloadPage();
		}
		else
		{
			atv.loadAndSwapXML(document);
		}
		atv._debugDumpCont...");
	atv.SKDefaultPaymentQueue.restoreCompletedTransactions();
}

function purchaseAndPlay(newPage)
{
	atv._debugDumpControllerStack();

	var observer = serviceClient.addPurchaseObserver(function(purchasedTransaction)
	{
		// PURCHASE success
		console.debug("purchase(): success purchased transactions " + purchasedTransaction);
		atv._debugDumpControllerStack();

		// link()
		serviceClient.doLink([ purchasedTransaction ], function()
		{
			console.debug("purchase(): link success");
			atv._debugDumpControllerStack();

			serviceClient.loadMediaPage(function(mediaDoc)
			{
				console.debug("purchase() loadMediaPage");
				newPage.swapXML(mediaDoc);
				atv._debugDumpControllerStack();
			});

		}, function()
		{
			console.debug("purchase(): link error after purchase");
			newPage.swapXML(atvutils.makeOkDialog("MLB.TV Link Error", "Failed to link your MLB.TV account properly after purchase"));
			atv._debugDumpControllerStack();
		})

	}, function(transaction)
	{
		// PURCHASE error
		console.debug("purchase(): error " + (transaction) ? transaction.error : null);
		if (!transaction || transaction.error != 2)
		{
			newPage.swapXML(atvutils.makeOkDialog("MLB.TV Purchase Error", "The purchase failed.  Please try again at a later time."));
		}
		atv._debugDumpControllerStack();
	});

	newPage.onCancel = function()
	{
		atv.SKDefaultPaymerollerStack();
		console.debug("play() after showing media proxy page...");
	});
}

function restoreManually()
{
	console.debug("restore receipts from iTunes account manually");

	var newPage = new atv.ProxyDocument();
	newPage.show();

	var restoreObserver = serviceClient.addRestoreObserver(function(transactions)
	{
		// restore transaction callback
		console.log("restoreManually() transactions " + transactions);
		if (transactions)
		{
			console.debug("restoreManually() successfully restored receipts from iTunes account manually");
			serviceClient.doLink(transactions, function()
			{
				console.debug("restoreManually() link success callback - determine authorization from feature service");
				serviceClient.loadUserAuthorized(function()
				{
					newPage.loadXML(atvutils.makeOkDialog("Restore Successful", "Your subscription has been restored."));
				});
				console.debug("restoreManually() link service success");
			}, function()
			{
				console.debug("restoreManually() link service error");
			});
		}
		else
		{
			console.debug("restoreManually() missing transaction - determine authorization from feature service");
			newPage.loadXML(atvutils.makeOkDialog("Not an MLB.TV subscriber", "You have not purchased a MLB.TV subscription on AppleTV."));
		}
	}, function(error)
	{
		// restore error callback
		console.debug("restoreManually() error attempting to restore receipts from iTunes account manually");

		if (!error || error.code != -500)
		{
			newPage.cancel();
		}
		else
		{
			newPage.loadXML(atvutils.makeOkDialog("Restore Unsuccessful", "An error occurred attempting to restore your MLB.TV account."));
		}
	});

	newPage.onCancel = function()
	{
		atv.SKDefaultPaymentQueue.removeTransactionObserver(restoreObserver);
	};

	// Restore completed transactions to get transactions purchased on other devices for this account.
	console.debug("restoreManually() Restoring completed transactions...");
	atv.SKDefaultPaymentQueue.restoreCompletedTransactions();
}

function restoreOnOffer()
{
	console.debug("restoreOnOffer() restore receipts from iTunes account manually");

	var newPage = new atv.ProxyDocument();
	newPage.show();

	var restoreObserver = serviceClient.addRestoreObserver(function(transactions)
	{
		// restore success callback
		console.log("restoreManually() transaction " + transactions);
		if (transactions)
		{
			console.debug("successfully restored receipts from iTunes account manually");
			serviceClient.doLink(transactions, function()
			{
				// link success callback
				if (flowManager.inMediaFlow())
				{
					console.debug("restoreOnOffer() in media flow, playing media");
					play(newPage);
				}
				else
				{
					console.debug("restoreOnOffer() NOons();
}

function applySettingsPage(document)
{
	// update logo image resolution
	updateImageResolution(document);

	// show scores menu item
	var showScores = atv.localStorage[SHOW_SCORES_KEY];
	var menuItem = document.getElementById("showScoresMenuItem");
	if (menuItem)
	{
		var label = menuItem.getElementByName('rightLabel');
		label.textContent = (!showScores || showScores == "true") ? "Show" : "Hide";
		console.debug("label.textContent: " + label.textContent);
	}

	// menu items
	var parent = document.rootElement.getElementByTagName("items");
	var signInMenu = document.getElementById("signInOutMenuItem");
	var restoreMenu = document.getElementById("restoreSub");
	var linkAcctMenu = document.getElementById("linkAcct");
	var showScoresMenu = document.getElementById("showScoresMenuItem");
	console.debug("applySettingsPage() parent " + parent);

	// restore menu item
	var showRestoreMenu = atv.localStorage[IOS_IDENTITY_POINT_KEY] == null && atv.localStorage[IOS_FINGERPRINT_KEY] == null;
	console.debug("applySettingsPage() showRestoreMenu " + showRestoreMenu + " restoreMenu " + restoreMenu + " atv.localStorage[IOS_IDENTITY_POINT_KEY] " + atv.localStorage[IOS_IDENTITY_POINT_KEY]);
	if (showRestoreMenu)
	{
		if (!restoreMenu)
		{
			var parent = document.rootElement.getElementByTagName("items");
			console.debug("applySettingsPage() parentT in media flow, display info message");
					serviceClient.loadUserAuthorized(function()
					{
						console.debug("restoreOnOffer(): load user authorized");
						newPage.swapXML(atvutils.makeOkDialog("Restore Successful", "Your subscription has been restored."));
						atv.unloadPage();
					});
				}
			}, function()
			{
				// link error callback
				newPage.loadXML(atvutils.makeOkDialog("Restore Unsuccessful", "There was a problem restoring your iTunes account. Please try again at a later time."));
			});
		}
		else
		{
			newPage.loadXML(atvutils.makeOkDialog("Not an MLB.TV subscriber", "You have not purchased a MLB.TV subscription on AppleTV."));
		}
	}, function(error)
	{
		// restore error callback
		console.debug("error attempting to restore receipts from iTunes account manually");

		if (!error || error.code != -500)
		{
			newPage.cancel();
		}
		else
		{
			newPage.loadXML(atvutils.makeOkDialog("Restore Unsuccessful", "An error occurred attempting to restore your MLB.TV account."));
		}
	});

	newPage.onCancel = function()
	{
		atv.SKDefaultPaymentQueue.removeTransactionObserver(restoreObserver);
	};

	// Restore completed transactions to get transactions purchased on other devices for this account.
	console.debug("restoreManually() Restoring completed transactions...");
	atv.SKDefaultPaymentQueue.restoreCompletedTransacti " + parent);
			if (parent && parent.childElements)
			{
				restoreMenu = atv.parseXML('<oneLineMenuItem id="restoreSub" accessibilityLabel="Restore Subscription" onSelect="restoreManually();"><label>Restore Subscription</label></oneLineMenuItem>').rootElement;
				console.debug("applySettingsPage() adding restoreMenu " + restoreMenu);
				restoreMenu.removeFromParent();
				parent.appendChild(restoreMenu);
				if (linkAcctMenu)
				{
					linkAcctMenu.removeFromParent();
					parent.appendChild(linkAcctMenu);
				}
				showScoresMenu.removeFromParent();
				parent.appendChild(showScoresMenu);
			}
		}
	}
	else if (restoreMenu)
	{
		restoreMenu.removeFromParent();
	}

	// link accounts menu item
	var showLinkMenu = atv.localStorage[EMAIL_LINKED_KEY] != "true" && atv.localStorage[IOS_IDENTITY_POINT_KEY] != null && atv.localStorage[IOS_FINGERPRINT_KEY] != null;
	console.debug("applySettingsPage() atv.localStorage[EMAIL_LINKED_KEY] " + atv.localStorage[EMAIL_LINKED_KEY] + " showLinkMenu " + showLinkMenu + " atv.localStorage[IOS_IDENTITY_POINT_KEY] " + atv.localStorage[IOS_IDENTITY_POINT_KEY] + " linkAcctMenu " + linkAcctMenu);
	if (showLinkMenu)
	{
		// add the menu if it's not there
		if (!linkAcctMenu)
		{
			if (parent && parent.childElements)
			{
				console.debug("applySettingsPage() before parent.childElements " + parent.childElements);

				linkAcctMenu = atv.parseXML('<oneLineMenuItem id="linkAcct" accessibilityLabel="Link MLB.TV Account" onSelect="linkManually();"><label>Link Account</label></oneLineMenuItem>').rootElement;
				linkAcctMenu.removeFromParent();

				showScoresMenu.removeFromParent();
				parent.appendChild(linkAcctMenu);
				parent.appendChild(showScoresMenu);

				console.debug("applySettingsPage() after parent.childElements " + parent.childElements);
			}
		}

		if (signInMenu)
		{
			signInMenu.removeFromParent();
		}
	}
	else
	{
		// remove the menu
		if (linkAcctMenu)
		{
			linkAcctMenu.removeFromParent();
		}

		if (!signInMenu)
		{
			signInMenu = atv.parseXML('\
				<signInSignOutMenuItem id=\"signInOutMenuItem\" accessibilityLabel=\"Sign out\" signOutExitsApp=\"false\">\
					<signInPageURL>' + config.appletvBase + '/views/Login</signInPageURL>\
					<confirmation>\
						<title>Are You Sure?</title>\
						<description>The account %@ will be logged out of the service.</description>\
					</confirmation>\
				</signInSignOutMenuItem>').rootElement;
			signInMenu.removeFromParent();

			parent.appendChild(signInMenu);
			if (showRestoreMenu && restoreMenu)
			{
				parent.appendChild(restoreMenu);
			}
			showScoresMenu.removeFromParent();
			parent.appendChild(showScoresMenu);
		}
	}

	return document;
}

function applyRecapsPage(responseXML)
{
	// update logo image resolution
	updateImageResolution(responseXML);

	return responseXML;
}

function applyTeamsPage(responseXML)
{
	try
	{
		// update logo image resolution
		updateImageResolution(responseXML);

		var favTeamIds = getFavoriteTeamIds();

		console.debug('sorting teams.. ' + responseXML.rootElement);
		var gridList = responseXML.rootElement.getElementByName("body").getElementByName("scroller").getElementByName("items").getElementsByName("grid");
		var gridIndex = 0;
		for (; gridIndex < gridList.length; gridIndex++)
		{
			// function to sort by title
			function sortByTitle(poster1, poster2)
			{
				var favTeamIds = (atv.localStorage[FAVORITE_TEAMS_KEY]) ? atv.localStorage[FAVORITE_TEAMS_KEY] : [];

				var team1Id = poster1.getAttribute("id");
				var is1Fav = favTeamIds.indexOf(team1Id) >= 0;
				var title1 = poster1.getElementByName("title").textContent;
				title1 = (is1Fav) ? title1.toUpperCase() : title1.toLowerCase();

				var team2Id = poster2.getAttribute("id");
				var is2Fav = favTeamIds.indexOf(team2Id) >= 0;
				var title2 = poster2.getElementByName("title").textContent;
				title2 = (is2Fav) ? title2.toUpperCase() : title2.toLowerCase();

				var result = (title1 > title2) ? 1 : -1;
				// console.debug('result: ' + title1 + ">" + title2 + " = " + result);

				return result;
			}

			// parent
			var parent = gridList[gridIndex].getElementByName("items");

			// sort the posters
			var orderedPosterList = parent.childElements.sort(sortByTitle);

			// re-append the child
			var index = 0;
			for (; index < orderedPosterList.length; index++)
			{
				var poster = orderedPosterList[index];
				var teamId = poster.getAttribute("id");
				poster.removeFromParent();
				parent.appendChild(poster);

				var isFav = favTeamIds.indexOf(teamId) >= 0;

				var imageElem = poster.getElementByName("image");
				if (isFav && imageElem.textContent.indexOf("-fav.png") < 0)
				{
					imageElem.textContent = imageElem.textContent.replace(".png", "-fav.png");
				}
				else if (!isFav && imageElem.textContent.indexOf("-fav.png") > 0)
				{
					imageElem.textContent = imageElem.textContent.replace("-fav.png", ".png");
				}
			}

		}

		console.debug('teams are sorted and favorite team logos in place');
		return responseXML;

	}
	catch (error)
	{
		console.debug("Unable to sort the teams: " + error);
	}
}

function applyGamesPage(responseXML)
{
	console.debug("applyeGamesPage() responseXML = " + responseXML);
	try
	{
		// determine if favorite team
		var listElem = responseXML.rootElement.getElementByName("body").getElementByName("listByNavigation");
		var pageId = listElem.getAttribute("id");
		var teamId = pageId.substring(pageId.lastIndexOf(".") + 1);
		var isFavorite = getFavoriteTeamIds().indexOf(teamId) > -1;
		console.debug("applyGamesPage() teamId: " + teamId + " isFavorite: " + isFavorite);

		var favButton = responseXML.getElementById("favoriteButton");
		var favoriteButtonLabel = favButton.getElementByName("label");
		console.debug("favoriteButtonLabel=" + favoriteButtonLabel);
		if (isFavorite)
		{
			var newLabel = "Remove from Favorites";
			favButton.setAttribute('accessibilityLabel', newLabel);
			favoriteButtonLabel.textContent = newLabel;
			console.debug("changing to Remove From Favorites favoriteButtonLabel.textContent=" + favoriteButtonLabel.textContent);
		}
		else
		{
			var newLabel = "Add to Favorites";
			favButton.setAttribute('accessibilityLabel', newLabel);
			favoriteButtonLabel.textContent = newLabel;
			console.debug("changing to Add to Favorites favoriteButtonLabel.textContent=" + favoriteButtonLabel.textContent);
		}

		return responseXML;
	}
	catch (error)
	{
		console.log("Unable to apply changes to the games page: " + error);
	}
}

/**
 * Should be called by the onRefresh event, I am also calling this from the handleNavigate function below to share code. Essentially this function: 1. Loads the current Navigation file 2. Pulls the
 * menu items out of the loaded file and dumps them into the current file 3. Checks to see if an onRefresh AJAX call is already being made and if so does nothing.
 * 
 * 
 * @params string url - url to be loaded
 */
function handleGamesPageRefresh(urlParam)
{
	var url = atv.localStorage[GAMES_PAGE_REFRESH_URL_KEY];
	if (url == null)
	{
		url = urlParam;
	}
	
	console.log("Handle refresh event called");

	console.log("We are about to check the value of Ajax.currentlyRefreshing: --> " + Ajax.currentlyRefreshing);
	// check the Ajax object and see if a refresh event is happening
	if (!Ajax.currentlyRefreshing)
	{

		// if a refresh event is not happening, make another one.
		console.log("About to make the AJAX call. --> ")

		var refresh = new Ajax({
			"url" : url,
			"refresh" : true,
			"success" : function()
			{
				var req = arguments[0];

				// update the content page
				var newDocument = atv.parseXML(req.responseText), // Instead of using the responseXML you should parse the responseText. I know it seems redundent but trust me.
				menu = document.rootElement.getElementByTagName('menu'), oldMenuSections = menu.getElementByTagName('sections'), newMenuSections = newDocument.rootElement.getElementByTagName('sections'), now = new Date();

				applyGamesPage(newDocument);

				newMenuSections.removeFromParent();
				menu.replaceChild(oldMenuSections, newMenuSections);

			}
		});

		console.debug("I have made the AJAX call. <--");
	}

	console.debugntQueue.removeTransactionObserver(observer);
	};

	serviceClient.startPurchase();
}

function restorePurchasePlay(productId)
{
	// store product id into the cart
	atv.localStorage[PURCHASE_PRODUCT_ID_KEY] = productId;

	var newPage = new atv.ProxyDocument();
	newPage.show();

	atv._debugDumpControllerStack();

	var restoreObserver = serviceClient.addRestoreObserver(function(transactions)
	{
		// link()
		serviceClient.doLink(transactions, function()
		{
			// LINK success -- determine whether to purchase
			serviceClient.loadMediaPage(function(document)
			{
				atv._debugDumpControllerStack();
				var authorized = !serviceClient.isMediaResponseUnauthorized(document);
				console.debug("restorePurchasePlay() authorized " + authorized);
				atv.localStorage.setItem(AUTHORIZED_KEY, (authorized) ? "true" : "false");
				console.debug("restorePurchasePlay() atv.localStorage[AUTHORIZED] " + atv.localStorage[AUTHORIZED_KEY]);
				if (authorized)
				{
					// authorized or media playback error (session key problem, media unavailable, etc.), display the page
					console.debug("restorePurchasePlay() authorized or error page, showing media proxy page...");
					newPage.swapXML(atvutils.makeOkDialog("MLB.TV Subscriber", "You have purchased this content before and already have access to watch MLB.TV content.", "play();"));
					atv._debugDumpContro("I am on the other side of the if. <--");

}

/**
 * This function works very similar to the handleRefresh Essentially we are manually loading the data and updating the page in Javascript This means we have to handle a bit more of the magic, but we
 * also get to control what is updated and update more of the page, like headers, previews, etc.
 * 
 * In this example, I am: 1. Getting the new URL from the Navigation Item 2. Changing the onRefresh URL 3. Calling handleRefresh and passing the new url to let handleRefresh do the magic.
 */
function handleGamesPageNavigate(e)
{
	console.log("Handle navigate event called: " + e + ", e.navigationItemId = " + e.navigationItemId + " document = " + document);

	// get the url
	var url = document.getElementById(e.navigationItemId).getElementByTagName('url').textContent;
	atv.localStorage[GAMES_PAGE_REFRESH_URL_KEY] = url;

	handleGamesPageRefresh(url);

	e.onCancel = function()
	{
		// Cancel any outstanding requests related to the navigation event.
		console.log("Navigation got onCancel.");
	}
}

function getFavoriteTeamIds()
{
	var favTeamIds = atv.localStorage[FAVORITE_TEAMS_KEY];
	console.debug("favoriteTeamIds=" + favTeamIds);
	return (favTeamIds != null) ? favTeamIds : [];
}

function toggleFavoriteTeam(teamId)
{
	try
	{
		console.log("toggleFavoriteTeams " + teamId);
		var favoriteTeamIds = getFavoriteTeamIds();

		var isFavorite;
		if (favoriteTeamIds == null || favoriteTeamIds.length == 0)
		{
			// first favorite team
			favoriteTeamIds = [ teamId ];
			isFavorite = true;
		}
		else
		{
			var teamIdIndex = favoriteTeamIds.indexOf(teamId);
			if (teamIdIndex > -1)
			{
				// remove
				favoriteTeamIds.splice(teamIdIndex, 1);
				isFavorite = false;
			}
			else
			{
				// add
				favoriteTeamIds.push(teamId);
				isFavorite = true;
			}
		}

		atv.localStorage[FAVORITE_TEAMS_KEY] = favoriteTeamIds;

		var favoriteButton = document.getElementById("favoriteButton");
		if (favoriteButton)
		{
			var newLabel = (isFavorite) ? "Remove from Favorites" : "Add to Favorites";
			var label = favoriteButton.getElementByName("label");
			label.textContent = newLabel;
			console.debug("after label.textContent: " + label.textContent);
		}
	}
	catch (error)
	{
		console.error("Caught exception trying to toggle DOM element: " + error);
	}

	console.debug("toggleFavoriteTeam() end " + teamId);
}

function toggleScores(id)
{
	try
	{
		var menuItem = document.getElementById(id);
		if (menuItem)
		{
			var label = menuItem.getElementByName('rightLabel');
			if (label.textContent == "Show")
			{
				label.textContent = "Hide";
				atv.localStorage[SHOW_SCORES_KEY] = "false";
			}
			else
			{
				label.textContent = "Show";
				atv.localStorage[SHOW_SCORES_KEY] = "true";
			}

			console.debug("showScores: " + atv.localStorage[SHOW_SCORES_KEY]);
		}
	}
	catch (error)
	{
		console.error("Caught exception trying to toggle DOM element: " + error);
	}
}
console.log("~~~~~ MLB.TV main.js end");
ntQueue.removeTransactionObserver(observer);
	};

	serviceClient.startPurchase();
}

function restorePurchasePlay(productId)
{
	// store product id into the cart
	atv.localStorage[PURCHASE_PRODUCT_ID_KEY] = productId;

	var newPage = new atv.ProxyDocument();
	newPage.show();

	atv._debugDumpControllerStack();

	var restoreObserver = serviceClient.addRestoreObserver(function(transactions)
	{
		// link()
		serviceClient.doLink(transactions, function()
		{
			// LINK success -- determine whether to purchase
			serviceClient.loadMediaPage(function(document)
			{
				atv._debugDumpControllerStack();
				var authorized = !serviceClient.isMediaResponseUnauthorized(document);
				console.debug("restorePurchasePlay() authorized " + authorized);
				atv.localStorage.setItem(AUTHORIZED_KEY, (authorized) ? "true" : "false");
				console.debug("restorePurchasePlay() atv.localStorage[AUTHORIZED] " + atv.localStorage[AUTHORIZED_KEY]);
				if (authorized)
				{
					// authorized or media playback error (session key problem, media unavailable, etc.), display the page
					console.debug("restorePurchasePlay() authorized or error page, showing media proxy page...");
					newPage.swapXML(atvutils.makeOkDialog("MLB.TV Subscriber", "You have purchased this content before and already have access to watch MLB.TV content.", "play();"));
					atv._debugDumpControgetFavoriteTeamIds();

		var isFavorite;
		if (favoriteTeamIds == null || favoriteTeamIds.length == 0)
		{
			// first favorite team
			favoriteTeamIds = [ teamId ];
			isFavorite = true;
		}
		else
		{
			var teamIdIndex = favoriteTeamIds.indexOf(teamId);
			if (teamIdIndex > -1)
			{
				// remove
				favoriteTeamIds.splice(teamIdIndex, 1);
				isFavorite = false;
			}
			else
			{
				// add
				favoriteTeamIds.push(teamId);
				isFavorite = true;
			}
		}

		atv.localStorage[FAVORITE_TEAMS_KEY] = favoriteTeamIds;

		var favoriteButton = document.getElementById("favoriteButton");
		if (favoriteButton)
		{
			var newLabel = (isFavorite) ? "Remove from Favorites" : "Add to Favorites";
			var label = favoriteButton.getElementByName("label");
			label.textContent = newLabel;
			console.debug("after label.textContent: " + label.textContent);
		}
	}
	catch (error)
	{
		console.error("Caught exception trying to toggle DOM element: " + error);
	}

	console.debug("toggleFavoriteTeam() end " + teamId);
}

function toggleScores(id)
{
	try
	{
		var menuItem = document.getElementById(id);
		if (menuItem)
		{
			var label = menuItem.getElementByName('rightLabel');
			if (label.textContent == "Show")
			{
				label.textContent = "Hide";
				atv.localStorage[SHOW_SCORES_KEY] = "false";
			}
			else
			{
				label.textContent = "Show";
				atv.localStoHTTP/1.1 200 OK
Server: Oracle-iPlanet-Web-Server/7.0
Date: Wed, 08 Aug 2012 16:14:36 GMT
Content-type: text/html
Content-type: application/json
Transfer-encoding: chunked

11c
var config = {env: '_proddc2split', imageBase: 'http://mlb.mlb.com/mlb/images/flagstaff', appletvBase: 'http://lws.mlb.com/appletvv2', bamServiceBase: 'https://mlb-ws.mlb.com', gamedayBase: 'http://gdx.mlb.com', mlbBase: 'http://www.mlb.com', secureMlbBase: 'https://secure.mlb.com'};
0

HTTP/1.1 200 OK
Server: Oracle-iPlanet-Web-Server/7.0
Date: Wed, 08 Aug 2012 16:14:36 GMT
Content-type: application/x-javascript
Last-modified: Mon, 23 Apr 2012 14:41:39 GMT
Content-length: 17889
Etag: "45e1-4f956a23"
Accept-ranges: bytes

// ***************************************************

var EMAIL_IDENTITY_POINT_KEY = "email.identityPointId";
var EMAIL_FINGERPRINT_KEY = "email.fingerprint";

// atv.Element extensions
atv.Element.prototype.getElementsByTagName = function(tagName) {
	return this.ownerDocument.evaluateXPath("descendant::" + tagName, this);
}

// Extend atv.ProxyDocument to load errors from a message and description.
atv.ProxyDocument.prototype.loadError = function(message, description) {
	var doc = atvutils.makeErrorDocument(message, description);
	this.loadXML(doc);
}

// Extend atv.ProxyDocument to load errors from a message and description.
atv.ProxyDocument.prototype.swapXML = function(xml) {
	var me = this;
    try 
    {
        me.loadXML(xml, function(success) {
            if ( success ) {
                atv.unloadPage();
            } else {
                console.log("swapXML failed to load " + xml);
                me.loadXML(atvutils.siteUnavailableError(), function(success) {
                	console.log("swapXML site unavailable error load success " + success);
                    if ( success ) {
                        atv.unloadPage();
                    }
                });
            }
        });
    } 
    catch (e) 
    {
        console.error("swapXML caught exception while loading " + xml + ". " + e);
        me.loadXML(atvutils.siteUnavailableError(), function(success) {
            if ( success ) {
                atv.unloadPage();
            }
        });
    }
}

// atv.Document extensions
atv.Document.prototype.getElementById = function(id) {
 var elements = this.evaluateXPath("//*[@id='" + id + "']", this);
 if ( elements && elements.length > 0 ) {
     return elements[0];
 }
 return undefined;
}

// atv.Element extensions
atv.Element.prototype.getElementsByTagName = function(tagName) {
 return this.ownerDocument.evaluateXPath("descendant::" + tagName, this);
}

atv.Element.prototype.getElementByTagName = function(tagName) {
 var elements = this.getElementsByTagName(tagName);
 if ( elements && elements.length > 0 ) {
     return elements[0];
 }
 return undefined;
}

// ATVUtils - a JavaScript helper library for Apple TV
ATVUtils = function() {};

ATVUtils.prototype.onGenerateRequest = function (request) {
	
    console.log('atvutils.js atv.onGenerateRequest() oldUrl: ' + request.url);

    console.debug("atvutils.js atv.onGenerateRequest() atv.localStorage[mediaMetaRequestParams] " + atv.localStorage[MEDIA_META_REQUEST_PARAMS_KEY]);
	if ( request.url.indexOf("encode(MEDIA_META_REQUEST_PARAMS)") > -1 && atv.localStorage[MEDIA_META_REQUEST_PARAMS_KEY])
	{
		request.url = request.url.replace("encode(MEDIA_META_REQUEST_PARAMS)", encodeURIComponent(atv.localStorage[MEDIA_META_REQUEST_PARAMS_KEY]));
	}
	console.debug("atvutils.js atv.onGenerateRequest() atv.localStorage[mediaMetaRequestParams] " + atv.localStorage[MEDIA_META_REQUEST_PARAMS_KEY]);
	if ( request.url.indexOf("MEDIA_META_REQUEST_PARAMS") > -1 && atv.localStorage[MEDIA_META_REQUEST_PARAMS_KEY])
	{
		request.url = request.url.replace("MEDIA_META_REQUEST_PARAMS", atv.localStorage[MEDIA_META_REQUEST_PARAMS_KEY]);
	}
	if ( request.url.indexOf("IPID") > -1 && atv.localStorage[BEST_IDENTITY_POINT_KEY])
	{
		request.url = request.url.replace("IPID", atv.localStorage[BEST_IDENTITY_POINT_KEY]);
	}
	if ( request.url.indexOf("FINGERPRINT") > -1 && atv.localStorage[BEST_FINGERPRINT_KEY])
	{
		request.url = request.url.replace("FINGERPRINT", encodeURIComponent(atv.localStorage[BEST_FINGERPRINT_KEY]));
	}
	if ( request.url.indexOf("SESSION_KEY") > -1 )
	{
		var sessionKey = atv.localStorage[SESSION_KEY]; 
		console.debug("atvutils.js atv.onGenerateRequest sessionKey=" + sessionKey);
		request.url = request.url.replace("SESSION_KEY", (sessionKey) ? encodeURIComponent(sessionKey) : "");
	}
	if ( request.url.indexOf("encode(LINK_PROXY_REQUEST)") > -1 && atv.localStorage[EMAIL_IDENTITY_POINT_KEY] != null && atv.localStorage[EMAIL_FINGERPRINT_KEY] != null)
	{
		var linkRequest = serviceClient.createLinkRequest();
		console.debug("atvutils.js atv.onGenerateRequest() linkRequest " + linkRequest);
		if (linkRequest != null)
		{
			// WARNING: encode twice because the xs:uri type does not suppot curly brackets so encode an extra time. The LinkProxy class will be responsible for url unencoding the json 
			request.url = request.url.replace("encode(LINK_PROXY_REQUEST)", encodeURIComponent(encodeURIComponent(JSON.stringify(linkRequest))));
		}
	}
	if ( request.url.indexOf("$$showScores") > -1 )
	{
		var showScores = atv.localStorage[SHOW_SCORES_KEY]; 
		console.log("showScores=" + showScores);
		request.url = request.url.replace("$$showScores", (showScores) ? showScores : "true");
	}
	var authorized = atv.localStorage[AUTHORIZED_KEY]; 
	if ( request.url.indexOf("AUTH_MESSAGE_CODE") > -1 && authorized != null)
	{
		console.debug("atvutils.js AUTH_MESSAGE_CODE atv.onGenerateRequest() authorized " + authorized);
//		request.url = request.url.replace("AUTH_MESSAGE_CODE", ((authorized == "true") ? "ALREADY_PURCHASED" : "NOT_AUHTORIZED"));
	}

	console.log('atvutils.js atv.onGenerateRequest() newUrl: ' + request.url);
}


ATVUtils.prototype.makeRequest = function(url, method, headers, body, callback) {
    if ( !url ) {
        throw "loadURL requires a url argument";
    }

	var method = method || "GET";
	var	headers = headers || {};
	var body = body || "";
	
    var xhr = new XMLHttpRequest();
    xhr.onreadystatechange = function() {
        try {
            if (xhr.readyState == 4 ) {
                if ( xhr.status == 200) {
                	console.log("makeRequest() getAllResponseHeaders(): " + xhr.getAllResponseHeaders());
                    callback(xhr.responseXML, xhr.responseText);
                } else {
                    console.log("makeRequest() received HTTP status " + xhr.status + " for " + url);
                    callback(xhr.responseXML, xhr.responseText);
                }
            }
        } catch (e) {
            console.error('makeRequest caught exception while processing request for ' + url + '. Aborting. Exception: ' + e);
            xhr.abort();
            callback(null, null);
        }
    }
    
    var request = {url: url};
    this.onGenerateRequest(request);
    
    console.debug("makeRequest() url " + request.url);

    xhr.open(method, request.url, true);

    for(var key in headers) {
        xhr.setRequestHeader(key, headers[key]);
    }

    console.debug("sending body in the send() method");
    xhr.send(body);
    return xhr;
}

ATVUtils.prototype.getRequest = function(url, callback) {
	return this.makeRequest(url, "GET", null, null, callback);
}

ATVUtils.prototype.postRequest = function(url, body, callback) {
	return this.makeRequest(url, "POST", null, body, callback);
}

ATVUtils.prototype.makeErrorDocument = function(message, description) {
    if ( !message ) {
        message = "";
    }
    if ( !description ) {
        description = "";
    }

    var errorXML = '<?xml version="1.0" encoding="UTF-8"?> \
    <atv> \
        <body> \
            <dialog id="com.bamnetworks.errorDialog"> \
                <title><![CDATA[' + message + ']]></title> \
                <description><![CDATA[' + description + ']]></description> \
            </dialog> \
        </body> \
    </atv>';

    return atv.parseXML(errorXML);
}

ATVUtils.prototype.makeOkDialog = function(message, description, onSelect) {
	if ( !message ) {
		message = "";
	}
	if ( !description ) {
		description = "";
	}
	
	if (!onSelect)
	{
		onSelect = "atv.unloadPage();";
	}
	
	var errorXML = '<?xml version="1.0" encoding="UTF-8"?> \
		<atv> \
		<head> \
			<script src="'+config.appletvBase+'/views/config.js" />\
			<script src="'+config.appletvBase+'/js/atvutils.js" />\
			<script src="'+config.appletvBase+'/js/service.js" />\
			<script src="'+config.appletvBase+'/js/main.js" />\
		</head>\
		<body> \
		<optionDialog id="com.bamnetowrks.optionDialog"> \
		<header><simpleHeader><title>' + message + '</title></simpleHeader></header> \
		<description><![CDATA[' + description + ']]></description> \
		<menu><sections><menuSection><items> \
			<oneLineMenuItem id="lineMenu" accessibilityLabel="OK" onSelect="' + onSelect + '">\
				<label>OK</label>\
			</oneLineMenuItem>\
		</items></menuSection></sections></menu>\
		</optionDialog> \
		</body> \
		</atv>';
	
	var doc = atv.parseXML(errorXML);
	return doc;
}

ATVUtils.prototype.printObject = function(object) {
	for (property in object)
	{
		console.log(property + ": " + object[property]);
	}
}


ATVUtils.prototype.siteUnavailableError = function() {
    // TODO: localize
	console.debug("siteUnavailableError()");
    return this.makeOkDialog("MLB.TV error", "MLB.TV is currently unavailable. Please try again later.");
}

ATVUtils.prototype.loadError = function(message, description) {
    atv.loadXML(this.makeErrorDocument(message, description));
}

ATVUtils.prototype.loadAndSwapError = function(message, description) {
    atv.loadAndSwapXML(this.makeErrorDocument(message, description));
}

ATVUtils.prototype.loadURLInternal = function(url, method, headers, body, loader) {
    var me = this;
    var xhr;
    var proxy = new atv.ProxyDocument;
    proxy.show();
    proxy.onCancel = function() {
        if ( xhr ) {
            xhr.abort();
        }
    };
    
    xhr = me.makeRequest(url, method, headers, body, function(xml, xhr) {
        try {
        	console.log("loadURLInternal() proxy=" + proxy + " xml=" + xml + " xhr=" + xhr);
            loader(proxy, xml, xhr);
        } catch(e) {
            console.error("Caught exception loading " + url + ". " + e);
            loader(proxy, me.siteUnavailableError(), xhr);
        }
    });
}

ATVUtils.prototype.loadGet = function(url, loader) {
	this.loadURLInternal(url, "GET", null, null, loader);
}

// TODO:: change to a url s) {
            if ( success ) {
                atv.unloadPage();
            }
        });
    }
}

// TODO:: change to a url string or hash
// loadAndSwapURL can only be called from page-level JavaScript of the page that wants to be swapped out.
ATVUtils.prototype.loadAndSwapURL = function(url, method, headers, body, processXML) {
    var me = this;
    this.loadURLInternal(url, method, headers, body, function(proxy, xml) { 
	    if(typeof(processXML) == "function") processXML.call(this, xml);
        me.swapXML(proxy, xml);
    });
}

/** Load URL and apply changes to the xml */
ATVUtils.prototype.loadAndApplyURL = function(url, processXML) {
    var me = this;
	this.loadURLInternal(url, 'GET', null, null, function(proxy, xml, xhr) {
		var xmlToLoad = xml;
        if(typeof(processXML) == "function") xmlToLoad = processXML.call(this, xml);
        if (!xmlToLoad) xmlToLoad = xml;
		try {
            proxy.loadXML(xmlToLoad, function(success) {
                if ( !success ) {
                    console.log("loadURL failed to load " + url);
                    proxy.loadXML(me.siteUnavailableError());
                }
            });
        } catch (e) {
            console.log("loadURL caught exception while loading " + url + ". " + e);
            proxy.loadXML(me.siteUnavailableError());
        }
    });
}

var atvutils = new Atring or hash
ATVUtils.prototype.loadURL = function(url, method, headers, body, processXML) {
    var me = this;
    this.loadURLInternal(url, method, headers, body, function(proxy, xml) {
        if(typeof(processXML) == "function") processXML.call(this, xml);
		try {
            proxy.loadXML(xml, function(success) {
                if ( !success ) {
                    console.log("loadURL failed to load " + url);
                    proxy.loadXML(me.siteUnavailableError());
                }
            });
        } catch (e) {
            console.log("loadURL caught exception while loading " + url + ". " + e);
            proxy.loadXML(me.siteUnavailableError());
        }
    });
}

ATVUtils.prototype.swapXML = function(proxy, xml) {
	var me = this;
    try {
        proxy.loadXML(xml, function(success) {
            if ( success ) {
                atv.unloadPage();
            } else {
                console.log("swapXML failed to load " + xml);
                proxy.loadXML(me.siteUnavailableError(), function(success) {
                    if ( success ) {
                        atv.unloadPage();
                    }
                });
            }
        });
    } catch (e) {
        console.error("swapXML caught exception while loading " + xml + ". " + e);
        proxy.loadXML(me.siteUnavailableError(), function(successsome string" }\n')
		return undefined;
	}

	this.createRequest();
	this.req.onreadystatechange = this.stateChange;
	this.req.object = this;
	this.open();
	this.send();
}

Ajax.currentlyRefreshing = false;

Ajax.prototype.stateChange = function()
{
	var me = this.object;

	// console.log("this in stateChage is: "+ this);
	// console.log("object in stateChange is: "+ this.object);

	switch (this.readyState)
	{

	case 1:
		if (typeof (me.connection) === "function")
			me.connection(this, me);
		break;
	case 2:
		if (typeof (me.received) === "function")
			me.received(this, me);
		break;
	case 3:
		if (typeof (me.processing) === "function")
			me.processing(this, me);
		break;
	case 4:
		if (this.status == "200")
		{
			if (typeof (me.success) === "function")
				me.success(this, me);
		}
		else
		{
			if (typeof (me.failure) === "function")
				me.failure(this.status, this, me);
		}
		if (typeof (me.complete) === "function")
			me.complete(this, me);
		if (me.refresh)
			Ajax.currentlyRefreshing = false;
		break;
	default:
		console.log("I don't think I should be here.");
		break;
	}
}

Ajax.prototype.createRequest = function()
{
	try
	{
		this.req = new XMLHttpRequest();
		if (this.refresh)
			Ajax.currentlyRefreshing = true;
	}
	catch (error)
	{
		alert("The request could not be created: </br>" + error);
		console.error("failed to create request: " + error);
	}

}

Ajax.prototype.open = function()
{
	try
	{
		this.req.open(this.method, this.url, this.type);
	}
	catch (error)
	{
		console.error("failed to open request: " + error);
	}
}

Ajax.prototype.send = function()
{
	var data = this.data || null;
	try
	{
		this.req.send(data);
	}
	catch (error)
	{
		console.error("failed to send request: " + error);
	}
}


// End ATVUtils
// ***************************************************



FlowManager = function()
{
};

FlowManager.prototype.setTargetPage = function(pageId)
{
	atv.localStorage[TARGET_PAGEID_KEY] = pageId;
	console.debug("flowManager.setTargetPage() atv.localStorage[TARGET_PAGEID_KEY] " + atv.localStorage[TARGET_PAGEID_KEY]);
}

FlowManager.prototype.clearTargetPage = function()
{
	atv.localStorage.removeItem(TARGET_PAGEID_KEY);
	console.debug("flowManager.clearTargetPage() atv.localStorage[TARGET_PAGEID_KEY] " + atv.localStorage[TARGET_PAGEID_KEY]);
}

FlowManager.prototype.startPurchaseFlow = function()
{
	atv.localStorage[PURCHASE_FLOW_KEY] = "true";
	console.debug("flowManager.startPurchaseFlow() atv.localStorage[PURCHASE_FLOW_KEY] " + atv.localStorage[PURCHASE_FLOW_KEY]);
}

FlowManager.prototype.endPurchaseFlow = function()
{
	atv.localStorage.removeItem(PURCHASE_FLOW_KEY);
	atv.localStorage.removeItem(PURCHASE_PRODUCT_ID_KEY);
	console.debug("flowManager.enTVUtils;

console.log("atvutils initialized " + atvutils);

/**
 * This is an AXR handler. It handles most of tediousness of the XHR request and keeps track of onRefresh XHR calls so that we don't end up with multiple page refresh calls.
 * 
 * You can see how I call it on the handleRefresh function below.
 * 
 * 
 * @params object (hash) $h
 * @params string $h.url - url to be loaded
 * @params string $h.method - "GET", "POST", "PUT", "DELTE"
 * @params bool $h.type - false = "Sync" or true = "Async" (You should always use true)
 * @params func $h.success - Gets called on readyState 4 & status 200
 * @params func $h.failure - Gets called on readyState 4 & status != 200
 * @params func $h.callback - Gets called after the success and failure on readyState 4
 * @params string $h.data - data to be sent to the server
 * @params bool $h.refresh - Is this a call from the onRefresh event.
 */
function Ajax($h)
{
	var me = this;
	$h = $h || {}

	// Setup properties
	this.url = $h.url || false;
	this.method = $h.method || "GET";
	this.type = ($h.type === false) ? false : true;
	this.success = $h.success || null;
	this.failure = $h.failure || null;
	this.data = $h.data || null;
	this.complete = $h.complete || null;
	this.refresh = $h.refresh || false;

	if (!this.url)
	{
		console.error('\nAjax Object requires a url to be passed in: e.g. { "url": "dPurchaseFlow() atv.localStorage[PURCHASE_FLOW_KEY] " + atv.localStorage[PURCHASE_FLOW_KEY]);
}

FlowManager.prototype.inPurchaseFlow = function()
{
	console.debug("flowManager.inPurchaseFlow() atv.localStorage[PURCHASE_FLOW_KEY] " + atv.localStorage[PURCHASE_FLOW_KEY]);
	return (atv.localStorage[PURCHASE_FLOW_KEY] == "true");
}

FlowManager.prototype.inPurchaseFlow = function()
{
	console.debug("flowManager.inPurchaseFlow() atv.localStorage[PURCHASE_FLOW_KEY] " + atv.localStorage[PURCHASE_FLOW_KEY]);
	return (atv.localStorage[PURCHASE_FLOW_KEY] == "true");
}

FlowManager.prototype.inMediaFlow = function()
{
	console.debug("flowManager.inMediaFlow() atv.localStorage[MEDIA_META_KEY] " + atv.localStorage[MEDIA_META_KEY]);
	return atv.localStorage.getItem(MEDIA_META_KEY) != null && atv.localStorage.getItem(MEDIA_META_REQUEST_PARAMS_KEY) != null;
}

FlowManager.prototype.endMediaFlow = function()
{
	atv.localStorage.removeItem(MEDIA_META_KEY);
	atv.localStorage.removeItem(MEDIA_META_REQUEST_PARAMS_KEY);
	console.debug("flowManager.endMediaFlow() atv.localStorage[MEDIA_META_KEY] " + atv.localStorage[MEDIA_META_KEY]);
}

var flowManager = new FlowManager;

console.debug("atvutils.js flowManager created");HTTP/1.1 200 OK
Server: Oracle-iPlanet-Web-Server/7.0
Date: Wed, 08 Aug 2012 16:14:37 GMT
Content-type: application/x-javascript
Last-modified: Thu, 14 Jun 2012 17:05:42 GMT
Content-length: 27397
Etag: "6b05-4fda19e6"
Accept-ranges: bytes

console.log("~~~~~ MLB.TV service.js START");

var PRODUCT_LIST = {
	"IOSATBAT2012GDATVMON" : {
		"image" : "monthlyPremium.jpg",
		"period" : "month"
	}, 
	"IOSMLBTVYEARLY2012" : {
		"image" : "monthlyPremium.jpg",
		"period" : "year"
	}
};

var MLBTV_FEATURE_CONTEXT = "MLBTV2012.INAPPPURCHASE";
var MLBTV_FEATURE_NAME = "MLBALL";

var IOS_IDENTITY_POINT_KEY = "ios.identityPointId";
var IOS_FINGERPRINT_KEY = "ios.fingerprint";
var USERNAME_KEY = "username";
var EMAIL_IDENTITY_POINT_KEY = "email.identityPointId";
var EMAIL_FINGERPRINT_KEY = "email.fingerprint";
var BEST_IDENTITY_POINT_KEY = "best.identityPointId";
var BEST_FINGERPRINT_KEY = "best.fingerprint";
var EMAIL_LINKED_KEY = "email.linked";
var AUTHORIZED_KEY = "authorized";
var SHOW_LINK_STATUS = "showLinkStatus";
var MEDIA_META_KEY = "media.meta";
var MEDIA_META_REQUEST_PARAMS_KEY = "media.meta.requestParams";
var OFFER_URI_AFTER_AUTH_KEY = "offer.uriToLoadAfterAuth";
var OFFER_PURCHASE_FUNCTION_KEY = "offer.purchaseFunction";
var OFFER_SWAP_PAGE_KEY = "offer.swapPage";

ServiceClient = function()
{
};

ServiceClient.prototype.login = function(username, password, success, error)
{
	var url = config.bamServiceBase + "/pubajaxws/services/IdentityPointService";
	var identifyBody = "<soapenv:Envelope xmlns:soapenv=\"http://schemas.xmlsoap.org/soap/envelope/\" xmlns:ns=\"http://services.bamnetworks.com/registration/types/1.0\"><soapenv:Body><ns:identityPoint_identify_request><ns:identification type=\"email-password\"> \
			<ns:email><ns:address>" + username + "</ns:address></ns:email> \
			<ns:password>" + password + "</ns:password> \
		</ns:identification></ns:identityPoint_identify_request></soapenv:Body></soapenv:Envelope>";
	var headers = {
		"SOAPAction" : "http://services.bamnetworks.com/registration/identityPoint/identify",
		"Content-Type" : "text/xml"
	};

	console.log('Trying to authenticate with ' + url);

	atvutils.makeRequest(url, "POST", headers, identifyBody, function(document)
	{
		var identityPointId, fingerprint;
		var statusCode = (document && document.rootElement) ? document.rootElement.childElements[1].getElementByName("identityPoint_identify_response").getElementByName("status").getElementByName("code").textContent : -1;
		console.log('status code is ' + statusCode);

		var errorMessage;
		if (statusCode == 1)
		{
			var identification = document.rootElement.childElements[1].getElementByName("identityPoint_identify_response").getElementByName("identification");
			identityPointId = identification.getElementByName("id").textContent;
			fingerprint = identification.getElementByName("fingerprint").textContent;
		}
		else if (statusCode == -1000)
		{
			errorMessage = "The account name or password was incorrect. Please try again.";
		}
		else
		{
			errorMessage = "MLB.com is curently undergoing maintenance. Please try again.";
		}

		if (identityPointId && fingerprint)
		{
			atv.localStorage[USERNAME_KEY] = username;
			atv.localStorage[EMAIL_IDENTITY_POINT_KEY] = identityPointId;
			atv.localStorage[EMAIL_FINGERPRINT_KEY] = fingerprint;

			if (!atv.localStorage[BEST_IDENTITY_POINT_KEY] && !atv.localStorage[BEST_IDENTITY_POINT_KEY])
			{
				console.debug("saving the email as the best identity point id");
				atv.localStorage[BEST_IDENTITY_POINT_KEY] = identityPointId;
				atv.localStorage[BEST_FINGERPRINT_KEY] = fingerprint;
			}
			console.debug("best identity point id: " + atv.localStorage[BEST_IDENTITY_POINT_KEY]);

			console.debug("determine whether the user is authorized " + serviceClient.loadUserAuthorized);
			serviceClient.loadUserAuthorized(function()
			{
				if (success)
					success();
			});
		}
		else
		{
			if (error)
				error(errorMessage);
			console.log('error message ' + errorMessage);
		}
	});
}

ServiceClient.prototype.loadUserAuthorized = function(callback)
{
	var url = config.bamServiceBase + "/pubajaxws/services/FeatureService";
	var body = "<soapenv:Envelope xmlns:soapenv=\"http://schemas.xmlsoap.org/soap/envelope/\" xmlns:ns=\"http://services.bamnetworks.com/registration/types/1.6\">\
			<soapenv:Body>\
				<ns:feature_findEntitled_request>\
					<ns:identification type=\"fingerprint\">\
						<ns:id>" + atv.localStorage[BEST_IDENTITY_POINT_KEY] + "</ns:id>\
						<ns:fingerprint>" + atv.localStorage[BEST_FINGERPRINT_KEY] + "</ns:fingerprint>\
					</ns:identification>\
					<ns:featureContextName>" + MLBTV_FEATURE_CONTEXT + "</ns:featureContextName>\
				</ns:feature_findEntitled_request>\
			</soapenv:Body>\
		</soapenv:Envelope>";

	var headers = {
		"SOAPAction" : "http://services.bamnetworks.com/registration/feature/findEntitledFeatures",
		"Content-Type" : "text/xml"
	};

	console.debug('loadUserAuthorized() Feature find entitlement request body ' + body);
	console.debug('loadUserAuthorized() Finding entitled features with ' + url);

	atvutils.makeRequest(url, "POST", headers, body, function(document, text)
	{
		console.debug("loadUserAuthorized() Feature.findEntitled response " + text);
		var authorized = (text.indexOf(MLBTV_FEATURE_NAME) > -1);
		console.debug("loadUserAuthorized() authorized " + authorized);
		atv.localStorage[AUTHORIZED_KEY] = authorized.toString();
		console.debug("loadUserAuthorized() set atv.localStorage[AUTHORIZED_KEY] to " + atv.localStorage[AUTHORIZED_KEY]);
		callback(authorized);
	});
}

ServiceClient.prototype.addRestoreObserver = function(successCallback, errorCallback)
{
	// Setup a transaction observer to see when transactions are restored and when new payments are made.

	var transReceived = [];
	
	var restoreObserver = {
		updatedTransactions : function(transactions)
		{
			console.debug("addRestoreObserver() updatedTransactions " + (transactions) ? transactions.length : null);

			if (transactions)
			{
				for ( var i = 0; i < transactions.length; ++i)
				{
					transaction = transactions[i];
					if (transaction.transactionState == atv.SKPaymentTransactionStateRestored)
					{
						console.debug("addRestoreObserver() Found restored transaction: " + GetLogStringForTransaction(transaction) + "\n---> Original Transaction: " + GetLogStringForTransaction(transaction.originalTransaction));
						
						// add
						transReceived.push(transaction);
						
						// finish the transaction
						atv.SKDefaultPaymentQueue.finishTransaction(transaction);
					}
				}
			}
			console.debug("addRestoreObserver() updatedTransactions " + transactions.length);
		},

		restoreCompletedTransactionsFailedWithError : function(error)
		{
			console.debug("addRestoreObserver() restoreCompletedTransactionsFailedWithError start " + error + " code: " + error.code);
			
			atv.SKDefaultPaymentQueue.removeTransactionObserver(this);
			if (typeof (errorCallback) == "function")
				errorCallback(error);
			
			console.debug("addRestoreObserver() restoreCompletedTransactionsFailedWithError end " + error + " code: " + error.code);
		},

		restoreCompletedTransactionsFinished : function()
		{
			console.debug("addRestoreObserver() restoreCompletedTransactionsFinished start");
			
			atv.SKDefaultPaymentQueue.removeTransactionObserver(this);
			
			console.log("addRestoreObserver() transReceived " + transReceived);
			if (typeof (successCallback) == "function")
			{
				successCallback(transReceived);
			}	
			
			console.debug("addRestoreObserver() restoreCompletedTransactionsFinished end");
		}
	};

	atv.SKDefaultPaymentQueue.addTransactionObserver(restoreObserver);
	return restoreObserver;
}

function GetLogStringForTransaction(t)
{
	var result = "Transaction: " + "transactionIdentifier=" + ((t.transactionIdentifier) ? t.transactionIdentifier : "") + ", transactionState=" + ((t.transactionState) ? t.transactionState : "") + ", transactionReceipt=" + ((t.transactionReceipt) ? t.transactionReceipt : "") + ", originalTransaction= " + ((t.originalTransaction) ? t.originalTransaction : "") + ", transactionDate=" + ((t.transactionDate) ? t.transactionDate : "");

	if (t.payment)
	{
		result += ", payment.productIdentifier=" + t.payment.productIdentifier + ", payment.quantity=" + t.payment.quantity;
	}

	if (t.error)
	{
		result += ", error.domain=" + t.error.domain + ", error.code=" + t.error.code + ", error.userInfo=" + t.error.userInfo;
	}

	return result;
}

ServiceClient.prototype.doRequestProduct = function(productIds, successCallback, errorCallback)
{
	console.debug("doRequestProduct() productIds " + productIds);
	// make another products request to get product information
	var request = new atv.SKProductsRequest(productIds);

	request.onRequestDidFinish = function()
	{
		console.debug("doRequestProduct() Products request succeeded");
		if (typeof (successCallback) == "function")
			successCallback();
	};

	request.onRequestDidFailWithError = function(error)
	{
		console.error("doRequestProduct() Products request failed. " + ErrorToDebugString(error));
		if (typeof (errorCallback) == "function")
			errorCallback(error);
	};

	request.onProductsRequestDidReceiveResponse = function(productsResponse)
	{
		console.log("doRequestProduct() Products request received response " + productsResponse);
		console.debug("There are " + productsResponse.products.length + " offers.");

		if (typeof (successCallback) == "function")
			successCallback(productsResponse);
	};

	console.debug("Calling doRequestProduct() start");
	request.start();
}

ServiceClient.prototype.startPurchase = function()
{
	var productID = atv.localStorage["purchase.productId"];

	if (!productID)
	{
		console.debug("startPurchase(): not making a purchase because productID was not provided");
	}
	else
	{
		// make another products request to get product information
		var request = new atv.SKProductsRequest([ productID ]);

		request.onRequestDidFailWithError = function(error)
		{
			console.error("startPurchase() Products request failed. " + ErrorToDebugString(error));
		};

		request.onProductsRequestDidReceiveResponse = function(productsResponse)
		{
			console.log("startPurchase() Products request received response " + productsResponse);
			console.debug("There are " + productsResponse.products.length + " offers.");

			if (productsResponse && productsResponse.products && productsResponse.products.length == 1)
			{
				var product = productsResponse.products[0];
				console.debug("Found available product for ID " + product);
				var payment = {
					product : product,
					quantity : 1
				};
				atv.SKDefaultPaymentQueue.addPayment(payment);
			}
		};

		console.debug("Calling doRequestProduct() start");
		request.start();
	}
}

ServiceClient.prototype.addPurchaseObserver = function(successCallback, errorCallback)
{
	console.debug("addPurchaseObserver(): Purchase product with identifier " + productID);

	var productID = atv.localStorage["purchase.productId"];

	var observer = {
		updatedTransactions : function(transactions)
		{
			console.debug("addPurchaseObserver(): purchase.updatedTransactions: " + transactions.length);

			for ( var i = 0; i < transactions.length; ++i)
			{
				var transaction = transactions[i];

				console.debug("addPurchaseObserver(): Processing transaction " + i + ": " + GetLogStringForTransaction(transaction));

				if (transaction.payment && transaction.payment.productIdentifier == productID)
				{
					console.debug("addPurchaseObserver(): transaction is for product ID " + productID);

					// This is the product ID this transaction observer was created for.
					switch (transaction.transactionState)
					{
					case atv.SKPaymentTransactionStatePurchasing:
						console.debug("addPurchaseObserver(): SKPaymentTransactionStatePurchasing");
						break;
					case atv.SKPaymentTransactionStateRestored:
					case atv.SKPaymentTransactionStatePurchased:
						var reason = transaction.transactionState == atv.SKPaymentTransactionStatePurchased ? "Purchased" : "Restored";
						console.debug("addPurchaseObserver(): SKPaymentTransactionStatePurchased: Purchase Complete!!! Reason: " + reason);
						atv.SKDefaultPaymentQueue.finishTransaction(transaction);
						atv.SKDefaultPaymentQueue.removeTransactionObserver(this);
						if (typeof (successCallback) == "function")
							successCallback(transaction);
						break;
					case atv.SKPaymentTransactionStateFailed:
						console.debug("addPurchaseObserver(): SKPaymentTransactionStateFailed. " + ErrorToDebugString(transaction.error));
						atv.SKDefaultPaymentQueue.finishTransaction(transaction);
						atv.SKDefaultPaymentQueue.removeTransactionObserver(this);
						if (typeof (errorCallback) == "function")
							errorCallback(transaction);
						break;
					default:
						if (typeof (errorCallback) == "function")
							errorCallback(transaction);
						console.error("addPurchaseObserver(): Unknown transaction state: " + transaction.transactionState);
						break;
					}
				}
			}
		}
	}

	atv.SKDefaultPaymentQueue.addTransactionObserver(observer);
	return observer;
}

ServiceClient.prototype.startMediaFlow = function(mediaMeta)
{
	console.debug("startMediaFlow() mediaMeta " + mediaMeta);

	if (mediaMeta)
	{
		// set media meta to the local storage
		atv.localStorage[MEDIA_META_KEY] = JSON.stringify(mediaMeta);

		mediaMeta.title = mediaMeta.awayTeamCity + " " + mediaMeta.awayTeamName + " vs " + mediaMeta.homeTeamCity + " " + mediaMeta.homeTeamName;
		mediaMeta.description = mediaMeta.feed;
		if (mediaMeta.awayTeamId && mediaMeta.homeTeamId)
		{
			mediaMeta.imageUrl = config.imageBase + "/recapThumbs/" + mediaMeta.awayTeamId + "_v_" + mediaMeta.homeTeamId + ".jpg";
		}

		// set the session key from the local storage
		mediaMeta.sessionKey = atv.localStorage["sessionKey"];

		var mediaMetaRequestParams = "";
		mediaMetaRequestParams += '&identityPointId=' + ((atv.localStorage[BEST_IDENTITY_POINT_KEY]) ? atv.localStorage[BEST_IDENTITY_POINT_KEY] : 'IPID');
		mediaMetaRequestParams += '&fingerprint=' + ((atv.localStorage[BEST_FINGERPRINT_KEY]) ? atv.localStorage[BEST_FINGERPRINT_KEY] : 'FINGERPRINT');
		mediaMetaRequestParams += "&contentId=" + mediaMeta.contentId;
		mediaMetaRequestParams += '&sessionKey=' + ((mediaMeta.sessionKey) ? encodeURIComponent(mediaMeta.sessionKey) : "SESSION_KEY");
		mediaMetaRequestParams += '&calendarEventId=' + mediaMeta.calendarEventId;
		mediaMetaRequestParams += "&audioContentId=" + ((mediaMeta.audioContentId) ? mediaMeta.audioContentId.replace(/\s/g, ',') : "");
		mediaMetaRequestParams += '&day=' + mediaMeta.day
		mediaMetaRequestParams += '&month=' + mediaMeta.month;
		mediaMetaRequestParams += '&year=' + mediaMeta.year;
		mediaMetaRequestParams += "&feedType=" + mediaMeta.feedType;
		mediaMetaRequestParams += "&playFromBeginning=" + mediaMeta.playFromBeginning;
		mediaMetaRequestParams += "&title=" + encodeURIComponent(mediaMeta.title);
		mediaMetaRequestParams += "&description=" + encodeURIComponent(mediaMeta.description);
		mediaMetaRequestParams += (mediaMeta.imageUrl) ? ("&imageUrl=" + encodeURIComponent(mediaMeta.imageUrl)) : "";

		// save in local storage (should be in session storage but session storage is not yet supported)
		atv.localStorage[MEDIA_META_KEY] = JSON.stringify(mediaMeta);
		atv.localStorage[MEDIA_META_REQUEST_PARAMS_KEY] = mediaMetaRequestParams;
		console.debug("storing into local storage mediaMetaRequestParams " + atv.localStorage[MEDIA_META_REQUEST_PARAMS_KEY]);
	}
}

ServiceClient.prototype.loadMediaPage = function(callback)
{
	if (atv.localStorage[MEDIA_META_REQUEST_PARAMS_KEY])
	{
		var mediaProxyUrl = config.appletvBase + "/views/MediaServiceProxy?";
		// mediaProxyUrl += '&identityPointId=' + atv.localStorage[BEST_IDENTITY_POINT_KEY];
		// mediaProxyUrl += '&fingerprint=' + encodeURIComponent(atv.localStorage[BEST_FINGERPRINT_KEY]);
		mediaProxyUrl += atv.localStorage[MEDIA_META_REQUEST_PARAMS_KEY];
		console.debug("loadMediaPage mediaProxyUrl " + mediaProxyUrl);

		// make request
		atvutils.getRequest(mediaProxyUrl, callback);
	}
	else if (callback)
	{
		callback();
	}
}

function applyOffer(document)
{
	console.debug("applyOffer() atv.localStorage[PURCHASE_FLOW_KEY] " + atv.localStorage[PURCHASE_FLOW_KEY]);
	console.debug("applyOffer() document " + document);
	var mlbSignIn = (document) ? document.getElementById("mlbSignIn") : null;
	if (mlbSignIn)
	{
		var label = mlbSignIn.getElementByName("label");
		console.debug("applyOffer() atv.localStorage[EMAIL_IDENTITY_POINT_KEY] " + atv.localStorage[EMAIL_FINGERPRINT_KEY]);
		var signedIn = atv.localStorage[EMAIL_IDENTITY_POINT_KEY] != null && atv.localStorage[EMAIL_FINGERPRINT_KEY] != null;
		console.debug("applyOffer() signedIn " + signedIn);

		var uriToLoadAfterAuth = atv.localStorage[OFFER_URI_AFTER_AUTH_KEY];

		if (signedIn)
		{
			label.removeFromParent();
			label = atv.parseXML("<label>Sign In with different user</label>").rootElement;
			label.removeFromParent();
			mlbSignIn.appendChild(label);

			mlbSignIn.setAttribute("onSelect", "atv.logout();atvutils.loadAndSwapURL('" + config.appletvBase + "/views/Login?urlToLoadAfterAuth=" + uriToLoadAfterAuth + "');");
			mlbSignIn.setAttribute("onPlay", "atv.logout();atvutils.loadAndSwapURL('" + config.appletvBase + "/views/Login?urlToLoadAfterAuth=" + uriToLoadAfterAuth + "');");
		}
		else
		{
			console.debug("applyOffer() uriToLoadAfterAuth " + uriToLoadAfterAuth);
			label.textContent = "MLB.TV Premium Subscriber Login";
			console.debug("applyOffer() mlbSignIn.getAttribute('onSelect') " + mlbSignIn.getAttribute('onSelect'));
			mlbSignIn.setAttribute("onSelect", "atvutils.loadAndSwapURL('" + config.appletvBase + "/views/Login?urlToLoadAfterAuth=" + uriToLoadAfterAuth + "');");
			mlbSignIn.setAttribute("onPlay", "atvutils.loadAndSwapURL('" + config.appletvBase + "/views/Login?urlToLoadAfterAuth=" + uriToLoadAfterAuth + "');");
			console.debug("applyOffer() mlbSignIn.getAttribute('onSelect') " + mlbSignIn.getAttribute('onSelect'));
		}
	}
}

ServiceClient.prototype.loadOfferPage = function(newPage)
{
	console.debug("loadOfferPage() enter");

	newPage
	
	// set purchase flow to true
	flowManager.startPurchaseFlow();

	var swap = (atv.localStorage[OFFER_SWAP_PAGE_KEY] == "true");
	console.debug("loadOfferPage() atv.localStorage[OFFER_SWAP_PAGE_KEY] " + atv.localStorage[OFFER_SWAP_PAGE_KEY]);
	console.debug("loadOfferPage() swap " + swap);

	// Use StoreKit to get the list of product identifiers.
	var request = new atv.SKProductsRequest(Object.keys(PRODUCT_LIST));
	request.onRequestDidFailWithError = function(error)
	{
		console.error("Products request failed. " + ErrorToDebugString(error));
		var xml = atvutils.makeOkDialog("MLB.TV", "MLB.TV Products could not be found. Please check again at a later time.");
		if (swap)
			newPage.swapXML(xml)
		else
			newPage.loadXML(xml);
	};
	request.onProductsRequestDidReceiveResponse = function(productsResponse)
	{
		// success
		console.debug("loadOfferPage() Products request received response " + productsResponse);
		console.debug("loadOfferPage() There are " + productsResponse.products.length + " offers.");

		// add image
		console.debug("loadOfferPage() checking for image location");
		for ( var i = 0; i < productsResponse.products.length; i++)
		{
			var product = productsResponse.products[i];
			console.debug("loadOfferPage() product.productIdentifier " + product.productIdentifier);
			if (PRODUCT_LIST[product.productIdentifier] != null)
			{
				product.image = PRODUCT_LIST[product.productIdentifier].image;
				console.debug("loadOfferPage() product.image " + product.image);

				product.period = PRODUCT_LIST[product.productIdentifier].period;
				console.debug("loadOfferPage() product.period " + product.period);
			}
		}

		// This page will display the offers, and call purchaseProduct below with the productId to purchase in an onSelect handler.
		var offerBody = "";
		offerBody += "productMeta=" + JSON.stringify(productsResponse);
		offerBody += "&purchaseFunction=" + atv.localStorage[OFFER_PURCHASE_FUNCTION_KEY];
		if (atv.localStorage[OFFER_URI_AFTER_AUTH_KEY])
		{
			offerBody += "&uriToLoadAfterAuth=" + atv.localStorage[OFFER_URI_AFTER_AUTH_KEY];
		}

		console.debug("loadOfferPage() offerBody: " + offerBody);

		atvutils.postRequest(config.appletvBase + '/views/Offer', offerBody, function(xml)
		{
			try
			{
				console.debug("loadOfferPage() postRequest xml=" + xml);
				applyOffer(xml);
				if (swap)
					newPage.swapXML(xml)
				else
					newPage.loadXML(xml);
			}
			catch (e)
			{
				console.error("Caught exception for the Offer page " + e);
				var errorXML = atvutils.makeOkDialog("MLB.TV Error", "MLB.TV Products could not be found. Please check again at a later time.");
				if (swap)
					newPage.swapXML(errorXML)
				else
					newPage.loadXML(errorXML);
			}
		});
	};

	console.debug("Calling loadOfferPage() start");
	request.start();
}
ServiceClient.prototype.isMediaResponseUnauthorized = function(xml)
{
	return (xml) ? this.isMediaUnauthorized(this.getMediaStatusCode(xml)) : true;
}

ServiceClient.prototype.isMediaUnauthorized = function(mediaStatusCode)
{
	return "" == mediaStatusCode || "UNDETERMINED" == mediaStatusCode || "NOT_AUHTORIZED" == mediaStatusCode || "LOGIN_REQUIRED" == mediaStatusCode || "INVALID_USER_CREDENTIALS_STATUS" == mediaStatusCode;
}

ServiceClient.prototype.isMediaPlayback = function(mediaStatusCode)
{
	return "VIDEO_PLAYBACK" == mediaStatusCode || "AUDIO_PLAYBACK" == mediaStatusCode;
}

ServiceClient.prototype.getMediaStatusCode = function(xml)
{
	var mediaStatusCodeElem = (xml && xml.rootElement) ? xml.rootElement.getElementByTagName("mediaStatusCode") : null;
	console.debug("getMediaStatusCode() mediaStatusCodeElem " + mediaStatusCodeElem);
	var mediaStatusCode = (mediaStatusCodeElem != null) ? mediaStatusCodeElem.textContent : "UNDETERMINED";
	console.debug("getMediaStatusCode() mediaStatusCode " + mediaStatusCode);
	return mediaStatusCode;
}

ServiceClient.prototype.createFingerprintIdpt = function(ipid, fingerprint)
{
	return {
		"type" : "fingerprint",
		"id" : ipid,
		"fingerprint" : fingerprint
	};
}

ServiceClient.prototype.createIosIdentityPoint = function(transaction)
{
	return {
		"type" : "iosInAppPurchase",
		"receipt" : transaction.transactionReceipt
	};
}

ServiceClient.prototype.createLinkRequest = function(transactions)
{
	var identityPoints = [];

	// email-password fingerprint identity point
	if (atv.localStorage[EMAIL_FINGERPRINT_KEY] && atv.localStorage[EMAIL_IDENTITY_POINT_KEY])
	{
		identityPoints.push(this.createFingerprintIdpt(atv.localStorage[EMAIL_IDENTITY_POINT_KEY], atv.localStorage[EMAIL_FINGERPRINT_KEY]));
	}

	if (transactions)
	{
		console.debug("createLinkRequest() filtering out for unique original transactions");
		
		// filter out unique original transactions
		var originalTransMap = {};
		
		for (var t = 0; t < transactions.length; t++)
		{
			var currTrans = transactions[t];
			var origTrans = currTrans.originalTransaction;
			var transId = (origTrans == null) ? currTrans.transactionIdentifier : origTrans.transactionIdentifier;  
			
			console.debug("createLinkRequest() transId " + transId);
			
			if (originalTransMap[transId] == null)
			{
				// transaction identity point
				identityPoints.push(this.createIosIdentityPoint(currTrans));
				
				originalTransMap[transId] = currTrans;
			}
		}
	}
	// best fingerprint identity point
	else if (atv.localStorage[BEST_FINGERPRINT_KEY] && atv.localStorage[BEST_IDENTITY_POINT_KEY])
	{
		identityPoints.push(this.createFingerprintIdpt(atv.localStorage[BEST_IDENTITY_POINT_KEY], atv.localStorage[BEST_FINGERPRINT_KEY]));
	}

	var linkRequest = null;
	if (identityPoints.length > 0)
	{
		// link request
		linkRequest = {
			"identityPoints" : identityPoints,
			"namespace" : "mlb",
			"key" : "h-Qupr_3eY"
		};
	}

	console.debug("createLinkRequest() linkRequest " + JSON.stringify(linkRequest));

	return linkRequest;
}

ServiceClient.prototype.doLink = function(transactions, successCallback, errorCallback)
{
	var linkRequest = this.createLinkRequest(transactions);

	if (linkRequest == null)
	{
		if (typeof (successCallback) == "function")
			successCallback();

		console.debug("doLink() link not called since nothing to link");
	}
	else
	{
		// link request
		var linkRequestBody = JSON.stringify(linkRequest);
		var req = new XMLHttpRequest();
		var url = config.mlbBase + "/mobile/LinkAll2.jsp";

		req.onreadystatechange = function()
		{
			try
			{
				if (req.readyState == 4)
				{
					console.log('link() Got link service http status code of ' + req.status);
					if (req.status == 200)
					{
						console.log('link() Response text is ' + req.responseText);
						var linkResponseJson = JSON.parse(req.responseText);

						if (linkResponseJson.status == 1)
						{
							console.debug("link() bestIdentityPointToUse: " + linkResponseJson.bestIdentityPointToUse);
							console.debug("link() messages: " + linkResponseJson.messages);

							if (linkResponseJson.bestIdentityPointToUse && linkResponseJson.bestIdentityPointToUse.id && linkResponseJson.bestIdentityPointToUse.fingerprint)
							{
								console.debug("link() saving the best identity point to use");
								atv.localStorage[BEST_IDENTITY_POINT_KEY] = linkResponseJson.bestIdentityPointToUse.id;
								atv.localStorage[BEST_FINGERPRINT_KEY] = linkResponseJson.bestIdentityPointToUse.fingerprint;
								console.debug("link() identityPointId " + atv.localStorage[BEST_IDENTITY_POINT_KEY]);

								if (linkResponseJson.bestIdentityPointToUse.id.toLowerCase().indexOf("ios") >= 0)
								{
									atv.localStorage[IOS_IDENTITY_POINT_KEY] = linkResponseJson.bestIdentityPointToUse.id;
									atv.localStorage[IOS_FINGERPRINT_KEY] = linkResponseJson.bestIdentityPointToUse.fingerprint;
								}
							}

							var linked = false;// , auth = false;
							for ( var m = 0; m < linkResponseJson.messages.length; m++)
							{
								var msg = linkResponseJson.messages[m];
								console.debug("doLink() msg " + msg);
								if (msg == "linked")
								{
									linked = true;
								}
							}
							linked = linked && atv.localStorage[EMAIL_IDENTITY_POINT_KEY] != null && atv.localStorage[EMAIL_FINGERPRINT_KEY] != null;
							// atv.localStorage[EMAIL_LINKED_KEY] = linked.toString();
							// atv.localStorage[AUTHORIZED_KEY] = auth.toString();

							// console.debug("doLink() atv.localStorage[EMAIL_LINKED_KEY] " + atv.localStorage[EMAIL_LINKED_KEY];
							console.debug("atv.localStorage[AUTHORIZED_KEY] " + atv.localStorage[AUTHORIZED_KEY]);

							if (typeof (successCallback) == "function")
								successCallback();

							console.log("doLink() getAllResponseHeaders: " + this.getAllResponseHeaders());
						}
						else
						{

							if (typeof (errorCallback) == "function")
								errorCallback();
							console.error("link response json has an invalid status code: " + linkResponseJson.status);
						}
					}
				}
			}
			catch (e)
			{
				// Specify a copyedited string because this will be displayed to
				// the user.
				console.error('Caught exception while processing request. Aborting. Exception: ' + e);
				if (typeof (errorCallback) == "function")
					errorCallback(e);

				req.abort();
			}
		}

		req.open("POST", url, true);
		req.setRequestHeader("Content-Type", "text/xml");

		console.log('Trying to link with ' + url);
		console.log("Link Service POST body is " + linkRequestBody);
		req.send(linkRequestBody);
	}
}

var serviceClient = new ServiceClient;

console.log("~~~~~ MLB.TV service.js END");HTTP/1.1 200 OK
Server: Oracle-iPlanet-Web-Server/7.0
Date: Wed, 08 Aug 2012 16:14:38 GMT
Content-type: application/x-javascript
Last-modified: Tue, 31 Jul 2012 21:32:23 GMT
Content-length: 42766
Etag: "a70e-50184ee7"
Accept-ranges: bytes

console.log("~~~~~ MLB.TV main.js start");

var IOS_IDENTITY_POINT_KEY = "ios.identityPointId";
var IOS_FINGERPRINT_KEY = "ios.fingerprint";
var BEST_IDENTITY_POINT_KEY = "best.identityPointId";
var BEST_FINGERPRINT_KEY = "best.fingerprint";
var USERNAME_KEY = "username";

var SHOW_SCORES_KEY = "showScores";
var FAVORITE_TEAMS_KEY = "favoriteTeamIds";

var SESSION_KEY = "sessionKey";
var MEDIA_META_KEY = "media.meta";
var MEDIA_META_REQUEST_PARAMS_KEY = "media.meta.requestParams";

var PURCHASE_FLOW_KEY = "flow.purchase";
var PURCHASE_PRODUCT_ID_KEY = "purchase.productId";

var EMAIL_LINKED_KEY = "email.linked";
var AUTHORIZED_KEY = "authorized";

var OFFER_URI_AFTER_AUTH_KEY = "offer.uriToLoadAfterAuth";
var OFFER_PURCHASE_FUNCTION_KEY = "offer.purchaseFunction";
var OFFER_SWAP_PAGE_KEY = "offer.swapPage";

var TARGET_PAGEID_KEY = "targetPage";
var PURCHASE_FLOW_KEY = "flow.purchase";
var OFFER_PAGE_ID = "com.bamnetworks.appletv.offer";

var TODAYS_GAMES_REFRESH_URL_KEY = "todaysGames.refreshUrl";
var GAMES_PAGE_REFRESH_URL_KEY = "games.refreshUrl";

// atv.onPageLoad
atv.onPageLoad = function(id)
{
	console.log("main.js atv.onPageLoad() id: " + id);

	// <mlbtv>
	var headElem = document.rootElement.getElementByName("head");
	if (headElem && headElem.getElementByName("mlbtv"))
	{
		var mlbtvElem = headElem.getElementByName("mlbtv");
		console.log("mlbtv exists");

		// store local variables
		storeLocalVars(mlbtvElem);

		// ping urls
		pingUrls(mlbtvElem);
	}

	// games by team
	if (id && id.indexOf("com.bamnetworks.appletv.gamesByTeam.") >= 0)
	{
		applyGamesPage(document);
	}

	if (id && id.indexOf("com.bamnetworks.appletv.media") >= 0 && typeof (flowManager) != "undefined")
	{
		flowManager.endPurchaseFlow();
		flowManager.endMediaFlow();
		console.debug("main.js onPageLoad() atv.localStorage[PURCHASE_FLOW_KEY] " + atv.localStorage[PURCHASE_FLOW_KEY]);
	}

	atv._debugDumpControllerStack();
}

// atv.onPageUnload
atv.onPageUnload = function(id)
{
	console.debug("main.js atv.onPageUnload() id " + id);
	console.debug("main.js atv.onPageUnload() atv.localStorage[PURCHASE_FLOW_KEY] " + atv.localStorage[PURCHASE_FLOW_KEY]);
	if (flowManager.inPurchaseFlow() && ("com.bamnetworks.appletv.authenticate" == id || id.indexOf("Not Authorized") > -1))
	{
		console.debug("main.js atv.onPageUnload() showing offer page");
		flowManager.setTargetPage(OFFER_PAGE_ID);

		var newPage = new atv.ProxyDocument();
		newPage.show();
		serviceClient.loadOfferPage(newPage);

		console.debug("main.js atv.onPageUnload() showing offer page");
	}
}

function storeLocalVars(mlbtvElem)
{
	// check local storage params
	var localStorageElems = mlbtvElem.getElementsByName("localStorage");
	console.debug("localStorageElems=" + localStorageElems);
	if (localStorageElems && localStorageElems.length > 0)
	{
		var index = 0;
		for (; index < localStorageElems.length; index++)
		{
			var key = localStorageElems[index].getAttribute("key");
			var value = localStorageElems[index].getAttribute("value");
			if (value)
			{
				atv.localStorage[key] = value;
			}
			else
			{
				atv.localStorage.removeItem(key);
			}
			console.log("localStorage[" + key + "] = " + atv.localStorage[key]);
		}
	}
}

function pingUrls(mlbtvElem)
{
	// check ping urls
	var pingElems = mlbtvElem.getElementsByName("ping");
	console.log("pingElems=" + pingElems);
	if (pingElems && pingElems.length > 0)
	{
		for ( var pingIndex = 0; pingIndex < pingElems.length; pingIndex++)
		{
			var url = pingElems[pingIndex].getAttribute('url');
			url = (url == null || url.trim().length() <= 0) ? pingElems[pingIndex].textContent : url;
			console.debug("url=" + url);
			if (url)
			{
				try
				{
					console.log('pinging reporting url ' + url);
					var req = new XMLHttpRequest();
					req.open("GET", url, true);
					req.send();
				}
				catch (e)
				{
					console.error("failed to ping the url " + url + " error " + e);
				}
			}
		}
	}
}

/**
 * Updates the logo according to the screen resolution
 * 
 * @param document
 * @returns
 */
function updateImageResolution(document)
{
	// update logos according to screen resolution
	var frame = atv.device.screenFrame;
	console.log("frame size: " + frame.height);
	if (frame.height == 1080)
	{
		var images = document.rootElement.getElementsByTagName('image');
		for ( var i = 0; i < images.length; i++)
		{
			images[i].textContent = images[i].textContent.replace('720p', '1080p');
		}
	}

	return document;
}

// ------------------------------ load and apply -------------------------------
function applyMain(document)
{
	console.log('applyMain ' + document);

	// end all flows when you're on the main page
	flowManager.endMediaFlow();
	flowManager.endPurchaseFlow();

	// check valid subscription
	// (1) via iTunes on this box and iTunes account signed in. Receipt is present and stored on the box
	// (2) via Vendor and is signed in with Vendor account.
	var validSubscription = (atv.localStorage[IOS_IDENTITY_POINT_KEY] != null && atv.localStorage[IOS_FINGERPRINT_KEY] != null) || (atv.localStorage[AUTHORIZED_KEY] == "true" && atv.localStorage[EMAIL_IDENTITY_POINT_KEY] != null && atv.localStorage[EMAIL_FINGERPRINT_KEY] != null);

	console.log("applyMain() validSubscription " + validSubscription);
	console.log("applyMain() atv.localStorage[AUTHORIZED_KEY] " + atv.localStorage[AUTHORIZED_KEY]);

	var subscribeMenu = document.getElementById("subscribeMlbTv");
	if (validSubscription)
	{
		if (subscribeMenu)
		{
			subscribeMenu.removeFromParent();
			console.debug("applyMain() removing the Subscribe To MLB.TV");
		}
	}
	else
	{
		// add the menu if it's not there
		if (!subscribeMenu)
		{
			subscribeMenu = atv.parseXML('<oneLineMenuItem id="subscribeMlbTv" accessibilityLabel="Subscribe To MLB.TV" onSelect="subscribeToMlbTv()"><label>Subscribe To MLB.TV</label></oneLineMenuItem>').rootElement;
			subscribeMenu.removeFromParent();

			var parent = document.rootElement.getElementByTagName("items");
			console.debug("applyMain() parent " + parent);
			if (parent && parent.childElements)
			{
				console.debug("applyMain() before parent.childElements " + parent.childElements);
				var settingsMenu = document.getElementById("settings");
				settingsMenu.removeFromParent();
				parent.appendChild(subscribeMenu);
				parent.appendChild(settingsMenu);
				console.debug("applyMain() after parent.childElements " + parent.childElements);
			}
		}
	}

	return document;
}

function handleMainVoltileReload()
{
	console.debug("handleMainVoltileReload() atv.localStorage[TARGET_PAGEID_KEY] " + atv.localStorage[TARGET_PAGEID_KEY]);
	if (atv.localStorage[TARGET_PAGEID_KEY] == null || atv.localStorage[TARGET_PAGEID_KEY] == "com.bamnetworks.appletv.main")
	{
		applyMain(document);
		atv.loadAndSwapXML(document);
	}
}

function handleOfferVoltileReload(document)
{
	console.debug("handleOfferVoltileReload() atv.localStorage[AUTHORIZED_KEY] " + atv.localStorage[AUTHORIZED_KEY]);
	if (atv.localStorage[AUTHORIZED_KEY] == "true")
	{
		console.debug("handleOfferVoltileReload() unloading the page because already authorized");
		atv.unloadPage();
	}
	else
	{
		applyOffer(document);
		atv.loadAndSwapXML(document);
	}
}

function applyTodaysGames(responseXML, refresh)
{
	console.log("applyTodaysGames() responseXML: " + responseXML);

	try
	{
		// elements
		var listByNavigation = responseXML.rootElement.getElementByTagName("listByNavigation");
		var imageMenuItems = responseXML.rootElement.getElementsByTagName("imageTextImageMenuItem");
		console.log("applyTodaysGames() navigation=" + navigation);

		// add behavior
		if (atv.localStorage[TODAYS_GAMES_REFRESH_URL_KEY] == null || !refresh)
		{
			var navigation = responseXML.rootElement.getElementByTagName("navigation");
			var currentIndex = navigation.getAttribute("currentIndex");
			console.debug("applyTodaysGames() currentIndex " + currentIndex);
			var refreshUrl = navigation.childElements[currentIndex].getElementByTagName("url").textContent;
			console.debug("applyTodaysGames() refreshUrl " + refreshUrl);
			atv.localStorage[TODAYS_GAMES_REFRESH_URL_KEY] = refreshUrl;
		}
		listByNavigation.setAttribute("refreshInterval", "60");
		listByNavigation.setAttribute("onRefresh", "console.log('refreshing todays games');handleTodaysGamesRefresh();");
		listByNavigation.setAttribute("onNavigate", "console.log('navigating todays games');handleTodaysGamesNavigate(event);");

		// sort
		var favTeamIds = getFavoriteTeamIds();
		function sort(menuOne, menuTwo)
		{
			var menuOneTeamIds = menuOne.getAttribute("id").split("-");
			var menuTwoTeamIds = menuTwo.getAttribute("id").split("-");

			var menuOneOrder = (favTeamIds.indexOf(menuOneTeamIds[0]) >= 0 || favTeamIds.indexOf(menuOneTeamIds[1]) >= 0) ? 1 : 0;
			var menuTwoOrder = (favTeamIds.indexOf(menuTwoTeamIds[0]) >= 0 || favTeamIds.indexOf(menuTwoTeamIds[1]) >= 0) ? 1 : 0;

			// console.log("favTeamIds.indexOf(menuOneTeamIds[0])=" + favTeamIds.indexOf(menuOneTeamIds[0]) + " favTeamIds.indexOf(menuOneTeamIds[1])=" + favTeamIds.indexOf(menuOneTeamIds[1]));
			// console.log(menuOneOrder + "-" + menuTwoOrder);

			return menuTwoOrder - menuOneOrder;
		}
		var orderedMenuList = imageMenuItems.sort(sort);

		// 1. re-append the sorted children
		// 2. set the favorite team logos
		// 3. hide scores on the page
		// 4. set the link to no scores preview
		var index = 0;
		for (; index < orderedMenuList.length; index++)
		{
			// 1. reappend child for sorting
			var menu = orderedMenuList[index];
			var parent = menu.parent;
			menu.removeFromParent();
			parent.appendChild(menu);

			// 2. set the favorite team logos
			console.debug("applyTodaysGames() menu item id " + menu.getAttribute("id"));
			var teamIds = menu.getAttribute("id").split("-");
			var isAwayFav = favTeamIds.indexOf(teamIds[0]) >= 0;
			var isHomeFav = favTeamIds.indexOf(teamIds[1]) >= 0;
			console.debug("applyTodaysGames() isAwayFav " + isAwayFav);
			console.debug("applyTodaysGames() isHomeFav " + isHomeFav);
			var leftImageElem = menu.getElementByName("leftImage");
			if (isAwayFav && leftImageElem.textContent.indexOf("-fav.png") < 0)
			{
				leftImageElem.textContent = leftImageElem.textContent.replace(".png", "-fav.png");
			}
			var rightImageElem = menu.getElementByName("rightImage");
			if (isHomeFav && rightImageElem.textContent.indexOf("-fav.png") < 0)
			{
				rightImageElem.textContent = rightImageElem.textContent.replace(".png", "-fav.png");
			}

			// 3. Hide scores
			// 4. Use the preview link with no scores
			var showScores = (atv.localStorage[SHOW_SCORES_KEY]) ? atv.localStorage[SHOW_SCORES_KEY] : "true";
			var rightLabelElem = menu.getElementByName("rightLabel");
			if (showScores == "false")
			{
				var previewLinkElem = menu.getElementByTagName("link");
				if (rightLabelElem && rightLabelElem.textContent.indexOf("Final") >= 0)
				{
					rightLabelElem.textContent = "Final";
				}
				if (previewLinkElem)
				{
					previewLinkElem.textContent = previewLinkElem.textContent.replace(".xml", "_noscores.xml");
				}
			}

			// 5. Check for double header 3:33 AM ET
			if (rightLabelElem && rightLabelElem.textContent.indexOf("3:33 AM") >= 0)
			{
				rightLabelElem.textContent = "Game 2";
			}
		}

		return responseXML;
	}
	catch (error)
	{
		console.error("unable to apply todays games " + error);
		atvutils.loadError("An error occurred loading today's games", "An error occurred loading today's games");
	}
}

/**
 * Should be called by the onRefresh event, I am also calling this from the handleNavigate function below to share code. Essentially this function: 1. Loads the current Navigation file 2. Pulls the
 * menu items out of the loaded file and dumps them into the current file 3. Checks to see if an onRefresh AJAX call is already being made and if so does nothing.
 * 
 * 
 * @params string url - url to be loaded
 */
function handleTodaysGamesRefresh()
{
	var url = atv.localStorage[TODAYS_GAMES_REFRESH_URL_KEY];
	console.log("handleTodaysGamesRefresh() url " + url);

	console.log("We are about to check the value of Ajax.currentlyRefreshing: --> " + Ajax.currentlyRefreshing);
	// check the Ajax object and see if a refresh event is happening
	if (!Ajax.currentlyRefreshing)
	{
		// if a refresh event is not happening, make another one.
		console.log("handleTodaysGamesRefresh()About to make the AJAX call. --> ")

		var refresh = new Ajax({
			"url" : url,
			"refresh" : true,
			"success" : function()
			{
				var req = arguments[0];

				console.log("handleTodaysGamesRefresh() Ajax url " + url);

				var newDocument = atv.parseXML(req.responseText); // Instead of using the responseXML you should parse the responseText. I know it seems redundent but trust me.

				// update the content page
				var newMenuSections = newDocument.rootElement.getElementByTagName('sections');
				applyTodaysGames(newDocument, true);
				newMenuSections.removeFromParent();

				var menu = document.rootElement.getElementByTagName('menu');
				console.log("handleTodaysGamesRefresh() menu=" + menu);
				if (menu)
				{
					var oldMenuSections = menu.getElementByTagName('sections');
					menu.replaceChild(oldMenuSections, newMenuSections);
				}
				else
				{
					menu = atv.parseXML("<menu></menu>").rootElement;
					menu.removeFromParent();
					menu.appendChild(newMenuSections);

					var listByNavigation = document.rootElement.getElementByTagName("listByNavigation");
					listByNavigation.appendChild(menu);
				}
				
				// replace # Games
				var newTumbler = newDocument.rootElement.getElementByTagName('tumblerWithSubtitle');
				newTumbler.removeFromParent();
				var header = document.rootElement.getElementByTagName('header');
				var oldTumbler = document.rootElement.getElementByTagName('tumblerWithSubtitle');
				header.replaceChild(oldTumbler, newTumbler);
			}
		});
		console.log("handleTodaysGamesRefresh() I have made the AJAX call. <--");
	}
	console.log("handleTodaysGamesRefresh() I am on the other side of the if. <--");
}

/**
 * This function works very similar to the handleRefresh Essentially we are manually loading the data and updating the page in Javascript This means we have to handle a bit more of the magic, but we
 * also get to control what is updated and update more of the page, like headers, previews, etc.
 * 
 * In this example, I am: 1. Getting the new URL from the Navigation Item 2. Changing the onRefresh URL 3. Calling handleRefresh and passing the new url to let handleRefresh do the magic.
 */
function handleTodaysGamesNavigate(e)
{
	console.log("handleTodaysGamesNavigate() " + e + ", e.navigationItemId = " + e.navigationItemId + " document = " + document);
	console.log("e=" + e);
	atvutils.printObject(e);
	console.log("document.getElementById(e.navigationItemId)=" + document.getElementById(e.navigationItemId));
	console.log("document.getElementById(e.navigationItemId).getElementByTagName=" + document.getElementById(e.navigationItemId).getElementByTagName);

	// get the url
	var url = document.getElementById(e.navigationItemId).getElementByTagName('url').textContent;
	atv.localStorage[TODAYS_GAMES_REFRESH_URL_KEY] = url;

	handleTodaysGamesRefresh();

	e.onCancel = function()
	{
		// Cancel any outstanding requests related to the navigation event.
		console.log("Navigation got onCancel.");
	}
}

function applyFeedSelectionPage(document)
{
	console.log("applyFeedSelectionPage() document: " + document);

	// add behavior by adding javascript file reference
	var head = document.rootElement.getElementByTagName("head");
	if (head)
	{
		var serviceJs = atv.parseXML('<script src="' + config.appletvBase + '/js/service.js" />').rootElement;
		serviceJs.removeFromParent();
		console.debug("adding service.js " + serviceJs);

		var children = head.childElements;
		children.unshift(serviceJs);

		for ( var i = 0; i < children.length; i++)
		{
			children[i].removeFromParent();
			head.appendChild(children[i]);
		}
	}

	flowManager.endMediaFlow();
	flowManager.endPurchaseFlow();
	console.log("purchase and media flow ended " + atv.localStorage[PURCHASE_PRODUCT_ID_KEY] + " " + atv.localStorage[MEDIA_META_REQUEST_PARAMS_KEY] + " " + atv.localStorage[MEDIA_META_KEY] + " " + atv.localStorage[SESSION_KEY]);

	return document;
}

// This is called from the feed_selection.xml
function loadFeed(mediaMeta)
{
	atv._debugDumpControllerStack();

	console.debug("showing new page..");
	var newPage = new atv.ProxyDocument();
	newPage.show();

	atv._debugDumpControllerStack();

	// store the media meta into the local storage for later use
	serviceClient.startMediaFlow(mediaMeta);
	atv.localStorage[OFFER_URI_AFTER_AUTH_KEY] = config.appletvBase + "/views/MediaServiceProxy?encode(MEDIA_META_REQUEST_PARAMS)";
	atv.localStorage[OFFER_PURCHASE_FUNCTION_KEY] = "restorePurchasePlay";
	atv.localStorage[OFFER_SWAP_PAGE_KEY] = "true";

	// load the media page
	serviceClient.loadMediaPage(function(document)
	{
		// media proxy returns a playback page, display
		var authorized = !serviceClient.isMediaResponseUnauthorized(document);
		// atv.localStorage.setItem(AUTHORIZED_KEY, (authorized) ? "true" : "false");
		if (authorized)
		{
			// if authorized, load the page
			console.debug("loadFeed() showing the media page");
			newPage.swapXML(document);
		}
		else
		{
			// if unauthorized, load the offer page
			console.debug("loadFeed() showing the offer page swap " + atv.localStorage[OFFER_SWAP_PAGE_KEY]);
			serviceClient.loadOfferPage(newPage);
		}
	});
}
// deprecated function name but it's still being generated by the gameday mule
var loadLiveGameVideoAsset = loadFeed;

function loadOfferPageAndPlay()
{
	var newPage = new atv.ProxyDocument();
	newPage.show();
	serviceClient.loadOfferPage(newPage);
}

function subscribeToMlbTv()
{
	console.debug("subscribeToMlbTv() enter");

	flowManager.endMediaFlow();

	// create new page where offer page will be loaded onto
	var newPage = new atv.ProxyDocument();
	newPage.show();

	// load the offer page onto the new page
	atv.localStorage[OFFER_URI_AFTER_AUTH_KEY] = config.appletvBase + "/views/Dialog?messageCode=AUTH_MESSAGE_CODE";
	atv.localStorage[OFFER_PURCHASE_FUNCTION_KEY] = "restoreAndPurchase";
	atv.localStorage[OFFER_SWAP_PAGE_KEY] = "false";
	serviceClient.loadOfferPage(newPage);
}

function ErrorToDebugString(error)
{
	var localizedDescription = error.userInfo["NSLocalizedDescription"];
	localizedDescription = localizedDescription ? localizedDescription : "Unknown";
	return error.domain + "(" + error.code + "): " + localizedDescription;
}

function linkManually()
{
	atv.loadURL(config.appletvBase + "/views/Login?urlToLoadAfterAuth=" + config.appletvBase + "/views/LinkProxy?request=encode(LINK_PROXY_REQUEST)");
}

function purchase(newPage)
{
	var observer = serviceClient.addPurchaseObserver(function(purchasedTransaction)
	{
		// PURCHASE success
		console.debug("purchase(): success purchased transactions " + purchasedTransaction);

		// link()
		serviceClient.doLink([ purchasedTransaction ], function()
		{
			console.debug("purchase(): link success");
			serviceClient.loadUserAuthorized(function()
			{
				console.debug("purchase(): load user authorized");
				newPage.loadXML(atvutils.makeOkDialog("MLB.TV Purchased", "You have successfully purchased MLB.TV."));
				atv.unloadPage();
			});
		}, function()
		{
			console.debug("purchase(): link error after purchase");
			newPage.cancel();
			atv.unloadPage();
			// do nothing -- go back to previous page
			// atv.loadAndSwapXML(atvutils.makeOkDialog("MLB.TV Error", "Failed to determine your MLB.TV subscription after purchase"));
		})
	}, function()
	{
		// PURCHASE error
		console.debug("purchase(): error ");
		newPage.cancel();
		atv.unloadPage();
	});

	newPage.onCancel = function()
	{
		atv.SKDefaultPaymentQueue.removeTransactionObserver(observer);
	};

	serviceClient.startPurchase();

}

function restoreAndPurchase(productId)
{
	atv._debugDumpControllerStack();

	// store product id into the cart
	atv.localStorage[PURCHASE_PRODUCT_ID_KEY] = productId;

	// create and show new page where dialogs will be loaded
	var newPage = new atv.ProxyDocument();
	newPage.show();

	atv._debugDumpControllerStack();

	var restoreObserver = serviceClient.addRestoreObserver(function(transactions)
	{
		// link()
		serviceClient.doLink(transactions, function()
		{
			// LINK success -- determine authorization thru feature service
			atv._debugDumpControllerStack();
			serviceClient.loadUserAuthorized(function()
			{
				console.debug("restoreAndPurchase() atv.localStorage[AUTHORIZED_KEY] " + atv.localStorage[AUTHORIZED_KEY]);
				if (atv.localStorage[AUTHORIZED_KEY] == "true")
				{
					console.debug("restoreAndPurchase() authorized or error page, showing Success page...");
					atv._debugDumpControllerStack();
					newPage.cancel();
					atv.loadAndSwapXML(atvutils.makeOkDialog("Success", "You are now authorized to watch MLB.TV.  Please select a game from Today's Games on the main menu."));
					console.debug("restoreAndPurchase() after showing Success page...");
					atv._debugDumpControllerStack();
				}
				else
				{
					// unauthorized, purchase
					console.debug("restoreAndPurchase() unauthorized, purchasing...");
					purchase(newPage);
					atv._debugDumpControllerStack();
				}
			});
		}, function()
		{
			// LINK error -- attempt to purchase now
			console.debug("restoreAndPurchase() link error - make the purchase ");
			purchase(newPage);
			atv._debugDumpControllerStack();
		});
	}, function(error)
	{
		newPage.cancel();
		if (error && error.code == -500)
		{
			atv.loadAndSwapXML(atvutils.makeOkDialog("MLB.TV Error", "Failed to determine your subscription."));
		}
		else
		{
			newPage.swapXML(document);
		}
		console.error("restoreAndPurchase() restore error - failed to restore " + document);
		atv._debugDumpControllerStack();
	});

	newPage.onCancel = function()
	{
		atv.SKDefaultPaymentQueue.removeTransactionObserver(restoreObserver);
	};

	// Restore completed transactions to get transactions purchased on other devices for this account.
	console.debug("restoreAndPurchase() Restoring completed transactions...");
	atv.SKDefaultPaymentQueue.restoreCompletedTransactions();
}

function purchaseAndPlay(newPage)
{
	atv._debugDumpControllerStack();

	var observer = serviceClient.addPurchaseObserver(function(purchasedTransaction)
	{
		// PURCHASE success
		console.debug("purchase(): success purchased transactions " + purchasedTransaction);
		atv._debugDumpControllerStack();

		// link()
		serviceClient.doLink([ purchasedTransaction ], function()
		{
			console.debug("purchase(): link success");
			atv._debugDumpControllerStack();

			serviceClient.loadMediaPage(function(mediaDoc)
			{
				console.debug("purchase() loadMediaPage");
				newPage.swapXML(mediaDoc);
				atv._debugDumpControllerStack();
			});

		}, function()
		{
			console.debug("purchase(): link error after purchase");
			newPage.swapXML(atvutils.makeOkDialog("MLB.TV Link Error", "Failed to link your MLB.TV account properly after purchase"));
			atv._debugDumpControllerStack();
		})

	}, function(transaction)
	{
		// PURCHASE error
		console.debug("purchase(): error " + (transaction) ? transaction.error : null);
		if (!transaction || transaction.error != 2)
		{
			newPage.swapXML(atvutils.makeOkDialog("MLB.TV Purchase Error", "The purchase failed.  Please try again at a later time."));
		}
		atv._debugDumpControllerStack();
	});

	newPage.onCancel = function()
	{
		atv.SKDefaultPaymentQueue.removeTransactionObserver(observer);
	};

	serviceClient.startPurchase();
}

function restorePurchasePlay(productId)
{
	// store product id into the cart
	atv.localStorage[PURCHASE_PRODUCT_ID_KEY] = productId;

	var newPage = new atv.ProxyDocument();
	newPage.show();

	atv._debugDumpControllerStack();

	var restoreObserver = serviceClient.addRestoreObserver(function(transactions)
	{
		// link()
		serviceClient.doLink(transactions, function()
		{
			// LINK success -- determine whether to purchase
			serviceClient.loadMediaPage(function(document)
			{
				atv._debugDumpControllerStack();
				var authorized = !serviceClient.isMediaResponseUnauthorized(document);
				console.debug("restorePurchasePlay() authorized " + authorized);
				atv.localStorage.setItem(AUTHORIZED_KEY, (authorized) ? "true" : "false");
				console.debug("restorePurchasePlay() atv.localStorage[AUTHORIZED] " + atv.localStorage[AUTHORIZED_KEY]);
				if (authorized)
				{
					// authorized or media playback error (session key problem, media unavailable, etc.), display the page
					console.debug("restorePurchasePlay() authorized or error page, showing media proxy page...");
					newPage.swapXML(atvutils.makeOkDialog("MLB.TV Subscriber", "You have purchased this content before and already have access to watch MLB.TV content.", "play();"));
					atv._debugDumpControllerStack();
					console.debug("restorePurchasePlay() after showing media proxy page...");
				}
				else
				{
					// unauthorized, purchase
					console.debug("restorePurchasePlay() unauthorized, purchasing...");
					purchaseAndPlay(newPage);
					atv._debugDumpControllerStack();
				}
			});
		}, function()
		{
			// LINK error -- attempt to purchase now
			console.debug("restorePurchasePlay() link error - make the purchase ");
			purchaseAndPlay(newPage);
		});
	}, function(error)
	{
		if (error && error.code == -500)
		{
			newPage.swapXML(atvutils.makeOkDialog("MLB.TV Error", "Failed to determine your subscription."));
		}
		else
		{
			newPage.swapXML(document);
		}
	});

	newPage.onCancel = function()
	{
		atv.SKDefaultPaymentQueue.removeTransactionObserver(restoreObserver);
	};

	// Restore completed transactions to get transactions purchased on other devices for this account.
	console.debug("restorePurchasePlay() Restoring completed transactions...");
	atv.SKDefaultPaymentQueue.restoreCompletedTransactions();
}

function play(newPage)
{
	serviceClient.loadMediaPage(function(document)
	{
		console.debug("play() show the media proxy page..." + newPage);
		atv._debugDumpControllerStack();
		if (newPage)
		{
			newPage.swapXML(document);
			atv.unloadPage();
		}
		else
		{
			atv.loadAndSwapXML(document);
		}
		atv._debugDumpControllerStack();
		console.debug("play() after showing media proxy page...");
	});
}

function restoreManually()
{
	console.debug("restore receipts from iTunes account manually");

	var newPage = new atv.ProxyDocument();
	newPage.show();

	var restoreObserver = serviceClient.addRestoreObserver(function(transactions)
	{
		// restore transaction callback
		console.log("restoreManually() transactions " + transactions);
		if (transactions)
		{
			console.debug("restoreManually() successfully restored receipts from iTunes account manually");
			serviceClient.doLink(transactions, function()
			{
				console.debug("restoreManually() link success callback - determine authorization from feature service");
				serviceClient.loadUserAuthorized(function()
				{
					newPage.loadXML(atvutils.makeOkDialog("Restore Successful", "Your subscription has been restored."));
				});
				console.debug("restoreManually() link service success");
			}, function()
			{
				console.debug("restoreManually() link service error");
			});
		}
		else
		{
			console.debug("restoreManually() missing transaction - determine authorization from feature service");
			newPage.loadXML(atvutils.makeOkDialog("Not an MLB.TV subscriber", "You have not purchased a MLB.TV subscription on AppleTV."));
		}
	}, function(error)
	{
		// restore error callback
		console.debug("restoreManually() error attempting to restore receipts from iTunes account manually");

		if (!error || error.code != -500)
		{
			newPage.cancel();
		}
		else
		{
			newPage.loadXML(atvutils.makeOkDialog("Restore Unsuccessful", "An error occurred attempting to restore your MLB.TV account."));
		}
	});

	newPage.onCancel = function()
	{
		atv.SKDefaultPaymentQueue.removeTransactionObserver(restoreObserver);
	};

	// Restore completed transactions to get transactions purchased on other devices for this account.
	console.debug("restoreManually() Restoring completed transactions...");
	atv.SKDefaultPaymentQueue.restoreCompletedTransactions();
}

function restoreOnOffer()
{
	console.debug("restoreOnOffer() restore receipts from iTunes account manually");

	var newPage = new atv.ProxyDocument();
	newPage.show();

	var restoreObserver = serviceClient.addRestoreObserver(function(transactions)
	{
		// restore success callback
		console.log("restoreManually() transaction " + transactions);
		if (transactions)
		{
			console.debug("successfully restored receipts from iTunes account manually");
			serviceClient.doLink(transactions, function()
			{
				// link success callback
				if (flowManager.inMediaFlow())
				{
					console.debug("restoreOnOffer() in media flow, playing media");
					play(newPage);
				}
				else
				{
					console.debug("restoreOnOffer() NOT in media flow, display info message");
					serviceClient.loadUserAuthorized(function()
					{
						console.debug("restoreOnOffer(): load user authorized");
						newPage.swapXML(atvutils.makeOkDialog("Restore Successful", "Your subscription has been restored."));
						atv.unloadPage();
					});
				}
			}, function()
			{
				// link error callback
				newPage.loadXML(atvutils.makeOkDialog("Restore Unsuccessful", "There was a problem restoring your iTunes account. Please try again at a later time."));
			});
		}
		else
		{
			newPage.loadXML(atvutils.makeOkDialog("Not an MLB.TV subscriber", "You have not purchased a MLB.TV subscription on AppleTV."));
		}
	}, function(error)
	{
		// restore error callback
		console.debug("error attempting to restore receipts from iTunes account manually");

		if (!error || error.code != -500)
		{
			newPage.cancel();
		}
		else
		{
			newPage.loadXML(atvutils.makeOkDialog("Restore Unsuccessful", "An error occurred attempting to restore your MLB.TV account."));
		}
	});

	newPage.onCancel = function()
	{
		atv.SKDefaultPaymentQueue.removeTransactionObserver(restoreObserver);
	};

	// Restore completed transactions to get transactions purchased on other devices for this account.
	console.debug("restoreManually() Restoring completed transactions...");
	atv.SKDefaultPaymentQueue.restoreCompletedTransactions();
}

function applySettingsPage(document)
{
	// update logo image resolution
	updateImageResolution(document);

	// show scores menu item
	var showScores = atv.localStorage[SHOW_SCORES_KEY];
	var menuItem = document.getElementById("showScoresMenuItem");
	if (menuItem)
	{
		var label = menuItem.getElementByName('rightLabel');
		label.textContent = (!showScores || showScores == "true") ? "Show" : "Hide";
		console.debug("label.textContent: " + label.textContent);
	}

	// menu items
	var parent = document.rootElement.getElementByTagName("items");
	var signInMenu = document.getElementById("signInOutMenuItem");
	var restoreMenu = document.getElementById("restoreSub");
	var linkAcctMenu = document.getElementById("linkAcct");
	var showScoresMenu = document.getElementById("showScoresMenuItem");
	console.debug("applySettingsPage() parent " + parent);

	// restore menu item
	var showRestoreMenu = atv.localStorage[IOS_IDENTITY_POINT_KEY] == null && atv.localStorage[IOS_FINGERPRINT_KEY] == null;
	console.debug("applySettingsPage() showRestoreMenu " + showRestoreMenu + " restoreMenu " + restoreMenu + " atv.localStorage[IOS_IDENTITY_POINT_KEY] " + atv.localStorage[IOS_IDENTITY_POINT_KEY]);
	if (showRestoreMenu)
	{
		if (!restoreMenu)
		{
			var parent = document.rootElement.getElementByTagName("items");
			console.debug("applySettingsPage() parent " + parent);
			if (parent && parent.childElements)
			{
				restoreMenu = atv.parseXML('<oneLineMenuItem id="restoreSub" accessibilityLabel="Restore Subscription" onSelect="restoreManually();"><label>Restore Subscription</label></oneLineMenuItem>').rootElement;
				console.debug("applySettingsPage() adding restoreMenu " + restoreMenu);
				restoreMenu.removeFromParent();
				parent.appendChild(restoreMenu);
				if (linkAcctMenu)
				{
					linkAcctMenu.removeFromParent();
					parent.appendChild(linkAcctMenu);
				}
				showScoresMenu.removeFromParent();
				parent.appendChild(showScoresMenu);
			}
		}
	}
	else if (restoreMenu)
	{
		restoreMenu.removeFromParent();
	}

	// link accounts menu item
	var showLinkMenu = atv.localStorage[EMAIL_LINKED_KEY] != "true" && atv.localStorage[IOS_IDENTITY_POINT_KEY] != null && atv.localStorage[IOS_FINGERPRINT_KEY] != null;
	console.debug("applySettingsPage() atv.localStorage[EMAIL_LINKED_KEY] " + atv.localStorage[EMAIL_LINKED_KEY] + " showLinkMenu " + showLinkMenu + " atv.localStorage[IOS_IDENTITY_POINT_KEY] " + atv.localStorage[IOS_IDENTITY_POINT_KEY] + " linkAcctMenu " + linkAcctMenu);
	if (showLinkMenu)
	{
		// add the menu if it's not there
		if (!linkAcctMenu)
		{
			if (parent && parent.childElements)
			{
				console.debug("applySettingsPage() before parent.childElements " + parent.childElements);

				linkAcctMenu = atv.parseXML('<oneLineMenuItem id="linkAcct" accessibilityLabel="Link MLB.TV Account" onSelect="linkManually();"><label>Link Account</label></oneLineMenuItem>').rootElement;
				linkAcctMenu.removeFromParent();

				showScoresMenu.removeFromParent();
				parent.appendChild(linkAcctMenu);
				parent.appendChild(showScoresMenu);

				console.debug("applySettingsPage() after parent.childElements " + parent.childElements);
			}
		}

		if (signInMenu)
		{
			signInMenu.removeFromParent();
		}
	}
	else
	{
		// remove the menu
		if (linkAcctMenu)
		{
			linkAcctMenu.removeFromParent();
		}

		if (!signInMenu)
		{
			signInMenu = atv.parseXML('\
				<signInSignOutMenuItem id=\"signInOutMenuItem\" accessibilityLabel=\"Sign out\" signOutExitsApp=\"false\">\
					<signInPageURL>' + config.appletvBase + '/views/Login</signInPageURL>\
					<confirmation>\
						<title>Are You Sure?</title>\
						<description>The account %@ will be logged out of the service.</description>\
					</confirmation>\
				</signInSignOutMenuItem>').rootElement;
			signInMenu.removeFromParent();

			parent.appendChild(signInMenu);
			if (showRestoreMenu && restoreMenu)
			{
				parent.appendChild(restoreMenu);
			}
			showScoresMenu.removeFromParent();
			parent.appendChild(showScoresMenu);
		}
	}

	return document;
}

function applyRecapsPage(responseXML)
{
	// update logo image resolution
	updateImageResolution(responseXML);

	return responseXML;
}

function applyTeamsPage(responseXML)
{
	try
	{
		// update logo image resolution
		updateImageResolution(responseXML);

		var favTeamIds = getFavoriteTeamIds();

		console.debug('sorting teams.. ' + responseXML.rootElement);
		var gridList = responseXML.rootElement.getElementByName("body").getElementByName("scroller").getElementByName("items").getElementsByName("grid");
		var gridIndex = 0;
		for (; gridIndex < gridList.length; gridIndex++)
		{
			// function to sort by title
			function sortByTitle(poster1, poster2)
			{
				var favTeamIds = (atv.localStorage[FAVORITE_TEAMS_KEY]) ? atv.localStorage[FAVORITE_TEAMS_KEY] : [];

				var team1Id = poster1.getAttribute("id");
				var is1Fav = favTeamIds.indexOf(team1Id) >= 0;
				var title1 = poster1.getElementByName("title").textContent;
				title1 = (is1Fav) ? title1.toUpperCase() : title1.toLowerCase();

				var team2Id = poster2.getAttribute("id");
				var is2Fav = favTeamIds.indexOf(team2Id) >= 0;
				var title2 = poster2.getElementByName("title").textContent;
				title2 = (is2Fav) ? title2.toUpperCase() : title2.toLowerCase();

				var result = (title1 > title2) ? 1 : -1;
				// console.debug('result: ' + title1 + ">" + title2 + " = " + result);

				return result;
			}

			// parent
			var parent = gridList[gridIndex].getElementByName("items");

			// sort the posters
			var orderedPosterList = parent.childElements.sort(sortByTitle);

			// re-append the child
			var index = 0;
			for (; index < orderedPosterList.length; index++)
			{
				var poster = orderedPosterList[index];
				var teamId = poster.getAttribute("id");
				poster.removeFromParent();
				parent.appendChild(poster);

				var isFav = favTeamIds.indexOf(teamId) >= 0;

				var imageElem = poster.getElementByName("image");
				if (isFav && imageElem.textContent.indexOf("-fav.png") < 0)
				{
					imageElem.textContent = imageElem.textContent.replace(".png", "-fav.png");
				}
				else if (!isFav && imageElem.textContent.indexOf("-fav.png") > 0)
				{
					imageElem.textContent = imageElem.textContent.replace("-fav.png", ".png");
				}
			}

		}

		console.debug('teams are sorted and favorite team logos in place');
		return responseXML;

	}
	catch (error)
	{
		console.debug("Unable to sort the teams: " + error);
	}
}

function applyGamesPage(responseXML)
{
	console.debug("applyeGamesPage() responseXML = " + responseXML);
	try
	{
		// determine if favorite team
		var listElem = responseXML.rootElement.getElementByName("body").getElementByName("listByNavigation");
		var pageId = listElem.getAttribute("id");
		var teamId = pageId.substring(pageId.lastIndexOf(".") + 1);
		var isFavorite = getFavoriteTeamIds().indexOf(teamId) > -1;
		console.debug("applyGamesPage() teamId: " + teamId + " isFavorite: " + isFavorite);

		var favButton = responseXML.getElementById("favoriteButton");
		var favoriteButtonLabel = favButton.getElementByName("label");
		console.debug("favoriteButtonLabel=" + favoriteButtonLabel);
		if (isFavorite)
		{
			var newLabel = "Remove from Favorites";
			favButton.setAttribute('accessibilityLabel', newLabel);
			favoriteButtonLabel.textContent = newLabel;
			console.debug("changing to Remove From Favorites favoriteButtonLabel.textContent=" + favoriteButtonLabel.textContent);
		}
		else
		{
			var newLabel = "Add to Favorites";
			favButton.setAttribute('accessibilityLabel', newLabel);
			favoriteButtonLabel.textContent = newLabel;
			console.debug("changing to Add to Favorites favoriteButtonLabel.textContent=" + favoriteButtonLabel.textContent);
		}

		return responseXML;
	}
	catch (error)
	{
		console.log("Unable to apply changes to the games page: " + error);
	}
}

/**
 * Should be called by the onRefresh event, I am also calling this from the handleNavigate function below to share code. Essentially this function: 1. Loads the current Navigation file 2. Pulls the
 * menu items out of the loaded file and dumps them into the current file 3. Checks to see if an onRefresh AJAX call is already being made and if so does nothing.
 * 
 * 
 * @params string url - url to be loaded
 */
function handleGamesPageRefresh(urlParam)
{
	var url = atv.localStorage[GAMES_PAGE_REFRESH_URL_KEY];
	if (url == null)
	{
		url = urlParam;
	}
	
	console.log("Handle refresh event called");

	console.log("We are about to check the value of Ajax.currentlyRefreshing: --> " + Ajax.currentlyRefreshing);
	// check the Ajax object and see if a refresh event is happening
	if (!Ajax.currentlyRefreshing)
	{

		// if a refresh event is not happening, make another one.
		console.log("About to make the AJAX call. --> ")

		var refresh = new Ajax({
			"url" : url,
			"refresh" : true,
			"success" : function()
			{
				var req = arguments[0];

				// update the content page
				var newDocument = atv.parseXML(req.responseText), // Instead of using the responseXML you should parse the responseText. I know it seems redundent but trust me.
				menu = document.rootElement.getElementByTagName('menu'), oldMenuSections = menu.getElementByTagName('sections'), newMenuSections = newDocument.rootElement.getElementByTagName('sections'), now = new Date();

				applyGamesPage(newDocument);

				newMenuSections.removeFromParent();
				menu.replaceChild(oldMenuSections, newMenuSections);

			}
		});

		console.debug("I have made the AJAX call. <--");
	}

	console.debug("I am on the other side of the if. <--");

}

/**
 * This function works very similar to the handleRefresh Essentially we are manually loading the data and updating the page in Javascript This means we have to handle a bit more of the magic, but we
 * also get to control what is updated and update more of the page, like headers, previews, etc.
 * 
 * In this example, I am: 1. Getting the new URL from the Navigation Item 2. Changing the onRefresh URL 3. Calling handleRefresh and passing the new url to let handleRefresh do the magic.
 */
function handleGamesPageNavigate(e)
{
	console.log("Handle navigate event called: " + e + ", e.navigationItemId = " + e.navigationItemId + " document = " + document);

	// get the url
	var url = document.getElementById(e.navigationItemId).getElementByTagName('url').textContent;
	atv.localStorage[GAMES_PAGE_REFRESH_URL_KEY] = url;

	handleGamesPageRefresh(url);

	e.onCancel = function()
	{
		// Cancel any outstanding requests related to the navigation event.
		console.log("Navigation got onCancel.");
	}
}

function getFavoriteTeamIds()
{
	var favTeamIds = atv.localStorage[FAVORITE_TEAMS_KEY];
	console.debug("favoriteTeamIds=" + favTeamIds);
	return (favTeamIds != null) ? favTeamIds : [];
}

function toggleFavoriteTeam(teamId)
{
	try
	{
		console.log("toggleFavoriteTeams " + teamId);
		var favoriteTeamIds = getFavoriteTeamIds();

		var isFavorite;
		if (favoriteTeamIds == null || favoriteTeamIds.length == 0)
		{
			// first favorite team
			favoriteTeamIds = [ teamId ];
			isFavorite = true;
		}
		else
		{
			var teamIdIndex = favoriteTeamIds.indexOf(teamId);
			if (teamIdIndex > -1)
			{
				// remove
				favoriteTeamIds.splice(teamIdIndex, 1);
				isFavorite = false;
			}
			else
			{
				// add
				favoriteTeamIds.push(teamId);
				isFavorite = true;
			}
		}

		atv.localStorage[FAVORITE_TEAMS_KEY] = favoriteTeamIds;

		var favoriteButton = document.getElementById("favoriteButton");
		if (favoriteButton)
		{
			var newLabel = (isFavorite) ? "Remove from Favorites" : "Add to Favorites";
			var label = favoriteButton.getElementByName("label");
			label.textContent = newLabel;
			console.debug("after label.textContent: " + label.textContent);
		}
	}
	catch (error)
	{
		console.error("Caught exception trying to toggle DOM element: " + error);
	}

	console.debug("toggleFavoriteTeam() end " + teamId);
}

function toggleScores(id)
{
	try
	{
		var menuItem = document.getElementById(id);
		if (menuItem)
		{
			var label = menuItem.getElementByName('rightLabel');
			if (label.textContent == "Show")
			{
				label.textContent = "Hide";
				atv.localStorage[SHOW_SCORES_KEY] = "false";
			}
			else
			{
				label.textContent = "Show";
				atv.localStorage[SHOW_SCORES_KEY] = "true";
			}

			console.debug("showScores: " + atv.localStorage[SHOW_SCORES_KEY]);
		}
	}
	catch (error)
	{
		console.error("Caught exception trying to toggle DOM element: " + error);
	}
}
console.log("~~~~~ MLB.TV main.js end");
HTTP/1.1 200 OK
Server: Oracle-iPlanet-Web-Server/7.0
Date: Wed, 08 Aug 2012 16:14:43 GMT
Content-type: text/html
Cache-control: max-age=300
Edge-control: max-age=300
Last-modified: Wed, 08 Aug 2012 16:14:43 GMT
Content-type: application/xml
Transfer-encoding: chunked

9b1
<?xml version="1.0" encoding="UTF-8"?>
<atv>
	<head>
		<script src="http://lws.mlb.com/appletvv2/views/config.js?js=service.js" />
		<script src="http://lws.mlb.com/appletvv2/js/atvutils.js" />
		<script src="http://lws.mlb.com/appletvv2/js/main.js" />
	</head>
	<body>
		<shelfList id="com.bamnetworks.appletv.offer" volatile="true" onVolatileReload="handleOfferVoltileReload(document);">
			<header>
				<simpleHeader accessibilityLabel="MLB.tv Offer Page">
					<title>&#160;</title>
				</simpleHeader>
			</header>
			<centerShelf>
				<shelf id="shelf">
					<sections>
						<shelfSection>
							<items>
																<goldenPoster id="IOSATBAT2012GDATVMON" accessibilityLabel="MLB.TV Premium Monthly" 
									onSelect="restoreAndPurchase('IOSATBAT2012GDATVMON');"
									onPlay="restoreAndPurchase('IOSATBAT2012GDATVMON');">
									<image>http://mlb.mlb.com/mlb/images/flagstaff/products/1080p/monthlyPremium.jpg</image>
									<title>BUY NOW - $24.99 / MONTH</title>
									<subtitle>MLB.TV Premium Monthly</subtitle>
								</goldenPoster>

								<goldenPoster id="IOSMLBTVYEARLY2012" accessibilityLabel="MLB.TV Premium Yearly" 
									onSelect="restoreAndPurchase('IOSMLBTVYEARLY2012');"
									onPlay="restoreAndPurchase('IOSMLBTVYEARLY2012');">
									<image>http://mlb.mlb.com/mlb/images/flagstaff/products/1080p/monthlyPremium.jpg</image>
									<title>BUY NOW - $79.99 / YEAR</title>
									<subtitle>MLB.TV Premium Yearly</subtitle>
								</goldenPoster>


							</items>
						</shelfSection>
					</sections>
				</shelf>
			</centerShelf>
			<menu>
				<sections>
					<menuSection>
						<items>
							<oneLineMenuItem id="mlbSignIn" accessibilityLabel="Sign in with MLB.TV" onPlay="atvutils.loadAndSwapURL('http://lws.mlb.com/appletvv2/views/Login?urlToLoadAfterAuth=http://lws.mlb.com/appletvv2/views/Dialog?messageCode=AUTH_MESSAGE_CODE')"
								onSelect="atvutils.loadAndSwapURL('http://lws.mlb.com/appletvv2/views/Login?urlToLoadAfterAuth=http://lws.mlb.com/appletvv2/views/Dialog?messageCode=AUTH_MESSAGE_CODE')">
								<label>MLB.TV Premium Subscriber Login</label>
							</oneLineMenuItem>
							<oneLineMenuItem id="restoreSub" accessibilityLabel="Subscribed with iTunes?  Restore" onPlay="restoreOnOffer();" onSelect="restoreOnOffer();">
								<label>Purchased Through iTunes? Restore</label>
							</oneLineMenuItem>
						</items>
					</menuSection>
				</sections>
			</menu>
		</shelfList>
	</body>
</atv>

0

HTTP/1.1 200 OK
Server: Oracle-iPlanet-Web-Server/7.0
Date: Wed, 08 Aug 2012 16:14:43 GMT
Content-type: text/html
Content-type: application/json
Transfer-encoding: chunked

2000
var config = {env: '_proddc2split', imageBase: 'http://mlb.mlb.com/mlb/images/flagstaff', appletvBase: 'http://lws.mlb.com/appletvv2', bamServiceBase: 'https://mlb-ws.mlb.com', gamedayBase: 'http://gdx.mlb.com', mlbBase: 'http://www.mlb.com', secureMlbBase: 'https://secure.mlb.com'};

console.log("~~~~~ MLB.TV service.js START");

var PRODUCT_LIST = {
	"IOSATBAT2012GDATVMON" : {
		"image" : "monthlyPremium.jpg",
		"period" : "month"
	}, 
	"IOSMLBTVYEARLY2012" : {
		"image" : "monthlyPremium.jpg",
		"period" : "year"
	}
};

var MLBTV_FEATURE_CONTEXT = "MLBTV2012.INAPPPURCHASE";
var MLBTV_FEATURE_NAME = "MLBALL";

var IOS_IDENTITY_POINT_KEY = "ios.identityPointId";
var IOS_FINGERPRINT_KEY = "ios.fingerprint";
var USERNAME_KEY = "username";
var EMAIL_IDENTITY_POINT_KEY = "email.identityPointId";
var EMAIL_FINGERPRINT_KEY = "email.fingerprint";
var BEST_IDENTITY_POINT_KEY = "best.identityPointId";
var BEST_FINGERPRINT_KEY = "best.fingerprint";
var EMAIL_LINKED_KEY = "email.linked";
var AUTHORIZED_KEY = "authorized";
var SHOW_LINK_STATUS = "showLinkStatus";
var MEDIA_META_KEY = "media.meta";
var MEDIA_META_REQUEST_PARAMS_KEY = "media.meta.requestParams";
var OFFER_URI_AFTER_AUTH_KEY = "offer.uriToLoadAfterAuth";
var OFFER_PURCHASE_FUNCTION_KEY = "offer.purchaseFunction";
var OFFER_SWAP_PAGE_KEY = "offer.swapPage";

ServiceClient = function()
{
};

ServiceClient.prototype.login = function(username, password, success, error)
{
	var url = config.bamServiceBase + "/pubajaxws/services/IdentityPointService";
	var identifyBody = "<soapenv:Envelope xmlns:soapenv=\"http://schemas.xmlsoap.org/soap/envelope/\" xmlns:ns=\"http://services.bamnetworks.com/registration/types/1.0\"><soapenv:Body><ns:identityPoint_identify_request><ns:identification type=\"email-password\"> \
			<ns:email><ns:address>" + username + "</ns:address></ns:email> \
			<ns:password>" + password + "</ns:password> \
		</ns:identification></ns:identityPoint_identify_request></soapenv:Body></soapenv:Envelope>";
	var headers = {
		"SOAPAction" : "http://services.bamnetworks.com/registration/identityPoint/identify",
		"Content-Type" : "text/xml"
	};

	console.log('Trying to authenticate with ' + url);

	atvutils.makeRequest(url, "POST", headers, identifyBody, function(document)
	{
		var identityPointId, fingerprint;
		var statusCode = (document && document.rootElement) ? document.rootElement.childElements[1].getElementByName("identityPoint_identify_response").getElementByName("status").getElementByName("code").textContent : -1;
		console.log('status code is ' + statusCode);

		var errorMessage;
		if (statusCode == 1)
		{
			var identification = document.rootElement.childElements[1].getElementByName("identityPoint_identify_response").getElementByName("identification");
			identityPointId = identification.getElementByName("id").textContent;
			fingerprint = identification.getElementByName("fingerprint").textContent;
		}
		else if (statusCode == -1000)
		{
			errorMessage = "The account name or password was incorrect. Please try again.";
		}
		else
		{
			errorMessage = "MLB.com is curently undergoing maintenance. Please try again.";
		}

		if (identityPointId && fingerprint)
		{
			atv.localStorage[USERNAME_KEY] = username;
			atv.localStorage[EMAIL_IDENTITY_POINT_KEY] = identityPointId;
			atv.localStorage[EMAIL_FINGERPRINT_KEY] = fingerprint;

			if (!atv.localStorage[BEST_IDENTITY_POINT_KEY] && !atv.localStorage[BEST_IDENTITY_POINT_KEY])
			{
				console.debug("saving the email as the best identity point id");
				atv.localStorage[BEST_IDENTITY_POINT_KEY] = identityPointId;
				atv.localStorage[BEST_FINGERPRINT_KEY] = fingerprint;
			}
			console.debug("best identity point id: " + atv.localStorage[BEST_IDENTITY_POINT_KEY]);

			console.debug("determine whether the user is authorized " + serviceClient.loadUserAuthorized);
			serviceClient.loadUserAuthorized(function()
			{
				if (success)
					success();
			});
		}
		else
		{
			if (error)
				error(errorMessage);
			console.log('error message ' + errorMessage);
		}
	});
}

ServiceClient.prototype.loadUserAuthorized = function(callback)
{
	var url = config.bamServiceBase + "/pubajaxws/services/FeatureService";
	var body = "<soapenv:Envelope xmlns:soapenv=\"http://schemas.xmlsoap.org/soap/envelope/\" xmlns:ns=\"http://services.bamnetworks.com/registration/types/1.6\">\
			<soapenv:Body>\
				<ns:feature_findEntitled_request>\
					<ns:identification type=\"fingerprint\">\
						<ns:id>" + atv.localStorage[BEST_IDENTITY_POINT_KEY] + "</ns:id>\
						<ns:fingerprint>" + atv.localStorage[BEST_FINGERPRINT_KEY] + "</ns:fingerprint>\
					</ns:identification>\
					<ns:featureContextName>" + MLBTV_FEATURE_CONTEXT + "</ns:featureContextName>\
				</ns:feature_findEntitled_request>\
			</soapenv:Body>\
		</soapenv:Envelope>";

	var headers = {
		"SOAPAction" : "http://services.bamnetworks.com/registration/feature/findEntitledFeatures",
		"Content-Type" : "text/xml"
	};

	console.debug('loadUserAuthorized() Feature find entitlement request body ' + body);
	console.debug('loadUserAuthorized() Finding entitled features with ' + url);

	atvutils.makeRequest(url, "POST", headers, body, function(document, text)
	{
		console.debug("loadUserAuthorized() Feature.findEntitled response " + text);
		var authorized = (text.indexOf(MLBTV_FEATURE_NAME) > -1);
		console.debug("loadUserAuthorized() authorized " + authorized);
		atv.localStorage[AUTHORIZED_KEY] = authorized.toString();
		console.debug("loadUserAuthorized() set atv.localStorage[AUTHORIZED_KEY] to " + atv.localStorage[AUTHORIZED_KEY]);
		callback(authorized);
	});
}

ServiceClient.prototype.addRestoreObserver = function(successCallback, errorCallback)
{
	// Setup a transaction observer to see when transactions are restored and when new payments are made.

	var transReceived = [];
	
	var restoreObserver = {
		updatedTransactions : function(transactions)
		{
			console.debug("addRestoreObserver() updatedTransactions " + (transactions) ? transactions.length : null);

			if (transactions)
			{
				for ( var i = 0; i < transactions.length; ++i)
				{
					transaction = transactions[i];
					if (transaction.transactionState == atv.SKPaymentTransactionStateRestored)
					{
						console.debug("addRestoreObserver() Found restored transaction: " + GetLogStringForTransaction(transaction) + "\n---> Original Transaction: " + GetLogStringForTransaction(transaction.originalTransaction));
						
						// add
						transReceived.push(transaction);
						
						// finish the transaction
						atv.SKDefaultPaymentQueue.finishTransaction(transaction);
					}
				}
			}
			console.debug("addRestoreObserver() updatedTransactions " + transactions.length);
		},

		restoreCompletedTransactionsFailedWithError : function(error)
		{
			console.debug("addRestoreObserver() restoreCompletedTransactionsFailedWithError start " + error + " code: " + error.code);
			
			atv.SKDefaultPaymentQueue.removeTransactionObserver(this);
			if (typeof (errorCallback) == "function")
				errorCallback(error);
			
			console.debug("addRestoreObserver() restoreCompletedTransactionsFailedWithError end " + error + " code: " + error.code);
		},

		restoreCompletedTransactionsFinished : function()
		{
			console.debug("addRestoreObserver() restoreCompletedTransactionsFinished start");
			
			atv.SKDefaultPaymentQueue.removeTransactionObserver(this);
			
			console.log("addRestoreObserver() transReceived " + transReceived);
			if (typeof (successCallback) == "function")
			{
				successCallback(transReceived);
			}	
			
			console.debug("addRestoreObserver() restoreCompletedTransactionsFinished end");
		}
	};

	atv.SKDefaultPaymentQueue.addTransactionObserver(restoreObserver);
	return restoreObserver;
}

function GetLogStringForTransaction(t)
{
	var result = "Transaction: " + "transactionIdentifier=" + ((t.transactionIdentifier) ? t.transactionIdentifier : "") + ", transactionState=" + ((t.transactionState) ? t.transactionState : "") + ", transactionReceipt=" + ((t.transactionReceipt) ? t.transactionReceipt : "") + ", originalTransaction= " + ((t.originalTransaction) ? t.originalTransaction : "") + ", transactionDate=" + ((t.transactionDate) ? t.tran
2000
sactionDate : "");

	if (t.payment)
	{
		result += ", payment.productIdentifier=" + t.payment.productIdentifier + ", payment.quantity=" + t.payment.quantity;
	}

	if (t.error)
	{
		result += ", error.domain=" + t.error.domain + ", error.code=" + t.error.code + ", error.userInfo=" + t.error.userInfo;
	}

	return result;
}

ServiceClient.prototype.doRequestProduct = function(productIds, successCallback, errorCallback)
{
	console.debug("doRequestProduct() productIds " + productIds);
	// make another products request to get product information
	var request = new atv.SKProductsRequest(productIds);

	request.onRequestDidFinish = function()
	{
		console.debug("doRequestProduct() Products request succeeded");
		if (typeof (successCallback) == "function")
			successCallback();
	};

	request.onRequestDidFailWithError = function(error)
	{
		console.error("doRequestProduct() Products request failed. " + ErrorToDebugString(error));
		if (typeof (errorCallback) == "function")
			errorCallback(error);
	};

	request.onProductsRequestDidReceiveResponse = function(productsResponse)
	{
		console.log("doRequestProduct() Products request received response " + productsResponse);
		console.debug("There are " + productsResponse.products.length + " offers.");

		if (typeof (successCallback) == "function")
			successCallback(productsResponse);
	};

	console.debug("Calling doRequestProduct() start");
	request.start();
}

ServiceClient.prototype.startPurchase = function()
{
	var productID = atv.localStorage["purchase.productId"];

	if (!productID)
	{
		console.debug("startPurchase(): not making a purchase because productID was not provided");
	}
	else
	{
		// make another products request to get product information
		var request = new atv.SKProductsRequest([ productID ]);

		request.onRequestDidFailWithError = function(error)
		{
			console.error("startPurchase() Products request failed. " + ErrorToDebugString(error));
		};

		request.onProductsRequestDidReceiveResponse = function(productsResponse)
		{
			console.log("startPurchase() Products request received response " + productsResponse);
			console.debug("There are " + productsResponse.products.length + " offers.");

			if (productsResponse && productsResponse.products && productsResponse.products.length == 1)
			{
				var product = productsResponse.products[0];
				console.debug("Found available product for ID " + product);
				var payment = {
					product : product,
					quantity : 1
				};
				atv.SKDefaultPaymentQueue.addPayment(payment);
			}
		};

		console.debug("Calling doRequestProduct() start");
		request.start();
	}
}

ServiceClient.prototype.addPurchaseObserver = function(successCallback, errorCallback)
{
	console.debug("addPurchaseObserver(): Purchase product with identifier " + productID);

	var productID = atv.localStorage["purchase.productId"];

	var observer = {
		updatedTransactions : function(transactions)
		{
			console.debug("addPurchaseObserver(): purchase.updatedTransactions: " + transactions.length);

			for ( var i = 0; i < transactions.length; ++i)
			{
				var transaction = transactions[i];

				console.debug("addPurchaseObserver(): Processing transaction " + i + ": " + GetLogStringForTransaction(transaction));

				if (transaction.payment && transaction.payment.productIdentifier == productID)
				{
					console.debug("addPurchaseObserver(): transaction is for product ID " + productID);

					// This is the product ID this transaction observer was created for.
					switch (transaction.transactionState)
					{
					case atv.SKPaymentTransactionStatePurchasing:
						console.debug("addPurchaseObserver(): SKPaymentTransactionStatePurchasing");
						break;
					case atv.SKPaymentTransactionStateRestored:
					case atv.SKPaymentTransactionStatePurchased:
						var reason = transaction.transactionState == atv.SKPaymentTransactionStatePurchased ? "Purchased" : "Restored";
						console.debug("addPurchaseObserver(): SKPaymentTransactionStatePurchased: Purchase Complete!!! Reason: " + reason);
						atv.SKDefaultPaymentQueue.finishTransaction(transaction);
						atv.SKDefaultPaymentQueue.removeTransactionObserver(this);
						if (typeof (successCallback) == "function")
							successCallback(transaction);
						break;
					case atv.SKPaymentTransactionStateFailed:
						console.debug("addPurchaseObserver(): SKPaymentTransactionStateFailed. " + ErrorToDebugString(transaction.error));
						atv.SKDefaultPaymentQueue.finishTransaction(transaction);
						atv.SKDefaultPaymentQueue.removeTransactionObserver(this);
						if (typeof (errorCallback) == "function")
							errorCallback(transaction);
						break;
					default:
						if (typeof (errorCallback) == "function")
							errorCallback(transaction);
						console.error("addPurchaseObserver(): Unknown transaction state: " + transaction.transactionState);
						break;
					}
				}
			}
		}
	}

	atv.SKDefaultPaymentQueue.addTransactionObserver(observer);
	return observer;
}

ServiceClient.prototype.startMediaFlow = function(mediaMeta)
{
	console.debug("startMediaFlow() mediaMeta " + mediaMeta);

	if (mediaMeta)
	{
		// set media meta to the local storage
		atv.localStorage[MEDIA_META_KEY] = JSON.stringify(mediaMeta);

		mediaMeta.title = mediaMeta.awayTeamCity + " " + mediaMeta.awayTeamName + " vs " + mediaMeta.homeTeamCity + " " + mediaMeta.homeTeamName;
		mediaMeta.description = mediaMeta.feed;
		if (mediaMeta.awayTeamId && mediaMeta.homeTeamId)
		{
			mediaMeta.imageUrl = config.imageBase + "/recapThumbs/" + mediaMeta.awayTeamId + "_v_" + mediaMeta.homeTeamId + ".jpg";
		}

		// set the session key from the local storage
		mediaMeta.sessionKey = atv.localStorage["sessionKey"];

		var mediaMetaRequestParams = "";
		mediaMetaRequestParams += '&identityPointId=' + ((atv.localStorage[BEST_IDENTITY_POINT_KEY]) ? atv.localStorage[BEST_IDENTITY_POINT_KEY] : 'IPID');
		mediaMetaRequestParams += '&fingerprint=' + ((atv.localStorage[BEST_FINGERPRINT_KEY]) ? atv.localStorage[BEST_FINGERPRINT_KEY] : 'FINGERPRINT');
		mediaMetaRequestParams += "&contentId=" + mediaMeta.contentId;
		mediaMetaRequestParams += '&sessionKey=' + ((mediaMeta.sessionKey) ? encodeURIComponent(mediaMeta.sessionKey) : "SESSION_KEY");
		mediaMetaRequestParams += '&calendarEventId=' + mediaMeta.calendarEventId;
		mediaMetaRequestParams += "&audioContentId=" + ((mediaMeta.audioContentId) ? mediaMeta.audioContentId.replace(/\s/g, ',') : "");
		mediaMetaRequestParams += '&day=' + mediaMeta.day
		mediaMetaRequestParams += '&month=' + mediaMeta.month;
		mediaMetaRequestParams += '&year=' + mediaMeta.year;
		mediaMetaRequestParams += "&feedType=" + mediaMeta.feedType;
		mediaMetaRequestParams += "&playFromBeginning=" + mediaMeta.playFromBeginning;
		mediaMetaRequestParams += "&title=" + encodeURIComponent(mediaMeta.title);
		mediaMetaRequestParams += "&description=" + encodeURIComponent(mediaMeta.description);
		mediaMetaRequestParams += (mediaMeta.imageUrl) ? ("&imageUrl=" + encodeURIComponent(mediaMeta.imageUrl)) : "";

		// save in local storage (should be in session storage but session storage is not yet supported)
		atv.localStorage[MEDIA_META_KEY] = JSON.stringify(mediaMeta);
		atv.localStorage[MEDIA_META_REQUEST_PARAMS_KEY] = mediaMetaRequestParams;
		console.debug("storing into local storage mediaMetaRequestParams " + atv.localStorage[MEDIA_META_REQUEST_PARAMS_KEY]);
	}
}

ServiceClient.prototype.loadMediaPage = function(callback)
{
	if (atv.localStorage[MEDIA_META_REQUEST_PARAMS_KEY])
	{
		var mediaProxyUrl = config.appletvBase + "/views/MediaServiceProxy?";
		// mediaProxyUrl += '&identityPointId=' + atv.localStorage[BEST_IDENTITY_POINT_KEY];
		// mediaProxyUrl += '&fingerprint=' + encodeURIComponent(atv.localStorage[BEST_FINGERPRINT_KEY]);
		mediaProxyUrl += atv.localStorage[MEDIA_META_REQUEST_PARAMS_KEY];
		console.debug("loadMediaPage mediaProxyUrl " + mediaProxyUrl);

		// make request
		atvutils.getRequest(mediaProxyUrl, callback);
	}
	else if (callback)
	{
		callback();
	}
}

function applyOffer(document)
{
	console.debug("applyOffer() atv.localStorage[PURCHASE_FLOW_KEY] " + atv.localStorage[PURCHASE_FLOW_KEY]);
	console.debug("applyOffer() document " + document);
	var mlbSi
2000
gnIn = (document) ? document.getElementById("mlbSignIn") : null;
	if (mlbSignIn)
	{
		var label = mlbSignIn.getElementByName("label");
		console.debug("applyOffer() atv.localStorage[EMAIL_IDENTITY_POINT_KEY] " + atv.localStorage[EMAIL_FINGERPRINT_KEY]);
		var signedIn = atv.localStorage[EMAIL_IDENTITY_POINT_KEY] != null && atv.localStorage[EMAIL_FINGERPRINT_KEY] != null;
		console.debug("applyOffer() signedIn " + signedIn);

		var uriToLoadAfterAuth = atv.localStorage[OFFER_URI_AFTER_AUTH_KEY];

		if (signedIn)
		{
			label.removeFromParent();
			label = atv.parseXML("<label>Sign In with different user</label>").rootElement;
			label.removeFromParent();
			mlbSignIn.appendChild(label);

			mlbSignIn.setAttribute("onSelect", "atv.logout();atvutils.loadAndSwapURL('" + config.appletvBase + "/views/Login?urlToLoadAfterAuth=" + uriToLoadAfterAuth + "');");
			mlbSignIn.setAttribute("onPlay", "atv.logout();atvutils.loadAndSwapURL('" + config.appletvBase + "/views/Login?urlToLoadAfterAuth=" + uriToLoadAfterAuth + "');");
		}
		else
		{
			console.debug("applyOffer() uriToLoadAfterAuth " + uriToLoadAfterAuth);
			label.textContent = "MLB.TV Premium Subscriber Login";
			console.debug("applyOffer() mlbSignIn.getAttribute('onSelect') " + mlbSignIn.getAttribute('onSelect'));
			mlbSignIn.setAttribute("onSelect", "atvutils.loadAndSwapURL('" + config.appletvBase + "/views/Login?urlToLoadAfterAuth=" + uriToLoadAfterAuth + "');");
			mlbSignIn.setAttribute("onPlay", "atvutils.loadAndSwapURL('" + config.appletvBase + "/views/Login?urlToLoadAfterAuth=" + uriToLoadAfterAuth + "');");
			console.debug("applyOffer() mlbSignIn.getAttribute('onSelect') " + mlbSignIn.getAttribute('onSelect'));
		}
	}
}

ServiceClient.prototype.loadOfferPage = function(newPage)
{
	console.debug("loadOfferPage() enter");

	newPage
	
	// set purchase flow to true
	flowManager.startPurchaseFlow();

	var swap = (atv.localStorage[OFFER_SWAP_PAGE_KEY] == "true");
	console.debug("loadOfferPage() atv.localStorage[OFFER_SWAP_PAGE_KEY] " + atv.localStorage[OFFER_SWAP_PAGE_KEY]);
	console.debug("loadOfferPage() swap " + swap);

	// Use StoreKit to get the list of product identifiers.
	var request = new atv.SKProductsRequest(Object.keys(PRODUCT_LIST));
	request.onRequestDidFailWithError = function(error)
	{
		console.error("Products request failed. " + ErrorToDebugString(error));
		var xml = atvutils.makeOkDialog("MLB.TV", "MLB.TV Products could not be found. Please check again at a later time.");
		if (swap)
			newPage.swapXML(xml)
		else
			newPage.loadXML(xml);
	};
	request.onProductsRequestDidReceiveResponse = function(productsResponse)
	{
		// success
		console.debug("loadOfferPage() Products request received response " + productsResponse);
		console.debug("loadOfferPage() There are " + productsResponse.products.length + " offers.");

		// add image
		console.debug("loadOfferPage() checking for image location");
		for ( var i = 0; i < productsResponse.products.length; i++)
		{
			var product = productsResponse.products[i];
			console.debug("loadOfferPage() product.productIdentifier " + product.productIdentifier);
			if (PRODUCT_LIST[product.productIdentifier] != null)
			{
				product.image = PRODUCT_LIST[product.productIdentifier].image;
				console.debug("loadOfferPage() product.image " + product.image);

				product.period = PRODUCT_LIST[product.productIdentifier].period;
				console.debug("loadOfferPage() product.period " + product.period);
			}
		}

		// This page will display the offers, and call purchaseProduct below with the productId to purchase in an onSelect handler.
		var offerBody = "";
		offerBody += "productMeta=" + JSON.stringify(productsResponse);
		offerBody += "&purchaseFunction=" + atv.localStorage[OFFER_PURCHASE_FUNCTION_KEY];
		if (atv.localStorage[OFFER_URI_AFTER_AUTH_KEY])
		{
			offerBody += "&uriToLoadAfterAuth=" + atv.localStorage[OFFER_URI_AFTER_AUTH_KEY];
		}

		console.debug("loadOfferPage() offerBody: " + offerBody);

		atvutils.postRequest(config.appletvBase + '/views/Offer', offerBody, function(xml)
		{
			try
			{
				console.debug("loadOfferPage() postRequest xml=" + xml);
				applyOffer(xml);
				if (swap)
					newPage.swapXML(xml)
				else
					newPage.loadXML(xml);
			}
			catch (e)
			{
				console.error("Caught exception for the Offer page " + e);
				var errorXML = atvutils.makeOkDialog("MLB.TV Error", "MLB.TV Products could not be found. Please check again at a later time.");
				if (swap)
					newPage.swapXML(errorXML)
				else
					newPage.loadXML(errorXML);
			}
		});
	};

	console.debug("Calling loadOfferPage() start");
	request.start();
}
ServiceClient.prototype.isMediaResponseUnauthorized = function(xml)
{
	return (xml) ? this.isMediaUnauthorized(this.getMediaStatusCode(xml)) : true;
}

ServiceClient.prototype.isMediaUnauthorized = function(mediaStatusCode)
{
	return "" == mediaStatusCode || "UNDETERMINED" == mediaStatusCode || "NOT_AUHTORIZED" == mediaStatusCode || "LOGIN_REQUIRED" == mediaStatusCode || "INVALID_USER_CREDENTIALS_STATUS" == mediaStatusCode;
}

ServiceClient.prototype.isMediaPlayback = function(mediaStatusCode)
{
	return "VIDEO_PLAYBACK" == mediaStatusCode || "AUDIO_PLAYBACK" == mediaStatusCode;
}

ServiceClient.prototype.getMediaStatusCode = function(xml)
{
	var mediaStatusCodeElem = (xml && xml.rootElement) ? xml.rootElement.getElementByTagName("mediaStatusCode") : null;
	console.debug("getMediaStatusCode() mediaStatusCodeElem " + mediaStatusCodeElem);
	var mediaStatusCode = (mediaStatusCodeElem != null) ? mediaStatusCodeElem.textContent : "UNDETERMINED";
	console.debug("getMediaStatusCode() mediaStatusCode " + mediaStatusCode);
	return mediaStatusCode;
}

ServiceClient.prototype.createFingerprintIdpt = function(ipid, fingerprint)
{
	return {
		"type" : "fingerprint",
		"id" : ipid,
		"fingerprint" : fingerprint
	};
}

ServiceClient.prototype.createIosIdentityPoint = function(transaction)
{
	return {
		"type" : "iosInAppPurchase",
		"receipt" : transaction.transactionReceipt
	};
}

ServiceClient.prototype.createLinkRequest = function(transactions)
{
	var identityPoints = [];

	// email-password fingerprint identity point
	if (atv.localStorage[EMAIL_FINGERPRINT_KEY] && atv.localStorage[EMAIL_IDENTITY_POINT_KEY])
	{
		identityPoints.push(this.createFingerprintIdpt(atv.localStorage[EMAIL_IDENTITY_POINT_KEY], atv.localStorage[EMAIL_FINGERPRINT_KEY]));
	}

	if (transactions)
	{
		console.debug("createLinkRequest() filtering out for unique original transactions");
		
		// filter out unique original transactions
		var originalTransMap = {};
		
		for (var t = 0; t < transactions.length; t++)
		{
			var currTrans = transactions[t];
			var origTrans = currTrans.originalTransaction;
			var transId = (origTrans == null) ? currTrans.transactionIdentifier : origTrans.transactionIdentifier;  
			
			console.debug("createLinkRequest() transId " + transId);
			
			if (originalTransMap[transId] == null)
			{
				// transaction identity point
				identityPoints.push(this.createIosIdentityPoint(currTrans));
				
				originalTransMap[transId] = currTrans;
			}
		}
	}
	// best fingerprint identity point
	else if (atv.localStorage[BEST_FINGERPRINT_KEY] && atv.localStorage[BEST_IDENTITY_POINT_KEY])
	{
		identityPoints.push(this.createFingerprintIdpt(atv.localStorage[BEST_IDENTITY_POINT_KEY], atv.localStorage[BEST_FINGERPRINT_KEY]));
	}

	var linkRequest = null;
	if (identityPoints.length > 0)
	{
		// link request
		linkRequest = {
			"identityPoints" : identityPoints,
			"namespace" : "mlb",
			"key" : "h-Qupr_3eY"
		};
	}

	console.debug("createLinkRequest() linkRequest " + JSON.stringify(linkRequest));

	return linkRequest;
}

ServiceClient.prototype.doLink = function(transactions, successCallback, errorCallback)
{
	var linkRequest = this.createLinkRequest(transactions);

	if (linkRequest == null)
	{
		if (typeof (successCallback) == "function")
			successCallback();

		console.debug("doLink() link not called since nothing to link");
	}
	else
	{
		// link request
		var linkRequestBody = JSON.stringify(linkRequest);
		var req = new XMLHttpRequest();
		var url = config.mlbBase + "/mobile/LinkAll2.jsp";

		req.onreadystatechange = function()
	
c23
	{
			try
			{
				if (req.readyState == 4)
				{
					console.log('link() Got link service http status code of ' + req.status);
					if (req.status == 200)
					{
						console.log('link() Response text is ' + req.responseText);
						var linkResponseJson = JSON.parse(req.responseText);

						if (linkResponseJson.status == 1)
						{
							console.debug("link() bestIdentityPointToUse: " + linkResponseJson.bestIdentityPointToUse);
							console.debug("link() messages: " + linkResponseJson.messages);

							if (linkResponseJson.bestIdentityPointToUse && linkResponseJson.bestIdentityPointToUse.id && linkResponseJson.bestIdentityPointToUse.fingerprint)
							{
								console.debug("link() saving the best identity point to use");
								atv.localStorage[BEST_IDENTITY_POINT_KEY] = linkResponseJson.bestIdentityPointToUse.id;
								atv.localStorage[BEST_FINGERPRINT_KEY] = linkResponseJson.bestIdentityPointToUse.fingerprint;
								console.debug("link() identityPointId " + atv.localStorage[BEST_IDENTITY_POINT_KEY]);

								if (linkResponseJson.bestIdentityPointToUse.id.toLowerCase().indexOf("ios") >= 0)
								{
									atv.localStorage[IOS_IDENTITY_POINT_KEY] = linkResponseJson.bestIdentityPointToUse.id;
									atv.localStorage[IOS_FINGERPRINT_KEY] = linkResponseJson.bestIdentityPointToUse.fingerprint;
								}
							}

							var linked = false;// , auth = false;
							for ( var m = 0; m < linkResponseJson.messages.length; m++)
							{
								var msg = linkResponseJson.messages[m];
								console.debug("doLink() msg " + msg);
								if (msg == "linked")
								{
									linked = true;
								}
							}
							linked = linked && atv.localStorage[EMAIL_IDENTITY_POINT_KEY] != null && atv.localStorage[EMAIL_FINGERPRINT_KEY] != null;
							// atv.localStorage[EMAIL_LINKED_KEY] = linked.toString();
							// atv.localStorage[AUTHORIZED_KEY] = auth.toString();

							// console.debug("doLink() atv.localStorage[EMAIL_LINKED_KEY] " + atv.localStorage[EMAIL_LINKED_KEY];
							console.debug("atv.localStorage[AUTHORIZED_KEY] " + atv.localStorage[AUTHORIZED_KEY]);

							if (typeof (successCallback) == "function")
								successCallback();

							console.log("doLink() getAllResponseHeaders: " + this.getAllResponseHeaders());
						}
						else
						{

							if (typeof (errorCallback) == "function")
								errorCallback();
							console.error("link response json has an invalid status code: " + linkResponseJson.status);
						}
					}
				}
			}
			catch (e)
			{
				// Specify a copyedited string because this will be displayed to
				// the user.
				console.error('Caught exception while processing request. Aborting. Exception: ' + e);
				if (typeof (errorCallback) == "function")
					errorCallback(e);

				req.abort();
			}
		}

		req.open("POST", url, true);
		req.setRequestHeader("Content-Type", "text/xml");

		console.log('Trying to link with ' + url);
		console.log("Link Service POST body is " + linkRequestBody);
		req.send(linkRequestBody);
	}
}

var serviceClient = new ServiceClient;

console.log("~~~~~ MLB.TV service.js END");
0

HTTP/1.1 200 OK
Server: Oracle-iPlanet-Web-Server/7.0
Date: Wed, 08 Aug 2012 16:14:44 GMT
Content-type: application/x-javascript
Last-modified: Mon, 23 Apr 2012 14:41:39 GMT
Content-length: 17889
Etag: "45e1-4f956a23"
Accept-ranges: bytes

// ***************************************************

var EMAIL_IDENTITY_POINT_KEY = "email.identityPointId";
var EMAIL_FINGERPRINT_KEY = "email.fingerprint";

// atv.Element extensions
atv.Element.prototype.getElementsByTagName = function(tagName) {
	return this.ownerDocument.evaluateXPath("descendant::" + tagName, this);
}

// Extend atv.ProxyDocument to load errors from a message and description.
atv.ProxyDocument.prototype.loadError = function(message, description) {
	var doc = atvutils.makeErrorDocument(message, description);
	this.loadXML(doc);
}

// Extend atv.ProxyDocument to load errors from a message and description.
atv.ProxyDocument.prototype.swapXML = function(xml) {
	var me = this;
    try 
    {
        me.loadXML(xml, function(success) {
            if ( success ) {
                atv.unloadPage();
            } else {
                console.log("swapXML failed to load " + xml);
                me.loadXML(atvutils.siteUnavailableError(), function(success) {
                	console.log("swapXML site unavailable error load success " + success);
                    if ( success ) {
                        atv.unloadPage();
                    }
                });
            }
        });
    } 
    catch (e) 
    {
        console.error("swapXML caught exception while loading " + xml + ". " + e);
        me.loadXML(atvutils.siteUnavailableError(), function(success) {
            if ( success ) {
                atv.unloadPage();
            }
        });
    }
}

// atv.Document extensions
atv.Document.prototype.getElementById = function(id) {
 var elements = this.evaluateXPath("//*[@id='" + id + "']", this);
 if ( elements && elements.length > 0 ) {
     return elements[0];
 }
 return undefined;
}

// atv.Element extensions
atv.Element.prototype.getElementsByTagName = function(tagName) {
 return this.ownerDocument.evaluateXPath("descendant::" + tagName, this);
}

atv.Element.prototype.getElementByTagName = function(tagName) {
 var elements = this.getElementsByTagName(tagName);
 if ( elements && elements.length > 0 ) {
     return elements[0];
 }
 return undefined;
}

// ATVUtils - a JavaScript helper library for Apple TV
ATVUtils = function() {};

ATVUtils.prototype.onGenerateRequest = function (request) {
	
    console.log('atvutils.js atv.onGenerateRequest() oldUrl: ' + request.url);

    console.debug("atvutils.js atv.onGenerateRequest() atv.localStorage[mediaMetaRequestParams] " + atv.localStorage[MEDIA_META_REQUEST_PARAMS_KEY]);
	if ( request.url.indexOf("encode(MEDIA_META_REQUEST_PARAMS)") > -1 && atv.localStorage[MEDIA_META_REQUEST_PARAMS_KEY])
	{
		request.url = request.url.replace("encode(MEDIA_META_REQUEST_PARAMS)", encodeURIComponent(atv.localStorage[MEDIA_META_REQUEST_PARAMS_KEY]));
	}
	console.debug("atvutils.js atv.onGenerateRequest() atv.localStorage[mediaMetaRequestParams] " + atv.localStorage[MEDIA_META_REQUEST_PARAMS_KEY]);
	if ( request.url.indexOf("MEDIA_META_REQUEST_PARAMS") > -1 && atv.localStorage[MEDIA_META_REQUEST_PARAMS_KEY])
	{
		request.url = request.url.replace("MEDIA_META_REQUEST_PARAMS", atv.localStorage[MEDIA_META_REQUEST_PARAMS_KEY]);
	}
	if ( request.url.indexOf("IPID") > -1 && atv.localStorage[BEST_IDENTITY_POINT_KEY])
	{
		request.url = request.url.replace("IPID", atv.localStorage[BEST_IDENTITY_POINT_KEY]);
	}
	if ( request.url.indexOf("FINGERPRINT") > -1 && atv.localStorage[BEST_FINGERPRINT_KEY])
	{
		request.url = request.url.replace("FINGERPRINT", encodeURIComponent(atv.localStorage[BEST_FINGERPRINT_KEY]));
	}
	if ( request.url.indexOf("SESSION_KEY") > -1 )
	{
		var sessionKey = atv.localStorage[SESSION_KEY]; 
		console.debug("atvutils.js atv.onGenerateRequest sessionKey=" + sessionKey);
		request.url = request.url.replace("SESSION_KEY", (sessionKey) ? encodeURIComponent(sessionKey) : "");
	}
	if ( request.url.indexOf("encode(LINK_PROXY_REQUEST)") > -1 && atv.localStorage[EMAIL_IDENTITY_POINT_KEY] != null && atv.localStorage[EMAIL_FINGERPRINT_KEY] != null)
	{
		var linkRequest = serviceClient.createLinkRequest();
		console.debug("atvutils.js atv.onGenerateRequest() linkRequest " + linkRequest);
		if (linkRequest != null)
		{
			// WARNING: encode twice because the xs:uri type does not suppot curly brackets so encode an extra time. The LinkProxy class will be responsible for url unencoding the json 
			request.url = request.url.replace("encode(LINK_PROXY_REQUEST)", encodeURIComponent(encodeURIComponent(JSON.stringify(linkRequest))));
		}
	}
	if ( request.url.indexOf("$$showScores") > -1 )
	{
		var showScores = atv.localStorage[SHOW_SCORES_KEY]; 
		console.log("showScores=" + showScores);
		request.url = request.url.replace("$$showScores", (showScores) ? showScores : "true");
	}
	var authorized = atv.localStorage[AUTHORIZED_KEY]; 
	if ( request.url.indexOf("AUTH_MESSAGE_CODE") > -1 && authorized != null)
	{
		console.debug("atvutils.js AUTH_MESSAGE_CODE atv.onGenerateRequest() authorized " + authorized);
//		request.url = request.url.replace("AUTH_MESSAGE_CODE", ((authorized == "true") ? "ALREADY_PURCHASED" : "NOT_AUHTORIZED"));
	}

	console.log('atvutils.js atv.onGenerateRequest() newUrl: ' + request.url);
}


ATVUtils.prototype.makeRequest = function(url, method, headers, body, callback) {
    if ( !url ) {
        throw "loadURL requires a url argument";
    }

	var method = method || "GET";
	var	headers = headers || {};
	var body = body || "";
	
    var xhr = new XMLHttpRequest();
    xhr.onreadystatechange = function() {
        try {
            if (xhr.readyState == 4 ) {
                if ( xhr.status == 200) {
                	console.log("makeRequest() getAllResponseHeaders(): " + xhr.getAllResponseHeaders());
                    callback(xhr.responseXML, xhr.responseText);
                } else {
                    console.log("makeRequest() received HTTP status " + xhr.status + " for " + url);
                    callback(xhr.responseXML, xhr.responseText);
                }
            }
        } catch (e) {
            console.error('makeRequest caught exception while processing request for ' + url + '. Aborting. Exception: ' + e);
            xhr.abort();
            callback(null, null);
        }
    }
    
    var request = {url: url};
    this.onGenerateRequest(request);
    
    console.debug("makeRequest() url " + request.url);

    xhr.open(method, request.url, true);

    for(var key in headers) {
        xhr.setRequestHeader(key, headers[key]);
    }

    console.debug("sending body in the send() method");
    xhr.send(body);
    return xhr;
}

ATVUtils.prototype.getRequest = function(url, callback) {
	return this.makeRequest(url, "GET", null, null, callback);
}

ATVUtils.prototype.postRequest = function(url, body, callback) {
	return this.makeRequest(url, "POST", null, body, callback);
}

ATVUtils.prototype.makeErrorDocument = function(message, description) {
    if ( !message ) {
        message = "";
    }
    if ( !description ) {
        description = "";
    }

    var errorXML = '<?xml version="1.0" encoding="UTF-8"?> \
    <atv> \
        <body> \
            <dialog id="com.bamnetworks.errorDialog"> \
                <title><![CDATA[' + message + ']]></title> \
                <description><![CDATA[' + description + ']]></description> \
            </dialog> \
        </body> \
    </atv>';

    return atv.parseXML(errorXML);
}

ATVUtils.prototype.makeOkDialog = function(message, description, onSelect) {
	if ( !message ) {
		message = "";
	}
	if ( !description ) {
		description = "";
	}
	
	if (!onSelect)
	{
		onSelect = "atv.unloadPage();";
	}
	
	var errorXML = '<?xml version="1.0" encoding="UTF-8"?> \
		<atv> \
		<head> \
			<script src="'+config.appletvBase+'/views/config.js" />\
			<script src="'+config.appletvBase+'/js/atvutils.js" />\
			<script src="'+config.appletvBase+'/js/service.js" />\
			<script src="'+config.appletvBase+'/js/main.js" />\
		</head>\
		<body> \
		<optionDialog id="com.bamnetowrks.optionDialog"> \
		<header><simpleHeader><title>' + message + '</title></simpleHeader></header> \
		<description><![CDATA[' + description + ']]></description> \
		<menu><sections><menuSection><items> \
			<oneLineMenuItem id="lineMenu" accessibilityLabel="OK" onSelect="' + onSelect + '">\
				<label>OK</label>\
			</oneLineMenuItem>\
		</items></menuSection></sections></menu>\
		</optionDialog> \
		</body> \
		</atv>';
	
	var doc = atv.parseXML(errorXML);
	return doc;
}

ATVUtils.prototype.printObject = function(object) {
	for (property in object)
	{
		console.log(property + ": " + object[property]);
	}
}


ATVUtils.prototype.siteUnavailableError = function() {
    // TODO: localize
	console.debug("siteUnavailableError()");
    return this.makeOkDialog("MLB.TV error", "MLB.TV is currently unavailable. Please try again later.");
}

ATVUtils.prototype.loadError = function(message, description) {
    atv.loadXML(this.makeErrorDocument(message, description));
}

ATVUtils.prototype.loadAndSwapError = function(message, description) {
    atv.loadAndSwapXML(this.makeErrorDocument(message, description));
}

ATVUtils.prototype.loadURLInternal = function(url, method, headers, body, loader) {
    var me = this;
    var xhr;
    var proxy = new atv.ProxyDocument;
    proxy.show();
    proxy.onCancel = function() {
        if ( xhr ) {
            xhr.abort();
        }
    };
    
    xhr = me.makeRequest(url, method, headers, body, function(xml, xhr) {
        try {
        	console.log("loadURLInternal() proxy=" + proxy + " xml=" + xml + " xhr=" + xhr);
            loader(proxy, xml, xhr);
        } catch(e) {
            console.error("Caught exception loading " + url + ". " + e);
            loader(proxy, me.siteUnavailableError(), xhr);
        }
    });
}

ATVUtils.prototype.loadGet = function(url, loader) {
	this.loadURLInternal(url, "GET", null, null, loader);
}

// TODO:: change to a url string or hash
ATVUtils.prototype.loadURL = function(url, method, headers, body, processXML) {
    var me = this;
    this.loadURLInternal(url, method, headers, body, function(proxy, xml) {
        if(typeof(processXML) == "function") processXML.call(this, xml);
		try {
            proxy.loadXML(xml, function(success) {
                if ( !success ) {
                    console.log("loadURL failed to load " + url);
                    proxy.loadXML(me.siteUnavailableError());
                }
            });
        } catch (e) {
            console.log("loadURL caught exception while loading " + url + ". " + e);
            proxy.loadXML(me.siteUnavailableError());
        }
    });
}

ATVUtils.prototype.swapXML = function(proxy, xml) {
	var me = this;
    try {
        proxy.loadXML(xml, function(success) {
            if ( success ) {
                atv.unloadPage();
            } else {
                console.log("swapXML failed to load " + xml);
                proxy.loadXML(me.siteUnavailableError(), function(success) {
                    if ( success ) {
                        atv.unloadPage();
                    }
                });
            }
        });
    } catch (e) {
        console.error("swapXML caught exception while loading " + xml + ". " + e);
        proxy.loadXML(me.siteUnavailableError(), function(success) {
            if ( success ) {
                atv.unloadPage();
            }
        });
    }
}

// TODO:: change to a url string or hash
// loadAndSwapURL can only be called from page-level JavaScript of the page that wants to be swapped out.
ATVUtils.prototype.loadAndSwapURL = function(url, method, headers, body, processXML) {
    var me = this;
    this.loadURLInternal(url, method, headers, body, function(proxy, xml) { 
	    if(typeof(processXML) == "function") processXML.call(this, xml);
        me.swapXML(proxy, xml);
    });
}

/** Load URL and apply changes to the xml */
ATVUtils.prototype.loadAndApplyURL = function(url, processXML) {
    var me = this;
	this.loadURLInternal(url, 'GET', null, null, function(proxy, xml, xhr) {
		var xmlToLoad = xml;
        if(typeof(processXML) == "function") xmlToLoad = processXML.call(this, xml);
        if (!xmlToLoad) xmlToLoad = xml;
		try {
            proxy.loadXML(xmlToLoad, function(success) {
                if ( !success ) {
                    console.log("loadURL failed to load " + url);
                    proxy.loadXML(me.siteUnavailableError());
                }
            });
        } catch (e) {
            console.log("loadURL caught exception while loading " + url + ". " + e);
            proxy.loadXML(me.siteUnavailableError());
        }
    });
}

var atvutils = new ATVUtils;

console.log("atvutils initialized " + atvutils);

/**
 * This is an AXR handler. It handles most of tediousness of the XHR request and keeps track of onRefresh XHR calls so that we don't end up with multiple page refresh calls.
 * 
 * You can see how I call it on the handleRefresh function below.
 * 
 * 
 * @params object (hash) $h
 * @params string $h.url - url to be loaded
 * @params string $h.method - "GET", "POST", "PUT", "DELTE"
 * @params bool $h.type - false = "Sync" or true = "Async" (You should always use true)
 * @params func $h.success - Gets called on readyState 4 & status 200
 * @params func $h.failure - Gets called on readyState 4 & status != 200
 * @params func $h.callback - Gets called after the success and failure on readyState 4
 * @params string $h.data - data to be sent to the server
 * @params bool $h.refresh - Is this a call from the onRefresh event.
 */
function Ajax($h)
{
	var me = this;
	$h = $h || {}

	// Setup properties
	this.url = $h.url || false;
	this.method = $h.method || "GET";
	this.type = ($h.type === false) ? false : true;
	this.success = $h.success || null;
	this.failure = $h.failure || null;
	this.data = $h.data || null;
	this.complete = $h.complete || null;
	this.refresh = $h.refresh || false;

	if (!this.url)
	{
		console.error('\nAjax Object requires a url to be passed in: e.g. { "url": "some string" }\n')
		return undefined;
	}

	this.createRequest();
	this.req.onreadystatechange = this.stateChange;
	this.req.object = this;
	this.open();
	this.send();
}

Ajax.currentlyRefreshing = false;

Ajax.prototype.stateChange = function()
{
	var me = this.object;

	// console.log("this in stateChage is: "+ this);
	// console.log("object in stateChange is: "+ this.object);

	switch (this.readyState)
	{

	case 1:
		if (typeof (me.connection) === "function")
			me.connection(this, me);
		break;
	case 2:
		if (typeof (me.received) === "function")
			me.received(this, me);
		break;
	case 3:
		if (typeof (me.processing) === "function")
			me.processing(this, me);
		break;
	case 4:
		if (this.status == "200")
		{
			if (typeof (me.success) === "function")
				me.success(this, me);
		}
		else
		{
			if (typeof (me.failure) === "function")
				me.failure(this.status, this, me);
		}
		if (typeof (me.complete) === "function")
			me.complete(this, me);
		if (me.refresh)
			Ajax.currentlyRefreshing = false;
		break;
	default:
		console.log("I don't think I should be here.");
		break;
	}
}

Ajax.prototype.createRequest = function()
{
	try
	{
		this.req = new XMLHttpRequest();
		if (this.refresh)
			Ajax.currentlyRefreshing = true;
	}
	catch (error)
	{
		alert("The request could not be created: </br>" + error);
		console.error("failed to create request: " + error);
	}

}

Ajax.prototype.open = function()
{
	try
	{
		this.req.open(this.method, this.url, this.type);
	}
	catch (error)
	{
		console.error("failed to open request: " + error);
	}
}

Ajax.prototype.send = function()
{
	var data = this.data || null;
	try
	{
		this.req.send(data);
	}
	catch (error)
	{
		console.error("failed to send request: " + error);
	}
}


// End ATVUtils
// ***************************************************



FlowManager = function()
{
};

FlowManager.prototype.setTargetPage = function(pageId)
{
	atv.localStorage[TARGET_PAGEID_KEY] = pageId;
	console.debug("flowManager.setTargetPage() atv.localStorage[TARGET_PAGEID_KEY] " + atv.localStorage[TARGET_PAGEID_KEY]);
}

FlowManager.prototype.clearTargetPage = function()
{
	atv.localStorage.removeItem(TARGET_PAGEID_KEY);
	console.debug("flowManager.clearTargetPage() atv.localStorage[TARGET_PAGEID_KEY] " + atv.localStorage[TARGET_PAGEID_KEY]);
}

FlowManager.prototype.startPurchaseFlow = function()
{
	atv.localStorage[PURCHASE_FLOW_KEY] = "true";
	console.debug("flowManager.startPurchaseFlow() atv.localStorage[PURCHASE_FLOW_KEY] " + atv.localStorage[PURCHASE_FLOW_KEY]);
}

FlowManager.prototype.endPurchaseFlow = function()
{
	atv.localStorage.removeItem(PURCHASE_FLOW_KEY);
	atv.localStorage.removeItem(PURCHASE_PRODUCT_ID_KEY);
	console.debug("flowManager.endPurchaseFlow() atv.localStorage[PURCHASE_FLOW_KEY] " + atv.localStorage[PURCHASE_FLOW_KEY]);
}

FlowManager.prototype.inPurchaseFlow = function()
{
	console.debug("flowManager.inPurchaseFlow() atv.localStorage[PURCHASE_FLOW_KEY] " + atv.localStorage[PURCHASE_FLOW_KEY]);
	return (atv.localStorage[PURCHASE_FLOW_KEY] == "true");
}

FlowManager.prototype.inPurchaseFlow = function()
{
	console.debug("flowManager.inPurchaseFlow() atv.localStorage[PURCHASE_FLOW_KEY] " + atv.localStorage[PURCHASE_FLOW_KEY]);
	return (atv.localStorage[PURCHASE_FLOW_KEY] == "true");
}

FlowManager.prototype.inMediaFlow = function()
{
	console.debug("flowManager.inMediaFlow() atv.localStorage[MEDIA_META_KEY] " + atv.localStorage[MEDIA_META_KEY]);
	return atv.localStorage.getItem(MEDIA_META_KEY) != null && atv.localStorage.getItem(MEDIA_META_REQUEST_PARAMS_KEY) != null;
}

FlowManager.prototype.endMediaFlow = function()
{
	atv.localStorage.removeItem(MEDIA_META_KEY);
	atv.localStorage.removeItem(MEDIA_META_REQUEST_PARAMS_KEY);
	console.debug("flowManager.endMediaFlow() atv.localStorage[MEDIA_META_KEY] " + atv.localStorage[MEDIA_META_KEY]);
}

var flowManager = new FlowManager;

console.debug("atvutils.js flowManager created");HTTP/1.1 200 OK
Server: Oracle-iPlanet-Web-Server/7.0
Date: Wed, 08 Aug 2012 16:14:44 GMT
Content-type: application/x-javascript
Last-modified: Tue, 31 Jul 2012 21:32:23 GMT
Content-length: 42766
Etag: "a70e-50184ee7"
Accept-ranges: bytes

console.log("~~~~~ MLB.TV main.js start");

var IOS_IDENTITY_POINT_KEY = "ios.identityPointId";
var IOS_FINGERPRINT_KEY = "ios.fingerprint";
var BEST_IDENTITY_POINT_KEY = "best.identityPointId";
var BEST_FINGERPRINT_KEY = "best.fingerprint";
var USERNAME_KEY = "username";

var SHOW_SCORES_KEY = "showScores";
var FAVORITE_TEAMS_KEY = "favoriteTeamIds";

var SESSION_KEY = "sessionKey";
var MEDIA_META_KEY = "media.meta";
var MEDIA_META_REQUEST_PARAMS_KEY = "media.meta.requestParams";

var PURCHASE_FLOW_KEY = "flow.purchase";
var PURCHASE_PRODUCT_ID_KEY = "purchase.productId";

var EMAIL_LINKED_KEY = "email.linked";
var AUTHORIZED_KEY = "authorized";

var OFFER_URI_AFTER_AUTH_KEY = "offer.uriToLoadAfterAuth";
var OFFER_PURCHASE_FUNCTION_KEY = "offer.purchaseFunction";
var OFFER_SWAP_PAGE_KEY = "offer.swapPage";

var TARGET_PAGEID_KEY = "targetPage";
var PURCHASE_FLOW_KEY = "flow.purchase";
var OFFER_PAGE_ID = "com.bamnetworks.appletv.offer";

var TODAYS_GAMES_REFRESH_URL_KEY = "todaysGames.refreshUrl";
var GAMES_PAGE_REFRESH_URL_KEY = "games.refreshUrl";

// atv.onPageLoad
atv.onPageLoad = function(id)
{
	console.log("main.js atv.onPageLoad() id: " + id);

	// <mlbtv>
	var headElem = document.rootElement.getElementByName("head");
	if (headElem && headElem.getElementByName("mlbtv"))
	{
		var mlbtvElem = headElem.getElementByName("mlbtv");
		console.log("mlbtv exists");

		// store local variables
		storeLocalVars(mlbtvElem);

		// ping urls
		pingUrls(mlbtvElem);
	}

	// games by team
	if (id && id.indexOf("com.bamnetworks.appletv.gamesByTeam.") >= 0)
	{
		applyGamesPage(document);
	}

	if (id && id.indexOf("com.bamnetworks.appletv.media") >= 0 && typeof (flowManager) != "undefined")
	{
		flowManager.endPurchaseFlow();
		flowManager.endMediaFlow();
		console.debug("main.js onPageLoad() atv.localStorage[PURCHASE_FLOW_KEY] " + atv.localStorage[PURCHASE_FLOW_KEY]);
	}

	atv._debugDumpControllerStack();
}

// atv.onPageUnload
atv.onPageUnload = function(id)
{
	console.debug("main.js atv.onPageUnload() id " + id);
	console.debug("main.js atv.onPageUnload() atv.localStorage[PURCHASE_FLOW_KEY] " + atv.localStorage[PURCHASE_FLOW_KEY]);
	if (flowManager.inPurchaseFlow() && ("com.bamnetworks.appletv.authenticate" == id || id.indexOf("Not Authorized") > -1))
	{
		console.debug("main.js atv.onPageUnload() showing offer page");
		flowManager.setTargetPage(OFFER_PAGE_ID);

		var newPage = new atv.ProxyDocument();
		newPage.show();
		serviceClient.loadOfferPage(newPage);

		console.debug("main.js atv.onPageUnload() showing offer page");
	}
}

function storeLocalVars(mlbtvElem)
{
	// check local storage params
	var localStorageElems = mlbtvElem.getElementsByName("localStorage");
	console.debug("localStorageElems=" + localStorageElems);
	if (localStorageElems && localStorageElems.length > 0)
	{
		var index = 0;
		for (; index < localStorageElems.length; index++)
		{
			var key = localStorageElems[index].getAttribute("key");
			var value = localStorageElems[index].getAttribute("value");
			if (value)
			{
				atv.localStorage[key] = value;
			}
			else
			{
				atv.localStorage.removeItem(key);
			}
			console.log("localStorage[" + key + "] = " + atv.localStorage[key]);
		}
	}
}

function pingUrls(mlbtvElem)
{
	// check ping urls
	var pingElems = mlbtvElem.getElementsByName("ping");
	console.log("pingElems=" + pingElems);
	if (pingElems && pingElems.length > 0)
	{
		for ( var pingIndex = 0; pingIndex < pingElems.length; pingIndex++)
		{
			var url = pingElems[pingIndex].getAttribute('url');
			url = (url == null || url.trim().length() <= 0) ? pingElems[pingIndex].textContent : url;
			console.debug("url=" + url);
			if (url)
			{
				try
				{
					console.log('pinging reporting url ' + url);
					var req = new XMLHttpRequest();
					req.open("GET", url, true);
					req.send();
				}
				catch (e)
				{
					console.error("failed to ping the url " + url + " error " + e);
				}
			}
		}
	}
}

/**
 * Updates the logo according to the screen resolution
 * 
 * @param document
 * @returns
 */
function updateImageResolution(document)
{
	// update logos according to screen resolution
	var frame = atv.device.screenFrame;
	console.log("frame size: " + frame.height);
	if (frame.height == 1080)
	{
		var images = document.rootElement.getElementsByTagName('image');
		for ( var i = 0; i < images.length; i++)
		{
			images[i].textContent = images[i].textContent.replace('720p', '1080p');
		}
	}

	return document;
}

// ------------------------------ load and apply -------------------------------
function applyMain(document)
{
	console.log('applyMain ' + document);

	// end all flows when you're on the main page
	flowManager.endMediaFlow();
	flowManager.endPurchaseFlow();

	// check valid subscription
	// (1) via iTunes on this box and iTunes account signed in. Receipt is present and stored on the box
	// (2) via Vendor and is signed in with Vendor account.
	var validSubscription = (atv.localStorage[IOS_IDENTITY_POINT_KEY] != null && atv.localStorage[IOS_FINGERPRINT_KEY] != null) || (atv.localStorage[AUTHORIZED_KEY] == "true" && atv.localStorage[EMAIL_IDENTITY_POINT_KEY] != null && atv.localStorage[EMAIL_FINGERPRINT_KEY] != null);

	console.log("applyMain() validSubscription " + validSubscription);
	console.log("applyMain() atv.localStorage[AUTHORIZED_KEY] " + atv.localStorage[AUTHORIZED_KEY]);

	var subscribeMenu = document.getElementById("subscribeMlbTv");
	if (validSubscription)
	{
		if (subscribeMenu)
		{
			subscribeMenu.removeFromParent();
			console.debug("applyMain() removing the Subscribe To MLB.TV");
		}
	}
	else
	{
		// add the menu if it's not there
		if (!subscribeMenu)
		{
			subscribeMenu = atv.parseXML('<oneLineMenuItem id="subscribeMlbTv" accessibilityLabel="Subscribe To MLB.TV" onSelect="subscribeToMlbTv()"><label>Subscribe To MLB.TV</label></oneLineMenuItem>').rootElement;
			subscribeMenu.removeFromParent();

			var parent = document.rootElement.getElementByTagName("items");
			console.debug("applyMain() parent " + parent);
			if (parent && parent.childElements)
			{
				console.debug("applyMain() before parent.childElements " + parent.childElements);
				var settingsMenu = document.getElementById("settings");
				settingsMenu.removeFromParent();
				parent.appendChild(subscribeMenu);
				parent.appendChild(settingsMenu);
				console.debug("applyMain() after parent.childElements " + parent.childElements);
			}
		}
	}

	return document;
}

function handleMainVoltileReload()
{
	console.debug("handleMainVoltileReload() atv.localStorage[TARGET_PAGEID_KEY] " + atv.localStorage[TARGET_PAGEID_KEY]);
	if (atv.localStorage[TARGET_PAGEID_KEY] == null || atv.localStorage[TARGET_PAGEID_KEY] == "com.bamnetworks.appletv.main")
	{
		applyMain(document);
		atv.loadAndSwapXML(document);
	}
}

function handleOfferVoltileReload(document)
{
	console.debug("handleOfferVoltileReload() atv.localStorage[AUTHORIZED_KEY] " + atv.localStorage[AUTHORIZED_KEY]);
	if (atv.localStorage[AUTHORIZED_KEY] == "true")
	{
		console.debug("handleOfferVoltileReload() unloading the page because already authorized");
		atv.unloadPage();
	}
	else
	{
		applyOffer(document);
		atv.loadAndSwapXML(document);
	}
}

function applyTodaysGames(responseXML, refresh)
{
	console.log("applyTodaysGames() responseXML: " + responseXML);

	try
	{
		// elements
		var listByNavigation = responseXML.rootElement.getElementByTagName("listByNavigation");
		var imageMenuItems = responseXML.rootElement.getElementsByTagName("imageTextImageMenuItem");
		console.log("applyTodaysGames() navigation=" + navigation);

		// add behavior
		if (atv.localStorage[TODAYS_GAMES_REFRESH_URL_KEY] == null || !refresh)
		{
			var navigation = responseXML.rootElement.getElementByTagName("navigation");
			var currentIndex = navigation.getAttribute("currentIndex");
			console.debug("applyTodaysGames() currentIndex " + currentIndex);
			var refreshUrl = navigation.childElements[currentIndex].getElementByTagName("url").textContent;
			console.debug("applyTodaysGames() refreshUrl " + refreshUrl);
			atv.localStorage[TODAYS_GAMES_REFRESH_URL_KEY] = refreshUrl;
		}
		listByNavigation.setAttribute("refreshInterval", "60");
		listByNavigation.setAttribute("onRefresh", "console.log('refreshing todays games');handleTodaysGamesRefresh();");
		listByNavigation.setAttribute("onNavigate", "console.log('navigating todays games');handleTodaysGamesNavigate(event);");

		// sort
		var favTeamIds = getFavoriteTeamIds();
		function sort(menuOne, menuTwo)
		{
			var menuOneTeamIds = menuOne.getAttribute("id").split("-");
			var menuTwoTeamIds = menuTwo.getAttribute("id").split("-");

			var menuOneOrder = (favTeamIds.indexOf(menuOneTeamIds[0]) >= 0 || favTeamIds.indexOf(menuOneTeamIds[1]) >= 0) ? 1 : 0;
			var menuTwoOrder = (favTeamIds.indexOf(menuTwoTeamIds[0]) >= 0 || favTeamIds.indexOf(menuTwoTeamIds[1]) >= 0) ? 1 : 0;

			// console.log("favTeamIds.indexOf(menuOneTeamIds[0])=" + favTeamIds.indexOf(menuOneTeamIds[0]) + " favTeamIds.indexOf(menuOneTeamIds[1])=" + favTeamIds.indexOf(menuOneTeamIds[1]));
			// console.log(menuOneOrder + "-" + menuTwoOrder);

			return menuTwoOrder - menuOneOrder;
		}
		var orderedMenuList = imageMenuItems.sort(sort);

		// 1. re-append the sorted children
		// 2. set the favorite team logos
		// 3. hide scores on the page
		// 4. set the link to no scores preview
		var index = 0;
		for (; index < orderedMenuList.length; index++)
		{
			// 1. reappend child for sorting
			var menu = orderedMenuList[index];
			var parent = menu.parent;
			menu.removeFromParent();
			parent.appendChild(menu);

			// 2. set the favorite team logos
			console.debug("applyTodaysGames() menu item id " + menu.getAttribute("id"));
			var teamIds = menu.getAttribute("id").split("-");
			var isAwayFav = favTeamIds.indexOf(teamIds[0]) >= 0;
			var isHomeFav = favTeamIds.indexOf(teamIds[1]) >= 0;
			console.debug("applyTodaysGames() isAwayFav " + isAwayFav);
			console.debug("applyTodaysGames() isHomeFav " + isHomeFav);
			var leftImageElem = menu.getElementByName("leftImage");
			if (isAwayFav && leftImageElem.textContent.indexOf("-fav.png") < 0)
			{
				leftImageElem.textContent = leftImageElem.textContent.replace(".png", "-fav.png");
			}
			var rightImageElem = menu.getElementByName("rightImage");
			if (isHomeFav && rightImageElem.textContent.indexOf("-fav.png") < 0)
			{
				rightImageElem.textContent = rightImageElem.textContent.replace(".png", "-fav.png");
			}

			// 3. Hide scores
			// 4. Use the preview link with no scores
			var showScores = (atv.localStorage[SHOW_SCORES_KEY]) ? atv.localStorage[SHOW_SCORES_KEY] : "true";
			var rightLabelElem = menu.getElementByName("rightLabel");
			if (showScores == "false")
			{
				var previewLinkElem = menu.getElementByTagName("link");
				if (rightLabelElem && rightLabelElem.textContent.indexOf("Final") >= 0)
				{
					rightLabelElem.textContent = "Final";
				}
				if (previewLinkElem)
				{
					previewLinkElem.textContent = previewLinkElem.textContent.replace(".xml", "_noscores.xml");
				}
			}

			// 5. Check for double header 3:33 AM ET
			if (rightLabelElem && rightLabelElem.textContent.indexOf("3:33 AM") >= 0)
			{
				rightLabelElem.textContent = "Game 2";
			}
		}

		return responseXML;
	}
	catch (error)
	{
		console.error("unable to apply todays games " + error);
		atvutils.loadError("An error occurred loading today's games", "An error occurred loading today's games");
	}
}

/**
 * Should be called by the onRefresh event, I am also calling this from the handleNavigate function below to share code. Essentially this function: 1. Loads the current Navigation file 2. Pulls the
 * menu items out of the loaded file and dumps them into the current file 3. Checks to see if an onRefresh AJAX call is already being made and if so does nothing.
 * 
 * 
 * @params string url - url to be loaded
 */
function handleTodaysGamesRefresh()
{
	var url = atv.localStorage[TODAYS_GAMES_REFRESH_URL_KEY];
	console.log("handleTodaysGamesRefresh() url " + url);

	console.log("We are about to check the value of Ajax.currentlyRefreshing: --> " + Ajax.currentlyRefreshing);
	// check the Ajax object and see if a refresh event is happening
	if (!Ajax.currentlyRefreshing)
	{
		// if a refresh event is not happening, make another one.
		console.log("handleTodaysGamesRefresh()About to make the AJAX call. --> ")

		var refresh = new Ajax({
			"url" : url,
			"refresh" : true,
			"success" : function()
			{
				var req = arguments[0];

				console.log("handleTodaysGamesRefresh() Ajax url " + url);

				var newDocument = atv.parseXML(req.responseText); // Instead of using the responseXML you should parse the responseText. I know it seems redundent but trust me.

				// update the content page
				var newMenuSections = newDocument.rootElement.getElementByTagName('sections');
				applyTodaysGames(newDocument, true);
				newMenuSections.removeFromParent();

				var menu = document.rootElement.getElementByTagName('menu');
				console.log("handleTodaysGamesRefresh() menu=" + menu);
				if (menu)
				{
					var oldMenuSections = menu.getElementByTagName('sections');
					menu.replaceChild(oldMenuSections, newMenuSections);
				}
				else
				{
					menu = atv.parseXML("<menu></menu>").rootElement;
					menu.removeFromParent();
					menu.appendChild(newMenuSections);

					var listByNavigation = document.rootElement.getElementByTagName("listByNavigation");
					listByNavigation.appendChild(menu);
				}
				
				// replace # Games
				var newTumbler = newDocument.rootElement.getElementByTagName('tumblerWithSubtitle');
				newTumbler.removeFromParent();
				var header = document.rootElement.getElementByTagName('header');
				var oldTumbler = document.rootElement.getElementByTagName('tumblerWithSubtitle');
				header.replaceChild(oldTumbler, newTumbler);
			}
		});
		console.log("handleTodaysGamesRefresh() I have made the AJAX call. <--");
	}
	console.log("handleTodaysGamesRefresh() I am on the other side of the if. <--");
}

/**
 * This function works very similar to the handleRefresh Essentially we are manually loading the data and updating the page in Javascript This means we have to handle a bit more of the magic, but we
 * also get to control what is updated and update more of the page, like headers, previews, etc.
 * 
 * In this example, I am: 1. Getting the new URL from the Navigation Item 2. Changing the onRefresh URL 3. Calling handleRefresh and passing the new url to let handleRefresh do the magic.
 */
function handleTodaysGamesNavigate(e)
{
	console.log("handleTodaysGamesNavigate() " + e + ", e.navigationItemId = " + e.navigationItemId + " document = " + document);
	console.log("e=" + e);
	atvutils.printObject(e);
	console.log("document.getElementById(e.navigationItemId)=" + document.getElementById(e.navigationItemId));
	console.log("document.getElementById(e.navigationItemId).getElementByTagName=" + document.getElementById(e.navigationItemId).getElementByTagName);

	// get the url
	var url = document.getElementById(e.navigationItemId).getElementByTagName('url').textContent;
	atv.localStorage[TODAYS_GAMES_REFRESH_URL_KEY] = url;

	handleTodaysGamesRefresh();

	e.onCancel = function()
	{
		// Cancel any outstanding requests related to the navigation event.
		console.log("Navigation got onCancel.");
	}
}

function applyFeedSelectionPage(document)
{
	console.log("applyFeedSelectionPage() document: " + document);

	// add behavior by adding javascript file reference
	var head = document.rootElement.getElementByTagName("head");
	if (head)
	{
		var serviceJs = atv.parseXML('<script src="' + config.appletvBase + '/js/service.js" />').rootElement;
		serviceJs.removeFromParent();
		console.debug("adding service.js " + serviceJs);

		var children = head.childElements;
		children.unshift(serviceJs);

		for ( var i = 0; i < children.length; i++)
		{
			children[i].removeFromParent();
			head.appendChild(children[i]);
		}
	}

	flowManager.endMediaFlow();
	flowManager.endPurchaseFlow();
	console.log("purchase and media flow ended " + atv.localStorage[PURCHASE_PRODUCT_ID_KEY] + " " + atv.localStorage[MEDIA_META_REQUEST_PARAMS_KEY] + " " + atv.localStorage[MEDIA_META_KEY] + " " + atv.localStorage[SESSION_KEY]);

	return document;
}

// This is called from the feed_selection.xml
function loadFeed(mediaMeta)
{
	atv._debugDumpControllerStack();

	console.debug("showing new page..");
	var newPage = new atv.ProxyDocument();
	newPage.show();

	atv._debugDumpControllerStack();

	// store the media meta into the local storage for later use
	serviceClient.startMediaFlow(mediaMeta);
	atv.localStorage[OFFER_URI_AFTER_AUTH_KEY] = config.appletvBase + "/views/MediaServiceProxy?encode(MEDIA_META_REQUEST_PARAMS)";
	atv.localStorage[OFFER_PURCHASE_FUNCTION_KEY] = "restorePurchasePlay";
	atv.localStorage[OFFER_SWAP_PAGE_KEY] = "true";

	// load the media page
	serviceClient.loadMediaPage(function(document)
	{
		// media proxy returns a playback page, display
		var authorized = !serviceClient.isMediaResponseUnauthorized(document);
		// atv.localStorage.setItem(AUTHORIZED_KEY, (authorized) ? "true" : "false");
		if (authorized)
		{
			// if authorized, load the page
			console.debug("loadFeed() showing the media page");
			newPage.swapXML(document);
		}
		else
		{
			// if unauthorized, load the offer page
			console.debug("loadFeed() showing the offer page swap " + atv.localStorage[OFFER_SWAP_PAGE_KEY]);
			serviceClient.loadOfferPage(newPage);
		}
	});
}
// deprecated function name but it's still being generated by the gameday mule
var loadLiveGameVideoAsset = loadFeed;

function loadOfferPageAndPlay()
{
	var newPage = new atv.ProxyDocument();
	newPage.show();
	serviceClient.loadOfferPage(newPage);
}

function subscribeToMlbTv()
{
	console.debug("subscribeToMlbTv() enter");

	flowManager.endMediaFlow();

	// create new page where offer page will be loaded onto
	var newPage = new atv.ProxyDocument();
	newPage.show();

	// load the offer page onto the new page
	atv.localStorage[OFFER_URI_AFTER_AUTH_KEY] = config.appletvBase + "/views/Dialog?messageCode=AUTH_MESSAGE_CODE";
	atv.localStorage[OFFER_PURCHASE_FUNCTION_KEY] = "restoreAndPurchase";
	atv.localStorage[OFFER_SWAP_PAGE_KEY] = "false";
	serviceClient.loadOfferPage(newPage);
}

function ErrorToDebugString(error)
{
	var localizedDescription = error.userInfo["NSLocalizedDescription"];
	localizedDescription = localizedDescription ? localizedDescription : "Unknown";
	return error.domain + "(" + error.code + "): " + localizedDescription;
}

function linkManually()
{
	atv.loadURL(config.appletvBase + "/views/Login?urlToLoadAfterAuth=" + config.appletvBase + "/views/LinkProxy?request=encode(LINK_PROXY_REQUEST)");
}

function purchase(newPage)
{
	var observer = serviceClient.addPurchaseObserver(function(purchasedTransaction)
	{
		// PURCHASE success
		console.debug("purchase(): success purchased transactions " + purchasedTransaction);

		// link()
		serviceClient.doLink([ purchasedTransaction ], function()
		{
			console.debug("purchase(): link success");
			serviceClient.loadUserAuthorized(function()
			{
				console.debug("purchase(): load user authorized");
				newPage.loadXML(atvutils.makeOkDialog("MLB.TV Purchased", "You have successfully purchased MLB.TV."));
				atv.unloadPage();
			});
		}, function()
		{
			console.debug("purchase(): link error after purchase");
			newPage.cancel();
			atv.unloadPage();
			// do nothing -- go back to previous page
			// atv.loadAndSwapXML(atvutils.makeOkDialog("MLB.TV Error", "Failed to determine your MLB.TV subscription after purchase"));
		})
	}, function()
	{
		// PURCHASE error
		console.debug("purchase(): error ");
		newPage.cancel();
		atv.unloadPage();
	});

	newPage.onCancel = function()
	{
		atv.SKDefaultPaymentQueue.removeTransactionObserver(observer);
	};

	serviceClient.startPurchase();

}

function restoreAndPurchase(productId)
{
	atv._debugDumpControllerStack();

	// store product id into the cart
	atv.localStorage[PURCHASE_PRODUCT_ID_KEY] = productId;

	// create and show new page where dialogs will be loaded
	var newPage = new atv.ProxyDocument();
	newPage.show();

	atv._debugDumpControllerStack();

	var restoreObserver = serviceClient.addRestoreObserver(function(transactions)
	{
		// link()
		serviceClient.doLink(transactions, function()
		{
			// LINK success -- determine authorization thru feature service
			atv._debugDumpControllerStack();
			serviceClient.loadUserAuthorized(function()
			{
				console.debug("restoreAndPurchase() atv.localStorage[AUTHORIZED_KEY] " + atv.localStorage[AUTHORIZED_KEY]);
				if (atv.localStorage[AUTHORIZED_KEY] == "true")
				{
					console.debug("restoreAndPurchase() authorized or error page, showing Success page...");
					atv._debugDumpControllerStack();
					newPage.cancel();
					atv.loadAndSwapXML(atvutils.makeOkDialog("Success", "You are now authorized to watch MLB.TV.  Please select a game from Today's Games on the main menu."));
					console.debug("restoreAndPurchase() after showing Success page...");
					atv._debugDumpControllerStack();
				}
				else
				{
					// unauthorized, purchase
					console.debug("restoreAndPurchase() unauthorized, purchasing...");
					purchase(newPage);
					atv._debugDumpControllerStack();
				}
			});
		}, function()
		{
			// LINK error -- attempt to purchase now
			console.debug("restoreAndPurchase() link error - make the purchase ");
			purchase(newPage);
			atv._debugDumpControllerStack();
		});
	}, function(error)
	{
		newPage.cancel();
		if (error && error.code == -500)
		{
			atv.loadAndSwapXML(atvutils.makeOkDialog("MLB.TV Error", "Failed to determine your subscription."));
		}
		else
		{
			newPage.swapXML(document);
		}
		console.error("restoreAndPurchase() restore error - failed to restore " + document);
		atv._debugDumpControllerStack();
	});

	newPage.onCancel = function()
	{
		atv.SKDefaultPaymentQueue.removeTransactionObserver(restoreObserver);
	};

	// Restore completed transactions to get transactions purchased on other devices for this account.
	console.debug("restoreAndPurchase() Restoring completed transactions...");
	atv.SKDefaultPaymentQueue.restoreCompletedTransactions();
}

function purchaseAndPlay(newPage)
{
	atv._debugDumpControllerStack();

	var observer = serviceClient.addPurchaseObserver(function(purchasedTransaction)
	{
		// PURCHASE success
		console.debug("purchase(): success purchased transactions " + purchasedTransaction);
		atv._debugDumpControllerStack();

		// link()
		serviceClient.doLink([ purchasedTransaction ], function()
		{
			console.debug("purchase(): link success");
			atv._debugDumpControllerStack();

			serviceClient.loadMediaPage(function(mediaDoc)
			{
				console.debug("purchase() loadMediaPage");
				newPage.swapXML(mediaDoc);
				atv._debugDumpControllerStack();
			});

		}, function()
		{
			console.debug("purchase(): link error after purchase");
			newPage.swapXML(atvutils.makeOkDialog("MLB.TV Link Error", "Failed to link your MLB.TV account properly after purchase"));
			atv._debugDumpControllerStack();
		})

	}, function(transaction)
	{
		// PURCHASE error
		console.debug("purchase(): error " + (transaction) ? transaction.error : null);
		if (!transaction || transaction.error != 2)
		{
			newPage.swapXML(atvutils.makeOkDialog("MLB.TV Purchase Error", "The purchase failed.  Please try again at a later time."));
		}
		atv._debugDumpControllerStack();
	});

	newPage.onCancel = function()
	{
		atv.SKDefaultPaymentQueue.removeTransactionObserver(observer);
	};

	serviceClient.startPurchase();
}

function restorePurchasePlay(productId)
{
	// store product id into the cart
	atv.localStorage[PURCHASE_PRODUCT_ID_KEY] = productId;

	var newPage = new atv.ProxyDocument();
	newPage.show();

	atv._debugDumpControllerStack();

	var restoreObserver = serviceClient.addRestoreObserver(function(transactions)
	{
		// link()
		serviceClient.doLink(transactions, function()
		{
			// LINK success -- determine whether to purchase
			serviceClient.loadMediaPage(function(document)
			{
				atv._debugDumpControllerStack();
				var authorized = !serviceClient.isMediaResponseUnauthorized(document);
				console.debug("restorePurchasePlay() authorized " + authorized);
				atv.localStorage.setItem(AUTHORIZED_KEY, (authorized) ? "true" : "false");
				console.debug("restorePurchasePlay() atv.localStorage[AUTHORIZED] " + atv.localStorage[AUTHORIZED_KEY]);
				if (authorized)
				{
					// authorized or media playback error (session key problem, media unavailable, etc.), display the page
					console.debug("restorePurchasePlay() authorized or error page, showing media proxy page...");
					newPage.swapXML(atvutils.makeOkDialog("MLB.TV Subscriber", "You have purchased this content before and already have access to watch MLB.TV content.", "play();"));
					atv._debugDumpControllerStack();
					console.debug("restorePurchasePlay() after showing media proxy page...");
				}
				else
				{
					// unauthorized, purchase
					console.debug("restorePurchasePlay() unauthorized, purchasing...");
					purchaseAndPlay(newPage);
					atv._debugDumpControllerStack();
				}
			});
		}, function()
		{
			// LINK error -- attempt to purchase now
			console.debug("restorePurchasePlay() link error - make the purchase ");
			purchaseAndPlay(newPage);
		});
	}, function(error)
	{
		if (error && error.code == -500)
		{
			newPage.swapXML(atvutils.makeOkDialog("MLB.TV Error", "Failed to determine your subscription."));
		}
		else
		{
			newPage.swapXML(document);
		}
	});

	newPage.onCancel = function()
	{
		atv.SKDefaultPaymentQueue.removeTransactionObserver(restoreObserver);
	};

	// Restore completed transactions to get transactions purchased on other devices for this account.
	console.debug("restorePurchasePlay() Restoring completed transactions...");
	atv.SKDefaultPaymentQueue.restoreCompletedTransactions();
}

function play(newPage)
{
	serviceClient.loadMediaPage(function(document)
	{
		console.debug("play() show the media proxy page..." + newPage);
		atv._debugDumpControllerStack();
		if (newPage)
		{
			newPage.swapXML(document);
			atv.unloadPage();
		}
		else
		{
			atv.loadAndSwapXML(document);
		}
		atv._debugDumpControllerStack();
		console.debug("play() after showing media proxy page...");
	});
}

function restoreManually()
{
	console.debug("restore receipts from iTunes account manually");

	var newPage = new atv.ProxyDocument();
	newPage.show();

	var restoreObserver = serviceClient.addRestoreObserver(function(transactions)
	{
		// restore transaction callback
		console.log("restoreManually() transactions " + transactions);
		if (transactions)
		{
			console.debug("restoreManually() successfully restored receipts from iTunes account manually");
			serviceClient.doLink(transactions, function()
			{
				console.debug("restoreManually() link success callback - determine authorization from feature service");
				serviceClient.loadUserAuthorized(function()
				{
					newPage.loadXML(atvutils.makeOkDialog("Restore Successful", "Your subscription has been restored."));
				});
				console.debug("restoreManually() link service success");
			}, function()
			{
				console.debug("restoreManually() link service error");
			});
		}
		else
		{
			console.debug("restoreManually() missing transaction - determine authorization from feature service");
			newPage.loadXML(atvutils.makeOkDialog("Not an MLB.TV subscriber", "You have not purchased a MLB.TV subscription on AppleTV."));
		}
	}, function(error)
	{
		// restore error callback
		console.debug("restoreManually() error attempting to restore receipts from iTunes account manually");

		if (!error || error.code != -500)
		{
			newPage.cancel();
		}
		else
		{
			newPage.loadXML(atvutils.makeOkDialog("Restore Unsuccessful", "An error occurred attempting to restore your MLB.TV account."));
		}
	});

	newPage.onCancel = function()
	{
		atv.SKDefaultPaymentQueue.removeTransactionObserver(restoreObserver);
	};

	// Restore completed transactions to get transactions purchased on other devices for this account.
	console.debug("restoreManually() Restoring completed transactions...");
	atv.SKDefaultPaymentQueue.restoreCompletedTransactions();
}

function restoreOnOffer()
{
	console.debug("restoreOnOffer() restore receipts from iTunes account manually");

	var newPage = new atv.ProxyDocument();
	newPage.show();

	var restoreObserver = serviceClient.addRestoreObserver(function(transactions)
	{
		// restore success callback
		console.log("restoreManually() transaction " + transactions);
		if (transactions)
		{
			console.debug("successfully restored receipts from iTunes account manually");
			serviceClient.doLink(transactions, function()
			{
				// link success callback
				if (flowManager.inMediaFlow())
				{
					console.debug("restoreOnOffer() in media flow, playing media");
					play(newPage);
				}
				else
				{
					console.debug("restoreOnOffer() NOT in media flow, display info message");
					serviceClient.loadUserAuthorized(function()
					{
						console.debug("restoreOnOffer(): load user authorized");
						newPage.swapXML(atvutils.makeOkDialog("Restore Successful", "Your subscription has been restored."));
						atv.unloadPage();
					});
				}
			}, function()
			{
				// link error callback
				newPage.loadXML(atvutils.makeOkDialog("Restore Unsuccessful", "There was a problem restoring your iTunes account. Please try again at a later time."));
			});
		}
		else
		{
			newPage.loadXML(atvutils.makeOkDialog("Not an MLB.TV subscriber", "You have not purchased a MLB.TV subscription on AppleTV."));
		}
	}, function(error)
	{
		// restore error callback
		console.debug("error attempting to restore receipts from iTunes account manually");

		if (!error || error.code != -500)
		{
			newPage.cancel();
		}
		else
		{
			newPage.loadXML(atvutils.makeOkDialog("Restore Unsuccessful", "An error occurred attempting to restore your MLB.TV account."));
		}
	});

	newPage.onCancel = function()
	{
		atv.SKDefaultPaymentQueue.removeTransactionObserver(restoreObserver);
	};

	// Restore completed transactions to get transactions purchased on other devices for this account.
	console.debug("restoreManually() Restoring completed transactions...");
	atv.SKDefaultPaymentQueue.restoreCompletedTransactions();
}

function applySettingsPage(document)
{
	// update logo image resolution
	updateImageResolution(document);

	// show scores menu item
	var showScores = atv.localStorage[SHOW_SCORES_KEY];
	var menuItem = document.getElementById("showScoresMenuItem");
	if (menuItem)
	{
		var label = menuItem.getElementByName('rightLabel');
		label.textContent = (!showScores || showScores == "true") ? "Show" : "Hide";
		console.debug("label.textContent: " + label.textContent);
	}

	// menu items
	var parent = document.rootElement.getElementByTagName("items");
	var signInMenu = document.getElementById("signInOutMenuItem");
	var restoreMenu = document.getElementById("restoreSub");
	var linkAcctMenu = document.getElementById("linkAcct");
	var showScoresMenu = document.getElementById("showScoresMenuItem");
	console.debug("applySettingsPage() parent " + parent);

	// restore menu item
	var showRestoreMenu = atv.localStorage[IOS_IDENTITY_POINT_KEY] == null && atv.localStorage[IOS_FINGERPRINT_KEY] == null;
	console.debug("applySettingsPage() showRestoreMenu " + showRestoreMenu + " restoreMenu " + restoreMenu + " atv.localStorage[IOS_IDENTITY_POINT_KEY] " + atv.localStorage[IOS_IDENTITY_POINT_KEY]);
	if (showRestoreMenu)
	{
		if (!restoreMenu)
		{
			var parent = document.rootElement.getElementByTagName("items");
			console.debug("applySettingsPage() parent " + parent);
			if (parent && parent.childElements)
			{
				restoreMenu = atv.parseXML('<oneLineMenuItem id="restoreSub" accessibilityLabel="Restore Subscription" onSelect="restoreManually();"><label>Restore Subscription</label></oneLineMenuItem>').rootElement;
				console.debug("applySettingsPage() adding restoreMenu " + restoreMenu);
				restoreMenu.removeFromParent();
				parent.appendChild(restoreMenu);
				if (linkAcctMenu)
				{
					linkAcctMenu.removeFromParent();
					parent.appendChild(linkAcctMenu);
				}
				showScoresMenu.removeFromParent();
				parent.appendChild(showScoresMenu);
			}
		}
	}
	else if (restoreMenu)
	{
		restoreMenu.removeFromParent();
	}

	// link accounts menu item
	var showLinkMenu = atv.localStorage[EMAIL_LINKED_KEY] != "true" && atv.localStorage[IOS_IDENTITY_POINT_KEY] != null && atv.localStorage[IOS_FINGERPRINT_KEY] != null;
	console.debug("applySettingsPage() atv.localStorage[EMAIL_LINKED_KEY] " + atv.localStorage[EMAIL_LINKED_KEY] + " showLinkMenu " + showLinkMenu + " atv.localStorage[IOS_IDENTITY_POINT_KEY] " + atv.localStorage[IOS_IDENTITY_POINT_KEY] + " linkAcctMenu " + linkAcctMenu);
	if (showLinkMenu)
	{
		// add the menu if it's not there
		if (!linkAcctMenu)
		{
			if (parent && parent.childElements)
			{
				console.debug("applySettingsPage() before parent.childElements " + parent.childElements);

				linkAcctMenu = atv.parseXML('<oneLineMenuItem id="linkAcct" accessibilityLabel="Link MLB.TV Account" onSelect="linkManually();"><label>Link Account</label></oneLineMenuItem>').rootElement;
				linkAcctMenu.removeFromParent();

				showScoresMenu.removeFromParent();
				parent.appendChild(linkAcctMenu);
				parent.appendChild(showScoresMenu);

				console.debug("applySettingsPage() after parent.childElements " + parent.childElements);
			}
		}

		if (signInMenu)
		{
			signInMenu.removeFromParent();
		}
	}
	else
	{
		// remove the menu
		if (linkAcctMenu)
		{
			linkAcctMenu.removeFromParent();
		}

		if (!signInMenu)
		{
			signInMenu = atv.parseXML('\
				<signInSignOutMenuItem id=\"signInOutMenuItem\" accessibilityLabel=\"Sign out\" signOutExitsApp=\"false\">\
					<signInPageURL>' + config.appletvBase + '/views/Login</signInPageURL>\
					<confirmation>\
						<title>Are You Sure?</title>\
						<description>The account %@ will be logged out of the service.</description>\
					</confirmation>\
				</signInSignOutMenuItem>').rootElement;
			signInMenu.removeFromParent();

			parent.appendChild(signInMenu);
			if (showRestoreMenu && restoreMenu)
			{
				parent.appendChild(restoreMenu);
			}
			showScoresMenu.removeFromParent();
			parent.appendChild(showScoresMenu);
		}
	}

	return document;
}

function applyRecapsPage(responseXML)
{
	// update logo image resolution
	updateImageResolution(responseXML);

	return responseXML;
}

function applyTeamsPage(responseXML)
{
	try
	{
		// update logo image resolution
		updateImageResolution(responseXML);

		var favTeamIds = getFavoriteTeamIds();

		console.debug('sorting teams.. ' + responseXML.rootElement);
		var gridList = responseXML.rootElement.getElementByName("body").getElementByName("scroller").getElementByName("items").getElementsByName("grid");
		var gridIndex = 0;
		for (; gridIndex < gridList.length; gridIndex++)
		{
			// function to sort by title
			function sortByTitle(poster1, poster2)
			{
				var favTeamIds = (atv.localStorage[FAVORITE_TEAMS_KEY]) ? atv.localStorage[FAVORITE_TEAMS_KEY] : [];

				var team1Id = poster1.getAttribute("id");
				var is1Fav = favTeamIds.indexOf(team1Id) >= 0;
				var title1 = poster1.getElementByName("title").textContent;
				title1 = (is1Fav) ? title1.toUpperCase() : title1.toLowerCase();

				var team2Id = poster2.getAttribute("id");
				var is2Fav = favTeamIds.indexOf(team2Id) >= 0;
				var title2 = poster2.getElementByName("title").textContent;
				title2 = (is2Fav) ? title2.toUpperCase() : title2.toLowerCase();

				var result = (title1 > title2) ? 1 : -1;
				// console.debug('result: ' + title1 + ">" + title2 + " = " + result);

				return result;
			}

			// parent
			var parent = gridList[gridIndex].getElementByName("items");

			// sort the posters
			var orderedPosterList = parent.childElements.sort(sortByTitle);

			// re-append the child
			var index = 0;
			for (; index < orderedPosterList.length; index++)
			{
				var poster = orderedPosterList[index];
				var teamId = poster.getAttribute("id");
				poster.removeFromParent();
				parent.appendChild(poster);

				var isFav = favTeamIds.indexOf(teamId) >= 0;

				var imageElem = poster.getElementByName("image");
				if (isFav && imageElem.textContent.indexOf("-fav.png") < 0)
				{
					imageElem.textContent = imageElem.textContent.replace(".png", "-fav.png");
				}
				else if (!isFav && imageElem.textContent.indexOf("-fav.png") > 0)
				{
					imageElem.textContent = imageElem.textContent.replace("-fav.png", ".png");
				}
			}

		}

		console.debug('teams are sorted and favorite team logos in place');
		return responseXML;

	}
	catch (error)
	{
		console.debug("Unable to sort the teams: " + error);
	}
}

function applyGamesPage(responseXML)
{
	console.debug("applyeGamesPage() responseXML = " + responseXML);
	try
	{
		// determine if favorite team
		var listElem = responseXML.rootElement.getElementByName("body").getElementByName("listByNavigation");
		var pageId = listElem.getAttribute("id");
		var teamId = pageId.substring(pageId.lastIndexOf(".") + 1);
		var isFavorite = getFavoriteTeamIds().indexOf(teamId) > -1;
		console.debug("applyGamesPage() teamId: " + teamId + " isFavorite: " + isFavorite);

		var favButton = responseXML.getElementById("favoriteButton");
		var favoriteButtonLabel = favButton.getElementByName("label");
		console.debug("favoriteButtonLabel=" + favoriteButtonLabel);
		if (isFavorite)
		{
			var newLabel = "Remove from Favorites";
			favButton.setAttribute('accessibilityLabel', newLabel);
			favoriteButtonLabel.textContent = newLabel;
			console.debug("changing to Remove From Favorites favoriteButtonLabel.textContent=" + favoriteButtonLabel.textContent);
		}
		else
		{
			var newLabel = "Add to Favorites";
			favButton.setAttribute('accessibilityLabel', newLabel);
			favoriteButtonLabel.textContent = newLabel;
			console.debug("changing to Add to Favorites favoriteButtonLabel.textContent=" + favoriteButtonLabel.textContent);
		}

		return responseXML;
	}
	catch (error)
	{
		console.log("Unable to apply changes to the games page: " + error);
	}
}

/**
 * Should be called by the onRefresh event, I am also calling this from the handleNavigate function below to share code. Essentially this function: 1. Loads the current Navigation file 2. Pulls the
 * menu items out of the loaded file and dumps them into the current file 3. Checks to see if an onRefresh AJAX call is already being made and if so does nothing.
 * 
 * 
 * @params string url - url to be loaded
 */
function handleGamesPageRefresh(urlParam)
{
	var url = atv.localStorage[GAMES_PAGE_REFRESH_URL_KEY];
	if (url == null)
	{
		url = urlParam;
	}
	
	console.log("Handle refresh event called");

	console.log("We are about to check the value of Ajax.currentlyRefreshing: --> " + Ajax.currentlyRefreshing);
	// check the Ajax object and see if a refresh event is happening
	if (!Ajax.currentlyRefreshing)
	{

		// if a refresh event is not happening, make another one.
		console.log("About to make the AJAX call. --> ")

		var refresh = new Ajax({
			"url" : url,
			"refresh" : true,
			"success" : function()
			{
				var req = arguments[0];

				// update the content page
				var newDocument = atv.parseXML(req.responseText), // Instead of using the responseXML you should parse the responseText. I know it seems redundent but trust me.
				menu = document.rootElement.getElementByTagName('menu'), oldMenuSections = menu.getElementByTagName('sections'), newMenuSections = newDocument.rootElement.getElementByTagName('sections'), now = new Date();

				applyGamesPage(newDocument);

				newMenuSections.removeFromParent();
				menu.replaceChild(oldMenuSections, newMenuSections);

			}
		});

		console.debug("I have made the AJAX call. <--");
	}

	console.debug("I am on the other side of the if. <--");

}

/**
 * This function works very similar to the handleRefresh Essentially we are manually loading the data and updating the page in Javascript This means we have to handle a bit more of the magic, but we
 * also get to control what is updated and update more of the page, like headers, previews, etc.
 * 
 * In this example, I am: 1. Getting the new URL from the Navigation Item 2. Changing the onRefresh URL 3. Calling handleRefresh and passing the new url to let handleRefresh do the magic.
 */
function handleGamesPageNavigate(e)
{
	console.log("Handle navigate event called: " + e + ", e.navigationItemId = " + e.navigationItemId + " document = " + document);

	// get the url
	var url = document.getElementById(e.navigationItemId).getElementByTagName('url').textContent;
	atv.localStorage[GAMES_PAGE_REFRESH_URL_KEY] = url;

	handleGamesPageRefresh(url);

	e.onCancel = function()
	{
		// Cancel any outstanding requests related to the navigation event.
		console.log("Navigation got onCancel.");
	}
}

function getFavoriteTeamIds()
{
	var favTeamIds = atv.localStorage[FAVORITE_TEAMS_KEY];
	console.debug("favoriteTeamIds=" + favTeamIds);
	return (favTeamIds != null) ? favTeamIds : [];
}

function toggleFavoriteTeam(teamId)
{
	try
	{
		console.log("toggleFavoriteTeams " + teamId);
		var favoriteTeamIds = getFavoriteTeamIds();

		var isFavorite;
		if (favoriteTeamIds == null || favoriteTeamIds.length == 0)
		{
			// first favorite team
			favoriteTeamIds = [ teamId ];
			isFavorite = true;
		}
		else
		{
			var teamIdIndex = favoriteTeamIds.indexOf(teamId);
			if (teamIdIndex > -1)
			{
				// remove
				favoriteTeamIds.splice(teamIdIndex, 1);
				isFavorite = false;
			}
			else
			{
				// add
				favoriteTeamIds.push(teamId);
				isFavorite = true;
			}
		}

		atv.localStorage[FAVORITE_TEAMS_KEY] = favoriteTeamIds;

		var favoriteButton = document.getElementById("favoriteButton");
		if (favoriteButton)
		{
			var newLabel = (isFavorite) ? "Remove from Favorites" : "Add to Favorites";
			var label = favoriteButton.getElementByName("label");
			label.textContent = newLabel;
			console.debug("after label.textContent: " + label.textContent);
		}
	}
	catch (error)
	{
		console.error("Caught exception trying to toggle DOM element: " + error);
	}

	console.debug("toggleFavoriteTeam() end " + teamId);
}

function toggleScores(id)
{
	try
	{
		var menuItem = document.getElementById(id);
		if (menuItem)
		{
			var label = menuItem.getElementByName('rightLabel');
			if (label.textContent == "Show")
			{
				label.textContent = "Hide";
				atv.localStorage[SHOW_SCORES_KEY] = "false";
			}
			else
			{
				label.textContent = "Show";
				atv.localStorage[SHOW_SCORES_KEY] = "true";
			}

			console.debug("showScores: " + atv.localStorage[SHOW_SCORES_KEY]);
		}
	}
	catch (error)
	{
		console.error("Caught exception trying to toggle DOM element: " + error);
	}
}
console.log("~~~~~ MLB.TV main.js end");
HTTP/1.1 200 OK
Server: Oracle-iPlanet-Web-Server/7.0
Date: Wed, 08 Aug 2012 16:14:47 GMT
Content-type: text/html
Content-type: application/json
Transfer-encoding: chunked

11c
var config = {env: '_proddc2split', imageBase: 'http://mlb.mlb.com/mlb/images/flagstaff', appletvBase: 'http://lws.mlb.com/appletvv2', bamServiceBase: 'https://mlb-ws.mlb.com', gamedayBase: 'http://gdx.mlb.com', mlbBase: 'http://www.mlb.com', secureMlbBase: 'https://secure.mlb.com'};
0

HTTP/1.1 200 OK
Server: Oracle-iPlanet-Web-Server/7.0
Date: Wed, 08 Aug 2012 16:14:48 GMT
Content-type: application/x-javascript
Last-modified: Mon, 23 Apr 2012 14:41:39 GMT
Content-length: 17889
Etag: "45e1-4f956a23"
Accept-ranges: bytes

// ***************************************************

var EMAIL_IDENTITY_POINT_KEY = "email.identityPointId";
var EMAIL_FINGERPRINT_KEY = "email.fingerprint";

// atv.Element extensions
atv.Element.prototype.getElementsByTagName = function(tagName) {
	return this.ownerDocument.evaluateXPath("descendant::" + tagName, this);
}

// Extend atv.ProxyDocument to load errors from a message and description.
atv.ProxyDocument.prototype.loadError = function(message, description) {
	var doc = atvutils.makeErrorDocument(message, description);
	this.loadXML(doc);
}

// Extend atv.ProxyDocument to load errors from a message and description.
atv.ProxyDocument.prototype.swapXML = function(xml) {
	var me = this;
    try 
    {
        me.loadXML(xml, function(success) {
            if ( success ) {
                atv.unloadPage();
            } else {
                console.log("swapXML failed to load " + xml);
                me.loadXML(atvutils.siteUnavailableError(), function(success) {
                	console.log("swapXML site unavailable error load success " + success);
                    if ( success ) {
                        atv.unloadPage();
                    }
                });
            }
        });
    } 
    catch (e) 
    {
        console.error("swapXML caught exception while loading " + xml + ". " + e);
        me.loadXML(atvutils.siteUnavailableError(), function(success) {
            if ( success ) {
                atv.unloadPage();
            }
        });
    }
}

// atv.Document extensions
atv.Document.prototype.getElementById = function(id) {
 var elements = this.evaluateXPath("//*[@id='" + id + "']", this);
 if ( elements && elements.length > 0 ) {
     return elements[0];
 }
 return undefined;
}

// atv.Element extensions
atv.Element.prototype.getElementsByTagName = function(tagName) {
 return this.ownerDocument.evaluateXPath("descendant::" + tagName, this);
}

atv.Element.prototype.getElementByTagName = function(tagName) {
 var elements = this.getElementsByTagName(tagName);
 if ( elements && elements.length > 0 ) {
     return elements[0];
 }
 return undefined;
}

// ATVUtils - a JavaScript helper library for Apple TV
ATVUtils = function() {};

ATVUtils.prototype.onGenerateRequest = function (request) {
	
    console.log('atvutils.js atv.onGenerateRequest() oldUrl: ' + request.url);

    console.debug("atvutils.js atv.onGenerateRequest() atv.localStorage[mediaMetaRequestParams] " + atv.localStorage[MEDIA_META_REQUEST_PARAMS_KEY]);
	if ( request.url.indexOf("encode(MEDIA_META_REQUEST_PARAMS)") > -1 && atv.localStorage[MEDIA_META_REQUEST_PARAMS_KEY])
	{
		request.url = request.url.replace("encode(MEDIA_META_REQUEST_PARAMS)", encodeURIComponent(atv.localStorage[MEDIA_META_REQUEST_PARAMS_KEY]));
	}
	console.debug("atvutils.js atv.onGenerateRequest() atv.localStorage[mediaMetaRequestParams] " + atv.localStorage[MEDIA_META_REQUEST_PARAMS_KEY]);
	if ( request.url.indexOf("MEDIA_META_REQUEST_PARAMS") > -1 && atv.localStorage[MEDIA_META_REQUEST_PARAMS_KEY])
	{
		request.url = request.url.replace("MEDIA_META_REQUEST_PARAMS", atv.localStorage[MEDIA_META_REQUEST_PARAMS_KEY]);
	}
	if ( request.url.indexOf("IPID") > -1 && atv.localStorage[BEST_IDENTITY_POINT_KEY])
	{
		request.url = request.url.replace("IPID", atv.localStorage[BEST_IDENTITY_POINT_KEY]);
	}
	if ( request.url.indexOf("FINGERPRINT") > -1 && atv.localStorage[BEST_FINGERPRINT_KEY])
	{
		request.url = request.url.replace("FINGERPRINT", encodeURIComponent(atv.localStorage[BEST_FINGERPRINT_KEY]));
	}
	if ( request.url.indexOf("SESSION_KEY") > -1 )
	{
		var sessionKey = atv.localStorage[SESSION_KEY]; 
		console.debug("atvutils.js atv.onGenerateRequest sessionKey=" + sessionKey);
		request.url = request.url.replace("SESSION_KEY", (sessionKey) ? encodeURIComponent(sessionKey) : "");
	}
	if ( request.url.indexOf("encode(LINK_PROXY_REQUEST)") > -1 && atv.localStorage[EMAIL_IDENTITY_POINT_KEY] != null && atv.localStorage[EMAIL_FINGERPRINT_KEY] != null)
	{
		var linkRequest = serviceClient.createLinkRequest();
		console.debug("atvutils.js atv.onGenerateRequest() linkRequest " + linkRequest);
		if (linkRequest != null)
		{
			// WARNING: encode twice because the xs:uri type does not suppot curly brackets so encode an extra time. The LinkProxy class will be responsible for url unencoding the json 
			request.url = request.url.replace("encode(LINK_PROXY_REQUEST)", encodeURIComponent(encodeURIComponent(JSON.stringify(linkRequest))));
		}
	}
	if ( request.url.indexOf("$$showScores") > -1 )
	{
		var showScores = atv.localStorage[SHOW_SCORES_KEY]; 
		console.log("showScores=" + showScores);
		request.url = request.url.replace("$$showScores", (showScores) ? showScores : "true");
	}
	var authorized = atv.localStorage[AUTHORIZED_KEY]; 
	if ( request.url.indexOf("AUTH_MESSAGE_CODE") > -1 && authorized != null)
	{
		console.debug("atvutils.js AUTH_MESSAGE_CODE atv.onGenerateRequest() authorized " + authorized);
//		request.url = request.url.replace("AUTH_MESSAGE_CODE", ((authorized == "true") ? "ALREADY_PURCHASED" : "NOT_AUHTORIZED"));
	}

	console.log('atvutils.js atv.onGenerateRequest() newUrl: ' + request.url);
}


ATVUtils.prototype.makeRequest = function(url, method, headers, body, callback) {
    if ( !url ) {
        throw "loadURL requires a url argument";
    }

	var method = method || "GET";
	var	headers = headers || {};
	var body = body || "";
	
    var xhr = new XMLHttpRequest();
    xhr.onreadystatechange = function() {
        try {
            if (xhr.readyState == 4 ) {
                if ( xhr.status == 200) {
                	console.log("makeRequest() getAllResponseHeaders(): " + xhr.getAllResponseHeaders());
                    callback(xhr.responseXML, xhr.responseText);
                } else {
                    console.log("makeRequest() received HTTP status " + xhr.status + " for " + url);
                    callback(xhr.responseXML, xhr.responseText);
                }
            }
        } catch (e) {
            console.error('makeRequest caught exception while processing request for ' + url + '. Aborting. Exception: ' + e);
            xhr.abort();
            callback(null, null);
        }
    }
    
    var request = {url: url};
    this.onGenerateRequest(request);
    
    console.debug("makeRequest() url " + request.url);

    xhr.open(method, request.url, true);

    for(var key in headers) {
        xhr.setRequestHeader(key, headers[key]);
    }

    console.debug("sending body in the send() method");
    xhr.send(body);
    return xhr;
}

ATVUtils.prototype.getRequest = function(url, callback) {
	return this.makeRequest(url, "GET", null, null, callback);
}

ATVUtils.prototype.postRequest = function(url, body, callback) {
	return this.makeRequest(url, "POST", null, body, callback);
}

ATVUtils.prototype.makeErrorDocument = function(message, description) {
    if ( !message ) {
        message = "";
    }
    if ( !description ) {
        description = "";
    }

    var errorXML = '<?xml version="1.0" encoding="UTF-8"?> \
    <atv> \
        <body> \
            <dialog id="com.bamnetworks.errorDialog"> \
                <title><![CDATA[' + message + ']]></title> \
                <description><![CDATA[' + description + ']]></description> \
            </dialog> \
        </body> \
    </atv>';

    return atv.parseXML(errorXML);
}

ATVUtils.prototype.makeOkDialog = function(message, description, onSelect) {
	if ( !message ) {
		message = "";
	}
	if ( !description ) {
		description = "";
	}
	
	if (!onSelect)
	{
		onSelect = "atv.unloadPage();";
	}
	
	var errorXML = '<?xml version="1.0" encoding="UTF-8"?> \
		<atv> \
		<head> \
			<script src="'+config.appletvBase+'/views/config.js" />\
			<script src="'+config.appletvBase+'/js/atvutils.js" />\
			<script src="'+config.appletvBase+'/js/service.js" />\
			<script src="'+config.appletvBase+'/js/main.js" />\
		</head>\
		<body> \
		<optionDialog id="com.bamnetowrks.optionDialog"> \
		<header><simpleHeader><title>' + message + '</title></simpleHeader></header> \
		<description><![CDATA[' + description + ']]></description> \
		<menu><sections><menuSection><items> \
			<oneLineMenuItem id="lineMenu" accessibilityLabel="OK" onSelect="' + onSelect + '">\
				<label>OK</label>\
			</oneLineMenuItem>\
		</items></menuSection></sections></menu>\
		</optionDialog> \
		</body> \
		</atv>';
	
	var doc = atv.parseXML(errorXML);
	return doc;
}

ATVUtils.prototype.printObject = function(object) {
	for (property in object)
	{
		console.log(property + ": " + object[property]);
	}
}


ATVUtils.prototype.siteUnavailableError = function() {
    // TODO: localize
	console.debug("siteUnavailableError()");
    return this.makeOkDialog("MLB.TV error", "MLB.TV is currently unavailable. Please try again later.");
}

ATVUtils.prototype.loadError = function(message, description) {
    atv.loadXML(this.makeErrorDocument(message, description));
}

ATVUtils.prototype.loadAndSwapError = function(message, description) {
    atv.loadAndSwapXML(this.makeErrorDocument(message, description));
}

ATVUtils.prototype.loadURLInternal = function(url, method, headers, body, loader) {
    var me = this;
    var xhr;
    var proxy = new atv.ProxyDocument;
    proxy.show();
    proxy.onCancel = function() {
        if ( xhr ) {
            xhr.abort();
        }
    };
    
    xhr = me.makeRequest(url, method, headers, body, function(xml, xhr) {
        try {
        	console.log("loadURLInternal() proxy=" + proxy + " xml=" + xml + " xhr=" + xhr);
            loader(proxy, xml, xhr);
        } catch(e) {
            console.error("Caught exception loading " + url + ". " + e);
            loader(proxy, me.siteUnavailableError(), xhr);
        }
    });
}

ATVUtils.prototype.loadGet = function(url, loader) {
	this.loadURLInternal(url, "GET", null, null, loader);
}

// TODO:: change to a url string or hash
ATVUtils.prototype.loadURL = function(url, method, headers, body, processXML) {
    var me = this;
    this.loadURLInternal(url, method, headers, body, function(proxy, xml) {
        if(typeof(processXML) == "function") processXML.call(this, xml);
		try {
            proxy.loadXML(xml, function(success) {
                if ( !success ) {
                    console.log("loadURL failed to load " + url);
                    proxy.loadXML(me.siteUnavailableError());
                }
            });
        } catch (e) {
            console.log("loadURL caught exception while loading " + url + ". " + e);
            proxy.loadXML(me.siteUnavailableError());
        }
    });
}

ATVUtils.prototype.swapXML = function(proxy, xml) {
	var me = this;
    try {
        proxy.loadXML(xml, function(success) {
            if ( success ) {
                atv.unloadPage();
            } else {
                console.log("swapXML failed to load " + xml);
                proxy.loadXML(me.siteUnavailableError(), function(success) {
                    if ( success ) {
                        atv.unloadPage();
                    }
                });
            }
        });
    } catch (e) {
        console.error("swapXML caught exception while loading " + xml + ". " + e);
        proxy.loadXML(me.siteUnavailableError(), function(success) {
            if ( success ) {
                atv.unloadPage();
            }
        });
    }
}

// TODO:: change to a url string or hash
// loadAndSwapURL can only be called from page-level JavaScript of the page that wants to be swapped out.
ATVUtils.prototype.loadAndSwapURL = function(url, method, headers, body, processXML) {
    var me = this;
    this.loadURLInternal(url, method, headers, body, function(proxy, xml) { 
	    if(typeof(processXML) == "function") processXML.call(this, xml);
        me.swapXML(proxy, xml);
    });
}

/** Load URL and apply changes to the xml */
ATVUtils.prototype.loadAndApplyURL = function(url, processXML) {
    var me = this;
	this.loadURLInternal(url, 'GET', null, null, function(proxy, xml, xhr) {
		var xmlToLoad = xml;
        if(typeof(processXML) == "function") xmlToLoad = processXML.call(this, xml);
        if (!xmlToLoad) xmlToLoad = xml;
		try {
            proxy.loadXML(xmlToLoad, function(success) {
                if ( !success ) {
                    console.log("loadURL failed to load " + url);
                    proxy.loadXML(me.siteUnavailableError());
                }
            });
        } catch (e) {
            console.log("loadURL caught exception while loading " + url + ". " + e);
            proxy.loadXML(me.siteUnavailableError());
        }
    });
}

var atvutils = new ATVUtils;

console.log("atvutils initialized " + atvutils);

/**
 * This is an AXR handler. It handles most of tediousness of the XHR request and keeps track of onRefresh XHR calls so that we don't end up with multiple page refresh calls.
 * 
 * You can see how I call it on the handleRefresh function below.
 * 
 * 
 * @params object (hash) $h
 * @params string $h.url - url to be loaded
 * @params string $h.method - "GET", "POST", "PUT", "DELTE"
 * @params bool $h.type - false = "Sync" or true = "Async" (You should always use true)
 * @params func $h.success - Gets called on readyState 4 & status 200
 * @params func $h.failure - Gets called on readyState 4 & status != 200
 * @params func $h.callback - Gets called after the success and failure on readyState 4
 * @params string $h.data - data to be sent to the server
 * @params bool $h.refresh - Is this a call from the onRefresh event.
 */
function Ajax($h)
{
	var me = this;
	$h = $h || {}

	// Setup properties
	this.url = $h.url || false;
	this.method = $h.method || "GET";
	this.type = ($h.type === false) ? false : true;
	this.success = $h.success || null;
	this.failure = $h.failure || null;
	this.data = $h.data || null;
	this.complete = $h.complete || null;
	this.refresh = $h.refresh || false;

	if (!this.url)
	{
		console.error('\nAjax Object requires a url to be passed in: e.g. { "url": "some string" }\n')
		return undefined;
	}

	this.createRequest();
	this.req.onreadystatechange = this.stateChange;
	this.req.object = this;
	this.open();
	this.send();
}

Ajax.currentlyRefreshing = false;

Ajax.prototype.stateChange = function()
{
	var me = this.object;

	// console.log("this in stateChage is: "+ this);
	// console.log("object in stateChange is: "+ this.object);

	switch (this.readyState)
	{

	case 1:
		if (typeof (me.connection) === "function")
			me.connection(this, me);
		break;
	case 2:
		if (typeof (me.received) === "function")
			me.received(this, me);
		break;
	case 3:
		if (typeof (me.processing) === "function")
			me.processing(this, me);
		break;
	case 4:
		if (this.status == "200")
		{
			if (typeof (me.success) === "function")
				me.success(this, me);
		}
		else
		{
			if (typeof (me.failure) === "function")
				me.failure(this.status, this, me);
		}
		if (typeof (me.complete) === "function")
			me.complete(this, me);
		if (me.refresh)
			Ajax.currentlyRefreshing = false;
		break;
	default:
		console.log("I don't think I should be here.");
		break;
	}
}

Ajax.prototype.createRequest = function()
{
	try
	{
		this.req = new XMLHttpRequest();
		if (this.refresh)
			Ajax.currentlyRefreshing = true;
	}
	catch (error)
	{
		alert("The request could not be created: </br>" + error);
		console.error("failed to create request: " + error);
	}

}

Ajax.prototype.open = function()
{
	try
	{
		this.req.open(this.method, this.url, this.type);
	}
	catch (error)
	{
		console.error("failed to open request: " + error);
	}
}

Ajax.prototype.send = function()
{
	var data = this.data || null;
	try
	{
		this.req.send(data);
	}
	catch (error)
	{
		console.error("failed to send request: " + error);
	}
}


// End ATVUtils
// ***************************************************



FlowManager = function()
{
};

FlowManager.prototype.setTargetPage = function(pageId)
{
	atv.localStorage[TARGET_PAGEID_KEY] = pageId;
	console.debug("flowManager.setTargetPage() atv.localStorage[TARGET_PAGEID_KEY] " + atv.localStorage[TARGET_PAGEID_KEY]);
}

FlowManager.prototype.clearTargetPage = function()
{
	atv.localStorage.removeItem(TARGET_PAGEID_KEY);
	console.debug("flowManager.clearTargetPage() atv.localStorage[TARGET_PAGEID_KEY] " + atv.localStorage[TARGET_PAGEID_KEY]);
}

FlowManager.prototype.startPurchaseFlow = function()
{
	atv.localStorage[PURCHASE_FLOW_KEY] = "true";
	console.debug("flowManager.startPurchaseFlow() atv.localStorage[PURCHASE_FLOW_KEY] " + atv.localStorage[PURCHASE_FLOW_KEY]);
}

FlowManager.prototype.endPurchaseFlow = function()
{
	atv.localStorage.removeItem(PURCHASE_FLOW_KEY);
	atv.localStorage.removeItem(PURCHASE_PRODUCT_ID_KEY);
	console.debug("flowManager.endPurchaseFlow() atv.localStorage[PURCHASE_FLOW_KEY] " + atv.localStorage[PURCHASE_FLOW_KEY]);
}

FlowManager.prototype.inPurchaseFlow = function()
{
	console.debug("flowManager.inPurchaseFlow() atv.localStorage[PURCHASE_FLOW_KEY] " + atv.localStorage[PURCHASE_FLOW_KEY]);
	return (atv.localStorage[PURCHASE_FLOW_KEY] == "true");
}

FlowManager.prototype.inPurchaseFlow = function()
{
	console.debug("flowManager.inPurchaseFlow() atv.localStorage[PURCHASE_FLOW_KEY] " + atv.localStorage[PURCHASE_FLOW_KEY]);
	return (atv.localStorage[PURCHASE_FLOW_KEY] == "true");
}

FlowManager.prototype.inMediaFlow = function()
{
	console.debug("flowManager.inMediaFlow() atv.localStorage[MEDIA_META_KEY] " + atv.localStorage[MEDIA_META_KEY]);
	return atv.localStorage.getItem(MEDIA_META_KEY) != null && atv.localStorage.getItem(MEDIA_META_REQUEST_PARAMS_KEY) != null;
}

FlowManager.prototype.endMediaFlow = function()
{
	atv.localStorage.removeItem(MEDIA_META_KEY);
	atv.localStorage.removeItem(MEDIA_META_REQUEST_PARAMS_KEY);
	console.debug("flowManager.endMediaFlow() atv.localStorage[MEDIA_META_KEY] " + atv.localStorage[MEDIA_META_KEY]);
}

var flowManager = new FlowManager;

console.debug("atvutils.js flowManager created");HTTP/1.1 200 OK
Server: Oracle-iPlanet-Web-Server/7.0
Date: Wed, 08 Aug 2012 16:14:48 GMT
Content-type: application/x-javascript
Last-modified: Thu, 14 Jun 2012 17:05:42 GMT
Content-length: 27397
Etag: "6b05-4fda19e6"
Accept-ranges: bytes

console.log("~~~~~ MLB.TV service.js START");

var PRODUCT_LIST = {
	"IOSATBAT2012GDATVMON" : {
		"image" : "monthlyPremium.jpg",
		"period" : "month"
	}, 
	"IOSMLBTVYEARLY2012" : {
		"image" : "monthlyPremium.jpg",
		"period" : "year"
	}
};

var MLBTV_FEATURE_CONTEXT = "MLBTV2012.INAPPPURCHASE";
var MLBTV_FEATURE_NAME = "MLBALL";

var IOS_IDENTITY_POINT_KEY = "ios.identityPointId";
var IOS_FINGERPRINT_KEY = "ios.fingerprint";
var USERNAME_KEY = "username";
var EMAIL_IDENTITY_POINT_KEY = "email.identityPointId";
var EMAIL_FINGERPRINT_KEY = "email.fingerprint";
var BEST_IDENTITY_POINT_KEY = "best.identityPointId";
var BEST_FINGERPRINT_KEY = "best.fingerprint";
var EMAIL_LINKED_KEY = "email.linked";
var AUTHORIZED_KEY = "authorized";
var SHOW_LINK_STATUS = "showLinkStatus";
var MEDIA_META_KEY = "media.meta";
var MEDIA_META_REQUEST_PARAMS_KEY = "media.meta.requestParams";
var OFFER_URI_AFTER_AUTH_KEY = "offer.uriToLoadAfterAuth";
var OFFER_PURCHASE_FUNCTION_KEY = "offer.purchaseFunction";
var OFFER_SWAP_PAGE_KEY = "offer.swapPage";

ServiceClient = function()
{
};

ServiceClient.prototype.login = function(username, password, success, error)
{
	var url = config.bamServiceBase + "/pubajaxws/services/IdentityPointService";
	var identifyBody = "<soapenv:Envelope xmlns:soapenv=\"http://schemas.xmlsoap.org/soap/envelope/\" xmlns:ns=\"http://services.bamnetworks.com/registration/types/1.0\"><soapenv:Body><ns:identityPoint_identify_request><ns:identification type=\"email-password\"> \
			<ns:email><ns:address>" + username + "</ns:address></ns:email> \
			<ns:password>" + password + "</ns:password> \
		</ns:identification></ns:identityPoint_identify_request></soapenv:Body></soapenv:Envelope>";
	var headers = {
		"SOAPAction" : "http://services.bamnetworks.com/registration/identityPoint/identify",
		"Content-Type" : "text/xml"
	};

	console.log('Trying to authenticate with ' + url);

	atvutils.makeRequest(url, "POST", headers, identifyBody, function(document)
	{
		var identityPointId, fingerprint;
		var statusCode = (document && document.rootElement) ? document.rootElement.childElements[1].getElementByName("identityPoint_identify_response").getElementByName("status").getElementByName("code").textContent : -1;
		console.log('status code is ' + statusCode);

		var errorMessage;
		if (statusCode == 1)
		{
			var identification = document.rootElement.childElements[1].getElementByName("identityPoint_identify_response").getElementByName("identification");
			identityPointId = identification.getElementByName("id").textContent;
			fingerprint = identification.getElementByName("fingerprint").textContent;
		}
		else if (statusCode == -1000)
		{
			errorMessage = "The account name or password was incorrect. Please try again.";
		}
		else
		{
			errorMessage = "MLB.com is curently undergoing maintenance. Please try again.";
		}

		if (identityPointId && fingerprint)
		{
			atv.localStorage[USERNAME_KEY] = username;
			atv.localStorage[EMAIL_IDENTITY_POINT_KEY] = identityPointId;
			atv.localStorage[EMAIL_FINGERPRINT_KEY] = fingerprint;

			if (!atv.localStorage[BEST_IDENTITY_POINT_KEY] && !atv.localStorage[BEST_IDENTITY_POINT_KEY])
			{
				console.debug("saving the email as the best identity point id");
				atv.localStorage[BEST_IDENTITY_POINT_KEY] = identityPointId;
				atv.localStorage[BEST_FINGERPRINT_KEY] = fingerprint;
			}
			console.debug("best identity point id: " + atv.localStorage[BEST_IDENTITY_POINT_KEY]);

			console.debug("determine whether the user is authorized " + serviceClient.loadUserAuthorized);
			serviceClient.loadUserAuthorized(function()
			{
				if (success)
					success();
			});
		}
		else
		{
			if (error)
				error(errorMessage);
			console.log('error message ' + errorMessage);
		}
	});
}

ServiceClient.prototype.loadUserAuthorized = function(callback)
{
	var url = config.bamServiceBase + "/pubajaxws/services/FeatureService";
	var body = "<soapenv:Envelope xmlns:soapenv=\"http://schemas.xmlsoap.org/soap/envelope/\" xmlns:ns=\"http://services.bamnetworks.com/registration/types/1.6\">\
			<soapenv:Body>\
				<ns:feature_findEntitled_request>\
					<ns:identification type=\"fingerprint\">\
						<ns:id>" + atv.localStorage[BEST_IDENTITY_POINT_KEY] + "</ns:id>\
						<ns:fingerprint>" + atv.localStorage[BEST_FINGERPRINT_KEY] + "</ns:fingerprint>\
					</ns:identification>\
					<ns:featureContextName>" + MLBTV_FEATURE_CONTEXT + "</ns:featureContextName>\
				</ns:feature_findEntitled_request>\
			</soapenv:Body>\
		</soapenv:Envelope>";

	var headers = {
		"SOAPAction" : "http://services.bamnetworks.com/registration/feature/findEntitledFeatures",
		"Content-Type" : "text/xml"
	};

	console.debug('loadUserAuthorized() Feature find entitlement request body ' + body);
	console.debug('loadUserAuthorized() Finding entitled features with ' + url);

	atvutils.makeRequest(url, "POST", headers, body, function(document, text)
	{
		console.debug("loadUserAuthorized() Feature.findEntitled response " + text);
		var authorized = (text.indexOf(MLBTV_FEATURE_NAME) > -1);
		console.debug("loadUserAuthorized() authorized " + authorized);
		atv.localStorage[AUTHORIZED_KEY] = authorized.toString();
		console.debug("loadUserAuthorized() set atv.localStorage[AUTHORIZED_KEY] to " + atv.localStorage[AUTHORIZED_KEY]);
		callback(authorized);
	});
}

ServiceClient.prototype.addRestoreObserver = function(successCallback, errorCallback)
{
	// Setup a transaction observer to see when transactions are restored and when new payments are made.

	var transReceived = [];
	
	var restoreObserver = {
		updatedTransactions : function(transactions)
		{
			console.debug("addRestoreObserver() updatedTransactions " + (transactions) ? transactions.length : null);

			if (transactions)
			{
				for ( var i = 0; i < transactions.length; ++i)
				{
					transaction = transactions[i];
					if (transaction.transactionState == atv.SKPaymentTransactionStateRestored)
					{
						console.debug("addRestoreObserver() Found restored transaction: " + GetLogStringForTransaction(transaction) + "\n---> Original Transaction: " + GetLogStringForTransaction(transaction.originalTransaction));
						
						// add
						transReceived.push(transaction);
						
						// finish the transaction
						atv.SKDefaultPaymentQueue.finishTransaction(transaction);
					}
				}
			}
			console.debug("addRestoreObserver() updatedTransactions " + transactions.length);
		},

		restoreCompletedTransactionsFailedWithError : function(error)
		{
			console.debug("addRestoreObserver() restoreCompletedTransactionsFailedWithError start " + error + " code: " + error.code);
			
			atv.SKDefaultPaymentQueue.removeTransactionObserver(this);
			if (typeof (errorCallback) == "function")
				errorCallback(error);
			
			console.debug("addRestoreObserver() restoreCompletedTransactionsFailedWithError end " + error + " code: " + error.code);
		},

		restoreCompletedTransactionsFinished : function()
		{
			console.debug("addRestoreObserver() restoreCompletedTransactionsFinished start");
			
			atv.SKDefaultPaymentQueue.removeTransactionObserver(this);
			
			console.log("addRestoreObserver() transReceived " + transReceived);
			if (typeof (successCallback) == "function")
			{
				successCallback(transReceived);
			}	
			
			console.debug("addRestoreObserver() restoreCompletedTransactionsFinished end");
		}
	};

	atv.SKDefaultPaymentQueue.addTransactionObserver(restoreObserver);
	return restoreObserver;
}

function GetLogStringForTransaction(t)
{
	var result = "Transaction: " + "transactionIdentifier=" + ((t.transactionIdentifier) ? t.transactionIdentifier : "") + ", transactionState=" + ((t.transactionState) ? t.transactionState : "") + ", transactionReceipt=" + ((t.transactionReceipt) ? t.transactionReceipt : "") + ", originalTransaction= " + ((t.originalTransaction) ? t.originalTransaction : "") + ", transactionDate=" + ((t.transactionDate) ? t.transactionDate : "");

	if (t.payment)
	{
		result += ", payment.productIdentifier=" + t.payment.productIdentifier + ", payment.quantity=" + t.payment.quantity;
	}

	if (t.error)
	{
		result += ", error.domain=" + t.error.domain + ", error.code=" + t.error.code + ", error.userInfo=" + t.error.userInfo;
	}

	return result;
}

ServiceClient.prototype.doRequestProduct = function(productIds, successCallback, errorCallback)
{
	console.debug("doRequestProduct() productIds " + productIds);
	// make another products request to get product information
	var request = new atv.SKProductsRequest(productIds);

	request.onRequestDidFinish = function()
	{
		console.debug("doRequestProduct() Products request succeeded");
		if (typeof (successCallback) == "function")
			successCallback();
	};

	request.onRequestDidFailWithError = function(error)
	{
		console.error("doRequestProduct() Products request failed. " + ErrorToDebugString(error));
		if (typeof (errorCallback) == "function")
			errorCallback(error);
	};

	request.onProductsRequestDidReceiveResponse = function(productsResponse)
	{
		console.log("doRequestProduct() Products request received response " + productsResponse);
		console.debug("There are " + productsResponse.products.length + " offers.");

		if (typeof (successCallback) == "function")
			successCallback(productsResponse);
	};

	console.debug("Calling doRequestProduct() start");
	request.start();
}

ServiceClient.prototype.startPurchase = function()
{
	var productID = atv.localStorage["purchase.productId"];

	if (!productID)
	{
		console.debug("startPurchase(): not making a purchase because productID was not provided");
	}
	else
	{
		// make another products request to get product information
		var request = new atv.SKProductsRequest([ productID ]);

		request.onRequestDidFailWithError = function(error)
		{
			console.error("startPurchase() Products request failed. " + ErrorToDebugString(error));
		};

		request.onProductsRequestDidReceiveResponse = function(productsResponse)
		{
			console.log("startPurchase() Products request received response " + productsResponse);
			console.debug("There are " + productsResponse.products.length + " offers.");

			if (productsResponse && productsResponse.products && productsResponse.products.length == 1)
			{
				var product = productsResponse.products[0];
				console.debug("Found available product for ID " + product);
				var payment = {
					product : product,
					quantity : 1
				};
				atv.SKDefaultPaymentQueue.addPayment(payment);
			}
		};

		console.debug("Calling doRequestProduct() start");
		request.start();
	}
}

ServiceClient.prototype.addPurchaseObserver = function(successCallback, errorCallback)
{
	console.debug("addPurchaseObserver(): Purchase product with identifier " + productID);

	var productID = atv.localStorage["purchase.productId"];

	var observer = {
		updatedTransactions : function(transactions)
		{
			console.debug("addPurchaseObserver(): purchase.updatedTransactions: " + transactions.length);

			for ( var i = 0; i < transactions.length; ++i)
			{
				var transaction = transactions[i];

				console.debug("addPurchaseObserver(): Processing transaction " + i + ": " + GetLogStringForTransaction(transaction));

				if (transaction.payment && transaction.payment.productIdentifier == productID)
				{
					console.debug("addPurchaseObserver(): transaction is for product ID " + productID);

					// This is the product ID this transaction observer was created for.
					switch (transaction.transactionState)
					{
					case atv.SKPaymentTransactionStatePurchasing:
						console.debug("addPurchaseObserver(): SKPaymentTransactionStatePurchasing");
						break;
					case atv.SKPaymentTransactionStateRestored:
					case atv.SKPaymentTransactionStatePurchased:
						var reason = transaction.transactionState == atv.SKPaymentTransactionStatePurchased ? "Purchased" : "Restored";
						console.debug("addPurchaseObserver(): SKPaymentTransactionStatePurchased: Purchase Complete!!! Reason: " + reason);
						atv.SKDefaultPaymentQueue.finishTransaction(transaction);
						atv.SKDefaultPaymentQueue.removeTransactionObserver(this);
						if (typeof (successCallback) == "function")
							successCallback(transaction);
						break;
					case atv.SKPaymentTransactionStateFailed:
						console.debug("addPurchaseObserver(): SKPaymentTransactionStateFailed. " + ErrorToDebugString(transaction.error));
						atv.SKDefaultPaymentQueue.finishTransaction(transaction);
						atv.SKDefaultPaymentQueue.removeTransactionObserver(this);
						if (typeof (errorCallback) == "function")
							errorCallback(transaction);
						break;
					default:
						if (typeof (errorCallback) == "function")
							errorCallback(transaction);
						console.error("addPurchaseObserver(): Unknown transaction state: " + transaction.transactionState);
						break;
					}
				}
			}
		}
	}

	atv.SKDefaultPaymentQueue.addTransactionObserver(observer);
	return observer;
}

ServiceClient.prototype.startMediaFlow = function(mediaMeta)
{
	console.debug("startMediaFlow() mediaMeta " + mediaMeta);

	if (mediaMeta)
	{
		// set media meta to the local storage
		atv.localStorage[MEDIA_META_KEY] = JSON.stringify(mediaMeta);

		mediaMeta.title = mediaMeta.awayTeamCity + " " + mediaMeta.awayTeamName + " vs " + mediaMeta.homeTeamCity + " " + mediaMeta.homeTeamName;
		mediaMeta.description = mediaMeta.feed;
		if (mediaMeta.awayTeamId && mediaMeta.homeTeamId)
		{
			mediaMeta.imageUrl = config.imageBase + "/recapThumbs/" + mediaMeta.awayTeamId + "_v_" + mediaMeta.homeTeamId + ".jpg";
		}

		// set the session key from the local storage
		mediaMeta.sessionKey = atv.localStorage["sessionKey"];

		var mediaMetaRequestParams = "";
		mediaMetaRequestParams += '&identityPointId=' + ((atv.localStorage[BEST_IDENTITY_POINT_KEY]) ? atv.localStorage[BEST_IDENTITY_POINT_KEY] : 'IPID');
		mediaMetaRequestParams += '&fingerprint=' + ((atv.localStorage[BEST_FINGERPRINT_KEY]) ? atv.localStorage[BEST_FINGERPRINT_KEY] : 'FINGERPRINT');
		mediaMetaRequestParams += "&contentId=" + mediaMeta.contentId;
		mediaMetaRequestParams += '&sessionKey=' + ((mediaMeta.sessionKey) ? encodeURIComponent(mediaMeta.sessionKey) : "SESSION_KEY");
		mediaMetaRequestParams += '&calendarEventId=' + mediaMeta.calendarEventId;
		mediaMetaRequestParams += "&audioContentId=" + ((mediaMeta.audioContentId) ? mediaMeta.audioContentId.replace(/\s/g, ',') : "");
		mediaMetaRequestParams += '&day=' + mediaMeta.day
		mediaMetaRequestParams += '&month=' + mediaMeta.month;
		mediaMetaRequestParams += '&year=' + mediaMeta.year;
		mediaMetaRequestParams += "&feedType=" + mediaMeta.feedType;
		mediaMetaRequestParams += "&playFromBeginning=" + mediaMeta.playFromBeginning;
		mediaMetaRequestParams += "&title=" + encodeURIComponent(mediaMeta.title);
		mediaMetaRequestParams += "&description=" + encodeURIComponent(mediaMeta.description);
		mediaMetaRequestParams += (mediaMeta.imageUrl) ? ("&imageUrl=" + encodeURIComponent(mediaMeta.imageUrl)) : "";

		// save in local storage (should be in session storage but session storage is not yet supported)
		atv.localStorage[MEDIA_META_KEY] = JSON.stringify(mediaMeta);
		atv.localStorage[MEDIA_META_REQUEST_PARAMS_KEY] = mediaMetaRequestParams;
		console.debug("storing into local storage mediaMetaRequestParams " + atv.localStorage[MEDIA_META_REQUEST_PARAMS_KEY]);
	}
}

ServiceClient.prototype.loadMediaPage = function(callback)
{
	if (atv.localStorage[MEDIA_META_REQUEST_PARAMS_KEY])
	{
		var mediaProxyUrl = config.appletvBase + "/views/MediaServiceProxy?";
		// mediaProxyUrl += '&identityPointId=' + atv.localStorage[BEST_IDENTITY_POINT_KEY];
		// mediaProxyUrl += '&fingerprint=' + encodeURIComponent(atv.localStorage[BEST_FINGERPRINT_KEY]);
		mediaProxyUrl += atv.localStorage[MEDIA_META_REQUEST_PARAMS_KEY];
		console.debug("loadMediaPage mediaProxyUrl " + mediaProxyUrl);

		// make request
		atvutils.getRequest(mediaProxyUrl, callback);
	}
	else if (callback)
	{
		callback();
	}
}

function applyOffer(document)
{
	console.debug("applyOffer() atv.localStorage[PURCHASE_FLOW_KEY] " + atv.localStorage[PURCHASE_FLOW_KEY]);
	console.debug("applyOffer() document " + document);
	var mlbSignIn = (document) ? document.getElementById("mlbSignIn") : null;
	if (mlbSignIn)
	{
		var label = mlbSignIn.getElementByName("label");
		console.debug("applyOffer() atv.localStorage[EMAIL_IDENTITY_POINT_KEY] " + atv.localStorage[EMAIL_FINGERPRINT_KEY]);
		var signedIn = atv.localStorage[EMAIL_IDENTITY_POINT_KEY] != null && atv.localStorage[EMAIL_FINGERPRINT_KEY] != null;
		console.debug("applyOffer() signedIn " + signedIn);

		var uriToLoadAfterAuth = atv.localStorage[OFFER_URI_AFTER_AUTH_KEY];

		if (signedIn)
		{
			label.removeFromParent();
			label = atv.parseXML("<label>Sign In with different user</label>").rootElement;
			label.removeFromParent();
			mlbSignIn.appendChild(label);

			mlbSignIn.setAttribute("onSelect", "atv.logout();atvutils.loadAndSwapURL('" + config.appletvBase + "/views/Login?urlToLoadAfterAuth=" + uriToLoadAfterAuth + "');");
			mlbSignIn.setAttribute("onPlay", "atv.logout();atvutils.loadAndSwapURL('" + config.appletvBase + "/views/Login?urlToLoadAfterAuth=" + uriToLoadAfterAuth + "');");
		}
		else
		{
			console.debug("applyOffer() uriToLoadAfterAuth " + uriToLoadAfterAuth);
			label.textContent = "MLB.TV Premium Subscriber Login";
			console.debug("applyOffer() mlbSignIn.getAttribute('onSelect') " + mlbSignIn.getAttribute('onSelect'));
			mlbSignIn.setAttribute("onSelect", "atvutils.loadAndSwapURL('" + config.appletvBase + "/views/Login?urlToLoadAfterAuth=" + uriToLoadAfterAuth + "');");
			mlbSignIn.setAttribute("onPlay", "atvutils.loadAndSwapURL('" + config.appletvBase + "/views/Login?urlToLoadAfterAuth=" + uriToLoadAfterAuth + "');");
			console.debug("applyOffer() mlbSignIn.getAttribute('onSelect') " + mlbSignIn.getAttribute('onSelect'));
		}
	}
}

ServiceClient.prototype.loadOfferPage = function(newPage)
{
	console.debug("loadOfferPage() enter");

	newPage
	
	// set purchase flow to true
	flowManager.startPurchaseFlow();

	var swap = (atv.localStorage[OFFER_SWAP_PAGE_KEY] == "true");
	console.debug("loadOfferPage() atv.localStorage[OFFER_SWAP_PAGE_KEY] " + atv.localStorage[OFFER_SWAP_PAGE_KEY]);
	console.debug("loadOfferPage() swap " + swap);

	// Use StoreKit to get the list of product identifiers.
	var request = new atv.SKProductsRequest(Object.keys(PRODUCT_LIST));
	request.onRequestDidFailWithError = function(error)
	{
		console.error("Products request failed. " + ErrorToDebugString(error));
		var xml = atvutils.makeOkDialog("MLB.TV", "MLB.TV Products could not be found. Please check again at a later time.");
		if (swap)
			newPage.swapXML(xml)
		else
			newPage.loadXML(xml);
	};
	request.onProductsRequestDidReceiveResponse = function(productsResponse)
	{
		// success
		console.debug("loadOfferPage() Products request received response " + productsResponse);
		console.debug("loadOfferPage() There are " + productsResponse.products.length + " offers.");

		// add image
		console.debug("loadOfferPage() checking for image location");
		for ( var i = 0; i < productsResponse.products.length; i++)
		{
			var product = productsResponse.products[i];
			console.debug("loadOfferPage() product.productIdentifier " + product.productIdentifier);
			if (PRODUCT_LIST[product.productIdentifier] != null)
			{
				product.image = PRODUCT_LIST[product.productIdentifier].image;
				console.debug("loadOfferPage() product.image " + product.image);

				product.period = PRODUCT_LIST[product.productIdentifier].period;
				console.debug("loadOfferPage() product.period " + product.period);
			}
		}

		// This page will display the offers, and call purchaseProduct below with the productId to purchase in an onSelect handler.
		var offerBody = "";
		offerBody += "productMeta=" + JSON.stringify(productsResponse);
		offerBody += "&purchaseFunction=" + atv.localStorage[OFFER_PURCHASE_FUNCTION_KEY];
		if (atv.localStorage[OFFER_URI_AFTER_AUTH_KEY])
		{
			offerBody += "&uriToLoadAfterAuth=" + atv.localStorage[OFFER_URI_AFTER_AUTH_KEY];
		}

		console.debug("loadOfferPage() offerBody: " + offerBody);

		atvutils.postRequest(config.appletvBase + '/views/Offer', offerBody, function(xml)
		{
			try
			{
				console.debug("loadOfferPage() postRequest xml=" + xml);
				applyOffer(xml);
				if (swap)
					newPage.swapXML(xml)
				else
					newPage.loadXML(xml);
			}
			catch (e)
			{
				console.error("Caught exception for the Offer page " + e);
				var errorXML = atvutils.makeOkDialog("MLB.TV Error", "MLB.TV Products could not be found. Please check again at a later time.");
				if (swap)
					newPage.swapXML(errorXML)
				else
					newPage.loadXML(errorXML);
			}
		});
	};

	console.debug("Calling loadOfferPage() start");
	request.start();
}
ServiceClient.prototype.isMediaResponseUnauthorized = function(xml)
{
	return (xml) ? this.isMediaUnauthorized(this.getMediaStatusCode(xml)) : true;
}

ServiceClient.prototype.isMediaUnauthorized = function(mediaStatusCode)
{
	return "" == mediaStatusCode || "UNDETERMINED" == mediaStatusCode || "NOT_AUHTORIZED" == mediaStatusCode || "LOGIN_REQUIRED" == mediaStatusCode || "INVALID_USER_CREDENTIALS_STATUS" == mediaStatusCode;
}

ServiceClient.prototype.isMediaPlayback = function(mediaStatusCode)
{
	return "VIDEO_PLAYBACK" == mediaStatusCode || "AUDIO_PLAYBACK" == mediaStatusCode;
}

ServiceClient.prototype.getMediaStatusCode = function(xml)
{
	var mediaStatusCodeElem = (xml && xml.rootElement) ? xml.rootElement.getElementByTagName("mediaStatusCode") : null;
	console.debug("getMediaStatusCode() mediaStatusCodeElem " + mediaStatusCodeElem);
	var mediaStatusCode = (mediaStatusCodeElem != null) ? mediaStatusCodeElem.textContent : "UNDETERMINED";
	console.debug("getMediaStatusCode() mediaStatusCode " + mediaStatusCode);
	return mediaStatusCode;
}

ServiceClient.prototype.createFingerprintIdpt = function(ipid, fingerprint)
{
	return {
		"type" : "fingerprint",
		"id" : ipid,
		"fingerprint" : fingerprint
	};
}

ServiceClient.prototype.createIosIdentityPoint = function(transaction)
{
	return {
		"type" : "iosInAppPurchase",
		"receipt" : transaction.transactionReceipt
	};
}

ServiceClient.prototype.createLinkRequest = function(transactions)
{
	var identityPoints = [];

	// email-password fingerprint identity point
	if (atv.localStorage[EMAIL_FINGERPRINT_KEY] && atv.localStorage[EMAIL_IDENTITY_POINT_KEY])
	{
		identityPoints.push(this.createFingerprintIdpt(atv.localStorage[EMAIL_IDENTITY_POINT_KEY], atv.localStorage[EMAIL_FINGERPRINT_KEY]));
	}

	if (transactions)
	{
		console.debug("createLinkRequest() filtering out for unique original transactions");
		
		// filter out unique original transactions
		var originalTransMap = {};
		
		for (var t = 0; t < transactions.length; t++)
		{
			var currTrans = transactions[t];
			var origTrans = currTrans.originalTransaction;
			var transId = (origTrans == null) ? currTrans.transactionIdentifier : origTrans.transactionIdentifier;  
			
			console.debug("createLinkRequest() transId " + transId);
			
			if (originalTransMap[transId] == null)
			{
				// transaction identity point
				identityPoints.push(this.createIosIdentityPoint(currTrans));
				
				originalTransMap[transId] = currTrans;
			}
		}
	}
	// best fingerprint identity point
	else if (atv.localStorage[BEST_FINGERPRINT_KEY] && atv.localStorage[BEST_IDENTITY_POINT_KEY])
	{
		identityPoints.push(this.createFingerprintIdpt(atv.localStorage[BEST_IDENTITY_POINT_KEY], atv.localStorage[BEST_FINGERPRINT_KEY]));
	}

	var linkRequest = null;
	if (identityPoints.length > 0)
	{
		// link request
		linkRequest = {
			"identityPoints" : identityPoints,
			"namespace" : "mlb",
			"key" : "h-Qupr_3eY"
		};
	}

	console.debug("createLinkRequest() linkRequest " + JSON.stringify(linkRequest));

	return linkRequest;
}

ServiceClient.prototype.doLink = function(transactions, successCallback, errorCallback)
{
	var linkRequest = this.createLinkRequest(transactions);

	if (linkRequest == null)
	{
		if (typeof (successCallback) == "function")
			successCallback();

		console.debug("doLink() link not called since nothing to link");
	}
	else
	{
		// link request
		var linkRequestBody = JSON.stringify(linkRequest);
		var req = new XMLHttpRequest();
		var url = config.mlbBase + "/mobile/LinkAll2.jsp";

		req.onreadystatechange = function()
		{
			try
			{
				if (req.readyState == 4)
				{
					console.log('link() Got link service http status code of ' + req.status);
					if (req.status == 200)
					{
						console.log('link() Response text is ' + req.responseText);
						var linkResponseJson = JSON.parse(req.responseText);

						if (linkResponseJson.status == 1)
						{
							console.debug("link() bestIdentityPointToUse: " + linkResponseJson.bestIdentityPointToUse);
							console.debug("link() messages: " + linkResponseJson.messages);

							if (linkResponseJson.bestIdentityPointToUse && linkResponseJson.bestIdentityPointToUse.id && linkResponseJson.bestIdentityPointToUse.fingerprint)
							{
								console.debug("link() saving the best identity point to use");
								atv.localStorage[BEST_IDENTITY_POINT_KEY] = linkResponseJson.bestIdentityPointToUse.id;
								atv.localStorage[BEST_FINGERPRINT_KEY] = linkResponseJson.bestIdentityPointToUse.fingerprint;
								console.debug("link() identityPointId " + atv.localStorage[BEST_IDENTITY_POINT_KEY]);

								if (linkResponseJson.bestIdentityPointToUse.id.toLowerCase().indexOf("ios") >= 0)
								{
									atv.localStorage[IOS_IDENTITY_POINT_KEY] = linkResponseJson.bestIdentityPointToUse.id;
									atv.localStorage[IOS_FINGERPRINT_KEY] = linkResponseJson.bestIdentityPointToUse.fingerprint;
								}
							}

							var linked = false;// , auth = false;
							for ( var m = 0; m < linkResponseJson.messages.length; m++)
							{
								var msg = linkResponseJson.messages[m];
								console.debug("doLink() msg " + msg);
								if (msg == "linked")
								{
									linked = true;
								}
							}
							linked = linked && atv.localStorage[EMAIL_IDENTITY_POINT_KEY] != null && atv.localStorage[EMAIL_FINGERPRINT_KEY] != null;
							// atv.localStorage[EMAIL_LINKED_KEY] = linked.toString();
							// atv.localStorage[AUTHORIZED_KEY] = auth.toString();

							// console.debug("doLink() atv.localStorage[EMAIL_LINKED_KEY] " + atv.localStorage[EMAIL_LINKED_KEY];
							console.debug("atv.localStorage[AUTHORIZED_KEY] " + atv.localStorage[AUTHORIZED_KEY]);

							if (typeof (successCallback) == "function")
								successCallback();

							console.log("doLink() getAllResponseHeaders: " + this.getAllResponseHeaders());
						}
						else
						{

							if (typeof (errorCallback) == "function")
								errorCallback();
							console.error("link response json has an invalid status code: " + linkResponseJson.status);
						}
					}
				}
			}
			catch (e)
			{
				// Specify a copyedited string because this will be displayed to
				// the user.
				console.error('Caught exception while processing request. Aborting. Exception: ' + e);
				if (typeof (errorCallback) == "function")
					errorCallback(e);

				req.abort();
			}
		}

		req.open("POST", url, true);
		req.setRequestHeader("Content-Type", "text/xml");

		console.log('Trying to link with ' + url);
		console.log("Link Service POST body is " + linkRequestBody);
		req.send(linkRequestBody);
	}
}

var serviceClient = new ServiceClient;

console.log("~~~~~ MLB.TV service.js END");HTTP/1.1 200 OK
Server: Oracle-iPlanet-Web-Server/7.0
Date: Wed, 08 Aug 2012 16:14:49 GMT
Content-type: application/x-javascript
Last-modified: Tue, 31 Jul 2012 21:32:23 GMT
Content-length: 42766
Etag: "a70e-50184ee7"
Accept-ranges: bytes

console.log("~~~~~ MLB.TV main.js start");

var IOS_IDENTITY_POINT_KEY = "ios.identityPointId";
var IOS_FINGERPRINT_KEY = "ios.fingerprint";
var BEST_IDENTITY_POINT_KEY = "best.identityPointId";
var BEST_FINGERPRINT_KEY = "best.fingerprint";
var USERNAME_KEY = "username";

var SHOW_SCORES_KEY = "showScores";
var FAVORITE_TEAMS_KEY = "favoriteTeamIds";

var SESSION_KEY = "sessionKey";
var MEDIA_META_KEY = "media.meta";
var MEDIA_META_REQUEST_PARAMS_KEY = "media.meta.requestParams";

var PURCHASE_FLOW_KEY = "flow.purchase";
var PURCHASE_PRODUCT_ID_KEY = "purchase.productId";

var EMAIL_LINKED_KEY = "email.linked";
var AUTHORIZED_KEY = "authorized";

var OFFER_URI_AFTER_AUTH_KEY = "offer.uriToLoadAfterAuth";
var OFFER_PURCHASE_FUNCTION_KEY = "offer.purchaseFunction";
var OFFER_SWAP_PAGE_KEY = "offer.swapPage";

var TARGET_PAGEID_KEY = "targetPage";
var PURCHASE_FLOW_KEY = "flow.purchase";
var OFFER_PAGE_ID = "com.bamnetworks.appletv.offer";

var TODAYS_GAMES_REFRESH_URL_KEY = "todaysGames.refreshUrl";
var GAMES_PAGE_REFRESH_URL_KEY = "games.refreshUrl";

// atv.onPageLoad
atv.onPageLoad = function(id)
{
	console.log("main.js atv.onPageLoad() id: " + id);

	// <mlbtv>
	var headElem = document.rootElement.getElementByName("head");
	if (headElem && headElem.getElementByName("mlbtv"))
	{
		var mlbtvElem = headElem.getElementByName("mlbtv");
		console.log("mlbtv exists");

		// store local variables
		storeLocalVars(mlbtvElem);

		// ping urls
		pingUrls(mlbtvElem);
	}

	// games by team
	if (id && id.indexOf("com.bamnetworks.appletv.gamesByTeam.") >= 0)
	{
		applyGamesPage(document);
	}

	if (id && id.indexOf("com.bamnetworks.appletv.media") >= 0 && typeof (flowManager) != "undefined")
	{
		flowManager.endPurchaseFlow();
		flowManager.endMediaFlow();
		console.debug("main.js onPageLoad() atv.localStorage[PURCHASE_FLOW_KEY] " + atv.localStorage[PURCHASE_FLOW_KEY]);
	}

	atv._debugDumpControllerStack();
}

// atv.onPageUnload
atv.onPageUnload = function(id)
{
	console.debug("main.js atv.onPageUnload() id " + id);
	console.debug("main.js atv.onPageUnload() atv.localStorage[PURCHASE_FLOW_KEY] " + atv.localStorage[PURCHASE_FLOW_KEY]);
	if (flowManager.inPurchaseFlow() && ("com.bamnetworks.appletv.authenticate" == id || id.indexOf("Not Authorized") > -1))
	{
		console.debug("main.js atv.onPageUnload() showing offer page");
		flowManager.setTargetPage(OFFER_PAGE_ID);

		var newPage = new atv.ProxyDocument();
		newPage.show();
		serviceClient.loadOfferPage(newPage);

		console.debug("main.js atv.onPageUnload() showing offer page");
	}
}

function storeLocalVars(mlbtvElem)
{
	// check local storage params
	var localStorageElems = mlbtvElem.getElementsByName("localStorage");
	console.debug("localStorageElems=" + localStorageElems);
	if (localStorageElems && localStorageElems.length > 0)
	{
		var index = 0;
		for (; index < localStorageElems.length; index++)
		{
			var key = localStorageElems[index].getAttribute("key");
			var value = localStorageElems[index].getAttribute("value");
			if (value)
			{
				atv.localStorage[key] = value;
			}
			else
			{
				atv.localStorage.removeItem(key);
			}
			console.log("localStorage[" + key + "] = " + atv.localStorage[key]);
		}
	}
}

function pingUrls(mlbtvElem)
{
	// check ping urls
	var pingElems = mlbtvElem.getElementsByName("ping");
	console.log("pingElems=" + pingElems);
	if (pingElems && pingElems.length > 0)
	{
		for ( var pingIndex = 0; pingIndex < pingElems.length; pingIndex++)
		{
			var url = pingElems[pingIndex].getAttribute('url');
			url = (url == null || url.trim().length() <= 0) ? pingElems[pingIndex].textContent : url;
			console.debug("url=" + url);
			if (url)
			{
				try
				{
					console.log('pinging reporting url ' + url);
					var req = new XMLHttpRequest();
					req.open("GET", url, true);
					req.send();
				}
				catch (e)
				{
					console.error("failed to ping the url " + url + " error " + e);
				}
			}
		}
	}
}

/**
 * Updates the logo according to the screen resolution
 * 
 * @param document
 * @returns
 */
function updateImageResolution(document)
{
	// update logos according to screen resolution
	var frame = atv.device.screenFrame;
	console.log("frame size: " + frame.height);
	if (frame.height == 1080)
	{
		var images = document.rootElement.getElementsByTagName('image');
		for ( var i = 0; i < images.length; i++)
		{
			images[i].textContent = images[i].textContent.replace('720p', '1080p');
		}
	}

	return document;
}

// ------------------------------ load and apply -------------------------------
function applyMain(document)
{
	console.log('applyMain ' + document);

	// end all flows when you're on the main page
	flowManager.endMediaFlow();
	flowManager.endPurchaseFlow();

	// check valid subscription
	// (1) via iTunes on this box and iTunes account signed in. Receipt is present and stored on the box
	// (2) via Vendor and is signed in with Vendor account.
	var validSubscription = (atv.localStorage[IOS_IDENTITY_POINT_KEY] != null && atv.localStorage[IOS_FINGERPRINT_KEY] != null) || (atv.localStorage[AUTHORIZED_KEY] == "true" && atv.localStorage[EMAIL_IDENTITY_POINT_KEY] != null && atv.localStorage[EMAIL_FINGERPRINT_KEY] != null);

	console.log("applyMain() validSubscription " + validSubscription);
	console.log("applyMain() atv.localStorage[AUTHORIZED_KEY] " + atv.localStorage[AUTHORIZED_KEY]);

	var subscribeMenu = document.getElementById("subscribeMlbTv");
	if (validSubscription)
	{
		if (subscribeMenu)
		{
			subscribeMenu.removeFromParent();
			console.debug("applyMain() removing the Subscribe To MLB.TV");
		}
	}
	else
	{
		// add the menu if it's not there
		if (!subscribeMenu)
		{
			subscribeMenu = atv.parseXML('<oneLineMenuItem id="subscribeMlbTv" accessibilityLabel="Subscribe To MLB.TV" onSelect="subscribeToMlbTv()"><label>Subscribe To MLB.TV</label></oneLineMenuItem>').rootElement;
			subscribeMenu.removeFromParent();

			var parent = document.rootElement.getElementByTagName("items");
			console.debug("applyMain() parent " + parent);
			if (parent && parent.childElements)
			{
				console.debug("applyMain() before parent.childElements " + parent.childElements);
				var settingsMenu = document.getElementById("settings");
				settingsMenu.removeFromParent();
				parent.appendChild(subscribeMenu);
				parent.appendChild(settingsMenu);
				console.debug("applyMain() after parent.childElements " + parent.childElements);
			}
		}
	}

	return document;
}

function handleMainVoltileReload()
{
	console.debug("handleMainVoltileReload() atv.localStorage[TARGET_PAGEID_KEY] " + atv.localStorage[TARGET_PAGEID_KEY]);
	if (atv.localStorage[TARGET_PAGEID_KEY] == null || atv.localStorage[TARGET_PAGEID_KEY] == "com.bamnetworks.appletv.main")
	{
		applyMain(document);
		atv.loadAndSwapXML(document);
	}
}

function handleOfferVoltileReload(document)
{
	console.debug("handleOfferVoltileReload() atv.localStorage[AUTHORIZED_KEY] " + atv.localStorage[AUTHORIZED_KEY]);
	if (atv.localStorage[AUTHORIZED_KEY] == "true")
	{
		console.debug("handleOfferVoltileReload() unloading the page because already authorized");
		atv.unloadPage();
	}
	else
	{
		applyOffer(document);
		atv.loadAndSwapXML(document);
	}
}

function applyTodaysGames(responseXML, refresh)
{
	console.log("applyTodaysGames() responseXML: " + responseXML);

	try
	{
		// elements
		var listByNavigation = responseXML.rootElement.getElementByTagName("listByNavigation");
		var imageMenuItems = responseXML.rootElement.getElementsByTagName("imageTextImageMenuItem");
		console.log("applyTodaysGames() navigation=" + navigation);

		// add behavior
		if (atv.localStorage[TODAYS_GAMES_REFRESH_URL_KEY] == null || !refresh)
		{
			var navigation = responseXML.rootElement.getElementByTagName("navigation");
			var currentIndex = navigation.getAttribute("currentIndex");
			console.debug("applyTodaysGames() currentIndex " + currentIndex);
			var refreshUrl = navigation.childElements[currentIndex].getElementByTagName("url").textContent;
			console.debug("applyTodaysGames() refreshUrl " + refreshUrl);
			atv.localStorage[TODAYS_GAMES_REFRESH_URL_KEY] = refreshUrl;
		}
		listByNavigation.setAttribute("refreshInterval", "60");
		listByNavigation.setAttribute("onRefresh", "console.log('refreshing todays games');handleTodaysGamesRefresh();");
		listByNavigation.setAttribute("onNavigate", "console.log('navigating todays games');handleTodaysGamesNavigate(event);");

		// sort
		var favTeamIds = getFavoriteTeamIds();
		function sort(menuOne, menuTwo)
		{
			var menuOneTeamIds = menuOne.getAttribute("id").split("-");
			var menuTwoTeamIds = menuTwo.getAttribute("id").split("-");

			var menuOneOrder = (favTeamIds.indexOf(menuOneTeamIds[0]) >= 0 || favTeamIds.indexOf(menuOneTeamIds[1]) >= 0) ? 1 : 0;
			var menuTwoOrder = (favTeamIds.indexOf(menuTwoTeamIds[0]) >= 0 || favTeamIds.indexOf(menuTwoTeamIds[1]) >= 0) ? 1 : 0;

			// console.log("favTeamIds.indexOf(menuOneTeamIds[0])=" + favTeamIds.indexOf(menuOneTeamIds[0]) + " favTeamIds.indexOf(menuOneTeamIds[1])=" + favTeamIds.indexOf(menuOneTeamIds[1]));
			// console.log(menuOneOrder + "-" + menuTwoOrder);

			return menuTwoOrder - menuOneOrder;
		}
		var orderedMenuList = imageMenuItems.sort(sort);

		// 1. re-append the sorted children
		// 2. set the favorite team logos
		// 3. hide scores on the page
		// 4. set the link to no scores preview
		var index = 0;
		for (; index < orderedMenuList.length; index++)
		{
			// 1. reappend child for sorting
			var menu = orderedMenuList[index];
			var parent = menu.parent;
			menu.removeFromParent();
			parent.appendChild(menu);

			// 2. set the favorite team logos
			console.debug("applyTodaysGames() menu item id " + menu.getAttribute("id"));
			var teamIds = menu.getAttribute("id").split("-");
			var isAwayFav = favTeamIds.indexOf(teamIds[0]) >= 0;
			var isHomeFav = favTeamIds.indexOf(teamIds[1]) >= 0;
			console.debug("applyTodaysGames() isAwayFav " + isAwayFav);
			console.debug("applyTodaysGames() isHomeFav " + isHomeFav);
			var leftImageElem = menu.getElementByName("leftImage");
			if (isAwayFav && leftImageElem.textContent.indexOf("-fav.png") < 0)
			{
				leftImageElem.textContent = leftImageElem.textContent.replace(".png", "-fav.png");
			}
			var rightImageElem = menu.getElementByName("rightImage");
			if (isHomeFav && rightImageElem.textContent.indexOf("-fav.png") < 0)
			{
				rightImageElem.textContent = rightImageElem.textContent.replace(".png", "-fav.png");
			}

			// 3. Hide scores
			// 4. Use the preview link with no scores
			var showScores = (atv.localStorage[SHOW_SCORES_KEY]) ? atv.localStorage[SHOW_SCORES_KEY] : "true";
			var rightLabelElem = menu.getElementByName("rightLabel");
			if (showScores == "false")
			{
				var previewLinkElem = menu.getElementByTagName("link");
				if (rightLabelElem && rightLabelElem.textContent.indexOf("Final") >= 0)
				{
					rightLabelElem.textContent = "Final";
				}
				if (previewLinkElem)
				{
					previewLinkElem.textContent = previewLinkElem.textContent.replace(".xml", "_noscores.xml");
				}
			}

			// 5. Check for double header 3:33 AM ET
			if (rightLabelElem && rightLabelElem.textContent.indexOf("3:33 AM") >= 0)
			{
				rightLabelElem.textContent = "Game 2";
			}
		}

		return responseXML;
	}
	catch (error)
	{
		console.error("unable to apply todays games " + error);
		atvutils.loadError("An error occurred loading today's games", "An error occurred loading today's games");
	}
}

/**
 * Should be called by the onRefresh event, I am also calling this from the handleNavigate function below to share code. Essentially this function: 1. Loads the current Navigation file 2. Pulls the
 * menu items out of the loaded file and dumps them into the current file 3. Checks to see if an onRefresh AJAX call is already being made and if so does nothing.
 * 
 * 
 * @params string url - url to be loaded
 */
function handleTodaysGamesRefresh()
{
	var url = atv.localStorage[TODAYS_GAMES_REFRESH_URL_KEY];
	console.log("handleTodaysGamesRefresh() url " + url);

	console.log("We are about to check the value of Ajax.currentlyRefreshing: --> " + Ajax.currentlyRefreshing);
	// check the Ajax object and see if a refresh event is happening
	if (!Ajax.currentlyRefreshing)
	{
		// if a refresh event is not happening, make another one.
		console.log("handleTodaysGamesRefresh()About to make the AJAX call. --> ")

		var refresh = new Ajax({
			"url" : url,
			"refresh" : true,
			"success" : function()
			{
				var req = arguments[0];

				console.log("handleTodaysGamesRefresh() Ajax url " + url);

				var newDocument = atv.parseXML(req.responseText); // Instead of using the responseXML you should parse the responseText. I know it seems redundent but trust me.

				// update the content page
				var newMenuSections = newDocument.rootElement.getElementByTagName('sections');
				applyTodaysGames(newDocument, true);
				newMenuSections.removeFromParent();

				var menu = document.rootElement.getElementByTagName('menu');
				console.log("handleTodaysGamesRefresh() menu=" + menu);
				if (menu)
				{
					var oldMenuSections = menu.getElementByTagName('sections');
					menu.replaceChild(oldMenuSections, newMenuSections);
				}
				else
				{
					menu = atv.parseXML("<menu></menu>").rootElement;
					menu.removeFromParent();
					menu.appendChild(newMenuSections);

					var listByNavigation = document.rootElement.getElementByTagName("listByNavigation");
					listByNavigation.appendChild(menu);
				}
				
				// replace # Games
				var newTumbler = newDocument.rootElement.getElementByTagName('tumblerWithSubtitle');
				newTumbler.removeFromParent();
				var header = document.rootElement.getElementByTagName('header');
				var oldTumbler = document.rootElement.getElementByTagName('tumblerWithSubtitle');
				header.replaceChild(oldTumbler, newTumbler);
			}
		});
		console.log("handleTodaysGamesRefresh() I have made the AJAX call. <--");
	}
	console.log("handleTodaysGamesRefresh() I am on the other side of the if. <--");
}

/**
 * This function works very similar to the handleRefresh Essentially we are manually loading the data and updating the page in Javascript This means we have to handle a bit more of the magic, but we
 * also get to control what is updated and update more of the page, like headers, previews, etc.
 * 
 * In this example, I am: 1. Getting the new URL from the Navigation Item 2. Changing the onRefresh URL 3. Calling handleRefresh and passing the new url to let handleRefresh do the magic.
 */
function handleTodaysGamesNavigate(e)
{
	console.log("handleTodaysGamesNavigate() " + e + ", e.navigationItemId = " + e.navigationItemId + " document = " + document);
	console.log("e=" + e);
	atvutils.printObject(e);
	console.log("document.getElementById(e.navigationItemId)=" + document.getElementById(e.navigationItemId));
	console.log("document.getElementById(e.navigationItemId).getElementByTagName=" + document.getElementById(e.navigationItemId).getElementByTagName);

	// get the url
	var url = document.getElementById(e.navigationItemId).getElementByTagName('url').textContent;
	atv.localStorage[TODAYS_GAMES_REFRESH_URL_KEY] = url;

	handleTodaysGamesRefresh();

	e.onCancel = function()
	{
		// Cancel any outstanding requests related to the navigation event.
		console.log("Navigation got onCancel.");
	}
}

function applyFeedSelectionPage(document)
{
	console.log("applyFeedSelectionPage() document: " + document);

	// add behavior by adding javascript file reference
	var head = document.rootElement.getElementByTagName("head");
	if (head)
	{
		var serviceJs = atv.parseXML('<script src="' + config.appletvBase + '/js/service.js" />').rootElement;
		serviceJs.removeFromParent();
		console.debug("adding service.js " + serviceJs);

		var children = head.childElements;
		children.unshift(serviceJs);

		for ( var i = 0; i < children.length; i++)
		{
			children[i].removeFromParent();
			head.appendChild(children[i]);
		}
	}

	flowManager.endMediaFlow();
	flowManager.endPurchaseFlow();
	console.log("purchase and media flow ended " + atv.localStorage[PURCHASE_PRODUCT_ID_KEY] + " " + atv.localStorage[MEDIA_META_REQUEST_PARAMS_KEY] + " " + atv.localStorage[MEDIA_META_KEY] + " " + atv.localStorage[SESSION_KEY]);

	return document;
}

// This is called from the feed_selection.xml
function loadFeed(mediaMeta)
{
	atv._debugDumpControllerStack();

	console.debug("showing new page..");
	var newPage = new atv.ProxyDocument();
	newPage.show();

	atv._debugDumpControllerStack();

	// store the media meta into the local storage for later use
	serviceClient.startMediaFlow(mediaMeta);
	atv.localStorage[OFFER_URI_AFTER_AUTH_KEY] = config.appletvBase + "/views/MediaServiceProxy?encode(MEDIA_META_REQUEST_PARAMS)";
	atv.localStorage[OFFER_PURCHASE_FUNCTION_KEY] = "restorePurchasePlay";
	atv.localStorage[OFFER_SWAP_PAGE_KEY] = "true";

	// load the media page
	serviceClient.loadMediaPage(function(document)
	{
		// media proxy returns a playback page, display
		var authorized = !serviceClient.isMediaResponseUnauthorized(document);
		// atv.localStorage.setItem(AUTHORIZED_KEY, (authorized) ? "true" : "false");
		if (authorized)
		{
			// if authorized, load the page
			console.debug("loadFeed() showing the media page");
			newPage.swapXML(document);
		}
		else
		{
			// if unauthorized, load the offer page
			console.debug("loadFeed() showing the offer page swap " + atv.localStorage[OFFER_SWAP_PAGE_KEY]);
			serviceClient.loadOfferPage(newPage);
		}
	});
}
// deprecated function name but it's still being generated by the gameday mule
var loadLiveGameVideoAsset = loadFeed;

function loadOfferPageAndPlay()
{
	var newPage = new atv.ProxyDocument();
	newPage.show();
	serviceClient.loadOfferPage(newPage);
}

function subscribeToMlbTv()
{
	console.debug("subscribeToMlbTv() enter");

	flowManager.endMediaFlow();

	// create new page where offer page will be loaded onto
	var newPage = new atv.ProxyDocument();
	newPage.show();

	// load the offer page onto the new page
	atv.localStorage[OFFER_URI_AFTER_AUTH_KEY] = config.appletvBase + "/views/Dialog?messageCode=AUTH_MESSAGE_CODE";
	atv.localStorage[OFFER_PURCHASE_FUNCTION_KEY] = "restoreAndPurchase";
	atv.localStorage[OFFER_SWAP_PAGE_KEY] = "false";
	serviceClient.loadOfferPage(newPage);
}

function ErrorToDebugString(error)
{
	var localizedDescription = error.userInfo["NSLocalizedDescription"];
	localizedDescription = localizedDescription ? localizedDescription : "Unknown";
	return error.domain + "(" + error.code + "): " + localizedDescription;
}

function linkManually()
{
	atv.loadURL(config.appletvBase + "/views/Login?urlToLoadAfterAuth=" + config.appletvBase + "/views/LinkProxy?request=encode(LINK_PROXY_REQUEST)");
}

function purchase(newPage)
{
	var observer = serviceClient.addPurchaseObserver(function(purchasedTransaction)
	{
		// PURCHASE success
		console.debug("purchase(): success purchased transactions " + purchasedTransaction);

		// link()
		serviceClient.doLink([ purchasedTransaction ], function()
		{
			console.debug("purchase(): link success");
			serviceClient.loadUserAuthorized(function()
			{
				console.debug("purchase(): load user authorized");
				newPage.loadXML(atvutils.makeOkDialog("MLB.TV Purchased", "You have successfully purchased MLB.TV."));
				atv.unloadPage();
			});
		}, function()
		{
			console.debug("purchase(): link error after purchase");
			newPage.cancel();
			atv.unloadPage();
			// do nothing -- go back to previous page
			// atv.loadAndSwapXML(atvutils.makeOkDialog("MLB.TV Error", "Failed to determine your MLB.TV subscription after purchase"));
		})
	}, function()
	{
		// PURCHASE error
		console.debug("purchase(): error ");
		newPage.cancel();
		atv.unloadPage();
	});

	newPage.onCancel = function()
	{
		atv.SKDefaultPaymentQueue.removeTransactionObserver(observer);
	};

	serviceClient.startPurchase();

}

function restoreAndPurchase(productId)
{
	atv._debugDumpControllerStack();

	// store product id into the cart
	atv.localStorage[PURCHASE_PRODUCT_ID_KEY] = productId;

	// create and show new page where dialogs will be loaded
	var newPage = new atv.ProxyDocument();
	newPage.show();

	atv._debugDumpControllerStack();

	var restoreObserver = serviceClient.addRestoreObserver(function(transactions)
	{
		// link()
		serviceClient.doLink(transactions, function()
		{
			// LINK success -- determine authorization thru feature service
			atv._debugDumpControllerStack();
			serviceClient.loadUserAuthorized(function()
			{
				console.debug("restoreAndPurchase() atv.localStorage[AUTHORIZED_KEY] " + atv.localStorage[AUTHORIZED_KEY]);
				if (atv.localStorage[AUTHORIZED_KEY] == "true")
				{
					console.debug("restoreAndPurchase() authorized or error page, showing Success page...");
					atv._debugDumpControllerStack();
					newPage.cancel();
					atv.loadAndSwapXML(atvutils.makeOkDialog("Success", "You are now authorized to watch MLB.TV.  Please select a game from Today's Games on the main menu."));
					console.debug("restoreAndPurchase() after showing Success page...");
					atv._debugDumpControllerStack();
				}
				else
				{
					// unauthorized, purchase
					console.debug("restoreAndPurchase() unauthorized, purchasing...");
					purchase(newPage);
					atv._debugDumpControllerStack();
				}
			});
		}, function()
		{
			// LINK error -- attempt to purchase now
			console.debug("restoreAndPurchase() link error - make the purchase ");
			purchase(newPage);
			atv._debugDumpControllerStack();
		});
	}, function(error)
	{
		newPage.cancel();
		if (error && error.code == -500)
		{
			atv.loadAndSwapXML(atvutils.makeOkDialog("MLB.TV Error", "Failed to determine your subscription."));
		}
		else
		{
			newPage.swapXML(document);
		}
		console.error("restoreAndPurchase() restore error - failed to restore " + document);
		atv._debugDumpControllerStack();
	});

	newPage.onCancel = function()
	{
		atv.SKDefaultPaymentQueue.removeTransactionObserver(restoreObserver);
	};

	// Restore completed transactions to get transactions purchased on other devices for this account.
	console.debug("restoreAndPurchase() Restoring completed transactions...");
	atv.SKDefaultPaymentQueue.restoreCompletedTransactions();
}

function purchaseAndPlay(newPage)
{
	atv._debugDumpControllerStack();

	var observer = serviceClient.addPurchaseObserver(function(purchasedTransaction)
	{
		// PURCHASE success
		console.debug("purchase(): success purchased transactions " + purchasedTransaction);
		atv._debugDumpControllerStack();

		// link()
		serviceClient.doLink([ purchasedTransaction ], function()
		{
			console.debug("purchase(): link success");
			atv._debugDumpControllerStack();

			serviceClient.loadMediaPage(function(mediaDoc)
			{
				console.debug("purchase() loadMediaPage");
				newPage.swapXML(mediaDoc);
				atv._debugDumpControllerStack();
			});

		}, function()
		{
			console.debug("purchase(): link error after purchase");
			newPage.swapXML(atvutils.makeOkDialog("MLB.TV Link Error", "Failed to link your MLB.TV account properly after purchase"));
			atv._debugDumpControllerStack();
		})

	}, function(transaction)
	{
		// PURCHASE error
		console.debug("purchase(): error " + (transaction) ? transaction.error : null);
		if (!transaction || transaction.error != 2)
		{
			newPage.swapXML(atvutils.makeOkDialog("MLB.TV Purchase Error", "The purchase failed.  Please try again at a later time."));
		}
		atv._debugDumpControllerStack();
	});

	newPage.onCancel = function()
	{
		atv.SKDefaultPaymentQueue.removeTransactionObserver(observer);
	};

	serviceClient.startPurchase();
}

function restorePurchasePlay(productId)
{
	// store product id into the cart
	atv.localStorage[PURCHASE_PRODUCT_ID_KEY] = productId;

	var newPage = new atv.ProxyDocument();
	newPage.show();

	atv._debugDumpControllerStack();

	var restoreObserver = serviceClient.addRestoreObserver(function(transactions)
	{
		// link()
		serviceClient.doLink(transactions, function()
		{
			// LINK success -- determine whether to purchase
			serviceClient.loadMediaPage(function(document)
			{
				atv._debugDumpControllerStack();
				var authorized = !serviceClient.isMediaResponseUnauthorized(document);
				console.debug("restorePurchasePlay() authorized " + authorized);
				atv.localStorage.setItem(AUTHORIZED_KEY, (authorized) ? "true" : "false");
				console.debug("restorePurchasePlay() atv.localStorage[AUTHORIZED] " + atv.localStorage[AUTHORIZED_KEY]);
				if (authorized)
				{
					// authorized or media playback error (session key problem, media unavailable, etc.), display the page
					console.debug("restorePurchasePlay() authorized or error page, showing media proxy page...");
					newPage.swapXML(atvutils.makeOkDialog("MLB.TV Subscriber", "You have purchased this content before and already have access to watch MLB.TV content.", "play();"));
					atv._debugDumpControllerStack();
					console.debug("restorePurchasePlay() after showing media proxy page...");
				}
				else
				{
					// unauthorized, purchase
					console.debug("restorePurchasePlay() unauthorized, purchasing...");
					purchaseAndPlay(newPage);
					atv._debugDumpControllerStack();
				}
			});
		}, function()
		{
			// LINK error -- attempt to purchase now
			console.debug("restorePurchasePlay() link error - make the purchase ");
			purchaseAndPlay(newPage);
		});
	}, function(error)
	{
		if (error && error.code == -500)
		{
			newPage.swapXML(atvutils.makeOkDialog("MLB.TV Error", "Failed to determine your subscription."));
		}
		else
		{
			newPage.swapXML(document);
		}
	});

	newPage.onCancel = function()
	{
		atv.SKDefaultPaymentQueue.removeTransactionObserver(restoreObserver);
	};

	// Restore completed transactions to get transactions purchased on other devices for this account.
	console.debug("restorePurchasePlay() Restoring completed transactions...");
	atv.SKDefaultPaymentQueue.restoreCompletedTransactions();
}

function play(newPage)
{
	serviceClient.loadMediaPage(function(document)
	{
		console.debug("play() show the media proxy page..." + newPage);
		atv._debugDumpControllerStack();
		if (newPage)
		{
			newPage.swapXML(document);
			atv.unloadPage();
		}
		else
		{
			atv.loadAndSwapXML(document);
		}
		atv._debugDumpControllerStack();
		console.debug("play() after showing media proxy page...");
	});
}

function restoreManually()
{
	console.debug("restore receipts from iTunes account manually");

	var newPage = new atv.ProxyDocument();
	newPage.show();

	var restoreObserver = serviceClient.addRestoreObserver(function(transactions)
	{
		// restore transaction callback
		console.log("restoreManually() transactions " + transactions);
		if (transactions)
		{
			console.debug("restoreManually() successfully restored receipts from iTunes account manually");
			serviceClient.doLink(transactions, function()
			{
				console.debug("restoreManually() link success callback - determine authorization from feature service");
				serviceClient.loadUserAuthorized(function()
				{
					newPage.loadXML(atvutils.makeOkDialog("Restore Successful", "Your subscription has been restored."));
				});
				console.debug("restoreManually() link service success");
			}, function()
			{
				console.debug("restoreManually() link service error");
			});
		}
		else
		{
			console.debug("restoreManually() missing transaction - determine authorization from feature service");
			newPage.loadXML(atvutils.makeOkDialog("Not an MLB.TV subscriber", "You have not purchased a MLB.TV subscription on AppleTV."));
		}
	}, function(error)
	{
		// restore error callback
		console.debug("restoreManually() error attempting to restore receipts from iTunes account manually");

		if (!error || error.code != -500)
		{
			newPage.cancel();
		}
		else
		{
			newPage.loadXML(atvutils.makeOkDialog("Restore Unsuccessful", "An error occurred attempting to restore your MLB.TV account."));
		}
	});

	newPage.onCancel = function()
	{
		atv.SKDefaultPaymentQueue.removeTransactionObserver(restoreObserver);
	};

	// Restore completed transactions to get transactions purchased on other devices for this account.
	console.debug("restoreManually() Restoring completed transactions...");
	atv.SKDefaultPaymentQueue.restoreCompletedTransactions();
}

function restoreOnOffer()
{
	console.debug("restoreOnOffer() restore receipts from iTunes account manually");

	var newPage = new atv.ProxyDocument();
	newPage.show();

	var restoreObserver = serviceClient.addRestoreObserver(function(transactions)
	{
		// restore success callback
		console.log("restoreManually() transaction " + transactions);
		if (transactions)
		{
			console.debug("successfully restored receipts from iTunes account manually");
			serviceClient.doLink(transactions, function()
			{
				// link success callback
				if (flowManager.inMediaFlow())
				{
					console.debug("restoreOnOffer() in media flow, playing media");
					play(newPage);
				}
				else
				{
					console.debug("restoreOnOffer() NOT in media flow, display info message");
					serviceClient.loadUserAuthorized(function()
					{
						console.debug("restoreOnOffer(): load user authorized");
						newPage.swapXML(atvutils.makeOkDialog("Restore Successful", "Your subscription has been restored."));
						atv.unloadPage();
					});
				}
			}, function()
			{
				// link error callback
				newPage.loadXML(atvutils.makeOkDialog("Restore Unsuccessful", "There was a problem restoring your iTunes account. Please try again at a later time."));
			});
		}
		else
		{
			newPage.loadXML(atvutils.makeOkDialog("Not an MLB.TV subscriber", "You have not purchased a MLB.TV subscription on AppleTV."));
		}
	}, function(error)
	{
		// restore error callback
		console.debug("error attempting to restore receipts from iTunes account manually");

		if (!error || error.code != -500)
		{
			newPage.cancel();
		}
		else
		{
			newPage.loadXML(atvutils.makeOkDialog("Restore Unsuccessful", "An error occurred attempting to restore your MLB.TV account."));
		}
	});

	newPage.onCancel = function()
	{
		atv.SKDefaultPaymentQueue.removeTransactionObserver(restoreObserver);
	};

	// Restore completed transactions to get transactions purchased on other devices for this account.
	console.debug("restoreManually() Restoring completed transactions...");
	atv.SKDefaultPaymentQueue.restoreCompletedTransactions();
}

function applySettingsPage(document)
{
	// update logo image resolution
	updateImageResolution(document);

	// show scores menu item
	var showScores = atv.localStorage[SHOW_SCORES_KEY];
	var menuItem = document.getElementById("showScoresMenuItem");
	if (menuItem)
	{
		var label = menuItem.getElementByName('rightLabel');
		label.textContent = (!showScores || showScores == "true") ? "Show" : "Hide";
		console.debug("label.textContent: " + label.textContent);
	}

	// menu items
	var parent = document.rootElement.getElementByTagName("items");
	var signInMenu = document.getElementById("signInOutMenuItem");
	var restoreMenu = document.getElementById("restoreSub");
	var linkAcctMenu = document.getElementById("linkAcct");
	var showScoresMenu = document.getElementById("showScoresMenuItem");
	console.debug("applySettingsPage() parent " + parent);

	// restore menu item
	var showRestoreMenu = atv.localStorage[IOS_IDENTITY_POINT_KEY] == null && atv.localStorage[IOS_FINGERPRINT_KEY] == null;
	console.debug("applySettingsPage() showRestoreMenu " + showRestoreMenu + " restoreMenu " + restoreMenu + " atv.localStorage[IOS_IDENTITY_POINT_KEY] " + atv.localStorage[IOS_IDENTITY_POINT_KEY]);
	if (showRestoreMenu)
	{
		if (!restoreMenu)
		{
			var parent = document.rootElement.getElementByTagName("items");
			console.debug("applySettingsPage() parent " + parent);
			if (parent && parent.childElements)
			{
				restoreMenu = atv.parseXML('<oneLineMenuItem id="restoreSub" accessibilityLabel="Restore Subscription" onSelect="restoreManually();"><label>Restore Subscription</label></oneLineMenuItem>').rootElement;
				console.debug("applySettingsPage() adding restoreMenu " + restoreMenu);
				restoreMenu.removeFromParent();
				parent.appendChild(restoreMenu);
				if (linkAcctMenu)
				{
					linkAcctMenu.removeFromParent();
					parent.appendChild(linkAcctMenu);
				}
				showScoresMenu.removeFromParent();
				parent.appendChild(showScoresMenu);
			}
		}
	}
	else if (restoreMenu)
	{
		restoreMenu.removeFromParent();
	}

	// link accounts menu item
	var showLinkMenu = atv.localStorage[EMAIL_LINKED_KEY] != "true" && atv.localStorage[IOS_IDENTITY_POINT_KEY] != null && atv.localStorage[IOS_FINGERPRINT_KEY] != null;
	console.debug("applySettingsPage() atv.localStorage[EMAIL_LINKED_KEY] " + atv.localStorage[EMAIL_LINKED_KEY] + " showLinkMenu " + showLinkMenu + " atv.localStorage[IOS_IDENTITY_POINT_KEY] " + atv.localStorage[IOS_IDENTITY_POINT_KEY] + " linkAcctMenu " + linkAcctMenu);
	if (showLinkMenu)
	{
		// add the menu if it's not there
		if (!linkAcctMenu)
		{
			if (parent && parent.childElements)
			{
				console.debug("applySettingsPage() before parent.childElements " + parent.childElements);

				linkAcctMenu = atv.parseXML('<oneLineMenuItem id="linkAcct" accessibilityLabel="Link MLB.TV Account" onSelect="linkManually();"><label>Link Account</label></oneLineMenuItem>').rootElement;
				linkAcctMenu.removeFromParent();

				showScoresMenu.removeFromParent();
				parent.appendChild(linkAcctMenu);
				parent.appendChild(showScoresMenu);

				console.debug("applySettingsPage() after parent.childElements " + parent.childElements);
			}
		}

		if (signInMenu)
		{
			signInMenu.removeFromParent();
		}
	}
	else
	{
		// remove the menu
		if (linkAcctMenu)
		{
			linkAcctMenu.removeFromParent();
		}

		if (!signInMenu)
		{
			signInMenu = atv.parseXML('\
				<signInSignOutMenuItem id=\"signInOutMenuItem\" accessibilityLabel=\"Sign out\" signOutExitsApp=\"false\">\
					<signInPageURL>' + config.appletvBase + '/views/Login</signInPageURL>\
					<confirmation>\
						<title>Are You Sure?</title>\
						<description>The account %@ will be logged out of the service.</description>\
					</confirmation>\
				</signInSignOutMenuItem>').rootElement;
			signInMenu.removeFromParent();

			parent.appendChild(signInMenu);
			if (showRestoreMenu && restoreMenu)
			{
				parent.appendChild(restoreMenu);
			}
			showScoresMenu.removeFromParent();
			parent.appendChild(showScoresMenu);
		}
	}

	return document;
}

function applyRecapsPage(responseXML)
{
	// update logo image resolution
	updateImageResolution(responseXML);

	return responseXML;
}

function applyTeamsPage(responseXML)
{
	try
	{
		// update logo image resolution
		updateImageResolution(responseXML);

		var favTeamIds = getFavoriteTeamIds();

		console.debug('sorting teams.. ' + responseXML.rootElement);
		var gridList = responseXML.rootElement.getElementByName("body").getElementByName("scroller").getElementByName("items").getElementsByName("grid");
		var gridIndex = 0;
		for (; gridIndex < gridList.length; gridIndex++)
		{
			// function to sort by title
			function sortByTitle(poster1, poster2)
			{
				var favTeamIds = (atv.localStorage[FAVORITE_TEAMS_KEY]) ? atv.localStorage[FAVORITE_TEAMS_KEY] : [];

				var team1Id = poster1.getAttribute("id");
				var is1Fav = favTeamIds.indexOf(team1Id) >= 0;
				var title1 = poster1.getElementByName("title").textContent;
				title1 = (is1Fav) ? title1.toUpperCase() : title1.toLowerCase();

				var team2Id = poster2.getAttribute("id");
				var is2Fav = favTeamIds.indexOf(team2Id) >= 0;
				var title2 = poster2.getElementByName("title").textContent;
				title2 = (is2Fav) ? title2.toUpperCase() : title2.toLowerCase();

				var result = (title1 > title2) ? 1 : -1;
				// console.debug('result: ' + title1 + ">" + title2 + " = " + result);

				return result;
			}

			// parent
			var parent = gridList[gridIndex].getElementByName("items");

			// sort the posters
			var orderedPosterList = parent.childElements.sort(sortByTitle);

			// re-append the child
			var index = 0;
			for (; index < orderedPosterList.length; index++)
			{
				var poster = orderedPosterList[index];
				var teamId = poster.getAttribute("id");
				poster.removeFromParent();
				parent.appendChild(poster);

				var isFav = favTeamIds.indexOf(teamId) >= 0;

				var imageElem = poster.getElementByName("image");
				if (isFav && imageElem.textContent.indexOf("-fav.png") < 0)
				{
					imageElem.textContent = imageElem.textContent.replace(".png", "-fav.png");
				}
				else if (!isFav && imageElem.textContent.indexOf("-fav.png") > 0)
				{
					imageElem.textContent = imageElem.textContent.replace("-fav.png", ".png");
				}
			}

		}

		console.debug('teams are sorted and favorite team logos in place');
		return responseXML;

	}
	catch (error)
	{
		console.debug("Unable to sort the teams: " + error);
	}
}

function applyGamesPage(responseXML)
{
	console.debug("applyeGamesPage() responseXML = " + responseXML);
	try
	{
		// determine if favorite team
		var listElem = responseXML.rootElement.getElementByName("body").getElementByName("listByNavigation");
		var pageId = listElem.getAttribute("id");
		var teamId = pageId.substring(pageId.lastIndexOf(".") + 1);
		var isFavorite = getFavoriteTeamIds().indexOf(teamId) > -1;
		console.debug("applyGamesPage() teamId: " + teamId + " isFavorite: " + isFavorite);

		var favButton = responseXML.getElementById("favoriteButton");
		var favoriteButtonLabel = favButton.getElementByName("label");
		console.debug("favoriteButtonLabel=" + favoriteButtonLabel);
		if (isFavorite)
		{
			var newLabel = "Remove from Favorites";
			favButton.setAttribute('accessibilityLabel', newLabel);
			favoriteButtonLabel.textContent = newLabel;
			console.debug("changing to Remove From Favorites favoriteButtonLabel.textContent=" + favoriteButtonLabel.textContent);
		}
		else
		{
			var newLabel = "Add to Favorites";
			favButton.setAttribute('accessibilityLabel', newLabel);
			favoriteButtonLabel.textContent = newLabel;
			console.debug("changing to Add to Favorites favoriteButtonLabel.textContent=" + favoriteButtonLabel.textContent);
		}

		return responseXML;
	}
	catch (error)
	{
		console.log("Unable to apply changes to the games page: " + error);
	}
}

/**
 * Should be called by the onRefresh event, I am also calling this from the handleNavigate function below to share code. Essentially this function: 1. Loads the current Navigation file 2. Pulls the
 * menu items out of the loaded file and dumps them into the current file 3. Checks to see if an onRefresh AJAX call is already being made and if so does nothing.
 * 
 * 
 * @params string url - url to be loaded
 */
function handleGamesPageRefresh(urlParam)
{
	var url = atv.localStorage[GAMES_PAGE_REFRESH_URL_KEY];
	if (url == null)
	{
		url = urlParam;
	}
	
	console.log("Handle refresh event called");

	console.log("We are about to check the value of Ajax.currentlyRefreshing: --> " + Ajax.currentlyRefreshing);
	// check the Ajax object and see if a refresh event is happening
	if (!Ajax.currentlyRefreshing)
	{

		// if a refresh event is not happening, make another one.
		console.log("About to make the AJAX call. --> ")

		var refresh = new Ajax({
			"url" : url,
			"refresh" : true,
			"success" : function()
			{
				var req = arguments[0];

				// update the content page
				var newDocument = atv.parseXML(req.responseText), // Instead of using the responseXML you should parse the responseText. I know it seems redundent but trust me.
				menu = document.rootElement.getElementByTagName('menu'), oldMenuSections = menu.getElementByTagName('sections'), newMenuSections = newDocument.rootElement.getElementByTagName('sections'), now = new Date();

				applyGamesPage(newDocument);

				newMenuSections.removeFromParent();
				menu.replaceChild(oldMenuSections, newMenuSections);

			}
		});

		console.debug("I have made the AJAX call. <--");
	}

	console.debug("I am on the other side of the if. <--");

}

/**
 * This function works very similar to the handleRefresh Essentially we are manually loading the data and updating the page in Javascript This means we have to handle a bit more of the magic, but we
 * also get to control what is updated and update more of the page, like headers, previews, etc.
 * 
 * In this example, I am: 1. Getting the new URL from the Navigation Item 2. Changing the onRefresh URL 3. Calling handleRefresh and passing the new url to let handleRefresh do the magic.
 */
function handleGamesPageNavigate(e)
{
	console.log("Handle navigate event called: " + e + ", e.navigationItemId = " + e.navigationItemId + " document = " + document);

	// get the url
	var url = document.getElementById(e.navigationItemId).getElementByTagName('url').textContent;
	atv.localStorage[GAMES_PAGE_REFRESH_URL_KEY] = url;

	handleGamesPageRefresh(url);

	e.onCancel = function()
	{
		// Cancel any outstanding requests related to the navigation event.
		console.log("Navigation got onCancel.");
	}
}

function getFavoriteTeamIds()
{
	var favTeamIds = atv.localStorage[FAVORITE_TEAMS_KEY];
	console.debug("favoriteTeamIds=" + favTeamIds);
	return (favTeamIds != null) ? favTeamIds : [];
}

function toggleFavoriteTeam(teamId)
{
	try
	{
		console.log("toggleFavoriteTeams " + teamId);
		var favoriteTeamIds = getFavoriteTeamIds();

		var isFavorite;
		if (favoriteTeamIds == null || favoriteTeamIds.length == 0)
		{
			// first favorite team
			favoriteTeamIds = [ teamId ];
			isFavorite = true;
		}
		else
		{
			var teamIdIndex = favoriteTeamIds.indexOf(teamId);
			if (teamIdIndex > -1)
			{
				// remove
				favoriteTeamIds.splice(teamIdIndex, 1);
				isFavorite = false;
			}
			else
			{
				// add
				favoriteTeamIds.push(teamId);
				isFavorite = true;
			}
		}

		atv.localStorage[FAVORITE_TEAMS_KEY] = favoriteTeamIds;

		var favoriteButton = document.getElementById("favoriteButton");
		if (favoriteButton)
		{
			var newLabel = (isFavorite) ? "Remove from Favorites" : "Add to Favorites";
			var label = favoriteButton.getElementByName("label");
			label.textContent = newLabel;
			console.debug("after label.textContent: " + label.textContent);
		}
	}
	catch (error)
	{
		console.error("Caught exception trying to toggle DOM element: " + error);
	}

	console.debug("toggleFavoriteTeam() end " + teamId);
}

function toggleScores(id)
{
	try
	{
		var menuItem = document.getElementById(id);
		if (menuItem)
		{
			var label = menuItem.getElementByName('rightLabel');
			if (label.textContent == "Show")
			{
				label.textContent = "Hide";
				atv.localStorage[SHOW_SCORES_KEY] = "false";
			}
			else
			{
				label.textContent = "Show";
				atv.localStorage[SHOW_SCORES_KEY] = "true";
			}

			console.debug("showScores: " + atv.localStorage[SHOW_SCORES_KEY]);
		}
	}
	catch (error)
	{
		console.error("Caught exception trying to toggle DOM element: " + error);
	}
}
console.log("~~~~~ MLB.TV main.js end");
HTTP/1.1 200 OK
Server: Oracle-iPlanet-Web-Server/7.0
Date: Wed, 08 Aug 2012 16:14:54 GMT
Content-type: text/html
Content-type: application/json
Transfer-encoding: chunked

11c
var config = {env: '_proddc2split', imageBase: 'http://mlb.mlb.com/mlb/images/flagstaff', appletvBase: 'http://lws.mlb.com/appletvv2', bamServiceBase: 'https://mlb-ws.mlb.com', gamedayBase: 'http://gdx.mlb.com', mlbBase: 'http://www.mlb.com', secureMlbBase: 'https://secure.mlb.com'};
0

HTTP/1.1 200 OK
Server: Oracle-iPlanet-Web-Server/7.0
Date: Wed, 08 Aug 2012 16:14:54 GMT
Content-type: application/x-javascript
Last-modified: Mon, 23 Apr 2012 14:41:39 GMT
Content-length: 17889
Etag: "45e1-4f956a23"
Accept-ranges: bytes

// ***************************************************

var EMAIL_IDENTITY_POINT_KEY = "email.identityPointId";
var EMAIL_FINGERPRINT_KEY = "email.fingerprint";

// atv.Element extensions
atv.Element.prototype.getElementsByTagName = function(tagName) {
	return this.ownerDocument.evaluateXPath("descendant::" + tagName, this);
}

// Extend atv.ProxyDocument to load errors from a message and description.
atv.ProxyDocument.prototype.loadError = function(message, description) {
	var doc = atvutils.makeErrorDocument(message, description);
	this.loadXML(doc);
}

// Extend atv.ProxyDocument to load errors from a message and description.
atv.ProxyDocument.prototype.swapXML = function(xml) {
	var me = this;
    try 
    {
        me.loadXML(xml, function(success) {
            if ( success ) {
                atv.unloadPage();
            } else {
                console.log("swapXML failed to load " + xml);
                me.loadXML(atvutils.siteUnavailableError(), function(success) {
                	console.log("swapXML site unavailable error load success " + success);
                    if ( success ) {
                        atv.unloadPage();
                    }
                });
            }
        });
    } 
    catch (e) 
    {
        console.error("swapXML caught exception while loading " + xml + ". " + e);
        me.loadXML(atvutils.siteUnavailableError(), function(success) {
            if ( success ) {
                atv.unloadPage();
            }
        });
    }
}

// atv.Document extensions
atv.Document.prototype.getElementById = function(id) {
 var elements = this.evaluateXPath("//*[@id='" + id + "']", this);
 if ( elements && elements.length > 0 ) {
     return elements[0];
 }
 return undefined;
}

// atv.Element extensions
atv.Element.prototype.getElementsByTagName = function(tagName) {
 return this.ownerDocument.evaluateXPath("descendant::" + tagName, this);
}

atv.Element.prototype.getElementByTagName = function(tagName) {
 var elements = this.getElementsByTagName(tagName);
 if ( elements && elements.length > 0 ) {
     return elements[0];
 }
 return undefined;
}

// ATVUtils - a JavaScript helper library for Apple TV
ATVUtils = function() {};

ATVUtils.prototype.onGenerateRequest = function (request) {
	
    console.log('atvutils.js atv.onGenerateRequest() oldUrl: ' + request.url);

    console.debug("atvutils.js atv.onGenerateRequest() atv.localStorage[mediaMetaRequestParams] " + atv.localStorage[MEDIA_META_REQUEST_PARAMS_KEY]);
	if ( request.url.indexOf("encode(MEDIA_META_REQUEST_PARAMS)") > -1 && atv.localStorage[MEDIA_META_REQUEST_PARAMS_KEY])
	{
		request.url = request.url.replace("encode(MEDIA_META_REQUEST_PARAMS)", encodeURIComponent(atv.localStorage[MEDIA_META_REQUEST_PARAMS_KEY]));
	}
	console.debug("atvutils.js atv.onGenerateRequest() atv.localStorage[mediaMetaRequestParams] " + atv.localStorage[MEDIA_META_REQUEST_PARAMS_KEY]);
	if ( request.url.indexOf("MEDIA_META_REQUEST_PARAMS") > -1 && atv.localStorage[MEDIA_META_REQUEST_PARAMS_KEY])
	{
		request.url = request.url.replace("MEDIA_META_REQUEST_PARAMS", atv.localStorage[MEDIA_META_REQUEST_PARAMS_KEY]);
	}
	if ( request.url.indexOf("IPID") > -1 && atv.localStorage[BEST_IDENTITY_POINT_KEY])
	{
		request.url = request.url.replace("IPID", atv.localStorage[BEST_IDENTITY_POINT_KEY]);
	}
	if ( request.url.indexOf("FINGERPRINT") > -1 && atv.localStorage[BEST_FINGERPRINT_KEY])
	{
		request.url = request.url.replace("FINGERPRINT", encodeURIComponent(atv.localStorage[BEST_FINGERPRINT_KEY]));
	}
	if ( request.url.indexOf("SESSION_KEY") > -1 )
	{
		var sessionKey = atv.localStorage[SESSION_KEY]; 
		console.debug("atvutils.js atv.onGenerateRequest sessionKey=" + sessionKey);
		request.url = request.url.replace("SESSION_KEY", (sessionKey) ? encodeURIComponent(sessionKey) : "");
	}
	if ( request.url.indexOf("encode(LINK_PROXY_REQUEST)") > -1 && atv.localStorage[EMAIL_IDENTITY_POINT_KEY] != null && atv.localStorage[EMAIL_FINGERPRINT_KEY] != null)
	{
		var linkRequest = serviceClient.createLinkRequest();
		console.debug("atvutils.js atv.onGenerateRequest() linkRequest " + linkRequest);
		if (linkRequest != null)
		{
			// WARNING: encode twice because the xs:uri type does not suppot curly brackets so encode an extra time. The LinkProxy class will be responsible for url unencoding the json 
			request.url = request.url.replace("encode(LINK_PROXY_REQUEST)", encodeURIComponent(encodeURIComponent(JSON.stringify(linkRequest))));
		}
	}
	if ( request.url.indexOf("$$showScores") > -1 )
	{
		var showScores = atv.localStorage[SHOW_SCORES_KEY]; 
		console.log("showScores=" + showScores);
		request.url = request.url.replace("$$showScores", (showScores) ? showScores : "true");
	}
	var authorized = atv.localStorage[AUTHORIZED_KEY]; 
	if ( request.url.indexOf("AUTH_MESSAGE_CODE") > -1 && authorized != null)
	{
		console.debug("atvutils.js AUTH_MESSAGE_CODE atv.onGenerateRequest() authorized " + authorized);
//		request.url = request.url.replace("AUTH_MESSAGE_CODE", ((authorized == "true") ? "ALREADY_PURCHASED" : "NOT_AUHTORIZED"));
	}

	console.log('atvutils.js atv.onGenerateRequest() newUrl: ' + request.url);
}


ATVUtils.prototype.makeRequest = function(url, method, headers, body, callback) {
    if ( !url ) {
        throw "loadURL requires a url argument";
    }

	var method = method || "GET";
	var	headers = headers || {};
	var body = body || "";
	
    var xhr = new XMLHttpRequest();
    xhr.onreadystatechange = function() {
        try {
            if (xhr.readyState == 4 ) {
                if ( xhr.status == 200) {
                	console.log("makeRequest() getAllResponseHeaders(): " + xhr.getAllResponseHeaders());
                    callback(xhr.responseXML, xhr.responseText);
                } else {
                    console.log("makeRequest() received HTTP status " + xhr.status + " for " + url);
                    callback(xhr.responseXML, xhr.responseText);
                }
            }
        } catch (e) {
            console.error('makeRequest caught exception while processing request for ' + url + '. Aborting. Exception: ' + e);
            xhr.abort();
            callback(null, null);
        }
    }
    
    var request = {url: url};
    this.onGenerateRequest(request);
    
    console.debug("makeRequest() url " + request.url);

    xhr.open(method, request.url, true);

    for(var key in headers) {
        xhr.setRequestHeader(key, headers[key]);
    }

    console.debug("sending body in the send() method");
    xhr.send(body);
    return xhr;
}

ATVUtils.prototype.getRequest = function(url, callback) {
	return this.makeRequest(url, "GET", null, null, callback);
}

ATVUtils.prototype.postRequest = function(url, body, callback) {
	return this.makeRequest(url, "POST", null, body, callback);
}

ATVUtils.prototype.makeErrorDocument = function(message, description) {
    if ( !message ) {
        message = "";
    }
    if ( !description ) {
        description = "";
    }

    var errorXML = '<?xml version="1.0" encoding="UTF-8"?> \
    <atv> \
        <body> \
            <dialog id="com.bamnetworks.errorDialog"> \
                <title><![CDATA[' + message + ']]></title> \
                <description><![CDATA[' + description + ']]></description> \
            </dialog> \
        </body> \
    </atv>';

    return atv.parseXML(errorXML);
}

ATVUtils.prototype.makeOkDialog = function(message, description, onSelect) {
	if ( !message ) {
		message = "";
	}
	if ( !description ) {
		description = "";
	}
	
	if (!onSelect)
	{
		onSelect = "atv.unloadPage();";
	}
	
	var errorXML = '<?xml version="1.0" encoding="UTF-8"?> \
		<atv> \
		<head> \
			<script src="'+config.appletvBase+'/views/config.js" />\
			<script src="'+config.appletvBase+'/js/atvutils.js" />\
			<script src="'+config.appletvBase+'/js/service.js" />\
			<script src="'+config.appletvBase+'/js/main.js" />\
		</head>\
		<body> \
		<optionDialog id="com.bamnetowrks.optionDialog"> \
		<header><simpleHeader><title>' + message + '</title></simpleHeader></header> \
		<description><![CDATA[' + description + ']]></description> \
		<menu><sections><menuSection><items> \
			<oneLineMenuItem id="lineMenu" accessibilityLabel="OK" onSelect="' + onSelect + '">\
				<label>OK</label>\
			</oneLineMenuItem>\
		</items></menuSection></sections></menu>\
		</optionDialog> \
		</body> \
		</atv>';
	
	var doc = atv.parseXML(errorXML);
	return doc;
}

ATVUtils.prototype.printObject = function(object) {
	for (property in object)
	{
		console.log(property + ": " + object[property]);
	}
}


ATVUtils.prototype.siteUnavailableError = function() {
    // TODO: localize
	console.debug("siteUnavailableError()");
    return this.makeOkDialog("MLB.TV error", "MLB.TV is currently unavailable. Please try again later.");
}

ATVUtils.prototype.loadError = function(message, description) {
    atv.loadXML(this.makeErrorDocument(message, description));
}

ATVUtils.prototype.loadAndSwapError = function(message, description) {
    atv.loadAndSwapXML(this.makeErrorDocument(message, description));
}

ATVUtils.prototype.loadURLInternal = function(url, method, headers, body, loader) {
    var me = this;
    var xhr;
    var proxy = new atv.ProxyDocument;
    proxy.show();
    proxy.onCancel = function() {
        if ( xhr ) {
            xhr.abort();
        }
    };
    
    xhr = me.makeRequest(url, method, headers, body, function(xml, xhr) {
        try {
        	console.log("loadURLInternal() proxy=" + proxy + " xml=" + xml + " xhr=" + xhr);
            loader(proxy, xml, xhr);
        } catch(e) {
            console.error("Caught exception loading " + url + ". " + e);
            loader(proxy, me.siteUnavailableError(), xhr);
        }
    });
}

ATVUtils.prototype.loadGet = function(url, loader) {
	this.loadURLInternal(url, "GET", null, null, loader);
}

// TODO:: change to a url string or hash
ATVUtils.prototype.loadURL = function(url, method, headers, body, processXML) {
    var me = this;
    this.loadURLInternal(url, method, headers, body, function(proxy, xml) {
        if(typeof(processXML) == "function") processXML.call(this, xml);
		try {
            proxy.loadXML(xml, function(success) {
                if ( !success ) {
                    console.log("loadURL failed to load " + url);
                    proxy.loadXML(me.siteUnavailableError());
                }
            });
        } catch (e) {
            console.log("loadURL caught exception while loading " + url + ". " + e);
            proxy.loadXML(me.siteUnavailableError());
        }
    });
}

ATVUtils.prototype.swapXML = function(proxy, xml) {
	var me = this;
    try {
        proxy.loadXML(xml, function(success) {
            if ( success ) {
                atv.unloadPage();
            } else {
                console.log("swapXML failed to load " + xml);
                proxy.loadXML(me.siteUnavailableError(), function(success) {
                    if ( success ) {
                        atv.unloadPage();
                    }
                });
            }
        });
    } catch (e) {
        console.error("swapXML caught exception while loading " + xml + ". " + e);
        proxy.loadXML(me.siteUnavailableError(), function(success) {
            if ( success ) {
                atv.unloadPage();
            }
        });
    }
}

// TODO:: change to a url string or hash
// loadAndSwapURL can only be called from page-level JavaScript of the page that wants to be swapped out.
ATVUtils.prototype.loadAndSwapURL = function(url, method, headers, body, processXML) {
    var me = this;
    this.loadURLInternal(url, method, headers, body, function(proxy, xml) { 
	    if(typeof(processXML) == "function") processXML.call(this, xml);
        me.swapXML(proxy, xml);
    });
}

/** Load URL and apply changes to the xml */
ATVUtils.prototype.loadAndApplyURL = function(url, processXML) {
    var me = this;
	this.loadURLInternal(url, 'GET', null, null, function(proxy, xml, xhr) {
		var xmlToLoad = xml;
        if(typeof(processXML) == "function") xmlToLoad = processXML.call(this, xml);
        if (!xmlToLoad) xmlToLoad = xml;
		try {
            proxy.loadXML(xmlToLoad, function(success) {
                if ( !success ) {
                    console.log("loadURL failed to load " + url);
                    proxy.loadXML(me.siteUnavailableError());
                }
            });
        } catch (e) {
            console.log("loadURL caught exception while loading " + url + ". " + e);
            proxy.loadXML(me.siteUnavailableError());
        }
    });
}

var atvutils = new ATVUtils;

console.log("atvutils initialized " + atvutils);

/**
 * This is an AXR handler. It handles most of tediousness of the XHR request and keeps track of onRefresh XHR calls so that we don't end up with multiple page refresh calls.
 * 
 * You can see how I call it on the handleRefresh function below.
 * 
 * 
 * @params object (hash) $h
 * @params string $h.url - url to be loaded
 * @params string $h.method - "GET", "POST", "PUT", "DELTE"
 * @params bool $h.type - false = "Sync" or true = "Async" (You should always use true)
 * @params func $h.success - Gets called on readyState 4 & status 200
 * @params func $h.failure - Gets called on readyState 4 & status != 200
 * @params func $h.callback - Gets called after the success and failure on readyState 4
 * @params string $h.data - data to be sent to the server
 * @params bool $h.refresh - Is this a call from the onRefresh event.
 */
function Ajax($h)
{
	var me = this;
	$h = $h || {}

	// Setup properties
	this.url = $h.url || false;
	this.method = $h.method || "GET";
	this.type = ($h.type === false) ? false : true;
	this.success = $h.success || null;
	this.failure = $h.failure || null;
	this.data = $h.data || null;
	this.complete = $h.complete || null;
	this.refresh = $h.refresh || false;

	if (!this.url)
	{
		console.error('\nAjax Object requires a url to be passed in: e.g. { "url": "some string" }\n')
		return undefined;
	}

	this.createRequest();
	this.req.onreadystatechange = this.stateChange;
	this.req.object = this;
	this.open();
	this.send();
}

Ajax.currentlyRefreshing = false;

Ajax.prototype.stateChange = function()
{
	var me = this.object;

	// console.log("this in stateChage is: "+ this);
	// console.log("object in stateChange is: "+ this.object);

	switch (this.readyState)
	{

	case 1:
		if (typeof (me.connection) === "function")
			me.connection(this, me);
		break;
	case 2:
		if (typeof (me.received) === "function")
			me.received(this, me);
		break;
	case 3:
		if (typeof (me.processing) === "function")
			me.processing(this, me);
		break;
	case 4:
		if (this.status == "200")
		{
			if (typeof (me.success) === "function")
				me.success(this, me);
		}
		else
		{
			if (typeof (me.failure) === "function")
				me.failure(this.status, this, me);
		}
		if (typeof (me.complete) === "function")
			me.complete(this, me);
		if (me.refresh)
			Ajax.currentlyRefreshing = false;
		break;
	default:
		console.log("I don't think I should be here.");
		break;
	}
}

Ajax.prototype.createRequest = function()
{
	try
	{
		this.req = new XMLHttpRequest();
		if (this.refresh)
			Ajax.currentlyRefreshing = true;
	}
	catch (error)
	{
		alert("The request could not be created: </br>" + error);
		console.error("failed to create request: " + error);
	}

}

Ajax.prototype.open = function()
{
	try
	{
		this.req.open(this.method, this.url, this.type);
	}
	catch (error)
	{
		console.error("failed to open request: " + error);
	}
}

Ajax.prototype.send = function()
{
	var data = this.data || null;
	try
	{
		this.req.send(data);
	}
	catch (error)
	{
		console.error("failed to send request: " + error);
	}
}


// End ATVUtils
// ***************************************************



FlowManager = function()
{
};

FlowManager.prototype.setTargetPage = function(pageId)
{
	atv.localStorage[TARGET_PAGEID_KEY] = pageId;
	console.debug("flowManager.setTargetPage() atv.localStorage[TARGET_PAGEID_KEY] " + atv.localStorage[TARGET_PAGEID_KEY]);
}

FlowManager.prototype.clearTargetPage = function()
{
	atv.localStorage.removeItem(TARGET_PAGEID_KEY);
	console.debug("flowManager.clearTargetPage() atv.localStorage[TARGET_PAGEID_KEY] " + atv.localStorage[TARGET_PAGEID_KEY]);
}

FlowManager.prototype.startPurchaseFlow = function()
{
	atv.localStorage[PURCHASE_FLOW_KEY] = "true";
	console.debug("flowManager.startPurchaseFlow() atv.localStorage[PURCHASE_FLOW_KEY] " + atv.localStorage[PURCHASE_FLOW_KEY]);
}

FlowManager.prototype.endPurchaseFlow = function()
{
	atv.localStorage.removeItem(PURCHASE_FLOW_KEY);
	atv.localStorage.removeItem(PURCHASE_PRODUCT_ID_KEY);
	console.debug("flowManager.endPurchaseFlow() atv.localStorage[PURCHASE_FLOW_KEY] " + atv.localStorage[PURCHASE_FLOW_KEY]);
}

FlowManager.prototype.inPurchaseFlow = function()
{
	console.debug("flowManager.inPurchaseFlow() atv.localStorage[PURCHASE_FLOW_KEY] " + atv.localStorage[PURCHASE_FLOW_KEY]);
	return (atv.localStorage[PURCHASE_FLOW_KEY] == "true");
}

FlowManager.prototype.inPurchaseFlow = function()
{
	console.debug("flowManager.inPurchaseFlow() atv.localStorage[PURCHASE_FLOW_KEY] " + atv.localStorage[PURCHASE_FLOW_KEY]);
	return (atv.localStorage[PURCHASE_FLOW_KEY] == "true");
}

FlowManager.prototype.inMediaFlow = function()
{
	console.debug("flowManager.inMediaFlow() atv.localStorage[MEDIA_META_KEY] " + atv.localStorage[MEDIA_META_KEY]);
	return atv.localStorage.getItem(MEDIA_META_KEY) != null && atv.localStorage.getItem(MEDIA_META_REQUEST_PARAMS_KEY) != null;
}

FlowManager.prototype.endMediaFlow = function()
{
	atv.localStorage.removeItem(MEDIA_META_KEY);
	atv.localStorage.removeItem(MEDIA_META_REQUEST_PARAMS_KEY);
	console.debug("flowManager.endMediaFlow() atv.localStorage[MEDIA_META_KEY] " + atv.localStorage[MEDIA_META_KEY]);
}

var flowManager = new FlowManager;

console.debug("atvutils.js flowManager created");HTTP/1.1 200 OK
Server: Oracle-iPlanet-Web-Server/7.0
Date: Wed, 08 Aug 2012 16:14:55 GMT
Content-type: application/x-javascript
Last-modified: Thu, 14 Jun 2012 17:05:42 GMT
Content-length: 27397
Etag: "6b05-4fda19e6"
Accept-ranges: bytes

console.log("~~~~~ MLB.TV service.js START");

var PRODUCT_LIST = {
	"IOSATBAT2012GDATVMON" : {
		"image" : "monthlyPremium.jpg",
		"period" : "month"
	}, 
	"IOSMLBTVYEARLY2012" : {
		"image" : "monthlyPremium.jpg",
		"period" : "year"
	}
};

var MLBTV_FEATURE_CONTEXT = "MLBTV2012.INAPPPURCHASE";
var MLBTV_FEATURE_NAME = "MLBALL";

var IOS_IDENTITY_POINT_KEY = "ios.identityPointId";
var IOS_FINGERPRINT_KEY = "ios.fingerprint";
var USERNAME_KEY = "username";
var EMAIL_IDENTITY_POINT_KEY = "email.identityPointId";
var EMAIL_FINGERPRINT_KEY = "email.fingerprint";
var BEST_IDENTITY_POINT_KEY = "best.identityPointId";
var BEST_FINGERPRINT_KEY = "best.fingerprint";
var EMAIL_LINKED_KEY = "email.linked";
var AUTHORIZED_KEY = "authorized";
var SHOW_LINK_STATUS = "showLinkStatus";
var MEDIA_META_KEY = "media.meta";
var MEDIA_META_REQUEST_PARAMS_KEY = "media.meta.requestParams";
var OFFER_URI_AFTER_AUTH_KEY = "offer.uriToLoadAfterAuth";
var OFFER_PURCHASE_FUNCTION_KEY = "offer.purchaseFunction";
var OFFER_SWAP_PAGE_KEY = "offer.swapPage";

ServiceClient = function()
{
};

ServiceClient.prototype.login = function(username, password, success, error)
{
	var url = config.bamServiceBase + "/pubajaxws/services/IdentityPointService";
	var identifyBody = "<soapenv:Envelope xmlns:soapenv=\"http://schemas.xmlsoap.org/soap/envelope/\" xmlns:ns=\"http://services.bamnetworks.com/registration/types/1.0\"><soapenv:Body><ns:identityPoint_identify_request><ns:identification type=\"email-password\"> \
			<ns:email><ns:address>" + username + "</ns:address></ns:email> \
			<ns:password>" + password + "</ns:password> \
		</ns:identification></ns:identityPoint_identify_request></soapenv:Body></soapenv:Envelope>";
	var headers = {
		"SOAPAction" : "http://services.bamnetworks.com/registration/identityPoint/identify",
		"Content-Type" : "text/xml"
	};

	console.log('Trying to authenticate with ' + url);

	atvutils.makeRequest(url, "POST", headers, identifyBody, function(document)
	{
		var identityPointId, fingerprint;
		var statusCode = (document && document.rootElement) ? document.rootElement.childElements[1].getElementByName("identityPoint_identify_response").getElementByName("status").getElementByName("code").textContent : -1;
		console.log('status code is ' + statusCode);

		var errorMessage;
		if (statusCode == 1)
		{
			var identification = document.rootElement.childElements[1].getElementByName("identityPoint_identify_response").getElementByName("identification");
			identityPointId = identification.getElementByName("id").textContent;
			fingerprint = identification.getElementByName("fingerprint").textContent;
		}
		else if (statusCode == -1000)
		{
			errorMessage = "The account name or password was incorrect. Please try again.";
		}
		else
		{
			errorMessage = "MLB.com is curently undergoing maintenance. Please try again.";
		}

		if (identityPointId && fingerprint)
		{
			atv.localStorage[USERNAME_KEY] = username;
			atv.localStorage[EMAIL_IDENTITY_POINT_KEY] = identityPointId;
			atv.localStorage[EMAIL_FINGERPRINT_KEY] = fingerprint;

			if (!atv.localStorage[BEST_IDENTITY_POINT_KEY] && !atv.localStorage[BEST_IDENTITY_POINT_KEY])
			{
				console.debug("saving the email as the best identity point id");
				atv.localStorage[BEST_IDENTITY_POINT_KEY] = identityPointId;
				atv.localStorage[BEST_FINGERPRINT_KEY] = fingerprint;
			}
			console.debug("best identity point id: " + atv.localStorage[BEST_IDENTITY_POINT_KEY]);

			console.debug("determine whether the user is authorized " + serviceClient.loadUserAuthorized);
			serviceClient.loadUserAuthorized(function()
			{
				if (success)
					success();
			});
		}
		else
		{
			if (error)
				error(errorMessage);
			console.log('error message ' + errorMessage);
		}
	});
}

ServiceClient.prototype.loadUserAuthorized = function(callback)
{
	var url = config.bamServiceBase + "/pubajaxws/services/FeatureService";
	var body = "<soapenv:Envelope xmlns:soapenv=\"http://schemas.xmlsoap.org/soap/envelope/\" xmlns:ns=\"http://services.bamnetworks.com/registration/types/1.6\">\
			<soapenv:Body>\
				<ns:feature_findEntitled_request>\
					<ns:identification type=\"fingerprint\">\
						<ns:id>" + atv.localStorage[BEST_IDENTITY_POINT_KEY] + "</ns:id>\
						<ns:fingerprint>" + atv.localStorage[BEST_FINGERPRINT_KEY] + "</ns:fingerprint>\
					</ns:identification>\
					<ns:featureContextName>" + MLBTV_FEATURE_CONTEXT + "</ns:featureContextName>\
				</ns:feature_findEntitled_request>\
			</soapenv:Body>\
		</soapenv:Envelope>";

	var headers = {
		"SOAPAction" : "http://services.bamnetworks.com/registration/feature/findEntitledFeatures",
		"Content-Type" : "text/xml"
	};

	console.debug('loadUserAuthorized() Feature find entitlement request body ' + body);
	console.debug('loadUserAuthorized() Finding entitled features with ' + url);

	atvutils.makeRequest(url, "POST", headers, body, function(document, text)
	{
		console.debug("loadUserAuthorized() Feature.findEntitled response " + text);
		var authorized = (text.indexOf(MLBTV_FEATURE_NAME) > -1);
		console.debug("loadUserAuthorized() authorized " + authorized);
		atv.localStorage[AUTHORIZED_KEY] = authorized.toString();
		console.debug("loadUserAuthorized() set atv.localStorage[AUTHORIZED_KEY] to " + atv.localStorage[AUTHORIZED_KEY]);
		callback(authorized);
	});
}

ServiceClient.prototype.addRestoreObserver = function(successCallback, errorCallback)
{
	// Setup a transaction observer to see when transactions are restored and when new payments are made.

	var transReceived = [];
	
	var restoreObserver = {
		updatedTransactions : function(transactions)
		{
			console.debug("addRestoreObserver() updatedTransactions " + (transactions) ? transactions.length : null);

			if (transactions)
			{
				for ( var i = 0; i < transactions.length; ++i)
				{
					transaction = transactions[i];
					if (transaction.transactionState == atv.SKPaymentTransactionStateRestored)
					{
						console.debug("addRestoreObserver() Found restored transaction: " + GetLogStringForTransaction(transaction) + "\n---> Original Transaction: " + GetLogStringForTransaction(transaction.originalTransaction));
						
						// add
						transReceived.push(transaction);
						
						// finish the transaction
						atv.SKDefaultPaymentQueue.finishTransaction(transaction);
					}
				}
			}
			console.debug("addRestoreObserver() updatedTransactions " + transactions.length);
		},

		restoreCompletedTransactionsFailedWithError : function(error)
		{
			console.debug("addRestoreObserver() restoreCompletedTransactionsFailedWithError start " + error + " code: " + error.code);
			
			atv.SKDefaultPaymentQueue.removeTransactionObserver(this);
			if (typeof (errorCallback) == "function")
				errorCallback(error);
			
			console.debug("addRestoreObserver() restoreCompletedTransactionsFailedWithError end " + error 	return result;
}

ServiceClient.prototype.doRequestProduct = function(productIds, successCallback, errorCallback)
{
	console.debug("doRequestProduct() productIds " + productIds);
	// make another products request to get product information
	var request = new atv.SKProductsRequest(productIds);

	req+ " code: " + error.code);
		},

		restoreCompletedTransactionsFinished : function()
		{
			console.debug("addRestoreObserver() restoreCompletedTransactionsFinished start");
			
			atv.SKDefaultPaymentQueue.removeTransactionObserver(this);
			
			console.log("addRestoreObserver() transReceived " + transReceived);
			if (typeof (successCallback) == "function")
			{
				successCallback(transReceived);
			}	
			
			console.debug("addRestoreObserver() restoreCompletedTransactionsFinished end");
		}
	};

	atv.SKDefaultPaymentQueue.addTransactionObserver(restoreObserver);
	return restoreObserver;
}

function GetLogStringForTransaction(t)
{
	var result = "Transaction: " + "transactionIdentifier=" + ((t.transactionIdentifier) ? t.transactionIdentifier : "") + ", transactionState=" + ((t.transactionState) ? t.transactionState : "") + ", transactionReceipt=" + ((t.transactionReceipt) ? t.transactionReceipt : "") + ", originalTransaction= " + ((t.originalTransaction) ? t.originalTransaction : "") + ", transactionDate=" + ((t.transactionDate) ? t.transactionDate : "");

	if (t.payment)
	{
		result += ", payment.productIdentifier=" + t.payment.productIdentifier + ", payment.quantity=" + t.payment.quantity;
	}

	if (t.error)
	{
		result += ", error.domain=" + t.error.domain + ", error.code=" + t.error.code + ", error.userInfo=" + t.error.userInfo;
	}

uest.onRequestDidFinish = function()
	{
		console.debug("doRequestProduct() Products request succeeded");
		if (typeof (successCallback) == "function")
			successCallback();
	};

	request.onRequestDidFailWithError = function(error)
	{
		console.error("doRequestProduct() Products request failed. " + ErrorToDebugString(error));
		if (typeof (errorCallback) == "function")
			errorCallback(error);
	};

	request.onProductsRequestDidReceiveResponse = function(productsResponse)
	{
		console.log("doRequestProduct() Products request received response " + productsResponse);
		console.debug("There are " + productsResponse.products.length + " offers.");

		if (typeof (successCallback) == "function")
			successCallback(productsResponse);
	};

	console.debug("Calling doRequestProduct() start");
	request.start();
}

ServiceClient.prototype.startPurchase = function()
{
	var productID = atv.localStorage["purchase.productId"];

	if (!productID)
	{
		console.debug("startPurchase(): not making a purchase because productID was not provided");
	}
	else
	{
		// make another products request to get product information
		var request = new atv.SKProductsRequest([ productID ]);

		request.onRequestDidFailWithError = function(error)
		{
			console.error("startPurchase() Products request failed. " + ErrorToDebugString(error));
		};

		request.onProductsRequestDidReceiveResponse = function(productsResponse)
		{
			console.log("startPurchase() Products request received response " + productsResponse);
			console.debug("There are " + productsResponse.products.length + " offers.");

			if (productsResponse && productsResponse.products && productsResponse.products.length == 1)
			{
				var product = productsResponse.products[0];
				console.debug("Found available product for ID " + product);
				var payment = {
					product : product,
					quantity : 1
				};
				atv.SKDefaultPaymentQueue.addPayment(payment);
			}
		};

		console.debug("Calling doRequestProduct() start");
		request.start();
	}
}

ServiceClient.prototype.addPurchaseObserver = function(successCallback, errorCallback)
{
	console.debug("addPurchaseObserver(): Purchase product with identifier " + productID);

	var productID = atv.localStorage["purchase.productId"];

	var observer = {
		updatedTransactions : function(transactions)
		{
			console.debug("addPurchaseObserver(): purchase.updatedTransactions: " + transactions.length);

			for ( var i = 0; i < transactions.length; ++i)
			{
				var transaction = transactions[i];

				console.debug("addPurchaseObserver(): Processing transaction " + i + ": " + GetLogStringForTransaction(transaction));

				if (transaction.payment && transaction.payment.productIdentifier == productID)
				{
					console.debug("addPurchaseObserver(): transaction is for product ID " + productID);

					// This is the product ID this transaction observer was created for.
					switch (transaction.transactionState)
					{
					case atv.SKPaymentTransactionStatePurchasing:
						console.debug("addPurchaseObserver(): SKPaymentTransactionStatePurchasing");
						break;
					case atv.SKPaymentTransactionStateRestored:
					case atv.SKPaymentTransactionStatePurchased:
						var reason = transaction.transactionState == atv.SKPaymentTransactionStatePurchased ? "Purchased" : "Restored";
						console.debug("addPurchaseObserver(): SKPaymentTransactionStatePurchased: Purchase Complete!!! Reason: " + reason);
						atv.SKDefaultPaymentQueue.finishTransaction(transaction);
						atv.SKDefaultPaymentQueue.removeTransactionObserver(this);
						if (typeof (successCallback) == "function")
							successCallback(transaction);
						break;
					case atv.SKPaymentTransactionStateFailed:
						console.debug("addPurchaseObserver(): SKPaymentTransactionStateFailed. " + ErrorToDebugString(transaction.error));
						atv.SKDefaultPaymentQueue.finishTransaction(transaction);
						atv.SKDefaultPaymentQueue.removeTransactionObserver(this);
						if (typeof (errorCallback) == "function")
							errorCallback(transaction);
						break;
					default:
						if (typeof (errorCallback) == "function")
							errorCallback(transaction);
						console.error("addPurchaseObserver(): Unknown transaction state: " + transaction.transactionState);
						break;
					}
				}
			}
		}
	}

	atv.SKDefaultPaymentQueue.addTransactionObserver(observer);
	return observer;
}

ServiceClient.prototype.startMediaFlow = function(mediaMeta)
{
	console.debug("startMediaFlow() mediaMeta " + mediaMeta);

	if (mediaMeta)
	{
		// set media meta to the local storage
		atv.localStorage[MEDIA_META_KEY] = JSON.stringify(mediaMeta);

		mediaMeta.title = mediaMeta.awayTeamCity + " " + mediaMeta.awayTeamName + " vs " + mediaMeta.homeTeamCity + " " + mediaMeta.homeTeamName;
		mediaMeta.description = mediaMeta.feed;
		if (mediaMeta.awayTeamId && mediaMeta.homeTeamId)
		{
			mediaMeta.imageUrl = config.imageBase + "/recapThumbs/" + mediaMeta.awayTeamId + "_v_" + mediaMeta.homeTeamId + ".jpg";
		}

		// set the session key from the local storage
		mediaMeta.sessionKey = atv.localStorage["sessionKey"];

		var mediaMetaRequestParams = "";
		mediaMetaRequestParams += '&identityPointId=' + ((atv.localStorage[BEST_IDENTITY_POINT_KEY]) ? atv.localStorage[BEST_IDENTITY_POINT_KEY] : 'IPID');
		mediaMetaRequestParams += '&fingerprint=' + ((atv.localStorage[BEST_FINGERPRINT_KEY]) ? atv.localStorage[BEST_FINGERPRINT_KEY] : 'FINGERPRINT');
		mediaMetaRequestParams += "&contentId=" + mediaMeta.contentId;
		mediaMetaRequestParams += '&sessionKey=' + ((mediaMeta.sessionKey) ? encodeURIComponent(mediaMeta.sessionKey) : "SESSION_KEY");
		mediaMetaRequestParams += '&calendarEventId=' + mediaMeta.calendarEventId;
		mediaMetaRequestParams += "&audioContentId=" + ((mediaMeta.audioContentId) ? mediaMeta.audioContentId.replace(/\s/g, ',') : "");
		mediaMetaRequestParams += '&day=' + mediaMeta.day
		mediaMetaRequestParams += '&month=' + mediaMeta.month;
		mediaMetaRequestParams += '&year=' + mediaMeta.year;
		mediaMetaRequestParams += "&feedType=" + mediaMeta.feedType;
		mediaMetaRequestParams += "&playFromBeginning=" + mediaMeta.playFromBeginning;
		mediaMetaRequestParams += "&title=" + encodeURIComponent(mediaMeta.title);
		mediaMetaRequestParams += "&description=" + encodeURIComponent(mediaMeta.description);
		mediaMetaRequestParams += (mediaMeta.imageUrl) ? ("&imageUrl=" + encodeURIComponent(mediaMeta.imageUrl)) : "";

		// save in local storage (should be in session storage but session storage is not yet supported)
		atv.localStorage[MEDIA_META_KEY] = JSON.stringify(mediaMeta);
		atv.localStorage[MEDIA_META_REQUEST_PARAMS_KEY] = mediaMetaRequestParams;
		console.debug("storing into local storage mediaMetaRequestParams " + atv.localStorage[MEDIA_META_REQUEST_PARAMS_KEY]);
	}
}

ServiceClient.prototype.loadMediaPage = function(callback)
{
	if (atv.localStorage[MEDIA_META_REQUEST_PARAMS_KEY])
	{
		var mediaProxyUrl = config.appletvBase + "/views/MediaServiceProxy?";
		// mediaProxyUrl += '&identityPointId=' + atv.localStorage[BEST_IDENTITY_POINT_KEY];
		// mediaProxyUrl += '&fingerprint=' + encodeURIComponent(atv.localStorage[BEST_FINGERPRINT_KEY]);
		mediaProxyUrl += atv.localStorage[MEDIA_META_REQUEST_PARAMS_KEY];
		console.debug("loadMediaPage mediaProxyUrl " + mediaProxyUrl);

		// make request
		atvutils.getRequest(mediaProxyUrl, callback);
	}
	else if (callback)
	{
		callback();
	}
}

function applyOffer(document)
{
	console.debug("applyOffer() atv.localStorage[PURCHASE_FLOW_KEY] " + atv.localStorage[PURCHASE_FLOW_KEY]);
	console.debug("applyOffer() document " + document);
	var mlbSignIn = (document) ? document.getElementById("mlbSignIn") : null;
	if (mlbSignIn)
	{
		var label = mlbSignIn.getElementByName("label");
		console.debug("applyOffer() atv.localStorage[EMAIL_IDENTITY_POINT_KEY] " + atv.localStorage[EMAIL_FINGERPRINT_KEY]);
		var signedIn = atv.localStorage[EMAIL_IDENTITY_POINT_KEY] != null && atv.localStorage[EMAIL_FINGERPRINT_KEY] != null;
		console.debug("applyOffer() signedIn " + signedIn);

		var uriToLoadAfterAuth = atv.localStorage[OFFER_URI_AFTER_AUTH_KEY];

		if (signedIn)
		{
			label.removeFromParent();
			label = atv.parseXML("<label>Sign In with different user</label>").rootElement;
			label.removeFromParent();
			mlbSignIn.appendChild(label);

			mlbSignIn.setAttribute("onSelect", "atv.logout();atvutils.loadAndSwapURL('" + config.appletvBase + "/views/Login?urlToLoadAfterAuth=" + uriToLoadAfterAuth + "');");
			mlbSignIn.setAttribute("onPlay", "atv.logout();atvutils.loadAndSwapURL('" + config.appletvBase + "/views/Login?urlToLoadAfterAuth=" + uriToLoadAfterAuth + "');");
		}
		else
		{
			console.debug("applyOffer() uriToLoadAfterAuth " + uriToLoadAfterAuth);
			label.textContent = "MLB.TV Premium Subscriber Login";
			console.debug("applyOffer() mlbSignIn.getAttribute('onSelect') " + mlbSignIn.getAttribute('onSelect'));
			mlbSignIn.setAttribute("onSelect", "atvutils.loadAndSwapURL('" + config.appletvBase + "/views/Login?urlToLoadAfterAuth=" + uriToLoadAfterAuth + "');");
			mlbSignIn.setAttribute("onPlay", "atvutils.loadAndSwapURL('" + config.appletvBase + "/views/Login?urlToLoadAfterAuth=" + uriToLoadAfterAuth + "');");
			console.debug("applyOffer() mlbSignIn.getAttribute('onSelect') " + mlbSignIn.getAttribute('onSelect'));
		}
	}
}

ServiceClient.prototype.loadOfferPage = function(newPage)
{
	console.debug("loadOfferPage() enter");

	newPage
	
	// set purchase flow to true
	flowManager.startPurchaseFlow();

	var swap = (atv.localStorage[OFFER_SWAP_PAGE_KEY] == "true");
	console.debug("loadOfferPage() atv.localStorage[OFFER_SWAP_PAGE_KEY] " + atv.localStorage[OFFER_SWAP_PAGE_KEY]);
	console.debug("loadOfferPage() swap " + swap);

	// Use StoreKit to get the list of product identifiers.
	var request = new atv.SKProductsRequest(Object.keys(PRODUCT_LIST));
	request.onRequestDidFailWithError = function(error)
	{
		console.error("Products request failed. " + ErrorToDebugString(error));
		var xml = atvutils.makeOkDialog("MLB.TV", "MLB.TV Products could not be found. Please check again at a later time.");
		if (swap)
			newPage.swapXML(xml)
		else
			newPage.loadXML(xml);
	};
	request.onProductsRequestDidReceiveResponse = function(productsResponse)
	{
		// success
		console.debug("loadOfferPage() Products request received response " + productsResponse);
		console.debug("loadOfferPage() There are " + productsResponse.products.length + " offers.");

		// add image
		console.debug("loadOfferPage() checking for image location");
		for ( var i = 0; i < productsResponse.products.length; i++)
		{
			var product = productsResponse.products[i];
			console.debug("loadOfferPage() product.productIdentifier " + product.productIdentifier);
			if (PRODUCT_LIST[product.productIdentifier] != null)
			{
				product.image = PRODUCT_LIST[product.productIdentifier].image;
				console.debug("loadOfferPage() product.image " + product.image);

				product.period = PRODUCT_LIST[product.productIdentifier].period;
				console.debug("loadOfferPage() product.period " + product.period);
			}
		}

		// This page will display the offers, and call purchaseProduct below with the productId to purchase in an onSelect handler.
		var offerBody = "";
		offerBody += "productMeta=" + JSON.stringify(productsResponse);
		offerBody += "&purchaseFunction=" + atv.localStorage[OFFER_PURCHASE_FUNCTION_KEY];
		if (atv.localStorage[OFFER_URI_AFTER_AUTH_KEY])
		{
			offerBody += "&uriToLoadAfterAuth=" + atv.localStorage[OFFER_URI_AFTER_AUTH_KEY];
		}

		console.debug("loadOfferPage() offerBody: " + offerBody);

		atvutils.postRequest(config.appletvBase + '/views/Offer', offerBody, function(xml)
		{
			try
			{
				console.debug("loadOfferPage() postRequest xml=" + xml);
				applyOffer(xml);
				if (swap)
					newPage.swapXML(xml)
				else
					newPage.loadXML(xml);
			}
			catch (e)
			{
				console.error("Caught exception for the Offer page " + e);
				var errorXML = atvutils.makeOkDialog("MLB.TV Error", "MLB.TV Products could not be found. Please check again at a later time.");
				if (swap)
					newPage.swapXML(errorXML)
				else
					newPage.loadXML(errorXML);
			}
		});
	};

	console.debug("Calling loadOfferPage() start");
	request.start();
}
ServiceClient.prototype.isMediaResponseUnauthorized = function(xml)
{
	return (xml) ? this.isMediaUnauthorized(this.getMediaStatusCode(xml)) : true;
}

ServiceClient.prototype.isMediaUnauthorized = function(mediaStatusCode)
{
	return "" == mediaStatusCode || "UNDETERMINED" == mediaStatusCode || "NOT_AUHTORIZED" == mediaStatusCode || "LOGIN_REQUIRED" == mediaStatusCode || "INVALID_USER_CREDENTIALS_STATUS" == mediaStatusCode;
}

ServiceClient.prototype.isMediaPlayback = function(mediaStatusCode)
{
	return "VIDEO_PLAYBACK" == mediaStatusCode || "AUDIO_PLAYBACK" == mediaStatusCode;
}

ServiceClient.prototype.getMediaStatusCode = function(xml)
{
	var mediaStatusCodeElem = (xml && xml.rootElement) ? xml.rootElement.getElementByTagName("mediaStatusCode") : null;
	console.debug("getMediaStatusCode() mediaStatusCodeElem " + mediaStatusCodeElem);
	var mediaStatusCode = (mediaStatusCodeElem != null) ? mediaStatusCodeElem.textContent : "UNDETERMINED";
	console.debug("getMediaStatusCode() mediaStatusCode " + mediaStatusCode);
	return mediaStatusCode;
}

ServiceClient.prototype.createFingerprintIdpt = function(ipid, fingerprint)
{
	return {
		"type" : "fingerprint",
		"id" : ipid,
		"fingerprint" : fingerprint
	};
}

ServiceClient.prototype.createIosIdentityPoint = function(transaction)
{
	return {
		"type" : "iosInAppPurchase",
		"receipt" : transaction.transactionReceipt
	};
}

ServiceClient.prototype.createLinkRequest = function(transactions)
{
	var identityPoints = [];

	// email-password fingerprint identity point
	if (atv.localStorage[EMAIL_FINGERPRINT_KEY] && atv.localStorage[EMAIL_IDENTITY_POINT_KEY])
	{
		identityPoints.push(this.createFingerprintIdpt(atv.localStorage[EMAIL_IDENTITY_POINT_KEY], atv.localStorage[EMAIL_FINGERPRINT_KEY]));
	}

	if (transactions)
	{
		console.debug("createLinkRequest() filtering out for unique original transactions");
		
		// filter out unique original transactions
		var originalTransMap = {};
		
		for (var t = 0; t < transactions.length; t++)
		{
			var currTrans = transactions[t];
			var origTrans = currTrans.originalTransaction;
			var transId = (origTrans == null) ? currTrans.transactionIdentifier : origTrans.transactionIdentifier;  
			
			console.debug("createLinkRequest() transId " + transId);
			
			if (originalTransMap[transId] == null)
			{
				// transaction identity point
				identityPoints.push(this.createIosIdentityPoint(currTrans));
				
				originalTransMap[transId] = currTrans;
			}
		}
	}
	// best fingerprint identity point
	else if (atv.localStorage[BEST_FINGERPRINT_KEY] && atv.localStorage[BEST_IDENTITY_POINT_KEY])
	{
		identityPoints.push(this.createFingerprintIdpt(atv.localStorage[BEST_IDENTITY_POINT_KEY], atv.localStorage[BEST_FINGERPRINT_KEY]));
	}

	var linkRequest = null;
	if (identityPoints.length > 0)
	{
		// link request
		linkRequest = {
			"identityPoints" : identityPoints,
			"namespace" : "mlb",
			"key" : "h-Qupr_3eY"
		};
	}

	console.debug("createLinkRequest() linkRequest " + JSON.stringify(linkRequest));

	return linkRequest;
}

ServiceClient.prototype.doLink = function(transactions, successCallback, errorCallback)
{
	var linkRequest = this.createLinkRequest(transactions);

	if (linkRequest == null)
	{
		if (typeof (successCallback) == "function")
			successCallback();

		console.debug("doLink() link not called since nothing to link");
	}
	else
	{
		// link request
		var linkRequestBody = JSON.stringify(linkRequest);
		var req = new XMLHttpRequest();
		var url = config.mlbBase + "/mobile/LinkAll2.jsp";

		req.onreadystatechange = function()
		{
			try
			{
				if (req.readyState == 4)
				{
					console.log('link() Got link service http status code of ' + req.status);
					if (req.status == 200)
					{
						console.log('link() Response text is ' + req.responseText);
						var linkResponseJson = JSON.parse(req.responseText);

						if (linkResponseJson.status == 1)
						{
							console.debug("link() bestIdentityPointToUse: " + linkResponseJson.bestIdentityPointToUse);
							console.debug("link() messages: " + linkResponseJson.messages);

							if (linkResponseJson.bestIdentityPointToUse && linkResponseJson.bestIdentityPointToUse.id && linkResponseJson.bestIdentityPointToUse.fingerprint)
							{
								console.debug("link() saving the best identity point to use");
								atv.localStorage[BEST_IDENTITY_POINT_KEY] = linkResponseJson.bestIdentityPointToUse.id;
								atv.localStorage[BEST_FINGERPRINT_KEY] = linkResponseJson.bestIdentityPointToUse.fingerprint;
								console.debug("link() identityPointId " + atv.localStorage[BEST_IDENTITY_POINT_KEY]);

								if (linkResponseJson.bestIdentityPointToUse.id.toLowerCase().indexOf("ios") >= 0)
								{
									atv.localStorage[IOS_IDENTITY_POINT_KEY] = linkResponseJson.bestIdentityPointToUse.id;
									atv.localStorage[IOS_FINGERPRINT_KEY] = linkResponseJson.bestIdentityPointToUse.fingerprint;
								}
							}

							var linked = false;// , auth = false;
							for ( var m = 0; m < linkResponseJson.messages.length; m++)
							{
								var msg = linkResponseJson.messages[m];
								console.debug("doLink() msg " + msg);
								if (msg == "linked")
								{
									linked = true;
								}
							}
							linked = linked && atv.localStorage[EMAIL_IDENTITY_POINT_KEY] != null && atv.localStorage[EMAIL_FINGERPRINT_KEY] != null;
							// atv.localStorage[EMAIL_LINKED_KEY] = linked.toString();
							// atv.localStorage[AUTHORIZED_KEY] = auth.toString();

							// console.debug("doLink() atv.localStorage[EMAIL_LINKED_KEY] " + atv.localStorage[EMAIL_LINKED_KEY];
							console.debug("atv.localStorage[AUTHORIZED_KEY] " + atv.localStorage[AUTHORIZED_KEY]);

							if (typeof (successCallback) == "function")
								successCallback();

							console.log("doLink() getAllResponseHeaders: " + this.getAllResponseHeaders());
						}
						else
						{

							if (typeof (errorCallback) == "function")
								errorCallback();
							console.error("link response json has an invalid status code: " + linkResponseJson.status);
						}
					}
				}
			}
			catch (e)
			{
				// Specify a copyedited string because this will be displayed to
				// the user.
				console.error('Caught exception while processing request. Aborting. Exception: ' + e);
				if (typeof (errorCallback) == "function")
					errorCallback(e);

				req.abort();
			}
		}

		req.open("POST", url, true);
		req.setRequestHeader("Content-Type", "text/xml");

		console.log('Trying to link with ' + url);
		console.log("Link Service POST body is " + linkRequestBody);
		req.send(linkRequestBody);
	}
}

var serviceClient = new ServiceClient;

console.log("~~~~~ MLB.TV service.js END");HTTP/1.1 200 OK
Server: Oracle-iPlanet-Web-Server/7.0
Date: Wed, 08 Aug 2012 16:14:56 GMT
Content-type: application/x-javascript
Last-modified: Tue, 31 Jul 2012 21:32:23 GMT
Content-length: 42766
Etag: "a70e-50184ee7"
Accept-ranges: bytes

console.log("~~~~~ MLB.TV main.js start");

var IOS_IDENTITY_POINT_KEY = "ios.identityPointId";
var IOS_FINGERPRINT_KEY = "ios.fingerprint";
var BEST_IDENTITY_POINT_KEY = "best.identityPointId";
var BEST_FINGERPRINT_KEY = "best.fingerprint";
var USERNAME_KEY = "username";

var SHOW_SCORES_KEY = "showScores";
var FAVORITE_TEAMS_KEY = "favoriteTeamIds";

var SESSION_KEY = "sessionKey";
var MEDIA_META_KEY = "media.meta";
var MEDIA_META_REQUEST_PARAMS_KEY = "media.meta.requestParams";

var PURCHASE_FLOW_KEY = "flow.purchase";
var PURCHASE_PRODUCT_ID_KEY = "purchase.productId";

var EMAIL_LINKED_KEY = "email.linked";
var AUTHORIZED_KEY = "authorized";

var OFFER_URI_AFTER_AUTH_KEY = "offer.uriToLoadAfterAuth";
var OFFER_PURCHASE_FUNCTION_KEY = "offer.purchaseFunction";
var OFFER_SWAP_PAGE_KEY = "offer.swapPage";

var TARGET_PAGEID_KEY = "targetPage";
var PURCHASE_FLOW_KEY = "flow.purchase";
var OFFER_PAGE_ID = "com.bamnetworks.appletv.offer";

var TODAYS_GAMES_REFRESH_URL_KEY = "todaysGames.refreshUrl";
var GAMES_PAGE_REFRESH_URL_KEY = "games.refreshUrl";

// atv.onPageLoad
atv.onPageLoad = function(id)
{
	console.log("main.js atv.onPageLoad() id: " + id);

	// <mlbtv>
	var headElem = document.rootElement.getElementByName("head");
	if (headElem && headElem.getElementByName("mlbtv"))
	{
		var mlbtvElem = headElem.getElementByName("mlbtv");
		console.log("mlbtv exists");

		// store local variables
		storeLocalVars(mlbtvElem);

		// ping urls
		pingUrls(mlbtvElem);
	}

	// games by team
	if (id && id.indexOf("com.bamnetworks.appletv.gamesByTeam.") >= 0)
	{
		applyGamesPage(document);
	}

	if (id && id.indexOf("com.bamnetworks.appletv.media") >= 0 && typeof (flowManager) != "undefined")
	{
		flowManager.endPurchaseFlow();
		flowManager.endMediaFlow();
		console.debug("main.js onPageLoad() atv.localStorage[PURCHASE_FLOW_KEY] " + atv.localStorage[PURCHASE_FLOW_KEY]);
	}

	atv._debugDumpControllerStack();
}

// atv.onPageUnload
atv.onPageUnload = function(id)
{
	console.debug("main.js atv.onPageUnload() id " + id);
	console.debug("main.js atv.onPageUnload() atv.localStorage[PURCHASE_FLOW_KEY] " + atv.localStorage[PURCHASE_FLOW_KEY]);
	if (flowManager.inPurchaseFlow() && ("com.bamnetworks.appletv.authenticate" == id || id.indexOf("Not Authorized") > -1))
	{
		console.debug("main.js atv.onPageUnload() showing offer page");
		flowManager.setTargetPage(OFFER_PAGE_ID);

		var newPage = new atv.ProxyDocument();
		newPage.show();
		serviceClient.loadOfferPage(newPage);

		console.debug("main.js atv.onPageUnload() showing offer page");
	}
}

fu
		}
	}
}

/**
 * Updates the logo according to the screen resolution
 * 
 * @param document
 * @returns
 */
function updateImageResolution(document)
{
	// update logos according to screen resolution
nction storeLocalVars(mlbtvElem)
{
	// check local storage params
	var localStorageElems = mlbtvElem.getElementsByName("localStorage");
	console.debug("localStorageElems=" + localStorageElems);
	if (localStorageElems && localStorageElems.length > 0)
	{
		var index = 0;
		for (; index < localStorageElems.length; index++)
		{
			var key = localStorageElems[index].getAttribute("key");
			var value = localStorageElems[index].getAttribute("value");
			if (value)
			{
				atv.localStorage[key] = value;
			}
			else
			{
				atv.localStorage.removeItem(key);
			}
			console.log("localStorage[" + key + "] = " + atv.localStorage[key]);
		}
	}
}

function pingUrls(mlbtvElem)
{
	// check ping urls
	var pingElems = mlbtvElem.getElementsByName("ping");
	console.log("pingElems=" + pingElems);
	if (pingElems && pingElems.length > 0)
	{
		for ( var pingIndex = 0; pingIndex < pingElems.length; pingIndex++)
		{
			var url = pingElems[pingIndex].getAttribute('url');
			url = (url == null || url.trim().length() <= 0) ? pingElems[pingIndex].textContent : url;
			console.debug("url=" + url);
			if (url)
			{
				try
				{
					console.log('pinging reporting url ' + url);
					var req = new XMLHttpRequest();
					req.open("GET", url, true);
					req.send();
				}
				catch (e)
				{
					console.error("failed to ping the url " + url + " error " + e);
				}
			}	var frame = atv.device.screenFrame;
	console.log("frame size: " + frame.height);
	if (frame.height == 1080)
	{
		var images = document.rootElement.getElementsByTagName('image');
		for ( var i = 0; i < images.length; i++)
		{
			images[i].textContent = images[i].textContent.replace('720p', '1080p');
		}
	}

	return document;
}

// ------------------------------ load and apply -------------------------------
function applyMain(document)
{
	console.log('applyMain ' + document);

	// end all flows when you're on the main page
	flowManager.endMediaFlow();
	flowManager.endPurchaseFlow();

	// check valid subscription
	// (1) via iTunes on this box and iTunes account signed in. Receipt is present and stored on the box
	// (2) via Vendor and is signed in with Vendor account.
	var validSubscription = (atv.localStorage[IOS_IDENTITY_POINT_KEY] != null && atv.localStorage[IOS_FINGERPRINT_KEY] != null) || (atv.localStorage[AUTHORIZED_KEY] == "true" && atv.localStorage[EMAIL_IDENTITY_POINT_KEY] != null && atv.localStorage[EMAIL_FINGERPRINT_KEY] != null);

	console.log("applyMain() validSubscription " + validSubscription);
	console.log("applyMain() atv.localStorage[AUTHORIZED_KEY] " + atv.localStorage[AUTHORIZED_KEY]);

	var subscribeMenu = document.getElementById("subscribeMlbTv");
	if (validSubscription)
	{
		if (subscribeMenu)
		{
			subscribeMenu.removeFromParent();
			console.debug("applyMain() removing the Subscribe To MLB.TV");
		}
	}
	else
	{
		// add the menu if it's not there
		if (!subscribeMenu)
		{
			subscribeMenu = atv.parseXML('<oneLineMenuItem id="subscribeMlbTv" accessibilityLabel="Subscribe To MLB.TV" onSelect="subscribeToMlbTv()"><label>Subscribe To MLB.TV</label></oneLineMenuItem>').rootElement;
			subscribeMenu.removeFromParent();

			var parent = document.rootElement.getElementByTagName("items");
			console.debug("applyMain() parent " + parent);
			if (parent && parent.childElements)
			{
				console.debug("applyMain() before parent.childElements " + parent.childElements);
				var settingsMenu = document.getElementById("settings");
				settingsMenu.removeFromParent();
				parent.appendChild(subscribeMenu);
				parent.appendChild(settingsMenu);
				console.debug("applyMain() after parent.childElements " + parent.childElements);
			}
		}
	}

	return document;
}

function handleMainVoltileReload()
{
	console.debug("handleMainVoltileReload() atv.localStorage[TARGET_PAGEID_KEY] " + atv.localStorage[TARGET_PAGEID_KEY]);
	if (atv.localStorage[TARGET_PAGEID_KEY] == null || atv.localStorage[TARGET_PAGEID_KEY] == "com.bamnetworks.appletv.main")
	{
		applyMain(document);
		atv.loadAndSwapXML(document);
	}
}

function handleOfferVoltileReload(document)
{
	console.debug("handleOfferVoltileReload() atv.localStorage[AUTHORIZED_KEY] " + atv.localStorage[AUTHORIZED_KEY]);
	if (atv.localStorage[AUTHORIZED_KEY] == "true")
	{
		console.debug("handleOfferVoltileReload() unloading the page because already authorized");
		atv.unloadPage();
	}
	else
	{
		applyOffer(document);
		atv.loadAndSwapXML(document);
	}
}

function applyTodaysGames(responseXML, refresh)
{
	console.log("applyTodaysGames() responseXML: " + responseXML);

	try
	{
		// elements
		var listByNavigation = responseXML.rootElement.getElementByTagName("listByNavigation");
		var imageMenuItems = responseXML.rootElement.getElementsByTagName("imageTextImageMenuItem");
		console.log("applyTodaysGames() navigation=" + navigation);

		// add behavior
		if (atv.localStorage[TODAYS_GAMES_REFRESH_URL_KEY] == null || !refresh)
		{
			var navigation = responseXML.rootElement.getElementByTagName("navigation");
			var currentIndex = navigation.getAttribute("currentIndex");
			console.debug("applyTodaysGames() currentIndex " + currentIndex);
			var refreshUrl = navigation.childElements[currentIndex].getElementByTagName("url").textContent;
			console.debug("applyTodaysGames() refreshUrl " + refreshUrl);
			atv.localStorage[TODAYS_GAMES_REFRESH_URL_KEY] = refreshUrl;
		}
		listByNavigation.setAttribute("refreshInterval", "60");
		listByNavigation.setAttribute("onRefresh", "console.log('refreshing todays games');handleTodaysGamesRefresh();");
		listByNavigation.setAttribute("onNavigate", "console.log('navigating todays games');handleTodaysGamesNavigate(event);");

		// sort
		var favTeamIds = getFavoriteTeamIds();
		function sort(menuOne, menuTwo)
		{
			var menuOneTeamIds = menuOne.getAttribute("id").split("-");
			var menuTwoTeamIds = menuTwo.getAttribute("id").split("-");

			var menuOneOrder = (favTeamIds.indexOf(menuOneTeamIds[0]) >= 0 || favTeamIds.indexOf(menuOneTeamIds[1]) >= 0) ? 1 : 0;
			var menuTwoOrder = (favTeamIds.indexOf(menuTwoTeamIds[0]) >= 0 || favTeamIds.indexOf(menuTwoTeamIds[1]) >= 0) ? 1 : 0;

			// console.log("favTeamIds.indexOf(menuOneTeamIds[0])=" + favTeamIds.indexOf(menuOneTeamIds[0]) + " favTeamIds.indexOf(menuOneTeamIds[1])=" + favTeamIds.indexOf(menuOneTeamIds[1]));
			// console.log(menuOneOrder + "-" + menuTwoOrder);

			return menuTwoOrder - menuOneOrder;
		}
		var orderedMenuList = imageMenuItems.sort(sort);

		// 1. re-append the sorted children
		// 2. set the favorite team logos
		// 3. hide scores on the page
		// 4. set the link to no scores preview
		var index = 0;
		for (; index < orderedMenuList.length; index++)
		{
			// 1. reappend child for sorting
			var menu = orderedMenuList[index];
			var parent = menu.parent;
			menu.removeFromParent();
			parent.appendChild(menu);

			// 2. set the favorite team logos
			console.debug("applyTodaysGames() menu item id " + menu.getAttribute("id"));
			var teamIds = menu.getAttribute("id").split("-");
			var isAwayFav = favTeamIds.indexOf(teamIds[0]) >= 0;
			var isHomeFav = favTeamIds.indexOf(teamIds[1]) >= 0;
			console.debug("applyTodaysGames() isAwayFav " + isAwayFav);
			console.debug("applyTodaysGames() isHomeFav " + isHomeFav);
			var leftImageElem = menu.getElementByName("leftImage");
			if (isAwayFav && leftImageElem.textContent.indexOf("-fav.png") < 0)
			{
				leftImageElem.textContent = leftImageElem.textContent.replace(".png", "-fav.png");
			}
			var rightImageElem = menu.getElementByName("rightImage");
			if (isHomeFav && rightImageElem.textContent.indexOf("-fav.png") < 0)
			{
				rightImageElem.textContent = rightImageElem.textContent.replace(".png", "-fav.png");
			}

			// 3. Hide scores
			// 4. Use the preview link with no scores
			var showScores = (atv.localStorage[SHOW_SCORES_KEY]) ? atv.localStorage[SHOW_SCORES_KEY] : "true";
			var rightLabelElem = menu.getElementByName("rightLabel");
			if (showScores == "false")
			{
				var previewLinkElem = menu.getElementByTagName("link");
				if (rightLabelElem && rightLabelElem.textContent.indexOf("Final") >= 0)
				{
					rightLabelElem.textContent = "Final";
				}
				if (previewLinkElem)
				{
					previewLinkElem.textContent = previewLinkElem.textContent.replace(".xml", "_noscores.xml");
				}
			}

			// 5. Check for double header 3:33 AM ET
			if (rightLabelElem && rightLabelElem.textContent.indexOf("3:33 AM") >= 0)
			{
				rightLabelElem.textContent = "Game 2";
			}
		}

		return responseXML;
	}
	catch (error)
	{
		console.error("unable to apply todays games " + error);
		atvutils.loadError("An error occurred loading today's games", "An error occurred loading today's games");
	}
}

/**
 * Should be called by the onRefresh event, I am also calling this from the handleNavigate function below to share code. Essentially this function: 1. Loads the current Navigation file 2. Pulls the
 * menu items out of the loaded file and dumps them into the current file 3. Checks to see if an onRefresh AJAX call is already being made and if so does nothing.
 * 
 * 
 * @params string url - url to be loaded
 */
function handleTodaysGamesRefresh()
{
	var url = atv.localStorage[TODAYS_GAMES_REFRESH_URL_KEY];
	console.log("handleTodaysGamesRefresh() url " + url);

	console.log("We are about to check the value of Ajax.currentlyRefreshing: --> " + Ajax.currentlyRefreshing);
	// check the Ajax object and see if a refresh event is happening
	if (!Ajax.currentlyRefreshing)
	{
		// if a refresh event is not happening, make another one.
		console.log("handleTodaysGamesRefresh()About to make the AJAX call. --> ")

		var refresh = new Ajax({
			"url" : url,
			"refresh" : true,
			"success" : function()
			{
				var req = arguments[0];

				console.log("handleTodaysGamesRefresh() Ajax url " + url);

				var newDocument = atv.parseXML(req.responseText); // Instead of using the responseXML you should parse the responseText. I know it seems redundent but trust me.

				// update the content page
				var newMenuSections = newDocument.rootElement.getElementByTagName('sections');
				applyTodaysGames(newDocument, true);
				newMenuSections.removeFromParent();

				var menu = document.rootElement.getElementByTagName('menu');
				console.log("handleTodaysGamesRefresh() menu=" + menu);
				if (menu)
				{
					var oldMenuSections = menu.getElementByTagName('sections');
					menu.replaceChild(oldMenuSections, newMenuSections);
				}
				else
				{
					menu = atv.parseXML("<menu></menu>").rootElement;
					menu.removeFromParent();
					menu.appendChild(newMenuSections);

					var listByNavigation = document.rootElement.getElementByTagName("listByNavigation");
					listByNavigation.appendChild(menu);
				}
				
				// replace # Games
				var newTumbler = newDocument.rootElement.getElementByTagName('tumblerWithSubtitle');
				newTumbler.removeFromParent();
				var header = document.rootElement.getElementByTagName('header');
				var oldTumbler = document.rootElement.getElementByTagName('tumblerWithSubtitle');
				header.replaceChild(oldTumbler, newTumbler);
			}
		});
		console.log("handleTodaysGamesRefresh() I have made the AJAX call. <--");
	}
	console.log("handleTodaysGamesRefresh() I am on the other side of the if. <--");
}

/**
 * This function works very similar to the handleRefresh Essentially we are manually loading the data and updating the page in Javascript This means we have to handle a bit more of the magic, but we
 * also get to control what is updated and update more of the page, like headers, previews, etc.
 * 
 * In this example, I am: 1. Getting the new URL from the Navigation Item 2. Changing the onRefresh URL 3. Calling handleRefresh and passing the new url to let handleRefresh do the magic.
 */
function handleTodaysGamesNavigate(e)
{
	console.log("handleTodaysGamesNavigate() " + e + ", e.navigationItemId = " + e.navigationItemId + " document = " + document);
	console.log("e=" + e);
	atvutils.printObject(e);
	console.log("document.getElementById(e.navigationItemId)=" + document.getElementById(e.navigationItemId));
	console.log("document.getElementById(e.navigationItemId).getElementByTagName=" + document.getElementById(e.navigationItemId).getElementByTagName);

	// get the url
	var url = document.getElementById(e.navigationItemId).getElementByTagName('url').textContent;
	atv.localStorage[TODAYS_GAMES_REFRESH_URL_KEY] = url;

	handleTodaysGamesRefresh();

	e.onCancel = function()
	{
		// Cancel any outstanding requests related to the navigation event.
		console.log("Navigation got onCancel.");
	}
}

function applyFeedSelectionPage(document)
{
	console.log("applyFeedSelectionPage() document: " + document);

	// add behavior by adding javascript file reference
	var head = document.rootElement.getElementByTagName("head");
	if (head)
	{
		var serviceJs = atv.parseXML('<script src="' + config.appletvBase + '/js/service.js" />').rootElement;
		serviceJs.removeFromParent();
		console.debug("adding service.js " + serviceJs);

		var children = head.childElements;
		children.unshift(serviceJs);

		for ( var i = 0; i < children.length; i++)
		{
			children[i].removeFromParent();
			head.appendChild(children[i]);
		}
	}

	flowManager.endMediaFlow();
	flowManager.endPurchaseFlow();
	console.log("purchase and media flow ended " + atv.localStorage[PURCHASE_PRODUCT_ID_KEY] + " " + atv.localStorage[MEDIA_META_REQUEST_PARAMS_KEY] + " " + atv.localStorage[MEDIA_META_KEY] + " " + atv.localStorage[SESSION_KEY]);

	return document;
}

// This is called from the feed_selection.xml
function loadFeed(mediaMeta)
{
	atv._debugDumpControllerStack();

	console.debug("showing new page..");
	var newPage = new atv.ProxyDocument();
	newPage.show();

	atv._debugDumpControllerStack();

	// store the media meta into the local storage for later use
	serviceClient.startMediaFlow(mediaMeta);
	atv.localStorage[OFFER_URI_AFTER_AUTH_KEY] = config.appletvBase + "/views/MediaServiceProxy?encode(MEDIA_META_REQUEST_PARAMS)";
	atv.localStorage[OFFER_PURCHASE_FUNCTION_KEY] = "restorePurchasePlay";
	atv.localStorage[OFFER_SWAP_PAGE_KEY] = "true";

	// load the media page
	serviceClient.loadMediaPage(function(document)
	{
		// media proxy returns a playback page, display
		var authorized = !serviceClient.isMediaResponseUnauthorized(document);
		// atv.localStorage.setItem(AUTHORIZED_KEY, (authorized) ? "true" : "false");
		if (authorized)
		{
			// if authorized, load the page
			console.debug("loadFeed() showing the media page");
			newPage.swapXML(document);
		}
		else
		{
			// if unauthorized, load the offer page
			console.debug("loadFeed() showing the offer page swap " + atv.localStorage[OFFER_SWAP_PAGE_KEY]);
			serviceClient.loadOfferPage(newPage);
		}
	});
}
// deprecated function name but it's still being generated by the gameday mule
var loadLiveGameVideoAsset = loadFeed;

function loadOfferPageAndPlay()
{
	var newPage = new atv.ProxyDocument();
	newPage.show();
	serviceClient.loadOfferPage(newPage);
}

function subscribeToMlbTv()
{
	console.debug("subscribeToMlbTv() enter");

	flowManager.endMediaFlow();

	// create new page where offer page will be loaded onto
	var newPage = new atv.ProxyDocument();
	newPage.show();

	// load the offer page onto the new page
	atv.localStorage[OFFER_URI_AFTER_AUTH_KEY] = config.appletvBase + "/views/Dialog?messageCode=AUTH_MESSAGE_CODE";
	atv.localStorage[OFFER_PURCHASE_FUNCTION_KEY] = "restoreAndPurchase";
	atv.localStorage[OFFER_SWAP_PAGE_KEY] = "false";
	serviceClient.loadOfferPage(newPage);
}

function ErrorToDebugString(error)
{
	var localizedDescription = error.userInfo["NSLocalizedDescription"];
	localizedDescription = localizedDescription ? localizedDescription : "Unknown";
	return error.domain + "(" + error.code + "): " + localizedDescription;
}

function linkManually()
{
	atv.loadURL(config.appletvBase + "/views/Login?urlToLoadAfterAuth=" + config.appletvBase + "/views/LinkProxy?request=encode(LINK_PROXY_REQUEST)");
}

function purchase(newPage)
{
	var observer = serviceClient.addPurchaseObserver(function(purchasedTransaction)
	{
		// PURCHASE success
		console.debug("purchase(): success purchased transactions " + purchasedTransaction);

		// link()
		serviceClient.doLink([ purchasedTransaction ], function()
		{
			console.debug("purchase(): link success");
			serviceClient.loadUserAuthorized(function()
			{
				console.debug("purchase(): load user authorized");
				newPage.loadXML(atvutils.makeOkDialog("MLB.TV Purchased", "You have successfully purchased MLB.TV."));
				atv.unloadPage();
			});
		}, function()
		{
			console.debug("purchase(): link error after purchase");
			newPage.cancel();
			atv.unloadPage();
			// do nothing -- go back to previous page
			// atv.loadAndSwapXML(atvutils.makeOkDialog("MLB.TV Error", "Failed to determine your MLB.TV subscription after purchase"));
		})
	}, function()
	{
		// PURCHASE error
		console.debug("purchase(): error ");
		newPage.cancel();
		atv.unloadPage();
	});

	newPage.onCancel = function()
	{
		atv.SKDefaultPaymentQueue.removeTransactionObserver(observer);
	};

	serviceClient.startPurchase();

}

function restoreAndPurchase(productId)
{
	atv._debugDumpControllerStack();

	// store product id into the cart
	atv.localStorage[PURCHASE_PRODUCT_ID_KEY] = productId;

	// create and show new page where dialogs will be loaded
	var newPage = new atv.ProxyDocument();
	newPage.show();

	atv._debugDumpControllerStack();

	var restoreObserver = serviceClient.addRestoreObserver(function(transactions)
	{
		// link()
		serviceClient.doLink(transactions, function()
		{
			// LINK success -- determine authorization thru feature service
			atv._debugDumpControllerStack();
			serviceClient.loadUserAuthorized(function()
			{
				console.debug("restoreAndPurchase() atv.localStorage[AUTHORIZED_KEY] " + atv.localStorage[AUTHORIZED_KEY]);
				if (atv.localStorage[AUTHORIZED_KEY] == "true")
				{
					console.debug("restoreAndPurchase() authorized or error page, showing Success page...");
					atv._debugDumpControllerStack();
					newPage.cancel();
					atv.loadAndSwapXML(atvutils.makeOkDialog("Success", "You are now authorized to watch MLB.TV.  Please select a game from Today's Games on the main menu."));
					console.debug("restoreAndPurchase() after showing Success page...");
					atv._debugDumpControllerStack();
				}
				else
				{
					// unauthorized, purchase
					console.debug("restoreAndPurchase() unauthorized, purchasing...");
					purchase(newPage);
					atv._debugDumpControllerStack();
				}
			});
		}, function()
		{
			// LINK error -- attempt to purchase now
			console.debug("restoreAndPurchase() link error - make the purchase ");
			purchase(newPage);
			atv._debugDumpControllerStack();
		});
	}, function(error)
	{
		newPage.cancel();
		if (error && error.code == -500)
		{
			atv.loadAndSwapXML(atvutils.makeOkDialog("MLB.TV Error", "Failed to determine your subscription."));
		}
		else
		{
			newPage.swapXML(document);
		}
		console.error("restoreAndPurchase() restore error - failed to restore " + document);
		atv._debugDumpControllerStack();
	});

	newPage.onCancel = function()
	{
		atv.SKDefaultPaymentQueue.removeTransactionObserver(restoreObserver);
	};

	// Restore completed transactions to get transactions purchased on other devices for this account.
	console.debug("restoreAndPurchase() Restoring completed transactions...");
	atv.SKDefaultPaymentQueue.restoreCompletedTransactions();
}

function purchaseAndPlay(newPage)
{
	atv._debugDumpControllerStack();

	var observer = serviceClient.addPurchaseObserver(function(purchasedTransaction)
	{
		// PURCHASE success
		console.debug("purchase(): success purchased transactions " + purchasedTransaction);
		atv._debugDumpControllerStack();

		// link()
		serviceClient.doLink([ purchasedTransaction ], function()
		{
			console.debug("purchase(): link success");
			atv._debugDumpControllerStack();

			serviceClient.loadMediaPage(function(mediaDoc)
			{
				console.debug("purchase() loadMediaPage");
				newPage.swapXML(mediaDoc);
				atv._debugDumpControllerStack();
			});

		}, function()
		{
			console.debug("purchase(): link error after purchase");
			newPage.swapXML(atvutils.makeOkDialog("MLB.TV Link Error", "Failed to link your MLB.TV account properly after purchase"));
			atv._debugDumpControllerStack();
		})

	}, function(transaction)
	{
		// PURCHASE error
		console.debug("purchase(): error " + (transaction) ? transaction.error : null);
		if (!transaction || transaction.error != 2)
		{
			newPage.swapXML(atvutils.makeOkDialog("MLB.TV Purchase Error", "The purchase failed.  Please try again at a later time."));
		}
		atv._debugDumpControllerStack();
	});

	newPage.onCancel = function()
	{
		atv.SKDefaultPaymentQueue.removeTransactionObserver(observer);
	};

	serviceClient.startPurchase();
}

function restorePurchasePlay(productId)
{
	// store product id into the cart
	atv.localStorage[PURCHASE_PRODUCT_ID_KEY] = productId;

	var newPage = new atv.ProxyDocument();
	newPage.show();

	atv._debugDumpControllerStack();

	var restoreObserver = serviceClient.addRestoreObserver(function(transactions)
	{
		// link()
		serviceClient.doLink(transactions, function()
		{
			// LINK success -- determine whether to purchase
			serviceClient.loadMediaPage(function(document)
			{
				atv._debugDumpControllerStack();
				var authorized = !serviceClient.isMediaResponseUnauthorized(document);
				console.debug("restorePurchasePlay() authorized " + authorized);
				atv.localStorage.setItem(AUTHORIZED_KEY, (authorized) ? "true" : "false");
				console.debug("restorePurchasePlay() atv.localStorage[AUTHORIZED] " + atv.localStorage[AUTHORIZED_KEY]);
				if (authorized)
				{
					// authorized or media playback error (session key problem, media unavailable, etc.), display the page
					console.debug("restorePurchasePlay() authorized or error page, showing media proxy page...");
					newPage.swapXML(atvutils.makeOkDialog("MLB.TV Subscriber", "You have purchased this content before and already have access to watch MLB.TV content.", "play();"));
					atv._debugDumpControllerStack();
					console.debug("restorePurchasePlay() after showing media proxy page...");
				}
				else
				{
					// unauthorized, purchase
					console.debug("restorePurchasePlay() unauthorized, purchasing...");
					purchaseAndPlay(newPage);
					atv._debugDumpControllerStack();
				}
			});
		}, function()
		{
			// LINK error -- attempt to purchase now
			console.debug("restorePurchasePlay() link error - make the purchase ");
			purchaseAndPlay(newPage);
		});
	}, function(error)
	{
		if (error && error.code == -500)
		{
			newPage.swapXML(atvutils.makeOkDialog("MLB.TV Error", "Failed to determine your subscription."));
		}
		else
		{
			newPage.swapXML(document);
		}
	});

	newPage.onCancel = function()
	{
		atv.SKDefaultPaymentQueue.removeTransactionObserver(restoreObserver);
	};

	// Restore completed transactions to get transactions purchased on other devices for this account.
	console.debug("restorePurchasePlay() Restoring completed transactions...");
	atv.SKDefaultPaymentQueue.restoreCompletedTransactions();
}

function play(newPage)
{
	serviceClient.loadMediaPage(function(document)
	{
		console.debug("play() show the media proxy page..." + newPage);
		atv._debugDumpControllerStack();
		if (newPage)
		{
			newPage.swapXML(document);
			atv.unloadPage();
		}
		else
		{
			atv.loadAndSwapXML(document);
		}
		atv._debugDumpControllerStack();
		console.debug("play() after showing media proxy page...");
	});
}

function restoreManually()
{
	console.debug("restore receipts from iTunes account manually");

	var newPage = new atv.ProxyDocument();
	newPage.show();

	var restoreObserver = serviceClient.addRestoreObserver(function(transactions)
	{
		// restore transaction callback
		console.log("restoreManually() transactions " + transactions);
		if (transactions)
		{
			console.debug("restoreManually() successfully restored receipts from iTunes account manually");
			serviceClient.doLink(transactions, function()
			{
				console.debug("restoreManually() link success callback - determine authorization from feature service");
				serviceClient.loadUserAuthorized(function()
				{
					newPage.loadXML(atvutils.makeOkDialog("Restore Successful", "Your subscription has been restored."));
				});
				console.debug("restoreManually() link service success");
			}, function()
			{
				console.debug("restoreManually() link service error");
			});
		}
		else
		{
			console.debug("restoreManually() missing transaction - determine authorization from feature service");
			newPage.loadXML(atvutils.makeOkDialog("Not an MLB.TV subscriber", "You have not purchased a MLB.TV subscription on AppleTV."));
		}
	}, function(error)
	{
		// restore error callback
		console.debug("restoreManually() error attempting to restore receipts from iTunes account manually");

		if (!error || error.code != -500)
		{
			newPage.cancel();
		}
		else
		{
			newPage.loadXML(atvutils.makeOkDialog("Restore Unsuccessful", "An error occurred attempting to restore your MLB.TV account."));
		}
	});

	newPage.onCancel = function()
	{
		atv.SKDefaultPaymentQueue.removeTransactionObserver(restoreObserver);
	};

	// Restore completed transactions to get transactions purchased on other devices for this account.
	console.debug("restoreManually() Restoring completed transactions...");
	atv.SKDefaultPaymentQueue.restoreCompletedTransactions();
}

function restoreOnOffer()
{
	console.debug("restoreOnOffer() restore receipts from iTunes account manually");

	var newPage = new atv.ProxyDocument();
	newPage.show();

	var restoreObserver = serviceClient.addRestoreObserver(function(transactions)
	{
		// restore success callback
		console.log("restoreManually() transaction " + transactions);
		if (transactions)
		{
			console.debug("successfully restored receipts from iTunes account manually");
			serviceClient.doLink(transactions, function()
			{
				// link success callback
				if (flowManager.inMediaFlow())
				{
					console.debug("restoreOnOffer() in media flow, playing media");
					play(newPage);
				}
				else
				{
					console.debug("restoreOnOffer() NOT in media flow, display info message");
					serviceClient.loadUserAuthorized(function()
					{
						console.debug("restoreOnOffer(): load user authorized");
						newPage.swapXML(atvutils.makeOkDialog("Restore Successful", "Your subscription has been restored."));
						atv.unloadPage();
					});
				}
			}, function()
			{
				// link error callback
				newPage.loadXML(atvutils.makeOkDialog("Restore Unsuccessful", "There was a problem restoring your iTunes account. Please try again at a later time."));
			});
		}
		else
		{
			newPage.loadXML(atvutils.makeOkDialog("Not an MLB.TV subscriber", "You have not purchased a MLB.TV subscription on AppleTV."));
		}
	}, function(error)
	{
		// restore error callback
		console.debug("error attempting to restore receipts from iTunes account manually");

		if (!error || error.code != -500)
		{
			newPage.cancel();
		}
		else
		{
			newPage.loadXML(atvutils.makeOkDialog("Restore Unsuccessful", "An error occurred attempting to restore your MLB.TV account."));
		}
	});

	newPage.onCancel = function()
	{
		atv.SKDefaultPaymentQueue.removeTransactionObserver(restoreObserver);
	};

	// Restore completed transactions to get transactions purchased on other devices for this account.
	console.debug("restoreManually() Restoring completed transactions...");
	atv.SKDefaultPaymentQueue.restoreCompletedTransactions();
}

function applySettingsPage(document)
{
	// update logo image resolution
	updateImageResolution(document);

	// show scores menu item
	var showScores = atv.localStorage[SHOW_SCORES_KEY];
	var menuItem = document.getElementById("showScoresMenuItem");
	if (menuItem)
	{
		var label = menuItem.getElementByName('rightLabel');
		label.textContent = (!showScores || showScores == "true") ? "Show" : "Hide";
		console.debug("label.textContent: " + label.textContent);
	}

	// menu items
	var parent = document.rootElement.getElementByTagName("items");
	var signInMenu = document.getElementById("signInOutMenuItem");
	var restoreMenu = document.getElementById("restoreSub");
	var linkAcctMenu = document.getElementById("linkAcct");
	var showScoresMenu = document.getElementById("showScoresMenuItem");
	console.debug("applySettingsPage() parent " + parent);

	// restore menu item
	var showRestoreMenu = atv.localStorage[IOS_IDENTITY_POINT_KEY] == null && atv.localStorage[IOS_FINGERPRINT_KEY] == null;
	console.debug("applySettingsPage() showRestoreMenu " + showRestoreMenu + " restoreMenu " + restoreMenu + " atv.localStorage[IOS_IDENTITY_POINT_KEY] " + atv.localStorage[IOS_IDENTITY_POINT_KEY]);
	if (showRestoreMenu)
	{
		if (!restoreMenu)
		{
			var parent = document.rootElement.getElementByTagName("items");
			console.debug("applySettingsPage() parent " + parent);
			if (parent && parent.childElements)
			{
				restoreMenu = atv.parseXML('<oneLineMenuItem id="restoreSub" accessibilityLabel="Restore Subscription" onSelect="restoreManually();"><label>Restore Subscription</label></oneLineMenuItem>').rootElement;
				console.debug("applySettingsPage() adding restoreMenu " + restoreMenu);
				restoreMenu.removeFromParent();
				parent.appendChild(restoreMenu);
				if (linkAcctMenu)
				{
					linkAcctMenu.removeFromParent();
					parent.appendChild(linkAcctMenu);
				}
				showScoresMenu.removeFromParent();
				parent.appendChild(showScoresMenu);
			}
		}
	}
	else if (restoreMenu)
	{
		restoreMenu.removeFromParent();
	}

	// link accounts menu item
	var showLinkMenu = atv.localStorage[EMAIL_LINKED_KEY] != "true" && atv.localStorage[IOS_IDENTITY_POINT_KEY] != null && atv.localStorage[IOS_FINGERPRINT_KEY] != null;
	console.debug("applySettingsPage() atv.localStorage[EMAIL_LINKED_KEY] " + atv.localStorage[EMAIL_LINKED_KEY] + " showLinkMenu " + showLinkMenu + " atv.localStorage[IOS_IDENTITY_POINT_KEY] " + atv.localStorage[IOS_IDENTITY_POINT_KEY] + " linkAcctMenu " + linkAcctMenu);
	if (showLinkMenu)
	{
		// add the menu if it's not there
		if (!linkAcctMenu)
		{
			if (parent && parent.childElements)
			{
				console.debug("applySettingsPage() before parent.childElements " + parent.childElements);

				linkAcctMenu = atv.parseXML('<oneLineMenuItem id="linkAcct" accessibilityLabel="Link MLB.TV Account" onSelect="linkManually();"><label>Link Account</label></oneLineMenuItem>').rootElement;
				linkAcctMenu.removeFromParent();

				showScoresMenu.removeFromParent();
				parent.appendChild(linkAcctMenu);
				parent.appendChild(showScoresMenu);

				console.debug("applySettingsPage() after parent.childElements " + parent.childElements);
			}
		}

		if (signInMenu)
		{
			signInMenu.removeFromParent();
		}
	}
	else
	{
		// remove the menu
		if (linkAcctMenu)
		{
			linkAcctMenu.removeFromParent();
		}

		if (!signInMenu)
		{
			signInMenu = atv.parseXML('\
				<signInSignOutMenuItem id=\"signInOutMenuItem\" accessibilityLabel=\"Sign out\" signOutExitsApp=\"false\">\
					<signInPageURL>' + config.appletvBase + '/views/Login</signInPageURL>\
					<confirmation>\
						<title>Are You Sure?</title>\
						<description>The account %@ will be logged out of the service.</description>\
					</confirmation>\
				</signInSignOutMenuItem>').rootElement;
			signInMenu.removeFromParent();

			parent.appendChild(signInMenu);
			if (showRestoreMenu && restoreMenu)
			{
				parent.appendChild(restoreMenu);
			}
			showScoresMenu.removeFromParent();
			parent.appendChild(showScoresMenu);
		}
	}

	return document;
}

function applyRecapsPage(responseXML)
{
	// update logo image resolution
	updateImageResolution(responseXML);

	return responseXML;
}

function applyTeamsPage(responseXML)
{
	try
	{
		// update logo image resolution
		updateImageResolution(responseXML);

		var favTeamIds = getFavoriteTeamIds();

		console.debug('sorting teams.. ' + responseXML.rootElement);
		var gridList = responseXML.rootElement.getElementByName("body").getElementByName("scroller").getElementByName("items").getElementsByName("grid");
		var gridIndex = 0;
		for (; gridIndex < gridList.length; gridIndex++)
		{
			// function to sort by title
			function sortByTitle(poster1, poster2)
			{
				var favTeamIds = (atv.localStorage[FAVORITE_TEAMS_KEY]) ? atv.localStorage[FAVORITE_TEAMS_KEY] : [];

				var team1Id = poster1.getAttribute("id");
				var is1Fav = favTeamIds.indexOf(team1Id) >= 0;
				var title1 = poster1.getElementByName("title").textContent;
				title1 = (is1Fav) ? title1.toUpperCase() : title1.toLowerCase();

				var team2Id = poster2.getAttribute("id");
				var is2Fav = favTeamIds.indexOf(team2Id) >= 0;
				var title2 = poster2.getElementByName("title").textContent;
				title2 = (is2Fav) ? title2.toUpperCase() : title2.toLowerCase();

				var result = (title1 > title2) ? 1 : -1;
				// console.debug('result: ' + title1 + ">" + title2 + " = " + result);

				return result;
			}

			// parent
			var parent = gridList[gridIndex].getElementByName("items");

			// sort the posters
			var orderedPosterList = parent.childElements.sort(sortByTitle);

			// re-append the child
			var index = 0;
			for (; index < orderedPosterList.length; index++)
			{
				var poster = orderedPosterList[index];
				var teamId = poster.getAttribute("id");
				poster.removeFromParent();
				parent.appendChild(poster);

				var isFav = favTeamIds.indexOf(teamId) >= 0;

				var imageElem = poster.getElementByName("image");
				if (isFav && imageElem.textContent.indexOf("-fav.png") < 0)
				{
					imageElem.textContent = imageElem.textContent.replace(".png", "-fav.png");
				}
				else if (!isFav && imageElem.textContent.indexOf("-fav.png") > 0)
				{
					imageElem.textContent = imageElem.textContent.replace("-fav.png", ".png");
				}
			}

		}

		console.debug('teams are sorted and favorite team logos in place');
		return responseXML;

	}
	catch (error)
	{
		console.debug("Unable to sort the teams: " + error);
	}
}

function applyGamesPage(responseXML)
{
	console.debug("applyeGamesPage() responseXML = " + responseXML);
	try
	{
		// determine if favorite team
		var listElem = responseXML.rootElement.getElementByName("body").getElementByName("listByNavigation");
		var pageId = listElem.getAttribute("id");
		var teamId = pageId.substring(pageId.lastIndexOf(".") + 1);
		var isFavorite = getFavoriteTeamIds().indexOf(teamId) > -1;
		console.debug("applyGamesPage() teamId: " + teamId + " isFavorite: " + isFavorite);

		var favButton = responseXML.getElementById("favoriteButton");
		var favoriteButtonLabel = favButton.getElementByName("label");
		console.debug("favoriteButtonLabel=" + favoriteButtonLabel);
		if (isFavorite)
		{
			var newLabel = "Remove from Favorites";
			favButton.setAttribute('accessibilityLabel', newLabel);
			favoriteButtonLabel.textContent = newLabel;
			console.debug("changing to Remove From Favorites favoriteButtonLabel.textContent=" + favoriteButtonLabel.textContent);
		}
		else
		{
			var newLabel = "Add to Favorites";
			favButton.setAttribute('accessibilityLabel', newLabel);
			favoriteButtonLabel.textContent = newLabel;
			console.debug("changing to Add to Favorites favoriteButtonLabel.textContent=" + favoriteButtonLabel.textContent);
		}

		return responseXML;
	}
	catch (error)
	{
		console.log("Unable to apply changes to the games page: " + error);
	}
}

/**
 * Should be called by the onRefresh event, I am also calling this from the handleNavigate function below to share code. Essentially this function: 1. Loads the current Navigation file 2. Pulls the
 * menu items out of the loaded file and dumps them into the current file 3. Checks to see if an onRefresh AJAX call is already being made and if so does nothing.
 * 
 * 
 * @params string url - url to be loaded
 */
function handleGamesPageRefresh(urlParam)
{
	var url = atv.localStorage[GAMES_PAGE_REFRESH_URL_KEY];
	if (url == null)
	{
		url = urlParam;
	}
	
	console.log("Handle refresh event called");

	console.log("We are about to check the value of Ajax.currentlyRefreshing: --> " + Ajax.currentlyRefreshing);
	// check the Ajax object and see if a refresh event is happening
	if (!Ajax.currentlyRefreshing)
	{

		// if a refresh event is not happening, make another one.
		console.log("About to make the AJAX call. --> ")

		var refresh = new Ajax({
			"url" : url,
			"refresh" : true,
			"success" : function()
			{
				var req = arguments[0];

				// update the content page
				var newDocument = atv.parseXML(req.responseText), // Instead of using the responseXML you should parse the responseText. I know it seems redundent but trust me.
				menu = document.rootElement.getElementByTagName('menu'), oldMenuSections = menu.getElementByTagName('sections'), newMenuSections = newDocument.rootElement.getElementByTagName('sections'), now = new Date();

				applyGamesPage(newDocument);

				newMenuSections.removeFromParent();
				menu.replaceChild(oldMenuSections, newMenuSections);

			}
		});

		console.debug("I have made the AJAX call. <--");
	}

	console.debug("I am on the other side of the if. <--");

}

/**
 * This function works very similar to the handleRefresh Essentially we are manually loading the data and updating the page in Javascript This means we have to handle a bit more of the magic, but we
 * also get to control what is updated and update more of the page, like headers, previews, etc.
 * 
 * In this example, I am: 1. Getting the new URL from the Navigation Item 2. Changing the onRefresh URL 3. Calling handleRefresh and passing the new url to let handleRefresh do the magic.
 */
function handleGamesPageNavigate(e)
{
	console.log("Handle navigate event called: " + e + ", e.navigationItemId = " + e.navigationItemId + " document = " + document);

	// get the url
	var url = document.getElementById(e.navigationItemId).getElementByTagName('url').textContent;
	atv.localStorage[GAMES_PAGE_REFRESH_URL_KEY] = url;

	handleGamesPageRefresh(url);

	e.onCancel = function()
	{
		// Cancel any outstanding requests related to the navigation event.
		console.log("Navigation got onCancel.");
	}
}

function getFavoriteTeamIds()
{
	var favTeamIds = atv.localStorage[FAVORITE_TEAMS_KEY];
	console.debug("favoriteTeamIds=" + favTeamIds);
	return (favTeamIds != null) ? favTeamIds : [];
}

function toggleFavoriteTeam(teamId)
{
	try
	{
		console.log("toggleFavoriteTeams " + teamId);
		var favoriteTeamIds = getFavoriteTeamIds();

		var isFavorite;
		if (favoriteTeamIds == null || favoriteTeamIds.length == 0)
		{
			// first favorite team
			favoriteTeamIds = [ teamId ];
			isFavorite = true;
		}
		else
		{
			var teamIdIndex = favoriteTeamIds.indexOf(teamId);
			if (teamIdIndex > -1)
			{
				// remove
				favoriteTeamIds.splice(teamIdIndex, 1);
				isFavorite = false;
			}
			else
			{
				// add
				favoriteTeamIds.push(teamId);
				isFavorite = true;
			}
		}

		atv.localStorage[FAVORITE_TEAMS_KEY] = favoriteTeamIds;

		var favoriteButton = document.getElementById("favoriteButton");
		if (favoriteButton)
		{
			var newLabel = (isFavorite) ? "Remove from Favorites" : "Add to Favorites";
			var label = favoriteButton.getElementByName("label");
			label.textContent = newLabel;
			console.debug("after label.textContent: " + label.textContent);
		}
	}
	catch (error)
	{
		console.error("Caught exception trying to toggle DOM element: " + error);
	}

	console.debug("toggleFavoriteTeam() end " + teamId);
}

function toggleScores(id)
{
	try
	{
		var menuItem = document.getElementById(id);
		if (menuItem)
		{
			var label = menuItem.getElementByName('rightLabel');
			if (label.textContent == "Show")
			{
				label.textContent = "Hide";
				atv.localStorage[SHOW_SCORES_KEY] = "false";
			}
			else
			{
				label.textContent = "Show";
				atv.localStorage[SHOW_SCORES_KEY] = "true";
			}

			console.debug("showScores: " + atv.localStorage[SHOW_SCORES_KEY]);
		}
	}
	catch (error)
	{
		console.error("Caught exception trying to toggle DOM element: " + error);
	}
}
console.log("~~~~~ MLB.TV main.js end");
HTTP/1.1 200 OK
Server: Oracle-iPlanet-Web-Server/7.0
Date: Wed, 08 Aug 2012 16:15:00 GMT
Content-type: text/html
Cache-control: max-age=3600000
Edge-control: max-age=3600000
Last-modified: Wed, 08 Aug 2012 16:15:00 GMT
Content-type: application/xml
Transfer-encoding: chunked

2000
<?xml version="1.0" encoding="UTF-8"?>
<atv>
	<head>
		<script src="http://lws.mlb.com/appletvv2/js/atvutils.js" />
		<script src="http://lws.mlb.com/appletvv2/js/main.js" />
	</head>
	<body>
		<scroller id="com.bamnetworks.appletv.teams" volatile="true" onVolatileReload="applyTeamsPage(document);atv.loadAndSwapXML(document);">
			<header>
				<simpleHeader accessibilityLabel="Team by Team">
					<title>Team By Team</title>
					<image required="true">http://mlb.mlb.com/mlb/images/flagstaff/logo/720p/LogoNoPad.png</image>
				</simpleHeader>
			</header>
			<items>
				<collectionDivider alignment="left" accessibilityLabel="American League">
					<title>American League</title>
				</collectionDivider>
				<grid id="americanLeagueTeams" columnCount="6">
					<items>
						
						<squarePoster id="110" alwaysShowTitles="true" accessibilityLabel="Baltimore Orioles" 
								onSelect="atvutils.loadAndApplyURL('http://lws.mlb.com/appletvv2/views/GamesByTeam?mode=recent&amp;team_id=110', applyGamesPage);" 
							>
							<title>Baltimore Orioles</title>
							<image>http://mlb.mlb.com/mlb/images/flagstaff/200x200/110.png</image>
							<defaultImage>resource://Square.png</defaultImage>
						</squarePoster>

						<squarePoster id="111" alwaysShowTitles="true" accessibilityLabel="Boston Red Sox" 
								onSelect="atvutils.loadAndApplyURL('http://lws.mlb.com/appletvv2/views/GamesByTeam?mode=recent&amp;team_id=111', applyGamesPage);" 
							>
							<title>Boston Red Sox</title>
							<image>http://mlb.mlb.com/mlb/images/flagstaff/200x200/111.png</image>
							<defaultImage>resource://Square.png</defaultImage>
						</squarePoster>

						<squarePoster id="145" alwaysShowTitles="true" accessibilityLabel="Chicago White Sox" 
								onSelect="atvutils.loadAndApplyURL('http://lws.mlb.com/appletvv2/views/GamesByTeam?mode=recent&amp;team_id=145', applyGamesPage);" 
							>
							<title>Chicago White Sox</title>
							<image>http://mlb.mlb.com/mlb/images/flagstaff/200x200/145.png</image>
							<defaultImage>resource://Square.png</defaultImage>
						</squarePoster>

						<squarePoster id="114" alwaysShowTitles="true" accessibilityLabel="Cleveland Indians" 
								onSelect="atvutils.loadAndApplyURL('http://lws.mlb.com/appletvv2/views/GamesByTeam?mode=recent&amp;team_id=114', applyGamesPage);" 
							>
							<title>Cleveland Indians</title>
							<image>http://mlb.mlb.com/mlb/images/flagstaff/200x200/114.png</image>
							<defaultImage>resource://Square.png</defaultImage>
						</squarePoster>

						<squarePoster id="116" alwaysShowTitles="true" accessibilityLabel="Detroit Tigers" 
								onSelect="atvutils.loadAndApplyURL('http://lws.mlb.com/appletvv2/views/GamesByTeam?mode=recent&amp;team_id=116', applyGamesPage);" 
							>
							<title>Detroit Tigers</title>
							<image>http://mlb.mlb.com/mlb/images/flagstaff/200x200/116.png</image>
							<defaultImage>resource://Square.png</defaultImage>
						</squarePoster>

						<squarePoster id="118" alwaysShowTitles="true" accessibilityLabel="Kansas City Royals" 
								onSelect="atvutils.loadAndApplyURL('http://lws.mlb.com/appletvv2/views/GamesByTeam?mode=recent&amp;team_id=118', applyGamesPage);" 
							>
							<title>Kansas City Royals</title>
							<image>http://mlb.mlb.com/mlb/images/flagstaff/200x200/118.png</image>
							<defaultImage>resource://Square.png</defaultImage>
						</squarePoster>

						<squarePoster id="108" alwaysShowTitles="true" accessibilityLabel="Los Angeles Angels" 
								onSelect="atvutils.loadAndApplyURL('http://lws.mlb.com/appletvv2/views/GamesByTeam?mode=recent&amp;team_id=108', applyGamesPage);" 
							>
							<title>Los Angeles Angels</title>
							<image>http://mlb.mlb.com/mlb/images/flagstaff/200x200/108.png</image>
							<defaultImage>resource://Square.png</defaultImage>
						</squarePoster>

						<squarePoster id="142" alwaysShowTitles="true" accessibilityLabel="Minnesota Twins" 
								onSelect="atvutils.loadAndApplyURL('http://lws.mlb.com/appletvv2/views/GamesByTeam?mode=recent&amp;team_id=142', applyGamesPage);" 
							>
							<title>Minnesota Twins</title>
							<image>http://mlb.mlb.com/mlb/images/flagstaff/200x200/142.png</image>
							<defaultImage>resource://Square.png</defaultImage>
						</squarePoster>

						<squarePoster id="147" alwaysShowTitles="true" accessibilityLabel="New York Yankees" 
								onSelect="atvutils.loadAndApplyURL('http://lws.mlb.com/appletvv2/views/GamesByTeam?mode=recent&amp;team_id=147', applyGamesPage);" 
							>
							<title>New York Yankees</title>
							<image>http://mlb.mlb.com/mlb/images/flagstaff/200x200/147.png</image>
							<defaultImage>resource://Square.png</defaultImage>
						</squarePoster>

						<squarePoster id="133" alwaysShowTitles="true" accessibilityLabel="Oakland Athletics" 
								onSelect="atvutils.loadAndApplyURL('http://lws.mlb.com/appletvv2/views/GamesByTeam?mode=recent&amp;team_id=133', applyGamesPage);" 
							>
							<title>Oakland Athletics</title>
							<image>http://mlb.mlb.com/mlb/images/flagstaff/200x200/133.png</image>
							<defaultImage>resource://Square.png</defaultImage>
						</squarePoster>

						<squarePoster id="136" alwaysShowTitles="true" accessibilityLabel="Seattle Mariners" 
								onSelect="atvutils.loadAndApplyURL('http://lws.mlb.com/appletvv2/views/GamesByTeam?mode=recent&amp;team_id=136', applyGamesPage);" 
							>
							<title>Seattle Mariners</title>
							<image>http://mlb.mlb.com/mlb/images/flagstaff/200x200/136.png</image>
							<defaultImage>resource://Square.png</defaultImage>
						</squarePoster>

						<squarePoster id="139" alwaysShowTitles="true" accessibilityLabel="Tampa Bay Rays" 
								onSelect="atvutils.loadAndApplyURL('http://lws.mlb.com/appletvv2/views/GamesByTeam?mode=recent&amp;team_id=139', applyGamesPage);" 
							>
							<title>Tampa Bay Rays</title>
							<image>http://mlb.mlb.com/mlb/images/flagstaff/200x200/139.png</image>
							<defaultImage>resource://Square.png</defaultImage>
						</squarePoster>

						<squarePoster id="140" alwaysShowTitles="true" accessibilityLabel="Texas Rangers" 
								onSelect="atvutils.loadAndApplyURL('http://lws.mlb.com/appletvv2/views/GamesByTeam?mode=recent&amp;team_id=140', applyGamesPage);" 
							>
							<title>Texas Rangers</title>
							<image>http://mlb.mlb.com/mlb/images/flagstaff/200x200/140.png</image>
							<defaultImage>resource://Square.png</defaultImage>
						</squarePoster>

						<squarePoster id="141" alwaysShowTitles="true" accessibilityLabel="Toronto Blue Jays" 
								onSelect="atvutils.loadAndApplyURL('http://lws.mlb.com/appletvv2/views/GamesByTeam?mode=recent&amp;team_id=141', applyGamesPage);" 
							>
							<title>Toronto Blue Jays</title>
							<image>http://mlb.mlb.com/mlb/images/flagstaff/200x200/141.png</image>
							<defaultImage>resource://Square.png</defaultImage>
						</squarePoster>

					</items>
				</grid>
				<collectionDivider alignment="left" accessibilityLabel="National League">
					<title>National League</title>
				</collectionDivider>
				<grid id="nationalLeagueTeams" columnCount="6">
					<items>
						
						<squarePoster id="109" alwaysShowTitles="true" accessibilityLabel="Arizona Diamondbacks" 
								onSelect="atvutils.loadAndApplyURL('http://lws.mlb.com/appletvv2/views/GamesByTeam?mode=recent&amp;team_id=109', applyGamesPage);" 
							>
							<title>Arizona Diamondbacks</title>
							<image>http://mlb.mlb.com/mlb/images/flagstaff/200x200/109.png</image>
							<defaultImage>resource://Square.png</defaultImage>
						</squarePoster>

						<squarePoster id="144" alwaysShowTitles="true" accessibilityLabel="Atlanta Braves" 
								onSelect="atvutils.loadAndApplyURL('http://lws.mlb.com/appletvv2/views/GamesByTeam?mode=recent&amp;team_id=144', applyGamesPage);" 
							>
							<title>Atlanta Braves</title>
							<image>http://mlb.mlb.com/mlb/images/flagstaff/200x200/144.png</image>
							<defaultImage>resource://Square.png</defaultImage>
						</squarePoster>

						<squarePoster id="112" alwaysShowTitles="true" accessibilityLabel="Chicago Cubs" 
								onSelect="atvutils.loadAndApplyURL('http://lws.mlb
17c5
.com/appletvv2/views/GamesByTeam?mode=recent&amp;team_id=112', applyGamesPage);" 
							>
							<title>Chicago Cubs</title>
							<image>http://mlb.mlb.com/mlb/images/flagstaff/200x200/112.png</image>
							<defaultImage>resource://Square.png</defaultImage>
						</squarePoster>

						<squarePoster id="113" alwaysShowTitles="true" accessibilityLabel="Cincinnati Reds" 
								onSelect="atvutils.loadAndApplyURL('http://lws.mlb.com/appletvv2/views/GamesByTeam?mode=recent&amp;team_id=113', applyGamesPage);" 
							>
							<title>Cincinnati Reds</title>
							<image>http://mlb.mlb.com/mlb/images/flagstaff/200x200/113.png</image>
							<defaultImage>resource://Square.png</defaultImage>
						</squarePoster>

						<squarePoster id="115" alwaysShowTitles="true" accessibilityLabel="Colorado Rockies" 
								onSelect="atvutils.loadAndApplyURL('http://lws.mlb.com/appletvv2/views/GamesByTeam?mode=recent&amp;team_id=115', applyGamesPage);" 
							>
							<title>Colorado Rockies</title>
							<image>http://mlb.mlb.com/mlb/images/flagstaff/200x200/115.png</image>
							<defaultImage>resource://Square.png</defaultImage>
						</squarePoster>

						<squarePoster id="117" alwaysShowTitles="true" accessibilityLabel="Houston Astros" 
								onSelect="atvutils.loadAndApplyURL('http://lws.mlb.com/appletvv2/views/GamesByTeam?mode=recent&amp;team_id=117', applyGamesPage);" 
							>
							<title>Houston Astros</title>
							<image>http://mlb.mlb.com/mlb/images/flagstaff/200x200/117.png</image>
							<defaultImage>resource://Square.png</defaultImage>
						</squarePoster>

						<squarePoster id="119" alwaysShowTitles="true" accessibilityLabel="Los Angeles Dodgers" 
								onSelect="atvutils.loadAndApplyURL('http://lws.mlb.com/appletvv2/views/GamesByTeam?mode=recent&amp;team_id=119', applyGamesPage);" 
							>
							<title>Los Angeles Dodgers</title>
							<image>http://mlb.mlb.com/mlb/images/flagstaff/200x200/119.png</image>
							<defaultImage>resource://Square.png</defaultImage>
						</squarePoster>

						<squarePoster id="146" alwaysShowTitles="true" accessibilityLabel="Miami Marlins" 
								onSelect="atvutils.loadAndApplyURL('http://lws.mlb.com/appletvv2/views/GamesByTeam?mode=recent&amp;team_id=146', applyGamesPage);" 
							>
							<title>Miami Marlins</title>
							<image>http://mlb.mlb.com/mlb/images/flagstaff/200x200/146.png</image>
							<defaultImage>resource://Square.png</defaultImage>
						</squarePoster>

						<squarePoster id="158" alwaysShowTitles="true" accessibilityLabel="Milwaukee Brewers" 
								onSelect="atvutils.loadAndApplyURL('http://lws.mlb.com/appletvv2/views/GamesByTeam?mode=recent&amp;team_id=158', applyGamesPage);" 
							>
							<title>Milwaukee Brewers</title>
							<image>http://mlb.mlb.com/mlb/images/flagstaff/200x200/158.png</image>
							<defaultImage>resource://Square.png</defaultImage>
						</squarePoster>

						<squarePoster id="121" alwaysShowTitles="true" accessibilityLabel="New York Mets" 
								onSelect="atvutils.loadAndApplyURL('http://lws.mlb.com/appletvv2/views/GamesByTeam?mode=recent&amp;team_id=121', applyGamesPage);" 
							>
							<title>New York Mets</title>
							<image>http://mlb.mlb.com/mlb/images/flagstaff/200x200/121.png</image>
							<defaultImage>resource://Square.png</defaultImage>
						</squarePoster>

						<squarePoster id="143" alwaysShowTitles="true" accessibilityLabel="Philadelphia Phillies" 
								onSelect="atvutils.loadAndApplyURL('http://lws.mlb.com/appletvv2/views/GamesByTeam?mode=recent&amp;team_id=143', applyGamesPage);" 
							>
							<title>Philadelphia Phillies</title>
							<image>http://mlb.mlb.com/mlb/images/flagstaff/200x200/143.png</image>
							<defaultImage>resource://Square.png</defaultImage>
						</squarePoster>

						<squarePoster id="134" alwaysShowTitles="true" accessibilityLabel="Pittsburgh Pirates" 
								onSelect="atvutils.loadAndApplyURL('http://lws.mlb.com/appletvv2/views/GamesByTeam?mode=recent&amp;team_id=134', applyGamesPage);" 
							>
							<title>Pittsburgh Pirates</title>
							<image>http://mlb.mlb.com/mlb/images/flagstaff/200x200/134.png</image>
							<defaultImage>resource://Square.png</defaultImage>
						</squarePoster>

						<squarePoster id="135" alwaysShowTitles="true" accessibilityLabel="San Diego Padres" 
								onSelect="atvutils.loadAndApplyURL('http://lws.mlb.com/appletvv2/views/GamesByTeam?mode=recent&amp;team_id=135', applyGamesPage);" 
							>
							<title>San Diego Padres</title>
							<image>http://mlb.mlb.com/mlb/images/flagstaff/200x200/135.png</image>
							<defaultImage>resource://Square.png</defaultImage>
						</squarePoster>

						<squarePoster id="137" alwaysShowTitles="true" accessibilityLabel="San Francisco Giants" 
								onSelect="atvutils.loadAndApplyURL('http://lws.mlb.com/appletvv2/views/GamesByTeam?mode=recent&amp;team_id=137', applyGamesPage);" 
							>
							<title>San Francisco Giants</title>
							<image>http://mlb.mlb.com/mlb/images/flagstaff/200x200/137.png</image>
							<defaultImage>resource://Square.png</defaultImage>
						</squarePoster>

						<squarePoster id="138" alwaysShowTitles="true" accessibilityLabel="St. Louis Cardinals" 
								onSelect="atvutils.loadAndApplyURL('http://lws.mlb.com/appletvv2/views/GamesByTeam?mode=recent&amp;team_id=138', applyGamesPage);" 
							>
							<title>St. Louis Cardinals</title>
							<image>http://mlb.mlb.com/mlb/images/flagstaff/200x200/138.png</image>
							<defaultImage>resource://Square.png</defaultImage>
						</squarePoster>

						<squarePoster id="120" alwaysShowTitles="true" accessibilityLabel="Washington Nationals" 
								onSelect="atvutils.loadAndApplyURL('http://lws.mlb.com/appletvv2/views/GamesByTeam?mode=recent&amp;team_id=120', applyGamesPage);" 
							>
							<title>Washington Nationals</title>
							<image>http://mlb.mlb.com/mlb/images/flagstaff/200x200/120.png</image>
							<defaultImage>resource://Square.png</defaultImage>
						</squarePoster>

					</items>
				</grid>
			</items>
		</scroller>
	</body>
</atv>
0

HTTP/1.1 200 OK
Server: Oracle-iPlanet-Web-Server/7.0
Date: Wed, 08 Aug 2012 16:15:02 GMT
Content-type: application/x-javascript
Last-modified: Mon, 23 Apr 2012 14:41:39 GMT
Content-length: 17889
Etag: "45e1-4f956a23"
Accept-ranges: bytes

// ***************************************************

var EMAIL_IDENTITY_POINT_KEY = "email.identityPointId";
var EMAIL_FINGERPRINT_KEY = "email.fingerprint";

// atv.Element extensions
atv.Element.prototype.getElementsByTagName = function(tagName) {
	return this.ownerDocument.evaluateXPath("descendant::" + tagName, this);
}

// Extend atv.ProxyDocument to load errors from a message and description.
atv.ProxyDocument.prototype.loadError = function(message, description) {
	var doc = atvutils.makeErrorDocument(message, description);
	this.loadXML(doc);
}

// Extend atv.ProxyDocument to load errors from a message and description.
atv.ProxyDocument.prototype.swapXML = function(xml) {
	var me = this;
    try 
    {
        me.loadXML(xml, function(success) {
            if ( success ) {
                atv.unloadPage();
            } else {
                console.log("swapXML failed to load " + xml);
                me.loadXML(atvutils.siteUnavailableError(), function(success) {
                	console.log("swapXML site unavailable error load success " + success);
                    if ( success ) {
                        atv.unloadPage();
                    }
                });
            }
        });
    } 
    catch (e) 
    {
        console.error("swapXML caught exception while loading " + xml + ". " + e);
        me.loadXML(atvutils.siteUnavailableError(), function(success) {
            if ( success ) {
                atv.unloadPage();
            }
        });
    }
}

// atv.Document extensions
atv.Document.prototype.getElementById = function(id) {
 var elements = this.evaluateXPath("//*[@id='" + id + "']", this);
 if ( elements && elements.length > 0 ) {
     return elements[0];
 }
 return undefined;
}

// atv.Element extensions
atv.Element.prototype.getElementsByTagName = function(tagName) {
 return this.ownerDocument.evaluateXPath("descendant::" + tagName, this);
}

atv.Element.prototype.getElementByTagName = function(tagName) {
 var elements = this.getElementsByTagName(tagName);
 if ( elements && elements.length > 0 ) {
     return elements[0];
 }
 return undefined;
}

// ATVUtils - a JavaScript helper library for Apple TV
ATVUtils = function() {};

ATVUtils.prototype.onGenerateRequest = function (request) {
	
    console.log('atvutils.js atv.onGenerateRequest() oldUrl: ' + request.url);

    console.debug("atvutils.js atv.onGenerateRequest() atv.localStorage[mediaMetaRequestParams] " + atv.localStorage[MEDIA_META_REQUEST_PARAMS_KEY]);
	if ( request.url.indexOf("encode(MEDIA_META_REQUEST_PARAMS)") > -1 && atv.localStorage[MEDIA_META_REQUEST_PARAMS_KEY])
	{
		request.url = request.url.replace("encode(MEDIA_META_REQUEST_PARAMS)", encodeURIComponent(atv.localStorage[MEDIA_META_REQUEST_PARAMS_KEY]));
	}
	console.debug("atvutils.js atv.onGenerateRequest() atv.localStorage[mediaMetaRequestParams] " + atv.localStorage[MEDIA_META_REQUEST_PARAMS_KEY]);
	if ( request.url.indexOf("MEDIA_META_REQUEST_PARAMS") > -1 && atv.localStorage[MEDIA_META_REQUEST_PARAMS_KEY])
	{
		request.url = request.url.replace("MEDIA_META_REQUEST_PARAMS", atv.localStorage[MEDIA_META_REQUEST_PARAMS_KEY]);
	}
	if ( request.url.indexOf("IPID") > -1 && atv.localStorage[BEST_IDENTITY_POINT_KEY])
	{
		request.url = request.url.replace("IPID", atv.localStorage[BEST_IDENTITY_POINT_KEY]);
	}
	if ( request.url.indexOf("FINGERPRINT") > -1 && atv.localStorage[BEST_FINGERPRINT_KEY])
	{
		request.url = request.url.replace("FINGERPRINT", encodeURIComponent(atv.localStorage[BEST_FINGERPRINT_KEY]));
	}
	if ( request.url.indexOf("SESSION_KEY") > -1 )
	{
		var sessionKey = atv.localStorage[SESSION_KEY]; 
		console.debug("atvutils.js atv.onGenerateRequest sessionKey=" + sessionKey);
		request.url = request.url.replace("SESSION_KEY", (sessionKey) ? encodeURIComponent(sessionKey) : "");
	}
	if ( request.url.indexOf("encode(LINK_PROXY_REQUEST)") > -1 && atv.localStorage[EMAIL_IDENTITY_POINT_KEY] != null && atv.localStorage[EMAIL_FINGERPRINT_KEY] != null)
	{
		var linkRequest = serviceClient.createLinkRequest();
		console.debug("atvutils.js atv.onGenerateRequest() linkRequest " + linkRequest);
		if (linkRequest != null)
		{
			// WARNING: encode twice because the xs:uri type does not suppot curly brackets so encode an extra time. The LinkProxy class will be responsible for url unencoding the json 
			request.url = request.url.replace("encode(LINK_PROXY_REQUEST)", encodeURIComponent(encodeURIComponent(JSON.stringify(linkRequest))));
		}
	}
	if ( request.url.indexOf("$$showScores") > -1 )
	{
		var showScores = atv.localStorage[SHOW_SCORES_KEY]; 
		console.log("showScores=" + showScores);
		request.url = request.url.replace("$$showScores", (showScores) ? showScores : "true");
	}
	var authorized = atv.localStorage[AUTHORIZED_KEY]; 
	if ( request.url.indexOf("AUTH_MESSAGE_CODE") > -1 && authorized != null)
	{
		console.debug("atvutils.js AUTH_MESSAGE_CODE atv.onGenerateRequest() authorized " + authorized);
//		request.url = request.url.replace("AUTH_MESSAGE_CODE", ((authorized == "true") ? "ALREADY_PURCHASED" : "NOT_AUHTORIZED"));
	}

	console.log('atvutils.js atv.onGenerateRequest() newUrl: ' + request.url);
}


ATVUtils.prototype.makeRequest = function(url, method, headers, body, callback) {
    if ( !url ) {
        throw "loadURL requires a url argument";
    }

	var method = method || "GET";
	var	headers = headers || {};
	var body = body || "";
	
    var xhr = new XMLHttpRequest();
    xhr.onreadystatechange = function() {
        try {
            if (xhr.readyState == 4 ) {
                if ( xhr.status == 200) {
                	console.log("makeRequest() getAllResponseHeaders(): " + xhr.getAllResponseHeaders());
                    callback(xhr.responseXML, xhr.responseText);
                } else {
                    console.log("makeRequest() received HTTP status " + xhr.status + " for " + url);
                    callback(xhr.responseXML, xhr.responseText);
                }
            }
        } catch (e) {
            console.error('makeRequest caught exception while processing request for ' + url + '. Aborting. Exception: ' + e);
            xhr.abort();
            callback(null, null);
        }
    }
    
    var request = {url: url};
    this.onGenerateRequest(request);
    
    console.debug("makeRequest() url " + request.url);

    xhr.open(method, request.url, true);

    for(var key in headers) {
        xhr.setRequestHeader(key, headers[key]);
    }

    console.debug("sending body in the send() method");
    xhr.send(body);
    return xhr;
}

ATVUtils.prototype.getRequest = function(url, callback) {
	return this.makeRequest(url, "GET", null, null, callback);
}

ATVUtils.prototype.postRequest = function(url, body, callback) {
	return this.makeRequest(url, "POST", null, body, callback);
}

ATVUtils.prototype.makeErrorDocument = function( onSelect="' + onSelect + '">\
				<label>OK</label>\
			</oneLineMenuItem>\
		</items></menuSection></sections></menu>\
		</optionDialog> \
		</body> \
		</atv>';
	
	var doc = atv.parseXML(errorXML);
	return doc;
}

ATVUtils.prototype.printObject = function(object) {
	for (property in object)
	{
		message, description) {
    if ( !message ) {
        message = "";
    }
    if ( !description ) {
        description = "";
    }

    var errorXML = '<?xml version="1.0" encoding="UTF-8"?> \
    <atv> \
        <body> \
            <dialog id="com.bamnetworks.errorDialog"> \
                <title><![CDATA[' + message + ']]></title> \
                <description><![CDATA[' + description + ']]></description> \
            </dialog> \
        </body> \
    </atv>';

    return atv.parseXML(errorXML);
}

ATVUtils.prototype.makeOkDialog = function(message, description, onSelect) {
	if ( !message ) {
		message = "";
	}
	if ( !description ) {
		description = "";
	}
	
	if (!onSelect)
	{
		onSelect = "atv.unloadPage();";
	}
	
	var errorXML = '<?xml version="1.0" encoding="UTF-8"?> \
		<atv> \
		<head> \
			<script src="'+config.appletvBase+'/views/config.js" />\
			<script src="'+config.appletvBase+'/js/atvutils.js" />\
			<script src="'+config.appletvBase+'/js/service.js" />\
			<script src="'+config.appletvBase+'/js/main.js" />\
		</head>\
		<body> \
		<optionDialog id="com.bamnetowrks.optionDialog"> \
		<header><simpleHeader><title>' + message + '</title></simpleHeader></header> \
		<description><![CDATA[' + description + ']]></description> \
		<menu><sections><menuSection><items> \
			<oneLineMenuItem id="lineMenu" accessibilityLabel="OK"console.log(property + ": " + object[property]);
	}
}


ATVUtils.prototype.siteUnavailableError = function() {
    // TODO: localize
	console.debug("siteUnavailableError()");
    return this.makeOkDialog("MLB.TV error", "MLB.TV is currently unavailable. Please try again later.");
}

ATVUtils.prototype.loadError = function(message, description) {
    atv.loadXML(this.makeErrorDocument(message, description));
}

ATVUtils.prototype.loadAndSwapError = function(message, description) {
    atv.loadAndSwapXML(this.makeErrorDocument(message, description));
}

ATVUtils.prototype.loadURLInternal = function(url, method, headers, body, loader) {
    var me = this;
    var xhr;
    var proxy = new atv.ProxyDocument;
    proxy.show();
    proxy.onCancel = function() {
        if ( xhr ) {
            xhr.abort();
        }
    };
    
    xhr = me.makeRequest(url, method, headers, body, function(xml, xhr) {
        try {
        	console.log("loadURLInternal() proxy=" + proxy + " xml=" + xml + " xhr=" + xhr);
            loader(proxy, xml, xhr);
        } catch(e) {
            console.error("Caught exception loading " + url + ". " + e);
            loader(proxy, me.siteUnavailableError(), xhr);
        }
    });
}

ATVUtils.prototype.loadGet = function(url, loader) {
	this.loadURLInternal(url, "GET", null, null, loader);
}

// TODO:: change to a url string or hash
ATVUtils.prototype.loadURL = function(url, method, headers, body, processXML) {
    var me = this;
    this.loadURLInternal(url, method, headers, body, function(proxy, xml) {
        if(typeof(processXML) == "function") processXML.call(this, xml);
		try {
            proxy.loadXML(xml, function(success) {
                if ( !success ) {
                    console.log("loadURL failed to load " + url);
                    proxy.loadXML(me.siteUnavailableError());
                }
            });
        } catch (e) {
            console.log("loadURL caught exception while loading " + url + ". " + e);
            proxy.loadXML(me.siteUnavailableError());
        }
    });
}

ATVUtils.prototype.swapXML = function(proxy, xml) {
	var me = this;
    try {
        proxy.loadXML(xml, function(success) {
            if ( success ) {
                atv.unloadPage();
            } else {
                console.log("swapXML failed to load " + xml);
                proxy.loadXML(me.siteUnavailableError(), function(success) {
                    if ( success ) {
                        atv.unloadPage();
                    }
                });
            }
        });
    } catch (e) {
        console.error("swapXML caught exception while loading " + xml + ". " + e);
        proxy.loadXML(me.siteUnavailableError(), function(success) {
            if ( success ) {
                atv.unloadPage();
            }
        });
    }
}

// TODO:: change to a url string or hash
// loadAndSwapURL can only be called from page-level JavaScript of the page that wants to be swapped out.
ATVUtils.prototype.loadAndSwapURL = function(url, method, headers, body, processXML) {
    var me = this;
    this.loadURLInternal(url, method, headers, body, function(proxy, xml) { 
	    if(typeof(processXML) == "function") processXML.call(this, xml);
        me.swapXML(proxy, xml);
    });
}

/** Load URL and apply changes to the xml */
ATVUtils.prototype.loadAndApplyURL = function(url, processXML) {
    var me = this;
	this.loadURLInternal(url, 'GET', null, null, function(proxy, xml, xhr) {
		var xmlToLoad = xml;
        if(typeof(processXML) == "function") xmlToLoad = processXML.call(this, xml);
        if (!xmlToLoad) xmlToLoad = xml;
		try {
            proxy.loadXML(xmlToLoad, function(success) {
                if ( !success ) {
                    console.log("loadURL failed to load " + url);
                    proxy.loadXML(me.siteUnavailableError());
                }
            });
        } catch (e) {
            console.log("loadURL caught exception while loading " + url + ". " + e);
            proxy.loadXML(me.siteUnavailableError());
        }
    });
}

var atvutils = new ATVUtils;

console.log("atvutils initialized " + atvutils);

/**
 * This is an AXR handler. It handles most of tediousness of the XHR request and keeps track of onRefresh XHR calls so that we don't end up with multiple page refresh calls.
 * 
 * You can see how I call it on the handleRefresh function below.
 * 
 * 
 * @params object (hash) $h
 * @params string $h.url - url to be loaded
 * @params string $h.method - "GET", "POST", "PUT", "DELTE"
 * @params bool $h.type - false = "Sync" or true = "Async" (You should always use true)
 * @params func $h.success - Gets called on readyState 4 & status 200
 * @params func $h.failure - Gets called on readyState 4 & status != 200
 * @params func $h.callback - Gets called after the success and failure on readyState 4
 * @params string $h.data - data to be sent to the server
 * @params bool $h.refresh - Is this a call from the onRefresh event.
 */
function Ajax($h)
{
	var me = this;
	$h = $h || {}

	// Setup properties
	this.url = $h.url || false;
	this.method = $h.method || "GET";
	this.type = ($h.type === false) ? false : true;
	this.success = $h.success || null;
	this.failure = $h.failure || null;
	this.data = $h.data || null;
	this.complete = $h.complete || null;
	this.refresh = $h.refresh || false;

	if (!this.url)
	{
		console.error('\nAjax Object requires a url to be passed in: e.g. { "url": "some string" }\n')
		return undefined;
	}

	this.createRequest();
	this.req.onreadystatechange = this.stateChange;
	this.req.object = this;
	this.open();
	this.send();
}

Ajax.currentlyRefreshing = false;

Ajax.prototype.stateChange = function()
{
	var me = this.object;

	// console.log("this in stateChage is: "+ this);
	// console.log("object in stateChange is: "+ this.object);

	switch (this.readyState)
	{

	case 1:
		if (typeof (me.connection) === "function")
			me.connection(this, me);
		break;
	case 2:
		if (typeof (me.received) === "function")
			me.received(this, me);
		break;
	case 3:
		if (typeof (me.processing) === "function")
			me.processing(this, me);
		break;
	case 4:
		if (this.status == "200")
		{
			if (typeof (me.success) === "function")
				me.success(this, me);
		}
		else
		{
			if (typeof (me.failure) === "function")
				me.failure(this.status, this, me);
		}
		if (typeof (me.complete) === "function")
			me.complete(this, me);
		if (me.refresh)
			Ajax.currentlyRefreshing = false;
		break;
	default:
		console.log("I don't think I should be here.");
		break;
	}
}

Ajax.prototype.createRequest = function()
{
	try
	{
		this.req = new XMLHttpRequest();
		if (this.refresh)
			Ajax.currentlyRefreshing = true;
	}
	catch (error)
	{
		alert("The request could not be created: </br>" + error);
		console.error("failed to create request: " + error);
	}

}

Ajax.prototype.open = function()
{
	try
	{
		this.req.open(this.method, this.url, this.type);
	}
	catch (error)
	{
		console.error("failed to open request: " + error);
	}
}

Ajax.prototype.send = function()
{
	var data = this.data || null;
	try
	{
		this.req.send(data);
	}
	catch (error)
	{
		console.error("failed to send request: " + error);
	}
}


// End ATVUtils
// ***************************************************



FlowManager = function()
{
};

FlowManager.prototype.setTargetPage = function(pageId)
{
	atv.localStorage[TARGET_PAGEID_KEY] = pageId;
	console.debug("flowManager.setTargetPage() atv.localStorage[TARGET_PAGEID_KEY] " + atv.localStorage[TARGET_PAGEID_KEY]);
}

FlowManager.prototype.clearTargetPage = function()
{
	atv.localStorage.removeItem(TARGET_PAGEID_KEY);
	console.debug("flowManager.clearTargetPage() atv.localStorage[TARGET_PAGEID_KEY] " + atv.localStorage[TARGET_PAGEID_KEY]);
}

FlowManager.prototype.startPurchaseFlow = function()
{
	atv.localStorage[PURCHASE_FLOW_KEY] = "true";
	console.debug("flowManager.startPurchaseFlow() atv.localStorage[PURCHASE_FLOW_KEY] " + atv.localStorage[PURCHASE_FLOW_KEY]);
}

FlowManager.prototype.endPurchaseFlow = function()
{
	atv.localStorage.removeItem(PURCHASE_FLOW_KEY);
	atv.localStorage.removeItem(PURCHASE_PRODUCT_ID_KEY);
	console.debug("flowManager.endPurchaseFlow() atv.localStorage[PURCHASE_FLOW_KEY] " + atv.localStorage[PURCHASE_FLOW_KEY]);
}

FlowManager.prototype.inPurchaseFlow = function()
{
	console.debug("flowManager.inPurchaseFlow() atv.localStorage[PURCHASE_FLOW_KEY] " + atv.localStorage[PURCHASE_FLOW_KEY]);
	return (atv.localStorage[PURCHASE_FLOW_KEY] == "true");
}

FlowManager.prototype.inPurchaseFlow = function()
{
	console.debug("flowManager.inPurchaseFlow() atv.localStorage[PURCHASE_FLOW_KEY] " + atv.localStorage[PURCHASE_FLOW_KEY]);
	return (atv.localStorage[PURCHASE_FLOW_KEY] == "true");
}

FlowManager.prototype.inMediaFlow = function()
{
	console.debug("flowManager.inMediaFlow() atv.localStorage[MEDIA_META_KEY] " + atv.localStorage[MEDIA_META_KEY]);
	return atv.localStorage.getItem(MEDIA_META_KEY) != null && atv.localStorage.getItem(MEDIA_META_REQUEST_PARAMS_KEY) != null;
}

FlowManager.prototype.endMediaFlow = function()
{
	atv.localStorage.removeItem(MEDIA_META_KEY);
	atv.localStorage.removeItem(MEDIA_META_REQUEST_PARAMS_KEY);
	console.debug("flowManager.endMediaFlow() atv.localStorage[MEDIA_META_KEY] " + atv.localStorage[MEDIA_META_KEY]);
}

var flowManager = new FlowManager;

console.debug("atvutils.js flowManager created");HTTP/1.1 200 OK
Server: Oracle-iPlanet-Web-Server/7.0
Date: Wed, 08 Aug 2012 16:15:02 GMT
Content-type: application/x-javascript
Last-modified: Tue, 31 Jul 2012 21:32:23 GMT
Content-length: 42766
Etag: "a70e-50184ee7"
Accept-ranges: bytes

console.log("~~~~~ MLB.TV main.js start");

var IOS_IDENTITY_POINT_KEY = "ios.identityPointId";
var IOS_FINGERPRINT_KEY = "ios.fingerprint";
var BEST_IDENTITY_POINT_KEY = "best.identityPointId";
var BEST_FINGERPRINT_KEY = "best.fingerprint";
var USERNAME_KEY = "username";

var SHOW_SCORES_KEY = "showScores";
var FAVORITE_TEAMS_KEY = "favoriteTeamIds";

var SESSION_KEY = "sessionKey";
var MEDIA_META_KEY = "media.meta";
var MEDIA_META_REQUEST_PARAMS_KEY = "media.meta.requestParams";

var PURCHASE_FLOW_KEY = "flow.purchase";
var PURCHASE_PRODUCT_ID_KEY = "purchase.productId";

var EMAIL_LINKED_KEY = "email.linked";
var AUTHORIZED_KEY = "authorized";

var OFFER_URI_AFTER_AUTH_KEY = "offer.uriToLoadAfterAuth";
var OFFER_PURCHASE_FUNCTION_KEY = "offer.purchaseFunction";
var OFFER_SWAP_PAGE_KEY = "offer.swapPage";

var TARGET_PAGEID_KEY = "targetPage";
var PURCHASE_FLOW_KEY = "flow.purchase";
var OFFER_PAGE_ID = "com.bamnetworks.appletv.offer";

var TODAYS_GAMES_REFRESH_URL_KEY = "todaysGames.refreshUrl";
var GAMES_PAGE_REFRESH_URL_KEY = "games.refreshUrl";

// atv.onPageLoad
atv.onPageLoad = function(id)
{
	console.log("main.js atv.onPageLoad() id: " + id);

	// <mlbtv>
	var headElem = document.rootElement.getElementByName("head");
	if (headElem && headElem.getElementByName("mlbtv"))
	{
		var mlbtvElem = headElem.getElementByName("mlbtv");
		console.log("mlbtv exists");

		// store local variables
		storeLocalVars(mlbtvElem);

		// ping urls
		pingUrls(mlbtvElem);
	}

	// games by team
	if (id && id.indexOf("com.bamnetworks.appletv.gamesByTeam.") >= 0)
	{
		applyGamesPage(document);
	}

	if (id && id.indexOf("com.bamnetworks.appletv.media") >= 0 && typeof (flowManager) != "undefined")
	{
		flowManager.endPurchaseFlow();
		flowManager.endMediaFlow();
		console.debug("main.js onPageLoad() atv.localStorage[PURCHASE_FLOW_KEY] " + atv.localStorage[PURCHASE_FLOW_KEY]);
	}

	atv._debugDumpControllerStack();
}

// atv.onPageUnload
atv.onPageUnload = function(id)
{
	console.debug("main.js atv.onPageUnload() id " + id);
	console.debug("main.js atv.onPageUnload() atv.localStorage[PURCHASE_FLOW_KEY] " + atv.localStorage[PURCHASE_FLOW_KEY]);
	if (flowManager.inPurchaseFlow() && ("com.bamnetworks.appletv.authenticate" == id || id.indexOf("Not Authorized") > -1))
	{
		console.debug("main.js atv.onPageUnload() showing offer page");
		flowManager.setTargetPage(OFFER_PAGE_ID);

		var newPage = new atv.ProxyDocument();
		newPage.show();
		serviceClient.loadOfferPage(newPage);

		console.debug("main.js atv.onPageUnload() showing offer page");
	}
}

function storeLocalVars(mlbtvElem)
{
	// check local storage params
	var localStorageElems = mlbtvElem.getElementsByName("localStorage");
	console.debug("localStorageElems=" + localStorageElems);
	if (localStorageElems && localStorageElems.length > 0)
	{
		var index = 0;
		for (; index < localStorageElems.length; index++)
		{
			var key = localStorageElems[index].getAttribute("key");
			var value = localStorageElems[index].getAttribute("value");
			if (value)
			{
				atv.localStorage[key] = value;
			}
			else
			{
				atv.localStorage.removeItem(key);
			}
			console.log("localStorage[" + key + "] = " + atv.localStorage[key]);
		}
	}
}

function pingUrls(mlbtvElem)
{
	// check ping urls
	var pingElems = mlbtvElem.getElementsByName("ping");
	console.log("pingElems=" + pingElems);
	if (pingElems && pingElems.length > 0)
	{
		for ( var pingIndex = 0; pingIndex < pingElems.length; pingIndex++)
		{
			var url = pingElems[pingIndex].getAttribute('url');
			url = (url == null || url.trim().length() <= 0) ? pingElems[pingIndex].textContent : url;
			console.debug("url=" + url);
			if (url)
			{
				try
				{
					console.log('pinging reporting url ' + url);
					var req = new XMLHttpRequest();
					req.open("GET", url, true);
					req.send();
				}
				catch (e)
				{
					console.error("failed to ping the url " + url + " error " + e);
				}
			}
		}
	}
}

/**
 * Updates the logo according to the screen resolution
 * 
 * @param document
 * @returns
 */
function updateImageResolution(document)
{
	// update logos according to screen resolution
	var frame = atv.device.screenFrame;
	console.log("frame size: " + frame.height);
	if (frame.height == 1080)
	{
		var images = document.rootElement.getElementsByTagName('image');
		for ( var i = 0; i < images.length; i++)
		{
			images[i].textContent = images[i].textContent.replace('720p', '1080p');
		}
	}

	return document;
}

// ------------------------------ load and apply -------------------------------
function applyMain(document)
{
	console.log('applyMain ' + document);

	// end all flows when you're on the main page
	flowManager.endMediaFlow();
	flowManager.endPurchaseFlow();

	// check valid subscription
	// (1) via iTunes on this box and iTunes account signed in. Receipt is present and stored on the box
	// (2) via Vendor and is signed in with Vendor account.
	var validSubscription = (atv.localStorage[IOS_IDENTITY_POINT_KEY] != null && atv.localStorage[IOS_FINGERPRINT_KEY] != null) || (atv.localStorage[AUTHORIZED_KEY] == "true" && atv.localStorage[EMAIL_IDENTITY_POINT_KEY] != null && atv.localStorage[EMAIL_FINGERPRINT_KEY] != null);

	console.log("applyMain() validSubscription " + validSubscription);
	console.log("applyMain() atv.localStorage[AUTHORIZED_KEY] " + atv.localStorage[AUTHORIZED_KEY]);

	var subscribeMenu = document.getElementById("subscribeMlbTv");
	if (validSubscription)
	{
		if (subscribeMenu)
		{
			subscribeMenu.removeFromParent();
			console.debug("applyMain() removing the Subscribe To MLB.TV");
		}
	}
	else
	{
		// add the menu if it's not there
		if (!subscribeMenu)
		{
			subscribeMenu = atv.parseXML('<oneLineMenuItem id="subscribeMlbTv" accessibilityLabel="Subscribe To MLB.TV" onSelect="subscribeToMlbTv()"><label>Subscribe To MLB.TV</label></oneLineMenuItem>').rootElement;
			subscribeMenu.removeFromParent();

			var parent = document.rootElement.getElementByTagName("items");
			console.debug("applyMain() parent " + parent);
			if (parent && parent.childElements)
			{
				console.debug("applyMain() before parent.childElements " + parent.childElements);
				var settingsMenu = document.getElementById("settings");
				settingsMenu.removeFromParent();
				parent.appendChild(subscribeMenu);
				parent.appendChild(settingsMenu);
				console.debug("applyMain() after parent.childElements " + parent.childElements);
			}
		}
	}

	return document;
}

function handleMainVoltileReload()
{
	console.debug("handleMainVoltileReload() atv.localStorage[TARGET_PAGEID_KEY] " + atv.localStorage[TARGET_PAGEID_KEY]);
	if (atv.localStorage[TARGET_PAGEID_KEY] == null || atv.localStorage[TARGET_PAGEID_KEY] == "com.bamnetworks.appletv.main")
	{
		applyMain(document);
		atv.loadAndSwapXML(document);
	}
}

function handleOfferVoltileReload(document)
{
	console.debug("handleOfferVoltileReload() atv.localStorage[AUTHORIZED_KEY] " + atv.localStorage[AUTHORIZED_KEY]);
	if (atv.localStorage[AUTHORIZED_KEY] == "true")
	{
		console.debug("handleOfferVoltileReload() unloading the page because already authorized");
		atv.unloadPage();
	}
	else
	{
		applyOffer(document);
		atv.loadAndSwapXML(document);
	}
}

function applyTodaysGames(responseXML, refresh)
{
	console.log("applyTodaysGames() responseXML: " + responseXML);

	try
	{
		// elements
		var listByNavigation = responseXML.rootElement.getElementByTagName("listByNavigation");
		var imageMenuItems = responseXML.rootElement.getElementsByTagName("imageTextImageMenuItem");
		console.log("applyTodaysGames() navigation=" + navigation);

		// add behavior
		if (atv.localStorage[TODAYS_GAMES_REFRESH_URL_KEY] == null || !refresh)
		{
			var navigation = responseXML.rootElement.getElementByTagName("navigation");
			var currentIndex = navigation.getAttribute("currentIndex");
			console.debug("applyTodaysGames() currentIndex " + currentIndex);
			var refreshUrl = navigation.childElements[currentIndex].getElementByTagName("url").textContent;
			console.debug("applyTodaysGames() refreshUrl " + refreshUrl);
			atv.localStorage[TODAYS_GAMES_REFRESH_URL_KEY] = refreshUrl;
		}
		listByNavigation.setAttribute("refreshInterval", "60");
		listByNavigation.setAttribute("onRefresh", "console.log('refreshing todays games');handleTodaysGamesRefresh();");
		listByNavigation.setAttribute("onNavigate", "console.log('navigating todays games');handleTodaysGamesNavigate(event);");

		// sort
		var favTeamIds = getFavoriteTeamIds();
		function sort(menuOne, menuTwo)
		{
			var menuOneTeamIds = menuOne.getAttribute("id").split("-");
			var menuTwoTeamIds = menuTwo.getAttribute("id").split("-");

			var menuOneOrder = (favTeamIds.indexOf(menuOneTeamIds[0]) >= 0 || favTeamIds.indexOf(menuOneTeamIds[1]) >= 0) ? 1 : 0;
			var menuTwoOrder = (favTeamIds.indexOf(menuTwoTeamIds[0]) >= 0 || favTeamIds.indexOf(menuTwoTeamIds[1]) >= 0) ? 1 : 0;

			// console.log("favTeamIds.indexOf(menuOneTeamIds[0])=" + favTeamIds.indexOf(menuOneTeamIds[0]) + " favTeamIds.indexOf(menuOneTeamIds[1])=" + favTeamIds.indexOf(menuOneTeamIds[1]));
			// console.log(menuOneOrder + "-" + menuTwoOrder);

			return menuTwoOrder - menuOneOrder;
		}
		var orderedMenuList = imageMenuItems.sort(sort);

		// 1. re-append the sorted children
		// 2. set the favorite team logos
		// 3. hide scores on the page
		// 4. set the link to no scores preview
		var index = 0;
		for (; index < orderedMenuList.length; index++)
		{
			// 1. reappend child for sorting
			var menu = orderedMenuList[index];
			var parent = menu.parent;
			menu.removeFromParent();
			parent.appendChild(menu);

			// 2. set the favorite team logos
			console.debug("applyTodaysGames() menu item id " + menu.getAttribute("id"));
			var teamIds = menu.getAttribute("id").split("-");
			var isAwayFav = favTeamIds.indexOf(teamIds[0]) >= 0;
			var isHomeFav = favTeamIds.indexOf(teamIds[1]) >= 0;
			console.debug("applyTodaysGames() isAwayFav " + isAwayFav);
			console.debug("applyTodaysGames() isHomeFav " + isHomeFav);
			var leftImageElem = menu.getElementByName("leftImage");
			if (isAwayFav && leftImageElem.textContent.indexOf("-fav.png") < 0)
			{
				leftImageElem.textContent = leftImageElem.textContent.replace(".png", "-fav.png");
			}
			var rightImageElem = menu.getElementByName("rightImage");
			if (isHomeFav && rightImageElem.textContent.indexOf("-fav.png") < 0)
			{
				rightImageElem.textContent = rightImageElem.textContent.replace(".png", "-fav.png");
			}

			// 3. Hide scores
			// 4. Use the preview link with no scores
			var showScores = (atv.localStorage[SHOW_SCORES_KEY]) ? atv.localStorage[SHOW_SCORES_KEY] : "true";
			var rightLabelElem = menu.getElementByName("rightLabel");
			if (showScores == "false")
			{
				var previewLinkElem = menu.getElementByTagName("link");
				if (rightLabelElem && rightLabelElem.textContent.indexOf("Final") >= 0)
				{
					rightLabelElem.textContent = "Final";
				}
				if (previewLinkElem)
				{
					previewLinkElem.textContent = previewLinkElem.textContent.replace(".xml", "_noscores.xml");
				}
			}

			// 5. Check for double header 3:33 AM ET
			if (rightLabelElem && rightLabelElem.textContent.indexOf("3:33 AM") >= 0)
			{
				rightLabelElem.textContent = "Game 2";
			}
		}

		return responseXML;
	}
	catch (error)
	{
		console.error("unable to apply todays games " + error);
		atvutils.loadError("An error occurred loading today's games", "An error occurred loading today's games");
	}
}

/**
 * Should be called by the onRefresh event, I am also calling this from the handleNavigate function below to share code. Essentially this function: 1. Loads the current Navigation file 2. Pulls the
 * menu items out of the loaded file and dumps them into the current file 3. Checks to see if an onRefresh AJAX call is already being made and if so does nothing.
 * 
 * 
 * @params string url - url to be loaded
 */
function handleTodaysGamesRefresh()
{
	var url = atv.localStorage[TODAYS_GAMES_REFRESH_URL_KEY];
	console.log("handleTodaysGamesRefresh() url " + url);

	console.log("We are about to check the value of Ajax.currentlyRefreshing: --> " + Ajax.currentlyRefreshing);
	// check the Ajax object and see if a refresh event is happening
	if (!Ajax.currentlyRefreshing)
	{
		// if a refresh event is not happening, make another one.
		console.log("handleTodaysGamesRefresh()About to make the AJAX call. --> ")

		var refresh = new Ajax({
			"url" : url,
			"refresh" : true,
			"success" : function()
			{
				var req = arguments[0];

				console.log("handleTodaysGamesRefresh() Ajax url " + url);

				var newDocument = atv.parseXML(req.responseText); // Instead of using the responseXML you should parse the responseText. I know it seems redundent but trust me.

				// update the content page
				var newMenuSections = newDocument.rootElement.getElementByTagName('sections');
				applyTodaysGames(newDocument, true);
				newMenuSections.removeFromParent();

				var menu = document.rootElement.getElementByTagName('menu');
				console.log("handleTodaysGamesRefresh() menu=" + menu);
				if (menu)
				{
					var oldMenuSections = menu.getElementByTagName('sections');
					menu.replaceChild(oldMenuSections, newMenuSections);
				}
				else
				{
					menu = atv.parseXML("<menu></menu>").rootElement;
					menu.removeFromParent();
					menu.appendChild(newMenuSections);

					var listByNavigation = document.rootElement.getElementByTagName("listByNavigation");
					listByNavigation.appendChild(menu);
				}
				
				// replace # Games
				var newTumbler = newDocument.rootElement.getElementByTagName('tumblerWithSubtitle');
				newTumbler.removeFromParent();
				var header = document.rootElement.getElementByTagName('header');
				var oldTumbler = document.rootElement.getElementByTagName('tumblerWithSubtitle');
				header.replaceChild(oldTumbler, newTumbler);
			}
		});
		console.log("handleTodaysGamesRefresh() I have made the AJAX call. <--");
	}
	console.log("handleTodaysGamesRefresh() I am on the other side of the if. <--");
}

/**
 * This function works very similar to the handleRefresh Essentially we are manually loading the data and updating the page in Javascript This means we have to handle a bit more of the magic, but we
 * also get to control what is updated and update more of the page, like headers, previews, etc.
 * 
 * In this example, I am: 1. Getting the new URL from the Navigation Item 2. Changing the onRefresh URL 3. Calling handleRefresh and passing the new url to let handleRefresh do the magic.
 */
function handleTodaysGamesNavigate(e)
{
	console.log("handleTodaysGamesNavigate() " + e + ", e.navigationItemId = " + e.navigationItemId + " document = " + document);
	console.log("e=" + e);
	atvutils.printObject(e);
	console.log("document.getElementById(e.navigationItemId)=" + document.getElementById(e.navigationItemId));
	console.log("document.getElementById(e.navigationItemId).getElementByTagName=" + document.getElementById(e.navigationItemId).getElementByTagName);

	// get the url
	var url = document.getElementById(e.navigationItemId).getElementByTagName('url').textContent;
	atv.localStorage[TODAYS_GAMES_REFRESH_URL_KEY] = url;

	handleTodaysGamesRefresh();

	e.onCancel = function()
	{
		// Cancel any outstanding requests related to the navigation event.
		console.log("Navigation got onCancel.");
	}
}

function applyFeedSelectionPage(document)
{
	console.log("applyFeedSelectionPage() document: " + document);

	// add behavior by adding javascript file reference
	var head = document.rootElement.getElementByTagName("head");
	if (head)
	{
		var serviceJs = atv.parseXML('<script src="' + config.appletvBase + '/js/service.js" />').rootElement;
		serviceJs.removeFromParent();
		console.debug("adding service.js " + serviceJs);

		var children = head.childElements;
		children.unshift(serviceJs);

		for ( var i = 0; i < children.length; i++)
		{
			children[i].removeFromParent();
			head.appendChild(children[i]);
		}
	}

	flowManager.endMediaFlow();
	flowManager.endPurchaseFlow();
	console.log("purchase and media flow ended " + atv.localStorage[PURCHASE_PRODUCT_ID_KEY] + " " + atv.localStorage[MEDIA_META_REQUEST_PARAMS_KEY] + " " + atv.localStorage[MEDIA_META_KEY] + " " + atv.localStorage[SESSION_KEY]);

	return document;
}

// This is called from the feed_selection.xml
function loadFeed(mediaMeta)
{
	atv._debugDumpControllerStack();

	console.debug("showing new page..");
	var newPage = new atv.ProxyDocument();
	newPage.show();

	atv._debugDumpControllerStack();

	// store the media meta into the local storage for later use
	serviceClient.startMediaFlow(mediaMeta);
	atv.localStorage[OFFER_URI_AFTER_AUTH_KEY] = config.appletvBase + "/views/MediaServiceProxy?encode(MEDIA_META_REQUEST_PARAMS)";
	atv.localStorage[OFFER_PURCHASE_FUNCTION_KEY] = "restorePurchasePlay";
	atv.localStorage[OFFER_SWAP_PAGE_KEY] = "true";

	// load the media page
	serviceClient.loadMediaPage(function(document)
	{
		// media proxy returns a playback page, display
		var authorized = !serviceClient.isMediaResponseUnauthorized(document);
		// atv.localStorage.setItem(AUTHORIZED_KEY, (authorized) ? "true" : "false");
		if (authorized)
		{
			// if authorized, load the page
			console.debug("loadFeed() showing the media page");
			newPage.swapXML(document);
		}
		else
		{
			// if unauthorized, load the offer page
			console.debug("loadFeed() showing the offer page swap " + atv.localStorage[OFFER_SWAP_PAGE_KEY]);
			serviceClient.loadOfferPage(newPage);
		}
	});
}
// deprecated function name but it's still being generated by the gameday mule
var loadLiveGameVideoAsset = loadFeed;

function loadOfferPageAndPlay()
{
	var newPage = new atv.ProxyDocument();
	newPage.show();
	serviceClient.loadOfferPage(newPage);
}

function subscribeToMlbTv()
{
	console.debug("subscribeToMlbTv() enter");

	flowManager.endMediaFlow();

	// create new page where offer page will be loaded onto
	var newPage = new atv.ProxyDocument();
	newPage.show();

	// load the offer page onto the new page
	atv.localStorage[OFFER_URI_AFTER_AUTH_KEY] = config.appletvBase + "/views/Dialog?messageCode=AUTH_MESSAGE_CODE";
	atv.localStorage[OFFER_PURCHASE_FUNCTION_KEY] = "restoreAndPurchase";
	atv.localStorage[OFFER_SWAP_PAGE_KEY] = "false";
	serviceClient.loadOfferPage(newPage);
}

function ErrorToDebugString(error)
{
	var localizedDescription = error.userInfo["NSLocalizedDescription"];
	localizedDescription = localizedDescription ? localizedDescription : "Unknown";
	return error.domain + "(" + error.code + "): " + localizedDescription;
}

function linkManually()
{
	atv.loadURL(config.appletvBase + "/views/Login?urlToLoadAfterAuth=" + config.appletvBase + "/views/LinkProxy?request=encode(LINK_PROXY_REQUEST)");
}

function purchase(newPage)
{
	var observer = serviceClient.addPurchaseObserver(function(purchasedTransaction)
	{
		// PURCHASE success
		console.debug("purchase(): success purchased transactions " + purchasedTransaction);

		// link()
		serviceClient.doLink([ purchasedTransaction ], function()
		{
			console.debug("purchase(): link success");
			serviceClient.loadUserAuthorized(function()
			{
				console.debug("purchase(): load user authorized");
				newPage.loadXML(atvutils.makeOkDialog("MLB.TV Purchased", "You have successfully purchased MLB.TV."));
				atv.unloadPage();
			});
		}, function()
		{
			console.debug("purchase(): link error after purchase");
			newPage.cancel();
			atv.unloadPage();
			// do nothing -- go back to previous page
			// atv.loadAndSwapXML(atvutils.makeOkDialog("MLB.TV Error", "Failed to determine your MLB.TV subscription after purchase"));
		})
	}, function()
	{
		// PURCHASE error
		console.debug("purchase(): error ");
		newPage.cancel();
		atv.unloadPage();
	});

	newPage.onCancel = function()
	{
		atv.SKDefaultPaymentQueue.removeTransactionObserver(observer);
	};

	serviceClient.startPurchase();

}

function restoreAndPurchase(productId)
{
	atv._debugDumpControllerStack();

	// store product id into the cart
	atv.localStorage[PURCHASE_PRODUCT_ID_KEY] = productId;

	// create and show new page where dialogs will be loaded
	var newPage = new atv.ProxyDocument();
	newPage.show();

	atv._debugDumpControllerStack();

	var restoreObserver = serviceClient.addRestoreObserver(function(transactions)
	{
		// link()
		serviceClient.doLink(transactions, function()
		{
			// LINK success -- determine authorization thru feature service
			atv._debugDumpControllerStack();
			serviceClient.loadUserAuthorized(function()
			{
				console.debug("restoreAndPurchase() atv.localStorage[AUTHORIZED_KEY] " + atv.localStorage[AUTHORIZED_KEY]);
				if (atv.localStorage[AUTHORIZED_KEY] == "true")
				{
					console.debug("restoreAndPurchase() authorized or error page, showing Success page...");
					atv._debugDumpControllerStack();
					newPage.cancel();
					atv.loadAndSwapXML(atvutils.makeOkDialog("Success", "You are now authorized to watch MLB.TV.  Please select a game from Today's Games on the main menu."));
					console.debug("restoreAndPurchase() after showing Success page...");
					atv._debugDumpControllerStack();
				}
				else
				{
					// unauthorized, purchase
					console.debug("restoreAndPurchase() unauthorized, purchasing...");
					purchase(newPage);
					atv._debugDumpControllerStack();
				}
			});
		}, function()
		{
			// LINK error -- attempt to purchase now
			console.debug("restoreAndPurchase() link error - make the purchase ");
			purchase(newPage);
			atv._debugDumpControllerStack();
		});
	}, function(error)
	{
		newPage.cancel();
		if (error && error.code == -500)
		{
			atv.loadAndSwapXML(atvutils.makeOkDialog("MLB.TV Error", "Failed to determine your subscription."));
		}
		else
		{
			newPage.swapXML(document);
		}
		console.error("restoreAndPurchase() restore error - failed to restore " + document);
		atv._debugDumpControllerStack();
	});

	newPage.onCancel = function()
	{
		atv.SKDefaultPaymentQueue.removeTransactionObserver(restoreObserver);
	};

	// Restore completed transactions to get transactions purchased on other devices for this account.
	console.debug("restoreAndPurchase() Restoring completed transactions...");
	atv.SKDefaultPaymentQueue.restoreCompletedTransactions();
}

function purchaseAndPlay(newPage)
{
	atv._debugDumpControllerStack();

	var observer = serviceClient.addPurchaseObserver(function(purchasedTransaction)
	{
		// PURCHASE success
		console.debug("purchase(): success purchased transactions " + purchasedTransaction);
		atv._debugDumpControllerStack();

		// link()
		serviceClient.doLink([ purchasedTransaction ], function()
		{
			console.debug("purchase(): link success");
			atv._debugDumpControllerStack();

			serviceClient.loadMediaPage(function(mediaDoc)
			{
				console.debug("purchase() loadMediaPage");
				newPage.swapXML(mediaDoc);
				atv._debugDumpControllerStack();
			});

		}, function()
		{
			console.debug("purchase(): link error after purchase");
			newPage.swapXML(atvutils.makeOkDialog("MLB.TV Link Error", "Failed to link your MLB.TV account properly after purchase"));
			atv._debugDumpControllerStack();
		})

	}, function(transaction)
	{
		// PURCHASE error
		console.debug("purchase(): error " + (transaction) ? transaction.error : null);
		if (!transaction || transaction.error != 2)
		{
			newPage.swapXML(atvutils.makeOkDialog("MLB.TV Purchase Error", "The purchase failed.  Please try again at a later time."));
		}
		atv._debugDumpControllerStack();
	});

	newPage.onCancel = function()
	{
		atv.SKDefaultPaymentQueue.removeTransactionObserver(observer);
	};

	serviceClient.startPurchase();
}

function restorePurchasePlay(productId)
{
	// store product id into the cart
	atv.localStorage[PURCHASE_PRODUCT_ID_KEY] = productId;

	var newPage = new atv.ProxyDocument();
	newPage.show();

	atv._debugDumpControllerStack();

	var restoreObserver = serviceClient.addRestoreObserver(function(transactions)
	{
		// link()
		serviceClient.doLink(transactions, function()
		{
			// LINK success -- determine whether to purchase
			serviceClient.loadMediaPage(function(document)
			{
				atv._debugDumpControllerStack();
				var authorized = !serviceClient.isMediaResponseUnauthorized(document);
				console.debug("restorePurchasePlay() authorized " + authorized);
				atv.localStorage.setItem(AUTHORIZED_KEY, (authorized) ? "true" : "false");
				console.debug("restorePurchasePlay() atv.localStorage[AUTHORIZED] " + atv.localStorage[AUTHORIZED_KEY]);
				if (authorized)
				{
					// authorized or media playback error (session key problem, media unavailable, etc.), display the page
					console.debug("restorePurchasePlay() authorized or error page, showing media proxy page...");
					newPage.swapXML(atvutils.makeOkDialog("MLB.TV Subscriber", "You have purchased this content before and already have access to watch MLB.TV content.", "play();"));
					atv._debugDumpControllerStack();
					console.debug("restorePurchasePlay() after showing media proxy page...");
				}
				else
				{
					// unauthorized, purchase
					console.debug("restorePurchasePlay() unauthorized, purchasing...");
					purchaseAndPlay(newPage);
					atv._debugDumpControllerStack();
				}
			});
		}, function()
		{
			// LINK error -- attempt to purchase now
			console.debug("restorePurchasePlay() link error - make the purchase ");
			purchaseAndPlay(newPage);
		});
	}, function(error)
	{
		if (error && error.code == -500)
		{
			newPage.swapXML(atvutils.makeOkDialog("MLB.TV Error", "Failed to determine your subscription."));
		}
		else
		{
			newPage.swapXML(document);
		}
	});

	newPage.onCancel = function()
	{
		atv.SKDefaultPaymentQueue.removeTransactionObserver(restoreObserver);
	};

	// Restore completed transactions to get transactions purchased on other devices for this account.
	console.debug("restorePurchasePlay() Restoring completed transactions...");
	atv.SKDefaultPaymentQueue.restoreCompletedTransactions();
}

function play(newPage)
{
	serviceClient.loadMediaPage(function(document)
	{
		console.debug("play() show the media proxy page..." + newPage);
		atv._debugDumpControllerStack();
		if (newPage)
		{
			newPage.swapXML(document);
			atv.unloadPage();
		}
		else
		{
			atv.loadAndSwapXML(document);
		}
		atv._debugDumpControllerStack();
		console.debug("play() after showing media proxy page...");
	});
}

function restoreManually()
{
	console.debug("restore receipts from iTunes account manually");

	var newPage = new atv.ProxyDocument();
	newPage.show();

	var restoreObserver = serviceClient.addRestoreObserver(function(transactions)
	{
		// restore transaction callback
		console.log("restoreManually() transactions " + transactions);
		if (transactions)
		{
			console.debug("restoreManually() successfully restored receipts from iTunes account manually");
			serviceClient.doLink(transactions, function()
			{
				console.debug("restoreManually() link success callback - determine authorization from feature service");
				serviceClient.loadUserAuthorized(function()
				{
					newPage.loadXML(atvutils.makeOkDialog("Restore Successful", "Your subscription has been restored."));
				});
				console.debug("restoreManually() link service success");
			}, function()
			{
				console.debug("restoreManually() link service error");
			});
		}
		else
		{
			console.debug("restoreManually() missing transaction - determine authorization from feature service");
			newPage.loadXML(atvutils.makeOkDialog("Not an MLB.TV subscriber", "You have not purchased a MLB.TV subscription on AppleTV."));
		}
	}, function(error)
	{
		// restore error callback
		console.debug("restoreManually() error attempting to restore receipts from iTunes account manually");

		if (!error || error.code != -500)
		{
			newPage.cancel();
		}
		else
		{
			newPage.loadXML(atvutils.makeOkDialog("Restore Unsuccessful", "An error occurred attempting to restore your MLB.TV account."));
		}
	});

	newPage.onCancel = function()
	{
		atv.SKDefaultPaymentQueue.removeTransactionObserver(restoreObserver);
	};

	// Restore completed transactions to get transactions purchased on other devices for this account.
	console.debug("restoreManually() Restoring completed transactions...");
	atv.SKDefaultPaymentQueue.restoreCompletedTransactions();
}

function restoreOnOffer()
{
	console.debug("restoreOnOffer() restore receipts from iTunes account manually");

	var newPage = new atv.ProxyDocument();
	newPage.show();

	var restoreObserver = serviceClient.addRestoreObserver(function(transactions)
	{
		// restore success callback
		console.log("restoreManually() transaction " + transactions);
		if (transactions)
		{
			console.debug("successfully restored receipts from iTunes account manually");
			serviceClient.doLink(transactions, function()
			{
				// link success callback
				if (flowManager.inMediaFlow())
				{
					console.debug("restoreOnOffer() in media flow, playing media");
					play(newPage);
				}
				else
				{
					console.debug("restoreOnOffer() NOT in media flow, display info message");
					serviceClient.loadUserAuthorized(function()
					{
						console.debug("restoreOnOffer(): load user authorized");
						newPage.swapXML(atvutils.makeOkDialog("Restore Successful", "Your subscription has been restored."));
						atv.unloadPage();
					});
				}
			}, function()
			{
				// link error callback
				newPage.loadXML(atvutils.makeOkDialog("Restore Unsuccessful", "There was a problem restoring your iTunes account. Please try again at a later time."));
			});
		}
		else
		{
			newPage.loadXML(atvutils.makeOkDialog("Not an MLB.TV subscriber", "You have not purchased a MLB.TV subscription on AppleTV."));
		}
	}, function(error)
	{
		// restore error callback
		console.debug("error attempting to restore receipts from iTunes account manually");

		if (!error || error.code != -500)
		{
			newPage.cancel();
		}
		else
		{
			newPage.loadXML(atvutils.makeOkDialog("Restore Unsuccessful", "An error occurred attempting to restore your MLB.TV account."));
		}
	});

	newPage.onCancel = function()
	{
		atv.SKDefaultPaymentQueue.removeTransactionObserver(restoreObserver);
	};

	// Restore completed transactions to get transactions purchased on other devices for this account.
	console.debug("restoreManually() Restoring completed transactions...");
	atv.SKDefaultPaymentQueue.restoreCompletedTransactions();
}

function applySettingsPage(document)
{
	// update logo image resolution
	updateImageResolution(document);

	// show scores menu item
	var showScores = atv.localStorage[SHOW_SCORES_KEY];
	var menuItem = document.getElementById("showScoresMenuItem");
	if (menuItem)
	{
		var label = menuItem.getElementByName('rightLabel');
		label.textContent = (!showScores || showScores == "true") ? "Show" : "Hide";
		console.debug("label.textContent: " + label.textContent);
	}

	// menu items
	var parent = document.rootElement.getElementByTagName("items");
	var signInMenu = document.getElementById("signInOutMenuItem");
	var restoreMenu = document.getElementById("restoreSub");
	var linkAcctMenu = document.getElementById("linkAcct");
	var showScoresMenu = document.getElementById("showScoresMenuItem");
	console.debug("applySettingsPage() parent " + parent);

	// restore menu item
	var showRestoreMenu = atv.localStorage[IOS_IDENTITY_POINT_KEY] == null && atv.localStorage[IOS_FINGERPRINT_KEY] == null;
	console.debug("applySettingsPage() showRestoreMenu " + showRestoreMenu + " restoreMenu " + restoreMenu + " atv.localStorage[IOS_IDENTITY_POINT_KEY] " + atv.localStorage[IOS_IDENTITY_POINT_KEY]);
	if (showRestoreMenu)
	{
		if (!restoreMenu)
		{
			var parent = document.rootElement.getElementByTagName("items");
			console.debug("applySettingsPage() parent " + parent);
			if (parent && parent.childElements)
			{
				restoreMenu = atv.parseXML('<oneLineMenuItem id="restoreSub" accessibilityLabel="Restore Subscription" onSelect="restoreManually();"><label>Restore Subscription</label></oneLineMenuItem>').rootElement;
				console.debug("applySettingsPage() adding restoreMenu " + restoreMenu);
				restoreMenu.removeFromParent();
				parent.appendChild(restoreMenu);
				if (linkAcctMenu)
				{
					linkAcctMenu.removeFromParent();
					parent.appendChild(linkAcctMenu);
				}
				showScoresMenu.removeFromParent();
				parent.appendChild(showScoresMenu);
			}
		}
	}
	else if (restoreMenu)
	{
		restoreMenu.removeFromParent();
	}

	// link accounts menu item
	var showLinkMenu = atv.localStorage[EMAIL_LINKED_KEY] != "true" && atv.localStorage[IOS_IDENTITY_POINT_KEY] != null && atv.localStorage[IOS_FINGERPRINT_KEY] != null;
	console.debug("applySettingsPage() atv.localStorage[EMAIL_LINKED_KEY] " + atv.localStorage[EMAIL_LINKED_KEY] + " showLinkMenu " + showLinkMenu + " atv.localStorage[IOS_IDENTITY_POINT_KEY] " + atv.localStorage[IOS_IDENTITY_POINT_KEY] + " linkAcctMenu " + linkAcctMenu);
	if (showLinkMenu)
	{
		// add the menu if it's not there
		if (!linkAcctMenu)
		{
			if (parent && parent.childElements)
			{
				console.debug("applySettingsPage() before parent.childElements " + parent.childElements);

				linkAcctMenu = atv.parseXML('<oneLineMenuItem id="linkAcct" accessibilityLabel="Link MLB.TV Account" onSelect="linkManually();"><label>Link Account</label></oneLineMenuItem>').rootElement;
				linkAcctMenu.removeFromParent();

				showScoresMenu.removeFromParent();
				parent.appendChild(linkAcctMenu);
				parent.appendChild(showScoresMenu);

				console.debug("applySettingsPage() after parent.childElements " + parent.childElements);
			}
		}

		if (signInMenu)
		{
			signInMenu.removeFromParent();
		}
	}
	else
	{
		// remove the menu
		if (linkAcctMenu)
		{
			linkAcctMenu.removeFromParent();
		}

		if (!signInMenu)
		{
			signInMenu = atv.parseXML('\
				<signInSignOutMenuItem id=\"signInOutMenuItem\" accessibilityLabel=\"Sign out\" signOutExitsApp=\"false\">\
					<signInPageURL>' + config.appletvBase + '/views/Login</signInPageURL>\
					<confirmation>\
						<title>Are You Sure?</title>\
						<description>The account %@ will be logged out of the service.</description>\
					</confirmation>\
				</signInSignOutMenuItem>').rootElement;
			signInMenu.removeFromParent();

			parent.appendChild(signInMenu);
			if (showRestoreMenu && restoreMenu)
			{
				parent.appendChild(restoreMenu);
			}
			showScoresMenu.removeFromParent();
			parent.appendChild(showScoresMenu);
		}
	}

	return document;
}

function applyRecapsPage(responseXML)
{
	// update logo image resolution
	updateImageResolution(responseXML);

	return responseXML;
}

function applyTeamsPage(responseXML)
{
	try
	{
		// update logo image resolution
		updateImageResolution(responseXML);

		var favTeamIds = getFavoriteTeamIds();

		console.debug('sorting teams.. ' + responseXML.rootElement);
		var gridList = responseXML.rootElement.getElementByName("body").getElementByName("scroller").getElementByName("items").getElementsByName("grid");
		var gridIndex = 0;
		for (; gridIndex < gridList.length; gridIndex++)
		{
			// function to sort by title
			function sortByTitle(poster1, poster2)
			{
				var favTeamIds = (atv.localStorage[FAVORITE_TEAMS_KEY]) ? atv.localStorage[FAVORITE_TEAMS_KEY] : [];

				var team1Id = poster1.getAttribute("id");
				var is1Fav = favTeamIds.indexOf(team1Id) >= 0;
				var title1 = poster1.getElementByName("title").textContent;
				title1 = (is1Fav) ? title1.toUpperCase() : title1.toLowerCase();

				var team2Id = poster2.getAttribute("id");
				var is2Fav = favTeamIds.indexOf(team2Id) >= 0;
				var title2 = poster2.getElementByName("title").textContent;
				title2 = (is2Fav) ? title2.toUpperCase() : title2.toLowerCase();

				var result = (title1 > title2) ? 1 : -1;
				// console.debug('result: ' + title1 + ">" + title2 + " = " + result);

				return result;
			}

			// parent
			var parent = gridList[gridIndex].getElementByName("items");

			// sort the posters
			var orderedPosterList = parent.childElements.sort(sortByTitle);

			// re-append the child
			var index = 0;
			for (; index < orderedPosterList.length; index++)
			{
				var poster = orderedPosterList[index];
				var teamId = poster.getAttribute("id");
				poster.removeFromParent();
				parent.appendChild(poster);

				var isFav = favTeamIds.indexOf(teamId) >= 0;

				var imageElem = poster.getElementByName("image");
				if (isFav && imageElem.textContent.indexOf("-fav.png") < 0)
				{
					imageElem.textContent = imageElem.textContent.replace(".png", "-fav.png");
				}
				else if (!isFav && imageElem.textContent.indexOf("-fav.png") > 0)
				{
					imageElem.textContent = imageElem.textContent.replace("-fav.png", ".png");
				}
			}

		}

		console.debug('teams are sorted and favorite team logos in place');
		return responseXML;

	}
	catch (error)
	{
		console.debug("Unable to sort the teams: " + error);
	}
}

function applyGamesPage(responseXML)
{
	console.debug("applyeGamesPage() responseXML = " + responseXML);
	try
	{
		// determine if favorite team
		var listElem = responseXML.rootElement.getElementByName("body").getElementByName("listByNavigation");
		var pageId = listElem.getAttribute("id");
		var teamId = pageId.substring(pageId.lastIndexOf(".") + 1);
		var isFavorite = getFavoriteTeamIds().indexOf(teamId) > -1;
		console.debug("applyGamesPage() teamId: " + teamId + " isFavorite: " + isFavorite);

		var favButton = responseXML.getElementById("favoriteButton");
		var favoriteButtonLabel = favButton.getElementByName("label");
		console.debug("favoriteButtonLabel=" + favoriteButtonLabel);
		if (isFavorite)
		{
			var newLabel = "Remove from Favorites";
			favButton.setAttribute('accessibilityLabel', newLabel);
			favoriteButtonLabel.textContent = newLabel;
			console.debug("changing to Remove From Favorites favoriteButtonLabel.textContent=" + favoriteButtonLabel.textContent);
		}
		else
		{
			var newLabel = "Add to Favorites";
			favButton.setAttribute('accessibilityLabel', newLabel);
			favoriteButtonLabel.textContent = newLabel;
			console.debug("changing to Add to Favorites favoriteButtonLabel.textContent=" + favoriteButtonLabel.textContent);
		}

		return responseXML;
	}
	catch (error)
	{
		console.log("Unable to apply changes to the games page: " + error);
	}
}

/**
 * Should be called by the onRefresh event, I am also calling this from the handleNavigate function below to share code. Essentially this function: 1. Loads the current Navigation file 2. Pulls the
 * menu items out of the loaded file and dumps them into the current file 3. Checks to see if an onRefresh AJAX call is already being made and if so does nothing.
 * 
 * 
 * @params string url - url to be loaded
 */
function handleGamesPageRefresh(urlParam)
{
	var url = atv.localStorage[GAMES_PAGE_REFRESH_URL_KEY];
	if (url == null)
	{
		url = urlParam;
	}
	
	console.log("Handle refresh event called");

	console.log("We are about to check the value of Ajax.currentlyRefreshing: --> " + Ajax.currentlyRefreshing);
	// check the Ajax object and see if a refresh event is happening
	if (!Ajax.currentlyRefreshing)
	{

		// if a refresh event is not happening, make another one.
		console.log("About to make the AJAX call. --> ")

		var refresh = new Ajax({
			"url" : url,
			"refresh" : true,
			"success" : function()
			{
				var req = arguments[0];

				// update the content page
				var newDocument = atv.parseXML(req.responseText), // Instead of using the responseXML you should parse the responseText. I know it seems redundent but trust me.
				menu = document.rootElement.getElementByTagName('menu'), oldMenuSections = menu.getElementByTagName('sections'), newMenuSections = newDocument.rootElement.getElementByTagName('sections'), now = new Date();

				applyGamesPage(newDocument);

				newMenuSections.removeFromParent();
				menu.replaceChild(oldMenuSections, newMenuSections);

			}
		});

		console.debug("I have made the AJAX call. <--");
	}

	console.debug("I am on the other side of the if. <--");

}

/**
 * This function works very similar to the handleRefresh Essentially we are manually loading the data and updating the page in Javascript This means we have to handle a bit more of the magic, but we
 * also get to control what is updated and update more of the page, like headers, previews, etc.
 * 
 * In this example, I am: 1. Getting the new URL from the Navigation Item 2. Changing the onRefresh URL 3. Calling handleRefresh and passing the new url to let handleRefresh do the magic.
 */
function handleGamesPageNavigate(e)
{
	console.log("Handle navigate event called: " + e + ", e.navigationItemId = " + e.navigationItemId + " document = " + document);

	// get the url
	var url = document.getElementById(e.navigationItemId).getElementByTagName('url').textContent;
	atv.localStorage[GAMES_PAGE_REFRESH_URL_KEY] = url;

	handleGamesPageRefresh(url);

	e.onCancel = function()
	{
		// Cancel any outstanding requests related to the navigation event.
		console.log("Navigation got onCancel.");
	}
}

function getFavoriteTeamIds()
{
	var favTeamIds = atv.localStorage[FAVORITE_TEAMS_KEY];
	console.debug("favoriteTeamIds=" + favTeamIds);
	return (favTeamIds != null) ? favTeamIds : [];
}

function toggleFavoriteTeam(teamId)
{
	try
	{
		console.log("toggleFavoriteTeams " + teamId);
		var favoriteTeamIds = getFavoriteTeamIds();

		var isFavorite;
		if (favoriteTeamIds == null || favoriteTeamIds.length == 0)
		{
			// first favorite team
			favoriteTeamIds = [ teamId ];
			isFavorite = true;
		}
		else
		{
			var teamIdIndex = favoriteTeamIds.indexOf(teamId);
			if (teamIdIndex > -1)
			{
				// remove
				favoriteTeamIds.splice(teamIdIndex, 1);
				isFavorite = false;
			}
			else
			{
				// add
				favoriteTeamIds.push(teamId);
				isFavorite = true;
			}
		}

		atv.localStorage[FAVORITE_TEAMS_KEY] = favoriteTeamIds;

		var favoriteButton = document.getElementById("favoriteButton");
		if (favoriteButton)
		{
			var newLabel = (isFavorite) ? "Remove from Favorites" : "Add to Favorites";
			var label = favoriteButton.getElementByName("label");
			label.textContent = newLabel;
			console.debug("after label.textContent: " + label.textContent);
		}
	}
	catch (error)
	{
		console.error("Caught exception trying to toggle DOM element: " + error);
	}

	console.debug("toggleFavoriteTeam() end " + teamId);
}

function toggleScores(id)
{
	try
	{
		var menuItem = document.getElementById(id);
		if (menuItem)
		{
			var label = menuItem.getElementByName('rightLabel');
			if (label.textContent == "Show")
			{
				label.textContent = "Hide";
				atv.localStorage[SHOW_SCORES_KEY] = "false";
			}
			else
			{
				label.textContent = "Show";
				atv.localStorage[SHOW_SCORES_KEY] = "true";
			}

			console.debug("showScores: " + atv.localStorage[SHOW_SCORES_KEY]);
		}
	}
	catch (error)
	{
		console.error("Caught exception trying to toggle DOM element: " + error);
	}
}
console.log("~~~~~ MLB.TV main.js end");
HTTP/1.1 200 OK
Server: Oracle-iPlanet-Web-Server/7.0
Date: Wed, 08 Aug 2012 16:15:11 GMT
Content-type: text/html
Content-type: application/json
Transfer-encoding: chunked

11c
var config = {env: '_proddc2split', imageBase: 'http://mlb.mlb.com/mlb/images/flagstaff', appletvBase: 'http://lws.mlb.com/appletvv2', bamServiceBase: 'https://mlb-ws.mlb.com', gamedayBase: 'http://gdx.mlb.com', mlbBase: 'http://www.mlb.com', secureMlbBase: 'https://secure.mlb.com'};
0

HTTP/1.1 200 OK
Server: Oracle-iPlanet-Web-Server/7.0
Date: Wed, 08 Aug 2012 16:15:11 GMT
Content-type: application/x-javascript
Last-modified: Mon, 23 Apr 2012 14:41:39 GMT
Content-length: 17889
Etag: "45e1-4f956a23"
Accept-ranges: bytes

// ***************************************************

var EMAIL_IDENTITY_POINT_KEY = "email.identityPointId";
var EMAIL_FINGERPRINT_KEY = "email.fingerprint";

// atv.Element extensions
atv.Element.prototype.getElementsByTagName = function(tagName) {
	return this.ownerDocument.evaluateXPath("descendant::" + tagName, this);
}

// Extend atv.ProxyDocument to load errors from a message and description.
atv.ProxyDocument.prototype.loadError = function(message, description) {
	var doc = atvutils.makeErrorDocument(message, description);
	this.loadXML(doc);
}

// Extend atv.ProxyDocument to load errors from a message and description.
atv.ProxyDocument.prototype.swapXML = function(xml) {
	var me = this;
    try 
    {
        me.loadXML(xml, function(success) {
            if ( success ) {
                atv.unloadPage();
            } else {
                console.log("swapXML failed to load " + xml);
                me.loadXML(atvutils.siteUnavailableError(), function(success) {
                	console.log("swapXML site unavailable error load success " + success);
                    if ( success ) {
                        atv.unloadPage();
                    }
                });
            }
        });
    } 
    catch (e) 
    {
        console.error("swapXML caught exception while loading " + xml + ". " + e);
        me.loadXML(atvutils.siteUnavailableError(), function(success) {
            if ( success ) {
                atv.unloadPage();
            }
        });
    }
}

// atv.Document extensions
atv.Document.prototype.getElementById = function(id) {
 var elements = this.evaluateXPath("//*[@id='" + id + "']", this);
 if ( elements && elements.length > 0 ) {
     return elements[0];
 }
 return undefined;
}

// atv.Element extensions
atv.Element.prototype.getElementsByTagName = function(tagName) {
 return this.ownerDocument.evaluateXPath("descendant::" + tagName, this);
}

atv.Element.prototype.getElementByTagName = function(tagName) {
 var elements = this.getElementsByTagName(tagName);
 if ( elements && elements.length > 0 ) {
     return elements[0];
 }
 return undefined;
}

// ATVUtils - a JavaScript helper library for Apple TV
ATVUtils = function() {};

ATVUtils.prototype.onGenerateRequest = function (request) {
	
    console.log('atvutils.js atv.onGenerateRequest() oldUrl: ' + request.url);

    console.debug("atvutils.js atv.onGenerateRequest() atv.localStorage[mediaMetaRequestParams] " + atv.localStorage[MEDIA_META_REQUEST_PARAMS_KEY]);
	if ( request.url.indexOf("encode(MEDIA_META_REQUEST_PARAMS)") > -1 && atv.localStorage[MEDIA_META_REQUEST_PARAMS_KEY])
	{
		request.url = request.url.replace("encode(MEDIA_META_REQUEST_PARAMS)", encodeURIComponent(atv.localStorage[MEDIA_META_REQUEST_PARAMS_KEY]));
	}
	console.debug("atvutils.js atv.onGenerateRequest() atv.localStorage[mediaMetaRequestParams] " + atv.localStorage[MEDIA_META_REQUEST_PARAMS_KEY]);
	if ( request.url.indexOf("MEDIA_META_REQUEST_PARAMS") > -1 && atv.localStorage[MEDIA_META_REQUEST_PARAMS_KEY])
	{
		request.url = request.url.replace("MEDIA_META_REQUEST_PARAMS", atv.localStorage[MEDIA_META_REQUEST_PARAMS_KEY]);
	}
	if ( request.url.indexOf("IPID") > -1 && atv.localStorage[BEST_IDENTITY_POINT_KEY])
	{
		request.url = request.url.replace("IPID", atv.localStorage[BEST_IDENTITY_POINT_KEY]);
	}
	if ( request.url.indexOf("FINGERPRINT") > -1 && atv.localStorage[BEST_FINGERPRINT_KEY])
	{
		request.url = request.url.replace("FINGERPRINT", encodeURIComponent(atv.localStorage[BEST_FINGERPRINT_KEY]));
	}
	if ( request.url.indexOf("SESSION_KEY") > -1 )
	{
		var sessionKey = atv.localStorage[SESSION_KEY]; 
		console.debug("atvutils.js atv.onGenerateRequest sessionKey=" + sessionKey);
		request.url = request.url.replace("SESSION_KEY", (sessionKey) ? encodeURIComponent(sessionKey) : "");
	}
	if ( request.url.indexOf("encode(LINK_PROXY_REQUEST)") > -1 && atv.localStorage[EMAIL_IDENTITY_POINT_KEY] != null && atv.localStorage[EMAIL_FINGERPRINT_KEY] != null)
	{
		var linkRequest = serviceClient.createLinkRequest();
		console.debug("atvutils.js atv.onGenerateRequest() linkRequest " + linkRequest);
		if (linkRequest != null)
		{
			// WARNING: encode twice because the xs:uri type does not suppot curly brackets so encode an extra time. The LinkProxy class will be responsible for url unencoding the json 
			request.url = request.url.replace("encode(LINK_PROXY_REQUEST)", encodeURIComponent(encodeURIComponent(JSON.stringify(linkRequest))));
		}
	}
	if ( request.url.indexOf("$$showScores") > -1 )
	{
		var showScores = atv.localStorage[SHOW_SCORES_KEY]; 
		console.log("showScores=" + showScores);
		request.url = request.url.replace("$$showScores", (showScores) ? showScores : "true");
	}
	var authorized = atv.localStorage[AUTHORIZED_KEY]; 
	if ( request.url.indexOf("AUTH_MESSAGE_CODE") > -1 && authorized != null)
	{
		console.debug("atvutils.js AUTH_MESSAGE_CODE atv.onGenerateRequest() authorized " + authorized);
//		request.url = request.url.replace("AUTH_MESSAGE_CODE", ((authorized == "true") ? "ALREADY_PURCHASED" : "NOT_AUHTORIZED"));
	}

	console.log('atvutils.js atv.onGenerateRequest() newUrl: ' + request.url);
}


ATVUtils.prototype.makeRequest = function(url, method, headers, body, callback) {
    if ( !url ) {
        throw "loadURL requires a url argument";
    }

	var method = method || "GET";
	var	headers = headers || {};
	var body = body || "";
	
    var xhr = new XMLHttpRequest();
    xhr.onreadystatechange = function() {
        try {
            if (xhr.readyState == 4 ) {
                if ( xhr.status == 200) {
                	console.log("makeRequest() getAllResponseHeaders(): " + xhr.getAllResponseHeaders());
                    callback(xhr.responseXML, xhr.responseText);
                } else {
                    console.log("makeRequest() received HTTP status " + xhr.status + " for " + url);
                    callback(xhr.responseXML, xhr.responseText);
                }
            }
        } catch (e) {
            console.error('makeRequest caught exception while processing request for ' + url + '. Aborting. Exception: ' + e);
            xhr.abort();
            callback(null, null);
        }
    }
    
    var request = {url: url};
    this.onGenerateRequest(request);
    
    console.debug("makeRequest() url " + request.url);

    xhr.open(method, request.url, true);

    for(var key in headers) {
        xhr.setRequestHeader(key, headers[key]);
    }

    console.debug("sending body in the send() method");
    xhr.send(body);
    return xhr;
}

ATVUtils.prototype.getRequest = function(url, callback) {
	return this.makeRequest(url, "GET", null, null, callback);
}

ATVUtils.prototype.postRequest = function(url, body, callback) {
	return this.makeRequest(url, "POST", null, body, callback);
}

ATVUtils.prototype.makeErrorDocument = function(message, description) {
    if ( !message ) {
        message = "";
    }
    if ( !description ) {
        description = "";
    }

    var errorXML = '<?xml version="1.0" encoding="UTF-8"?> \
    <atv> \
        <body> \
            <dialog id="com.bamnetworks.errorDialog"> \
                <title><![CDATA[' + message + ']]></title> \
                <description><![CDATA[' + description + ']]></description> \
            </dialog> \
        </body> \
    </atv>';

    return atv.parseXML(errorXML);
}

ATVUtils.prototype.makeOkDialog = function(message, description, onSelect) {
	if ( !message ) {
		message = "";
	}
	if ( !description ) {
		description = "";
	}
	
	if (!onSelect)
	{
		onSelect = "atv.unloadPage();";
	}
	
	var errorXML = '<?xml version="1.0" encoding="UTF-8"?> \
		<atv> \
		<head> \
			<script src="'+config.appletvBase+'/views/config.js" />\
			<script src="'+config.appletvBase+'/js/atvutils.js" />\
			<script src="'+config.appletvBase+'/js/service.js" />\
			<script src="'+config.appletvBase+'/js/main.js" />\
		</head>\
		<body> \
		<optionDialog id="com.bamnetowrks.optionDialog"> \
		<header><simpleHeader><title>' + message + '</title></simpleHeader></header> \
		<description><![CDATA[' + description + ']]></description> \
		<menu><sections><menuSection><items> \
			<oneLineMenuItem id="lineMenu" accessibilityLabel="OK" onSelect="' + onSelect + '">\
				<label>OK</label>\
			</oneLineMenuItem>\
		</items></menuSection></sections></menu>\
		</optionDialog> \
		</body> \
		</atv>';
	
	var doc = atv.parseXML(errorXML);
	return doc;
}

ATVUtils.prototype.printObject = function(object) {
	for (property in object)
	{
		console.log(property + ": " + object[property]);
	}
}


ATVUtils.prototype.siteUnavailableError = function() {
    // TODO: localize
	console.debug("siteUnavailableError()");
    return this.makeOkDialog("MLB.TV error", "MLB.TV is currently unavailable. Please try again later.");
}

ATVUtils.prototype.loadError = function(message, description) {
    atv.loadXML(this.makeErrorDocument(message, description));
}

ATVUtils.prototype.loadAndSwapError = function(message, description) {
    atv.loadAndSwapXML(this.makeErrorDocument(message, description));
}

ATVUtils.prototype.loadURLInternal = function(url, method, headers, body, loader) {
    var me = this;
    var xhr;
    var proxy = new atv.ProxyDocument;
    proxy.show();
    proxy.onCancel = function() {
        if ( xhr ) {
            xhr.abort();
        }
    };
    
    xhr = me.makeRequest(url, method, headers, body, function(xml, xhr) {
        try {
        	console.log("loadURLInternal() proxy=" + proxy + " xml=" + xml + " xhr=" + xhr);
            loader(proxy, xml, xhr);
        } catch(e) {
            console.error("Caught exception loading " + url + ". " + e);
            loader(proxy, me.siteUnavailableError(), xhr);
        }
    });
}

ATVUtils.prototype.loadGet = function(url, loader) {
	this.loadURLInternal(url, "GET", null, null, loader);
}

// TODO:: change to a url s) {
            if ( success ) {
                atv.unloadPage();
            }
        });
    }
}

// TODO:: change to a url string or hash
// loadAndSwapURL can only be called from page-level JavaScript of the page that wants to be swapped out.
ATVUtils.prototype.loadAndSwapURL = function(url, method, headers, body, processXML) {
    var me = this;
    this.loadURLInternal(url, method, headers, body, function(proxy, xml) { 
	    if(typeof(processXML) == "function") processXML.call(this, xml);
        me.swapXML(proxy, xml);
    });
}

/** Load URL and apply changes to the xml */
ATVUtils.prototype.loadAndApplyURL = function(url, processXML) {
    var me = this;
	this.loadURLInternal(url, 'GET', null, null, function(proxy, xml, xhr) {
		var xmlToLoad = xml;
        if(typeof(processXML) == "function") xmlToLoad = processXML.call(this, xml);
        if (!xmlToLoad) xmlToLoad = xml;
		try {
            proxy.loadXML(xmlToLoad, function(success) {
                if ( !success ) {
                    console.log("loadURL failed to load " + url);
                    proxy.loadXML(me.siteUnavailableError());
                }
            });
        } catch (e) {
            console.log("loadURL caught exception while loading " + url + ". " + e);
            proxy.loadXML(me.siteUnavailableError());
        }
    });
}

var atvutils = new ATVUtils;

console.log("atvutils initialized " + atvutils);

/**
 * This is an AXR handler. It handles most of tediousness of the XHR request and keeps track of onRefresh XHR calls so that we don't end up with multiple page refresh calls.
 * 
 * You can see how I call it on the handleRefresh function below.
 * 
 * 
 * @params object (hash) $h
 * @params string $h.url - url to be loaded
 * @params string $h.method - "GET", "POST", "PUT", "DELTE"
 * @params bool $h.type - false = "Sync" or true = "Async" (You should always use true)
 * @params func $h.success - Gets called on readyState 4 & status 200
 * @params func $h.failure - Gets called on readyState 4 & status != 200
 * @params func $h.callback - Gets called after the success and failure on readyState 4
 * @params string $h.data - data to be sent to the server
 * @params bool $h.refresh - Is this a call from the onRefresh event.
 */
function Ajax($h)
{
	var me = this;
	$h = $h || {}

	// Setup properties
	this.url = $h.url || false;
	this.method = $h.method || "GET";
	this.type = ($h.type === false) ? false : true;
	this.success = $h.success || null;
	this.failure = $h.failure || null;
	this.data = $h.data || null;
	this.complete = $h.complete || null;
	this.refresh = $h.refresh || false;

	if (!this.url)
	{
		console.error('\nAjax Object requires a url to be passed in: e.g. { "url": "tring or hash
ATVUtils.prototype.loadURL = function(url, method, headers, body, processXML) {
    var me = this;
    this.loadURLInternal(url, method, headers, body, function(proxy, xml) {
        if(typeof(processXML) == "function") processXML.call(this, xml);
		try {
            proxy.loadXML(xml, function(success) {
                if ( !success ) {
                    console.log("loadURL failed to load " + url);
                    proxy.loadXML(me.siteUnavailableError());
                }
            });
        } catch (e) {
            console.log("loadURL caught exception while loading " + url + ". " + e);
            proxy.loadXML(me.siteUnavailableError());
        }
    });
}

ATVUtils.prototype.swapXML = function(proxy, xml) {
	var me = this;
    try {
        proxy.loadXML(xml, function(success) {
            if ( success ) {
                atv.unloadPage();
            } else {
                console.log("swapXML failed to load " + xml);
                proxy.loadXML(me.siteUnavailableError(), function(success) {
                    if ( success ) {
                        atv.unloadPage();
                    }
                });
            }
        });
    } catch (e) {
        console.error("swapXML caught exception while loading " + xml + ". " + e);
        proxy.loadXML(me.siteUnavailableError(), function(successsome string" }\n')
		return undefined;
	}

	this.createRequest();
	this.req.onreadystatechange = this.stateChange;
	this.req.object = this;
	this.open();
	this.send();
}

Ajax.currentlyRefreshing = false;

Ajax.prototype.stateChange = function()
{
	var me = this.object;

	// console.log("this in stateChage is: "+ this);
	// console.log("object in stateChange is: "+ this.object);

	switch (this.readyState)
	{

	case 1:
		if (typeof (me.connection) === "function")
			me.connection(this, me);
		break;
	case 2:
		if (typeof (me.received) === "function")
			me.received(this, me);
		break;
	case 3:
		if (typeof (me.processing) === "function")
			me.processing(this, me);
		break;
	case 4:
		if (this.status == "200")
		{
			if (typeof (me.success) === "function")
				me.success(this, me);
		}
		else
		{
			if (typeof (me.failure) === "function")
				me.failure(this.status, this, me);
		}
		if (typeof (me.complete) === "function")
			me.complete(this, me);
		if (me.refresh)
			Ajax.currentlyRefreshing = false;
		break;
	default:
		console.log("I don't think I should be here.");
		break;
	}
}

Ajax.prototype.createRequest = function()
{
	try
	{
		this.req = new XMLHttpRequest();
		if (this.refresh)
			Ajax.currentlyRefreshing = true;
	}
	catch (error)
	{
		alert("The request could not be created: </br>" + error);
		console.error("failed to create request: " + error);
	}

}

Ajax.prototype.open = function()
{
	try
	{
		this.req.open(this.method, this.url, this.type);
	}
	catch (error)
	{
		console.error("failed to open request: " + error);
	}
}

Ajax.prototype.send = function()
{
	var data = this.data || null;
	try
	{
		this.req.send(data);
	}
	catch (error)
	{
		console.error("failed to send request: " + error);
	}
}


// End ATVUtils
// ***************************************************



FlowManager = function()
{
};

FlowManager.prototype.setTargetPage = function(pageId)
{
	atv.localStorage[TARGET_PAGEID_KEY] = pageId;
	console.debug("flowManager.setTargetPage() atv.localStorage[TARGET_PAGEID_KEY] " + atv.localStorage[TARGET_PAGEID_KEY]);
}

FlowManager.prototype.clearTargetPage = function()
{
	atv.localStorage.removeItem(TARGET_PAGEID_KEY);
	console.debug("flowManager.clearTargetPage() atv.localStorage[TARGET_PAGEID_KEY] " + atv.localStorage[TARGET_PAGEID_KEY]);
}

FlowManager.prototype.startPurchaseFlow = function()
{
	atv.localStorage[PURCHASE_FLOW_KEY] = "true";
	console.debug("flowManager.startPurchaseFlow() atv.localStorage[PURCHASE_FLOW_KEY] " + atv.localStorage[PURCHASE_FLOW_KEY]);
}

FlowManager.prototype.endPurchaseFlow = function()
{
	atv.localStorage.removeItem(PURCHASE_FLOW_KEY);
	atv.localStorage.removeItem(PURCHASE_PRODUCT_ID_KEY);
	console.debug("flowManager.endPurchaseFlow() atv.localStorage[PURCHASE_FLOW_KEY] " + atv.localStorage[PURCHASE_FLOW_KEY]);
}

FlowManager.prototype.inPurchaseFlow = function()
{
	console.debug("flowManager.inPurchaseFlow() atv.localStorage[PURCHASE_FLOW_KEY] " + atv.localStorage[PURCHASE_FLOW_KEY]);
	return (atv.localStorage[PURCHASE_FLOW_KEY] == "true");
}

FlowManager.prototype.inPurchaseFlow = function()
{
	console.debug("flowManager.inPurchaseFlow() atv.localStorage[PURCHASE_FLOW_KEY] " + atv.localStorage[PURCHASE_FLOW_KEY]);
	return (atv.localStorage[PURCHASE_FLOW_KEY] == "true");
}

FlowManager.prototype.inMediaFlow = function()
{
	console.debug("flowManager.inMediaFlow() atv.localStorage[MEDIA_META_KEY] " + atv.localStorage[MEDIA_META_KEY]);
	return atv.localStorage.getItem(MEDIA_META_KEY) != null && atv.localStorage.getItem(MEDIA_META_REQUEST_PARAMS_KEY) != null;
}

FlowManager.prototype.endMediaFlow = function()
{
	atv.localStorage.removeItem(MEDIA_META_KEY);
	atv.localStorage.removeItem(MEDIA_META_REQUEST_PARAMS_KEY);
	console.debug("flowManager.endMediaFlow() atv.localStorage[MEDIA_META_KEY] " + atv.localStorage[MEDIA_META_KEY]);
}

var flowManager = new FlowManager;

console.debug("atvutils.js flowManager created");HTTP/1.1 200 OK
Server: Oracle-iPlanet-Web-Server/7.0
Date: Wed, 08 Aug 2012 16:15:12 GMT
Content-type: application/x-javascript
Last-modified: Thu, 14 Jun 2012 17:05:42 GMT
Content-length: 27397
Etag: "6b05-4fda19e6"
Accept-ranges: bytes

console.log("~~~~~ MLB.TV service.js START");

var PRODUCT_LIST = {
	"IOSATBAT2012GDATVMON" : {
		"image" : "monthlyPremium.jpg",
		"period" : "month"
	}, 
	"IOSMLBTVYEARLY2012" : {
		"image" : "monthlyPremium.jpg",
		"period" : "year"
	}
};

var MLBTV_FEATURE_CONTEXT = "MLBTV2012.INAPPPURCHASE";
var MLBTV_FEATURE_NAME = "MLBALL";

var IOS_IDENTITY_POINT_KEY = "ios.identityPointId";
var IOS_FINGERPRINT_KEY = "ios.fingerprint";
var USERNAME_KEY = "username";
var EMAIL_IDENTITY_POINT_KEY = "email.identityPointId";
var EMAIL_FINGERPRINT_KEY = "email.fingerprint";
var BEST_IDENTITY_POINT_KEY = "best.identityPointId";
var BEST_FINGERPRINT_KEY = "best.fingerprint";
var EMAIL_LINKED_KEY = "email.linked";
var AUTHORIZED_KEY = "authorized";
var SHOW_LINK_STATUS = "showLinkStatus";
var MEDIA_META_KEY = "media.meta";
var MEDIA_META_REQUEST_PARAMS_KEY = "media.meta.requestParams";
var OFFER_URI_AFTER_AUTH_KEY = "offer.uriToLoadAfterAuth";
var OFFER_PURCHASE_FUNCTION_KEY = "offer.purchaseFunction";
var OFFER_SWAP_PAGE_KEY = "offer.swapPage";

ServiceClient = function()
{
};

ServiceClient.prototype.login = function(username, password, success, error)
{
	var url = config.bamServiceBase + "/pubajaxws/services/IdentityPointService";
	var identifyBody = "<soapenv:Envelope xmlns:soapenv=\"http://schemas.xmlsoap.org/soap/envelope/\" xmlns:ns=\"http://services.bamnetworks.com/registration/types/1.0\"><soapenv:Body><ns:identityPoint_identify_request><ns:identification type=\"email-password\"> \
			<ns:email><ns:address>" + username + "</ns:address></ns:email> \
			<ns:password>" + password + "</ns:password> \
		</ns:identification></ns:identityPoint_identify_request></soapenv:Body></soapenv:Envelope>";
	var headers = {
		"SOAPAction" : "http://services.bamnetworks.com/registration/identityPoint/identify",
		"Content-Type" : "text/xml"
	};

	console.log('Trying to authenticate with ' + url);

	atvutils.makeRequest(url, "POST", headers, identifyBody, function(document)
	{
		var identityPointId, fingerprint;
		var statusCode = (document && document.rootElement) ? document.rootElement.childElements[1].getElementByName("identityPoint_identify_response").getElementByName("status").getElementByName("code").textContent : -1;
		console.log('status code is ' + statusCode);

		var errorMessage;
		if (statusCode == 1)
		{
			var identification = document.rootElement.childElements[1].getElementByName("identityPoint_identify_response").getElementByName("identification");
			identityPointId = identification.getElementByName("id").textContent;
			fingerprint = identification.getElementByName("fingerprint").textContent;
		}
		else if (statusCode == -1000)
		{
			errorMessage = "The account name or password was incorrect. Please try again.";
		}
		else
		{
			errorMessage = "MLB.com is curently undergoing maintenance. Please try again.";
		}

		if (identityPointId && fingerprint)
		{
			atv.localStorage[USERNAME_KEY] = username;
			atv.localStorage[EMAIL_IDENTITY_POINT_KEY] = identityPointId;
			atv.localStorage[EMAIL_FINGERPRINT_KEY] = fingerprint;

			if (!atv.localStorage[BEST_IDENTITY_POINT_KEY] && !atv.localStorage[BEST_IDENTITY_POINT_KEY])
			{
				console.debug("saving the email as the best identity point id");
				atv.localStorage[BEST_IDENTITY_POINT_KEY] = identityPointId;
				atv.localStorage[BEST_FINGERPRINT_KEY] = fingerprint;
			}
			console.debug("best identity point id: " + atv.localStorage[BEST_IDENTITY_POINT_KEY]);

			console.debug("determine whether the user is authorized " + serviceClient.loadUserAuthorized);
			serviceClient.loadUserAuthorized(function()
			{
				if (success)
					success();
			});
		}
		else
		{
			if (error)
				error(errorMessage);
			console.log('error message ' + errorMessage);
		}
	});
}

ServiceClient.prototype.loadUserAuthorized = function(callback)
{
	var url = config.bamServiceBase + "/pubajaxws/services/FeatureService";
	var body = "<soapenv:Envelope xmlns:soapenv=\"http://schemas.xmlsoap.org/soap/envelope/\" xmlns:ns=\"http://services.bamnetworks.com/registration/types/1.6\">\
			<soapenv:Body>\
				<ns:feature_findEntitled_request>\
					<ns:identification type=\"fingerprint\">\
						<ns:id>" + atv.localStorage[BEST_IDENTITY_POINT_KEY] + "</ns:id>\
						<ns:fingerprint>" + atv.localStorage[BEST_FINGERPRINT_KEY] + "</ns:fingerprint>\
					</ns:identification>\
					<ns:featureContextName>" + MLBTV_FEATURE_CONTEXT + "</ns:featureContextName>\
				</ns:feature_findEntitled_request>\
			</soapenv:Body>\
		</soapenv:Envelope>";

	var headers = {
		"SOAPAction" : "http://services.bamnetworks.com/registration/feature/findEntitledFeatures",
		"Content-Type" : "text/xml"
	};

	console.debug('loadUserAuthorized() Feature find entitlement request body ' + body);
	console.debug('loadUserAuthorized() Finding entitled features with ' + url);

	atvutils.makeRequest(url, "POST", headers, body, function(document, text)
	{
		console.debug("loadUserAuthorized() Feature.findEntitled response " + text);
		var authorized = (text.indexOf(MLBTV_FEATURE_NAME) > -1);
		console.debug("loadUserAuthorized() authorized " + authorized);
		atv.localStorage[AUTHORIZED_KEY] = authorized.toString();
		console.debug("loadUserAuthorized() set atv.localStorage[AUTHORIZED_KEY] to " + atv.localStorage[AUTHORIZED_KEY]);
		callback(authorized);
	});
}

ServiceClient.prototype.addRestoreObserver = function(successCallback, errorCallback)
{
	// Setup a transaction observer to see when transactions are restored and when new payments are made.

	var transReceived = [];
	
	var restoreObserver = {
		updatedTransactions : function(transactions)
		{
			console.debug("addRestoreObserver() updatedTransactions " + (transactions) ? transactions.length : null);

			if (transactions)
			{
				for ( var i = 0; i < transactions.length; ++i)
				{
					transaction = transactions[i];
					if (transaction.transactionState == atv.SKPaymentTransactionStateRestored)
					{
						console.debug("addRestoreObserver() Found restored transaction: " + GetLogStringForTransaction(transaction) + "\n---> Original Transaction: " + GetLogStringForTransaction(transaction.originalTransaction));
						
						// add
						transReceived.push(transaction);
						
						// finish the transaction
						atv.SKDefaultPaymentQueue.finishTransaction(transaction);
					}
				}
			}
			console.debug("addRestoreObserver() updatedTransactions " + transactions.length);
		},

		restoreCompletedTransactionsFailedWithError : function(error)
		{
			console.debug("addRestoreObserver() restoreCompletedTransactionsFailedWithError start " + error + " code: " + error.code);
			
			atv.SKDefaultPaymentQueue.removeTransactionObserver(this);
			if (typeof (errorCallback) == "function")
				errorCallback(error);
			
			console.debug("addRestoreObserver() restoreCompletedTransactionsFailedWithError end " + error + " code: " + error.code);
		},

		restoreCompletedTransactionsFinished : function()
		{
			console.debug("addRestoreObserver() restoreCompletedTransactionsFinished start");
			
			atv.SKDefaultPaymentQueue.removeTransactionObserver(this);
			
			console.log("addRestoreObserver() transReceived " + transReceived);
			if (typeof (successCallback) == "function")
			{
				successCallback(transReceived);
			}	
			
			console.debug("addRestoreObserver() restoreCompletedTransactionsFinished end");
		}
	};

	atv.SKDefaultPaymentQueue.addTransactionObserver(restoreObserver);
	return restoreObserver;
}

function GetLogStringForTransaction(t)
{
	var result = "Transaction: " + "transactionIdentifier=" + ((t.transactionIdentifier) ? t.transactionIdentifier : "") + ", transactionState=" + ((t.transactionState) ? t.transactionState : "") + ", transactionReceipt=" + ((t.transactionReceipt) ? t.transactionReceipt : "") + ", originalTransaction= " + ((t.originalTransaction) ? t.originalTransaction : "") + ", transactionDate=" + ((t.transactionDate) ? t.transactionDate : "");

	if (t.payment)
	{
		result += ", payment.productIdentifier=" + t.payment.productIdentifier + ", payment.quantity=" + t.payment.quantity;
	}

	if (t.error)
	{
		result += ", error.domain=" + t.error.domain + ", error.code=" + t.error.code + ", error.userInfo=" + t.error.userInfo;
	}

	return result;
}

ServiceClient.prototype.doRequestProduct = function(productIds, successCallback, errorCallback)
{
	console.debug("doRequestProduct() productIds " + productIds);
	// make another products request to get product information
	var request = new atv.SKProductsRequest(productIds);

	request.onRequestDidFinish = function()
	{
		console.debug("doRequestProduct() Products request succeeded");
		if (typeof (successCallback) == "function")
			successCallback();
	};

	request.onRequestDidFailWithError = function(error)
	{
		console.error("doRequestProduct() Products request failed. " + ErrorToDebugString(error));
		if (typeof (errorCallback) == "function")
			errorCallback(error);
	};

	request.onProductsRequestDidReceiveResponse = function(productsResponse)
	{
		console.log("doRequestProduct() Products request received response " + productsResponse);
		console.debug("There are " + productsResponse.products.length + " offers.");

		if (typeof (successCallback) == "function")
			successCallback(productsResponse);
	};

	console.debug("Calling doRequestProduct() start");
	request.start();
}

ServiceClient.prototype.startPurchase = function()
{
	var productID = atv.localStorage["purchase.productId"];

	if (!productID)
	{
		console.debug("startPurchase(): not making a purchase because productID was not provided");
	}
	else
	{
		// make another products request to get product information
		var request = new atv.SKProductsRequest([ productID ]);

		request.onRequestDidFailWithError = function(error)
		{
			console.error("startPurchase() Products request failed. " + ErrorToDebugString(error));
		};

		request.onProductsRequestDidReceiveResponse = function(productsResponse)
		{
			console.log("startPurchase() Products request received response " + productsResponse);
			console.debug("There are " + productsResponse.products.length + " offers.");

			if (productsResponse && productsResponse.products && productsResponse.products.length == 1)
			{
				var product = productsResponse.products[0];
				console.debug("Found available product for ID " + product);
				var payment = {
					product : product,
					quantity : 1
				};
				atv.SKDefaultPaymentQueue.addPayment(payment);
			}
		};

		console.debug("Calling doRequestProduct() start");
		request.start();
	}
}

ServiceClient.prototype.addPurchaseObserver = function(successCallback, errorCallback)
{
	console.debug("addPurchaseObserver(): Purchase product with identifier " + productID);

	var productID = atv.localStorage["purchase.productId"];

	var observer = {
		updatedTransactions : function(transactions)
		{
			console.debug("addPurchaseObserver(): purchase.updatedTransactions: " + transactions.length);

			for ( var i = 0; i < transactions.length; ++i)
			{
				var transaction = transactions[i];

				console.debug("addPurchaseObserver(): Processing transaction " + i + ": " + GetLogStringForTransaction(transaction));

				if (transaction.payment && transaction.payment.productIdentifier == productID)
				{
					console.debug("addPurchaseObserver(): transaction is for product ID " + productID);

					// This is the product ID this transaction observer was created for.
					switch (transaction.transactionState)
					{
					case atv.SKPaymentTransactionStatePurchasing:
						console.debug("addPurchaseObserver(): SKPaymentTransactionStatePurchasing");
						break;
					case atv.SKPaymentTransactionStateRestored:
					case atv.SKPaymentTransactionStatePurchased:
						var reason = transaction.transactionState == atv.SKPaymentTransactionStatePurchased ? "Purchased" : "Restored";
						console.debug("addPurchaseObserver(): SKPaymentTransactionStatePurchased: Purchase Complete!!! Reason: " + reason);
						atv.SKDefaultPaymentQueue.finishTransaction(transaction);
						atv.SKDefaultPaymentQueue.removeTransactionObserver(this);
						if (typeof (successCallback) == "function")
							successCallback(transaction);
						break;
					case atv.SKPaymentTransactionStateFailed:
						console.debug("addPurchaseObserver(): SKPaymentTransactionStateFailed. " + ErrorToDebugString(transaction.error));
						atv.SKDefaultPaymentQueue.finishTransaction(transaction);
						atv.SKDefaultPaymentQueue.removeTransactionObserver(this);
						if (typeof (errorCallback) == "function")
							errorCallback(transaction);
						break;
					default:
						if (typeof (errorCallback) == "function")
							errorCallback(transaction);
						console.error("addPurchaseObserver(): Unknown transaction state: " + transaction.transactionState);
						break;
					}
				}
			}
		}
	}

	atv.SKDefaultPaymentQueue.addTransactionObserver(observer);
	return observer;
}

ServiceClient.prototype.startMediaFlow = function(mediaMeta)
{
	console.debug("startMediaFlow() mediaMeta " + mediaMeta);

	if (mediaMeta)
	{
		// set media meta to the local storage
		atv.localStorage[MEDIA_META_KEY] = JSON.stringify(mediaMeta);

		mediaMeta.title = mediaMeta.awayTeamCity + " " + mediaMeta.awayTeamName + " vs " + mediaMeta.homeTeamCity + " " + mediaMeta.homeTeamName;
		mediaMeta.description = mediaMeta.feed;
		if (mediaMeta.awayTeamId && mediaMeta.homeTeamId)
		{
			mediaMeta.imageUrl = config.imageBase + "/recapThumbs/" + mediaMeta.awayTeamId + "_v_" + mediaMeta.homeTeamId + ".jpg";
		}

		// set the session key from the local storage
		mediaMeta.sessionKey = atv.localStorage["sessionKey"];

		var mediaMetaRequestParams = "";
		mediaMetaRequestParams += '&identityPointId=' + ((atv.localStorage[BEST_IDENTITY_POINT_KEY]) ? atv.localStorage[BEST_IDENTITY_POINT_KEY] : 'IPID');
		mediaMetaRequestParams += '&fingerprint=' + ((atv.localStorage[BEST_FINGERPRINT_KEY]) ? atv.localStorage[BEST_FINGERPRINT_KEY] : 'FINGERPRINT');
		mediaMetaRequestParams += "&contentId=" + mediaMeta.contentId;
		mediaMetaRequestParams += '&sessionKey=' + ((mediaMeta.sessionKey) ? encodeURIComponent(mediaMeta.sessionKey) : "SESSION_KEY");
		mediaMetaRequestParams += '&calendarEventId=' + mediaMeta.calendarEventId;
		mediaMetaRequestParams += "&audioContentId=" + ((mediaMeta.audioContentId) ? mediaMeta.audioContentId.replace(/\s/g, ',') : "");
		mediaMetaRequestParams += '&day=' + mediaMeta.day
		mediaMetaRequestParams += '&month=' + mediaMeta.month;
		mediaMetaRequestParams += '&year=' + mediaMeta.year;
		mediaMetaRequestParams += "&feedType=" + mediaMeta.feedType;
		mediaMetaRequestParams += "&playFromBeginning=" + mediaMeta.playFromBeginning;
		mediaMetaRequestParams += "&title=" + encodeURIComponent(mediaMeta.title);
		mediaMetaRequestParams += "&description=" + encodeURIComponent(mediaMeta.description);
		mediaMetaRequestParams += (mediaMeta.imageUrl) ? ("&imageUrl=" + encodeURIComponent(mediaMeta.imageUrl)) : "";

		// save in local storage (should be in session storage but session storage is not yet supported)
		atv.localStorage[MEDIA_META_KEY] = JSON.stringify(mediaMeta);
		atv.localStorage[MEDIA_META_REQUEST_PARAMS_KEY] = mediaMetaRequestParams;
		console.debug("storing into local storage mediaMetaRequestParams " + atv.localStorage[MEDIA_META_REQUEST_PARAMS_KEY]);
	}
}

ServiceClient.prototype.loadMediaPage = function(callback)
{
	if (atv.localStorage[MEDIA_META_REQUEST_PARAMS_KEY])
	{
		var mediaProxyUrl = config.appletvBase + "/views/MediaServiceProxy?";
		// mediaProxyUrl += '&identityPointId=' + atv.localStorage[BEST_IDENTITY_POINT_KEY];
		// mediaProxyUrl += '&fingerprint=' + encodeURIComponent(atv.localStorage[BEST_FINGERPRINT_KEY]);
		mediaProxyUrl += atv.localStorage[MEDIA_META_REQUEST_PARAMS_KEY];
		console.debug("loadMediaPage mediaProxyUrl " + mediaProxyUrl);

		// make request
		atvutils.getRequest(mediaProxyUrl, callback);
	}
	else if (callback)
	{
		callback();
	}
}

function applyOffer(document)
{
	console.debug("applyOffer() atv.localStorage[PURCHASE_FLOW_KEY] " + atv.localStorage[PURCHASE_FLOW_KEY]);
	console.debug("applyOffer() document " + document);
	var mlbSignIn = (document) ? document.getElementById("mlbSignIn") : null;
	if (mlbSignIn)
	{
		var label = mlbSignIn.getElementByName("label");
		console.debug("applyOffer() atv.localStorage[EMAIL_IDENTITY_POINT_KEY] " + atv.localStorage[EMAIL_FINGERPRINT_KEY]);
		var signedIn = atv.localStorage[EMAIL_IDENTITY_POINT_KEY] != null && atv.localStorage[EMAIL_FINGERPRINT_KEY] != null;
		console.debug("applyOffer() signedIn " + signedIn);

		var uriToLoadAfterAuth = atv.localStorage[OFFER_URI_AFTER_AUTH_KEY];

		if (signedIn)
		{
			label.removeFromParent();
			label = atv.parseXML("<label>Sign In with different user</label>").rootElement;
			label.removeFromParent();
			mlbSignIn.appendChild(label);

			mlbSignIn.setAttribute("onSelect", "atv.logout();atvutils.loadAndSwapURL('" + config.appletvBase + "/views/Login?urlToLoadAfterAuth=" + uriToLoadAfterAuth + "');");
			mlbSignIn.setAttribute("onPlay", "atv.logout();atvutils.loadAndSwapURL('" + config.appletvBase + "/views/Login?urlToLoadAfterAuth=" + uriToLoadAfterAuth + "');");
		}
		else
		{
			console.debug("applyOffer() uriToLoadAfterAuth " + uriToLoadAfterAuth);
			label.textContent = "MLB.TV Premium Subscriber Login";
			console.debug("applyOffer() mlbSignIn.getAttribute('onSelect') " + mlbSignIn.getAttribute('onSelect'));
			mlbSignIn.setAttribute("onSelect", "atvutils.loadAndSwapURL('" + config.appletvBase + "/views/Login?urlToLoadAfterAuth=" + uriToLoadAfterAuth + "');");
			mlbSignIn.setAttribute("onPlay", "atvutils.loadAndSwapURL('" + config.appletvBase + "/views/Login?urlToLoadAfterAuth=" + uriToLoadAfterAuth + "');");
			console.debug("applyOffer() mlbSignIn.getAttribute('onSelect') " + mlbSignIn.getAttribute('onSelect'));
		}
	}
}

ServiceClient.prototype.loadOfferPage = function(newPage)
{
	console.debug("loadOfferPage() enter");

	newPage
	
	// set purchase flow to true
	flowManager.startPurchaseFlow();

	var swap = (atv.localStorage[OFFER_SWAP_PAGE_KEY] == "true");
	console.debug("loadOfferPage() atv.localStorage[OFFER_SWAP_PAGE_KEY] " + atv.localStorage[OFFER_SWAP_PAGE_KEY]);
	console.debug("loadOfferPage() swap " + swap);

	// Use StoreKit to get the list of product identifiers.
	var request = new atv.SKProductsRequest(Object.keys(PRODUCT_LIST));
	request.onRequestDidFailWithError = function(error)
	{
		console.error("Products request failed. " + ErrorToDebugString(error));
		var xml = atvutils.makeOkDialog("MLB.TV", "MLB.TV Products could not be found. Please check again at a later time.");
		if (swap)
			newPage.swapXML(xml)
		else
			newPage.loadXML(xml);
	};
	request.onProductsRequestDidReceiveResponse = function(productsResponse)
	{
		// success
		console.debug("loadOfferPage() Products request received response " + productsResponse);
		console.debug("loadOfferPage() There are " + productsResponse.products.length + " offers.");

		// add image
		console.debug("loadOfferPage() checking for image location");
		for ( var i = 0; i < productsResponse.products.length; i++)
		{
			var product = productsResponse.products[i];
			console.debug("loadOfferPage() product.productIdentifier " + product.productIdentifier);
			if (PRODUCT_LIST[product.productIdentifier] != null)
			{
				product.image = PRODUCT_LIST[product.productIdentifier].image;
				console.debug("loadOfferPage() product.image " + product.image);

				product.period = PRODUCT_LIST[product.productIdentifier].period;
				console.debug("loadOfferPage() product.period " + product.period);
			}
		}

		// This page will display the offers, and call purchaseProduct below with the productId to purchase in an onSelect handler.
		var offerBody = "";
		offerBody += "productMeta=" + JSON.stringify(productsResponse);
		offerBody += "&purchaseFunction=" + atv.localStorage[OFFER_PURCHASE_FUNCTION_KEY];
		if (atv.localStorage[OFFER_URI_AFTER_AUTH_KEY])
		{
			offerBody += "&uriToLoadAfterAuth=" + atv.localStorage[OFFER_URI_AFTER_AUTH_KEY];
		}

		console.debug("loadOfferPage() offerBody: " + offerBody);

		atvutils.postRequest(config.appletvBase + '/views/Offer', offerBody, function(xml)
		{
			try
			{
				console.debug("loadOfferPage() postRequest xml=" + xml);
				applyOffer(xml);
				if (swap)
					newPage.swapXML(xml)
				else
					newPage.loadXML(xml);
			}
			catch (e)
			{
				console.error("Caught exception for the Offer page " + e);
				var errorXML = atvutils.makeOkDialog("MLB.TV Error", "MLB.TV Products could not be found. Please check again at a later time.");
				if (swap)
					newPage.swapXML(errorXML)
				else
					newPage.loadXML(errorXML);
			}
		});
	};

	console.debug("Calling loadOfferPage() start");
	request.start();
}
ServiceClient.prototype.isMediaResponseUnauthorized = function(xml)
{
	return (xml) ? this.isMediaUnauthorized(this.getMediaStatusCode(xml)) : true;
}

ServiceClient.prototype.isMediaUnauthorized = function(mediaStatusCode)
{
	return "" == mediaStatusCode || "UNDETERMINED" == mediaStatusCode || "NOT_AUHTORIZED" == mediaStatusCode || "LOGIN_REQUIRED" == mediaStatusCode || "INVALID_USER_CREDENTIALS_STATUS" == mediaStatusCode;
}

ServiceClient.prototype.isMediaPlayback = function(mediaStatusCode)
{
	return "VIDEO_PLAYBACK" == mediaStatusCode || "AUDIO_PLAYBACK" == mediaStatusCode;
}

ServiceClient.prototype.getMediaStatusCode = function(xml)
{
	var mediaStatusCodeElem = (xml && xml.rootElement) ? xml.rootElement.getElementByTagName("mediaStatusCode") : null;
	console.debug("getMediaStatusCode() mediaStatusCodeElem " + mediaStatusCodeElem);
	var mediaStatusCode = (mediaStatusCodeElem != null) ? mediaStatusCodeElem.textContent : "UNDETERMINED";
	console.debug("getMediaStatusCode() mediaStatusCode " + mediaStatusCode);
	return mediaStatusCode;
}

ServiceClient.prototype.createFingerprintIdpt = function(ipid, fingerprint)
{
	return {
		"type" : "fingerprint",
		"id" : ipid,
		"fingerprint" : fingerprint
	};
}

ServiceClient.prototype.createIosIdentityPoint = function(transaction)
{
	return {
		"type" : "iosInAppPurchase",
		"receipt" : transaction.transactionReceipt
	};
}

ServiceClient.prototype.createLinkRequest = function(transactions)
{
	var identityPoints = [];

	// email-password fingerprint identity point
	if (atv.localStorage[EMAIL_FINGERPRINT_KEY] && atv.localStorage[EMAIL_IDENTITY_POINT_KEY])
	{
		identityPoints.push(this.createFingerprintIdpt(atv.localStorage[EMAIL_IDENTITY_POINT_KEY], atv.localStorage[EMAIL_FINGERPRINT_KEY]));
	}

	if (transactions)
	{
		console.debug("createLinkRequest() filtering out for unique original transactions");
		
		// filter out unique original transactions
		var originalTransMap = {};
		
		for (var t = 0; t < transactions.length; t++)
		{
			var currTrans = transactions[t];
			var origTrans = currTrans.originalTransaction;
			var transId = (origTrans == null) ? currTrans.transactionIdentifier : origTrans.transactionIdentifier;  
			
			console.debug("createLinkRequest() transId " + transId);
			
			if (originalTransMap[transId] == null)
			{
				// transaction identity point
				identityPoints.push(this.createIosIdentityPoint(currTrans));
				
				originalTransMap[transId] = currTrans;
			}
		}
	}
	// best fingerprint identity point
	else if (atv.localStorage[BEST_FINGERPRINT_KEY] && atv.localStorage[BEST_IDENTITY_POINT_KEY])
	{
		identityPoints.push(this.createFingerprintIdpt(atv.localStorage[BEST_IDENTITY_POINT_KEY], atv.localStorage[BEST_FINGERPRINT_KEY]));
	}

	var linkRequest = null;
	if (identityPoints.length > 0)
	{
		// link request
		linkRequest = {
			"identityPoints" : identityPoints,
			"namespace" : "mlb",
			"key" : "h-Qupr_3eY"
		};
	}

	console.debug("createLinkRequest() linkRequest " + JSON.stringify(linkRequest));

	return linkRequest;
}

ServiceClient.prototype.doLink = function(transactions, successCallback, errorCallback)
{
	var linkRequest = this.createLinkRequest(transactions);

	if (linkRequest == null)
	{
		if (typeof (successCallback) == "function")
			successCallback();

		console.debug("doLink() link not called since nothing to link");
	}
	else
	{
		// link request
		var linkRequestBody = JSON.stringify(linkRequest);
		var req = new XMLHttpRequest();
		var url = config.mlbBase + "/mobile/LinkAll2.jsp";

		req.onreadystatechange = function()
		{
			try
			{
				if (req.readyState == 4)
				{
					console.log('link() Got link service http status code of ' + req.status);
					if (req.status == 200)
					{
						console.log('link() Response text is ' + req.responseText);
						var linkResponseJson = JSON.parse(req.responseText);

						if (linkResponseJson.status == 1)
						{
							console.debug("link() bestIdentityPointToUse: " + linkResponseJson.bestIdentityPointToUse);
							console.debug("link() messages: " + linkResponseJson.messages);

							if (linkResponseJson.bestIdentityPointToUse && linkResponseJson.bestIdentityPointToUse.id && linkResponseJson.bestIdentityPointToUse.fingerprint)
							{
								console.debug("link() saving the best identity point to use");
								atv.localStorage[BEST_IDENTITY_POINT_KEY] = linkResponseJson.bestIdentityPointToUse.id;
								atv.localStorage[BEST_FINGERPRINT_KEY] = linkResponseJson.bestIdentityPointToUse.fingerprint;
								console.debug("link() identityPointId " + atv.localStorage[BEST_IDENTITY_POINT_KEY]);

								if (linkResponseJson.bestIdentityPointToUse.id.toLowerCase().indexOf("ios") >= 0)
								{
									atv.localStorage[IOS_IDENTITY_POINT_KEY] = linkResponseJson.bestIdentityPointToUse.id;
									atv.localStorage[IOS_FINGERPRINT_KEY] = linkResponseJson.bestIdentityPointToUse.fingerprint;
								}
							}

							var linked = false;// , auth = false;
							for ( var m = 0; m < linkResponseJson.messages.length; m++)
							{
								var msg = linkResponseJson.messages[m];
								console.debug("doLink() msg " + msg);
								if (msg == "linked")
								{
									linked = true;
								}
							}
							linked = linked && atv.localStorage[EMAIL_IDENTITY_POINT_KEY] != null && atv.localStorage[EMAIL_FINGERPRINT_KEY] != null;
							// atv.localStorage[EMAIL_LINKED_KEY] = linked.toString();
							// atv.localStorage[AUTHORIZED_KEY] = auth.toString();

							// console.debug("doLink() atv.localStorage[EMAIL_LINKED_KEY] " + atv.localStorage[EMAIL_LINKED_KEY];
							console.debug("atv.localStorage[AUTHORIZED_KEY] " + atv.localStorage[AUTHORIZED_KEY]);

							if (typeof (successCallback) == "function")
								successCallback();

							console.log("doLink() getAllResponseHeaders: " + this.getAllResponseHeaders());
						}
						else
						{

							if (typeof (errorCallback) == "function")
								errorCallback();
							console.error("link response json has an invalid status code: " + linkResponseJson.status);
						}
					}
				}
			}
			catch (e)
			{
				// Specify a copyedited string because this will be displayed to
				// the user.
				console.error('Caught exception while processing request. Aborting. Exception: ' + e);
				if (typeof (errorCallback) == "function")
					errorCallback(e);

				req.abort();
			}
		}

		req.open("POST", url, true);
		req.setRequestHeader("Content-Type", "text/xml");

		console.log('Trying to link with ' + url);
		console.log("Link Service POST body is " + linkRequestBody);
		req.send(linkRequestBody);
	}
}

var serviceClient = new ServiceClient;

console.log("~~~~~ MLB.TV service.js END");HTTP/1.1 200 OK
Server: Oracle-iPlanet-Web-Server/7.0
Date: Wed, 08 Aug 2012 16:15:13 GMT
Content-type: application/x-javascript
Last-modified: Tue, 31 Jul 2012 21:32:23 GMT
Content-length: 42766
Etag: "a70e-50184ee7"
Accept-ranges: bytes

console.log("~~~~~ MLB.TV main.js start");

var IOS_IDENTITY_POINT_KEY = "ios.identityPointId";
var IOS_FINGERPRINT_KEY = "ios.fingerprint";
var BEST_IDENTITY_POINT_KEY = "best.identityPointId";
var BEST_FINGERPRINT_KEY = "best.fingerprint";
var USERNAME_KEY = "username";

var SHOW_SCORES_KEY = "showScores";
var FAVORITE_TEAMS_KEY = "favoriteTeamIds";

var SESSION_KEY = "sessionKey";
var MEDIA_META_KEY = "media.meta";
var MEDIA_META_REQUEST_PARAMS_KEY = "media.meta.requestParams";

var PURCHASE_FLOW_KEY = "flow.purchase";
var PURCHASE_PRODUCT_ID_KEY = "purchase.productId";

var EMAIL_LINKED_KEY = "email.linked";
var AUTHORIZED_KEY = "authorized";

var OFFER_URI_AFTER_AUTH_KEY = "offer.uriToLoadAfterAuth";
var OFFER_PURCHASE_FUNCTION_KEY = "offer.purchaseFunction";
var OFFER_SWAP_PAGE_KEY = "offer.swapPage";

var TARGET_PAGEID_KEY = "targetPage";
var PURCHASE_FLOW_KEY = "flow.purchase";
var OFFER_PAGE_ID = "com.bamnetworks.appletv.offer";

var TODAYS_GAMES_REFRESH_URL_KEY = "todaysGames.refreshUrl";
var GAMES_PAGE_REFRESH_URL_KEY = "games.refreshUrl";

// atv.onPageLoad
atv.onPageLoad = function(id)
{
	console.log("main.js atv.onPageLoad() id: " + id);

	// <mlbtv>
	var headElem = document.rootElement.getElementByName("head");
	if (headElem && headElem.getElementByName("mlbtv"))
	{
		var mlbtvElem = headElem.getElementByName("mlbtv");
		console.log("mlbtv exists");

		// store local variables
		storeLocalVars(mlbtvElem);

		// ping urls
		pingUrls(mlbtvElem);
	}

	// games by team
	if (id && id.indexOf("com.bamnetworks.appletv.gamesByTeam.") >= 0)
	{
		applyGamesPage(document);
	}

	if (id && id.indexOf("com.bamnetworks.appletv.media") >= 0 && typeof (flowManager) != "undefined")
	{
		flowManager.endPurchaseFlow();
		flowManager.endMediaFlow();
		console.debug("main.js onPageLoad() atv.localStorage[PURCHASE_FLOW_KEY] " + atv.localStorage[PURCHASE_FLOW_KEY]);
	}

	atv._debugDumpControllerStack();
}

// atv.onPageUnload
atv.onPageUnload = function(id)
{
	console.debug("main.js atv.onPageUnload() id " + id);
	console.debug("main.js atv.onPageUnload() atv.localStorage[PURCHASE_FLOW_KEY] " + atv.localStorage[PURCHASE_FLOW_KEY]);
	if (flowManager.inPurchaseFlow() && ("com.bamnetworks.appletv.authenticate" == id || id.indexOf("Not Authorized") > -1))
	{
		console.debug("main.js atv.onPageUnload() showing offer page");
		flowManager.setTargetPage(OFFER_PAGE_ID);

		var newPage = new atv.ProxyDocument();
		newPage.show();
		serviceClient.loadOfferPage(newPage);

		console.debug("main.js atv.onPageUnload() showing offer page");
	}
}

function storeLocalVars(mlbtvElem)
{
	// check local storage params
	var localStorageElems = mlbtvElem.getElementsByName("localStorage");
	console.debug("localStorageElems=" + localStorageElems);
	if (localStorageElems && localStorageElems.length > 0)
	{
		var index = 0;
		for (; index < localStorageElems.length; index++)
		{
			var key = localStorageElems[index].getAttribute("key");
			var value = localStorageElems[index].getAttribute("value");
			if (value)
			{
				atv.localStorage[key] = value;
			}
			else
			{
				atv.localStorage.removeItem(key);
			}
			console.log("localStorage[" + key + "] = " + atv.localStorage[key]);
		}
	}
}

function pingUrls(mlbtvElem)
{
	// check ping urls
	var pingElems = mlbtvElem.getElementsByName("ping");
	console.log("pingElems=" + pingElems);
	if (pingElems && pingElems.length > 0)
	{
		for ( var pingIndex = 0; pingIndex < pingElems.length; pingIndex++)
		{
			var url = pingElems[pingIndex].getAttribute('url');
			url = (url == null || url.trim().length() <= 0) ? pingElems[pingIndex].textContent : url;
			console.debug("url=" + url);
			if (url)
			{
				try
				{
					console.log('pinging reporting url ' + url);
					var req = new XMLHttpRequest();
					req.open("GET", url, true);
					req.send();
				}
				catch (e)
				{
					console.error("failed to ping the url " + url + " error " + e);
				}
			}
		}
	}
}

/**
 * Updates the logo according to the screen resolution
 * 
 * @param document
 * @returns
 */
function updateImageResolution(document)
{
	// update logos according to screen resolution
	var frame = atv.device.screenFrame;
	console.log("frame size: " + frame.height);
	if (frame.height == 1080)
	{
		var images = document.rootElement.getElementsByTagName('image');
		for ( var i = 0; i < images.length; i++)
		{
			images[i].textContent = images[i].textContent.replace('720p', '1080p');
		}
	}

	return document;
}

// ------------------------------ load and apply -------------------------------
function applyMain(document)
{
	console.log('applyMain ' + document);

	// end all flows when you're on the main page
	flowManager.endMediaFlow();
	flowManager.endPurchaseFlow();

	// check valid subscription
	// (1) via iTunes on this box and iTunes account signed in. Receipt is present and stored on the box
	// (2) via Vendor and is signed in with Vendor account.
	var validSubscription = (atv.localStorage[IOS_IDENTITY_POINT_KEY] != null && atv.localStorage[IOS_FINGERPRINT_KEY] != null) || (atv.localStorage[AUTHORIZED_KEY] == "true" && atv.localStorage[EMAIL_IDENTITY_POINT_KEY] != null && atv.localStorage[EMAIL_FINGERPRINT_KEY] != null);

	console.log("applyMain() validSubscription " + validSubscription);
	console.log("applyMain() atv.localStorage[AUTHORIZED_KEY] " + atv.localStorage[AUTHORIZED_KEY]);

	var subscribeMenu = document.getElementById("subscribeMlbTv");
	if (validSubscription)
	{
		if (subscribeMenu)
		{
			subscribeMenu.removeFromParent();
			console.debug("applyMain() removing the Subscribe To MLB.TV");
		}
	}
	else
	{
		// add the menu if it's not there
		if (!subscribeMenu)
		{
			subscribeMenu = atv.parseXML('<oneLineMenuItem id="subscribeMlbTv" accessibilityLabel="Subscribe To MLB.TV" onSelect="subscribeToMlbTv()"><label>Subscribe To MLB.TV</label></oneLineMenuItem>').rootElement;
			subscribeMenu.removeFromParent();

			var parent = document.rootElement.getElementByTagName("items");
			console.debug("applyMain() parent " + parent);
			if (parent && parent.childElements)
			{
				console.debug("applyMain() before parent.childElements " + parent.childElements);
				var settingsMenu = document.getElementById("settings");
				settingsMenu.removeFromParent();
				parent.appendChild(subscribeMenu);
				parent.appendChild(settingsMenu);
				console.debug("applyMain() after parent.childElements " + parent.childElements);
			}
		}
	}

	return document;
}

function handleMainVoltileReload()
{
	console.debug("handleMainVoltileReload() atv.localStorage[TARGET_PAGEID_KEY] " + atv.localStorage[TARGET_PAGEID_KEY]);
	if (atv.localStorage[TARGET_PAGEID_KEY] == null || atv.localStorage[TARGET_PAGEID_KEY] == "com.bamnetworks.appletv.main")
	{
		applyMain(document);
		atv.loadAndSwapXML(document);
	}
}

function handleOfferVoltileReload(document)
{
	console.debug("handleOfferVoltileReload() atv.localStorage[AUTHORIZED_KEY] " + atv.localStorage[AUTHORIZED_KEY]);
	if (atv.localStorage[AUTHORIZED_KEY] == "true")
	{
		console.debug("handleOfferVoltileReload() unloading the page because already authorized");
		atv.unloadPage();
	}
	else
	{
		applyOffer(document);
		atv.loadAndSwapXML(document);
	}
}

function applyTodaysGames(responseXML, refresh)
{
	console.log("applyTodaysGames() responseXML: " + responseXML);

	try
	{
		// elements
		var listByNavigation = responseXML.rootElement.getElementByTagName("listByNavigation");
		var imageMenuItems = responseXML.rootElement.getElementsByTagName("imageTextImageMenuItem");
		console.log("applyTodaysGames() navigation=" + navigation);

		// add behavior
		if (atv.localStorage[TODAYS_GAMES_REFRESH_URL_KEY] == null || !refresh)
		{
			var navigation = responseXML.rootElement.getElementByTagName("navigation");
			var currentIndex = navigation.getAttribute("currentIndex");
			console.debug("applyTodaysGames() currentIndex " + currentIndex);
			var refreshUrl = navigation.childElements[currentIndex].getElementByTagName("url").textContent;
			console.debug("applyTodaysGames() refreshUrl " + refreshUrl);
			atv.localStorage[TODAYS_GAMES_REFRESH_URL_KEY] = refreshUrl;
		}
		listByNavigation.setAttribute("refreshInterval", "60");
		listByNavigation.setAttribute("onRefresh", "console.log('refreshing todays games');handleTodaysGamesRefresh();");
		listByNavigation.setAttribute("onNavigate", "console.log('navigating todays games');handleTodaysGamesNavigate(event);");

		// sort
		var favTeamIds = getFavoriteTeamIds();
		function sort(menuOne, menuTwo)
		{
			var menuOneTeamIds = menuOne.getAttribute("id").split("-");
			var menuTwoTeamIds = menuTwo.getAttribute("id").split("-");

			var menuOneOrder = (favTeamIds.indexOf(menuOneTeamIds[0]) >= 0 || favTeamIds.indexOf(menuOneTeamIds[1]) >= 0) ? 1 : 0;
			var menuTwoOrder = (favTeamIds.indexOf(menuTwoTeamIds[0]) >= 0 || favTeamIds.indexOf(menuTwoTeamIds[1]) >= 0) ? 1 : 0;

			// console.log("favTeamIds.indexOf(menuOneTeamIds[0])=" + favTeamIds.indexOf(menuOneTeamIds[0]) + " favTeamIds.indexOf(menuOneTeamIds[1])=" + favTeamIds.indexOf(menuOneTeamIds[1]));
			// console.log(menuOneOrder + "-" + menuTwoOrder);

			return menuTwoOrder - menuOneOrder;
		}
		var orderedMenuList = imageMenuItems.sort(sort);

		// 1. re-append the sorted children
		// 2. set the favorite team logos
		// 3. hide scores on the page
		// 4. set the link to no scores preview
		var index = 0;
		for (; index < orderedMenuList.length; index++)
		{
			// 1. reappend child for sorting
			var menu = orderedMenuList[index];
			var parent = menu.parent;
			menu.removeFromParent();
			parent.appendChild(menu);

			// 2. set the favorite team logos
			console.debug("applyTodaysGames() menu item id " + menu.getAttribute("id"));
			var teamIds = menu.getAttribute("id").split("-");
			var isAwayFav = favTeamIds.indexOf(teamIds[0]) >= 0;
			var isHomeFav = favTeamIds.indexOf(teamIds[1]) >= 0;
			console.debug("applyTodaysGames() isAwayFav " + isAwayFav);
			console.debug("applyTodaysGames() isHomeFav " + isHomeFav);
			var leftImageElem = menu.getElementByName("leftImage");
			if (isAwayFav && leftImageElem.textContent.indexOf("-fav.png") < 0)
			{
				leftImageElem.textContent = leftImageElem.textContent.replace(".png", "-fav.png");
			}
			var rightImageElem = menu.getElementByName("rightImage");
			if (isHomeFav && rightImageElem.textContent.indexOf("-fav.png") < 0)
			{
				rightImageElem.textContent = rightImageElem.textContent.replace(".png", "-fav.png");
			}

			// 3. Hide scores
			// 4. Use the preview link with no scores
			var showScores = (atv.localStorage[SHOW_SCORES_KEY]) ? atv.localStorage[SHOW_SCORES_KEY] : "true";
			var rightLabelElem = menu.getElementByName("rightLabel");
			if (showScores == "false")
			{
				var previewLinkElem = menu.getElementByTagName("link");
				if (rightLabelElem && rightLabelElem.textContent.indexOf("Final") >= 0)
				{
					rightLabelElem.textContent = "Final";
				}
				if (previewLinkElem)
				{
					previewLinkElem.textContent = previewLinkElem.textContent.replace(".xml", "_noscores.xml");
				}
			}

			// 5. Check for double header 3:33 AM ET
			if (rightLabelElem && rightLabelElem.textContent.indexOf("3:33 AM") >= 0)
			{
				rightLabelElem.textContent = "Game 2";
			}
		}

		return responseXML;
	}
	catch (error)
	{
		console.error("unable to apply todays games " + error);
		atvutils.loadError("An error occurred loading today's games", "An error occurred loading today's games");
	}
}

/**
 * Should be called by the onRefresh event, I am also calling this from the handleNavigate function below to share code. Essentially this function: 1. Loads the current Navigation file 2. Pulls the
 * menu items out of the loaded file and dumps them into the current file 3. Checks to see if an onRefresh AJAX call is already being made and if so does nothing.
 * 
 * 
 * @params string url - url to be loaded
 */
function handleTodaysGamesRefresh()
{
	var url = atv.localStorage[TODAYS_GAMES_REFRESH_URL_KEY];
	console.log("handleTodaysGamesRefresh() url " + url);

	console.log("We are about to check the value of Ajax.currentlyRefreshing: --> " + Ajax.currentlyRefreshing);
	// check the Ajax object and see if a refresh event is happening
	if (!Ajax.currentlyRefreshing)
	{
		// if a refresh event is not happening, make another one.
		console.log("handleTodaysGamesRefresh()About to make the AJAX call. --> ")

		var refresh = new Ajax({
			"url" : url,
			"refresh" : true,
			"success" : function()
			{
				var req = arguments[0];

				console.log("handleTodaysGamesRefresh() Ajax url " + url);

				var newDocument = atv.parseXML(req.responseText); // Instead of using the responseXML you should parse the responseText. I know it seems redundent but trust me.

				// update the content page
				var newMenuSections = newDocument.rootElement.getElementByTagName('sections');
				applyTodaysGames(newDocument, true);
				newMenuSections.removeFromParent();

				var menu = document.rootElement.getElementByTagName('menu');
				console.log("handleTodaysGamesRefresh() menu=" + menu);
				if (menu)
				{
					var oldMenuSections = menu.getElementByTagName('sections');
					menu.replaceChild(oldMenuSections, newMenuSections);
				}
				else
				{
					menu = atv.parseXML("<menu></menu>").rootElement;
					menu.removeFromParent();
					menu.appendChild(newMenuSections);

					var listByNavigation = document.rootElement.getElementByTagName("listByNavigation");
					listByNavigation.appendChild(menu);
				}
				
				// replace # Games
				var newTumbler = newDocument.rootElement.getElementByTagName('tumblerWithSubtitle');
				newTumbler.removeFromParent();
				var header = document.rootElement.getElementByTagName('header');
				var oldTumbler = document.rootElement.getElementByTagName('tumblerWithSubtitle');
				header.replaceChild(oldTumbler, newTumbler);
			}
		});
		console.log("handleTodaysGamesRefresh() I have made the AJAX call. <--");
	}
	console.log("handleTodaysGamesRefresh() I am on the other side of the if. <--");
}

/**
 * This function works very similar to the handleRefresh Essentially we are manually loading the data and updating the page in Javascript This means we have to handle a bit more of the magic, but we
 * also get to control what is updated and update more of the page, like headers, previews, etc.
 * 
 * In this example, I am: 1. Getting the new URL from the Navigation Item 2. Changing the onRefresh URL 3. Calling handleRefresh and passing the new url to let handleRefresh do the magic.
 */
function handleTodaysGamesNavigate(e)
{
	console.log("handleTodaysGamesNavigate() " + e + ", e.navigationItemId = " + e.navigationItemId + " document = " + document);
	console.log("e=" + e);
	atvutils.printObject(e);
	console.log("document.getElementById(e.navigationItemId)=" + document.getElementById(e.navigationItemId));
	console.log("document.getElementById(e.navigationItemId).getElementByTagName=" + document.getElementById(e.navigationItemId).getElementByTagName);

	// get the url
	var url = document.getElementById(e.navigationItemId).getElementByTagName('url').textContent;
	atv.localStorage[TODAYS_GAMES_REFRESH_URL_KEY] = url;

	handleTodaysGamesRefresh();

	e.onCancel = function()
	{
		// Cancel any outstanding requests related to the navigation event.
		console.log("Navigation got onCancel.");
	}
}

function applyFeedSelectionPage(document)
{
	console.log("applyFeedSelectionPage() document: " + document);

	// add behavior by adding javascript file reference
	var head = document.rootElement.getElementByTagName("head");
	if (head)
	{
		var serviceJs = atv.parseXML('<script src="' + config.appletvBase + '/js/service.js" />').rootElement;
		serviceJs.removeFromParent();
		console.debug("adding service.js " + serviceJs);

		var children = head.childElements;
		children.unshift(serviceJs);

		for ( var i = 0; i < children.length; i++)
		{
			children[i].removeFromParent();
			head.appendChild(children[i]);
		}
	}

	flowManager.endMediaFlow();
	flowManager.endPurchaseFlow();
	console.log("purchase and media flow ended " + atv.localStorage[PURCHASE_PRODUCT_ID_KEY] + " " + atv.localStorage[MEDIA_META_REQUEST_PARAMS_KEY] + " " + atv.localStorage[MEDIA_META_KEY] + " " + atv.localStorage[SESSION_KEY]);

	return document;
}

// This is called from the feed_selection.xml
function loadFeed(mediaMeta)
{
	atv._debugDumpControllerStack();

	console.debug("showing new page..");
	var newPage = new atv.ProxyDocument();
	newPage.show();

	atv._debugDumpControllerStack();

	// store the media meta into the local storage for later use
	serviceClient.startMediaFlow(mediaMeta);
	atv.localStorage[OFFER_URI_AFTER_AUTH_KEY] = config.appletvBase + "/views/MediaServiceProxy?encode(MEDIA_META_REQUEST_PARAMS)";
	atv.localStorage[OFFER_PURCHASE_FUNCTION_KEY] = "restorePurchasePlay";
	atv.localStorage[OFFER_SWAP_PAGE_KEY] = "true";

	// load the media page
	serviceClient.loadMediaPage(function(document)
	{
		// media proxy returns a playback page, display
		var authorized = !serviceClient.isMediaResponseUnauthorized(document);
		// atv.localStorage.setItem(AUTHORIZED_KEY, (authorized) ? "true" : "false");
		if (authorized)
		{
			// if authorized, load the page
			console.debug("loadFeed() showing the media page");
			newPage.swapXML(document);
		}
		else
		{
			// if unauthorized, load the offer page
			console.debug("loadFeed() showing the offer page swap " + atv.localStorage[OFFER_SWAP_PAGE_KEY]);
			serviceClient.loadOfferPage(newPage);
		}
	});
}
// deprecated function name but it's still being generated by the gameday mule
var loadLiveGameVideoAsset = loadFeed;

function loadOfferPageAndPlay()
{
	var newPage = new atv.ProxyDocument();
	newPage.show();
	serviceClient.loadOfferPage(newPage);
}

function subscribeToMlbTv()
{
	console.debug("subscribeToMlbTv() enter");

	flowManager.endMediaFlow();

	// create new page where offer page will be loaded onto
	var newPage = new atv.ProxyDocument();
	newPage.show();

	// load the offer page onto the new page
	atv.localStorage[OFFER_URI_AFTER_AUTH_KEY] = config.appletvBase + "/views/Dialog?messageCode=AUTH_MESSAGE_CODE";
	atv.localStorage[OFFER_PURCHASE_FUNCTION_KEY] = "restoreAndPurchase";
	atv.localStorage[OFFER_SWAP_PAGE_KEY] = "false";
	serviceClient.loadOfferPage(newPage);
}

function ErrorToDebugString(error)
{
	var localizedDescription = error.userInfo["NSLocalizedDescription"];
	localizedDescription = localizedDescription ? localizedDescription : "Unknown";
	return error.domain + "(" + error.code + "): " + localizedDescription;
}

function linkManually()
{
	atv.loadURL(config.appletvBase + "/views/Login?urlToLoadAfterAuth=" + config.appletvBase + "/views/LinkProxy?request=encode(LINK_PROXY_REQUEST)");
}

function purchase(newPage)
{
	var observer = serviceClient.addPurchaseObserver(function(purchasedTransaction)
	{
		// PURCHASE success
		console.debug("purchase(): success purchased transactions " + purchasedTransaction);

		// link()
		serviceClient.doLink([ purchasedTransaction ], function()
		{
			console.debug("purchase(): link success");
			serviceClient.loadUserAuthorized(function()
			{
				console.debug("purchase(): load user authorized");
				newPage.loadXML(atvutils.makeOkDialog("MLB.TV Purchased", "You have successfully purchased MLB.TV."));
				atv.unloadPage();
			});
		}, function()
		{
			console.debug("purchase(): link error after purchase");
			newPage.cancel();
			atv.unloadPage();
			// do nothing -- go back to previous page
			// atv.loadAndSwapXML(atvutils.makeOkDialog("MLB.TV Error", "Failed to determine your MLB.TV subscription after purchase"));
		})
	}, function()
	{
		// PURCHASE error
		console.debug("purchase(): error ");
		newPage.cancel();
		atv.unloadPage();
	});

	newPage.onCancel = function()
	{
		atv.SKDefaultPaymentQueue.removeTransactionObserver(observer);
	};

	serviceClient.startPurchase();

}

function restoreAndPurchase(productId)
{
	atv._debugDumpControllerStack();

	// store product id into the cart
	atv.localStorage[PURCHASE_PRODUCT_ID_KEY] = productId;

	// create and show new page where dialogs will be loaded
	var newPage = new atv.ProxyDocument();
	newPage.show();

	atv._debugDumpControllerStack();

	var restoreObserver = serviceClient.addRestoreObserver(function(transactions)
	{
		// link()
		serviceClient.doLink(transactions, function()
		{
			// LINK success -- determine authorization thru feature service
			atv._debugDumpControllerStack();
			serviceClient.loadUserAuthorized(function()
			{
				console.debug("restoreAndPurchase() atv.localStorage[AUTHORIZED_KEY] " + atv.localStorage[AUTHORIZED_KEY]);
				if (atv.localStorage[AUTHORIZED_KEY] == "true")
				{
					console.debug("restoreAndPurchase() authorized or error page, showing Success page...");
					atv._debugDumpControllerStack();
					newPage.cancel();
					atv.loadAndSwapXML(atvutils.makeOkDialog("Success", "You are now authorized to watch MLB.TV.  Please select a game from Today's Games on the main menu."));
					console.debug("restoreAndPurchase() after showing Success page...");
					atv._debugDumpControllerStack();
				}
				else
				{
					// unauthorized, purchase
					console.debug("restoreAndPurchase() unauthorized, purchasing...");
					purchase(newPage);
					atv._debugDumpControllerStack();
				}
			});
		}, function()
		{
			// LINK error -- attempt to purchase now
			console.debug("restoreAndPurchase() link error - make the purchase ");
			purchase(newPage);
			atv._debugDumpControllerStack();
		});
	}, function(error)
	{
		newPage.cancel();
		if (error && error.code == -500)
		{
			atv.loadAndSwapXML(atvutils.makeOkDialog("MLB.TV Error", "Failed to determine your subscription."));
		}
		else
		{
			newPage.swapXML(document);
		}
		console.error("restoreAndPurchase() restore error - failed to restore " + document);
		atv._debugDumpControllerStack();
	});

	newPage.onCancel = function()
	{
		atv.SKDefaultPaymentQueue.removeTransactionObserver(restoreObserver);
	};

	// Restore completed transactions to get transactions purchased on other devices for this account.
	console.debug("restoreAndPurchase() Restoring completed transactions...");
	atv.SKDefaultPaymentQueue.restoreCompletedTransactions();
}

function purchaseAndPlay(newPage)
{
	atv._debugDumpControllerStack();

	var observer = serviceClient.addPurchaseObserver(function(purchasedTransaction)
	{
		// PURCHASE success
		console.debug("purchase(): success purchased transactions " + purchasedTransaction);
		atv._debugDumpControllerStack();

		// link()
		serviceClient.doLink([ purchasedTransaction ], function()
		{
			console.debug("purchase(): link success");
			atv._debugDumpControllerStack();

			serviceClient.loadMediaPage(function(mediaDoc)
			{
				console.debug("purchase() loadMediaPage");
				newPage.swapXML(mediaDoc);
				atv._debugDumpControllerStack();
			});

		}, function()
		{
			console.debug("purchase(): link error after purchase");
			newPage.swapXML(atvutils.makeOkDialog("MLB.TV Link Error", "Failed to link your MLB.TV account properly after purchase"));
			atv._debugDumpControllerStack();
		})

	}, function(transaction)
	{
		// PURCHASE error
		console.debug("purchase(): error " + (transaction) ? transaction.error : null);
		if (!transaction || transaction.error != 2)
		{
			newPage.swapXML(atvutils.makeOkDialog("MLB.TV Purchase Error", "The purchase failed.  Please try again at a later time."));
		}
		atv._debugDumpControllerStack();
	});

	newPage.onCancel = function()
	{
		atv.SKDefaultPaymentQueue.removeTransactionObserver(observer);
	};

	serviceClient.startPurchase();
}

function restorePurchasePlay(productId)
{
	// store product id into the cart
	atv.localStorage[PURCHASE_PRODUCT_ID_KEY] = productId;

	var newPage = new atv.ProxyDocument();
	newPage.show();

	atv._debugDumpControllerStack();

	var restoreObserver = serviceClient.addRestoreObserver(function(transactions)
	{
		// link()
		serviceClient.doLink(transactions, function()
		{
			// LINK success -- determine whether to purchase
			serviceClient.loadMediaPage(function(document)
			{
				atv._debugDumpControllerStack();
				var authorized = !serviceClient.isMediaResponseUnauthorized(document);
				console.debug("restorePurchasePlay() authorized " + authorized);
				atv.localStorage.setItem(AUTHORIZED_KEY, (authorized) ? "true" : "false");
				console.debug("restorePurchasePlay() atv.localStorage[AUTHORIZED] " + atv.localStorage[AUTHORIZED_KEY]);
				if (authorized)
				{
					// authorized or media playback error (session key problem, media unavailable, etc.), display the page
					console.debug("restorePurchasePlay() authorized or error page, showing media proxy page...");
					newPage.swapXML(atvutils.makeOkDialog("MLB.TV Subscriber", "You have purchased this content before and already have access to watch MLB.TV content.", "play();"));
					atv._debugDumpControllerStack();
					console.debug("restorePurchasePlay() after showing media proxy page...");
				}
				else
				{
					// unauthorized, purchase
					console.debug("restorePurchasePlay() unauthorized, purchasing...");
					purchaseAndPlay(newPage);
					atv._debugDumpControllerStack();
				}
			});
		}, function()
		{
			// LINK error -- attempt to purchase now
			console.debug("restorePurchasePlay() link error - make the purchase ");
			purchaseAndPlay(newPage);
		});
	}, function(error)
	{
		if (error && error.code == -500)
		{
			newPage.swapXML(atvutils.makeOkDialog("MLB.TV Error", "Failed to determine your subscription."));
		}
		else
		{
			newPage.swapXML(document);
		}
	});

	newPage.onCancel = function()
	{
		atv.SKDefaultPaymentQueue.removeTransactionObserver(restoreObserver);
	};

	// Restore completed transactions to get transactions purchased on other devices for this account.
	console.debug("restorePurchasePlay() Restoring completed transactions...");
	atv.SKDefaultPaymentQueue.restoreCompletedTransactions();
}

function play(newPage)
{
	serviceClient.loadMediaPage(function(document)
	{
		console.debug("play() show the media proxy page..." + newPage);
		atv._debugDumpControllerStack();
		if (newPage)
		{
			newPage.swapXML(document);
			atv.unloadPage();
		}
		else
		{
			atv.loadAndSwapXML(document);
		}
		atv._debugDumpControllerStack();
		console.debug("play() after showing media proxy page...");
	});
}

function restoreManually()
{
	console.debug("restore receipts from iTunes account manually");

	var newPage = new atv.ProxyDocument();
	newPage.show();

	var restoreObserver = serviceClient.addRestoreObserver(function(transactions)
	{
		// restore transaction callback
		console.log("restoreManually() transactions " + transactions);
		if (transactions)
		{
			console.debug("restoreManually() successfully restored receipts from iTunes account manually");
			serviceClient.doLink(transactions, function()
			{
				console.debug("restoreManually() link success callback - determine authorization from feature service");
				serviceClient.loadUserAuthorized(function()
				{
					newPage.loadXML(atvutils.makeOkDialog("Restore Successful", "Your subscription has been restored."));
				});
				console.debug("restoreManually() link service success");
			}, function()
			{
				console.debug("restoreManually() link service error");
			});
		}
		else
		{
			console.debug("restoreManually() missing transaction - determine authorization from feature service");
			newPage.loadXML(atvutils.makeOkDialog("Not an MLB.TV subscriber", "You have not purchased a MLB.TV subscription on AppleTV."));
		}
	}, function(error)
	{
		// restore error callback
		console.debug("restoreManually() error attempting to restore receipts from iTunes account manually");

		if (!error || error.code != -500)
		{
			newPage.cancel();
		}
		else
		{
			newPage.loadXML(atvutils.makeOkDialog("Restore Unsuccessful", "An error occurred attempting to restore your MLB.TV account."));
		}
	});

	newPage.onCancel = function()
	{
		atv.SKDefaultPaymentQueue.removeTransactionObserver(restoreObserver);
	};

	// Restore completed transactions to get transactions purchased on other devices for this account.
	console.debug("restoreManually() Restoring completed transactions...");
	atv.SKDefaultPaymentQueue.restoreCompletedTransactions();
}

function restoreOnOffer()
{
	console.debug("restoreOnOffer() restore receipts from iTunes account manually");

	var newPage = new atv.ProxyDocument();
	newPage.show();

	var restoreObserver = serviceClient.addRestoreObserver(function(transactions)
	{
		// restore success callback
		console.log("restoreManually() transaction " + transactions);
		if (transactions)
		{
			console.debug("successfully restored receipts from iTunes account manually");
			serviceClient.doLink(transactions, function()
			{
				// link success callback
				if (flowManager.inMediaFlow())
				{
					console.debug("restoreOnOffer() in media flow, playing media");
					play(newPage);
				}
				else
				{
					console.debug("restoreOnOffer() NOT in media flow, display info message");
					serviceClient.loadUserAuthorized(function()
					{
						console.debug("restoreOnOffer(): load user authorized");
						newPage.swapXML(atvutils.makeOkDialog("Restore Successful", "Your subscription has been restored."));
						atv.unloadPage();
					});
				}
			}, function()
			{
				// link error callback
				newPage.loadXML(atvutils.makeOkDialog("Restore Unsuccessful", "There was a problem restoring your iTunes account. Please try again at a later time."));
			});
		}
		else
		{
			newPage.loadXML(atvutils.makeOkDialog("Not an MLB.TV subscriber", "You have not purchased a MLB.TV subscription on AppleTV."));
		}
	}, function(error)
	{
		// restore error callback
		console.debug("error attempting to restore receipts from iTunes account manually");

		if (!error || error.code != -500)
		{
			newPage.cancel();
		}
		else
		{
			newPage.loadXML(atvutils.makeOkDialog("Restore Unsuccessful", "An error occurred attempting to restore your MLB.TV account."));
		}
	});

	newPage.onCancel = function()
	{
		atv.SKDefaultPaymentQueue.removeTransactionObserver(restoreObserver);
	};

	// Restore completed transactions to get transactions purchased on other devices for this account.
	console.debug("restoreManually() Restoring completed transactions...");
	atv.SKDefaultPaymentQueue.restoreCompletedTransactions();
}

function applySettingsPage(document)
{
	// update logo image resolution
	updateImageResolution(document);

	// show scores menu item
	var showScores = atv.localStorage[SHOW_SCORES_KEY];
	var menuItem = document.getElementById("showScoresMenuItem");
	if (menuItem)
	{
		var label = menuItem.getElementByName('rightLabel');
		label.textContent = (!showScores || showScores == "true") ? "Show" : "Hide";
		console.debug("label.textContent: " + label.textContent);
	}

	// menu items
	var parent = document.rootElement.getElementByTagName("items");
	var signInMenu = document.getElementById("signInOutMenuItem");
	var restoreMenu = document.getElementById("restoreSub");
	var linkAcctMenu = document.getElementById("linkAcct");
	var showScoresMenu = document.getElementById("showScoresMenuItem");
	console.debug("applySettingsPage() parent " + parent);

	// restore menu item
	var showRestoreMenu = atv.localStorage[IOS_IDENTITY_POINT_KEY] == null && atv.localStorage[IOS_FINGERPRINT_KEY] == null;
	console.debug("applySettingsPage() showRestoreMenu " + showRestoreMenu + " restoreMenu " + restoreMenu + " atv.localStorage[IOS_IDENTITY_POINT_KEY] " + atv.localStorage[IOS_IDENTITY_POINT_KEY]);
	if (showRestoreMenu)
	{
		if (!restoreMenu)
		{
			var parent = document.rootElement.getElementByTagName("items");
			console.debug("applySettingsPage() parent " + parent);
			if (parent && parent.childElements)
			{
				restoreMenu = atv.parseXML('<oneLineMenuItem id="restoreSub" accessibilityLabel="Restore Subscription" onSelect="restoreManually();"><label>Restore Subscription</label></oneLineMenuItem>').rootElement;
				console.debug("applySettingsPage() adding restoreMenu " + restoreMenu);
				restoreMenu.removeFromParent();
				parent.appendChild(restoreMenu);
				if (linkAcctMenu)
				{
					linkAcctMenu.removeFromParent();
					parent.appendChild(linkAcctMenu);
				}
				showScoresMenu.removeFromParent();
				parent.appendChild(showScoresMenu);
			}
		}
	}
	else if (restoreMenu)
	{
		restoreMenu.removeFromParent();
	}

	// link accounts menu item
	var showLinkMenu = atv.localStorage[EMAIL_LINKED_KEY] != "true" && atv.localStorage[IOS_IDENTITY_POINT_KEY] != null && atv.localStorage[IOS_FINGERPRINT_KEY] != null;
	console.debug("applySettingsPage() atv.localStorage[EMAIL_LINKED_KEY] " + atv.localStorage[EMAIL_LINKED_KEY] + " showLinkMenu " + showLinkMenu + " atv.localStorage[IOS_IDENTITY_POINT_KEY] " + atv.localStorage[IOS_IDENTITY_POINT_KEY] + " linkAcctMenu " + linkAcctMenu);
	if (showLinkMenu)
	{
		// add the menu if it's not there
		if (!linkAcctMenu)
		{
			if (parent && parent.childElements)
			{
				console.debug("applySettingsPage() before parent.childElements " + parent.childElements);

				linkAcctMenu = atv.parseXML('<oneLineMenuItem id="linkAcct" accessibilityLabel="Link MLB.TV Account" onSelect="linkManually();"><label>Link Account</label></oneLineMenuItem>').rootElement;
				linkAcctMenu.removeFromParent();

				showScoresMenu.removeFromParent();
				parent.appendChild(linkAcctMenu);
				parent.appendChild(showScoresMenu);

				console.debug("applySettingsPage() after parent.childElements " + parent.childElements);
			}
		}

		if (signInMenu)
		{
			signInMenu.removeFromParent();
		}
	}
	else
	{
		// remove the menu
		if (linkAcctMenu)
		{
			linkAcctMenu.removeFromParent();
		}

		if (!signInMenu)
		{
			signInMenu = atv.parseXML('\
				<signInSignOutMenuItem id=\"signInOutMenuItem\" accessibilityLabel=\"Sign out\" signOutExitsApp=\"false\">\
					<signInPageURL>' + config.appletvBase + '/views/Login</signInPageURL>\
					<confirmation>\
						<title>Are You Sure?</title>\
						<description>The account %@ will be logged out of the service.</description>\
					</confirmation>\
				</signInSignOutMenuItem>').rootElement;
			signInMenu.removeFromParent();

			parent.appendChild(signInMenu);
			if (showRestoreMenu && restoreMenu)
			{
				parent.appendChild(restoreMenu);
			}
			showScoresMenu.removeFromParent();
			parent.appendChild(showScoresMenu);
		}
	}

	return document;
}

function applyRecapsPage(responseXML)
{
	// update logo image resolution
	updateImageResolution(responseXML);

	return responseXML;
}

function applyTeamsPage(responseXML)
{
	try
	{
		// update logo image resolution
		updateImageResolution(responseXML);

		var favTeamIds = getFavoriteTeamIds();

		console.debug('sorting teams.. ' + responseXML.rootElement);
		var gridList = responseXML.rootElement.getElementByName("body").getElementByName("scroller").getElementByName("items").getElementsByName("grid");
		var gridIndex = 0;
		for (; gridIndex < gridList.length; gridIndex++)
		{
			// function to sort by title
			function sortByTitle(poster1, poster2)
			{
				var favTeamIds = (atv.localStorage[FAVORITE_TEAMS_KEY]) ? atv.localStorage[FAVORITE_TEAMS_KEY] : [];

				var team1Id = poster1.getAttribute("id");
				var is1Fav = favTeamIds.indexOf(team1Id) >= 0;
				var title1 = poster1.getElementByName("title").textContent;
				title1 = (is1Fav) ? title1.toUpperCase() : title1.toLowerCase();

				var team2Id = poster2.getAttribute("id");
				var is2Fav = favTeamIds.indexOf(team2Id) >= 0;
				var title2 = poster2.getElementByName("title").textContent;
				title2 = (is2Fav) ? title2.toUpperCase() : title2.toLowerCase();

				var result = (title1 > title2) ? 1 : -1;
				// console.debug('result: ' + title1 + ">" + title2 + " = " + result);

				return result;
			}

			// parent
			var parent = gridList[gridIndex].getElementByName("items");

			// sort the posters
			var orderedPosterList = parent.childElements.sort(sortByTitle);

			// re-append the child
			var index = 0;
			for (; index < orderedPosterList.length; index++)
			{
				var poster = orderedPosterList[index];
				var teamId = poster.getAttribute("id");
				poster.removeFromParent();
				parent.appendChild(poster);

				var isFav = favTeamIds.indexOf(teamId) >= 0;

				var imageElem = poster.getElementByName("image");
				if (isFav && imageElem.textContent.indexOf("-fav.png") < 0)
				{
					imageElem.textContent = imageElem.textContent.replace(".png", "-fav.png");
				}
				else if (!isFav && imageElem.textContent.indexOf("-fav.png") > 0)
				{
					imageElem.textContent = imageElem.textContent.replace("-fav.png", ".png");
				}
			}

		}

		console.debug('teams are sorted and favorite team logos in place');
		return responseXML;

	}
	catch (error)
	{
		console.debug("Unable to sort the teams: " + error);
	}
}

function applyGamesPage(responseXML)
{
	console.debug("applyeGamesPage() responseXML = " + responseXML);
	try
	{
		// determine if favorite team
		var listElem = responseXML.rootElement.getElementByName("body").getElementByName("listByNavigation");
		var pageId = listElem.getAttribute("id");
		var teamId = pageId.substring(pageId.lastIndexOf(".") + 1);
		var isFavorite = getFavoriteTeamIds().indexOf(teamId) > -1;
		console.debug("applyGamesPage() teamId: " + teamId + " isFavorite: " + isFavorite);

		var favButton = responseXML.getElementById("favoriteButton");
		var favoriteButtonLabel = favButton.getElementByName("label");
		console.debug("favoriteButtonLabel=" + favoriteButtonLabel);
		if (isFavorite)
		{
			var newLabel = "Remove from Favorites";
			favButton.setAttribute('accessibilityLabel', newLabel);
			favoriteButtonLabel.textContent = newLabel;
			console.debug("changing to Remove From Favorites favoriteButtonLabel.textContent=" + favoriteButtonLabel.textContent);
		}
		else
		{
			var newLabel = "Add to Favorites";
			favButton.setAttribute('accessibilityLabel', newLabel);
			favoriteButtonLabel.textContent = newLabel;
			console.debug("changing to Add to Favorites favoriteButtonLabel.textContent=" + favoriteButtonLabel.textContent);
		}

		return responseXML;
	}
	catch (error)
	{
		console.log("Unable to apply changes to the games page: " + error);
	}
}

/**
 * Should be called by the onRefresh event, I am also calling this from the handleNavigate function below to share code. Essentially this function: 1. Loads the current Navigation file 2. Pulls the
 * menu items out of the loaded file and dumps them into the current file 3. Checks to see if an onRefresh AJAX call is already being made and if so does nothing.
 * 
 * 
 * @params string url - url to be loaded
 */
function handleGamesPageRefresh(urlParam)
{
	var url = atv.localStorage[GAMES_PAGE_REFRESH_URL_KEY];
	if (url == null)
	{
		url = urlParam;
	}
	
	console.log("Handle refresh event called");

	console.log("We are about to check the value of Ajax.currentlyRefreshing: --> " + Ajax.currentlyRefreshing);
	// check the Ajax object and see if a refresh event is happening
	if (!Ajax.currentlyRefreshing)
	{

		// if a refresh event is not happening, make another one.
		console.log("About to make the AJAX call. --> ")

		var refresh = new Ajax({
			"url" : url,
			"refresh" : true,
			"success" : function()
			{
				var req = arguments[0];

				// update the content page
				var newDocument = atv.parseXML(req.responseText), // Instead of using the responseXML you should parse the responseText. I know it seems redundent but trust me.
				menu = document.rootElement.getElementByTagName('menu'), oldMenuSections = menu.getElementByTagName('sections'), newMenuSections = newDocument.rootElement.getElementByTagName('sections'), now = new Date();

				applyGamesPage(newDocument);

				newMenuSections.removeFromParent();
				menu.replaceChild(oldMenuSections, newMenuSections);

			}
		});

		console.debug("I have made the AJAX call. <--");
	}

	console.debug("I am on the other side of the if. <--");

}

/**
 * This function works very similar to the handleRefresh Essentially we are manually loading the data and updating the page in Javascript This means we have to handle a bit more of the magic, but we
 * also get to control what is updated and update more of the page, like headers, previews, etc.
 * 
 * In this example, I am: 1. Getting the new URL from the Navigation Item 2. Changing the onRefresh URL 3. Calling handleRefresh and passing the new url to let handleRefresh do the magic.
 */
function handleGamesPageNavigate(e)
{
	console.log("Handle navigate event called: " + e + ", e.navigationItemId = " + e.navigationItemId + " document = " + document);

	// get the url
	var url = document.getElementById(e.navigationItemId).getElementByTagName('url').textContent;
	atv.localStorage[GAMES_PAGE_REFRESH_URL_KEY] = url;

	handleGamesPageRefresh(url);

	e.onCancel = function()
	{
		// Cancel any outstanding requests related to the navigation event.
		console.log("Navigation got onCancel.");
	}
}

function getFavoriteTeamIds()
{
	var favTeamIds = atv.localStorage[FAVORITE_TEAMS_KEY];
	console.debug("favoriteTeamIds=" + favTeamIds);
	return (favTeamIds != null) ? favTeamIds : [];
}

function toggleFavoriteTeam(teamId)
{
	try
	{
		console.log("toggleFavoriteTeams " + teamId);
		var favoriteTeamIds = getFavoriteTeamIds();

		var isFavorite;
		if (favoriteTeamIds == null || favoriteTeamIds.length == 0)
		{
			// first favorite team
			favoriteTeamIds = [ teamId ];
			isFavorite = true;
		}
		else
		{
			var teamIdIndex = favoriteTeamIds.indexOf(teamId);
			if (teamIdIndex > -1)
			{
				// remove
				favoriteTeamIds.splice(teamIdIndex, 1);
				isFavorite = false;
			}
			else
			{
				// add
				favoriteTeamIds.push(teamId);
				isFavorite = true;
			}
		}

		atv.localStorage[FAVORITE_TEAMS_KEY] = favoriteTeamIds;

		var favoriteButton = document.getElementById("favoriteButton");
		if (favoriteButton)
		{
			var newLabel = (isFavorite) ? "Remove from Favorites" : "Add to Favorites";
			var label = favoriteButton.getElementByName("label");
			label.textContent = newLabel;
			console.debug("after label.textContent: " + label.textContent);
		}
	}
	catch (error)
	{
		console.error("Caught exception trying to toggle DOM element: " + error);
	}

	console.debug("toggleFavoriteTeam() end " + teamId);
}

function toggleScores(id)
{
	try
	{
		var menuItem = document.getElementById(id);
		if (menuItem)
		{
			var label = menuItem.getElementByName('rightLabel');
			if (label.textContent == "Show")
			{
				label.textContent = "Hide";
				atv.localStorage[SHOW_SCORES_KEY] = "false";
			}
			else
			{
				label.textContent = "Show";
				atv.localStorage[SHOW_SCORES_KEY] = "true";
			}

			console.debug("showScores: " + atv.localStorage[SHOW_SCORES_KEY]);
		}
	}
	catch (error)
	{
		console.error("Caught exception trying to toggle DOM element: " + error);
	}
}
console.log("~~~~~ MLB.TV main.js end");
