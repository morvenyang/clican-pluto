//
//  AppDelegate.m
//  dailyreport
//
//  Created by zhang wei on 14-5-16.
//  Copyright (c) 2014年 Peace Bird. All rights reserved.
//

#import "AppDelegate.h"
#import "style/StyleSheet.h"
#import "IndexViewController.h"
#import "LoginViewController.h"
#import "BrandViewController.h"
#import "KPIViewController.h"
#import "RetailViewController.h"
#import "StoreRankViewController.h"
#import "KKViewController.h"
#import "B2CKPIViewController.h"
#import "GoodRankViewController.h"
#import "GoodViewController.h"
#import "StoreSumController.h"
#import "NoRetailsController.h"
#import "PBNavigationViewController.h"
#import "PasswordViewController.h"
#import "GuideViewController.h"
#import "CRNavigator.h"
@implementation AppDelegate

@synthesize user=_user;
@synthesize token = _token;
@synthesize loginModel = _loginModel;
@synthesize navigation = _navigation;
@synthesize latestClientVersion = _latestClientVersion;
- (BOOL)application:(UIApplication *)application didFinishLaunchingWithOptions:(NSDictionary *)launchOptions
{
    
    #ifdef __IPHONE_8_0
    if([[UIApplication sharedApplication] respondsToSelector:@selector(registerUserNotificationSettings:)]){
        [[UIApplication sharedApplication] registerUserNotificationSettings:
         [UIUserNotificationSettings settingsForTypes:(UIUserNotificationTypeSound | UIUserNotificationTypeAlert) categories:nil]];
    }else{
        [[UIApplication sharedApplication] registerForRemoteNotificationTypes:
         (UIRemoteNotificationTypeSound | UIRemoteNotificationTypeAlert)];
    }
    #endif
    #ifndef __IPHONE_8_0
        [[UIApplication sharedApplication] registerForRemoteNotificationTypes:
         (UIRemoteNotificationTypeSound | UIRemoteNotificationTypeAlert)];
    #endif

    self.user = [[[User alloc] init] autorelease];
    self.user.sessionId = @"";
    self.user.showGestureLock =NO;
    self.loginModel = [[[LoginModel alloc] init] autorelease];
    self.loginModel.checkSessionDelegate = self;
    [application setStatusBarHidden:NO withAnimation:UIStatusBarAnimationFade];
    [WXApi registerApp:@"wx489983fd6835c0e8"];
    [TTStyleSheet setGlobalStyleSheet:[[[StyleSheet alloc] init] autorelease]];
    
    TTNavigator* navigator = [CRNavigator navigator];
    [[TTURLCache sharedCache] removeAll:YES];
    navigator.supportsShakeToReload = NO;
    navigator.persistenceMode = TTNavigatorPersistenceModeNone;
    navigator.window = [[[UIWindow alloc] initWithFrame:TTScreenBounds()] autorelease];
    [[TTURLCache sharedCache] removeAll:YES];
    TTURLMap* map = navigator.URLMap;
    [map from:@"peacebird://login" toSharedViewController:[LoginViewController class]];
    [map from:@"peacebird://index" toSharedViewController:
     [IndexViewController class]];
    [map from:@"peacebird://brand/(initWithBrand:)" toSharedViewController:
    [BrandViewController class]];
    [map from:@"peacebird://kpi/(initWithBrand:)" toSharedViewController:
     [KPIViewController class]];
    [map from:@"peacebird://retail/(initWithBrand:)" toSharedViewController:
     [RetailViewController class]];
    [map from:@"peacebird://storeRank/(initWithBrand:)" toSharedViewController:
     [StoreRankViewController class]];
    [map from:@"peacebird://goodRank/(initWithBrand:)" toSharedViewController:
     [GoodRankViewController class]];
    [map from:@"peacebird://good/(initWithBrand:)/(index:)" toSharedViewController:
     [GoodViewController class]];
    [map from:@"peacebird://b2cKpi/(initWithBrand:)" toSharedViewController:
     [B2CKPIViewController class]];
    [map from:@"peacebird://storeSum/(initWithBrand:)" toSharedViewController:
     [StoreSumController class]];
    [map from:@"peacebird://noRetails/(initWithBrand:)" toSharedViewController:
     [NoRetailsController class]];
    [map from:@"peacebird://gestureLock/(initWithType:)" toSharedViewController:
     [KKViewController class]];
    [map from:@"peacebird://password/(initWithType:)" toSharedViewController:
     [PasswordViewController class]];
    [map from:@"peacebird://pbNavigation/(initWithBrand:)/(backIndex:)" toSharedViewController:
     [PBNavigationViewController class]];
    [map from:@"peacebird://guide" toSharedViewController:
     [GuideViewController class]];

    if (![navigator restoreViewControllers]) {
        NSUserDefaults* defaults = [NSUserDefaults standardUserDefaults];
        NSString* userName = [defaults objectForKey:LAST_USER_NAME];
        NSString* guideFlag = [defaults objectForKey:GUIDE_FLAG];
        guideFlag=@"false";
        if (guideFlag!=nil&&[guideFlag isEqualToString:@"false"]) {
            //第一次打开应用直接显示登录页面
            if(userName==nil||userName.length==0){
                [navigator openURLAction:[TTURLAction actionWithURLPath:@"peacebird://login"]];
            }else{
                [navigator openURLAction:[TTURLAction actionWithURLPath:@"peacebird://index"]];
            }
        }else{
            [navigator openURLAction:[TTURLAction actionWithURLPath:@"peacebird://guide"]];
        }

    }
    
    return YES;
}

