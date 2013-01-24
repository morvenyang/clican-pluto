package com.taobao.api.request;

import java.util.Date;
import com.taobao.api.internal.util.RequestCheckUtils;
import java.util.HashMap;
import java.util.Map;

import com.taobao.api.FileItem;
import com.taobao.api.TaobaoUploadRequest;
import com.taobao.api.internal.util.TaobaoHashMap;
import com.taobao.api.response.ItemUpdateResponse;
import com.taobao.api.ApiRuleException;
/**
 * TOP API: taobao.item.update request
 * 
 * @author auto create
 * @since 1.0, 2013-01-24 12:41:47
 */
public class ItemUpdateRequest implements TaobaoUploadRequest<ItemUpdateResponse> {

	private TaobaoHashMap udfParams; // add user-defined text parameters
	private Long timestamp;

	/** 
	* 售后服务说明模板id
	 */
	private Long afterSaleId;

	/** 
	* 商品上传后的状态。可选值:onsale（出售中）,instock（库中），如果同时更新商品状态为出售中及list_time为将来的时间，则商品还是处于定时上架的状态, 此时item.is_timing为true
	 */
	private String approveStatus;

	/** 
	* 商品的积分返点比例。如：5 表示返点比例0.5%. 注意：返点比例必须是>0的整数，而且最大是90,即为9%.B商家在发布非虚拟商品时，返点必须是 5的倍数，即0.5%的倍数。其它是1的倍数，即0.1%的倍数。无名良品商家发布商品时，复用该字段记录积分宝返点比例，返点必须是对应类目的返点步长的整数倍，默认是5，即0.5%。注意此时该字段值依旧必须是>0的整数，注意此时该字段值依旧必须是>0的整数，最高值不超过500，即50%
	 */
	private Long auctionPoint;

	/** 
	* 代充商品类型。只有少数类目下的商品可以标记上此字段，具体哪些类目可以上传可以通过taobao.itemcat.features.get获得。在代充商品的类目下，不传表示不标记商品类型（交易搜索中就不能通过标记搜到相关的交易了）。可选类型： 
no_mark(不做类型标记) 
time_card(点卡软件代充) 
fee_card(话费软件代充)
	 */
	private String autoFill;

	/** 
	* 叶子类目id
	 */
	private Long cid;

	/** 
	* 货到付款运费模板ID
	 */
	private Long codPostageId;

	/** 
	* 商品描述. 字数要大于5个字符，小于25000个字符 ，受违禁词控制
	 */
	private String desc;

	/** 
	* 支持宝贝信息的删除,如需删除对应的食品安全信息中的储藏方法、保质期， 则应该设置此参数的值为：food_security.plan_storage,food_security.period; 各个参数的名称之间用【,】分割, 如果对应的参数有设置过值，即使在这个列表中，也不会被删除; 目前支持此功能的宝贝信息如下：食品安全信息所有字段、电子交易凭证字段（locality_life，locality_life.verification，locality_life.refund_ratio，locality_life.network_id ）
	 */
	private String emptyFields;

	/** 
	* ems费用。取值范围:0.01-999.00;精确到2位小数;单位:元。如:25.07，表示:25元7分
	 */
	private String emsFee;

	/** 
	* 快递费用。取值范围:0.01-999.00;精确到2位小数;单位:元。如:15.07，表示:15元7分
	 */
	private String expressFee;

	/** 
	* 厂家联系方式
	 */
	private String foodSecurityContact;

	/** 
	* 产品标准号
	 */
	private String foodSecurityDesignCode;

	/** 
	* 厂名
	 */
	private String foodSecurityFactory;

	/** 
	* 厂址
	 */
	private String foodSecurityFactorySite;

	/** 
	* 食品添加剂
	 */
	private String foodSecurityFoodAdditive;

	/** 
	* 配料表
	 */
	private String foodSecurityMix;

	/** 
	* 保质期
	 */
	private String foodSecurityPeriod;

	/** 
	* 储藏方法
	 */
	private String foodSecurityPlanStorage;

	/** 
	* 生产许可证号
	 */
	private String foodSecurityPrdLicenseNo;

	/** 
	* 生产结束日期,格式必须为yyyy-MM-dd
	 */
	private String foodSecurityProductDateEnd;

	/** 
	* 生产开始日期，格式必须为yyyy-MM-dd
	 */
	private String foodSecurityProductDateStart;

	/** 
	* 进货结束日期，要在生产日期之后，格式必须为yyyy-MM-dd
	 */
	private String foodSecurityStockDateEnd;

