//
//  IndexViewController.m
//  HCMas
//
//  Created by zhang wei on 14-8-29.
//  Copyright (c) 2014年 HC. All rights reserved.
//

#import "IndexViewController.h"
#import "StyleSheet.h"
#import "Constants.h"
#import "AppDelegate.h"
#import "MenuButton.h"
#import "KpiButton.h"
#import "DateButton.h"
#import "PointNameButton.h"

@implementation IndexViewController
@synthesize imageIndex = _imageIndex;
@synthesize pointImageViews = _pointImageViews;
@synthesize menuBgImageViews= _menuBgImageViews;

@synthesize topImageView = _topImageView;
@synthesize backgroundShadowView = _backgroundShadowView;
@synthesize popupView = _popupView;
@synthesize popupTextField = _popupTextField;
@synthesize settingKey = _settingKey;
@synthesize settingName = _settingName;
@synthesize projectPicker = _projectPicker;
@synthesize userNameTextField = _userNameTextField;
@synthesize passwordTextField = _passwordTextField;
@synthesize rememberPasswordSwitch = _rememberPasswordSwitch;
@synthesize footLabel = _footLabel;
@synthesize progressHUD = _progressHUD;
@synthesize projects = _projects;
@synthesize menuView = _menuView;
@synthesize kpis = _kpis;
@synthesize footView = _footView;
@synthesize dataView = _dataView;
@synthesize dataHistoryView = _dataHistoryView;
@synthesize kpiType = _kpiType;
@synthesize startDate = _startDate;
@synthesize endDate = _endDate;
@synthesize pointName = _pointName;
@synthesize pointNames = _pointNames;
@synthesize pointNameButton = _pointNameButton;
@synthesize webPieChartView = _webPieChartView;
@synthesize headLabel = _headlLabel;
- (id)initWithNibName:(NSString *)nibNameOrNil bundle:(NSBundle *)nibBundleOrNil
{
    self = [super initWithNibName:nibNameOrNil bundle:nibBundleOrNil];
    if (self) {
        self.imageIndex = 1;
        self.pointImageViews = [NSMutableArray array];
        self.menuBgImageViews =[NSMutableArray array];
        self.menuButtonViews =[NSMutableArray array];
        
        _loginModel = [[LoginModel alloc] init];
        _loginModel.delegate = self;
        
        _projectModel = [[ProjectModel alloc] init];
        _projectModel.delegate = self;
        
        _kpiModel = [[KpiModel alloc] init];
        _kpiModel.delegate = self;
        
        _kpiHistoryModel = [[KpiHistoryModel alloc] init];
        _kpiHistoryModel.delegate = self;
        
        _initModel = [[InitModel alloc] init];
        _initModel.delegate = self;
    }
    return self;
}
-(void)loadView{
    [super loadView];
    
    self.navigationController.navigationBarHidden = YES;
    self.view.backgroundColor = [UIColor whiteColor];
    CGRect frame = [[UIScreen mainScreen] bounds];
    CGFloat height = 0;

    if(DEVICE_VERSION>=7.0){
        height = 22;
    }

    [self.view addSubview:[self createImageViewFromNamedImage:@"main_background.png" frame:CGRectMake(0, height, 320, frame.size.height-height)]];
    [self.view addSubview:[self createImageViewFromNamedImage:@"title_logo_v15.png" frame:CGRectMake(2, 2+height, 24, 26)]];
    NSString* appName = [self getValueByKey:APP_NAME];
    if(appName==nil||[appName length]==0){
        appName =@"变形监测与预警系统";
    }
    self.headLabel =[self createLabel:appName frame:CGRectMake(27, height-2, 200, 30) textColor:@"#ffffff" font:15 backgroundColor:nil textAlignment:ALIGN_LEFT];
    [self.view addSubview:self.headLabel];
    self.topImageView = [self createImageViewFromNamedImage:@"default_pic_1.jpg" frame:CGRectMake(0, height+28, 320, 130)];
    [self.view addSubview:self.topImageView];
    
    UISwipeGestureRecognizer* swipeGestureRight = [[[UISwipeGestureRecognizer alloc] initWithTarget:self action:@selector(switchImage:)] autorelease];
    swipeGestureRight.direction = UISwipeGestureRecognizerDirectionRight;
    
    UISwipeGestureRecognizer* swipeGestureLeft = [[[UISwipeGestureRecognizer alloc] initWithTarget:self action:@selector(switchImage:)] autorelease];
    swipeGestureLeft.direction = UISwipeGestureRecognizerDirectionLeft;
    self.topImageView.userInteractionEnabled =YES;
    [self.topImageView addGestureRecognizer:swipeGestureLeft];
    [self.topImageView addGestureRecognizer:swipeGestureRight];
    NSDateFormatter *dateFormatter = [[[NSDateFormatter alloc] init] autorelease];
    [dateFormatter setDateFormat:@"yyyy年MM月dd日 EEEE"];
    
    [self.view addSubview:[self createLabel:[dateFormatter stringFromDate:[NSDate date]] frame:CGRectMake(10, height+158, 150, 20) textColor:@"#ffffff" font:12 backgroundColor:nil textAlignment:ALIGN_LEFT]];
    
    [self.view addSubview:[self createPaginationView:height+158]];
    self.menuView = [self createMenuView:height+178];
    [self.view addSubview:self.menuView];
    self.dataView = [self createSettingView:height+308];
    [self.view addSubview:self.dataView];
    self.footView =[self createFooterView];
    [self.view addSubview:self.footView];
    self.backgroundShadowView = [[[UIView alloc] initWithFrame:frame] autorelease];
    
    [_initModel loadInitData];
}
-(void)showPointNamePopupView{
    NSArray* kpiArray = [self.kpis objectForKey:self.kpiType];
    NSMutableArray* pns = [NSMutableArray array];
    for(Kpi* kpi in kpiArray){
        [pns addObject:kpi.pointName];
    }
    
    if(pns.count==0){
        TTAlert(@"没有可选择的点名");
        return;
    }
    self.pointNames = pns;
    self.settingKey = POINT_NAME;
    self.settingName = @"请选择点名";
    [self showSettingPopupView];
}
-(void)showProjectSettingPopupView{
    if(self.projects==nil||self.projects.count==0){
        TTAlert(@"没有可选择的工程");
        return;
    }
    self.settingKey = PROJECT_NAME;
    self.settingName = @"请选择工程";
    [self showSettingPopupView];
}
-(void)showFrequencySettingPopupView{
    self.settingKey =UPDATE_FREQUENCY_NAME;
    self.settingName = @"更新频率";
    [self showSettingPopupView];
}
-(void)showServerSettingPopupView{
    self.settingKey = BASE_URL_NAME;
    self.settingName = @"服务地址";
    [self showSettingPopupView];
}
-(void)showLoginPopupView{
    self.settingKey = LOGIN;
    CGFloat height = 480;
    if(IS_IPHONE5){
        height=568;
    }
    [self.view addSubview:self.backgroundShadowView];
    CGFloat popupViewHeight = 280;
    self.popupView = [[[UIView alloc] initWithFrame:CGRectMake(30, (height-popupViewHeight)/2, 260, popupViewHeight)] autorelease];
    self.popupView.layer.masksToBounds=YES;
    self.popupView.layer.cornerRadius =6;
    self.popupView.backgroundColor = [UIColor colorWithRed:0 green:0 blue:0 alpha:0.6f];
    [self.popupView addSubview:[self createLabel:@"帐号" frame:CGRectMake(20, 0, 220, 60) textColor:@"#ffffff" font:24 backgroundColor:nil textAlignment:ALIGN_LEFT]];
    self.userNameTextField =[self createTextFieldWithFrame:CGRectMake(20, 60, 220, 30) defaultValue:[self getValueByKey:LAST_USER_NAME] security:NO];
    [self.popupView addSubview:self.userNameTextField];
    
    [self.popupView addSubview:[self createLabel:@"密码" frame:CGRectMake(20, 90, 220, 60) textColor:@"#ffffff" font:24 backgroundColor:nil textAlignment:ALIGN_LEFT]];
    NSString* rb = [self getValueByKey:REMEMBER_PASSWORD];
    if(rb!=nil&&[rb isEqualToString:@"true"]){
        self.passwordTextField =[self createTextFieldWithFrame:CGRectMake(20, 150, 220, 30) defaultValue:[self getValueByKey:LAST_PASSWORD] security:YES];
    }else{
        self.passwordTextField =[self createTextFieldWithFrame:CGRectMake(20, 150, 220, 30) defaultValue:nil security:YES];
    }
    
    [self.popupView addSubview:self.passwordTextField];
    
    [self.popupView addSubview:[self createLabel:@"记住密码" frame:CGRectMake(20, 180, 120, 60) textColor:@"#ffffff" font:24 backgroundColor:nil textAlignment:ALIGN_LEFT]];
    self.rememberPasswordSwitch = [[[UISwitch alloc] initWithFrame:CGRectMake(150, 195, 80, 40)] autorelease];
    if(rb!=nil&&[rb isEqualToString:@"true"]){
        self.rememberPasswordSwitch.on =YES;
    }
    [self.popupView addSubview:self.rememberPasswordSwitch];
    UIButton* saveButton = [UIButton buttonWithType:UIButtonTypeCustom];
    
    [saveButton setImage:[UIImage imageNamed:@"blue_button.png"] forState:UIControlStateNormal];
    [saveButton addTarget:self action:@selector(saveSetting) forControlEvents:UIControlEventTouchUpInside];
    [saveButton addSubview:[self createLabel:@"登录" frame:CGRectMake(0, 0, 70, 30) textColor:@"#ffffff" font:22 backgroundColor:nil textAlignment:ALIGN_CENTER]];
    saveButton.frame = CGRectMake(20, 240, 70, 30);
    UIButton* cancelButton = [UIButton buttonWithType:UIButtonTypeCustom];
    [cancelButton setImage:[UIImage imageNamed:@"blue_button_dark.png"] forState:UIControlStateNormal];
    [cancelButton addTarget:self action:@selector(cancelSetting) forControlEvents:UIControlEventTouchUpInside];
    [cancelButton addSubview:[self createLabel:@"取消" frame:CGRectMake(0, 0, 70, 30) textColor:@"#ffffff" font:22 backgroundColor:nil textAlignment:ALIGN_CENTER]];
    cancelButton.frame = CGRectMake(170, 240, 70, 30);
    [self.popupView addSubview:saveButton];
    [self.popupView addSubview:cancelButton];
    [self.backgroundShadowView addSubview:self.popupView];
    self.backgroundShadowView.backgroundColor = [UIColor colorWithRed:0 green:0 blue:0 alpha:0.3f];
    [UIView animateWithDuration:0.25f animations:^{
        self.popupView.alpha = 1;
        [self.popupView layoutIfNeeded];
    }];
}
-(void)showDetailKpi:(id*)sender{
    _popup = YES;
    KpiButton* kpiButton = (KpiButton*)sender;
    Kpi* kpi = kpiButton.kpi;
    CGFloat height = 480;
    if(IS_IPHONE5){
        height=568;
    }
    [self.view addSubview:self.backgroundShadowView];
    CGFloat popupViewHeight = 240;
    self.popupView = [[[UIView alloc] initWithFrame:CGRectMake(30, (height-popupViewHeight)/2, 260, popupViewHeight)] autorelease];
    self.popupView.layer.masksToBounds=YES;
    self.popupView.layer.cornerRadius =6;
    self.popupView.backgroundColor = [UIColor colorWithRed:0 green:0 blue:0 alpha:0.6f];
    
    [self.popupView addSubview:[self createLabel:@"点名:" frame:CGRectMake(10, 0, 120, 20) textColor:@"#ffffff" font:16 backgroundColor:nil textAlignment:ALIGN_LEFT]];

    
    [self.popupView addSubview:[self createLabel:kpi.pointName frame:CGRectMake(140, 0, 100, 20) textColor:@"#ffffff" font:16 backgroundColor:nil textAlignment:ALIGN_LEFT]];
    
    [self.popupView addSubview:[self createLabel:@"时间:" frame:CGRectMake(10, 20, 120, 20) textColor:@"#ffffff" font:16 backgroundColor:nil textAlignment:ALIGN_LEFT]];
    
    NSDateFormatter *dateFormatter = [[[NSDateFormatter alloc] init] autorelease];
    [dateFormatter setDateFormat:@"hh:mm:ss"];
    NSNumberFormatter* formatter = [[[NSNumberFormatter alloc]init] autorelease];
    [formatter setMinimumIntegerDigits:1];
    [formatter setMaximumFractionDigits:2];
    [formatter setMinimumFractionDigits:0];
    [self.popupView addSubview:[self createLabel:[dateFormatter stringFromDate:kpi.dacTime] frame:CGRectMake(140, 20, 100, 20) textColor:@"#ffffff" font:16 backgroundColor:nil textAlignment:ALIGN_LEFT]];
    NSString* kpiType = kpi.type;
    if([kpiType isEqualToString:@"Surface"]){
        [self.popupView addSubview:[self createLabel:@"3D:" frame:CGRectMake(10, 40, 120, 20) textColor:@"#ffffff" font:16 backgroundColor:nil textAlignment:ALIGN_LEFT]];
        
        [self.popupView addSubview:[self createLabel:[formatter stringFromNumber:kpi.d3] frame:CGRectMake(140, 40, 100, 20) textColor:@"#ffffff" font:16 backgroundColor:nil textAlignment:ALIGN_LEFT]];
        
        [self.popupView addSubview:[self createLabel:@"今日变化量:" frame:CGRectMake(10, 60, 120, 20) textColor:@"#ffffff" font:16 backgroundColor:nil textAlignment:ALIGN_LEFT]];
        
        [self.popupView addSubview:[self createLabel:[formatter stringFromNumber:kpi.todayChangeValue] frame:CGRectMake(140, 60, 100, 20) textColor:@"#ffffff" font:16 backgroundColor:nil textAlignment:ALIGN_LEFT]];
        
        [self.popupView addSubview:[self createLabel:@"昨日变化量:" frame:CGRectMake(10, 80, 120, 20) textColor:@"#ffffff" font:16 backgroundColor:nil textAlignment:ALIGN_LEFT]];
        
        [self.popupView addSubview:[self createLabel:[formatter stringFromNumber:kpi.yesterdayChangeValue] frame:CGRectMake(140, 80, 100, 20) textColor:@"#ffffff" font:16 backgroundColor:nil textAlignment:ALIGN_LEFT]];
        
        [self.popupView addSubview:[self createLabel:@"最近七天变化量:" frame:CGRectMake(10, 100, 120, 20) textColor:@"#ffffff" font:16 backgroundColor:nil textAlignment:ALIGN_LEFT]];
        
        [self.popupView addSubview:[self createLabel:[formatter stringFromNumber:kpi.weekChangeValue] frame:CGRectMake(140, 100, 100, 20) textColor:@"#ffffff" font:16 backgroundColor:nil textAlignment:ALIGN_LEFT]];
        
        [self.popupView addSubview:[self createLabel:@"X:" frame:CGRectMake(10, 120, 120, 20) textColor:@"#ffffff" font:16 backgroundColor:nil textAlignment:ALIGN_LEFT]];
        
        [self.popupView addSubview:[self createLabel:[NSString stringWithFormat:@"%@",kpi.dis_x] frame:CGRectMake(140, 120, 100, 20) textColor:@"#ffffff" font:16 backgroundColor:nil textAlignment:ALIGN_LEFT]];
        if(kpi.alertGrade_x.intValue>0){
            [self.popupView addSubview:[self createImageViewFromNamedImage:[self getImageNameByGrade:kpi.alertGrade_x.intValue] frame:CGRectMake(200, 120, 20, 20) ]];
        }
        
        
        [self.popupView addSubview:[self createLabel:@"Y:" frame:CGRectMake(10, 140, 120, 20) textColor:@"#ffffff" font:16 backgroundColor:nil textAlignment:ALIGN_LEFT]];
        
        [self.popupView addSubview:[self createLabel:[NSString stringWithFormat:@"%@",kpi.dis_y] frame:CGRectMake(140, 140, 100, 20) textColor:@"#ffffff" font:16 backgroundColor:nil textAlignment:ALIGN_LEFT]];
        if(kpi.alertGrade_y.intValue>0){
            [self.popupView addSubview:[self createImageViewFromNamedImage:[self getImageNameByGrade:kpi.alertGrade_y.intValue] frame:CGRectMake(200, 140, 20, 20) ]];
        }
        
        [self.popupView addSubview:[self createLabel:@"Z:" frame:CGRectMake(10, 160, 120, 20) textColor:@"#ffffff" font:16 backgroundColor:nil textAlignment:ALIGN_LEFT]];
        
        [self.popupView addSubview:[self createLabel:[NSString stringWithFormat:@"%@",kpi.dis_h] frame:CGRectMake(140, 160, 100, 20) textColor:@"#ffffff" font:16 backgroundColor:nil textAlignment:ALIGN_LEFT]];
        if(kpi.alertGrade_h.intValue>0){
            [self.popupView addSubview:[self createImageViewFromNamedImage:[self getImageNameByGrade:kpi.alertGrade_h.intValue] frame:CGRectMake(200, 160, 20, 20) ]];
        }
        
    }else if([kpiType isEqualToString:@"Inner"]){
        [self.popupView addSubview:[self createLabel:@"X:" frame:CGRectMake(10, 40, 120, 20) textColor:@"#ffffff" font:16 backgroundColor:nil textAlignment:ALIGN_LEFT]];
        
        [self.popupView addSubview:[self createLabel:[NSString stringWithFormat:@"%@",kpi.dis_x] frame:CGRectMake(140, 40, 100, 20) textColor:@"#ffffff" font:16 backgroundColor:nil textAlignment:ALIGN_LEFT]];
        if(kpi.alertGrade_x.intValue>0){
                    [self.popupView addSubview:[self createImageViewFromNamedImage:[self getImageNameByGrade:kpi.alertGrade_x.intValue] frame:CGRectMake(200, 40, 20, 20) ]];
        }


        [self.popupView addSubview:[self createLabel:@"Y:" frame:CGRectMake(10, 60, 120, 20) textColor:@"#ffffff" font:16 backgroundColor:nil textAlignment:ALIGN_LEFT]];
        
        [self.popupView addSubview:[self createLabel:[NSString stringWithFormat:@"%@",kpi.dis_y] frame:CGRectMake(140, 60, 100, 20) textColor:@"#ffffff" font:16 backgroundColor:nil textAlignment:ALIGN_LEFT]];
        if(kpi.alertGrade_y.intValue>0){
             [self.popupView addSubview:[self createImageViewFromNamedImage:[self getImageNameByGrade:kpi.alertGrade_y.intValue] frame:CGRectMake(200, 60, 20, 20) ]];
        }
       
    }else{
        UILabel* valueLabel =[self createLabel:@"" frame:CGRectMake(10, 40, 120, 20) textColor:@"#ffffff" font:16 backgroundColor:nil textAlignment:ALIGN_LEFT];
        if([kpiType isEqualToString:@"Reservoir"]){
            valueLabel.text = @"水位高程(m)";
        }else if([kpiType isEqualToString:@"Saturation"]){
            valueLabel.text = @"水位高程(m)";
        }else if([kpiType isEqualToString:@"Rainfall"]){
            valueLabel.text = @"降雨量(mm)";
        }else if([kpiType isEqualToString:@"SeeFlow"]){
            valueLabel.text = @"流速(m/s)";
        }else if([kpiType isEqualToString:@"DryBeach"]){
            valueLabel.text = @"干滩长度(m)";
        }else if([kpiType isEqualToString:@"Tyl"]){
            valueLabel.text = @"压力";
        }else if([kpiType isEqualToString:@"Rxwy"]){
            valueLabel.text = @"柔性位移";
        }else if([kpiType isEqualToString:@"Lf"]){
            valueLabel.text = @"裂缝";
        }else if([kpiType isEqualToString:@"Wd"]){
            valueLabel.text = @"温度";
        }else if([kpiType isEqualToString:@"Thsl"]){
            valueLabel.text = @"土含水率";
        }else if([kpiType isEqualToString:@"Dqwd"]){
            valueLabel.text = @"大气温度";
        }else if([kpiType isEqualToString:@"Dqsd"]){
            valueLabel.text = @"大气湿度";
        }else if([kpiType isEqualToString:@"Dqyl"]){
            valueLabel.text = @"大气压力";
        }else if([kpiType isEqualToString:@"Fx"]){
            valueLabel.text = @"风向";
        }else if([kpiType isEqualToString:@"Fs"]){
            valueLabel.text = @"风速";
        }
        [self.popupView addSubview:valueLabel];
        
        [self.popupView addSubview:[self createLabel:[NSString stringWithFormat:@"%@",kpi.v1] frame:CGRectMake(140, 40, 100, 20) textColor:@"#ffffff" font:16 backgroundColor:nil textAlignment:ALIGN_LEFT]];
        if(kpi.alertGrade.intValue>0){
            [self.popupView addSubview:[self createImageViewFromNamedImage:[self getImageNameByGrade:kpi.alertGrade.intValue] frame:CGRectMake(200, 40, 20, 20) ]];
        }
        
    }
    
    UIButton* cancelButton = [UIButton buttonWithType:UIButtonTypeCustom];
    [cancelButton setImage:[UIImage imageNamed:@"blue_button_dark.png"] forState:UIControlStateNormal];
    [cancelButton addTarget:self action:@selector(cancelSetting) forControlEvents:UIControlEventTouchUpInside];
    [cancelButton addSubview:[self createLabel:@"返回" frame:CGRectMake(0, 0, 70, 30) textColor:@"#ffffff" font:22 backgroundColor:nil textAlignment:ALIGN_CENTER]];
    cancelButton.frame = CGRectMake(95, popupViewHeight-40, 70, 30);
    [self.popupView addSubview:cancelButton];
    [self.backgroundShadowView addSubview:self.popupView];
    self.backgroundShadowView.backgroundColor = [UIColor colorWithRed:0 green:0 blue:0 alpha:0.3f];
    [UIView animateWithDuration:0.25f animations:^{
        self.popupView.alpha = 1;
        [self.popupView layoutIfNeeded];
    }];
}
-(void)showSettingPopupView{
    _popup = YES;
    [self.view addSubview:self.backgroundShadowView];
    CGFloat height = 480;
    if(IS_IPHONE5){
        height=568;
    }
    CGFloat popupViewHeight = 200;
    if([self.settingKey isEqual:PROJECT_NAME]||[self.settingKey isEqual:POINT_NAME]){
        popupViewHeight = 300;
    }
    CGFloat heightOffset = 0;
    self.popupView = [[[UIView alloc] initWithFrame:CGRectMake(30, (height-popupViewHeight)/2, 260, popupViewHeight)] autorelease];
    self.popupView.layer.masksToBounds=YES;
    self.popupView.layer.cornerRadius =6;
    self.popupView.backgroundColor = [UIColor colorWithRed:0 green:0 blue:0 alpha:0.6f];

    [self.popupView addSubview:[self createLabel:self.settingName frame:CGRectMake(20, 0, 220, 60) textColor:@"#ffffff" font:24 backgroundColor:nil textAlignment:ALIGN_LEFT]];
    if([self.settingKey isEqual:PROJECT_NAME]||[self.settingKey isEqual:POINT_NAME]){
        heightOffset = 140+70;
        self.projectPicker = [[[UIPickerView alloc] initWithFrame:CGRectMake(20, 70, 220, 140)] autorelease];
        self.projectPicker = [[[UIPickerView alloc] initWithFrame:CGRectMake(20, 70, 220, 140)] autorelease];
        self.projectPicker.dataSource = self;
        self.projectPicker.delegate = self;
        self.projectPicker.layer.masksToBounds=YES;
        self.projectPicker.layer.cornerRadius =6;
        self.projectPicker.backgroundColor = [UIColor colorWithWhite:15 alpha:0.7];
        if([self.settingKey isEqual:PROJECT_NAME]){
            NSString* defaultIndex = [self getValueByKey:PROJECT_NAME];
            int di = defaultIndex.intValue;
            if(di>=self.projects.count){
                di = 0;
            }
            [self.projectPicker selectRow:di inComponent:0 animated:NO];
        }else{

            long di = 0;
            if(self.pointName!=nil){
                di =[self.pointNames indexOfObject:self.pointName];
            }
            if(di<0||di>=self.pointNames.count){
                di = 0;
            }
            [self.projectPicker selectRow:di inComponent:0 animated:NO];
        }
        
        [self.popupView addSubview:self.projectPicker];
    }else{
        heightOffset = 30+70;
        self.popupTextField =[self createTextFieldWithFrame:CGRectMake(20, 70, 220, 30) defaultValue:[self getValueByKey:self.settingKey] security:NO];
        [self.popupView addSubview:self.popupTextField];
    }
    
    UIButton* saveButton = [UIButton buttonWithType:UIButtonTypeCustom];

    [saveButton setImage:[UIImage imageNamed:@"blue_button.png"] forState:UIControlStateNormal];
    [saveButton addTarget:self action:@selector(saveSetting) forControlEvents:UIControlEventTouchUpInside];
    [saveButton addSubview:[self createLabel:@"确定" frame:CGRectMake(0, 0, 70, 30) textColor:@"#ffffff" font:22 backgroundColor:nil textAlignment:ALIGN_CENTER]];
    saveButton.frame = CGRectMake(20, heightOffset+40, 70, 30);
    UIButton* cancelButton = [UIButton buttonWithType:UIButtonTypeCustom];
    [cancelButton setImage:[UIImage imageNamed:@"blue_button_dark.png"] forState:UIControlStateNormal];
    [cancelButton addTarget:self action:@selector(cancelSetting) forControlEvents:UIControlEventTouchUpInside];
    [cancelButton addSubview:[self createLabel:@"取消" frame:CGRectMake(0, 0, 70, 30) textColor:@"#ffffff" font:22 backgroundColor:nil textAlignment:ALIGN_CENTER]];
    cancelButton.frame = CGRectMake(170, heightOffset+40, 70, 30);
    [self.popupView addSubview:saveButton];
    [self.popupView addSubview:cancelButton];
    [self.backgroundShadowView addSubview:self.popupView];
    self.backgroundShadowView.backgroundColor = [UIColor colorWithRed:0 green:0 blue:0 alpha:0.3f];
    [UIView animateWithDuration:0.25f animations:^{
        self.popupView.alpha = 1;
        [self.popupView layoutIfNeeded];
    }];
}
-(UITextField*) createTextFieldWithFrame:(CGRect)frame defaultValue:(NSString*) defaultValue security:(BOOL)security{
    UITextField* field = [[UITextField alloc] initWithFrame:frame];
    field.backgroundColor = [UIColor whiteColor];
    field.layer.borderWidth=1;
    field.layer.borderColor= [[StyleSheet colorFromHexString:@"#ff8040"] CGColor];
    field.font = [UIFont fontWithName:@"Microsoft YaHei" size:24];
    field.textColor = [StyleSheet colorFromHexString:@"#a3a3a3"];
    
    field.autocorrectionType = UITextAutocorrectionTypeNo;
    field.keyboardType = UIKeyboardTypeDefault;
    field.returnKeyType = UIReturnKeyDone;
    
    field.clearButtonMode = UITextFieldViewModeAlways;
    field.contentVerticalAlignment = UIControlContentHorizontalAlignmentCenter;
    field.text = defaultValue;
    field.secureTextEntry = security;
    field.delegate = self;
    return field;
}

