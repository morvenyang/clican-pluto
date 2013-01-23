
// Loads the full screen media browser
function onPhotoSelection(imageUrl) {
	var photoDict = {type:'photo',assets:[{width:1024,height:768,src:imageUrl}]};
	var photoDicts =[photoDict];
	var fullScreenMediaBrowser = new atv.FullScreenMediaBrowser();
	fullScreenMediaBrowser.show(photoDicts, 0);
};

