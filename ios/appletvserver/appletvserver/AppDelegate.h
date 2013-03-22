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
#import "Mp4Process.h"
#import "MTAudioPlayer.h"
#import "WebContentSync.h"
#define AppDele ((AppDelegate *)[[UIApplication sharedApplication] delegate])


@interface AppDelegate : UIResponder <UIApplicationDelegate>{
    HTTPServer *httpServer;
    ASINetworkQueue* _queue;
    NSString* _ipAddress;
    NSString* _localM3u8UrlPrefix;
    NSString* _localM3u8PathPrefix;
    M3u8Process* _m3u8Process;
    
    NSString* _localMp4UrlPrefix;
    NSString* _localMp4PathPrefix;

    Mp4Process* _mp4Process;
    UIBackgroundTaskIdentifier bgTask;
    MTAudioPlayer * audioPlayer;
    
    WebContentSync* _webContentSync;
}

@property (nonatomic, retain) ASINetworkQueue* queue;
@property (nonatomic, retain) NSString* ipAddress;
@property (nonatomic, retain) NSString* localM3u8UrlPrefix;
@property (nonatomic, retain) NSString* localM3u8PathPrefix;
@property (nonatomic, retain) M3u8Process* m3u8Process;

@property (nonatomic, retain) NSString* localMp4UrlPrefix;
@property (nonatomic, retain) NSString* localMp4PathPrefix;
@property (nonatomic, retain) Mp4Process* mp4Process;
@property (nonatomic, retain) WebContentSync* webContentSync;
@end
