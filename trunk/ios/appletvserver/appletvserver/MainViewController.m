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
@synthesize assets = _assets;
@synthesize library = _library;
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
    self.assets = [[[NSMutableArray alloc] init] autorelease];
    self.library = [[[ALAssetsLibrary alloc] init] autorelease];

    [self.library enumerateGroupsWithTypes:ALAssetsGroupSavedPhotos
                           usingBlock:^(ALAssetsGroup *group, BOOL *stop){
                               if(group != NULL) {
                                   [group enumerateAssetsUsingBlock:^(ALAsset *result, NSUInteger index, BOOL *stop){
                                       if(result != NULL) {
                                           NSLog(@"See Asset: %@", result);
                                           [self.assets addObject:result];
                                       }
                                   }];
                               }
                           }
           					 failureBlock: ^(NSError *error) {
               						 NSLog(@"Failure");
               					 }];
    NSLog(@"asset number:%i",[self.assets count]);
    self.progressHUD = [[[MBProgressHUD alloc] initWithView:self.view] autorelease];
    self.progressHUD.delegate = self;
    self.progressHUD.labelText = @"加载脚本中...";
    [self.view addSubview:self.progressHUD];
    [self.view bringSubviewToFront:self.progressHUD];
    [self.progressHUD show:YES];
    
}

-(void) initWebContent{
    [[AppDele webContentSync] syncWebContent:self.progressHUD];
}

-(void) initJSEngine{
    [[AppDele jsEngine] reloadJS];
}

- (void)viewDidLoad
{
    [super viewDidLoad];

    [NSThread detachNewThreadSelector:@selector(afterViewLoaded:) toTarget:self withObject:nil];
}

- (void) afterViewLoaded:(id)object{
    NSAutoreleasePool *pool = [[NSAutoreleasePool alloc] init];
    [self initWebContent];
    [self initJSEngine];
    [self.progressHUD hide:YES];
    [pool release];
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
