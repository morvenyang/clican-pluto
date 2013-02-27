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
		String m3u8Url = "http://gdl.lixian.vip.xunlei.com/download?dt=16&g=48BB0C352722E889E945BFCBAE251846CADECA9E&t=2&ui=5663595&s=587057068&v_type=-1&scn=t12&it=1361958204&cc=2044160738531415486&p=1&n=0C54EEFFE234470400&p=1&xplaybackid=3852e4c0-8098-11e2-aa95-842b2b62740d";
		String test = "http://16.158.169.15:8080/appletv/proxy/m3u8?url="+URLEncoder.encode(m3u8Url,"utf-8");
//		
		PostResponse pr5 = tudouClient.httpGetForCookie(test, null, null);
		System.out.println(pr5.getContent());
	}
}