	/** 
	* 进货开始日期，要在生产日期之后，格式必须为yyyy-MM-dd
	 */
	private String foodSecurityStockDateStart;

	/** 
	* 供货商
	 */
	private String foodSecuritySupplier;

	/** 
	* 运费承担方式。运费承担方式。可选值:seller（卖家承担）,buyer(买家承担);
	 */
	private String freightPayer;

	/** 
	* 针对全球购卖家的库存类型业务，
有两种库存类型：现货和代购
参数值为1时代表现货，值为2时代表代购
如果传值为这两个值之外的值，会报错;
如果不是全球购卖家，这两个值即使设置也不会处理
	 */
	private String globalStockType;

	/** 
	* 支持会员打折。可选值:true,false;
	 */
	private Boolean hasDiscount;

	/** 
	* 是否有发票。可选值:true,false (商城卖家此字段必须为true)
	 */
	private Boolean hasInvoice;

	/** 
	* 橱窗推荐。可选值:true,false;
	 */
	private Boolean hasShowcase;

	/** 
	* 是否有保修。可选值:true,false;
	 */
	private Boolean hasWarranty;

	/** 
	* 商品图片。类型:JPG,GIF;最大长度:500k
	 */
	private FileItem image;

	/** 
	* 加价幅度 如果为0，代表系统代理幅度
	 */
	private String increment;

	/** 
	* 用户自行输入的类目属性ID串，结构："pid1,pid2,pid3"，如："20000"（表示品牌） 注：通常一个类目下用户可输入的关键属性不超过1个。
	 */
	private String inputPids;

	/** 
	* 用户自行输入的子属性名和属性值，结构:"父属性值;一级子属性名;一级子属性值;二级子属性名;自定义输入值,....",如：“耐克;耐克系列;科比系列;科比系列;2K5,Nike乔丹鞋;乔丹系列;乔丹鞋系列;乔丹鞋系列;json5”，多个自定义属性用','分割，input_str需要与input_pids一一对应，注：通常一个类目下用户可输入的关键属性不超过1个。所有属性别名加起来不能超过3999字节。此处不可以使用“其他”、“其它”和“其她”这三个词。
	 */
	private String inputStr;

	/** 
	* 是否是3D
	 */
	private Boolean is3D;

	/** 
	* 是否在外店显示
	 */
	private Boolean isEx;

	/** 
	* 实物闪电发货。注意：在售的闪电发货产品不允许取消闪电发货，需要先下架商品才能取消闪电发货标记
	 */
	private Boolean isLightningConsignment;

	/** 
	* 是否替换sku
	 */
	private Boolean isReplaceSku;

	/** 
	* 是否在淘宝上显示（如果传FALSE，则在淘宝主站无法显示该商品）
	 */
	private Boolean isTaobao;

	/** 
	* 商品是否为新品。只有在当前类目开通新品,并且当前用户拥有该类目下发布新品权限时才能设置is_xinpin为true，否则设置true后会返回错误码:isv.invalid-permission:xinpin。同时只有一口价全新的宝贝才能设置为新品，否则会返回错误码：isv.invalid-parameter:xinpin。不设置参数就保持原有值。
	 */
	private Boolean isXinpin;

	/** 
	* 表示商品的体积，如果需要使用按体积计费的运费模板，一定要设置这个值。该值的单位为立方米（m3），如果是其他单位，请转换成成立方米。
该值支持两种格式的设置：格式1：bulk:3,单位为立方米(m3),表示直接设置为商品的体积。格式2：length:10;breadth:10;height:10，单位为米（m）。体积和长宽高都支持小数类型。
在传入体积或长宽高时候，不能带单位。体积的单位默认为立方米（m3），长宽高的单位默认为米(m)
在编辑的时候，如果需要删除体积属性，请设置该值为0，如bulk:0
	 */
	private String itemSize;

	/** 
	* 商品的重量，用于按重量计费的运费模板。注意：单位为kg。 只能传入数值类型（包含小数），不能带单位，单位默认为kg。 在编辑时候，如果需要在商品里删除重量的信息，就需要将值设置为0
	 */
	private String itemWeight;

	/** 
	* 商品文字的版本，繁体传入”zh_HK”，简体传入”zh_CN”
	 */
	private String lang;

	/** 
	* 上架时间。大于当前时间则宝贝会下架进入定时上架的宝贝中。
	 */
	private Date listTime;

