function loadData() {
	var page ="<?xml version=\"1.0\" encoding=\"UTF-8\"?><atv><body><listScrollerSplit id=\"bbbb\"><header><simpleHeader horizontalAlignment=\"left\"><title>aaa</title><image>http://i4.tdimg.com/152/199/347/p.jpg</image></simpleHeader></header><menu><sections><menuSection><items>";
	page + "<imageTextImageMenuItem id=\"1\"><leftImage></leftImage><imageSeparatorText></imageSeparatorText><label>1</label><rightImage></rightImage></imageTextImageMenuItem>";
	page = page+"</items></menuSection></sections></menu></listScrollerSplit></body></atv>";
	atv.loadXML(atv.parseXML(page));
}