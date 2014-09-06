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
@implementation IndexViewController
@synthesize imageIndex = _imageIndex;
@synthesize pointImageViews = _pointImageViews;
@synthesize menuBgImageViews= _menuBgImageViews;
@synthesize menuButtonViews = _menuButtonViews;
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
        
    }
    return self;
}
-(void)loadView{
    [super loadView];
    self.navigationController.navigationBarHidden = YES;
    self.view.backgroundColor = [UIColor whiteColor];
    CGRect frame = [[UIScreen mainScreen] bounds];
    CGFloat height = 0;
    #ifdef __IPHONE_7_0
    if(DEVICE_VERSION>=7.0){
        height = 22;
    }
    #endif
    [self.view addSubview:[self createImageViewFromNamedImage:@"main_background.png" frame:CGRectMake(0, height, 320, frame.size.height-height)]];
    [self.view addSubview:[self createImageViewFromNamedImage:@"title_logo_v15.png" frame:CGRectMake(2, 2+height, 24, 26)]];
    [self.view addSubview:[self createLabel:@"华测自动化检测与预警系统" frame:CGRectMake(27, height-2, 200, 30) textColor:@"#ffffff" font:15 backgroundColor:nil textAlignment:ALIGN_LEFT]];
    [self.view addSubview:[self createImageViewFromNamedImage:@"clear.png" frame:CGRectMake(290, 2+height, 26, 26)]];
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
    [self.view addSubview:[self createMenuView:height+178]];
    [self.view addSubview:[self createSettingView:height+308]];
    [self.view addSubview:[self createFooterView]];
    self.backgroundShadowView = [[[UIView alloc] initWithFrame:frame] autorelease];
    

}
-(void)showProjectSettingPopupView{
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
-(void)showSettingPopupView{
    [self.view addSubview:self.backgroundShadowView];
    CGFloat height = 480;
    if(IS_IPHONE5){
        height=568;
    }
    CGFloat popupViewHeight = 200;
    if([self.settingKey isEqual:PROJECT_NAME]){
        popupViewHeight = 300;
    }
    CGFloat heightOffset = 0;
    self.popupView = [[[UIView alloc] initWithFrame:CGRectMake(30, (height-popupViewHeight)/2, 260, popupViewHeight)] autorelease];
    self.popupView.layer.masksToBounds=YES;
    self.popupView.layer.cornerRadius =6;
    self.popupView.backgroundColor = [UIColor colorWithRed:0 green:0 blue:0 alpha:0.6f];

    [self.popupView addSubview:[self createLabel:self.settingName frame:CGRectMake(20, 0, 220, 60) textColor:@"#ffffff" font:24 backgroundColor:nil textAlignment:ALIGN_LEFT]];
    if([self.settingKey isEqual:PROJECT_NAME]){
        heightOffset = 140+70;
        self.projectPicker = [[[UIPickerView alloc] initWithFrame:CGRectMake(20, 70, 220, 140)] autorelease];
        self.projectPicker = [[[UIPickerView alloc] initWithFrame:CGRectMake(20, 70, 220, 140)] autorelease];
        self.projectPicker.dataSource = self;
        self.projectPicker.delegate = self;
        self.projectPicker.layer.masksToBounds=YES;
        self.projectPicker.layer.cornerRadius =6;
        self.projectPicker.backgroundColor = [UIColor colorWithWhite:15 alpha:0.7];

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
        NSInteger rowIndex = [self.projectPicker numberOfRowsInComponent:1];
        [self setValue:@"1" byKey:self.settingKey];
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
    }];
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
    UIButton* buttonView = (UIButton*)sender;
    long index = [self.menuButtonViews indexOfObject:buttonView];
    for(int i=0;i<self.menuBgImageViews.count;i++){
        UIImageView* bgImageView = [self.menuBgImageViews objectAtIndex:i];
        if(i==index){
            bgImageView.image = [UIImage imageNamed:@"bg_press.png"];
        }else{
            bgImageView.image = [UIImage imageNamed:@"bg_nopress.png"];
        }
    }
}
-(UIView*)createMenuView:(int)y{
    UIScrollView* menuView = [[[UIScrollView alloc] initWithFrame:CGRectMake(0, y, 320, 130)] autorelease];
    NSArray* imageArray = [NSArray arrayWithObjects:@"logo_xtgl.png",@"logo_ksw.png",@"logo_zxgt.png",@"logo_dqwd.png",@"logo_dqsd.png",@"logo_dqyl.png",@"logo_fs.png",@"logo_fx.png",@"logo_yl.png",@"logo_bmwy.png",@"logo_jrx.png",@"logo_nbwy.png",@"logo_sl.png",@"logo_tyl.png",@"logo_thsl.png",@"logo_lf.png",@"logo_rxwy.png",@"logo_wd.png", nil];
    NSArray* nameArray = [NSArray arrayWithObjects:@"系统设置",@"库水位",@"干滩",@"大气温度",@"大气湿度",@"大气气压",@"风速",@"风向",@"雨量",@"表面位移",@"浸润线",@"内部位移",@"滲流",@"土压力",@"土壤含水率",@"裂缝",@"柔性位移",@"土壤温度", nil];
    
    
    for(int i=0;i<imageArray.count;i++){
        int yoffset = 0;
        long xoffset = 0;
        if(i>=imageArray.count/2){
            yoffset = 62;
            xoffset =-imageArray.count/2;
        }
        
        NSString* image =[imageArray objectAtIndex:i];
        NSString* name =[nameArray objectAtIndex:i];
        UIButton* buttonView = [[UIButton alloc] initWithFrame:CGRectMake(16+(i+xoffset)*72, 5+yoffset, 40, 40)];
        [buttonView setImage:[UIImage imageNamed:image] forState:UIControlStateNormal];
        [buttonView addTarget:self action:@selector(selectMenu:) forControlEvents:UIControlEventTouchUpInside];
        UIImageView* bgImageView = [self createImageViewFromNamedImage:@"bg_nopress.png" frame:CGRectMake(1+(i+xoffset)*72, 0+yoffset, 70, 60)];
        [self.menuBgImageViews addObject:bgImageView];
        [self.menuButtonViews addObject:buttonView];
        [menuView addSubview:bgImageView];
        [menuView addSubview:buttonView];
        [menuView addSubview:[self createLabel:name frame:CGRectMake(2+(i+xoffset)*72, 45+yoffset, 70, 12) textColor:@"#000000" font:12 backgroundColor:nil textAlignment:ALIGN_CENTER]];
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
    settingView.contentSize = CGSizeMake(320, 400);
    TTTableView* tableView = [[[TTTableView alloc] initWithFrame:CGRectMake(0, 0, 320, 400)] autorelease];
    tableView.scrollEnabled=NO;

    TTSectionedDataSource* ds = [TTSectionedDataSource dataSourceWithObjects:
        @"设置",
        [self createTableItemByTitle:@"服务地址" subTitle:@"请正确填写服务端地址" action:@selector(showServerSettingPopupView)],
        [self createTableItemByTitle:@"工程项目" subTitle:@"请勾选需要查看的工程" action:@selector(showProjectSettingPopupView)],
        [self createTableItemByTitle:@"更新频率" subTitle:@"设置刷新周期(秒)" action:@selector(showFrequencySettingPopupView)],
        @"报警模式",
        [self createTableItemByTitle:@"报警开/关" subTitle:@"请勾选是否开启报警" action:nil],
        [self createTableItemByTitle:@"情景模式" subTitle:@"报警情景模式" action:nil],
        @"关于",
        [TTTableStyledTextItem itemWithText:[TTStyledText textFromXHTML:[NSString stringWithFormat:@"<span class=\"settingRow3\">软件名称:华测自动化监测与预警系统（手机版）</span><br/><span class=\"settingRow3\">版本:1.0</span><br/><span class=\"settingRow3\">版权:上海华测导航技术有限公司</span><br/><span class=\"settingRow3\">网址:www.huace.cn</span>"] lineBreaks:YES URLs:YES]],
                                 nil];
    tableView.dataSource = ds;
    tableView.delegate = self;
    tableView.backgroundColor = [UIColor blackColor];
    #ifdef __IPHONE_7_0
    if(DEVICE_VERSION>=7.0){
        tableView.separatorInset = UIEdgeInsetsZero;
        tableView.sectionIndexBackgroundColor=[UIColor blackColor];
    }
    #endif
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
    #ifdef __IPHONE_7_0
    if(DEVICE_VERSION>=7.0){
        height = height+22;
    }
    #endif
    
    UIView* footerView = [[[UIView alloc] initWithFrame:CGRectMake(0, height-30, 320, 30)] autorelease];
    footerView.backgroundColor = [StyleSheet colorFromHexString:@"#000080"];
    self.footLabel =[self createLabel:@"请登录" frame:CGRectMake(10,0,100,30) textColor:@"#ff0000" font:16 backgroundColor:nil textAlignment:ALIGN_LEFT];
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
    if(indexPath.row==0&&indexPath.section==0){
        [self showServerSettingPopupView];
    }else if(indexPath.row==1&&indexPath.section==0){
        [self showProjectSettingPopupView];
    }else if(indexPath.row==2&&indexPath.section==0){
        [self showFrequencySettingPopupView];
    }
}
- (void)tableView:(UITableView *)tableView willDisplayCell:(UITableViewCell *)cell forRowAtIndexPath:(NSIndexPath *)indexPath
{
    if([cell isKindOfClass:[TTStyledTextTableItemCell class]]){
        TTStyledTextTableItemCell* itemCell = (TTStyledTextTableItemCell*)cell;
        itemCell.label.backgroundColor = [UIColor clearColor];
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
    return 2;
}

- (NSString *)pickerView:(UIPickerView *)pickerView titleForRow:(NSInteger)row forComponent:(NSInteger)component{
    if(row==1){
        return @"1";
    }else{
        return @"2";
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
    self.footLabel.text = user.username;
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

#pragma mark -
#pragma mark MBProgressHUDDelegate methods

- (void)hudWasHidden:(MBProgressHUD *)hud {
    // Remove HUD from screen when the HUD was hidded
    [_progressHUD removeFromSuperview];
    [_progressHUD release];
    _progressHUD = nil;
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