- (BOOL)textFieldShouldReturn:(UITextField *)textField; {
    return [textField resignFirstResponder];
}

-(void)saveSetting{
    if([self.settingKey isEqualToString:PROJECT_NAME]){
        NSInteger rowIndex = [self.projectPicker selectedRowInComponent:0];
        Project* project = (Project*)[self.projects objectAtIndex:rowIndex];
        HCMasAppDelegate.selectedProject = project;
        [self setValue:[NSString stringWithFormat:@"%li",rowIndex] byKey:PROJECT_NAME];
        [self setValue:HCMasAppDelegate.selectedProject.serverUrl byKey:BASE_URL_NAME];
        [_kpiModel loadKpiByProjectId:HCMasAppDelegate.selectedProject.projectId];
    }else if([self.settingKey isEqualToString:POINT_NAME]){
        NSInteger rowIndex = [self.projectPicker selectedRowInComponent:0];
        self.pointName = (NSString*)[self.pointNames objectAtIndex:rowIndex];
        [self.pointNameButton setTitle:self.pointName forState:UIControlStateNormal];
    }else if([self.settingKey isEqualToString:LOGIN]){
         [self setValue:[self.userNameTextField.text stringByTrimmingCharactersInSet:[NSCharacterSet whitespaceCharacterSet]] byKey:LAST_USER_NAME];
        [self setValue:[self.passwordTextField.text stringByTrimmingCharactersInSet:[NSCharacterSet whitespaceCharacterSet]] byKey:LAST_PASSWORD];
        if(self.rememberPasswordSwitch.on){
            [self setValue:@"true" byKey:REMEMBER_PASSWORD];
        }else{
            [self setValue:@"false" byKey:REMEMBER_PASSWORD];
        }
        
        [_loginModel login:self.userNameTextField.text password:self.passwordTextField.text];
    }else{
        if([self.settingKey isEqualToString:UPDATE_FREQUENCY_NAME]){
            [HCMasAppDelegate startTimer];
        }
        [self setValue:[self.popupTextField.text stringByTrimmingCharactersInSet:[NSCharacterSet whitespaceCharacterSet]] byKey:self.settingKey];
        
    }

    [self cancelSetting];
}
-(void)cancelSetting{
    
    [UIView animateWithDuration:0.25f animations:^{
        self.popupView.alpha = 0;
        [self.popupView layoutIfNeeded];
    } completion:^(BOOL finished) {
        [self.popupView removeFromSuperview];
        [self.backgroundShadowView removeFromSuperview];
        _popup=NO;
    }];
}

