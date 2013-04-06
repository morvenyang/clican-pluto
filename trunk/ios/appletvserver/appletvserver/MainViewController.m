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
    TTTableTextItem* item1 = [TTTableTextItem itemWithText:@"腾讯视频" URL:@"qqClient.loadChannelPage();"];
    [items addObject:item1];
    TTTableTextItem* item2 = [TTTableTextItem itemWithText:@"优酷视频" URL:@"youkuClient.loadChannelPage();"];
    [items addObject:item2];
    TTTableTextItem* item3 = [TTTableTextItem itemWithText:@"土豆视频" URL:@"tudouClient.loadChannelPage();"];
    [items addObject:item3];
    TTTableTextItem* item4 = [TTTableTextItem itemWithText:@"人人影视" URL:@"yyetsClient.loadChannelPage();"];
    [items addObject:item4];
    TTTableTextItem* item5 = [TTTableTextItem itemWithText:@"龙部落影视" URL:@"lblClient.loadChannelPage();"];
    [items addObject:item5];
    TTTableTextItem* item6 = [TTTableTextItem itemWithText:@"收藏" URL:@"appletv.loadFavoritePage();"];
    [items addObject:item6];
    
    NSString* url =@"http://vod30.t19.lixian.vip.xunlei.com:443/download?fid=emlPgw9jX5lBcmGnVJ4RgeXO+Hxq6VFcAAAAANuBvkGCNBSzd4lE4fk1okftXEb7&mid=666&threshold=150&tid=EDCDE1299DC6E43F04EFF63FACBBE88D&srcid=4&verno=1&g=DB81BE41823414B3778944E1F935A247ED5C46FB&scn=t9&i=817119D8471F0BCC3DCFDF0712D5CD4B&t=4&ui=5663595&ti=161029492610&s=1548872042&m=0&n=011559817177616C6B085F8371646561644F42D46C6531362E5603D42F2E686474171F9C6D36342D320955CA326B760000&ff=0&co=909A5217E21D287776E49561158F5D2B&cm=1&ts=1365205639";
    TTTableTextItem* item7 = [TTTableTextItem itemWithText:@"播放" URL:[NSString stringWithFormat:@"appletv.playMkv('%@');",url]];
    [items addObject:item7];
    TTListDataSource* ds = [[TTListDataSource alloc] initWithItems:items];
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
    }
    [pool release];
}
-(void)showNetworkWarning{
    TTAlert(@"加载脚本失败,请检查网络是否正常连接");
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
