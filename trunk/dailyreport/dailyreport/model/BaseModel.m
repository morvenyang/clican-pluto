//
//  BaseModel.m
//  dailyreport
//
//  Created by zhang wei on 14-5-26.
//  Copyright (c) 2014年 Peace Bird. All rights reserved.
//

#import "BaseModel.h"
#import "User.h"
#import "AppDelegate.h"

@implementation BaseModel

@synthesize loginModel = _loginModel;

- (id)init{
     if ((self = [super init])) {
         self.loginModel = [[[LoginModel alloc] init] autorelease];
         self.loginModel.delegate = self;
     }
    return self;
}

-(void) tryAutoLogin{
    NSUserDefaults* defaults = [NSUserDefaults standardUserDefaults];
    NSString* userName = [defaults objectForKey:LAST_USER_NAME];
    NSString* password = [defaults objectForKey:LAST_PASSWORD];
    if (userName!=nil&&password!=nil) {
        [self.loginModel login:userName password:password];
    }else{
        TTOpenURL(@"peacebird://login");
    }
}

- (void)loginStart:(User*) user
{
    NSLog(@"start auto login");
}

- (void)loginSuccess:(User*) user
{
    NSUserDefaults* defaults = [NSUserDefaults standardUserDefaults];
    [defaults setObject:user.username forKey:LAST_USER_NAME];
    [defaults setObject:user.password forKey:LAST_PASSWORD];
    DrAppDelegate.user = user;

    [self load:TTURLRequestCachePolicyNone more:NO];
}

- (void)loginFailed:(NSError*) error message:(NSString*) message
{
    //-1004 connection is not available
    //-1001 timeout
    if([error code]==-1004||[error code]==-1001){
        TTAlert(@"请检查网络链接");
    }else{
        TTOpenURL(@"peacebird://login");
    }
    
}

- (void) dealloc {
    _loginModel.delegate = nil;
    TT_RELEASE_SAFELY(_loginModel);
    [super dealloc];
}

@end
