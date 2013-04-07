package com.clican.appletv.service;

import java.net.URLEncoder;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;

import org.apache.commons.codec.binary.Base64;

import com.clican.appletv.common.PostResponse;
import com.clican.appletv.core.service.tudou.TudouClientImpl;

public class XunleiTestCase extends BaseServiceTestCase {

	private TudouClientImpl tudouClient;

	public void setTudouClient(TudouClientImpl tudouClient) {
		this.tudouClient = tudouClient;
	}

	public void testDownloadWithXunleiSessionid() throws Exception {
		// Map<String, String> header = new HashMap<String, String>();
		// header.put("Cookie",
		// "sessionid=C2B77244AF1AEE37702C0D02584CBF9E122A4028928C056EA9330CB212EF85D6F829993E332D5A1C6F8C577912792360C116ED29A1F84D1FF80F3CF6214BC8FA;userid=5663595;");
		// String listurl =
		// "http://i.vod.xunlei.com/req_history_play_list/req_num/30/req_offset/0?type=all&order=create&t="+Calendar.getInstance().getTimeInMillis();
		// PostResponse pr1 = tudouClient.httpGetForCookie(listurl, header,
		// null);
		// System.out.println(pr1.getContent());
		// String btid = "3FE8791AE28A02D7CE9E6839019E2CC612C568FF";
		// String infourl1 =
		// "http://i.vod.xunlei.com/req_subBT/info_hash/"+btid+"/req_num/2/req_offset/0";
		// String infourl2 =
		// "http://i.vod.xunlei.com/req_screenshot?req_list=6A37AA8C07BB65F17EEA25DEFCA5E4ECB9DE5304%2F4FA4E7CB8D59155E3EBA751C4F1454F1F74D3EC9&t=1361853340067";
		// PostResponse pr2 = tudouClient.httpGetForCookie(infourl1, header,
		// null);
		// System.out.println(pr2.getContent());
		// String userid= "5663595";
		// String filename =
		// "%E8%A1%8C%E5%B0%B8%E8%B5%B0%E8%82%89.The.Walking.Dead.S03E09.Chi_Eng.WEB-HR.AAC.1024X576.x264-YYeTs%E4%BA%BA%E4%BA%BA%E5%BD%B1%E8%A7%86.mkv";
		// String gcid = "73D2977DC13E0314439355D6326831D45AA858DF";
		// String dwonloadListUrl =
		// "http://i.vod.xunlei.com/vod_dl_all?userid="+userid+"&gcid="+gcid+"&filename="+filename+"&t="+Calendar.getInstance().getTimeInMillis();
		// PostResponse pr3 = tudouClient.httpGetForCookie(dwonloadListUrl,
		// header, null);
		// System.out.println(pr3.getContent());
		// String downloadUrl =
		// "http://i.vod.xunlei.com/req_get_method_vod?url=bt%3A%2F%2F3FE8791AE28A02D7CE9E6839019E2CC612C568FF%2F0&video_name=325998%40%E8%8D%89%E6%A6%B4%E7%A4%BE%E5%8D%80%40SMHDBD-02%20S%20Model%20HDBD%2002%20%E5%8F%88%E5%8F%AF%E6%84%9B%E5%8F%88%E6%83%B9%E4%BA%BA%E6%84%9B%E6%86%90%E7%9A%84%E6%9E%81%E5%93%81%E8%90%9D%E8%8E%89Miku%20Airi%20%E6%B7%AB%E4%BA%82%E6%9C%AC%E6%80%A7%E5%A4%A7%E7%88%86%E7%99%BC&platform=1&userid=5663595&vip=1&sessionid=C2B77244AF1AEE37702C0D02584CBF9E122A4028928C056EA9330CB212EF85D6F829993E332D5A1C6F8C577912792360C116ED29A1F84D1FF80F3CF6214BC8FA&cache=1361854457067&from=vlist&jsonp=XL_CLOUD_FX_INSTANCEqueryBack";
		// header.put("Referer", "http://61.147.76.6/iplay.html");
		// PostResponse pr4 = tudouClient.httpGetForCookie(downloadUrl, header,
		// null);
		// System.out.println(pr4.getContent());
		String mp4Url = "http://vhoth.dnion.videocdn.qq.com/flv/18/185/p00116ducjk.mp4?vkey=CF0D8BBF9F011AC92CD418D2DD1993AA112C1AF6EC6AF9C748B17E0BDFA3A1314273F9C0905D2AF1&br=72&platform=0&fmt=mp4&level=1";
		String mkvUrl = "http://vod30.t19.lixian.vip.xunlei.com:443/download?fid=emlPgw9jX5lBcmGnVJ4RgeXO+Hxq6VFcAAAAANuBvkGCNBSzd4lE4fk1okftXEb7&mid=666&threshold=150&tid=EDCDE1299DC6E43F04EFF63FACBBE88D&srcid=4&verno=1&g=DB81BE41823414B3778944E1F935A247ED5C46FB&scn=t9&i=817119D8471F0BCC3DCFDF0712D5CD4B&t=4&ui=5663595&ti=161029492610&s=1548872042&m=0&n=011559817177616C6B085F8371646561644F42D46C6531362E5603D42F2E686474171F9C6D36342D320955CA326B760000&ff=0&co=909A5217E21D287776E49561158F5D2B&cm=1&ts=1365205639";
		String test4 = "http://16.158.169.15:8080/appletv/proxy/mp4?url="
				+ URLEncoder.encode(mp4Url, "utf-8");
		String m3u8Url1 = "http://devimages.apple.com/iphone/samples/bipbop/gear1/prog_index.m3u8";
		String m3u8Url2 = "http://devimages.apple.com/iphone/samples/bipbop/gear3/prog_index.m3u8";
		String m3u8Url3 = "http://gdl.lixian.vip.xunlei.com/download?dt=16&g=FCA52C0D243D5E306462E7A8AD3BE8ABF1BDCEEF&t=2&ui=5663595&s=197116809&v_type=-1&scn=t3&it=1362055588&cc=11745147014848014333&p=1&n=048AACF5E179380000&p=1&xplaybackid=f5999992-817a-11e2-a098-842b2b62740d";
		String test1 = "http://16.158.169.15:8080/appletv/noctl/proxy/play.m3u8?url="
				+ URLEncoder.encode(m3u8Url1, "utf-8");
		String test2 = "http://16.158.169.15:8080/appletv/noctl/proxy/play.m3u8?url="
				+ URLEncoder.encode(m3u8Url2, "utf-8");
		String test3 = "http://16.158.169.15:8080/appletv/noctl/proxy/play.m3u8?url="
				+ URLEncoder.encode(m3u8Url3, "utf-8");
		String test5 = "http://16.158.169.15:8080/appletv/noctl/mkv/play.m3u8?url="
			+ URLEncoder.encode(mkvUrl, "utf-8");
		System.out.println(tudouClient.httpGet(m3u8Url1));
		System.out.println(test1);
		System.out.println(test2);
		System.out.println(test3);
		System.out.println(test4);
		System.out.println(test5);
		// PostResponse pr5 = tudouClient.httpGetForCookie(test, null, null);
		// System.out.println(pr5.getContent());
	}

