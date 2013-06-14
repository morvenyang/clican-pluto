//
//  AppDelegate.m
//  appletvserver
//
//  Created by zhang wei on 13-2-12.
//  Copyright (c) 2013年 __MyCompanyName__. All rights reserved.
//

#import "AppDelegate.h"
#import "RootViewController.h"
#import "XunLeiLoginViewController.h"
#import "FFMpegPlayViewController.h"
#import "DownloadStatusViewController.h"
#import "HTTPServer.h"
#import "DDLog.h"
#import "DDTTYLogger.h"
#import "MainViewController.h"
#import "MyHTTPConnection.h"
#import "AtvUtil.h"
#import "ASIHTTPRequest.h"
#import "Constants.h"
#import "InputViewController.h"
#import "ConfigViewController.h"
#import "TreeViewController.h"
#import "SmbAuthViewController.h"
#import "DownloadProgressViewController.h"
#import "LocalDownloadProgressViewController.h"
#import "StyleSheet.h"
static const int ddLogLevel = LOG_LEVEL_VERBOSE;

@implementation AppDelegate

@synthesize queue=_queue;
@synthesize downloadQueue = _downloadQueue;
@synthesize ipAddress=_ipAddress;
@synthesize localM3u8PathPrefix = _localM3u8PathPrefix;
@synthesize localNativeM3u8UrlPrefix = _localNativeM3u8UrlPrefix;
@synthesize localM3u8UrlPrefix = _localM3u8UrlPrefix;
@synthesize m3u8Process = _m3u8Process;

@synthesize localMp4PathPrefix = _localMp4PathPrefix;
@synthesize localMp4UrlPrefix = _localMp4UrlPrefix;
@synthesize mp4Process = _mp4Process;
@synthesize webContentSync = _webContentSync;
@synthesize localWebPathPrefix = _localWebPathPrefix;
@synthesize jsEngine = _jsEngine;
@synthesize photoProcess = _photoProcess;
@synthesize serverIP = _serverIP;
@synthesize atvDeviceId = _atvDeviceId;
@synthesize ipad = _ipad;
@synthesize simulate = _simulate;
@synthesize videoSizePerLine = _videoSizePerLine;
@synthesize mkvProcess = _mkvProcess;
@synthesize localMkvM3u8PathPrefix = _localMkvM3u8PathPrefix;
@synthesize localMkvM3u8UrlPrefix = _localMkvM3u8UrlPrefix;
@synthesize auth = _auth;
@synthesize smbProcess = _smbProcess;
@synthesize appleApproveCheck = _appleApproveCheck;
@synthesize localMp3PathPrefix = _localMp3PathPrefix;
@synthesize scriptRefreshDelegateArray = _scriptRefreshDelegateArray;
@synthesize proxy = _proxy;
@synthesize subTitleProcess = _subTitleProcess;
@synthesize clientVersion = _clientVersion;
@synthesize downloadProcess = _downloadProcess;
@synthesize offlineRecordProcess = _offlineRecordProcess;
@synthesize localDownloadPathPrefix = _localDownloadPathPrefix;
- (void)dealloc
{
    [super dealloc];
}

- (void)startServer
{
    // Start the server (and check for problems)
	
	NSError *error;
	if([httpServer start:&error])
	{
		DDLogInfo(@"Started HTTP Server on port %hu", [httpServer listeningPort]);
	}
	else
	{
		DDLogError(@"Error starting HTTP Server: %@", error);
	}
    
    NSError *error2;
	if([innerHttpServer start:&error2])
	{
		DDLogInfo(@"Started HTTP Server on port %hu", [innerHttpServer listeningPort]);
	}
	else
	{
		DDLogError(@"Error starting HTTP Server: %@", error2);
	}
}


-(void) initJSEngine{
    self.jsEngine = [[JSEngine alloc] init];
}

