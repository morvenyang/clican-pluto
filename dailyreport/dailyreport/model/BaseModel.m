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
#import "IndexListModel.h"
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
    NSString* lastLoginDateStr = [defaults objectForKey:LAST_LOGIN_DATE];
    

    if(lastLoginDateStr==nil||lastLoginDateStr.length==0||user.expiredDays<=0){
        TTOpenURL(@"peacebird://login");
    }else{
        NSDateFormatter* dateFormatter = [[[NSDateFormatter alloc] init] autorelease];
        [dateFormatter setTimeStyle:NSDateFormatterFullStyle];
        [dateFormatter setDateFormat:@"yyyy-MM-dd"];
        NSDate* lastLoginDate= [dateFormatter dateFromString:lastLoginDateStr];
        NSDate* now = [NSDate date];
        NSDate* expiredDate = [lastLoginDate dateByAddingTimeInterval:60*60*24*user.expiredDays.intValue];
        if([expiredDate compare:now]==NSOrderedDescending){
            DrAppDelegate.user = user;
            if([self isKindOfClass:[IndexListModel class]]){
                NSUserDefaults* defaults = [NSUserDefaults standardUserDefaults];
                
                NSString* gesturePassword = [defaults objectForKey:GESTURE_PASSWORD];
                if(gesturePassword!=nil&&gesturePassword.length!=0){
                    [[TTNavigator navigator] removeAllViewControllers];
                    TTOpenURL(@"peacebird://gestureLock/unlock");
                }else{
                    [self load:TTURLRequestCachePolicyNone more:NO];
                }
            }else{
                [self load:TTURLRequestCachePolicyNone more:NO];
            }
        }else{
            TTOpenURL(@"peacebird://login");
        }
    }
    

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
