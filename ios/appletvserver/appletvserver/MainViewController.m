//
//  MainViewController.m
//  appletvserver
//
//  Created by zhang wei on 13-2-12.
//  Copyright (c) 2013年 __MyCompanyName__. All rights reserved.
//

#import "MainViewController.h"
#import "AppDelegate.h"
#import "IndexMenu.h"
#import "Constants.h"
#import "RootViewController.h"
#import "ConfigViewController.h"
@implementation MainViewController


- (id)initWithNibName:(NSString *)nibNameOrNil bundle:(NSBundle *)nibBundleOrNil
{
    self = [super initWithNibName:nibNameOrNil bundle:nibBundleOrNil];
    if (self) {
        self.title = @"首页";
        self.tabBarItem = [[[UITabBarItem alloc] initWithTitle:@"首页" image:nil tag:0] autorelease];
        [[AppDele scriptRefreshDelegateArray] addObject:self];
    }
    return self;
}

- (void)createModel {
    
    NSMutableArray* items = [NSMutableArray array];
    TTListDataSource* ds = [[[TTListDataSource alloc] initWithItems:items] autorelease];
    self.dataSource = ds;
    self.tableView.delegate =self;
}

- (void)tableView:(UITableView *)tableView didSelectRowAtIndexPath:(NSIndexPath *)indexPath{
    TTListDataSource* ds = (TTListDataSource*)tableView.dataSource;

    TTTableTextItem* item = [ds.items objectAtIndex:indexPath.row];
    NSString* script = item.URL;
    [[AppDele jsEngine] runJS:script];
}




- (void)viewDidLoad
{
    [super viewDidLoad];
    [self showIndex];
}

-(void)showNetworkWarning{
    TTAlert(@"加载脚本失败,请检查网络是否正常连接");
}
-(void)showIndex{
    NSMutableArray* items = [NSMutableArray array];
    NSArray* array = [AppDele.webContentSync loadLocalXml];
    if(array.count==0){
        [self showNetworkWarning];
    }else{
        for(int i=0;i<array.count;i++){
            IndexMenu* im = [array objectAtIndex:i];
            TTTableTextItem* item = [TTTableTextItem itemWithText:im.title URL:im.onPlay];
            [items addObject:item];
        }
        TTListDataSource* ds = [[[TTListDataSource alloc] initWithItems:items] autorelease];
        self.dataSource = ds;
        self.tableView.delegate =self;
        _flags.isUpdatingView=NO;
        [self updateView];
    }
}

-(void) refreshScript{
    NSMutableArray* items = [NSMutableArray array];
    NSArray* array = [AppDele.webContentSync loadLocalXml];
    if(array.count!=0){
        for(int i=0;i<array.count;i++){
            IndexMenu* im = [array objectAtIndex:i];
            TTTableTextItem* item = [TTTableTextItem itemWithText:im.title URL:im.onPlay];
            [items addObject:item];
        }
        TTListDataSource* ds = [[[TTListDataSource alloc] initWithItems:items] autorelease];
        self.dataSource = ds;
        self.tableView.delegate =self;
    }
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