	public void testPlay() throws Exception {
		String url = "ed2k://|file|%E8%A1%8C%E5%B0%B8%E8%B5%B0%E8%82%89.The.Walking.Dead.S03E13.Chi_Eng.HDTVrip.720X400.mp4|203034532|CD40755A174FE5EA525208DA18DD3333|h=FCU3MZ65SO2FDBVQZ2IQPKG2TJJHKFBY|/";
		String userid = "5663595";
		String sessionid = "75F30341DD84F450A07B5F048941BDA785F6AC3A12F9A04ADD949849CD339CC26B882B5F062969E7F71BF99995D9719814CB2E481F15545E89D85D6161F9649A";
		String xunleiurl = "http://i.vod.xunlei.com/req_get_method_vod?url="
				+ URLEncoder.encode(url, "utf-8")
				+ "&video_name=%E8%A1%8C%E5%B0%B8%E8%B5%B0%E8%82%89.The.Walking.Dead.S03E13.Chi_Eng.HDTVrip.720X400.mp4"
				+ "&platform=1&userid=" + userid + "&vip=6&sessionid="
				+ sessionid + "&cache="
				+ Calendar.getInstance().getTimeInMillis()
				+ "&from=vlist&jsonp=xunleiClient.xunleicallback";
		String refer = "http://61.147.76.6/iplay.html";
		Map<String, String> header = new HashMap<String, String>();
		header.put("Referer", refer);
		PostResponse pr = tudouClient.httpGetForCookie(xunleiurl, header, null);
		System.out.println(pr.getContent());
	}

