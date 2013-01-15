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
								<c:if test="${weiboFavoriteAuthor}">
									<actionButton id="shelf_1" onSelect="atv.loadURL('${serverurl}/weibo/homeTimeline.xml');" onPlay="atv.loadURL('${serverurl}/weibo/homeTimeline.xml');">
										<title>开始微博</title>
									</actionButton>
								</c:if>
							</items>
						</shelfSection>
					</sections>
				</shelf>
			</centerShelf>
		</itemDetail>
	</body>
</atv>