-(void)doSearch{
//    NSDateFormatter* dateFormatter = [[[NSDateFormatter alloc] init] autorelease];
//    [dateFormatter setDateFormat:@"yyyyMMdd"];
//    self.pointName=@"Surface-1";
//    self.startDate = [dateFormatter dateFromString:@"20140115"];
//    self.endDate = [dateFormatter dateFromString:@"20140131"];
    if(self.startDate==nil||self.endDate==nil){
        TTAlert(@"请线选择时间");
        return;
    }
    _doSearch = TRUE;
    [_kpiHistoryModel loadHistoryKpiByProjectId:HCMasAppDelegate.selectedProject.projectId kpiType:self.kpiType pointName:self.pointName startDate:self.startDate endDate:self.endDate];
}
-(void)switchImage:(UISwipeGestureRecognizer*)gestureRecognizer{
    UISwipeGestureRecognizerDirection direction =gestureRecognizer.direction;
    if(direction==UISwipeGestureRecognizerDirectionLeft){
        if(self.imageIndex<4){
            self.imageIndex=self.imageIndex+1;
        }
    }else if(direction==UISwipeGestureRecognizerDirectionRight){
        if(self.imageIndex>1){
            self.imageIndex=self.imageIndex-1;
        }
    }
    self.topImageView.image = [UIImage imageNamed:[NSString stringWithFormat:@"default_pic_%i.jpg",self.imageIndex]];
    int i = 1;
    UIImage* lightImage= [UIImage imageNamed:@"img_ratio.png"];
    UIImage* highLightImage= [UIImage imageNamed:@"img_ratio_selected.png"];
    for(UIImageView* view in self.pointImageViews){
        if(i==self.imageIndex){
            view.image =highLightImage;
        }else{
            view.image =lightImage;
        }
        i++;
    }
}