-(void) initQueue{
    self.queue = [[[ASINetworkQueue alloc] init] autorelease];
    self.downloadQueue = [[[ASINetworkQueue alloc] init] autorelease];
    [self.queue setShouldCancelAllRequestsOnFailure:NO];
    [self.queue go];
    [self.downloadQueue setShouldCancelAllRequestsOnFailure:NO];
    [self.downloadQueue go];
}
-(void) initProcess{
    self.m3u8Process = [[[M3u8Process alloc] init] autorelease];
    self.mp4Process = [[[Mp4Process alloc] init] autorelease];
    self.photoProcess = [[[PhotoProcess alloc] init] autorelease];
    self.mkvProcess = [[[MkvProcess alloc] init] autorelease];
    self.smbProcess = [[[SMBProcess alloc] init] autorelease];
    self.subTitleProcess = [[[SubTitleProcess alloc] init] autorelease];
    self.downloadProcess = [[[DownloadProcess alloc] init] autorelease];
    self.offlineRecordProcess = [[[OfflineRecordProcess alloc] init] autorelease];
}
-(void) initEnvironment{
    self.scriptRefreshDelegateArray = [NSMutableArray array];
    self.ipAddress = [AtvUtil getIPAddress];
    NSHTTPCookieStorage* cookieStorage = [NSHTTPCookieStorage sharedHTTPCookieStorage];
    [cookieStorage setCookieAcceptPolicy:NSHTTPCookieAcceptPolicyAlways];
    NSUserDefaults* defaults = [NSUserDefaults standardUserDefaults];
    self.serverIP = [defaults stringForKey: ATV_SERVER_IP_NAME];
    if(self.serverIP==nil||self.serverIP.length==0){
        self.serverIP = ATV_SERVER_DEFAULT_IP;
    }
    self.atvDeviceId =  [defaults stringForKey: ATV_DEVICE_ID_NAME];
    if(self.atvDeviceId==nil||self.atvDeviceId.length==0){
        self.atvDeviceId = [AtvUtil getUUID];
        [defaults setValue:self.atvDeviceId forKey:ATV_DEVICE_ID_NAME];
    }
    if(TTIsPad()){
        self.ipad = YES;
        self.videoSizePerLine = 7;
    }else{
        self.ipad = NO;
        self.videoSizePerLine = 3;
    }
    NSData *myEncodedObject = [defaults objectForKey:SMB_AUTH_NAME ];
    if(myEncodedObject!=nil&&myEncodedObject.length>0){
        self.auth = (KxSMBAuth *)[NSKeyedUnarchiver unarchiveObjectWithData: myEncodedObject];
    }
    NSLog(@"%@",self.auth);
    
    NSDictionary *proxySettings = NSMakeCollectable([(NSDictionary *)CFNetworkCopySystemProxySettings() autorelease]);
    NSString* httpHost = [proxySettings valueForKey:@"HTTPProxy"];
    NSNumber* httpPort = [proxySettings valueForKey:@"HTTPPort"];
    if(httpHost!=nil&&httpHost.length!=0&&httpPort!=nil){
        NSString* proxy = [NSString stringWithFormat:@"http://%@:%i",httpHost,httpPort.intValue];
        setenv("http_proxy", [proxy cStringUsingEncoding:NSASCIIStringEncoding], 1);
        self.proxy = YES;
    }else{
        self.proxy = NO;
    }
    
    NSLog(@"Run in simulate:%d",self.simulate);
    
    [[UIApplication sharedApplication] registerForRemoteNotificationTypes:(UIRemoteNotificationTypeAlert | UIRemoteNotificationTypeSound | UIRemoteNotificationTypeBadge)]; 
}
-(void) initWebContent{
    self.webContentSync = [[WebContentSync alloc] init];
}
-(void) initHttpServer{
    // Create server using our custom MyHTTPServer class
	httpServer = [[HTTPServer alloc] init];
    innerHttpServer = [[HTTPServer alloc] init];
	//httpServer.port = 80;
	// Tell the server to broadcast its presence via Bonjour.
	// This allows browsers such as Safari to automatically discover our service.
   	[httpServer setType:@"_http._tcp."];
	[innerHttpServer setType:@"_http._tcp."];
	// Normally there's no need to run our server on any specific port.
	// Technologies like Bonjour allow clients to dynamically discover the server's port at runtime.
	// However, for easy testing you may want force a certain port so you can just hit the refresh button.
	[httpServer setPort:8080];
    [innerHttpServer setPort:8081];
	[httpServer setConnectionClass:[MyHTTPConnection class]];
    [innerHttpServer setConnectionClass:[MyHTTPConnection class]];

	NSLog(@"Setting document root: %@", self.localWebPathPrefix);
	[httpServer setDocumentRoot:self.localWebPathPrefix];
    [innerHttpServer setDocumentRoot:self.localWebPathPrefix];
    [self startServer];

}
-(void) registerLocalServer{
    NSString* url = [NSString stringWithFormat:@"%@/appletv/ctl/localserver/register.do?innerIP=%@",AppDele.serverIP,self.ipAddress];
    @try {
        ASIHTTPRequest *req = [ASIHTTPRequest requestWithURL:[NSURL URLWithString:url]];
        [req startSynchronous];
        NSError *error = [req error];
        if (error) {
            NSLog(@"Register local server error %@",error.description);
        }else{
            NSLog(@"Register local server success");
        }
    }
    @catch (NSException *exception) {
        NSLog(@"exception occured when register local server %@",exception);
    }
    
}

