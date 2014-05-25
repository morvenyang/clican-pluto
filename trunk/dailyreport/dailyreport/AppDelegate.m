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
#import "CRNavigator.h"
@implementation AppDelegate

@synthesize user=_user;
@synthesize token = _token;

- (BOOL)application:(UIApplication *)application didFinishLaunchingWithOptions:(NSDictionary *)launchOptions
{
    [application setStatusBarHidden:NO withAnimation:UIStatusBarAnimationFade];
    [WXApi registerApp:@"123"];
    [TTStyleSheet setGlobalStyleSheet:[[[StyleSheet alloc] init] autorelease]];
    
    TTNavigator* navigator = [CRNavigator navigator];
    
    navigator.supportsShakeToReload = NO;
    navigator.persistenceMode = TTNavigatorPersistenceModeNone;
    navigator.window = [[[UIWindow alloc] initWithFrame:TTScreenBounds()] autorelease];
    
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
    if (![navigator restoreViewControllers]) {
        [navigator openURLAction:[TTURLAction actionWithURLPath:@"peacebird://login"]];
    }
    
    return YES;
}

-(BOOL) application:(UIApplication *)application openURL:(NSURL *)url sourceApplication:(NSString *)sourceApplication annotation:(id)annotation{
    return [WXApi handleOpenURL:url delegate:self];
}

-(BOOL) application:(UIApplication *)application handleOpenURL:(NSURL *)url{
    return [WXApi handleOpenURL:url delegate:self];
}

-(void)onReq:(BaseReq *)req{
    
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
