<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %><%@ page contentType="text/xml;charset=utf-8" %><?xml version="1.0" encoding="UTF-8"?>
<atv>
<head>
<script src="${serverurl}/javascript/clican.js"/>
<script src="${serverurl}/javascript/ejs.js"/>
<script src="${serverurl}/javascript/qq.js"/>
</head>
<body>
		<scrollingText id="releasenote">
		    <title>升级维护中2013/01/23</title>
		    <text>
		        <![CDATA[
联系方式: http://15.185.225.83/appletv/index.html
邮件clican@gmail.com
或新浪微博:clican
关注QQ群内讨论,群号:222099750 个人QQ: 103984484

=================================本次更新2013/01/23=================================	
本次升级内容:
1, 在新浪微博中增加音乐频道,主要支持虾米和Sina的MP3,通过Plan键来播放
2, 支持新浪微博的收藏和转发功能
3, 使用高级版本的新浪分享接口，分享时可以分享相关图片
4, 通过Plan键来预览大图片

修复Bug如下:
N/A

待开发内容:
1, 收藏
2, 新浪微电影的集成

已知问题:
1, 微博内长图片无法适配显示，受限于ATV3渲染能力，目前无法解决
2, 鉴于目前服务器压力并不是很大,暂时不会优化性能

=================================历史更新2013/01/18=================================	

新增加新浪微博集成，具体操作流程请参见 http://15.185.225.83/appletv/index.html
本次集成内容如下:
1, 土豆和QQ站点内的视频可以分享到个人微博
2, 通过Weibo站点可以浏览个人的微博内容
3, 特别加入视频类微博,其中支持的视频网站有 Youku, Tudou, QQ, Sohu, 56. 暂时无法支持Sina和QiYi

修复Bug如下:
1, 修复部分QQ频道无法使用的问题

待开发内容:
1, 收藏
2, 微博内音乐的集成播放


已知问题:
1, 微博内长图片无法适配显示，受限于ATV3渲染能力，目前无法解决
2, 性能有待优化

=================================历史更新2013/01/12=================================		        
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



		        ]]>
		    </text>
		    <buttons>
		        <actionButton id="shelf_item_1" 
							onSelect="atv.loadURL('${serverurl}/tudou/index.xml');">
							<title>土豆</title>
						</actionButton>
						<actionButton id="shelf_item_2" 
							onSelect="atv.loadURL('${serverurl}/qq/index.xml');">
							<title>QQ</title>
						</actionButton>
						<actionButton id="shelf_item_3" 
							onSelect="atv.loadURL('${serverurl}/weibo/checkAccessToken.xml?deviceId='+atv.device.udid);">
							<title>Weibo</title>
						</actionButton>
		    </buttons>
		</scrollingText>
	</body>
</atv>