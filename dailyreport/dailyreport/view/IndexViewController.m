//
//  IndexViewController.m
//  dailyreport
//
//  Created by zhang wei on 14-5-17.
//  Copyright (c) 2014年 Peace Bird. All rights reserved.
//

#import "IndexViewController.h"
#import "IndexDataSource.h"
@interface IndexViewController ()

@end

@implementation IndexViewController

- (id)initWithNibName:(NSString *)nibNameOrNil bundle:(NSBundle *)nibBundleOrNil
{
    self = [super initWithNibName:nibNameOrNil bundle:nibBundleOrNil];
    if (self) {
        self.title = @"PEACEBIRD 经营日报";
    }
    return self;
}

- (void)createModel {
    IndexDataSource* ds = [[[IndexDataSource alloc] init] autorelease];
    self.dataSource = ds;
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

@end
