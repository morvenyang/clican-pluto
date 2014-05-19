//
//  LoginViewController.h
//  hpsdf-ngp-mudole-iphone-client
//
//  Created by zhang wei on 11-11-22.
//  Copyright 2011年 __MyCompanyName__. All rights reserved.
//

#import <UIKit/UIKit.h>
#import "MBProgressHUD.h"
#import "Constants.h"
#import "LoginModel.h"
#import "LoginDataSource.h"

@interface LoginViewController : UIViewController<UITextFieldDelegate,LoginDelegate,MBProgressHUDDelegate> {
    UITextField* _usernameField;
    UITextField* _passwordField;
    LoginModel* _loginModel;
    TTImageView* _titleImageView;
    UIButton* _loginButton;
    MBProgressHUD* _progressHUD;
}

@property (nonatomic, retain) UITextField *usernameField;
@property (nonatomic, retain) UITextField *passwordField;
@property (nonatomic, retain) TTImageView *titleImageView;
@property (nonatomic, retain) UIButton *loginButton;
@property (nonatomic, retain) MBProgressHUD    *progressHUD;


@end
