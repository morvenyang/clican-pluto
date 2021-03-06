//
//  IndexViewController.m
//  dailyreport
//
//  Created by zhang wei on 14-5-17.
//  Copyright (c) 2014年 Peace Bird. All rights reserved.
//

#import "IndexViewController.h"
#import "IndexDataSource.h"
#import "StyleSheet.h"
#import "AppDelegate.h"
#import "IndexTableItem.h"

@implementation IndexViewController

@synthesize configView = _configView;
@synthesize backgroundView = _backgroundView;
@synthesize configButton = _configButton;
- (id)initWithNibName:(NSString *)nibNameOrNil bundle:(NSBundle *)nibBundleOrNil
{
    self = [super initWithNibName:nibNameOrNil bundle:nibBundleOrNil];
    if (self) {
        
    }
    self.variableHeightRows = YES;
    self.rowHeight = [UIImage imageNamed:@"首页女装"].size.height;
    return self;
}

- (void)createModel {
    IndexDataSource* ds = [[[IndexDataSource alloc] init] autorelease];
    self.dataSource = ds;
}

- (void)viewWillAppear:(BOOL)animated {
    #ifdef __IPHONE_7_0
        if(DEVICE_VERSION>=7.0){
            self.navigationController.navigationBar.barTintColor =[StyleSheet colorFromHexString:@"#EAEEF2"];
        }else{
            self.navigationBarTintColor=[StyleSheet colorFromHexString:@"#EAEEF2"];
        }
    #else
        self.navigationBarTintColor=[StyleSheet colorFromHexString:@"#EAEEF2"];
    #endif
    
    [super viewWillAppear:animated];
}
- (void)viewDidDisappear:(BOOL)animated {
    [self hideConfig];
    [super viewDidDisappear:animated];
}
-(void)updateDate{
    UILabel* label = [[[UILabel alloc] initWithFrame:CGRectZero] autorelease];
    label.backgroundColor = [UIColor clearColor];
    label.font =[UIFont systemFontOfSize:18];
    label.textAlignment = [self getAlignment:ALIGN_CENTER];
    label.textColor=[StyleSheet colorFromHexString:@"#323232"];
    NSDateFormatter* dateFormatter = [[[NSDateFormatter alloc] init] autorelease];
    [dateFormatter setTimeStyle:NSDateFormatterFullStyle];
    [dateFormatter setDateFormat:@"MM月dd日 EEE"];
    if(DrAppDelegate.user.date!=nil){
        label.text = [NSString stringWithFormat:@"%@%@",@"   ",[dateFormatter stringFromDate:DrAppDelegate.user.date]];
    }else{
        label.text = [NSString stringWithFormat:@"%@%@",@"   ",[dateFormatter stringFromDate:[[NSDate date] dateByAddingDays:-1]]];
    }
    self.navigationItem.titleView = label;
    [label sizeToFit];
}

