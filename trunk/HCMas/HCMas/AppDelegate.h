//
//  AppDelegate.h
//  HCMas
//
//  Created by zhang wei on 14-8-29.
//  Copyright (c) 2014年 HC. All rights reserved.
//
#ifndef AppDelegate_h
#define AppDelegate_h
#import <UIKit/UIKit.h>
#import "User.h"

#define HCMasAppDelegate ((AppDelegate *)[[UIApplication sharedApplication] delegate])
#endif
@protocol RefreshDelegate;
@interface AppDelegate : UIResponder <UIApplicationDelegate>{
    User* _user;
    NSString* _token;
    NSTimer* _timer;
    NSArray* kpis;
}
@property (nonatomic, retain) User* user;
@property (nonatomic, retain) NSTimer* timer;
@property (strong, nonatomic) UIWindow *window;
@property(nonatomic,assign) id<RefreshDelegate> refreshDelegate;
@property (nonatomic, copy) NSString* token;
@property (nonatomic, retain) NSArray* kpis;
-(void)startTimer;
@end
@protocol RefreshDelegate <NSObject>
- (void)refresh;
@end