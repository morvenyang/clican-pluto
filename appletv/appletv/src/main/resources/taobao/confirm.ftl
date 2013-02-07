<html>
<body style="font: 12px/1.5 Tahoma, Helvetica, Arial, '\5b8b\4f53', sans-serif;">
	<div id="page" style="width: 950px;margin-left: auto;margin-right: auto;">
		<div id="content">
			<form id="J_Form" name="J_Form"
				action="http://buy.taobao.com/auction/order/unity_order_confirm.htm"
				method="post">
				<div>
					<table cellspacing="0" cellpadding="0" style="border-color: gray;width: 100%;margin-top: 20px;font-size: inherit;border-collapse: collapse;border-spacing: 0;display: table;font: 12px/1.5 Tahoma, Helvetica, Arial, '\5b8b\4f53', sans-serif;">
						<thead>
							<tr>
								<th style="width: 350px;border-left: 0;text-align: center;height: 24px;line-height: 24px;font-weight: bold;display: table-cell;vertical-align: inherit;font-size: inherit;border-collapse: collapse;border-spacing: 2px;border-color: gray;font: 12px/1.5 Tahoma, Helvetica, Arial, '\5b8b\4f53', sans-serif;">店铺宝贝
									<hr>
								</th>
								<th style="width: 148px;border-left: 0;text-align: center;height: 24px;line-height: 24px;font-weight: bold;display: table-cell;vertical-align: inherit;font-size: inherit;border-collapse: collapse;border-spacing: 2px;border-color: gray;font: 12px/1.5 Tahoma, Helvetica, Arial, '\5b8b\4f53', sans-serif;">单价(元)
									<hr>
								</th>
								<th style="width: 118px;border-left: 0;text-align: center;height: 24px;line-height: 24px;font-weight: bold;display: table-cell;vertical-align: inherit;font-size: inherit;border-collapse: collapse;border-spacing: 2px;border-color: gray;font: 12px/1.5 Tahoma, Helvetica, Arial, '\5b8b\4f53', sans-serif;">数量
									<hr>
								</th>
								<th style="width: 191px;border-left: 0;text-align: center;height: 24px;line-height: 24px;font-weight: bold;display: table-cell;vertical-align: inherit;font-size: inherit;border-collapse: collapse;font: 12px/1.5 Tahoma, Helvetica, Arial, '\5b8b\4f53', sans-serif;">优惠方式(元)
									<hr>
								</th>
								<th style="width: 138px;border-left: 0;text-align: center;height: 24px;line-height: 24px;font-weight: bold;display: table-cell;vertical-align: inherit;font-size: inherit;border-collapse: collapse;font: 12px/1.5 Tahoma, Helvetica, Arial, '\5b8b\4f53', sans-serif;">小计(元)
									<hr>
								</th>
							</tr>
						</thead>
						<tbody>
							<#list tco.shopList as shop>
								<tr>
								<td colspan="5"></td>
							</tr>
							<tr style="line-height: 30px;">
								<td colspan="3">店铺：<a
									href="#" target="_blank" style="text-decoration: none;">${shop.title}</a> <span>卖家：${shop.title}</a> </span></td>
								<td colspan="2"></td>
							</tr>
							<#list shop.itemList as item>
							<tr style="background-color: #fafcff;text-align: center;overflow: hidden;padding: 5px 0;height: 70px">
								<td style="text-align: left;padding-left: 70px;"><a href="#" style="text-decoration: none;">${item.title}</a></td>
								<td>${item.price}</td>
								<td >${item.quantity}</td>
								<td>${item.promotion.title}</td>
								<td style="text-align: right"><span
									style="color: red; font: bold 12px tahoma; margin-right: 15px;">${item.actualPrice}</span>
								</td>
							</tr>
							</#list>
							<#if shop.promotion??>
							<tr style="background-color: #f2f7ff;text-align: center;overflow: hidden;padding: 5px 0;height: 30px">
								<td colspan="2"></td>
								<td >店铺优惠：</td>
								<td>${shop.promotion.title}</td>
								<td style="text-align: right"><span
									style="color: #404040; font: bold 12px tahoma; margin-right: 15px;">-${shop.promotion.discount}</span>
								</td>
							</tr>
							</#if>
							<tr style="background-color: #f2f7ff;text-align: center;overflow: hidden;padding: 5px 0;height: 30px">
								<td colspan="2"></td>
								<td >运送方式：</td>
								<td>${shop.selectedFare.label}</td>
								<td style="text-align: right"><span
									style="color: red; font: bold 12px tahoma; margin-right: 15px;">${shop.selectedFare.fareFee}</span>
								</td>
							</tr>
							<tr style="background-color: #f2f7ff;text-align: center;overflow: hidden;padding: 5px 0;height: 30px">
								<td colspan="5" style="margin: 0;padding: 0;text-align:right">店铺合计(含运费，服务费)：￥<span
										style="color: red; font: bold 12px tahoma; margin-right: 15px;">${shop.total}</span>
								</td>
							</tr>
							</#list> 
							<tr style="background-color: #f2f7ff;text-align: center;overflow: hidden;padding: 5px 0;height: 30px">
								<td colspan="5" style="margin: 0;padding: 0;text-align:right">合计：￥<span
										style="color: red; font: bold 12px tahoma; margin-right: 15px;">${tco.total}</span>
								</td>
							</tr>
						</tbody>
					</table>
				</div>
			</form>
		</div>
	</div>
</body>
</html>