var ob1 = document.getElementsByTagName('div');
for (var i = 0; i < ob1.length; i++) {
    if (ob1[i].id.indexOf('outbrain') >= 0 || ob1[i].className.match(/outbrain/i) != null) {
        var i = document.createElement('img');
        i.setAttribute('src', 'http://Impressions-Proxy-1085035873.us-east-1.elb.amazonaws.com/impression.do/?event=ob&user_id=' + escape(pbuid) + '&implementation_id=' + escape(pbimp) + '&source=' + escape(pbs) + '&referrer=' + escape(pbref));
        i.style.display = 'none';
        document.body.appendChild(i);

        var eg = document.createElement('script');
        // Do not escape content string. IE max url limit of 2083 chars.
        eg.src = 'http://inline.playbryte.com/eg.js?' + 'user_id=' + escape(pbuid) + '&implementation_id=' + escape(pbimp) + '&source=' + escape(pbs) + '&uc=' + escape(pbuc) + '&content=' + pbcontent.replace(/(['"])/g,"\\$1");
        eg.type = "text/javascript";
        eg.id = "eg";
        document.getElementsByTagName("head")[0].appendChild(eg);
        
        break;
    } else if (ob1[i].id.indexOf('nrelate') == 0) {
        var i = document.createElement('img');
        i.setAttribute('src', 'http://Impressions-Proxy-1085035873.us-east-1.elb.amazonaws.com/impression.do/?event=nrelate&user_id=' + escape(pbuid) + '&implementation_id=' + escape(pbimp) + '&source=' + escape(pbs) + '&referrer=' + escape(pbref));
        i.style.display = 'none';
        document.body.appendChild(i);
        break;
    } else if (ob1[i].id.indexOf('linkwithin') == 0) {
        var i = document.createElement('img');
        i.setAttribute('src', 'http://Impressions-Proxy-1085035873.us-east-1.elb.amazonaws.com/impression.do/?event=linkwithin&user_id=' + escape(pbuid) + '&implementation_id=' + escape(pbimp) + '&source=' + escape(pbs) + '&referrer=' + escape(pbref));
        i.style.display = 'none';
        document.body.appendChild(i);
        break;
    }
}