	public void testDecodeURL() throws Exception{
		String s = new String(Base64.decodeBase64("QUFlZDJrOi8vfGZpbGV8JUU4JUExJThDJUU1JUIwJUI4JUU4JUI1JUIwJUU4JTgyJTg5LlRoZS5XYWxraW5nLkRlYWQuJUU3JUFDJUFDJUU0JUI4JTg5JUU1JUFEJUEzJUU1JUFFJTk4JUU2JTk2JUI5JUU3JTg5JTg4JUU5JUEyJTg0JUU1JTkxJThBLVlZZVRzJUU0JUJBJUJBJUU0JUJBJUJBJUU1JUJEJUIxJUU4JUE3JTg2JUU1JThFJTlGJUU1JTg4JTlCJUU3JUJGJUJCJUU4JUFGJTkxLnJtdmJ8MTYwMjYyMjd8ZTk5OTVmMDYwZjBlOWM3YmU2OTEyZDlkNDQ3NDk5YmJ8aD1pM2tobG95aWZxeHdreGV1c3hsbmFndGtiM3BxeWxrenwvWlo=".getBytes("utf-8")),"utf-8");
		System.out.println(s);
	}
	
	public void decodeBase64() throws Exception{
		String s1 = "eW91a3VDbGllbnQubG9hZFZpZGVvUGFnZSgnWE5EZzVNak15TlRnMCcsOTYsZmFsc2UsJ2h0dHA6Ly9yZXMubWZzLnlraW1nLmNvbS8wNTBFMDAwMDUwRDAzOEIyOTc5MjczNjY2MzBFNzg1NCcpOw==";
		String s2 = "eW91a3VDbGllbnQubG9hZFZpZGVvUGFnZSgnemY5MmMyMDljNDhmNTExZTJiMmFjJyw5NixmYWxzZSwnaHR0cDovL3Jlcy5tZnMueWtpbWcuY29tLzA1MEUwMDAwNTBEMDM4QjI5NzkyNzM2NjYzMEU3ODU0Jyk7";
		System.out.println(new String(Base64.decodeBase64(s1.getBytes("utf-8")),"utf-8"));
		System.out.println(new String(Base64.decodeBase64(s2.getBytes("utf-8")),"utf-8"));
	}
	
	public void testOfflineDownload() throws Exception{
		Map<String, String> header = new HashMap<String, String>();
		header.put("Cookie", "gdriveid=08D39F59B366F371195050D992B72FD2;");
		String url ="http://vod30.t19.lixian.vip.xunlei.com:443/download?fid=emlPgw9jX5lBcmGnVJ4RgeXO+Hxq6VFcAAAAANuBvkGCNBSzd4lE4fk1okftXEb7&mid=666&threshold=150&tid=EDCDE1299DC6E43F04EFF63FACBBE88D&srcid=4&verno=1&g=DB81BE41823414B3778944E1F935A247ED5C46FB&scn=t9&i=817119D8471F0BCC3DCFDF0712D5CD4B&t=4&ui=5663595&ti=161029492610&s=1548872042&m=0&n=011559817177616C6B085F8371646561644F42D46C6531362E5603D42F2E686474171F9C6D36342D320955CA326B760000&ff=0&co=909A5217E21D287776E49561158F5D2B&cm=1&ts=1365205639";
		tudouClient.httpGetByData(url, header, null);
	}
	
