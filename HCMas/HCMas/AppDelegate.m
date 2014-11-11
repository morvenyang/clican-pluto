//
//  AppDelegate.m
//  HCMas
//
//  Created by zhang wei on 14-8-29.
//  Copyright (c) 2014年 HC. All rights reserved.
//

#import "AppDelegate.h"
#import "IndexViewController.h"
#import "StyleSheet.h"
#import "Constants.h"
#import "ChartViewController.h"

@implementation AppDelegate
@synthesize user= _user;
@synthesize token = _token;
@synthesize timer = _timer;
@synthesize refreshDelegate = _refreshDelegate;

- (BOOL)application:(UIApplication *)application didFinishLaunchingWithOptions:(NSDictionary *)launchOptions
{
    if([[UIApplication sharedApplication] respondsToSelector:@selector(registerUserNotificationSettings:)]){
        [[UIApplication sharedApplication] registerUserNotificationSettings:
         [UIUserNotificationSettings settingsForTypes:(UIUserNotificationTypeSound | UIUserNotificationTypeAlert) categories:nil]];
    }else{
        [[UIApplication sharedApplication] registerForRemoteNotificationTypes:
         (UIRemoteNotificationTypeSound | UIRemoteNotificationTypeAlert)];
    }

    self.user = [[[User alloc] init] autorelease];
    self.user.sessionId = @"";
    [application setStatusBarHidden:NO withAnimation:UIStatusBarAnimationFade];
    [TTStyleSheet setGlobalStyleSheet:[[[StyleSheet alloc] init] autorelease]];
    
    TTNavigator* navigator = [TTNavigator navigator];
    
    navigator.supportsShakeToReload = NO;
    navigator.persistenceMode = TTNavigatorPersistenceModeNone;
    navigator.window = [[[UIWindow alloc] initWithFrame:TTScreenBounds()] autorelease];
    [[TTURLCache sharedCache] removeAll:YES];
    TTURLMap* map = navigator.URLMap;
    [map from:@"hcmas://index" toSharedViewController:[IndexViewController class]];
    [map from:@"hcmas://chart/(initWithKpiType:)/(pointName:)/(startTime:)/(endTime:)" toSharedViewController:[ChartViewController class]];
    if (![navigator restoreViewControllers]) {
        IndexViewController* index= (IndexViewController*)[navigator openURLAction:[TTURLAction actionWithURLPath:@"hcmas://index"]];
        self.refreshDelegate = index;
    }
    
    return YES;
}

- (void)applicationWillResignActive:(UIApplication *)application
{
    // Sent when the application is about to move from active to inactive state. This can occur for certain types of temporary interruptions (such as an incoming phone call or SMS message) or when the user quits the application and it begins the transition to the background state.
    // Use this method to pause ongoing tasks, disable timers, and throttle down OpenGL ES frame rates. Games should use this method to pause the game.
}
-(void)startTimer{
    if(self.timer){
        [self.timer invalidate];
        self.timer = nil;
    }
    NSUserDefaults* defaults = [NSUserDefaults standardUserDefaults];
    NSString* updateFrequence = [defaults objectForKey:UPDATE_FREQUENCY_NAME];
    if(self.user.username!=nil&&updateFrequence!=nil&&updateFrequence.length>0){
        @try{
            int uf = updateFrequence.intValue;
            self.timer = [NSTimer scheduledTimerWithTimeInterval:uf target:self selector:@selector(invoketimer) userInfo:nil repeats:YES];
        }@catch(NSException* e){
            
        }
    }
}
- (void)applicationDidEnterBackground:(UIApplication *)application
{
    if(self.timer){
        [self.timer invalidate];
        self.timer = nil;
    }
}

- (void)invoketimer{
    NSLog(@"invoke timer");
    if(self.refreshDelegate){
        [self.refreshDelegate refresh];
    }
}

- (void)applicationWillEnterForeground:(UIApplication *)application
{
    NSUserDefaults* defaults = [NSUserDefaults standardUserDefaults];
    NSString* updateFrequence = [defaults objectForKey:UPDATE_FREQUENCY_NAME];
    if(self.user.username!=nil&&updateFrequence!=nil&&updateFrequence.length>0){
        @try{
            int uf = updateFrequence.intValue;
            self.timer = [NSTimer scheduledTimerWithTimeInterval:uf target:self selector:@selector(invoketimer) userInfo:nil repeats:YES];
        }@catch(NSException* e){
            
        }
    }
}

- (void)applicationDidBecomeActive:(UIApplication *)application
{
    // Restart any tasks that were paused (or not yet started) while the application was inactive. If the application was previously in the background, optionally refresh the user interface.
}

- (void)applicationWillTerminate:(UIApplication *)application
{
    // Called when the application is about to terminate. Save data if appropriate. See also applicationDidEnterBackground:.
}

- (void)application:(UIApplication *)app didRegisterForRemoteNotificationsWithDeviceToken:(NSData *)deviceToken {
    
    NSMutableString *token = [NSMutableString stringWithCapacity:([deviceToken length] * 2)];
    const unsigned char *dataBuffer = [deviceToken bytes];
    
    for (int i = 0; i < [deviceToken length]; ++i)
    {
        [token appendFormat:@"%02X", (unsigned int)dataBuffer[ i ]];
    }
    NSLog(@"My token is:%@", token);
    self.token = token;
}
-(void)application:(UIApplication *)application didRegisterUserNotificationSettings:(UIUserNotificationSettings *)notificationSettings{
    [application registerForRemoteNotifications];
}
- (void)application:(UIApplication *)app didFailToRegisterForRemoteNotificationsWithError:(NSError *)error {
    NSString *error_str = [NSString stringWithFormat: @"%@", error];
    NSLog(@"Failed to get token, error:%@", error_str);
}

- (void)application:(UIApplication *)application didReceiveRemoteNotification:(NSDictionary *)userInfo
{
    if ([[userInfo objectForKey:@"aps"] objectForKey:@"alert"] != nil)
    {
        UIAlertView *alert = [[UIAlertView alloc] initWithTitle:@"报警通知"
                                                        message:[[userInfo objectForKey:@"aps"] objectForKey:@"alert"]
                                                       delegate:self
                                              cancelButtonTitle:@"确定"
                                              otherButtonTitles:nil];
        [alert show];
        [alert release];
    }
}
@end
