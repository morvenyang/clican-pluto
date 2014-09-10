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
#import "ProjectModel.h"
#import "KpiModel.h"
#import "SwipeScrollView.h"
#import "PMCalendar.h"
#import "KpiHistoryModel.h"
@interface IndexViewController : UIViewController<UITableViewDelegate,UIPickerViewDataSource,UIPickerViewDelegate,LoginDelegate,MBProgressHUDDelegate,UITextFieldDelegate,ProjectDelegate,KpiDelegate,SwitchDataDelegate,PMCalendarControllerDelegate,KpiHistoryDelegate>{
    int _imageIndex;
    NSMutableArray* _pointImageViews;
    UIImageView* _topImageView;
    NSMutableArray* _menuBgImageViews;
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
    NSArray* _projects;
    ProjectModel* _projectModel;
    UIView* _menuView;
    KpiModel* _kpiModel;
    KpiHistoryModel* _kpiHistoryModel;
    NSDictionary* _kpis;
    UIView* _footView;
    UIView* _dataView;
    UIView* _dataHistoryView;
    NSString* _kpiType;
    NSDate* _startDate;
    NSDate* _endDate;
    NSString* _pointName;
    NSArray* _pointNames;
    UIButton* _pointNameButton;
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
@property (nonatomic, retain) NSArray    *projects;
@property (nonatomic, retain) UIView    *menuView;
@property (nonatomic, retain) NSDictionary    *kpis;
@property (nonatomic, retain) UIView    *footView;
@property (nonatomic, retain) UIView    *dataView;
@property (nonatomic, retain) UIView    *dataHistoryView;
@property (nonatomic, copy) NSString    *kpiType;
@property (nonatomic, retain) NSDate    *startDate;
@property (nonatomic, retain) NSDate    *endDate;
@property (nonatomic, retain) NSString    *pointName;
@property (nonatomic, retain) NSArray    *pointNames;
@property (nonatomic, retain) UIButton    *pointNameButton;
@end
