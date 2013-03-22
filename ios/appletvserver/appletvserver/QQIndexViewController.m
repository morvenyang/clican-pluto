//
//  QQIndexViewController.m
//  appletvserver
//
//  Created by zhang wei on 13-2-12.
//  Copyright (c) 2013年 __MyCompanyName__. All rights reserved.
//

#import "QQIndexViewController.h"
#import "QQIndexDataSource.h"

@implementation QQIndexViewController

@synthesize channelId = _channelId;

- (id)initWithNibName:(NSString *)nibNameOrNil bundle:(NSBundle *)nibBundleOrNil
{
    self = [super initWithNibName:nibNameOrNil bundle:nibBundleOrNil];
    if (self) {
        self.variableHeightRows = YES;
    }
    return self;
}

- (id) initWithChannelId:(QQChannel) channelId{
    self = [super init];
    if(self){
        self.channelId = channelId;
    }
    return self;
}

- (void)createModel {
    QQIndexDataSource* ds = [[QQIndexDataSource alloc] initWithQQChannel:self.channelId];
    self.dataSource = ds;
}

- (void)viewDidLoad
{
    [super viewDidLoad];
	// Do any additional setup after loading the view.
}

- (void)viewDidUnload
{
    [super viewDidUnload];
    // Release any retained subviews of the main view.
}

- (BOOL)shouldAutorotateToInterfaceOrientation:(UIInterfaceOrientation)interfaceOrientation
{
    return (interfaceOrientation == UIInterfaceOrientationPortrait);
}

- (void)dealloc
{
    [super dealloc];
}
@end