	/** 
	* 编辑电子凭证宝贝时候表示是否使用邮寄
0: 代表不使用邮寄；
1：代表使用邮寄；
如果不设置这个值，代表不使用邮寄
	 */
	private String localityLifeChooseLogis;

	/** 
	* 本地生活电子交易凭证业务，目前此字段只涉及到的信息为有效期;
如果有效期为起止日期类型，此值为2012-08-06,2012-08-16
如果有效期为【购买成功日 至】类型则格式为2012-08-16
如果有效期为天数类型则格式为15
	 */
	private String localityLifeExpirydate;

	/** 
	* 码商信息，格式为 码商id:nick
	 */
	private String localityLifeMerchant;

	/** 
	* 网点ID,在参数empty_fields里设置locality_life.network_id可删除网点ID
	 */
	private String localityLifeNetworkId;

	/** 
	* 退款比例，百分比%前的数字,1-100的正整数值; 在参数empty_fields里设置locality_life.refund_ratio可删除退款比例
	 */
	private Long localityLifeRefundRatio;

	/** 
	* 核销打款,1代表核销打款 0代表非核销打款; 在参数empty_fields里设置locality_life.verification可删除核销打款
	 */
	private String localityLifeVerification;

	/** 
	* 所在地城市。如杭州 具体可以下载http://dl.open.taobao.com/sdk/商品城市列表.rar 取到
	 */
	private String locationCity;

	/** 
	* 所在地省份。如浙江 具体可以下载http://dl.open.taobao.com/sdk/商品城市列表.rar 取到
	 */
	private String locationState;

	/** 
	* 商品数量，取值范围:0-999999的整数。且需要等于Sku所有数量的和
	 */
	private Long num;

	/** 
	* 商品数字ID，该参数必须
	 */
	private Long numIid;

	/** 
	* 商家编码
	 */
	private String outerId;

	/** 
	* 商品主图需要关联的图片空间的相对url。这个url所对应的图片必须要属于当前用户。pic_path和image只需要传入一个,如果两个都传，默认选择pic_path
	 */
	private String picPath;

	/** 
	* 平邮费用。取值范围:0.01-999.00;精确到2位小数;单位:元。如:5.07，表示:5元7分, 注:post_fee,express_fee,ems_fee需一起填写
	 */
	private String postFee;

	/** 
	* 宝贝所属的运费模板ID。取值范围：整数且必须是该卖家的运费模板的ID（可通过taobao.postages.get获得当前会话用户的所有邮费模板）
	 */
	private Long postageId;

	/** 
	* 商品价格。取值范围:0-100000000;精确到2位小数;单位:元。如:200.07，表示:200元7分。需要在正确的价格区间内。
	 */
	private String price;

	/** 
	* 商品所属的产品ID(B商家发布商品需要用)
	 */
	private Long productId;

	/** 
	* 属性值别名。如pid:vid:别名;pid1:vid1:别名1， pid:属性id vid:值id。总长度不超过512字节
	 */
	private String propertyAlias;

	/** 
	* 商品属性列表。格式:pid:vid;pid:vid。属性的pid调用taobao.itemprops.get取得，属性值的vid用taobao.itempropvalues.get取得vid。 如果该类目下面没有属性，可以不用填写。如果有属性，必选属性必填，其他非必选属性可以选择不填写.属性不能超过35对。所有属性加起来包括分割符不能超过549字节，单个属性没有限制。 如果有属性是可输入的话，则用字段input_str填入属性的值。
	 */
	private String props;

	/** 
	* 景区门票在选择订金支付时候，需要交的预订费。传入的值是1到20之间的数值，小数点后最多可以保留两位（多余的部分将做四舍五入的处理）。这个数值表示的是预订费的比例，最终的预订费为 scenic_ticket_book_cost乘一口价除以100
	 */
	private String scenicTicketBookCost;

	/** 
	* 景区门票类宝贝编辑时候，当卖家签订了支付宝代扣协议时候，需要选择支付方式：全额支付和订金支付。当scenic_ticket_pay_way为1时表示全额支付，为2时表示订金支付
	 */
	private Long scenicTicketPayWay;

	/** 
	* 是否承诺退换货服务!虚拟商品无须设置此项!
	 */
	private Boolean sellPromise;

	/** 
	* 重新关联商品与店铺类目，结构:",cid1,cid2,...,"，如果店铺类目存在二级类目，必须传入子类目cids。
	 */
	private String sellerCids;

