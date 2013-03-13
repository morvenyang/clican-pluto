var htmlparse = {
	HTMLParser : function(htmlContent) {
		var html = document.implementation.createDocument(
				"http://www.w3.org/1999/xhtml", "html", null), body = document
				.createElementNS("http://www.w3.org/1999/xhtml", "body");
		html.documentElement.appendChild(body);

		body.appendChild(Components.classes["@mozilla.org/feed-unescapehtml;1"]
				.getService(Components.interfaces.nsIScriptableUnescapeHTML)
				.parseFragment(htmlContent, false, null, body));
		return body;
	},
	
	each: function(array,callback){
		for(var i=0;i<array.length;i++){
			callback(i,array[i]);
		}
	},
	
	find: function(dom,name) {
		var result;
		var index = name.indexOf(".");
		if(index<0){
			result = dom.getElementsByTagName(name);
		}else{
			var elemName = name.substring(0,index);
			var className = name.substring(index+1);
			var temp = dom.getElementsByTagName(elemName);
			result = [];
			for(var i=0;i<temp.length;i++){
				var elem = temp[i];
				var cName = elem.getAttribute("class");
				if(className==cName){
					result.push(elem);
				}
			}
		}
	}
	
}