package com.taobao.api.request;

import com.taobao.api.internal.util.RequestCheckUtils;
import java.util.Map;

import com.taobao.api.TaobaoRequest;
import com.taobao.api.internal.util.TaobaoHashMap;
import com.taobao.api.response.TaobaokeItemsRelateGetResponse;
import com.taobao.api.ApiRuleException;
/**
 * TOP API: taobao.taobaoke.items.relate.get request
 * 
 * @author auto create
 * @since 1.0, 2013-01-24 12:41:47
 */
public class TaobaokeItemsRelateGetRequest implements TaobaoRequest<TaobaokeItemsRelateGetResponse> {

	private TaobaoHashMap udfParams; // add user-defined text parameters
	private Long timestamp;

	/** 
	* 分类id.推荐类型为5时cid不能为空。仅支持叶子类目ID，即通过taobao.itemcats.get获取到is_parent=false的cid。
	 */
	private Long cid;

	/** 
	* 需返回的字段列表.可选值:num_iid,title,nick,pic_url,price,click_url,commission,ommission_rate,commission_num,commission_volume,shop_click_url,seller_credit_score,item_location,volume;字段之间用","分隔
	 */
	private String fields;

	/** 
	* 标识一个应用是否来在无线或者手机应用,如果是true则会使用其他规则加密点击串.如果不传值,则默认是false
	 */
	private Boolean isMobile;

	/** 
	* 指定返回结果的最大条数.实际返回结果个数根据算法来确定,所以该值会小于或者等于该值
	 */
	private Long maxCount;

	/** 
	* 推广者的淘宝会员昵称.注:指的是淘宝的会员登录名
	 */
	private String nick;

	/** 
	* 淘宝客商品数字id.推荐类型为1,2,3时num_iid不能为空
	 */
	private Long numIid;

	/** 
	* 自定义输入串.格式:英文和数字组成;长度不能大于12个字符,区分不同的推广渠道,如:bbs,表示bbs为推广渠道;blog,表示blog为推广渠道
	 */
	private String outerCode;

	/** 
	* 用户的pid,必须是mm_xxxx_0_0这种格式中间的"xxxx". 注意nick和pid至少需要传递一个,如果2个都传了,将以pid为准,且pid的最大长度是20。第一次调用接口的用户，推荐该入参不要填写，使用nick=（淘宝账号）的方式去获取，以免出错。
	 */
	private Long pid;

	/** 
	* <p>推荐类型.</p>
<p>1:同类商品推荐;此时必须得输入num_iid</p>
<p>2:异类商品推荐;此时必须得输入num_iid</p>
<p>3:同店商品推荐;此时必须得输入num_iid</p>
<p>4:店铺热门推荐;此时必须得输入seller_id，这里的seller_id得通过<a href="http://api.taobao.com/apidoc/api.htm?path=cid:38-apiId:10449">taobao.taobaoke.shops.get</a>
跟<a href="http://api.taobao.com/apidoc/api.htm?path=cid:38-apiId:134">taobao.taobaoke.shops.convert</a>这两个接口去获取user_id字段</p>
<p>5:类目热门推荐;此时必须得输入cid</p>
	 */
	private Long relateType;

	/** 
	* 卖家的用户id，这里的seller_id得通过<a href="http://api.taobao.com/apidoc/api.htm?path=cid:38-apiId:10449">taobao.taobaoke.shops.get</a>
跟<a href="http://api.taobao.com/apidoc/api.htm?path=cid:38-apiId:134">taobao.taobaoke.shops.convert</a>这两个接口去获取user_id字段。
注：推荐类型为4时seller_id不能为空
	 */
	private Long sellerId;

	/** 
	* 店铺类型.默认all,商城:b,集市:c
	 */
	private String shopType;

	/** 
	* default(默认排序,关联推荐相关度),price_desc(价格从高到低), price_asc(价格从低到高),commissionRate_desc(佣金比率从高到低), commissionRate_asc(佣金比率从低到高), commissionNum_desc(成交量成高到低), commissionNum_asc(成交量从低到高)
	 */
	private String sort;

	/** 
	* 商品数字ID(带有跟踪效果)
	 */
	private String trackIid;

	public void setCid(Long cid) {
		this.cid = cid;
	}
	public Long getCid() {
		return this.cid;
	}

	public void setFields(String fields) {
		this.fields = fields;
	}
	public String getFields() {
		return this.fields;
	}

	public void setIsMobile(Boolean isMobile) {
		this.isMobile = isMobile;
	}
	public Boolean getIsMobile() {
		return this.isMobile;
	}

	public void setMaxCount(Long maxCount) {
		this.maxCount = maxCount;
	}
	public Long getMaxCount() {
		return this.maxCount;
	}

	public void setNick(String nick) {
		this.nick = nick;
	}
	public String getNick() {
		return this.nick;
	}

	public void setNumIid(Long numIid) {
		this.numIid = numIid;
	}
	public Long getNumIid() {
		return this.numIid;
	}

	public void setOuterCode(String outerCode) {
		this.outerCode = outerCode;
	}
	public String getOuterCode() {
		return this.outerCode;
	}

	public void setPid(Long pid) {
		this.pid = pid;
	}
	public Long getPid() {
		return this.pid;
	}

	public void setRelateType(Long relateType) {
		this.relateType = relateType;
	}
	public Long getRelateType() {
		return this.relateType;
	}

	public void setSellerId(Long sellerId) {
		this.sellerId = sellerId;
	}
	public Long getSellerId() {
		return this.sellerId;
	}

	public void setShopType(String shopType) {
		this.shopType = shopType;
	}
	public String getShopType() {
		return this.shopType;
	}

	public void setSort(String sort) {
		this.sort = sort;
	}
	public String getSort() {
		return this.sort;
	}

	public void setTrackIid(String trackIid) {
		this.trackIid = trackIid;
	}
	public String getTrackIid() {
		return this.trackIid;
	}
	private Map<String,String> headerMap=new TaobaoHashMap();
	
	public Long getTimestamp() {
		return this.timestamp;
	}

	public void setTimestamp(Long timestamp) {
		this.timestamp = timestamp;
	}

	public String getApiMethodName() {
		return "taobao.taobaoke.items.relate.get";
	}

	public Map<String, String> getTextParams() {		
		TaobaoHashMap txtParams = new TaobaoHashMap();
		txtParams.put("cid", this.cid);
		txtParams.put("fields", this.fields);
		txtParams.put("is_mobile", this.isMobile);
		txtParams.put("max_count", this.maxCount);
		txtParams.put("nick", this.nick);
		txtParams.put("num_iid", this.numIid);
		txtParams.put("outer_code", this.outerCode);
		txtParams.put("pid", this.pid);
		txtParams.put("relate_type", this.relateType);
		txtParams.put("seller_id", this.sellerId);
		txtParams.put("shop_type", this.shopType);
		txtParams.put("sort", this.sort);
		txtParams.put("track_iid", this.trackIid);
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

	public Class<TaobaokeItemsRelateGetResponse> getResponseClass() {
		return TaobaokeItemsRelateGetResponse.class;
	}

	public void check() throws ApiRuleException {
		
		RequestCheckUtils.checkNotEmpty(fields,"fields");
		RequestCheckUtils.checkNotEmpty(relateType,"relateType");
	}
	
	public Map<String,String> getHeaderMap() {
		return headerMap;
	}
}
