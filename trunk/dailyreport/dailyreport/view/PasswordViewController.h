//
//  PasswordViewController.h
//  dailyreport
//
//  Created by zhang wei on 14-12-1.
//  Copyright (c) 2014年 Peace Bird. All rights reserved.
//

#import <UIKit/UIKit.h>
#import "Constants.h"
#import "LoginModel.h"


@interface PasswordViewController : UIViewController<UITextFieldDelegate,PasswordDelegate>{

    UITextField* _oldPasswordField;
    UITextField* _passwordField;
    UITextField* _confirmPasswordField;
    LoginModel* _loginModel;
    TTImageView* _titleImageView;
    UIButton* _submitButton;
    NSString* _type;
}

@property (nonatomic, retain) UITextField *passwordField;
@property (nonatomic, retain) UITextField *oldPasswordField;
@property (nonatomic, retain) UITextField *confirmPasswordField;
@property (nonatomic, retain) TTImageView *titleImageView;
@property (nonatomic, retain) UIButton *submitButton;
@property (nonatomic, copy) NSString *type;
-(id) initWithType:(NSString*) type;
@end
