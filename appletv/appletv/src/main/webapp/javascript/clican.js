var clican = {
    makeRequest: function(url, callback) {
        if ( !url ) {
            throw "loadURL requires a url argument";
        }

        var xhr = new XMLHttpRequest();
        xhr.onreadystatechange = function() {
            try {
                if (xhr.readyState == 4 ) {
                    if ( xhr.status == 200) {
                        callback(xhr.responseText);
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
        xhr.open("GET", url, true);
        xhr.send();
        return xhr;
    },
    
    
    /**
     * @params options.name - string node name
     * @params options.text - string textContent
     * @params options.attrs - array of attribute to set {"name": string, "value": string, bool}
     * @params options.children = array of childNodes same values as options
     * @params doc - document to attach the node to
     * returns node
     */
     createNode: function(options, doc) {
        var doc = doc || document;
        options = options || {};

        if(options.name && options.name != '') {
            var newElement = doc.makeElementNamed(options.name);

            if(options.text) newElement.textContent = options.text;

            if(options.attrs) {
                options.attrs.forEach(function(e, i, a) {
                    newElement.setAttribute(e.name, e.value);
                }, this);
            }

            if(options.children) {
                options.children.forEach(function(e,i,a) {
                    newElement.appendChild( this.createNode( e, doc ) );
                }, this)
            }

            return newElement;
        }
     }
     
};