-(UIView*) createPaginationView:(int)y{
    UIView* paginationView = [[[UIView alloc] initWithFrame:CGRectMake(250, y, 16*4, 20)] autorelease];
    UIImage* lightImage= [UIImage imageNamed:@"img_ratio.png"];
    UIImage* highLightImage= [UIImage imageNamed:@"img_ratio_selected.png"];
    paginationView.backgroundColor = [UIColor clearColor];
    for(int i=1;i<=4;i++){
        UIImageView* v = [[[UIImageView alloc] initWithFrame:CGRectMake(16*(i-1), 5, 6, 6)] autorelease];
        if(i==self.imageIndex){
            v.image = highLightImage;
        }else{
            v.image = lightImage;
        }
        [self.pointImageViews addObject:v];
        [paginationView addSubview:v];
    }
    return paginationView;
}
-(void)selectMenu:(id*)sender{
    if(self.dataView){
        [self.dataView removeFromSuperview];
        self.dataView = nil;
    }
    
    if(self.footView){
        [self.footView removeFromSuperview];
        self.footView = nil;
    }
    if(self.dataHistoryView){
        [self.dataHistoryView removeFromSuperview];
        self.dataHistoryView = nil;
    }
    _current =YES;
    self.startDate = nil;
    self.endDate = nil;
    self.pointName = nil;
    MenuButton* buttonView = (MenuButton*)sender;
    NSString* kpiType = buttonView.type;
    self.kpiType = kpiType;
    CGFloat height = 0;

    if(DEVICE_VERSION>=7.0){
        height = 22;
    }

    if([kpiType isEqualToString:SYSTEM_CONFIG]){
        self.dataView=[self createSettingView:height+308];
        [self.view addSubview:self.dataView];
        self.footView =[self createFooterView];
        [self.view addSubview:self.footView];
    }else{
        NSArray* kpiArray = [self.kpis objectForKey:kpiType];
        self.dataView=[self createDataView:kpiArray y:height+308];
        [self.view addSubview:self.dataView];
        self.footView = nil;
    }
    long index = [self.menuBgImageViews indexOfObject:buttonView];
    for(int i=0;i<self.menuBgImageViews.count;i++){
        MenuButton* bgImageView = [self.menuBgImageViews objectAtIndex:i];
        if(i==index){
            [bgImageView setImage:[UIImage imageNamed:@"bg_press.png"] forState:UIControlStateNormal];
        }else{
             [bgImageView setImage:[UIImage imageNamed:@"bg_nopress.png"] forState:UIControlStateNormal];
        }
    }
    
}
-(UIView*)createDataHistoryView:(int)y{
    CGFloat height = 480;
    if(IS_IPHONE5){
        height=568;
    }
    UIScrollView* dataView = [[[UIScrollView alloc] initWithFrame:CGRectMake(0, y, 320, height-y)] autorelease];
    self.pointNameButton = [UIButton buttonWithType:UIButtonTypeCustom];
    if(self.pointName!=nil){
        [self.pointNameButton setTitle:self.pointName forState:UIControlStateNormal];
    }else{
        [self.pointNameButton setTitle:@"<点名>" forState:UIControlStateNormal];

    }
    self.pointNameButton.frame = CGRectMake(5, 0, 80, 20);
    self.pointNameButton.layer.masksToBounds=YES;
    self.pointNameButton.layer.cornerRadius=6;
    self.pointNameButton.layer.borderWidth = 1;
    self.pointNameButton.layer.borderColor = [UIColor blackColor].CGColor;
    [self.pointNameButton addTarget:self action:@selector(showPointNamePopupView) forControlEvents:UIControlEventTouchUpInside];
    DateButton* startDateButton = [DateButton buttonWithType:UIButtonTypeCustom];
    startDateButton.layer.masksToBounds=YES;
    startDateButton.layer.cornerRadius=6;
    startDateButton.layer.borderWidth = 1;
    startDateButton.layer.borderColor = [UIColor blackColor].CGColor;
    startDateButton.type = @"start";
    [startDateButton setTitle:@"<开始>" forState:UIControlStateNormal];
    startDateButton.frame = CGRectMake(85, 0, 60, 20);
    [startDateButton addTarget:self action:@selector(openCalendar:) forControlEvents:UIControlEventTouchUpInside];
    DateButton* endDateButton = [DateButton buttonWithType:UIButtonTypeCustom];
    endDateButton.layer.masksToBounds=YES;
    endDateButton.layer.cornerRadius=6;
    endDateButton.layer.borderWidth = 1;
    endDateButton.layer.borderColor = [UIColor blackColor].CGColor;
    endDateButton.type = @"end";
    [endDateButton setTitle:@"<结束>" forState:UIControlStateNormal];
    endDateButton.frame = CGRectMake(145, 0, 60, 20);
    [endDateButton addTarget:self action:@selector(openCalendar:) forControlEvents:UIControlEventTouchUpInside];
    UIButton* searchButton = [UIButton buttonWithType:UIButtonTypeCustom];
    [searchButton setTitle:@"查询" forState:UIControlStateNormal];
    searchButton.frame = CGRectMake(220, 0, 45, 20);
    [searchButton addTarget:self action:@selector(doSearch) forControlEvents:UIControlEventTouchUpInside];
    searchButton.layer.cornerRadius = 6;
    searchButton.layer.masksToBounds = YES;
    searchButton.layer.backgroundColor = [UIColor orangeColor].CGColor;
    
    UIButton* chartButton = [UIButton buttonWithType:UIButtonTypeCustom];
    [chartButton setTitle:@"全屏" forState:UIControlStateNormal];
    chartButton.frame = CGRectMake(270, 0, 45, 20);
    [chartButton addTarget:self action:@selector(openChart) forControlEvents:UIControlEventTouchUpInside];
    chartButton.layer.cornerRadius = 6;
    chartButton.layer.masksToBounds = YES;
    chartButton.layer.backgroundColor = [UIColor orangeColor].CGColor;
    
    [dataView addSubview:self.pointNameButton];
    [dataView addSubview:startDateButton];
    [dataView addSubview:endDateButton];
    [dataView addSubview:searchButton];
    [dataView addSubview:chartButton];
    return dataView;
}
-(UIView*)createDataView:(NSArray*)kpis y:(int)y{
    
    CGFloat height = 480;
    if(IS_IPHONE5){
        height=568;
    }
    CGFloat rowHeight = 20;
    UIScrollView* dataView = [[[UIScrollView alloc] initWithFrame:CGRectMake(0, y, 320, height-y)] autorelease];
    dataView.contentSize = CGSizeMake(320, rowHeight*(kpis.count+1));
    [dataView setBackgroundColor:[UIColor grayColor]];
    UIView* pointNameView = [[[UIView alloc] initWithFrame:CGRectMake(0, 0, 80, rowHeight*(kpis.count+1))] autorelease];
    UIScrollView* rightDataView = [[[UIScrollView alloc] initWithFrame:CGRectMake(80, 0,240, rowHeight*(kpis.count+1))] autorelease];
    [dataView addSubview:pointNameView];
    [dataView addSubview:rightDataView];
    [pointNameView addSubview:[self createLabel:@"点名" frame:CGRectMake(1, 0, 79, 20) textColor:@"#000000" font:16 backgroundColor:@"#ffc90e" textAlignment:ALIGN_CENTER]];
    CGFloat xOffset = 0;
    [rightDataView addSubview:[self createLabel:@"时间" frame:CGRectMake(xOffset+1, 0, 116, 20) textColor:@"#000000" font:16 backgroundColor:@"#ffc90e" textAlignment:ALIGN_CENTER]];
    xOffset+=116;
    [rightDataView addSubview:[self createLabel:@"状态" frame:CGRectMake(xOffset+1, 0, 49, 20) textColor:@"#000000" font:16 backgroundColor:@"#ffc90e" textAlignment:ALIGN_CENTER]];
    xOffset+=49;
    if([self.kpiType isEqualToString:@"Surface"]){
        [rightDataView addSubview:[self createLabel:@"3D(mm)" frame:CGRectMake(xOffset+1, 0, 59, 20) textColor:@"#000000" font:16 backgroundColor:@"#ffc90e" textAlignment:ALIGN_CENTER]];
        xOffset+=59;
        [rightDataView addSubview:[self createLabel:@"今日变化量(mm)" frame:CGRectMake(xOffset+1, 0, 119, 20) textColor:@"#000000" font:16 backgroundColor:@"#ffc90e" textAlignment:ALIGN_CENTER]];
        xOffset+=119;
        [rightDataView addSubview:[self createLabel:@"昨日变化量(mm)" frame:CGRectMake(xOffset+1, 0, 119, 20) textColor:@"#000000" font:16 backgroundColor:@"#ffc90e" textAlignment:ALIGN_CENTER]];
        xOffset+=119;
        [rightDataView addSubview:[self createLabel:@"最近七天变化量(mm)" frame:CGRectMake(xOffset+1, 0, 159, 20) textColor:@"#000000" font:16 backgroundColor:@"#ffc90e" textAlignment:ALIGN_CENTER]];
        xOffset+=159;
        [rightDataView addSubview:[self createLabel:@"X(mm)" frame:CGRectMake(xOffset+1, 0, 59, 20) textColor:@"#000000" font:16 backgroundColor:@"#ffc90e" textAlignment:ALIGN_CENTER]];
        xOffset+=59;
        [rightDataView addSubview:[self createLabel:@"Y(mm)" frame:CGRectMake(xOffset+1, 0, 59, 20) textColor:@"#000000" font:16 backgroundColor:@"#ffc90e" textAlignment:ALIGN_CENTER]];
        xOffset+=59;
        [rightDataView addSubview:[self createLabel:@"Z(mm)" frame:CGRectMake(xOffset+1, 0, 59, 20) textColor:@"#000000" font:16 backgroundColor:@"#ffc90e" textAlignment:ALIGN_CENTER]];
        xOffset+=59;
        rightDataView.contentSize = CGSizeMake(xOffset, rowHeight*(kpis.count+1));

    }else if([self.kpiType isEqualToString:@"Inner"]){
        [rightDataView addSubview:[self createLabel:@"X(mm)" frame:CGRectMake(xOffset+1, 0, 59, 20) textColor:@"#000000" font:16 backgroundColor:@"#ffc90e" textAlignment:ALIGN_CENTER]];
        xOffset+=59;
        [rightDataView addSubview:[self createLabel:@"Y(mm)" frame:CGRectMake(xOffset+1, 0, 59, 20) textColor:@"#000000" font:16 backgroundColor:@"#ffc90e" textAlignment:ALIGN_CENTER]];
        xOffset+=59;
        rightDataView.contentSize = CGSizeMake(xOffset, rowHeight*(kpis.count+1));
    }else{
        UILabel* valueLabel =[self createLabel:@"水位高程(m)" frame:CGRectMake(xOffset+1, 0, 106, 20) textColor:@"#000000" font:16 backgroundColor:@"#ffc90e" textAlignment:ALIGN_CENTER];
        xOffset+=106;
        rightDataView.contentSize = CGSizeMake(xOffset, rowHeight*(kpis.count+1));
        if([self.kpiType isEqualToString:@"Reservoir"]){
            valueLabel.text = @"水位高程(m)";
        }else if([self.kpiType isEqualToString:@"Saturation"]){
            valueLabel.text = @"水位高程(m)";
        }else if([self.kpiType isEqualToString:@"Rainfall"]){
            valueLabel.text = @"降雨量(mm)";
        }else if([self.kpiType isEqualToString:@"SeeFlow"]){
            valueLabel.text = @"流速(m/s)";
        }else if([self.kpiType isEqualToString:@"DryBeach"]){
            valueLabel.text = @"干滩长度(m)";
        }else if([self.kpiType isEqualToString:@"Tyl"]){
            valueLabel.text = @"压力(Mpa)";
        }else if([self.kpiType isEqualToString:@"Rxwy"]){
            valueLabel.text = @"位移(m)";
        }else if([self.kpiType isEqualToString:@"Lf"]){
            valueLabel.text = @"裂缝(m)";
        }else if([self.kpiType isEqualToString:@"Wd"]){
            valueLabel.text = @"温度(℃)";
        }else if([self.kpiType isEqualToString:@"Thsl"]){
            valueLabel.text = @"土含水率(%)";
        }else if([self.kpiType isEqualToString:@"Dqwd"]){
            valueLabel.text = @"温度(℃)";
        }else if([self.kpiType isEqualToString:@"Dqsd"]){
            valueLabel.text = @"湿度(%)";
        }else if([self.kpiType isEqualToString:@"Dqyl"]){
            valueLabel.text = @"气压(Mpa)";
        }else if([self.kpiType isEqualToString:@"Fx"]){
            valueLabel.text = @"风向(°)";
        }else if([self.kpiType isEqualToString:@"Fs"]){
            valueLabel.text = @"风速(m/s)";
        }
        [rightDataView addSubview:valueLabel];
    }
    NSDateFormatter* dateFormatter = [[[NSDateFormatter alloc] init] autorelease];
    [dateFormatter setDateFormat:@"yyyy/MM/dd HH:mm:ss"];
    for(int i=0;i<kpis.count;i++){
        Kpi* kpi = [kpis objectAtIndex:i];
        
        PointNameButton* pointNameButton = [PointNameButton buttonWithType:UIButtonTypeCustom];
        [pointNameButton setTitle:kpi.pointName forState:UIControlStateNormal];
        pointNameButton.pointName = kpi.pointName;
        [pointNameButton addTarget:self action:@selector(displayHistory:) forControlEvents:UIControlEventTouchUpInside];
        pointNameButton.backgroundColor=[UIColor whiteColor];
        [pointNameButton.titleLabel setFont:[UIFont systemFontOfSize: 12]];
        [pointNameButton setContentHorizontalAlignment:UIControlContentHorizontalAlignmentCenter];
        [pointNameButton setTitleColor:[UIColor blackColor] forState:UIControlStateNormal];
        pointNameButton.frame=CGRectMake(1, rowHeight*(i+1), 79, rowHeight);
        [pointNameView addSubview:pointNameButton];
        xOffset=0;
       [rightDataView addSubview:[self createLabel:[dateFormatter stringFromDate:kpi.dacTime] frame:CGRectMake(xOffset+1, rowHeight*(i+1), 116, rowHeight) textColor:@"#000000" font:12 backgroundColor:@"#ffffff" textAlignment:ALIGN_CENTER]];
         xOffset+=116;
        KpiButton* imageBgView = [KpiButton buttonWithType:UIButtonTypeCustom];
        imageBgView.kpi = kpi;
        [imageBgView addTarget:self action:@selector(showDetailKpi:) forControlEvents:UIControlEventTouchUpInside];
        imageBgView.backgroundColor=[UIColor whiteColor];
        imageBgView.frame=CGRectMake(xOffset+1, rowHeight*(i+1), 49, rowHeight);
        
        [rightDataView addSubview:imageBgView];
        if(kpi.alertGrade.intValue==0){
            [rightDataView addSubview:[self createLabel:@"正常" frame:CGRectMake(xOffset+1, rowHeight*(i+1), 49, rowHeight) textColor:@"#000000" font:12 backgroundColor:nil textAlignment:ALIGN_CENTER]];
        }else{
            [rightDataView addSubview:[self createImageViewFromNamedImage:[self getImageNameByGrade:kpi.alertGrade.intValue] frame:CGRectMake(xOffset+1+(49-rowHeight)/2, rowHeight*(i+1), rowHeight, rowHeight)]];
        }
        xOffset+=49;
        NSNumberFormatter* formatter = [[[NSNumberFormatter alloc]init] autorelease];
        [formatter setMinimumIntegerDigits:1];
        [formatter setMaximumFractionDigits:2];
        [formatter setMinimumFractionDigits:0];
        if([self.kpiType isEqualToString:@"Surface"]){
            [rightDataView addSubview:[self createLabel:[formatter stringFromNumber:kpi.d3] frame:CGRectMake(xOffset+1, rowHeight*(i+1), 59, 20) textColor:@"#000000" font:12 backgroundColor:@"#ffffff" textAlignment:ALIGN_CENTER]];
             xOffset+=59;
            [rightDataView addSubview:[self createLabel:[formatter stringFromNumber:kpi.todayChangeValue] frame:CGRectMake(xOffset+1, rowHeight*(i+1), 119, 20) textColor:@"#000000" font:12 backgroundColor:@"#ffffff" textAlignment:ALIGN_CENTER]];
             xOffset+=119;
            [rightDataView addSubview:[self createLabel:[formatter stringFromNumber:kpi.yesterdayChangeValue] frame:CGRectMake(xOffset+1, rowHeight*(i+1), 119, 20) textColor:@"#000000" font:12 backgroundColor:@"#ffffff" textAlignment:ALIGN_CENTER]];
            xOffset+=119;
            [rightDataView addSubview:[self createLabel:[formatter stringFromNumber:kpi.weekChangeValue] frame:CGRectMake(xOffset+1, rowHeight*(i+1), 159, 20) textColor:@"#000000" font:12 backgroundColor:@"#ffffff" textAlignment:ALIGN_CENTER]];
            xOffset+=159;
            [rightDataView addSubview:[self createLabel:[formatter stringFromNumber:kpi.dis_x] frame:CGRectMake(xOffset+1, rowHeight*(i+1), 59, 20) textColor:[self getTextColorByGrade:kpi.alertGrade_x.intValue] font:12 backgroundColor:@"#ffffff" textAlignment:ALIGN_CENTER]];
            xOffset+=59;
            [rightDataView addSubview:[self createLabel:[formatter stringFromNumber:kpi.dis_y] frame:CGRectMake(xOffset+1, rowHeight*(i+1), 59, 20) textColor:[self getTextColorByGrade:kpi.alertGrade_y.intValue] font:12 backgroundColor:@"#ffffff" textAlignment:ALIGN_CENTER]];
            xOffset+=59;
            [rightDataView addSubview:[self createLabel:[formatter stringFromNumber:kpi.dis_h] frame:CGRectMake(xOffset+1, rowHeight*(i+1), 59, 20) textColor:[self getTextColorByGrade:kpi.alertGrade_h.intValue] font:12 backgroundColor:@"#ffffff" textAlignment:ALIGN_CENTER]];
            xOffset+=59;
        }else if([self.kpiType isEqualToString:@"Inner"]){
            [rightDataView addSubview:[self createLabel:[formatter stringFromNumber:kpi.dis_x] frame:CGRectMake(xOffset+1, rowHeight*(i+1), 59, 20) textColor:[self getTextColorByGrade:kpi.alertGrade_x.intValue] font:12 backgroundColor:@"#ffffff" textAlignment:ALIGN_CENTER]];
            xOffset+=59;
            [rightDataView addSubview:[self createLabel:[formatter stringFromNumber:kpi.dis_y] frame:CGRectMake(xOffset+1, rowHeight*(i+1), 59, 20) textColor:[self getTextColorByGrade:kpi.alertGrade_y.intValue] font:12 backgroundColor:@"#ffffff" textAlignment:ALIGN_CENTER]];
            xOffset+=59;
        }else{
            [rightDataView addSubview:[self createLabel:[formatter stringFromNumber:kpi.v1] frame:CGRectMake(xOffset+1, rowHeight*(i+1), 106, 20) textColor:[self getTextColorByGrade:kpi.alertGrade_x.intValue] font:12 backgroundColor:@"#ffffff" textAlignment:ALIGN_CENTER]];
            xOffset+=106;
        }
        
    }
    return dataView;
}

