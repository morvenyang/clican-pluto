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
#import "PhotoProcess.h"
#import "MTAudioPlayer.h"
#import "WebContentSync.h"
#import "JSEngine.h"
#import "MkvProcess.h"
#import "KxSMBProvider.h"
#import "SMBProcess.h"
#import "SubTitleProcess.h"
#import "DownloadProcess.h"
#import "OfflineRecordProcess.h"
#define AppDele ((AppDelegate *)[[UIApplication sharedApplication] delegate])


@interface AppDelegate : UIResponder <UIApplicationDelegate>{
    HTTPServer *httpServer;
    HTTPServer *innerHttpServer;
    ASINetworkQueue* _queue;
    ASINetworkQueue* _downloadQueue;
    NSString* _ipAddress;
    NSString* _localDownloadPathPrefix;
    NSString* _localM3u8UrlPrefix;
    NSString* _localNativeM3u8UrlPrefix;
    NSString* _localM3u8PathPrefix;
    M3u8Process* _m3u8Process;
    
    NSString* _localMp4UrlPrefix;
    NSString* _localMp4PathPrefix;
    
    Mp4Process* _mp4Process;
    UIBackgroundTaskIdentifier bgTask;
    MTAudioPlayer * audioPlayer;
    
    WebContentSync* _webContentSync;
    NSString* _localWebPathPrefix;
    JSEngine* _jsEngine;
    
    PhotoProcess* _photoProcess;
    NSString* _serverIP;
    NSString* _atvDeviceId;
    
    MkvProcess* _mkvProcess;
    NSString* _localMkvM3u8UrlPrefix;
    NSString* _localMkvM3u8PathPrefix;
    
    KxSMBAuth* _auth;
    
    BOOL _ipad;
    BOOL _simulate;
    int _videoSizePerLine;
    
    SMBProcess* _smbProcess;
    BOOL* _appleApproveCheck;
    NSString* _localMp3PathPrefix;
    NSMutableArray* _scriptRefreshDelegateArray;
    BOOL _proxy;
    
    SubTitleProcess* _subTitleProcess;
    NSString* _clientVersion;
    
    DownloadProcess* _downloadProcess;
    OfflineRecordProcess* _offlineRecordProcess;
    BOOL _ttgNetwork;
}

@property (nonatomic, retain) ASINetworkQueue* queue;
@property (nonatomic, retain) ASINetworkQueue* downloadQueue;
@property (nonatomic, retain) NSString* ipAddress;
@property (nonatomic, retain) NSString* localM3u8UrlPrefix;
@property (nonatomic, retain) NSString* localNativeM3u8UrlPrefix;

@property (nonatomic, retain) NSString* localM3u8PathPrefix;
@property (nonatomic, retain) M3u8Process* m3u8Process;

@property (nonatomic, retain) NSString* localMp4UrlPrefix;
@property (nonatomic, retain) NSString* localMp4PathPrefix;
@property (nonatomic, retain) Mp4Process* mp4Process;
@property (nonatomic, retain) WebContentSync* webContentSync;
@property (nonatomic, retain) NSString* localWebPathPrefix;
@property (nonatomic, retain) JSEngine* jsEngine;
@property (nonatomic, retain) PhotoProcess* photoProcess;
@property (nonatomic, copy) NSString* serverIP;
@property (nonatomic, copy) NSString* atvDeviceId;
@property (nonatomic, assign) BOOL ipad;
@property (nonatomic, assign) BOOL simulate;
@property (nonatomic, assign) int videoSizePerLine;
@property (nonatomic, retain) MkvProcess* mkvProcess;
@property (nonatomic, copy) NSString* localMkvM3u8UrlPrefix;
@property (nonatomic, copy) NSString* localMkvM3u8PathPrefix;
@property (nonatomic, retain) KxSMBAuth* auth;
@property (nonatomic, retain) SMBProcess* smbProcess;
@property (nonatomic, assign) BOOL* appleApproveCheck;
@property (nonatomic, copy) NSString* localMp3PathPrefix;
@property (nonatomic, retain) NSMutableArray* scriptRefreshDelegateArray;
@property (nonatomic, assign) BOOL proxy;
@property (nonatomic, retain) SubTitleProcess* subTitleProcess;
@property (nonatomic, copy) NSString* clientVersion;
@property (nonatomic, retain) DownloadProcess* downloadProcess;
@property (nonatomic, retain) OfflineRecordProcess* offlineRecordProcess;
@property (nonatomic, copy) NSString* localDownloadPathPrefix;
@property (nonatomic, assign) BOOL ttgNetwork;
-(void) initProcess;

@end
