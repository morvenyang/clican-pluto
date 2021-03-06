<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %><%@ page contentType="text/xml;charset=utf-8" %><?xml version="1.0" encoding="UTF-8"?>
<atv>
<head><script src="${serverurl}/javascript/clican.js"/></head>
<body>
		<itemDetail id="profile">
			<title>${weiboUser.name}</title>
			<image style="moviePoster">${weiboUser.avatarLarge}</image>
			<summary>${weiboUser.description}</summary>
			<table>
				<columnDefinitions>
					<columnDefinition width="30"></columnDefinition>
					<columnDefinition width="30"></columnDefinition>
					<columnDefinition width="30"></columnDefinition>
				</columnDefinitions>
				<rows>
					<row>
						<label><![CDATA[关注:${weiboUser.friendsCount}]]></label>
						<label><![CDATA[粉丝:${weiboUser.followersCount}]]></label>
						<label><![CDATA[微博:${weiboUser.statusesCount}]]></label>
					</row>
				</rows>
			</table>
			<centerShelf>
				<shelf id="shelf">
					<sections>
						<shelfSection>
							<items>
									<actionButton id="shelf_1" onSelect="atv.loadURL('${serverurl}/ctl/weibo/homeTimeline.xml');" onPlay="atv.loadURL('${serverurl}/ctl/weibo/homeTimeline.xml');">
										<title>全部</title>
									</actionButton>
									<actionButton id="shelf_2" onSelect="atv.loadURL('${serverurl}/ctl/weibo/homeTimeline.xml?feature=4');" onPlay="atv.loadURL('${serverurl}/ctl/weibo/homeTimeline.xml?feature=4');">
										<title>音乐</title>
									</actionButton>
									<actionButton id="shelf_3" onSelect="atv.loadURL('${serverurl}/ctl/weibo/homeTimeline.xml?feature=3');" onPlay="atv.loadURL('${serverurl}/ctl/weibo/homeTimeline.xml?feature=3');">
										<title>视频</title>
									</actionButton>
									<actionButton id="shelf_4" onSelect="atv.loadURL('${serverurl}/ctl/weibo/favorite.xml');" onPlay="atv.loadURL('${serverurl}/ctl/weibo/favorite.xml');">
										<title>收藏</title>
									</actionButton>
							</items>
						</shelfSection>
					</sections>
				</shelf>
			</centerShelf>
		</itemDetail>
	</body>
</atv>