-(void) loadView{
    [super loadView];
    
    self.tableView.backgroundColor =[UIColor blackColor];
    self.tableView.delegate = self;
    
    if([self.tableView respondsToSelector:@selector(setSeparatorInset:)]){
        [self.tableView setSeparatorInset:UIEdgeInsetsZero];
    }
    if([self.tableView respondsToSelector:@selector(setLayoutMargins:)]){
        [self.tableView setLayoutMargins:UIEdgeInsetsZero];
    }

    self.tableView.separatorColor = [UIColor blackColor];
    CGRect frame = [[UIScreen mainScreen] bounds];
    self.backgroundView =[[UIView alloc] initWithFrame:frame];
    
    UIImage* menuImage =[UIImage imageNamed:@"menu"];
    UIImage* downarrowImage =[UIImage imageNamed:@"downarrow"];
    UIImageView* configBackgroundImage =[[[UIImageView alloc] initWithFrame:CGRectMake(0, 0, menuImage.size.width, menuImage.size.height)] autorelease];
    configBackgroundImage.contentMode = UIViewContentModeScaleToFill;
    configBackgroundImage.image =menuImage;
    self.configView = [[UIView alloc] initWithFrame:CGRectMake(SCREEN_WIDTH-menuImage.size.width-10, 60, menuImage.size.width, menuImage.size.height)];
    [self.configView addSubview:configBackgroundImage];
    
    self.configButton = [UIButton buttonWithType:UIButtonTypeCustom];
    [self.configButton setImage:downarrowImage forState:UIControlStateNormal];

    self.configButton.frame =CGRectMake(0, 0, downarrowImage.size.width, downarrowImage.size.height);
    [self.configButton addTarget:self action:@selector(showConfig:) forControlEvents:UIControlEventTouchUpInside];
    
    UIBarButtonItem* configItem = [[[UIBarButtonItem alloc] initWithCustomView:self.configButton] autorelease];
    [self.navigationItem setRightBarButtonItem:configItem animated:YES];
    
    UIButton* titleButton = [UIButton buttonWithType:UIButtonTypeCustom];
    [titleButton setTitle:@"经营日报" forState:UIControlStateNormal];
    [titleButton setTitleColor:[StyleSheet colorFromHexString:@"#323232"] forState:UIControlStateNormal];
    titleButton.frame =CGRectMake(0, 0, 55, 20);
    titleButton.contentEdgeInsets=UIEdgeInsetsMake(0, 3, 0, 0);
    [titleButton sizeToFit];
    UIBarButtonItem* titleItem = [[UIBarButtonItem alloc] initWithCustomView:titleButton];
    [self.navigationItem setLeftBarButtonItem:titleItem];

    UIButton* logoutButton = [UIButton buttonWithType:UIButtonTypeCustom];
    logoutButton.titleEdgeInsets = UIEdgeInsetsMake(0, 45, 0, 0);
    [logoutButton setTitle:@"Exit" forState:UIControlStateNormal];
    [logoutButton setTitleColor:[UIColor whiteColor] forState:UIControlStateNormal];
    logoutButton.frame =CGRectMake(0, 55, 137, 48);
    logoutButton.contentVerticalAlignment = UIControlContentVerticalAlignmentCenter;
    logoutButton.contentHorizontalAlignment = UIControlContentHorizontalAlignmentLeft;
    [logoutButton addTarget:self action:@selector(logoutAction) forControlEvents:UIControlEventTouchUpInside];


    CGFloat buttonOffset = 4;
 
    UIButton* calendarButton = [UIButton buttonWithType:UIButtonTypeCustom];
    calendarButton.titleEdgeInsets = UIEdgeInsetsMake(0, 45, 0, 0);
    [calendarButton setTitle:@"选择日期" forState:UIControlStateNormal];
    [calendarButton setTitleColor:[UIColor whiteColor] forState:UIControlStateNormal];

    calendarButton.frame =CGRectMake(0, buttonOffset, menuImage.size.width, (menuImage.size.height-buttonOffset)/2);
    calendarButton.contentVerticalAlignment = UIControlContentVerticalAlignmentCenter;
    calendarButton.contentHorizontalAlignment = UIControlContentHorizontalAlignmentLeft;
    [calendarButton addTarget:self action:@selector(openCalendar:) forControlEvents:UIControlEventTouchUpInside];
    
    UIButton* gestureButton = [UIButton buttonWithType:UIButtonTypeCustom];
    gestureButton.titleEdgeInsets = UIEdgeInsetsMake(0, 45, 0, 0);
    [gestureButton setTitle:@"手势密码" forState:UIControlStateNormal];
    [gestureButton setTitleColor:[UIColor whiteColor] forState:UIControlStateNormal];
    
    gestureButton.frame =CGRectMake(0, buttonOffset+(menuImage.size.height-buttonOffset)/3, menuImage.size.width, (menuImage.size.height-buttonOffset)/3);
    gestureButton.contentVerticalAlignment = UIControlContentVerticalAlignmentCenter;
    gestureButton.contentHorizontalAlignment = UIControlContentHorizontalAlignmentLeft;
    [gestureButton addTarget:self action:@selector(openGestureLockViewAction) forControlEvents:UIControlEventTouchUpInside];
    
    UIButton* passwordButton = [UIButton buttonWithType:UIButtonTypeCustom];
    passwordButton.titleEdgeInsets = UIEdgeInsetsMake(0, 45, 0, 0);
    [passwordButton setTitle:@"修改密码" forState:UIControlStateNormal];
    [passwordButton setTitleColor:[UIColor whiteColor] forState:UIControlStateNormal];
    
    passwordButton.frame =CGRectMake(0, buttonOffset+(menuImage.size.height-buttonOffset)*2/3, menuImage.size.width, (menuImage.size.height-buttonOffset)/3);
    passwordButton.contentVerticalAlignment = UIControlContentVerticalAlignmentCenter;
    passwordButton.contentHorizontalAlignment = UIControlContentHorizontalAlignmentLeft;
    [passwordButton addTarget:self action:@selector(openPasswordViewAction) forControlEvents:UIControlEventTouchUpInside];
    
    
    [self.configView addSubview:calendarButton];
    [self.configView addSubview:gestureButton];
    [self.configView addSubview:passwordButton];
    [self.backgroundView addSubview:self.configView];
    UITapGestureRecognizer* singleFingerTap = [[[UITapGestureRecognizer alloc] initWithTarget:self action:@selector(hideConfig)] autorelease];
    
    [self.backgroundView addGestureRecognizer:singleFingerTap];
}