-(void) initDocument{
    NSArray *path = NSSearchPathForDirectoriesInDomains(NSDocumentDirectory, NSUserDomainMask, YES);
    NSString* tempPath = NSTemporaryDirectory();
    NSString *m3u8Outpath = [tempPath stringByAppendingFormat:@"%@",@"/temp/m3u8/"];
    NSString *mp4Outpath = [tempPath stringByAppendingFormat:@"%@",@"/temp/mp4/"];
    NSString *mkvM3u8Outpath = [tempPath stringByAppendingFormat:@"%@",@"/temp/mkvM3u8/"];
    NSString *webOutpath = [[path objectAtIndex:0] stringByAppendingFormat:@"%@",@"/web"];
    NSString *localDownloadOutpath = [[path objectAtIndex:0] stringByAppendingFormat:@"%@",@"/download"];
    NSString *mp3Outpath = [tempPath stringByAppendingFormat:@"%@",@"/temp/mp3/"];

    //outpath = @"/Users/zhangwei/Desktop/m3u8/";
    if(![[NSFileManager defaultManager] fileExistsAtPath:m3u8Outpath]){
        [[NSFileManager defaultManager] createDirectoryAtPath:m3u8Outpath withIntermediateDirectories:YES attributes:nil error:nil];
    }
    if(![[NSFileManager defaultManager] fileExistsAtPath:mp4Outpath]){
        [[NSFileManager defaultManager] createDirectoryAtPath:mp4Outpath withIntermediateDirectories:YES attributes:nil error:nil];
    }
    if(![[NSFileManager defaultManager] fileExistsAtPath:mp3Outpath]){
        [[NSFileManager defaultManager] createDirectoryAtPath:mp3Outpath withIntermediateDirectories:YES attributes:nil error:nil];
    }
    if(![[NSFileManager defaultManager] fileExistsAtPath:localDownloadOutpath]){
        [[NSFileManager defaultManager] createDirectoryAtPath:localDownloadOutpath withIntermediateDirectories:YES attributes:nil error:nil];
    }
    self.localM3u8UrlPrefix = [@"http://" stringByAppendingFormat:@"%@:8080/appletv/noctl/proxy/temp/m3u8/",self.ipAddress];
    if(self.proxy){
        self.localNativeM3u8UrlPrefix = @"http://localhost:8080/appletv/noctl/proxy/temp/m3u8/";
    }else{
        self.localNativeM3u8UrlPrefix = self.localM3u8UrlPrefix;
    }
    
    self.localMp4UrlPrefix = [@"http://" stringByAppendingFormat:@"%@:8080/appletv/noctl/proxy/temp/mp4/",self.ipAddress];
    self.localMkvM3u8UrlPrefix = [@"http://" stringByAppendingFormat:@"%@:8080/appletv/noctl/proxy/temp/mkvM3u8/",self.ipAddress];
    self.localM3u8PathPrefix = m3u8Outpath;
    self.localMp4PathPrefix = mp4Outpath;
    self.localWebPathPrefix = webOutpath;
    self.localDownloadPathPrefix = localDownloadOutpath;
    self.localMkvM3u8PathPrefix = mkvM3u8Outpath;
    self.localMp3PathPrefix = mp3Outpath;
}

- (BOOL)application:(UIApplication *)application didFinishLaunchingWithOptions:(NSDictionary *)launchOptions
{
    // Configure our logging framework.
	// To keep things simple and fast, we're just going to log to the Xcode console.
	[DDLog addLogger:[DDTTYLogger sharedInstance]];
    
    [self initEnvironment];
    [self initQueue];
    [self initDocument];
    [self registerLocalServer];
    [self initWebContent];
    [self initHttpServer];
    [self initJSEngine];
    [TTStyleSheet setGlobalStyleSheet:[[[StyleSheet alloc] init] autorelease]];
    [[TTURLRequestQueue mainQueue] setMaxContentLength:0];
    TTNavigator* navigator = [TTNavigator navigator];
    
    navigator.supportsShakeToReload = NO;
    navigator.persistenceMode = TTNavigatorPersistenceModeNone;
    CGRect rect = TTScreenBounds();
    NSLog(@"width:%f height:%f",rect.size.width,rect.size.height);
    navigator.window = [[[UIWindow alloc] initWithFrame:TTScreenBounds()] autorelease];
    
    
    TTURLMap* map = navigator.URLMap;
    [map from:@"atvserver://ffmpeg" toSharedViewController:
     [FFMpegPlayViewController class]];
    [map from:@"atvserver://root" toSharedViewController:
     [RootViewController class]];
    [map from:@"atvserver://main" toSharedViewController:
     [MainViewController class]];
    [map from:@"atvserver://xunlei/login/(initWithType:)" toSharedViewController:
     [XunLeiLoginViewController class]];
    [map from:@"atvserver://download" toSharedViewController:
     [DownloadProgressViewController class]];
    [map from:@"atvserver://localdownload" toModalViewController:
     [LocalDownloadProgressViewController class]];
    [map from:@"atvserver://smb/(initWithUrl:)" toSharedViewController:
     [TreeViewController class]];
    [map from:@"atvserver://smb" toSharedViewController:
     [TreeViewController class]];
    [map from:@"atvserver://smb/auth" toSharedViewController:
     [SmbAuthViewController class]];
    [map from:@"atvserver://config" toSharedViewController:
     [ConfigViewController class]];
    [map from:@"atvserver://atv/input/(initWithLabel:)/(instruction:)/(initialText:)" toSharedViewController:
     [InputViewController class]];

    if (![navigator restoreViewControllers]) {
        [navigator openURLAction:[TTURLAction actionWithURLPath:@"atvserver://root"]];
    }
    
    return YES;
}