	/** 
	* Sku的外部id串，结构如：1234,1342,… sku_properties, sku_quantities, sku_prices, sku_outer_ids在输入数据时要一一对应，如果没有sku_outer_ids也要写上这个参数，入参是","(这个是两个sku的示列，逗号数应该是sku个数减1)；该参数最大长度是512个字节
	 */
	private String skuOuterIds;

	/** 
	* 更新的Sku的价格串，结构如：10.00,5.00,… 精确到2位小数;单位:元。如:200.07，表示:200元7分
	 */
	private String skuPrices;

	/** 
	* 更新的Sku的属性串，调用taobao.itemprops.get获取类目属性，如果属性是销售属性，再用taobao.itempropvalues.get取得vid。格式:pid:vid;pid:vid,多个sku之间用逗号分隔。该字段内的销售属性(自定义的除外)也需要在props字段填写 . 规则：如果该SKU存在旧商品，则修改；否则新增Sku。如果更新时有对Sku进行操作，则Sku的properties一定要传入。如果存在自定义销售属性，则格式为pid:vid;pid2:vid2;$pText:vText，其中$pText:vText为自定义属性。限制：其中$pText的’$’前缀不能少，且pText和vText文本中不可以存在 冒号:和分号;以及逗号
	 */
	private String skuProperties;

	/** 
	* 更新的Sku的数量串，结构如：num1,num2,num3 如:2,3,4
	 */
	private String skuQuantities;

	/** 
	* 商品新旧程度。可选值:new（全新）,unused（闲置）,second（二手）。
	 */
	private String stuffStatus;

	/** 
	* 商品是否支持拍下减库存:1支持;2取消支持(付款减库存);0(默认)不更改
集市卖家默认拍下减库存;
商城卖家默认付款减库存
	 */
	private Long subStock;

	/** 
	* 宝贝标题. 不能超过60字符,受违禁词控制
	 */
	private String title;

	/** 
	* 有效期。可选值:7,14;单位:天;
	 */
	private Long validThru;

	/** 
	* 商品的重量(商超卖家专用字段)
	 */
	private Long weight;

	public void setAfterSaleId(Long afterSaleId) {
		this.afterSaleId = afterSaleId;
	}
	public Long getAfterSaleId() {
		return this.afterSaleId;
	}

	public void setApproveStatus(String approveStatus) {
		this.approveStatus = approveStatus;
	}
	public String getApproveStatus() {
		return this.approveStatus;
	}

	public void setAuctionPoint(Long auctionPoint) {
		this.auctionPoint = auctionPoint;
	}
	public Long getAuctionPoint() {
		return this.auctionPoint;
	}

	public void setAutoFill(String autoFill) {
		this.autoFill = autoFill;
	}
	public String getAutoFill() {
		return this.autoFill;
	}

	public void setCid(Long cid) {
		this.cid = cid;
	}
	public Long getCid() {
		return this.cid;
	}

	public void setCodPostageId(Long codPostageId) {
		this.codPostageId = codPostageId;
	}
	public Long getCodPostageId() {
		return this.codPostageId;
	}

	public void setDesc(String desc) {
		this.desc = desc;
	}
	public String getDesc() {
		return this.desc;
	}

	public void setEmptyFields(String emptyFields) {
		this.emptyFields = emptyFields;
	}
	public String getEmptyFields() {
		return this.emptyFields;
	}

	public void setEmsFee(String emsFee) {
		this.emsFee = emsFee;
	}
	public String getEmsFee() {
		return this.emsFee;
	}

	public void setExpressFee(String expressFee) {
		this.expressFee = expressFee;
	}
	public String getExpressFee() {
		return this.expressFee;
	}

	public void setFoodSecurityContact(String foodSecurityContact) {
		this.foodSecurityContact = foodSecurityContact;
	}
	public String getFoodSecurityContact() {
		return this.foodSecurityContact;
	}

	public void setFoodSecurityDesignCode(String foodSecurityDesignCode) {
		this.foodSecurityDesignCode = foodSecurityDesignCode;
	}
	public String getFoodSecurityDesignCode() {
		return this.foodSecurityDesignCode;
	}

	public void setFoodSecurityFactory(String foodSecurityFactory) {
		this.foodSecurityFactory = foodSecurityFactory;
	}
	public String getFoodSecurityFactory() {
		return this.foodSecurityFactory;
	}

