var subTitleClient = {
		
		playWithSubTitlesScript : function(url,subTitlesScript){
			this.playWithSubTitles(url,JSON.parse(appletv.decode(subTitlesScript)));
		},
		
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
			var offsetList =[-10,-9,-8,-7,-6,-5,-4,-3,-2,-1,0,1,2,3,4,5,6,7,8,9,10];
			var currentIndex = parseInt(selectedOffset)+10;
			if(currentIndex<0){
				currentIndex=0;
			}else if(currentIndex>20){
				currentIndex=20;
			}
			var data = {"serverurl":appletv.serverurl,"url":url.replace(new RegExp('&', 'g'), '&amp;'),"subTitles":subTitles,"subTitlesScript":subTitlesScript,"offsetList":offsetList,"selectedOffset":selectedOffset,"currentIndex":currentIndex};
			var xml = new EJS({
				url : appletv.serverurl + '/template/subTitle.ejs'
			}).render(data);
			appletv.loadAndSwapXML(xml);
		},
		
		playWithSubTitleAndOffset : function(url,subTitleUrl,offset){
			offset = parseInt(offset)
			appletv.logToServer('offset='+offset)
			if(subTitleUrl==null||subTitleUrl.length==0){
				if(url.indexOf("/appletv/noctl")!=-1){
					appletv.play(url);
				}else{
					if(appletv.isFlvPlay()){
						appletv.playMkv(url);
					}else{
						appletv.playM3u8(url);
					}
				}
			}else{
				appletv.makeRequest(subTitleUrl,function(content){
					var subTitles = [];
					if(content!=null&&content.length>0){
						if (content.indexOf('[Script Info]') == 0
								&& content.indexOf('[Events]') > 0) {
							var subValues = appletv.getSubValues(content,'Dialogue:','\n');
							appletv.logToServer('dialogue size:'+subValues.length);
							for ( var i = 0; i < subValues.length; i++) {
								fields = subValues[i].split(',');
								startt = subTitleClient.subti(fields[1], '.',offset);
								endt = subTitleClient.subti(fields[2], '.',offset);
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
								var startt = subTitleClient.subti(sta[0],offset);
								var endt = subTitleClient.subti(sta[1],offset);
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
					}
					if(url.indexOf("/appletv/noctl")!=-1&&subTitles.length>0){
						appletv.setValue('clican.play.subTitles', subTitles);
						//for smb resource
						appletv.makePlayPlist(url);
					}else{
						//for xunlei
						if(appletv.isFlvPlay()){
							appletv.playMkv(url,'',subTitles);
						}else{
							appletv.playM3u8(url,'',subTitles);
						}
						
						
					}
				});
			}
		},
		
		subti : function(sts, point, offset) {
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
			result = secs + (ml / 1000.0)+offset;
			if(result<0){
				result = 0;
			}
			return result;
		},
}