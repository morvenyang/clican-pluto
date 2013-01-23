
// Loads the full screen media browser
function onPhotoSelection(imageUrl) {
	var photoDict = {id:"ph1",type:'photo',assets:[{width:1024,height:768,src:imageUrl}]};
	var photoDicts =[photoDict];
	var fullScreenMediaBrowser = new atv.FullScreenMediaBrowser();
	
	fullScreenMediaBrowser.onItemSelection = function(photoID) {
		//
	};
	
	fullScreenMediaBrowser.show(photoDicts, 0);
};