-(BOOL) application:(UIApplication *)application openURL:(NSURL *)url sourceApplication:(NSString *)sourceApplication annotation:(id)annotation{
    return [WXApi handleOpenURL:url delegate:self];
}

-(BOOL) application:(UIApplication *)application handleOpenURL:(NSURL *)url{
    return [WXApi handleOpenURL:url delegate:self];
}

- (void)applicationWillResignActive:(UIApplication *)application
{
    // Sent when the application is about to move from active to inactive state. This can occur for certain types of temporary interruptions (such as an incoming phone call or SMS message) or when the user quits the application and it begins the transition to the background state.
    // Use this method to pause ongoing tasks, disable timers, and throttle down OpenGL ES frame rates. Games should use this method to pause the game.
}

- (void)applicationDidEnterBackground:(UIApplication *)application
{
    self.user.showGestureLock = YES;
}

- (void)applicationWillEnterForeground:(UIApplication *)application
{
    NSUserDefaults* defaults = [NSUserDefaults standardUserDefaults];
   
    NSString* gesturePassword = [defaults objectForKey:GESTURE_PASSWORD];
    if(self.user.showGestureLock&&gesturePassword!=nil&&gesturePassword.length!=0){
        [[TTNavigator navigator] removeAllViewControllers];
        TTOpenURL(@"peacebird://gestureLock/unlock");
    }else{
        [self.loginModel checkSession];
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
#ifdef __IPHONE_8_0
-(void)application:(UIApplication *)application didRegisterUserNotificationSettings:(UIUserNotificationSettings *)notificationSettings{
    [application registerForRemoteNotifications];
}
#endif
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

-(void) onReq:(BaseReq*)req
{
    NSLog(@"Ignore weixin request");
}

-(void) onResp:(BaseResp*)resp
{
    if([resp isKindOfClass:[SendMessageToWXResp class]])
    {
        if(resp.errCode==0){
            UIAlertView *alert = [[UIAlertView alloc] initWithTitle:@"分享成功" message:@"" delegate:self cancelButtonTitle:@"OK" otherButtonTitles:nil, nil];
            [alert show];
            [alert release];
        }
        
    }
}

- (void)checkSessionResult:(BOOL) result{
    if(!result){
        TTOpenURL(@"peacebird://login");
    }
}

@end