-(NSString*)getUnitForKpiType:(NSString*)kt{
    if([kt isEqualToString:@"Reservoir"]){
        return @"m";
    }else if([kt  isEqualToString:@"Saturation"]){
        return @"m";
    }else if([kt isEqualToString:@"Rainfall"]){
        return @"mm";
    }else if([kt isEqualToString:@"SeeFlow"]){
        return @"m/s";
    }else if([kt isEqualToString:@"DryBeach"]){
        return @"m";
    }else if([kt isEqualToString:@"Tyl"]){
        return @"Mpa";
    }else if([kt isEqualToString:@"Rxwy"]){
        return @"m";
    }else if([kt isEqualToString:@"Lf"]){
        return @"m";
    }else if([kt isEqualToString:@"Wd"]){
        return @"℃";
    }else if([kt isEqualToString:@"Thsl"]){
        return @"%";
    }else if([kt isEqualToString:@"Dqwd"]){
        return @"℃";
    }else if([kt isEqualToString:@"Dqsd"]){
        return @"%";
    }else if([kt isEqualToString:@"Dqyl"]){
        return @"Mpa";
    }else if([kt isEqualToString:@"Fx"]){
        return @"°";
    }else if([kt isEqualToString:@"Fs"]){
        return @"m/s";
    }else{
        return @"";
    }
}
-(NSString*) getImageNameByGrade:(int)grade{
    NSString* imageName=nil;
    if(grade==0){
        imageName = @"on.jpg";
    }else if(grade==1){
        imageName = @"yellow.png";
    }else if(grade==2){
        imageName = @"orange.png";
    }else if(grade==3){
        imageName = @"red.png";
    }else{
        imageName = @"on.jpg";
    }
    return imageName;
}
-(NSString*) getTextColorByGrade:(int)grade{
    if(grade==0){
        return @"#000000";
    }else if(grade==1){
        return @"#00ff00";
    }else if(grade==2){
        return @"#ffff00";
    }else if(grade==3){
        return @"#ff0000";
    }else{
        return @"#000000";
    }
}
-(UIView*)createMenuView:(int)y{
    UIScrollView* menuView = [[[UIScrollView alloc] initWithFrame:CGRectMake(0, y, 320, 130)] autorelease];
    NSArray* imageArray = [NSArray arrayWithObjects:@"logo_xtgl.png",@"logo_ksw.png",@"logo_zxgt.png",@"logo_dqwd.png",@"logo_dqsd.png",@"logo_dqyl.png",@"logo_fs.png",@"logo_fx.png",@"logo_yl.png",@"logo_bmwy.png",@"logo_jrx.png",@"logo_nbwy.png",@"logo_sl.png",@"logo_tyl.png",@"logo_thsl.png",@"logo_lf.png",@"logo_rxwy.png",@"logo_wd.png", nil];

    NSArray* kpiTypeArray = [NSArray arrayWithObjects:SYSTEM_CONFIG,@"Reservoir",@"DryBeach",@"Dqwd",@"Dqsd",@"Dqyl",@"Fs",@"Fx",@"Rainfall",@"Surface",@"Saturation",@"Inner",@"SeeFlow",@"Tyl",@"Thsl",@"Lf",@"Rxwy",@"Wd", nil];
    
    int usedCount = 0;
    for(int i=0;i<imageArray.count;i++){
        int yoffset = 0;
        long xoffset = 0;
        if(usedCount>=imageArray.count/2){
            yoffset = 62;
            xoffset =-imageArray.count/2;
        }
         NSString* kpiType = [kpiTypeArray objectAtIndex:i];
        NSUInteger index = 0;
        if (i!=0) {
            if(HCMasAppDelegate.user==nil||HCMasAppDelegate.selectedProject==nil){
                continue;
            }
            index = [HCMasAppDelegate.selectedProject.kpis indexOfObject:kpiType];
            if (index==NSNotFound) {
                continue;
            }
        }
        NSString* image =[imageArray objectAtIndex:i];
        NSString* name = nil;
        
        if(i==0){
            name = @"系统设置";
        }else{
            name =[HCMasAppDelegate.selectedProject.kpiNames objectAtIndex:index];
        }
        UIImageView* imageView=[self createImageViewFromNamedImage:image frame:CGRectMake(16+(usedCount+xoffset)*72, 5+yoffset, 40, 40)];
        
        MenuButton* bgImageView = [[MenuButton alloc] initWithFrame:CGRectMake(1+(usedCount+xoffset)*72, 0+yoffset, 70, 60)];
        bgImageView.type = kpiType;
        [bgImageView setImage:[UIImage imageNamed:@"bg_nopress.png"] forState:UIControlStateNormal];
        [bgImageView addTarget:self action:@selector(selectMenu:) forControlEvents:UIControlEventTouchUpInside];
        
        [self.menuBgImageViews addObject:bgImageView];
        [menuView addSubview:bgImageView];
        [menuView addSubview:imageView];
        [menuView addSubview:[self createLabel:name frame:CGRectMake(2+(usedCount+xoffset)*72, 45+yoffset, 70, 12) textColor:@"#000000" font:12 backgroundColor:nil textAlignment:ALIGN_CENTER]];
        usedCount++;
    }
    
    
    menuView.contentSize =
    CGSizeMake(imageArray.count/2*72+2, 130);
    return menuView;
}