-(void)logoutAction{
    [[TTNavigator navigator] removeAllViewControllers];
    NSUserDefaults* defaults = [NSUserDefaults standardUserDefaults];
    [defaults removeObjectForKey:LAST_USER_NAME];
    [defaults removeObjectForKey:LAST_PASSWORD];
    [defaults removeObjectForKey:LAST_LOGIN_DATE];
    DrAppDelegate.user.date = nil;
    TTOpenURL(@"peacebird://login");
}
-(void)openGestureLockViewAction{
    [[TTNavigator navigator] removeAllViewControllers];
    [self hideConfig];
    TTOpenURL(@"peacebird://gestureLock/setting");
}

-(void)openPasswordViewAction{
    [[TTNavigator navigator] removeAllViewControllers];
    [self hideConfig];
    TTOpenURL(@"peacebird://password/change");
}

- (void)viewDidLoad
{
    [super viewDidLoad];
}






- (void)viewDidUnload
{
    [super viewDidUnload];
    // Release any retained subviews of the main view.
}


- (CGFloat)tableView:(UITableView *)tableView heightForRowAtIndexPath:(NSIndexPath *)indexPath{
    return self.rowHeight+0.5;
}
-(void)hideConfig{
    [self.configButton setImage:[UIImage imageNamed:@"downarrow.png"] forState:UIControlStateNormal];
    [UIView animateWithDuration:0.25f animations:^{
        self.configView.alpha = 0;
        [self.configView layoutIfNeeded];
        
    } completion:^(BOOL finished) {
        [self.backgroundView removeFromSuperview];
    }];
}
-(void)showConfig:(id)sender{
    [self.configButton setImage:[UIImage imageNamed:@"uparrow"] forState:UIControlStateNormal];

    [[UIApplication sharedApplication].keyWindow addSubview:self.backgroundView];
    self.configView.alpha = 0;
    self.backgroundView.backgroundColor = [UIColor colorWithRed:0 green:0 blue:0 alpha:0.3f];
    [UIView animateWithDuration:0.25f animations:^{
        self.configView.alpha = 1;
        [self.configView layoutIfNeeded];
    }];
}
-(void)openGestureSetting:(id)sender{
    [self hideConfig];
    TTOpenURL(@"peacebird://gestureLock/setting");
}
-(void)openCalendar:(id)sender{
    NSDate* date = DrAppDelegate.user.date;
    if(date==nil){
        date = [NSDate date];
    }
    PMCalendarController* pmCC = [[[PMCalendarController alloc] initWithDate:date] autorelease];
    pmCC.delegate = self;
    pmCC.allowsPeriodSelection = NO;
    pmCC.mondayFirstDayOfWeek = YES;
    
    [pmCC presentCalendarFromView:self.navigationItem.rightBarButtonItem.customView
         permittedArrowDirections:PMCalendarArrowDirectionRight
                         animated:YES];
    [self hideConfig];
}

- (void)calendarController:(PMCalendarController *)calendarController didChangePeriod:(PMPeriod *)newPeriod
{
    NSDate* date = newPeriod.startDate;
    DrAppDelegate.user.oldDate = DrAppDelegate.user.date;
    DrAppDelegate.user.date = date;
    [calendarController dismissCalendarAnimated:YES];
    [self reload];
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
- (void)dealloc
{
    TT_RELEASE_SAFELY(_configView);
    TT_RELEASE_SAFELY(_backgroundView);
    TT_RELEASE_SAFELY(_configButton);
    [super dealloc];
}
@end
