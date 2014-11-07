//
//  Constants.h
//  dailyreport
//
//  Created by zhang wei on 14-5-17.
//  Copyright (c) 2014年 Peace Bird. All rights reserved.
//

#ifndef HCMas_Constants_h
#define HCMas_Constants_h
#define LOGIN @"LOGIN"
#define BASE_URL_NAME @"BASE_URL_NAME"
#define SYSTEM_CONFIG @"SYSTEM_CONFIG"
#define UPDATE_FREQUENCY_NAME @"UPDATE_FREQUENCY_NAME"
#define PROJECT_NAME @"PROJECT_NAME"
#define POINT_NAME @"POINT_NAME"
#define LAST_USER_NAME @"LAST_USER_NAME"
#define LAST_PASSWORD @"LAST_PASSWORD"
#define REMEMBER_PASSWORD @"REMEMBER_PASSWORD"
#define LAST_LOGIN_DATE @"LAST_LOGIN_DATE"
#define FIRST_ACCESS_VERSION @"FIRST_ACCESS_VERSION"
#define DEVICE_TOKEN @"DEVICE_TOKEN"
#define COPY_RIGHT @"COPY_RIGHT"
#define APP_NAME @"APP_NAME"
#define VERSION [[[NSBundle mainBundle] infoDictionary] objectForKey:@"CFBundleVersion"]
#define DEVICE_VERSION [[[UIDevice currentDevice] systemVersion] doubleValue]
#define ALIGN_LEFT 1
#define ALIGN_CENTER 2
#define ALIGN_RIGHT 3
#define LAST_ALERT_DATE_STR @"LAST_ALERT_DATE_STR"
#define SCREEN_WIDTH [UIScreen mainScreen].bounds.size.width
#define SCREEN_HEIGHT [UIScreen mainScreen].bounds.size.height
#define IS_IPHONE5 [UIScreen mainScreen].bounds.size.height==568

#define TAB_COLOR @"#8f8f8f"
#define STORE_RANK_TABLE_HEAD_COLOR @"#bdbdbd"
#define GESTURE_PASSWORD @"GESTURE_PASSWORD"
#endif
