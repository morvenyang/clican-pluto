var subTitleClient = {
		
		playWithSubTitles : function(url,subTitles){
			var subTitlesScript = appletv.encode(JSON.stringify(subTitles));
			var offsetList =[-10,-9,-8,-7,-6,-5,-4,-3,-2,-1,0,1,2,3,4,5,6,7,8,9,10];
			var data = {"serverurl":appletv.serverurl,"url":url.replace(new RegExp('&', 'g'), '&amp;'),"subTitles":subTitles,"subTitlesScript":subTitlesScript,"offsetList":offsetList,"selectedOffset":0,"currentIndex":10};
			var xml = new EJS({
				url : appletv.serverurl + '/template/subTitle.ejs'
			}).render(data);
			appletv.loadAndSwapXML(xml);
		},
		
		changeSubTitleOffset : function(url,subTitlesScript,selectedOffset){
			var subTitles =JSON.parse(appletv.decode(subTitlesScript));
			var offsetList =[-10,-9,-8,-7,-6,-5,-4,-3,-1,0,1,2,3,4,5,6,7,8,9,10];
			var currentIndex = parseInt(selectedOffset)+10;
			appletv.logToServer('currentIndex:'+currentIndex);
			var data = {"serverurl":appletv.serverurl,"url":url.replace(new RegExp('&', 'g'), '&amp;'),"subTitles":subTitles,"subTitlesScript":subTitlesScript,"offsetList":offsetList,"selectedOffset":selectedOffset,"currentIndex":currentIndex};
			var xml = new EJS({
				url : appletv.serverurl + '/template/subTitle.ejs'
			}).render(data);
			appletv.loadAndSwapXML(xml);
		},
		
		playWithSubTitleAndOffset : function(url,subTitleUrl,offset){
			appletv.makeRequest(subTitleUrl,function(content){
				var subTitles = [];
				appletv.logToServer(content);
				if (content.indexOf('[Script Info]') == 0
						&& content.indexOf('[Events]') > 0) {
					var subValues = appletv.getSubValues(content,'Dialogue: (',')');
					for ( var i = 0; i < subValues.length; i++) {
						fields = subValues[i][0].split(',');
						startt = subTitleClient.subti(fields[1], '.');
						endt = subTitleClient.subti(fields[2], '.');
						subinfo = fields.splice(9).join(",").replace(
								/\\n/i, '\n').replace(/{[^}]*}/g, '');
						if (i > 0)
							laststartt = subTitles[subTitles.length - 1][1];
						else
							laststartt = -1;
						if (startt < laststartt) {
							for ( var j = 0; j < subTitles.length; j++) {
								laststartt = subTitles[j][1];
								if (startt < laststartt) {
									subTitles.splice(j, 0, [ i, startt, endt,
											subinfo ]);
									break;
								}
							}
						} else
							subTitles.push([ i, startt, endt, subinfo ]);
					}
				} else {
					lines = content.replace(/\r/g, '').split('\n\n');
					for ( var i = 0; i < lines.length; i++) {
						var subline = lines[i].split('\n');
						while (subline[0] == '')
							subline.shift();
						var subid = subline.shift();
						if (!subid)
							continue;
						var subtime = subline.shift();
						if (!subtime)
							continue;
						var sta = subtime.split('-->');
						if (sta.length != 2)
							continue;
						var startt = subTitleClient.subti(sta[0]);
						var endt = subTitleClient.subti(sta[1]);
						var subinfo = subline.join('\n').replace(/\\n/i,
								'\n').replace(/{[^}]*}/g, '');
						if (i > 0)
							laststartt = subTitles[subTitles.length - 1][1];
						else
							laststartt = -1;
						if (startt < laststartt) {
							for ( var j = 0; j < subTitles.length; j++) {
								laststartt = subTitles[j][1];
								if (startt < laststartt) {
									subTitles.splice(j, 0, [ subid, startt,
											endt, subinfo ]);
									break;
								}
							}
						} else
							subTitles.push([ subid, startt, endt, subinfo ]);
					}
				}
				appletv.logToServer(JSON.stringify(subTitles));
				appletv.playM3u8(url,'',subTitles);
			});
		},
		
		subti : function(sts, point) {
			point = point || ','
			sa = sts.split(point);
			sec = sa[0];
			if (sa.length > 1) {
				ml = parseInt(sa[1], 10);
			} else
				ml = 0;
			ta = sec.split(':');
			if (ta.length == 3) {
				secs = parseInt(ta[0], 10) * 3600 + parseInt(ta[1], 10) * 60
						+ parseInt(ta[2], 10);
			} else if (ta.length == 2) {
				secs = parseInt(ta[0], 10) * 60 + parseInt(ta[1], 10);
			} else
				secs = parseInt(ta[0], 10);
			return secs + (ml / 1000.0);
		},
}