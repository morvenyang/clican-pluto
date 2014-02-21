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
#define ATV_CLIENT_VERSION @"1.1.0"
#define ATV_SERVER_DEFAULT_IP @"http://10.0.1.5"
#define WEB_CONTENT_SYNC_API @"/appletv/ctl/proxy/sync.zip"
#define WEB_CONTENT_SYNC_VERSION_API @"/appletv/ctl/proxy/sync/version.do"
#define MP4_PARTIAL_LENGTH 1048576
#define ALog(fmt, ...) NSLog((@"%s [Line %d] " fmt), __PRETTY_FUNCTION__, __LINE__, ##__VA_ARGS__)
#define SMB_AUTH_NAME @"SMB_AUTH_NAME"
#endif

@interface Constants :NSObject{


}

@end