	public void setFoodSecurityFactorySite(String foodSecurityFactorySite) {
		this.foodSecurityFactorySite = foodSecurityFactorySite;
	}
	public String getFoodSecurityFactorySite() {
		return this.foodSecurityFactorySite;
	}

	public void setFoodSecurityFoodAdditive(String foodSecurityFoodAdditive) {
		this.foodSecurityFoodAdditive = foodSecurityFoodAdditive;
	}
	public String getFoodSecurityFoodAdditive() {
		return this.foodSecurityFoodAdditive;
	}

	public void setFoodSecurityMix(String foodSecurityMix) {
		this.foodSecurityMix = foodSecurityMix;
	}
	public String getFoodSecurityMix() {
		return this.foodSecurityMix;
	}

	public void setFoodSecurityPeriod(String foodSecurityPeriod) {
		this.foodSecurityPeriod = foodSecurityPeriod;
	}
	public String getFoodSecurityPeriod() {
		return this.foodSecurityPeriod;
	}

	public void setFoodSecurityPlanStorage(String foodSecurityPlanStorage) {
		this.foodSecurityPlanStorage = foodSecurityPlanStorage;
	}
	public String getFoodSecurityPlanStorage() {
		return this.foodSecurityPlanStorage;
	}

	public void setFoodSecurityPrdLicenseNo(String foodSecurityPrdLicenseNo) {
		this.foodSecurityPrdLicenseNo = foodSecurityPrdLicenseNo;
	}
	public String getFoodSecurityPrdLicenseNo() {
		return this.foodSecurityPrdLicenseNo;
	}

	public void setFoodSecurityProductDateEnd(String foodSecurityProductDateEnd) {
		this.foodSecurityProductDateEnd = foodSecurityProductDateEnd;
	}
	public String getFoodSecurityProductDateEnd() {
		return this.foodSecurityProductDateEnd;
	}

	public void setFoodSecurityProductDateStart(String foodSecurityProductDateStart) {
		this.foodSecurityProductDateStart = foodSecurityProductDateStart;
	}
	public String getFoodSecurityProductDateStart() {
		return this.foodSecurityProductDateStart;
	}

	public void setFoodSecurityStockDateEnd(String foodSecurityStockDateEnd) {
		this.foodSecurityStockDateEnd = foodSecurityStockDateEnd;
	}
	public String getFoodSecurityStockDateEnd() {
		return this.foodSecurityStockDateEnd;
	}

	public void setFoodSecurityStockDateStart(String foodSecurityStockDateStart) {
		this.foodSecurityStockDateStart = foodSecurityStockDateStart;
	}
	public String getFoodSecurityStockDateStart() {
		return this.foodSecurityStockDateStart;
	}

	public void setFoodSecuritySupplier(String foodSecuritySupplier) {
		this.foodSecuritySupplier = foodSecuritySupplier;
	}
	public String getFoodSecuritySupplier() {
		return this.foodSecuritySupplier;
	}

	public void setFreightPayer(String freightPayer) {
		this.freightPayer = freightPayer;
	}
	public String getFreightPayer() {
		return this.freightPayer;
	}

	public void setGlobalStockType(String globalStockType) {
		this.globalStockType = globalStockType;
	}
	public String getGlobalStockType() {
		return this.globalStockType;
	}

	public void setHasDiscount(Boolean hasDiscount) {
		this.hasDiscount = hasDiscount;
	}
	public Boolean getHasDiscount() {
		return this.hasDiscount;
	}

	public void setHasInvoice(Boolean hasInvoice) {
		this.hasInvoice = hasInvoice;
	}
	public Boolean getHasInvoice() {
		return this.hasInvoice;
	}

	public void setHasShowcase(Boolean hasShowcase) {
		this.hasShowcase = hasShowcase;
	}
	public Boolean getHasShowcase() {
		return this.hasShowcase;
	}

	public void setHasWarranty(Boolean hasWarranty) {
		this.hasWarranty = hasWarranty;
	}
	public Boolean getHasWarranty() {
		return this.hasWarranty;
	}

	public void setImage(FileItem image) {
		this.image = image;
	}
	public FileItem getImage() {
		return this.image;
	}

	public void setIncrement(String increment) {
		this.increment = increment;
	}
	public String getIncrement() {
		return this.increment;
	}

	public void setInputPids(String inputPids) {
		this.inputPids = inputPids;
	}
	public String getInputPids() {
		return this.inputPids;
	}

	public void setInputStr(String inputStr) {
		this.inputStr = inputStr;
	}
	public String getInputStr() {
		return this.inputStr;
	}

