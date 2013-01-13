<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %><%@ page contentType="text/xml;charset=utf-8" %><?xml version="1.0" encoding="UTF-8"?>
<atv>
<body>
		<scrollingText id="releasenote">
		    <title>更新日志2013/01/12</title>
		    <text>
		        <![CDATA[
本次升级内容:
新增加QQ站点

修复Bug如下:
1, 搜索导致的系统奔溃，由于土豆的搜索接口对于某些关键字搜索响应非常慢，最终导致服务器线程无法快速释放，导致系统崩溃。现在对搜索接口设置3秒超时。
2, 修复搜索时带空格导致搜索结果为空的错误

待开发内容:
收藏

已知问题:
分页实现有待优化
性能有待优化


联系方式:
邮件clican@gmail.com
或新浪微博:clican
关注QQ群内讨论,群号:222099750
		        ]]>
		    </text>
		    <buttons>
		        <actionButton id="shelf_item_1" 
							onSelect="atv.loadURL('http://10.0.1.5/appletv/tudou/index.xml');">
							<title>土豆</title>
						</actionButton>
						<actionButton id="shelf_item_2" 
							onSelect="atv.loadURL('http://10.0.1.5/appletv/qq/index.xml');">
							<title>QQ</title>
						</actionButton>
						<actionButton id="shelf_item_2" 
							onSelect="atv.loadURL('http://10.0.1.5/appletv/weibo/checkAccessToken.xml?deviceId='+atv.device.udid);">
							<title>Weibo</title>
						</actionButton>
		    </buttons>
		</scrollingText>
	</body>
</atv>