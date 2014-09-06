//
//  IndexViewController.h
//  HCMas
//
//  Created by zhang wei on 14-8-29.
//  Copyright (c) 2014年 HC. All rights reserved.
//

#import <UIKit/UIKit.h>
#import "LoginModel.h"
#import "MBProgressHUD.h"
@interface IndexViewController : UIViewController<UITableViewDelegate,UIPickerViewDataSource,UIPickerViewDelegate,LoginDelegate,MBProgressHUDDelegate,UITextFieldDelegate>{
    int _imageIndex;
    NSMutableArray* _pointImageViews;
    UIImageView* _topImageView;
    NSMutableArray* _menuBgImageViews;
    NSMutableArray* _menuButtonViews;
    UIView* _backgroundShadowView;
    UIView* _popupView;
    UITextField* _popupTextField;
    UITextField* _userNameTextField;
    UITextField* _passwordTextField;
    UISwitch* _rememberPasswordSwitch;
    NSString* _settingKey;
    NSString* _settingName;
    UIPickerView* _projectPicker;
    UILabel* _footLabel;
    LoginModel* _loginModel;
    MBProgressHUD* _progressHUD;
}
@property (nonatomic, assign) int imageIndex;
@property (nonatomic, retain) NSMutableArray* pointImageViews;
@property (nonatomic, retain) UIImageView* topImageView;
@property (nonatomic, retain) NSMutableArray* menuBgImageViews;
@property (nonatomic, retain) NSMutableArray* menuButtonViews;
@property (nonatomic, retain) UIView* backgroundShadowView;
@property (nonatomic, retain) UIView* popupView;
@property (nonatomic, retain) UITextField* popupTextField;
@property (nonatomic, copy) NSString* settingKey;
@property (nonatomic, copy) NSString* settingName;
@property (nonatomic, retain) UIPickerView* projectPicker;
@property (nonatomic, retain) UITextField* userNameTextField;
@property (nonatomic, retain) UITextField* passwordTextField;
@property (nonatomic, retain) UISwitch* rememberPasswordSwitch;
@property (nonatomic, retain) UILabel* footLabel;
@property (nonatomic, retain) MBProgressHUD    *progressHUD;
@end
