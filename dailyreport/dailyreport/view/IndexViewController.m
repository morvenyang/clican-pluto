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

- (id)initWithNibName:(NSString *)nibNameOrNil bundle:(NSBundle *)nibBundleOrNil
{
    self = [super initWithNibName:nibNameOrNil bundle:nibBundleOrNil];
    if (self) {
        self.title = @"PEACEBIRD 经营日报";
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
    
    UILabel* label = [[[UILabel alloc] initWithFrame:CGRectZero] autorelease];
    label.backgroundColor = [UIColor clearColor];
    label.font =[UIFont systemFontOfSize:18];
    label.textAlignment = NSTextAlignmentCenter;
    label.textColor=[StyleSheet colorFromHexString:@"#323232"];
    label.text = @"PEACEBIRD 经营日报";
    self.navigationItem.titleView = label;
    [label sizeToFit];
    
    [super viewWillAppear:animated];
}

-(void) loadView{
    [super loadView];
    self.tableView.backgroundColor =[UIColor blackColor];
    self.tableView.delegate = self;
    if(DEVICE_VERSION>=7.0){
        self.tableView.separatorInset = UIEdgeInsetsZero;
    }
    self.tableView.separatorColor = [UIColor blackColor];
    
    UIButton* logoutButton = [UIButton buttonWithType:UIButtonTypeCustom];
    [logoutButton setTitle:@"Exit" forState:UIControlStateNormal];
    [logoutButton setTitleColor:[StyleSheet colorFromHexString:@"#323232"] forState:UIControlStateNormal];
    logoutButton.frame =CGRectMake(0, 0, 25, 20);
    [logoutButton addTarget:self action:@selector(logoutAction) forControlEvents:UIControlEventTouchUpInside];

    [logoutButton sizeToFit];
    
    UIBarButtonItem* logoutItem = [[UIBarButtonItem alloc] initWithCustomView:logoutButton];
    [self.navigationItem setRightBarButtonItem:logoutItem animated:YES];
    
}

-(void)logoutAction{
    [[TTNavigator navigator] removeAllViewControllers];
    NSUserDefaults* defaults = [NSUserDefaults standardUserDefaults];
    [defaults removeObjectForKey:LAST_USER_NAME];
    [defaults removeObjectForKey:LAST_PASSWORD];
    [defaults removeObjectForKey:LAST_LOGIN_DATE];
    
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

- (void)dealloc
{
    [super dealloc];
}
@end
