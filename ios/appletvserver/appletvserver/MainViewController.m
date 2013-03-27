//
//  MainViewController.m
//  appletvserver
//
//  Created by zhang wei on 13-2-12.
//  Copyright (c) 2013年 __MyCompanyName__. All rights reserved.
//

#import "MainViewController.h"
#import "AppDelegate.h"

@implementation MainViewController

- (id)initWithNibName:(NSString *)nibNameOrNil bundle:(NSBundle *)nibBundleOrNil
{
    self = [super initWithNibName:nibNameOrNil bundle:nibBundleOrNil];
    if (self) {
        self.title = @"视频";
        self.tabBarItem = [[[UITabBarItem alloc] initWithTitle:@"视频" image:nil tag:0] autorelease];
    }
    return self;
}

- (void)createModel {
    NSMutableArray* items = [NSMutableArray array];
    TTTableTextItem* item1 = [TTTableTextItem itemWithText:@"QQ" URL:@"qqClient.loadChannelPage();"];
    [items addObject:item1];
    TTTableTextItem* item2 = [TTTableTextItem itemWithText:@"Youku" URL:@"youkuClient.loadChannelPage();"];
    [items addObject:item2];
    TTTableTextItem* item3 = [TTTableTextItem itemWithText:@"Tudou" URL:@"tudouClient.loadChannelPage();"];
    [items addObject:item3];
    TTTableTextItem* item4 = [TTTableTextItem itemWithText:@"Yyets" URL:@"yyetsClient.loadChannelPage();"];
    [items addObject:item4];
    TTListDataSource* ds = [[TTListDataSource alloc] initWithItems:items];
    self.dataSource = ds;
    self.tableView.delegate =self;
}

- (void)tableView:(UITableView *)tableView didSelectRowAtIndexPath:(NSIndexPath *)indexPath{
    TTListDataSource* ds = (TTListDataSource*)tableView.dataSource;
    TTTableTextItem* item = [ds.items objectAtIndex:indexPath.item];
    NSString* script = item.URL;
    [[AppDele jsEngine] runJS:script];
}

- (void)loadView
{
    [super loadView];
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


@end
