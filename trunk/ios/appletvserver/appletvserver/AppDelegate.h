//
//  AppDelegate.h
//  appletvserver
//
//  Created by zhang wei on 13-2-12.
//  Copyright (c) 2013年 __MyCompanyName__. All rights reserved.
//

#import <UIKit/UIKit.h>
#import <Three20/Three20.h>
#import "ASINetworkQueue.h"
#import "HTTPServer.h"
#import "M3u8Process.h"
#define AppDele ((AppDelegate *)[[UIApplication sharedApplication] delegate])


@interface AppDelegate : UIResponder <UIApplicationDelegate>{
    HTTPServer *httpServer;
    ASINetworkQueue* _queue;
    NSString* _ipAddress;
    NSString* _localM3u8UrlPrefix;
    NSString* _localM3u8PathPrefix;
    M3u8Process* _m3u8Process;
}

@property (nonatomic, retain) ASINetworkQueue* queue;
@property (nonatomic, retain) NSString* ipAddress;
@property (nonatomic, retain) NSString* localM3u8UrlPrefix;
@property (nonatomic, retain) NSString* localM3u8PathPrefix;
@property (nonatomic, retain) M3u8Process* m3u8Process;
@end
