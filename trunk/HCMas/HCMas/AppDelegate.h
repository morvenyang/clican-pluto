//
//  AppDelegate.h
//  HCMas
//
//  Created by zhang wei on 14-8-29.
//  Copyright (c) 2014年 HC. All rights reserved.
//

#import <UIKit/UIKit.h>
#import "User.h"

#define HCMasAppDelegate ((AppDelegate *)[[UIApplication sharedApplication] delegate])

@interface AppDelegate : UIResponder <UIApplicationDelegate>{
    User* _user;
    NSString* _token;
}
@property (nonatomic, retain) User* user;

@property (strong, nonatomic) UIWindow *window;

@property (nonatomic, copy) NSString* token;

@end
