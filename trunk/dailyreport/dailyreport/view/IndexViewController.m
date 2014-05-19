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
    if(DEVICE_VERSION>=7.0){
        UIColor *navigationTextColor = [StyleSheet colorFromHexString:@"#323232"];
        DrAppDelegate.window.tintColor = navigationTextColor;
        [[UINavigationBar appearance] setTitleTextAttributes:@{
                                                               NSForegroundColorAttributeName : navigationTextColor
                                                               }];
    }
    
    return self;
}

- (void)createModel {
    IndexDataSource* ds = [[[IndexDataSource alloc] init] autorelease];
    self.dataSource = ds;
}




-(void) loadView{
    [super loadView];
    self.tableView.backgroundColor =[UIColor blackColor];
    self.tableView.delegate = self;
    if(DEVICE_VERSION>=7.0){
        self.tableView.separatorInset = UIEdgeInsetsZero;
    }
    self.tableView.separatorColor = [UIColor blackColor];
    if(DEVICE_VERSION>=7.0){
        self.navigationController.navigationBar.barTintColor=[StyleSheet colorFromHexString:@"#EAEEF2"];
    }else{
        self.navigationBarTintColor=[StyleSheet colorFromHexString:@"#EAEEF2"];
        UILabel* label = [[[UILabel alloc] initWithFrame:CGRectZero] autorelease];
        label.backgroundColor = [UIColor clearColor];
        label.font =[UIFont systemFontOfSize:18];
        label.textAlignment = NSTextAlignmentCenter;
        label.textColor=[StyleSheet colorFromHexString:@"#323232"];
        label.text = @"PEACEBIRD 经营日报";
        self.navigationItem.titleView = label;
        [label sizeToFit];
    }
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