	public void setIs3D(Boolean is3D) {
		this.is3D = is3D;
	}
	public Boolean getIs3D() {
		return this.is3D;
	}

	public void setIsEx(Boolean isEx) {
		this.isEx = isEx;
	}
	public Boolean getIsEx() {
		return this.isEx;
	}

	public void setIsLightningConsignment(Boolean isLightningConsignment) {
		this.isLightningConsignment = isLightningConsignment;
	}
	public Boolean getIsLightningConsignment() {
		return this.isLightningConsignment;
	}

	public void setIsReplaceSku(Boolean isReplaceSku) {
		this.isReplaceSku = isReplaceSku;
	}
	public Boolean getIsReplaceSku() {
		return this.isReplaceSku;
	}

	public void setIsTaobao(Boolean isTaobao) {
		this.isTaobao = isTaobao;
	}
	public Boolean getIsTaobao() {
		return this.isTaobao;
	}

	public void setIsXinpin(Boolean isXinpin) {
		this.isXinpin = isXinpin;
	}
	public Boolean getIsXinpin() {
		return this.isXinpin;
	}

	public void setItemSize(String itemSize) {
		this.itemSize = itemSize;
	}
	public String getItemSize() {
		return this.itemSize;
	}

	public void setItemWeight(String itemWeight) {
		this.itemWeight = itemWeight;
	}
	public String getItemWeight() {
		return this.itemWeight;
	}

	public void setLang(String lang) {
		this.lang = lang;
	}
	public String getLang() {
		return this.lang;
	}

	public void setListTime(Date listTime) {
		this.listTime = listTime;
	}
	public Date getListTime() {
		return this.listTime;
	}

	public void setLocalityLifeChooseLogis(String localityLifeChooseLogis) {
		this.localityLifeChooseLogis = localityLifeChooseLogis;
	}
	public String getLocalityLifeChooseLogis() {
		return this.localityLifeChooseLogis;
	}

	public void setLocalityLifeExpirydate(String localityLifeExpirydate) {
		this.localityLifeExpirydate = localityLifeExpirydate;
	}
	public String getLocalityLifeExpirydate() {
		return this.localityLifeExpirydate;
	}

	public void setLocalityLifeMerchant(String localityLifeMerchant) {
		this.localityLifeMerchant = localityLifeMerchant;
	}
	public String getLocalityLifeMerchant() {
		return this.localityLifeMerchant;
	}

	public void setLocalityLifeNetworkId(String localityLifeNetworkId) {
		this.localityLifeNetworkId = localityLifeNetworkId;
	}
	public String getLocalityLifeNetworkId() {
		return this.localityLifeNetworkId;
	}

	public void setLocalityLifeRefundRatio(Long localityLifeRefundRatio) {
		this.localityLifeRefundRatio = localityLifeRefundRatio;
	}
	public Long getLocalityLifeRefundRatio() {
		return this.localityLifeRefundRatio;
	}

	public void setLocalityLifeVerification(String localityLifeVerification) {
		this.localityLifeVerification = localityLifeVerification;
	}
	public String getLocalityLifeVerification() {
		return this.localityLifeVerification;
	}

	public void setLocationCity(String locationCity) {
		this.locationCity = locationCity;
	}
	public String getLocationCity() {
		return this.locationCity;
	}

	public void setLocationState(String locationState) {
		this.locationState = locationState;
	}
	public String getLocationState() {
		return this.locationState;
	}

	public void setNum(Long num) {
		this.num = num;
	}
	public Long getNum() {
		return this.num;
	}

	public void setNumIid(Long numIid) {
		this.numIid = numIid;
	}
	public Long getNumIid() {
		return this.numIid;
	}

	public void setOuterId(String outerId) {
		this.outerId = outerId;
	}
	public String getOuterId() {
		return this.outerId;
	}

	public void setPicPath(String picPath) {
		this.picPath = picPath;
	}
	public String getPicPath() {
		return this.picPath;
	}

	public void setPostFee(String postFee) {
		this.postFee = postFee;
	}
	public String getPostFee() {
		return this.postFee;
	}

	public void setPostageId(Long postageId) {
		this.postageId = postageId;
	}
	public Long getPostageId() {
		return this.postageId;
	}

	public void setPrice(String price) {
		this.price = price;
	}
	public String getPrice() {
		return this.price;
	}

	public void setProductId(Long productId) {
		this.productId = productId;
	}
	public Long getProductId() {
		return this.productId;
	}

