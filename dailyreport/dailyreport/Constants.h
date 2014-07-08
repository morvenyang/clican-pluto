//
//  Constants.h
//  dailyreport
//
//  Created by zhang wei on 14-5-17.
//  Copyright (c) 2014年 Peace Bird. All rights reserved.
//

#ifndef dailyreport_Constants_h
#define dailyreport_Constants_h
#define BASE_URL @"http://192.168.1.100:9000/peacebird"
#define LAST_USER_NAME @"LAST_USER_NAME"
#define LAST_PASSWORD @"LAST_PASSWORD"
#define LAST_LOGIN_DATE @"LAST_LOGIN_DATE"
#define FIRST_ACCESS_VERSION @"FIRST_ACCESS_VERSION"
#define DEVICE_TOKEN @"DEVICE_TOKEN"
#define VERSION [[[NSBundle mainBundle] infoDictionary] objectForKey:@"CFBundleVersion"]
#define DEVICE_VERSION [[[UIDevice currentDevice] systemVersion] doubleValue]
#define ALIGN_LEFT 1
#define ALIGN_CENTER 2
#define ALIGN_RIGHT 3

#define TAB_COLOR @"#8f8f8f"
#define STORE_RANK_TABLE_HEAD_COLOR @"#bdbdbd"
#define GESTURE_PASSWORD @"GESTURE_PASSWORD"
#endif