	public void submitOfflineTask() throws Exception{
		
		Map<String, String> header = new HashMap<String, String>();
		//header.put("Cookie", "KANKANWEBUID=cc007136178182505835004214626600; pgv_pvi=7034767790; lsessionid=75F30341DD84F450A07B5F048941BDA741543D43DFE28DBA96FA9F60AF4974487886810DDFA6DBB571B29B00799E85C40C0EBD7F4351C869315ADEDABC0118B4; luserid=5663595; active=0; blogresult=0; downbyte=757879921644; downfile=1429; isspwd=0; jumpkey=8670FCFC6D6C258573228A776251FB5D0F5F7A06F1643C7B4F7BEFB15B519C13688F08A60DB76D590CEB01B72DF60E9321144FA43E1F4E0B70FFF975EA91BBFB4F6CA0CF0D6C0972D7825325F2752C99; logintype=1; nickname=clican; onlinetime=12569983; order=2133467; safe=0; score=23872; sessionid=75F30341DD84F450A07B5F048941BDA741543D43DFE28DBA96FA9F60AF4974487886810DDFA6DBB571B29B00799E85C40C0EBD7F4351C869315ADEDABC0118B4; sex=m; upgrade=0; userid=5663595; usernewno=0; usernick=clican; usertype=0; usrname=clican; lx_sessionid=75F30341DD84F450A07B5F048941BDA741543D43DFE28DBA96FA9F60AF4974487886810DDFA6DBB571B29B00799E85C40C0EBD7F4351C869315ADEDABC0118B4; isnewer_5663595=0; lx_login=5663595; initbg_pop5663595=0; vip_is_good_number=0; rw_list_open=1; queryTime=1; __xltjbr=1365301991096; isvip=6; in_xl=0; task_nowclick=161690361218; tgshownew=1; vip_isvip=1; vip_level=6; vip_paytype=5; vip_expiredate=2013-06-08; user_type=1; dl_enable=1; dl_size=1048589; dl_num=11; dl_expire=17; lx_nf_all=page_check_all%3Dcommtask%26class_check%3D0%26page_check%3Dtask%26fl_page_id%3D0%26class_check_new%3D0%26set_tab_status%3D4; gdriveid=08D39F59B366F371195050D992B72FD2; _xltj=34a1365305532887b6c; _s34=1366181032432b1365305258874b7bhttp%3A//dynamic.cloud.vip.xunlei.com/user_task%3Fuserid%3D5663595%26st%3D4%26cache%3DSun%2520Apr%252007%25202013%252011%3A31%3A50%2520GMT+0800%2520%28%25E4%25B8%25AD%25E5%259B%25BD%25E6%25A0%2587%25E5%2587%2586%25E6%2597%25B6%25E9%2597%25B4%29; Hm_lvt_b5b854a027ebb02e3b145ae3d58ab697=1365301993,1365305251; Hm_lpvt_b5b854a027ebb02e3b145ae3d58ab697=1365305521");
		header.put("Cookie", "userid=5663595; lx_sessionid=75F30341DD84F450A07B5F048941BDA741543D43DFE28DBA96FA9F60AF4974487886810DDFA6DBB571B29B00799E85C40C0EBD7F4351C869315ADEDABC0118B4;lx_login=5663595;gdriveid=08D39F59B366F371195050D992B72FD2;");
		String url1 = "http://dynamic.cloud.vip.xunlei.com/interface/task_check?callback=queryCid&url=ed2k%3A%2F%2F%7Cfile%7Ccrossbow.inception.720p.mkv%7C7034822735%7C596F80CF650C1401C216817DBD312D75%7Ch%3DSYREZHO5Y3VFTP3KGHHW5PQOP72OFORC%7C%2F&interfrom=task&random=1365305929759329104.26622072&tcache=1365305972636";
		System.out.println(tudouClient.httpGet(url1, header, null));
		String url2 = "http://dynamic.cloud.vip.xunlei.com/interface/task_commit?callback=ret_task&uid=5663595&cid=&gcid=&size=7034822735&goldbean=0&silverbean=0&t=crossbow.inception.720p.mkv&url=ed2k%3A%2F%2F%7Cfile%7Ccrossbow.inception.720p.mkv%7C7034822735%7C596F80CF650C1401C216817DBD312D75%7Ch%3DSYREZHO5Y3VFTP3KGHHW5PQOP72OFORC%7C%2F&type=2&o_page=history&o_taskid=0&class_id=0&database=undefined&interfrom=task&time=Sun%20Apr%2007%202013%2011:36:50%20GMT+0800%20(%E4%B8%AD%E5%9B%BD%E6%A0%87%E5%87%86%E6%97%B6%E9%97%B4)&noCacheIE=1365305810274";
		System.out.println(tudouClient.httpGet(url2, header, null));
		String url3 = "http://dynamic.cloud.vip.xunlei.com/user_task?userid=5663595";
		System.out.println(tudouClient.httpGet(url3, header, null));
	}
}
