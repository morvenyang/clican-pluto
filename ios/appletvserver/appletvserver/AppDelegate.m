//
//  AppDelegate.m
//  appletvserver
//
//  Created by zhang wei on 13-2-12.
//  Copyright (c) 2013年 __MyCompanyName__. All rights reserved.
//

#import "AppDelegate.h"
#import "QQIndexViewController.h"
#import "QQVideoViewController.h"
#import "QQPlayViewController.h"
#import "FFMpegPlayViewController.h"
#import "HTTPServer.h"
#import "DDLog.h"
#import "DDTTYLogger.h"
#import "MainViewController.h"
#import "MyHTTPConnection.h"
#import "AtvUtil.h"

static const int ddLogLevel = LOG_LEVEL_VERBOSE;

@implementation AppDelegate

@synthesize queue=_queue;
@synthesize ipAddress=_ipAddress;
@synthesize localM3u8PathPrefix = _localM3u8PathPrefix;
@synthesize localM3u8UrlPrefix = _localM3u8UrlPrefix;
@synthesize m3u8Process = _m3u8Process;

@synthesize localMp4PathPrefix = _localMp4PathPrefix;
@synthesize localMp4UrlPrefix = _localMp4UrlPrefix;
@synthesize mp4Process = _mp4Process;

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
}

-(void) initQueue{
    self.queue = [[ASINetworkQueue alloc] init];
    [self.queue setShouldCancelAllRequestsOnFailure:NO];
    [self.queue go];
}
-(void) initProcess{
    self.m3u8Process = [[M3u8Process alloc] init];
    self.mp4Process = [[Mp4Process alloc] init];
}
-(void) initDocument{
    NSArray *path = NSSearchPathForDirectoriesInDomains(NSDocumentDirectory, NSUserDomainMask, YES);
    NSString *m3u8Outpath = [[path objectAtIndex:0] stringByAppendingFormat:@"%@",@"/temp/m3u8/"];
    NSString *mp4Outpath = [[path objectAtIndex:0] stringByAppendingFormat:@"%@",@"/temp/mp4/"];
    //outpath = @"/Users/zhangwei/Desktop/m3u8/";
    if(![[NSFileManager defaultManager] fileExistsAtPath:m3u8Outpath]){
        [[NSFileManager defaultManager] createDirectoryAtPath:m3u8Outpath withIntermediateDirectories:YES attributes:nil error:nil];
    }
    if(![[NSFileManager defaultManager] fileExistsAtPath:mp4Outpath]){
        [[NSFileManager defaultManager] createDirectoryAtPath:mp4Outpath withIntermediateDirectories:YES attributes:nil error:nil];
    }
    self.localM3u8UrlPrefix = [@"http://" stringByAppendingFormat:@"%@:8080/appletv/temp/m3u8/",[AtvUtil getIPAddress]];
    self.localMp4UrlPrefix = [@"http://" stringByAppendingFormat:@"%@:8080/appletv/temp/mp4/",[AtvUtil getIPAddress]];
    NSLog(@"m3u8 url prefix:%@",self.localM3u8UrlPrefix);
    NSLog(@"mp4 url prefix:%@",self.localMp4UrlPrefix);
    self.localM3u8PathPrefix = m3u8Outpath;
    self.localMp4PathPrefix = mp4Outpath;
}

- (BOOL)application:(UIApplication *)application didFinishLaunchingWithOptions:(NSDictionary *)launchOptions
{
    [self initQueue];
    [self initDocument];
    [self initProcess];
    // Configure our logging framework.
	// To keep things simple and fast, we're just going to log to the Xcode console.
	[DDLog addLogger:[DDTTYLogger sharedInstance]];
    self.ipAddress = [AtvUtil getIPAddress];

	// Create server using our custom MyHTTPServer class
	httpServer = [[HTTPServer alloc] init];
	//httpServer.port = 80;
	// Tell the server to broadcast its presence via Bonjour.
	// This allows browsers such as Safari to automatically discover our service.
	[httpServer setType:@"_http._tcp."];
	
	// Normally there's no need to run our server on any specific port.
	// Technologies like Bonjour allow clients to dynamically discover the server's port at runtime.
	// However, for easy testing you may want force a certain port so you can just hit the refresh button.
	[httpServer setPort:8080];
	[httpServer setConnectionClass:[MyHTTPConnection class]];
	// Serve files from our embedded Web folder
	NSString *webPath = [[[NSBundle mainBundle] resourcePath] stringByAppendingPathComponent:@"web"];
	DDLogInfo(@"Setting document root: %@", webPath);
	
	[httpServer setDocumentRoot:webPath];
    [self startServer];

    TTNavigator* navigator = [TTNavigator navigator];
    
    navigator.supportsShakeToReload = NO;
    navigator.persistenceMode = TTNavigatorPersistenceModeNone;
    navigator.window = [[[UIWindow alloc] initWithFrame:TTScreenBounds()] autorelease];
    
    
    TTURLMap* map = navigator.URLMap;
    [map from:@"atvserver://ffmpeg" toSharedViewController:
     [FFMpegPlayViewController class]];
    [map from:@"atvserver://main" toSharedViewController:
     [MainViewController class]];
    [map from:@"atvserver://qq/index" toSharedViewController:
     [QQIndexViewController class]];
    [map from:@"atvserver://qq/video/(initWithVid:)" toSharedViewController:
     [QQVideoViewController class]];
    [map from:@"atvserver://qq/play/(initWithVideoItemId:)/(vid:)" toSharedViewController:
     [QQPlayViewController class]];
    if (![navigator restoreViewControllers]) {
        [navigator openURLAction:[TTURLAction actionWithURLPath:@"atvserver://main"]];
    }
    
    return YES;
}

- (void)applicationWillResignActive:(UIApplication *)application
{
    // Sent when the application is about to move from active to inactive state. This can occur for certain types of temporary interruptions (such as an incoming phone call or SMS message) or when the user quits the application and it begins the transition to the background state.
    // Use this method to pause ongoing tasks, disable timers, and throttle down OpenGL ES frame rates. Games should use this method to pause the game.
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
    // Called when the application is about to terminate. Save data if appropriate. See also applicationDidEnterBackground:.
}

@end
