//
//  Constants.h
//  appletvserver
//
//  Created by zhang wei on 13-2-12.
//  Copyright (c) 2013年 __MyCompanyName__. All rights reserved.
//

#ifndef appletvserver_Constants_h
#define appletvserver_Constants_h


#define ATV_SERVER_IP @"http://16.158.171.2/appletv"
#define WEB_CONTENT_SYNC_API @"/ctl/proxy/sync.zip"
#define WEB_CONTENT_SYNC_VERSION_API @"/ctl/proxy/sync/version.do"
#define MP4_PARTIAL_LENGTH 1048576
#define WEB_CONTENT_DOWNLOAD YES
#define ALog(fmt, ...) NSLog((@"%s [Line %d] " fmt), __PRETTY_FUNCTION__, __LINE__, ##__VA_ARGS__)

#endif


@interface Constants :NSObject{


}

@end

