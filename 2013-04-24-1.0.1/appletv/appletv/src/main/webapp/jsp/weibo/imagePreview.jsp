<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %><%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %><%@ page contentType="text/xml;charset=utf-8" %><?xml version="1.0" encoding="UTF-8"?>
<atv>
<head>
<script src="${serverurl}/javascript/photoPreview.js"/>
</head>
	<body>
		<mediaBrowser id="mb" gridLayout="mixed">
			<header>
				<headerWithCountAndButtons>
					<buttons>
						<actionButton id="settings_action_button">
							<title>放映</title>
						</actionButton>
					</buttons>
					<title></title>
					<subtitle></subtitle>
					<count>1</count>
				</headerWithCountAndButtons>
			</header>
			<items>
				<photo id="p1" onSelect="onPhotoSelection('http://img6.ph.126.net/i_SnwbRq1n7zJnHyk-wc0w==/6597548054214996466.jpg');">
					<assets>
						<photoAsset name="high_res" width="1024" height="768" src="http://img6.ph.126.net/i_SnwbRq1n7zJnHyk-wc0w==/6597548054214996466.jpg"/>
					</assets>
				</photo>
			</items>
		</mediaBrowser>
	</body>
</atv>