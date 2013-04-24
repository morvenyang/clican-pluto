<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %><%@ page contentType="text/xml;charset=utf-8" %><?xml version="1.0" encoding="UTF-8"?>
<atv>
<head><script src="${serverurl}/javascript/clican.js"/></head>
<head><script src="${serverurl}/javascript/userconfig.js"/></head>
<body>
		<listScrollerSplit id="bbbb">
			<header>
				<simpleHeader horizontalAlignment="left">
					<title>个人设置</title>
					<image></image>
				</simpleHeader>
			</header>
			<menu>
				<sections>
					<menuSection>
						<items>
							<oneLineMenuItem id="1" onSelect="appletv.showInputTextPage('设置本地服务器IP','本地服务器用于代理视频内容的加载,提升加载速度减少延迟卡顿现象',userconfig.saveLocalServerIP,'userconfig.saveLocalServerIP','${result['userconfig.localServerIP']}');">
									<label>设置本地服务器IP:${result['userconfig.localServerIP']}</label>
							</oneLineMenuItem>
						</items>
					</menuSection>
				</sections>
			</menu>
		</listScrollerSplit>
	</body>
</atv>