-(UIView*)createSettingView:(int)y{
    CGFloat height = 480;
    if(IS_IPHONE5){
        height=568;
    }

    UIScrollView* settingView = [[[UIScrollView alloc] initWithFrame:CGRectMake(0, y, 320, height-y-30)] autorelease];
    CGFloat contentHeight = 255;
    if(HCMasAppDelegate.selectedProject==nil||!HCMasAppDelegate.selectedProject.serverConfig){
        contentHeight = contentHeight - 50;
    }
    
    settingView.contentSize = CGSizeMake(320, contentHeight);
    TTTableView* tableView = [[[TTTableView alloc] initWithFrame:CGRectMake(0, 0, 320, contentHeight)] autorelease];
    tableView.scrollEnabled=NO;
    NSString* copyRight = [self getValueByKey:COPY_RIGHT];
    NSString* appName = [self getValueByKey:APP_NAME];
    if(copyRight==nil||[copyRight length]==0){
        copyRight = @"上海华测导航技术有限公司";
    }
    if(appName==nil||[appName length]==0){
        appName = @"变形监测与预警系统";
    }
    TTSectionedDataSource* ds = nil;
    if(HCMasAppDelegate.selectedProject!=nil&&HCMasAppDelegate.selectedProject.serverConfig){
        ds = [TTSectionedDataSource dataSourceWithObjects:
                                     @"设置",
                                     [self createTableItemByTitle:@"服务地址" subTitle:@"请正确填写服务端地址" action:@selector(showServerSettingPopupView)],
                                     [self createTableItemByTitle:@"工程项目" subTitle:@"请勾选需要查看的工程" action:@selector(showProjectSettingPopupView)],
                                     [self createTableItemByTitle:@"更新频率" subTitle:@"设置刷新周期(秒)" action:@selector(showFrequencySettingPopupView)],
                                     @"关于",
                                     [TTTableStyledTextItem itemWithText:[TTStyledText textFromXHTML:[NSString stringWithFormat:@"<span class=\"settingRow3\">软件名称:%@</span><br/><span class=\"settingRow3\">版本:%@</span><br/><span class=\"settingRow3\">版权:%@</span>",appName,VERSION,copyRight] lineBreaks:YES URLs:YES]],
                                     nil];
    }else{
        ds = [TTSectionedDataSource dataSourceWithObjects:
              @"设置",
              [self createTableItemByTitle:@"工程项目" subTitle:@"请勾选需要查看的工程" action:@selector(showProjectSettingPopupView)],
              [self createTableItemByTitle:@"更新频率" subTitle:@"设置刷新周期(秒)" action:@selector(showFrequencySettingPopupView)],
              @"关于",
              [TTTableStyledTextItem itemWithText:[TTStyledText textFromXHTML:[NSString stringWithFormat:@"<span class=\"settingRow3\">软件名称:%@</span><br/><span class=\"settingRow3\">版本:%@</span><br/><span class=\"settingRow3\">版权:%@</span>",appName,VERSION,copyRight] lineBreaks:YES URLs:YES]],
              nil];
    }
    
    tableView.dataSource = ds;
    tableView.delegate = self;
    tableView.backgroundColor = [UIColor blackColor];
    
    if([tableView respondsToSelector:@selector(setSeparatorInset:)]){
        [tableView setSeparatorInset:UIEdgeInsetsZero];
    }
    if([tableView respondsToSelector:@selector(setLayoutMargins:)]){
        [tableView setLayoutMargins:UIEdgeInsetsZero];
    }
    tableView.separatorColor = [UIColor blackColor];

    [settingView addSubview:tableView];
    return settingView;
}

-(UIView*)createFooterView{
    CGFloat height = 480;
    if(IS_IPHONE5){
        height=568;
    }
    height = height-22;

    if(DEVICE_VERSION>=7.0){
        height = height+22;
    }

    
    UIView* footerView = [[[UIView alloc] initWithFrame:CGRectMake(0, height-30, 320, 30)] autorelease];
    footerView.backgroundColor = [StyleSheet colorFromHexString:@"#000080"];
    self.footLabel =[self createLabel:@"请登录" frame:CGRectMake(10,0,100,30) textColor:@"#ff0000" font:16 backgroundColor:nil textAlignment:ALIGN_LEFT];
    if(HCMasAppDelegate.user.username!=nil&&HCMasAppDelegate.user.username.length>0){
        self.footLabel.text =HCMasAppDelegate.user.username;
    }
    [footerView addSubview:self.footLabel];
    UIButton* loginButton = [UIButton buttonWithType:UIButtonTypeCustom];
    [loginButton setImage:[UIImage imageNamed:@"blue_button.png"] forState:UIControlStateNormal];
    [loginButton addTarget:self action:@selector(showLoginPopupView) forControlEvents:UIControlEventTouchUpInside];
    loginButton.frame = CGRectMake(230, 1, 70, 28);
    [footerView addSubview:loginButton];
    [footerView addSubview:[self createLabel:@"登录" frame:CGRectMake(230,1,70,28) textColor:@"#ffffff" font:16 backgroundColor:nil textAlignment:ALIGN_CENTER]];
    return footerView;
}
-(TTTableStyledTextItem*)createTableItemByTitle:(NSString*) title subTitle:(NSString*) subTitle action:(SEL)action{
    TTTableStyledTextItem* item=[TTTableStyledTextItem itemWithText:[TTStyledText textFromXHTML:[NSString stringWithFormat:@"<span class=\"settingRow1\">%@</span><br/><span class=\"settingRow2\">%@</span>",title,subTitle] lineBreaks:YES URLs:NO]];
    return item;
}

