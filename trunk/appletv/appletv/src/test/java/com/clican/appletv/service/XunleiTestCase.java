package com.clican.appletv.service;

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
		Map<String, String> header = new HashMap<String, String>();
		 header.put("Cookie",
		 "KANKANWEBUID=cc007136178182505835004214626600; pgv_pvi=7034767790; VERIFY_KEY=403580F4E9704C4E58803D5B360632AE; check_result=0:!83G; active=0; blogresult=0; downbyte=746968936026; downfile=1401; isspwd=0; jumpkey=8670FCFC6D6C258573228A776251FB5DD7CF39488CF6EA2F8346DE076F91ABC812D4AADFEAA303E4715A033543A233BC18FE3D6DA5D0DB9C57E7E10145702E58581AF420DF4DA0289EAD9C538324D098; logintype=0; lsessionid=75F30341DD84F450A07B5F048941BDA797C147038E10AF59E2CD578F86B75F1759D142410D8AA78F6B8D6B66F4FF84662C652160490961DA007AE4AF95959D1B; luserid=5663595; nickname=clican; onlinetime=12500930; order=2118015; safe=0; score=23556; sessionid=C2B77244AF1AEE37702C0D02584CBF9E122A4028928C056EA9330CB212EF85D6F829993E332D5A1C6F8C577912792360C116ED29A1F84D1FF80F3CF6214BC8FA; sex=m; upgrade=0; usernewno=0; usernick=clican; usertype=0; usrname=clican; userid=5663595; isvip=6; Hm_lvt_cb40dd55b713d4ff8da1d8e032c83cd4=1361360156,1361784992,1361845956,1361849412; Hm_lpvt_cb40dd55b713d4ff8da1d8e032c83cd4=1361854317");
		String listurl = "http://i.vod.xunlei.com/req_history_play_list/req_num/30/req_offset/0?type=all&order=create&t="+Calendar.getInstance().getTimeInMillis();
		PostResponse pr1 = tudouClient.httpGetForCookie(listurl, header, null);
		System.out.println(pr1.getContent());
		String btid = "3FE8791AE28A02D7CE9E6839019E2CC612C568FF";
		String infourl1 = "http://i.vod.xunlei.com/req_subBT/info_hash/"+btid+"/req_num/2/req_offset/0";
		String infourl2 = "http://i.vod.xunlei.com/req_screenshot?req_list=6A37AA8C07BB65F17EEA25DEFCA5E4ECB9DE5304%2F4FA4E7CB8D59155E3EBA751C4F1454F1F74D3EC9&t=1361853340067";
		PostResponse pr2 = tudouClient.httpGetForCookie(infourl1, header, null);
		System.out.println(pr2.getContent());
		String userid= "5663595";
		String filename = "%E8%A1%8C%E5%B0%B8%E8%B5%B0%E8%82%89.The.Walking.Dead.S03E09.Chi_Eng.WEB-HR.AAC.1024X576.x264-YYeTs%E4%BA%BA%E4%BA%BA%E5%BD%B1%E8%A7%86.mkv";
		String gcid = "73D2977DC13E0314439355D6326831D45AA858DF";
		String dwonloadListUrl = "http://i.vod.xunlei.com/vod_dl_all?userid="+userid+"&gcid="+gcid+"&filename="+filename+"&t="+Calendar.getInstance().getTimeInMillis();
		PostResponse pr3 = tudouClient.httpGetForCookie(dwonloadListUrl, header, null);
		System.out.println(pr3.getContent());
		String downloadUrl = "http://i.vod.xunlei.com/req_get_method_vod?url=bt%3A%2F%2F3FE8791AE28A02D7CE9E6839019E2CC612C568FF%2F0&video_name=325998%40%E8%8D%89%E6%A6%B4%E7%A4%BE%E5%8D%80%40SMHDBD-02%20S%20Model%20HDBD%2002%20%E5%8F%88%E5%8F%AF%E6%84%9B%E5%8F%88%E6%83%B9%E4%BA%BA%E6%84%9B%E6%86%90%E7%9A%84%E6%9E%81%E5%93%81%E8%90%9D%E8%8E%89Miku%20Airi%20%E6%B7%AB%E4%BA%82%E6%9C%AC%E6%80%A7%E5%A4%A7%E7%88%86%E7%99%BC&platform=1&userid=5663595&vip=1&sessionid=C2B77244AF1AEE37702C0D02584CBF9E122A4028928C056EA9330CB212EF85D6F829993E332D5A1C6F8C577912792360C116ED29A1F84D1FF80F3CF6214BC8FA&cache=1361854457067&from=vlist&jsonp=XL_CLOUD_FX_INSTANCEqueryBack";
		header.put("Referer", "http://61.147.76.6/iplay.html");
		PostResponse pr4 = tudouClient.httpGetForCookie(downloadUrl, header, null);
		System.out.println(pr4.getContent());
	}
}