- (void)applicationWillResignActive:(UIApplication *)application
{
    [self continuousServer];
}

- (void)applicationDidEnterBackground:(UIApplication *)application
{
    // Use this method to release shared resources, save user data, invalidate timers, and store enough application state information to restore your application to its current state in case it is terminated later. 
    // If your application supports background execution, this method is called instead of applicationWillTerminate: when the user quits.
}

- (void)applicationWillEnterForeground:(UIApplication *)application
{
    // Called as part of the transition from the background to the inactive state; here you can undo many of the changes made on entering the background.
}

- (void)applicationDidBecomeActive:(UIApplication *)application
{
    // Restart any tasks that were paused (or not yet started) while the application was inactive. If the application was previously in the background, optionally refresh the user interface.
}

- (void)applicationWillTerminate:(UIApplication *)application
{
    NSLog(@"App is terminated");
    if(audioPlayer){
        [audioPlayer stop];
        [audioPlayer release];
    }
    // Called when the application is about to terminate. Save data if appropriate. See also applicationDidEnterBackground:.
}

- (void) continuousServer
{
    UIApplication*    app = [UIApplication sharedApplication];
    
    bgTask = [app beginBackgroundTaskWithExpirationHandler:^{
        // Clean up any unfinished task business by marking where you.
        // stopped or ending the task outright.
        [app endBackgroundTask:bgTask];
        bgTask = UIBackgroundTaskInvalid;
    }];
    
    // Start the long-running task and return immediately.
    dispatch_async(dispatch_get_global_queue(DISPATCH_QUEUE_PRIORITY_DEFAULT, 0), ^{
        
        audioPlayer = [[MTAudioPlayer alloc]init];
        [audioPlayer playBackgroundAudio];
        
        // Must comment out or else will stop server
        // [app endBackgroundTask:bgTask];
        bgTask = UIBackgroundTaskInvalid;
    });
}

- (void)application:(UIApplication *)app didRegisterForRemoteNotificationsWithDeviceToken:(NSData *)deviceToken {
    
    NSMutableString *token = [NSMutableString stringWithCapacity:([deviceToken length] * 2)];
    const unsigned char *dataBuffer = [deviceToken bytes];
    
    for (int i = 0; i < [deviceToken length]; ++i)
    {
        [token appendFormat:@"%02X", (unsigned int)dataBuffer[ i ]];
    }
    NSLog(@"My token is:%@", token);
    NSString* url = [NSString stringWithFormat:@"%@/appletv/ctl/apns/register.do?token=%@",self.serverIP,[AtvUtil encodeURL:token]];
    ASIHTTPRequest *req = [ASIHTTPRequest requestWithURL:[NSURL URLWithString:url]];
    [req setTimeOutSeconds:60];
    [req startAsynchronous];
}

- (void)application:(UIApplication *)app didFailToRegisterForRemoteNotificationsWithError:(NSError *)error {
    NSString *error_str = [NSString stringWithFormat: @"%@", error];
    NSLog(@"Failed to get token, error:%@", error_str);
}

- (void)application:(UIApplication *)application didReceiveRemoteNotification:(NSDictionary *)userInfo
{
    if ([[userInfo objectForKey:@"aps"] objectForKey:@"alert"] != nil)
    {
        UIAlertView *alert = [[UIAlertView alloc] initWithTitle:@"通知"
                                                        message:[[userInfo objectForKey:@"aps"] objectForKey:@"alert"]
                                                       delegate:self
                                              cancelButtonTitle:@"确定"
                                              otherButtonTitles:nil];
        [alert show];
        [alert release];
    }
}
@end
