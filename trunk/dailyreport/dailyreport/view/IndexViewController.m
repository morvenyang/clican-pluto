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
    return self;
}

- (void)createModel {
    IndexDataSource* ds = [[[IndexDataSource alloc] init] autorelease];
    self.dataSource = ds;
}

- (void)viewWillAppear:(BOOL)animated {
    if(DEVICE_VERSION>=7.0){
        self.navigationController.navigationBar.barTintColor =[StyleSheet colorFromHexString:@"#EAEEF2"];
    }else{
        self.navigationBarTintColor=[StyleSheet colorFromHexString:@"#EAEEF2"];
    }

    [super viewWillAppear:animated];
}
-(void)updateDate{
    UILabel* label = [[[UILabel alloc] initWithFrame:CGRectZero] autorelease];
    label.backgroundColor = [UIColor clearColor];
    label.font =[UIFont systemFontOfSize:18];
    label.textAlignment = NSTextAlignmentCenter;
    label.textColor=[StyleSheet colorFromHexString:@"#323232"];
    NSDateFormatter* dateFormatter = [[[NSDateFormatter alloc] init] autorelease];
    [dateFormatter setTimeStyle:NSDateFormatterFullStyle];
    [dateFormatter setDateFormat:@"MM月dd日 EEE"];
    if(DrAppDelegate.user.date!=nil){
        label.text = [dateFormatter stringFromDate:DrAppDelegate.user.date];
    }else{
        label.text = [dateFormatter stringFromDate:[[NSDate date] dateByAddingDays:-1]];
    }
    self.navigationItem.titleView = label;
    [label sizeToFit];
}

-(void) loadView{
    [super loadView];
    
    self.tableView.backgroundColor =[UIColor blackColor];
    self.tableView.delegate = self;
    if(DEVICE_VERSION>=7.0){
        self.tableView.separatorInset = UIEdgeInsetsZero;
    }
    self.tableView.separatorColor = [UIColor blackColor];
    CGRect frame = [[UIScreen mainScreen] bounds];
    self.backgroundView =[[UIView alloc] initWithFrame:frame];
    self.configView = [[UIView alloc] initWithFrame:CGRectMake(170, 60, 137, 102)];
    
    UIImageView* configBackgroundImage =[[[UIImageView alloc] initWithFrame:CGRectMake(0, 0, 137, 102)] autorelease];
    configBackgroundImage.contentMode = UIViewContentModeScaleToFill;
    configBackgroundImage.image =[UIImage imageNamed:@"menu.png"];
    [self.configView addSubview:configBackgroundImage];
    
    self.configButton = [UIButton buttonWithType:UIButtonTypeCustom];
    [self.configButton setImage:[UIImage imageNamed:@"downarrow.png"] forState:UIControlStateNormal];

    self.configButton.frame =CGRectMake(0, 0, 23, 23);
    [self.configButton addTarget:self action:@selector(showConfig:) forControlEvents:UIControlEventTouchUpInside];
    
    
    UIBarButtonItem* configItem = [[UIBarButtonItem alloc] initWithCustomView:self.configButton];
    [self.navigationItem setRightBarButtonItem:configItem animated:YES];
    
    UIButton* titleButton = [UIButton buttonWithType:UIButtonTypeCustom];
    [titleButton setTitle:@"经营日报" forState:UIControlStateNormal];
    [titleButton setTitleColor:[StyleSheet colorFromHexString:@"#323232"] forState:UIControlStateNormal];
    titleButton.frame =CGRectMake(0, 0, 55, 20);
    [titleButton sizeToFit];
    UIBarButtonItem* titleItem = [[UIBarButtonItem alloc] initWithCustomView:titleButton];
    
    [self.navigationItem setLeftBarButtonItem:titleItem];

    UIButton* logoutButton = [UIButton buttonWithType:UIButtonTypeCustom];
    logoutButton.titleEdgeInsets = UIEdgeInsetsMake(0, 45, 0, 0);
    [logoutButton setTitle:@"Exit" forState:UIControlStateNormal];
    [logoutButton setTitleColor:[UIColor whiteColor] forState:UIControlStateNormal];
    logoutButton.frame =CGRectMake(0, 8, 137, 48);
    logoutButton.contentVerticalAlignment = UIControlContentVerticalAlignmentCenter;
    logoutButton.contentHorizontalAlignment = UIControlContentHorizontalAlignmentLeft;
    [logoutButton addTarget:self action:@selector(logoutAction) forControlEvents:UIControlEventTouchUpInside];


    
    UIButton* calendarButton = [UIButton buttonWithType:UIButtonTypeCustom];
    calendarButton.titleEdgeInsets = UIEdgeInsetsMake(0, 45, 0, 0);
    [calendarButton setTitle:@"选择日期" forState:UIControlStateNormal];
    [calendarButton setTitleColor:[UIColor whiteColor] forState:UIControlStateNormal];

    calendarButton.frame =CGRectMake(0, 55, 137, 47);
    calendarButton.contentVerticalAlignment = UIControlContentVerticalAlignmentCenter;
    calendarButton.contentHorizontalAlignment = UIControlContentHorizontalAlignmentLeft;
    [calendarButton addTarget:self action:@selector(openCalendar:) forControlEvents:UIControlEventTouchUpInside];

    
    
    [self.configView addSubview:calendarButton];
    [self.configView addSubview:logoutButton];
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
    return 109;
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
    [self.configButton setImage:[UIImage imageNamed:@"uparrow.png"] forState:UIControlStateNormal];

    [[UIApplication sharedApplication].keyWindow addSubview:self.backgroundView];
    self.configView.alpha = 0;
    self.backgroundView.backgroundColor = [UIColor colorWithRed:0 green:0 blue:0 alpha:0.3f];
    [UIView animateWithDuration:0.25f animations:^{
        self.configView.alpha = 1;
        [self.configView layoutIfNeeded];
    }];
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

- (void)dealloc
{
    TT_RELEASE_SAFELY(_configView);
    TT_RELEASE_SAFELY(_backgroundView);
    TT_RELEASE_SAFELY(_configButton);
    [super dealloc];
}
@end