- (CGFloat)tableView:(UITableView *)tableView heightForRowAtIndexPath:(NSIndexPath *)indexPath{
    if(indexPath.row==0&&indexPath.section==2){
        return 100;
    }else{
        return 50;
    }
}

- (void)tableView:(UITableView *)tableView didSelectRowAtIndexPath:(NSIndexPath *)indexPath{
    int row =indexPath.row;
    if(HCMasAppDelegate.selectedProject==nil||!HCMasAppDelegate.selectedProject.serverConfig){
        row++;
    }
    if(row==0&&indexPath.section==0){
        [self showServerSettingPopupView];
    }else if(row==1&&indexPath.section==0){
        [self showProjectSettingPopupView];
    }else if(row==2&&indexPath.section==0){
        [self showFrequencySettingPopupView];
    }
}
- (void)tableView:(UITableView *)tableView willDisplayCell:(UITableViewCell *)cell forRowAtIndexPath:(NSIndexPath *)indexPath
{
    if([cell isKindOfClass:[TTStyledTextTableItemCell class]]){
        TTStyledTextTableItemCell* itemCell = (TTStyledTextTableItemCell*)cell;
        if([itemCell respondsToSelector:@selector(setSeparatorInset:)]){
            [itemCell setSeparatorInset:UIEdgeInsetsZero];
        }
        if([itemCell respondsToSelector:@selector(setLayoutMargins:)]){
            [itemCell setLayoutMargins:UIEdgeInsetsZero];
        }        itemCell.label.backgroundColor = [UIColor clearColor];
        itemCell.backgroundView = [[UIImageView alloc] initWithImage:[UIImage imageNamed:@"main_background.png"]];
    }
}



-(NSString*)getValueByKey:(NSString*)key{
    NSUserDefaults* defaults = [NSUserDefaults standardUserDefaults];
    return [defaults objectForKey:key];
}

-(void)setValue:(NSString*)value byKey:(NSString*)key{
    NSUserDefaults* defaults = [NSUserDefaults standardUserDefaults];
    [defaults setObject:value forKey:key];
}

- (void)viewDidLoad
{
    [super viewDidLoad];
    // Do any additional setup after loading the view.
}

- (void)didReceiveMemoryWarning
{
    [super didReceiveMemoryWarning];
    // Dispose of any resources that can be recreated.
}
#ifdef __IPHONE_6_0
-(NSTextAlignment) getAlignment:(int)textAlignment{
    if(textAlignment==ALIGN_LEFT){
        return NSTextAlignmentLeft;
    }else if(textAlignment==ALIGN_CENTER){
        return NSTextAlignmentCenter;
    }else{
        return NSTextAlignmentRight;
    }
}
#else
-(UITextAlignment) getAlignment:(int)textAlignment{
    if(textAlignment==ALIGN_LEFT){
        return UITextAlignmentLeft;
    }else if(textAlignment==ALIGN_CENTER){
        return UITextAlignmentCenter;
    }else{
        return UITextAlignmentRight;
    }
}
#endif
-(UILabel*) createLabel:(NSString*) text frame:(CGRect) frame textColor:(NSString*) textColor font:(int) font backgroundColor:(NSString*) backgroundColor textAlignment:(int) textAlignment{
    UILabel* label = [[[UILabel alloc] initWithFrame:frame] autorelease];
    label.text = text;
    label.font = [UIFont systemFontOfSize:font];
    label.textColor = [StyleSheet colorFromHexString:textColor];
    label.textAlignment =[self getAlignment:textAlignment];
    if(backgroundColor==nil){
        label.backgroundColor = [UIColor clearColor];
    }else{
        label.backgroundColor = [StyleSheet colorFromHexString:backgroundColor];
    }
    
    return label;
}

-(UIImageView*) createImageViewFromNamedImage:(NSString*) imageName frame:(CGRect) frame{
    UIImage* image =[UIImage imageNamed:imageName];
    UIImageView* imageView = [[UIImageView alloc] initWithFrame:frame];
    
    imageView.image = image;
    return imageView;
}

- (UIImage *) createImageWithColor: (UIColor *) color
{
    CGRect rect=CGRectMake(0.0f, 0.0f, 1.0f, 1.0f);
    UIGraphicsBeginImageContext(rect.size);
    CGContextRef context = UIGraphicsGetCurrentContext();
    CGContextSetFillColorWithColor(context, [color CGColor]);
    CGContextFillRect(context, rect);
    
    UIImage *theImage = UIGraphicsGetImageFromCurrentImageContext();
    UIGraphicsEndImageContext();
    return theImage;
}

-(UIImageView*) createImageViewFromColor:(UIColor*) color frame:(CGRect) frame{
    UIImage* image =[self createImageWithColor:color];
    UIImageView* imageView = [[UIImageView alloc] initWithFrame:frame];
    
    imageView.image = image;
    return imageView;
}

// returns the number of 'columns' to display.
- (NSInteger)numberOfComponentsInPickerView:(UIPickerView *)pickerView{
    return 1;
}

// returns the # of rows in each component..
- (NSInteger)pickerView:(UIPickerView *)pickerView numberOfRowsInComponent:(NSInteger)component{
    if([self.settingKey isEqualToString:PROJECT_NAME]){
        return self.projects.count;
    }else{
        return self.pointNames.count;
    }
}

- (NSString *)pickerView:(UIPickerView *)pickerView titleForRow:(NSInteger)row forComponent:(NSInteger)component{
     if([self.settingKey isEqualToString:PROJECT_NAME]){
         Project* project = (Project*) [self.projects objectAtIndex:row];
         return project.projectName;
     }else{
         NSString* pointName = (NSString*) [self.pointNames objectAtIndex:row];
         return pointName;
     }
}


#pragma mark -
#pragma mark LoginDelegate

- (void)loginStart:(User*) user
{
    self.progressHUD = [[[MBProgressHUD alloc] initWithView:self.view] autorelease];
    self.progressHUD.delegate = self;
    self.progressHUD.labelText = @"登录中...";
    [self.view addSubview:self.progressHUD];
    [self.view bringSubviewToFront:self.progressHUD];
    [self.progressHUD show:YES];
    
}

- (void)loginSuccess:(User*) user
{
    [self.progressHUD hide:NO];
    
    HCMasAppDelegate.user = user;
    self.footLabel.text = user.username;
    [self setValue:user.appName byKey:APP_NAME];
    [self setValue:user.cr byKey:COPY_RIGHT];
//    if([self.kpiType isEqualToString:SYSTEM_CONFIG]){
//        CGFloat height = 0;
//        if(DEVICE_VERSION>=7.0){
//            height = 22;
//        }
//        if(self.dataView){
//            [self.dataView removeFromSuperview];
//            self.dataView = nil;
//        }
//        self.dataView = [self createSettingView:height+308];
//        [self.view addSubview:self.dataView];
//    }
    self.headLabel.text = user.appName;
    CGFloat height = 0;
    
    if(DEVICE_VERSION>=7.0){
        height = 22;
    }

    [self.menuView removeFromSuperview];
    self.menuView = [self createMenuView:height+178];
    [self.view addSubview:self.menuView];
    
    [_kpiModel loadKpiByProjectId:HCMasAppDelegate.selectedProject.projectId];
    [HCMasAppDelegate startTimer];
}
- (void)loginFailed:(NSError*) error message:(NSString*) message
{
    [self.progressHUD hide:NO];
    //-1004 connection is not available
    //-1001 timeout
    if([error code]==-1004||[error code]==-1001){
        TTAlert(@"请检查网络链接");
    }else{
        if(message){
            TTAlert(message);
        }else{
            TTAlert([error localizedDescription]);
        }
    }
    
}



#pragma mark -
#pragma mark MBProgressHUDDelegate methods
- (void)hudWasHidden:(MBProgressHUD *)hud {
    // Remove HUD from screen when the HUD was hidded
    [_progressHUD removeFromSuperview];
    [_progressHUD release];
    _progressHUD = nil;
}

#pragma mark -
#pragma mark ProjectDelegate methods
- (void)loadProjectStart{
    self.progressHUD = [[[MBProgressHUD alloc] initWithView:self.view] autorelease];
    self.progressHUD.delegate = self;
    self.progressHUD.labelText = @"加载项目...";
    [self.view addSubview:self.progressHUD];
    [self.view bringSubviewToFront:self.progressHUD];
    [self.progressHUD show:YES];
}
- (void)loadProjectSuccess:(NSArray*) projects{
    [self.progressHUD hide:NO];
    self.projects = projects;
    if(self.projects!=nil&&self.projects.count>0){
        NSString* defaultIndex = [self getValueByKey:PROJECT_NAME];
        int di = defaultIndex.intValue;
        if(di>=self.projects.count){
            di = 0;
        }
        HCMasAppDelegate.selectedProject = [self.projects objectAtIndex:di];
        [_kpiModel loadKpiByProjectId:HCMasAppDelegate.selectedProject.projectId];
    }
    CGFloat height = 0;

    if(DEVICE_VERSION>=7.0){
        height = 22;
    }

    [self.menuView removeFromSuperview];
    self.menuView = [self createMenuView:height+178];
    [self.view addSubview:self.menuView];
}
- (void)loadProjectFailed:(NSError*) error message:(NSString*) message{
    [self.progressHUD hide:NO];
    //-1004 connection is not available
    //-1001 timeout
    if([error code]==-1004||[error code]==-1001){
        TTAlert(@"请检查网络链接");
    }else{
        if(message){
            TTAlert(message);
        }else{
            TTAlert([error localizedDescription]);
        }
    }
}
#pragma mark -
#pragma mark KpiDelegate methods
- (void)loadKpiStart{
    self.progressHUD = [[[MBProgressHUD alloc] initWithView:self.view] autorelease];
    self.progressHUD.delegate = self;
    self.progressHUD.labelText = @"加载数据...";
    [self.view addSubview:self.progressHUD];
    [self.view bringSubviewToFront:self.progressHUD];
    [self.progressHUD show:YES];
}
- (void)loadKpiSuccess:(NSDictionary*) kpis{
    [self.progressHUD hide:NO];
    self.kpis = kpis;
    if(_current&&self.dataView!=nil&&![self.kpiType isEqualToString:SYSTEM_CONFIG]&&!_popup){
        NSLog(@"refresh current kpi");
        NSArray* kpiArray = [self.kpis objectForKey:self.kpiType];
        [self.dataView removeFromSuperview];
        CGFloat height = 0;
  
        if(DEVICE_VERSION>=7.0){
            height = 22;
        }

        self.dataView=[self createDataView:kpiArray y:height+308];
        [self.view addSubview:self.dataView];
    }
}
- (void)loadKpiFailed:(NSError*) error message:(NSString*) message{
    [self.progressHUD hide:NO];
    //-1004 connection is not available
    //-1001 timeout
    if([error code]==-1004||[error code]==-1001){
        TTAlert(@"请检查网络链接");
    }else{
        if(message){
            TTAlert(message);
        }else{
            TTAlert([error localizedDescription]);
        }
    }
}