	public void setPropertyAlias(String propertyAlias) {
		this.propertyAlias = propertyAlias;
	}
	public String getPropertyAlias() {
		return this.propertyAlias;
	}

	public void setProps(String props) {
		this.props = props;
	}
	public String getProps() {
		return this.props;
	}

	public void setScenicTicketBookCost(String scenicTicketBookCost) {
		this.scenicTicketBookCost = scenicTicketBookCost;
	}
	public String getScenicTicketBookCost() {
		return this.scenicTicketBookCost;
	}

	public void setScenicTicketPayWay(Long scenicTicketPayWay) {
		this.scenicTicketPayWay = scenicTicketPayWay;
	}
	public Long getScenicTicketPayWay() {
		return this.scenicTicketPayWay;
	}

	public void setSellPromise(Boolean sellPromise) {
		this.sellPromise = sellPromise;
	}
	public Boolean getSellPromise() {
		return this.sellPromise;
	}

	public void setSellerCids(String sellerCids) {
		this.sellerCids = sellerCids;
	}
	public String getSellerCids() {
		return this.sellerCids;
	}

	public void setSkuOuterIds(String skuOuterIds) {
		this.skuOuterIds = skuOuterIds;
	}
	public String getSkuOuterIds() {
		return this.skuOuterIds;
	}

	public void setSkuPrices(String skuPrices) {
		this.skuPrices = skuPrices;
	}
	public String getSkuPrices() {
		return this.skuPrices;
	}

	public void setSkuProperties(String skuProperties) {
		this.skuProperties = skuProperties;
	}
	public String getSkuProperties() {
		return this.skuProperties;
	}

	public void setSkuQuantities(String skuQuantities) {
		this.skuQuantities = skuQuantities;
	}
	public String getSkuQuantities() {
		return this.skuQuantities;
	}

	public void setStuffStatus(String stuffStatus) {
		this.stuffStatus = stuffStatus;
	}
	public String getStuffStatus() {
		return this.stuffStatus;
	}

	public void setSubStock(Long subStock) {
		this.subStock = subStock;
	}
	public Long getSubStock() {
		return this.subStock;
	}

	public void setTitle(String title) {
		this.title = title;
	}
	public String getTitle() {
		return this.title;
	}

	public void setValidThru(Long validThru) {
		this.validThru = validThru;
	}
	public Long getValidThru() {
		return this.validThru;
	}

	public void setWeight(Long weight) {
		this.weight = weight;
	}
	public Long getWeight() {
		return this.weight;
	}
	private Map<String,String> headerMap=new TaobaoHashMap();
	
	public Long getTimestamp() {
		return this.timestamp;
	}

	public void setTimestamp(Long timestamp) {
		this.timestamp = timestamp;
	}

	public String getApiMethodName() {
		return "taobao.item.update";
	}

