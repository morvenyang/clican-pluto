//
//  Constants.h
//  appletvserver
//
//  Created by zhang wei on 13-2-12.
//  Copyright (c) 2013年 __MyCompanyName__. All rights reserved.
//

#ifndef appletvserver_Constants_h
#define appletvserver_Constants_h

#define QQ_CHANNEL_URL @"http://sns.video.qq.com/fcgi-bin/dlib/dataout?sort=2&iarea=-1&itype=-1&iyear=-1&iedition=-1&pagesize=30&itrailer=-1&otype=json&version=20000&qq=&appver=2.0.0.2208&sysver=ios5.1.1&device=iPad&lang=zh_CN&timeout=0&page=%i&auto_id=%i&platform=%i"
#define QQ_SEARCH_URL @"http://ncgi.video.qq.com/tvideo/fcgi-bin/srh_ipad?num=30&tabid=0&plat=4&pver=2&sort=0&filter=18&otype=json&qq=&appver=2.0.0.2208&sysver=ios5.1.1&device=iPad&lang=zh_CN&comment=%i&cur=%i&query=%@"
#define QQ_VIDEO_URL @"http://live.qq.com/json/ipad/cover/%@/%@.json?qq=&appver=2.0.0.2208&sysver=ios5.1.1&device=iPad&lang=zh_CN"
#define QQ_PLAY_API @"http://vv.video.qq.com/geturl?otype=json&callback=a&qq=&appver=2.0.0.2208&sysver=ios5.1.1&device=iPad&lang=zh_CN&vid=%@"
#define WEB_CONTENT_SYNC_API @"http://www.clican.org/appletv/ctl/proxy/sync.zip"
#define WEB_CONTENT_SYNC_VERSION_API @"http://www.clican.org/appletv/ctl/proxy/sync/version.do"
#define MP4_PARTIAL_LENGTH 1048510


typedef enum {
    QQ_Recommand = 3,
    QQ_Search = 1001,
    QQ_DianShiJu = 15,
    QQ_DianYing = 14,
    QQ_DongMan = 16,
    QQ_ZongYi = 17,
    QQ_XinWen = 22,
    QQ_DianShiZhiBo = 24,
    QQ_MeiJu = 48,
    QQ_YuLe = 21,
    QQ_WeiJiangTang = 37,
    QQ_TiYu = 20,
    QQ_JiLuPian = 19
} QQChannel;

#endif