- (void) displayCurrent{
    _current =YES;
    if(self.dataView){
        [self.view addSubview:self.dataView];
    }
}

- (void) displayHistory:(id)sender{
    _current = NO;
    PointNameButton* button = (PointNameButton*)sender;
    self.pointName = button.pointName;
    self.startDate = nil;
    self.endDate = nil;
    [self.dataView removeFromSuperview];
    CGFloat height = 0;

    if(DEVICE_VERSION>=7.0){
        height = 22;
    }

    self.dataHistoryView=[self createDataHistoryView:height+308];
    [self.view addSubview:self.dataHistoryView];
}
-(void)openChart{
    if(self.startDate==nil||self.endDate==nil){
        TTAlert(@"请线选择时间");
        return;
    }
    NSDateFormatter* dateFormatter = [[[NSDateFormatter alloc] init] autorelease];
    [dateFormatter setDateFormat:@"yyyyMMdd"];
    _doSearch = false;
    [_kpiHistoryModel loadHistoryKpiByProjectId:HCMasAppDelegate.selectedProject.projectId kpiType:self.kpiType pointName:self.pointName startDate:self.startDate endDate:self.endDate];

}
-(void)openCalendar:(id)sender{
    DateButton* button = (DateButton*)sender;
    NSDate* date =[NSDate date];
    if([button.type isEqualToString:@"start"]){
        if(self.startDate!=nil){
            date=self.startDate;
        }
    }else{
        if(self.endDate!=nil){
            date=self.endDate;
        }
    }
    PMCalendarController* pmCC = [[[PMCalendarController alloc] initWithDate:date] autorelease];
    pmCC.delegate = self;
    pmCC.allowsPeriodSelection = NO;
    pmCC.mondayFirstDayOfWeek = YES;
    
    [pmCC presentCalendarFromView:button
         permittedArrowDirections:PMCalendarArrowDirectionDown
                         animated:YES];
}
- (void)calendarController:(PMCalendarController *)calendarController didChangePeriod:(PMPeriod *)newPeriod
{
    DateButton* button = (DateButton*)calendarController.anchorView;
    
    NSDate* date = newPeriod.startDate;
    NSDateFormatter* dateFormatter = [[[NSDateFormatter alloc] init] autorelease];
    [dateFormatter setDateFormat:@"MM/dd"];
    [button setTitle:[dateFormatter stringFromDate:date] forState:UIControlStateNormal];
    if([button.type isEqualToString:@"start"]){
        self.startDate = date;
    }else{
        self.endDate = date;
    }
    [calendarController dismissCalendarAnimated:YES];
}

#pragma mark -
#pragma mark KpiHistoryDelegate methods
- (void)loadKpiHistoryStart{
    self.progressHUD = [[[MBProgressHUD alloc] initWithView:self.view] autorelease];
    self.progressHUD.delegate = self;
    self.progressHUD.labelText = @"加载数据...";
    [self.view addSubview:self.progressHUD];
    [self.view bringSubviewToFront:self.progressHUD];
    [self.progressHUD show:YES];
}
- (void)loadKpiHistorySuccess:(NSArray*) kpis{
    [self.progressHUD hide:NO];
    if(_doSearch){
        long width = 0;
        NSString* fileName = @"historyLineOther";
        if([self.kpiType isEqualToString:@"Surface"]){
            fileName= @"historyLineSurface";
            width = 300+kpis.count*50;
        }else if([self.kpiType isEqualToString:@"Inner"]){
            fileName= @"historyLineInner";
            width = 200+kpis.count*50;
        }else{
            width = 100+kpis.count*50;
        }
        if(width<900){
            width = 900;
        }
        UIScrollView* scrollView = [[[UIScrollView alloc] initWithFrame:CGRectMake(0,20,320,self.dataHistoryView.frame.size.height-20)] autorelease];
        if(self.webPieChartView){
            [self.webPieChartView removeFromSuperview];
        }
        self.webPieChartView = [[[UIWebView alloc] initWithFrame:CGRectMake(0,0,width/2,self.dataHistoryView.frame.size.height-20)] autorelease];
        self.webPieChartView.scalesPageToFit=YES;
        self.webPieChartView.userInteractionEnabled =YES;
        
        NSString* dataProvider = [[[NSString alloc] initWithData:[NSJSONSerialization dataWithJSONObject:kpis options:NSJSONWritingPrettyPrinted error:nil] encoding:NSUTF8StringEncoding] autorelease];
        
        NSString* htmlPath = [[NSBundle mainBundle] pathForResource:fileName ofType:@"html" inDirectory:@"web"];
        NSString* html = [NSString stringWithContentsOfFile:htmlPath encoding:NSUTF8StringEncoding error:nil];
        
        html=[html stringByReplacingOccurrencesOfString:@"$dataProvider" withString:dataProvider];
        html=[html stringByReplacingOccurrencesOfString:@"$width" withString:[NSString stringWithFormat:@"%li",width]];
        html=[html stringByReplacingOccurrencesOfString:@"$height" withString:[NSString stringWithFormat:@"%.0f",(self.dataHistoryView.frame.size.height-20)*2]];
        html=[html stringByReplacingOccurrencesOfString:@"$unit" withString:[self getUnitForKpiType:self.kpiType]];
        
        
        NSLog(@"%@",html);
        NSLog(@"%@",[NSString stringWithFormat:@"%@/web/",[[NSBundle mainBundle] bundlePath]]);
        [self.webPieChartView loadHTMLString:html baseURL:[NSURL fileURLWithPath:[NSString stringWithFormat:@"%@/web/",[[NSBundle mainBundle] bundlePath]]]];
        [scrollView addSubview:self.webPieChartView];
        [self.dataHistoryView addSubview:scrollView];
        scrollView.contentSize = CGSizeMake(width/2, self.dataHistoryView.frame.size.height-20);
    }else{
        HCMasAppDelegate.kpis = kpis;
        NSString* action = [NSString stringWithFormat:@"hcmas://chart/%@",self.kpiType];
        TTOpenURL(action);
    }
    
}
- (void)loadKpiHistoryFailed:(NSError*) error message:(NSString*) message{
    [self.progressHUD hide:NO];
    //-1004 connection is not available
    //-1001 timeout
    if([error code]==-1004||[error code]==-1001){
        TTAlert(@"请检查网络链接");
    }else{
        if(message){
            TTAlert(message);
        }else{
            TTAlert([error localizedDescription]);
        }
    }
}

- (void)initStart{
    self.progressHUD = [[[MBProgressHUD alloc] initWithView:self.view] autorelease];
    self.progressHUD.delegate = self;
    self.progressHUD.labelText = @"初始化数据...";
    [self.view addSubview:self.progressHUD];
    [self.view bringSubviewToFront:self.progressHUD];
    [self.progressHUD show:YES];
}
- (void)initSuccess:(NSArray*) projects{
    [self.progressHUD hide:NO];
    self.projects = projects;
    if(self.projects!=nil&&self.projects.count>0){
        NSString* defaultIndex = [self getValueByKey:PROJECT_NAME];
        int di = defaultIndex.intValue;
        if(di>=self.projects.count){
            di = 0;
        }
        Project* p =[self.projects objectAtIndex:di];
        NSLog(@"%@", p.projectName);
        HCMasAppDelegate.selectedProject = [self.projects objectAtIndex:di];
        [self setValue:HCMasAppDelegate.selectedProject.serverUrl byKey:BASE_URL_NAME];
    }else{
        TTAlert(@"服务器端初始化数据配置错误");
    }
    
}
- (void)initFailed:(NSError*) error message:(NSString*) message{
    [self.progressHUD hide:NO];
    //-1004 connection is not available
    //-1001 timeout
    if([error code]==-1004||[error code]==-1001){
        TTAlert(@"请检查网络链接");
    }else{
        if(message){
            TTAlert(message);
        }else{
            TTAlert([error localizedDescription]);
        }
    }
}
#pragma mark -
#pragma mark RefreshDelegate methods
-(void)refresh{
    if([self.kpiType isEqualToString:SYSTEM_CONFIG]){
        NSLog(@"At system config page, not refresh");
        return;
    }
    if(!_current){
        NSLog(@"At history page, not refresh");
        return;
    }
    if(_popup){
        NSLog(@"At popup page, not refresh");
        return;
    }

    NSLog(@"Reload current kpi from server");
    if(HCMasAppDelegate.selectedProject!=nil){
            [_kpiModel loadKpiByProjectId:HCMasAppDelegate.selectedProject.projectId];
    }
}
- (void)dealloc
{
    TT_RELEASE_SAFELY(_pointImageViews);
    TT_RELEASE_SAFELY(_topImageView);
    TT_RELEASE_SAFELY(_menuBgImageViews);
    TT_RELEASE_SAFELY(_menuButtonViews);
    [super dealloc];
}

@end
