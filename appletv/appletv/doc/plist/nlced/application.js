// ***************************************************
// ATVUtils - a JavaScript helper library for Apple TV
ATVUtils = function() {};

ATVUtils.prototype.makeRequest = function(url, method, headers, body, callback) {
    if ( !url ) {
        throw "loadURL requires a url argument";
    }

	var method = method || "GET",
		headers = headers || {},
 		body = body || "";

    var xhr = new XMLHttpRequest();
    xhr.onreadystatechange = function() {
        try {
            if (xhr.readyState == 4 ) {
                if ( xhr.status == 200) {
                    callback(xhr.responseXML);
                } else {
                    console.log("makeRequest received HTTP status " + xhr.status + " for " + url);
                    callback(null);
                }
            }
        } catch (e) {
            console.error('makeRequest caught exception while processing request for ' + url + '. Aborting. Exception: ' + e);
            xhr.abort();
            callback(null);
        }
    }
    xhr.open(method, url, true);

    for(var key in headers) {
        xhr.setRequestHeader(key, headers[key]);
    }

    xhr.send();
    return xhr;
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
            <dialog id="com.sample.error-dialog"> \
                <title><![CDATA[' + message + ']]></title> \
                <description><![CDATA[' + description + ']]></description> \
            </dialog> \
        </body> \
    </atv>';

    return atv.parseXML(errorXML);
}

ATVUtils.prototype.siteUnavailableError = function() {
    // TODO: localize
    return this.makeErrorDocument("sample-xml is currently unavailable. Try again later.", "Go to sample-xml.com/appletv for more information.");
}

ATVUtils.prototype.loadError = function(message, description) {
    atv.loadXML(this.makeErrorDocument(message, description));
}

ATVUtils.prototype.loadAndSwapError = function(message, description) {
    atv.loadAndSwapXML(this.makeErrorDocument(message, description));
}

ATVUtils.prototype.loadURLInternal = function(url, method, headers, body, loader) {
    var me = this,
    	xhr,
    	proxy = new atv.ProxyDocument;
    
    proxy.show();
    
    proxy.onCancel = function() {
        if ( xhr ) {
            xhr.abort();
        }
    };
    
    xhr = me.makeRequest(url, method, headers, body, function(xml) {
        try {
            loader(proxy, xml);
        } catch(e) {
            console.error("Caught exception in for " + url + ". " + e);
            loader(me.siteUnavailableError());
        }
    });
}

