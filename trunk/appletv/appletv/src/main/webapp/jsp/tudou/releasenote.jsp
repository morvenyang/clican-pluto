<%@ page contentType="text/xml;charset=utf-8" %><?xml version="1.0" encoding="UTF-8"?>
<atv>
	<body>
		<scroller id="com.atvttvv.index">
			<header>
				<simpleHeader><title>土豆相关视频试用/2013/01/05</title></simpleHeader>
			</header>
			
			<items>
				<collectionDivider alignment="left" accessibilityLabel=""><title>性能有待优化</title></collectionDivider>
				<collectionDivider alignment="left" accessibilityLabel=""><title>收藏功能还未实现</title></collectionDivider>
				<collectionDivider alignment="left" accessibilityLabel=""><title>搜索功能还未实现</title></collectionDivider>
				<collectionDivider alignment="left" accessibilityLabel=""><title>电视剧的剧集查看还未实现</title></collectionDivider>
				<collectionDivider alignment="left" accessibilityLabel=""><title>分页简单实现有待修改</title></collectionDivider>
				<collectionDivider alignment="left" accessibilityLabel=""><title>由于个人精力有限下期升级预计2周左右，期间大家有问题请去http://www.ottnt.com论坛反馈</title></collectionDivider>
				<grid id="grid_1" columnCount="5">
					<items>
						<actionButton id="shelf_item_1" 
							onSelect="atv.loadURL('${serverurl}/tudou/index.xml');"
							onPlay="atv.loadURL('${serverurl}/tudou/index.xml');">
							<title>继续</title>
						</actionButton>
					</items>
				</grid>
			</items>
		</scroller>
	</body>
</atv>