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
@implementation MainViewController

@synthesize progressHUD = _progressHUD;
- (id)initWithNibName:(NSString *)nibNameOrNil bundle:(NSBundle *)nibBundleOrNil
{
    self = [super initWithNibName:nibNameOrNil bundle:nibBundleOrNil];
    if (self) {
        self.title = @"首页";
        self.tabBarItem = [[[UITabBarItem alloc] initWithTitle:@"首页" image:nil tag:0] autorelease];
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

- (void)loadView
{
    [super loadView];
    self.progressHUD = [[[MBProgressHUD alloc] initWithView:self.view] autorelease];
    self.progressHUD.delegate = self;
    self.progressHUD.labelText = @"加载脚本中...";
    [self.view addSubview:self.progressHUD];
    [self.view bringSubviewToFront:self.progressHUD];
    [self.progressHUD show:YES];
    
}


- (void)viewDidLoad
{
    [super viewDidLoad];

    [NSThread detachNewThreadSelector:@selector(afterViewLoaded:) toTarget:self withObject:nil];
}

- (void) afterViewLoaded:(id)object{
    NSAutoreleasePool *pool = [[NSAutoreleasePool alloc] init];
    BOOL result = [[AppDele webContentSync] syncWebContent:self.progressHUD force:NO];
    
    [[AppDele jsEngine] reloadJS];
    
    [AppDele initProcess];
    [self.progressHUD hide:YES];
    if(!result){
        [self performSelectorOnMainThread:@selector(showNetworkWarning) withObject:nil waitUntilDone:NO];
    }else{
         [self performSelectorOnMainThread:@selector(showIndex) withObject:nil waitUntilDone:NO];
    }
    [pool release];
}
-(void)showNetworkWarning{
    TTAlert(@"加载脚本失败,请检查网络是否正常连接");
}
-(void)showIndex{
    NSMutableArray* items = [NSMutableArray array];
    NSArray* array = [AppDele.webContentSync loadLocalXml];
    for(int i=0;i<array.count;i++){
        IndexMenu* im = [array objectAtIndex:i];
        TTTableTextItem* item = [TTTableTextItem itemWithText:im.title URL:im.onSelect];
        [items addObject:item];
    }
    TTListDataSource* ds = [[[TTListDataSource alloc] initWithItems:items] autorelease];
    self.dataSource = ds;
    self.tableView.delegate =self;
    _flags.isUpdatingView=NO;
    [self updateView];
}

- (void)viewDidUnload
{
    [super viewDidUnload];
    // Release any retained subviews of the main view.
}

#pragma mark -
#pragma mark MBProgressHUDDelegate methods

- (void)hudWasHidden:(MBProgressHUD *)hud {
    // Remove HUD from screen when the HUD was hidded
    [_progressHUD removeFromSuperview];
    [_progressHUD release];
    _progressHUD = nil;
}

- (BOOL)shouldAutorotateToInterfaceOrientation:(UIInterfaceOrientation)interfaceOrientation
{
    return (interfaceOrientation == UIInterfaceOrientationPortrait);
}


@end
