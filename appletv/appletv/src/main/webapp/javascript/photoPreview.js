
// Loads the full screen media browser
function onPhotoSelection(imageUrl) {
	var photoDict = {id:"ph1",type:'photo',assets:[{width:1024,height:768,src:imageUrl}]};
	var photoDicts =[photoDict];
	var fullScreenMediaBrowser = new atv.FullScreenMediaBrowser();
	//fullScreenMediaBrowser.fullScreenSize = {width:1024,height:7680};
	//fullScreenMediaBrowser.scrollRectToVisible = {size:{width:1024,height:7680},origin:{x:0,y:0}};
	fullScreenMediaBrowser.onItemSelection = function(photoID) {
	};
	
	fullScreenMediaBrowser.show(photoDicts, 0);
};

function onPhotoSelections(imageUrls) {
	var photoDicts =[];
	var imageUrlArray = imageUrls.split(",");
	var photoDict;
	var id;
	for(var i=0;i<imageUrlArray.length;i++){
		id = 'ph'+i;
		photoDict = {'id':id,'type':'photo','assets':[{width:1024,height:768,src:imageUrlArray[i]}]};
		photoDicts.push(photoDict);
	}
	appletv.logToServer(JSON.stringify(photoDicts));
	appletv.logToServer(imageUrlArray.length-1);
	var fullScreenMediaBrowser = new atv.FullScreenMediaBrowser();
	
	fullScreenMediaBrowser.onItemSelection = function(photoID) {
		//
	};
	
	fullScreenMediaBrowser.show(photoDicts, 0);
};

