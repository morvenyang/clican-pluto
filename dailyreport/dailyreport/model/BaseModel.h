//
//  BaseModel.h
//  dailyreport
//
//  Created by zhang wei on 14-5-26.
//  Copyright (c) 2014年 Peace Bird. All rights reserved.
//

#import <Three20Network/Three20Network.h>
#import "LoginModel.h"
@interface BaseModel : TTURLRequestModel<LoginDelegate>{
    LoginModel* _loginModel;
}
@property (nonatomic, retain) LoginModel *loginModel;
-(void) tryAutoLogin;
@end
