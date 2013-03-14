package com.clican.appletv.service;

import java.net.URLEncoder;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;

import com.clican.appletv.common.PostResponse;
import com.clican.appletv.core.service.tudou.TudouClientImpl;

public class XunleiTestCase extends BaseServiceTestCase {

	private TudouClientImpl tudouClient;

	public void setTudouClient(TudouClientImpl tudouClient) {
		this.tudouClient = tudouClient;
	}

	public void testDownloadWithXunleiSessionid() throws Exception {
//		Map<String, String> header = new HashMap<String, String>();
//		 header.put("Cookie",
//		 "sessionid=C2B77244AF1AEE37702C0D02584CBF9E122A4028928C056EA9330CB212EF85D6F829993E332D5A1C6F8C577912792360C116ED29A1F84D1FF80F3CF6214BC8FA;userid=5663595;");
//		String listurl = "http://i.vod.xunlei.com/req_history_play_list/req_num/30/req_offset/0?type=all&order=create&t="+Calendar.getInstance().getTimeInMillis();
//		PostResponse pr1 = tudouClient.httpGetForCookie(listurl, header, null);
//		System.out.println(pr1.getContent());
//		String btid = "3FE8791AE28A02D7CE9E6839019E2CC612C568FF";
//		String infourl1 = "http://i.vod.xunlei.com/req_subBT/info_hash/"+btid+"/req_num/2/req_offset/0";
//		String infourl2 = "http://i.vod.xunlei.com/req_screenshot?req_list=6A37AA8C07BB65F17EEA25DEFCA5E4ECB9DE5304%2F4FA4E7CB8D59155E3EBA751C4F1454F1F74D3EC9&t=1361853340067";
//		PostResponse pr2 = tudouClient.httpGetForCookie(infourl1, header, null);
//		System.out.println(pr2.getContent());
//		String userid= "5663595";
//		String filename = "%E8%A1%8C%E5%B0%B8%E8%B5%B0%E8%82%89.The.Walking.Dead.S03E09.Chi_Eng.WEB-HR.AAC.1024X576.x264-YYeTs%E4%BA%BA%E4%BA%BA%E5%BD%B1%E8%A7%86.mkv";
//		String gcid = "73D2977DC13E0314439355D6326831D45AA858DF";
//		String dwonloadListUrl = "http://i.vod.xunlei.com/vod_dl_all?userid="+userid+"&gcid="+gcid+"&filename="+filename+"&t="+Calendar.getInstance().getTimeInMillis();
//		PostResponse pr3 = tudouClient.httpGetForCookie(dwonloadListUrl, header, null);
//		System.out.println(pr3.getContent());
//		String downloadUrl = "http://i.vod.xunlei.com/req_get_method_vod?url=bt%3A%2F%2F3FE8791AE28A02D7CE9E6839019E2CC612C568FF%2F0&video_name=325998%40%E8%8D%89%E6%A6%B4%E7%A4%BE%E5%8D%80%40SMHDBD-02%20S%20Model%20HDBD%2002%20%E5%8F%88%E5%8F%AF%E6%84%9B%E5%8F%88%E6%83%B9%E4%BA%BA%E6%84%9B%E6%86%90%E7%9A%84%E6%9E%81%E5%93%81%E8%90%9D%E8%8E%89Miku%20Airi%20%E6%B7%AB%E4%BA%82%E6%9C%AC%E6%80%A7%E5%A4%A7%E7%88%86%E7%99%BC&platform=1&userid=5663595&vip=1&sessionid=C2B77244AF1AEE37702C0D02584CBF9E122A4028928C056EA9330CB212EF85D6F829993E332D5A1C6F8C577912792360C116ED29A1F84D1FF80F3CF6214BC8FA&cache=1361854457067&from=vlist&jsonp=XL_CLOUD_FX_INSTANCEqueryBack";
//		header.put("Referer", "http://61.147.76.6/iplay.html");
//		PostResponse pr4 = tudouClient.httpGetForCookie(downloadUrl, header, null);
//		System.out.println(pr4.getContent());
		String mp4Url = "http://vhoth.dnion.videocdn.qq.com/flv/18/185/p00116ducjk.mp4?vkey=CF0D8BBF9F011AC92CD418D2DD1993AA112C1AF6EC6AF9C748B17E0BDFA3A1314273F9C0905D2AF1&br=72&platform=0&fmt=mp4&level=1";
		String test4 = "http://16.158.169.15:8080/appletv/proxy/mp4?url="+URLEncoder.encode(mp4Url,"utf-8");
		String m3u8Url1 = "http://devimages.apple.com/iphone/samples/bipbop/gear1/prog_index.m3u8";
		String m3u8Url2 = "http://devimages.apple.com/iphone/samples/bipbop/gear3/prog_index.m3u8";
		String m3u8Url3 = "http://gdl.lixian.vip.xunlei.com/download?dt=16&g=FCA52C0D243D5E306462E7A8AD3BE8ABF1BDCEEF&t=2&ui=5663595&s=197116809&v_type=-1&scn=t3&it=1362055588&cc=11745147014848014333&p=1&n=048AACF5E179380000&p=1&xplaybackid=f5999992-817a-11e2-a098-842b2b62740d";
		String test1 = "http://192.168.1.103:8080/appletv/proxy/m3u8?url="+URLEncoder.encode(m3u8Url1,"utf-8");
		String test2 = "http://192.168.1.103:8080/appletv/proxy/m3u8?url="+URLEncoder.encode(m3u8Url2,"utf-8");
		String test3 = "http://192.168.1.103:8080/appletv/proxy/m3u8?url="+URLEncoder.encode(m3u8Url3,"utf-8");
		System.out.println(test1);
		System.out.println(test2);
		System.out.println(test3);
		System.out.println(test4);
//		PostResponse pr5 = tudouClient.httpGetForCookie(test, null, null);
//		System.out.println(pr5.getContent());
	}
	
	public void testPlay() throws Exception{
		String url = "ed2k://|file|%E8%A1%8C%E5%B0%B8%E8%B5%B0%E8%82%89.The.Walking.Dead.S03E13.Chi_Eng.HDTVrip.720X400.mp4|203034532|CD40755A174FE5EA525208DA18DD3333|h=FCU3MZ65SO2FDBVQZ2IQPKG2TJJHKFBY|/";
		String userid="5663595";
		String sessionid = "75F30341DD84F450A07B5F048941BDA785F6AC3A12F9A04ADD949849CD339CC26B882B5F062969E7F71BF99995D9719814CB2E481F15545E89D85D6161F9649A";
		String xunleiurl = "http://i.vod.xunlei.com/req_get_method_vod?url="
		+ URLEncoder.encode(url,"utf-8") + "&video_name=%E8%A1%8C%E5%B0%B8%E8%B5%B0%E8%82%89.The.Walking.Dead.S03E13.Chi_Eng.HDTVrip.720X400.mp4" + "&platform=1&userid=" + userid
		+ "&vip=6&sessionid=" + sessionid
		+ "&cache=" + Calendar.getInstance().getTimeInMillis()
		+ "&from=vlist&jsonp=xunleiClient.xunleicallback";
		String refer = "http://61.147.76.6/iplay.html";
		Map<String,String> header = new HashMap<String,String>();
		header.put("Referer", refer);
		PostResponse pr = tudouClient.httpGetForCookie(xunleiurl, header, null);
		System.out.println(pr.getContent());
	}
}
