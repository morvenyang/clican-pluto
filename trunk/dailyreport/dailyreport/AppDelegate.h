//
//  AppDelegate.h
//  dailyreport
//
//  Created by zhang wei on 14-5-16.
//  Copyright (c) 2014年 Peace Bird. All rights reserved.
//

#import <UIKit/UIKit.h>
#import "User.h"
#import "WXApi.h"
#define DrAppDelegate ((AppDelegate *)[[UIApplication sharedApplication] delegate])

@interface AppDelegate : UIResponder <UIApplicationDelegate,WXApiDelegate>{
    User* _user;
    NSString* _token;
}

@property (nonatomic, retain) User* user;
@property (nonatomic, copy) NSString* token;
@property (strong, nonatomic) UIWindow *window;

@end