	public Map<String, String> getTextParams() {		
		TaobaoHashMap txtParams = new TaobaoHashMap();
		txtParams.put("after_sale_id", this.afterSaleId);
		txtParams.put("approve_status", this.approveStatus);
		txtParams.put("auction_point", this.auctionPoint);
		txtParams.put("auto_fill", this.autoFill);
		txtParams.put("cid", this.cid);
		txtParams.put("cod_postage_id", this.codPostageId);
		txtParams.put("desc", this.desc);
		txtParams.put("empty_fields", this.emptyFields);
		txtParams.put("ems_fee", this.emsFee);
		txtParams.put("express_fee", this.expressFee);
		txtParams.put("food_security.contact", this.foodSecurityContact);
		txtParams.put("food_security.design_code", this.foodSecurityDesignCode);
		txtParams.put("food_security.factory", this.foodSecurityFactory);
		txtParams.put("food_security.factory_site", this.foodSecurityFactorySite);
		txtParams.put("food_security.food_additive", this.foodSecurityFoodAdditive);
		txtParams.put("food_security.mix", this.foodSecurityMix);
		txtParams.put("food_security.period", this.foodSecurityPeriod);
		txtParams.put("food_security.plan_storage", this.foodSecurityPlanStorage);
		txtParams.put("food_security.prd_license_no", this.foodSecurityPrdLicenseNo);
		txtParams.put("food_security.product_date_end", this.foodSecurityProductDateEnd);
		txtParams.put("food_security.product_date_start", this.foodSecurityProductDateStart);
		txtParams.put("food_security.stock_date_end", this.foodSecurityStockDateEnd);
		txtParams.put("food_security.stock_date_start", this.foodSecurityStockDateStart);
		txtParams.put("food_security.supplier", this.foodSecuritySupplier);
		txtParams.put("freight_payer", this.freightPayer);
		txtParams.put("global_stock_type", this.globalStockType);
		txtParams.put("has_discount", this.hasDiscount);
		txtParams.put("has_invoice", this.hasInvoice);
		txtParams.put("has_showcase", this.hasShowcase);
		txtParams.put("has_warranty", this.hasWarranty);
		txtParams.put("increment", this.increment);
		txtParams.put("input_pids", this.inputPids);
		txtParams.put("input_str", this.inputStr);
		txtParams.put("is_3D", this.is3D);
		txtParams.put("is_ex", this.isEx);
		txtParams.put("is_lightning_consignment", this.isLightningConsignment);
		txtParams.put("is_replace_sku", this.isReplaceSku);
		txtParams.put("is_taobao", this.isTaobao);
		txtParams.put("is_xinpin", this.isXinpin);
		txtParams.put("item_size", this.itemSize);
		txtParams.put("item_weight", this.itemWeight);
		txtParams.put("lang", this.lang);
		txtParams.put("list_time", this.listTime);
		txtParams.put("locality_life.choose_logis", this.localityLifeChooseLogis);
		txtParams.put("locality_life.expirydate", this.localityLifeExpirydate);
		txtParams.put("locality_life.merchant", this.localityLifeMerchant);
		txtParams.put("locality_life.network_id", this.localityLifeNetworkId);
		txtParams.put("locality_life.refund_ratio", this.localityLifeRefundRatio);
		txtParams.put("locality_life.verification", this.localityLifeVerification);
		txtParams.put("location.city", this.locationCity);
		txtParams.put("location.state", this.locationState);
		txtParams.put("num", this.num);
		txtParams.put("num_iid", this.numIid);
		txtParams.put("outer_id", this.outerId);
		txtParams.put("pic_path", this.picPath);
		txtParams.put("post_fee", this.postFee);
		txtParams.put("postage_id", this.postageId);
		txtParams.put("price", this.price);
		txtParams.put("product_id", this.productId);
		txtParams.put("property_alias", this.propertyAlias);
		txtParams.put("props", this.props);
		txtParams.put("scenic_ticket_book_cost", this.scenicTicketBookCost);
		txtParams.put("scenic_ticket_pay_way", this.scenicTicketPayWay);
		txtParams.put("sell_promise", this.sellPromise);
		txtParams.put("seller_cids", this.sellerCids);
		txtParams.put("sku_outer_ids", this.skuOuterIds);
		txtParams.put("sku_prices", this.skuPrices);
		txtParams.put("sku_properties", this.skuProperties);
		txtParams.put("sku_quantities", this.skuQuantities);
		txtParams.put("stuff_status", this.stuffStatus);
		txtParams.put("sub_stock", this.subStock);
		txtParams.put("title", this.title);
		txtParams.put("valid_thru", this.validThru);
		txtParams.put("weight", this.weight);
		if(this.udfParams != null) {
			txtParams.putAll(this.udfParams);
		}
		return txtParams;
	}

	public void putOtherTextParam(String key, String value) {
		if(this.udfParams == null) {
			this.udfParams = new TaobaoHashMap();
		}
		this.udfParams.put(key, value);
	}

	public Map<String, FileItem> getFileParams() {
		Map<String, FileItem> params = new HashMap<String, FileItem>();
		params.put("image", this.image);
		return params;
	}

	public Class<ItemUpdateResponse> getResponseClass() {
		return ItemUpdateResponse.class;
	}

	public void check() throws ApiRuleException {
		
		RequestCheckUtils.checkMinValue(cid,0L,"cid");
		RequestCheckUtils.checkMaxLength(desc,200000,"desc");
		RequestCheckUtils.checkMaxLength(image,524288,"image");
		RequestCheckUtils.checkMaxValue(num,999999L,"num");
		RequestCheckUtils.checkMinValue(num,0L,"num");
		RequestCheckUtils.checkNotEmpty(numIid,"numIid");
		RequestCheckUtils.checkMinValue(numIid,1L,"numIid");
		RequestCheckUtils.checkMaxListSize(sellerCids,10,"sellerCids");
		RequestCheckUtils.checkMaxLength(title,60,"title");
	}
	
	public Map<String,String> getHeaderMap() {
		return headerMap;
	}
}
