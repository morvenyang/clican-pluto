//
//  Constants.h
//  appletvserver
//
//  Created by zhang wei on 13-2-12.
//  Copyright (c) 2013年 __MyCompanyName__. All rights reserved.
//

#ifndef appletvserver_Constants_h
#define appletvserver_Constants_h

#define ATV_SERVER_IP_NAME @"ATV_SERVER_IP_NAME"
#define ATV_DEVICE_ID_NAME @"ATV_DEVICE_ID_NAME"
#define ATV_CLIENT_VERSION @"1.1.1"
#define ATV_SERVER_DEFAULT_IP @"http://16.165.13.28"
#define WEB_CONTENT_SYNC_API @"/appletv/ctl/proxy/sync.zip"
#define WEB_CONTENT_SYNC_VERSION_API @"/appletv/ctl/proxy/sync/version.do"
#define MP4_PARTIAL_LENGTH 1048576
#define ALog(fmt, ...) NSLog((@"%s [Line %d] " fmt), __PRETTY_FUNCTION__, __LINE__, ##__VA_ARGS__)
#define SMB_AUTH_NAME @"SMB_AUTH_NAME"
#define FILE_TYPE_MP4 @"mp4"
#define FILE_TYPE_M3U8 @"m3u8"
#define TTG_NETWORK_NAME @"TTG_NETWORK"
#endif

@interface Constants :NSObject{


}

@end

