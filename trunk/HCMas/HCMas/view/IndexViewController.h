//
//  IndexViewController.h
//  HCMas
//
//  Created by zhang wei on 14-8-29.
//  Copyright (c) 2014年 HC. All rights reserved.
//

#import <UIKit/UIKit.h>

@interface IndexViewController : UIViewController<UITableViewDelegate,UIPickerViewDataSource,UIPickerViewDelegate>{
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
@property (nonatomic, copy) UIPickerView* projectPicker;
@property (nonatomic, retain) UITextField* userNameTextField;
@property (nonatomic, retain) UITextField* passwordTextField;
@property (nonatomic, retain) UISwitch* rememberPasswordSwitch;
@property (nonatomic, retain) UILabel* footLabel;
@end