ATVUtils.prototype.loadURL = function($h) { //url, method, headers, body, processXML) {
    var me = this;
	if( typeof($h) === "string" ) {
		var url = $h;
	} else {
		var url = $h.url,
			method = $h.method || null,
			headers = $h.headers || null,
			body = $h.body || null,
			processXML = $h.processXML || null;
	}
	
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

// loadAndSwapURL can only be called from page-level JavaScript of the page that wants to be swapped out.
ATVUtils.prototype.loadAndSwapURL = function($h) { //url, method, headers, body, processXML) {
    var me = this;
	if( typeof($h) === "string" ) {
		var url = $h;
	} else {
		var url = $h.url,
			method = $h.method || null,
			headers = $h.headers || null,
			body = $h.body || null,
			processXML = $h.processXML || null;
	}
	
    this.loadURLInternal(url, method, headers, body, function(proxy, xml) { 
	    if(typeof(processXML) == "function") processXML.call(this, xml);
        try {
            proxy.loadXML(xml, function(success) {
                if ( success ) {
                    atv.unloadPage();
                } else {
                    console.log("loadAndSwapURL failed to load " + url);
                    proxy.loadXML(me.siteUnavailableError(), function(success) {
                        if ( success ) {
                            atv.unloadPage();
                        }
                    });
                }
            });
        } catch (e) {
            console.error("loadAndSwapURL caught exception while loading " + url + ". " + e);
            proxy.loadXML(me.siteUnavailableError(), function(success) {
                if ( success ) {
                    atv.unloadPage();
                }
            });
        }
    });
}

/**
 * Used to manage setting and retrieving data from local storage
 * @params key - string
 * @params value - string, bool
 */
ATVUtils.prototype.data = function(key, value) {
	if(key && value) {
		try {
			atv.localStorage.setItem(key, value);
			return value;
		} catch(error) {
			console.error('Failed to store data element: '+ error);
		}
	
	} else if(key) {
		try {
			return atv.localStorage.getItem(key);
		} catch(error) {
			console.error('Failed to retrieve data element: '+ error);
		}
	}
}

/**
 * @params $h.name - string node name
 * @params $h.text - string textContent
 * @params $h.attrs - array of attribute to set {"name": string, "value": string, bool}
 * @params $h.children = array of childNodes same values as $h
 * @params doc - document to attach the node to
 * returns node
 */
ATVUtils.prototype.createNode = function($h, doc) {
	var doc = doc || document;
	$h = $h || {};
	
	if($h.name && $h.name != '') {
		var newElement = doc.makeElementNamed($h.name);
		
		if($h.text) newElement.textContent = $h.text;
		
		if($h.attrs) {
			$h.attrs.forEach(function(e) {
				newElement.setAttribute(e.name, e.value);
			}, this);
		}
		
		if($h.children) {
			$h.children.forEach(function(e) {
				newElement.appendChild(this.createNode(e, doc));
			}, this);
		}
		
		return newElement;
	}
}

var atvutils = new ATVUtils;

// Extend atv.ProxyDocument to load errors from a message and description.
atv.ProxyDocument.prototype.loadError = function(message, description) {
    var doc = atvutils.makeErrorDocument(message, description);
    this.loadXML(doc);
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

// End ATVUtils
// ***************************************************

// Application-level JavaScript. bag.plist links to this file.
console.log("application.js start");

// atv.onGenerateRequest
// Called when Apple TV is about to make a URL request. Use this method to make changes to the URL. For example, use it
// to decorate the URL with auth tokens or signatures.
atv.onGenerateRequest = function (request) {
	console.log('--- old url: ' + request.url);
	console.log('--- new url: ' + request.url);
}

// atv.onLogout
// Called when the user is logged out. Use this method to remove any per-user data. For example, you probably
// should call atv.sessionStorage.clear() and atv.localStorage.clear().
atv.onLogout = function () {
	
	console.log('Notified that our account has logged out, clearing sessionStorage.');
	
	try {
		atv.sessionStorage.clear();
        atv.localStorage.removeItem("username");
        atv.localStorage.removeItem("password");
	}
	catch (e) {
		console.log('Caught exception trying to clear sessionStorage. Exception: ' + e);
	}
}

// atv.onAuthenticate
// Called when the user needs to be authenticated. Some events that would call this are:
// - the user has explicitly tried to login via a sign-in-sign-out menu item
// - the server returned a 401 and silent authentication is occuring
// - non-silent authentication is occuring (because there are no credentials or silent auth failed)
//
// This method should not block. If it makes an XMLHttpRequest, it should do so asynchronously. When authentication is complete, you must notify
// Apple TV of success or failure by calling callback.success() or callback.failure(msg).
//
// Do not save the username or password in atv.localStorage or atv.sessionStorage; Apple TV will manage the user's credentials.
//
// username - The username to authenticate with
// password - The password to authenticate with
// callback - Called to indicate success or failure to Apple TV. Either callback.success() or callback.failure(msg) must be called
//               in all situations.
//
atv.onAuthenticate = function (username, password, callback) {
	
	try {
		console.log('---- asked to auth user: ' + username + ', pass: ' + password);
		var url = "https://gamecenter.nhl.com/nhlgc/secure/login?username=" + encodeURIComponent(username) + "&password=" + encodeURIComponent(password);
		console.log('Trying to authenticate with ' + url);

		var req = new XMLHttpRequest();
		
		req.onreadystatechange = function() {
			try {
				console.log('Got ready state change of ' + req.readyState);
				if (req.readyState == 4 ) {
					console.log('Got status code of ' + req.status);
					if ( req.status == 200) {
						console.debug(req.responseText);
						var xmlDoc = atv.parseXML(req.responseText);
						var root = xmlDoc.rootElement;
						var code = root.getElementByName("code").textContent;
						if ( code == "loginsuccess" ) {
							var data = root.getElementByName("data");
							if ( data ) {
								var gcl = data.getElementByName("gclSubscription");
								if ( gcl ) {
									console.debug('gcl is ' + gcl.textContent);
									atv.localStorage["username"] = username;
									atv.localStorage["password"] = password;
									callback.success();
								} 
                                else {
									console.debug("gcl doesn't appear in document");
									callback.failure("The account specified does not have a valid GameCenter LIVE subscription.");
								}
							}
						}
						else if (code == "failedsso") {
							console.log("Code was not login success " + code)
							callback.failure("The NHL account information provided is incorrect. Please try again.");
						}
						else {
							console.log("Code was not login success " + code);
							callback.failure("NHL account authentication has failed. Verify that the username and password entered are correct. If the problem persists, please contact NHL GameCenter customer support.");
						}
					}
					else {
						// Specify a copyedited string because this will be displayed to the user.
						callback.failure('Auth failed. Status ' + req.status + ': ' + req.statusText);
					}
				}
			}
			catch (e) {
				// Specify a copyedited string because this will be displayed to the user.
				callback.failure('Caught exception while processing request. Aborting. Exception: ' + e);
				req.abort();
			}
		}
	
		req.open("GET", url, false);
		req.send();
	}
	catch (e) {
		// Specify a copyedited string because this will be displayed to the user.
		callback.failure('Caught exception setting up request. Exception: ' + e);
	}
}

/**
 * Retrieves list of NHL products that this user can purchase based on their location
 */ 
function getProducts(){
    
    var inclusionSet = [];
    //var url = "http://gamecenter-qa.nhl.com/nhlgc/servlets/products?format=xml";
    var url = "http://gamecenter.nhl.com/nhlgc/servlets/products?format=xml";
    var req = new XMLHttpRequest();
    req.onreadystatechange = function() {
        try {
            console.log('Got ready state change of ' + req.readyState);
            if (req.readyState == 4 ) {
                console.log('Got status code of ' + req.status);
                if ( req.status == 200) {
                    console.debug(req.responseText);
                    var xmlDoc = atv.parseXML(req.responseText);
                    var root = xmlDoc.rootElement;
                    products = root.getElementsByName("products");
                    productList = products[0].getElementsByName("product");
                    nProducts = productList.length;
                    console.debug("Number of products: " + nProducts);
                    for (i = 0; i < nProducts; i++) {
                        product = productList[i];
                        productType = product.getElementsByName("productType")[0].textContent;
                        sku = product.getElementsByName("sku")[0].textContent;
                        tier = product.getElementsByName("tier")[0].textContent;
                        //value = 'com.neulion.atv.nba.2012.' + tier + '.' + productType + '_' + sku;
                        value = 'com.nbaimd.gametime.universal.2012.' + tier + '.' + productType + '_' + sku;
                        //NEED TO IGNORE MONTHLY INSTALLMENT PRODUCTS where <isMonthly>true</isMonthly>
                        //WHAT WILL TEAM PASS PRODUCT LOOK LIKE

                        //AS PER CONTRACT, Only the following products will be available for IAP
                        //League Pass Tier 1
                        //League Pass Tier 6
                        if ((productType == 'LPPREMIUM' && sku == 'SEASON_PLAYOFFS') && tier == 'tier1' || tier =='tier6') {
                            console.log('FOUND PRODUCT: ' + value);
                            inclusionSet.push(value);
                        }
                    }
                    console.debug("GetInclusionSet RETURN: " + inclusionSet);
                    atv.sessionStorage.setItem('ProductList', inclusionSet);
                    return;
                }
                else {
                    // Specify a copyedited string because this will be displayed to the user.
                    console.log('Failed products inclusion request');
                    return;
                }
            }
        }
        catch (e) {
            console.log('Failed products inclusion request. Aborting. Exception: ' + e);
            req.abort();
            return;
        }
    }

    req.open("GET", url, false);
    req.setRequestHeader('Geo-Coord','52.5233,13.4127');
    req.send();
}

function checkAuth(params) {
    console.debug("PARAMS: " + params);
	var url = params['url'];
    atv.sessionStorage.setItem( 'playlist', url );
    atv.sessionStorage.setItem( 'StoreManagerLandingPage', url );

    console.debug("URL: " + url);
    var live = params['live'];
    console.debug("LIVE: " + live);
	var proxy = new atv.ProxyDocument();
    atv.unloadPage();
	proxy.onCancel = function() {
		console.debug("The user menued out while loading checkAuth. If I was good, I'd cancel outstanding requests related to the load.");
	};

    //if (live == "true") {
        console.debug("Performing blackout verification");
        var gameId = params['id'];
        var gameType = params['type'];
        var season = params['season'];
        var geo_url = "http://gamecenter.nhl.com/nhlgeo/blackout?season=" + season + "&gameType=" + gameType + "&gameId=" + gameId;
        console.debug("GEO: " + geo_url);

        var xmlHttp = null;
        xmlHttp = new XMLHttpRequest();
        xmlHttp.open("GET", geo_url, false);
        xmlHttp.send(null);
        var rc = xmlHttp.responseText;
        console.debug("Blackout verification returns: " + rc);

        if(rc == "1") {
            proxy.show();
		    proxy.loadURL('http://nlced.cdnak.neulion.com/nhl/player/atv/nhlgc12/blackout-dialog.xml');
		    return;
        }
    //}

    //Highlights should not require authorization at this time
    /*if (url.indexOf("recap") != -1) {
        //proxy.show();
        //proxy.loadURL(url);
        atvutils.playVideo(url,'Game Highlights','Full Game Recap','none');
        return;
    }*/

	console.debug("Validating user authorization");

    if (atv.localStorage["storeReceipt"]) {
        proxy.show();
        proxy.loadURL(url);
        return;
    }
	username = atv.localStorage["username"];
	password = atv.localStorage["password"];
	if (username == null || password == null) {
        //if valid receipt exists, but user has not registered with NHL, continue to play content
	    /*if (atv.localStorage["storeReceipt"]) {
            var playbackXML = '<?xml version="1.0" encoding="UTF-8"?> \
            <atv> \
              <body> \
                <videoPlayer id="com.neulion.nhl.video-player"> \
                  <httpLiveStreamingVideoAsset id="nhl.stream" indefiniteDuration="true"> \
                    <mediaURL><![CDATA[' + url + ']]></mediaURL> \
                    <title><![CDATA[Premium Content]]></title> \
                    <description><![CDATA[Premium Content]]></description> \
                    <image><![CDATA[none]]></image> \
                  </httpLiveStreamingVideoAsset> \
                </videoPlayer> \
              </body> \
            </atv>';

            atv.loadXML(atv.parseXML(playbackXML));
            return;
        }*/
        if (atv.localStorage["storeReceipt"]) {
            proxy.show();
            proxy.loadURL(url);
            return;
        }
        else {
            atv.sessionStorage.setItem( 'StoreManagerState', 'LoadOfferPage' );
            atv.sessionStorage.setItem( 'StoreManagerFlow', 'BLOCKMEDIA' );
            atvutils.loadURL( 'http://nlced.cdnak.neulion.com/nhl/player/atv/nhlgc12/InAppPurchase/StoreManager.xml' );
            return;
        }
	}
	
	proxy.show();
    var auth_url = "https://gamecenter.nhl.com/nhlgc/secure/login?username=" + encodeURIComponent(username) + "&password=" + encodeURIComponent(password);
	console.log('Trying to authenticate with ' + auth_url);
	
	var req = new XMLHttpRequest();
	req.onreadystatechange = function() {
	    try {
		    console.log('Got ready state change of ' + req.readyState);
		    if (req.readyState == 4 ) {
			    console.log('Got status code of ' + req.status);
			    if ( req.status == 200) {
				    console.log(req.responseText);
				    var xmlDoc = atv.parseXML(req.responseText);
				    var root = xmlDoc.rootElement;
				    var code = root.getElementByName("code").textContent;
				    if ( code == "loginsuccess" ) {
					    var data = root.getElementByName("data");
					    if ( data ) {
						    var gcl = data.getElementByName("gclSubscription");
						    /*if ( gcl ) {
							    console.log("gcl is " + gcl.textContent);
							    console.log('About to load: ' + url);
							    var playbackXML = '<?xml version="1.0" encoding="UTF-8"?> \
                                <atv> \
                                  <body> \
                                    <videoPlayer id="com.neulion.nhl.video-player"> \
                                      <httpLiveStreamingVideoAsset id="nhl.stream" indefiniteDuration="true"> \
                                        <mediaURL><![CDATA[' + url + ']]></mediaURL> \
                                        <title><![CDATA[Premium Content]]></title> \
                                        <description><![CDATA[Premium Content]]></description> \
                                        <image><![CDATA[none]]></image> \
                                      </httpLiveStreamingVideoAsset> \
                                    </videoPlayer> \
                                  </body> \
                                </atv>';

                                proxy.loadXML(atv.parseXML(playbackXML));
						    }*/ 
                            if ( gcl ) {
                                console.log("gcl is " + gcl.textContent);
                                console.log('About to load: ' + url);
                                proxy.loadURL(url);
                            } 
                            else {
							    /*console.log("gcl doesn't appear in document");
                                atv.sessionStorage.setItem( 'StoreManagerState', 'LoadOfferPage' );
                                atv.sessionStorage.setItem( 'StoreManagerFlow', 'DEFAULT' );
                                proxy.loadURL( 'http://nlced.cdnak.neulion.com/nhl/player/atv/nhlgc12/InAppPurchase/StoreManager.xml' );*/
                                console.debug("gcl doesn't appear in document");
                                proxy.loadURL( 'http://nlced.cdnak.neulion.com/nhl/player/atv/nhlgc12/InAppPurchase/StoreManager.xml' );
						    }
					    }
				    }
				    else {
					    console.log('Code was not login success ' + code);
                        atv.sessionStorage.setItem( 'StoreManagerState', 'LoadOfferPage' );
                        atv.sessionStorage.setItem( 'StoreManagerFlow', 'BLOCKMEDIA' );
                        proxy.loadURL( 'http://nlced.cdnak.neulion.com/nhl/player/atv/nhlgc12/InAppPurchase/StoreManager.xml' );
				    }
			    }
			    else {
				    // Specify a copyedited string because this will be displayed to the user.
                    atv.sessionStorage.setItem( 'StoreManagerState', 'LoadOfferPage' );
                    atv.sessionStorage.setItem( 'StoreManagerFlow', 'BLOCKMEDIA' );
                    proxy.loadURL( 'http://nlced.cdnak.neulion.com/nhl/player/atv/nhlgc12/InAppPurchase/StoreManager.xml' );
			    }
		    }
	    }
	    catch (e) {
		    // Specify a copyedited string because this will be displayed to the user.
		    //callback.failure('Caught exception while processing request. Aborting. Exception: ' + e);
		    req.abort();
	    }
	
	}
	req.open("GET", auth_url, true);
	req.send();
}

// atv.onAppEntry
// Called when you enter an application but before the root plist is requested. This method should not return until
// application initialization is complete. In other words, once this method has returned Apple TV will assume it can call
// into any other callback. For instance, after this returns, the next method likely to be called is atv.onGenerateRequest
// on the root plist.
atv.onAppEntry = function() {
	console.log('App entered');

    //getProducts();

    atv.sessionStorage["InvokeSubscribe"] = "main";

	hide_scores = atv.localStorage["HideScores"];
	if (hide_scores == null)
		atv.localStorage["HideScores"] = false;
	console.debug('HideScores preference: ' + hide_scores);
    
	device_id = atv.localStorage["DeviceId"];
	if (device_id == null) {
		device_id = atv.uuid();
		atv.localStorage["DeviceId"] = device_id;
	}
	console.debug('Device ID: ' + device_id);

	receipt = atv.localStorage["storeReceipt"];
	if (receipt == null)
		console.debug('No stored receipts found');
    else
	    console.debug('Stored receipt: ' + receipt);
    //need to check for expiration of subscription; similar to iOS implementation, this
    //can be done by checking a config file and doing a date comparison. The config file 
    //will contain the product id and expiration date

    sid = atv.localStorage["username"];
    if (sid == null)
        sid = "0";
	track_app_start = "http://track1.neulion.com/nhlgc_ced/track.gif?uid=110&sid=" + sid + "&event=as";
	console.debug('Track application start: ' + track_app_start);
	var xmlHttp = null;
    xmlHttp = new XMLHttpRequest();
    xmlHttp.open("GET", track_app_start, false);
    xmlHttp.send(null);

    atvutils.loadURL({
		"url":"http://nlced.cdnak.neulion.com/nhl/player/atv/nhlgc12/main.xml",
		"processXML": function(xml) {
            
            var itmsUrl = atv.localStorage["itms-link"];
	        if ( itmsUrl ) {
		        placeholder = xml.getElementById('itmsLink');
	            if( placeholder ) {
                    console.log("Creating itms link");
		            placeholder.setAttribute('onSelect', "atv.loadURL('"+ itmsUrl +"')");
	            }
	        } 
            else {
		        try {
	                var menuItem = xml.getElementById('itms-link');
	                console.log("removing menu item: " + menuItem);
	                if(menuItem) menuItem.removeFromParent();
	            }
	            catch(error) {
	                console.log("Caught exception trying to toggle DOM element: " + error);
	            }
	        }

            if (atv.localStorage["storeReceipt"] || sid != 0) {
				try {
	                var menuItem = xml.getElementById("subscribe");
	                console.log("removing menu item: " + menuItem);
	                if(menuItem) menuItem.removeFromParent();
	            }
	            catch(error) {
	                console.log("Caught exception trying to toggle DOM element: " + error);
	            }
            }
            var bannerImage = xml.getElementById("banner");
            frame = atv.device.screenFrame;
            console.debug("Frame height: " + frame.height);
            if (frame.height >= 1080) {
                bannerImage.textContent = "http://nlced.cdnak.neulion.com/nhl/player/atv/nhlgc12/images/nhl-authenticate-logo_1080.png"
            }
		}
	});
}

// atv.onAppExit
// Called when the application exits. The application doesn't exit when the user goes to the main menu because the application
// is still required to display it's top shelf. Rather, the application exits when an application is entered, even
// if this application is the one that is entered. For example:
// 1. User enters this application: atv.onAppEntry called
// 2. User goes back to the main menu (nothing happens yet)
// 3. User enters this application: atv.onAppExit called, then atv.onAppEntry is called
atv.onAppExit = function() {
	console.log('App exited');
}

// atv.onPageLoad
// Called when a plist is loaded.
// id - The page identifier
atv.onPageLoad = function(id) {
	console.log('Page ' + id + ' loaded');
	
	if ( id == "com.sample.javascript-logout" ) {
		console.log("JavaScript logout page loaded. Perform logout.");
		
		// This is not always needed. If you want to perform logout only when the user explicitly asks for it, you
		// can use the sign-in-sign-out menu item.
		atv.logout();
	}
}

// atv.onPageUnload
// Called when a plist is unloaded.
// id - The page identifier
atv.onPageUnload = function(id) {
	console.log('Page ' + id + ' unloaded');
}


//
// Player callbacks
//

function logPlayerAssetAndEventGroups() {

	console.log('Logging Player Asset ---------------------------------');

	// Test out asset and event groups on player
	var asset = atv.player.asset;
	
	if ( asset != null ) {
		console.log('The current asset is ' + asset);
		var eventGroups = atv.player.eventGroups;
		if ( eventGroups != null ) {
			console.log('The current event groups are ' + eventGroups + ', len is ' + eventGroups.length);
			for( var i=0, len = eventGroups.length; i < len; ++i ) {
				var group = eventGroups[i];
				console.log('group is a ' + typeof group);
				for( var key in group ) {
					console.log('group[' + key + '] = ' + group[key]);
				}
		
				var events = group.events;
				for( var j=0, eventLen=events.length; j < eventLen; ++j ) {
					var event = events[j];
					for( var key in event ) {
						console.log( '  event[' + key + '] = ' + event[key]);
					}
				}
			}
		}
	}
	
	console.log('END ---------------------------------');
}

// atv.player.willStartPlaying
// Called at some point before playback starts. Use this to do per-playback setup or reporting.
var logTimer = null;
var playlistRequest = null;
var playerOverlay = null;

atv.player.willStartPlaying = function() {
	console.log('willStartPlaying');
	
	var mediaUrl = atv.player.asset["media-asset"]["media-url"];
    var live = "1";
    if(mediaUrl.indexOf("whole") > 0 || mediaUrl.indexOf("continuous") > 0)
        live = "0"; 
    var path = mediaUrl.split('//');
    var partial = path[1].substring(path[1].indexOf("/"));
    var assetId = atv.uuid();
    atv.sessionStorage["media-asset"] = partial;
    atv.sessionStorage["media-asset-live"] = live;
    atv.sessionStorage["media-asset-id"] = assetId;
    var dgeth_url = "http://gamecenter.nhl.com/nhlgeo/dgeth?cdn_type=3&url=" + partial + "&token=" + atv.localStorage["DeviceId"];
    var xmlHttp = null;
    xmlHttp = new XMLHttpRequest();
    xmlHttp.open("GET", dgeth_url, false);
    xmlHttp.send(null);
    var token = xmlHttp.responseText;
    atv.player.asset["media-asset"]["media-url"] = mediaUrl + "?" + token;
    
    logPlayerAssetAndEventGroups();
   	
	console.log('starting timer ======================');
	
	logTimer = atv.setInterval(function() {
		logPlayerAssetAndEventGroups();
	}, 5000);
	
	atv.sessionStorage["already-watched-ad"] = false;
	atv.sessionStorage["in-ad"] = false;

	//
	// Create an overlay with a network bug
	//
	//playerOverlay = makePlayerOverlay();
	//atv.player.overlay = playerOverlay;

	//
	// Use loadMoreAssets callback for playlists
	// 
	var playlist = atv.player.asset['com.sample.playlist'];
	if (playlist != null) {
		var currentPlaylistPart = 1;
		
		// This function is called whenever more assets are required by the player. The implementation
		// should call callback.success or callback.failure at least once as a result. This function
		// will not be called again until the invocation from the last call invokes callback.success
		// or callback.failure. Also, this function will not be called again for a playback if callback.success 
		// is called with null argument, or callback.failure is called.
		// Calling any of the callback functions more than once during the function execution has no effect.
		atv.player.loadMoreAssets = function(callback) {
		
			console.log('load more assets called---------------');
			
			// Request the next item in the playlist.
			playlistRequest = new XMLHttpRequest();
			playlistRequest.onreadystatechange = function() {
				try {
					if (playlistRequest.readyState == 4 ) {
						if ( playlistRequest.status == 200) {
						
							json = JSON.parse(playlistRequest.responseText);
							console.log('Playlist response is ' + json);
							
							// Pass the loaded assets in callback.success. 
							callback.success(json["videos"]);
						}
						else if (playlistRequest.status == 404) {
							// This example implementation counts on a 404 to signal the end of the playlist.
							// null will stop any further calls to loadMoreAssets for this playback.
							callback.success(null);
						}
						else {
							console.error('HTTP request failed. Status ' + playlistRequest.status + ': ' + playlistRequest.statusText);

							// Signal the failure
							callback.failure('HTTP request failed. Status ' + playlistRequest.status + ': ' + playlistRequest.statusText);
						}
					}
				}
				catch (e) {
					console.error('Caught exception while processing request. Aborting. Exception: ' + e);
					playlistRequest.abort();

					// Signal the failure
					callback.failure('Error communicating to the server');
				}
			}

			playlistRequest.open("GET", playlist['base-url'] + currentPlaylistPart + ".json");
			currentPlaylistPart++;
			playlistRequest.send();
		};
	} 
	else {
		// Don't use dynamic playlists
		atv.player.loadMoreAssets = null
	}
}

atv.player.currentAssetChanged = function() {
	//
	// Create an overlay with a network bug
	//
	playerOverlay = makePlayerOverlay();
	atv.player.overlay = playerOverlay;
}

// atv.player.didStopPlaying
// Called at some point after playback stops. Use this to to per-playback teardown or reporting.
atv.player.didStopPlaying = function() {
	console.log('didStopPlaying');

    console.log(atv.player.asset);

	atv.clearInterval(logTimer);
	logTimer = null;
	// Cancel request
	if (playlistRequest != null) {
		playlistRequest.abort();
		playlistRequest = null;
	}
	atv.player.loadMoreAssets = null;
}

function makePlayerOverlay () {
	
	/*
	* simple network bug
	var screenFrame = atv.device.screenFrame;
	
	// Create an image view for a network bug
	var networkBug = new atv.ImageView();
	var bugWidth = 150;
	var bugHeight = 65;
	var bugOffsetFromEdges = screenFrame.width * 0.03;
	networkBug.loadImageAtURL("http://192.168.1.103/sample/images/bug.png");
	networkBug.frame = {
		x: screenFrame.width - bugWidth - bugOffsetFromEdges,
		y: bugOffsetFromEdges,
		width: bugWidth,
		height: bugHeight
	};
	return networkBug;
	*/
	
	var screenFrame = atv.device.screenFrame;

	// Create the grey background color for the overlay
	var overlayHeight = screenFrame.height * 0.07;
	var overlay = new atv.View();
	overlay.frame = { x: screenFrame.x, y: screenFrame.y + screenFrame.height - overlayHeight, width: screenFrame.width, height: overlayHeight };
	overlay.backgroundColor = { red: 0.188, green: 0.188, blue: 0.188, alpha: 1 };

	// Add a message on top of the overlay
	var messageAttributes = {
		pointSize: 22.0,
		color: { red: 1, blue: 1, green: 1 }
	};

	var message = new atv.TextView();
	var topPadding = overlay.frame.height * 0.35;
	var horizontalPadding = overlay.frame.width * 0.05;
	message.frame = {
		x: horizontalPadding,
		y: 0,
		width: overlay.frame.width - (2 * horizontalPadding),
		height: overlay.frame.height - topPadding
	};

	var numberOfSeconds = 0;

	function updateMessage() {
		message.attributedString = {
			string: "Overlay message: This has been displayed for " + numberOfSeconds + (numberOfSeconds == 1 ? " second" : " seconds"),
			attributes: messageAttributes
		};
		numberOfSeconds += 1;
		console.log("Updated attributed string to: " + "Overlay message: This has been displayed for " + numberOfSeconds + (numberOfSeconds == 1 ? " second" : " seconds"));
	}
	
	updateMessage();

	overlay.subviews = [ message ];

	// Create an image view for a network bug
	var networkBug = new atv.ImageView();
	var bugWidth = 150;
	var bugHeight = 65;
	var bugOffsetFromEdges = screenFrame.width * 0.03;
	networkBug.loadImageAtURL("http://192.168.1.103/sample/images/bug.png");
	networkBug.frame = {
		x: screenFrame.width - bugWidth - bugOffsetFromEdges,
		y: bugOffsetFromEdges,
		width: bugWidth,
		height: bugHeight
	};

	// Create a root view that has the fake video content with the overlay on top of it.
	var rootView = new atv.View();
	rootView.subviews = [ overlay, networkBug ];
	
	// Update the overlay message
	var handle = atv.setInterval(function() {
		updateMessage();
	}, 1000);
	
	atv.setTimeout(function (handleToKill) {
		atv.clearInterval(handle);
		rootView.subviews = [ networkBug ]
	}, 5500, handle);
	
	return rootView;
}

var gDateBufferingStarted = null;

// atv.player.onEventGroupsUpdated
// Called when event groups are updated. This happens once after media resolution. It can also happen on an interval
// by setting refresh-interval-sec in the event group (this is typcially used for live video when the event groups are not known up front).
//atv.player.onEventGroupsUpdated = function(eventGroups) {
//	console.log('onEventGroupsUpdated')
//}

// atv.player.onStartBuffering
// Called when the playhead has moved to a new location (including the initial load) and buffering starts.
// playheadLocation - The location of the playhead in seconds from the beginning
atv.player.onStartBuffering = function(playheadLocation) {
	gDateBufferingStarted = new Date(); 
	console.log('onStartBuffering at location ' + playheadLocation + ' at this time: ' + gDateBufferingStarted);
	logPlayerAssetAndEventGroups();
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
// Called when there is a buffer underrun during normal speed video playback (i.e. not fast-forward or rewind).
atv.player.onStallDuringPlayback = function(playheadLocation) {
	var now = new Date();
	console.log("onStallDuringPlayback: stall occurred at location " + playheadLocation + " at this time: " + now);
}

// atv.player.onPlaybackError
// Called when an error occurred that terminated playback.
// debugMessage - A debug message for development and reporting purposes only. Not for display to the user.
atv.player.onPlaybackError = function (debugMessage) {
	// debugMessage is only intended for debugging purposes. Don't rely on specific values.
	console.log('onPlaybackError: error message is ' + debugMessage);
}

atv.player.onQualityOfServiceReport = function(report) {
	console.log("QoS report is\n" + report);
	
	// accessLog and errorLog are not gaurenteed to be present, so check for them before using.
	
	if ( 'accessLog' in report ) {
		console.log("Acces Log:\n" + report.accessLog + "\----------------------------\n");
	}
	
	if ( 'errorLog' in report ) {
		console.log("Error Log:\n" + report.errorLog + "\----------------------------\n");
	}
}

// TODO - only show event callbacks example for media asset with a known asset-id, for now, control for all player items via this flag
SHOW_EVENT_EXAMPLE = false;

// atv.player.playerWillSeekToTime
// Called after the user stops fast forwarding, rewinding, or skipping in the stream
// timeIntervalSec - The elapsed time, in seconds, where the user stopped seeking in the stream
// Returns: the adjusted time offset for the player. If no adjustment is needed, return timeIntervalSec. 
// Clients can check whether the playback is within an unskippable event and reset the playhead to the start of that event.
atv.player.playerWillSeekToTime = function(timeIntervalSec) {
	
	console.log('playerWillSeekToTime: ' + timeIntervalSec);
	
	if (!SHOW_EVENT_EXAMPLE) {
		return timeIntervalSec;
	}
	
	// TODO - replace example using event group config
	// Example of event from offset 10-15 sec that is unskippable. If the user seeks within or past, reset to beginning of event
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
// Called to check if the given event should be allowed given the current player time and state.
// event - One of: atv.player.events.FFwd, atv.player.events.Pause, atv.player.events.Play, atv.player.events.Rew, atv.player.events.SkipBack, atv.player.events.SkipFwd
// timeIntervalSec - The elapsed time, in seconds, where the event would be fired
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

atv.player.playerStateChanged = function(newState, timeIntervalSec) {	
	/*
	state constants are:
	atv.player.states.FastForwarding
	atv.player.states.Loading
	atv.player.states.Paused
	atv.player.states.Playing
	atv.player.states.Rewinding
	atv.player.states.Stopped
	*/

    sid = atv.localStorage["username"];
    if (sid == null)
        sid = "0";
    console.debug("SID: " + sid);
    mediaUrl = atv.sessionStorage["media-asset"];
    if (mediaUrl == null)
        mediaUrl = "0";
    console.debug("Media URL: " + mediaUrl);
    live = atv.sessionStorage["media-asset-live"];
    if (live == null)
        live = "1";
    console.debug("Live: " + live);
    aid = atv.sessionStorage["media-asset-id"];
    console.debug("Asset ID: " + aid);
    
    if (newState == "Loading") {
    
        track_video_event = "http://track1.neulion.com/nhlgc_ced/track.gif?uid=110&sid=" + sid + "&event=vs&live=" + live + "&name=0&id=" + aid + "&path=" + mediaUrl;
        console.debug("Track Video Start: " + track_video_event);
        var xmlHttp = null;
        xmlHttp = new XMLHttpRequest();
        xmlHttp.open("GET", track_video_event, false);
        xmlHttp.send(null);
    }
    if (newState == "Stopped") {
    
        track_video_event = "http://track1.neulion.com/nhlgc_ced/track.gif?uid=110&sid=" + sid + "&event=ve&live=" + live + "&name=0&id=" + aid + "&path=" + mediaUrl;
        console.debug("Track Video Stop: " + track_video_event);
        var xmlHttp = null;
        xmlHttp = new XMLHttpRequest();
        xmlHttp.open("GET", track_video_event, false);
        xmlHttp.send(null);
    }
	
	console.log("Player state changed to " + newState + " at this time " + timeIntervalSec);
}

// atv.player.playerTimeDidChange
// Called whenever the playhead time changes for the currently playing asset.
// timeIntervalSec - The elapsed time, in seconds, of the current playhead position
atv.player.playerTimeDidChange = function(timeIntervalSec) {
	
	var netTime = atv.player.convertGrossToNetTime(timeIntervalSec);
	var andBackToGross = atv.player.convertNetToGrossTime(netTime);
	//console.log('playerTimeDidChange: ' + timeIntervalSec + " net time " + netTime + " and back to gross " + andBackToGross);
	
	if (!SHOW_EVENT_EXAMPLE) {
		return;
	}
	
	// TODO - replace example using event group config
	// If we are currently in the sample event, and are about to exit, clear our flag, mark that the event was watched, and resume if needed
	if (atv.sessionStorage["in-event"] && timeIntervalSec > 15) {
		atv.sessionStorage["in-event"] = false;
		atv.sessionStorage["already-watched-event"] = true;
		if (atv.sessionStorage["resume-time"]) {
			atv.player.playerSeekToTime(atv.sessionStorage["resume-time"]);
			atv.sessionStorage.removeItem("resume-time");
		}
	}
}

// Example of a JS callback used in a js-invoke action. The params argument contains all keys/values in the action params dictionary.
// In this example, we'll simply look for a parameter named "url", and load it using the atv.loadURL function
function myLoadURL(params) {
	console.log('myLoadURL(params)');
	for( var key in params ) {
		console.log('--- params[' + key + '] = ' + params[key]);
	}
	var url = params['url'];
	if (url) {
		atv.loadURL(url);
	}
}

function myLoadAndSwapURL(params) {
	console.log('myLoadAndSwapURL(params)');
	for( var key in params ) {
		console.log('--- params[' + key + '] = ' + params[key]);
	}
	var url = params['url'];
	if (url) {
		atv.loadAndSwapURL(url);
	}
}

function myLoadPlist(params) {
	console.log("myLoadPlist called");
	atv.loadPlist(params['plist']);
}

function myLoadAndSwapPlist(params) {
	console.log("myLoadAndSwapPlist called");
	var plistString =
		"<dict>" + 
    		"<key>merchant</key>" +
    		"<string>sample</string>" +
	   		"<key>identifier</key>" +
    		"<string>com.sample.myLoadAndSwapPlist</string>" +
	    	"<key>dialog</key>" +
	    	"<dict>" +
	    		"<key>message</key>" +
	    		"<string>Loaded plist using atv.loadAndSwapPlist</string>" +
	    		"<key>explanation</key>" +
	    		"<string>This is an example of a plist built from a JavaScript string</string>" +
	    	"</dict>" +
	    "</dict>";
	
	atv.loadAndSwapPlist(plistString);
}

function subscribeFromTumbler(xml) {

    console.debug("LOAD FROM TUMBLER");
    atv.sessionStorage["InvokeSubscribe"] = "tumbler";

}

atv.config = {
    doesJavaScriptLoadRoot: true
};


console.log("application.js end");