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
#define ATV_SERVER_DEFAULT_IP @"http://clican.org"
#define WEB_CONTENT_SYNC_API @"/appletv/sync_1.1.1.zip"
#define WEB_CONTENT_SYNC_VERSION_API @"/appletv/ctl/proxy/sync/version.do"
#define MP4_PARTIAL_LENGTH 1048576
#define ALog(fmt, ...) NSLog((@"%s [Line %d] " fmt), __PRETTY_FUNCTION__, __LINE__, ##__VA_ARGS__)
#define SMB_AUTH_NAME @"SMB_AUTH_NAME"
#endif

@interface Constants :NSObject{


}

@end

