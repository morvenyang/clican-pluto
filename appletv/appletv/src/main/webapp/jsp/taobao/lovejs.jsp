<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %><%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %><%@ page contentType="text/javascript;charset=utf-8" %>
function onPhotoSelection() {
	var photoDicts =[];
	<c:forEach var="item" items="${itemList}">
		var photoDict${item.itemId} = {id:"${item.itemId}",type:'photo',assets:[{width:1024,height:768,src:'${item.picUrl}'}]};
		photoDicts.push(photoDict${item.itemId});
	</c:forEach>
	
	
	var fullScreenMediaBrowser = new atv.FullScreenMediaBrowser();
	fullScreenMediaBrowser.onItemSelection = function(photoID) {
		atv.loadURL('${serverurl}/ctl/taobao/item.xml?itemId='+photoID);
	};
	
	fullScreenMediaBrowser.onLoadMetadata = function(photoID) {
		var comments = [],
		var metadata = {};
		var photoTextMap ={};
		metadata.comments = comments;
		<c:forEach var="item" items="${itemList}">
		photoTextMap['${item.itemId}'] = '${item.title}'+' '+'${item.operateTime}';
		</c:forEach>
		var comment = {
				"text": photoTextMap[photoID],
				"footer": ''
		};
		comments.push(comment);
		
		fullScreenMediaBrowser.updateMetadata(photoID, metadata);
	};
	fullScreenMediaBrowser.show(photoDicts, 0);
};