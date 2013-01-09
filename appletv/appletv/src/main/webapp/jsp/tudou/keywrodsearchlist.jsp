<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %><%@ page contentType="text/xml;charset=utf-8" %><?xml version="1.0" encoding="UTF-8"?>
<!DOCTYPE plist PUBLIC "-//Apple//DTD PLIST 1.0//EN" "http://www.apple.com/DTDs/PropertyList-1.0.dtd">
<plist version="1.0">
	<dict>
		<key>items</key>
		<array>
			<c:forEach items="${keywordList}" var="keyword" varStatus="status">
				<dict>
				<key>menu-item</key>
				<dict>
					<key>type</key>
					<string>one-line-menu-item</string>
					<key>label</key>
					<string><![CDATA[${keyword.label}]]></string>
					<key>event-handlers</key>
					<dict>
						<key>select</key>
						<dict>
							<key>action</key>
							<string>load-url</string>
							<key>parameters</key>
							<dict>
							<key>url</key>
							<string>${serverurl}/tudou/index.xml?channelId=1001&amp;keyword=${keyword.urlValue}</string>
							</dict>
						</dict>
						<key>plan</key>
						<dict>
							<key>action</key>
							<string>load-url</string>
							<key>parameters</key>
							<dict>
							<key>url</key>
							<string>${serverurl}/tudou/index.xml?channelId=1001&amp;keyword=${keyword.urlValue}</string>
							</dict>
						</dict>
					</dict>
				</dict>
				<key>identifier</key>
				<string>list_${status.count}</string>
			</dict>
			</c:forEach>
		</array>
	</dict>
</plist>