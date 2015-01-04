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
#import "LoginModel.h"
#define DrAppDelegate ((AppDelegate *)[[UIApplication sharedApplication] delegate])

@interface AppDelegate : UIResponder <UIApplicationDelegate,WXApiDelegate,CheckSessionDelegate>{
    User* _user;
    NSString* _token;
    LoginModel* _loginModel;
    BOOL _navigation;
    NSString* _latestClientVersion;

}

@property (nonatomic, retain) User* user;
@property (nonatomic, retain) LoginModel* loginModel;
@property (nonatomic, copy) NSString* token;
@property (strong, nonatomic) UIWindow *window;
@property (nonatomic, assign) BOOL navigation;
@property (nonatomic, copy) NSString* latestClientVersion;
@end
